package com.google.scanner.tools;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;

import com.google.scanner.R;
import com.google.scanner.data.DBUtil;
import com.google.scanner.data.InventarioDao;
import com.google.scanner.data.SerializableDao;
import com.google.scanner.logic.Inventario;
import com.google.scanner.logic.InventarioList;

public class Query extends Activity {
	Context mContext;
	ImageButton ibDelete;
	Inventario inventario = new Inventario();
	InventarioDao invService;
	InventarioList[] queryList;
	/** AL INICIO **/
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mContext = this;
		invService = new InventarioDao(this);
		Cursor c = invService.getDataTo();
		if (c.getCount() > 0) {
			// Creamos la lista
			queryList = new InventarioList[c.getCount() + 1];
			int i = 0;
			queryList[i] = new InventarioList(DBUtil.TINV_PLU,
					DBUtil.TINV_TCOD, DBUtil.TINV_LOC, DBUtil.TINV_CANT);
			i++;
			while (c.moveToNext()) {
				queryList[i] = new InventarioList((c.getString(0)),
						(c.getString(1)), (c.getString(2)), (c.getString(3)));
				i++;
			}
		} else {
			queryList = new InventarioList[1];
			queryList[0] = new InventarioList(DBUtil.TINV_PLU,
					DBUtil.TINV_TCOD, DBUtil.TINV_LOC, DBUtil.TINV_CANT);
		}
		setContentView(R.layout.query);
		ibDelete = (ImageButton) findViewById(R.id.ibDelete);
		ListView queryListView = (ListView) findViewById(R.id.queryListView);
		LitemItemAdapter mcqListAdapter = new LitemItemAdapter(this,
				R.layout.row_list, queryList);
		queryListView.setAdapter(mcqListAdapter);

		// boton borrar inventario
		ibDelete.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				alert("Esta seguro de borrar el inventario? (no se podra recuperar)");
			}
		});
	}
	/** PREGUNTAMOS SI DESEA BORRAR LA BASE **/
	public void alert(final String title) {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMessage(title)
				.setCancelable(false)
				.setPositiveButton("Aceptar",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								DeleteInventario();
								finish();
							}
						})
				.setNegativeButton("Cancelar",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								dialog.cancel();
							}
						});
		AlertDialog alert = builder.create();
		alert.show();
	}
	/** BORRA EL CONTENIDO DE LA TABLA INVENTARIO Y SERIALIZABLES **/
	public void DeleteInventario() {
		try {
			invService.DeleteAllInventario();
			SerializableDao serService = new SerializableDao(this);
			serService.DeleteAllSerializable();
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
	/** LLENA LA LISTA CON LOS DATOS **/
	class LitemItemAdapter extends ArrayAdapter<InventarioList> {
		
		public LitemItemAdapter(Context context, int textViewResourceId,
				InventarioList[] objects) {
			super(context, textViewResourceId, objects);
		}

		public View getView(int position, View convertView, ViewGroup parent) {
			View v = convertView;
			if (v == null) {

				LayoutInflater vi = (LayoutInflater) mContext
						.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				v = vi.inflate(R.layout.row_list, null);
			}
			InventarioList item = queryList[position];
			if (item != null) {
				ListItemView field1 = (ListItemView) v
						.findViewById(R.id.field1);
				ListItemView field2 = (ListItemView) v
						.findViewById(R.id.field2);
				ListItemView field3 = (ListItemView) v
						.findViewById(R.id.field3);
				ListItemView field4 = (ListItemView) v
						.findViewById(R.id.field4);
				if (position == 0) {
					field1.setHeader(true);
					field2.setHeader(true);
					field3.setHeader(true);
					field4.setHeader(true);
				}
				if (field1 != null) {
					field1.setText(item.getPlu());
				}
				if (field2 != null) {
					field2.setText(item.getTipCod());
				}
				if (field3 != null) {
					field3.setText(item.getCodLoc());
				}
				if (field4 != null) {
					field4.setText(item.getCantArt());
				}
			}
			return v;
		}

	}
}