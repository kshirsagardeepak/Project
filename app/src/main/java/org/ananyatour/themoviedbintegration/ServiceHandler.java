package org.ananyatour.themoviedbintegration;

import android.os.Build;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.util.Iterator;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

public class ServiceHandler {

    static String response = null;
    public final static int GET = 1;
    public final static int POST = 2;

    public ServiceHandler() {
 
    }
    
    @SuppressWarnings("resource")
	static String convertStreamToString(InputStream is) {
        Scanner s = new Scanner(is).useDelimiter("\\A");
        return s.hasNext() ? s.next() : "";
    }
 
    
    public static String postRequest(String strURL, Map<String,String> dataMap) throws IOException, JSONException {
    	URL url = new URL(strURL);
    	  
	  HttpURLConnection urlConn = (HttpURLConnection) url.openConnection();
  //  	DataOutputStream printout;
   // 	DataInputStream  input;
    	urlConn.setDoInput (true);
//    	urlConn.setDoOutput (true);
    	urlConn.setUseCaches (false);
//    	urlConn.setRequestProperty("Content-Type","application/json");   
//    	urlConn.setRequestProperty("Host", "android.schoolportal.gr");
//    	urlConn.setRequestMethod( "POST" );
    	urlConn.connect();  
    	
    	//Create JSONObject here
//    	JSONObject jsonParam = new JSONObject(dataMap);
//    	Set<String> keySet = dataMap.keySet();
//    	String message = new JSONObject().toString();
//    	Iterator<String> strIterator = keySet.iterator();
    	
//    	while(strIterator.hasNext()){
//    		String key = strIterator.next();
//    		jsonParam.put(name, value)(key)
//    	}
    	
    	
//    	printout = new DataOutputStream(urlConn.getOutputStream ());
//    	printout.writeBytes(URLEncoder.encode(jsonParam.toString(),"UTF-8"));
//    	printout.flush ();
//    	printout.close (); 
    	
    	StringBuilder sb = new StringBuilder();
    	int HttpResult =((HttpURLConnection) urlConn).getResponseCode();
        if(HttpResult == HttpURLConnection.HTTP_OK){
            BufferedReader br = new BufferedReader(new InputStreamReader(
            		urlConn.getInputStream()));  
            String line = null;
            while ((line = br.readLine()) != null) {  
                sb.append(line);  
                
            }  

          
            br.close();
          
          return  sb.toString();
            
        }else{
            	
            	Log.e("erroramol",convertStreamToString(((HttpURLConnection) urlConn).getErrorStream()));
            	System.out.println(convertStreamToString(((HttpURLConnection) urlConn).getErrorStream()));
            	System.err.println(convertStreamToString(((HttpURLConnection) urlConn).getErrorStream()));
            	
            	if (HttpResult >= 400 && HttpResult <= 499) {
            	    Log.e("error","Bad authentication status: " + HttpResult); //provide a more meaningful exception message
            	}
            	else {
//            	    InputStream in = con.getInputStream();
//            	    //etc...
            	}
//            	BufferedReader br = new BufferedReader(new InputStreamReader(  
//                		urlConn.getInputStream(),"utf-8"));  
//                String line = null;  
//                while ((line = br.readLine()) != null) {  
//                    sb.append(line + "\n");  
//                    
//                }  
            //	Log.d("Error Response from webservice","error");
//            	br.close();\
         	 return  "Error";
            }
		
        
    	
    }
    
