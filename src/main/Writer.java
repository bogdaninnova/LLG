package main;


import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

public class Writer {

	@SuppressWarnings("resource")//damn
	public static void writeDoubleList(List<Double> averrageList, String name) {
		
		ListIterator<Double> iter = averrageList.listIterator();
		
		File file = new File(name + ".txt");
		try {
			FileWriter writer = new FileWriter(file);

			while (iter.hasNext())
				writer.append(iter.next() + "\r\n");
			
			writer.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@SuppressWarnings("resource")
	public static void writeIntegerList(LinkedList<Integer> averrageList, String name) {
		
		ListIterator<Integer> iter = averrageList.listIterator();
		
		File file = new File(name + ".txt");
		try {
			FileWriter writer = new FileWriter(file);

			while (iter.hasNext()) writer.append(iter.next() + "\r\n");
			
			writer.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@SuppressWarnings("resource")
	public static void writeTraectorysCoordinates(List<Vector> list, String name) {
		
		//String tittle = name + new SimpleDateFormat("yyyy-MM-dd HH-mm-ss.SSS").format(new Date());
		
		File xFile = new File(name + "_x.txt");
		File yFile = new File(name + "_y.txt");
		File zFile = new File(name + "_z.txt");
		
		ListIterator<Vector> iter = list.listIterator();
		
		try {
			FileWriter xWriter = new FileWriter(xFile);
			FileWriter yWriter = new FileWriter(yFile);
			FileWriter zWriter = new FileWriter(zFile);

			Vector dot;
			
		while (iter.hasNext()){
				dot = iter.next();
				xWriter.append(dot.getX() + "\r\n");
				yWriter.append(dot.getY() + "\r\n");
				zWriter.append(dot.getZ() + "\r\n");
			}
			xWriter.flush();
			yWriter.flush();
			zWriter.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
