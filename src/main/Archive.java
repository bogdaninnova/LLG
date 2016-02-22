package main;

import bulkFileEditing.DrawQW;
import bulkFileEditing.DrawQWSet;
import bulkFileEditing.FolderEditor;
import bulkFileEditing.TextWriter;
import main.fields.Anisotrophia;
import main.fields.Lineal;

import java.io.*;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;

public final class Archive {
    private Archive() {}

    public static void Q_w(double h, double w1, double w2) {
        CartesianCalculation c = new CartesianCalculation();
        ArrayList<Double> wList = new ArrayList<>();
        ArrayList<Vector> mList = new ArrayList<>();
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

        ArrayList<Double> listX = new ArrayList<>();
        ArrayList<Double> listY = new ArrayList<>();
        ArrayList<Double> listZ = new ArrayList<>();
        ArrayList<Double> listE = new ArrayList<>();
        for (int i = 0; i < 200; i++) {
            listX.add(0d);
            listY.add(0d);
            listZ.add(0d);
            listE.add(0d);
        }
        int counter = 0;
        if (path.contains("lineal"))
            counter = 2;
        System.out.println();
        for(String name : names) {
            File file = new File(path + "/" + name);
            String[] cons = file.list();
            for(String c : cons) {

                if (c.contains("M_x")) {
                    if ((name.contains("theta=0.0;") || name.contains("theta=1.0;")) && (path.contains("lineal") || path.contains("elliptical"))) {
                        listX = addLists(listX, multiple(DrawQW.readDoubleListList(path + "/" + name + "/" + c), 20));
                    } else {
                        listX = addLists(listX, DrawQW.readDoubleListList(path + "/" + name + "/" + c));
                    }
                }
                if (c.contains("M_y")) {

                    if ((name.contains("theta=0.0;") || name.contains("theta=1.0;")) && (path.contains("lineal") || path.contains("elliptical"))) {
                        listY = addLists(listY, multiple(DrawQW.readDoubleListList(path + "/" + name + "/" + c), 20));
                    } else {
                        listY = addLists(listY, DrawQW.readDoubleListList(path + "/" + name + "/" + c));
                    }
                }
                if (c.contains("M_z")) {
                    if ((name.contains("theta=0.0;") || name.contains("theta=1.0;")) && (path.contains("lineal") || path.contains("elliptical"))) {
                        listZ = addLists(listZ, multiple(DrawQW.readDoubleListList(path + "/" + name + "/" + c), 20));
                    } else {
                        listZ = addLists(listZ, DrawQW.readDoubleListList(path + "/" + name + "/" + c));
                    }
                }

                if (c.contains("Energy")) {
                    if ((name.contains("theta=0.0;") || name.contains("theta=3.141592653")) && (path.contains("lineal") || path.contains("elliptical"))) {
                        listE = addLists(listE, multiple(DrawQW.readDoubleListList(path + "/" + name + "/" + c), 20));
                        counter += 20;
                    } else {
                        listE = addLists(listE, DrawQW.readDoubleListList(path + "/" + name + "/" + c));
                        counter++;
                    }
                }
            }
        }

        System.out.println(counter);

        ArrayList<ArrayList<Double>> result = new ArrayList<>();
        result.add(multiple(listX, 1 / (double) counter));
        result.add(multiple(listY, 1 / (double) counter));
        result.add(multiple(listZ, 1 / (double) counter));
        result.add(multiple(listE, 1 / (double) counter));

        return result;
    }