    /**
     * Post data using map
     * @param strURL
     * @param dataMap
     * @return
     * @throws IOException
     * @throws JSONException
     */
    @SuppressWarnings("deprecation")
	public static String postRequestByPost(String strURL, Map<String,String> dataMap) throws IOException, JSONException {
    	URL url = new URL(strURL);
    	  try{
	  HttpURLConnection urlConn = (HttpURLConnection) url.openConnection();
    	DataOutputStream printout;
    	DataInputStream input;
    	urlConn.setDoInput (true);
    	urlConn.setDoOutput (true);
    	urlConn.setUseCaches (false);
    	urlConn.setRequestProperty("Content-Type","application/json");   
    	urlConn.setConnectTimeout(10000);
    	urlConn.connect();  
    	
    	//Create JSONObject here
    	JSONObject jsonParam = new JSONObject(dataMap);
    	Set<String> keySet = dataMap.keySet();
    	String message = new JSONObject().toString();
    	Iterator<String> strIterator = keySet.iterator();
    	
    	
    	System.out.println("Amol"+jsonParam.toString());
    	printout = new DataOutputStream(urlConn.getOutputStream ());
    	printout.writeBytes(jsonParam.toString());
    	printout.flush ();
    	printout.close (); 
    	
    	StringBuilder sb = new StringBuilder();
    	int HttpResult =((HttpURLConnection) urlConn).getResponseCode();
        if(HttpResult == HttpURLConnection.HTTP_OK){
            BufferedReader br = new BufferedReader(new InputStreamReader(
            		urlConn.getInputStream()));  
            String line = null;
            while ((line = br.readLine()) != null) {  
                sb.append(line);  
                
            }  
            //Log.d("Response from webservice", sb.toString());
          
            br.close();
          
          return  sb.toString();
            
        }else{
            	String errorString = convertStreamToString(((HttpURLConnection) urlConn).getErrorStream());
//            	Log.d("erroramol",convertStreamToString(((HttpURLConnection) urlConn).getErrorStream()));
//            	writeToFile(errorString);
            	System.out.println(errorString);
            	
            	
            	if (HttpResult >= 400 && HttpResult <= 499) {
            	    Log.e("error","Bad authentication status: " + HttpResult); //provide a more meaningful exception message
            	}
            	else {
//            	    InputStream in = con.getInputStream();
//            	    //etc...
            	}
//            	BufferedReader br = new BufferedReader(new InputStreamReader(  
//                		urlConn.getInputStream(),"utf-8"));  
//                String line = null;  
//                while ((line = br.readLine()) != null) {  
//                    sb.append(line + "\n");  
//                    
//                }  
            	//Log.d("Error Response from webservice","error");
//            	br.close();\
         	 return  "Error";
            }
    	  }catch(SocketTimeoutException e){
    		
    		  return  "Error";
    	  }

    }
    
