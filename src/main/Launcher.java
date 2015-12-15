package main;

import main.fields.Anisotrophia;
import main.fields.EffectiveField;
import main.fields.Lineal;
import painting.Draw;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Date;

public class Launcher {

	public static void main(String...strings) {

//		oneParticle(0.25, 0, 0.3, 1);



		Q_w(0.3, 0.01, 2);



	}

	public static void Q_w(double h, double w1, double w2) {
		Calculator c = new Calculator();
		ArrayList<Double> wList = new ArrayList<Double>();
		ArrayList<Vector> mList = new ArrayList<Vector>();
		for (double w = w1; w <= w2; w = round(w + 0.1, 1)) {
			double wSum = 0;
			Vector m_aver = new Vector();
			double counter = 0;
			for (double theta = 0.0; theta <= 1; theta = round(theta + 0.01, 2)) {
				System.out.println("\n" + new Date());
				System.out.println("h = " + h + ". W = " + w + ". Theta: " + theta);
				for (double fi = 0; fi < 1; fi = round(fi + 0.01, 2)) {

					if (theta == 0.5)
						if ((fi == 0) || (fi == 0.5) || (fi == 1)) {
							counter++;
							continue;
						}
					c.w = w;
					c.startVector = new Vector(Math.acos(2 * theta - 1), 2 * Math.PI * fi);
					Calculator.fieldsList = new EffectiveField();
					Calculator.fieldsList.add(new Anisotrophia(Math.acos(2 * theta - 1), 2 * Math.PI * fi));
					Calculator.fieldsList.add(new Lineal(new Vector(1, 0, 0), w, h));
					c.run();
					wSum += c.getEnergy();
					m_aver = m_aver.plus(c.getM_aver());
					counter++;
				}
			}
			mList.add(m_aver.multiply(1 / counter));
			wList.add(wSum / counter);

			Writer.writeDoubleList(wList, "Energy h = " + h + ". From w = " + w1 + " to w = " + w2);
			Writer.writeTraectorysCoordinates(mList, "M_aver h = " + h + ". From w = " + w1 + " to w = " + w2);
		}
	}



	public static void oneParticle(double tetta, double fi, double h, double w) {

		Date start = new Date();

		Calculator.fieldsList = new EffectiveField();
		Calculator.fieldsList.add(new Anisotrophia(Math.PI * tetta, 0));
		Calculator.fieldsList.add(new Lineal(new Vector(1, 0, 0), w, h));

		Calculator c = new Calculator();
		c.w = w;
		c.startVector = new Vector(Math.PI * tetta, Math.PI * fi);
		//c.run(15000, 1000);
		c.run();
		System.out.println(c.getEnergy());
		System.out.println(c.getM_aver());
		System.out.println(new Date().getTime() - start.getTime());

		String trackName = "(t= "+ tetta +", h= " + h + ", w= " + w + ")";
		new Draw(c, 0.4 * Math.PI, 0.4 * Math.PI, 0, "res/" + trackName).drawTraectory(true);
	}


	public static double round(double value, int places) {
		if (places < 0) throw new IllegalArgumentException();

		BigDecimal bd = new BigDecimal(value);
		bd = bd.setScale(places, RoundingMode.HALF_UP);
		return bd.doubleValue();
	}


}