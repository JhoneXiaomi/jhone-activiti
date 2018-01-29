package com.hebabr.base.util;


import java.io.File;
import java.io.IOException;

import com.jacob.activeX.ActiveXComponent;
import com.jacob.com.Dispatch;

public class PDFUtil {
    private static final int wdFormatPDF = 17;
    private static final int xlTypePDF = 0;
    private static final int ppSaveAsPDF = 32;
    //Aplication引用
    //WPS文字使用KWPS.Application、Excel表格是KET.Application、PPT演示文档是KWPP.Application
    //Word使用Word.Application、Excel表格是Excel.Application、PPT演示文档是PowerPoint.Application
    private static final String WORD_PROGRAM_ID = "KWPS.Application";
    private static final String EXCEL_PROGRAM_ID = "KET.Application";
    private static final String PPT_PROGRAM_ID = "KWPP.Application";


    public static boolean convert2PDF(String inputFile, String pdfFile) {
        String suffix = getFileSufix(inputFile);
        File file = new File(inputFile);
        if (!file.exists()) {
            return false;
        }
        if (suffix.equals("pdf")) {
            return false;
        }
        if (suffix.equals("doc") || suffix.equals("docx")
                || suffix.equals("txt")) {
            return word2PDF(inputFile, pdfFile);
        } else if (suffix.equals("ppt") || suffix.equals("pptx")) {
            return ppt2PDF(inputFile, pdfFile);
        } else if (suffix.equals("xls") || suffix.equals("xlsx")) {
            return excel2PDF(inputFile, pdfFile);
        } else {
            return false;
        }
    }
    public static String getFileSufix(String fileName) {
        int splitIndex = fileName.lastIndexOf(".");
        return fileName.substring(splitIndex + 1);
    }
    private static void createFile(String path) throws IOException {
        File file = new File(path);
        if(!file.exists()){
            file.getParentFile().mkdirs();
            file.createNewFile();
        }
    }

    // word转换为pdf
    public static boolean word2PDF(String inputFile, String pdfFile) {
        try {
            //创建文件
            createFile(pdfFile);
            // 打开word应用程序
            ActiveXComponent app = new ActiveXComponent(WORD_PROGRAM_ID);
            // 设置word不可见
            app.setProperty("Visible", false);
            // 获得word中所有打开的文档,返回Documents对象
            Dispatch docs = app.getProperty("Documents").toDispatch();
            // 调用Documents对象中Open方法打开文档，并返回打开的文档对象Document
            Dispatch doc = Dispatch.call(docs, "Open", inputFile, false, true)
                    .toDispatch();
            // 调用Document对象的SaveAs方法，将文档保存为pdf格式
            /*
             * Dispatch.call(doc, "SaveAs", pdfFile, wdFormatPDF
             * //word保存为pdf格式宏，值为17 );
             */
            Dispatch.call(doc, "ExportAsFixedFormat", pdfFile, wdFormatPDF);// word保存为pdf格式宏，值为17
            // 关闭文档
            Dispatch.call(doc, "Close", false);
            // 关闭word应用程序
            app.invoke("Quit", 0);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    // excel转换为pdf
    public static boolean excel2PDF(String inputFile, String pdfFile) {
        try {
            //创建文件
            createFile(pdfFile);
            ActiveXComponent app = new ActiveXComponent(EXCEL_PROGRAM_ID);
            app.setProperty("Visible", false);
            Dispatch excels = app.getProperty("Workbooks").toDispatch();
            Dispatch excel = Dispatch.call(excels, "Open", inputFile, false,
                    true).toDispatch();
            Dispatch.call(excel, "ExportAsFixedFormat", xlTypePDF, pdfFile);
            Dispatch.call(excel, "Close", false);
            app.invoke("Quit");
            return true;
        } catch (Exception e) {
        	e.printStackTrace();
            return false;
        }
    }
    // ppt转换为pdf
    public static boolean ppt2PDF(String inputFile, String pdfFile) {
        try {
            //创建文件
            createFile(pdfFile);
            ActiveXComponent app = new ActiveXComponent(PPT_PROGRAM_ID);
            app.setProperty("Visible", false);
            Dispatch ppts = app.getProperty("Presentations").toDispatch();

            Dispatch ppt = Dispatch.call(ppts, "Open", inputFile, true,// ReadOnly
                    true,// Untitled指定文件是否有标题
                    false// WithWindow指定文件是否可见
            ).toDispatch();

            Dispatch.call(ppt, "SaveAs", pdfFile, ppSaveAsPDF);

            Dispatch.call(ppt, "Close");

            app.invoke("Quit");
            return true;
        } catch (Exception e) {
        	e.printStackTrace();
            return false;
        }
    }
}