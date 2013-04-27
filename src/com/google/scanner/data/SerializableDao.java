package com.google.scanner.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.google.scanner.logic.Serializable;

public class SerializableDao extends Serializable {
	DBHelper data;
	SQLiteDatabase db;

	/** CONEXION A LA BASE **/
	public SerializableDao(Context c) {
		// ABRO LA CONEXION CON SQLITE3
		db = (new DBHelper(c)).open();
	}

	/** SETEO DE VARIABLES LUEGO DE UNA CONSULTA **/
	private Serializable rowMapper(Cursor c) {
		// NO EXISTE EL CAMPO, NO CONTINUO !
		if (c.getCount() == 0)
			return new Serializable();

		c.moveToNext();
		Serializable row = new Serializable();
		row.setPlu(c.getString(0));
		row.setCodLoc(c.getString(1));
		row.setSerial(c.getString(2));
		c.close();
		return row;
	}

	/** CONSULTA POR CODIGO **/
	public Serializable findByPrimaryKey(String plu, String loc, String ser) {
		//String where = DBUtil.TSER_CODPLU + "='" + plu + "'";
		String where = "(" + DBUtil.TSER_CODPLU + "='" + plu + "'" + " AND "
				+ DBUtil.TSER_SERIAL + "='" + ser + "'" + ") AND "
				+ DBUtil.TSER_CODLOC + "='" + loc + "'";
		Cursor c = db.query(DBUtil.TBL_SER, DBUtil.TSER_COLS, where, null,
				null, null, null);
		return rowMapper(c);
	}

	/** CONSULTA SIN FILTROS **/
	public Serializable find() {
		Cursor c = db.rawQuery("SELECT * FROM " + DBUtil.TBL_SER, null);
		return rowMapper(c);
	}

	/** CONSULTA SIN FILTROS QUE DEVUELVE CURSOR **/
	public Cursor getDataTo() {
		Cursor c = db.rawQuery("SELECT * FROM " + DBUtil.TBL_SER, null);
		return c;
	}

	/** SETEO DE VALORES PARA AGREGAR Y ACTUALIZAR FILAS A LA TABLA **/
	private ContentValues loadObject(Serializable a) {
		ContentValues c = new ContentValues();
		c.put(DBUtil.TSER_CODPLU,
				(a.getPlu() != null && a.getPlu() != "") ? a.getPlu() : "");
		c.put(DBUtil.TSER_CODLOC,
				(a.getCodLoc() != null && a.getCodLoc() != "") ? a.getCodLoc()
						: "");
		c.put(DBUtil.TSER_SERIAL,
				(a.getSerial() != null && a.getSerial() != "") ? a.getSerial()
						: "");
		return c;
	}

	/** INSERCION DE FILAS EN UNA TABLA **/
	public void insert(Serializable a) {
		db.insert(DBUtil.TBL_SER, null, loadObject(a));
	}

	/**
	 * ACTUALIZACION DE CAMPOS DE UNA FILA EN UNA TABLA CORRESPONDIENTE A UN
	 * CODIGO
	 **/
	public void update(Serializable a) {
		db.update(DBUtil.TBL_SER, loadObject(a),
				DBUtil.TSER_CODPLU + "='" + a.getPlu() + "'", null);
	}

	/** ELIMINACION DE UNA FILA EN UNA TABLA CORRESPONDIENTE A UN CODIGO **/
	public void deleteSerializable(String plu) {
		String where = DBUtil.TSER_CODPLU + "='" + plu + "'";
		db.delete(DBUtil.TBL_SER, where, null);
	}

	/** ELIMINACION DE TODOS LOS REGISTROS DE UNA TABLA **/
	public void DeleteAllSerializable() {
		db.delete(DBUtil.TBL_SER, null, null);
	}

	/** DEVUELVE LA CONEXION A LA BASE **/
	public SQLiteDatabase getDBConnection() {
		return this.db;
	}
}
