package main.fields;

import main.Vector;

public class Anisotrophia extends Field {

	private Vector axeOfAnisotropia;
	
	public Anisotrophia(double tetta, double fi) {
		setVector(new Vector(tetta, fi));
	}
	
	public Vector getValue(Vector M, double t) {
		return axeOfAnisotropia.multiply(M.dotProduct(axeOfAnisotropia));
	}

	@Override
	public Double getW() {
		return null;
	}

	public Vector getAxe() {
		return axeOfAnisotropia;
	}
	
	public void setVector(Vector axeOfAnisotropia) {
		this.axeOfAnisotropia = axeOfAnisotropia;
	}

	
}
