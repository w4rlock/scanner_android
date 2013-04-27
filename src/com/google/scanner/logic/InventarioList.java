package com.google.scanner.logic;

public class InventarioList {
	String plu;// Codigo de Producto
	String tipCod;// Tipo codigo
	String codLoc;// Codigo de Locacion
	String cantArt;// Cantidad

	public InventarioList(String plu, String tipCod, String codLoc,
			String cantArt) {
		this.plu = plu;
		this.tipCod = tipCod;
		this.codLoc = codLoc;
		this.cantArt = cantArt;
	}

	public String getPlu() {
		return plu;
	}

	public void setPlu(String plu) {
		this.plu = plu;
	}

	public String getTipCod() {
		return tipCod;
	}

	public void setTipCod(String tipCod) {
		this.tipCod = tipCod;
	}

	public String getCodLoc() {
		return codLoc;
	}

	public void setCodLoc(String codLoc) {
		this.codLoc = codLoc;
	}

	public String getCantArt() {
		return cantArt;
	}

	public void setCantArt(String cantArt) {
		this.cantArt = cantArt;
	}

}
