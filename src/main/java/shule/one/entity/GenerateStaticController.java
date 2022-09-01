package shule.one.entity;

import javax.servlet.http.HttpServletRequest;

import org.json.simple.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class GenerateStaticController {
	
	public static String imgpath="";  
	 
	  
	  @PostMapping("/generatestaticpdf") 
	  @ResponseBody
	  public String handleGeneratePdf(@RequestBody  JSONObject  search, HttpServletRequest request) {
  
		  try {
			  
//				String adm=search.get("adm").toString();
//
//				String SQL = "SELECT `adm no`, CONCAT(`first name`,' ',`second name`,' ',surname) AS name,"
//						+ "address,city,`DATE OF JOINING`,`date of leaving`,	`date of joining`,`date of leaving`,"
//						+ "(SELECT class FROM studentclass WHERE NO=`current form`) AS currentc,\r\n"
//						+ " (SELECT class FROM studentclass WHERE NO=`class joined`) AS classjoined,"
//						+ "`date of birth`,schoolname,schoolcategory \r\n" + " FROM registration \r\n"
//						+ " JOIN schooldetails  where `ADM NO`='" + adm + "'";

//				InputStream ip = getClass().getResourceAsStream("/LEAVINGCERT.jrxml");
//				JasperDesign jd = JRXmlLoader.load(ip);
//				Map<String, Object> param = new HashMap<>();
//				param.put("img", "/goklogo.png");
//				param.put("schoolname", "MATERI GIRL'S CENTER");
//				param.put("class", "FORM4");
//
//
//				JRDesignQuery newQuery = new JRDesignQuery();
//				newQuery.setText(SQL);
//				jd.setQuery(newQuery);
//				JasperReport jr = JasperCompileManager.compileReport(jd);
//				JasperPrint jp = JasperFillManager.fillReport(jr, param, conn);
//				JasperExportManager.exportReportToPdfFile(jp, System.getProperty("user.dir")+ "/target/"+"mcee.pdf");


			} catch (Exception e) {
				e.printStackTrace();
			}
		  
	    return "{\"querystatus\" : \"Image uploaded successfully\",\"path\" : \""+imgpath+"\"}";
	  }

	  
	  
	  
	}
