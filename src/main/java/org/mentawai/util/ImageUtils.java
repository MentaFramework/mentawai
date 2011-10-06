package org.mentawai.util;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.image.ConvolveOp;
import java.awt.image.Kernel;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Iterator;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.stream.MemoryCacheImageOutputStream;
import javax.swing.ImageIcon;

public class ImageUtils {
	
	public static Dimension getSize(byte[] data) {
		
		ImageIcon ii = new ImageIcon(data);
		
		Image img = ii.getImage();
		
		return new Dimension(img.getWidth(null), img.getHeight(null));
	}
	
	public static Image createImage(byte[] data) {
		
		return new ImageIcon(data).getImage();
	}
	
    public static Image resize(Image img, int newWidth, float quality) throws IOException {
    	 
        if (quality < 0 || quality > 1) {
            throw new IllegalArgumentException("Quality has to be between 0 and 1");
        }
 
        ImageIcon ii = new ImageIcon(img);
        Image i = ii.getImage();
        Image resizedImage = null;
 
        int iWidth = i.getWidth(null);
        int iHeight = i.getHeight(null);
 
        if (iWidth > iHeight) {
            resizedImage = i.getScaledInstance(newWidth, (newWidth * iHeight) / iWidth, Image.SCALE_SMOOTH);
        } else {
            resizedImage = i.getScaledInstance((newWidth * iWidth) / iHeight, newWidth, Image.SCALE_SMOOTH);
        }
 
        // This code ensures that all the pixels in the image are loaded.
        Image temp = new ImageIcon(resizedImage).getImage();
 
        // Create the buffered image.
        BufferedImage bufferedImage = new BufferedImage(temp.getWidth(null), temp.getHeight(null),
                                                        BufferedImage.TYPE_INT_RGB);
 
        // Copy image to buffered image.
        Graphics g = bufferedImage.createGraphics();
 
        // Clear background and paint the image.
        g.setColor(Color.white);
        g.fillRect(0, 0, temp.getWidth(null), temp.getHeight(null));
        g.drawImage(temp, 0, 0, null);
        g.dispose();
 
        // Soften.
        float softenFactor = 0.05f;
        float[] softenArray = {0, softenFactor, 0, softenFactor, 1-(softenFactor*4), softenFactor, 0, softenFactor, 0};
        Kernel kernel = new Kernel(3, 3, softenArray);
        ConvolveOp cOp = new ConvolveOp(kernel, ConvolveOp.EDGE_NO_OP, null);
        bufferedImage = cOp.filter(bufferedImage, null);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        MemoryCacheImageOutputStream mos = new MemoryCacheImageOutputStream(baos);
        
        Object[] obj = getWriter(quality);
        
        if (obj == null) return null;
        
        ImageWriter writer = (ImageWriter) obj[0];
        ImageWriteParam iwp = (ImageWriteParam) obj[1];
        
        writer.setOutput(mos);
        
        IIOImage image = new IIOImage(bufferedImage, null, null);
        writer.write(null, image, iwp);
        
        return new ImageIcon(baos.toByteArray()).getImage();
    }
    
    public static BufferedImage toBufferedImage(Image img) {
    	
    	ImageIcon ii = new ImageIcon(img);
    	img = ii.getImage();

    	BufferedImage bi = new BufferedImage(img.getWidth(null), img.getHeight(null),
    	    BufferedImage.TYPE_INT_RGB);
    	Graphics2D g = bi.createGraphics();
    	g.drawImage(img, 0, 0, null);
    	g.dispose();
    	 
    	return bi;
    }
    
    private static Object[] getWriter(float quality) {
    	
    	Object[] obj = new Object[2];
    	
    	Iterator<ImageWriter> iter = ImageIO.getImageWritersByFormatName("jpeg");
    	
    	if (iter.hasNext()) {
    		
    		ImageWriter writer = iter.next();
    		
    		ImageWriteParam iwp = writer.getDefaultWriteParam();

    		iwp.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
    		
    		iwp.setCompressionQuality(quality);
    		
    		obj[0] = writer;
    		obj[1] = iwp;
    		
    		return obj;
    	}
    	
    	return null;

    }
    
    public static byte[] toByteArray(Image img) throws IOException {
    	
    	ImageIcon ii = new ImageIcon(img);
    	img = ii.getImage();
    	
	 // img eh o seu objeto image
    	BufferedImage bi = new BufferedImage( img.getWidth(null), img.getHeight(null), BufferedImage.TYPE_INT_RGB );
    	Graphics2D g2 = bi.createGraphics();
    	g2.drawImage( img, 0, 0, null ); 

    	ByteArrayOutputStream baos = new ByteArrayOutputStream();
    	MemoryCacheImageOutputStream mos = new MemoryCacheImageOutputStream(baos);
        
        Object[] obj = getWriter(1);
        
        if (obj == null) return null;
        
        ImageWriter writer = (ImageWriter) obj[0];
        ImageWriteParam iwp = (ImageWriteParam) obj[1];
        
        writer.setOutput(mos);
        
        IIOImage image = new IIOImage(toBufferedImage(img), null, null);
        writer.write(null, image, iwp);

    	return baos.toByteArray();

    }
    
    public static Image crop(Image source, int x1, int y1, int x2, int y2) {
    	
        BufferedImage bi = toBufferedImage(source);
        
        BufferedImage img = bi.getSubimage(x1,y1,x2,y2); 

        /*
        JFrame frame = new JFrame();
        
        Image cropped = frame.createImage(new FilteredImageSource(source.getSource(), new CropImageFilter(x1, y1, x2, y2)));
        
        frame.dispose();
        */
        
        return img;
    }
    
    public static Image loadImage(String filename) {
    	
    	ImageIcon icon = new ImageIcon(filename);
    	
    	return icon.getImage();
    }

}
