package com.google.scanner.data;

import android.os.Environment;

public class DBUtil {
	public static final int DB_VERSION = 3;
	public static final String DB_FILE = "productos.db";
	public static final String DB_FILE_IMPORT = "import.db";
	public static final String DB_FULLPATH = Environment.getDataDirectory()
			+ "/data/com.google.scanner/databases/";
	public static final String PATH_SDCARD = "/mnt/sdcard/";

	// TABLAS
	public static final String TBL_ART = "articulos";
	public static final String TBL_LOC = "locaciones";
	public static final String TBL_INV = "inventario";
	public static final String TBL_SER = "serializables";
	public static final String TBL_SET = "setting";

	// TABLA DE ARTICULOS
	public static final String TART_PLU = "PLU";
	public static final String TART_PKGCOD = "cod_pkg";
	public static final String TART_DESCR = "descr";
	public static final String TART_PRICE = "precio";
	public static final String TART_EXIST = "existencia";
	public static final String TART_FRACC = "fraccionable";
	public static final String TART_FACT = "factor_conver";
	public static final String TART_UxPACK = "unidades_x_pack";
	public static final String TART_SERIAL = "serializable";

	// USADOS EN LOS INSERT AND UPDATES
	public static final String[] TART_COLS = { TART_PLU, TART_PKGCOD,
			TART_DESCR, TART_PRICE, TART_EXIST, TART_FRACC, TART_FACT,
			TART_UxPACK, TART_SERIAL };

	public static final String TART_ALLCOLS = TART_PLU + "," + TART_PKGCOD
			+ "," + TART_DESCR + "," + TART_PRICE + "," + TART_EXIST + ","
			+ TART_FRACC + "," + TART_FACT + "," + TART_UxPACK + ","
			+ TART_SERIAL;

	// TABLA DE LOCACIONES
	public static final String TLOC_COD = "codigo";
	public static final String TLOC_DESC = "descr";
	public static final String[] TLOC_COLS = { TLOC_COD, TLOC_DESC };
	public static final String TLOC_ALLCOLS = TLOC_COD + "," + TLOC_DESC;

	// TABLA DE INVENTARIOS
	public static final String TINV_PLU = "PLU";
	public static final String TINV_TCOD = "tip_cod";
	public static final String TINV_LOC = "cod_loc";
	public static final String TINV_CANT = "cantidad";

	// USADOS EN LOS FIND, INSERT AND UPDATES
	public static final String[] TINV_COLS = { TART_PLU, TINV_TCOD, TINV_LOC,
			TINV_CANT };

	// TABLA DE SERIALIZABLES
	public static final String TSER_CODLOC = "cod_loc";
	public static final String TSER_CODPLU = "cod_pro";
	public static final String TSER_SERIAL = "serial";
	public static final String[] TSER_COLS = { TSER_CODLOC, TSER_CODPLU,
			TSER_SERIAL };

	// TABLA DE CONFIGURACION DEL APP
	public static final String TSET_URL = "url";
	public static final String TSET_UPLOAD = "upload";	
	public static final String TSET_PERMISSION = "permission";
	public static final String[] TSET_COLS = { TSET_URL, TSET_UPLOAD, TSET_PERMISSION };
	public static final String TSET_ALLCOLS = TSET_URL + "," + TSET_UPLOAD + "," + TSET_PERMISSION;

}