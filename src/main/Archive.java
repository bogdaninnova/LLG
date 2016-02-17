package main;

import bulkFileEditing.*;
import main.fields.Anisotrophia;
import main.fields.Circular;
import main.fields.Lineal;
import painting.Draw;

import java.io.*;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.ListIterator;

/**
 * Created by bope0915 on 1/27/2016.
 */
public final class Archive {
    private Archive() {}

    public static void Q_w(double h, double w1, double w2) {
        CartesianCalculation c = new CartesianCalculation();
        ArrayList<Double> wList = new ArrayList<Double>();
        ArrayList<Vector> mList = new ArrayList<Vector>();
        for (double w = w1; w <= w2; w = round(w + 0.01, 2)) {
            double wSum = 0;
            Vector m_aver = new Vector();
            double counter = 0;
            for (double theta = 0.0; theta <= 1; theta = round(theta + 0.05, 2)) {
                System.out.println("\n" + new Date());
                System.out.println("h = " + h + ". W = " + w + ". Theta: " + theta);
                for (double fi = 0; fi < 1; fi = round(fi + 0.05, 2)) {

                    if (theta == 0.5)
                        if ((fi == 0) || (fi == 0.5) || (fi == 1)) {
                            counter++;
                            continue;
                        }
                    c.setFields(
                            new Anisotrophia(Math.acos(2 * theta - 1), 2 * Math.PI * fi),
                            new Lineal(new Vector(1, 0, 0), w, h));
                    c.run();
                    wSum += c.getEnergy();
                    m_aver = m_aver.plus(c.getM_aver());
                    counter++;
                }
            }
            mList.add(m_aver.multiply(1 / counter));
            wList.add(wSum / counter);

            TextWriter.writeDoubleList(wList, "Energy h = " + h + ". From w = " + w1 + " to w = " + w2);
            TextWriter.writeTraectorysCoordinates(mList, "M_aver h = " + h + ". From w = " + w1 + " to w = " + w2);
        }
    }

    public static ArrayList<ArrayList<Double>> averrageComponents(String path) throws NumberFormatException {

        //System.out.println(hFolder);

        File folder = new File(path);
        String[] names = folder.list();

        ArrayList<Double> listX = new ArrayList<Double>();
        ArrayList<Double> listY = new ArrayList<Double>();
        ArrayList<Double> listZ = new ArrayList<Double>();
        ArrayList<Double> listE = new ArrayList<Double>();
        for (int i = 0; i < 200; i++) {
            listX.add(0d);
            listY.add(0d);
            listZ.add(0d);//for lineal field need to make something TODO
            listE.add(0d);
        }
        int counter = 40;
        System.out.println();
        for(String name : names) {
            File file = new File(path + "/" + name);
            String[] cons = file.list();
            for(String c : cons) {

                if (c.contains("M_x")) {
                    if (c.contains("theta=0.0;") || c.contains("theta=1.0;")) {
                        listX = addLists(listX, multiple(DrawQW.readDoubleListList(path + "/" + name + "/" + c), 20));
                    } else {
                        listX = addLists(listX, DrawQW.readDoubleListList(path + "/" + name + "/" + c));
                    }
                }
                if (c.contains("M_y")) {

                    if (c.contains("theta=0.0;") || c.contains("theta=1.0;")) {
                        listY = addLists(listY, multiple(DrawQW.readDoubleListList(path + "/" + name + "/" + c), 20));
                    } else {
                        listY = addLists(listY, DrawQW.readDoubleListList(path + "/" + name + "/" + c));
                    }
                }
                if (c.contains("M_z")) {
                    counter++;
                    if (c.contains("theta=0.0;") || c.contains("theta=1.0;")) {
                        listZ = addLists(listZ, multiple(DrawQW.readDoubleListList(path + "/" + name + "/" + c), 20));
                    } else {
                        listZ = addLists(listZ, DrawQW.readDoubleListList(path + "/" + name + "/" + c));
                    }
                }

                if (c.contains("Energy")) {
                    //counter++;
                    if (c.contains("theta=0.0;") || c.contains("theta=1.0;")) {
                        listE = addLists(listE, multiple(DrawQW.readDoubleListList(path + "/" + name + "/" + c), 20));
                    } else {
                        listE = addLists(listE, DrawQW.readDoubleListList(path + "/" + name + "/" + c));
                    }
                }
            }
        }
//        TextWriter.writeDoubleList(multiple(listX, 1 / (double) counter), h + " Average X");
//        TextWriter.writeDoubleList(multiple(listY, 1 / (double) counter), h + " Average Y");
//        TextWriter.writeDoubleList(multiple(listZ, 1 / (double) counter), h + " Average Z");
//        TextWriter.writeDoubleList(multiple(listE, 1 / (double) counter), h + " Average E");

        ArrayList<ArrayList<Double>> result = new ArrayList<>();
        result.add(multiple(listX, 1 / (double) counter));
        result.add(multiple(listY, 1 / (double) counter));
        result.add(multiple(listZ, 1 / (double) counter));
        result.add(multiple(listE, 1 / (double) counter));

        return result;
    }



