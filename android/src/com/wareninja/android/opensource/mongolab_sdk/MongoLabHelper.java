/***
 *  Copyleft - Inkognito Ninja!
 *  
 *  @author: yg@wareninja.com
 *  @see https://github.com/WareNinja
 *  @see http://www.WareNinja.com
 *  
 *  disclaimer: I code for fun, dunno what I'm coding about :)
 */


package com.wareninja.android.opensource.mongolab_sdk;


import java.util.List;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.wareninja.android.opensource.mongolab_sdk.common.AppContext;
import com.wareninja.android.opensource.mongolab_sdk.common.AsyncTaskExecutor;
import com.wareninja.android.opensource.mongolab_sdk.common.CommonUtils;
import com.wareninja.android.opensource.mongolab_sdk.common.GenericRequestListener;
import com.wareninja.android.opensource.mongolab_sdk.common.ResponseMeta;
import com.wareninja.android.opensource.mongolab_sdk.common.ResponseModel;


/**
 * Implementation of MongoLab RESTful interface: 
 * @see: https://support.mongolab.com/entries/20433053-REST-API-for-MongoDB
 */
public class MongoLabHelper {

	private static final String TAG = MongoLabHelper.class.getSimpleName();
   
    public Context mContext;
    
    protected Gson gson;
	protected JsonParser jsonParser;
	public String apiKey = "";
    
    /*public MongoLabHelper(Context context) throws Exception{
        this.mContext = context;
        checkInitStuff();
    }*/
    public MongoLabHelper(Context context, String apiKey) throws Exception{
        this.mContext = context;
        this.apiKey = apiKey;
        checkInitStuff();
    }
    
    public void checkInitStuff() throws Exception {
    	
    	if (TextUtils.isEmpty(apiKey)) {
    		//-apiKey = mContext.getString(R.string.api_key);
    		throw new Exception("You MUST pass apiKey of your MongoLab account!!!!");
		}
    	
    	gson = CommonUtils.getGson();
		jsonParser = new JsonParser();
    }
    
    /*
     * >> List databases
     * get the databases linked to the authenticated account:
     * 	GET /databases
     * Example:
     * 	https://api.mongolab.com/api/1/databases?apiKey=myAPIKey
     */
    public void listDatabases(final GenericRequestListener reqListener) {
    	
    	Bundle reqParams = new Bundle();
    	reqParams.putString(AppContext.API_PARAM_APIKEY, apiKey);
    	
        AsyncTaskExecutor mAsyncTaskExecutor = new AsyncTaskExecutor( mContext );
        mAsyncTaskExecutor.setApiAction(AppContext.API_ACTION.DATABASES);
        mAsyncTaskExecutor.setHttpAction(AppContext.HTTP_ACTION.GET);
		mAsyncTaskExecutor.setReqListener(new GenericRequestListener() {

			@Override
			public void onComplete_wBundle(Bundle responseParams) {

				//if (AppContext.isDebugMode()) Log.d(TAG, "responseParams-> " + responseParams);

				if (responseParams.containsKey(AppContext.API_DATA_PARAM_RESPONSEMODEL)) {

					ResponseModel mResponseModel = (ResponseModel) responseParams.getSerializable(AppContext.API_DATA_PARAM_RESPONSEMODEL);
					if (AppContext.isDebugMode()) Log.d(TAG, "mResponseModel-> " + mResponseModel);
					if (mResponseModel.meta.code <= 201) {// 200 or 201 is success!!!
					  
						// TODO: process & put data into bundle!!!
						Bundle respBundle = new Bundle();
						//-respBundle.putSerializable(AppContext.DATA_EXTRA_SNPOIMETALIST, ptMetaList);
						respBundle.putSerializable(AppContext.API_DATA_PARAM_RESPONSEMODEL, mResponseModel);
						if (reqListener!=null) reqListener.onComplete_wBundle(respBundle);
					} else {
						if (reqListener!=null) reqListener.onError_wMeta(mResponseModel.meta);
					}
				}
			}

			@Override
			public void onError(String e) {
				if (reqListener!=null) reqListener.onError_wMeta( new ResponseMeta("onError : "+ e.toString()) );
			}
			@Override
			public void onError_wMeta(ResponseMeta responseMeta) {
				if (reqListener!=null) reqListener.onError_wMeta(responseMeta);
		    }
			@Override
			public void onBegin() {
				if (reqListener!=null) reqListener.onBegin();
			}
		});
		mAsyncTaskExecutor.execute(reqParams);
	}

