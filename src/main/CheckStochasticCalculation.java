package main;

import java.util.Random;

public class CheckStochasticCalculation extends Calculation {

    private static final double dt = Math.pow(10, -3);
    private static final double da = Math.pow(10, -8);//step by angle

    public static final double w = 1;
    public static final double h = 0.1;

    private static Random rand = new Random();

    private static final double ALPHA = 0.01;
    private static final double L = 1 + ALPHA * ALPHA;
    private static final double Ha = 5 * Math.pow(10, 4);
    private static final double M = Math.pow(10, 4) / (2 * Math.PI);
    private static final double R = Math.pow(10, -5);
    private static final double V = 4/3 * Math.PI * Math.pow(R, 3);
    private static final double Kb = 1.38 * Math.pow(10, -16);
    private static final double T = 20;
    private static final double E = Ha * M * V / Kb / T;

    private static final double SQRT_2_ALPHA_E = Math.sqrt(2 * ALPHA / E);

    public static final double THETA_AN = Math.PI / 4;
    public static final double PHI_AN = 0;

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

    public void iteration() {

        double coef = Math.pow(10, 2);

        double N1 = coef * rand.nextDouble();//0..1
        double N2 = coef * rand.nextDouble();//0..1

//        N1 = 0;
//        N2 = 0;


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


    public static double get_dW_dtheta(double theta, double phi, double time) {

        double at = THETA_AN;
        double af = PHI_AN;
        double f = phi;
        double t = theta;
        Vector h0 = getH0(time);
        double hx = h0.getX();
        double hy = h0.getY();
        double hz = h0.getZ();

        return (-hx * cos(t)* cos(f) - hy * cos(t) * sin(f) + hz * sin(t))
                - (cos(af) * sin(at) * cos(f) * cos(t) - cos(at) * sin(t) + sin(af) * sin(at) * cos(t) * sin(f)) *
                (cos(at) * cos(t) + cos(af) * sin(at) * cos(f) * sin(t) + sin(af) * sin(at) * sin(f) * sin(t));


    }

    public static double get_dW_dphi(double theta, double phi, double time) {
        double at = THETA_AN;
        double af = PHI_AN;
        double f = phi;
        double t = theta;
        Vector h0 = getH0(time);
        double hx = h0.getX();
        double hy = h0.getY();
        double hz = h0.getZ();
        return (hx * sin(t) * sin(f) - hy * sin(t) * cos(f))
                + (cos(af) * sin(at) * sin(t) * sin(f) - sin(af) * sin(at) * cos(f) * sin(t)) *
                (cos(at) * cos(t) + cos(af) * sin(at) * cos(f) * sin(t) + sin(af) * sin(at) * sin(f) * sin(t));

    }

    private static Vector getH0(double t) {
        return new Vector(h * cos(w * t), h * sin(w * t), 0);
    }
}
