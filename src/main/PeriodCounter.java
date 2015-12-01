package main;

import main.fields.Lineal;

import java.util.LinkedList;

public class PeriodCounter {

	private Vector startDot;
	private static final double r = Math.pow(10, -2);
	private boolean isLastInside;
	private boolean isNowInside;
	private int counter;

	private boolean isBegin = false;

	private double time;
	//public LinkedList<Vector> list = new LinkedList<Vector>(); //TODO return for drawing

	public LinkedList<Double> energyList = new LinkedList<Double>();

	private double omega;
	private double dt;

	private static final int LAPS = 2;

	private double energy = 0;
	private double steps = 0;

	private static final double MAX_PERIOD = 10000;

	public static boolean isQ = false;


	private void move(Calculator c) {

		if (!isBegin) {
			isBegin = true;
			reset(c);
		}


		time += dt;
		//list.add(c.M.clone());
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
	private void move2(Calculator c) {

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
				System.out.println("Q");
			}
		}

		if 	(isStartWrite) {
			//list.add(c.M.clone());
			raiseEnergy(c);
		}

		isGrow = z2 > z1;

	}

	private void move3(Calculator c) {
		raiseEnergy(c);
		if (c.t > 1.1 * MAX_PERIOD)
			counter = LAPS;
	}


	private void reset(Calculator c) {
		counter = 0;
		omega = ((Lineal) c.fieldsList.get(Lineal.class)).getW();//TODO
		startDot = c.M.clone();
		isLastInside = true;
		time = 0;
		dt = c.dt;
		//list = new LinkedList<Vector>();//TODO
		
		
		
		energyList = new LinkedList<Double>();//TODO
		steps = 0;
		energy = 0;
	}

	private static boolean isDotNearDot(Vector dot1, Vector dot2) {
		return (Math.pow(dot1.getX() - dot2.getX(), 2) +
				Math.pow(dot1.getY() - dot2.getY(), 2) +
					Math.pow(dot1.getZ() - dot2.getZ(), 2) < r*r);
	}


	private void raiseEnergy(Calculator c) {
		energy += c.fieldsList.getHeff(c.M, c.t).dotProduct(c.dM) / c.dt;
		steps++;
	}

	public double getEnergy() {
		if (steps == 0)
			return 0;
		else
			return energy / steps;
	}


	public double getValue() {
		return counter;
	}

	public void update(Calculator c) {
		if (isQ)
			move3(c);
		else
			move(c);
	}

	public boolean isOver() {
		return counter == LAPS;
	}

	public void reset() {
		z1 = 2;
		z2 = 2;
		counter = 0;
		isBegin = false;
		energy = 0;
		steps = 0;
		isLastInside = true;
		time = 0;
		isQ = false;
		isStartWrite = false;
		//list = new LinkedList<Vector>();//TODO
		energyList = new LinkedList<Double>();
	}
}