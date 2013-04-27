package com.google.scanner.view;

import jim.h.common.android.zxinglib.integrator.IntentIntegrator;
import jim.h.common.android.zxinglib.integrator.IntentResult;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.scanner.R;
import com.google.scanner.data.ArticuloDao;
import com.google.scanner.data.InventarioDao;
import com.google.scanner.data.LocacionDao;
import com.google.scanner.data.SerializableDao;
import com.google.scanner.data.SettingDao;
import com.google.scanner.logic.Articulo;
import com.google.scanner.logic.Inventario;
import com.google.scanner.logic.Locacion;
import com.google.scanner.logic.Serializable;
import com.google.scanner.logic.Setting;

public class Read extends Activity {
	// VARIABLES
	private Handler  handler = new Handler();
	Inventario inventario = new Inventario();
	InventarioDao invService;
	Serializable serializable = new Serializable();
	SerializableDao serService;
	Articulo articulo = new Articulo();
	ArticuloDao artService;
	Locacion locacion = new Locacion();
	LocacionDao locService;
	EditText etxCode, etxCount;
	TextView lbHistoryCountValue, tvDescr, tvUnidades_x_pack, tvPrecio;
	private ImageButton btSave, btClear, btUpdate;
	private ImageButton btIncrease, btDecrease, ibRead, ibQuery;
	ImageView ivMessage;
	String lastCode, location;
	Spinner spinner;
	ArrayAdapter<String> adapter;
	String[] items;
	Boolean exist=false;
	Boolean onLoad=true;

