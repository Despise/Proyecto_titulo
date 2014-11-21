package prueba.login;

import java.util.ArrayList;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
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
	HttpHelper post;
	//String IP_Server = "proyectodetitulo.com";
	String URL_CONNECT = "http://192.168.1.37/webservice/login";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		post = new HttpHelper();
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
					failLogin();
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

	public boolean loginStatus(String username, String password) {
		int logstatus = -1;
		ArrayList<NameValuePair> postParametersSend = new ArrayList<NameValuePair>();
		postParametersSend.add(new BasicNameValuePair("usuario", username));
		postParametersSend.add(new BasicNameValuePair("password", password));
		JSONArray jdata = post.getServerData(postParametersSend, URL_CONNECT);
		SystemClock.sleep(950);
		if (jdata != null && jdata.length() > 0) {
			JSONObject json_data;
			try {
				json_data = jdata.getJSONObject(0);
				logstatus = json_data.getInt("logstatus");
				Log.e("loginstatus", "logstatus= " + logstatus);
			} catch (JSONException e) {
				e.printStackTrace();
			}
			if (logstatus == 0) {
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
		String user, pass;
		ProgressDialog pDialog = new ProgressDialog(MainActivity.this);
	
		@Override
		protected void onPreExecute(){
			//ProgressDialog pDialog = new ProgressDialog(MainActivity.this);
			pDialog.setMessage("Conectando...");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(false);
			pDialog.show();
		}
		
		@Override
		protected String doInBackground(String... params) {
			user = params[0];
			pass = params[1];
			String re = (loginStatus(user,pass) == true) ? "Ok" : "err"; 
			return re;
		}
		
		@Override
		protected void onPostExecute(String result){
		//	ProgressDialog pDialog;
			pDialog.dismiss();
			Log.e("onPostExecute= ",""+result);
			if(result.equals("Ok")){
	/*			Intent i = new Intent(MainActivity.this, Home2.class);
				i.putExtra("user", user);
				startActivity(i);
				finish();
	*/
			startActivity(new Intent(getBaseContext(), Home2.class)
                .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP));
            //	.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_REORDER_TO_FRONT));
			finish();
    			
			}else{
				failLogin();
			}
		}

	}

}