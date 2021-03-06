package bulkFileEditing;

import painting.Draw;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.*;
import java.util.List;
public class DrawGraphics {

    private final static int sizeW = 2000;
    private final static int sizeH = 1000;
    BufferedImage bi = new BufferedImage(sizeW, sizeH, BufferedImage.TYPE_4BYTE_ABGR);
    Graphics2D g = Draw.getBackgroundedGraphics2D(bi, Color.white);

    private Set<ArrayList<Double>> set = new HashSet<>();

    double step;
    double coefQ;


    public DrawGraphics() {
        g.setStroke(new BasicStroke(3));
    }

    private void wright() {

        step = (double) sizeW / (double) set.iterator().next().size();
        coefQ = (double) sizeH / getMaxFromSet(set);

        for (ArrayList<Double> list : set)
            wright(list, g, Color.BLACK);
    }

    public void addTrack(String path) {
        set.add(TextReader.readDoubleListList(path));
    }

    public void save(String name) {
        wright();
        Draw.save(bi, new File(name + ".png"));
    }

    private void wright(List<Double> list, Graphics2D g, Color color) {

        ListIterator<Double> iter = list.listIterator();

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

    private static double getMaxFromSet(Set <ArrayList<Double>> set) {
        double max = 0;
        double current;
        for (ArrayList<Double> list : set)  {
            current = getMaxFromTrack(list);
            if (current > max)
                max = current;
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
}