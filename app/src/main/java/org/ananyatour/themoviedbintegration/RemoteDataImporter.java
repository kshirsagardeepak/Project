package org.ananyatour.themoviedbintegration;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.FrameLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public abstract class RemoteDataImporter extends AsyncTask<Void, Void, Void> {
	String url, loadingMessage="Please wait...";
	Context context;
	String returnString;
	private Context activity;
	private FrameLayout mFrameOverlay;
	boolean processDialog;
	private ProgressDialog pDialog;
	public static final String LOG="RemoteDataImporter";

	public RemoteDataImporter(Context appContext, Context activity) {
		// this.url = url;
		this.context = appContext;
		this.activity = activity;
	}

	public RemoteDataImporter(Context appContext, Activity activity, boolean processDialog, String loadingMessage) {
		// this.url = url;
		this.processDialog = processDialog;
		this.loadingMessage = loadingMessage;
		this.context = appContext;
		this.activity = activity;
		if(processDialog) {
			pDialog = new ProgressDialog(activity);
			pDialog.setMessage(loadingMessage);
			pDialog.setCancelable(false);
			pDialog.show();
		}
	}

	protected abstract FrameLayout getFrameLayout();

	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		mFrameOverlay = getFrameLayout();

		// set a touch listener and consume the event so the ListView
		// doesn't get clicked
		if (mFrameOverlay != null) {
			mFrameOverlay.setOnTouchListener(new OnTouchListener() {

				@Override
				public boolean onTouch(View v, MotionEvent event) {

					return true;
				}
			});
			mFrameOverlay.setVisibility(View.VISIBLE);
		}

	}

	public abstract String getURL();

	protected Void doInBackground(Void... arg0) {
		// Creating service handler class instance

		url = getURL();

		// Making a request to url and getting response
		//Changes done on 4/20 -Amol
//		JSONArray jsonObj = ServiceHandler1.requestWebService(url);
		Object returnObject = ServiceHandler.requestWebServiceForImport(url);


		try {

			//if object is JSONArray
			if(returnObject instanceof JSONArray) {
				JSONArray jsonObj= (JSONArray)returnObject;
				if (jsonObj != null && jsonObj.length() > 0) {
					processJSONArray(jsonObj);
					returnString = "success";
				} else {
					returnString = "null";
					Log.e("ServiceHandler", "Couldn't get any data from the url");
				}
			}else if(returnObject instanceof JSONObject) { //if object is JSONObject
				JSONObject jsonObj= (JSONObject)returnObject;
				if (jsonObj != null && jsonObj.length() > 0) {
					processJSON(jsonObj);
					returnString = "success";
				} else {
					returnString = "null";
					Log.e("ServiceHandler", "Couldn't get any data from the url");
				}
			}
			else
			{//expect response as plain string
				returnString = returnObject.toString();
			}

		}catch (Exception e){//in case response is null or not expected one
			returnString = "ServerException";
			Log.e(LOG, "Exception while importing data",e);
		}
		return null;
	}

	/**
	 * Empty implementation for processing json array, override this class in child object
	 * @param jsonObj
	 * @throws JSONException
	 */
	public void processJSONArray(JSONArray jsonObj) throws JSONException {

	}

	/**
	 * Empty implementation for processing json Object, override this class in child object
	 * @param jsonObj
	 * @throws JSONException
	 */
	public void processJSON(JSONObject jsonObj) throws JSONException {

	}

	@Override
	protected void onPostExecute(Void result) {
		try {
			super.onPostExecute(result);
			// Dismiss the progress dialog
			// if (pDialog.isShowing())
			// pDialog.dismiss();
			if (mFrameOverlay != null) {
				mFrameOverlay.setVisibility(View.GONE);
			}
			processPostExecute();

			if (processDialog && pDialog.isShowing())
				pDialog.dismiss();
		}catch (Exception e){
			//TODO - need popup here

			Log.e("PostExecuteError", "Unexpected error occured while importing data",e);
		}
	}



	public abstract void processPostExecute();
}