	/** INICIO */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.read);

		// OPEN CONNECTIONS TO DATABASES
		invService = new InventarioDao(this);
		artService = new ArticuloDao(this);
		locService = new LocacionDao(this);

		// Asignamos a cada objeto visual creado a su
		// respectivo elemento de main.xml
		tvDescr = (TextView) findViewById(R.id.tvDescr);
		tvUnidades_x_pack = (TextView) findViewById(R.id.tvUnidades_x_pack);
		tvPrecio = (TextView) findViewById(R.id.tvPrecio);
		lbHistoryCountValue = (TextView) findViewById(R.id.tvHistoryCountValue);
		ivMessage = (ImageView) findViewById(R.id.ivMessage);
		ibQuery = (ImageButton) findViewById(R.id.ibQuery);
		ibRead = (ImageButton) findViewById(R.id.ibRead);
		btSave = (ImageButton) findViewById(R.id.bSave);
		btClear = (ImageButton) findViewById(R.id.bClear);
		btUpdate = (ImageButton) findViewById(R.id.bUpdate);
		btIncrease = (ImageButton) findViewById(R.id.bIncrease);
		btDecrease = (ImageButton) findViewById(R.id.bDecrease);
		etxCode = (EditText) findViewById(R.id.etCode);
		etxCount = (EditText) findViewById(R.id.etCount);
		location = "1";
		spinner = (Spinner) findViewById(R.id.spLocation);
		
		loadForm();		

		// Seleccionar Localidad
		spinner.setOnItemSelectedListener(new OnItemSelectedListener() {
			// @Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int position, long id) {
				location = String.valueOf(position + 1);
				if (!onLoad) clear();
			}

			// @Override
			public void onNothingSelected(AdapterView<?> arg0) {
				location = "1";
			}
		});

		// boton escanear
		ibRead.setOnClickListener(new OnClickListener() {
			// @Override
			public void onClick(View v) {
				onLoad=false;
				clear();
			}
		});

		// boton grabar
		btSave.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				save(false);
			}
		});

		// limpiar
		btClear.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
			 clear();
			}
		});

		// reemplazar
		btUpdate.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				save(true);
			}
		});

		// incrementar cantidad
		btIncrease.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				etxCount.setText(String.valueOf(Integer.parseInt(etxCount
						.getText().toString()) + 1));
			}
		});

		// disminuir cantidad
		btDecrease.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				etxCount.setText(String.valueOf(Integer.parseInt(etxCount
						.getText().toString()) - 1));
				if (Integer.parseInt(etxCount.getText().toString()) < 0)
					etxCount.setText("0");
			}
		});

		// boton leer
		ibQuery.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				onLoad=false;
				readCode(etxCode.getText().toString());
			}
		});	
	}

	/** AL CARGAR PANTALLA **/
	public void loadForm() {
		// Creamos el cursor
		Cursor c = locService.getDataToSpinner();
		if (c.getCount() > 0) {
			// Creamos la lista
			items = new String[c.getCount()];
			int i = 0;
			while (c.moveToNext()) {
				items[i] = new String(c.getString(1));
				i++;
			}
		} else {
			items = new String[1];
			items[0] = "Deposito";
		}
		c.close();
		adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, items);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner.setAdapter(adapter);
		setPermission();
		if (!onLoad) clear(); else ScanCode();
	}

	// SETEAR LOS PERMISOS Y MOSTRAT U OCULTAR OBJETOS
	public void setPermission() {
		tvPrecio.setVisibility(View.INVISIBLE);
		try {
			Setting setting = new Setting();
			SettingDao setService;
			setService = new SettingDao(this);
			setting = setService.find();
			if (setting.getPermission() != null
					&& setting.getPermission().equals(1)) {
				tvPrecio.setVisibility(View.VISIBLE);
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	// PROCESAR CODIGO DE BARRAS LEIDO
	public void readCode(String codigoScaneado) {
		inventario = invService.findByPrimaryKey(etxCode.getText()
				.toString(), location);
		if (inventario.exist()) {
			lbHistoryCountValue.setText(inventario.getCantArt().toString());
			exist=true;
		} else {
			exist=false;
			lbHistoryCountValue.setText("0");
		}
		//etxCode.setText(codigoScaneado);
		articulo = artService.findByPrimaryKey(codigoScaneado);
		if (articulo.exist()) {
			if (articulo.getPlu() != null
					&& articulo.getPlu().equals(codigoScaneado)) {// es plu
				ivMessage.setVisibility(View.INVISIBLE);
				if (lastCode != codigoScaneado) {
					tvDescr.setText("Articulo: " + articulo.getDescr());
					tvUnidades_x_pack.setText("Unidades x Pack: "
							+ String.valueOf(articulo.getUnidades_x_pack()));
					tvPrecio.setText("Precio: $"
							+ String.valueOf(articulo.getPrecio()));
				} else {
					ivMessage.setVisibility(View.VISIBLE);
					tvDescr.setText("Codigo Repetido " + articulo.getDescr());
					tvUnidades_x_pack.setText("Unidades x Pack: "
							+ String.valueOf(articulo.getUnidades_x_pack()));
					tvPrecio.setText("Precio: $"
							+ String.valueOf(articulo.getPrecio()));
				}
			} else {// es codpkg
				ivMessage.setVisibility(View.INVISIBLE);
				if (lastCode != codigoScaneado) {
					tvDescr.setText("Articulo: " + articulo.getDescr());
					tvUnidades_x_pack.setText("Unidades x Pack: "
							+ String.valueOf(articulo.getUnidades_x_pack()));
					tvPrecio.setText("Precio: $"
							+ String.valueOf(articulo.getPrecio()));
				} else {
					ivMessage.setVisibility(View.VISIBLE);
					tvDescr.setText("Codigo Repetido " + "Articulo: "
							+ articulo.getDescr());
					tvUnidades_x_pack.setText("Unidades x Pack: "
							+ String.valueOf(articulo.getUnidades_x_pack()));
					tvPrecio.setText("Precio: $"
							+ String.valueOf(articulo.getPrecio()));
				}
			}
		} else {
			ivMessage.setVisibility(View.VISIBLE);
			tvDescr.setText("Codigo No Existente ");
		}
		// ULTIMO CODIGO LEIDO
		lastCode = etxCode.getText().toString();
		// SI ES SERIALIZABLE ABRIR PANTALLA DE CARGA
		if (articulo.isSerializable()) {
			// abrir el explorador de archivos
			Intent i = new Intent(this, ReadSerial.class);
			i.putExtra("key", etxCode.getText().toString());
			startActivityForResult(i, 1);
		}
	}

	// grabar
	public void save(Boolean update) {
		if (!etxCode.getText().toString().equals("")&&!articulo.isSerializable()) {
			Double c1, c2, cant;
			InventarioDao invService = new InventarioDao(this);
			inventario
					.setPlu((articulo.getPlu() != null && articulo.getPlu() != "") ? articulo
							.getPlu() : etxCode.getText().toString());
			inventario.setTipCod(articulo.getCod_pkg());
			inventario.setCodLoc(location);
			c1 = Double.parseDouble(lbHistoryCountValue.getText().toString());
			c2 = Double.parseDouble(etxCount.getText().toString());
			if (articulo.getCod_pkg() != null
					&& articulo.getCod_pkg().equals(
							etxCode.getText().toString()))
				c2 = c2 * articulo.getUnidades_x_pack();
			if (articulo.isFraccionable())
				c2 = c2 * articulo.getFactor_conver();
			if ((c1 != null && c1 != 0) && !update) {
				cant = c1 + c2;
			} else {
				cant = c2;
			}
			inventario.setCantArt(cant);
			//if (c1 == null || c1 == 0) {
			if (!exist) {
				invService.insert(inventario);
			} else {
				invService.update(inventario);
			}
			clear();
		}
	}

	// GRABAR SERIALIZABLES
	public void saveSerial(String[] list) {
		if (list.length > 0) {
			SerializableDao serService = new SerializableDao(this);
			Double c1, c2, cant;
			InventarioDao invService = new InventarioDao(this);
			inventario.setPlu((articulo.getPlu() != null && articulo.getPlu() != "") ? articulo.getPlu() : etxCode.getText().toString());
			inventario.setTipCod(articulo.getCod_pkg());
			inventario.setCodLoc(location);
			c1 = Double.parseDouble(lbHistoryCountValue.getText().toString());
			c2 = (double) (list.length);
			if (articulo.getCod_pkg() != null
					&& articulo.getCod_pkg().equals(
							etxCode.getText().toString()))
				c2 = c2 * articulo.getUnidades_x_pack();
			if (articulo.isFraccionable())
				c2 = c2 * articulo.getFactor_conver();
			if ((c1 != null && c1 != 0)) {
				cant = c1 + c2;
			} else {
				cant = c2;
			}
			inventario.setCantArt(cant);
			//if (c1 == null || c1 == 0) {
			if (!exist) {
				invService.insert(inventario);
				for (int i = 0; i < list.length; i++) {
					serializable.setCodLoc(location);
					serializable.setPlu(etxCode.getText().toString());
					serializable.setSerial(list[i]);
					serService.insert(serializable);
				}
			} else {
				//graba solo los que faltan y actualiza la cantidad 
				c2=(double) 0;
				for (int i = 0; i < list.length; i++) {
					//serializable=new Serializable();
					serializable=serService.findByPrimaryKey(etxCode.getText().toString(), location, list[i]);
					if(!serializable.exist()){
						serializable.setCodLoc(location);
						serializable.setPlu(etxCode.getText().toString());
						serializable.setSerial(list[i]);
						serService.insert(serializable);
						c2++;
					}
				}
				inventario.setCantArt(c1+c2);	
				invService.update(inventario);
			}
		} 
		clear();
	}

	// limpiar pantalla
	public void clear() {
		etxCode.setText("");
		etxCount.setText("1");
		tvDescr.setText("");
		tvUnidades_x_pack.setText("");
		tvPrecio.setText("");
		lbHistoryCountValue.setText("0");
		ivMessage.setVisibility(View.INVISIBLE);
		exist=false;
		// lastCode = "";
		ScanCode();
	}

	// leer codigo de barra
	public void ScanCode() {
		// setear el ultimo parametro para activar luz
		//IntentIntegrator.initiateScan(Read.this, R.layout.capture, R.id.viewfinder_view, R.id.preview_view, true);
		IntentIntegrator.initiateScan(Read.this, R.layout.capture,
                R.id.viewfinder_view, R.id.preview_view, false);
	}

	/** TRAER PARAMETROS CUANDO VUELVE DE OTRA ACTIVITY **/
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case IntentIntegrator.REQUEST_CODE:
                IntentResult scanResult = IntentIntegrator.parseActivityResult(requestCode,
                        resultCode, data);
                if (scanResult == null) {
                    return;
                }
                final String result = scanResult.getContents();
                if (result != null) {
                    handler.post(new Runnable() {
                        //@Override
                        public void run() {
                        	etxCode.setText(result);
    						readCode(result);
                        }
                    });
                }
                break;
		case (1): {
			if (resultCode == Activity.RESULT_OK) {
				if (data.getStringExtra("operation").equals("2"))
					saveSerial(data.getStringArrayExtra("result"));
			}
			break;
		}
		}
	}
}