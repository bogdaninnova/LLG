package bulkFileEditing;

import painting.Draw;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
public class DrawQW {

    private final static int sizeW = 2000;
    private final static int sizeH = 1000;
    BufferedImage bi = new BufferedImage(sizeW, sizeH, BufferedImage.TYPE_4BYTE_ABGR);
    Graphics2D g = Draw.getBackgroundedGraphics2D(bi, Color.white);

    public DrawQW() {
        g.setStroke(new BasicStroke(3));
    }

    public void wright(String path) {

        List<Double> list = null;
        try {
            list = readDoubleListList(path);
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }

        wright(list, g, Color.BLACK);
    }

    public void save(String name) {
        Draw.save(bi, new File(name + ".png"));
    }

    private void wright(List<Double> list, Graphics2D g, Color color) {

        ListIterator<Double> iter = list.listIterator();

        double step = (double) sizeW / (double) list.size();
        double coefQ = (double) sizeH / getMax(list);

        double x1 = 0;
        double x2 = step;

        double y1 = sizeH;
        double y2 = sizeH - coefQ * iter.next();

        g.setColor(color);
        while (iter.hasNext()) {
            g.drawLine( (int) x1, (int) y1, (int) x2, (int) y2);

            x1 = x2;
            x2 += step;

            y1 = y2;
            y2 = sizeH - iter.next() * coefQ;

        }
    }

    private static double getMax(List<Double> list) {
        ListIterator<Double> iter = list.listIterator();
        double max = 0;

        while (iter.hasNext()) {
            double current = iter.next();
            if (current > max)
                max = current;
        }
        return max;
    }


    public static ArrayList<Double> readDoubleListList(String path) {

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





}
