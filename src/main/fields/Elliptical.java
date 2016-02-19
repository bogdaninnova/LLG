package main.fields;

import main.Vector;

public class Elliptical extends Field {

    private double rho;

    public Elliptical(double rho, double w, double h) {
        setNewData(rho, w, h);
    }

    public Vector getValue(Vector M, double t) {
        return new Vector(h * Math.cos(w * t), rho * h * Math.sin(w * t), 0);
    }

    public void setNewData(double rho, double w, double h) {
        this.w = w;
        this.h = h;
        this.rho = rho;
    }

}
