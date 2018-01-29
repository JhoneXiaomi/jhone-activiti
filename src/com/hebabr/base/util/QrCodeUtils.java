package com.hebabr.base.util;

import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Hashtable;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.LuminanceSource;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

public class QrCodeUtils extends LuminanceSource {
	
	private static final int BLACK = 0xFF000000;
	private static final int WHITE = 0xFFFFFFFF;
	
	private final BufferedImage image;
	private final int left;
	private final int top;
	
	public static BufferedImage toBufferedImage(BitMatrix matrix){
	     int width = matrix.getWidth();
	     int height = matrix.getHeight();
	     BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
	     for (int x = 0; x < width; x++) {
	       for (int y = 0; y < height; y++) {
	         image.setRGB(x, y, matrix.get(x, y) ? BLACK : WHITE);
	       }
	     }
	     return image;
	   }
	
	public static BufferedImage drawLogo(BufferedImage image,BufferedImage logo){
		 //开始绘制图片  
	    Graphics2D g2 = image.createGraphics();  
	    int matrixWidth = image.getWidth();  
	    int matrixHeigh = image.getHeight();
	    //二维码中logo宽高
	    int drawLogoWidth =  matrixWidth/5; 
	    int drawLogoHeigh =  matrixHeigh/5;
	    g2.drawImage(logo,matrixWidth/5*2,matrixHeigh/5*2,drawLogoWidth, drawLogoHeigh, null);//绘制       
	    g2.dispose();
		return image;
    }
	
	   
	   public static File writeToFile(BufferedImage image, String fileAddress, String fileFormat)
	       throws IOException {
	     File file = new File(fileAddress+"."+fileFormat);//指定输出路径  
		if(!file.exists())
			file.createNewFile();
	     if (!ImageIO.write(image, fileFormat, file)) {
	    	 System.out.println("失败"); 
	    	 throw new IOException("Could not write an image of format " + fileFormat + " to " + fileAddress);
	     }else{  
	    	 System.out.println("图片生成成功！");  
	    	 return file;
        }
	   }
	 
	   
	   public static void writeToStream(BitMatrix matrix, String format, ServletOutputStream stream)
	       throws IOException {
	     BufferedImage image = toBufferedImage(matrix);
	     if (!ImageIO.write(image, format, stream)) {
	       throw new IOException("Could not write an image of format " + format);
	     }
	   }
	   
	   public QrCodeUtils(BufferedImage image) {
		     this(image, 0, 0, image.getWidth(), image.getHeight());
		   }
		 
		   public QrCodeUtils(BufferedImage image, int left, int top, int width, int height) {
		     super(width, height);
		 
		     int sourceWidth = image.getWidth();
		     int sourceHeight = image.getHeight();
		     if (left + width > sourceWidth || top + height > sourceHeight) {
		       throw new IllegalArgumentException("Crop rectangle does not fit within image data.");
		     }
		 
		     for (int y = top; y < top + height; y++) {
		       for (int x = left; x < left + width; x++) {
		         if ((image.getRGB(x, y) & 0xFF000000) == 0) {
		           image.setRGB(x, y, 0xFFFFFFFF); // = white
		         }
		       }
		     }
		 
		     this.image = new BufferedImage(sourceWidth, sourceHeight, BufferedImage.TYPE_BYTE_GRAY);
		     this.image.getGraphics().drawImage(image, 0, 0, null);
		     this.left = left;
		     this.top = top;
		   }
		 
		   @Override
		   public byte[] getRow(int y, byte[] row) {
		     if (y < 0 || y >= getHeight()) {
		       throw new IllegalArgumentException("Requested row is outside the image: " + y);
		     }
		     int width = getWidth();
		     if (row == null || row.length < width) {
		       row = new byte[width];
		     }
		     image.getRaster().getDataElements(left, top + y, width, 1, row);
		     return row;
		   }
		 
		   @Override
		   public byte[] getMatrix() {
		     int width = getWidth();
		     int height = getHeight();
		     int area = width * height;
		     byte[] matrix = new byte[area];
		     image.getRaster().getDataElements(left, top, width, height, matrix);
		     return matrix;
		   }
		 
		   @Override
		   public boolean isCropSupported() {
		     return true;
		   }
		 
		   @Override
		   public LuminanceSource crop(int left, int top, int width, int height) {
		     return new QrCodeUtils(image, this.left + left, this.top + top, width, height);
		   }
		 
		   @Override
		   public boolean isRotateSupported() {
		     return true;
		   }
		 
		   @Override
		   public LuminanceSource rotateCounterClockwise() {
		 
		       int sourceWidth = image.getWidth();
		     int sourceHeight = image.getHeight();
		 
		     AffineTransform transform = new AffineTransform(0.0, -1.0, 1.0, 0.0, 0.0, sourceWidth);
		 
		     BufferedImage rotatedImage = new BufferedImage(sourceHeight, sourceWidth, BufferedImage.TYPE_BYTE_GRAY);
		 
		     Graphics2D g = rotatedImage.createGraphics();
		     g.drawImage(image, transform, null);
		     g.dispose();
		 
		     int width = getWidth();
		     return new QrCodeUtils(rotatedImage, top, sourceWidth - (left + width), getHeight(), width);
		   }
		 
		   /**
		    * 
		    * @param content 二维码编入内容
		    * @return 生成的二维码文件
		    * @throws WriterException
		    * @throws IOException
		    */
			public static File createQRCode(String content) throws WriterException, IOException{
				int width = 128;			//生成二维码的宽高
				int hight = 128;
				Hashtable<EncodeHintType,Object> hints = new Hashtable<EncodeHintType,Object>();
				hints.put(EncodeHintType.CHARACTER_SET, "utf-8");		//内容字符集设置
				hints.put(EncodeHintType.ERROR_CORRECTION,  ErrorCorrectionLevel.L);	//二维码纠错级别  从低到高依次为 L(7%) M(15%) Q(25%) H(30%)
				hints.put(EncodeHintType.MARGIN, 1);	//设置边距宽度
				BitMatrix bitStr = new MultiFormatWriter().encode(content,BarcodeFormat.QR_CODE,width , hight,hints);
				BufferedImage image = QrCodeUtils.toBufferedImage(bitStr);
				BufferedImage logo = ImageIO.read(new File("E:\\logo.jpg")); //读取Logo文件
				//BufferedImage logo = ImageIO.read(new File(PropertiesUtils.getInstance().getConfigItem("logoAddress"))); //读取Logo文件
		        image = QrCodeUtils.drawLogo(image,logo); //为二维码添加Logo
		        //生成二维码图片
				return QrCodeUtils.writeToFile(image,"e:\\new-1", "gif");
			}

}
