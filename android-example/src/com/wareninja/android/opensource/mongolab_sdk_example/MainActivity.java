package com.wareninja.android.opensource.mongolab_sdk_example;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.JsonObject;
import com.wareninja.android.opensource.mongolab_sdk.MongoLabHelper;
import com.wareninja.android.opensource.mongolab_sdk.common.CommonUtils;
import com.wareninja.android.opensource.mongolab_sdk.common.GenericRequestListener;
import com.wareninja.android.opensource.mongolab_sdk.common.ResponseMeta;

import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.Menu;

public class MainActivity extends Activity {

	public static String TAG = MainActivity.class.getSimpleName();
	Context mContext;
    Activity mActivity;
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		mContext = this;
	    mActivity = this;
		
		quickTesting();
	}

	/*
	 * Quick examples of basic usage!
	 */
	MongoLabHelper mongoLabHelper;
	protected void quickTesting () {
		
		try {
			// FIXME: you MUST use your own apiKey!!! see docs: https://support.mongolab.com/entries/20433053-REST-API-for-MongoDB 
			mongoLabHelper = new MongoLabHelper(mContext, mContext.getString(R.string.mongolab_api_key));
			
		} catch (Exception e) {
			
			e.printStackTrace();
			return;
		}
		
		Log.d(TAG, ">>> listDatabases");
		mongoLabHelper.listDatabases(new GenericRequestListener() {

			@Override
			public void onBegin() {
				// NOTE: we can use to display progress dialog etc
			}
			@Override
			public void onError_wMeta(ResponseMeta responseMeta) {
				Log.w(TAG, "listDatabases | " + "onError_wMeta -> " + responseMeta.toString());
			}
			
			@Override
			public void onComplete_wBundle(Bundle responseParams) {
				Log.d(TAG, "listDatabases | " + "onComplete_wBundle -> " + responseParams);
// listDatabases | onComplete_wBundle -> Bundle[{api_data_param_responsemodel=
//	ResponseModel [meta=ResponseMeta [code=200, errorType=, errorDetail=, extraData=Bundle[{}]], notification=null, data=[ "dev-playground" ]]}]
			}			
		});
	
		Log.d(TAG, ">>> listCollections");
		mongoLabHelper.listCollections("dev-playground", new GenericRequestListener() {

			@Override
			public void onBegin() {
				// NOTE: we can use to display progress dialog etc
			}
			@Override
			public void onError_wMeta(ResponseMeta responseMeta) { 
				Log.w(TAG, "listCollections | " + "onError_wMeta -> " + responseMeta.toString());
			}
			
			@Override
			public void onComplete_wBundle(Bundle responseParams) {
				Log.d(TAG, "listCollections | " + "onComplete_wBundle -> " + responseParams);
				
			}			
		});
		
		
		Log.d(TAG, ">>> createDocuments");
		List<String> documents = new ArrayList<String>();
		JsonObject jsonObject = new JsonObject();
		jsonObject.addProperty("key1", "value-"+System.currentTimeMillis());
		jsonObject.addProperty("key2", "value-"+System.currentTimeMillis());
		documents.add( CommonUtils.getGsonSimple().toJson(jsonObject) );
		// NOTE: 'param1' is database name, 'param2' is collection name!!!
		mongoLabHelper.createDocuments("dev-playground", "dev-collection", documents
				, new GenericRequestListener() {

			@Override
			public void onBegin() {
				// NOTE: we can use to display progress dialog etc
			}
			@Override
			public void onError_wMeta(ResponseMeta responseMeta) {
				// NOTE: 
				Log.w(TAG, "createDocuments | " + "onError_wMeta -> " + responseMeta.toString());
			}
			
			@Override
			public void onComplete_wBundle(Bundle responseParams) {
				Log.d(TAG, "createDocuments | " + "onComplete_wBundle -> " + responseParams);
				
                Log.d(TAG, ">>> listDocuments");
				mongoLabHelper.listDocuments("dev-playground", "dev-collection", new GenericRequestListener() {

					@Override
					public void onBegin() {
						// NOTE: we can use to display progress dialog etc
					}
					@Override
					public void onError_wMeta(ResponseMeta responseMeta) { 
						Log.w(TAG, "listDocuments | " + "onError_wMeta -> " + responseMeta.toString());
					}
					
					@Override
					public void onComplete_wBundle(Bundle responseParams) {
						Log.d(TAG, "listDocuments | " + "onComplete_wBundle -> " + responseParams);
						
					}			
				});

                }
            });
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
