/***
 * 	Copyright (c) 2011-2012 WareNinja.com
 *  http://www.WareNinja.com - https://github.com/WareNinja
 *  	
 *  Author: yg@wareninja.com / twitter: @WareNinja
 */

package com.wareninja.android.opensource.mongolab_sdk.common;


public interface RequestParameter {

	/**
	 * @return the key
	 */
	public abstract String getKey();

	/**
	 * @param key the key to set
	 */
	public abstract void setKey(String key);

	/**
	 * @return the value
	 */
	public abstract Object getValue();

	/**
	 * @param value the value to set
	 */
	public abstract void setValue(Object value);

	/**
	 * Return the formatted pair.
	 */
	public abstract String format();

}