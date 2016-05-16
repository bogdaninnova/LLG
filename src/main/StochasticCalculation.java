package main;

import java.util.Random;

public class StochasticCalculation extends Calculation {

    private static final double da = Math.pow(10, -8);//step by angle

    public static final double w = 1;
    public static final double h = 0.1;

    private static Random rand = new Random();

    private static final double L = 1 + ALPHA * ALPHA;

    private static final double Kb = 1.38 * Math.pow(10, -16);
    private static final double T = 300;
    private static final double E = Ha * modM * V / (Kb * T);

    private static final double SQRT_2_ALPHA_E = Math.sqrt(2 * ALPHA / E);

    public static final double THETA_AN = Math.PI / 4;
    public static final double PHI_AN = 0;
    private static final double SIN_PHI_AN = sin(PHI_AN);
    private static final double COS_PHI_AN = cos(PHI_AN);
    private static final double SIN_THETA_AN = sin(THETA_AN);
    private static final double COS_THETA_AN = cos(THETA_AN);

    private double theta = THETA_AN;
    private double phi = PHI_AN;
    private double t = 0;

    @Deprecated
    @Override
    public void run() {
    }

    @Override
    public void run(double waitingTime, double workingTime) {
        for (int i = 0; i < waitingTime; i++)
            iteration();
        for (int i = 0; i < workingTime; i++) {
            iteration();
            array.add(new Vector(theta, phi));
        }
    }

    @Override
    public void iteration() {

        double coef = 1;
        double N1 = coef * rand.nextDouble();//0..1
        double N2 = coef * rand.nextDouble();//0..1

        double dEnergy_dtheta = get_dW_dtheta(theta, phi, t);
        double dEnergy_dphi = get_dW_dphi(theta, phi, t);
        double sinTheta_1 = 1 / sin(theta);

        double dTheta = (
                - ALPHA / L * dEnergy_dtheta
                        - sinTheta_1 / L * dEnergy_dphi
                + ALPHA / E / L / Math.tan(theta)
        ) * dt
                + SQRT_2_ALPHA_E / L * N1 * Math.sqrt(dt);

        double dPhi = (
                sinTheta_1 / L * dEnergy_dtheta
                        - ALPHA / L * sinTheta_1 * sinTheta_1 * dEnergy_dphi) * dt
                + SQRT_2_ALPHA_E / L * sinTheta_1 * N2 * Math.sqrt(dt);

        theta += dTheta;
        phi += dPhi;
        t += dt;
    }


    public static double get_dW_dtheta(double theta, double phi, double t) {
        return (getW(theta + da, phi, t) - getW(theta, phi, t)) / da;
    }

    public static double get_dW_dphi(double theta, double phi, double t) {
        return (getW(theta, phi + da, t) - getW(theta, phi, t)) / da;
    }

    private static double getW(double theta, double phi, double t) {
        return -0.5 * Math.pow(getM_Ea(theta, phi), 2) - getM_H0(t, theta, phi);
    }

    private static double getM_H0(double t, double theta, double phi) {
        Vector h0 = getH0(t);
        return sin(theta) * cos(phi) * h0.getX() +
                sin(theta) * sin(phi) * h0.getY() +
                cos(theta) * h0.getZ();
    }

    private static Vector getH0(double t) {
        return new Vector(h * cos(w * t), h * sin(w * t), 0);
    }

    private static double getM_Ea(double theta, double phi) {
        return sin(theta) * cos(phi) * SIN_THETA_AN * COS_PHI_AN +
                sin(theta) * sin(phi) * SIN_THETA_AN * SIN_PHI_AN +
                cos(theta) * COS_THETA_AN;
    }
}
