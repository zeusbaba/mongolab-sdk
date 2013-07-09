package com.wareninja.android.opensource.mongolab_sdk_example;

import java.util.ArrayList;
import java.util.List;

import org.acra.ReportField;
import org.acra.collector.CrashReportData;
import org.acra.sender.ReportSender;
import org.acra.sender.ReportSenderException;

import com.google.gson.JsonObject;
import com.wareninja.android.opensource.mongolab_sdk.MongoLabHelper;
import com.wareninja.android.opensource.mongolab_sdk.common.CommonUtils;
import com.wareninja.android.opensource.mongolab_sdk.common.GenericRequestListener;
import com.wareninja.android.opensource.mongolab_sdk.common.ResponseMeta;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;

public class ACRA_ReportSender implements ReportSender {

	/**
	 * You can use this Custom ReportSender together with mongolab-sdk
	 * 
	 * for example while initializing ACRA in your Application, you just define this class as a custom reporter!
	 * // The following line triggers the initialization of ACRA
        ACRA.init(this);
        // setting our custom reporter
        ACRA_ReportSender reportSender = new ACRA_ReportSender(this);
        ACRA.getErrorReporter().setReportSender(reportSender);
	 */
	
	private static final String TAG = ACRA_ReportSender.class.getSimpleName();
	
    public Context mContext;
	
	public ACRA_ReportSender (Context context) {
        this.mContext = context;
    }
    
	@Override
	public void send(CrashReportData reportData) throws ReportSenderException {
		
		JsonObject reqJson = new JsonObject();
		try {
			
			/*// if you want to include ALL!!!
			for (Map.Entry entry:reportData.entrySet()) {
				reqJson.addProperty(entry.getKey().toString(), entry.getValue().toString());
			}*/
			// >>> add only required fields
			ReportField field;
			field = ReportField.USER_EMAIL;
			if (reportData.containsKey(field)) reqJson.addProperty(field.name(), reportData.get(field).toString());
			field = ReportField.USER_COMMENT;
			if (reportData.containsKey(field)) reqJson.addProperty(field.name(), reportData.get(field).toString());
			field = ReportField.USER_CRASH_DATE;
			if (reportData.containsKey(field)) reqJson.addProperty(field.name(), reportData.get(field).toString());
			field = ReportField.PHONE_MODEL;
			if (reportData.containsKey(field)) reqJson.addProperty(field.name(), reportData.get(field).toString());
			field = ReportField.PACKAGE_NAME;
			if (reportData.containsKey(field)) reqJson.addProperty(field.name(), reportData.get(field).toString());
			field = ReportField.ANDROID_VERSION;
			if (reportData.containsKey(field)) reqJson.addProperty(field.name(), reportData.get(field).toString());
			field = ReportField.APP_VERSION_CODE;
			if (reportData.containsKey(field)) reqJson.addProperty(field.name(), reportData.get(field).toString());
			field = ReportField.APP_VERSION_NAME;
			if (reportData.containsKey(field)) reqJson.addProperty(field.name(), reportData.get(field).toString());
			field = ReportField.DISPLAY;
			if (reportData.containsKey(field)) reqJson.addProperty(field.name(), reportData.get(field).toString());
			field = ReportField.AVAILABLE_MEM_SIZE;
			if (reportData.containsKey(field)) reqJson.addProperty(field.name(), reportData.get(field).toString());
			field = ReportField.TOTAL_MEM_SIZE;
			if (reportData.containsKey(field)) reqJson.addProperty(field.name(), reportData.get(field).toString());
			field = ReportField.REPORT_ID;
			if (reportData.containsKey(field)) reqJson.addProperty(field.name(), reportData.get(field).toString());
			field = ReportField.CUSTOM_DATA;
			if (reportData.containsKey(field)) reqJson.addProperty(field.name(), reportData.get(field).toString());
			field = ReportField.STACK_TRACE;
			if (reportData.containsKey(field)) reqJson.addProperty(field.name(), reportData.get(field).toString());
			
			
			//Log.d(TAG, ">>> test: " + mContext.getString(R.string.api_base_url));
			
			//-if (AppContext.isDebugMode()) Log.w(TAG, "reqJson: "+reqJson.toString());
			
			// FIXME: you MUST use your own apiKey!!! see docs: https://support.mongolab.com/entries/20433053-REST-API-for-MongoDB 
			MongoLabHelper mongoLabHelper = new MongoLabHelper(mContext, mContext.getString(R.string.mongolab_api_key));
			List<String> documents = new ArrayList<String>();
			documents.add( CommonUtils.getGsonSimple().toJson(reqJson) );
			
			// FIXME: 'param1' is database name, 'param2' is collection name!!!
			mongoLabHelper.createDocuments("dev-playground", "dev-collection", documents
					, new GenericRequestListener() {

				@Override
				public void onBegin() {
				}
				@Override
				public void onError_wMeta(ResponseMeta responseMeta) {
					Log.w(TAG, "createDocuments | " + "onError_wMeta -> " + responseMeta.toString());
				}
				
				@Override
				public void onComplete_wBundle(Bundle responseParams) {
					Log.d(TAG, "createDocuments | " + "onComplete_wBundle -> " + responseParams);
				}
			});
		}
		catch (Exception ex) {
			Log.w(TAG, "while passing ACRA report! ex: "+ex.toString());
		}
	}
}
