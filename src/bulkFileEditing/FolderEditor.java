package bulkFileEditing;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Iterator;

public class FolderEditor {

    public static void rename(String hFolder) {
        String path = "res/angle_step = 0.1/" + hFolder;

        File folder = new File(path);
        String[] names = folder.list();

        for(String name : names) {
            File file = new File(path + "/" + name);
            String[] cons = file.list();
            for(String c : cons) {

            //    if (!c.contains("Energy.txt") && !c.contains("track"))
                if (c.contains("Average M_x"))
                    new File(path + "/" + name + "/" + c).renameTo(new File(path + "/" + name + "/" + name + " M_x.txt"));
                if (c.contains("Average M_y"))
                    new File(path + "/" + name + "/" + c).renameTo(new File(path + "/" + name + "/" + name + " M_y.txt"));
                if (c.contains("Average M_z"))
                    new File(path + "/" + name + "/" + c).renameTo(new File(path + "/" + name + "/" + name + " M_z.txt"));
            }

        }
    }

    public static void deleteFiles(String path) {

        File folder = new File(path);
        String[] names = folder.list();

        for(String name : names) {
            File folder2 = new File(path + "/" + name);
            String[] cons = folder2.list();
            for(String c : cons) {

                File file = new File(path + "/" + name + "/" + c);
                String[] in = file.list();
                for(String c2 : in)
                    if (c2.equals("track")) {
                        System.out.println(path + "/" + name + "/" + c + "/" + c2);
                        File deleteFile = new File(path + "/" + name + "/" + c + "/" + c2);
                        deleteDirectory(deleteFile);
                    }
            }

        }
    }

    private static boolean deleteDirectory(File directory) {
        if (directory.exists()){
            File[] files = directory.listFiles();
            if (null != files)
                for (File file : files)
                    if(file.isDirectory())
                        deleteDirectory(file);
                    else
                        file.delete();
        }
        return directory.delete();
    }


    public static void copyFolder(String hFolder) {
        String path = "txts/angle_step = 0.05/" + hFolder;

        File folder = new File(path);
        String[] names = folder.list();

        for(String name : names) {
            File file = new File(path + "/" + name);
            String[] cons = file.list();
            for(String c : cons)
                if (c.contains("Average M_z")) {
                    try {
                        Files.copy(new File(path + "/" + name + "/" + c).toPath(), new File(path + "/some/" + c).toPath(), StandardCopyOption.REPLACE_EXISTING);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
        }
    }

    private static void parseName(String name) {
        //h = 0.1;theta=0.3;fi=0.1 h = 0.1;theta=0.3;fi=0.1 h = 0.1;theta=0.3;fi=0.1
        System.out.println("-----------------------------------");
        System.out.println(name);
        double h = Double.parseDouble(name.substring(name.indexOf('=') + 1, name.indexOf(';')));
        System.out.println(h);
        double theta = Double.parseDouble(name.substring(name.indexOf('a') + 2, name.indexOf('f') - 1));
        System.out.println(theta);

        System.out.println(name.charAt(name.indexOf('i') + 5));

        double fi;
        if (name.charAt(name.indexOf('i') + 6) == ' ')
            fi = Double.parseDouble(name.substring(name.indexOf('i') + 2, name.indexOf('i') + 5));
        else
            fi = Double.parseDouble(name.substring(name.indexOf('i') + 2, name.indexOf('i') + 6));
        System.out.println(fi);
        System.out.println("-----------------------------------");
    }



    public static void averrageComponents(String hFolder) throws NumberFormatException, IOException {

        System.out.println(hFolder);
        String path = "res/angle_step = 0.05/" + hFolder;

        File folder = new File(path);
        String[] names = folder.list();

        ArrayList<Double> listX = new ArrayList<Double>();
        ArrayList<Double> listY = new ArrayList<Double>();
        ArrayList<Double> listZ = new ArrayList<Double>();
        for (int i = 0; i < 200; i++) {
            listX.add(0d);
            listY.add(0d);
            listZ.add(0d);
        }
        int counter = 39;

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


            }
        }
        TextWriter.writeDoubleList(multiple(listX, 1 / (double) counter), hFolder + " Averrage X");
        TextWriter.writeDoubleList(multiple(listY, 1 / (double) counter), hFolder + " Averrage Y");
        TextWriter.writeDoubleList(multiple(listZ, 1 / (double) counter), hFolder + " Averrage Z");

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

}
