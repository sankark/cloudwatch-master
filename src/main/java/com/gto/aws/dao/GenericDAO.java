package com.gto.aws.dao;

import org.springframework.beans.factory.annotation.Autowired;

import com.basho.riak.client.IRiakClient;
import com.basho.riak.client.RiakException;
import com.basho.riak.client.RiakRetryFailedException;
import com.basho.riak.client.bucket.Bucket;
import com.basho.riak.client.cap.UnresolvedConflictException;
import com.basho.riak.client.convert.ConversionException;
import com.gto.aws.service.RiakFactory;

import java.lang.reflect.ParameterizedType;
import java.util.List;

public abstract class GenericDAO<T> implements IGenericDAO<T>{
protected Bucket bucket; 
IRiakClient riakClient;
protected Class<T> clazz;
public GenericDAO(String Bucket){
	try {
		riakClient = RiakFactory.getRiakClient();
		System.out.println(riakClient);
		this.bucket = riakClient.fetchBucket(Bucket).execute();
		clazz = ((Class<T>)((ParameterizedType)getClass().getGenericSuperclass()).getActualTypeArguments()[0]);
	} catch (RiakRetryFailedException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
}

/* (non-Javadoc)
 * @see com.gto.aws.service.IGenericDAO#get(java.lang.String)
 */
@Override
public T get(String id){
	T myObject=null;
	try {
		myObject = bucket.fetch(id, clazz).execute();
	} catch (UnresolvedConflictException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	} catch (RiakRetryFailedException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	} catch (ConversionException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}

	return myObject;
}

/* (non-Javadoc)
 * @see com.gto.aws.service.IGenericDAO#get(T)
 */
@Override
public T get(T id){
	T myObject=null;
	try {
		myObject = bucket.fetch(id).execute();
	} catch (UnresolvedConflictException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	} catch (RiakRetryFailedException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	} catch (ConversionException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}

	return myObject;
}

/* (non-Javadoc)
 * @see com.gto.aws.service.IGenericDAO#put(T)
 */
@Override
public T put(T object){
	T myObject=null;
	try {
		
		myObject = bucket.store(object).execute();
	} catch (RiakRetryFailedException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	} catch (UnresolvedConflictException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	} catch (ConversionException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	return myObject;
}

/* (non-Javadoc)
 * @see com.gto.aws.service.IGenericDAO#delete(java.lang.String)
 */
@Override
public void delete(String id){
	
	try {
			bucket.delete(id).execute();
		} catch (RiakException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	

}

/* (non-Javadoc)
 * @see com.gto.aws.service.IGenericDAO#delete(T)
 */
@Override
public void delete(T id){
	
	try {
			bucket.delete(id).execute();
		} catch (RiakException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	

}

/* (non-Javadoc)
 * @see com.gto.aws.service.IGenericDAO#put(java.lang.String, T)
 */
@Override
public T put(String id,T object){
	T myObject=null;
	try {
		
		myObject = bucket.store(id,object).execute();
	} catch (RiakRetryFailedException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	} catch (UnresolvedConflictException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	} catch (ConversionException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	return myObject;
}

@Override
public Iterable<String> listKeys(){
	Iterable<String> myObject=null;

		
		try {
			myObject = bucket.keys();
		} catch (RiakException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	
	return myObject;
}
@Override
public void clean(){
	Iterable<String> iter = listKeys();
	for (String string : iter) {
		this.delete(string);
	}
}


}
