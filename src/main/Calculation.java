package main;

import java.util.ArrayList;

public abstract class Calculation {

	public static final double dt = Math.pow(10, -3);
	protected double t = 0;
	protected ArrayList<Vector> array = new ArrayList<Vector>();
	
	private static final double sigma = 0 * Math.pow(10, 18);
	
	private static final double alpha0 = 0.01;
	private static final double a = Math.pow(10, -5);
	private static final double mu1 = 1;
	private static final double mu2 = 1;
	private static final double c = 3 * Math.pow(10, 10);
	private static final double gamma = 1.76 * Math.pow(10, 7);
	private static final double modulM = Math.pow(10, 4) / (2 * Math.PI);
	private static final double Ha = 5 * Math.pow(10, 4);
	protected static final double taoSigma = getTaoSigma();

	public static final double ALPHA = alpha0 + getAlphaSigma();

	public abstract void run();
	
	public abstract void run(double waitingTime, double workingTime);
	
	protected void wait(double waitingTime) {
		while (t < waitingTime) 
			iteration();
	}
	
	protected abstract void iteration();
	
	public ArrayList<Vector> getArray() {
		return array;
	}
	
	protected static double sin(double a) {
		return Math.sin(a);
	}
	
	protected static double cos(double a) {
		return Math.cos(a);
	}
	
	private static double getTaoSigma() {
		double answer = 4 * Math.PI * sigma *
				Math.pow(3 * mu2 / (mu1 + 2 * mu2), 2) *
				a * a * mu1 / (15 * c * c);
		answer *= gamma * Ha;
		return answer;
	}
	
	private static  double getAlphaSigma() {
		return 8 * Math.PI * gamma * modulM * getTaoSigma() /
				(3 * mu1) / (gamma * Ha);
	}

}
