package main;

public class Vector {

	private double x;
	private double y;
	private double z;

	public Vector(double x, double y, double z) {
		this.x = x;
		this.y = y;
		this.z = z;
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

	public strictfp double getThetta() {
		return Math.acos(z / modul());
	}

	public strictfp double getPhi() {
		if (x == 0)
			return Math.PI / 2;
		return Math.atan(y / x);
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
}
