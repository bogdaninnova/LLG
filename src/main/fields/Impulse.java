package main.fields;

import main.Vector;

import java.util.ArrayList;
import java.util.ListIterator;

public class Impulse extends Field {

	public ArrayList<ImpulsePart> list = new ArrayList<>();
	private final Vector e;
	
	public Impulse(Vector e) {
		this.e = e;
	}
	
	public void add(double h0, double h1, double tao) {
		list.add(new ImpulsePart(this, h0, h1, tao));
	}
	
	public Vector getValue(Vector M, double t) {
		ListIterator<ImpulsePart> iter = list.listIterator();
		ImpulsePart imp;
		while (iter.hasNext()) {
			imp = iter.next();
			if (imp.startTime + imp.tao > t) 
				return e.multiply(imp.getValue(t));
		}
		return e.multiply(0);
	}

	@Override
	public Double getW() {
		return null;
	}

	public double getCurrentTao(double t) {
		ListIterator<ImpulsePart> iter = list.listIterator();
		ImpulsePart imp;
		while (iter.hasNext()) {
			imp = iter.next();
			if (imp.startTime + imp.tao > t) 
				return imp.tao;
		}
		return Math.pow(10, 10);
	}
	
	public Double getH() {
		double h = -999;
		for (ImpulsePart imPart : list)
			if (h < imPart.h0)
				h = imPart.h0;
		return h;
	}
	
	public ArrayList<Double> getTao() {
		ArrayList<Double> result = new ArrayList<Double>();
		for (ImpulsePart imPart : list)
			result.add(imPart.tao);
		return result;
	}
	
	
	private class ImpulsePart {

		private double h0, h1, tao, startTime;
		
		ImpulsePart(Impulse imp, double h0, double h1, double tao) {
			this.h0 = h0;
			this.h1 = h1;
			this.tao = tao;
			startTime = 0;
			for (ImpulsePart part : imp.list)
				startTime += part.tao;
		}
		
		private double getValue(double t) {
			return h0 + (startTime - t) / tao * (h0 - h1);
		}
		
	}
	
}
