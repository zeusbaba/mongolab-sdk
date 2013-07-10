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

import java.util.Map;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;

import com.commonsware.cwac.task.AsyncTaskEx;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.wareninja.android.opensource.mongolab_sdk.R;

public class AsyncTaskExecutor extends AsyncTaskEx<Bundle, Void, Bundle> {

	protected final static String TAG = AsyncTaskExecutor.class.getSimpleName();

	WebService webService;
	String target = "";

	AppContext.API_ACTION apiAction = AppContext.API_ACTION.NULL;
	AppContext.HTTP_ACTION httpAction = AppContext.HTTP_ACTION.GET;
	GenericRequestListener mReqListener;
	Context taskContext;
	
	public AsyncTaskExecutor(Context taskContext) {
		this.taskContext = taskContext;
	}
	public AsyncTaskExecutor(Context taskContext,
			AppContext.API_ACTION apiAction) {
		this.taskContext = taskContext;
		this.apiAction = apiAction;
	}
	public AsyncTaskExecutor(Context taskContext,
			AppContext.API_ACTION apiAction,
			GenericRequestListener mReqListener) {
		this.taskContext = taskContext;
		this.mReqListener = mReqListener;
		this.apiAction = apiAction;
	}
	public AsyncTaskExecutor(Context taskContext,
			AppContext.API_ACTION apiAction,
			AppContext.HTTP_ACTION httpAction,
			GenericRequestListener mReqListener) {
		this.taskContext = taskContext;
		this.mReqListener = mReqListener;
		this.apiAction = apiAction;
		this.httpAction = httpAction;
	}

	public Context getTaskContext() {
		return taskContext;
	}
	public void setTaskContext(Context taskContext) {
		this.taskContext = taskContext;
	}

	public GenericRequestListener getReqListener() {
		return mReqListener;
	}

	public void setReqListener(GenericRequestListener mReqListener) {
		this.mReqListener = mReqListener;
	}

	public AppContext.API_ACTION getApiAction() {
		return apiAction;
	}
	public void setApiAction(AppContext.API_ACTION apiAction) {
		this.apiAction = apiAction;
	}
	public AppContext.HTTP_ACTION getHttpAction() {
		return httpAction;
	}
	public void setHttpAction(AppContext.HTTP_ACTION httpAction) {
		this.httpAction = httpAction;
	}

	@Override
	protected void onPreExecute() {
		

		webService = new WebService();
		//webService.setWebServiceUrl(taskContext.getString(R.string.api_base_url));
		
		//if (AppContext.isDebugMode()) Log.d(TAG, ">> onPreExecute: " + webService.webServiceUrl);

		if (mReqListener != null) mReqListener.onBegin();
		
		try {
		super.onPreExecute();
		} catch (Exception ex){}
	}

	@Override
	protected void onPostExecute(Bundle results) {
		
		// reqListener must be called with results here!!!
		// -if (AppContext.isDebugMode()) Log.d(TAG, "results-> " + results);

		if (results!=null && mReqListener!=null) mReqListener.onComplete_wBundle(results);
		try {
		super.onPostExecute(results);
		} catch (Exception ex){}
	}

