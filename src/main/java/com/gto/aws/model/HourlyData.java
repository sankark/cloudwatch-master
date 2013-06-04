package com.gto.aws.model;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;

import com.amazonaws.services.cloudwatch.model.Datapoint;
import com.amazonaws.services.cloudwatch.model.StandardUnit;
import com.basho.riak.client.RiakLink;
import com.basho.riak.client.convert.RiakKey;
import com.basho.riak.client.convert.RiakLinks;

public class HourlyData extends Datapoint implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Override
    public void setUnit(String unit)
    {
        super.setUnit(unit);
    }

}
