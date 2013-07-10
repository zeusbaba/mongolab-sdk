/***
 *  Copyleft - Inkognito Ninja!
 *  
 *  @author: yg@wareninja.com
 *  @see https://github.com/WareNinja
 *  @see http://www.WareNinja.com
 *  
 *  disclaimer: I code for fun, dunno what I'm coding about :)
 */

package com.wareninja.android.opensource.mongolab_sdk.common;

public class AppContext {
	
	private static boolean debugMode = true;
	public static boolean isDebugMode() {
        return debugMode;
    }
    public static void setDebugMode(boolean debugMode) {
    	AppContext.debugMode = debugMode;
    }
    
	public static final String APPWEBSITE_URL = "http://wareninja.com";
	public static final String WEB_APP_BASE_URL = "http://wareninja.com";
	public static final String WEB_APP_ABOUT = "/about";
	public static final String WEB_APP_APPVERSION = "app_version";
	
	public static final String PACKAGE_NAME = "com.wareninja.android.opensource.mongolab_sdk";
	
	public static final String APP_MARKET_URL = "market://details?id=" + PACKAGE_NAME;
	public static final String APP_MARKET_URL_WEB = "https://play.google.com/store/apps/details?id=" + PACKAGE_NAME;
	public static final String WARENINJAAPPS_MARKET_URL = "market://search?q=wareninja";
	public static final String WARENINJAAPPS_MARKET_URL_WEB = "https://play.google.com/store/search?q=wareninja";
	
	public static String API_ACTION_TYPE = "api_action_type";
	public static String API_DATA_PARAM_RESPONSEMODEL = "api_data_param_responsemodel";
    public static String API_DATA_PARAM_DATALIST = "api_data_param_datalist";
    public static String API_DATA_PARAM_RESPONSE_EXTRA = "api_data_param_response_extra";
    public static String API_DATA_PARAM_REQUESTPARAMS = "api_data_param_requestparams";
    
    public static String API_PARAM_JSONBODY = "jsonbody";
    // [q=<query>][&c=true][&f=<fields>][&fo=true][&s=<order>][&sk=<skip>][&l=<limit>]
    public static String API_PARAM_LIMIT = "l";
    public static String API_PARAM_QUERY = "q";
    public static String API_PARAM_ORDER = "s";
    public static String API_PARAM_FIELDS = "f";
    public static String API_PARAM_SKIP = "sk";
    public static String API_PARAM_FO = "fo";
    public static String API_PARAM_COUNT = "c";
    
    public static String API_PARAM_APIKEY = "apiKey";
    
	public enum HTTP_ACTION { 
    	GET, POST, PUT
	};
	public enum API_ACTION { 
    	NULL
    	, DATABASES, COLLECTIONS, DOCUMENTS
	};
	
	
}