	@Override
	protected Bundle doInBackground(Bundle... params) {

		String rawResponse = "";
		Bundle requestParams = params[0];
		Bundle resultParams = new Bundle();
		Bundle extraParams = new Bundle();
		
		String apiBaseUrl = taskContext.getString(R.string.api_base_url);
		if (requestParams.containsKey(AppContext.API_PARAM_APIKEY)) {
			extraParams.putString(AppContext.API_PARAM_APIKEY, requestParams.getString(AppContext.API_PARAM_APIKEY));
		}
		//else {
		//	extraParams.putString(AppContext.API_PARAM_APIKEY, taskContext.getString(R.string.api_key));
		//}
		
		if (AppContext.API_ACTION.DATABASES.equals(apiAction)) {
			target = taskContext.getString(R.string.api_endpoint_databases);
			//httpMethod = AppContext.HTTP_METHOD.GET;
		}
		else if (AppContext.API_ACTION.COLLECTIONS.equals(apiAction)) {
			target = taskContext.getString(R.string.api_endpoint_collections, requestParams.getString("database"));
		}
		else if (AppContext.API_ACTION.DOCUMENTS.equals(apiAction)) {
			target = taskContext.getString(R.string.api_endpoint_documents, requestParams.getString("database"), requestParams.getString("collection"));
		}
		
		webService.setWebServiceUrl(apiBaseUrl);
		
		if (TextUtils.isEmpty(target)) {
			if (mReqListener!=null) mReqListener.onError_wMeta( new ResponseMeta("API action type is not valid or doesnot exist!") );
			return (resultParams);
		}
		
		if (requestParams.containsKey(AppContext.API_PARAM_LIMIT)) {
			extraParams.putString(AppContext.API_PARAM_LIMIT, requestParams.getString(AppContext.API_PARAM_LIMIT));
		}
		if (requestParams.containsKey(AppContext.API_PARAM_QUERY)) {
			extraParams.putString(AppContext.API_PARAM_QUERY, requestParams.getString(AppContext.API_PARAM_QUERY));
		}
		if (requestParams.containsKey(AppContext.API_PARAM_ORDER)) {
			extraParams.putString(AppContext.API_PARAM_ORDER, requestParams.getString(AppContext.API_PARAM_ORDER));
		}
		if (requestParams.containsKey(AppContext.API_PARAM_FIELDS)) {
			extraParams.putString(AppContext.API_PARAM_FIELDS, requestParams.getString(AppContext.API_PARAM_FIELDS));
		}
		if (requestParams.containsKey(AppContext.API_PARAM_SKIP)) {
			extraParams.putString(AppContext.API_PARAM_SKIP, requestParams.getString(AppContext.API_PARAM_SKIP));
		}
		if (requestParams.containsKey(AppContext.API_PARAM_FO)) {
			extraParams.putString(AppContext.API_PARAM_FO, requestParams.getString(AppContext.API_PARAM_FO));
		}
		if (requestParams.containsKey(AppContext.API_PARAM_COUNT)) {
			extraParams.putString(AppContext.API_PARAM_COUNT, requestParams.getString(AppContext.API_PARAM_COUNT));
		}
		if (requestParams.containsKey(AppContext.API_PARAM_M)) {
			extraParams.putString(AppContext.API_PARAM_M, requestParams.getString(AppContext.API_PARAM_M));
		}
		if (requestParams.containsKey(AppContext.API_PARAM_U)) {
			extraParams.putString(AppContext.API_PARAM_U, requestParams.getString(AppContext.API_PARAM_U));
		}
		
		if (!AppContext.HTTP_ACTION.GET.equals(httpAction)
				&& requestParams.containsKey(AppContext.API_PARAM_JSONBODY)
				) {
			extraParams.putString(AppContext.API_PARAM_JSONBODY, requestParams.getString(AppContext.API_PARAM_JSONBODY));
		}

		try {

			if (AppContext.isDebugMode()) {
				Log.d(TAG, "httpAction-> " + httpAction);
				Log.d(TAG, "base+target-> " + webService.getWebServiceUrl() + target);
				Log.d(TAG, "requestParams (key, val)");
				for (String key : requestParams.keySet()) {
					Log.d(TAG, "Key: " + key + " / Val: " + requestParams.getString(key));
				}
			}

			// optional: you can also append custom user-agent 
			//-webService.addRequestHeader("User-Agent", CommonUtils.getMyUserAgent(taskContext));
			webService.addRequestHeader("Accept", "application/json");
			
			if (AppContext.HTTP_ACTION.POST.equals(httpAction)) {
				target += (target.contains("&"))? "&":"?";
				target += AppContext.API_PARAM_APIKEY+"="+extraParams.getString(AppContext.API_PARAM_APIKEY);
				//Log.d(TAG, "target -> "+target);
				rawResponse = webService.webInvokeWithJson(target, extraParams.getString(AppContext.API_PARAM_JSONBODY));
			}
			else if (AppContext.HTTP_ACTION.PUT.equals(httpAction)) {
				target += (target.contains("&"))? "&":"?";
				//target += AppContext.API_PARAM_APIKEY+"="+extraParams.getString(AppContext.API_PARAM_APIKEY);
				if (extraParams!=null && extraParams.size()>0) {
		    		for (String key:extraParams.keySet()) {
		    			target += key + "=" +extraParams.getString(key) +"&";
					}
		    	}
				if (target.endsWith("&")) target = target.substring(0, target.length()-1);
				
				rawResponse = webService.webInvokeWithJsonPUT(target, extraParams.getString(AppContext.API_PARAM_JSONBODY));
			}
			else if (AppContext.HTTP_ACTION.DELETE.equals(httpAction)) {
				target += (target.contains("&"))? "&":"?";
				target += AppContext.API_PARAM_APIKEY+"="+extraParams.getString(AppContext.API_PARAM_APIKEY);
				rawResponse = webService.webDelete(target, extraParams);
			}
			else {
				rawResponse = webService.webGet(target, extraParams);
			}
			//-if (AppContext.isDebugMode()) Log.d(TAG, "rawResponse-> " + rawResponse);

			ResponseModel mResponseModel = new ResponseModel();
			ResponseMeta responseMeta = new ResponseMeta();
			if (webService.getHttpResponseCode()!=null) responseMeta.code = webService.getHttpResponseCode();

            mResponseModel.meta = responseMeta;
            mResponseModel.data = rawResponse;
            //mResponseModel.notification = jsonResp.has("notifications") ? jsonResp.get("notifications")+"" : null;
            //if (AppContext.isDebugMode()) Log.d(TAG, "mResponseModel-> " + mResponseModel);
			
			resultParams.putSerializable(
					AppContext.API_DATA_PARAM_RESPONSEMODEL, mResponseModel);
			// NOTE: do we need to return back all requestParams again???
			resultParams.putBundle(AppContext.API_DATA_PARAM_REQUESTPARAMS,
					requestParams);

		} catch (Exception ex) {
			Log.w(TAG, ex.toString() + " |ÊrawResponse: "+rawResponse);
			//mReqListener.onError(ex.toString());
			ResponseMeta responseMeta = new ResponseMeta("Exception : "+ex.toString());
			if (webService.getHttpResponseCode()!=null) responseMeta.code = webService.getHttpResponseCode();
			if (mReqListener!=null) mReqListener.onError_wMeta( responseMeta );
		}

		return (resultParams);
	}

}
