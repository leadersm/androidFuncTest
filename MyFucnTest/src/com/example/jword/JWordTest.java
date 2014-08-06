package com.example.jword;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.app.Service;
import android.content.Intent;
import android.net.Uri;
import android.os.IBinder;

import com.example.myfucntest.R;
import com.independentsoft.office.ExtendedBoolean;
import com.independentsoft.office.IContentElement;
import com.independentsoft.office.Unit;
import com.independentsoft.office.UnitType;
import com.independentsoft.office.drawing.Extents;
import com.independentsoft.office.drawing.Offset;
import com.independentsoft.office.drawing.Picture;
import com.independentsoft.office.drawing.PresetGeometry;
import com.independentsoft.office.drawing.ShapeType;
import com.independentsoft.office.drawing.Transform2D;
import com.independentsoft.office.word.BottomBorder;
import com.independentsoft.office.word.DrawingObject;
import com.independentsoft.office.word.HorizontalAlignmentType;
import com.independentsoft.office.word.LineSpacingRule;
import com.independentsoft.office.word.Paragraph;
import com.independentsoft.office.word.Run;
import com.independentsoft.office.word.Spacing;
import com.independentsoft.office.word.StandardBorderStyle;
import com.independentsoft.office.word.TopBorder;
import com.independentsoft.office.word.WordDocument;
import com.independentsoft.office.word.drawing.DrawingObjectSize;
import com.independentsoft.office.word.drawing.Inline;
import com.independentsoft.office.word.fonts.ThemeFont;
import com.independentsoft.office.word.headerFooter.Footer;
import com.independentsoft.office.word.headerFooter.Header;
import com.independentsoft.office.word.sections.Section;
import com.independentsoft.office.word.styles.Style;
import com.independentsoft.office.word.styles.StyleDefinitions;
import com.independentsoft.office.word.styles.StyleType;
import com.independentsoft.office.word.tables.Cell;
import com.independentsoft.office.word.tables.Row;
import com.independentsoft.office.word.tables.Table;
import com.independentsoft.office.word.tables.TableWidthUnit;
import com.independentsoft.office.word.tables.Width;

public class JWordTest extends Service {
	
	File outFile;
	
	static StyleDefinitions documentStyles = new StyleDefinitions();
	
	static Section section1 = new Section();
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		// TODO Auto-generated method stub
		File dir = new File("/mnt/sdcard/test");
		if(!dir.exists()){
			dir.mkdirs();
		}
		
		outFile = new File(dir,"jword.docx");
		
		if(outFile.exists()){
			outFile.delete(); 
		}
		
//		yunTianYi();
//		tableTest();
		
		try {
			WordDocument doc = new WordDocument(getResources().openRawResource(R.raw.jwordtest));
			System.out.println("size:"+doc.getTables().size());
			doc.replace("{no.}", "12345");
            doc.replace("{keywords}","关键词");

            doc.replace("{转发增量图}", getImageRun());
            
            List<Table> tables = doc.getTables();
            System.out.println("tables.size:"+tables.size());
            
            Table newsTable = tables.get(0);
//            for(IContentElement i : newsTable.getContentElements()){
//            	handleElement(i);
//            }
            
            
            addDataToTable(newsTable);
            
            doc.save(outFile.getAbsolutePath());
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return super.onStartCommand(intent, flags, startId);
	}
	
	private static void handleElement(IContentElement element) {
		// TODO Auto-generated method stub
		if(element.getContentElements().size()>0){
			for(IContentElement temp:element.getContentElements()){
				System.out.println(temp);
				handleElement(temp);
			}
		}
	}

	private static void addDataToTable(Table table){
		
		List<TableItem> items = new ArrayList<TableItem>();
		
		TableItem item1 = new TableItem("排序2", 1101);
		TableItem item2 = new TableItem("新闻指数2", 1559);
		TableItem item3 = new TableItem("新闻标题2", 2594);
		TableItem item4 = new TableItem("新闻来源2", 1517);
		TableItem item5 = new TableItem("发表日期2", 1876);
		
		items.add(item1);
		items.add(item2);
		items.add(item3);
		items.add(item4);
		items.add(item5);
		
		Row row = new Row();
		
		for(TableItem item:items){
			row.add(item.getCell());
		}
		
		table.add(row);
		
	}
	