    public static ArrayList<ArrayList<Double>> averrageComponentsWithout20(String path) throws NumberFormatException {

        //System.out.println(hFolder);

        File folder = new File(path);
        String[] names = folder.list();

        ArrayList<Double> listX = new ArrayList<Double>();
        ArrayList<Double> listY = new ArrayList<Double>();
        ArrayList<Double> listZ = new ArrayList<Double>();
        ArrayList<Double> listE = new ArrayList<Double>();
        for (int i = 0; i < 200; i++) {
            listX.add(0d);
            listY.add(0d);
            listZ.add(0d);
            listE.add(0d);
        }
        int counter = 0;
        for(String name : names) {
            File file = new File(path + "/" + name);
            String[] cons = file.list();
            for(String c : cons) {

                if (c.contains("M_x"))
                   listX = addLists(listX, DrawQW.readDoubleListList(path + "/" + name + "/" + c));

                if (c.contains("M_y"))
                     listY = addLists(listY, DrawQW.readDoubleListList(path + "/" + name + "/" + c));

                if (c.contains("M_z")) {
                    counter++;
                    listZ = addLists(listZ, DrawQW.readDoubleListList(path + "/" + name + "/" + c));
                }

                if (c.contains("Energy"))
                    listE = addLists(listE, DrawQW.readDoubleListList(path + "/" + name + "/" + c));
            }
        }
        ArrayList<ArrayList<Double>> result = new ArrayList<>();
        result.add(multiple(listX, 1 / (double) counter));
        result.add(multiple(listY, 1 / (double) counter));
        result.add(multiple(listZ, 1 / (double) counter));
        result.add(multiple(listE, 1 / (double) counter));

        return result;
    }


    public static ArrayList<ArrayList<Double>> averrageComponentsForRandom(String path, int dots) throws NumberFormatException {

        File folder = new File(path);
        String[] names = folder.list();

        ArrayList<Double> listX = new ArrayList<Double>();
        ArrayList<Double> listY = new ArrayList<Double>();
        ArrayList<Double> listZ = new ArrayList<Double>();
        ArrayList<Double> listE = new ArrayList<Double>();
        for (int i = 0; i < 200; i++) {
            listX.add(0d);
            listY.add(0d);
            listZ.add(0d);
            listE.add(0d);
        }
        int counter = 0;
        System.out.println();
        for(String name : names) {
            File file = new File(path + "/" + name);
            String[] cons = file.list();
            for(String c : cons) {

                if (c.contains("M_x"))
                    listX = addLists(listX, DrawQW.readDoubleListList(path + "/" + name + "/" + c));

                if (c.contains("M_y"))
                     listY = addLists(listY, DrawQW.readDoubleListList(path + "/" + name + "/" + c));

                if (c.contains("M_z"))
                    listZ = addLists(listZ, DrawQW.readDoubleListList(path + "/" + name + "/" + c));

                if (c.contains("Energy")) {
                    counter++;
                    listE = addLists(listE, DrawQW.readDoubleListList(path + "/" + name + "/" + c));
                }

                if (counter == dots) break;
            }
            if (counter == dots) break;
        }
        ArrayList<ArrayList<Double>> result = new ArrayList<>();
        result.add(multiple(listX, 1 / (double) counter));
        result.add(multiple(listY, 1 / (double) counter));
        result.add(multiple(listZ, 1 / (double) counter));
        result.add(multiple(listE, 1 / (double) counter));

        return result;
    }




    private static ArrayList<Double> multiple(ArrayList<Double> list, double num) {
        ArrayList<Double> newList = new ArrayList<Double>();
        Iterator<Double> iter = list.iterator();
        while (iter.hasNext())
            newList.add(iter.next() * num);
        return newList;
    }


