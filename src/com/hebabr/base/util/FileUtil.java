package com.hebabr.base.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.channels.FileChannel;

public class FileUtil {

	/**
	 * 
	 */
	public static File getFile( String fileName, String propertyName ) throws Exception {
		String filePath = null;
		if ( propertyName != null && ! "".equals( propertyName ) ) {
			filePath = System.getProperty( propertyName );
		}
		File file = null;

		if ( filePath == null || "".equals( filePath ) ) {
			URL url = FileUtil.class.getClassLoader().getResource( fileName );
			if ( url == null ) {
				throw new FileNotFoundException( fileName + " not found!" );
			}
			file = new File( url.getPath() );
		} else {
			filePath = filePath.endsWith( "/" ) ? filePath.concat( fileName ) 
					: filePath.concat( "/" ).concat(  fileName );
			file = new File( filePath );
		}
		return file;
	}

	public static String getFilePath( String fileName, String propertyName ) throws Exception {
		String filePath = null;
		if ( propertyName != null && ! "".equals( propertyName ) ) {
			filePath = System.getProperty( propertyName );
		}

		if ( filePath == null || "".equals( filePath ) ) {
			URL url = FileUtil.class.getClassLoader().getResource( fileName );
			if ( url == null ) {
				throw new FileNotFoundException( fileName + " not found!" );
			}
			filePath = url.getPath();
		} else {
			filePath = filePath.endsWith( "/" ) ? filePath.concat( fileName ) 
					: filePath.concat( "/" ).concat(  fileName );
		}
		return filePath;
	}

	public static boolean CreateFile(String destFileName) {
		File file = new File(destFileName);
		if (file.exists()) {
			System.out.println("创建单个文件" + destFileName + "失败，目标文件已存在！");
			return false;
		}
		if (destFileName.endsWith(File.separator)) {
			System.out.println("创建单个文件" + destFileName + "失败，目标不能是目录！");
			return false;
		}
		if (!file.getParentFile().exists()) {
			System.out.println("目标文件所在路径不存在，准备创建。。。");
			if (!file.getParentFile().mkdirs()) {
				System.out.println("创建目录文件所在的目录失败！");
				return false;
			}
		}
		// 创建目标文件
		try {
			if (file.createNewFile()) {
				System.out.println("创建单个文件" + destFileName + "成功！");
				return true;
			} else {
				System.out.println("创建单个文件" + destFileName + "失败！");
				return false;
			}
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("创建单个文件" + destFileName + "失败！");
			return false;
		}
	}

	public static boolean createDir(String destDirName) {
		File dir = new File(destDirName);
		if (dir.exists()) {
			System.out.println("创建目录" + destDirName + "失败，目标目录已存在！");
			return false;
		}
		if (!destDirName.endsWith(File.separator))
			destDirName = destDirName + File.separator;
		// 创建单个目录
		if (dir.mkdirs()) {
			System.out.println("创建目录" + destDirName + "成功！");
			return true;
		} else {
			System.out.println("创建目录" + destDirName + "成功！");
			return false;
		}
	}

	public static String createTempFile(String prefix, String suffix,
			String dirName) {
		File tempFile = null;
		try {
			if (dirName == null) {
				// 在默认文件夹下创建临时文件
				tempFile = File.createTempFile(prefix, suffix);
				return tempFile.getCanonicalPath();
			} else {
				File dir = new File(dirName);
				// 如果临时文件所在目录不存在，首先创建
				if (!dir.exists()) {
					if (!FileUtil.createDir(dirName)) {
						System.out.println("创建临时文件失败，不能创建临时文件所在目录！");
						return null;
					}
				}
				tempFile = File.createTempFile(prefix, suffix, dir);
				return tempFile.getCanonicalPath();
			}
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("创建临时文件失败" + e.getMessage());
			return null;
		}
	}

