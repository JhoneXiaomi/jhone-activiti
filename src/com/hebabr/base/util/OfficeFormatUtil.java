package com.hebabr.base.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.regex.Pattern;
import com.aspose.words.Document;
import com.aspose.words.DocumentBuilder;
import com.aspose.words.FileFormatUtil;
import com.aspose.words.ImageData;
import com.aspose.words.ImageSaveOptions;
import com.aspose.words.License;
import com.aspose.words.Node;
import com.aspose.words.NodeCollection;
import com.aspose.words.NodeType;
import com.aspose.words.SaveFormat;
import com.aspose.words.Shape;
import com.aspose.words.ShapeType;

public class OfficeFormatUtil {
	
	private static InputStream license;

    /**
     * 获取license
     * 
     * @return
     */
    public static boolean getLicense() {
        boolean result = false;
        try {
            license = OfficeFormatUtil.class.getClassLoader().getResource("conf/license.xml").openStream();    // license路径
            //word = OfficeFormatUtil.class.getClassLoader().getResourceAsStream("\\report.doc");     // 原始word路径
            License aposeLic = new License();
            aposeLic.setLicense(license);
            result = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
    
    public static boolean getPdfFileByWordFile(String wordPath, String pdfPath){
    	// 验证License
        if (!getLicense()) {
            return false;
        }
        
        try {
        	File f = new File(wordPath);   
            InputStream wordStream = new FileInputStream(f);
        	
            // long old = System.currentTimeMillis();
            Document doc = new Document(wordStream);
            File file = new File(pdfPath);// 输出路径
            FileOutputStream fileOS = new FileOutputStream(file);
            //ImageSaveOptions imageSaveOptions = new ImageSaveOptions(SaveFormat.PDF);
            doc.save(fileOS, SaveFormat.PDF);
            fileOS.close(); 
            wordStream.close();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
     
    public static void getJpgFilesByWordFile(String jpgPath){
    	// 验证License
        if (!getLicense()) {
            return; 
        }
        try {
	        Document doc = new Document("");
	        System.out.println(doc.getPageCount());
	        int pageCount = doc.getPageCount();
	        
	        for(int i=0; i<pageCount; i++){
	        	File file = new File(jpgPath);// 输出路径	        	
		        FileOutputStream fileOS = new FileOutputStream(file + "test" + i + ".png");
		        ImageSaveOptions imageSaveOptions = new ImageSaveOptions(SaveFormat.PNG);
		        imageSaveOptions.setResolution(512);
		        imageSaveOptions.setPageIndex(i);
		        doc.save(fileOS, imageSaveOptions);
		        fileOS.flush();
		        fileOS.close();
		        System.out.println("打印出第" +i+ "页信息");
	        }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
	/**
	 * 替换签名图片内容的主要操作
	 * 
	 * @param input    输入的文档路径
	 * @param output   输出的文档路径（同一路径下的同文件，自身覆盖）
	 * @param datas    map格式  key为对应的人名，value为对应的签名图片地址
	 */
	public static boolean replaceDocSign(String input, String output, HashMap<String, Object> datas) {

		if (getLicense()) {
			try {
				Document doc = new Document(input);
				// 遍历要替换的内容
				Iterator<String> keys = datas.keySet().iterator();
				while (keys.hasNext()) {
					String key = keys.next();
					String value = String.valueOf(datas.get(key));
					// 对显示值得修改
					if (value == null) {
						value = "";
					}					
					// 要求替换的内容是完全匹配时的替换
					// System.out.println(doc.getRange().getText());					
					
					/**
					 * 书签方式来插入图片
					 */
//					//输出图象文件二进制数制 
//					DocumentBuilder builder = new DocumentBuilder(doc);
//					builder.moveToBookmark(key);
//					//String imgPath = "E:\\wangyang.png"; 
//					InputStream in = new FileInputStream(new File(value));
//					if(key.length() == 2){
//						Shape shape =  builder.insertImage(in, 40, 20);
//					}else if(key.length() == 3){
//						Shape shape =  builder.insertImage(in, 60, 20);
//					}
					
					/**
					 *  替换文字方式插入图片
					 */
					Pattern pattern = Pattern.compile(key);
					if(key.contains("【编号】")){
						doc.getRange().replace(pattern, value);						
					}else{
						if(key.length() == 4){					
							doc.getRange().replace(pattern, new ReplaceAndInSertImage(key, value, 40.0, 20.0), false);						
						}else if(key.length() ==5){
							doc.getRange().replace(pattern, new ReplaceAndInSertImage(key, value, 60.0, 20.0), false);						
						}
					}
				}							
				// 替换保存后的内容
				doc.save(output);
				String[] ss = output.split("\\.");
				String fileType = ss[ss.length-1];
				String pdfPath = output.substring(0, output.length()-(fileType.length()+1));
				return OfficeFormatUtil.getPdfFileByWordFile(output, pdfPath + ".pdf");
			} catch (Exception e) {
				e.printStackTrace();
				return false;
			}
		}else{
			return false;
		}
	}
	
	/**
	 * 替换签名图片内容的主要操作
	 * 
	 * @param input    输入的文档路径
	 * @param output   输出的文档路径（同一路径下的同文件，自身覆盖）
	 * @param datas    map格式  key为对应的人名，value为对应的签名图片地址
	 */
	public static void removeDocSign(String input, String output, String[] datas){
		if (getLicense()) {
			try {
				Document doc = new Document(input);
				NodeCollection<?> shapeCollection = doc.getChildNodes(NodeType.SHAPE, true);// 查询文档中所有wmf图片
				Node[] shapes = shapeCollection.toArray();// 序列化
				for (Node node : shapes) {
                    Shape shape = (Shape) node;
                    if (shape.getShapeType() == ShapeType.IMAGE) {// 如果shape类型是ole类型
                        ImageData i = shape.getImageData();// 获得图片数据
                        String imageType = FileFormatUtil.imageTypeToExtension(shape.getImageData().getImageType()); // 获取图片最初的格式
                        if(Arrays.binarySearch(datas, shape.getName()) > -1){
                        	DocumentBuilder builder = new DocumentBuilder(doc);// 新建文档节点
                            builder.moveTo(shape);// 移动到图片位置
                            builder.write(shape.getName());// 插入替换文本
                            shape.remove();// 移除图形
                        }                      
                    }
				}
				doc.save(output);
			} catch (Exception e) {
				e.printStackTrace();
			}
			
		}else{
			System.out.println("error");
		}
	}
	
}
