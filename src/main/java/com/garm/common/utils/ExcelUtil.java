package com.garm.common.utils;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.tools.ant.util.DateUtils;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

/**
 *
 * excel数据解析工具类
 *
 * @author liwt
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */
@Slf4j
public class ExcelUtil {

	/**
	 * 获取文件名称
	 * @return
	 */
	public static String getFileName( String fileName) {
		// 20170207 针对IE环境下filename是整个文件路径的情况而做以下处理
		Integer index = fileName.lastIndexOf("\\");
		String newStr = "";
		if (index > -1) {
			newStr = fileName.substring(index + 1);
		} else {
			newStr = fileName;
		}
		if (!newStr.equals("")) {
			fileName = newStr;
		}
		return fileName;
	}

	/**
	 *
	 * 是否是2003的excel，返回true是2003
	 *
	 *
	 * @param filePath
	 * @return
	 * @see [类、类#方法、类#成员]
	 */
	public static boolean isExcel2003(String filePath) {
		return filePath.matches("^.+\\.(?i)(xls)$");
	}

	/**
	 *
	 * 是否是2007的excel，返回true是2007
	 *
	 *
	 * @param filePath
	 * @return
	 * @see [类、类#方法、类#成员]
	 */
	public static boolean isExcel2007(String filePath) {
		return filePath.matches("^.+\\.(?i)(xlsx)$");
	}

	/**
	 *
	 * 根据流读取Excel文件
	 *
	 *
	 * @param inputStream
	 * @param isExcel2003
	 * @return
	 * @see [类、类#方法、类#成员]
	 */
	public static List<List<List<String>>> read(InputStream inputStream, boolean isExcel2003,int sheetNum) throws IOException {


		/** 根据版本选择创建Workbook的方式 */
		Workbook wb = null;

		if (isExcel2003)
			wb = new HSSFWorkbook(inputStream);
		else
			wb = new XSSFWorkbook(inputStream);

		return readData(wb,sheetNum);
	}

	/**
	 *
	 * 根据流只读取Excel文件的sheet1
	 *
	 *
	 * @param inputStream
	 * @param isExcel2003
	 * @return
	 * @see [类、类#方法、类#成员]
	 */
	public static List<List<String>> readOne(InputStream inputStream, boolean isExcel2003) throws IOException {


		/** 根据版本选择创建Workbook的方式 */
		Workbook wb = null;

		if (isExcel2003)
			wb = new HSSFWorkbook(inputStream);
		else
			wb = new XSSFWorkbook(inputStream);

		return readDataOne(wb);
	}

	/**
	 *
	 * 读取数据
	 *
	 * @param wb
	 * @return
	 * @see [类、类#方法、类#成员]
	 */
	private static List<List<String>> readDataOne(Workbook wb) {
		//单个sheet页数据
		List<List<String>> sheetResult = new ArrayList<List<String>>();
		/** 得到第一个shell */
		Sheet sheet = wb.getSheetAt(0);


		/** 循环Excel的行 */
		for (int r = 1; r < sheet.getPhysicalNumberOfRows(); r++) {
			Row row = sheet.getRow(r);
			if (row == null) {
				continue;
			}

			List<String> rowLst = new ArrayList<String>();

			/** 循环Excel的列 */
			for (int c = 0; c < row.getLastCellNum(); c++) {
				String value = null;
				if(isMergedRegion(sheet,r,c)){
					value = getMergedRegionValue(sheet,r,c);
				}else{
					value = getCellValue(row.getCell(c));
				}
				/** 保存第r行的第c列 */
				rowLst.add(value);
			}
			if(!"".equals(rowLst.get(0))&&rowLst.size()!=1){
				sheetResult.add(rowLst);
			}
		}

		return sheetResult;

	}

	/**
	 *
	 * 读取数据
	 *
	 * @param wb
	 * @return
	 * @see [类、类#方法、类#成员]
	 */
	private static List<List<List<String>>> readData(Workbook wb, int sheetNum) {
		//多个sheet页数据集合
		List<List<List<String>>> result = new ArrayList<List<List<String>>>();
		for(int i=0;i<sheetNum;i++){
			//单个sheet页数据
			List<List<String>> sheetResult = new ArrayList<List<String>>();
			/** 得到第一个shell */
			Sheet sheet = wb.getSheetAt(i);


			/** 循环Excel的行 */
			for (int r = 1; r < sheet.getPhysicalNumberOfRows(); r++) {
				Row row = sheet.getRow(r);
				if (row == null||row.getPhysicalNumberOfCells()==0) {
					continue;
				}
//				else if(StringUtils.isEmpty(getCellValue(row.getCell(0)))&&StringUtils.isEmpty(getCellValue(row.getCell(1)))){
//					log.info("存在为空的列");
//					continue;
//				}

				List<String> rowLst = new ArrayList<String>();

				/** 循环Excel的列 */
				for (int c = 0; c < row.getLastCellNum(); c++) {

					String value = null;
					if(isMergedRegion(sheet,r,c)){
						value = getMergedRegionValue(sheet,r,c);
					}else{
						value = getCellValue(row.getCell(c));
					}
					/** 保存第r行的第c列 */
					rowLst.add(value);
//					if(!"".equals(value)){
//						rowLst.add(value);
//					}

				}
				if(rowLst.size()!=0&&checkList(rowLst)){
					rowLst = checkCloumn(rowLst);
					sheetResult.add(rowLst);
				}
			}

			result.add(sheetResult);
		}

		return result;

	}
	/**
	 * 判断字段是否为连续空串
	 * @param list
	 * @return	true  不为空
	 * 			false 为连续末尾空串
	 *
	 */
	private static List<String> checkCloumn(List<String> list){
		List<Integer> check = new ArrayList<>();
		for (int i=0;i<list.size();i++){
			if("".equals(list.get(i))){
				check.add(i);
			}
		}

		if(check.size()!=0&&((list.size()-1)==(check.get(check.size()-1))&&judege(check))){
			for(int i=check.size()-1;i>=0;i--){
				list.remove(check.get(i).intValue());
			}
		}
		return list;

	}

