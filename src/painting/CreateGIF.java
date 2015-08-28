package painting;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;

import javax.imageio.ImageIO;
import javax.imageio.stream.FileImageOutputStream;
import javax.imageio.stream.ImageOutputStream;

import main.Calculator;
import main.Vector;

public class CreateGIF {

	public static void create(String path, String name) {
		File files[] = (new File(path)).listFiles();
		try {
        	ImageOutputStream output =
        			new FileImageOutputStream(new File(name +".GIF"));
        	GifSequenceWriter writer =
        			new GifSequenceWriter(output, ImageIO.read(files[0]).getType(), 1, true);
        	for (int i = 0; i < files.length; i++) {
        		writer.writeToSequence(ImageIO.read(files[i]));
        		System.out.println(i);
        	}

		      writer.close();
		      output.close();
	      } catch (IOException e) {
				e.printStackTrace();
	      }
	}
}