package bulkFileEditing;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

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

}
