package main.fields;

import java.util.ArrayList;

import main.Constants;
import main.Vector;

public class EffectiveField {

	private ArrayList<Field> fields = new ArrayList<Field>();

	@SuppressWarnings("rawtypes")
	public boolean isContain(Class c) {
		for (Field field : fields)
			if (field.getClass().equals(c))
				return true;
		return false;
	}
	
	public void add(Field field) {
		fields.add(field);
	}
	
	@SuppressWarnings("rawtypes")
	public Field get(Class c) {
		for (Field field : fields)
			if (field.getClass().equals(c))
				return (Field) field;
		return null;
	}
	
	public Vector getHeff(Vector M, double t) {
		Vector temp = new Vector();

		for (Field field : fields) {
			temp = temp.plus(field.getValue(M, t));
			//temp = temp.plus(field.getDerivative(M, t).multiply(-Constants.taoSigma)); //TODO
		}
		return temp;
	}
	
}
