package main;

import main.fields.Anisotrophia;
import main.fields.Circular;
import main.fields.EffectiveField;
import main.fields.Lineal;
import painting.Draw;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

public class Launcher {

	public static void main(String...strings) {

		//oneParticle(0, 0.3, 0.7);
		Q_w(0.05);
	}


	public static void Q_w(double h) {
		Calculator c = new Calculator();
		ArrayList<Double> wList = new ArrayList<Double>();
		for (double w = 0.01; w <= 2.001; w += 0.01) {
			double wSum = 0;
			double counter = 0;
			for (double theta = 0; theta <= 1.001; theta += 0.1) {
				System.out.println("\n" + new Date());
				System.out.println("W = " + w + ". Theta: " + theta);
				for (double fi = 0; fi < 1; fi += 0.1) {

					if (theta > 0.499 && theta < 0.501)
						if ((fi == 0) || (fi > 0.499 && fi < 0.501) || (fi > 0.999)) {
							counter++;
							continue;
						}

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
			Writer.writeDoubleList(wList, "energy");
			System.out.println("\n Energy: " + wSum / counter + "\n");
		}
	}



	public static void oneParticle(double tetta, double h, double w) {

		Date start = new Date();
		
		Calculator.fieldsList = new EffectiveField();
		Calculator.fieldsList.add(new Anisotrophia(Math.PI * tetta, 0));
		Calculator.fieldsList.add(new Lineal(new Vector(1, 0, 0), w, h));
		
		Calculator c = new Calculator();

		c.startVector = new Vector(Math.PI * tetta, 0);
		//c.run(1000, 1000);
		c.run();
		System.out.println(c.getEnergy());
		System.out.println(new Date().getTime() - start.getTime());
		
		String trackName = "(t= "+ tetta +", h= " + h + ", w= " + w + ")";
		//new Draw(c, 0.4 * Math.PI, 0.4 * Math.PI, 0, "res/" + trackName).drawTraectory(true);
	}




	public static void longStory() {
		Calculator c = new Calculator();
		c.fieldsList.add(new Anisotrophia(0, 0));
		c.fieldsList.add(new Circular(0, 0));


		for (double tetta = 0.1; tetta < 0.101; tetta += 0.1) {
			((Anisotrophia) c.fieldsList.get(Anisotrophia.class)).
				setVector(new Vector(Math.PI * tetta, 0));


			for (double h = 0.25; h <= 0.251; h += 0.05) {
				List<Double> list = new LinkedList<Double>();
				for (double w = 0.95; w <= 1.201; w += 0.01) {
					System.out.println(new Date().toString());
					System.out.println("Tetta = " + tetta);
					System.out.println("h = " + h);
					System.out.println("w = " + w);
					System.out.println();
					((Circular) c.fieldsList.get(Circular.class)).setNewData(w, h);
					c.run();
					list.add(c.getEnergy());


					String trackName;
					if (PeriodCounter.isQ)
						trackName = "Track: (t: "+ tetta +", h: " + h + ", w: " + w + " (Q))";
					else
						trackName = "Track: (t: "+ tetta +", h: " + h + ", w: " + w + ")";


					new Draw(c, 0.4 * Math.PI, 0.4 * Math.PI, 0, "res/" + trackName).drawTraectory(true);
					//DrawComponents.draw(c, "comp. " + trackName);
					//DrawComponents.normal(c.pc.energyList, "energ. " + trackName);


				}
				Writer.writeDoubleList(list, "Tetta = " + tetta + ", (h = " + h +")");
			}
		}
	}

}