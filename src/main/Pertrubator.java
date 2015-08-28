package main;

public class Pertrubator {

	private static double EPSILON = 0.01;
	private static final double THETTA_EPS = Math.acos(1 - EPSILON * EPSILON / 2);
	

	
	public static Vector getNewPosition(Vector vec) {
		double thetta0 = Math.acos(vec.getZ());
		double mult = Math.sin(thetta0 + THETTA_EPS) / Math.sin(thetta0);
		
		return new Vector(vec.getX() * mult, vec.getY() * mult, Math.cos(thetta0 + THETTA_EPS));
	}
	
	public static double getLcurrent(Vector v1, Vector v2) {
		return Math.log(
				Math.sqrt(
						Math.pow(v1.getX() - v2.getX(), 2) +
						Math.pow(v1.getY() - v2.getY(), 2) +
						Math.pow(v1.getZ() - v2.getZ(), 2))
					/ EPSILON);
	}
	
}
