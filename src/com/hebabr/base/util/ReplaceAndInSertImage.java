package com.hebabr.base.util;

import java.io.FileInputStream;
import java.io.InputStream;
import com.aspose.words.Document;
import com.aspose.words.DocumentBuilder;
import com.aspose.words.IReplacingCallback;
import com.aspose.words.Node;
import com.aspose.words.ReplaceAction;
import com.aspose.words.ReplacingArgs;
import com.aspose.words.Shape;

public class ReplaceAndInSertImage implements IReplacingCallback {

	public String imagePath;
	public double width;
	public double height;
	public String name;



	public ReplaceAndInSertImage(String name, String imagePath, double width, double height){
		this.name = name;
		this.imagePath = imagePath;
		this.width = width;
		this.height = height;
	}
	
	public String getImagePath() {
		return imagePath;
	}
	public void setImagePath(String imagePath) {
		this.imagePath = imagePath;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public double getWidth() {
		return width;
	}

	public void setWidth(double width) {
		this.width = width;
	}

	public double getHeight() {
		return height;
	}

	public void setHeight(double height) {
		this.height = height;
	}

	@Override
	public int replacing(ReplacingArgs arg0) throws Exception {
		//获取当前节点
		Node node = arg0.getMatchNode();
		//获取当前文档
		Document doc = (Document) node.getDocument();
		DocumentBuilder builder = new DocumentBuilder(doc);
		//将光标移动到指定节点
		builder.moveTo(node);
		//插入图片
		InputStream is = new FileInputStream(imagePath);
		Shape shape = builder.insertImage(is, width, height);
		shape.setName(name);
		return ReplaceAction.REPLACE;
	}

}
