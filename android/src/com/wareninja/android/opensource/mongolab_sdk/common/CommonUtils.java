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

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Modifier;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

import org.apache.http.impl.cookie.DateUtils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;


public final class CommonUtils {
	
	protected static final String TAG = CommonUtils.class.getSimpleName();

	public static Gson getGson() {
		return new GsonBuilder()
		    .excludeFieldsWithModifiers( new int[] { 
		    		Modifier.STATIC, Modifier.TRANSIENT//, Modifier.FINAL 
		    		} )
		    .excludeFieldsWithoutExposeAnnotation()
		    .create();
	}
	public static Gson getGsonWithPrettyPrinting() {
		return new GsonBuilder()
		    .excludeFieldsWithModifiers( new int[] { 
		    		Modifier.STATIC, Modifier.TRANSIENT//, Modifier.FINAL 
		    		} )
		    .excludeFieldsWithoutExposeAnnotation()
		    .setPrettyPrinting()
		    .create();
	}
	public static Gson getGsonSimple() {
		return new GsonBuilder()
		    .excludeFieldsWithModifiers( new int[] { 
		    		Modifier.STATIC, Modifier.TRANSIENT//, Modifier.FINAL 
		    		} )
		    .create();
	}
	public static Gson getGsonSimpleWithPrettyPrinting() {
		return new GsonBuilder()
		    .excludeFieldsWithModifiers( new int[] { 
		    		Modifier.STATIC, Modifier.TRANSIENT//, Modifier.FINAL 
		    		} )
		    .setPrettyPrinting()
		    .create();
	}
	
	
	public static String getShortFormattedDate() {
		return getShortFormattedDate(System.currentTimeMillis());
	}
	public static String getShortFormattedDate(long millis) {
		String resp = "";
		try {
			resp = getShortFormattedDate(new Date(millis));
		} catch (Exception ex){}
		return resp;
	}
	public static String getShortFormattedDate(Date date) {
		
		String resp = "";
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
			resp = sdf.format(date);
		} catch (Exception ex){}
		return resp;
	}
	 
	public static String getFormattedDate() {
		return getFormattedDate(System.currentTimeMillis());
	}
	public static String getFormattedDate(long millis) {
		String resp = "";
		try {
			resp = getFormattedDate(new Date(millis));
		} catch (Exception ex){}
		return resp;
	}
	public static String getFormattedDate(Date date) {
		
		String resp = "";
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ", Locale.US);
			resp = sdf.format(date);
		} catch (Exception ex){}
		return resp;
	}
	public static String getFormattedDate(Long millis, String timeZone) {
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ", Locale.US);
		if (!TextUtils.isEmpty(timeZone)) sdf.setTimeZone(TimeZone.getTimeZone(timeZone));
		
		return millis!=null?sdf.format( new Date(millis) ):"";
	}
	
    /**
     * Generate the multi-part post body providing the parameters and boundary
     * string
     * 
     * @param parameters the parameters need to be posted
     * @param boundary the random string as boundary
     * @return a string of the post body
     */
    public static String encodePostBody(Bundle parameters, String boundary) {
        if (parameters == null) return "";
        StringBuilder sb = new StringBuilder();
        
        for (String key : parameters.keySet()) {
        	
        	/*//YG:removed this from sdk
        	try{
            if (parameters.getByteArray(key) != null) {
        	    continue;
            }
        	}catch(Exception ex){}
        	*/
        	
            sb.append("Content-Disposition: form-data; name=\"" + key + 
            		//"\"\r\n\r\n" + parameters.getString(key));
            		"\"\r\n\r\n" + parameters.get(key));//to avoid type clash
            sb.append("\r\n" + "--" + boundary + "\r\n");
        }
        
        return sb.toString();
    }

    public static String encodeUrl(Bundle parameters) {
        if (parameters == null) {
        	return "";
        }
        
        StringBuilder sb = new StringBuilder();
        boolean first = true;
        for (String key : parameters.keySet()) {
            if (first) first = false; else sb.append("&");
            sb.append(URLEncoder.encode(key) + "=" +
                      URLEncoder.encode(parameters.getString(key)));
        }
        return sb.toString();
    }

    public static Bundle decodeUrl(String s) {
        Bundle params = new Bundle();
        if (s != null) {
            String array[] = s.split("&");
            for (String parameter : array) {
                String v[] = parameter.split("=");
                // YG: in case param has no value
                if (v.length==2){
                	params.putString(URLDecoder.decode(v[0]),
                                 URLDecoder.decode(v[1]));
                }
                else {
                	params.putString(URLDecoder.decode(v[0])," ");
                }
            }
        }
        return params;
    }

    /**
     * Parse a URL query and fragment parameters into a key-value bundle.
     * 
     * @param url the URL to parse
     * @return a dictionary bundle of keys and values
     */
    public static Bundle parseUrl(String url) {
        
        try {
            URL u = new URL(url);
            Bundle b = decodeUrl(u.getQuery());
            b.putAll(decodeUrl(u.getRef()));
            return b;
        } catch (MalformedURLException e) {
        	Log.w(TAG, "parseUrl ex ->" + e.toString());
            return new Bundle();
        }
    }

    
    /**
     * Connect to an HTTP URL and return the response as a string.
     * 
     * Note that the HTTP method override is used on non-GET requests. (i.e.
     * requests are made as "POST" with method specified in the body).
     * 
     * @param url - the resource to open: must be a welformed URL
     * @param method - the HTTP method to use ("GET", "POST", etc.)
     * @param params - the query parameter for the URL (e.g. access_token=foo)
     * @return the URL contents as a String
     * @throws MalformedURLException - if the URL format is invalid
     * @throws IOException - if a network problem occurs
     */
    public static String openUrl(String url, String method, Bundle params) 
          throws MalformedURLException, IOException {
    	// random string as boundary for multi-part http post
    	String strBoundary = "3i2ndDfv2rTHiSisAbouNdArYfORhtTPEefj3q2f";
    	String endLine = "\r\n";
   
    	OutputStream os;

        if (method.equals("GET")) {
            url = url + "?" + encodeUrl(params);
        }
        if (AppContext.isDebugMode())Log.d(TAG, method + " URL: " + url);
        HttpURLConnection conn = 
            (HttpURLConnection) new URL(url).openConnection();
        conn.setRequestProperty("User-Agent", System.getProperties().
                getProperty("http.agent") + " WareNinjaAndroidSDK");
        if (!method.equals("GET")) {
            Bundle dataparams = new Bundle();
            for (String key : params.keySet()) {
            	
            	/*
                if (params.getByteArray(key) != null) {
                        dataparams.putByteArray(key, params.getByteArray(key));
                }
                */
            	// YG: added this to avoid fups
            	byte[] byteArr = null;
            	try {
            		byteArr = (byte[])params.get(key);
            	}catch(Exception ex1){}
            	if (byteArr!=null)
            		dataparams.putByteArray(key, byteArr );
            }
        	
            // use method override
            if (!params.containsKey("method")) {
            	params.putString("method", method);           	
            }
            
            if (params.containsKey("access_token")) {
            	String decoded_token = URLDecoder.decode(params.getString("access_token"));
            	params.putString("access_token", decoded_token);
            }
                     
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "multipart/form-data;boundary="+strBoundary);
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.setRequestProperty("Connection", "Keep-Alive");
            conn.connect();
            os = new BufferedOutputStream(conn.getOutputStream());
            
            os.write(("--" + strBoundary +endLine).getBytes());
            os.write((encodePostBody(params, strBoundary)).getBytes());
            os.write((endLine + "--" + strBoundary + endLine).getBytes());
            
            if (!dataparams.isEmpty()) {
            	
                for (String key: dataparams.keySet()){
                    os.write(("Content-Disposition: form-data; filename=\"" + key + "\"" + endLine).getBytes());
                    os.write(("Content-Type: content/unknown" + endLine + endLine).getBytes());
                    os.write(dataparams.getByteArray(key));
                    os.write((endLine + "--" + strBoundary + endLine).getBytes());

                }          	
            }
            os.flush();
        }
        
        String response = "";
        try {
        	response = read(conn.getInputStream());
        } catch (FileNotFoundException e) {
            // Error Stream contains JSON that we can parse to a FB error
            response = read(conn.getErrorStream());
        }
        if (AppContext.isDebugMode())Log.d(TAG, method + " response: " + response);
        
        return response;
    }

    public static String read(InputStream in) throws IOException {
        StringBuilder sb = new StringBuilder();
        BufferedReader r = new BufferedReader(new InputStreamReader(in), 1000);
        for (String line = r.readLine(); line != null; line = r.readLine()) {
            sb.append(line);
        }
        in.close();
        return sb.toString();
    }

    public static void clearCookies(Context context) {
        // Edge case: an illegal state exception is thrown if an instance of 
        // CookieSyncManager has not be created.  CookieSyncManager is normally
        // created by a WebKit view, but this might happen if you start the 
        // app, restore saved state, and click logout before running a UI 
        // dialog in a WebView -- in which case the app crashes
    	try {
	        @SuppressWarnings("unused")
	        CookieSyncManager cookieSyncMngr = 
	            CookieSyncManager.createInstance(context);
	        CookieManager cookieManager = CookieManager.getInstance();
	        cookieManager.removeAllCookie();
    	}
    	catch (Exception ex) {}
    }

    /**
     * Display a simple alert dialog with the given text and title.
     * 
     * @param context 
     *          Android context in which the dialog should be displayed
     * @param title 
     *          Alert dialog title
     * @param text
     *          Alert dialog message
     */
    public static void showAlert(Context context, String title, String text) {
        Builder alertBuilder = new Builder(context);
        alertBuilder.setTitle(title);
        alertBuilder.setMessage(text);
        alertBuilder.create().show();
    }

    /*
     * building Gravatar URL;
     * 	String email = "someone@somewhere.com";
     * 	String hash = Util.md5Hex(email);
     * 
     * 	http://www.gravatar.com/205e460b479e2e5b48aec07710c08d50.json
     * 
     * 	check here for gravatar profiles: http://en.gravatar.com/site/implement/profiles/json/
     * 
     * OR very simply request the image; http://en.gravatar.com/site/implement/images/ 
     * -> http://www.gravatar.com/avatar/HASH.png
     * By default, images are presented at 80px by 80px if no size parameter is supplied
     * optional; ?s=200   to set size  (1px up to 512px)
     * 
     */
    public static String hex(byte[] array) {
    	StringBuffer sb = new StringBuffer();
    	for (int i = 0; i < array.length; ++i) {
    		sb.append(Integer.toHexString((array[i]& 0xFF) | 0x100).substring(1,3));        
    	}
    	return sb.toString();
    }
    public static String md5Hex (String message) {
    	try {
    		MessageDigest md = MessageDigest.getInstance("MD5");
    		return hex (md.digest(message.getBytes("CP1252")));
      } catch (NoSuchAlgorithmException e) {
      } catch (UnsupportedEncodingException e) {
      }
      return null;
    }
    	
    
    // --------------
    public static boolean checkInternetConnection(Context context) {

    	ConnectivityManager conMgr = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
    	
    	// ARE WE CONNECTED TO THE NET?
    	if (conMgr.getActiveNetworkInfo() != null
    			&& conMgr.getActiveNetworkInfo().isAvailable()
    			&& conMgr.getActiveNetworkInfo().isConnected()) {
    		return true;
    	} else {
    		Log.w(TAG, "Internet Connection NOT Present");
    		return false;
    	}
    }
	public static boolean isConnAvailAndNotRoaming(Context context) {

    	ConnectivityManager conMgr = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);

    	if (conMgr.getActiveNetworkInfo() != null
    			&& conMgr.getActiveNetworkInfo().isAvailable()
    			&& conMgr.getActiveNetworkInfo().isConnected()) {
    		
    		if(!conMgr.getActiveNetworkInfo().isRoaming())
    			return true;
    		else
    			return false;
    	} else {
    		Log.w(TAG, "Internet Connection NOT Present");
    		return false;
    	}
    }
	public static boolean isRoaming(Context context) {

    	ConnectivityManager conMgr = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);

    	return (conMgr.getActiveNetworkInfo()!=null && conMgr.getActiveNetworkInfo().isRoaming());
    }
	
	public static void showSoftKeyboard (Context context, View view) {
    	try {
			((InputMethodManager)context.getSystemService(Context.INPUT_METHOD_SERVICE))
		    .showSoftInput(view, InputMethodManager.SHOW_FORCED);
    	}
    	catch (Exception ex) {
    		Log.w(TAG, "showSoftKeyboard->"+ex.toString());
    	}
  	}
    public static void hideSoftKeyboard (Context context, View view) {
    	try {
			InputMethodManager imm = (InputMethodManager)context.getSystemService(Context.INPUT_METHOD_SERVICE);
			imm.hideSoftInputFromWindow(view.getApplicationWindowToken(), 0);
    	}
    	catch (Exception ex) {
    		Log.w(TAG, "hideSoftKeyboard->"+ex.toString());
    	}
  	}
    
	
	public static boolean isIntentAvailable(final Context context, final Intent intent) {
	    final PackageManager packageManager = context.getPackageManager();
	    
	    List<ResolveInfo> list =
	            packageManager.queryIntentActivities(intent,
	                    PackageManager.MATCH_DEFAULT_ONLY);
	    return list.size() > 0;
	}
	
	
	public static String getVersionString(Context context) {
        // Get a version string for the app.
        try {
            PackageManager pm = context.getPackageManager();
            PackageInfo pi = pm.getPackageInfo(AppContext.PACKAGE_NAME, 0);
            return AppContext.PACKAGE_NAME + ":" + String.valueOf(pi.versionCode);
        } catch (NameNotFoundException e) {
            if(AppContext.isDebugMode())Log.d(TAG, "Could not retrieve package info", e);
            //throw new RuntimeException(e);
           // ErrorReporter.getInstance().handleSilentException(e);
            return "";
        }
    }

	public static String getMyUserAgent(Context context) {
		return getMyUserAgent(context, false);
	}
	public static String getMyUserAgent(Context context, boolean withDeviceDetails) {
		
		String userAgent = String.format("%s (%s)"
				, CommonUtils.getVersionName(context)
				, CommonUtils.getVersionString(context)
				);
		userAgent += withDeviceDetails? " / "+CommonUtils.getDeviceDetails(context) : "";
		
		return userAgent;
	}
	public static String getVersionName(Context context) {
        // Get a version name for the app.
		String version = "?";
		try {
	        PackageInfo pi = context.getPackageManager().getPackageInfo(
	        		context.getPackageName(), 0);
	        version = "v"+pi.versionName;
		} 
		catch (PackageManager.NameNotFoundException e) {
		        Log.wtf(TAG, "Version name not found", e);
		        //ACRA.getErrorReporter().handleSilentException(e);
		}
		
		return version;
    }
	public static int getVersionCode(Context context) {
        // Get a version name for the app.
		int versionCode = 0;
		try {
	        PackageInfo pi = context.getPackageManager().getPackageInfo(
	        		context.getPackageName(), 0);
	        versionCode = pi.versionCode;
		} 
		catch (PackageManager.NameNotFoundException e) {
	        Log.wtf(TAG, "version code not found", e);
	        //ACRA.getErrorReporter().handleSilentException(e);
		}
		
		return versionCode;
    }
	
	public static String getDeviceDetails(Context context) {
		StringBuilder sb = new StringBuilder();
		String deviceDetails = "";
		try {
			
	        //sb.append(" model: " + Build.BRAND+" "+Build.MODEL);
			sb.append(Build.BRAND+" "+Build.MODEL);
	        sb.append(" | " + Build.VERSION.RELEASE + " " + Build.ID + " (build "+ Build.VERSION.INCREMENTAL+")" );
	        
	        deviceDetails = sb.toString();
		}
		catch (Exception ex) {
			if(AppContext.isDebugMode())Log.d(TAG, "error while getDeviceDetails : " + ex.toString());
		}
		
		return deviceDetails;
	}

	
	// silly method for stackoverflow
	public static boolean isIP(String input) {
		
		if (input.contains(".") && input.length()>1) {
			return TextUtils.isDigitsOnly( input.replace(".", "").trim() );
		}
		else {
			return false;
		}
	}


	
	// --- GENERAL Util funcs ---
	public static String convertTimeInMillis2DayString(long millis) {
    	
    	if (millis<=0)
    		return ""+millis;
    	else {
	    	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
	    	
	    	return sdf.format(new Date(millis));
    	}
    }
	public static String convertTimeInMillis2String(long millis) {
    	
    	if (millis<=0)
    		return ""+millis;
    	else {
	    	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.US);
	    	
	    	return sdf.format(new Date(millis));
    	}
    }
	// 
	/* "2011-06-01T09:34:20+02:00" current LOCO
	 * * this is the format from facebook & LOCO!
	 * "updated_time": "2011-01-03T15:55:47+0000" -> using "UTC" by default
	 * yyyy-MM-dd'T'HH:mm:ssZ
	 */ 
	public static String convertTimeInMillis2NiceStr(long millis) {
    	
    	if (millis<=0)
    		return ""+millis;
    	else {
	    	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ", Locale.US);
	    	
	    	return sdf.format(new Date(millis));
    	}
    }
	public static String getTimeBetweenNowNiceStr(String date1) {
		
		if (TextUtils.isEmpty(date1)) return "";
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ", Locale.US);
		String date2 = sdf.format(new Date(System.currentTimeMillis()));
		
		return getTimeBetweenNiceStr(date1, date2);
	}
	public static String getTimeBetweenNiceStr(String date1, String date2) {
    	// input MUST be exactly like this; "2011-06-01T09:34:20+02:00"
    	String result = "";
    	
    	if (TextUtils.isEmpty(date1)||TextUtils.isEmpty(date2)) return "";
    	
    	try {
    		
			Date dateOne = DateUtils.parseDate(date1, new String[] {"yyyy-MM-dd'T'HH:mm:ssZ"} );
			Calendar cal1 = Calendar.getInstance();
			cal1.setTime(dateOne);
			
			Date dateTwo = DateUtils.parseDate(date2, new String[] {"yyyy-MM-dd'T'HH:mm:ssZ"} );
			Calendar cal2 = Calendar.getInstance();
			cal2.setTime(dateTwo);
			
			Date dateBig;// = null;
			Date dateSmall;// = null;
			
			if (cal1.compareTo(cal2)==1) {
				dateBig = dateOne;
				dateSmall = dateTwo;
			}
			else if (cal1.compareTo(cal2)==-1) {
				dateBig = dateTwo;
				dateSmall = dateOne;
			}
			else {// both are equal
				//result = "now";
				return "now";
			}
			//Log.d(TAG, "cal1<>cal2 : " + cal1.compareTo(cal2) );
			/*
			 * 0 if the times of the two Calendars are equal, 
			 * -1 if the time of this Calendar is before the other one, 
			 * 1 if the time of this Calendar is after the other one.
			 */
			
			// day difference between two dates!
			long diff = dateBig.getTime() - dateSmall.getTime();
			diff = TimeUnit.MILLISECONDS.toSeconds(diff);
			
			long days = diff / (24 * 60 * 60);// * 1000);
			long hours = diff / (60 * 60);// * 1000);
			long mins = diff / (60);// * 1000);
			long secs = diff;// / (1000);
			
			if (days!=0) {
				result = days+" day" + (days>1?"s":"");
			}
			else if (hours!=0) {
				result = hours+" hour" + (hours>1?"s":"");
			}
			else if (mins!=0) {
				result = mins+" min" + (mins>1?"s":"");
			}
			else if (secs!=0) {
				result = secs+" sec" + (secs>1?"s":"");
			}
			if (!TextUtils.isEmpty(result)) result += " ago";
			
			/*Log.d(TAG, 
					"days in between:" 
					+ ( TimeUnit.MILLISECONDS.toSeconds(diff)/60/60/24  ) 
					);*/
			// days in between:4
		}
		catch (Exception ex) {
			Log.w(TAG, ex.toString());
		}
    	
    	return result;
    }

	
	// -> source from CastUtils: https://github.com/apache/pig/blob/89c2e8e76c68d0d0abe6a36b4e08ddc56979796f/src/org/apache/pig/impl/util/CastUtils.java
    private static Integer mMaxInt = Integer.valueOf(Integer.MAX_VALUE);
    private static Long mMaxLong = Long.valueOf(Long.MAX_VALUE);

    public static Double stringToDouble(String str) {
	    if (str == null) {
	    	return null;
	    } else {
		    try {
		    return Double.parseDouble(str);
		    } catch (NumberFormatException e) {
		    	Log.w(TAG, "Unable to interpret value "
		    		    + str
		    		    + " in field being "
		    		    + "converted to double, caught NumberFormatException <"
		    		    + e.getMessage() + "> field discarded");
		    	return null;
		    }
	    }
    }
    public static Float stringToFloat(String str) {
	    if (str == null) {
	    	return null;
	    } else {
		    try {
		    	return Float.parseFloat(str);
		    } catch (NumberFormatException e) {
		    	Log.w(TAG, "Unable to interpret value "
		    		    + str
		    		    + " in field being "
		    		    + "converted to float, caught NumberFormatException <"
		    		    + e.getMessage() + "> field discarded");
		    	return null;
		    }
	    }
    }
    public static Integer stringToInteger(String str) {
	    if (str == null) {
	    	return null;
	    } else {
		    try {
		    	return Integer.parseInt(str);
		    } catch (NumberFormatException e) {
			    // It's possible that this field can be interpreted as a double.
			    // Unfortunately Java doesn't handle this in Integer.valueOf. So
			    // we need to try to convert it to a double and if that works
			    // then
			    // go to an int.
			    try {
				    Double d = Double.valueOf(str);
				    // Need to check for an overflow error
				    if (d.doubleValue() > mMaxInt.doubleValue() + 1.0) {
				    	Log.w(TAG, "Value " + d
							    + " too large for integer");
				    	return null;
				    }
				    return Integer.valueOf(d.intValue());
			    } catch (NumberFormatException nfe2) {
			    	Log.w(TAG, "Unable to interpret value "
						    + str
						    + " in field being "
						    + "converted to int, caught NumberFormatException <"
						    + e.getMessage()
						    + "> field discarded");
			    	return null;
			    }
		    }
	    }
    }
    public static Long stringToLong(String str) {
	    if (str == null) {
	    	return null;
	    } else {
		    try {
		    	return Long.parseLong(str);
		    } catch (NumberFormatException e) {
			    // It's possible that this field can be interpreted as a double.
			    // Unfortunately Java doesn't handle this in Long.valueOf. So
			    // we need to try to convert it to a double and if that works
			    // then
			    // go to an long.
			    try {
				    Double d = Double.valueOf(str);
				    // Need to check for an overflow error
				    if (d.doubleValue() > mMaxLong.doubleValue() + 1.0) {
				    	Log.w(TAG, "Value " + d
							    + " too large for long");
				    	return null;
				    }
				    return Long.valueOf(d.longValue());
			    } catch (NumberFormatException nfe2) {
			    	Log.w(TAG, "Unable to interpret value "
						    + str
						    + " in field being "
						    + "converted to long, caught NumberFormatException <"
						    + nfe2.getMessage()
						    + "> field discarded");
			    	return null;
			    }
		    }
	    }
    }
	// ---
    
    
	// --- GeoLoc conversions ---
	/** Multiplier for km to miles. **/
	private static final double MILES_CONVERT = 0.621371192;
	/** Multiplier for meters to feet. **/
	private static final double FEET_CONVERT = 3.2808399;
	/** Conversion factor for degrees/mdegrees. **/
	public static final double CNV = 1E6;
	/**
	 * Convert a number of kilometers to miles.
	 * @param km number of km to convert
	 * @return number of km in miles
	 */
	public static double asMiles(final double km) {
		DecimalFormat twoDForm = new DecimalFormat("#.##");
		return Double.valueOf(twoDForm.format(km * MILES_CONVERT));
	}
	//public static double asMiles(final int meters) {
	public static double asMiles(final long meters) {
		DecimalFormat twoDForm = new DecimalFormat("#.##");
		return Double.valueOf(twoDForm.format(asMiles(meters / 1000.0)));
	}
	/**
	 * Convert a number of meters to feet.
	 * @param meters Value to convert in meters.
	 * @return meters converted to feet.
	 */
	public static double asFeet(final double meters) {
		DecimalFormat twoDForm = new DecimalFormat("#.##");
		return Double.valueOf(twoDForm.format(meters * FEET_CONVERT));
	}
	public static String asFeetString(final double meters) {
		return asFeet(meters) + " ft";
	}
	
	public static String asMilesString(final double km) {
		return asMiles(km) + " mi";
	}
	public static String asMilesString(final int meters) {
		return asMiles(meters) + " mi";
	}
	public static String asKilometerString(final double km) {
		return km + " km";
	}
	
	public static String asKilometerString(final int meters) {
		return (double)(meters/1000) + " km";
	}
	public static String asMeterString(final int meters) {
		return meters + " meters";
	}
	
	/**
	 * Convert degrees to microdegrees.
	 * @param degrees
	 * @return integer microdegrees.
	 */
	public static int asMicroDegrees(final double degrees) {
		return (int) (degrees * CNV);
	}
	
	/**
	 * Convert microdegrees to degrees.
	 * @param mDegrees
	 * @return double type degrees.
	 */
	public static double asDegrees(final int mDegrees) {
		return mDegrees / CNV;
	}
	
}
