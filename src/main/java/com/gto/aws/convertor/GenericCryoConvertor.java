package com.gto.aws.convertor;
import static com.basho.riak.client.convert.KeyUtil.getKey;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.lang.reflect.ParameterizedType;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

import com.amazonaws.services.cloudwatch.model.StandardUnit;
import com.basho.riak.client.IRiakObject;
import com.basho.riak.client.RiakLink;
import com.basho.riak.client.builders.RiakObjectBuilder;
import com.basho.riak.client.cap.VClock;
import com.basho.riak.client.convert.ConversionException;
import com.basho.riak.client.convert.NoKeySpecifedException;
import com.basho.riak.client.convert.RiakIndexConverter;
import com.basho.riak.client.convert.RiakLinksConverter;
import com.basho.riak.client.convert.UsermetaConverter;
import com.basho.riak.client.http.util.Constants;
import com.basho.riak.client.query.indexes.RiakIndexes;
import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.InputChunked;
import com.esotericsoftware.kryo.io.Output;
import com.esotericsoftware.kryo.serializers.CollectionSerializer;
import com.esotericsoftware.kryo.serializers.DefaultSerializers.DateSerializer;
import com.esotericsoftware.kryo.serializers.FieldSerializer;
import com.gto.aws.model.Data;
import com.gto.aws.model.DataPoint;
import com.gto.aws.model.Ec2CpuUtilizationInstance;
import com.gto.aws.model.Ec2CpuUtilizationJob;

public abstract class GenericCryoConvertor<T> implements IGenericCryoConvertor<T>
{

	protected String bucket;
    protected Class<T> clazz;
    protected final UsermetaConverter<T> usermetaConverter;
    protected final RiakIndexConverter<T> riakIndexConverter;
    protected final RiakLinksConverter<T> riakLinksConverter;
    
    public GenericCryoConvertor(String bucket)
    {
        this.bucket = bucket;
        this.usermetaConverter = new UsermetaConverter<T>();
        this.riakIndexConverter = new RiakIndexConverter<T>();
        this.riakLinksConverter = new RiakLinksConverter<T>();
        clazz = ((Class<T>)((ParameterizedType)getClass().getGenericSuperclass()).getActualTypeArguments()[0]);
    }
    
    
    /* (non-Javadoc)
	 * @see com.gto.aws.service.IGenericCryoConvertor#fromDomain(T, com.basho.riak.client.cap.VClock)
	 */
    @Override
	public IRiakObject fromDomain(T domainObject, VClock vclock) throws ConversionException
    {
        //throw new UnsupportedOperationException("Not supported yet.");
        System.out.println("from DOmain");
        String key = getKey(domainObject);
        
        if (key == null)
        {
            throw new NoKeySpecifedException(domainObject);
        }
        
   
        Kryo kryo = new Kryo();
        
        
        FieldSerializer dataPointSerializer = new FieldSerializer(kryo, com.gto.aws.model.DataPoint.class);
        FieldSerializer dataSerializer = new FieldSerializer(kryo, com.gto.aws.model.Data.class);
        CollectionSerializer listSerializer2 = new CollectionSerializer();
        listSerializer2.setElementClass(DataPoint.class,dataPointSerializer);
        listSerializer2.setElementsCanBeNull(false);
        dataSerializer.getField("data").setClass(ArrayList.class, listSerializer2);
        kryo.register(com.gto.aws.model.Data.class, dataSerializer,33);
        
        FieldSerializer instanceSerializer = new FieldSerializer(kryo, Ec2CpuUtilizationInstance.class);
        CollectionSerializer listSerializer = new CollectionSerializer();
        listSerializer.setElementClass(Data.class,dataSerializer);
        listSerializer.setElementsCanBeNull(false);
        instanceSerializer.getField("hourlyData").setClass(ArrayList.class, listSerializer);
        kryo.register(com.gto.aws.model.Ec2CpuUtilizationInstance.class, instanceSerializer,36);
        
        
 
        
     
        kryo.register(Ec2CpuUtilizationJob.class,30);
 
        kryo.register(StandardUnit.class,32);
       // kryo.register(com.gto.aws.model.Data.class,33);

       kryo.register(com.gto.aws.model.JobRequestInstance.class,35);
       // kryo.register(com.gto.aws.model.Ec2CpuUtilizationInstance.class,36);
        kryo.register(com.gto.aws.model.Ec2CpuUtilizationJob.class,37);
        kryo.register(Date.class,new DateSerializer(),40);
        
  /*      byte[] value = toByteArray(domainObject);
        kryo.writeObject(new Output(value), domainObject);*/
        Output output = new Output(new ByteArrayOutputStream(),4*4096);
        kryo.writeObjectOrNull(output, domainObject,clazz);
        Map<String, String> usermetaData = usermetaConverter.getUsermetaData(domainObject);
        RiakIndexes indexes = riakIndexConverter.getIndexes(domainObject);
        Collection<RiakLink> links = riakLinksConverter.getLinks(domainObject);
        
        return RiakObjectBuilder.newBuilder(bucket, key)
            .withValue(output.getBuffer())
            .withVClock(vclock)
            .withUsermeta(usermetaData)
            .withIndexes(indexes)
            .withLinks(links)
            .withContentType(Constants.CTYPE_OCTET_STREAM)
            .build();
        
        
    }

