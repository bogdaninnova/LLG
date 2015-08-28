package painting;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import javax.imageio.ImageIO;

public class CreateGraphics {

	private final static int sizeW = 2000;
	private final static int sizeH = 1000;

	public static void create(String path, String name) {
		File files[] = (new File(path)).listFiles();
		String strNumb;
		double dblNumb;
		BufferedReader in;
		try {
        	for (int i = 0; i < files.length; i++) {
        		in = new BufferedReader(new FileReader(files[i].getAbsoluteFile()));
                while ((strNumb = in.readLine()) != null) {
                	dblNumb = Double.valueOf(strNumb);
                }
        	}
	      } catch (IOException e) {
				e.printStackTrace();
	      }
	}

}
