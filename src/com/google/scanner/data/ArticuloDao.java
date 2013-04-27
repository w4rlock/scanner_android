package com.google.scanner.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.google.scanner.logic.Articulo;

public class ArticuloDao extends Articulo {
	SQLiteDatabase db;

	/** CONEXION A LA BASE **/
	public ArticuloDao(Context c) {
		// ABRO LA CONEXION CON SQLITE3
		db = (new DBHelperClient(c)).open();
	}

	/** SETEO DE VARIABLES LUEGO DE UNA CONSULTA **/
	private Articulo rowMapper(Cursor c) {
		// NO EXISTE EL ARTICULO, NO CONTINUO !
		if (c.getCount() == 0)
			return new Articulo();

		c.moveToNext();
		Articulo row = new Articulo();
		row.setPlu(c.getString(0));
		row.setCod_pkg(c.getString(1));
		row.setDescr(c.getString(2));
		row.setPrecio(c.getDouble(3));
		row.setExistencia(c.getShort(4));
		// row.setFraccionable((c.getShort(5) == 1) ? true : false);
		row.setFraccionable(c.getString(5).contains("true")
				|| c.getShort(5) == 1);
		row.setFactor_conver(c.getDouble(6));
		row.setUnidades_x_pack(c.getInt(7));
		// row.setSerializable((c.getShort(8) == 1) ? true : false);
		row.setSerializable(c.getString(8).contains("true")
				|| c.getShort(8) == 1);

		c.close();
		return row;
	}

	/** CONSULTA POR CODIGO **/
	public Articulo findByPrimaryKey(String id) {
		String where = DBUtil.TART_PLU + "='" + id + "'" + " OR "
				+ DBUtil.TART_PKGCOD + "='" + id + "'";
		Cursor c = db.query(DBUtil.TBL_ART, DBUtil.TART_COLS, where, null,
				null, null, null);
		return rowMapper(c);
	}

	/** FUNCIONES INTERNAS DE JAVA PARA COMPARACIONES **/
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((db == null) ? 0 : db.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ArticuloDao other = (ArticuloDao) obj;
		if (db == null) {
			if (other.db != null)
				return false;
		} else if (!db.equals(other.db))
			return false;
		return true;
	}

	/** CONSULTA SIN FILTROS **/
	public Articulo find() {
		Cursor c = db.rawQuery("SELECT * FROM " + DBUtil.TBL_ART, null);
		return rowMapper(c);
	}

	/** CONSULTA SIN FILTROS QUE DEVUELVE CURSOR **/
	public Cursor getDataTo() {
		Cursor c = db.rawQuery("SELECT * FROM " + DBUtil.TBL_ART, null);
		return c;
	}

	/** SETEO DE VALORES PARA AGREGAR Y ACTUALIZAR FILAS A LA TABLA **/
	private ContentValues loadObject(Articulo a) {
		ContentValues c = new ContentValues();
		c.put(DBUtil.TART_PLU,
				(a.getPlu() != null && a.getPlu() != "") ? a.getPlu() : "");
		c.put(DBUtil.TART_PKGCOD,
				(a.getCod_pkg() != null && a.getCod_pkg() != "") ? a
						.getCod_pkg() : "");
		c.put(DBUtil.TART_DESCR,
				(a.getDescr() != null && a.getDescr() != "") ? a.getDescr()
						: "");
		c.put(DBUtil.TART_PRICE, a.getPrecio());
		c.put(DBUtil.TART_EXIST, a.getExistencia());
		c.put(DBUtil.TART_FRACC, (a.isFraccionable()) ? 1 : 0);
		c.put(DBUtil.TART_FACT, a.getFactor_conver());
		c.put(DBUtil.TART_UxPACK, a.getUnidades_x_pack());
		c.put(DBUtil.TART_SERIAL, (a.isSerializable()) ? 1 : 0);
		return c;
	}

	/** INSERCION DE FILAS EN UNA TABLA **/
	public void insert(Articulo a) {
		db.insert(DBUtil.TBL_ART, null, loadObject(a));
	}

	/**
	 * ACTUALIZACION DE CAMPOS DE UNA FILA EN UNA TABLA CORRESPONDIENTE A UN
	 * CODIGO
	 **/
	public void update(Articulo a) {
		db.update(DBUtil.TBL_ART, loadObject(a),
				DBUtil.TART_PLU + "='" + a.getPlu() + "'", null);
	}

	/** ELIMINACION DE UNA FILA EN UNA TABLA CORRESPONDIENTE A UN CODIGO **/
	public void DeleteArticulo(String plu) {
		String where = DBUtil.TART_PLU + "='" + plu + "'";
		db.delete(DBUtil.TBL_ART, where, null);
	}

	/** CREACION DE INDICE PARA UNA TABLA **/
	public void createIndex() {
		db.execSQL("CREATE UNIQUE INDEX idx_articulo ON articulos (PLU,cod_pkg)");

	}
}