    /*
     * >> List collections
     * To get the collections in the specified database: 
     * 	GET /databases/{database}/collections
     * Example:
     * https://api.mongolab.com/api/1/databases/my-db/collections?apiKey=myAPIKey
     * 
     */
    public void listCollections(String database
    		, final GenericRequestListener reqListener) {
    	
    	Bundle reqParams = new Bundle();
    	reqParams.putString(AppContext.API_PARAM_APIKEY, apiKey);
    	reqParams.putString("database", database);
    	
        AsyncTaskExecutor mAsyncTaskExecutor = new AsyncTaskExecutor( mContext );
        mAsyncTaskExecutor.setApiAction(AppContext.API_ACTION.COLLECTIONS);
        mAsyncTaskExecutor.setHttpAction(AppContext.HTTP_ACTION.GET);
		mAsyncTaskExecutor.setReqListener(new GenericRequestListener() {

			@Override
			public void onComplete_wBundle(Bundle responseParams) {

				//if (AppContext.isDebugMode()) Log.d(TAG, "responseParams-> " + responseParams);

				if (responseParams.containsKey(AppContext.API_DATA_PARAM_RESPONSEMODEL)) {

					ResponseModel mResponseModel = (ResponseModel) responseParams.getSerializable(AppContext.API_DATA_PARAM_RESPONSEMODEL);
					if (AppContext.isDebugMode()) Log.d(TAG, "mResponseModel-> " + mResponseModel);
					if (mResponseModel.meta.code <= 201) {// 200 or 201 is success!!!
					  
						// TODO: process & put data into bundle!!!
						Bundle respBundle = new Bundle();
						//-respBundle.putSerializable(AppContext.DATA_EXTRA_SNPOIMETALIST, ptMetaList);
						respBundle.putSerializable(AppContext.API_DATA_PARAM_RESPONSEMODEL, mResponseModel);
						if (reqListener!=null) reqListener.onComplete_wBundle(respBundle);
						
					} else {
						if (reqListener!=null) reqListener.onError_wMeta(mResponseModel.meta);
					}
				}
			}

			@Override
			public void onError(String e) {
				if (reqListener!=null) reqListener.onError_wMeta( new ResponseMeta("onError : "+ e.toString()) );
			}
			@Override
			public void onError_wMeta(ResponseMeta responseMeta) {
				if (reqListener!=null) reqListener.onError_wMeta(responseMeta);
		    }
			@Override
			public void onBegin() {
				if (reqListener!=null) reqListener.onBegin();
			}
		});
		mAsyncTaskExecutor.execute(reqParams);
	}
    
    /*
     * List documents
     * To get the documents in the specified collection. If no parameters are passed, it lists all of them. Otherwise, it lists the documents 
     * in the collection matching the specified parameters:
     * 
     * GET /databases/{database}/collections/{collection}
     * 
     * Example listing all documents in a given collection:
     * https://api.mongolab.com/api/1/databases/my-db/collections/my-coll?apiKey=myAPIKey
     * 
     * Optional parameters:
     * [q=<query>][&c=true][&f=<fields>][&fo=true][&s=<order>][&sk=<skip>][&l=<limit>]
     */
    public void listDocuments(String database, String collection
    		, final GenericRequestListener reqListener) {
    	
    	Bundle reqParams = new Bundle();
    	reqParams.putString(AppContext.API_PARAM_APIKEY, apiKey);
    	reqParams.putString("database", database);
    	reqParams.putString("collection", collection);
    	
        AsyncTaskExecutor mAsyncTaskExecutor = new AsyncTaskExecutor( mContext );
        mAsyncTaskExecutor.setApiAction(AppContext.API_ACTION.DOCUMENTS);
        mAsyncTaskExecutor.setHttpAction(AppContext.HTTP_ACTION.GET);
		mAsyncTaskExecutor.setReqListener(new GenericRequestListener() {

			@Override
			public void onComplete_wBundle(Bundle responseParams) {

				//if (AppContext.isDebugMode()) Log.d(TAG, "responseParams-> " + responseParams);

				if (responseParams.containsKey(AppContext.API_DATA_PARAM_RESPONSEMODEL)) {

					ResponseModel mResponseModel = (ResponseModel) responseParams.getSerializable(AppContext.API_DATA_PARAM_RESPONSEMODEL);
					if (AppContext.isDebugMode()) Log.d(TAG, "mResponseModel-> " + mResponseModel);
					if (mResponseModel.meta.code <= 201) {// 200 or 201 is success!!!
					  
						// TODO: process & put data into bundle!!!
						Bundle respBundle = new Bundle();
						//-respBundle.putSerializable(AppContext.DATA_EXTRA_SNPOIMETALIST, ptMetaList);
						respBundle.putSerializable(AppContext.API_DATA_PARAM_RESPONSEMODEL, mResponseModel);
						if (reqListener!=null) reqListener.onComplete_wBundle(respBundle);
						
					} else {
						if (reqListener!=null) reqListener.onError_wMeta(mResponseModel.meta);
					}
				}
			}

			@Override
			public void onError(String e) {
				if (reqListener!=null) reqListener.onError_wMeta( new ResponseMeta("onError : "+ e.toString()) );
			}
			@Override
			public void onError_wMeta(ResponseMeta responseMeta) {
				if (reqListener!=null) reqListener.onError_wMeta(responseMeta);
		    }
			@Override
			public void onBegin() {
				if (reqListener!=null) reqListener.onBegin();
			}
		});
		mAsyncTaskExecutor.execute(reqParams);
	}
    
