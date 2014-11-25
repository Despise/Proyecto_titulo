package prueba.helper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;

import android.util.Log;

public class HttpHelper {

	InputStream is = null;
    String resultado = "";
    String algo="";

	public JSONArray getServerData(ArrayList<NameValuePair> parameters, String urlwebserver) throws JSONException {
		httpPostConnect(parameters, urlwebserver);
		if (is != null) {
			try{
			BufferedReader reader = new BufferedReader(new InputStreamReader(is));
			StringBuilder sb = new StringBuilder();
			String line = null;
			while ((line = reader.readLine()) != null) {
				sb.append(line + "\n");
			}
			is.close();
			resultado = sb.toString();
			Log.e("getPostResponse", "resultado= " + sb.toString());
			}catch(IOException e){
				
			}
			
			
//			getPostResponse();
			Log.d("getServerData", "hola");
//			return getJsonArray();
			JSONArray jsonA = new JSONArray(resultado);
//			JSONArray jsonA = new JSONArray(getPostResponse());
			return jsonA;
		} else {
			return null;
		}
	}

	private void httpPostConnect(ArrayList<NameValuePair> parametros, String urlwebserver) {
		try {
			HttpClient httpclient = new DefaultHttpClient();
			HttpPost httppost = new HttpPost(urlwebserver);
			httppost.setEntity(new UrlEncodedFormEntity(parametros));
			HttpResponse response = httpclient.execute(httppost);
			HttpEntity entity = response.getEntity();
			is = entity.getContent();

		} catch (Exception e) {
			Log.e("Error Conex", "Error en la conexion " + e.toString());
		}
	}

	/*
	public void getPostResponse() {
		try {
//			BufferedReader reader = new BufferedReader(new InputStreamReader(is, "iso-8859-1"), 8);
//			BufferedReader reader = new BufferedReader(new InputStreamReader(is, "utf-8"), 8);
			BufferedReader reader = new BufferedReader(new InputStreamReader(is));
			StringBuilder sb = new StringBuilder();
			String line = null;
			while ((line = reader.readLine()) != null) {
				sb.append(line + "\n");
			}
			is.close();
			resultado = sb.toString();
			Log.e("getPostResponse", "resultado= " + sb.toString());
		} catch (Exception e) {
			Log.e("Error", "Error al leer result " + e.toString());
		}

	}
*/
/*
	public JSONArray getJsonArray() {
		try {
			JSONArray jsonA = new JSONArray(resultado);
			return jsonA;
		} catch (JSONException e) {
			Log.e("log_tag", "Error parsing data " + e.toString());
			return null;
		}
	}
*/
}
