package main;

import bulkFileEditing.DrawGraphics;
import bulkFileEditing.TextReader;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;

public final class Archive {
    private Archive() {}

    public static ArrayList<ArrayList<Double>> averrageComponents(String path) throws NumberFormatException {

        //System.out.println(hFolder);

        File folder = new File(path);
        String[] names = folder.list();

        ArrayList<Double> listX = new ArrayList<>();
        ArrayList<Double> listY = new ArrayList<>();
        ArrayList<Double> listZ = new ArrayList<>();
        ArrayList<Double> listE = new ArrayList<>();
        for (int i = 0; i < 191; i++) {
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
                        listX = addLists(listX, multiple(TextReader.readDoubleListList(path + "/" + name + "/" + c), 20));
                    } else {
                        listX = addLists(listX, TextReader.readDoubleListList(path + "/" + name + "/" + c));
                    }
                }
                if (c.contains("M_y")) {

                    if ((name.contains("theta=0.0;") || name.contains("theta=1.0;")) && (path.contains("lineal") || path.contains("elliptical"))) {
                        listY = addLists(listY, multiple(TextReader.readDoubleListList(path + "/" + name + "/" + c), 20));
                    } else {
                        listY = addLists(listY, TextReader.readDoubleListList(path + "/" + name + "/" + c));
                    }
                }
                if (c.contains("M_z")) {
                    if ((name.contains("theta=0.0;") || name.contains("theta=1.0;")) && (path.contains("lineal") || path.contains("elliptical"))) {
                        listZ = addLists(listZ, multiple(TextReader.readDoubleListList(path + "/" + name + "/" + c), 20));
                    } else {
                        listZ = addLists(listZ, TextReader.readDoubleListList(path + "/" + name + "/" + c));
                    }
                }

                if (c.contains("Energy")) {
                    if ((name.contains("theta=0.0;") || name.contains("theta=3.141592653")) && (path.contains("lineal") || path.contains("elliptical"))) {
                        listE = addLists(listE, multiple(TextReader.readDoubleListList(path + "/" + name + "/" + c), 20));
                        counter += 20;
                    } else {
                        listE = addLists(listE, TextReader.readDoubleListList(path + "/" + name + "/" + c));
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
        DrawGraphics dr = new DrawGraphics();
        File folder = new File(path);
        String[] names = folder.list();
        for(String name : names) {
            File file = new File(path + "/" + name);
            String[] cons = file.list();
            for(String c : cons)
                if (c.contains(contain))
                    dr.addTrack(path + "/" + name + "/" + c);
        }
        dr.save(resultName);
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
                    DrawGraphics draw = new DrawGraphics();
                    draw.addTrack(path + "/" + name + "/" + c);
                    fileName = c.substring(0, c.length() - 4);
                    draw.save(path + "/" + name + "/" + fileName);
                }
        }
    }




}
