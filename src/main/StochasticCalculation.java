package main;

import main.fields.Anisotropy;
import main.fields.Field;

import java.util.Random;

public class StochasticCalculation extends Calculation {

    private Anisotropy anisotropyField;
    private Field outerField;

    private static Random rand = new Random();

    private static final double da = Math.pow(10, -8);//step by angle
    private static final double L = 1 + ALPHA * ALPHA;
    private static final double Kb = 1.38 * Math.pow(10, -16);
    private static final double T = 300;
    private static final double E = Ha * modM * V / (Kb * T);
    private static final double SQRT_2_ALPHA_E = Math.sqrt(2 * ALPHA / E);

    private PeriodCounter pc = new PeriodCounter();

    public StochasticCalculation(Field... fields) {
        setFields(fields);
        pc.externalReset(this);
    }

    public void setFields(Field... fields) {
        dM = new Vector();
        this.omega = -1;
        setBeginningLocation(null);
        for (Field field : fields) {
            if (field.getW() != null) {
                if (omega == -1) {
                    this.omega = field.getW();
                    this.outerField = field;
                } else {
                    throw new IllegalArgumentException("More than one field has frequency");
                }
            }
            if (field.getClass().equals(Anisotropy.class))
                if (M == null) {
                    anisotropyField = (Anisotropy) field;
                    Vector easyAxe = ((Anisotropy) field).getAxe();
                    if (easyAxe.getX() > 0.99)
                        setBeginningLocation(new Vector(Math.PI * 0.1, 0));
                    else if (easyAxe.getX() < -0.99)
                        setBeginningLocation(new Vector(Math.PI * 0.9, 0));
                    else
                        setBeginningLocation(easyAxe);
                } else {
                    throw new IllegalArgumentException("More than one easy axe");
                }
        }
        if (M == null)
            throw new IllegalArgumentException("No easy axe");
    }

    @Override
    public Vector getHeff(Vector M, double t) {
        return outerField.getValue(M, t).plus(anisotropyField.getValue(M, t));
    }

    @Override
    public void run() {
        pc.externalReset(this);
        wait(300d);
        while (true) {
            iteration();
            pc.update(this);
            if (pc.isOver())
                break;
        }
        for (Vector vector : pc.list)
            array.add(vector);
    }

    @Override
    public void run(double waitingTime, double workingTime) {
        wait(waitingTime);
        while (true) {
            iteration();
            pc.update(this);
            array.add(new Vector(M));
            if (t >  waitingTime + workingTime)
                break;
        }
    }

    public double getEnergy() {
        return pc.getEnergy();
    }

    @Override
    public void iteration() {

        double N1 = rand.nextDouble();//0..1
        double N2 = rand.nextDouble();//0..1

        double dEnergy_dtheta = get_dW_dtheta(M, t);
        double dEnergy_dphi = get_dW_dphi(M, t);
        double sinTheta_1 = 1 / M.getSinTheta();

        double dTheta = (
                - ALPHA / L * dEnergy_dtheta
                        - sinTheta_1 / L * dEnergy_dphi
                        + ALPHA / E / L * ( M.getCosTheta() / M.getSinTheta())
        ) * dt
                + SQRT_2_ALPHA_E / L * N1 * Math.sqrt(dt);

        double dPhi = (
                sinTheta_1 / L * dEnergy_dtheta
                        - ALPHA / L * sinTheta_1 * sinTheta_1 * dEnergy_dphi) * dt
                + SQRT_2_ALPHA_E / L * sinTheta_1 * N2 * Math.sqrt(dt);

        dM = new Vector(dTheta, dPhi);

        Vector oldM = M.clone();
        M = new Vector(M.getThetta() + dTheta, M.getPhi() + dPhi);
        dM = M.minus(oldM);
        t += dt;
    }

    public double get_dW_dtheta(Vector M, double t) {
        return (getW(new Vector(M.getThetta() + da, M.getPhi()), t) -
                getW(new Vector(M.getThetta(), M.getPhi()), t)) / da;
    }

    public double get_dW_dphi(Vector M, double t) {
        return (getW(new Vector(M.getThetta(), M.getPhi() + da), t) -
                getW(new Vector(M.getThetta(), M.getPhi()), t)) / da;
    }

    private double getW(Vector M, double t) {
        return -0.5 * Math.pow(getM_Ea(M), 2) - getM_H0(M, t);
    }

    private double getM_H0(Vector M, double t) {
        Vector h0 = outerField.getValue(M, t);
        return M.getSinTheta() * M.getCosPhi() * h0.getX() +
                M.getSinTheta() * M.getSinPhi() * h0.getY() +
                M.getCosTheta() * h0.getZ();
    }

    private double getM_Ea(Vector M) {
        return M.getSinTheta() * M.getCosPhi() * anisotropyField.getAxe().getSinTheta() * anisotropyField.getAxe().getCosPhi() +
                M.getSinTheta() * M.getSinPhi() * anisotropyField.getAxe().getSinTheta() * anisotropyField.getAxe().getSinPhi() +
                M.getCosTheta() * anisotropyField.getAxe().getCosTheta();
    }
}
