package prueba.login;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import prueba.helper.EncriptarMD5;
import prueba.helper.HttpHelper;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.SystemClock;
import android.os.Vibrator;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends Activity {

	EditText user;
	EditText pass;
	Button bLogin;
	HttpHelper post = new HttpHelper();
	//String IP_Server = "proyectodetitulo.com";
	protected String URL_CONNECT = "http://192.168.1.36/webservice/login";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
//		HttpHelper post = new HttpHelper();
		user = (EditText) findViewById(R.id.txtUser);
		pass = (EditText) findViewById(R.id.txtPass);
		bLogin = (Button) findViewById(R.id.acceder);

		bLogin.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {	
				String usuario = user.getText().toString();
				String passw = pass.getText().toString();
				EncriptarMD5 emd5 = new EncriptarMD5();
				passw = emd5.encriptaEnMD5(passw);

				if (checkDatos(usuario, passw) == true) {
					new AsyncLogin().execute(usuario, passw);
				} else {
					failVacio();
				}
			}
		});
	}
	
	

	@Override
	protected void onPause() {
		super.onPause();
		Log.d("TAG", "Estamos en onPause");
		
	}
	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		Log.d("TAG", "Estamos en onStop");
	}
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		Log.d("TAG", "Estamos en onDestroy");
	}


	public void failLogin() {
		Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
		v.vibrate(200);
		Toast tt = Toast.makeText(getApplicationContext(),
				"Credenciales invalidas !", Toast.LENGTH_SHORT);
		tt.show();
	}
	
	public void failVacio() {
		Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
		v.vibrate(200);
		Toast tt = Toast.makeText(getApplicationContext(),
				"Ingrese USER y PASS !", Toast.LENGTH_SHORT);
		tt.show();
	}

	public boolean loginStatus(String username, String password) throws JSONException {
//		int logstatus = -1;
//		String logstatusStr;
		String logstatus = "";
		ArrayList<NameValuePair> postParametersSend = new ArrayList<NameValuePair>();
		postParametersSend.add(new BasicNameValuePair("usuario", username));
		postParametersSend.add(new BasicNameValuePair("password", password));
		JSONArray jdata = post.getServerData(postParametersSend, URL_CONNECT);
		Log.e("Jsonarray", jdata.toString());
		SystemClock.sleep(500);
		if (jdata != null && jdata.length() > 0) {
			try {
				Log.d("Antesde ", "adawd");
				JSONObject json_data = jdata.getJSONObject(0);
//				logstatus = json_data.getInt("logstatus");
				logstatus = json_data.getString("logstatus");
//				logstatusStr = json_data.getString("logstatus");
//				logstatus = Integer.parseInt(logstatusStr);
				Log.e("loginstatus_Parte json", "logstatus= " + logstatus);
			} catch (JSONException e) {
				e.printStackTrace();
			}
		//	if (logstatus == 0) {
			if (logstatus.equals("0")) {
				Log.e("loginstatus", "Invalido");
				return false;
			} else {
				Log.e("loginstatus", "Valido");
				return true;
			}

		} else {
			Log.e("JSON", "ERROR");
			return false;
		}
	}

	public boolean checkDatos(String usuario, String passw) {
		if (usuario.equals("") || passw.equals("")) {
			Log.e("Login", "checkData user o pass error");
			return false;
		} else {
			return true;
		}
	}

	class AsyncLogin extends AsyncTask<String, String, String> {
		String useR, pasS;
		ProgressDialog pDialog = new ProgressDialog(MainActivity.this);

		@Override
		protected void onPreExecute() {
			pDialog.setMessage("Conectando...");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(false);
			pDialog.show();
		}

		@Override
		protected String doInBackground(String... params) {

			String prueba = "\"no\"";
			String algo="";
			
			///////
			
			try {
				HttpClient httpclient = new DefaultHttpClient();
				HttpPost httppost = new HttpPost(URL_CONNECT);
				List<NameValuePair> p = new ArrayList<NameValuePair>();
				p.add(new BasicNameValuePair("username", params[0]));
				p.add(new BasicNameValuePair("password", params[1]));
				httppost.setEntity(new UrlEncodedFormEntity(p));
				
				HttpResponse r = httpclient.execute(httppost);
				HttpEntity ent = r.getEntity();
				
				algo = EntityUtils.toString(ent);
				
			
			} catch (Exception e) {
				Log.e("ERROR", "No se k mierda");
			}
			if (algo.equals(prueba)) {
				Log.e("loginstatus", "Valido "+ algo);
				return "Err";
			} else {
				Log.e("loginstatus", "Invalido "+ algo);
				return "Ok";
			}
		}

		@Override
		protected void onPostExecute(String result) {
			// ProgressDialog pDialog;
			String o = "Ok";
			pDialog.dismiss();
			Log.e("onPostExecute= ", result);
			if (result.equals(o)) {
				startActivity(new Intent(MainActivity.this, Home2.class)
						.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
								| Intent.FLAG_ACTIVITY_SINGLE_TOP));
				finish();
			} else {
				failLogin();
			}
		}

	}
}