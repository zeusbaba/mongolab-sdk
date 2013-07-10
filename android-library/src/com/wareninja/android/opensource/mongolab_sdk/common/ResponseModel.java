/***
 * 	Copyright (c) 2011-2012 WareNinja.com
 *  http://www.WareNinja.com - https://github.com/WareNinja
 *  	
 *  Author: yg@wareninja.com / twitter: @WareNinja
 */

package com.wareninja.android.opensource.mongolab_sdk.common;

import java.io.Serializable;

import com.google.gson.annotations.SerializedName;


public class ResponseModel implements Serializable {
    
	private static final long serialVersionUID = 1L;
	
	@SerializedName("meta")
	public ResponseMeta meta;
	
	@SerializedName("data")
	public Object data;

	@SerializedName("notification")
    public Object notification;// (optional) used for notifications
	
	@Override
	public String toString() {
		return "ResponseModel [" +
				"meta=" + meta 
				+ ", notification=" + notification
				+ ", data=" + data
				+ "]";
	}
}
