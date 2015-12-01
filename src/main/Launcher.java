package main;

import main.fields.Anisotrophia;
import main.fields.Circular;
import painting.Draw;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

public class Launcher {

	public static void main(String...strings) {
		//longStory();
		System.out.println("test");
		//oneParticle(0.0, 0.1, 0.5);
	}




	public static void oneParticle(double tetta, double h, double w) {



		Calculator c = new Calculator();
		c.fieldsList.add(new Anisotrophia(Math.PI * tetta, 0));
		c.fieldsList.add(new Circular(w, h));

		c.startVector = new Vector(Math.PI * tetta, 0);
		c.run(1198, 2);

	//	System.out.println(new Date().getTime() - date.getTime());

	//	System.out.println(c.getEnergy());
		System.out.println(c.lsList.get(c.lsList.size()-1));

		String trackName;
		if (PeriodCounter.isQ)
			trackName = "(t= "+ tetta +", h= " + h + ", w= " + w + " (Q))";
		else
			trackName = "(t= "+ tetta +", h= " + h + ", w= " + w + ")";

		Writer.writeDoubleList(c.lsList, "Lypunoff");
		new Draw(c, 0.4 * Math.PI, 0.4 * Math.PI, 0, trackName).drawTraectory(true);
		
		System.out.println("Start");
		Date date = new Date();
		List<Vector> list = Vector.grammShmidtOrthogonal(c.getArray());
		System.out.println(list.get(list.size()-1).getX());
		System.out.println(new Date().getTime() - date.getTime());
		
		//DrawComponents.draw(c, "comp. " + trackName);
		//DrawComponents.normal(c.pc.energyList, "energ. " + trackName);

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


					new Draw(c, 0.4 * Math.PI, 0.4 * Math.PI, 0, trackName).drawTraectory(true);
					//DrawComponents.draw(c, "comp. " + trackName);
					//DrawComponents.normal(c.pc.energyList, "energ. " + trackName);


				}
				Writer.writeDoubleList(list, "Tetta = " + tetta + ", (h = " + h +")");
			}
		}
	}

}
