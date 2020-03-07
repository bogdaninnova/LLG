package main.fields;

import main.Vector;

public class Anisotropy extends Field {

	private Vector axeOfAnisotropy;
	
	public Anisotropy(double tetta, double fi) {
		setAxe(new Vector(tetta, fi));
	}
	
	public Vector getValue(Vector M, double t) {
		return axeOfAnisotropy.multiply(M.dotProduct(axeOfAnisotropy));
	}

	public Double getW() {
		return null;
	}
	public Double getH() { return null; }

	public Vector getAxe() {
		return axeOfAnisotropy;
	}
	
	public void setAxe(Vector axeOfAnisotropy) {
		this.axeOfAnisotropy = axeOfAnisotropy;
	}

	
}
