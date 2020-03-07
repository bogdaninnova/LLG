package main;

import main.fields.Elliptical;
import java.util.LinkedList;

public class PeriodCounter {

	private Vector startDot;
	private boolean isLastInside;
	private int counter;

	private boolean isBegin = false;

	private double time;
	public LinkedList<Vector> list = new LinkedList<>();

	public LinkedList<Double> energyList = new LinkedList<>();

	private static final int LAPS = 5;

	private double energy = 0;
	private Vector M_aver = new Vector();
	private double steps = 0;

	public double MAX_PERIOD;

	public boolean isQ = false;

	private void move(Calculation c) {

		if (!isBegin) {
			isBegin = true;
			reset(c);
		}


		time += Calculation.dt;
		list.add(c.M.clone());
		raiseEnergy(c);
		energyList.add(getEnergy());

		if (c.M.dotProduct(startDot) > 0.999999) {
			if (!isLastInside) {
				counter++;
				isLastInside = true;
			}
		} else
			isLastInside = false;

		if (time > (LAPS * 10 / c.getField(Elliptical.class).getW()))
			isBegin = false;

//		System.out.println("c.t "+c.t);
//		System.out.println("time "+time);
//		System.out.println("MAX_PERIOD "+MAX_PERIOD);
		if (c.t > MAX_PERIOD) {
			reset(c);
			isQ = true;
			System.out.print("Q");
		}
	}




	private void move3(Calculation c) {
		raiseEnergy(c);
		list.add(c.M.clone());
		if (c.t > 2 * MAX_PERIOD)
			counter = LAPS;
	}


	private void reset(Calculation c) {
		counter = 0;

		startDot = c.M.clone();
		isLastInside = true;
		time = 0;
		list = new LinkedList<>();

		energyList = new LinkedList<>();
		steps = 0;
		energy = 0;
		M_aver = new Vector();
	}

	private void raiseEnergy(Calculation c) {
		energy += c.getHeff(c.M, c.t).dotProduct(c.dM) / Calculation.dt;
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

	public void update(Calculation c) {
		if (isQ)
			move3(c);
		else
			move(c);
	}

	public boolean isOver() {
		return counter == LAPS;
	}

	public void externalReset(Calculation c) {
		counter = 0;
		isBegin = false;
		energy = 0;
		M_aver = new Vector();
		steps = 0;
		isLastInside = true;
		time = 0;
		isQ = false;
		list = new LinkedList<>();
		energyList = new LinkedList<>();
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

