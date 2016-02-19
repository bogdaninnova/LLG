package main;

import main.fields.Circular;
import main.fields.Elliptical;
import main.fields.Lineal;

import java.util.LinkedList;

public class PeriodCounter {

	private Vector startDot;
	private static final double r = Math.pow(10, -3);
	private boolean isLastInside;
	private boolean isNowInside;
	private int counter;

	private boolean isBegin = false;

	private double time;
	public LinkedList<Vector> list = new LinkedList<Vector>();

	public LinkedList<Double> energyList = new LinkedList<Double>();

	private double omega;
	private double dt;

	private static final int LAPS = 5;

	private double energy = 0;
	private Vector M_aver = new Vector();
	private double steps = 0;

	public double MAX_PERIOD;

	public boolean isQ = false;

	private void move(CartesianCalculation c) {

		if (!isBegin) {
			isBegin = true;
			reset(c);
		}


		time += dt;
		list.add(c.M.clone());
		isNowInside = isDotNearDot(c.M, startDot);
		raiseEnergy(c);
		energyList.add(getEnergy());

		if (isNowInside) {
			if (!isLastInside) {
				counter++;
				isLastInside = true;
			}
		} else
			isLastInside = false;

		if (time > (LAPS * 10 / omega))
			isBegin = false;

		if (c.t > MAX_PERIOD) {
			reset(c);
			isQ = true;
			System.out.print("Q");
		}
	}

	private double z1, z2;
	private boolean isGrow;
	private boolean isStartWrite = false;


	/**
	 *
	 * Algorithm for finding period between minimum and maximum of some track
	 *
	 * */
	private void move2(CartesianCalculation c) {

		if (!isBegin) {
			z1 = 2;
			z2 = c.M.getZ();
			isBegin = true;
			return;
		}

		if (z1 == 2) {
			z1 = z2;
			z2 = c.M.getZ();
			isGrow = z2 > z1;
			return;
		}

		z1 = z2;
		z2 = c.M.getZ();

		if (z2 < z1 && isGrow) {
			if (!isStartWrite)
				isStartWrite = true;
			else {
				counter = LAPS;
				System.out.print("Q");
			}
		}

		if 	(isStartWrite) {
			list.add(c.M.clone());
			raiseEnergy(c);
		}

		isGrow = z2 > z1;

	}

	private void move3(CartesianCalculation c) {
		raiseEnergy(c);
		list.add(c.M.clone());
		if (c.t > 2 * MAX_PERIOD)
			counter = LAPS;
	}


	private void reset(CartesianCalculation c) {
		counter = 0;

		if (c.isContainField(Circular.class))
			omega = c.getField(Circular.class).getW();
		else if (c.isContainField(Lineal.class))
			omega = c.getField(Lineal.class).getW();
		else if (c.isContainField(Elliptical.class))
			omega = c.getField(Elliptical.class).getW();

		startDot = c.M.clone();
		isLastInside = true;
		time = 0;
		dt = c.dt;
		list = new LinkedList<Vector>();



		energyList = new LinkedList<Double>();
		steps = 0;
		energy = 0;
		M_aver = new Vector();
	}

	private static boolean isDotNearDot(Vector dot1, Vector dot2) {
		return (Math.pow(dot1.getX() - dot2.getX(), 2) +
				Math.pow(dot1.getY() - dot2.getY(), 2) +
				Math.pow(dot1.getZ() - dot2.getZ(), 2) < r*r);
	}


	private void raiseEnergy(CartesianCalculation c) {
		energy += c.getHeff(c.M, c.t).dotProduct(c.dM) / c.dt;
		M_aver = M_aver.plus(c.M);
		steps++;
	}

	public double getEnergy() {
		if (steps == 0)
			return 0;
		else
			return energy / steps;
	}

	public Vector getM_aver() {
		if (steps == 0)
			return new Vector();
		else
			return M_aver.multiply(1 / steps);
	}


	public double getValue() {
		return counter;
	}

	public void update(CartesianCalculation c) {
		if (isQ)
			move3(c);
		else
			move(c);
	}

	public boolean isOver() {
		return counter == LAPS;
	}

	public void externalReset(CartesianCalculation c) {
		z1 = 2;
		z2 = 2;
		counter = 0;
		isBegin = false;
		energy = 0;
		M_aver = new Vector();
		steps = 0;
		isLastInside = true;
		time = 0;
		isQ = false;
		isStartWrite = false;
		list = new LinkedList<Vector>();
		energyList = new LinkedList<Double>();

		if (c.isContainField(Circular.class))
			MAX_PERIOD = maxWaiting2period(c.getField(Circular.class).getW());
		else if (c.isContainField(Lineal.class))
			MAX_PERIOD = maxWaiting2period(c.getField(Lineal.class).getW());
		else if (c.isContainField(Elliptical.class))
			MAX_PERIOD = maxWaiting2period(c.getField(Elliptical.class).getW());
	}


	private static double maxWaiting2period(double w) {
		if (w > 1)
			return 4096 / w;
		if (w > 0.5)
			return 2048 / w;
		if (w > 0.25)
			return 1024 / w;
		if (w > 0.0625)
			return 512 / w;
		if (w > 0.03125)
			return 128 / w;
		if (w > 0.015625)
			return 64 / w;
		return 32 / w;
	}
}