    /*
     * CREATE DOCUMENTS in a specific collection
     * p.s. if collection doesn't exists already, it will be created right away!
     * 
     * >> Insert multiple documents
     * To add multiple documents to the specified collection, specify a list of documents in the data payload:
     * POST /databases/{database}/collections/{collection}
     * Content-Type: application/json
     * Body: <JSON data>
     * 
     */
    public void createDocuments(String database, String collection, List<String> documents
    		, final GenericRequestListener reqListener) {
    	
    	Bundle reqParams = new Bundle();
    	reqParams.putString(AppContext.API_PARAM_APIKEY, apiKey);
    	reqParams.putString("database", database);
    	reqParams.putString("collection", collection);
    	
    	JsonArray jsonArray = new JsonArray();
    	JsonObject jsonObject;
    	for (String document:documents) {
    		jsonObject = new JsonObject();
    		jsonObject = jsonParser.parse(document).getAsJsonObject();
    		jsonArray.add(jsonObject);
    	}
    	reqParams.putSerializable(AppContext.API_PARAM_JSONBODY
    			, CommonUtils.getGsonSimple().toJson(jsonArray)//, jsonArray.toString()
    			);
    	
        AsyncTaskExecutor mAsyncTaskExecutor = new AsyncTaskExecutor( mContext );
        mAsyncTaskExecutor.setApiAction(AppContext.API_ACTION.DOCUMENTS);
        mAsyncTaskExecutor.setHttpAction(AppContext.HTTP_ACTION.POST);
		mAsyncTaskExecutor.setReqListener(new GenericRequestListener() {

			@Override
			public void onComplete_wBundle(Bundle responseParams) {

				//if (AppContext.isDebugMode()) Log.d(TAG, "responseParams-> " + responseParams);

				if (responseParams.containsKey(AppContext.API_DATA_PARAM_RESPONSEMODEL)) {

					ResponseModel mResponseModel = (ResponseModel) responseParams.getSerializable(AppContext.API_DATA_PARAM_RESPONSEMODEL);
					if (AppContext.isDebugMode()) Log.d(TAG, "mResponseModel-> " + mResponseModel);
					if (mResponseModel.meta.code <= 201) {// 200 or 201 is success!!!
					  
						Bundle respBundle = new Bundle();
						//-respBundle.putSerializable(AppContext.DATA_EXTRA_SNPOIMETALIST, ptMetaList);
						respBundle.putSerializable(AppContext.API_DATA_PARAM_RESPONSEMODEL, mResponseModel);
						if (reqListener!=null) reqListener.onComplete_wBundle(respBundle);
						
					} else {
						if (reqListener!=null) reqListener.onError_wMeta(mResponseModel.meta);
					}
				}
			}

			@Override
			public void onError(String e) {
				if (reqListener!=null) reqListener.onError_wMeta( new ResponseMeta("onError : "+ e.toString()) );
			}
			@Override
			public void onError_wMeta(ResponseMeta responseMeta) {
				if (reqListener!=null) reqListener.onError_wMeta(responseMeta);
		    }
			@Override
			public void onBegin() {
				if (reqListener!=null) reqListener.onBegin();
			}
		});
		mAsyncTaskExecutor.execute(reqParams);
	}
}
