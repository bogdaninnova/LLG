package main;

import java.util.Random;

public class OptimStochasticCalculation extends Calculation {

    private static final double da = Math.pow(10, -8);//step by angle

    public static final double w = 1;
    public static final double h = 0.1;

    private static Random rand = new Random();

    private static final double L = 1 + ALPHA * ALPHA;

    private static final double Kb = 1.38 * Math.pow(10, -16);
    private static final double T = 20;
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

        double sinTheta_1 = 1 / sin(theta);

        double dEnergy_dtheta = get_dW_dtheta(theta, phi, t);
        double dEnergy_dphi = get_dW_dphi(theta, phi, t);


        double dTheta = (
                - ALPHA / L * dEnergy_dtheta
                        - sinTheta_1 / L * dEnergy_dphi
                        + ALPHA / E / L / Math.tan(theta)
        ) * dt;

        double dPhi = (
                sinTheta_1 / L * dEnergy_dtheta
                        - ALPHA / L * sinTheta_1 * sinTheta_1 * dEnergy_dphi) * dt;

        theta += dTheta;
        phi += dPhi;
        t += dt;
    }


    public void iterationNoize() {
        double N1 = rand.nextDouble();//0..1
        double N2 = rand.nextDouble();//0..1

        double sinTheta_1 = 1 / sin(theta);

        double dEnergy_dtheta = get_dW_dtheta(theta, phi, t);
        double dEnergy_dphi = get_dW_dphi(theta, phi, t);


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
        double sintheta = sin(theta);
        double sintheta_da = sin(theta + da);
        double costheta = cos(theta);
        double costheta_da = cos(theta + da);
        double sinphi = sin(phi);
        double cosphi = cos(phi);
        return (getW(sintheta_da, costheta_da, sinphi, cosphi, t)
                - getW(sintheta, costheta, sinphi, cosphi, t)) / da;
    }

    public static double get_dW_dphi(double theta, double phi, double t) {
        double sintheta = sin(theta);
        double costheta = cos(theta);
        double sinphi = sin(phi);
        double sinphi_da = sin(phi + da);
        double cosphi = cos(phi);
        double cosphi_da = cos(phi + da);
        return (getW(sintheta, costheta, sinphi_da, cosphi_da, t)
                - getW(sintheta, costheta, sinphi, cosphi, t)) / da;
    }

    private static double getW(double sintheta, double costheta, double sinphi, double cosphi, double t) {
        return -0.5 * Math.pow(getM_Ea(sintheta, costheta, sinphi, cosphi), 2) - getM_H0(t, sintheta, costheta, sinphi, cosphi);
    }

    private static double getM_H0(double t, double sintheta, double costheta, double sinphi, double cosphi) {
        Vector h0 = getH0(t);
        return sintheta * cosphi * h0.getX() +
                sintheta * sinphi * h0.getY() +
                costheta * h0.getZ();
    }

    private static Vector getH0(double t) {
        return new Vector(h * cos(w * t), h * sin(w * t), 0);
    }

    private static double getM_Ea(double sintheta, double costheta, double sinphi, double cosphi) {
        return sintheta * cosphi * SIN_THETA_AN * COS_PHI_AN +
                sintheta * sinphi * SIN_THETA_AN * SIN_PHI_AN +
                costheta * COS_THETA_AN;
    }
}
