package com.google.scanner.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.google.scanner.logic.Setting;

public class SettingDao {
	SQLiteDatabase db;

	/** CONEXION A LA BASE **/
	public SettingDao(Context c) {
		// ABRO LA CONEXION CON SQLITE3
		db = (new DBHelperClient(c)).open();
	}

	/** SETEO DE VARIABLES LUEGO DE UNA CONSULTA **/
	private Setting rowMapper(Cursor c) {
		// NO EXISTE, NO CONTINUO !
		if (c.getCount() == 0)
			return new Setting();
		Setting row = new Setting();
		c.moveToNext();
		row.setUrl(c.getString(0));
		row.setPermission(c.getInt(1));
		row.setUpload(c.getString(2));
		c.close();
		return row;
	}

	/** CONSULTA POR CODIGO **/
	public Setting findByPrimaryKey(String plu) {
		String where = DBUtil.TSET_URL + "= '" + plu + "'";
		Cursor c = db.query(DBUtil.TBL_SET, DBUtil.TSET_COLS, where, null,
				null, null, null);
		return rowMapper(c);
	}

	/** CONSULTA SIN FILTROS **/
	public Setting find() {
		Cursor c = db.rawQuery("SELECT * FROM " + DBUtil.TBL_SET, null);
		return rowMapper(c);
	}

	/** CONSULTA SIN FILTROS QUE DEVUELVE CURSOR **/
	public Cursor getDataTo() {
		Cursor c = db.rawQuery("SELECT * FROM " + DBUtil.TBL_SET, null);
		return c;
	}

	/** SETEO DE VALORES PARA AGREGAR Y ACTUALIZAR FILAS A LA TABLA **/
	private ContentValues loadObject(Setting set) {
		ContentValues c = new ContentValues();
		c.put(DBUtil.TSET_URL, (set.getUrl() != null && set.getUrl() != "") ? set.getUrl() : "");
		c.put(DBUtil.TSET_UPLOAD, (set.getUpload() != null && set.getUpload() != "") ? set.getUpload() : "");
		c.put(DBUtil.TSET_PERMISSION, set.getPermission());
		return c;
	}

	/** CONSULTA SIN FILTROS QUE DEVUELVE CURSOR **/
	public Cursor getDataToSpinner() {
		Cursor c = db.rawQuery("SELECT * FROM " + DBUtil.TBL_SET, null);
		return c;
	}

	/** INSERCION DE FILAS EN UNA TABLA **/
	public void insert(Setting set) {
		db.insert(DBUtil.TBL_SET, null, loadObject(set));
	}

	/**
	 * ACTUALIZACION DE CAMPOS DE UNA FILA EN UNA TABLA CORRESPONDIENTE A UN
	 * CODIGO
	 **/
	public void update(Setting set) {
		db.update(DBUtil.TBL_SET, loadObject(set), DBUtil.TSET_URL + "= '"
				+ set.getUrl() + "'", null);
	}

	/** ELIMINACION DE UNA FILA EN UNA TABLA CORRESPONDIENTE A UN CODIGO **/
	public void DeleteSetting(String id) {
		String where = DBUtil.TSET_URL + "= '" + id + "'";
		db.delete(DBUtil.TBL_SET, where, null);
	}
}