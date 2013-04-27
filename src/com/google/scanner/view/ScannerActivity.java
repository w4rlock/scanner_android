package com.google.scanner.view;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import com.google.scanner.R;
import com.google.scanner.tools.Query;

public final class ScannerActivity extends Activity {
	private ImageButton bRead, bImport, bExport, bQuery;

	/** AL INICIO DEL ACTIVITY */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		// componentes del layout
		bRead = (ImageButton) findViewById(R.id.ibRead);
		bImport = (ImageButton) findViewById(R.id.ibImport);
		bExport = (ImageButton) findViewById(R.id.ibExport);
		bQuery = (ImageButton) findViewById(R.id.ibQuery);

		// boton leer codigo de barras
		bRead.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				Intent intent = new Intent(ScannerActivity.this, Read.class);
				startActivity(intent);
			}
		});
		// boton importar base de datos
		bImport.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				Intent intent = new Intent(ScannerActivity.this, Import.class);
				startActivity(intent);
			}
		});
		// boton exportar base de datos
		bExport.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				Intent intent = new Intent(ScannerActivity.this, Export.class);
				startActivity(intent);
			}
		});
		// INVENTARIO
		bQuery.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				Intent intent = new Intent(ScannerActivity.this, Query.class);
				startActivity(intent);
			}
		});

	}
}