	/**
	 * 当最大值-最小值>n(数组长度)-1时，一定不是连续相邻数组
	 * @param list
	 * @return	false	不是连续相邻
	 * 			true	是连续相邻
	 */
	private static boolean judege(List<Integer> list) {
		int min = list.get(0);
		int max = list.get(list.size()-1);

		if ((max - min)>list.size()-1) {
			return false;
		}else{
			return true;
		}
	}

	/**
	 * 判断list是否为空
	 * @param list
	 * @return	true  不为空
	 * 			false 为空
	 *
	 */
	private static boolean checkList(List<String> list){
		AtomicInteger size = new AtomicInteger(0);
		list.stream().forEach(s->{
			if("".equals(s)){
				size.getAndIncrement();
			}
		});
		if(size.get()==list.size()){
			return false;
		}
		return true;

	}

	/**
	 * 获取合并单元格的值
	 * @param sheet
	 * @param row
	 * @param column
	 * @return
	 */
	public static String getMergedRegionValue(Sheet sheet, int row, int column){
		int sheetMergeCount = sheet.getNumMergedRegions();

		for(int i = 0 ; i < sheetMergeCount ; i++){
			CellRangeAddress ca = sheet.getMergedRegion(i);
			int firstColumn = ca.getFirstColumn();
			int lastColumn = ca.getLastColumn();
			int firstRow = ca.getFirstRow();
			int lastRow = ca.getLastRow();

			if(row >= firstRow && row <= lastRow){
				if(column >= firstColumn && column <= lastColumn){
					Row fRow = sheet.getRow(firstRow);
					Cell fCell = fRow.getCell(firstColumn);
					return getCellValue(fCell) ;
				}
			}
		}

		return null ;
	}

