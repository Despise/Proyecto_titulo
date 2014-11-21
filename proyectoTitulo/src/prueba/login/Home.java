package prueba.login;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONObject;

import clases.parcela;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class Home extends Activity {

	static ListView lv;
	ArrayList<parcela> lista = new ArrayList<parcela>();
	static parcela par;
	int[] imagenes = {R.drawable.disponible, R.drawable.reservada, R.drawable.vendida};
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home);
		
		lv = (ListView)findViewById(R.id.listaParcelas);
		
		Tarea1 t = new Tarea1();
//		t.cargarContenido(getApplicationContext());
		t.cargarContenido(getBaseContext());
		t.execute(lv);
		
		lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int i, long l) {
			Intent intent = new Intent(Home.this, DetalleParcela.class);
				intent.putExtra("id", i);
//				Log.e("EL id: ", par.getId());
				startActivity(intent);
				
			}
			
		}); 

	}

	
	@Override
	public boolean onCreateOptionsMenu(Menu menu){
		//MenuInflater infM = getMenuInflater();
		//infM.inflate(R.menu.menu_principal, menu);
		getMenuInflater().inflate(R.menu.menu_principal, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	//	switch(item.getItemId()){
	//		case R.id.:
				
	//			break;
	//		case :
				
	//			break;
	//	}
		return super.onOptionsItemSelected(item);
	}

	static class Tarea1 extends AsyncTask<ListView, Void, ArrayAdapter<parcela>>{

		Context contexto;
	    ListView list;
		InputStream is;
		ArrayList<parcela> listaParcelas = new ArrayList<parcela>();
		
		//aca digo donde voy a cargar los datos o algo asi 
		public void cargarContenido(Context contexto){
			this.contexto = contexto;
		}
		
		 
		
		@Override
		protected ArrayAdapter<parcela> doInBackground(ListView... params) {
			
			//list = params[0];
			lv = params[0];
			String resultado = "";
			//parcela par;
			
			//la conexion
			HttpClient cliente = new DefaultHttpClient();
			HttpPost peticionPost = new HttpPost("http://10.31.48.143/webservice/llenaListView");
			try {
				HttpResponse response = cliente.execute(peticionPost);
				HttpEntity contenido = response.getEntity();
				is = contenido.getContent();
//			} catch (ClientProtocolException e) {
//				e.printStackTrace();
			} catch (IOException e){
				e.printStackTrace();
			}
			
			BufferedReader buferlector = new BufferedReader(new InputStreamReader(is));
			StringBuilder sb = new StringBuilder();
			String linea = null;
			try {
				while((linea = buferlector.readLine()) != null){
					sb.append(linea+"\n");
				}
				is.close();
				resultado = sb.toString();
			} catch (IOException e) {
				e.printStackTrace();
			}
			
//			try{
//				is.close();
//			} catch (IOException e){
//				e.printStackTrace();
//			}
//			resultado = sb.toString();
			
			try {
				JSONArray arrayJson = new JSONArray(resultado);
				for (int i = 0; i < arrayJson.length(); i++) {
					JSONObject objJson = arrayJson.getJSONObject(i);
					par = new parcela(objJson.getString("id"), objJson.getString("nombre"), objJson.getString("status"));
					listaParcelas.add(par);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			ArrayAdapter<parcela> adaptador = new ArrayAdapter<parcela>(contexto, android.R.layout.simple_selectable_list_item, listaParcelas);

			return adaptador;
		}
		
		@Override
		protected void onPostExecute(ArrayAdapter<parcela> result){
			lv.setAdapter(result);
			
		}
		
	}
	
}