    /* (non-Javadoc)
	 * @see com.gto.aws.service.IGenericCryoConvertor#toDomain(com.basho.riak.client.IRiakObject)
	 */
    @Override
	public T toDomain(IRiakObject riakObject) throws ConversionException
    {
        
        if (riakObject == null)
            return null;
        System.out.println(clazz);
        Kryo kryo = new Kryo();
        FieldSerializer dataPointSerializer = new FieldSerializer(kryo, com.gto.aws.model.DataPoint.class);
        FieldSerializer dataSerializer = new FieldSerializer(kryo, com.gto.aws.model.Data.class);
        CollectionSerializer listSerializer2 = new CollectionSerializer();
        listSerializer2.setElementClass(DataPoint.class,dataPointSerializer);
        listSerializer2.setElementsCanBeNull(false);
        dataSerializer.getField("data").setClass(ArrayList.class, listSerializer2);
        kryo.register(com.gto.aws.model.Data.class, dataSerializer,33);
        
        FieldSerializer instanceSerializer = new FieldSerializer(kryo, Ec2CpuUtilizationInstance.class);
        CollectionSerializer listSerializer = new CollectionSerializer();
        listSerializer.setElementClass(Data.class,dataSerializer);
        listSerializer.setElementsCanBeNull(false);
        instanceSerializer.getField("hourlyData").setClass(ArrayList.class, listSerializer);
        kryo.register(com.gto.aws.model.Ec2CpuUtilizationInstance.class, instanceSerializer,36);
        kryo.register(Date.class,new DateSerializer(),40);
        
 
        
   
        kryo.register(Ec2CpuUtilizationJob.class,30);
        kryo.register(StandardUnit.class,32);
       // kryo.register(com.gto.aws.model.Data.class,33);
       
       kryo.register(com.gto.aws.model.JobRequestInstance.class,35);
       // kryo.register(com.gto.aws.model.Ec2CpuUtilizationInstance.class,36);
        kryo.register(com.gto.aws.model.Ec2CpuUtilizationJob.class,37);
 
   Input input =new Input(new ByteArrayInputStream(riakObject.getValue()),4*4096);
       


        T domainObject = kryo.readObject(input, clazz);

         usermetaConverter.populateUsermeta(riakObject.getMeta(), domainObject);
         riakIndexConverter.populateIndexes(new RiakIndexes(riakObject.allBinIndexes(), riakObject.allIntIndexesV2()),
                                               domainObject);
         riakLinksConverter.populateLinks(riakObject.getLinks(), domainObject);
        System.out.println(riakObject.allBinIndexes().size());
        
        return domainObject;
        
    }
    
    public byte[] toByteArray(Object object){
    	ByteArrayOutputStream bos = new ByteArrayOutputStream();
    	ObjectOutput out = null;
    	byte[] result = null;
    	try {
			try {
			  out = new ObjectOutputStream(bos);   
			  out.writeObject(object);
			  result = bos.toByteArray();
			  
			} finally {
			  out.close();
			  bos.close();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result;
    }
}