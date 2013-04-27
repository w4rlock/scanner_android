package com.google.scanner.view;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import jim.h.common.android.zxinglib.integrator.IntentIntegrator;
import jim.h.common.android.zxinglib.integrator.IntentResult;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;

import com.google.scanner.R;
import com.google.scanner.tools.TextImput;

public class ReadSerial extends ListActivity {
	private Handler handlerRead = new Handler();
	private List<String> item = new ArrayList<String>();
	public Integer intent;
	String result = "";
	ImageButton ibRead, ibSave, ibTextImput;
	String key;
	public int pos=0;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.serial);
		// traer datos del formulario llamador
		ibRead = (ImageButton) findViewById(R.id.ibRead);
		ibSave = (ImageButton) findViewById(R.id.ibSave);
		ibSave = (ImageButton) findViewById(R.id.ibSave);
		ibTextImput = (ImageButton) findViewById(R.id.ibTextImput);

		Bundle extras = getIntent().getExtras();
		if (extras != null)
			key = extras.getString("key");
		alert("A continuación se leeran los nros de serie", "continuar", "terminar");
		// BOTON LEER
		ibRead.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				// TODO Auto-generated method stub
				ScanCode();
			}
		});
		// BOTON GUARDAR
		ibSave.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				// solo si la lista esta completa pasa los parametros
				//if (item.size()>0) exit(); else finish();
				exit();
			}
		});
		// BOTON AGREGAR NUEVO CAMPO
		ibTextImput.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				// TODO Auto-generated method stub
				textImput();
			}
		});

	}

	/** ABRIR ACTIVITY DE INGRESO DE TEXTO **/
	public void textImput() {
		Intent i = new Intent(this, TextImput.class);
		i.putExtra("key", 3);
		startActivityForResult(i, 1);
	}

	/** AGREGO EL SERIAL A LA LISTA **/
	private void setPlu(String plu) {
		if (plu!=null&&plu!="") if (!exist(plu)){
			item.add(plu); 
			updatePlu();
			alert("Escaneo de nros de serie", "continuar", "terminar");
		}
		else alert("El código ya existe", "continuar", "terminar");
	}
	
	/** ACTUALIZO LA LISTA **/
	private void updatePlu() {
		ArrayAdapter<String> pluList = new ArrayAdapter<String>(this,
				R.layout.row, item);
		setListAdapter(pluList);
	}
	
	/** QUITO EL SERIAL A LA LISTA **/
	private void deletePlu() {
		item.remove(pos);
		updatePlu();
	}
	
	/** FUNCION PARA DETERMINAR SI YA FUE ESCANEADO EL CODIGO **/
	public Boolean exist(String plu){
		return item.contains(plu);
	}
	
	/** PREGUNTAMOS QUE HACER **/
	public void alert(final String title, String ok, String cancel) {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMessage(title)
				.setCancelable(false)
				.setPositiveButton(ok,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								// volver a escanear
								if (!title.equals("Eliminar")) ScanCode(); 
								else deletePlu();
							}
						})
				.setNegativeButton(cancel,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								//volver a la lista
								dialog.cancel();
							}
						});
		AlertDialog alert = builder.create();
		alert.show();
	}
	
	/** AL HACER CLICK EN UN ITEM DE LA LISTA **/
	@Override
	protected void onListItemClick(ListView l, View v, final int position,
			long id) {
		pos=position;
		alert("Eliminar", "si", "no");
	}

	
	/** CERRAR ACTIVITY PASANDO PARAMETROS **/
	public void exit() {
		String[] items = convert(item);
		// En la Activity llamada utilizamos esto para devolver el parametro
		Intent resultIntent;
		resultIntent = new Intent();
		resultIntent.putExtra("result", items);
		resultIntent.putExtra("operation", "2");
		setResult(Activity.RESULT_OK, resultIntent);
		finish();
	}

	/** CONVERTIR ARRAY EN STRING[] **/
	static String[] convert(List<String> item2) {
		ArrayList<String> list = new ArrayList<String>();
		for (String strings : item2) {
			Collections.addAll(list, strings);
		}
		return list.toArray(new String[list.size()]);
	}

	// leer codigo de barra
	public void ScanCode() {
		// setear el ultimo parametro para activar luz
		IntentIntegrator.initiateScan(ReadSerial.this, R.layout.capture,
				R.id.viewfinder_view, R.id.preview_view, true);
	}

	/** TRAER PARAMETROS CUANDO VUELVE DE OTRA ACTIVITY **/
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		switch (requestCode) {
		// SCANNER
		case IntentIntegrator.REQUEST_CODE:
			IntentResult scanResult = IntentIntegrator.parseActivityResult(
					requestCode, resultCode, data);
			if (scanResult == null) {
				return;
			}
			final String result = scanResult.getContents();
			if (result != null) {
				handlerRead.post(new Runnable() {
					// @Override
					public void run() {
						//ingresar codigo en la lista
						setPlu(result);
					}
				});
			}
			break;
		// INGRESO MANUAL
		case (1): {
			if (resultCode == Activity.RESULT_OK) {
				if (data.getStringExtra("operation").equals("3"))
					setPlu(data.getStringExtra("result"));
			}
			break;
		}
		default:
		}
	}
}