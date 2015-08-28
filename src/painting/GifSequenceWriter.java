package painting;

import javax.imageio.*;
import javax.imageio.metadata.*;
import javax.imageio.stream.*;
import java.awt.image.*;
import java.io.*;
import java.util.Iterator;

class GifSequenceWriter {
  protected ImageWriter gifWriter;
  protected ImageWriteParam imageWriteParam;
  protected IIOMetadata imageMetaData;
  
  public GifSequenceWriter(ImageOutputStream outputStream, int imageType,
      int delay, boolean loopContinuously) throws IIOException, IOException {
	  
    gifWriter = getWriter(); 
    imageWriteParam = gifWriter.getDefaultWriteParam();
    ImageTypeSpecifier imageTypeSpecifier =
      ImageTypeSpecifier.createFromBufferedImageType(imageType);

    imageMetaData = gifWriter.getDefaultImageMetadata(imageTypeSpecifier, imageWriteParam);

    String metaFormatName = imageMetaData.getNativeMetadataFormatName();

    IIOMetadataNode root = (IIOMetadataNode) imageMetaData.getAsTree(metaFormatName);

    IIOMetadataNode gce = getNode(root, "GraphicControlExtension");

    gce.setAttribute("disposalMethod", "none");
    gce.setAttribute("userInputFlag", "FALSE");
    gce.setAttribute("transparentColorFlag", "FALSE");
    gce.setAttribute("delayTime", Integer.toString(delay / 10));
    gce.setAttribute("transparentColorIndex", "0");

    IIOMetadataNode ce = getNode(root, "CommentExtensions");
    ce.setAttribute("CommentExtension", "calculator");//TODO name

    IIOMetadataNode ae = getNode(root, "ApplicationExtensions");
    IIOMetadataNode cae = new IIOMetadataNode("ApplicationExtension");

    cae.setAttribute("applicationID", "NETSCAPE");
    cae.setAttribute("authenticationCode", "2.0");

    int loop = loopContinuously ? 0 : 1;

    cae.setUserObject(new byte[]{ 0x1, (byte) (loop & 0xFF), (byte) ((loop >> 8) & 0xFF)});
    ae.appendChild(cae);

    imageMetaData.setFromTree(metaFormatName, root);
    gifWriter.setOutput(outputStream);
    gifWriter.prepareWriteSequence(null);
  }
  
  public void writeToSequence(RenderedImage img) throws IOException {
	  gifWriter.writeToSequence(new IIOImage(img, null, imageMetaData), imageWriteParam);
  }
  
  public void close() throws IOException {
    gifWriter.endWriteSequence();    
  }

  private static ImageWriter getWriter() throws IIOException {
    Iterator<ImageWriter> iter = ImageIO.getImageWritersBySuffix("gif");
    if(!iter.hasNext()) 
      throw new IIOException("No GIF Image Writers Exist");
     else 
      return iter.next();
  }

  private static IIOMetadataNode getNode(
		  IIOMetadataNode rootNode, String nodeName) {
    int nNodes = rootNode.getLength();
    for (int i = 0; i < nNodes; i++) {
      if (rootNode.item(i).getNodeName().compareToIgnoreCase(nodeName) == 0) 
        return((IIOMetadataNode) rootNode.item(i));
    }
    IIOMetadataNode node = new IIOMetadataNode(nodeName);
    rootNode.appendChild(node);
    return(node);
  }
}