package com.garm.common.utils;

import org.apache.poi.xwpf.usermodel.*;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.*;

import java.io.FileOutputStream;
import java.math.BigInteger;

public class TestPaperExportUtil {

    /**
     * 设置title
     * @param document
     * @param title
     */
    public static void setTilte(CustomXWPFDocument document, String title) {
        XWPFParagraph p = document.createParagraph();
        setTextFontInfo(p, false, false, title, "宋体", "000000",
                "40", true, null, false, false, null, 0, null);
        setParagraphSpacingInfo(p, true, "0", "0", "0", "0", true, "240",
                STLineSpacingRule.Enum.forString("auto"));
        setParagraphAlignInfo(p, ParagraphAlignment.CENTER,
                TextAlignment.CENTER);

    }

    /**
     * 设置题组名称
     * @param document
     * @param qGName
     */
    public static void setQuestionGroup(CustomXWPFDocument document, String qGName){
        XWPFParagraph p = document.createParagraph();
        setTextFontInfo(p, false, false, qGName, "宋体", "000000",
                "30", true, null, false, false, null, 0,null);
        setParagraphSpacingInfo(p, true, "0", "0", "100", "0", true, "240",
                STLineSpacingRule.Enum.forString("auto"));
        setParagraphAlignInfo(p, ParagraphAlignment.LEFT, TextAlignment.CENTER);

    }

    /**
     * 设置班级，姓名
     * @param document
     * @param Bj
     */
    public static void setBJ(CustomXWPFDocument document, String Bj){
        Bj = "班级：________    姓名：________";
        XWPFParagraph p = document.createParagraph();
        setTextFontInfo(p, false, false, Bj, "宋体",
                "000000", "30", false, null, false, false, null, 0,null);
        setParagraphSpacingInfo(p, true, "0", "0", "0", "0", true, "240",
                STLineSpacingRule.Enum.forString("auto"));
        setParagraphAlignInfo(p, ParagraphAlignment.CENTER,
                TextAlignment.CENTER);
    }

    /**
     * 设置题名称
     * @param document
     * @param qName
     */
    public static void setQuestion(CustomXWPFDocument document, String qName){
        XWPFParagraph  p = document.createParagraph();
        setTextFontInfo(p, false, false, qName, "宋体",
                "000000", "25", false, null, false, false, null, 0,null);
        setParagraphSpacingInfo(p, true, "0", "0", "0", "0", true, "240",
                STLineSpacingRule.Enum.forString("auto"));
        setParagraphAlignInfo(p, ParagraphAlignment.LEFT, TextAlignment.CENTER);
    }



    /**
     * 设置子题名称
     * @param document
     * @param qName
     */
    public static void setChildQuestion(CustomXWPFDocument document, String qName){
        XWPFParagraph  p = document.createParagraph();
        setTextFontInfo(p, false, false, qName, "宋体",
                "000000", "23", false, null, false, false, null, 0,null);
        setParagraphSpacingInfo(p, true, "0", "0", "0", "0", true, "240",
                STLineSpacingRule.Enum.forString("auto"));
        setParagraphAlignInfo(p, ParagraphAlignment.LEFT, TextAlignment.CENTER);
    }

    /**
     * 设置问题选项
     * @param document
     * @param qName
     */
    public static void setItem(CustomXWPFDocument document, String qName){
        XWPFParagraph  p = document.createParagraph();
        setTextFontInfo(p, false, false, qName, "宋体",
                "000000", "21", false, null, false, false, null, 0,null);
        setParagraphSpacingInfo(p, true, "0", "3", "0", "4", true, "240",
                STLineSpacingRule.Enum.forString("auto"));
//        setParagraphAlignInfo(p, ParagraphAlignment.LEFT, TextAlignment.CENTER);
    }

