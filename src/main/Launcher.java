package main;

import main.fields.Anisotrophia;
import main.fields.EffectiveField;
import main.fields.Lineal;
import painting.Draw;

import java.util.ArrayList;
import java.util.Date;

public class Launcher {

	public static void main(String...strings) {

//		oneParticle(0, 0, 0.3, 2);

		Q_w(0.05, 0.01, 2);
		Q_w(0.1, 0.01, 2);
		Q_w(0.15, 0.01, 2);
		Q_w(0.2, 0.01, 2);
		Q_w(0.25, 0.01, 2);
		Q_w(0.3, 0.01, 2);





	}

	public static void Q_w(double h, double w1, double w2) {
		Calculator c = new Calculator();
		ArrayList<Double> wList = new ArrayList<Double>();
		for (double w = w1; w <= w2; w += 0.01) {
			double wSum = 0;
			double counter = 0;
			for (double theta = 0; theta <= 1.001; theta += 0.05) {
				System.out.println("\n" + new Date());
				System.out.println("h = " + h + ". W = " + w + ". Theta: " + theta);
				for (double fi = 0; fi < 0.999; fi += 0.05) {

					if (theta > 0.499 && theta < 0.501)
						if ((fi == 0) || (fi > 0.499 && fi < 0.501) || (fi > 0.999)) {
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
					counter++;
				}
			}
			wList.add(wSum / counter);
			Writer.writeDoubleList(wList, "Energy h = " + h + ". From w = " + w1 + " to w = " + w2);
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
		System.out.println(new Date().getTime() - start.getTime());

		String trackName = "(t= "+ tetta +", h= " + h + ", w= " + w + ")";
		new Draw(c, 0.4 * Math.PI, 0.4 * Math.PI, 0, "res/" + trackName).drawTraectory(true);
	}

}