	/**
	 * 判断指定的单元格是否是合并单元格
	 * @param sheet
	 * @param row 行下标
	 * @param column 列下标
	 * @return
	 */
	private static boolean isMergedRegion(Sheet sheet, int row, int column) {
		int sheetMergeCount = sheet.getNumMergedRegions();
		for (int i = 0; i < sheetMergeCount; i++) {
			CellRangeAddress range = sheet.getMergedRegion(i);
			int firstColumn = range.getFirstColumn();
			int lastColumn = range.getLastColumn();
			int firstRow = range.getFirstRow();
			int lastRow = range.getLastRow();
			if(row >= firstRow && row <= lastRow){
				if(column >= firstColumn && column <= lastColumn){
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * 获取单元格的值
	 * @param cell
	 * @return
	 */
	private static String getCellValue(Cell cell){
		String cellValue = "";
		if (null != cell) {
			// 以下是判断数据的类型
			switch (cell.getCellType()) {
				case HSSFCell.CELL_TYPE_NUMERIC: // 数字

					if (HSSFDateUtil.isCellDateFormatted(cell)) { //判断是否为日期格式
						Date date = cell.getDateCellValue();
						cellValue = DateUtils.format(date, DateUtils.ISO8601_DATE_PATTERN);
					}else{
						System.out.println(cell.getNumericCellValue());
						BigDecimal bd = new BigDecimal(cell.getNumericCellValue());
						cellValue = bd.toPlainString();
					}
					break;

				case HSSFCell.CELL_TYPE_STRING: // 字符串
					cellValue = cell.getStringCellValue();
					break;

				case HSSFCell.CELL_TYPE_BOOLEAN: // Boolean
					cellValue = cell.getBooleanCellValue() + "";
					break;

				case HSSFCell.CELL_TYPE_FORMULA: // 公式
					cellValue = cell.getCellFormula() + "";
					break;

				case HSSFCell.CELL_TYPE_BLANK: // 空值
					cellValue = "";
					break;

				case HSSFCell.CELL_TYPE_ERROR: // 故障
					cellValue = "非法字符";
					break;

				default:
					cellValue = "未知类型";
					break;
			}
		}
		return cellValue;
	}


	/**
	 * 将数据保存为xml格式的Excel表格文件
	 * @param fileName 文件名
	 * @param excelHeader 表头数组（"表头名"#"map的key值"，..）
	 * @param data 数据
	 */
	public static SXSSFWorkbook datas2Excel(String fileName, String[] excelHeader,
                                            List<Map<String, Object>> data) {
		SXSSFWorkbook wb;
		Row row;
		Cell cell;
		CellStyle style;
		try {

			// 创建一个Workbook，对应一个Excel文件
			wb = new SXSSFWorkbook(100);

			// 设置标题样式
			style = getTitleStyle(wb);

			// 在Workbook中添加一个sheet,对应Excel文件中的sheet
			Sheet sheet = wb.createSheet(fileName);

			// 标题数组
			String[] titleArray = new String[excelHeader.length];
			// 字段名数组
			String[] fieldArray = new String[excelHeader.length];

			for (int i = 0; i < excelHeader.length; i++) {
				String[] tempArray = excelHeader[i].split("#");// 临时数组 分割#
				titleArray[i] = tempArray[0];
				fieldArray[i] = tempArray[1];
			}

			// 在sheet中添加标题行
			row = sheet.createRow((int) 0);// 行数从0开始
			cell = row.createCell(0);// cell列 从0开始 第一列添加序号

			cell.setCellValue("序号");
			cell.setCellStyle(style);
			sheet.autoSizeColumn(0);// 自动设置宽度

			// 为标题行赋值
			for (int i = 0; i < titleArray.length; i++) {
				cell = row.createCell(i + 1);// 0号位被序号占用，所以需+1
				cell.setCellValue(titleArray[i]);
				cell.setCellStyle(style);
				sheet.autoSizeColumn(i + 1);// 0号位被序号占用，所以需+1
			}

			// 数据样式 因为标题和数据样式不同 需要分开设置 不然会覆盖
			style = getDataStyle(wb);

			// 遍历集合数据，产生数据行
			int index = 0;
			if (data != null) {
				// 循环集合
				for (Map<String, Object> info : data) {
					index++;
					row = sheet.createRow(index);
					cell = row.createCell(0);// 序号值永远是第0列
					cell.setCellValue(index);
					cell.setCellStyle(style);
					for (int i = 0; i < fieldArray.length; i++) {
						cell = row.createCell(i + 1);
						cell.setCellStyle(style);
						String str = String.valueOf(info.get(fieldArray[i]));
						if(!"null".equals(str)) {
							cell.setCellValue(String.valueOf(info.get(fieldArray[i])));// 实现了动态封装数据
						}else {
							cell.setCellValue("");
						}
					}
				}
			}
			//设置自动列宽
			for (int i = 0; i <= titleArray.length; i++) {
				int colWidth = sheet.getColumnWidth(i)*15/10;
				if(colWidth<255*256){
					sheet.setColumnWidth(i, colWidth < 3000 ? 3000 : colWidth);
				}else{
					sheet.setColumnWidth(i,6000 );
				}
			}
			return wb ;
			// 导出（下载）Excel
//			outExcel(fileName, wb);
		} catch (Exception e) {
			log.error(e.getMessage());
			System.out.println(e.getMessage());
		}
		return null;
	}

	/**
	 * 数据样式
	 * @param wb
	 * @return
	 */
	public static CellStyle getDataStyle(Workbook wb) {
		// 设置数据样式
		CellStyle dataStyle = wb.createCellStyle();

		// 设置数据单元格边框样式
		dataStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);// 上边框 细边线
		dataStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);// 下边框 细边线
		dataStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);// 左边框 细边线
		dataStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);// 右边框 细边线

		// 设置数据单元格对齐方式
		dataStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 水平居中
		dataStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER); // 垂直居中

		// 设置数据字体
		Font dataFont = wb.createFont();
		dataFont.setFontHeightInPoints((short) 12); // 字体高度
		dataFont.setFontName("宋体"); // 字体
		dataStyle.setFont(dataFont);
		return dataStyle;
	}
	/**
	 * 表头样式
	 * @param wb
	 * @return
	 */
	public static CellStyle getTitleStyle(Workbook wb) {
		// 设置标题样式
		CellStyle titleStyle = wb.createCellStyle();

		// 设置单元格边框样式
		titleStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);// 上边框 细边线
		titleStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);// 下边框 细边线
		titleStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);// 左边框 细边线
		titleStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);// 右边框 细边线
		titleStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());// 设置单元格前景填充色
		titleStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);// 设置单元格前景填充样式
		// 设置单元格对齐方式
		titleStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 水平居中
		titleStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER); // 垂直居中

		// 设置字体样式
		Font titleFont = wb.createFont();
		titleFont.setFontHeightInPoints((short) 12); // 字体高度
		titleFont.setFontName("黑体"); // 字体样式
		titleStyle.setFont(titleFont);

		return titleStyle;
	}


}
