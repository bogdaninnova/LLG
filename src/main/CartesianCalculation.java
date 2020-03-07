package main;

import jdk.nashorn.internal.ir.annotations.Ignore;
import main.fields.Anisotropy;
import main.fields.Field;

import java.util.ArrayList;

public class CartesianCalculation extends Calculation {

	protected ArrayList<Field> fields;

	public CartesianCalculation(Field... fields) {
		setFields(fields);
		update();
	}

	public void setFields(Field... fields) {
		dM = new Vector();
		this.fields = new ArrayList<>();
		setBeginningLocation(null);
		for (Field field : fields) {
			this.fields.add(field);
			if (field.getClass().equals(Anisotropy.class))
				if (M == null) {
					setBeginningLocation(((Anisotropy) field).getAxe());
				} else {
					throw new IllegalArgumentException("More than one easy axe");
				}
		}
		if (M == null)
			setBeginningLocation(new Vector(0, 0));
	}

	public Field getField(Class fieldClass) {
		for (Field field : fields)
			if (field.getClass().equals(fieldClass))
				return field;
		return null;
	}

	protected void iteration() {
		dM = getdM(M);
		M = M.plus(dM);
		t += dt;
	}

	@Override
	public Vector getEasyAxe() {
		return ((Anisotropy) getField(Anisotropy.class)).getAxe();
	}

	private Vector getdM(Vector M) {
		Vector d1 = LLG(new Vector(M.getX(), M.getY(), M.getZ()), t);
		Vector d2 = LLG(new Vector(M.getX() + dt / 2 * d1.getX(), M.getY() + dt / 2 * d1.getY(), M.getZ() + dt / 2 * d1.getZ()), t + dt / 2);
		Vector d3 = LLG(new Vector(M.getX() + dt / 2 * d2.getX(), M.getY() + dt / 2 * d2.getY(), M.getZ() + dt / 2 * d2.getZ()), t + dt / 2);
		Vector d4 = LLG(new Vector(M.getX() + dt / 1 * d3.getX(), M.getY() + dt / 1 * d3.getY(), M.getZ() + dt / 1 * d3.getZ()), t + dt / 1);

		return new Vector(
				dt/6 * (d1.getX() + 2 * d2.getX() + 2 * d3.getX() + d4.getX()),
				dt/6 * (d1.getY() + 2 * d2.getY() + 2 * d3.getY() + d4.getY()),
				dt/6 * (d1.getZ() + 2 * d2.getZ() + 2 * d3.getZ() + d4.getZ()));
	}

	private Vector LLG(Vector M, double t) {
		Vector MxH = M.crossProduct(getHeff(M, t));
		Vector a_MxHxH = M.crossProduct(MxH).multiply(ALPHA);

		return new Vector(
				(MxH.getX() + a_MxHxH.getX()),
				(MxH.getY() + a_MxHxH.getY()),
				(MxH.getZ() + a_MxHxH.getZ())).
						multiply(-1 / (1 + Math.pow(ALPHA, 2)));
	}

	private Vector LLG_conductive(double mx, double my, double mz, double t) {
		Vector M = new Vector(mx, my, mz);

		Vector dH0 = getDerHeff(M, t);
		Vector H = new Vector(
				getHeff(M, t),
				dH0.multiply(-mu1 * taoSigma),
				M.crossProduct(dH0).multiply(taoAnomal));

		Vector MxH = M.crossProduct(H);
		Vector MxMxH = M.crossProduct(MxH);

		Vector c1_MxH = MxH.multiply(1 - ksi);
		Vector c2_MxMxH = MxMxH.multiply(ALPHA);

		return new Vector(
				(c1_MxH.getX() + c2_MxMxH.getX()),
				(c1_MxH.getY() + c2_MxMxH.getY()),
				(c1_MxH.getZ() + c2_MxMxH.getZ())).multiply(constant);
	}

	@Override
	public Vector getHeff(Vector M, double t) {
		return fields.stream().map(field -> field.getValue(M, t)).reduce(new Vector(), Vector::plus);
	}

	public Vector getDerHeff(Vector M, double t) {
		return fields.stream().map(field -> field.getDerivative(M, t)).reduce(new Vector(), Vector::plus);
	}

}
