/***
 * 	Copyright (c) 2011-2012 WareNinja.com
 *  http://www.WareNinja.com - https://github.com/WareNinja
 *  	
 *  Author: yg@wareninja.com / twitter: @WareNinja
 */

package com.wareninja.android.opensource.mongolab_sdk.common;


import java.io.Serializable;
import android.os.Bundle;
import com.google.gson.annotations.SerializedName;


/*
 * meta part of json response
 */
public class ResponseMeta implements Serializable {

	private static final long serialVersionUID = 1L;
	
	public ResponseMeta() {
		this.code = 200;
		this.errorType = "";//"general";
		this.errorDetail = "";
	}
	public ResponseMeta(String errorMsg) {
		//new ResponseMeta(errorMsg, false);
		this.code = 400;
		this.errorType = "";//"general";
		this.errorDetail = errorMsg;
	}
	public ResponseMeta(String errorMsg, boolean isNoContent) {
		this.code = isNoContent?204:400;
		this.errorType = "";//"general";
		this.errorDetail = errorMsg;
	}
	
	@SerializedName("code")
	public Integer code;
	
	@SerializedName("errorType")
	public String errorType;
	
	@SerializedName("errorDetail")
	public String errorDetail;

	@Override
	public String toString() {
		return "ResponseMeta [code=" + code + ", errorType=" + errorType
				+ ", errorDetail=" + errorDetail
				+ "]";
	}
}

