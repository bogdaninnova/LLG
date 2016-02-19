package main.fields;

import main.Vector;

public class Circular extends Field {

	public Circular(double w, double h) {
		setNewData(w, h);
	}

	public Vector getValue(Vector M, double t) {
		return new Vector(h * Math.cos(w * t), h * Math.sin(w * t), 0);
	}

	public void setNewData(double w, double h) {
		this.w = w;
		this.h = h;
	}

}
