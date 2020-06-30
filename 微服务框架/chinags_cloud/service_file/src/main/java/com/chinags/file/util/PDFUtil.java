package com.chinags.file.util;

import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import com.jacob.activeX.ActiveXComponent;
import com.jacob.com.Dispatch;
import com.jacob.com.Variant;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;

/**
 * 各种格式的文件转pdf工具类
 * @Author : Mark_wang
 * @Date : 2019/8/8
 **/
public class PDFUtil {

    static final int wdDoNotSaveChanges = 0;// 不保存待定的更改。
    static final int wdFormatPDF = 17;// word转PDF 格式
    static final int ppSaveAsPDF = 32;// ppt 转PDF 格式
    /**
     * 图片文件转pdf
     * @param imagePath 图片文件路径
     * @param pdfPath pdf文件输出路径
     */
    public static void Img2PDF(String imagePath,String pdfPath) {
        try {
            BufferedImage img = ImageIO.read(new File(imagePath));
            FileOutputStream fos = new FileOutputStream(pdfPath);
            Document doc = new Document(null, 0, 0, 0, 0);
            doc.setPageSize(new Rectangle(img.getWidth(), img.getHeight()));
            Image image = Image.getInstance(imagePath);
            PdfWriter.getInstance(doc, fos);
            doc.open();
            doc.add(image);
            doc.close();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (BadElementException e) {
            e.printStackTrace();
        } catch (DocumentException e) {
            e.printStackTrace();
        }
    }

    /**
     * 文本文件转pdf文件
     * @param textPath text 文件路径
     * @param pdfPath pdf文件输出路径
     */
    public static void text2pdf(String textPath, String pdfPath){
        try {
            // 获取win字体
            String FONT = "C:\\Windows\\Fonts\\simhei.ttf";
            Document document = new Document();
            OutputStream os = new FileOutputStream(new File(pdfPath));
            PdfWriter.getInstance(document, os);
            document.open();
            BaseFont baseFont = BaseFont.createFont(FONT, BaseFont.IDENTITY_H, BaseFont.NOT_EMBEDDED);
            Font font = new Font(baseFont);
            InputStreamReader isr = new InputStreamReader(new FileInputStream(new File(textPath)), "GBK");
            BufferedReader bufferedReader = new BufferedReader(isr);
            String str = "";
            while ((str = bufferedReader.readLine()) != null) {
                document.add(new Paragraph(str, font));
            }
            document.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * excel 转 pdf
     * @param excelPath
     * @param pdfPath
     */
    public static void excel2pdf(String excelPath, String pdfPath) {
        ActiveXComponent app = new ActiveXComponent("Excel.Application"); //启动excel(Excel.Application)
        try {
            app.setProperty("Visible", false);
            Dispatch workbooks = app.getProperty("Workbooks").toDispatch();
            Dispatch workbook = Dispatch.invoke(workbooks, "Open", Dispatch.Method, new Object[]{excelPath, new Variant(false),new Variant(false)}, new int[3]).toDispatch();
            Dispatch.invoke(workbook, "SaveAs", Dispatch.Method, new Object[] {
                    pdfPath, new Variant(57), new Variant(false),
                    new Variant(57), new Variant(57), new Variant(false),
                    new Variant(true), new Variant(57), new Variant(true),
                    new Variant(true), new Variant(true) }, new int[1]);
            Variant f = new Variant(false);
            Dispatch.call(workbook, "Close", f);
        } catch (Exception e) {
            System.out.println("========Error:文档转换失败：" + e.getMessage());
        } finally {
            if (app != null){
                app.invoke("Quit", new Variant[] {});
            }
        }
    }

    /**
     * word 转 pdf
     * @param wordPath
     * @param pdfPath
     */
    public static void word2pdf(String wordPath,String pdfPath){
        ActiveXComponent app = null;
        try {
            app = new ActiveXComponent("Word.Application");
            app.setProperty("Visible", false);

            Dispatch docs = app.getProperty("Documents").toDispatch();
            Dispatch doc = Dispatch.call(docs,//
                    "Open", //
                    wordPath,// FileName
                    false,// ConfirmConversions
                    true // ReadOnly
            ).toDispatch();

            File tofile = new File(pdfPath);
            if (tofile.exists()) {
                tofile.delete();
            }
            Dispatch.call(doc,//
                    "SaveAs", //
                    pdfPath, // FileName
                    wdFormatPDF);

            Dispatch.call(doc, "Close", false);
        } catch (Exception e) {
            System.out.println("========Error:文档转换失败：" + e.getMessage());
        } finally {
            if (app != null)
                app.invoke("Quit", wdDoNotSaveChanges);
        }
    }

    /**
     * ppt 转 pdf
     * @param pptPath
     * @param pdfPath
     */
    public static void ppt2pdf(String pptPath,String pdfPath){
        ActiveXComponent app = null;
        try {
            app = new ActiveXComponent("PowerPoint.Application");
            Dispatch presentations = app.getProperty("Presentations").toDispatch();
            Dispatch presentation = Dispatch.call(presentations,//
                    "Open",
                    pptPath,// FileName
                    true, // ReadOnly
                    true,// Untitled 指定文件是否有标题。
                    false // WithWindow 指定文件是否可见。
            ).toDispatch();

            File tofile = new File(pdfPath);
            if (tofile.exists()) {
                tofile.delete();
            }
            Dispatch.call(presentation,//
                    "SaveAs", //
                    pdfPath, // FileName
                    ppSaveAsPDF);

            Dispatch.call(presentation, "Close");
        } catch (Exception e) {
            System.out.println("========Error:文档转换失败：" + e.getMessage());
        } finally {
            if (app != null) app.invoke("Quit");
        }
    }

    /**
     * 给pdf加水印（会创建一个新的PDF）
     * @param inputFile  原始文件路径
     * @param outputFile  输出路径
     * @param waterMarkName  水印内容
     */
    public static void makePdfShuiyin(String inputFile, String outputFile, String waterMarkName) {
        try {
            PdfReader reader = new PdfReader(inputFile);
            PdfStamper stamper = new PdfStamper(reader, new FileOutputStream(outputFile));

            BaseFont base = BaseFont.createFont("STSong-Light", "UniGB-UCS2-H",   BaseFont.EMBEDDED);
            Rectangle pageRect = null;
            PdfGState gs = new PdfGState();
            gs.setFillOpacity(0.3f);
            gs.setStrokeOpacity(0.4f);
            int total = reader.getNumberOfPages() + 1;

            JLabel label = new JLabel();
            FontMetrics metrics;
            int textH = 0;
            int textW = 0;
            label.setText(waterMarkName);
            metrics = label.getFontMetrics(label.getFont());
            textH = metrics.getHeight();
            textW = metrics.stringWidth(label.getText());

            PdfContentByte under;
            for (int i = 1; i < total; i++) {
                pageRect = reader.getPageSizeWithRotation(i);
                under = stamper.getOverContent(i);
                under.saveState();
                under.setGState(gs);
                under.beginText();
                under.setFontAndSize(base, 20);

                // 水印文字成30度角倾斜
                //你可以随心所欲的改你自己想要的角度
                //textH*6水印上下宽度   textW*2水印左右宽度
                for (int height = -5 + textH; height < pageRect.getHeight();
                     height = height + textH*6) {
                    for (int width = -5 + textW; width < pageRect.getWidth() + textW;
                         width = width + textW*2) {
                        under.showTextAligned(Element.ALIGN_LEFT
                                , waterMarkName, width - textW,
                                height - textH, 30);
                    }
                }
                // 添加水印文字
                under.endText();
            }
            //说三遍
            //一定不要忘记关闭流
            //一定不要忘记关闭流
            //一定不要忘记关闭流
            stamper.close();
            reader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    // 统计pdf页数
    public static int getPdfPage(String filepath){
        int pagecount = 0;
        PdfReader reader;
        try {
            reader = new PdfReader(filepath);
            pagecount= reader.getNumberOfPages();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return pagecount;
    }

    /**
     * 根据word路径获取word页数
     * @param path
     * @return
     */
    public static int getWordPage(String path) {
        String pdfPath = path.replaceAll(".docx", ".pdf").replaceAll(".doc", ".pdf");
        word2pdf(path, pdfPath);
        int pageSize = getPdfPage(pdfPath);
        File pdfFile = new File(pdfPath);
        pdfFile.delete();
        return pageSize;
    }

    /*public static void asposeExcel2pdf(String Address, String pdfPath) {
        try {
            File pdfFile = new File(pdfPath);// 输出路径
            Workbook wb = new Workbook(Address);// 原始excel路径
            PdfSaveOptions pdfSaveOptions = new PdfSaveOptions();
            pdfSaveOptions.setOnePagePerSheet(true);
            FileOutputStream fileOS = new FileOutputStream(pdfFile);
            wb.save(fileOS, SaveFormat.PDF);
            fileOS.close();
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void asposePpt2pdf(String Address, String pdfPaht) {
        try {
            File file = new File(pdfPaht);// 输出路径
            Presentation pres = new Presentation(Address);//输入pdf路径
            FileOutputStream fileOS = new FileOutputStream(file);
            pres.save(fileOS, SaveFormat.PDF);
            fileOS.close();

            //    long now = System.currentTimeMillis();
            //    System.out.println("共耗时：" + ((now - old) / 1000.0) + "秒\n\n" + "文件保存在:" + file.getPath()); //转化过程耗时
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void asposeWord2Pdf(String Address, String pdfPaht) {
        try {
            File file = new File(pdfPaht);  //新建一个pdf文档
            FileOutputStream os = new FileOutputStream(file);
            com.aspose.words.Document doc = new com.aspose.words.Document(Address);                    //Address是将要被转化的word文档
            doc.save(os, SaveFormat.PDF);//全面支持DOC, DOCX, OOXML, RTF HTML, OpenDocument, PDF, EPUB, XPS, SWF 相互转换
            os.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }*/

    public static void main(String[] args) {
        String path = "F:\\project\\chinags\\设计文档\\数据资源系统详细设计说明书.docx";
        System.out.println(getWordPage(path));
    }
}
