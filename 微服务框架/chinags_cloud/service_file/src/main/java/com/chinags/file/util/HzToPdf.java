package com.chinags.file.util;

import com.chinags.common.utils.StringUtils;
import com.chinags.file.entity.*;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.draw.LineSeparator;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;

/**
 * @Author : Mark_Wang
 * @Date : 2019/12/23
 **/
public class HzToPdf {
    // 字体设置
    String fontPath = "C:\\font\\FZXBSJW.TTF";
    //
    BaseFont fzxbs = BaseFont.createFont(fontPath, BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
    private BaseFont fs = BaseFont.createFont("C:\\font\\simfang.ttf", BaseFont.IDENTITY_H, BaseFont.NOT_EMBEDDED);
    String ktPath = "C:\\font\\simkai.ttf";
    BaseFont kt = BaseFont.createFont(ktPath, BaseFont.IDENTITY_H, BaseFont.NOT_EMBEDDED);

    String htPath = "C:\\font\\simhei.ttf";
    BaseFont ht = BaseFont.createFont(htPath, BaseFont.IDENTITY_H, BaseFont.NOT_EMBEDDED);

    String stPath = "C:\\font\\simsun.ttf";
    private BaseFont st = BaseFont.createFont(stPath, BaseFont.IDENTITY_H, BaseFont.NOT_EMBEDDED);


    private Font fontTitlexx = new Font(fzxbs, 29, Font.NORMAL, BaseColor.RED);
    private Font fontNumber = new Font(fs, 16, Font.NORMAL, BaseColor.BLACK);
    private Font fornContentTitle = new Font(fzxbs, 22, Font.NORMAL, BaseColor.BLACK);
    private Font yijiTitle = new Font(ht, 16, Font.NORMAL, BaseColor.BLACK);
    private Font erjiTitle = new Font(kt, 16, Font.NORMAL, BaseColor.BLACK);
    private Font fontcontent = new Font(fs, 16, Font.NORMAL, BaseColor.BLACK);
    private Font fontFort = new Font(fs, 16, Font.NORMAL, BaseColor.BLACK);
    private Font fontQfr = new Font(kt, 16, Font.NORMAL, BaseColor.BLACK);
    private Font fontForter = new Font(fs, 14, Font.NORMAL, BaseColor.BLACK);
    private Font fornContentTitledw = new Font(fzxbs, 21, Font.NORMAL, BaseColor.BLACK);

    //参数
    //1.线宽度
    //2.直线长度，是个百分百，0-100之间
    //3.直线颜色
    //4.直线位置
    //5.上下移动位置
    private LineSeparator lineTitle = new LineSeparator(2f,100,BaseColor.RED,Element.ALIGN_CENTER,-8f);
    private LineSeparator linepx = new LineSeparator(1f,100,BaseColor.RED,Element.ALIGN_CENTER,-8f);

    // 模版文字
    private String FILE_TITLE_SX = "山东省调水工程运行维护中心\n\n";

    private String FILE_TITLE_XX = "山东省调水工程运行维护中心文件\n\n";

    public HzToPdf() throws IOException, DocumentException {

    }

    private String path = "C:\\hz\\";

    private SimpleDateFormat format2 = new SimpleDateFormat("YYYY  年  MM  月  dd  日");

    /**
     * 验证存储路径是否存在
     */
    public void checkPath() {
        File pdfFile = new File(path);
        if (!pdfFile.exists()) {
            pdfFile.mkdirs();
        }
    }

    private void compressPicture(Image image) {
        float width = image.getWidth();
        int percent = Math.round(450 / width * 100);
        image.setAlignment(Image.MIDDLE);
        image.scalePercent(percent);
    }

    /**
     * 上行文/下行文生成pdf
     * @param hzTopDown
     */
    public String topToPdf(HzTopDown hzTopDown) {
        checkPath();
        Document doc = new Document(PageSize.A4, 78, 78 , 88, 88);
        String filePath = null;
        try {
            String pdfPath = path + hzTopDown.getId() + ".pdf";
            deleteOldFile(pdfPath);
            PdfWriter writer = PdfWriter.getInstance(doc, new FileOutputStream(pdfPath));
            writer.setPageEvent(new PdfExport());
            doc.open();
            Paragraph paragraphtitle = new Paragraph();
            paragraphtitle.setLeading(335);
            doc.add(paragraphtitle);
            // 标题
            Paragraph paragraphTitle = new Paragraph();
            if (hzTopDown.getType() == 0) {
                Image image = Image.getInstance("C:\\font\\titleFile.png");
                compressPicture(image);
                doc.add(image);
            }
            else {
                Image image = Image.getInstance("C:\\font\\title.png");
                compressPicture(image);
                doc.add(image);
            }
            Paragraph paragraphtitle1 = new Paragraph("\n");
            paragraphtitle1.setLeading(55);
            doc.add(paragraphtitle1);
            // 文件标号
            String fileNumber = "";
            if (hzTopDown.getType() == 1) {
                PdfPTable table = new PdfPTable(2);
                table.setWidthPercentage(100);
                PdfPCell cell = new PdfPCell();
                Chunk chunk = new Chunk((hzTopDown.getFileNumber() != null ? hzTopDown.getFileNumber() : ""), fontNumber);
                Paragraph paragraph = new Paragraph();
                paragraph.add(chunk);
                cell.addElement(paragraph);
                cell.setBorder(Rectangle.NO_BORDER);
                cell.setFixedHeight(20);
                table.addCell(cell);

                PdfPCell cell1 = new PdfPCell();
                Paragraph paragraph1 = new Paragraph();
                Chunk chunk1 = new Chunk((hzTopDown.getUserName() != null ? hzTopDown.getUserName() : ""), fontQfr);
                Chunk chunk2 = new Chunk("签发人： ", fontNumber);
                paragraph1.add(chunk2);
                paragraph1.add(chunk1);
                paragraph1.setAlignment(Element.ALIGN_RIGHT);
                cell1.addElement(paragraph1);
                cell1.setBorder(Rectangle.NO_BORDER);
                table.addCell(cell1);
                doc.add(table);
            } else if (hzTopDown.getType() == 0) {
                fileNumber = hzTopDown.getFileNumber();
                Paragraph paragraphNumber = new Paragraph(fileNumber, fontNumber);
                paragraphNumber.setAlignment(Element.ALIGN_CENTER);
                doc.add(paragraphNumber);
            }
            // 添加分割线
            doc.add(lineTitle);
            if (hzTopDown.getType() == 2) {
                Paragraph paragraphhx = new Paragraph();
                paragraphhx.setLeading(3);
                paragraphhx.add(linepx);
                doc.add(paragraphhx);
                Paragraph paragraphNumber = new Paragraph((hzTopDown.getFileNumber() != null ? hzTopDown.getFileNumber() : ""), fontNumber);
                paragraphNumber.setLeading(30);
                paragraphNumber.setAlignment(Element.ALIGN_RIGHT);
                doc.add(paragraphNumber);
            }
            // 添加内容标题
            Paragraph paragraphmr = new Paragraph("\n山东省调水工程运行维护中心\n", fornContentTitle);
            paragraphmr.setAlignment(Element.ALIGN_CENTER);
            paragraphmr.setIndentationLeft(50);
            paragraphmr.setIndentationRight(50);
            doc.add(paragraphmr);

            Paragraph paragraph = new Paragraph((hzTopDown.getTitle() != null ? hzTopDown.getTitle() : "" + "\n") + "\n\n", fornContentTitle);
            paragraph.setLeading(28);
            paragraph.setAlignment(Element.ALIGN_CENTER);
            doc.add(paragraph);
            // 发送单位
            Paragraph paragraphfsdw = new Paragraph(hzTopDown.getFsdw() != null ? hzTopDown.getFsdw() + "：" : "", fontcontent);
            paragraphfsdw.setLeading(30);
            paragraphfsdw.setAlignment(3);
            doc.add(paragraphfsdw);
            // 添加内容
            String content = replaceHtml(hzTopDown.getContent());
            if (StringUtils.isNotEmpty(content)) {
                String[] contents = content.split("\n", -1);
                for (String con : contents) {
                    if (con.indexOf("<h1>") != -1) {
                        Chunk chunkContent = new Chunk(con.replaceAll("<h1>", "").replaceAll("</h1>", ""), yijiTitle);
                        chunkContent.setSplitCharacter(ChineseSplitCharater.SplitCharacter);
                        Paragraph paragrapContent = new Paragraph(chunkContent);
                        paragrapContent.setLeading(30);
                        paragrapContent.setAlignment(0);
                        doc.add(paragrapContent);
                    } else if (con.indexOf("<h2>") != -1) {
                        Chunk chunkContent = new Chunk(con.replaceAll("<h2>", "").replaceAll("</h2>", ""), erjiTitle);
                        chunkContent.setSplitCharacter(ChineseSplitCharater.SplitCharacter);
                        Paragraph paragrapContent = new Paragraph(chunkContent);
                        paragrapContent.setLeading(30);
                        paragrapContent.setAlignment(0);
                        doc.add(paragrapContent);
                    } else {
                        Chunk chunkContent = new Chunk(con, fontcontent);
                        chunkContent.setSplitCharacter(ChineseSplitCharater.SplitCharacter);
                        Paragraph paragrapContent = new Paragraph(chunkContent);
                        paragrapContent.setLeading(30);
                        paragrapContent.setAlignment(0);
                        doc.add(paragrapContent);
                    }
                }
            }

            Paragraph paragraph1 = new Paragraph("\n\n\n\n\n\n", fontFort);
            doc.add(paragraph1);
            int endPx = 66 + 22 * hzTopDown.getEndSize();
            // 联系人
            if (StringUtils.isNotEmpty(hzTopDown.getLxr()) || StringUtils.isNotEmpty(hzTopDown.getLxdh())) {
                endPx+=44;
                PdfPTable table4 = new PdfPTable(1);
                table4.setWidthPercentage(84);
                PdfPCell celllx = new PdfPCell();
                Paragraph paragraphlx = new Paragraph("   （联系人：" + hzTopDown.getLxr() + " ， 联系电话：" + hzTopDown.getLxdh() + "）", fontFort);
                paragraphlx.setLeading(17);
                celllx.addElement(paragraphlx);
                celllx.setBorderColor(BaseColor.BLACK);
                celllx.setBorder(Rectangle.NO_BORDER);
                celllx.setBorderWidth(1);
                celllx.setFixedHeight(28);
                table4.addCell(celllx);
                table4.setLockedWidth(true);
                table4.setTotalWidth(Float.parseFloat(String.valueOf(PageSize.A4.getWidth()*0.72)));
                table4.writeSelectedRows(0, 1, doc.left(), doc.bottomMargin() + 80, writer.getDirectContent());
            }
            // 抄送单位
            if (StringUtils.isNotEmpty(hzTopDown.getCsdw())) {
                endPx+=22;
                PdfPTable table3 = new PdfPTable(1);
                table3.setWidthPercentage(84);
                PdfPCell cellcsdw = new PdfPCell();
                Paragraph paragraphcsdw = new Paragraph("  抄送：" + hzTopDown.getCsdw(), fontForter);
                paragraphcsdw.setLeading(15);
                cellcsdw.addElement(paragraphcsdw);
                cellcsdw.setBorderColor(BaseColor.BLACK);
                cellcsdw.setBorder(Rectangle.TOP);
                cellcsdw.setBorderWidth(1.5f);
                cellcsdw.setFixedHeight(24);
                table3.addCell(cellcsdw);
                table3.setLockedWidth(true);
                table3.setTotalWidth(Float.parseFloat(String.valueOf(PageSize.A4.getWidth()*0.72)));
                table3.writeSelectedRows(0, 1, doc.left(), doc.bottomMargin() + 33, writer.getDirectContent());
            }
            // 文件结尾固定底部内容
            Paragraph paragraphCompany = new Paragraph("山东省调水工程运行维护中心", fontFort);
            String date = hzTopDown.getDay();
            String[] dates = date.split("-");
            date = dates[0] + "年" + Integer.parseInt(dates[1]) + "月" + Integer.parseInt(dates[2]) + "日";
            String date1 = dates[0] + " 年 " + Integer.parseInt(dates[1]) + " 月 " + Integer.parseInt(dates[2]) + " 日";
            String bukongge = "";
            if (Integer.parseInt(dates[1]) < 10) {
                bukongge+=" ";
            }
            if (Integer.parseInt(dates[2]) < 10) {
                bukongge+=" ";
            }
            if (Integer.parseInt(dates[1]) < 10 || Integer.parseInt(dates[2]) < 10) {
                date1 = " " + date1;
            }
            if (Integer.parseInt(dates[1]) >= 10 && Integer.parseInt(dates[2]) >= 10) {
                date1 = " " + date1;
            }
            Paragraph paragraphDay = new Paragraph("   " + date1, fontFort);
            //
            PdfPTable table2 = new PdfPTable(2);
            table2.setWidthPercentage(84);
            PdfPCell cell5 = new PdfPCell();
            Paragraph pcell5 = new Paragraph("", fontcontent);
            cell5.addElement(pcell5);
            cell5.setBorderColor(BaseColor.BLACK);
            cell5.setHorizontalAlignment(Element.ALIGN_BOTTOM);
            cell5.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell5.setBorder(Rectangle.NO_BORDER);
            cell5.setFixedHeight(60);
            table2.addCell(cell5);

            PdfPCell cell3 = new PdfPCell();
            cell3.addElement(paragraphCompany);
            cell3.addElement(paragraphDay);
            cell3.setBorderColor(BaseColor.BLACK);
            cell3.setBorder(Rectangle.NO_BORDER);
            cell3.setFixedHeight(60);
            table2.addCell(cell3);
            table2.setLockedWidth(true);
            table2.setTotalWidth(Float.parseFloat(String.valueOf(PageSize.A4.getWidth()*0.72)));

            // 判断位置在中间还是结尾
            table2.writeSelectedRows(0, 1, doc.left() + 10, doc.bottomMargin() + endPx, writer.getDirectContent());
            PdfPTable table1 = new PdfPTable(1);
            table1.setWidthPercentage(84);
            PdfPCell cell4 = new PdfPCell();
            Paragraph paragraph7 = new Paragraph("山东省调水工程运行维护中心办公室          " + bukongge + date + "印发", fontForter);
            paragraph7.setLeading(15);
            cell4.addElement(paragraph7);
            cell4.setBorderColor(BaseColor.BLACK);
            cell4.setBorder(Rectangle.ALIGN_JUSTIFIED);
            if (StringUtils.isNotEmpty(hzTopDown.getCsdw())) {
                cell4.setBorderWidth(1);
            } else {
                cell4.setBorderWidth(1.5f);
            }
            cell4.setFixedHeight(22);
            table1.addCell(cell4);

            table1.setLockedWidth(true);
            table1.setTotalWidth(Float.parseFloat(String.valueOf(PageSize.A4.getWidth()*0.72)));
            table1.writeSelectedRows(0, 1, doc.left(), doc.bottomMargin() + 11, writer.getDirectContent());
            if (StringUtils.isNotEmpty(hzTopDown.getCsdw())) {
                PdfPTable table5 = new PdfPTable(1);
                PdfPCell cell8 = new PdfPCell();
                cell8.setBorderColor(BaseColor.BLACK);
                cell8.setBorder(Rectangle.ALIGN_RIGHT);
                cell8.setBorderWidth(1.5f);
                cell8.setFixedHeight(22);
                table5.addCell(cell8);
                table5.setLockedWidth(true);
                table5.setTotalWidth(Float.parseFloat(String.valueOf(PageSize.A4.getWidth()*0.72)));
                table5.writeSelectedRows(0, 1, doc.left(), doc.bottomMargin() + 11, writer.getDirectContent());
            }
            doc.close();
            filePath = pdfPath;
        } catch(Exception e) {
            e.printStackTrace();
        } finally {
            return filePath;
        }
    }

    /**
     * 中心处室发函发函公文生成pdf
     * @param hzTopDown
     */
    public String topToPdfZXCSFH(HzTopDown hzTopDown) {
        checkPath();
        Document doc = new Document(PageSize.A4, 78, 78 , 88, 88);
        String filePath = null;
        try {
            String pdfPath = path + hzTopDown.getId() + ".pdf";
            deleteOldFile(pdfPath);
            PdfWriter writer = PdfWriter.getInstance(doc, new FileOutputStream(pdfPath));
            writer.setPageEvent(new PdfExport());
            doc.open();
            /*Paragraph paragraphtitle = new Paragraph();
            paragraphtitle.setLeading(0);
            doc.add(paragraphtitle);*/
            // 标题
            Image image = Image.getInstance("C:\\font\\fhTitle.png");
            compressPicture(image);
            doc.add(image);
            /*Paragraph paragraphtitle1 = new Paragraph("\n");
            paragraphtitle1.setLeading(55);
            doc.add(paragraphtitle1);*/
            // 文件标号
            /*if (hzTopDown.getType() == 1) {
                PdfPTable table = new PdfPTable(2);
                table.setWidthPercentage(100);
                PdfPCell cell = new PdfPCell();
                Chunk chunk = new Chunk((""), fontNumber);
                Paragraph paragraph = new Paragraph();
                paragraph.add(chunk);
                cell.addElement(paragraph);
                cell.setBorder(Rectangle.NO_BORDER);
                cell.setFixedHeight(20);
                table.addCell(cell);

                PdfPCell cell1 = new PdfPCell();
                Paragraph paragraph1 = new Paragraph();
                Chunk chunk1 = new Chunk((hzTopDown.getUserName() != null ? hzTopDown.getUserName() : ""), fontQfr);
                Chunk chunk2 = new Chunk("签发人： ", fontNumber);
                paragraph1.add(chunk2);
                paragraph1.add(chunk1);
                paragraph1.setAlignment(Element.ALIGN_RIGHT);
                cell1.addElement(paragraph1);
                cell1.setBorder(Rectangle.NO_BORDER);
                table.addCell(cell1);
                doc.add(table);
            }*/
            // 添加分割线
            doc.add(lineTitle);
            Paragraph paragraphhx1 = new Paragraph();
            paragraphhx1.setLeading(0);
            paragraphhx1.add(lineTitle);
            Paragraph paragraphhx = new Paragraph();
            paragraphhx.setLeading(7);
            paragraphhx.add(linepx);
            doc.add(paragraphhx);
            if (hzTopDown.getFlowtype().equalsIgnoreCase("zhongxinfahan")) {
                Paragraph paragraphNumber = new Paragraph((hzTopDown.getFileNumber() != null ? hzTopDown.getFileNumber() + " " : ""), fontNumber);
                paragraphNumber.setLeading(35);
                paragraphNumber.setAlignment(Element.ALIGN_RIGHT);
                doc.add(paragraphNumber);
            }
            // 添加内容标题
            /*Paragraph paragraphmr = new Paragraph("\n山东省调水工程运行维护中心\n", fornContentTitle);
            paragraphmr.setAlignment(Element.ALIGN_CENTER);
            paragraphmr.setIndentationLeft(50);
            paragraphmr.setIndentationRight(50);
            doc.add(paragraphmr);*/

            Paragraph paragraph = new Paragraph((hzTopDown.getTitle() != null ? "\n" + hzTopDown.getTitle() : "" + "\n") + "\n\n", fornContentTitle);
            paragraph.setLeading(28);
            paragraph.setAlignment(Element.ALIGN_CENTER);
            doc.add(paragraph);
            // 发送单位
            Paragraph paragraphfsdw = new Paragraph(hzTopDown.getFsdw() != null ? hzTopDown.getFsdw() + "：" : "", fontcontent);
            paragraphfsdw.setLeading(30);
            paragraphfsdw.setAlignment(3);
            doc.add(paragraphfsdw);
            // 添加内容
            String content = replaceHtml(hzTopDown.getContent());
            if (StringUtils.isNotEmpty(content)) {
                String[] contents = content.split("\n", -1);
                for (String con : contents) {
                    if (con.indexOf("<h1>") != -1) {
                        Chunk chunkContent = new Chunk(con.replaceAll("<h1>", "").replaceAll("</h1>", ""), yijiTitle);
                        chunkContent.setSplitCharacter(ChineseSplitCharater.SplitCharacter);
                        Paragraph paragrapContent = new Paragraph(chunkContent);
                        paragrapContent.setLeading(30);
                        paragrapContent.setAlignment(0);
                        doc.add(paragrapContent);
                    } else if (con.indexOf("<h2>") != -1) {
                        Chunk chunkContent = new Chunk(con.replaceAll("<h2>", "").replaceAll("</h2>", ""), erjiTitle);
                        chunkContent.setSplitCharacter(ChineseSplitCharater.SplitCharacter);
                        Paragraph paragrapContent = new Paragraph(chunkContent);
                        paragrapContent.setLeading(30);
                        paragrapContent.setAlignment(0);
                        doc.add(paragrapContent);
                    } else {
                        Chunk chunkContent = new Chunk(con, fontcontent);
                        chunkContent.setSplitCharacter(ChineseSplitCharater.SplitCharacter);
                        Paragraph paragrapContent = new Paragraph(chunkContent);
                        paragrapContent.setLeading(30);
                        paragrapContent.setAlignment(0);
                        doc.add(paragrapContent);
                    }
                }
            }

            Paragraph paragraph1 = new Paragraph("\n\n\n\n\n\n", fontFort);
            doc.add(paragraph1);
            int endPx = 66 + 22 * hzTopDown.getEndSize();
            // 联系人
            /*if (StringUtils.isNotEmpty(hzTopDown.getLxr()) || StringUtils.isNotEmpty(hzTopDown.getLxdh())) {
                PdfPTable table4 = new PdfPTable(1);
                table4.setWidthPercentage(84);
                PdfPCell celllx = new PdfPCell();
                Paragraph paragraphlx = new Paragraph("   （联系人：" + hzTopDown.getLxr() + " ， 联系电话：" + hzTopDown.getLxdh() + "）", fontFort);
                paragraphlx.setLeading(17);
                celllx.addElement(paragraphlx);
                celllx.setBorderColor(BaseColor.BLACK);
                celllx.setBorder(Rectangle.NO_BORDER);
                celllx.setBorderWidth(1);
                celllx.setFixedHeight(28);
                table4.addCell(celllx);
                table4.setLockedWidth(true);
                table4.setTotalWidth(Float.parseFloat(String.valueOf(PageSize.A4.getWidth()*0.72)));
                table4.writeSelectedRows(0, 1, doc.left(), doc.bottomMargin() + 80, writer.getDirectContent());
            }*/
            // 抄送单位
            /*if (StringUtils.isNotEmpty(hzTopDown.getCsdw())) {
                PdfPTable table3 = new PdfPTable(1);
                table3.setWidthPercentage(30);
                PdfPCell cellcsdw = new PdfPCell();
                Paragraph paragraphcsdw = new Paragraph("  抄送：" + hzTopDown.getCsdw(), fontForter);
                paragraphcsdw.setLeading(15);
                cellcsdw.addElement(paragraphcsdw);
                cellcsdw.setBorderColor(BaseColor.BLACK);
                cellcsdw.setBorder(Rectangle.TOP);
                cellcsdw.setBorderWidth(1.5f);
                cellcsdw.setFixedHeight(24);
                table3.addCell(cellcsdw);
                table3.setLockedWidth(true);
                table3.setTotalWidth(Float.parseFloat(String.valueOf(PageSize.A4.getWidth()*0.72)));
                table3.writeSelectedRows(0, 1, doc.left(), doc.bottomMargin() + 11, writer.getDirectContent());
            }*/
            // 文件结尾固定底部内容
            Paragraph paragraphCompany = new Paragraph("山东省调水工程运行维护中心", fontFort);
            String date = hzTopDown.getDay();
            String[] dates = date.split("-");
            date = dates[0] + "年" + Integer.parseInt(dates[1]) + "月" + Integer.parseInt(dates[2]) + "日";
            String date1 = dates[0] + " 年 " + Integer.parseInt(dates[1]) + " 月 " + Integer.parseInt(dates[2]) + " 日";
            String bukongge = "";
            if (Integer.parseInt(dates[1]) < 10) {
                bukongge+=" ";
            }
            if (Integer.parseInt(dates[2]) < 10) {
                bukongge+=" ";
            }
            if (Integer.parseInt(dates[1]) < 10 || Integer.parseInt(dates[2]) < 10) {
                date1 = " " + date1;
            }
            if (Integer.parseInt(dates[1]) >= 10 && Integer.parseInt(dates[2]) >= 10) {
                date1 = " " + date1;
            }
            Paragraph paragraphDay = new Paragraph("   " + date1, fontFort);
            //
            PdfPTable table2 = new PdfPTable(2);
            table2.setWidthPercentage(84);
            PdfPCell cell5 = new PdfPCell();
            Paragraph pcell5 = new Paragraph("", fontcontent);
            cell5.addElement(pcell5);
            cell5.setBorderColor(BaseColor.BLACK);
            cell5.setHorizontalAlignment(Element.ALIGN_BOTTOM);
            cell5.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell5.setBorder(Rectangle.NO_BORDER);
            cell5.setFixedHeight(60);
            table2.addCell(cell5);

            PdfPCell cell3 = new PdfPCell();
            cell3.addElement(paragraphCompany);
            cell3.addElement(paragraphDay);
            cell3.setBorderColor(BaseColor.BLACK);
            cell3.setBorder(Rectangle.NO_BORDER);
            cell3.setFixedHeight(60);
            table2.addCell(cell3);
            table2.setLockedWidth(true);
            table2.setTotalWidth(Float.parseFloat(String.valueOf(PageSize.A4.getWidth()*0.72)));
            table2.writeSelectedRows(0, 1, doc.left() + 10, doc.bottomMargin() + 160, writer.getDirectContent());
            if (hzTopDown.getFlowtype().equalsIgnoreCase("zhongxinfahan")) {
                PdfPTable table1 = new PdfPTable(1);
                table1.setWidthPercentage(84);
                PdfPCell cell4 = new PdfPCell();
                Paragraph paragraph7 = new Paragraph("山东省调水工程运行维护中心办公室          " + bukongge + date + "印发", fontForter);
                paragraph7.setLeading(15);
                cell4.addElement(paragraph7);
                cell4.setBorderColor(BaseColor.BLACK);
                cell4.setBorder(Rectangle.ALIGN_JUSTIFIED);
                if (StringUtils.isNotEmpty(hzTopDown.getCsdw())) {
                    cell4.setBorderWidth(1);
                } else {
                    cell4.setBorderWidth(1.5f);
                }
                cell4.setFixedHeight(22);
                table1.addCell(cell4);

                table1.setLockedWidth(true);
                table1.setTotalWidth(Float.parseFloat(String.valueOf(PageSize.A4.getWidth() * 0.72)));
                table1.writeSelectedRows(0, 1, doc.left(), doc.bottomMargin() + 11, writer.getDirectContent());
            } else {
                PdfPTable table5 = new PdfPTable(1);
                PdfPCell cell8 = new PdfPCell();
                cell8.setBorderColor(BaseColor.RED);
                cell8.setBorder(Rectangle.ALIGN_RIGHT);
                cell8.setBorderWidth(1.5f);
                cell8.setFixedHeight(22);
                table5.addCell(cell8);
                table5.setLockedWidth(true);
                table5.setTotalWidth(Float.parseFloat(String.valueOf(PageSize.A4.getWidth()*0.72)));
                table5.writeSelectedRows(0, 1, doc.left(), doc.bottomMargin() + 18, writer.getDirectContent());
            }
            doc.close();
            filePath = pdfPath;
        } catch(Exception e) {
            e.printStackTrace();
        } finally {
            return filePath;
        }
    }


    /**
     * 党委公文生成pdf
     * @param hzTopDown
     */
    public String topToPdfDw(HzTopDown hzTopDown) {
        checkPath();
        Document doc = new Document(PageSize.A4, 78, 78 , 88, 88);
        String filePath = null;
        try {
            String pdfPath = path + hzTopDown.getId() + ".pdf";
            deleteOldFile(pdfPath);
            PdfWriter writer = PdfWriter.getInstance(doc, new FileOutputStream(pdfPath));
            writer.setPageEvent(new PdfExport());
            doc.open();
            Paragraph paragraphtitle = new Paragraph();
            paragraphtitle.setLeading(335);
            doc.add(paragraphtitle);
            // 标题
            Image image = Image.getInstance("C:\\font\\dw.png");
            compressPicture(image);
            doc.add(image);
            Paragraph paragraphtitle1 = new Paragraph("\n");
            paragraphtitle1.setLeading(55);
            doc.add(paragraphtitle1);
            // 文件标号
            String fileNumber = hzTopDown.getFileNumber();
            Paragraph paragraphNumber = new Paragraph(fileNumber, fontNumber);
            paragraphNumber.setAlignment(Element.ALIGN_CENTER);
            doc.add(paragraphNumber);
            // 添加分割线
            Image imagewjx = Image.getInstance("C:\\font\\wjx.png");
            compressPicture(imagewjx);
            doc.add(imagewjx);
            // 添加内容标题
            Paragraph paragraphmr = new Paragraph("\n中共山东省调水工程运行维护中心委员会\n", fornContentTitledw);
            paragraphmr.setAlignment(Element.ALIGN_CENTER);
            paragraphmr.setIndentationLeft(25);
            paragraphmr.setIndentationRight(25);
            doc.add(paragraphmr);

            Paragraph paragraph = new Paragraph((hzTopDown.getTitle() != null ? hzTopDown.getTitle() : "" + "\n") + "\n\n", fornContentTitledw);
            paragraph.setLeading(28);
            paragraph.setAlignment(Element.ALIGN_CENTER);
            doc.add(paragraph);
            // 发送单位
            Paragraph paragraphfsdw = new Paragraph(hzTopDown.getFsdw() != null ? hzTopDown.getFsdw() + "：" : "", fontcontent);
            paragraphfsdw.setLeading(30);
            paragraphfsdw.setAlignment(3);
            doc.add(paragraphfsdw);
            // 添加内容
            String content = replaceHtml(hzTopDown.getContent());
            if (StringUtils.isNotEmpty(content)) {
                String[] contents = content.split("\n", -1);
                for (String con : contents) {
                    if (con.indexOf("<h1>") != -1) {
                        Chunk chunkContent = new Chunk(con.replaceAll("<h1>", "").replaceAll("</h1>", ""), yijiTitle);
                        chunkContent.setSplitCharacter(ChineseSplitCharater.SplitCharacter);
                        Paragraph paragrapContent = new Paragraph(chunkContent);
                        paragrapContent.setLeading(30);
                        paragrapContent.setAlignment(0);
                        doc.add(paragrapContent);
                    } else if (con.indexOf("<h2>") != -1) {
                        Chunk chunkContent = new Chunk(con.replaceAll("<h2>", "").replaceAll("</h2>", ""), erjiTitle);
                        chunkContent.setSplitCharacter(ChineseSplitCharater.SplitCharacter);
                        Paragraph paragrapContent = new Paragraph(chunkContent);
                        paragrapContent.setLeading(30);
                        paragrapContent.setAlignment(0);
                        doc.add(paragrapContent);
                    } else {
                        Chunk chunkContent = new Chunk(con, fontcontent);
                        chunkContent.setSplitCharacter(ChineseSplitCharater.SplitCharacter);
                        Paragraph paragrapContent = new Paragraph(chunkContent);
                        paragrapContent.setLeading(30);
                        paragrapContent.setAlignment(0);
                        doc.add(paragrapContent);
                    }
                }
            }

            Paragraph paragraph1 = new Paragraph("\n\n\n\n\n\n", fontFort);
            doc.add(paragraph1);
            int endPx = 66 + 22 * hzTopDown.getEndSize();
            // 联系人
            /*if (StringUtils.isNotEmpty(hzTopDown.getLxr()) || StringUtils.isNotEmpty(hzTopDown.getLxdh())) {
                PdfPTable table4 = new PdfPTable(1);
                table4.setWidthPercentage(84);
                PdfPCell celllx = new PdfPCell();
                Paragraph paragraphlx = new Paragraph("   （联系人：" + hzTopDown.getLxr() + " ， 联系电话：" + hzTopDown.getLxdh() + "）", fontFort);
                paragraphlx.setLeading(17);
                celllx.addElement(paragraphlx);
                celllx.setBorderColor(BaseColor.BLACK);
                celllx.setBorder(Rectangle.NO_BORDER);
                celllx.setBorderWidth(1);
                celllx.setFixedHeight(28);
                table4.addCell(celllx);
                table4.setLockedWidth(true);
                table4.setTotalWidth(Float.parseFloat(String.valueOf(PageSize.A4.getWidth()*0.72)));
                table4.writeSelectedRows(0, 1, doc.left(), doc.bottomMargin() + 80, writer.getDirectContent());
            }*/
            // 抄送单位
            if (StringUtils.isNotEmpty(hzTopDown.getCsdw())) {
                endPx+=22;
                PdfPTable table3 = new PdfPTable(1);
                table3.setWidthPercentage(84);
                PdfPCell cellcsdw = new PdfPCell();
                Paragraph paragraphcsdw = new Paragraph("  抄送：" + hzTopDown.getCsdw(), fontForter);
                paragraphcsdw.setLeading(15);
                cellcsdw.addElement(paragraphcsdw);
                cellcsdw.setBorderColor(BaseColor.BLACK);
                cellcsdw.setBorder(Rectangle.TOP);
                cellcsdw.setBorderWidth(1.5f);
                cellcsdw.setFixedHeight(24);
                table3.addCell(cellcsdw);
                table3.setLockedWidth(true);
                table3.setTotalWidth(Float.parseFloat(String.valueOf(PageSize.A4.getWidth()*0.72)));
                table3.writeSelectedRows(0, 1, doc.left(), doc.bottomMargin() + 33, writer.getDirectContent());
            }
            // 文件结尾固定底部内容
            Paragraph paragraphCompany = new Paragraph("               中共山东省调水工程运行维护中心委员会", fontFort);
            String date = hzTopDown.getDay();
            String[] dates = date.split("-");
            date = dates[0] + "年" + Integer.parseInt(dates[1]) + "月" + Integer.parseInt(dates[2]) + "日";
            String date1 = dates[0] + " 年 " + Integer.parseInt(dates[1]) + " 月 " + Integer.parseInt(dates[2]) + " 日";
            String bukongge = "";
            if (Integer.parseInt(dates[1]) < 10) {
                bukongge+=" ";
            }
            if (Integer.parseInt(dates[2]) < 10) {
                bukongge+=" ";
            }
            if (Integer.parseInt(dates[1]) < 10 || Integer.parseInt(dates[2]) < 10) {
                date1 = " " + date1;
            }
            if (Integer.parseInt(dates[1]) >= 10 && Integer.parseInt(dates[2]) >= 10) {
                date1 = " " + date1;
            }
            Paragraph paragraphDay = new Paragraph("                         " + date1, fontFort);
            //
            PdfPTable table2 = new PdfPTable(1);
            table2.setWidthPercentage(84);
            PdfPCell cell5 = new PdfPCell();
            /*Paragraph pcell5 = new Paragraph("", fontcontent);
            cell5.addElement(pcell5);
            cell5.setBorderColor(BaseColor.BLACK);
            cell5.setHorizontalAlignment(Element.ALIGN_BOTTOM);
            cell5.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell5.setBorder(Rectangle.NO_BORDER);
            cell5.setFixedHeight(60);
            table2.addCell(cell5);*/

            PdfPCell cell3 = new PdfPCell();
            cell3.addElement(paragraphCompany);
            cell3.addElement(paragraphDay);
            cell3.setBorderColor(BaseColor.BLACK);
            cell3.setBorder(Rectangle.NO_BORDER);
            cell3.setFixedHeight(60);
            table2.addCell(cell3);
            table2.setLockedWidth(true);
            table2.setTotalWidth(Float.parseFloat(String.valueOf(PageSize.A4.getWidth()*0.72)));
            table2.writeSelectedRows(0, 1, doc.left() + 10, doc.bottomMargin() + 160, writer.getDirectContent());
            PdfPTable table1 = new PdfPTable(1);
            table1.setWidthPercentage(84);
            PdfPCell cell4 = new PdfPCell();
            Paragraph paragraph7 = new Paragraph("山东省调水工程运行维护中心                " + bukongge + date + "印发", fontForter);
            paragraph7.setLeading(15);
            cell4.addElement(paragraph7);
            cell4.setBorderColor(BaseColor.BLACK);
            cell4.setBorder(Rectangle.ALIGN_JUSTIFIED);
            if (StringUtils.isNotEmpty(hzTopDown.getCsdw())) {
                cell4.setBorderWidth(1);
            } else {
                cell4.setBorderWidth(1.5f);
            }
            cell4.setFixedHeight(22);
            table1.addCell(cell4);

            table1.setLockedWidth(true);
            table1.setTotalWidth(Float.parseFloat(String.valueOf(PageSize.A4.getWidth()*0.72)));
            table1.writeSelectedRows(0, 1, doc.left(), doc.bottomMargin() + 11, writer.getDirectContent());
            if (StringUtils.isNotEmpty(hzTopDown.getCsdw())) {
                PdfPTable table5 = new PdfPTable(1);
                PdfPCell cell8 = new PdfPCell();
                cell8.setBorderColor(BaseColor.BLACK);
                cell8.setBorder(Rectangle.ALIGN_RIGHT);
                cell8.setBorderWidth(1.5f);
                cell8.setFixedHeight(22);
                table5.addCell(cell8);
                table5.setLockedWidth(true);
                table5.setTotalWidth(Float.parseFloat(String.valueOf(PageSize.A4.getWidth()*0.72)));
                table5.writeSelectedRows(0, 1, doc.left(), doc.bottomMargin() + 11, writer.getDirectContent());
            }
            doc.close();
            filePath = pdfPath;
        } catch(Exception e) {
            e.printStackTrace();
        } finally {
            return filePath;
        }
    }

    /**
     * html去除html标签
     * @param html
     * @return
     */
    public String replaceHtml(String html) {
        if (StringUtils.isBlank(html)){
            return "";
        }
        html = html.replaceAll("&nbsp;", " ").replaceAll("<br>", "\n").replaceAll("</p>", "\n").replaceAll("&#x2014;", "——");
        /*String regEx = "<.+?>";
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(html);
        String s = m.replaceAll("");*/
        return html;
    }

    private Font gzFontTitle = new Font(st, 26, Font.BOLD, BaseColor.RED);
    private Font fzxgzFontTitle = new Font(st, 22, Font.BOLD, BaseColor.RED);
    private Font gzRedFort = new Font(st, 15, Font.NORMAL, BaseColor.RED);
    private Font gzFort = new Font(st, 15, Font.NORMAL, BaseColor.BLACK);
    // 中心发文稿纸标题
    private String GZ_ZXFW_TITLE = "\n山东省调水工程运行维护中心发文稿纸\n";

    private LineSeparator gzLine = new LineSeparator(1f,95,BaseColor.RED,Element.ALIGN_CENTER,-10f);

    /**
     * 中心发文稿纸转pdf
     * @param hzZxfwGz
     * @return
     */
    public String gzzxfwToPdf(HzZxfwGz hzZxfwGz) {
        String filePath = null;
        checkPath();
        Document doc = new Document(PageSize.A4);
        try {
            String pdfPath = path + hzZxfwGz.getId() + ".pdf";
            deleteOldFile(pdfPath);
            PdfWriter.getInstance(doc, new FileOutputStream(pdfPath));
            doc.open();
            // 标题
            Paragraph paragraphTitle = new Paragraph();
            // 根据发文机关确定发文标题
            switch (hzZxfwGz.getType() != null ? hzZxfwGz.getType() : "0") {
                case "1":
                    break;
                default:
                    if (StringUtils.isNotEmpty(hzZxfwGz.getFzx())) {
                        String bt = "\n山东省调水工程运行维护中心" + hzZxfwGz.getFzx() + "发文稿纸\n";
                        paragraphTitle = new Paragraph(bt, fzxgzFontTitle);
                    } else {
                        paragraphTitle = new Paragraph(GZ_ZXFW_TITLE, gzFontTitle);
                    }

            }
            paragraphTitle.setAlignment(Element.ALIGN_CENTER);
            paragraphTitle.setIndentationLeft(10);
            paragraphTitle.setIndentationRight(10);
            paragraphTitle.setSpacingAfter(10);
            doc.add(paragraphTitle);
            // 表格1
            PdfPTable table = new PdfPTable(3);
            table.setWidthPercentage(94);
            PdfPCell cell1 = new PdfPCell();
            Paragraph paragraph1 = new Paragraph("签 发： ", gzRedFort);
            cell1.addElement(paragraph1);
            Paragraph paragraph2 = new Paragraph(hzZxfwGz.getQf() != null ? rep(hzZxfwGz.getQf()) : "", gzFort);
            cell1.addElement(paragraph2);
            // 边框颜色
            cell1.setBorderColor(BaseColor.RED);
            cell1.setBorder(Rectangle.ALIGN_JUSTIFIED);
            cell1.setBorderWidth(2);
            cell1.setFixedHeight(110);
            table.addCell(cell1);

            PdfPCell cell2 = new PdfPCell();
            Paragraph paragraph3 = new Paragraph(" 审 阅： ", gzRedFort);
            cell2.addElement(paragraph3);
            Paragraph paragraph4 = new Paragraph(hzZxfwGz.getSy() != null ? rep(hzZxfwGz.getSy()) : "", gzFort);
            paragraph4.setIndentationLeft(8);
            cell2.addElement(paragraph4);
            cell2.setBorderColor(BaseColor.RED);
            cell2.setBorderWidth(2);
            table.addCell(cell2);

            PdfPCell cell3 = new PdfPCell();
            Paragraph paragraph5 = new Paragraph(" 会 签： ", gzRedFort);
            cell3.addElement(paragraph5);
            Paragraph paragraph6 = new Paragraph(hzZxfwGz.getHq() != null ? rep(hzZxfwGz.getHq()) : "", gzFort);
            paragraph6.setIndentationLeft(8);
            cell3.addElement(paragraph6);
            cell3.setBorderColor(BaseColor.RED);
            cell3.setBorderWidth(2);
            cell3.setBorder(Rectangle.ALIGN_BASELINE);
            table.addCell(cell3);
            doc.add(table);
            // 办公室处理意见
            PdfPTable yjtable = new PdfPTable(1);
            yjtable.setWidthPercentage(94);
            PdfPCell cellyj = new PdfPCell();
            Chunk yjTitle = new Chunk("办公室处理意见： ", gzRedFort);
            Paragraph pBgsyj = new Paragraph();
            pBgsyj.setLeading(20);
            pBgsyj.add(yjTitle);
            if (hzZxfwGz.getBgsyj() != null) {
                String[] yjs = rep(hzZxfwGz.getBgsyj()).split("\n", -1);
                Chunk yjContent = new Chunk(yjs[0], gzFort);
                pBgsyj.add(yjContent);
                cellyj.addElement(pBgsyj);
                if (yjs.length > 1) {
                    String content = "";
                    for (int i = 1; i < yjs.length; i++) {
                        content += yjs[i] + "\n";
                    }
                    if (yjs.length < 3) {
                        cellyj.setFixedHeight(90);
                    }
                    Paragraph pBgsyj1 = new Paragraph(content + "\n", gzFort);
                    cellyj.addElement(pBgsyj1);
                } else {
                    cellyj.setFixedHeight(90);
                }
            } else {
                cellyj.addElement(pBgsyj);
                cellyj.setFixedHeight(90);
            }
            // 边框颜色
            cellyj.setBorderColor(BaseColor.RED);
            cellyj.setBorder(Rectangle.NO_BORDER);
            cellyj.setBorderWidth(2);
            yjtable.addCell(cellyj);
            doc.add(yjtable);
            // 表格2
            PdfPTable table2 = new PdfPTable(2);
            table2.setWidthPercentage(94);
            PdfPCell cellngr = new PdfPCell();
            Paragraph bgsngrTitle = new Paragraph();
            bgsngrTitle.setLeading(20);
            Chunk ngrTitle = new Chunk((hzZxfwGz.getNgrcs() != null ? hzZxfwGz.getNgrcs() : "") +  "拟稿人：", gzRedFort);
            Chunk ngrContent = new Chunk(hzZxfwGz.getNgr() != null ? rep(hzZxfwGz.getNgr()) : "", gzFort);
            bgsngrTitle.add(ngrTitle);
            bgsngrTitle.add(ngrContent);
            cellngr.addElement(bgsngrTitle);
            Paragraph bgsngrphoneTitle = new Paragraph();
            bgsngrphoneTitle.setLeading(40);
            Chunk ngrphoneTitle = new Chunk("联系电话：", gzRedFort);
            Chunk ngrphoneContent = new Chunk(hzZxfwGz.getNgrPhone() != null ? rep(hzZxfwGz.getNgrPhone()) : "", gzFort);
            bgsngrphoneTitle.add(ngrphoneTitle);
            bgsngrphoneTitle.add(ngrphoneContent);
            cellngr.addElement(bgsngrphoneTitle);
            // 边框颜色
            cellngr.setBorderColor(BaseColor.RED);
            cellngr.setBorder(Rectangle.ALIGN_JUSTIFIED);
            cellngr.setBorderWidth(2);
            cellngr.setFixedHeight(75);
            table2.addCell(cellngr);

            PdfPCell cellhgr = new PdfPCell();
            Paragraph bgshgrTitle = new Paragraph();
            bgshgrTitle.setLeading(20);
            Chunk hgrTitle = new Chunk("  核稿人：", gzRedFort);
            Chunk hgrContent = new Chunk(hzZxfwGz.getHgr() != null ? rep(hzZxfwGz.getHgr()) : "", gzFort);
            bgshgrTitle.add(hgrTitle);
            bgshgrTitle.add(hgrContent);
            cellhgr.addElement(bgshgrTitle);
            Paragraph bgshgrphoneTitle = new Paragraph();
            bgshgrphoneTitle.setLeading(40);
            Chunk hgrphoneTitle = new Chunk("  联系电话：", gzRedFort);
            Chunk hgrphoneContent = new Chunk(hzZxfwGz.getHgrPhone() != null ? rep(hzZxfwGz.getHgrPhone()) : "", gzFort);
            bgshgrphoneTitle.add(hgrphoneTitle);
            bgshgrphoneTitle.add(hgrphoneContent);
            cellhgr.addElement(bgshgrphoneTitle);
            // 边框颜色
            cellhgr.setBorderColor(BaseColor.RED);
            cellhgr.setBorder(Rectangle.ALIGN_BASELINE);
            cellhgr.setBorderWidth(2);
            table2.addCell(cellhgr);
            doc.add(table2);
            // 公文标题
            PdfPTable gwtable = new PdfPTable(1);
            gwtable.setWidthPercentage(94);
            PdfPCell cellgw = new PdfPCell();
            Paragraph pTitle = new Paragraph();
            pTitle.setLeading(20);
            Chunk gwTitle = new Chunk("公文标题：", gzRedFort);
            pTitle.add(gwTitle);
            if (hzZxfwGz.getTitle() != null) {
                String[] titles = rep(hzZxfwGz.getTitle()).split("\n", -1);
                Chunk gwContent = new Chunk(titles[0], gzFort);
                pTitle.add(gwContent);
                cellgw.addElement(pTitle);
                if (titles.length > 1) {
                    String content = "";
                    for (int i = 1; i < titles.length; i++) {
                        content += titles[i] + "\n";
                    }
                    if (titles.length < 3) {
                        cellgw.setFixedHeight(90);
                    }
                    Paragraph paragraphTitle2 = new Paragraph(content + "\n", gzFort);
                    cellgw.addElement(paragraphTitle2);
                } else {
                    cellgw.setFixedHeight(90);
                }
            } else {
                cellgw.addElement(pTitle);
                cellgw.setFixedHeight(90);
            }
            // 边框颜色
            cellgw.setBorderColor(BaseColor.RED);
            cellgw.setBorder(Rectangle.NO_BORDER);
            cellgw.setBorderWidth(2);
            gwtable.addCell(cellgw);
            doc.add(gwtable);
            // 主送单位表格
            PdfPTable table4 = new PdfPTable(1);
            table4.setWidthPercentage(94);
            PdfPCell cell9 = new PdfPCell();
            Paragraph paragraph = new Paragraph();
            paragraph.setLeading(20);
            Chunk zsdwTitle = new Chunk("主送单位：", gzRedFort);
            paragraph.add(zsdwTitle);
            if (hzZxfwGz.getZsCompany() != null) {
                String[] zsCompanys = rep(hzZxfwGz.getZsCompany()).split("\n", -1);
                Chunk zsdwContent = new Chunk(zsCompanys[0], gzFort);
                paragraph.add(zsdwContent);
                cell9.addElement(paragraph);
                if (zsCompanys.length > 1) {
                    String content = "";
                    for (int i = 1; i < zsCompanys.length; i++) {
                        content += zsCompanys[i] + "\n";
                    }
                    if (zsCompanys.length < 3) {
                        cell9.setFixedHeight(90);
                    }
                    Paragraph paragraphZs = new Paragraph(content + "\n", gzFort);
                    cell9.addElement(paragraphZs);
                } else {
                    cell9.setFixedHeight(90);
                }
            } else {
                cell9.addElement(paragraph);
                cell9.setFixedHeight(90);
            }
            // 边框颜色
            cell9.setBorderColor(BaseColor.RED);
            cell9.setBorder(Rectangle.ALIGN_JUSTIFIED);
            cell9.setBorderWidth(2);
            table4.addCell(cell9);
            doc.add(table4);
            //抄送单位表格
            PdfPTable table5 = new PdfPTable(1);
            table5.setWidthPercentage(94);
            PdfPCell cellcsdw = new PdfPCell();
            Paragraph paragraphcsdw = new Paragraph();
            paragraphcsdw.setLeading(20);
            Chunk csdwTitle = new Chunk("抄送单位：", gzRedFort);
            paragraphcsdw.add(csdwTitle);
            if (hzZxfwGz.getCsCompany() != null) {
                String[] csCompany = rep(hzZxfwGz.getCsCompany()).split("\n", -1);
                Chunk csdwContent = new Chunk(csCompany[0], gzFort);
                paragraphcsdw.add(csdwContent);
                cellcsdw.addElement(paragraphcsdw);
                if (csCompany.length > 1) {
                    String contet = "";
                    for (int i = 1; i < csCompany.length; i++) {
                        contet += csCompany[i] + "\n";
                    }
                    if (csCompany.length < 3) {
                        cellcsdw.setFixedHeight(90);
                    }
                    Paragraph paragraphCs = new Paragraph(contet + "\n", gzFort);
                    cellcsdw.addElement(paragraphCs);
                } else {
                    cellcsdw.setFixedHeight(90);
                }
            } else {
                cellcsdw.addElement(paragraphcsdw);
                cellcsdw.setFixedHeight(90);
            }
            // 边框颜色
            cellcsdw.setBorderColor(BaseColor.RED);
            cellcsdw.setBorder(Rectangle.ALIGN_JUSTIFIED);
            cellcsdw.setBorderWidth(2);
            table5.addCell(cellcsdw);
            doc.add(table5);
            // 打印验证等
            Paragraph paragraphdy = new Paragraph();
            paragraphdy.setAlignment(Element.ALIGN_LEFT);
            paragraphdy.setLeading(20);
            Chunk dyTitle = new Chunk("打印份数：  ", gzRedFort);
            Chunk dyContent = new Chunk(hzZxfwGz.getNumber() != null ? rep(hzZxfwGz.getNumber()) : "", gzFort);
            Chunk dyfen = new Chunk("     份", gzRedFort);
            Paragraph paragraphdy2 = new Paragraph();
            paragraphdy2.setAlignment(Element.ALIGN_RIGHT);
            paragraphdy2.setLeading(20);
            paragraphdy2.setIndentationRight(60);
            Chunk jd = new Chunk("校对： ", gzRedFort);
            Chunk user = new Chunk(hzZxfwGz.getUserName() != null ? rep(hzZxfwGz.getUserName()) : "", gzFort);
            paragraphdy.add(dyTitle);
            paragraphdy.add(dyContent);
            paragraphdy.add(dyfen);
            paragraphdy2.add(jd);
            paragraphdy2.add(user);
            PdfPTable table6 = new PdfPTable(2);
            table6.setWidthPercentage(94);
            PdfPCell celldy = new PdfPCell();
            celldy.addElement(paragraphdy);
            // 边框颜色
            celldy.setBorderColor(BaseColor.RED);
            celldy.setBorder(Rectangle.ALIGN_JUSTIFIED);
            celldy.setBorderWidth(2);
            celldy.setFixedHeight(30);
            table6.addCell(celldy);
            PdfPCell celldy2 = new PdfPCell();
            celldy2.addElement(paragraphdy2);
            // 边框颜色
            celldy2.setBorderColor(BaseColor.RED);
            celldy2.setBorder(Rectangle.ALIGN_JUSTIFIED);
            celldy2.setBorderWidth(2);
            celldy2.setFixedHeight(30);
            table6.addCell(celldy2);
            doc.add(table6);
            // 发文字号
            Paragraph paragraphfwzh = new Paragraph();
            paragraphfwzh.setLeading(20);
            Chunk fwzhTitle = new Chunk("发文字号：", gzRedFort);
            Chunk fwzhContent = new Chunk(hzZxfwGz.getFileNumber() != null ? rep(hzZxfwGz.getFileNumber()) : "", gzFort);
            paragraphfwzh.add(fwzhTitle);
            paragraphfwzh.add(fwzhContent);
            PdfPTable table7 = new PdfPTable(1);
            table7.setWidthPercentage(94);
            PdfPCell cellfwzh = new PdfPCell();
            cellfwzh.addElement(paragraphfwzh);
            // 边框颜色
            cellfwzh.setBorderColor(BaseColor.RED);
            cellfwzh.setBorder(Rectangle.NO_BORDER);
            cellfwzh.setBorderWidth(2);
            cellfwzh.setFixedHeight(30);
            table7.addCell(cellfwzh);
            doc.add(table7);
            // 印发机关
            Paragraph paragraphyfjg = new Paragraph();
            paragraphyfjg.setLeading(18);
            Paragraph paragraphyfjg2 = new Paragraph();
            paragraphyfjg2.setLeading(20);
            Chunk yfjgTitle = new Chunk("印制机关：", gzRedFort);
            Chunk yfjgContent = null;
            if (StringUtils.isNotEmpty(hzZxfwGz.getFzx())) {
                yfjgContent = new Chunk((hzZxfwGz.getYfjg() != null ? rep(hzZxfwGz.getYfjg()) : "") + " ", gzFort);
            } else {
                yfjgContent = new Chunk((hzZxfwGz.getYfjg() != null ? rep(hzZxfwGz.getYfjg()) : "") + "办公室     ", gzFort);
            }
            String[] dates = hzZxfwGz.getDay().split("-", -1);
            String buwei = "";
            if (Integer.parseInt(dates[1]) < 10) {
                buwei+=" ";
            }
            if (Integer.parseInt(dates[2]) < 10) {
                buwei+=" ";
            }
            Chunk day = new Chunk(buwei + dates[0] + "年" + Integer.parseInt(dates[1]) + "月" + Integer.parseInt(dates[2]) + "日", gzFort);
            Chunk ff = new Chunk("封发", gzRedFort);
            paragraphyfjg.add(yfjgTitle);
            paragraphyfjg.add(yfjgContent);
            paragraphyfjg.add(day);
            paragraphyfjg.add(ff);
            paragraphyfjg.setIndentationRight(0);
            PdfPTable table8 = new PdfPTable(1);
            table8.setWidthPercentage(94);
            PdfPCell cellyfjg = new PdfPCell();
            cellyfjg.addElement(paragraphyfjg);
            // 边框颜色
            cellyfjg.setBorderColor(BaseColor.RED);
            cellyfjg.setBorder(Rectangle.ALIGN_JUSTIFIED);
            cellyfjg.setBorderWidth(2);
            cellyfjg.setFixedHeight(28);
            table8.addCell(cellyfjg);
            doc.add(table8);
            doc.close();
            filePath = pdfPath;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            return filePath;
        }
    }

    private String GZ_DWFW_TITLE = "\n中共山东省调水工程运行维护中心委员会  稿纸";

    /**
     * 党委发文转pdf
     * @param hzDwfwGz
     * @return
     */

    private Font gzFontTitleDW = new Font(st, 24, Font.BOLD, BaseColor.RED);
    public String gzdwfwToPdf(HzDwfwGz hzDwfwGz) {
        String filePath = null;
        checkPath();
        Document doc = new Document(PageSize.A4);
        try {
            String pdfPath = path + hzDwfwGz.getId() + ".pdf";
            deleteOldFile(pdfPath);
            PdfWriter.getInstance(doc, new FileOutputStream(pdfPath));
            doc.open();
            // 标题
            Paragraph paragraphTitle = new Paragraph(GZ_DWFW_TITLE, gzFontTitleDW);
            paragraphTitle.setAlignment(Element.ALIGN_CENTER);
            paragraphTitle.setIndentationLeft(5);
            paragraphTitle.setIndentationRight(5);
            paragraphTitle.setSpacingAfter(10);
            doc.add(paragraphTitle);
            // 机密等级
            Paragraph paragraphjmdj = new Paragraph();
            paragraphjmdj.setLeading(20);
            Chunk jmdjTitle = new Chunk("机密等级：", gzRedFort);
            Chunk jmdjContent = new Chunk(hzDwfwGz.getMj() != null ? rep(hzDwfwGz.getMj()) : "", gzFort);
            paragraphjmdj.add(jmdjTitle);
            paragraphjmdj.add(jmdjContent);
            PdfPTable tablejmdj = new PdfPTable(1);
            tablejmdj.setWidthPercentage(95);
            PdfPCell celljmdj = new PdfPCell();
            celljmdj.addElement(paragraphjmdj);
                // 边框颜色
            celljmdj.setBorderColor(BaseColor.RED);
            celljmdj.setBorder(Rectangle.ALIGN_JUSTIFIED);
            celljmdj.setBorderWidth(2);
            celljmdj.setFixedHeight(35);
            tablejmdj.addCell(celljmdj);
            doc.add(tablejmdj);
            // 签发/审签表格
                // 签发
            PdfPTable tableqf = new PdfPTable(1);
            tableqf.setWidthPercentage(95);
            PdfPCell cellqf = new PdfPCell();
            Paragraph paragraphqf = new Paragraph();
            Chunk qfTitle = new Chunk("签     发：", gzRedFort);
            paragraphqf.add(qfTitle);
            if (hzDwfwGz.getQf() != null) {
                String[] qfs = rep(hzDwfwGz.getQf()).split("\n", -1);
                Chunk qfContent = new Chunk(qfs[0], gzFort);
                paragraphqf.add(qfContent);
                cellqf.addElement(paragraphqf);
                if (qfs.length > 1) {
                    String content = "";
                    for (int i=1; i < qfs.length; i++) {
                        content+=qfs[i] + "\n";
                    }
                    if (qfs.length < 2) {
                        cellqf.setFixedHeight(50);
                    }
                    Paragraph paragraphqf1 = new Paragraph(content + "\n", gzFort);
                    cellqf.addElement(paragraphqf1);
                } else {
                    cellqf.setFixedHeight(60);
                }
            } else {
                cellqf.addElement(paragraphqf);
                cellqf.setFixedHeight(60);
            }
                // 边框颜色
            cellqf.setBorderColor(BaseColor.RED);
            cellqf.setBorder(Rectangle.ALIGN_JUSTIFIED);
            cellqf.setBorderWidth(1);
            tableqf.addCell(cellqf);
            doc.add(tableqf);
            // 会签
            PdfPTable tablehq = new PdfPTable(1);
            tablehq.setWidthPercentage(95);
            PdfPCell cellhq = new PdfPCell();
            Paragraph paragraphhq = new Paragraph();
            Chunk hqTitle = new Chunk("会     签：", gzRedFort);
            paragraphhq.add(hqTitle);
            if (hzDwfwGz.getQf() != null) {
                String[] hqs = rep(hzDwfwGz.getHq()).split("\n", -1);
                Chunk hqContent = new Chunk(hqs[0], gzFort);
                paragraphhq.add(hqContent);
                cellhq.addElement(paragraphhq);
                if (hqs.length > 1) {
                    String content = "";
                    for (int i=1; i < hqs.length; i++) {
                        content+=hqs[i] + "\n";
                    }
                    if (hqs.length < 2) {
                        cellhq.setFixedHeight(50);
                    }
                    Paragraph paragraphhq1 = new Paragraph(content + "\n", gzFort);
                    cellhq.addElement(paragraphhq1);
                } else {
                    cellhq.setFixedHeight(60);
                }
            } else {
                cellhq.addElement(paragraphhq);
                cellhq.setFixedHeight(60);
            }
            // 边框颜色
            cellhq.setBorderColor(BaseColor.RED);
            cellhq.setBorder(Rectangle.ALIGN_JUSTIFIED);
            cellhq.setBorderWidth(2);
            tablehq.addCell(cellhq);
            doc.add(tablehq);
            // 处理意见
            PdfPTable tableclyj = new PdfPTable(1);
            tableclyj.setWidthPercentage(95);
            PdfPCell cellclyj = new PdfPCell();
            Paragraph paragraphclyj = new Paragraph();
            Chunk clyjTitle = new Chunk("处理意见：", gzRedFort);
            paragraphclyj.add(clyjTitle);
            if (hzDwfwGz.getQf() != null) {
                String[] clyjs = rep(hzDwfwGz.getClyj()).split("\n", -1);
                Chunk clyjContent = new Chunk(clyjs[0], gzFort);
                paragraphclyj.add(clyjContent);
                cellclyj.addElement(paragraphclyj);
                if (clyjs.length > 1) {
                    String content = "";
                    for (int i=1; i < clyjs.length; i++) {
                        content+=clyjs[i] + "\n";
                    }
                    if (clyjs.length < 4) {
                        cellclyj.setFixedHeight(90);
                    }
                    Paragraph paragraphclyj1 = new Paragraph(content + "\n", gzFort);
                    cellclyj.addElement(paragraphclyj1);
                } else {
                    cellclyj.setFixedHeight(90);
                }
            } else {
                cellclyj.addElement(paragraphclyj);
                cellclyj.setFixedHeight(90);
            }
            // 边框颜色
            cellclyj.setBorderColor(BaseColor.RED);
            cellclyj.setBorder(Rectangle.ALIGN_JUSTIFIED);
            cellclyj.setBorderWidth(2);
            tableclyj.addCell(cellclyj);
            doc.add(tableclyj);
            // 拟稿人、核稿人
            PdfPTable tablenhgr = new PdfPTable(2);
            tablenhgr.setWidthPercentage(95);
                // 拟稿人
            PdfPCell cellngr = new PdfPCell();
            Paragraph paragraphngr = new Paragraph();
            Chunk ngrTitle = new Chunk((hzDwfwGz.getNgrcs() != null ? rep(hzDwfwGz.getNgrcs()) : "") + "拟稿人：", gzRedFort);
            Chunk ngrContent = new Chunk(hzDwfwGz.getNgr() != null ? rep(hzDwfwGz.getNgr()) : "", gzFort);
            paragraphngr.add(ngrTitle);
            paragraphngr.add(ngrContent);
            cellngr.addElement(paragraphngr);
            Paragraph paragraphngrphone = new Paragraph();
            Chunk ngrphoneTitle = new Chunk("联系电话：", gzRedFort);
            Chunk ngrphoneContent = new Chunk(hzDwfwGz.getNgrPhone() != null ? rep(hzDwfwGz.getNgrPhone()) : "", gzFort);
            paragraphngrphone.add(ngrphoneTitle);
            paragraphngrphone.add(ngrphoneContent);
            paragraphngrphone.setLeading(30);
            cellngr.addElement(paragraphngrphone);
                    // 边框设置
            cellngr.setBorderColor(BaseColor.RED);
            cellngr.setBorder(Rectangle.ALIGN_JUSTIFIED);
            cellngr.setBorderWidth(2);
            cellngr.setFixedHeight(60);
            tablenhgr.addCell(cellngr);
                // 核稿人
            PdfPCell cellhgr = new PdfPCell();
            Paragraph paragraphhgr = new Paragraph();
            Chunk hgrTitle = new Chunk("拟办单位核稿人：", gzRedFort);
            Chunk hgrContent = new Chunk(hzDwfwGz.getHgr() != null ? rep(hzDwfwGz.getHgr()) : "", gzFort);
            paragraphhgr.add(hgrTitle);
            paragraphhgr.add(hgrContent);
            cellhgr.addElement(paragraphhgr);
                    // 边框设置
            cellhgr.setBorderColor(BaseColor.RED);
            cellhgr.setBorder(Rectangle.ALIGN_BASELINE);
            cellhgr.setBorderWidth(2);
            cellhgr.setFixedHeight(50);
            tablenhgr.addCell(cellhgr);
            doc.add(tablenhgr);
            // 本文标题
            PdfPTable tablebwbt = new PdfPTable(1);
            tablebwbt.setWidthPercentage(95);
            PdfPCell cellbwbt = new PdfPCell();
            Paragraph paragraphbwbt = new Paragraph();
            Chunk bwbtTitle = new Chunk("本文标题：", gzRedFort);
            paragraphbwbt.add(bwbtTitle);
            if (hzDwfwGz.getQf() != null) {
                String[] bwbts = rep(hzDwfwGz.getBwbt()).split("\n", -1);
                Chunk bwbtContent = new Chunk(bwbts[0], gzFort);
                paragraphbwbt.add(bwbtContent);
                cellbwbt.addElement(paragraphbwbt);
                if (bwbts.length > 1) {
                    String content = "";
                    for (int i=1; i < bwbts.length; i++) {
                        content+=bwbts[i] + "\n";
                    }
                    if (bwbts.length < 3) {
                        cellbwbt.setFixedHeight(70);
                    }
                    Paragraph paragraphbwbt1 = new Paragraph(content + "\n", gzFort);
                    cellbwbt.addElement(paragraphbwbt1);
                } else {
                    cellbwbt.setFixedHeight(70);
                }
            } else {
                cellbwbt.addElement(paragraphbwbt);
                cellbwbt.setFixedHeight(70);
            }
            // 边框颜色
            cellbwbt.setBorderColor(BaseColor.RED);
            cellbwbt.setBorder(Rectangle.ALIGN_JUSTIFIED);
            cellbwbt.setBorderWidth(2);
            tablebwbt.addCell(cellbwbt);
            doc.add(tablebwbt);
            // 发送单位
            PdfPTable tablefsdw = new PdfPTable(1);
            tablefsdw.setWidthPercentage(95);
            PdfPCell cellfsdw = new PdfPCell();
            Paragraph paragraphfsdw = new Paragraph();
            Chunk fsdwTitle = new Chunk("发送单位：", gzRedFort);
            paragraphfsdw.add(fsdwTitle);
            if (hzDwfwGz.getFsdw() != null) {
                String[] fsdws = rep(hzDwfwGz.getFsdw()).split("\n", -1);
                Chunk fsdwContent = new Chunk(fsdws[0], gzFort);
                paragraphfsdw.add(fsdwContent);
                cellfsdw.addElement(paragraphfsdw);
                if (fsdws.length > 1) {
                    String content = "";
                    for (int i=1; i < fsdws.length; i++) {
                        content+=fsdws[i] + "\n";
                    }
                    if (fsdws.length < 2) {
                        cellfsdw.setFixedHeight(70);
                    }
                    Paragraph paragraphfsdw1 = new Paragraph(content + "\n", gzFort);
                    cellfsdw.addElement(paragraphfsdw1);
                } else {
                    cellfsdw.setFixedHeight(70);
                }
            } else {
                cellfsdw.addElement(paragraphfsdw);
                cellfsdw.setFixedHeight(70);
            }
            // 边框颜色
            cellfsdw.setBorderColor(BaseColor.RED);
            cellfsdw.setBorder(Rectangle.ALIGN_JUSTIFIED);
            cellfsdw.setBorderWidth(2);
            tablefsdw.addCell(cellfsdw);
            doc.add(tablefsdw);
            // 抄报单位
            PdfPTable tablecbdw = new PdfPTable(1);
            tablecbdw.setWidthPercentage(95);
            PdfPCell cellcbdw = new PdfPCell();
            Paragraph paragraphcbdw = new Paragraph();
            Chunk cbdwTitle = new Chunk("抄报单位：", gzRedFort);
            paragraphcbdw.add(cbdwTitle);
            if (hzDwfwGz.getCbdw() != null) {
                String[] cbdws = rep(hzDwfwGz.getCbdw()).split("\n", -1);
                Chunk cbdwContent = new Chunk(cbdws[0], gzFort);
                paragraphcbdw.add(cbdwContent);
                cellcbdw.addElement(paragraphcbdw);
                if (cbdws.length > 1) {
                    String content = "";
                    for (int i=1; i < cbdws.length; i++) {
                        content+=cbdws[i] + "\n";
                    }
                    if (cbdws.length < 3) {
                        cellcbdw.setFixedHeight(70);
                    }
                    Paragraph paragraphcbdw1 = new Paragraph(content + "\n", gzFort);
                    cellcbdw.addElement(paragraphcbdw1);
                } else {
                    cellcbdw.setFixedHeight(70);
                }
            } else {
                cellcbdw.addElement(paragraphcbdw);
                cellcbdw.setFixedHeight(70);
            }
            // 边框颜色
            cellcbdw.setBorderColor(BaseColor.RED);
            cellcbdw.setBorder(Rectangle.ALIGN_JUSTIFIED);
            cellcbdw.setBorderWidth(2);
            tablecbdw.addCell(cellcbdw);
            doc.add(tablecbdw);
            // 抄送单位
            PdfPTable tablecsdw = new PdfPTable(1);
            tablecsdw.setWidthPercentage(95);
            PdfPCell cellcsdw = new PdfPCell();
            Paragraph paragraphcsdw = new Paragraph();
            Chunk csdwTitle = new Chunk("抄送单位：", gzRedFort);
            paragraphcsdw.add(csdwTitle);
            if (hzDwfwGz.getCsdw() != null) {
                String[] csdws = rep(hzDwfwGz.getCsdw()).split("\n", -1);
                Chunk csdwContent = new Chunk(csdws[0], gzFort);
                paragraphcsdw.add(csdwContent);
                cellcsdw.addElement(paragraphcsdw);
                if (csdws.length > 1) {
                    String content = "";
                    for (int i=1; i < csdws.length; i++) {
                        content+=csdws[i] + "\n";
                    }
                    if (csdws.length < 3) {
                        cellcsdw.setFixedHeight(70);
                    }
                    Paragraph paragraphcsdw1 = new Paragraph(content + "\n", gzFort);
                    cellcsdw.addElement(paragraphcsdw1);
                } else {
                    cellcsdw.setFixedHeight(70);
                }
            } else {
                cellcsdw.addElement(paragraphcsdw);
                cellcsdw.setFixedHeight(70);
            }
            // 边框颜色
            cellcsdw.setBorderColor(BaseColor.RED);
            cellcsdw.setBorder(Rectangle.ALIGN_JUSTIFIED);
            cellcsdw.setBorderWidth(2);
            tablecsdw.addCell(cellcsdw);
            doc.add(tablecsdw);
            // 打印信息
            PdfPTable pdfPTabledy = new PdfPTable(3);
            pdfPTabledy.setWidthPercentage(95);
            PdfPCell celldy = new PdfPCell();
            Paragraph paragraphdy = new Paragraph();
            PdfPCell celldz = new PdfPCell();
            Paragraph paragraphdz = new Paragraph();
            PdfPCell celljd = new PdfPCell();
            Paragraph paragraphjd = new Paragraph();
            Chunk dyTitle = new Chunk("打印份数: ", gzRedFort);
            Chunk dyNumber = new Chunk(hzDwfwGz.getNumber() != null ? rep(hzDwfwGz.getNumber()) : "", gzFort);
            Chunk dyFen = new Chunk(" 份", gzRedFort);
            Chunk dzTitle = new Chunk("打字:", gzRedFort);
            Chunk dzContent = new Chunk(hzDwfwGz.getDz() != null ? rep(hzDwfwGz.getDz()) : "", gzFort);
            Chunk jdTitle = new Chunk("校对:", gzRedFort);
            Chunk jdContent = new Chunk(hzDwfwGz.getUserName() != null ? rep(hzDwfwGz.getUserName()) : "", gzFort);
            paragraphdy.setAlignment(Element.ALIGN_LEFT);
            paragraphdy.setLeading(18);
            paragraphdy.add(dyTitle);
            paragraphdy.add(dyNumber);
            paragraphdy.add(dyFen);
            paragraphdz.setAlignment(Element.ALIGN_LEFT);
            paragraphdz.setLeading(18);
            paragraphdz.add(dzTitle);
            paragraphdz.add(dzContent);
            paragraphjd.setAlignment(Element.ALIGN_RIGHT);
            paragraphjd.setLeading(18);
            paragraphjd.add(jdTitle);
            paragraphjd.add(jdContent);
            celldy.addElement(paragraphdy);
            celldz.addElement(paragraphdz);
            celljd.addElement(paragraphjd);
                // 边框设置
            celldy.setBorderColor(BaseColor.RED);
            celldy.setBorder(Rectangle.ALIGN_JUSTIFIED);
            celldy.setBorderWidth(1);
            celldy.setFixedHeight(30);
            celldz.setBorderColor(BaseColor.RED);
            celldz.setBorder(Rectangle.ALIGN_JUSTIFIED);
            celldz.setBorderWidth(1);
            celldz.setFixedHeight(30);
            celljd.setBorderColor(BaseColor.RED);
            celljd.setBorder(Rectangle.ALIGN_JUSTIFIED);
            celljd.setBorderWidth(1);
            celljd.setFixedHeight(30);
            pdfPTabledy.addCell(celldy);
            pdfPTabledy.addCell(celldz);
            pdfPTabledy.addCell(celljd);
            doc.add(pdfPTabledy);
            // 发文
            PdfPTable pdfPTablefw = new PdfPTable(2);
            pdfPTablefw.setWidthPercentage(95);
            PdfPCell cellfw = new PdfPCell();
            PdfPCell cellday = new PdfPCell();
            Paragraph paragraphday = new Paragraph();
            paragraphday.setLeading(18);
            paragraphday.setAlignment(Element.ALIGN_RIGHT);
            Paragraph paragraphfw = new Paragraph();
            paragraphfw.setLeading(18);
            Chunk fwTitle = new Chunk("发文: 鲁调水党字〔", gzRedFort);
            paragraphfw.add(fwTitle);
            if (StringUtils.isNotEmpty(hzDwfwGz.getFw())) {
                String[] zh = rep(hzDwfwGz.getFw()).split("-");
                if (zh.length == 2) {
                    Chunk zi = new Chunk(zh[0], gzFort);
                    paragraphfw.add(zi);
                    Chunk kuang = new Chunk("〕", gzRedFort);
                    paragraphfw.add(kuang);
                    Chunk hao = new Chunk(zh[1], gzFort);
                    paragraphfw.add(hao);
                } else {
                    Chunk kuang = new Chunk("〕", gzRedFort);
                    paragraphfw.add(kuang);
                }
            } else {
                Chunk kuang = new Chunk("〕", gzRedFort);
                paragraphfw.add(kuang);
            }
            Chunk fwHao = new Chunk("号", gzRedFort);
            paragraphfw.add(fwHao);
            cellfw.addElement(paragraphfw);
            String date = rep(hzDwfwGz.getDay());
            if (StringUtils.isNotEmpty(date)) {
                String[] dates = date.split("-", -1);
                if (dates.length == 3) {
                    String buwei = "";
                    if (Integer.parseInt(dates[1]) < 10) {
                        buwei+=" ";
                    }
                    if (Integer.parseInt(dates[2]) < 10) {
                        buwei+=" ";
                    }
                    Chunk year = new Chunk(buwei + dates[0], gzFort);
                    paragraphday.add(year);
                    Chunk yearChunk = new Chunk("年", gzRedFort);
                    paragraphday.add(yearChunk);
                    Chunk month = new Chunk(String.valueOf(Integer.parseInt(dates[1])), gzFort);
                    paragraphday.add(month);
                    Chunk monthChunk = new Chunk("月", gzRedFort);
                    paragraphday.add(monthChunk);
                    Chunk day = new Chunk(String.valueOf(Integer.parseInt(dates[2])), gzFort);
                    paragraphday.add(day);
                    Chunk dayChunk = new Chunk("日", gzRedFort);
                    paragraphday.add(dayChunk);

                } else {
                    paragraphday = new Paragraph(date, gzFort);
                }
            }
            Chunk dayTitle = new Chunk(" 封发", gzRedFort);
            paragraphday.add(dayTitle);
            cellday.addElement(paragraphday);
                // 边框设置
            cellfw.setBorderColor(BaseColor.RED);
            cellfw.setBorder(Rectangle.ALIGN_JUSTIFIED);
            cellfw.setBorderWidth(2);
            cellfw.setFixedHeight(30);
            cellday.setBorderColor(BaseColor.RED);
            cellday.setBorder(Rectangle.ALIGN_JUSTIFIED);
            cellday.setBorderWidth(2);
            cellday.setFixedHeight(30);
            pdfPTablefw.addCell(cellfw);
            pdfPTablefw.addCell(cellday);
            doc.add(pdfPTablefw);
            doc.close();
            filePath = pdfPath;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            return filePath;
        }
    }

    private String ZX_SW_TITLE = "\n山东省调水工程运行维护中心文件处理笺\n";
    private String DW_SW_TITLE = "\n中国共产党山东省调水工程运行维护中心\n党委文件处理\n";

    /**
     * 党委收文稿纸转pdf
     * @return
     */
    public String gzDwSwToPdf(HzDwSwGz hzDwSwGz) {
        String filePath = null;
        checkPath();
        Document doc = new Document(PageSize.A4);
        try {
            String pdfPath = path + hzDwSwGz.getId() + ".pdf";
            deleteOldFile(pdfPath);
            PdfWriter.getInstance(doc, new FileOutputStream(pdfPath));
            doc.open();
            // 标题
            Paragraph paragraphTitle = new Paragraph();
            paragraphTitle = new Paragraph(DW_SW_TITLE, gzFontTitle);
            paragraphTitle.setAlignment(Element.ALIGN_CENTER);
            paragraphTitle.setIndentationLeft(5);
            paragraphTitle.setIndentationRight(5);
            paragraphTitle.setSpacingAfter(10);
            doc.add(paragraphTitle);
            // 收文号
            // 表格1
            PdfPTable table = new PdfPTable(2);
            table.setWidthPercentage(95);
            PdfPCell cell1 = new PdfPCell();
            Paragraph paragraph1 = new Paragraph();
            paragraph1.setLeading(20);
            Chunk swTitle = new Chunk(" 收文：", gzRedFort);
            paragraph1.add(swTitle);
            if (StringUtils.isNotEmpty(hzDwSwGz.getSw())) {
                String zi = hzDwSwGz.getSw().split("字")[0];
                String hao = hzDwSwGz.getSw().split("第")[1].split("号")[0];
                Chunk ziChunk = new Chunk(zi, gzFort);
                paragraph1.add(ziChunk);
                Chunk diChunk = new Chunk("字  第", gzRedFort);
                paragraph1.add(diChunk);
                Chunk haoChunk = new Chunk(hao, gzFort);
                paragraph1.add(haoChunk);
                Chunk chunk = new Chunk("号", gzRedFort);
                paragraph1.add(chunk);
            }
            cell1.addElement(paragraph1);
            // 边框颜色
            cell1.setBorderColor(BaseColor.RED);
            cell1.setBorder(7);
            cell1.setBorderWidth(2);
            cell1.setFixedHeight(35);
            table.addCell(cell1);

            PdfPCell cell3 = new PdfPCell();
            Paragraph paragraph5 = new Paragraph();
            paragraph5.setAlignment(Element.ALIGN_RIGHT);
            paragraph5.setIndentationRight(10);
            paragraph5.setLeading(20);
            String date = hzDwSwGz.getDay();
            if (StringUtils.isNotEmpty(date)) {
                String[] dates = date.split("-", -1);
                String buwei = "";
                if (Integer.parseInt(dates[1]) < 10) {
                    buwei+=" ";
                }
                if (Integer.parseInt(dates[2]) < 10) {
                    buwei+=" ";
                }
                if (dates.length == 3) {
                    Chunk year = new Chunk(buwei + dates[0], gzFort);
                    paragraph5.add(year);
                    Chunk yearChunk = new Chunk(" 年 ", gzRedFort);
                    paragraph5.add(yearChunk);
                    Chunk month = new Chunk(String.valueOf(Integer.parseInt(dates[1])), gzFort);
                    paragraph5.add(month);
                    Chunk monthChunk = new Chunk(" 月 ", gzRedFort);
                    paragraph5.add(monthChunk);
                    Chunk day = new Chunk(String.valueOf(Integer.parseInt(dates[2])), gzFort);
                    paragraph5.add(day);
                    Chunk dayChunk = new Chunk(" 日 ", gzRedFort);
                    paragraph5.add(dayChunk);

                }
            }
            cell3.addElement(paragraph5);
            cell3.setBorderColor(BaseColor.RED);
            cell3.setBorderWidth(2);
            cell3.setBorder(9);
            table.addCell(cell3);
            doc.add(table);

            PdfPTable pdfPTable = new PdfPTable(1);
            pdfPTable.setWidthPercentage(95);
            // 来文机关
            PdfPCell celllwjg = new PdfPCell();
            Paragraph paragraphlwjg = new Paragraph();
            paragraphlwjg.setLeading(20);
            Chunk lwjgTitle = new Chunk(" 来文机关：", gzRedFort);
            paragraphlwjg.add(lwjgTitle);
            if (hzDwSwGz.getLwjg() != null) {
                String[] lwjgs = rep(hzDwSwGz.getLwjg()).split("\n", -1);
                Chunk lwjgContent = new Chunk(lwjgs[0], gzFort);
                paragraphlwjg.add(lwjgContent);
                celllwjg.addElement(paragraphlwjg);
                if (lwjgs.length > 1) {
                    String content = "";
                    for (int i=1; i < lwjgs.length; i++) {
                        content+=lwjgs[i] + "\n";
                    }
                    if (lwjgs.length < 3) {
                        celllwjg.setFixedHeight(50);
                    }
                    Paragraph paragraphlwjg1 = new Paragraph(content + "\n", gzFort);
                    celllwjg.addElement(paragraphlwjg1);
                } else {
                    celllwjg.setFixedHeight(50);
                }
            } else {
                celllwjg.addElement(paragraphlwjg);
                celllwjg.setFixedHeight(35);
            }
                // 设置边框
            celllwjg.setBorderColor(BaseColor.RED);
            celllwjg.setBorderWidth(2);
            pdfPTable.addCell(celllwjg);
            // 文件标题
            PdfPCell cellwjbt = new PdfPCell();
            Paragraph paragraphwjbt = new Paragraph();
            paragraphwjbt.setLeading(20);
            Chunk wjbtTitle = new Chunk(" 文件标题：", gzRedFort);
            paragraphwjbt.add(wjbtTitle);
            if (hzDwSwGz.getLwjg() != null) {
                String[] wjbts = rep(hzDwSwGz.getWjbt()).split("\n", -1);
                Chunk wjbtContent = new Chunk(wjbts[0], gzFort);
                paragraphwjbt.add(wjbtContent);
                cellwjbt.addElement(paragraphwjbt);
                if (wjbts.length > 1) {
                    String content = "";
                    for (int i=1; i < wjbts.length; i++) {
                        content+=wjbts[i] + "\n";
                    }
                    if (wjbts.length < 3) {
                        cellwjbt.setFixedHeight(50);
                    }
                    Paragraph paragraphwjbt1 = new Paragraph(content + "\n", gzFort);
                    cellwjbt.addElement(paragraphwjbt1);
                } else {
                    cellwjbt.setFixedHeight(50);
                }
            } else {
                cellwjbt.addElement(paragraphwjbt);
                cellwjbt.setFixedHeight(50);
            }
                // 设置边框
            cellwjbt.setBorderColor(BaseColor.RED);
            cellwjbt.setBorderWidth(2);
            pdfPTable.addCell(cellwjbt);
            // 拟办意见
            PdfPCell cellnbyj = new PdfPCell();
            Paragraph paragraphnbyj = new Paragraph();
            paragraphnbyj.setLeading(20);
            Chunk nbyjTitle = new Chunk(" 拟办意见：", gzRedFort);
            paragraphnbyj.add(nbyjTitle);
            if (hzDwSwGz.getLwjg() != null) {
                String[] nbyjs = rep(hzDwSwGz.getNbyj()).split("\n", -1);
                Chunk nbyjContent = new Chunk(nbyjs[0], gzFort);
                paragraphnbyj.add(nbyjContent);
                cellnbyj.addElement(paragraphnbyj);
                if (nbyjs.length > 1) {
                    String content = "";
                    for (int i=1; i < nbyjs.length; i++) {
                        content+=nbyjs[i] + "\n";
                    }
                    if (nbyjs.length < 2) {
                        cellnbyj.setFixedHeight(50);
                    }
                    Paragraph paragraphnbyj1 = new Paragraph(content + "\n", gzFort);
                    cellnbyj.addElement(paragraphnbyj1);
                } else {
                    cellnbyj.setFixedHeight(50);
                }
            } else {
                cellnbyj.addElement(paragraphnbyj);
                cellnbyj.setFixedHeight(50);
            }
                // 设置边框
            cellnbyj.setBorderColor(BaseColor.RED);
            cellnbyj.setBorderWidth(2);
            pdfPTable.addCell(cellnbyj);
            // 领导批示
            PdfPCell cellldpshqz = new PdfPCell();
            Paragraph paragraphldpshqz = new Paragraph();
            paragraphldpshqz.setLeading(20);
            Chunk ldpshqzTitle = new Chunk(" 领导批示和签字：", gzRedFort);
            paragraphldpshqz.add(ldpshqzTitle);
            if (hzDwSwGz.getLwjg() != null) {
                String[] ldpshqzs = rep(hzDwSwGz.getLdps()).split("\n", -1);
                Chunk ldpshqzContent = new Chunk(ldpshqzs[0], gzFort);
                paragraphldpshqz.add(ldpshqzContent);
                cellldpshqz.addElement(paragraphldpshqz);
                if (ldpshqzs.length > 1) {
                    String content = "";
                    for (int i=1; i < ldpshqzs.length; i++) {
                        content+=ldpshqzs[i] + "\n";
                    }
                    if (ldpshqzs.length < 8) {
                        cellldpshqz.setFixedHeight(200);
                    }
                    Paragraph paragraphldpshqz1 = new Paragraph(content + "\n", gzFort);
                    cellldpshqz.addElement(paragraphldpshqz1);
                } else {
                    cellldpshqz.setFixedHeight(200);
                }
            } else {
                cellldpshqz.addElement(paragraphldpshqz);
                cellldpshqz.setFixedHeight(200);
            }
                // 设置边框
            cellldpshqz.setBorderColor(BaseColor.RED);
            cellldpshqz.setBorderWidth(2);
            pdfPTable.addCell(cellldpshqz);
            // 承办处室意见
            PdfPCell cellcbcsyj = new PdfPCell();
            Paragraph paragraphcbcsyj = new Paragraph();
            paragraphcbcsyj.setLeading(20);
            Chunk cbcsyjTitle = new Chunk(" 承办处室意见：", gzRedFort);
            paragraphcbcsyj.add(cbcsyjTitle);
            if (hzDwSwGz.getLwjg() != null) {
                String[] cbcsyjs = rep(hzDwSwGz.getCbcsyj()).split("\n", -1);
                Chunk cbcsyjContent = new Chunk(cbcsyjs[0], gzFort);
                paragraphcbcsyj.add(cbcsyjContent);
                cellcbcsyj.addElement(paragraphcbcsyj);
                if (cbcsyjs.length > 1) {
                    String content = "";
                    for (int i=1; i < cbcsyjs.length; i++) {
                        content+=cbcsyjs[i] + "\n";
                    }
                    if (cbcsyjs.length < 3) {
                        cellcbcsyj.setFixedHeight(70);
                    }
                    Paragraph paragraphcbcsyj1 = new Paragraph(content + "\n", gzFort);
                    cellcbcsyj.addElement(paragraphcbcsyj1);
                } else {
                    cellcbcsyj.setFixedHeight(70);
                }
            } else {
                cellcbcsyj.addElement(paragraphcbcsyj);
                cellcbcsyj.setFixedHeight(70);
            }
                // 设置边框
            cellcbcsyj.setBorderColor(BaseColor.RED);
            cellcbcsyj.setBorderWidth(2);
            pdfPTable.addCell(cellcbcsyj);
            // 协办处室意见
            PdfPCell cellxbcsyj = new PdfPCell();
            Paragraph paragraphxbcsyj = new Paragraph();
            paragraphxbcsyj.setLeading(20);
            Chunk xbcsyjTitle = new Chunk(" 协办处室意见：", gzRedFort);
            paragraphxbcsyj.add(xbcsyjTitle);
            if (hzDwSwGz.getLwjg() != null) {
                String[] xbcsyjs = rep(hzDwSwGz.getXbcsyj()).split("\n", -1);
                Chunk xbcsyjContent = new Chunk(xbcsyjs[0], gzFort);
                paragraphxbcsyj.add(xbcsyjContent);
                cellxbcsyj.addElement(paragraphxbcsyj);
                if (xbcsyjs.length > 1) {
                    String content = "";
                    for (int i=1; i < xbcsyjs.length; i++) {
                        content+=xbcsyjs[i] + "\n";
                    }
                    if (xbcsyjs.length < 2) {
                        cellxbcsyj.setFixedHeight(50);
                    }
                    Paragraph paragraphxbcsyj1 = new Paragraph(content + "\n", gzFort);
                    cellxbcsyj.addElement(paragraphxbcsyj1);
                } else {
                    cellxbcsyj.setFixedHeight(50);
                }
            } else {
                cellxbcsyj.addElement(paragraphxbcsyj);
                cellxbcsyj.setFixedHeight(50);
            }
                // 设置边框
            cellxbcsyj.setBorderColor(BaseColor.RED);
            cellxbcsyj.setBorderWidth(2);
            pdfPTable.addCell(cellxbcsyj);
            // 办结意见
            PdfPCell cellbjyj = new PdfPCell();
            Paragraph paragraphbjyj = new Paragraph();
            paragraphbjyj.setLeading(20);
            Chunk bjyjTitle = new Chunk(" 办结意见：", gzRedFort);
            paragraphbjyj.add(bjyjTitle);
            if (hzDwSwGz.getLwjg() != null) {
                String[] bjyjs = rep(hzDwSwGz.getBjyj()).split("\n", -1);
                Chunk bjyjContent = new Chunk(bjyjs[0], gzFort);
                paragraphbjyj.add(bjyjContent);
                cellbjyj.addElement(paragraphbjyj);
                if (bjyjs.length > 1) {
                    String content = "";
                    for (int i=1; i < bjyjs.length; i++) {
                        content+=bjyjs[i] + "\n";
                    }
                    if (bjyjs.length < 2) {
                        cellbjyj.setFixedHeight(50);
                    }
                    Paragraph paragraphbjyj1 = new Paragraph(content + "\n", gzFort);
                    cellbjyj.addElement(paragraphbjyj1);
                } else {
                    cellbjyj.setFixedHeight(50);
                }
            } else {
                cellbjyj.addElement(paragraphbjyj);
                cellbjyj.setFixedHeight(50);
            }
                // 设置边框
            cellbjyj.setBorderColor(BaseColor.RED);
            cellbjyj.setBorderWidth(2);
            pdfPTable.addCell(cellbjyj);
            // 备注
            PdfPCell cellbz = new PdfPCell();
            Paragraph paragraphbz = new Paragraph();
            paragraphbz.setLeading(20);
            Chunk bzTitle = new Chunk(" 备注：", gzRedFort);
            paragraphbz.add(bzTitle);
            if (hzDwSwGz.getLwjg() != null) {
                String[] bzs = rep(hzDwSwGz.getBz()).split("\n", -1);
                Chunk bzContent = new Chunk(bzs[0], gzFort);
                paragraphbz.add(bzContent);
                cellbz.addElement(paragraphbz);
                if (bzs.length > 1) {
                    String content = "";
                    for (int i=1; i < bzs.length; i++) {
                        content+=bzs[i] + "\n";
                    }
                    if (bzs.length < 2) {
                        cellbz.setFixedHeight(50);
                    }
                    Paragraph paragraphbz1 = new Paragraph(content + "\n", gzFort);
                    cellbz.addElement(paragraphbz1);
                } else {
                    cellbz.setFixedHeight(50);
                }
            } else {
                cellbz.addElement(paragraphbz);
                cellbz.setFixedHeight(50);
            }
                // 设置边框
            cellbz.setBorderColor(BaseColor.RED);
            cellbz.setBorderWidth(2);
            pdfPTable.addCell(cellbz);

            doc.add(pdfPTable);
            doc.close();
            filePath = pdfPath;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            return filePath;
        }
    }

    /**
     * 中心收文
     * @param hzZxSwGz
     * @return
     */
    public String gzZxswToPdf(HzZxSwGz hzZxSwGz) {
        String filePath = null;
        checkPath();
        Document doc = new Document(PageSize.A4);
        try {
            String pdfPath = path + hzZxSwGz.getId() + ".pdf";
            deleteOldFile(pdfPath);
            PdfWriter.getInstance(doc, new FileOutputStream(pdfPath));
            doc.open();
            Paragraph paragraphTitle = null;
            // 标题
            if (StringUtils.isNotEmpty(hzZxSwGz.getFzx())) {
                String bt = "\n山东省调水工程运行维护中心" + hzZxSwGz.getFzx() + "文件处理笺\n";
                paragraphTitle = new Paragraph(bt, fzxgzFontTitle);
            } else {
                paragraphTitle = new Paragraph(ZX_SW_TITLE, gzFontTitle);
            }
            paragraphTitle.setAlignment(Element.ALIGN_CENTER);
            paragraphTitle.setIndentationLeft(5);
            paragraphTitle.setIndentationRight(5);
            paragraphTitle.setSpacingAfter(10);
            doc.add(paragraphTitle);
            // 收文日期
            String swrq = "";
            String[] swrqs = hzZxSwGz.getSwrq().split("-", -1);
            String buwei = "";
            if (swrqs.length == 3) {
                if (Integer.parseInt(swrqs[1]) < 10) {
                    buwei+=" ";
                }
                if (Integer.parseInt(swrqs[2]) < 10) {
                    buwei+=" ";
                }
                swrq = swrqs[0] + "年" + Integer.parseInt(swrqs[1]) + "月" + Integer.parseInt(swrqs[2]) + "日";
            }
            Paragraph paragraphSwrq = new Paragraph();
            Chunk swrqTitle = new Chunk("\n" + buwei + "收文日期：", gzRedFort);
            paragraphSwrq.add(swrqTitle);
            Chunk swrqContent = new Chunk(swrq, gzFort);
            paragraphSwrq.add(swrqContent);
            paragraphSwrq.setAlignment(Element.ALIGN_RIGHT);
            paragraphSwrq.setIndentationLeft(5);
            paragraphSwrq.setIndentationRight(20);
            paragraphSwrq.setSpacingAfter(10);
            doc.add(paragraphSwrq);
            // 表单
            PdfPTable pdfPTable = new PdfPTable(6);
            pdfPTable.setWidthPercentage(95);
            // 来文单位
            PdfPCell lwdwTitle = new PdfPCell();
            Paragraph lwdwParagraph = new Paragraph(" 来文单位  ", gzRedFort);
            lwdwParagraph.setLeading(15);
            lwdwTitle.addElement(lwdwParagraph);
                // 设置边框
            lwdwTitle.setBorderColor(BaseColor.RED);
            lwdwTitle.setBorder(Rectangle.ALIGN_JUSTIFIED);
            lwdwTitle.setBorderWidth(2);
            lwdwTitle.setFixedHeight(25);
            pdfPTable.addCell(lwdwTitle);
            // 来文单位内容
            PdfPCell lwdwContent = new PdfPCell();
            Paragraph lwdwContentParagraph = new Paragraph(" " + hzZxSwGz.getLwdw() != null ? rep(hzZxSwGz.getLwdw()) : "", gzFort);
            lwdwContentParagraph.setLeading(15);
            lwdwContent.addElement(lwdwContentParagraph);
                // 设置边框
            lwdwContent.setBorderColor(BaseColor.RED);
            lwdwContent.setColspan(2);
            lwdwContent.setBorderWidth(2);
            pdfPTable.addCell(lwdwContent);
            // 来文号
            PdfPCell lwhTitle = new PdfPCell();
            Paragraph lwhParagraph = new Paragraph(" 来 文 号", gzRedFort);
            lwhParagraph.setLeading(15);
            lwhTitle.addElement(lwhParagraph);
                // 设置边框
            lwhTitle.setBorderColor(BaseColor.RED);
            lwhTitle.setBorderWidth(2);
            pdfPTable.addCell(lwhTitle);
            // 来文号内容
            PdfPCell lwhContent = new PdfPCell();
            Paragraph lwhContentParagraph = new Paragraph(" " + hzZxSwGz.getLwh() != null ? rep(hzZxSwGz.getLwh()) : "", gzFort);
            lwhContentParagraph.setLeading(15);
            lwhContent.addElement(lwhContentParagraph);
                // 设置边框
            lwhContent.setBorderColor(BaseColor.RED);
            lwhContent.setBorder(Rectangle.ALIGN_JUSTIFIED);
            lwhContent.setColspan(2);
            lwhContent.setBorderWidth(2);
            pdfPTable.addCell(lwhContent);
            // 收文类别
            PdfPCell swlbTitle = new PdfPCell();
            Paragraph swlbParagraph = new Paragraph(" 收文类别", gzRedFort);
            swlbParagraph.setLeading(15);
            swlbTitle.addElement(swlbParagraph);
                // 设置边框
            swlbTitle.setBorderColor(BaseColor.RED);
            swlbTitle.setBorderWidth(2);
            swlbTitle.setBorder(Rectangle.ALIGN_JUSTIFIED);
            swlbTitle.setFixedHeight(25);
            pdfPTable.addCell(swlbTitle);
            // 收文类别内容
            PdfPCell swlbContent = new PdfPCell();
            Paragraph swlbContentParagraph = new Paragraph(" " + hzZxSwGz.getSwlb() != null ? rep(hzZxSwGz.getSwlb()) : "", gzFort);
            swlbContentParagraph.setLeading(15);
            swlbContent.addElement(swlbContentParagraph);
                // 设置边框
            swlbContent.setBorderColor(BaseColor.RED);
            swlbContent.setColspan(2);
            swlbContent.setBorderWidth(2);
            pdfPTable.addCell(swlbContent);
            // 收文号
            PdfPCell swhTitle = new PdfPCell();
            Paragraph swhParagraph = new Paragraph(" 收 文 号", gzRedFort);
            swhParagraph.setLeading(15);
            swhTitle.addElement(swhParagraph);
                // 设置边框
            swhTitle.setBorderColor(BaseColor.RED);
            swhTitle.setBorderWidth(2);
            pdfPTable.addCell(swhTitle);
            // 收文号内容
            PdfPCell swhContent = new PdfPCell();
            Paragraph swhContentParagraph = new Paragraph(" " + hzZxSwGz.getSwh() != null ? rep(hzZxSwGz.getSwh()) : "", gzFort);
            swhContentParagraph.setLeading(15);
            swhContent.addElement(swhContentParagraph);
                // 设置边框
            swhContent.setBorderColor(BaseColor.RED);
            swhContent.setBorder(Rectangle.ALIGN_JUSTIFIED);
            swhContent.setColspan(2);
            swhContent.setBorderWidth(2);
            pdfPTable.addCell(swhContent);
            // 办理期限
            PdfPCell blqxTitle = new PdfPCell();
            Paragraph blqxParagraph = new Paragraph(" 办理期限", gzRedFort);
            blqxParagraph.setLeading(15);
            blqxTitle.addElement(blqxParagraph);
                // 设置边框
            blqxTitle.setBorderColor(BaseColor.RED);
            blqxTitle.setBorderWidth(2);
            blqxTitle.setBorder(Rectangle.ALIGN_JUSTIFIED);
            blqxTitle.setFixedHeight(25);
            pdfPTable.addCell(blqxTitle);
            // 办理期限内容
            PdfPCell blqxContent = new PdfPCell();
            Paragraph blqxContentParagraph = new Paragraph(" " + hzZxSwGz.getBlqx() != null ? rep(hzZxSwGz.getBlqx()) : "", gzFort);
            blqxContentParagraph.setLeading(15);
            blqxContent.addElement(blqxContentParagraph);
                // 设置边框
            blqxContent.setBorderColor(BaseColor.RED);
            blqxContent.setColspan(2);
            blqxContent.setBorderWidth(2);
            pdfPTable.addCell(blqxContent);
            // 生效日期
            PdfPCell sxrqTitle = new PdfPCell();
            Paragraph sxrqParagraph = new Paragraph(" 生效日期", gzRedFort);
            sxrqParagraph.setLeading(15);
            sxrqTitle.addElement(sxrqParagraph);
                // 设置边框
            sxrqTitle.setBorderColor(BaseColor.RED);
            sxrqTitle.setBorderWidth(2);
            pdfPTable.addCell(sxrqTitle);
            // 生效日期内容
            PdfPCell sxrqContent = new PdfPCell();
            Paragraph sxrqContentParagraph = new Paragraph(" " + hzZxSwGz.getSxrq() != null ? rep(hzZxSwGz.getSxrq()) : "", gzFort);
            sxrqContentParagraph.setLeading(15);
            sxrqContent.addElement(sxrqContentParagraph);
                // 设置边框
            sxrqContent.setBorderColor(BaseColor.RED);
            sxrqContent.setBorder(Rectangle.ALIGN_JUSTIFIED);
            sxrqContent.setColspan(2);
            sxrqContent.setBorderWidth(2);
            pdfPTable.addCell(sxrqContent);
            // 密级
            PdfPCell mijiTitle = new PdfPCell();
            Paragraph mijiParagraph = new Paragraph(" 密    级", gzRedFort);
            mijiParagraph.setLeading(13);
            mijiTitle.addElement(mijiParagraph);
                // 设置边框
            mijiTitle.setBorderColor(BaseColor.RED);
            mijiTitle.setBorderWidth(2);
            mijiTitle.setBorder(Rectangle.ALIGN_JUSTIFIED);
            mijiTitle.setFixedHeight(25);
            pdfPTable.addCell(mijiTitle);
            // 密级内容
            PdfPCell mijiContent = new PdfPCell();
            Paragraph mijiContentParagraph = new Paragraph(" " + hzZxSwGz.getMj() != null ? rep(hzZxSwGz.getMj()) : "", gzFort);
            mijiContentParagraph.setLeading(15);
            mijiContent.addElement(mijiContentParagraph);
                // 设置边框
            mijiContent.setBorderColor(BaseColor.RED);
            mijiContent.setColspan(2);
            mijiContent.setBorderWidth(2);
            pdfPTable.addCell(mijiContent);
            // 缓急
            PdfPCell huanjiTitle = new PdfPCell();
            Paragraph huanjiParagraph = new Paragraph(" 缓    急", gzRedFort);
            huanjiParagraph.setLeading(15);
            huanjiTitle.addElement(huanjiParagraph);
                // 设置边框
            huanjiTitle.setBorderColor(BaseColor.RED);
            huanjiTitle.setBorderWidth(2);
            pdfPTable.addCell(huanjiTitle);
            // 缓急内容
            PdfPCell huanjiContent = new PdfPCell();
            Paragraph huanjiContentParagraph = new Paragraph(" " + hzZxSwGz.getHj() != null ? rep(hzZxSwGz.getHj()) : "", gzFort);
            huanjiContentParagraph.setLeading(15);
            huanjiContent.addElement(huanjiContentParagraph);
                // 设置边框
            huanjiContent.setBorderColor(BaseColor.RED);
            huanjiContent.setBorder(Rectangle.ALIGN_JUSTIFIED);
            huanjiContent.setColspan(2);
            huanjiContent.setBorderWidth(2);
            pdfPTable.addCell(huanjiContent);
            // 标题
            PdfPCell biaotiTitle = new PdfPCell();
            Paragraph biaotiParagraph = new Paragraph(" 标    题", gzRedFort);
            biaotiParagraph.setLeading(30);
            biaotiTitle.addElement(biaotiParagraph);
                // 设置边框
            biaotiTitle.setBorderColor(BaseColor.RED);
            biaotiTitle.setBorderWidth(2);
            biaotiTitle.setBorder(Rectangle.ALIGN_JUSTIFIED);
            biaotiTitle.setFixedHeight(60);
            pdfPTable.addCell(biaotiTitle);
            // 标题内容
            PdfPCell biaotiContent = new PdfPCell();
            Paragraph biaotiContentParagraph = new Paragraph(" " + hzZxSwGz.getBt() != null ? rep(hzZxSwGz.getBt()) : "", gzFort);
            biaotiContentParagraph.setLeading(15);
            biaotiContent.addElement(biaotiContentParagraph);
                // 设置边框
            biaotiContent.setBorderColor(BaseColor.RED);
            biaotiContent.setBorder(Rectangle.ALIGN_BASELINE);
            biaotiContent.setColspan(5);
            biaotiContent.setBorderWidth(2);
            pdfPTable.addCell(biaotiContent);
            // 拟办意见
            PdfPCell nbyjTitle = new PdfPCell();
            Paragraph nbyjParagraph = new Paragraph(" 拟办意见", gzRedFort);
            nbyjParagraph.setLeading(45);
            nbyjTitle.addElement(nbyjParagraph);
                // 设置边框
            nbyjTitle.setBorderColor(BaseColor.RED);
            nbyjTitle.setBorderWidth(2);
            nbyjTitle.setBorder(Rectangle.ALIGN_JUSTIFIED);
            nbyjTitle.setFixedHeight(100);
            pdfPTable.addCell(nbyjTitle);
            // 拟办意见内容
            PdfPCell nbyjContent = new PdfPCell();
            Paragraph nbyjContentParagraph = new Paragraph(" " + hzZxSwGz.getNbyj() != null ? rep(hzZxSwGz.getNbyj()) : "", gzFort);
            nbyjContentParagraph.setLeading(20);
            nbyjContent.addElement(nbyjContentParagraph);
                // 设置边框
            nbyjContent.setBorderColor(BaseColor.RED);
            nbyjContent.setBorder(Rectangle.ALIGN_BASELINE);
            nbyjContent.setColspan(5);
            nbyjContent.setBorderWidth(2);
            pdfPTable.addCell(nbyjContent);
            // 领导批示
            PdfPCell ldpsTitle = new PdfPCell();
            Paragraph ldpsParagraph = new Paragraph(" 领导批示", gzRedFort);
            ldpsParagraph.setLeading(30);
            ldpsTitle.addElement(ldpsParagraph);
                // 设置边框
            ldpsTitle.setBorderColor(BaseColor.RED);
            ldpsTitle.setBorderWidth(2);
            ldpsTitle.setBorder(Rectangle.ALIGN_JUSTIFIED);
            ldpsTitle.setFixedHeight(70);
            pdfPTable.addCell(ldpsTitle);
            // 领导批示内容
            PdfPCell ldpsContent = new PdfPCell();
            Paragraph ldpsContentParagraph = new Paragraph(" " + hzZxSwGz.getLdps() != null ? rep(hzZxSwGz.getLdps()) : "", gzFort);
            ldpsContentParagraph.setLeading(20);
            ldpsContent.addElement(ldpsContentParagraph);
                // 设置边框
            ldpsContent.setBorderColor(BaseColor.RED);
            ldpsContent.setBorder(Rectangle.ALIGN_BASELINE);
            ldpsContent.setColspan(5);
            ldpsContent.setBorderWidth(2);
            pdfPTable.addCell(ldpsContent);
            // 主办处室办理意见
            PdfPCell zbcsblyjTitle = new PdfPCell();
            String zbcsyj = " 主办处室\n 办理意见";
            if (StringUtils.isNotEmpty(hzZxSwGz.getFzx()))
                zbcsyj = " 主办科室\n 办理意见";
            Paragraph zbcsblyjParagraph = new Paragraph(zbcsyj, gzRedFort);
            zbcsblyjParagraph.setLeading(20);
            zbcsblyjTitle.addElement(zbcsblyjParagraph);
                // 设置边框
            zbcsblyjTitle.setBorderColor(BaseColor.RED);
            zbcsblyjTitle.setBorderWidth(2);
            zbcsblyjTitle.setBorder(Rectangle.ALIGN_JUSTIFIED);
            zbcsblyjTitle.setFixedHeight(60);
            pdfPTable.addCell(zbcsblyjTitle);
            // 主办处室办理意见内容
            PdfPCell zbcsblyjContent = new PdfPCell();
            Paragraph zbcsblyjContentParagraph = new Paragraph(" " + hzZxSwGz.getZbcsblyj() != null ? rep(hzZxSwGz.getZbcsblyj()) : "", gzFort);
            zbcsblyjContentParagraph.setLeading(20);
            zbcsblyjContent.addElement(zbcsblyjContentParagraph);
                // 设置边框
            zbcsblyjContent.setBorderColor(BaseColor.RED);
            zbcsblyjContent.setBorder(Rectangle.ALIGN_BASELINE);
            zbcsblyjContent.setColspan(5);
            zbcsblyjContent.setBorderWidth(2);
            pdfPTable.addCell(zbcsblyjContent);
            // 主办处室办理结果
            PdfPCell zbcsbljgTitle = new PdfPCell();
            String zbcsbljg = " 主办处室\n 办理结果";
            if (StringUtils.isNotEmpty(hzZxSwGz.getFzx()))
                zbcsbljg = " 主办科室\n 办理结果";
            Paragraph zbcsbljgParagraph = new Paragraph(zbcsbljg, gzRedFort);
            zbcsbljgParagraph.setLeading(20);
            zbcsbljgTitle.addElement(zbcsbljgParagraph);
                // 设置边框
            zbcsbljgTitle.setBorderColor(BaseColor.RED);
            zbcsbljgTitle.setBorderWidth(2);
            zbcsbljgTitle.setBorder(Rectangle.ALIGN_JUSTIFIED);
            zbcsbljgTitle.setFixedHeight(60);
            pdfPTable.addCell(zbcsbljgTitle);
            // 主办处室办理结果内容
            PdfPCell zbcsbljgContent = new PdfPCell();
            Paragraph zbcsbljgContentParagraph = new Paragraph(" " + hzZxSwGz.getZbcsbljg() != null ? rep(hzZxSwGz.getZbcsbljg()) : "", gzFort);
            zbcsbljgContentParagraph.setLeading(20);
            zbcsbljgContent.addElement(zbcsbljgContentParagraph);
                // 设置边框
            zbcsbljgContent.setBorderColor(BaseColor.RED);
            zbcsbljgContent.setBorder(Rectangle.ALIGN_BASELINE);
            zbcsbljgContent.setColspan(5);
            zbcsbljgContent.setBorderWidth(2);
            pdfPTable.addCell(zbcsbljgContent);
            // 协办处室办理意见
            PdfPCell xbcsblyjTitle = new PdfPCell();
            String xbcsblyj = " 协办处室\n 办理意见";
            if (StringUtils.isNotEmpty(hzZxSwGz.getFzx()))
                xbcsblyj = " 协办科室\n 办理意见";
            Paragraph xbcsblyjParagraph = new Paragraph(xbcsblyj, gzRedFort);
            xbcsblyjParagraph.setLeading(20);
            xbcsblyjTitle.addElement(xbcsblyjParagraph);
                // 设置边框
            xbcsblyjTitle.setBorderColor(BaseColor.RED);
            xbcsblyjTitle.setBorderWidth(2);
            xbcsblyjTitle.setBorder(Rectangle.ALIGN_JUSTIFIED);
            xbcsblyjTitle.setFixedHeight(60);
            pdfPTable.addCell(xbcsblyjTitle);
            // 协办处室办理意见内容
            PdfPCell xbcsblyjContent = new PdfPCell();
            Paragraph xbcsblyjContentParagraph = new Paragraph(" " + hzZxSwGz.getXbcsblyj() != null ? rep(hzZxSwGz.getXbcsblyj()) : "", gzFort);
            xbcsblyjContentParagraph.setLeading(20);
            xbcsblyjContent.addElement(xbcsblyjContentParagraph);
                // 设置边框
            xbcsblyjContent.setBorderColor(BaseColor.RED);
            xbcsblyjContent.setBorder(Rectangle.ALIGN_BASELINE);
            xbcsblyjContent.setColspan(5);
            xbcsblyjContent.setBorderWidth(2);
            pdfPTable.addCell(xbcsblyjContent);
            // 协办处室办理结果
            PdfPCell xbcsbljgTitle = new PdfPCell();
            String xbcsbljg = " 协办处室\n 办理结果";
            if (StringUtils.isNotEmpty(hzZxSwGz.getFzx()))
                xbcsbljg = " 协办科室\n 办理结果";
            Paragraph xbcsbljgParagraph = new Paragraph(xbcsbljg, gzRedFort);
            xbcsbljgParagraph.setLeading(20);
            xbcsbljgTitle.addElement(xbcsbljgParagraph);
                // 设置边框
            xbcsbljgTitle.setBorderColor(BaseColor.RED);
            xbcsbljgTitle.setBorderWidth(2);
            xbcsbljgTitle.setBorder(Rectangle.ALIGN_JUSTIFIED);
            xbcsbljgTitle.setFixedHeight(60);
            pdfPTable.addCell(xbcsbljgTitle);
            // 协办处室办理结果内容
            PdfPCell xbcsbljgContent = new PdfPCell();
            Paragraph xbcsbljgContentParagraph = new Paragraph(" " + hzZxSwGz.getXbcsbljg() != null ? rep(hzZxSwGz.getXbcsbljg()) : "", gzFort);
            xbcsbljgContentParagraph.setLeading(20);
            xbcsbljgContent.addElement(xbcsbljgContentParagraph);
                // 设置边框
            xbcsbljgContent.setBorderColor(BaseColor.RED);
            xbcsbljgContent.setBorder(Rectangle.ALIGN_BASELINE);
            xbcsbljgContent.setColspan(5);
            xbcsbljgContent.setBorderWidth(2);
            pdfPTable.addCell(xbcsbljgContent);
            // 备注
            PdfPCell beizhuTitle = new PdfPCell();
            Paragraph beizhuParagraph = new Paragraph(" 备    注", gzRedFort);
            beizhuParagraph.setLeading(15);
            beizhuTitle.addElement(beizhuParagraph);
            // 设置边框
            beizhuTitle.setBorderColor(BaseColor.RED);
            beizhuTitle.setBorderWidth(2);
            beizhuTitle.setBorder(Rectangle.ALIGN_JUSTIFIED);
            beizhuTitle.setFixedHeight(30);
            pdfPTable.addCell(beizhuTitle);
                // 备注内容
            PdfPCell beizhuContent = new PdfPCell();
            Paragraph beizhuContentParagraph = new Paragraph(" " + hzZxSwGz.getBz() != null ? rep(hzZxSwGz.getBz()) : "", gzFort);
            beizhuContentParagraph.setLeading(20);
            beizhuContent.addElement(beizhuContentParagraph);
                // 设置边框
            beizhuContent.setBorderColor(BaseColor.RED);
            beizhuContent.setBorder(Rectangle.ALIGN_BASELINE);
            beizhuContent.setColspan(5);
            beizhuContent.setBorderWidth(2);
            pdfPTable.addCell(beizhuContent);

            doc.add(pdfPTable);
            doc.close();
            filePath = pdfPath;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            return filePath;
        }
    }

    /**
     * 删除之前生成的pdf文件
     * @param path
     */
    public void deleteOldFile(String path) {
        File file = new File(path);
        if (file.exists()) {
            file.delete();
        }
    }

    public String rep(String val) {
        if (StringUtils.isEmpty(val)) {
            return "";
        }
        return val.replaceAll("<br>", "\n").replaceAll("<br/>", "\n").replaceAll("<br />", "\n");
    }
    public static void main(String[] args) throws IOException, DocumentException {
        HzToPdf hzToPdf = new HzToPdf();
        // 公文
        /*HzTopDown hzTopDown = new HzTopDown();
        hzTopDown.setId("12345672");
        hzTopDown.setType(1);
        hzTopDown.setTitle("山东省胶东调水局关于转发《关于做好 2016年度全省水利档案统计工作的通知》的通知");
        hzTopDown.setContent("&nbsp;&nbsp;&nbsp;&nbsp;各分中心： 近期自动化调度系统干线光缆多次损坏造成系统断网2020年9月16日——2014年11月31日");
        hzTopDown.setDay("2019-11-13");
        hzTopDown.setFileNumber("鲁胶调水质监字〔2013〕26号");
        hzTopDown.setUserName("刘长军");
        hzTopDown.setFsdw("省中心机关各出（室），各分中心");
        hzTopDown.setCsdw("省水利厅人事处");
        hzTopDown.setLxr("张斌");
        hzTopDown.setLxdh("89028401");
        hzToPdf.topToPdf(hzTopDown);*/
        // 党委公文
        /*HzTopDown hzTopDowndw = new HzTopDown();
        hzTopDowndw.setId("1234567");
        hzTopDowndw.setType(1);
        hzTopDowndw.setTitle("山东省胶东调水局关于转发《关于做好 2016年度全省水利档案统计工作的通知》的通知");
        hzTopDowndw.setContent("&nbsp;&nbsp;&nbsp;&nbsp;各分中心： 近期自动化调度系统干线光缆多次损坏造成系统断网，严 重影响自动化调度系统正常运行和烟台、威海应急调水数据传 输。因管道段运行对运行数据实时性要求高，信号传输中断存 在巨大的安全隐患，现将有关情况通报并要求如下：<br><h1>一、光缆近期损坏情况</h1><br>&nbsp;&nbsp;&nbsp;&nbsp;9 月 17 日，王耨泵站下游生产桥西南侧管道主干光缆因改 扩建施工单位非法取土挖断；9 月 28 日，王耨泵站下游南侧管道主干光缆改扩建施工单位非法取土挖断；10 月 7 日，王耨泵 站上游架空侧主干光缆、光电复合缆改扩建施工单位剐断；10 月 12 日，徐林庄上游 2km 出管道主干光缆改扩建施工单位非法 取土挖断。<br><h1>二、光缆抢修及恢复</h1><br><h2>（1）按照省中心文件及会议要求</h2><br>&nbsp;&nbsp;&nbsp;&nbsp;按照省中心文件及会议要求，由各分中心查明责任单位、 责任人，并对造成光缆损坏的责任单位进行追责。严格遵照“先 抢修，再整理，谁损坏，损失由谁承担”的原则，追究损坏方 责任。一旦发生光缆损坏，抢修恢复由自动化调度系统光缆施 工标段负责，相关费用由损坏方支付给光缆施工标段，如不支 付，从引黄济青改扩建工程计量款中扣除；如因损坏影响光缆 性能，造成光缆更换的，光缆更换费用由损坏方承担；如因光 缆损坏影响到工程输水安全，造成工程损失、周围设施损坏、 人员伤亡的，一切费用由损坏方承担。<br><h1>三、加强光缆保护</h1><br>&nbsp;&nbsp;&nbsp;&nbsp;各分中心要加强对施工单位的管理，对堤顶取土等野蛮施 工行为要采取措施、坚决制止，确保渠堤安全。 责成各施工单位要吸取教训，制定详细的光缆保护措施方 案，并明确责任人。责任人要做好技术交底并负责现场施工管 理，确保架空及管道光缆安全。监理单位加强现场施工管理，严格监督执行施工方案和各 项操作规程，坚决杜绝再次出现光缆损坏现象。<br>&nbsp;<br>");
        hzTopDowndw.setDay("2019-11-13");
        hzTopDowndw.setFileNumber("鲁胶调水质监字〔2013〕26号");
        hzTopDowndw.setUserName("刘长军");
        hzTopDowndw.setFsdw("省中心机关各出（室），各分中心");
        hzTopDowndw.setCsdw("省水利厅人事处");
        hzTopDowndw.setLxr("张斌");
        hzTopDowndw.setLxdh("89028401");
        hzToPdf.topToPdfDw(hzTopDowndw);*/
        // 公文
        /*HzTopDown hzTopDownCS = new HzTopDown();
        hzTopDownCS.setId("111111");
        hzTopDownCS.setType(1);
        hzTopDownCS.setTitle("24242424");
        hzTopDownCS.setFileNumber("〔〕号");
        hzTopDownCS.setUserName("刘长军");
        hzTopDownCS.setFsdw("24224");
        hzTopDownCS.setDay("2020-04-15");
        hzTopDownCS.setCsdw("242424");
        hzTopDownCS.setLxr("秘书科公共岗");
        hzTopDownCS.setLxdh("2444");
        hzTopDownCS.setFlowtype("chushifahan");
        hzToPdf.topToPdfZXCSFH(hzTopDownCS);*/
        // 中心发文
        HzZxfwGz hzZxfwGz = new HzZxfwGz();
        hzZxfwGz.setId("8888888");
        hzZxfwGz.setBgsyj("");
        hzZxfwGz.setCsCompany("斤斤计较军军军军军\n军军军军军军军军军军\n军军军军军军军军军军军军军军\n军军军军军军军军军军\n军军");
        hzZxfwGz.setFileNumber("333333");
        hzZxfwGz.setNgr("王蕾");
        hzZxfwGz.setNgrPhone("89028201");
        //hzZxfwGz.setHgr("李念平");
        //hzZxfwGz.setHgrPhone("89028026");
        //hzZxfwGz.setQf("刘长军  2019-04-01");
        //hzZxfwGz.setSy("李风强 2019-04-02");
        hzZxfwGz.setYfjg("山东省调水工程运行维护中心青岛分中心");
        //hzZxfwGz.setZsCompany("省中心机关各处室\n各个中心省中心机关各处室\n各个中心省中心机关各处室\n各个\n中心");
        //hzZxfwGz.setTitle("省中心机关各处室\n各个中心省中\n心机关各处室各个\n中心省中心机关\n各处室各个中心");
        //hzZxfwGz.setBgsyj("解耦螯合钙我哦哈狗屁哈光谱哦哈狗IP发的哈手工铺我横扫共我会飞的搜IG啊哈搜狗一\n安徽省偏高");
        hzZxfwGz.setDay("2019-13-22");
        hzZxfwGz.setNumber("1");
        //hzZxfwGz.setHq("刘长军\n王蕾");
        //hzZxfwGz.setNgrcs("省中心机关各处");
        //hzZxfwGz.setUserName("王蕾");
        hzZxfwGz.setFzx("青岛分中心");
        hzToPdf.gzzxfwToPdf(hzZxfwGz);

        //pdf转word和html
        /*PdfDocument pdf = new PdfDocument(hzToPdf.gzzxfwToPdf(hzZxfwGz));
        //pdf.saveToFile("D:\\ToWord.docx", FileFormat.DOCX);
        pdf.saveToFile("D:\\ToHTML.html", FileFormat.HTML);*/
        // 党委发文
        /*HzDwfwGz hzDwfwGz = new HzDwfwGz();
        hzDwfwGz.setId("122222222222");
        hzDwfwGz.setBwbt("省中心机关各处室\n各个中心省中\n心机关各处室\n各个中心省中心机关各处室\n各个中心");
        hzDwfwGz.setMj("机密");
        hzDwfwGz.setQf("省中心机关各处室\n各个中心省中\n心机关各处室\n各个中心省中心机关各处室\n各个中心");
        hzDwfwGz.setHq("省中心机关各处室\n各个中心省中\n心机关各处室\n各个中心省中心机关各处室\n各个中心");
        hzDwfwGz.setClyj("解耦螯合钙我哦哈狗屁哈光谱哦哈狗IP发的哈手工铺我横扫共我会飞的搜IG啊哈搜狗一安徽省偏高解耦螯合钙我哦哈狗屁哈光谱哦哈狗IP发的哈手工铺我横扫共我会飞的搜IG啊哈搜狗一安徽省偏高");
        hzDwfwGz.setNgr("拟稿人");
        hzDwfwGz.setNgrcs("党委发文核稿人");
        hzDwfwGz.setNgrPhone("12345678911");
        hzDwfwGz.setHgr("党委发文核稿人");
        hzDwfwGz.setCsdw("省中心机关各处室\n各个中心省中\n心机关各处室\n各个中心省中心机关各处室\n各个中心");
        hzDwfwGz.setCbdw("省中心机关各处室\n各个中心省中\n心机关各处室\n各个中心省中心机关各处室\n各个中心");
        hzDwfwGz.setFsdw("省中心机关各处室\n各个中心省中\n心机关各处室\n各个中心省中心机关各处室\n各个中心");
        hzDwfwGz.setNumber("20");
        hzDwfwGz.setDz("省中心机关各处室");
        hzDwfwGz.setUserName("中心机关各处室");
        hzDwfwGz.setFw("");
        hzDwfwGz.setDay("2020-01-07");
        hzToPdf.gzdwfwToPdf(hzDwfwGz);*/
        // 收文
        /*HzDwSwGz hzDwSwGz = new HzDwSwGz();
        hzDwSwGz.setId("456789");
        hzDwSwGz.setBjyj("省中心机关各处室各个中心省中心机关各处室");
        hzDwSwGz.setBz("解耦螯合钙我哦哈狗屁哈光谱哦哈狗IP发的哈工");
        hzDwSwGz.setCbcsyj("解耦螯合钙我哦哈狗屁哈光谱哦哈狗IP发的");
        hzDwSwGz.setDay("2019-03-02");
        hzDwSwGz.setLdps("解耦螯合钙我哦哈狗屁哈光谱哦哈狗IP发的哈");
        hzDwSwGz.setLwjg("山东省党委");
        //hzSwGz.setMj("绝密");
        hzDwSwGz.setNbyj("解耦螯合钙我哦哈狗屁哈光谱哦哈狗IP发的");
        hzDwSwGz.setXbcsyj("解耦螯合钙我哦哈狗屁哈光谱哦哈狗IP发");
        hzDwSwGz.setSw("鲁调水调字 第1号");
        hzDwSwGz.setSx("永久有效");
        hzDwSwGz.setWjbt("解耦螯合钙我哦哈狗屁哈光谱哦哈狗IP发的哈手工铺我横扫共我会飞的搜IG啊哈搜狗一\n王11111111111111111111111111111111111111111111111111111111111111111111");
        hzToPdf.gzDwSwToPdf(hzDwSwGz);*/

        // 中心收文
        /*HzZxSwGz hzZxSwGz = new HzZxSwGz();
        hzZxSwGz.setId("12345677");
        hzZxSwGz.setSwrq("2020-01-06");
        hzZxSwGz.setSxrq("2020-01-06");
        hzZxSwGz.setLwdw("平度管理站");
        hzZxSwGz.setLwh("甲字2222号");
        hzZxSwGz.setFzx("青岛分中心");
        hzZxSwGz.setSwlb("aaaaaaaa");
        hzZxSwGz.setSwh("aaaaaaaa");
        hzZxSwGz.setBlqx("aaaaaaaa");
        hzZxSwGz.setMj("aaaaaaaa");
        hzZxSwGz.setHj("aaaaaaaa");
        hzZxSwGz.setBt("aaaaaaaa");
        hzZxSwGz.setNbyj("aaaaaaaa");
        hzZxSwGz.setLdps("aaaaaaaa");
        hzZxSwGz.setZbcsblyj("aaaaaaaa");
        hzZxSwGz.setZbcsbljg("aaaaaaaa");
        hzZxSwGz.setXbcsblyj("aaaaaaaa");
        hzZxSwGz.setXbcsbljg("aaaaaaaa");
        hzZxSwGz.setBz("aaaaaaaa");
        hzToPdf.gzZxswToPdf(hzZxSwGz);*/
        //System.out.println("王11111111111111111111111111111111111111111111111111111111111111111111".getBytes().length);
        //String word = hzToPdf.readWord("F:\\领导待确认问题1.doc");
        //System.out.println(word);
        //hzToPdf.readPdf();
    }

}
