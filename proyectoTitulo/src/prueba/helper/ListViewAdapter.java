package prueba.helper;

import prueba.login.R;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class ListViewAdapter extends BaseAdapter{

	Context context;
	String[] parcelas;
	String[] id;
	int[] imagenes;
	
	LayoutInflater inflater;
	
	public ListViewAdapter(Context context, String[] parcelas, int[] imagenes){
		this.context = context;
		this.parcelas = parcelas;
		this.imagenes = imagenes;
	}
	
	public ListViewAdapter(Context context, String[] parcelas, String[] id, int[] imagenes){
		this.context = context;
		this.parcelas = parcelas;
		this.id = id;
		this.imagenes = imagenes;
	}
	
	@Override
	public int getCount() {
		return parcelas.length;
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		TextView txtTitle;
		ImageView imgImg;
		TextView idParcela;
		
		inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View itemView = inflater.inflate(R.layout.lista_rows, parent, false);
		txtTitle = (TextView) itemView.findViewById(R.id.list_row_title2);
		imgImg = (ImageView) itemView.findViewById(R.id.list_row_img2);
//		idParcela = (TextView) itemView.findViewById(R.id.list_row_id);

		txtTitle.setText(parcelas[position]);
		imgImg.setImageResource(imagenes[position]);
//		idParcela.setText(id[position]);
		
		return itemView;
		
	}

}
