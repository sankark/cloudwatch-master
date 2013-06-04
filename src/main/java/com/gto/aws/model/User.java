package com.gto.aws.model;

import com.basho.riak.client.convert.RiakKey;

public class User {
	@RiakKey
String  loginID;
public String getLoginID() {
	return loginID;
}
public void setLoginID(String loginID) {
	this.loginID = loginID;
}
public String getPwd() {
	return pwd;
}
public void setPwd(String pwd) {
	this.pwd = pwd;
}
String pwd;
}