	/** 
	 *  根据路径删除指定的目录或文件，无论存在与否 
	 *@param sPath  要删除的目录或文件 
	 *@return 删除成功返回 true，否则返回 false。 
	 */  
	public static boolean DeleteFolder(String sPath) {  
		boolean flag = false;  
		File file = new File(sPath);  
		// 判断目录或文件是否存在  
		if (!file.exists()) {  // 不存在返回 false  
			return flag;  
		} else {  
			// 判断是否为文件  
			if (file.isFile()) {  // 为文件时调用删除文件方法  
				return deleteFile(sPath);  
			} else {  // 为目录时调用删除目录方法  
				return deleteDirectory(sPath);  
			}  
		}  
	} 
	/** 
2. * 删除单个文件 
3. * @param   sPath    被删除文件的文件名 
4. * @return 单个文件删除成功返回true，否则返回false 
5. */  
	public static boolean deleteFile(String sPath) {  
		boolean flag = false;  
		File file = new File(sPath);  
		// 路径为文件且不为空则进行删除  
		if (file.isFile() && file.exists()) {  
			file.delete();  
			flag = true;  
		}  
		return flag;  
	}  
	/** 
	 * 删除目录（文件夹）以及目录下的文件 
	 * @param   sPath 被删除目录的文件路径 
	 * @return  目录删除成功返回true，否则返回false 
	 */  
	public static boolean deleteDirectory(String sPath) {  
		//如果sPath不以文件分隔符结尾，自动添加文件分隔符  
		if (!sPath.endsWith(File.separator)) {  
			sPath = sPath + File.separator;  
		}  
		File dirFile = new File(sPath);  
		//如果dir对应的文件不存在，或者不是一个目录，则退出  
		if (!dirFile.exists() || !dirFile.isDirectory()) {  
			return false;  
		}  
		boolean flag = true;  
		//删除文件夹下的所有文件(包括子目录)  
		File[] files = dirFile.listFiles();  
		for (int i = 0; i < files.length; i++) {  
			//删除子文件  
			if (files[i].isFile()) {  
				flag = deleteFile(files[i].getAbsolutePath());  
				if (!flag) break;  
			} //删除子目录  
			else {  
				flag = deleteDirectory(files[i].getAbsolutePath());  
				if (!flag) break;  
			}  
		}  
		if (!flag) return false;  
		//删除当前目录  
		if (dirFile.delete()) {  
			return true;  
		} else {  
			return false;  
		}  
	}  

	/**
	 * 文件通道实现文件复制
	 * @param s 源文件
	 * @param t 目标文件
	 */
	public static void fileChannelCopy(File s, File t) {

		FileInputStream fi = null;

		FileOutputStream fo = null;

		FileChannel in = null;

		FileChannel out = null;

		try {

			fi = new FileInputStream(s);

			fo = new FileOutputStream(t);

			in = fi.getChannel();//得到对应的文件通道

			out = fo.getChannel();//得到对应的文件通道

			in.transferTo(0, in.size(), out);//连接两个通道，并且从in通道读取，然后写入out通道

		} catch (IOException e) {

			e.printStackTrace();

		} finally {

			try {

				fi.close();

				in.close();

				fo.close();

				out.close();

			} catch (IOException e) {

				e.printStackTrace();

			}

		}

	}
	
	public static String getRootPath(Class<?> clazz) {
		  String classPath = clazz.getClassLoader().getResource("/").getPath();
		  String rootPath  = "";
		  //windows下
		  if("\\".equals(File.separator)){   
		   rootPath  = classPath.substring(1,classPath.indexOf("/WEB-INF/classes"));
		   rootPath = rootPath.replace("/", "\\");
		  }
		  //linux下
		  if("/".equals(File.separator)){   
		   rootPath  = classPath.substring(0,classPath.indexOf("/WEB-INF/classes"));
		   rootPath = rootPath.replace("\\", "/");
		  }
		  return rootPath;
	}

	public static String formatPathRoot(String formatStr){
		if("\\".equals(File.separator)){   
				formatStr = formatStr.replace("/", "\\");
			  }
		  //linux下
		else  if("/".equals(File.separator)){
			  formatStr = formatStr.replace("\\", "/");
		  }
		return formatStr;
	}
	
	 public static boolean copyFile(String oldPath, String newPath){    
	          try{    
		          int bytesum = 0;    
	              int byteread = 0;    
	              File oldfile = new File(oldPath);    
	              if(oldfile.exists()){      
	                      InputStream inStream = new FileInputStream(oldPath);     
	                      FileOutputStream fs = new FileOutputStream(newPath);    
	                      byte[] buffer  =  new byte[1444];    
	                      while ((byteread = inStream.read(buffer)) != -1){    
	                              bytesum += byteread; 
	                              fs.write(buffer, 0, byteread);    
	                      }    
	                      inStream.close();
	                      fs.close();
	              } 
	              return true;
	          }    
	          catch(Exception e){
	                  e.printStackTrace();
	                  return false;
	          }    
	    }    
}
