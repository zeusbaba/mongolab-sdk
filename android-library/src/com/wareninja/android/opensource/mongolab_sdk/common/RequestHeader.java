/***
 * 	Copyright (c) 2011-2012 WareNinja.com
 *  http://www.WareNinja.com - https://github.com/WareNinja
 *  	
 *  Author: yg@wareninja.com / twitter: @WareNinja
 */

package com.wareninja.android.opensource.mongolab_sdk.common;


/**
 * An interface used to describe an HTTP Request Header.  Implementations may
 * derive/build headers in any way they see fit (static, dynamic, contact another host, etc).
 * 
 */
public interface RequestHeader {
	
	public String getKey();
	
	public String getValue();
}
