package main;

import java.util.ArrayList;
import java.util.List;

public class Vector {

	private final double x;
	private final double y;
	private final double z;

	
	public Vector(double x, double y, double z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}
	

	
	public Vector(double tetta, double fi) {
		this.x = Math.sin(tetta) * Math.cos(fi);
		this.y = Math.sin(tetta) * Math.sin(fi);
		this.z = Math.cos(tetta);
	}
	
	public Vector(Vector ... vectors) {
		double x = 0, y = 0, z = 0;
		for (Vector vector : vectors) {
			x += vector.getX();
			y += vector.getY();
			z += vector.getZ();
		}
		this.x = x;
		this.y = y;
		this.z = z;
	}

	public strictfp double getX() {
		return x;
	}

	public strictfp double getY() {
		return y;
	}

	public strictfp double getZ() {
		return z;
	}
	
	public Vector plus(Vector vec) {
		return new Vector(x + vec.getX(), y + vec.getY(), z + vec.getZ());
	}
	
	public Vector multiply(double a) {
		return new Vector(x * a, y * a, z * a);
	}
	
	public strictfp double modul() {
		return Math.sqrt(x*x+y*y+z*z);
	}
	
	public strictfp Vector crossProduct(Vector vec) {
		return new Vector(y * vec.getZ() - z * vec.getY(),
											z * vec.getX() - x * vec.getZ(),
											x * vec.getY() - y * vec.getX());
	}
	
	public strictfp double dotProduct(Vector vec) {
		return x * vec.getX() + y * vec.getY() + z * vec.getZ();
	}
	
	private Vector rotateAroundX(double a) {
		return new Vector(
				x, y * Math.cos(a) - z * Math.sin(a), y * Math.sin(a) + z * Math.cos(a));
	} 
	
	private Vector rotateAroundY(double a) {
		return new Vector(
				x * Math.cos(a) + z * Math.sin(a), y, -x * Math.sin(a) + z * Math.cos(a));
	} 
	
	private Vector rotateAroundZ(double a) {
		return new Vector(
				x * Math.cos(a) - y * Math.sin(a), x * Math.sin(a) + y * Math.cos(a), z);
	}
	
	public Vector rotate(double xAngle, double yAngle, double zAngle) {
		return rotateAroundX(xAngle).	rotateAroundY(yAngle).rotateAroundZ(zAngle);
	}
	
	public Vector clone() {
		return new Vector(x, y, z);
	}
	
	
	public static List<Vector> grammShmidtOrthogonal(List<Vector> list) {
		ArrayList<Vector> ortList = new ArrayList<Vector>();
		ortList.add(list.get(0));

		for (int k = 1; k < list.size(); k++) {
			Vector s = new Vector();
			for (int i = 1; i < k; i++) {
				double lyambda = - list.get(k).dotProduct(ortList.get(i)) / ortList.get(i).modul();
				s = s.plus(ortList.get(i).multiply(lyambda));
			}
			ortList.add(list.get(k).plus(s));
		}
		return ortList;
	}

	public String toString() {
		return "(x:" + x + ";y=" + y + ";z=" + z + ")";
	}
	
	
	
}
