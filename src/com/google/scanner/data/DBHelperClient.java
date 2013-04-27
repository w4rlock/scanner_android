package com.google.scanner.data;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DBHelperClient extends SQLiteOpenHelper {

	private static final String TAG = "DBHelperClient";
	public String failTitle = "";
	public String failMsg = "";

	/****************************** PUBLIC METHODS ***********************************/
	public DBHelperClient(Context context) {
		super(context, DBUtil.DB_FILE_IMPORT, null, DBUtil.DB_VERSION);
	}

	/** CUANDO HAY CAMBIO DE VERSION DE LA DB **/
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		try {
			// String erase = "DROP TABLE IF EXISTS " + DBUtil.TBL_ART
			// + ";DROP TABLE IF EXISTS " + DBUtil.TBL_LOC + ";";
			// db.execSQL(erase);
			Log.d(TAG, "[DEBUG]: Database erased....................[ OK ]");
		} catch (SQLException e) {
			failTitle = "Error al borrar base de datos";
			failMsg = e.toString();
			e.printStackTrace();
		}
		onCreate(db);
	}

	@Override
	public void onCreate(SQLiteDatabase database) {
		try {
			database.execSQL("CREATE TABLE IF NOT EXISTS " + DBUtil.TBL_ART
					+ " ( " + DBUtil.TART_PLU + " VARCHAR(20),"
					+ DBUtil.TART_PKGCOD + " VARCHAR(20)," + DBUtil.TART_DESCR
					+ " VARCHAR(70)," + DBUtil.TART_PRICE + " DECIMAL(10,3),"
					+ DBUtil.TART_EXIST + " DECIMAL(10,3)," + DBUtil.TART_FRACC
					+ " BOOLEAN, " + DBUtil.TART_FACT + " DECIMAL(10,3),"
					+ DBUtil.TART_UxPACK + " INTEGER, " + DBUtil.TART_SERIAL
					+ " BOOLEAN" + ");");
			database.execSQL("CREATE TABLE IF NOT EXISTS " + DBUtil.TBL_LOC
					+ " ( " + DBUtil.TLOC_COD + " VARCHAR(20), "
					+ DBUtil.TLOC_DESC + " VARCHAR(70)" + ");");
			database.execSQL("CREATE TABLE IF NOT EXISTS " + DBUtil.TBL_SET
					+ " ( " + DBUtil.TSET_URL + " VARCHAR(20), "
					+ DBUtil.TSET_UPLOAD + " VARCHAR(20), "
					+ DBUtil.TSET_PERMISSION + " INTEGER " + ");");
			Log.d(TAG, "[DEBUG]: Database Created...............[ OK ]");
		} catch (SQLException e) {
			failTitle = "Error al borrar base de datos";
			failMsg = e.getMessage();
			Log.e(TAG, "[ERROR]: " + e.getMessage());
		}
	}

	public SQLiteDatabase open() throws SQLException {
		return this.getWritableDatabase();
	}

	public void close() {
		this.close();
	}

}