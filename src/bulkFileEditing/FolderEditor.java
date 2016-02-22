package bulkFileEditing;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

public class FolderEditor {

    public static void rename(String path) {

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

    public static void deleteFiles(String path, String... patternForDeleting) {

        File folder = new File(path);
        String[] names = folder.list();

        for(String name : names) {
            File folder2 = new File(path + "/" + name);
            String[] cons = folder2.list();
            for(String c : cons) {
                if (isContainsPattern(c, patternForDeleting)) {
                    System.out.println(path + "/" + name + "/" + c);
                    File deleteFile = new File(path + "/" + name + "/" + c);
                    deleteDirectory(deleteFile);
                }
            }
        }
    }

    private static boolean isContainsPattern(String name, String... patternForDeleting) {
        for (String pattern : patternForDeleting)
            if (name.contains(pattern))
                return true;
        return false;
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

    public static double[] parseName(String name) {
        double h = Double.parseDouble(name.substring(name.indexOf('=') + 1, name.indexOf(';')));
        double theta = Double.parseDouble(name.substring(name.indexOf('a') + 2, name.indexOf('f') - 1));
        double fi = Double.parseDouble(name.substring(name.indexOf('i') + 2, name.length()));
        return new double[]{h, theta, fi};
    }

}