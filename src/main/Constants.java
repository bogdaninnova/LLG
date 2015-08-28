package main;

public class Constants {

	private static final double sigma = 0 * Math.pow(10, 18);
	
	public static double dt = Math.pow(10, -3);
	
	public static final double alpha0 = 0.01;
	
	private static final double a = Math.pow(10, -5);
	private static final double mu1 = 1;
	private static final double mu2 = 1;
	private static final double c = 3 * Math.pow(10, 10);
	private static final double gamma = 1.76 * Math.pow(10, 7);
	private static final double modulM = Math.pow(10, 4) / (2 * Math.PI);
	private static final double Ha = 5 * Math.pow(10, 4);
	
	public static double alpha = alpha0 + getAlphaSigma();
	public static final double taoSigma = getTaoSigma();
	
	public static double getTaoSigma() {
		double answer = 4 * Math.PI * sigma *
				Math.pow(3 * mu2 / (mu1 + 2 * mu2), 2) *
				a * a * mu1 / (15 * c * c);
		answer *= gamma * Ha;
		return answer;
	}
	
	public static  double getAlphaSigma() {
		return 8 * Math.PI * gamma * modulM * getTaoSigma() /
				(3 * mu1) / (gamma * Ha);
	}
	
}
