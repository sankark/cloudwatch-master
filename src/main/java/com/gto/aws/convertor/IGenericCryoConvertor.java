package com.gto.aws.convertor;

import com.basho.riak.client.IRiakObject;
import com.basho.riak.client.cap.VClock;
import com.basho.riak.client.convert.ConversionException;
import com.basho.riak.client.convert.Converter;

public interface IGenericCryoConvertor<T> extends Converter<T> {

	public abstract IRiakObject fromDomain(T domainObject, VClock vclock)
			throws ConversionException;

	public abstract T toDomain(IRiakObject riakObject)
			throws ConversionException;

}