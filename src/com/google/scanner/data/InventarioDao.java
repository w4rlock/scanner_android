package com.google.scanner.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.google.scanner.logic.Inventario;

public class InventarioDao extends Inventario {
	DBHelper data;
	SQLiteDatabase db;

	/** CONEXION A LA BASE **/
	public InventarioDao(Context c) {
		// ABRO LA CONEXION CON SQLITE3
		db = (new DBHelper(c)).open();
	}

	/** SETEO DE VARIABLES LUEGO DE UNA CONSULTA **/
	private Inventario rowMapper(Cursor c) {
		// NO EXISTE EL CAMPO, NO CONTINUO !
		if (c.getCount() == 0)
			return new Inventario();

		c.moveToNext();
		Inventario row = new Inventario();
		row.setPlu(c.getString(0));
		row.setTipCod(c.getString(1));
		row.setCodLoc(c.getString(2));
		row.setCantArt(c.getDouble(3));
		c.close();
		return row;
	}

	/** CONSULTA POR CODIGO **/
	public Inventario findByPrimaryKey(String id, String location) {
		String where = "(" + DBUtil.TINV_PLU + "='" + id + "'" + " OR "
				+ DBUtil.TINV_TCOD + "='" + id + "'" + ") and "
				+ DBUtil.TINV_LOC + "='" + location + "'";
		Cursor c = db.query(DBUtil.TBL_INV, DBUtil.TINV_COLS, where, null,
				null, null, null);
		return rowMapper(c);
	}

	/** CONSULTA SIN FILTROS **/
	public Inventario find() {
		Cursor c = db.rawQuery("SELECT * FROM " + DBUtil.TBL_INV, null);
		return rowMapper(c);
	}

	/** CONSULTA SIN FILTROS QUE DEVUELVE CURSOR **/
	public Cursor getDataTo() {
		Cursor c = db.rawQuery("SELECT * FROM " + DBUtil.TBL_INV, null);
		return c;
	}

	/** SETEO DE VALORES PARA AGREGAR Y ACTUALIZAR FILAS A LA TABLA **/
	private ContentValues loadObject(Inventario a) {
		ContentValues c = new ContentValues();
		c.put(DBUtil.TINV_PLU,
				(a.getPlu() != null && a.getPlu() != "") ? a.getPlu() : "");
		c.put(DBUtil.TINV_TCOD,
				(a.getTipCod() != null && a.getTipCod() != "") ? a.getTipCod()
						: "");
		c.put(DBUtil.TINV_LOC,
				(a.getCodLoc() != null && a.getCodLoc() != "") ? a.getCodLoc()
						: "");
		c.put(DBUtil.TINV_CANT,
				(a.getCantArt() != null && a.getCantArt() != 0) ? a
						.getCantArt() : 0);
		return c;
	}

	/** INSERCION DE FILAS EN UNA TABLA **/
	public void insert(Inventario a) {
		db.insert(DBUtil.TBL_INV, null, loadObject(a));
	}

	/**
	 * ACTUALIZACION DE CAMPOS DE UNA FILA EN UNA TABLA CORRESPONDIENTE A UN
	 * CODIGO
	 **/
	public void update(Inventario a) {
		String where = "(" + DBUtil.TINV_PLU + "='" + a.getPlu() + "'" + " OR "
				+ DBUtil.TINV_TCOD + "='" + a.getTipCod() + "'" + ") and "
				+ DBUtil.TINV_LOC + "='" + a.getCodLoc() + "'";
		db.update(DBUtil.TBL_INV, loadObject(a), where, null);
	}

	/** ELIMINACION DE UNA FILA EN UNA TABLA CORRESPONDIENTE A UN CODIGO **/
	public void DeleteInventario(String id, String location) {
		String where = "(" + DBUtil.TINV_PLU + "='" + id + "'" + " OR "
				+ DBUtil.TINV_TCOD + "='" + id + "'" + ") and "
				+ DBUtil.TINV_LOC + "='" + location + "'";
		db.delete(DBUtil.TBL_INV, where, null);
	}

	/** ELIMINACION DE TODOS LOS REGISTROS DE UNA TABLA **/
	public void DeleteAllInventario() {
		db.delete(DBUtil.TBL_INV, null, null);
	}

	/** DEVUELVE LA CONEXION A LA BASE **/
	public SQLiteDatabase getDBConnection() {
		return this.db;
	}
}
