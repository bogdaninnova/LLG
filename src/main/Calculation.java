package main;

import java.util.ArrayList;

public abstract class Calculation {

	public static final double dt = Math.pow(10, -3);
	protected double t = 0;
	protected ArrayList<Vector> array = new ArrayList<>();

	public static final double sigma = 1 * Math.pow(10, 15);
	public static final double nu = 0 * Math.pow(10, 15);//TODO find AHE koeff

	private static final double alpha0 = 0.01;
	private static final double a = Math.pow(10, -5);
	protected static final double mu1 = 1;
	private static final double mu2 = 1;
	private static final double c = 3 * Math.pow(10, 10);
	private static final double gamma = 1.76 * Math.pow(10, 7);
	protected static final double modulM = Math.pow(10, 4) / (2 * Math.PI);
	private static final double Ha = 5 * Math.pow(10, 4);

	protected static final double kappa = 3 * mu2 / (mu1 + 2 * mu2);


	protected static final double ksi = 0;//todo getKsi();

	protected static final double taoSigma = getTaoSigma();
	protected static final double taoAnomal = getTaoAnomal();

	protected static final double ALPHA = alpha0;//todo + getAlphaSigma();




	protected static final double constant = -1 / (Math.pow(1 - ksi, 2) + Math.pow(ALPHA, 2));






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
		double answer = 4 * Math.PI * sigma * kappa * kappa * a * a * mu1 / (15 * c * c);
		answer *= gamma * Ha;
		return answer;
	}

	private static double getTaoAnomal() {
		double answer = 4 * Math.PI * nu * kappa * kappa * a * a * mu1 / (15 * c * c);
		answer *= gamma * Ha;
		return answer;
	}
	
	private static  double getAlphaSigma() {
		return 8 * Math.PI / 3 * gamma * modulM * getTaoSigma();
	}

	private static  double getKsi() {
		return 8 * Math.PI / 3 * gamma * modulM * getTaoAnomal();
	}

	public static void print() {
//		System.out.println("Light speed = " + c);
//		System.out.println("dt = " + dt);
//		System.out.println("Radius = " + a);
//		System.out.println("-----------------------------------");
		System.out.println("sigma = " + sigma);
		System.out.println("nu = " + nu);
		System.out.println("-----------------------------------");
		System.out.println("alpha0 = " + alpha0);
		System.out.println("ALPHA = " + ALPHA);
		System.out.println("getAlphaSigma() = " + getAlphaSigma());
//		System.out.println("-----------------------------------");
//		System.out.println("mu1 = " + mu1);
//		System.out.println("mu2 = " + mu2);
//		System.out.println("kappa = " + kappa);
//		System.out.println("-----------------------------------");
//		System.out.println("gamma = " + gamma);
//		System.out.println("Ha = " + Ha);
//		System.out.println("modulM = " + modulM);
		System.out.println("-----------------------------------");
		System.out.println("ksi = " + ksi);
		System.out.println("taoSigma = " + taoSigma);
		System.out.println("taoAnomal = " + taoAnomal);
		System.out.println("-----------------------------------");
		System.out.println("constant = " + constant);

	}

}
