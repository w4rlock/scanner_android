package com.google.scanner.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.google.scanner.logic.Locacion;

public class LocacionDao {
	SQLiteDatabase db;

	/** CONEXION A LA BASE **/
	public LocacionDao(Context c) {
		// ABRO LA CONEXION CON SQLITE3
		db = (new DBHelperClient(c)).open();
	}

	/** SETEO DE VARIABLES LUEGO DE UNA CONSULTA **/
	private Locacion rowMapper(Cursor c) {
		// NO EXISTE LA LOCACION, NO CONTINUO !
		if (c.getCount() == 0)
			return new Locacion();

		Locacion row = new Locacion();
		c.moveToNext();

		row.setCodigo(c.getString(0));
		row.setDescripcion(c.getString(1));
		c.close();

		return row;
	}

	/** CONSULTA POR CODIGO **/
	public Locacion findByPrimaryKey(String plu) {
		String where = DBUtil.TLOC_COD + "= '" + plu + "'";
		Cursor c = db.query(DBUtil.TBL_LOC, DBUtil.TLOC_COLS, where, null,
				null, null, null);
		return rowMapper(c);
	}

	/** CONSULTA SIN FILTROS **/
	public Locacion find() {
		Cursor c = db.rawQuery("SELECT * FROM " + DBUtil.TBL_LOC, null);
		return rowMapper(c);
	}

	/** CONSULTA SIN FILTROS QUE DEVUELVE CURSOR **/
	public Cursor getDataToSpinner() {
		Cursor c = db.rawQuery("SELECT * FROM " + DBUtil.TBL_LOC, null);
		return c;
	}

	/** SETEO DE VALORES PARA AGREGAR Y ACTUALIZAR FILAS A LA TABLA **/
	private ContentValues loadObject(Locacion loc) {
		ContentValues c = new ContentValues();
		c.put(DBUtil.TLOC_COD,
				(loc.getCodigo() != null && loc.getCodigo() != "") ? loc
						.getCodigo() : "");
		c.put(DBUtil.TLOC_DESC, (loc.getDescripcion() != null && loc
				.getDescripcion() != "") ? loc.getDescripcion() : "");

		return c;
	}

	/** INSERCION DE FILAS EN UNA TABLA **/
	public void insert(Locacion loc) {
		db.insert(DBUtil.TBL_LOC, null, loadObject(loc));
	}

	/**
	 * ACTUALIZACION DE CAMPOS DE UNA FILA EN UNA TABLA CORRESPONDIENTE A UN
	 * CODIGO
	 **/
	public void update(Locacion loc) {
		db.update(DBUtil.TBL_LOC, loadObject(loc), DBUtil.TLOC_COD + "= '"
				+ loc.getCodigo() + "'", null);
	}

	/** ELIMINACION DE UNA FILA EN UNA TABLA CORRESPONDIENTE A UN CODIGO **/
	public void DeleteLocacion(String plu) {
		String where = DBUtil.TLOC_COD + "= '" + plu + "'";
		db.delete(DBUtil.TBL_LOC, where, null);
	}
}
