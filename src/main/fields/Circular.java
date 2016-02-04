package main.fields;

import main.Vector;

public class Circular extends Field {

	private double w, h;

	public Circular(double w, double h) {
		setNewData(w, h);
	}

	public Vector getValue(Vector M, double t) {
		return new Vector(h * Math.cos(w * t), h * Math.sin(w * t), 0);
	}

	public Double getW() {
		return w;
	}

	public double getH() {
		return h;
	}

	public void setNewData(double w, double h) {
		this.w = w;
		this.h = h;
	}

}