    /**
     * @param verticalAlign
     *            SUPERSCRIPT上标 SUBSCRIPT下标
     * @param position
     *            字符位置 1磅=2
     */
    // 设置字体信息 设置字符间距信息(CTSignedTwipsMeasure)
    public static void setTextFontInfo(XWPFParagraph p, boolean isInsert,
                                       boolean isNewLine, String content, String fontFamily,
                                       String colorVal, String fontSize, boolean isBlod,
                                       UnderlinePatterns underPatterns, boolean isItalic,
                                       boolean isStrike, VerticalAlign verticalAlign, int position,
                                       String spacingValue) {
        XWPFRun pRun = null;
        if (isInsert) {
            pRun = p.createRun();
        } else {
            if (p.getRuns() != null && p.getRuns().size() > 0) {
                pRun = p.getRuns().get(0);
            } else {
                pRun = p.createRun();
            }
        }
        if (isNewLine) {
            pRun.addBreak();
        }
        pRun.setText(content);
        // 设置字体样式
        pRun.setBold(isBlod);
        pRun.setItalic(isItalic);
        pRun.setStrike(isStrike);
        if (underPatterns != null) {
            pRun.setUnderline(underPatterns);
        }
        pRun.setColor(colorVal);
        if (verticalAlign != null) {
            pRun.setSubscript(verticalAlign);
        }
        pRun.setTextPosition(position);

        CTRPr pRpr = null;
        if (pRun.getCTR() != null) {
            pRpr = pRun.getCTR().getRPr();
            if (pRpr == null) {
                pRpr = pRun.getCTR().addNewRPr();
            }
        } else {
            // pRpr = p.getCTP().addNewR().addNewRPr();
        }
        // 设置字体
        CTFonts fonts = pRpr.isSetRFonts() ? pRpr.getRFonts() : pRpr
                .addNewRFonts();
        fonts.setAscii(fontFamily);
        fonts.setEastAsia(fontFamily);
        fonts.setHAnsi(fontFamily);

        // 设置字体大小
        CTHpsMeasure sz = pRpr.isSetSz() ? pRpr.getSz() : pRpr.addNewSz();
        sz.setVal(new BigInteger(fontSize));

        CTHpsMeasure szCs = pRpr.isSetSzCs() ? pRpr.getSzCs() : pRpr
                .addNewSzCs();
        szCs.setVal(new BigInteger(fontSize));

//        if(spacingValue!=null){ //设置字符间距信息
//            CTSignedTwipsMeasure ctSTwipsMeasure=pRpr.isSetSpacing()?pRpr.getSpacing():pRpr.addNewSpacing(); ctSTwipsMeasure.setVal(new BigInteger(spacingValue));
//        }
    }

    public static void addNewPage(XWPFDocument document, BreakType breakType) {
        XWPFParagraph xp = document.createParagraph();
        xp.createRun().addBreak(breakType);
    }

    public static void saveDocument(XWPFDocument document, String savePath)
            throws Exception {
        FileOutputStream fos = new FileOutputStream(savePath);
        document.write(fos);
        fos.close();
    }

    // 设置段落间距信息
    // 一行=100 一磅=20
    public static void setParagraphSpacingInfo(XWPFParagraph p, boolean isSpace,
                                               String before, String after, String beforeLines, String afterLines,
                                               boolean isLine, String line, STLineSpacingRule.Enum lineValue) {
        CTPPr pPPr = null;
        if (p.getCTP() != null) {
            if (p.getCTP().getPPr() != null) {
                pPPr = p.getCTP().getPPr();
            } else {
                pPPr = p.getCTP().addNewPPr();
            }
        }
        CTSpacing pSpacing = pPPr.getSpacing() != null ? pPPr.getSpacing()
                : pPPr.addNewSpacing();
        if (isSpace) {
            // 段前磅数
            if (before != null) {
                pSpacing.setBefore(new BigInteger(before));
            }
            // 段后磅数
            if (after != null) {
                pSpacing.setAfter(new BigInteger(after));
            }
            // 段前行数
            if (beforeLines != null) {
                pSpacing.setBeforeLines(new BigInteger(beforeLines));
            }
            // 段后行数
            if (afterLines != null) {
                pSpacing.setAfterLines(new BigInteger(afterLines));
            }
        }
        if (isLine) {
            if (line != null) {
                pSpacing.setLine(new BigInteger(line));
            }
            if (lineValue != null) {
                pSpacing.setLineRule(lineValue);
            }
        }
    }

    // 设置段落对齐
    public static void setParagraphAlignInfo(XWPFParagraph p,
                                             ParagraphAlignment pAlign, TextAlignment valign) {
        p.setAlignment(pAlign);
        p.setVerticalAlignment(valign);
    }



}
