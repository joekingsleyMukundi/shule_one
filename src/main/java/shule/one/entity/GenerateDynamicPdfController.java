package shule.one.entity;

import org.springframework.stereotype.Controller;

@Controller
public class GenerateDynamicPdfController {
	
	public static String imgpath="";  
	public static  String reportsql="";
	public static  String Headermsg="";
	public static  String subttlestyle="";
	 
	  
//	  @PostMapping("/generatepdf") 
//	  @ResponseBody
//	  public String handleGeneratePdf() {
//  
//		  testReport();
//          exportToJRXML();              
//		  
//	    return "{\"querystatus\" : \"Image uploaded successfully\",\"path\" : \""+imgpath+"\"}";
//	  }
//
//	  
//	  public DynamicReport buildReport() throws Exception {
//		    
//		Style detailStyle = new Style("detail");
//	    detailStyle.setFont(new Font(7, null, false));
//	    detailStyle.setHorizontalAlign(HorizontalAlign.CENTER);
//	    detailStyle.setBorder(Border.THIN());
//	    
//	    Style groupVariables = new Style("groupVariables");
//	    groupVariables.setFont(new Font(7, null, true));
//	    groupVariables.setTextColor(Color.BLUE);
//	    groupVariables.setBorderBottom(Border.THIN());
//	    groupVariables.setHorizontalAlign(HorizontalAlign.RIGHT);
//	    groupVariables.setVerticalAlign(VerticalAlign.BOTTOM);
//	    
//	    Style headerStyle = new Style("header");
//	    headerStyle.setFont(new Font(7, null, true));
//	    headerStyle.setBackgroundColor(Color.gray);
//	    headerStyle.setTextColor(Color.white);
//	    headerStyle.setHorizontalAlign(HorizontalAlign.CENTER);
//	    headerStyle.setVerticalAlign(VerticalAlign.MIDDLE);
//	    headerStyle.setTransparency(Transparency.OPAQUE);
//	    headerStyle.setBorder(Border.THIN());
//	   
//	    Style titleStyle = new Style("titleStyle");
//	    titleStyle.setFont(new Font(7, null, false));
//	    titleStyle.setHorizontalAlign(HorizontalAlign.CENTER);
//	    
//	    Style titleStyle1 = new Style("titleStyle1");
//	    titleStyle1.setFont(new Font(7, null, true));
//	    titleStyle1.setHorizontalAlign(HorizontalAlign.LEFT);
//	    titleStyle1.setBorder(Border.THIN());
//	    
//	    Style titleStylegg = new Style("titleStylegg");
//	    titleStylegg.setFont(new Font(7, null, true));
//	    titleStylegg.setHorizontalAlign(HorizontalAlign.CENTER);
//	    
//	    Style subtitlestyle = new Style("titleStyle");
//	    subtitlestyle.setFont(new Font(7, null, true));
//	    subtitlestyle.setHorizontalAlign(HorizontalAlign.CENTER);
//	    
//	    DynamicReportBuilder drb = new DynamicReportBuilder();
//	    Integer margin = new Integer(10);
//	    drb.setTitleStyle(titleStyle)
//	      .setTitle(Headermsg)
//	      .setDetailHeight((new Integer(0)).intValue()).setLeftMargin(margin.intValue())
//	      .setRightMargin(margin.intValue()).setTopMargin(margin.intValue()).setBottomMargin(margin.intValue());
//	    drb.setSubtitleStyle(titleStyle)
//	      .setSubtitle(subttlestyle)
//	      .setDetailHeight((new Integer(0)).intValue()).setLeftMargin(margin.intValue())
//	      .setRightMargin(margin.intValue()).setTopMargin(margin.intValue()).setBottomMargin(margin.intValue());
//	    
//	    AbstractColumn columnBranch = null;
//	    
//	    
//	      
//	    ResultSet rs = conn.prepareStatement(reportsql).executeQuery();
//	    for (int i = 1; i < rs.getMetaData().getColumnCount(); i++) {
//	    	
//	    	System.out.println(rs.getMetaData().getColumnName(i)+" mcee");
//	    	
//	    	if (rs.getMetaData().getColumnName(i).equals("Sn")) {
//	  	      
//	    	 columnBranch = ColumnBuilder.getNew()
//	    		      .setColumnProperty("Sn", Integer.class.getName()).setTitle(
//	    		        "Sn").setWidth((new Integer(30)).intValue())
//	    		      .setFixedWidth(true).setStyle(
//	    		        detailStyle).setHeaderStyle(headerStyle).build();
//	    		    drb.addColumn(columnBranch);
//	    	}
//	    	else if (rs.getMetaData().getColumnName(i).equals("Name")) {
//		        columnBranch = ColumnBuilder.getNew()
//		          .setColumnProperty(rs.getMetaData().getColumnName(i), String.class.getName()).setTitle(
//		            rs.getMetaData().getColumnName(i)).setWidth((new Integer(135)).intValue())
//		          .setFixedWidth(true).setStyle(
//		            detailStyle).setHeaderStyle(headerStyle).build();
//		        drb.addColumn(columnBranch);
//		      } 
//	      
//	      else if (rs.getMetaData().getColumnName(i).equals("Regno")) {
//		        columnBranch = ColumnBuilder.getNew()
//		          .setColumnProperty(rs.getMetaData().getColumnName(i), String.class.getName()).setTitle(
//		            rs.getMetaData().getColumnName(i)).setWidth((new Integer(50)).intValue())
//		          .setFixedWidth(true).setStyle(
//		            detailStyle).setHeaderStyle(headerStyle).build();
//		        drb.addColumn(columnBranch);
//		      } 
//	    	
//	      else  {
//	    	  
//		        columnBranch = ColumnBuilder.getNew()
//		          .setColumnProperty(rs.getMetaData().getColumnName(i), String.class.getName()).setTitle(
//		            rs.getMetaData().getColumnName(i))
//		          .setFixedWidth(true).setStyle(
//		            detailStyle).setHeaderStyle(headerStyle).build();
//		        drb.addColumn(columnBranch);
//		      } 
//	      
//	      
//	    } 
//	    
//	    drb.setQuery(reportsql, "sql");
//	    drb.setUseFullPageWidth(true);
//	    drb.setPageSizeAndOrientation(Page.Page_A4_Landscape());
//	    drb.setTemplateFile("TemplateReportTest.jrxml");
//	    DynamicReport dr = drb.build();
//	    
//	    return dr;
//	    
//	  }
//	 	
//	  
//	  protected static JasperPrint jp;
//	  
//	  protected JasperReport jr;
//	  
//	  protected final Map<String, Object> params = new HashMap<>();
//	  
//	  protected DynamicReport dr;
//	  
//	  public void testReport() {
//		  try {
//	    this.dr = buildReport();
//	    this.jr = DynamicJasperHelper.generateJasperReport(this.dr, getLayoutManager(), this.params);
//	    System.out.println("Filling the report");
//	    if (conn != null) {
//	      jp = JasperFillManager.fillReport(this.jr, this.params, conn);
//	    } else {
//	      jp = JasperFillManager.fillReport(this.jr, this.params);
//	    } 
//	    jp.setOrientation(OrientationEnum.LANDSCAPE);
//	    jp.setPageWidth(842);
//	    jp.setPageHeight(530);
//	    
//	    exportReport();
//	    
//		  } catch (Exception e) {
//	            e.printStackTrace();
//	     }
//	  }
//	  
//	  protected LayoutManager getLayoutManager() {
//	    return (LayoutManager)new ClassicLayoutManager();
//	  }
//	  
//	  protected void exportReport() throws Exception {
//	    exportToJRXML();
//	  }
//	  
//	  protected void exportToJRXML(){
//	    
//		  try {
//			  
//		  if (this.jr != null) {
//	    	
//	    	jp = DynamicJasperHelper.generateJasperPrint(dr, new ClassicLayoutManager(), conn,params );
//		    ReportExporter.exportReport(jp, System.getProperty("user.dir")+ "/target/"+this.getClass().getName()+".pdf");
//		    jr = DynamicJasperHelper.generateJasperReport(dr,  new ClassicLayoutManager(),params);	    	
//	    } else {
//	    	jp = DynamicJasperHelper.generateJasperPrint(dr, new ClassicLayoutManager(), conn,params );
//		    ReportExporter.exportReport(jp, System.getProperty("user.dir")+ "/target/"+this.getClass().getName()+".pdf");
//		    jr = DynamicJasperHelper.generateJasperReport(dr,  new ClassicLayoutManager(),params);		   
//		    }
//	    
//	  	} catch (Exception e) {
//          e.printStackTrace();
//        }
//	  }
//	  
//	  
//	  public DynamicReport getDynamicReport() {
//	    return this.dr;
//	  }
//	  
//	  public int getYear() {
//	    return Calendar.getInstance().get(1);
//	  }
//	  
//	  
//	  
	 
	  

	}
