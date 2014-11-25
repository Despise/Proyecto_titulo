package prueba.login;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.protocol.HttpContext;
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
import android.os.SystemClock;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


public class DetalleParcela extends Activity {
	
	protected TextView txtNombre, txtLugar, txtMetros, txtPrecio;
	TextView  txtId, txtTipoActual;
	String id, tipoActual;
	String tipoNueva = "3";
	Button btnBack, btnReservar;
	Bundle b;
	String[] nomParcela, lugarParcela, mts2Parcela, precioParcela;
	protected String nombreP, lugarP, mts2P, precioP;
	protected String URL_SERVER = "192.168.1.50";
	
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
		b	   	   = getIntent().getExtras();
		id 		   = b.getString("id");
		tipoActual = b.getString("tipoActual");

		txtId.setText(id);
		txtTipoActual.setText(tipoActual);
	
		
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
				}
			}
		});
		AsyncTarea at = new AsyncTarea();
		at.execute();
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
	
	public class AsyncTarea extends AsyncTask<String, String, Void>{

		ProgressDialog p = new ProgressDialog(DetalleParcela.this);
		
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			p.setMessage("Cargando datos...");
			p.setIndeterminate(false);
			p.setCancelable(false);
			p.show();
		}

		@Override
		protected Void doInBackground(String... params) {
			Log.d("llenaDatos()", "Entramos al metodo");
			JSONObject obj;
			HttpClient httpClient = new DefaultHttpClient();
			HttpPost cParcela = new HttpPost("http://"+URL_SERVER+"/webservice/consultaParcela");
			ArrayList<NameValuePair> p = new ArrayList<NameValuePair>();
			p.add(new BasicNameValuePair("id", txtId.getText().toString()));
			try {
				cParcela.setEntity(new UrlEncodedFormEntity(p, HTTP.UTF_8));
			} catch (UnsupportedEncodingException e1) {
				e1.printStackTrace();
			}
			Log.d("llenaDatos()", p.toString());
			try {
				HttpResponse resp = httpClient.execute(cParcela);
				HttpEntity entity = resp.getEntity();
				InputStream respStr = entity.getContent();
				BufferedReader reader = new BufferedReader(new InputStreamReader(respStr,"utf-8"));
				StringBuilder sb = new StringBuilder();
				String line = null;
				while((line = reader.readLine()) != null){
					sb.append(line+"\n");
				}
				respStr.close();
				String result = sb.toString();
				JSONArray respJSON = new JSONArray(result);
				nomParcela = new String[respJSON.length()];
				lugarParcela = new String[respJSON.length()];
//				mts2Parcela = new String[respJSON.length()];
				precioParcela = new String[respJSON.length()];
				//time
				for (int i = 0; i < respJSON.length(); i++) {
					obj = respJSON.getJSONObject(i);		
					nomParcela[i] = obj.getString("nombre");
					lugarParcela[i] = obj.getString("direccion");;
//	    			mtsParcela[i] = mts2P;
					precioParcela[i] = obj.getString("precio");;
				} 
				
			} catch (Exception e) {
				Log.e("LLENAR LOS DATOS", "Error !" + e);
			}
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);
			txtNombre.setText(nomParcela[0]);
			txtLugar.setText(lugarParcela[0]);
//			txtMetros.setText(mtsParcela[i]);		
			txtPrecio.setText(precioParcela[0]);
			SystemClock.sleep(200);
			p.dismiss();
		}
		
		
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
			String v = null;
			id = params[0];
			tipoMod = params[1]; 
			HttpClient cl = new DefaultHttpClient();
			HttpPost pp = new HttpPost("http://"+URL_SERVER+"/webservice/reservaParcela");
			List<NameValuePair> p = new ArrayList<NameValuePair>();
			p.add(new BasicNameValuePair("id", id));
			p.add(new BasicNameValuePair("tipoMod", tipoMod));
			try {
				pp.setEntity(new UrlEncodedFormEntity(p,HTTP.UTF_8));
				HttpResponse resp = cl.execute(pp);
				if(resp.equals("true")){
					v = "ok";
				}else{
					v = "fail";
				}
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			} catch (ClientProtocolException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			SystemClock.sleep(500);
			return v;
		}

/*		@Override
		protected void onProgressUpdate(String... values) {
			super.onProgressUpdate(values);
			
		}
*/		
		@Override
		protected void onPostExecute(String result){
			String ok = result;
			if(result.equals(ok)){
				pDialog.dismiss();
				
				AlertDialog.Builder b = new AlertDialog.Builder(DetalleParcela.this);
				b.setTitle("Exito !!");
				b.setMessage("Reserva exitosa!");
				b.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						startActivity(new Intent(DetalleParcela.this, Home2.class)
		        		.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP));
					finish();	
					}
				});
				//b.setNegativeButton("Cancelar", null);
				AlertDialog alert = b.create();
				alert.show();
				
			}else{
				pDialog.dismiss();
				AlertDialog.Builder b = new AlertDialog.Builder(DetalleParcela.this);
				b.setTitle("Fail !!");
				b.setMessage("La reserva no fue realizada!");
				b.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						startActivity(new Intent(DetalleParcela.this, Home2.class)
		        		.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP));
					finish();
					}
				});
				//b.setNegativeButton("Cancelar", null);
				AlertDialog alert = b.create();
				alert.show();
			}
		}
	}
}