    private static ArrayList<Double> addLists(ArrayList<Double> list1, ArrayList<Double> list2) {

        if (list1.size() != list2.size())
            return null;
        ArrayList<Double> list = new ArrayList<Double>();
        Iterator<Double> iter1 = list1.iterator();
        Iterator<Double> iter2 = list2.iterator();
        while (iter1.hasNext())
            list.add(iter1.next() + iter2.next());
        return list;
    }

    public static void bulkWright(String path, String contain, String resultName) {
        DrawQWSet dr = new DrawQWSet();
        Vector easyAxe = new Vector(1, 0, 0);

        File folder = new File(path);
        String[] names = folder.list();
        ArrayList<Double> listAngles = new ArrayList<Double>();
        ArrayList<Double> listTheta = new ArrayList<Double>();
        ArrayList<Double> listFi = new ArrayList<Double>();
        for(String name : names) {
            File file = new File(path + "/" + name);
            String[] cons = file.list();
            for(String c : cons)
                if (c.contains(contain)) {
                    double[] data = FolderEditor.parseName(name);
                    double angle = Math.acos(new Vector(data[1], data[2]).dotProduct(easyAxe)) / Math.PI;
                    int d = dr.addTrack(path + "/" + name + "/" + c);
                    for (int i = 0; i < d; i++) {
                        listAngles.add(angle);
                        listTheta.add(data[1]);
                        listFi.add(data[2]);
                    }
                }
        }
//        TextWriter.writeDoubleList(listAngles, "angles");
//        TextWriter.writeDoubleList(listTheta, "theta");
//        TextWriter.writeDoubleList(listFi, "fi");
        dr.wright();
        dr.save(resultName);
    }

    public static void getMaximalDot(String path) {

        Vector easyAxe = new Vector(1, 0, 0);
        double max = -999;
        double cur = 0;
        File folder = new File(path);
        String[] names = folder.list();
        for(String name : names) {
            File file = new File(path + "/" + name);
            String[] cons = file.list();
            for(String c : cons)
                if (c.contains("Energy")) {
                    cur = getMaxFromTrack(readDoubleListList(path + "/" + name + "/" + c));
                    if ((cur > max) && (!"h = 0.01;theta=2.34619382340565;fi=4.71238898038469".equals(name))) {
                        max = cur;
                        System.out.println(name);
                    }
                }
        }
    }

    private static double getMaxFromTrack(ArrayList<Double> list) {
        double max = 0;
        double step = 0;
        double w = 0;
        ListIterator<Double> iter = list.listIterator();
        while (iter.hasNext()) {
            double current = iter.next();
            step++;
            if (current > max) {
                max = current;
                w = step;
            }
        }
        return max;
    }

    private static ArrayList<Double> readDoubleListList(String path) {

        BufferedReader in = null;
        try {
            in = new BufferedReader(new FileReader(new File(path)));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        ArrayList<Double> list = new ArrayList<Double>();

        String a;

        try {
            while ((a = in.readLine()) != null)
                list.add(Double.parseDouble(a));
        } catch (IOException e) {
            e.printStackTrace();
        }

        return list;
    }

    public static double parametricResonanse(double delta, double h, double w, String path) {

        //Date start = new Date();

        CartesianCalculation c = new CartesianCalculation(
                new Anisotrophia(0.5 * Math.PI + delta, 0),
                new Lineal(new Vector(1,0,0), w, h));
        //c.run(500, 500);
        c.run();

//		new Draw(c.getArray(),
//				((Anisotrophia) c.fieldsList.get(Anisotrophia.class)).getAxe(),
//				0.4 * Math.PI, 0.4 * Math.PI, 0, path).drawTraectory(true);

        return c.getEnergy();
    }


    public static void speedTest(double theta, double fi, double h, double w) {
        String path = "theta=" + theta + ";fi=" + fi + ";h=" + h + ";w=" + w;

        Date d0 = new Date();

        CartesianCalculation cc = new CartesianCalculation(new Anisotrophia(theta, fi), new Circular(w, h));
        cc.run(0, 2000);
        new Draw(cc.getArray(), new Vector(theta, fi), 0.4 * Math.PI, 0.4 * Math.PI, 0, "CC-" + path).drawTraectory(true);

        Date d1 = new Date();
        System.out.println(d1.getTime()-d0.getTime());

        SphericalCalculation sc = new SphericalCalculation(theta, fi, h, w);
        sc.run(500, 500);
        new Draw(sc.array, new Vector(theta, fi), 0.4 * Math.PI, 0.4 * Math.PI, 0, "SC-" + path).drawTraectory(true);

        System.out.println(new Date().getTime()-d1.getTime());
    }

    public static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        BigDecimal bd = new BigDecimal(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }
}
