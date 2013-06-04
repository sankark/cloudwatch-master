package com.gto.aws.service;

import com.basho.riak.client.IRiakClient;
import com.basho.riak.client.RiakException;


public final class RiakFactory {
	private static IRiakClient riakClient= getClient();
	private static IRiakClient getClient(){
		try {
			return com.basho.riak.client.RiakFactory.pbcClient("i-88b704f6", 8081);
		} catch (RiakException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	public static IRiakClient getRiakClient(){
		return riakClient;
	}

}
