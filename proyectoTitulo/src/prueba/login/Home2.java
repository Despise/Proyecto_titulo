package prueba.login;

import java.util.ArrayList;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.DefaultClientConnection;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import clases.parcela;

import prueba.helper.ListViewAdapter;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class Home2 extends Activity{

//	ListView lv;
//	ArrayList<String> lista = new ArrayList<String>();
	
	ListView listView;
	
	String[] parcelas;
	String nombre;
	parcela p;
	
	String[] estado;
	String statusDeParcela;
	
	String[] id;
	String idDeParcela;
	
	
	int[] imgBase = {R.drawable.disponible, R.drawable.vendida, R.drawable.reservada};
	int[] imgBase2 = {R.drawable.reserva_green, R.drawable.reserva_red, R.drawable.reserva_yellow};
	int[] imgDelList;
	
	ListViewAdapter adapter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home2);
		Mostrar t = new Mostrar();
		t.execute();
	
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
		switch(item.getItemId()){
			case R.id.refresh:
				Mostrar x = new Mostrar();
				x.execute();
				break;
			case R.id.Cerrar:
				finish();
			default:
				break;
		}
		return super.onOptionsItemSelected(item);
	}
	
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		Log.d("TAG", "Se destruye Home2");
	}
	
	private class Mostrar extends AsyncTask<String, Integer, Boolean>{
		
		JSONObject obj;
		ProgressDialog pDialog = new ProgressDialog(Home2.this);
		
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pDialog.setMessage("Cargando datos...");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(false);
			pDialog.show();
		}
		@Override
		protected Boolean doInBackground(String... params){
			boolean result = true;
			HttpClient httpClient = new DefaultHttpClient();
			HttpPost del = new HttpPost("http://192.168.1.36/webservice/llenaListView");
			
	        try{                      
	        	HttpResponse resp = httpClient.execute(del);
	        	String respStr = EntityUtils.toString(resp.getEntity());
	        	JSONArray respJSON = new JSONArray(respStr);
	        	parcelas = new String[respJSON.length()];
	        	id = new String[respJSON.length()];
	        	estado = new String[respJSON.length()];
	        	imgDelList = new int[respJSON.length()];
	        	SystemClock.sleep(200);
	        	for(int i=0; i<respJSON.length(); i++){
	        		obj = respJSON.getJSONObject(i);
	        		
	        		nombre = obj.getString("nombre");
	        		statusDeParcela = obj.getString("status");
	        		idDeParcela = obj.getString("id");
	        		
	        		parcelas[i] = nombre;
	        		estado[i] 	= statusDeParcela;
	        		id[i] 		= idDeParcela;
	        		
	        		//1 disponible
	        		//2 vendida
	        		//3 reservada
	        		if(statusDeParcela.equals("1"))imgDelList[i] = imgBase2[0];    		
	        		if(statusDeParcela.equals("2"))imgDelList[i] = imgBase2[1];
	        		if(statusDeParcela.equals("3"))imgDelList[i] = imgBase2[2];      		
	        	}
	        } catch (Exception ex){
	        	Log.e("ServicioRest","Error!", ex);
	        	result = false;
	        }

	        return result;
		}
		@Override
		protected void onPostExecute(Boolean result) {
			//Rellenamos la lista con los nombres de las parcelas
	        //Rellenamos la lista con los resultados
	    //    ArrayAdapter<String> adaptador =  new ArrayAdapter<String>(Home2.this, android.R.layout.simple_list_item_1, parcelas);
			
			adapter = new ListViewAdapter(Home2.this, parcelas, id, imgDelList);
			
//			adapter = new ListViewAdapter(Home2.this, parcelas, imgDelList);

			
	        listView = (ListView) findViewById(R.id.listaParcelas2);    
	        listView.setAdapter(adapter);
	    //  listView.setAdapter(adaptador);
	        
	        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> adapterView, View view, int posicion, long l) {
					Intent intent = new Intent(Home2.this, DetalleParcela.class);
					intent.putExtra("nombre", parcelas[posicion]);
					intent.putExtra("id", id[posicion]);
					intent.putExtra("tipoActual", estado[posicion]);
					startActivity(intent);
					finish();
//					Toast toast = Toast.makeText(Home2.this, posicion, Toast.LENGTH_LONG);
//			        toast.show();
				}
			
	        });
	        pDialog.dismiss();
	   }
		
	}
}
