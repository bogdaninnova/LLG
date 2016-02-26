package bulkFileEditing;

import java.io.*;
import java.util.ArrayList;

public class TextReader {

    public static ArrayList<Double> readDoubleListList(String path) {
        BufferedReader in = null;
        try {
            in = new BufferedReader(new FileReader(new File(path)));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        ArrayList<Double> list = new ArrayList<>();
        String a;
        try {
            while ((a = in.readLine()) != null)
                list.add(Double.parseDouble(a));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return list;
    }

}