    public static void writeToFile(String text){
    	System.out.println(text);
    	  File logFile = new File("sdcard/log.file");
    	   if (!logFile.exists())
    	   {
    	      try
    	      {
    	         logFile.createNewFile();
    	      } 
    	      catch (IOException e)
    	      {
    	         // TODO Auto-generated catch block
    	         e.printStackTrace();
    	      }
    	   }

    	PrintWriter writer=null;
		try {
			writer = new PrintWriter(logFile);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	writer.println(text);
    	writer.close();
    }

	/**
	 * Function to import data, URL will be passed as string and result can be of any object
	 * Changes done, duplicate function added below 4/20 - AMol
	 * @param serviceUrl
	 * @return
	 */
public static JSONArray requestWebService(String serviceUrl) {
    disableConnectionReuseIfNecessary();
 
    HttpURLConnection urlConnection = null;
    try {
        // create connection
        URL urlToRequest = new URL(serviceUrl);
        urlConnection = (HttpURLConnection)
            urlToRequest.openConnection();
        urlConnection.setConnectTimeout(1000000);
        urlConnection.setReadTimeout(10000000);
         
        // handle issues
        int statusCode = urlConnection.getResponseCode();
        if (statusCode == HttpURLConnection.HTTP_UNAUTHORIZED) {
        	Log.d("ERROR","unauthorized issuej");
        } else if (statusCode != HttpURLConnection.HTTP_OK) {
        	Log.d("ERROR","response ok");
        }
         
        // create JSON object from content
        InputStream in = new BufferedInputStream(
            urlConnection.getInputStream());

        return new JSONArray(getResponseText(in));
         
    } catch (MalformedURLException e) {
    	Log.d("ERROR","mAL",e);
    } catch (SocketTimeoutException e) {
    	Log.d("ERROR","timeout",e);
    } catch (IOException e) {
    	Log.d("ERROR","io issuej",e);
    } catch (JSONException e) {
    	Log.e("ERROR","json",e);
    } finally {
        if (urlConnection != null) {
            urlConnection.disconnect();
        }
    }       
     
    return null;
}

	/**
	 * Function to import data, URL will be passed as string and result can be of any object
	 * Changes done, new function added on 4/20 - AMol
	 * @param serviceUrl
	 * @return Object representing String, JSONObject, JSONArray
	 */
	public static Object requestWebServiceForImport(String serviceUrl) {
		disableConnectionReuseIfNecessary();
	Object returnObject = null;
		HttpURLConnection urlConnection = null;
		try {
			// create connection
			URL urlToRequest = new URL(serviceUrl);
			urlConnection = (HttpURLConnection)
					urlToRequest.openConnection();
			urlConnection.setConnectTimeout(1000000);
			urlConnection.setReadTimeout(10000000);

			// handle issues
			int statusCode = urlConnection.getResponseCode();
			if (statusCode == HttpURLConnection.HTTP_UNAUTHORIZED) {
				Log.d("ERROR","unauthorized issuej");
			} else if (statusCode != HttpURLConnection.HTTP_OK) {
				Log.d("ERROR","response ok");
			}

			// create JSON object from content
			InputStream in = new BufferedInputStream(
					urlConnection.getInputStream());
		String res = getResponseText(in);
			try{

				returnObject = new JSONArray(res);
		}catch(JSONException w){
				try{
					returnObject = new JSONObject(res);

			}catch(Exception e){
					returnObject = res;
				}
			}
		

		} catch (MalformedURLException e) {
			Log.d("ERROR","MalformedURLException in requestWebServiceForImport",e);
		} catch (SocketTimeoutException e) {
			Log.d("ERROR","Time  in requestWebServiceForImport",e);
		} catch (IOException e) {
			Log.d("ERROR","IOException  in requestWebServiceForImport",e);
		} catch (Exception e) {
			Log.e("ERROR","General exception  in requestWebServiceForImport",e);
		} finally {
			if (urlConnection != null) {
				urlConnection.disconnect();
			}
		}

		return returnObject;
	}

	/*
	Return single json object
	 */
	public static JSONObject requestWebServiceResultJSON(String serviceUrl) {
		disableConnectionReuseIfNecessary();

		HttpURLConnection urlConnection = null;
		try {
			// create connection
			URL urlToRequest = new URL(serviceUrl);
			urlConnection = (HttpURLConnection)
					urlToRequest.openConnection();
			urlConnection.setConnectTimeout(1000000);
			urlConnection.setReadTimeout(10000000);

			// handle issues
			int statusCode = urlConnection.getResponseCode();
			if (statusCode == HttpURLConnection.HTTP_UNAUTHORIZED) {
				Log.d("ERROR","unauthorized issuej");
			} else if (statusCode != HttpURLConnection.HTTP_OK) {
				Log.d("ERROR","response ok");
			}

			// create JSON object from content
			InputStream in = new BufferedInputStream(
					urlConnection.getInputStream());

			return new JSONObject(getResponseText(in));

		} catch (MalformedURLException e) {
			Log.d("ERROR","mAL",e);
		} catch (SocketTimeoutException e) {
			Log.d("ERROR","timeout",e);
		} catch (IOException e) {
			Log.d("ERROR","io issuej",e);
		} catch (JSONException e) {
			Log.e("ERROR","json",e);
		} finally {
			if (urlConnection != null) {
				urlConnection.disconnect();
			}
		}

		return null;
	}

	/**
 * required in order to prevent issues in earlier Android version.
 */
@SuppressWarnings("deprecation")
private static void disableConnectionReuseIfNecessary() {
    // see HttpURLConnection API doc
    if (Integer.parseInt(Build.VERSION.SDK)
            < Build.VERSION_CODES.FROYO) {
        System.setProperty("http.keepAlive", "false");
    }
}
 
@SuppressWarnings("resource")
private static String getResponseText(InputStream inStream) {
    // very nice trick from 
    // http://weblogs.java.net/blog/pat/archive/2004/10/stupid_scanner_1.html
    return new Scanner(inStream).useDelimiter("\\A").next();
}

//public List<MyItem> findAllItems() {
//    JSONObject serviceResult = ServiceHandler.requestWebService(
//        "http://url/to/findAllService");
//     
//    List<MyItem> foundItems = new ArrayList<MyItem>(20);
//     
//    try {
//        JSONArray items = serviceResult.getJSONArray("items");
//         
//        for (int i = 0; i < items.length(); i++) {
//            JSONObject obj = items.getJSONObject(i);
//            foundItems.add(
//                    new Item(obj.getInt("id"), 
//                        obj.getString("name"), 
//                        obj.getBoolean("active")));
//        }
//         
//    } catch (JSONException e) {
//        // handle exception
//    }
//     
//    return foundItems;
//}

}