package prueba.login;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


public class DetalleParcela extends Activity {
	
	TextView txtNombre, txtLugar, txtMetros, txtPrecio;
	TextView  txtId, txtTipoActual;
	String id, nombre, tipoActual;
	String tipoNueva = "3";
	Button btnBack, btnReservar;
	Bundle b;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_detalle);	
		getActionBar().setDisplayHomeAsUpEnabled(true);
		
		txtId = (TextView) findViewById(R.id.txtId);
		txtTipoActual = (TextView) findViewById(R.id.txtTipoActual);
		
		txtNombre = (TextView) findViewById(R.id.inNombre);
		txtLugar = (TextView) findViewById(R.id.inLugar);
		txtMetros = (TextView) findViewById(R.id.inMetros);
		txtPrecio = (TextView) findViewById(R.id.inPrecio);
		
		btnBack = (Button) findViewById(R.id.btnBack);
		btnReservar = (Button) findViewById(R.id.btnReservar);

//		Bundle bd = getIntent().getExtras();
//		String id = getIntent().getStringExtra("id");
		b	   	   = getIntent().getExtras();
		nombre 	   = b.getString("nombre");
		id 		   = b.getString("id");
		tipoActual = b.getString("tipoActual");
		
		txtNombre.setText(nombre);
		txtId.setText(id);
		txtTipoActual.setText(tipoActual);
//		txtTipoMod.setText("tipoActual");
//		txtNombre.setText(id);
		//tv.setText(bd.getString("id"));
		
//			Toast toast = Toast.makeText(DetalleParcela.this, nombre, Toast.LENGTH_SHORT);
//	        toast.show();

		AsyncReserva ar = new AsyncReserva();
		
		
		
		btnBack.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				startActivity(new Intent(DetalleParcela.this, Home2.class)
                	.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP));
				finish();
			}
		});
		
		btnReservar.setOnClickListener(new View.OnClickListener() {	
			@Override
			public void onClick(View v) {
				if(tipoActual.equals("1")){
					AlertDialog.Builder builder = new AlertDialog.Builder(DetalleParcela.this);
					builder.setTitle("Confirme Reserva");
					builder.setMessage("Esta seguro que desea hacer esta reserva?");
					builder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							//dialog.cancel();
							new AsyncReserva().execute(id,tipoNueva);
						}
					});
					builder.setNegativeButton("Cancelar", null);
					AlertDialog alert = builder.create();
					alert.show();
				}else{
					AlertDialog.Builder b = new AlertDialog.Builder(DetalleParcela.this);
					b.setTitle("Alerta !");
					b.setMessage("Parcela NO DISPONIBLE para reservar.");
					b.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							dialog.cancel();
						}
					});
					AlertDialog a = b.create();
					a.show();
//					Toast toast = Toast.makeText(DetalleParcela.this, txtTipoActual.getText(), Toast.LENGTH_SHORT);
//			        toast.show();
				}
			}
		});
	}
	
	public void llenaDatos() {
		JSONObject obj;
		HttpClient httpClient = new DefaultHttpClient();
		HttpPost cParcela = new HttpPost("http://192.168.1.37/webservice/consultaParcela");
		List<NameValuePair> p = new ArrayList<NameValuePair>();
		p.add(new BasicNameValuePair("id", txtId.getText().toString()));
		try {
			HttpResponse resp = httpClient.execute(cParcela);
			String respStr = EntityUtils.toString(resp.getEntity());
			JSONArray respJSON = new JSONArray(respStr);

		} catch (Exception e) {
			Log.e("LLENAR LOS DATOS", "Error !" + e);
		}
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()){
		case android.R.id.home:
			startActivity(new Intent(DetalleParcela.this, Home2.class)
        		.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP));
			finish();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		Log.d("TAG", "Se destruye DetalleParcela");
	}
	
	public class AsyncReserva extends AsyncTask<String, String, String>{

		String id, tipoMod;
		ProgressDialog pDialog = new ProgressDialog(DetalleParcela.this);
		
		@Override
		protected void onPreExecute(){
			pDialog.setMessage("Procesando...");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(false);
			pDialog.show();
		}
		
		@Override
		protected String doInBackground(String... params) {

			id = params[0];
			tipoMod = params[1]; 
			HttpClient cl = new DefaultHttpClient();
			HttpPost pp = new HttpPost("http://192.168.1.37/webservice/reservaParcela");
			List<NameValuePair> p = new ArrayList<NameValuePair>();
			p.add(new BasicNameValuePair("id", id));
			p.add(new BasicNameValuePair("tipoMod", tipoMod));
			try {
				pp.setEntity(new UrlEncodedFormEntity(p,HTTP.UTF_8));
				HttpResponse resp = cl.execute(pp);
				if(resp.equals("true")){
					//onProgressUpdate("true");
					
				}else{
					
				}
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			} catch (ClientProtocolException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			return null;
		}

/*		@Override
		protected void onProgressUpdate(String... values) {
			super.onProgressUpdate(values);
			
		}
*/		
		@Override
		protected void onPostExecute(String result){
			pDialog.dismiss();
		}
		
		
	}

}
