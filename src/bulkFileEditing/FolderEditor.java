package bulkFileEditing;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

public class FolderEditor {

    public static void rename() {
        String path = "txts/angle_step = 0.05/h = 0.3";

        File folder = new File(path);
        String[] names = folder.list();

        for(String name : names) {
            File file = new File(path + "/" + name);
            String[] cons = file.list();
            for(String c : cons)
                if (c.contains("nergy")) {
                    new File(path + "/" + name + "/" + "Energy.txt").renameTo(new File(path + "/" + name + "/" + name + " energy.txt"));
                }

        }
    }

    public static void copyFolder() {
        String path = "txts/angle_step = 0.05/h = 0.3";

        File folder = new File(path);
        String[] names = folder.list();

        for(String name : names) {
            File file = new File(path + "/" + name);
            String[] cons = file.list();
            for(String c : cons)
                if (c.contains("nergy")) {
                    try {
                        Files.copy(new File(path + "/" + name + "/" + c).toPath(), new File(path + "/some/" + c).toPath(), StandardCopyOption.REPLACE_EXISTING);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

        }
    }

}