	private static Run getImageRun(){
		try {
			Picture picture = new Picture("/mnt/sdcard/fnews/images/sz300254_k_day.png");
			Unit pictureWidth = new Unit(640, UnitType.PIXEL);
			Unit pictureHeight = new Unit(480, UnitType.PIXEL);

			Offset offset = new Offset(0, 0);
			Extents extents = new Extents(pictureWidth, pictureHeight);

			picture.getShapeProperties().setPresetGeometry(
					new PresetGeometry(ShapeType.RECTANGLE));
			picture.getShapeProperties().setTransform2D(
					new Transform2D(offset, extents));
			picture.setID("1");
			picture.setName("image.jpg");
			
			Inline inline = new Inline(picture);
			inline.setSize(new DrawingObjectSize(pictureWidth, pictureHeight));
			inline.setID("1");
			inline.setName("Picture 1");
			inline.setDescription("image.jpg");
			
			DrawingObject drawingObject = new DrawingObject(inline);
			Run run = new Run();
			run.add(drawingObject);

			return run;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	private void yunTianYi() {
		try {
    		initStyles();
            String keywords = "越南";
			WordDocument doc = new WordDocument();
			
			//添加页眉//页脚
			addHeaderAndFooter(doc);
			
			addReportNum(doc,"编号：TWSM-REPORT-SCIO-2014");
			
			addEmptyParagraph(doc,8);
			
			addReportTitle(doc,keywords);
			
			addEmptyParagraph(doc,14);
			
			addDate(doc);
			
			addEmptyParagraph(doc,10);
			
			addLogo(doc);
			
			addTitle1(doc, "（一）门户新闻舆情分析");
			
			addTitle2(doc, "（1）新闻简述");
			
			addText(doc, "这里是描述信息。。。。");
			
			addTitle2(doc, "（2）相关新闻排名");
			
			addNewsTable(doc);
			
			addEmptyParagraph(doc,1);
			
			String startTime = "2014-05-11";
			String endTime = "2014-05-21";
			String trend = "持续增长";
			int top100times = 2;
			int topMax = 10;
			
			addNewsJianXi(doc,keywords,startTime,endTime,trend,top100times,topMax);
			
			addTitle1(doc, "（二）微博舆情综述");   
			
			addTitle2(doc, "（1）微博用户参与量分析");
			
			addImageTitle(doc, "此话题的微博用户参与量横向图");
			
			addImage(doc,600,260,"/mnt/sdcard/test/temp.png");
			
			addTitle3(doc, "微博作者构成分析");
			
			addPlayersTable(doc);
			String topUsers5 = "用户1，用户2，用户3，用户4，用户5";
			
			addWeiboJianxi(doc,keywords,"媒体|网民",topUsers5);
			
			addTitle2(doc, "（2）微博转发趋势图表");
			
			addImageTitle(doc, String.format("“%s”事件转发增量图", keywords));
			
			addImage(doc,600,260,"/mnt/sdcard/test/temp.png");
			
			
			String time = "2014.03.31（00:00-12:00PM）";
			addImageTitle(doc, String.format("统计时间:%s", time));
			
			String timeRange = "";
			String maxRangeTime = "";
			int max = 500000;
			String nickname = "秦某某";
			int count = 10000;
			addZhuanfaJianxi(doc,keywords,timeRange,maxRangeTime,max,nickname,count);
			
			addTitle1(doc, "（二）微博舆情分析");
			addTitle3(doc, "(1)媒体微博报道一览");
			addText(doc, "目前媒体对此事高度关注，{媒体描述信息}");
			addImage(doc, 408, 346, "/mnt/sdcard/test/guandian.png");
			addTitle2(doc, "(2)网络舆论倾向性分析");
			addImage(doc, 408, 346, "/mnt/sdcard/test/guandian.png");

			
			
			addTitle2(doc, "(3)相关话题重点微博排名");
			addWeiboTop10Table(doc);
			
			doc.setStyleDefinitions(documentStyles);
			doc.save(outFile.getAbsolutePath());
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void tableTest(){
		try
        {
            WordDocument doc = new WordDocument();

            Run run1 = new Run("Quantity");
            run1.setBold(ExtendedBoolean.TRUE);

            Paragraph paragraph1 = new Paragraph();
            paragraph1.add(run1);

            Run run2 = new Run("Item #");
            run2.setBold(ExtendedBoolean.TRUE);

            Paragraph paragraph2 = new Paragraph();
            paragraph2.add(run2);

            Run run3 = new Run("Description");
            run3.setBold(ExtendedBoolean.TRUE);

            Paragraph paragraph3 = new Paragraph();
            paragraph3.add(run3);

            Run run4 = new Run("Unit Price");
            run4.setBold(ExtendedBoolean.TRUE);

            Paragraph paragraph4 = new Paragraph();
            paragraph4.add(run4);

            Run run5 = new Run("Line Total");
            run5.setBold(ExtendedBoolean.TRUE);

            Paragraph paragraph5 = new Paragraph();
            paragraph5.add(run5);

            Cell cell1 = new Cell();
            cell1.setWidth(new Width(TableWidthUnit.POINT, 120));
//            cell1.setShading(new Shading(ShadingPattern.PERCENT_10));
            cell1.add(paragraph1);

            Cell cell2 = new Cell();
            cell2.setWidth(new Width(TableWidthUnit.POINT, 140));
//            cell2.setShading(new Shading(ShadingPattern.PERCENT_10));
            cell2.add(paragraph2);

            Cell cell3 = new Cell();
            cell3.setWidth(new Width(TableWidthUnit.POINT, 410));
//            cell3.setShading(new Shading(ShadingPattern.PERCENT_10));
            cell3.add(paragraph3);

            Cell cell4 = new Cell();
            cell4.setWidth(new Width(TableWidthUnit.POINT, 160));
//            cell4.setShading(new Shading(ShadingPattern.PERCENT_10));
            cell4.add(paragraph4);

            Cell cell5 = new Cell();
            cell5.setWidth(new Width(TableWidthUnit.POINT, 160));
//            cell5.setShading(new Shading(ShadingPattern.PERCENT_10));
            cell5.add(paragraph5);

            Row firstRow = new Row();
            firstRow.add(cell1);
            firstRow.add(cell2);
            firstRow.add(cell3);
            firstRow.add(cell4);
            firstRow.add(cell5);

            Row row1 = new Row();
//            row1.add(new Cell());
//            row1.add(new Cell());
//            row1.add(new Cell());
//            row1.add(new Cell());
//            row1.add(new Cell());
            row1.add(cell1);
            row1.add(cell2);
            row1.add(cell3);
            row1.add(cell4);
            row1.add(cell5);

            Table table1 = new Table(StandardBorderStyle.SINGLE_LINE);

            table1.add(firstRow);
            
            table1.add(row1);
            table1.add(row1);
            table1.add(row1);
            table1.add(row1);
            table1.add(row1);
            table1.add(row1);
            table1.add(row1);
            table1.add(row1);
            table1.add(row1);
            table1.add(row1);

            doc.getBody().add(table1);

            doc.save(outFile.getAbsolutePath());
            
            new Thread(){public void run() {
            	Intent intent = new Intent("android.intent.action.VIEW");
    			intent.addCategory("android.intent.category.DEFAULT");
    			intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
    			Uri uri = Uri.fromFile(outFile);
    			intent.setDataAndType(uri, "application/msword");
    			intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
    			startActivity(intent);
            };}.start();
        }
        catch (Exception e)
        {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
	}
	
	static String zhuanfa = "当前“%s”话题相关微博转发量增长主要集中在%s。" +
    		"其中，微博转发量最大激增点发生在%s左右，单次增长量达到了%d次，" +
    		"高于一般国内热点事件的增长速度。对此次增长起到关键推动作用的是%s" +
    		"在相应时间点发布的博文。截止统计时间，该话题始端转发量超过了%d万，预计" +
    		"短时间内相关数据将继续增长。";
    private static void addZhuanfaJianxi(WordDocument doc, String keywords,
			String timeRange, String maxRangeTime, int max, String nickname,
			int count) {
		addTitle3(doc, "简析：");
		addText(doc, String.format(zhuanfa, keywords ,timeRange ,maxRangeTime,max,nickname,count));
	}


	static String weiboJianxi = "从上述的图表可以看出，今日参与“%s”相关话题讨论的以%s用户为主，其中最活跃的账号为：%s。";
    private static void addWeiboJianxi(WordDocument doc, String keywords,
			String goucheng, String topUsers5) {
    	addTitle3(doc, "简析:");
    	addText(doc, String.format(weiboJianxi, keywords,goucheng,topUsers5));
	}


	private static void addImage(WordDocument doc,int w,int h, String path) throws IOException {
		// TODO Auto-generated method stub
    	Picture picture = new Picture(path);
		Unit pictureWidth = new Unit(w, UnitType.PIXEL);
		Unit pictureHeight = new Unit(h, UnitType.PIXEL);

		Offset offset = new Offset(0, 0);
		Extents extents = new Extents(pictureWidth, pictureHeight);

		picture.getShapeProperties().setPresetGeometry(
				new PresetGeometry(ShapeType.RECTANGLE));
		picture.getShapeProperties().setTransform2D(
				new Transform2D(offset, extents));

		Inline inline = new Inline(picture);
		inline.setSize(new DrawingObjectSize(pictureWidth, pictureHeight));

		DrawingObject drawingObject = new DrawingObject(inline);

		Run run = new Run();
		run.add(drawingObject);

		Paragraph p = new Paragraph();
		p.setHorizontalTextAlignment(HorizontalAlignmentType.CENTER);
		p.add(run);

		doc.getBody().add(p);
	}


	static String jianxi = "从以上数据可以看出，“%s”相关新闻热度从%s到%s %s，%d次进入全国热点新" +
    		"闻排名前百名，相关报道最高排名第%d位。";
	private static void addNewsJianXi(WordDocument doc, String keywords,
			String startTime, String endTime,String trend, int top100times, int topMax) {
		// TODO Auto-generated method stub
		addTitle3(doc, "简析:");
		addText(doc, String.format(jianxi, keywords,startTime,endTime,trend,top100times,topMax));
		
	}


	private static void addNewsTable(WordDocument doc) {
		Table table = new Table(StandardBorderStyle.SINGLE_LINE);
		List<TableItem> items = new ArrayList<TableItem>();
		
		TableItem item1 = new TableItem("排序", 1101 ,true);
		TableItem item2 = new TableItem("新闻指数", 1559 ,true);
		TableItem item3 = new TableItem("新闻标题", 2694 ,true);
		TableItem item4 = new TableItem("新闻来源", 1517 ,true);
		TableItem item5 = new TableItem("发表日期", 1876 ,true);
		
		items.add(item1);
		items.add(item2);
		items.add(item3);
		items.add(item4);
		items.add(item5);
		
		Row row = new Row();
		
		for(TableItem item:items){
			row.add(item.getCell());
		}
		table.add(row);
		
		doc.getBody().add(table);
	}
	
	private static void addWeiboTop10Table(WordDocument doc) {
		Table table = new Table(StandardBorderStyle.SINGLE_LINE);
		List<TableItem> items = new ArrayList<TableItem>();
		
		TableItem item1 = new TableItem("排名", 1100 ,true);
		TableItem item2 = new TableItem("昵称", 1300 ,true);
		TableItem item3 = new TableItem("微博内容", 4000 ,true);
		TableItem item4 = new TableItem("转发量", 800 ,true);
		TableItem item5 = new TableItem("评论量", 800,true);
		TableItem item6 = new TableItem("发布时间", 1500 ,true);
		
		items.add(item1);
		items.add(item2);
		items.add(item3);
		items.add(item4);
		items.add(item5);
		items.add(item6);
		
		Row row = new Row();
		
		for(TableItem item:items){
			row.add(item.getCell());
		}
		table.add(row);
		
		doc.getBody().add(table);
	}
	
	private static void addPlayersTable(WordDocument doc) {
		Table table = new Table(StandardBorderStyle.SINGLE_LINE);
		List<TableItem> items = new ArrayList<TableItem>();
		
		TableItem item1 = new TableItem("排序", 2000 ,true);
		TableItem item2 = new TableItem("昵称", 3000 ,true);
		TableItem item3 = new TableItem("发布次数", 3000 ,true);
		
		items.add(item1);
		items.add(item2);
		items.add(item3);
		
		Row row = new Row();
		
		for(TableItem item:items){
			row.add(item.getCell());
		}
		table.add(row);
		
		doc.getBody().add(table);
	}
	
	static class TableItem {
		String name;
		int width ;
		boolean bold = false;
		
		public TableItem(String name, int width) {
			this.name = name;
			this.width = width;
		}

		public TableItem(String name, int width, boolean bold) {
			super();
			this.name = name;
			this.width = width;
			this.bold = bold;
		}


		public Cell getCell(){
			Run run = new Run(name);
			if(bold)run.setBold(ExtendedBoolean.TRUE);
			run.setFontSize(30);
			
			Paragraph p = new Paragraph();
			p.setHorizontalTextAlignment(HorizontalAlignmentType.CENTER);
			Spacing s = new Spacing();
			s.setBefore(100);
			s.setAfter(100);
			p.setSpacing(s);
			p.add(run);
			
			Cell c = new Cell();
			c.setWidth(new Width(TableWidthUnit.POINT, width));
			c.add(p);
			
			return c;
		}
	}
	

	private static void initStyles() {
		createHeaderStyle();
		createStyle1();
		createStyle2();
		createStyle3();
		createImageTitleStyle();
		createTextStyle();
	}
    


	private static void createImageTitleStyle() {
		// TODO Auto-generated method stub
		Style style = new Style();
		style.setID("imageTitle");
		style.setName("imageTitle");
		style.setType(StyleType.PARAGRAPH);
		style.setPrimary(ExtendedBoolean.TRUE);
		style.setUserInterfacePriority(9);
		
		Spacing spacing = new Spacing();
		spacing.setBefore(150);
		spacing.setAfter(150);
		spacing.setLine(240);
		spacing.setLineRule(LineSpacingRule.AUTO);
		
		style.getParagraphProperties().setSpacing(spacing);
		
		style.getParagraphProperties().setKeepNext(ExtendedBoolean.TRUE);
		style.getParagraphProperties().setKeepLines(ExtendedBoolean.TRUE);
		style.getParagraphProperties().setHorizontalTextAlignment(HorizontalAlignmentType.CENTER);
		style.getParagraphProperties().setOutlineLevel(0);
		
		style.getRunProperties().getFonts().setAsciiThemeFont(ThemeFont.MAJOR_HIGH_ANSI);
		style.getRunProperties().getFonts().setEastAsiaThemeFont(ThemeFont.MAJOR_EAST_ASIA);
		style.getRunProperties().getFonts().setHighAnsiThemeFont(ThemeFont.MAJOR_HIGH_ANSI);
		style.getRunProperties().getFonts().setComplexScriptThemeFont(ThemeFont.MAJOR_COMPLEX_SCRIPT);
		style.getRunProperties().setBold(ExtendedBoolean.TRUE);
		style.getRunProperties().setComplexScriptBold(ExtendedBoolean.TRUE);
		style.getRunProperties().setFontSize(26);
		style.getRunProperties().setComplexScriptFontSize(26);
		
		documentStyles.getStyles().add(style);
	}


	private static void addTitle1(WordDocument doc,String title) {
		// TODO Auto-generated method stub
		Run r = new Run();
		r.addText(title);
		
		Paragraph p = new Paragraph();
		p.add(r);
		p.setStyleName("title1");
		
		doc.getBody().add(p);
	}
	
	private static void addTitle2(WordDocument doc,String title) {
		// TODO Auto-generated method stub
		Run r = new Run();
		r.addText(title);
		
		Paragraph p = new Paragraph();
		p.add(r);
		p.setStyleName("title2");
		
		doc.getBody().add(p);
	}
	
	private static void addTitle3(WordDocument doc,String title) {
		// TODO Auto-generated method stub
		Run r = new Run();
		r.addText(title);
		
		Paragraph p = new Paragraph();
		p.add(r);
		p.setStyleName("title3");
		
		doc.getBody().add(p);
	}
	
	private static void addImageTitle(WordDocument doc,String title) {
		// TODO Auto-generated method stub
		Run r = new Run();
		r.addText(title);
		
		Paragraph p = new Paragraph();
		p.add(r);
		p.setStyleName("imageTitle");
		
		doc.getBody().add(p);
	}
	
	private static void addText(WordDocument doc,String text) {
		// TODO Auto-generated method stub
		Run r = new Run();
		r.addText("    "+text);
		
		Paragraph p = new Paragraph();
		p.add(r);
		p.setStyleName("text");
		
		doc.getBody().add(p);
	}
	


	private static void createStyle1() {
		Style titleStyle1 = new Style();
        titleStyle1.setID("title1");
        titleStyle1.setName("title1");
        titleStyle1.setType(StyleType.PARAGRAPH);
        titleStyle1.setPrimary(ExtendedBoolean.TRUE);
        titleStyle1.setUserInterfacePriority(9);

        Spacing spacing1 = new Spacing();
        spacing1.setBefore(50);
        spacing1.setAfter(300);
        spacing1.setLine(240);
        spacing1.setLineRule(LineSpacingRule.AUTO);

        titleStyle1.getParagraphProperties().setSpacing(spacing1);
        
        titleStyle1.getParagraphProperties().setKeepNext(ExtendedBoolean.TRUE);
        titleStyle1.getParagraphProperties().setKeepLines(ExtendedBoolean.TRUE);
        titleStyle1.getParagraphProperties().setOutlineLevel(0);

        titleStyle1.getRunProperties().getFonts().setAsciiThemeFont(ThemeFont.MAJOR_HIGH_ANSI);
        titleStyle1.getRunProperties().getFonts().setEastAsiaThemeFont(ThemeFont.MAJOR_EAST_ASIA);
        titleStyle1.getRunProperties().getFonts().setHighAnsiThemeFont(ThemeFont.MAJOR_HIGH_ANSI);
        titleStyle1.getRunProperties().getFonts().setComplexScriptThemeFont(ThemeFont.MAJOR_COMPLEX_SCRIPT);
        titleStyle1.getRunProperties().setBold(ExtendedBoolean.TRUE);
        titleStyle1.getRunProperties().setComplexScriptBold(ExtendedBoolean.TRUE);
        titleStyle1.getRunProperties().setFontSize(34);
        titleStyle1.getRunProperties().setComplexScriptFontSize(34);

        documentStyles.getStyles().add(titleStyle1);
	}


	private static void createStyle2() {
		Style titleStyle2 = new Style();
		titleStyle2.setID("title2");
		titleStyle2.setName("title2");
		titleStyle2.setType(StyleType.PARAGRAPH);
		titleStyle2.setPrimary(ExtendedBoolean.TRUE);
		titleStyle2.setUserInterfacePriority(9);
		
		Spacing spacing2 = new Spacing();
		spacing2.setBefore(50);
		spacing2.setAfter(300);
		spacing2.setLine(240);
		spacing2.setLineRule(LineSpacingRule.AUTO);
		
		titleStyle2.getParagraphProperties().setSpacing(spacing2);
		
		titleStyle2.getParagraphProperties().setKeepNext(ExtendedBoolean.TRUE);
		titleStyle2.getParagraphProperties().setKeepLines(ExtendedBoolean.TRUE);
		titleStyle2.getParagraphProperties().setOutlineLevel(0);
		
		titleStyle2.getRunProperties().getFonts().setAsciiThemeFont(ThemeFont.MAJOR_HIGH_ANSI);
		titleStyle2.getRunProperties().getFonts().setEastAsiaThemeFont(ThemeFont.MAJOR_EAST_ASIA);
		titleStyle2.getRunProperties().getFonts().setHighAnsiThemeFont(ThemeFont.MAJOR_HIGH_ANSI);
		titleStyle2.getRunProperties().getFonts().setComplexScriptThemeFont(ThemeFont.MAJOR_COMPLEX_SCRIPT);
		titleStyle2.getRunProperties().setBold(ExtendedBoolean.TRUE);
		titleStyle2.getRunProperties().setComplexScriptBold(ExtendedBoolean.TRUE);
		titleStyle2.getRunProperties().setFontSize(34);
		titleStyle2.getRunProperties().setComplexScriptFontSize(34);
		
		documentStyles.getStyles().add(titleStyle2);
	}
	
	private static void createStyle3() {
		Style style = new Style();
		style.setID("title3");
		style.setName("title3");
		style.setType(StyleType.PARAGRAPH);
		style.setPrimary(ExtendedBoolean.TRUE);
		style.setUserInterfacePriority(9);
		
		Spacing spacing = new Spacing();
		spacing.setBefore(150);
		spacing.setAfter(150);
		spacing.setLine(240);
		spacing.setLineRule(LineSpacingRule.AUTO);
		
		style.getParagraphProperties().setSpacing(spacing);
		
		style.getParagraphProperties().setKeepNext(ExtendedBoolean.TRUE);
		style.getParagraphProperties().setKeepLines(ExtendedBoolean.TRUE);
		style.getParagraphProperties().setOutlineLevel(0);
		
		style.getRunProperties().getFonts().setAsciiThemeFont(ThemeFont.MAJOR_HIGH_ANSI);
		style.getRunProperties().getFonts().setEastAsiaThemeFont(ThemeFont.MAJOR_EAST_ASIA);
		style.getRunProperties().getFonts().setHighAnsiThemeFont(ThemeFont.MAJOR_HIGH_ANSI);
		style.getRunProperties().getFonts().setComplexScriptThemeFont(ThemeFont.MAJOR_COMPLEX_SCRIPT);
		style.getRunProperties().setBold(ExtendedBoolean.TRUE);
		style.getRunProperties().setComplexScriptBold(ExtendedBoolean.TRUE);
		style.getRunProperties().setFontSize(30);
		style.getRunProperties().setComplexScriptFontSize(30);
		
		documentStyles.getStyles().add(style);
	}
	
	private static void createTextStyle() {
		Style style = new Style();
        style.setID("text");
        style.setName("text");
        style.setType(StyleType.PARAGRAPH);
        style.setPrimary(ExtendedBoolean.TRUE);
        style.setUserInterfacePriority(9);
        
        Spacing spacing = new Spacing();
        spacing.setBefore(150);
        spacing.setAfter(150);
        spacing.setLine(240);
        spacing.setLineRule(LineSpacingRule.AUTO);

        style.getParagraphProperties().setSpacing(spacing);

        style.getParagraphProperties().setKeepNext(ExtendedBoolean.TRUE);
        style.getParagraphProperties().setKeepLines(ExtendedBoolean.TRUE);
        style.getParagraphProperties().setOutlineLevel(0);

        style.getRunProperties().getFonts().setAsciiThemeFont(ThemeFont.MAJOR_HIGH_ANSI);
        style.getRunProperties().getFonts().setEastAsiaThemeFont(ThemeFont.MAJOR_EAST_ASIA);
        style.getRunProperties().getFonts().setHighAnsiThemeFont(ThemeFont.MAJOR_HIGH_ANSI);
        style.getRunProperties().getFonts().setComplexScriptThemeFont(ThemeFont.MAJOR_COMPLEX_SCRIPT);
        style.getRunProperties().setFontSize(26);
        style.getRunProperties().setComplexScriptFontSize(26);

        documentStyles.getStyles().add(style);
	}


	private static void addLogo(WordDocument doc) throws IOException {
		// TODO Auto-generated method stub
		Picture picture = new Picture("/mnt/sdcard/test/twsmlogo.png");
		Unit pictureWidth = new Unit(194, UnitType.PIXEL);
		Unit pictureHeight = new Unit(42, UnitType.PIXEL);

		Offset offset = new Offset(0, 0);
		Extents extents = new Extents(pictureWidth, pictureHeight);

		picture.getShapeProperties().setPresetGeometry(
				new PresetGeometry(ShapeType.RECTANGLE));
		picture.getShapeProperties().setTransform2D(
				new Transform2D(offset, extents));
		
		Inline inline = new Inline(picture);
		inline.setSize(new DrawingObjectSize(pictureWidth, pictureHeight));
		
		DrawingObject drawingObject = new DrawingObject(inline);
		Run logoRun = new Run();
		logoRun.add(drawingObject);
		
		Paragraph p = new Paragraph();
        p.add(logoRun);
        p.setHorizontalTextAlignment(HorizontalAlignmentType.CENTER);
        p.setSection(section1);
        
        doc.getBody().add(p);
	}


	private static void addDate(WordDocument doc) {
		// TODO Auto-generated method stub
		Date date = new Date(System.currentTimeMillis());
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

		Run run = new Run(sdf.format(date));
		Paragraph p = new Paragraph();
        p.setHorizontalTextAlignment(HorizontalAlignmentType.CENTER);
        p.add(run);
        p.setStyleName("title");
        
        doc.getBody().add(p);
	}


	private static void addReportTitle(WordDocument doc, String title) {
		// TODO Auto-generated method stub
		Run run = new Run();
        run.addText(title);
        
        Style style = new Style();
        style.setID("title");
        style.setName("title");
        style.setType(StyleType.PARAGRAPH);
        style.setPrimary(ExtendedBoolean.TRUE);
        style.setUserInterfacePriority(9);

        style.getParagraphProperties().setKeepNext(ExtendedBoolean.TRUE);
        style.getParagraphProperties().setKeepLines(ExtendedBoolean.TRUE);
        style.getParagraphProperties().setOutlineLevel(0);

        style.getRunProperties().getFonts().setAsciiThemeFont(ThemeFont.MAJOR_HIGH_ANSI);
        style.getRunProperties().getFonts().setEastAsiaThemeFont(ThemeFont.MAJOR_EAST_ASIA);
        style.getRunProperties().getFonts().setHighAnsiThemeFont(ThemeFont.MAJOR_HIGH_ANSI);
        style.getRunProperties().getFonts().setComplexScriptThemeFont(ThemeFont.MAJOR_COMPLEX_SCRIPT);
        style.getRunProperties().setBold(ExtendedBoolean.TRUE);
        style.getRunProperties().setComplexScriptBold(ExtendedBoolean.TRUE);
        style.getRunProperties().setFontSize(50);
        style.getRunProperties().setComplexScriptFontSize(50);

        documentStyles.getStyles().add(style);
        
        Paragraph p = new Paragraph();
        p.setHorizontalTextAlignment(HorizontalAlignmentType.CENTER);
        p.add(run);
        p.setStyleName("title");
        
        Run r2 = new Run("舆情分析");
        Paragraph p2 = new Paragraph();
        p2.setHorizontalTextAlignment(HorizontalAlignmentType.CENTER);
        p2.add(r2);
        p2.setStyleName("title");
        
        doc.getBody().add(p);
        doc.getBody().add(p2);
	}


	private static void addEmptyParagraph(WordDocument doc,int num) {
		for(int i=0;i<num;i++){
			Paragraph p = new Paragraph();
			doc.getBody().add(p);
		}
	}


	private static void addHeaderAndFooter(WordDocument doc) throws IOException {
		Picture picture = new Picture("/mnt/sdcard/test/headerImage.png");
		Unit pictureWidth = new Unit(101, UnitType.PIXEL);
		Unit pictureHeight = new Unit(22, UnitType.PIXEL);

		Offset offset = new Offset(0, 0);
		Extents extents = new Extents(pictureWidth, pictureHeight);

		picture.getShapeProperties().setPresetGeometry(
				new PresetGeometry(ShapeType.RECTANGLE));
		picture.getShapeProperties().setTransform2D(
				new Transform2D(offset, extents));
		
		Inline inline = new Inline(picture);
		inline.setSize(new DrawingObjectSize(pictureWidth, pictureHeight));
		
		DrawingObject drawingObject = new DrawingObject(inline);
		Run headerRun = new Run();
		headerRun.add(drawingObject);

        Paragraph headerParagraph = new Paragraph();
        headerParagraph.add(headerRun);
        headerParagraph.setStyleName("header");
        headerParagraph.setHorizontalTextAlignment(HorizontalAlignmentType.LEFT);
        
        Header header = new Header();
        header.add(headerParagraph);

        Run footerRun = new Run();
        
        footerRun.addText("天闻机密，未经允许不得扩散");

        Paragraph footerParagraph = new Paragraph();
        footerParagraph.add(footerRun);
        
        TopBorder tb = new TopBorder(StandardBorderStyle.SINGLE_LINE);
        tb.setWidth(8);
        footerParagraph.setTopBorder(tb);
        
        footerParagraph.setHorizontalTextAlignment(HorizontalAlignmentType.CENTER);

        Footer footer = new Footer();
        footer.add(footerParagraph);

        doc.getBody().setHeader(header);
        doc.getBody().setFooter(footer);
	}


	private static void createHeaderStyle() {
		Style headerStyle = new Style();
        headerStyle.setID("header");
        headerStyle.setName("header");
        headerStyle.setType(StyleType.PARAGRAPH);
        headerStyle.setPrimary(ExtendedBoolean.TRUE);
        headerStyle.setUserInterfacePriority(9);

        BottomBorder bottomBorder = new BottomBorder(StandardBorderStyle.SINGLE_LINE);
        bottomBorder.setSpace(1);
        bottomBorder.setWidth(8);
        
        headerStyle.getParagraphProperties().setBottomBorder(bottomBorder);
        headerStyle.getParagraphProperties().setIgnoreSpace(ExtendedBoolean.TRUE);
        
        documentStyles.getStyles().add(headerStyle);
	}
	
	private static void addReportNum(WordDocument doc, String num){
		Run run1 = new Run();
        run1.addText(num);
        
        Style style1 = new Style();
        style1.setID("num");
        style1.setName("num");
        style1.setType(StyleType.PARAGRAPH);
        style1.setPrimary(ExtendedBoolean.TRUE);
        style1.setUserInterfacePriority(9);

        style1.getParagraphProperties().setKeepNext(ExtendedBoolean.TRUE);
        style1.getParagraphProperties().setKeepLines(ExtendedBoolean.TRUE);
        style1.getParagraphProperties().setOutlineLevel(0);

        style1.getRunProperties().getFonts().setAsciiThemeFont(ThemeFont.MAJOR_HIGH_ANSI);
        style1.getRunProperties().getFonts().setEastAsiaThemeFont(ThemeFont.MAJOR_EAST_ASIA);
        style1.getRunProperties().getFonts().setHighAnsiThemeFont(ThemeFont.MAJOR_HIGH_ANSI);
        style1.getRunProperties().getFonts().setComplexScriptThemeFont(ThemeFont.MAJOR_COMPLEX_SCRIPT);
        style1.getRunProperties().setBold(ExtendedBoolean.TRUE);
        style1.getRunProperties().setComplexScriptBold(ExtendedBoolean.TRUE);
        style1.getRunProperties().setFontSize(28); //14 points
        style1.getRunProperties().setComplexScriptFontSize(28); //14 points

        documentStyles.getStyles().add(style1);
        
        Paragraph p = new Paragraph();
        p.add(run1);
        p.setStyleName("num");
        doc.getBody().add(p);
	}
	
	

	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}

}
