package main;


import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;

public class Writer {

	@SuppressWarnings("resource")//damn
	public static void writeDoubleList(List<Double> averrageList, String name) {
		
		ListIterator<Double> iter = averrageList.listIterator();
		
		File file = new File("res/" + name + ".txt");
		try {
			FileWriter writer = new FileWriter(file);

			while (iter.hasNext()) writer.append(iter.next() + "\r\n");
			
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
		
		String tittle = name + new SimpleDateFormat("yyyy-MM-dd HH-mm-ss.SSS").format(new Date());
		
		File xFile = new File("x_" + tittle + ".txt");
		File yFile = new File("y_" + tittle + ".txt");
		File zFile = new File("z_" + tittle + ".txt");
		
		ListIterator<Vector> iter = list.listIterator();
		
		try {
			FileWriter xWriter = new FileWriter(xFile);
			FileWriter yWriter = new FileWriter(yFile);
			FileWriter zWriter = new FileWriter(zFile);

			Vector dot;
			
		while (iter.hasNext()){
				dot = iter.next();
				xWriter.append(Double.toString(dot.getX()).replace(',', '.') + "\r\n");
				yWriter.append(Double.toString(dot.getY()).replace(',', '.') + "\r\n");
				zWriter.append(Double.toString(dot.getZ()).replace(',', '.') + "\r\n");
			}
			xWriter.flush();
			yWriter.flush();
			zWriter.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