    public static ArrayList<ArrayList<Double>> averrageComponentsForRandom(String path) throws NumberFormatException {

        File folder = new File(path);
        String[] names = folder.list();

        ArrayList<Double> listX = new ArrayList<>();
        ArrayList<Double> listY = new ArrayList<>();
        ArrayList<Double> listZ = new ArrayList<>();
        ArrayList<Double> listE = new ArrayList<>();
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

                if (c.contains("M_z"))
                    listZ = addLists(listZ, DrawQW.readDoubleListList(path + "/" + name + "/" + c));

                if (c.contains("Energy")) {
                    counter++;
                    listE = addLists(listE, DrawQW.readDoubleListList(path + "/" + name + "/" + c));
                }
            }
        }
        ArrayList<ArrayList<Double>> result = new ArrayList<>();
        result.add(multiple(listX, 1 / (double) counter));
        result.add(multiple(listY, 1 / (double) counter));
        result.add(multiple(listZ, 1 / (double) counter));
        result.add(multiple(listE, 1 / (double) counter));

        return result;
    }




    private static ArrayList<Double> multiple(ArrayList<Double> list, double num) {
        ArrayList<Double> newList = new ArrayList<>();
        Iterator<Double> iter = list.iterator();
        while (iter.hasNext())
            newList.add(iter.next() * num);
        return newList;
    }


    private static ArrayList<Double> addLists(ArrayList<Double> list1, ArrayList<Double> list2) {

        if (list1.size() != list2.size())
            return null;
        ArrayList<Double> list = new ArrayList<>();
        Iterator<Double> iter1 = list1.iterator();
        Iterator<Double> iter2 = list2.iterator();
        while (iter1.hasNext())
            list.add(iter1.next() + iter2.next());
        return list;
    }

    public static void bulkWright(String path, String contain, String resultName) {
        DrawQWSet dr = new DrawQWSet();
        File folder = new File(path);
        String[] names = folder.list();
        for(String name : names) {
            File file = new File(path + "/" + name);
            String[] cons = file.list();
            for(String c : cons)
                if (c.contains(contain))
                    dr.addTrackWithJump(path + "/" + name + "/" + c);
        }
        dr.save(resultName);
    }

    public static void jumpAnalysis(String path, String contain, String resultName) {
        DrawQWSet dr = new DrawQWSet();
        Vector easyAxe = new Vector(1, 0, 0);

        File folder = new File(path);
        String[] names = folder.list();
        ArrayList<Double> listAngles = new ArrayList<>();
        ArrayList<Double> listTheta = new ArrayList<>();
        ArrayList<Double> listFi = new ArrayList<>();
        for(String name : names) {
            File file = new File(path + "/" + name);
            String[] cons = file.list();
            for(String c : cons)
                if (c.contains(contain)) {
                    double[] data = FolderEditor.parseName(name);
                    double angle = Math.acos(new Vector(data[1], data[2]).dotProduct(easyAxe)) / Math.PI;
                    int d = dr.addTrackWithJump(path + "/" + name + "/" + c);
                    for (int i = 0; i < d; i++) {
                        listAngles.add(angle);
                        listTheta.add(data[1]);
                        listFi.add(data[2]);
                    }
                }
        }
        TextWriter.writeDoubleList(listAngles, "angles");
        TextWriter.writeDoubleList(listTheta, "theta");
        TextWriter.writeDoubleList(listFi, "fi");
        dr.save(resultName);
    }

    public static double getMaximalDot(String path) {

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
        return max;
    }

    private static double getMaxFromTrack(ArrayList<Double> list) {
        double max = 0;
        for (Double current : list)
            if (current > max)
                max = current;
        return max;
    }

    private static ArrayList<Double> readDoubleListList(String path) {

        BufferedReader in = null;
        try {
            in = new BufferedReader(new FileReader(new File(path)));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        ArrayList<Double> list = new ArrayList<>();

        String a;

        try {
            assert in != null;
            while ((a = in.readLine()) != null)
                list.add(Double.parseDouble(a));
        } catch (IOException e) {
            e.printStackTrace();
        }

        return list;
    }

    public static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        BigDecimal bd = new BigDecimal(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }


    public static void createGraphics(String path) {

        File folder = new File(path);
        String[] names = folder.list();
        String fileName;
        for(String name : names) {
            File file = new File(path + "/" + name);
            String[] cons = file.list();
            for(String c : cons)
                if ((c.contains("Average M_") || c.contains("Energy")) && (!c.contains(".png"))) {
                    DrawQWSet draw = new DrawQWSet();
                    draw.addTrack(path + "/" + name + "/" + c);
                    fileName = c.substring(0, c.length() - 4);
                    draw.save(path + "/" + name + "/" + fileName);
                }
        }
    }


}
