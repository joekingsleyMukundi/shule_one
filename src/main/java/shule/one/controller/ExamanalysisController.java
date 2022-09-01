package shule.one.controller;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;

import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.RequestContextHolder;

import com.lowagie.text.pdf.RGBColor;

import ar.com.fdvs.dj.core.DynamicJasperHelper;
import ar.com.fdvs.dj.core.layout.ClassicLayoutManager;
import ar.com.fdvs.dj.core.layout.LayoutManager;
import ar.com.fdvs.dj.domain.DJCalculation;
import ar.com.fdvs.dj.domain.DynamicReport;
import ar.com.fdvs.dj.domain.Style;
import ar.com.fdvs.dj.domain.builders.ColumnBuilder;
import ar.com.fdvs.dj.domain.builders.DynamicReportBuilder;
import ar.com.fdvs.dj.domain.builders.GroupBuilder;
import ar.com.fdvs.dj.domain.constants.Border;
import ar.com.fdvs.dj.domain.constants.Font;
import ar.com.fdvs.dj.domain.constants.HorizontalAlign;
import ar.com.fdvs.dj.domain.constants.Page;
import ar.com.fdvs.dj.domain.constants.Transparency;
import ar.com.fdvs.dj.domain.constants.VerticalAlign;
import ar.com.fdvs.dj.domain.entities.DJGroup;
import ar.com.fdvs.dj.domain.entities.columns.AbstractColumn;
import ar.com.fdvs.dj.domain.entities.columns.PropertyColumn;
import javafx.beans.property.ListProperty;
import javafx.beans.property.StringProperty;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.design.JRDesignQuery;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.type.OrientationEnum;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import shule.one.DbConnector;
import shule.one.entity.accountsmodel;
import shule.one.entity.jsontosql;
import shule.one.service.getremoteip;

@Controller
public class ExamanalysisController{	
	
	
	
	@Value("${dynamic.template.report.path}")
    private String dynamicreport_template;
	
	
	@Value("${best.class.report.path}")
    private String best_class_report;
	
	
	
	
	 @RequestMapping(value="/examcategories", method=RequestMethod.POST)
	 public String ExamCategories() {
		 
		 if(LoginController.getloggedinuseridtype()!=2) {
				return "fragments/exam :: modalerror";        
			}else {
				return "fragments/exam :: examcategories";        
			}
			
	 }	 
	 	 
	 
	 
	 @RequestMapping(value = "/api/exam/loadexamcategories" , method = RequestMethod.GET)
	 @ResponseBody
	 public String listofExamCategories() {
		 
		 
		 accountsmodel.dbaction(" SELECT '0' as querystatus,0 as checkstatus,a.id,Category,'' Action FROM examcategories a"
			 		+ " where school='"+LoginController.getschoo()+"' ",1, 0, 0,0);
		   
			
	     return accountsmodel.jsondata;
	 }
	
	 	
	 
	 @RequestMapping(value = "/api/exam/getexamcategory", method = RequestMethod.POST)
	 	@ResponseBody
	 	public  String  getexamcategory(@RequestBody  JSONObject  search, HttpServletRequest request) {		    
	 		
	 		jsontosql.jsontosqlaction("select id,Category from examcategories where ", search, 1,"");
		 		
	 		 
	 		return accountsmodel.jsondata.replace("[", "").replace("]", "");	 			
	 		
		}
		

	 	@SuppressWarnings("unchecked")
		@PostMapping(value = "/api/exam/addexamcategory")
	 	@ResponseBody
	 	
		public  String  addexamcategory(@RequestBody  JSONObject  search, HttpServletRequest request) {		    
	 			
	 		search.put("school", LoginController.getschoo());
			
	 		jsontosql.jsontosqlaction("examcategories", search, 2,"");

	 		if(accountsmodel.response.equals("an error occured")) {
	 			  return accountsmodel.jsondata="[{\"querystatus\" : \""+accountsmodel.response+"\"}]";
	 		}
	 		
	 		
	
			accountsmodel.dbaction("SELECT '"+accountsmodel.response+"' as querystatus,0 as checkstatus,b.* from ("
					+ " SELECT id,Category,'' Action  from examcategories  where "
					+ "school='"+LoginController.getschoo()+"' ORDER BY id DESC LIMIT 1 )b ",1, 0, 0,0);
			
	 		return accountsmodel.jsondata;

		}
	 
	 	@PostMapping(value = "/api/exam/updateexamcategory")
	 	@ResponseBody
	 	
		public  String  updateexamcategory(@RequestBody  JSONObject  search, HttpServletRequest request) {		    
	 			
	 		jsontosql.jsontosqlaction("examcategories", search, 3,"");			 			

	 		if(accountsmodel.response.equals("an error occured")) {
	 			  return accountsmodel.jsondata="[{\"querystatus\" : \""+accountsmodel.response+"\"}]";
	 		}
	 		
	 		
			accountsmodel.dbaction("SELECT '"+accountsmodel.response+"' as querystatus,0 as checkstatus,b.* from "
					+ " (SELECT id,category,'' Action  from examcategories  where"
	       			+ " id='"+search.get("id").toString()+"')b ",1, 0, 0,0);
			
			return accountsmodel.jsondata;	 			
		 	
		}
	 	
	 	
	 	@PostMapping(value = "/api/exam/deleteexamcategory")
	 	
		@ResponseBody
	 	public  String  deleteexamcategory(@RequestBody  JSONObject  search, HttpServletRequest request) {		    
	 			
	 		jsontosql.jsontosqlaction("delete from examcategories where ", search, 4,"");
			
			return accountsmodel.jsondata="[{\"querystatus\" : \""+accountsmodel.response+"\"}]";
			 			
	 		
		}
	 
	 	
	 	
	 	
	 	@RequestMapping(value="/subjectgroups", method=RequestMethod.POST)
			public String SubjectGroups() {
	 		
	 		 if(LoginController.getloggedinuseridtype()!=2) {
					return "fragments/exam :: modalerror";        
				}else {
					return "fragments/exam :: subjectgroups";        
				}
	 		
		 }	
	 	 
		 @RequestMapping(value = "/api/exam/loadsubjectgroups" , method = RequestMethod.GET)
		 @ResponseBody
		 public String listofSubjectgroups() {
			 
			 
			 accountsmodel.dbaction(" SELECT '0' as querystatus,0 as checkstatus,a.id,`Subject group`,"
		 				+ " '' Action FROM subjectgroups a where school='"+LoginController.getschoo()+"' ",1, 0, 0,0);
	 		   
			 
			return accountsmodel.jsondata;
		 }
		
		 
		 
		 @RequestMapping(value="/subjects", method=RequestMethod.POST)
		 public String Subjects(Model model) {
			
			 
			 accountsmodel.dbaction("select no,class from studentclass where "
						+ " class!='Transfered' AND class!='Allumni' and school='"+LoginController.getschoo()+"'"
						+ " UNION ALL  SELECT 'All' AS no,'All' AS class",1, 0, 0,0);
	 		   
			 
				List<accountsmodel> newList111 = new ArrayList<accountsmodel>(accountsmodel.datanew);		
				model.addAttribute("classeswithall", newList111); 
				
				if(LoginController.getloggedinuseridtype()!=2) {
				
					
					return "fragments/exam :: modalerror";        
					
				}else {
				
					return "fragments/exam :: subjects";        
					
				}
				
				
			 	
		 }	
		 
		 
		 
		 
		 @RequestMapping(value = "/api/exam/loadsubjects" , method = RequestMethod.GET)
		 @ResponseBody
		 public String listofSubjects() {
			 
			 accountsmodel.dbaction(" SELECT '0' as querystatus,0 as checkstatus,a.subid,CODE,Sub, "
		 				+ " s.Class,`Subject group`,'' Action FROM subjects1 a "
		 				+ " INNER JOIN subjectgroups g ON g.id=a.Category "
		 				+ " INNER JOIN studentclass s ON NO=a.Class "
		 				+ " where a.school='"+LoginController.getschoo()+"' GROUP BY sub,a.class  order by class,`order`",1, 0, 0,0);
	 		   
		     return accountsmodel.jsondata;
		 }
		 
		 
		 @RequestMapping(value = "/api/exam/loadsubjectsbyclass" , method = RequestMethod.POST)
		 @ResponseBody
		 public String listofSubjectsbyclass(@RequestBody  JSONObject  search, HttpServletRequest request) {
			 
			 if(search.get("class").equals("All")) {
				 
				 
				 accountsmodel.dbaction(" SELECT '0' as querystatus,0 as checkstatus,a.subid,CODE,Sub, "
			 				+ " s.Class,`Subject group`,'' Action FROM subjects1 a "
			 				+ " INNER JOIN subjectgroups g ON g.id=a.Category "
			 				+ " INNER JOIN studentclass s ON NO=a.Class "
			 				+ " where a.school='"+LoginController.getschoo()+"'  GROUP BY sub,class  order by class,`order` ",1, 0, 0,0);
		 		   
				 
			 }else {
				 
				 
				 
				 accountsmodel.dbaction(" SELECT '0' as querystatus,0 as checkstatus,a.subid,CODE,Sub, "
			 				+ " s.Class,`Subject group`,'' Action FROM subjects1 a "
			 				+ " INNER JOIN subjectgroups g ON g.id=a.Category "
			 				+ " INNER JOIN studentclass s ON NO=a.Class "
			 				+ " where a.school='"+LoginController.getschoo()+"' "
			 				+ " and a.class='"+search.get("class")+"' GROUP BY sub  order by class,`order` ",1, 0, 0,0);
		 		   
			 }
			 
			 	
		     return accountsmodel.jsondata;
		 }
		
		 
		 
		 
		 @RequestMapping(value = "/api/exam/getsubject", method = RequestMethod.POST)
		 	@ResponseBody
		 	public  String  getsubject(@RequestBody  JSONObject  search, HttpServletRequest request) {		    
		 	 
		 		
		 		accountsmodel.dbaction(" SELECT * from (SELECT '"+accountsmodel.response+"' as querystatus,"
		 				+ " 0 as checkstatus,a.subid,code,sub, "
		 				+ " class,category,'' Action FROM subjects1 a "
		 				+ " where a.school='"+LoginController.getschoo()+"' "
		 				+ " and subid='"+search.get("id")+"' order by class,`order`)k ",1, 0, 0,0);
		 		   
			 		
		 		return accountsmodel.jsondata.replace("[", "").replace("]", "");	 			
		 		
			}
			
		 
		 
		 
		 @SuppressWarnings("unchecked")
			@PostMapping(value = "/api/exam/addsubject")
		 	@ResponseBody
		 	public  String  addsubject(@RequestBody  JSONObject  search, HttpServletRequest request) {		    
		 			
		 		search.put("school", LoginController.getschoo());
	 			
		 		jsontosql.jsontosqlaction("subjects1", search, 2,"");

		 		if(accountsmodel.response.equals("an error occured")) {
		 			  return accountsmodel.jsondata="[{\"querystatus\" : \""+accountsmodel.response+"\"}]";
		 		}
		 		
		 		
	 		
	 			accountsmodel.dbaction(" SELECT '"+accountsmodel.response+"' as querystatus,0 as checkstatus,a.subid,CODE,Sub, "
		 				+ " s.Class,`Subject group`,'' Action FROM subjects1 a "
		 				+ " INNER JOIN subjectgroups g ON g.id=a.Category "
		 				+ " INNER JOIN studentclass s ON NO=a.Class "
		 				+ " where a.school='"+LoginController.getschoo()+"' ORDER BY subid DESC LIMIT 1  ",1, 0, 0,0);
		 		   
		 		return accountsmodel.jsondata;

			}
		 
		 	@PostMapping(value = "/api/exam/updatesubject")
		 	
			@ResponseBody
		 	public  String  updatesubject(@RequestBody  JSONObject  search, HttpServletRequest request) {		    
		 			
		 		search.put("subid", search.get("id"));
		 		search.remove("id");
		 		
		 		System.out.println(search);
			 
		 		jsontosql.jsontosqlaction("subjects1", search, 3,"");

		 		if(accountsmodel.response.equals("an error occured")) {
		 			  return accountsmodel.jsondata="[{\"querystatus\" : \""+accountsmodel.response+"\"}]";
		 		}
		 		
		 		
	 			
	 			accountsmodel.dbaction(" SELECT '"+accountsmodel.response+"' as querystatus,0 as checkstatus,a.subid,CODE,Sub, "
		 				+ " s.Class,`Subject group`,'' Action FROM subjects1 a "
		 				+ " INNER JOIN subjectgroups g ON g.id=a.Category "
		 				+ " INNER JOIN studentclass s ON NO=a.Class "
		 				+ " where a.school='"+LoginController.getschoo()+"' and subid='"+search.get("subid")+"'  ",1, 0, 0,0);
		 		   
	 			return accountsmodel.jsondata;	 			
	 		 	
			}
		 	
		 	
		 	@PostMapping(value = "/api/exam/deletesubject")
		 	@ResponseBody
		 	
			public  String  deletesubject(@RequestBody  JSONObject  search, HttpServletRequest request) {		    
		 			
		 		search.put("subid", search.get("id"));
		 		search.remove("id");
		 		
		 		jsontosql.jsontosqlaction("delete from subjects1 where ", search, 4,"");
	 			
	 			return accountsmodel.jsondata="[{\"querystatus\" : \""+accountsmodel.response+"\"}]";
	 			 			
		 		
			}
		 
		 
		 
		 
		 
		 
		 @RequestMapping(value = "/api/exam/getsubjectgroup", method = RequestMethod.POST)
		 	@ResponseBody
		 	public  String  getsubjectgroup(@RequestBody  JSONObject  search, HttpServletRequest request) {		    
		 		
		 		jsontosql.jsontosqlaction("select * from subjectgroups where ", search, 1,"");
			 		
		 		return accountsmodel.jsondata.replace("[", "").replace("]", "");	 			
		 		
			}
			
		 
		 
		 
		    @SuppressWarnings("unchecked")
			@PostMapping(value = "/api/exam/addsubjectgroup")
		 	@ResponseBody
		 	
			public  String  addsubjectgroup(@RequestBody  JSONObject  search, HttpServletRequest request) {		    
		 			
		 		search.put("school", LoginController.getschoo());
	 			
		 		jsontosql.jsontosqlaction("subjectgroups", search, 2,"");

		 		if(accountsmodel.response.equals("an error occured")) {
		 			  return accountsmodel.jsondata="[{\"querystatus\" : \""+accountsmodel.response+"\"}]";
		 		}
		 		
		 		
	 		
	 			accountsmodel.dbaction("SELECT '"+accountsmodel.response+"' as querystatus,0 as checkstatus,a.id,`Subject group`,'' Action  "
	 	 				+ "FROM subjectgroups a  where  school='"+LoginController.getschoo()+"'   ORDER BY id DESC LIMIT 1",1, 0, 0,0);
		 	
	 			
		 		return accountsmodel.jsondata;

			}
		 
		 	@PostMapping(value = "/api/exam/updatesubjectgroup")
		 	@ResponseBody
		 	
			public  String  updatesubjectgroup(@RequestBody  JSONObject  search, HttpServletRequest request) {		    
		 			
		 		jsontosql.jsontosqlaction("subjectgroups", search, 3,"");
		 		
	 		
	 			accountsmodel.dbaction("SELECT '"+accountsmodel.response+"' as querystatus,"
	 					+ "0 as checkstatus,a.id,`Subject group`,'' Action  "
	 					+ " FROM subjectgroups a  where id='"+search.get("id").toString()+"' ",1, 0, 0,0);
		 	
	 			return accountsmodel.jsondata;	 			
	 		 	
			}
		 	
		 	
		 	@PostMapping(value = "/api/exam/deletesubjectgroup")
		 	@ResponseBody
		 	
			public  String  deletesubjectgroup(@RequestBody  JSONObject  search, HttpServletRequest request) {		    
		 			
		 		jsontosql.jsontosqlaction("delete from subjectgroups where ", search, 4,"");
	 			
	 			return accountsmodel.jsondata="[{\"querystatus\" : \""+accountsmodel.response+"\"}]";
	 			 			
		 		
			}
		 
		 
		 
		 
		 
		 	
		 	
		 	
		 	
		 	
		 	
		 	
		 	@RequestMapping(value="/grades", method=RequestMethod.POST)
			public String Grades() {
		 		
		 		if(LoginController.getloggedinuseridtype()!=2) {
				
					
					return "fragments/exam :: modalerror";        
					
				}else {
				
					return "fragments/exam :: grades";        
					
				}
		 		
			 }	 
		 	
			 
		 	 @RequestMapping(value = "/api/exam/loadgrades" , method = RequestMethod.GET)
			 @ResponseBody
			 public String listofgrades() {
		 		 
		 		
		 		accountsmodel.dbaction("SELECT '0' as querystatus,0 as checkstatus,a.id,Grade,'' Action "
		 				+ " FROM grades a where school='"+LoginController.getschoo()+"'",1, 0, 0,0);
		 		   
			     return accountsmodel.jsondata;
			 }
			 
			 	@RequestMapping(value = "/api/exam/getgrade", method = RequestMethod.POST)
			 	@ResponseBody
			 	public  String  getgrade(@RequestBody  JSONObject  search, HttpServletRequest request) {		    
			 		
				 jsontosql.jsontosqlaction("select id,Grade from grades where ", search, 1,"");
			 		
			 		return accountsmodel.jsondata.replace("[", "").replace("]", "");	 			
			 		
			 }
			 
			 	
			 	@SuppressWarnings("unchecked")
				@PostMapping(value = "/api/exam/addgrade")
			 	@ResponseBody
			 	
				public  String  addgrade(@RequestBody  JSONObject  search, HttpServletRequest request) {		    
			 			
			 		search.put("school", LoginController.getschoo());
		 			
			 		jsontosql.jsontosqlaction("grades", search, 2,"");

			 		if(accountsmodel.response.equals("an error occured")) {
			 			  return accountsmodel.jsondata="[{\"querystatus\" : \""+accountsmodel.response+"\"}]";
			 		}
			 		
			 		
		 			accountsmodel.dbaction("SELECT '"+accountsmodel.response+"' as querystatus,0 as checkstatus,a.id,Grade,'' Action  "
		 					+ " FROM grades a where school='"+LoginController.getschoo()+"' ORDER BY id DESC LIMIT 1",1, 0, 0,0);
			 		
			 		return accountsmodel.jsondata;

				}
			 
			 	@PostMapping(value = "/api/exam/updategrade")
			 	@ResponseBody
			 	
				public  String  updategrade(@RequestBody  JSONObject  search, HttpServletRequest request) {		    
			 			
			 		jsontosql.jsontosqlaction("grades", search, 3,"");

			 		if(accountsmodel.response.equals("an error occured")) {
			 			  return accountsmodel.jsondata="[{\"querystatus\" : \""+accountsmodel.response+"\"}]";
			 		}
			 		
			 		
		 			jsontosql.jsontosqlaction("SELECT '"+accountsmodel.response+"' as querystatus,0 as checkstatus,"
		 					+ " a.id,Grade,'' Action  FROM grades a where", search, 1 ,"");
			 
				 		
		 			return accountsmodel.jsondata;	 			
		 		 	
				}
			 	
			 	
			 	@PostMapping(value = "/api/exam/deletegrade")
			 	@ResponseBody
			 	
				public  String  deletestudent(@RequestBody  JSONObject  search, HttpServletRequest request) {		    
			 			
			 		jsontosql.jsontosqlaction("delete from grades where ", search, 4,"");
		 			
		 			return accountsmodel.jsondata="[{\"querystatus\" : \""+accountsmodel.response+"\"}]";
		 			 			
			 		
				}
			 
		 	
			 	String grade="";
			 	String classs="";
			 	
		 	
		 	
		 	
		 	
		 	@RequestMapping(value="/remarks", method=RequestMethod.POST)
			 public String remarks(Model model) {
		 		
		 		
		 		accountsmodel.dbaction("SELECT id,`Subject group` FROM subjectgroups where school='"+LoginController.getschoo()+"'"
		 				+ " UNION ALL "
		 				+ " SELECT 'Principal','Principal/Director/Rector' "
		 				+ " UNION ALL "
		 				+ " SELECT 'Teachers','Class teachers'",1, 0, 0,0);
		 		   
		 		
				List<accountsmodel> newList1111newListofclassesreport = new ArrayList<accountsmodel>(accountsmodel.datanew);		
				model.addAttribute("subjectgroups", newList1111newListofclassesreport); 	
			
					
				accountsmodel.dbaction(" SELECT a.id,concat(`First name`,' ',`Second name`,' ',surname,'   -    ',"
						+ " c.class,' ',' ',d.stream) teacher  FROM teacherregistration a"
						+ " INNER JOIN classteachers b ON a.id=b.Teacher"
						+ " INNER JOIN studentclass c ON b.Class=c.`no`"
						+ " INNER JOIN studentstreams d ON b.Stream=d.streanid "
						+ " where a.school='"+LoginController.getschoo()+"' ",1, 0, 0,0);
		 		   
				List<accountsmodel> classteacher = new ArrayList<accountsmodel>(accountsmodel.datanew);		
				model.addAttribute("classteacher", classteacher);  
				
				
				if(LoginController.getloggedinuseridtype()!=2) {
				
					
					return "fragments/exam :: modalerror";        
					
				}else {
				
					return "fragments/exam :: remarks";        
					
				}
		 		
			 }	
		 	
		 	
		 	@RequestMapping(value = "/api/exam/loadremarks")
			 @ResponseBody
			 public String listofremarks(@RequestBody  JSONObject  search, HttpServletRequest request) {			 				 
				 
				System.out.println(search);  
		 		
					 grade=search.get("category").toString();	
					 
					 if(grade.equals("Principal")) {
						 
										 		
						 accountsmodel.dbaction(" SELECT * from(SELECT '0' as querystatus,0 as checkstatus,a.id ,c.Grade,Remark,'' "
					 				+ " Action FROM principalremarks a INNER JOIN grades c ON a.Grade=c.id "
					 				+ " where a.school='"+LoginController.getschoo()+"' ORDER BY c.id )g  ",1, 0, 0,0);
				 		   
						}else if(grade.equals("Teachers")) {
						
							
							accountsmodel.dbaction(" SELECT * from(SELECT '0' as querystatus,0 as checkstatus,a.id , "
							 		+ " concat(s.Class,' ',e.stream) Class,c.Grade,Remark,"
							 		+ " '' Action  FROM classteacherremarks a "
							 		+ " INNER JOIN teacherregistration b ON a.Teacher=b.id  "
							 		+ " INNER JOIN classteachers d ON d.Teacher=b.id "
							 		+ " INNER JOIN studentstreams e ON d.stream=streanid "
							 		+ " INNER JOIN studentclass s ON s.`no`=d.Teacher "
							 		+ " INNER JOIN grades c ON a.Grade=c.id   where a.school='"+LoginController.getschoo()+"' "
							 		+ " and b.id="+search.get("teacher")+" ORDER BY c.id )h ",1, 0, 0,0);
					 		   
											 		
						}else {
							
							
							accountsmodel.dbaction(" SELECT '0' as querystatus,0 as checkstatus,a.id,c.Grade,Remark,"
									+ " '' Action FROM remarks a "
							 		+ " INNER JOIN subjectgroups b ON a.Category=b.id  "
							 		+ " INNER JOIN grades c ON a.Grade=c.id  "
							 		+ " WHERE a.Category= '"+grade+"' and a.school='"+LoginController.getschoo()+"'"
							 		+ " ORDER BY c.id ",1, 0, 0,0);
					 		   
							
						}
					  
					 
				    return accountsmodel.jsondata;
				    
    

			 }
			 
			 	
		 	
		 	
		 	@RequestMapping(value = "/api/exam/getremark", method = RequestMethod.POST)
		 	@ResponseBody
		 	public  String  getremark(@RequestBody  JSONObject  search, HttpServletRequest request) {		    
			 		 
			 		String table="";
			 		
			 		grade=search.get("category").toString();	
					 
			 		
			 		
			 		if(grade.equals("Principal")) {
			 			table="principalremarks";
			 			search.remove("category");				 		
			 		}else if(grade.equals("Teachers")) {
			 			table="classteacherremarks";
			 			search.remove("category");			 				 			
			 		}
			 		else {
			 			
			 			table="remarks";
			 			search.remove("teacher");
			 			
			 		}
			 		
			 			
			 			jsontosql.jsontosqlaction("select *,'' Action  from "+table+" where ", search, 1,"");
				 	
			 		return accountsmodel.jsondata.replace("[", "").replace("]", "");	 			
			 		
			 }
			 
			 	
		 	
		 	
		 	
		 	
			 	@SuppressWarnings("unchecked")
				@PostMapping(value = "/api/exam/addremark")
			 	@ResponseBody
			 	
				public  String  addremark(@RequestBody  JSONObject  search, HttpServletRequest request) {		    
			 			
			 		
			 		String table="";
			 		
			 		grade=search.get("category").toString();
			 		
			 		
			 		if(grade.equals("Principal")) {
			 			
			 			table="principalremarks";
			 			search.remove("category");
			 			search.remove("teacher");
			 			search.remove("Category");		 				 							 	
			 			
			 		}else if(grade.equals("Teachers")) {
			 			
			 			table="classteacherremarks";
			 			search.remove("category");	
			 			search.remove("Category");		 				 							 	
			 			
			 		}
			 		else {
			 			
			 			table="remarks";
			 			search.remove("teacher");
			 			search.remove("Category");		 				 							 	
			 			
			 		}
			 		
					
			 		search.put("school", LoginController.getschoo());
		 			
			 		
			 		jsontosql.jsontosqlaction(table, search, 2,"");

			 		if(accountsmodel.response.equals("an error occured")) {
			 			  return accountsmodel.jsondata="[{\"querystatus\" : \""+accountsmodel.response+"\"}]";
			 		}
			 		
			 		
		 			
		 			if(grade.equals("Principal")) {
		 				
		 				accountsmodel.dbaction(" SELECT * from(SELECT '"+accountsmodel.response+"' as querystatus,"
		 						+ " 0 as checkstatus,a.id,c.Grade,Remark,'' "
								+ " Action FROM principalremarks a "
								+ " INNER JOIN grades c ON a.Grade=c.id   where   c.school='"+LoginController.getschoo()+"'  "
								+ " ORDER BY a.ID DESC LIMIT 1)g  ",1, 0, 0,0);
				 		

			 			
						
		 			}else if(grade.equals("Teachers")) {				 			
		 					
		 				
		 				accountsmodel.dbaction("SELECT * from( SELECT '"+accountsmodel.response+"' as querystatus,"
		 						+ " 0 as checkstatus,a.id ,"
		 						+ " concat(s.Class,' ',e.stream) Class,C.Grade,Remark,'' Action "
		 			 			+ " FROM classteacherremarks a "
		 			 			+ " INNER JOIN teacherregistration b ON a.Teacher=b.id "
		 			 			+ " INNER JOIN grades c ON a.Grade=c.id  "
		 			 			+ " INNER JOIN classteachers d ON d.Teacher=b.id "
		 			 			+ " INNER JOIN studentstreams e ON d.stream=streanid "
			 			 		+ " INNER JOIN studentclass s ON s.`no`=d.Teacher "
			 			 		+ " where a.school='"+LoginController.getschoo()+"' )h "
		 			 			+ " ORDER BY id DESC LIMIT 1",1, 0, 0,0);
				 		
				 
		 				
			 		}else {
			 			
			 			
			 			accountsmodel.dbaction("SELECT '"+accountsmodel.response+"' as querystatus,0 as checkstatus,"
			 					+ " a.id,c.Grade,Remark, '' Action FROM remarks a"
			 					+ " INNER JOIN examcategories b ON a.Category=b.id "
			 					+ " INNER JOIN grades c ON a.Grade=c.id  "
						 		+ " where   c.school='"+LoginController.getschoo()+"'   "
						 		+ " ORDER BY a.id DESC LIMIT 1",1, 0, 0,0);
				 
			 			
			 		}
		 			
			 		return accountsmodel.jsondata;

				}
			 
			 	
			 	
			 	
			 	
			 	@PostMapping(value = "/api/exam/updateremark")
			 	@ResponseBody
			 	
				public  String  updateremark(@RequestBody  JSONObject  search, HttpServletRequest request) {		    
			 			
			 		String table="";
			 		
			 		grade=search.get("category").toString();	
					 
			 		System.out.println(search.get("category").toString());
			 		
			 		if(grade.equals("Principal")) {
			 			table="principalremarks";
			 			search.remove("category");				 		
			 		}else if(grade.equals("Teachers")) {
			 			table="classteacherremarks";
			 			search.remove("category");			 				 			
			 		}
			 		else {			 			
			 			table="remarks";
			 			search.remove("teacher");			 			
			 		}
			 		
			 		
			 		search.remove("Category");		 				 							 	
			 		search.remove("teacher");
		 			
			 		
			 		
			 		jsontosql.jsontosqlaction(table, search, 3,"");

			 		if(accountsmodel.response.equals("an error occured")) {
			 			  return accountsmodel.jsondata="[{\"querystatus\" : \""+accountsmodel.response+"\"}]";
			 		}
			 		
			 		
			 		
			 		
		 			if(grade.equals("Principal")) {
		 				
		 				accountsmodel.dbaction(" SELECT * from(SELECT '"+accountsmodel.response+"' as querystatus,0 as checkstatus,"
		 						+ " a.id,c.Grade,Remark,'' "
				 				+ " Action FROM principalremarks a "
				 				+ " INNER JOIN grades c ON a.Grade=c.id "
				 				+ " where a.school='"+LoginController.getschoo()+"'  "
				 				+ " and a.`id` ='"+search.get("id")+"'  )h ",1, 0, 0,0);
				 	
		 				
						
		 			}else if(grade.equals("Teachers")) {
			 			
		 				accountsmodel.dbaction("SELECT * from(SELECT '"+accountsmodel.response+"' as querystatus,"
	 				 			+ " 0 as checkstatus,a.id ,"
	 				 			+ " concat(s.Class,' ',e.stream) Class,c.Grade,Remark, '' Action  FROM classteacherremarks a "
			 			 		+ " INNER JOIN teacherregistration b ON a.Teacher=b.id  "
			 			 		+ " INNER JOIN classteachers d ON d.Teacher=b.id "
			 			 		+ " INNER JOIN studentstreams e ON d.stream=streanid "
			 			 		+ " INNER JOIN studentclass s ON s.`no`=d.Teacher "
			 			 		+ " INNER JOIN grades c ON a.Grade=c.id "
			 			 		+ " where a.school='"+LoginController.getschoo()+"' "
			 			 		+ " and a.`id` ='"+search.get("id")+"'  )h ",1, 0, 0,0);
				 	
		 				
		 					
			 		}else {
			 			
			 			accountsmodel.dbaction("SELECT '"+accountsmodel.response+"' as querystatus,0 as checkstatus,"
			 					+ " a.id,c.Grade,Remark,'' Action FROM remarks a"
			 					+ " INNER JOIN examcategories b ON a.Category=b.id "
			 					+ " INNER JOIN grades c ON a.Grade=c.id  "
						 		+ " where a.school='"+LoginController.getschoo()+"' and a.`id` ='"+search.get("id")+"'  ",1, 0, 0,0);
				 	
			 			
		 			}		
		 			
			 			
		 			return accountsmodel.jsondata;	 			
		 		 	
				}
			 	
			 	
			 	
			 	
			 	
			 	
			 	
			 	
			 	@PostMapping(value = "/api/exam/deleteremark")
			 	@ResponseBody
			 	public  String  deleteremark(@RequestBody  JSONObject  search, HttpServletRequest request) {		    
			 		
			 		String table="";
			 		
			 		
			 		System.out.println(search.get("category"));
			 		
			 		grade=search.get("category").toString();	
					 
			 		if(grade.equals("Principal")) {
			 			table="principalremarks";
			 			search.remove("category");				 		
			 		}else if(grade.equals("Teachers")) {
			 			table="classteacherremarks";
			 			search.remove("category");			 				 			
			 		}
			 		else {
			 			table="remarks";
			 			search.remove("Teacher");			 			 		
			 		}
			 		
			 		jsontosql.jsontosqlaction("delete from "+table+" where ", search, 4,"");
		 			
		 			return accountsmodel.jsondata="[{\"querystatus\" : \""+accountsmodel.response+"\"}]";
		 			 			
			 		
				}
		 	
		 	
		 	
			 	
			 	
			 	 // gading system	 
			 	
			 	@RequestMapping(value="/gradingsystem", method=RequestMethod.POST)
				 public String gradingsystem(Model model) {			 		
			 		
			 		
			 		
			 		accountsmodel.dbaction("SELECT id,`Subject group` FROM subjectgroups "
			 				+ " where school='"+LoginController.getschoo()+"'  "
			 				+ " UNION ALL "
			 				+ " SELECT 'Overall','Overall' ",1, 0, 0,0);
			 		   
			 		
					List<accountsmodel> newList1111newListofclassesreport = new ArrayList<accountsmodel>(accountsmodel.datanew);		
					model.addAttribute("subjectgroups", newList1111newListofclassesreport); 	
					
					
						
					accountsmodel.dbaction("SELECT id,`Subject group` FROM subjectgroups "
			 				+ " where school='"+LoginController.getschoo()+"'  "
			 				+ " UNION ALL "
			 				+ " SELECT 'Overall','Overall' "
			 				+ " UNION ALL "
			 				+ " SELECT 'ALL','ALL' ",1, 0, 0,0);
			 		   
			 		
					List<accountsmodel> rty = new ArrayList<accountsmodel>(accountsmodel.datanew);		
					model.addAttribute("subjectgroupswithall", rty); 
					accountsmodel.dbaction(" select * from studentclass where  school='"+LoginController.getschoo()+"' "
							+ " and status='Active' ",1, 0, 0,0);
			 		   
					List<accountsmodel> classteacher1 = new ArrayList<accountsmodel>(accountsmodel.datanew);		
					model.addAttribute("activecls", classteacher1);  
					
					
					if(LoginController.getloggedinuseridtype()!=2) {
						
						
						return "fragments/exam :: modalerror";        
						
					}else {
					
						return "fragments/exam :: gradingsystem";        
						
					}
			 		
			 		
				 }	
			 	
			 	
			 	 
			 	 
			 	 
			 	
			 	 @RequestMapping(value = "/api/exam/loadgradingsystem")
				 @ResponseBody
				 public String listofgradingsystem(@RequestBody  JSONObject  search, HttpServletRequest request) {			 				 
					 
					 grade=search.get("category").toString();	
					 classs=search.get("classs").toString();	
					 
					 if(grade.equals("Overall")) {
				 			
						 
						 accountsmodel.dbaction(" SELECT * from (SELECT '0' as querystatus,0 as checkstatus,a.id,c.Grade,Start,"
				 			 		+ " End,'' Action FROM overallgradingsystem a "
				 			 		+ " INNER JOIN grades c ON a.Grade=c.id  "
				 			 		+ " where a.school='"+LoginController.getschoo()+"' and a.class='"+classs+"'  "
				 			 		+ " ORDER BY c.id)f",1, 0, 0,0);
				 		   
											 		
				 		}else {
				 			
				 			
				 			
				 			accountsmodel.dbaction(" SELECT * from (SELECT '0' as querystatus,0 as checkstatus,a.id,c.Grade,START, "
				 			 		+ " End,'' Action FROM gradingsystem a  "
				 			 		+ " INNER JOIN subjectgroups b ON a.Category=b.id   "
				 			 		+ " INNER JOIN grades c ON a.Grade=c.id   "
				 			 		+ " where a.school='"+LoginController.getschoo()+"' and "
				 			 		+ " a.Category='"+grade+"' and class='"+classs+"'  ORDER BY c.id)g ",1, 0, 0,0);
					 		   
							
				 		}
					  
					 
				     return accountsmodel.jsondata;
				     
				     
				 }
			 	
			 	
			 	 
			 	 
				
				 
					@RequestMapping(value = "/api/exam/getgradingsystem", method = RequestMethod.POST)
				 	@ResponseBody
				 	public  String  getgradingsystem(@RequestBody  JSONObject  search, HttpServletRequest request) {		    
				 		
						String table="";
						
						System.out.println(search);
						
				 		 if(grade.equals("Overall")) {
				 			search.remove("Category");
				 			table="overallgradingsystem";
				 			search.remove("classs");
				 			search.remove("category");
				 		}
				 		else {
				 			table="gradingsystem";
				 			search.remove("classs");
				 			search.remove("category");
				 		}
				 		
				 		
				 		jsontosql.jsontosqlaction("select *,'' Action  from "+table+" where ", search, 1,"");
			 			
			 			return accountsmodel.jsondata.replace("[", "").replace("]", "");	 			
				
					}
					

				 	@SuppressWarnings("unchecked")
					@PostMapping(value = "/api/exam/addgradingsystem")
				 	@ResponseBody
				 	public  String  addgradingsystem(@RequestBody  JSONObject  search, HttpServletRequest request) {		    
				 		
				 		String table="";
						
				 		 if(grade.equals("Overall")) {
				 			 
				 			search.remove("Category");
				 			search.remove("category");
				 			table="overallgradingsystem";
					 		
				 		}
				 		 
				 		else {
				 			
				 			table="gradingsystem";
				 			search.remove("Category");
				 			
				 		}
				 		 
				 		
				 		
			 			search.put("school", LoginController.getschoo());
			 			
			 			jsontosql.jsontosqlaction(table, search, 2,"");

				 		if(accountsmodel.response.equals("an error occured")) {
				 			  return accountsmodel.jsondata="[{\"querystatus\" : \""+accountsmodel.response+"\"}]";
				 		}
				 		
				 		
			 			if(grade.equals("Overall")) {				 			
		 					
			 				
			 				accountsmodel.dbaction(" SELECT '"+accountsmodel.response+"' as querystatus,"
			 						+ " 0 as checkstatus,a.id,c.Grade,Start, End,'' Action FROM overallgradingsystem a "
				 			 		+ " INNER JOIN grades c ON a.Grade=c.id   where c.school='"+LoginController.getschoo()+"'"
				 			 		+ " ORDER BY id DESC LIMIT 1 ",1, 0, 0,0);
					 		   
				 			
			 				
				 		}else {
				 			
				 			
				 			accountsmodel.dbaction(" SELECT '"+accountsmodel.response+"' as querystatus,"
			 						+ " 0 as checkstatus,a.id,c.Grade,START,End,'' Action FROM gradingsystem a  "
				 			 		+ " INNER JOIN examcategories b ON a.Category=b.id   "
				 			 		+ " INNER JOIN grades c ON a.Grade=c.id   "
				 			 		+ " where a.school='"+LoginController.getschoo()+"' ORDER BY a.id DESC LIMIT 1",1, 0, 0,0);
					 		   
				 			
				 		}
				 
			 				
			 	
			 			
				 		return accountsmodel.jsondata;

					}
				 	
				 	
				 
				 	@PostMapping(value = "/api/exam/updategradingsystem")
				 	@ResponseBody
				 	public  String  updatesgradingsystem(@RequestBody  JSONObject  search, HttpServletRequest request) {		    
				 			
				 		String table="";
				 		
				 		
				 		 if(grade.equals("Overall")) {
				 			 
				 			search.remove("Category");
				 			table="overallgradingsystem";
				 			search.remove("classs");
				 			search.remove("category");
				 			
				 		}
				 		else {
				 			
				 			table="gradingsystem";
				 			search.remove("classs");
				 			search.remove("Class");
				 			search.remove("Category");	
				 			
				 		}
				 		
				 		String id=search.get("id").toString();
				 		

			 			jsontosql.jsontosqlaction(table, search, 3,"");

				 		if(accountsmodel.response.equals("an error occured")) {
				 			  return accountsmodel.jsondata="[{\"querystatus\" : \""+accountsmodel.response+"\"}]";
				 		}
				 		
				 		
			 			
			 			if(grade.equals("Overall")) {
		 					
			 				
			 				accountsmodel.dbaction(" SELECT '"+accountsmodel.response+"' as querystatus,"
			 						+ " 0 as checkstatus,a.id,c.Grade,Start,"
				 			 		+ " End,'' Action FROM overallgradingsystem a "
				 			 		+ " INNER JOIN grades c ON a.Grade=c.id  "
				 			 		+ " where  a.`id` ='"+id+"' ",1, 0, 0,0);
					 	
			 				
			 					
				 		}else {
				 			
				 			accountsmodel.dbaction(" SELECT '"+accountsmodel.response+"' as querystatus,"
			 						+ " 0 as checkstatus,a.id,c.Grade,START,End,'' Action FROM gradingsystem a  "
				 			 		+ " INNER JOIN subjectgroups b ON a.Category=b.id   "
				 			 		+ " INNER JOIN grades c ON a.Grade=c.id   "
							 		+ " where  a.`id` ='"+id+"'  ",1, 0, 0,0);
					 	
				 			
			 			
			 			}		
			 					
			 		
			 				
			 			return accountsmodel.jsondata;	 			
			 		 	
					}
				 	
				 	
				 	
				 	
				 	
				 	
				 	
				 	
				 	
				 	@PostMapping(value = "/api/exam/deletegradingsystem")
				 	@ResponseBody
				 	public  String  deletegradingsystem(@RequestBody  JSONObject  search, HttpServletRequest request) {		    
				 			
				 		String table="";
						
				 		 if(grade.equals("Overall")) {
				 			search.remove("Category");
				 			table="overallgradingsystem";
					 		
				 		}
				 		else {
				 			table="gradingsystem";
				 		}
				 		
				 		
				 		jsontosql.jsontosqlaction("delete from "+table+" where ", search,4,"");
			 			
			 			return accountsmodel.jsondata="[{\"querystatus\" : \""+accountsmodel.response+"\"}]";
			 		
					}
				 

				 
				 	
				 	
			 	
			 	
			 	
				 	
			 	
				   @RequestMapping(value="/moresettings", method=RequestMethod.POST)
					public String gotomoresettings(Model model) { 	   		 
						
						
						accountsmodel.dbaction("select * from studentclass  where school='"+LoginController.getschoo()+"' and "
								+ " class!='Transfered' AND class!='Allumni'",1, 0, 0,0);
				 		   
						List<accountsmodel> newList1 = new ArrayList<accountsmodel>(accountsmodel.datanew);		
						model.addAttribute("classes", newList1); 
						
						
						if(LoginController.getloggedinuseridtype()!=2) {
							
							
							return "fragments/exam :: modalerror";        
							
						}else {
						
							return "fragments/exam :: moresettings";        
							
						}
						
				 		
			      }	 	
				 
				 
				 
			 	
				 @RequestMapping(value = "/api/exam/loadmoresettings")
				 @ResponseBody
				 public String listmoresettinfs(@RequestBody  JSONObject  search, HttpServletRequest request) {			 				 
					 
					 accountsmodel.dbaction("SELECT id,nexttermddates,midtermdate,clinicdates,  "
					 			+ "  cloasingdates FROM analysisformulaandreportformdatesdates "
					 			+ "  WHERE class="+search.get("classs")+" and school='"+LoginController.getschoo()+"' ",1, 0, 0,0);
				 	
					 		
				     return accountsmodel.jsondata;
				     
				 }
			 	
			 	
			 	
			 	
				 @RequestMapping(value = "/api/exam/savereportformdates", method = RequestMethod.POST)
				 @ResponseBody				 
				 public String savemoresettinfs(@RequestBody  JSONObject  search, HttpServletRequest request) {			 				 
					 
					 Connection conn=null;
					 try {
						 conn = DbConnector.dataSource.getConnection();
			 		 		
						PreparedStatement pstmt= conn.prepareStatement("update analysisformulaandreportformdatesdates set"
 			 					+ " nexttermddates=?, "
 			 					+ " midtermdate=?, "
 			 					+ " clinicdates=?, "
 			 					+ " cloasingdates=? " 			 					
 			 					+ " WHERE school='"+LoginController.getschoo()+"' and class=?"); 
						
						pstmt.setString(1,search.get("nexttermddates").toString());      
						pstmt.setString(2,search.get("midtermdate").toString());      
						pstmt.setString(3,search.get("clinicdates").toString());
						pstmt.setString(4,search.get("cloasingdates").toString());
						pstmt.setString(5,search.get("classs").toString());						 
						
						pstmt.execute();
						pstmt.close(); 
						
						String respo="Operation successful";
						
					 accountsmodel.jsondata="[{\"querystatus\" : \""+respo+"\"}]";
	 			 			
					 } catch (Exception e1) {
							e1.printStackTrace(); 
							accountsmodel.response="an error occured"; return accountsmodel.jsondata="[{\"querystatus\" : \""+accountsmodel.response+"\"}]";
					 }finally {
						    if (conn != null) try { conn.close(); } catch (SQLException ignore) {}
					  }	
					 
					 
				     return accountsmodel.jsondata;
				     
				 }
			 	
	 	
	 	

			@RequestMapping(value="/listofexams", method=RequestMethod.POST)
			public String gototimetable(Model model) {  	    		 
			 
				if(LoginController.getloggedinuseridtype()!=2) {
					return "fragments/exam :: modalerror";        
				}else {
					
					
					accountsmodel.dbaction("select no,class from studentclass where "
							+ " class!='Transfered' AND class!='Allumni' and school='"+LoginController.getschoo()+"'"
							+ " UNION ALL  SELECT 'All' AS no,'All' AS class",1, 0, 0,0);
			 		   
					List<accountsmodel> newList111 = new ArrayList<accountsmodel>(accountsmodel.datanew);		
					model.addAttribute("classeswithall", newList111);      	
					
					
					accountsmodel.dbaction("SELECT id,sem FROM schoolsemesters where school='"+LoginController.getschoo()+"' "
							+ " UNION ALL "
							+ " SELECT 'All' AS id,'All' AS sem ",1, 0, 0,0);
			 		   
					List<accountsmodel> newList1111 = new ArrayList<accountsmodel>(accountsmodel.datanew);		
					model.addAttribute("termwithall", newList1111);      	
					
					return "fragments/exam :: listofexams";        
				}
				
				
	      }
		 
		 
		 
		 @RequestMapping(value = "/api/exam/loadlistofexams" , method = RequestMethod.GET)
		 @ResponseBody
		 public String loadlistofexams() {
			 
			 
			 	
			 	accountsmodel.dbaction("SELECT * from (SELECT '0' as querystatus,0 as checkstatus,"
						+ " a.id,'List of exams' as title,`Exam Name`,s.Class,m.sem AS Term,"
						+ " Performance,DATE,a.Status,`Sms status`,'' AS Action FROM allexams a"
						+ " INNER JOIN studentclass s ON a.Class=s.no"
						+ " INNER JOIN schoolsemesters m ON a.term=m.id  "
						+ " where a.school='"+LoginController.getschoo()+"' ORDER BY a.id)g",1, 0, 0,0);
		 		   
			 	
			 	
			 	return accountsmodel.jsondata;
		     
		     
		 }
			 
		 
		 @RequestMapping(value = "/api/exam/getexam", method = RequestMethod.POST)
		 	@ResponseBody
		 	public  String  getstudent(@RequestBody  JSONObject  search, HttpServletRequest request) {		    

			 	jsontosql.jsontosqlaction("select * from (select id,date as examdate,term as examterm,"
			 			+ "  Class as examclass,category as examcategory,"
			 			+ "  case when customname=1 then `Exam name` else '' end as customexamname"
			 			+ "  from allexams  where school='"+LoginController.getschoo()+"' )d where ", search, 1,"");
			 		
		 		return accountsmodel.jsondata.replace("[", "").replace("]", "");	 			
		 		
			}
			
		 

		 	@SuppressWarnings("unchecked")
			@PostMapping(value = "/api/exam/addexam")
		 	@ResponseBody
		 	public  String  addExam(@RequestBody  JSONObject  search, HttpServletRequest request) {		    
		 			 					
	 					
	 					if(search.get("includeallclass")!=null) {
	 						
	 						if(search.get("customexamname")!=null) {
	 							
	 							String exnme=search.get("examname").toString();
	 							
	 							search.remove("includeallclass");
	 							
	 							Connection conn=null;
		 						try {
		 							
		 							conn.prepareStatement("start transaction").execute();
		 							
		 							
		 							conn = DbConnector.dataSource.getConnection();
		 				 		 	
		 							ResultSet rt = conn.prepareStatement("SELECT *  FROM `studentclass`  "
		 									+ " where school='"+LoginController.getschoo()+"' and "
											+ " status='Active' ORDER BY `no` ASC").executeQuery();
		 							
									while (rt.next()) {											
								
											accountsmodel.liststring.clear();
											
											search.remove("Class");
											
											search.put("Class", rt.getString("no"));
											search.put("customname", 0);
									 		
											
											String Examname="";
					 			 			
											PreparedStatement pstmt= conn.prepareStatement(
													  " SELECT concat(upper(class),' ','"+exnme+"') "
							 					    + " AS Examname FROM studentclass   "
							 						+ " where school='"+LoginController.getschoo()+"' and NO=?"); 
											
											pstmt.setString(1,search.get("Class").toString());      
											ResultSet rs = pstmt.executeQuery();         
											while (rs.next()) { 
												Examname=rs.getString("Examname");	
											}
					 			 			
					 			 			
					 			 			search.remove("examname");
					 			 			search.remove("customexamname");	 						
					 						
					 						
					 						search.put("Exam name", Examname);
									 		search.put("Status", "Open");
									 		search.put("Sms status", "Not Sent");
									 		search.put("customname", "1");
									 		
									 		
									 		
									 		search.put("school", LoginController.getschoo());								 			
									 		jsontosql.jsontosqlaction("allexams", search, 2,"");

									 		if(accountsmodel.response.equals("an error occured")) {
									 			  return accountsmodel.jsondata="[{\"querystatus\" : \""+accountsmodel.response+"\"}]";
									 		}
									 		
									 		
									 		
									 		
									 		pstmt= conn.prepareStatement("INSERT ignore INTO `exams` (`studentid`,`exam`,`sub`)  "
								 					+ " SELECT id,(select id from allexams where `exam name`='"+Examname+"'),"
								 					+ " subid  FROM `subjects1` "
								 					+ " INNER JOIN registration r on `current form`=class "
								 					+ " WHERE r.school=?  and  `current form`=?"
								 					+ " ORDER BY `id`,`ORDER` ASC"); 
								 			
											pstmt.setInt(1,LoginController.getschoo()); 
											pstmt.setString(2,search.get("Class").toString());
											pstmt.execute();
											pstmt.close(); 
											
										            
										    pstmt= conn.prepareStatement("INSERT ignore INTO `mainexams`"
										            		+ " (`studentid`,`exam`)  select id,"
										            		+ " (select id from allexams where "
										            		+ " `exam name`='"+Examname+"') from registration "
										            		+ " WHERE school='"+LoginController.getschoo()+"' and `CURRENT FORM`=?"); 
										    pstmt.setString(1,search.get("Class").toString());      
											pstmt.executeUpdate();         
											pstmt.close();          
											
													accountsmodel.dbaction("SELECT * from (SELECT 'Operation successfull' as querystatus,0 as checkstatus,"
															+ " a.id,'List of exams' as title,`Exam Name`,s.class,m.sem AS Term,"
															+ " Performance,DATE,a.Status,`Sms status`,'' AS Action FROM allexams a"
															+ " INNER JOIN studentclass s ON a.Class=s.no"
															+ " INNER JOIN schoolsemesters m ON a.term=m.id  "
															+ " where a.school='"+LoginController.getschoo()+"' ORDER BY a.id)g",1, 0, 0,0);
											 		   
													
													rs.close();                       
													pstmt.close(); 
													
												}
									
									conn.prepareStatement("COMMIT").execute();
					    			
											
				 					} catch (Exception e1) {
										e1.printStackTrace(); accountsmodel.response="an error occured"; return accountsmodel.jsondata="[{\"querystatus\" : \""+accountsmodel.response+"\"}]";
									}finally {
									    if (conn != null) try { conn.close(); } catch (SQLException ignore) {}
									  }	
	 							
	 						
	 							
	 							
		 						return accountsmodel.jsondata;
		 			 			
	 							
	 						}else {

	 							String Examname="";
		 			 			
	 							Connection conn=null;
	 			 				try {	 			 				
	 						
	 			 					conn = DbConnector.dataSource.getConnection();
	 					 		 	
				 						ResultSet rt = conn.prepareStatement("SELECT *  FROM `studentclass` where "
												+ " school='"+LoginController.getschoo()+"' and status='Active' ORDER BY `no` ASC").executeQuery();
										while (rt.next()) {											
											
											accountsmodel.liststring.clear();
											
											search.remove("Class");
											
											search.put("Class", rt.getString("no"));
									 		
											
											PreparedStatement pstmt= conn.prepareStatement("SELECT b.* from (SELECT "
													+ " concat(upper(class),' ',upper(sem),' ',upper(e.Category),"
					 			 					+ " ' EXAM') AS Examname FROM studentclass "
					 			 					+ " INNER JOIN schoolsemesters s ON s.id=?"
					 			 					+ " INNER JOIN examcategories  e ON e.id=?"
					 			 					+ " WHERE studentclass.school='"+LoginController.getschoo()+"' and NO=?)b"); 
				 							pstmt.setString(1,search.get("Term").toString());      
											pstmt.setString(2,search.get("Category").toString());      
											pstmt.setString(3,search.get("Class").toString()); 
											
											
											ResultSet rs = pstmt.executeQuery();         
											while (rs.next()) { 
											 Examname=rs.getString("Examname")+" "+search.get("Date").toString().split("-")[0];
											}
											
		
											search.remove("customexamname");
											search.remove("includeallclass");
				 							
											
					 			 			
					 			 			accountsmodel.dbaction("SELECT b.* from (SELECT * from allexams "
					 			 					+ " WHERE `exam name` ='"+Examname+"' and "
					 			 					+ " school='"+LoginController.getschoo()+"' )b ",1, 0, 0,0);
					 			 			
					 			 			
					 			 			if(!accountsmodel.liststring.isEmpty()) {
					 			 				
					 			 				accountsmodel.response="An exam with a similar name exists";
					 			 				
					 			 				return accountsmodel.jsondata="[{\"querystatus\" : \""+accountsmodel.response+"\"}]";
					 			 			}
					 						
										
										search.put("Exam name", Examname);
								 		search.put("Status", "Open");
								 		search.put("Sms status", "Not Sent");								 		
								 		search.put("school", LoginController.getschoo());
								 		
								 		jsontosql.jsontosqlaction("allexams", search, 2,"");								 		

								 		if(accountsmodel.response.equals("an error occured")) {
								 			  return accountsmodel.jsondata="[{\"querystatus\" : \""+accountsmodel.response+"\"}]";
								 		}
								 		
								 		
									 		
							 			pstmt= conn.prepareStatement("INSERT INTO `exams` (`studentid`,`exam`,`sub`)  "
							 					+ " SELECT id,(select id from allexams where `exam name`='"+Examname+"'),"
							 					+ " subid  FROM `subjects1` "
							 					+ " INNER JOIN registration r on `current form`=class "
							 					+ " WHERE r.school=?  and  `current form`=?"
							 					+ " ORDER BY `id`,`ORDER` ASC"); 
							 			
										pstmt.setInt(1,LoginController.getschoo()); 
										pstmt.setString(2,search.get("Class").toString());
										pstmt.execute();
										pstmt.close(); 
										
									            
									    pstmt= conn.prepareStatement("INSERT INTO `mainexams`"
									            		+ " (`studentid`,`exam`)  select id,"
									            		+ " (select id from allexams where "
									            		+ " `exam name`='"+Examname+"') from registration "
									            		+ " WHERE school='"+LoginController.getschoo()+"' and `CURRENT FORM`=?"); 
									    pstmt.setString(1,search.get("Class").toString());      
										pstmt.executeUpdate();         
										pstmt.close(); 
											
										
										accountsmodel.dbaction("SELECT * from (SELECT 'Operation successfull' as querystatus,0 as checkstatus,"
												+ " a.id,'List of exams' as title,`Exam Name`,s.class,m.sem AS Term,"
												+ " Performance,DATE,a.Status,`Sms status`,'' AS Action FROM allexams a"
												+ " INNER JOIN studentclass s ON a.Class=s.no"
												+ " INNER JOIN schoolsemesters m ON a.term=m.id  "
												+ " where a.school='"+LoginController.getschoo()+"' ORDER BY a.id)g",1, 0, 0,0);
								 		   
										rs.close();                       
										
						 		
										}
								
	 			 				} catch (Exception e1) {
									e1.printStackTrace(); accountsmodel.response="an error occured"; return accountsmodel.jsondata="[{\"querystatus\" : \""+accountsmodel.response+"\"}]";
								}finally {
								    if (conn != null) try { conn.close(); } catch (SQLException ignore) {}
								  }	
	 			 				
								
								return accountsmodel.jsondata;
	 			 			
	 							
	 						}
	 						
	 					}else {
	 						
	 						if(search.get("customexamname")!=null) {
	 						
	 							String exnme=search.get("examname").toString();
	 							
	 							Connection conn=null;
		 						try {
		 							conn = DbConnector.dataSource.getConnection();
		 				 		 	
		 							String Examname="";
			 			 			
									PreparedStatement pstmt= conn.prepareStatement("SELECT "
													+ " concat(upper(class),' ','"+exnme+"') "
					 								+ " AS Examname FROM studentclass  "
					 								+ " WHERE school='"+LoginController.getschoo()+"'  and NO=?"); 
											pstmt.setString(1,search.get("Class").toString());      
									ResultSet rs = pstmt.executeQuery();         
									while (rs.next()) { 
										Examname=rs.getString("Examname");	
									}
			 			 			
			 			 			search.remove("examname");
			 			 			search.remove("customexamname");	 						
			 						
			 						
			 						search.put("Exam name", Examname);
							 		search.put("Status", "Open");
							 		search.put("Sms status", "Not Sent");
							 		search.put("customname", "1");							 		
							 		search.put("school", LoginController.getschoo());	
							 		
							 		jsontosql.jsontosqlaction("allexams", search, 2,"");

							 		if(accountsmodel.response.equals("an error occured")) {
							 			  return accountsmodel.jsondata="[{\"querystatus\" : \""+accountsmodel.response+"\"}]";
							 		}
							 		
							 		
							 		
							 		
							 		pstmt= conn.prepareStatement("INSERT INTO `exams` (`studentid`,`exam`,`sub`)  "
						 					+ " SELECT id,(select id from allexams where `exam name`='"+Examname+"'),"
						 					+ " subid  FROM `subjects1` "
						 					+ " INNER JOIN registration r on `current form`=class "
						 					+ " WHERE r.school=?  and  `current form`=?"
						 					+ " ORDER BY `id`,`ORDER` ASC"); 
						 			
									pstmt.setInt(1,LoginController.getschoo()); 
									pstmt.setString(2,search.get("Class").toString());
									pstmt.execute();
									pstmt.close(); 
									
								            
								    pstmt= conn.prepareStatement("INSERT INTO `mainexams`"
								            		+ " (`studentid`,`exam`)  select id,"
								            		+ " (select id from allexams where "
								            		+ " `exam name`='"+Examname+"') from registration "
								            		+ " WHERE school='"+LoginController.getschoo()+"' and `CURRENT FORM`=?"); 
								    pstmt.setString(1,search.get("Class").toString());      
									pstmt.executeUpdate();         
									pstmt.close();          
														
								
									accountsmodel.dbaction("SELECT * from (SELECT 'Operation successfull' as querystatus,0 as checkstatus,"
											+ " a.id,'List of exams' as title,`Exam Name`,s.class,m.sem AS Term,"
											+ " Performance,DATE,a.Status,`Sms status`,'' AS Action FROM allexams a"
											+ " INNER JOIN studentclass s ON a.Class=s.no"
											+ " INNER JOIN schoolsemesters m ON a.term=m.id where "
											+ " a.school='"+LoginController.getschoo()+"' ORDER BY a.id)g",1, 0, 0,0);
							 		   
									
									rs.close();                       
									pstmt.close(); 
									
									
				 					} catch (Exception e1) {
										e1.printStackTrace(); accountsmodel.response="an error occured"; return accountsmodel.jsondata="[{\"querystatus\" : \""+accountsmodel.response+"\"}]";
									}finally {
									    if (conn != null) try { conn.close(); } catch (SQLException ignore) {}
									  }		
	 							
	 						
	 							
		 						return accountsmodel.jsondata;
		 			 			
	 							
	 							
	 						}else {
	 							
	 							String Examname="";
	 							
	 							Connection conn=null;
		 						try {
		 							conn = DbConnector.dataSource.getConnection();
		 				 		 	
		 							PreparedStatement pstmt= conn.prepareStatement("SELECT b.* from "
		 									+ " (SELECT concat(upper(class),' ',upper(sem),' ',upper(e.Category),"
			 			 					+ "' EXAM') AS Examname FROM studentclass "
			 			 					+ " INNER JOIN schoolsemesters s ON s.id=?"
			 			 					+ " INNER JOIN examcategories  e ON e.id=?"
			 			 					+ " WHERE studentclass.school='"+LoginController.getschoo()+"' and NO=?)b"); 
		 							
		 							pstmt.setString(1,search.get("Term").toString());      
									pstmt.setString(2,search.get("Category").toString());      
									pstmt.setString(3,search.get("Class").toString()); 
									
									
									ResultSet rs = pstmt.executeQuery();         
									while (rs.next()) { 
									 Examname=rs.getString("Examname")+" "+search.get("Date").toString().split("-")[0];
									}
			 						
									search.remove("customexamname");	 						
			 						
			 						search.put("Exam name", Examname);
							 		search.put("Status", "Open");
							 		search.put("Sms status", "Not Sent");
							 		search.put("school", LoginController.getschoo());								 			
							 		
							 		jsontosql.jsontosqlaction("allexams", search, 2,"");

							 		if(accountsmodel.response.equals("an error occured")) {
							 			  return accountsmodel.jsondata="[{\"querystatus\" : \""+accountsmodel.response+"\"}]";
							 		}
							 		
							 		
							 		
							 		
							 		pstmt= conn.prepareStatement("INSERT INTO `exams` (`studentid`,`exam`,`sub`)  "
						 					+ " SELECT id,(select id from allexams where `exam name`='"+Examname+"' "
						 					+ " and school="+LoginController.getschoo()+" ),"
						 					+ " subid  FROM `subjects1` "
						 					+ " INNER JOIN registration r on `current form`=class "
						 					+ " WHERE r.school=?  and  `current form`=?"
						 					+ " ORDER BY `id`,`ORDER` ASC"); 
						 			
									pstmt.setInt(1,LoginController.getschoo()); 
									pstmt.setString(2,search.get("Class").toString());
									pstmt.execute();
									pstmt.close(); 
									
								            
								    pstmt= conn.prepareStatement("INSERT INTO `mainexams`"
								            		+ " (`studentid`,`exam`)  select id,"
								            		+ " (select id from allexams where "
								            		+ " `exam name`='"+Examname+"' and school='"+LoginController.getschoo()+"') "
								            		+ " from registration  WHERE school='"+LoginController.getschoo()+"' "
								            		+ " and `CURRENT FORM`=?"); 
								    pstmt.setString(1,search.get("Class").toString());      
									pstmt.executeUpdate();         
									pstmt.close();         
										
									
									accountsmodel.dbaction("SELECT * from (SELECT 'Operation successfull' as querystatus,0 as checkstatus,"
											+ " a.id,'List of exams' as title,`Exam Name`,s.class,m.sem AS Term,"
											+ " Performance,DATE,a.Status,`Sms status`,'' AS Action FROM allexams a"
											+ " INNER JOIN studentclass s ON a.Class=s.no"
											+ " INNER JOIN schoolsemesters m ON a.term=m.id "
											+ " where a.school='"+LoginController.getschoo()+"' ORDER BY a.id)g",1, 0, 0,0);
							 		   
									rs.close();                       
									pstmt.close(); 
								
			 					} catch (Exception e1) {
									e1.printStackTrace(); accountsmodel.response="an error occured"; return accountsmodel.jsondata="[{\"querystatus\" : \""+accountsmodel.response+"\"}]";
								}finally {
								    if (conn != null) try { conn.close(); } catch (SQLException ignore) {}
								  }		
 							
		 						
		 						return accountsmodel.jsondata;
		 						
	 						}
 							
	 						
			 		
	 					}
	 				
	 			
			}
		 	
		 	
		 	
		 	@SuppressWarnings({ "resource", "unchecked" })
			@PostMapping(value = "/api/exam/updateexam")
		 	@ResponseBody
		 	public  String  updatestudent(@RequestBody  JSONObject  search, HttpServletRequest request) {		    
		 		
		 		Connection conn=null;
		 		try {
		 			
		 			conn = DbConnector.dataSource.getConnection();
		 		 	
		 			String Examname="";
					PreparedStatement pstmt= conn.prepareStatement("SELECT `exam name`  from allexams WHERE "
							+ " school='"+LoginController.getschoo()+"' and id=?"); 
							pstmt.setString(1,search.get("id").toString());      
					ResultSet rs = pstmt.executeQuery();         
					while (rs.next()) { 
						Examname=rs.getString("exam name");	
					}
		 		
					
					
		 		if(search.get("exam name")!=null) {
		 			
		 			
		 			String NewExamname="";
			 			
				    pstmt= conn.prepareStatement("SELECT concat(upper(class),' ','"+search.get("exam name").toString()+"') "
	 								+ " AS Examname FROM studentclass  WHERE school='"+LoginController.getschoo()+"' and NO=?"); 
							pstmt.setString(1,search.get("Class").toString());      
					rs = pstmt.executeQuery();         
					while (rs.next()) { 
						NewExamname=rs.getString("Examname");	
					}
		 			
					
					
					accountsmodel.dbaction("SELECT b.* from (SELECT * from allexams WHERE "
							+ " school='"+LoginController.getschoo()+"' and `exam name`"+"='"+NewExamname+"')b ",1, 0, 0,0);
			 			
					
			 			if(!accountsmodel.liststring.isEmpty()) {
			 				accountsmodel.response="An exam with a similar name exists";
			 				return accountsmodel.jsondata="[{\"querystatus\" : \""+accountsmodel.response+"\"}]";
			 			}
					
		 			search.remove("exam name");
					search.put("exam name", NewExamname);

		 			jsontosql.jsontosqlaction("allexams", search, 3,"");

			 		if(accountsmodel.response.equals("an error occured")) {
			 			  return accountsmodel.jsondata="[{\"querystatus\" : \""+accountsmodel.response+"\"}]";
			 		}
			 		
			 		
		 	
		 		}else {
		 			
		 			
		 			String NewExamname="";
			 			
		 			pstmt= conn.prepareStatement("SELECT b.* from "
								+ " (SELECT concat(upper(class),' ',upper(sem),' ',upper(e.Category),"
			 					+ "' EXAM') AS Examname FROM studentclass "
			 					+ " INNER JOIN schoolsemesters s ON s.id=?"
			 					+ " INNER JOIN examcategories  e ON e.id=?"
			 					+ " WHERE studentclass.school='"+LoginController.getschoo()+"' and NO=?)b"); 
		 			
		 			pstmt.setString(1,search.get("Term").toString());      
					pstmt.setString(2,search.get("Category").toString());      
					pstmt.setString(3,search.get("Class").toString()); 
					
					
					rs = pstmt.executeQuery();         
					while (rs.next()) { 
						NewExamname=rs.getString("Examname")+" "+search.get("Date").toString().split("-")[0];
					}
		 			
					
					accountsmodel.dbaction("SELECT b.* from (SELECT * from allexams WHERE "
							+ " school='"+LoginController.getschoo()+"' and `exam name`='"+NewExamname+"')b ",1, 0, 0,0);
			 		
					
		 			if(!accountsmodel.liststring.isEmpty()) {
		 				accountsmodel.response="An exam with a similar name exists";
		 				return accountsmodel.jsondata="[{\"querystatus\" : \""+accountsmodel.response+"\"}]";
		 			}
		 			
		 			conn.prepareStatement("RENAME TABLE `"+Examname+"` TO `"+NewExamname+"`").execute();		 			

		 			search.remove("exam name");
					search.put("exam name", NewExamname);

		 			jsontosql.jsontosqlaction("allexams", search, 3,"");

			 		if(accountsmodel.response.equals("an error occured")) {
			 			  return accountsmodel.jsondata="[{\"querystatus\" : \""+accountsmodel.response+"\"}]";
			 		}
			 		
			 		
		 	
		 		}
		 		
		 		
		 		accountsmodel.dbaction("SELECT * from (SELECT '"+accountsmodel.response+"' as querystatus,0 as checkstatus,"
						+ " a.id,'List of exams' as title,`Exam Name`,s.class,m.sem AS Term,"
						+ " Performance,DATE,a.Status,`Sms status`,'' AS Action FROM allexams a"
						+ " INNER JOIN studentclass s ON a.Class=s.no"
						+ " INNER JOIN schoolsemesters m ON a.term=m.id  "
						+ " where a.school='"+LoginController.getschoo()+"' "
						+ " ORDER BY a.id)g ",1, 0, 0,0);
		 		   
		 		
		 		} catch (Exception e1) {
					e1.printStackTrace(); accountsmodel.response="an error occured"; return accountsmodel.jsondata="[{\"querystatus\" : \""+accountsmodel.response+"\"}]";
				}finally {
				    if (conn != null) try { conn.close(); } catch (SQLException ignore) {}
				  }	
		 		
		 		
	 			return accountsmodel.jsondata;	 			
	 		 	
			}
		 	
		 	
		 	
		 	
		 	@PostMapping(value = "/api/exam/deleteexam")
		 	@ResponseBody
		 	public  String  deleteexam(@RequestBody  JSONObject  search, HttpServletRequest request) {		
		 		
		 		jsontosql.jsontosqlaction("delete from allexams where ", search, 4,"");		 		

		 		if(accountsmodel.response.equals("an error occured")) {
		 			  return accountsmodel.jsondata="[{\"querystatus\" : \""+accountsmodel.response+"\"}]";
		 		}
		 		
		 		
		 		Connection conn=null;
		 		try {
		 			conn = DbConnector.dataSource.getConnection();
		 		 	
	 				conn.prepareStatement("INSERT into logs(school,action,ip,time,user)  "
	 								+ "  VALUES("+LoginController.getschoo()+","
	 								+ "  'Deleted exam id "+search.get("id")+"',"
	 								+ " '"+getremoteip.getremoteip()+"',NOW(),"
	 								+ "  "+LoginController.getloggedinuserid()+" )").execute();
	 			
	 			} catch (Exception e) {
	 				e.printStackTrace();
	 			} finally {
				    if (conn != null) try { conn.close(); } catch (SQLException ignore) {}
				  }	
		 		
	 			return accountsmodel.jsondata="[{\"querystatus\" : \""+accountsmodel.response+"\"}]";
	 			 			
			}
		 	
		 	
		 	
		 	
		 	@PostMapping(value = "/api/exam/loadexamsby")
		 	@ResponseBody
		 	public  String  loadexamsby(@RequestBody  JSONObject  search, HttpServletRequest request) {		
		 	
			accountsmodel.dbaction("SELECT * from (SELECT '0' as querystatus,0 as checkstatus,  "
					+ " a.id,'List of exams' as title,`Exam Name`,s.class,m.sem AS Term,  "
					+ " Performance,DATE,a.Status,`Sms status`,'' AS Action FROM (  "
					+ " SELECT *,'All' AS allterm,'All' AS allclass FROM allexams a					   "
					+ " where school='"+LoginController.getschoo()+"')a  "
					+ " INNER JOIN studentclass s ON a.Class=s.no  "
					+ " INNER JOIN schoolsemesters m ON a.term=m.id    				  "
					+ " WHERE  s.school='"+LoginController.getschoo()+"' and "
					+ " case when allterm='"+search.get("Term").toString()+"' "
					+ " then 1=1 ELSE  a.term='"+search.get("Term").toString()+"' end	AND				   "
					+ " case when allclass='"+search.get("Class").toString()+"' then 1=1 "
				    + " ELSE a.class='"+search.get("Class").toString()+"' end	AND  "
					+ " YEAR(DATE)='"+search.get("Date").toString()+"' ORDER BY a.id)f	",1, 0, 0,0);
	 		   
			     return accountsmodel.jsondata;
			     
			 }
		 	
		 	
		 	
		 	
		 	
		 	
		 	
		 	
		 	
		 	@RequestMapping(value="/allowedexams", method=RequestMethod.POST)
			public String gotoentermarks(Model model) {  	    		 
					
		 		return "fragments/exam :: listofavailableexams";        
				 }
		 	 
		 	
		 	@RequestMapping(value = "/api/exam/getallowedexams" , method = RequestMethod.GET)
		 	@ResponseBody
		 	public  String  getallowedexams() {
		 		
		 		
		 		accountsmodel.dbaction("SELECT * FROM (  "
						+ " SELECT a.id,  "
						+ " group_concat(shortname,'',y.stream,' ',q.code) subtought ,  "
						+ "`Exam Name`,s.Class,m.sem AS Term,  "
						+ "							 DATE,a.Status  FROM allexams a  "
						+ "							 INNER JOIN studentclass s ON a.Class=s.no  "
						+ "							 INNER JOIN schoolsemesters m ON a.term=m.id   "
						+ "							 INNER JOIN teaxchersubtought t ON s.no=t.class   "
						+ "							 INNER JOIN teacherregistration r  ON r.id=t.tscnu   "
						+ "							 INNER JOIN studentstreams y ON streanid=t.stream  "
						+ "							 INNER JOIN subjects1 q ON q.subid=t.sub  "
						+ "							 INNER JOIN users u ON u.trnu=r.id   "
						+ "							 where a.school="+LoginController.getschoo()+" "
						+ " 						 and u.id="+LoginController.getloggedinuserid()+"  "
						+ "							 group BY a.id,t.class  ORDER BY a.id)g",1, 0, 0,0);
		 		   
		 		return accountsmodel.jsondata;			 	
		 	}
		 	 
		 	
		 	 
		 	@PostMapping(value = "/api/exam/getexamstudents")
		 	@ResponseBody
		 	public  String  getexamstudents(@RequestBody  JSONObject  search, HttpServletRequest request) {
		 		 		
					
					accountsmodel.dbaction("SELECT r.Id,subid,exam,Admno,NAME,"							
							+ " Case when Score=0  "
							+ " then '' ELSE Score END AS  'mceesub:: "+search.get("classandstream").toString().split(" ")[1]+"' "
							+ " from ( SELECT id,`adm no` as Admno, concat(`first name`,' ',`second name`,' ',surname) AS NAME "
							+ "  FROM registration r "
							+ " INNER JOIN studentstreams s ON streanid=r.stream  "
							+ " WHERE r.school="+LoginController.getschoo()+" AND   "
							+ " s.stream='"+search.get("classandstream").toString().split(" ")[0]+"')r "
							+ " INNER JOIN "
							+ " (SELECT e.studentid,e.sub,Score,exam FROM exams e "
							+ " INNER JOIN subjects1 t ON subid=e.sub   "
							+ " INNER JOIN subjectselection l ON l.studentid=e.studentid and l.sub=t.subid"
							+ " WHERE  yesno=1  and exam="+search.get("examid")+" AND t.school="+LoginController.getschoo()+"  "
							+ " AND t.code='"+search.get("classandstream").toString().split(" ")[1]+"')e "
							+ " ON e.studentid=r.id "
							+ " INNER JOIN subjects1 t ON subid=e.sub  ORDER BY Admno",1, 0, 0,0);
			 		   
					
			    
					return accountsmodel.jsondata;
			     
			 
		 	}
		 	
		 	
		 	
		 	
		 	
		 	@PostMapping(value = "/api/exam/postmarks")
		 	@ResponseBody
		 	public  String  postmarks(@RequestBody  JSONObject  search, HttpServletRequest request) {	
		 		
		 		Connection conn=null;
		 		try {
		 			conn = DbConnector.dataSource.getConnection();
		 		 	
		 			List<String>   array=(List<String>) search.get("marks");		 			
		 			
			 		conn.prepareStatement("DROP TABLE IF EXISTS "
			 				+ "  `"+LoginController.getloggedinuserid()+"_exam_"+LoginController.getschoo()+"`;CREATE TABLE "
			 				+ "  `"+LoginController.getloggedinuserid()+"_exam_"+LoginController.getschoo()+"` ( "
			 				+ "    studentid INT NOT NULL DEFAULT 0, "
			 				+ "    sub INT NOT NULL  DEFAULT 0, "
			 				+ "    exam INT NOT NULL  DEFAULT 0, "
			 				+ "    outof INT NOT NULL  DEFAULT 0, "
			 				+ "    value INT NOT NULL  DEFAULT 0)").execute();
	
			 			List<String> nna = new ArrayList<>();
		        	 
			 		    for(Object object : array) {
			        	 LinkedHashMap record =  (LinkedHashMap) object;
			        	 
			        	 int val=0;
			        	 if(record.get("value").toString().isEmpty()) {
			        		  val=0;
			        	 }else {
			        		  val=Integer.parseInt(record.get("value").toString());
			        	 }
			        	 
			        	 nna.add("('"+record.get("studentid")+"','"+record.get("sub")+"','"+record.get("exam")+"','"+
			        			 record.get("outof")+"','"+val+"')");
			        	 
			 		    }  
			 		    
			 		    
			 		    
			 		   String insertquery = nna.toString().replace("[", "").replace("]", "")
			 	                .trim();
			 		   
			 		   
			 		   
			         conn.prepareStatement("INSERT INTO "
				 				+ " `"+LoginController.getloggedinuserid()+"_exam_"+LoginController.getschoo()+"` "
				 				+ " VALUES "+insertquery
				 				
				 				+ " ;UPDATE exams e "
			            		+ " inner join `"+LoginController.getloggedinuserid()+"_exam_"+LoginController.getschoo()+"` c"
			            		+ " on c.studentid=e.studentid and c.exam=e.exam and c.sub=e.sub"
			            		+ " set Score=value,e.outof=c.outof;"
				            		
				            	+ " DROP TABLE `"
				            	+LoginController.getloggedinuserid()+"_exam_"+LoginController.getschoo()+"`").execute();; 
			       
				            	
			         accountsmodel.response = "Operation successfull";
						
		 		
		 		} catch (Exception e1) {
					e1.printStackTrace(); 
					accountsmodel.response="an error occured"; return accountsmodel.jsondata="[{\"querystatus\" : \""+accountsmodel.response+"\"}]";
				}finally {
				    if (conn != null) try { conn.close(); } catch (SQLException ignore) {}
				  }		
				
		 		
				return accountsmodel.jsondata="[{\"querystatus\" : \""+accountsmodel.response+"\"}]";
		
			     
			 }
		 	
		 	
		 	
		 	@PostMapping(value = "/api/exam/postmarksasadmin")
		 	@ResponseBody
		 	public  String  postmarksasadmin(@RequestBody  JSONObject  search, HttpServletRequest request) {	
		 		
		 		
		 		
		 		Connection conn=null;
		 		try {
		 			
		 			conn = DbConnector.dataSource.getConnection();
		 		 	
		 			
		 			int examid=0;
		 			
		 			int classs=0;
		 			
		 			
		 			ResultSet rt = conn.prepareStatement("SELECT id,class from   allexams  "
							+ " where school='"+LoginController.getschoo()+"' and "
							+ " `exam name`='"+search.get("examname").toString().
							 replace("[", "").replace("]", "")+"' ").executeQuery();
					
		 				while (rt.next()) {
		 					examid=rt.getInt("id");
		 					classs=rt.getInt("class");
		                 }
		 			
		 				
		 					List<String> nna = new ArrayList<>();
			        	 
		 				 	
		 				 List<Map<String, String>> array=(List<Map<String, String>>) search.get("employees");
		 				for(Map<String, String> object : array) {			 		    				 		    	 
			 		    	
			 		    	for(Iterator<?> iterator = object.keySet().iterator(); iterator.hasNext();) {					
								String key = (String) iterator.next();				    
							    
								if(!key.equals("studentid")) {
								   
									 int val=0;
						        	 if(object.get(key).toString().isEmpty()) {
						        		  val=0;
						        	 }else {
						        		  val=Integer.parseInt(object.get(key).toString());
						        	 }
						        	 
								   nna.add("('"+object.get("studentid")+"','"+key+"','"+examid+"','"+classs+"','"+val+"')");
						 		   
								}
							    			    
							}
			        	 
			 		    }  
			 		    	 
			 		    
			 		    
			 		   String insertquery = nna.toString().replace("[", "").replace("]", "").trim();
			 		   
			 		     
			         conn.prepareStatement("DROP TABLE IF EXISTS "
				 				+ "  `"+LoginController.getloggedinuserid()+"_examadminmrks_"+LoginController.getschoo()+"`;"
				 				
				 				+ " CREATE TABLE "
				 				+ "  `"+LoginController.getloggedinuserid()+"_examadminmrks_"+LoginController.getschoo()+"` ( "
				 				+ "    studentid INT NOT NULL DEFAULT 0, "
				 				+ "    sub varchar (25)  DEFAULT '', "
				 				+ "    exam INT NOT NULL  DEFAULT 0, "
				 				+ "    class INT NOT NULL  DEFAULT 0, "
				 				+ "    value INT NOT NULL  DEFAULT 0);"
				 				
				 				+ " INSERT INTO "
				 				+ " `"+LoginController.getloggedinuserid()+"_examadminmrks_"+LoginController.getschoo()+"` "
				 				+ " VALUES "+insertquery
				 				
				 				+ " ;UPDATE exams e "
				 				+ " INNER JOIN subjects1 s on s.class="+classs+" and subid=e.sub"
				            	+ " inner join `"+LoginController.getloggedinuserid()+"_examadminmrks_"+LoginController.getschoo()+"` c"
			            		+ " on c.studentid=e.studentid and c.exam=e.exam and s.code=c.sub "
			            		+ " set Score=value;").execute();; 
			       
				            	
			         accountsmodel.response = "Operation successfull";
						
		 		
		 		} catch (Exception e1) {
					e1.printStackTrace(); 
					accountsmodel.response="an error occured"; return accountsmodel.jsondata="[{\"querystatus\" : \""+accountsmodel.response+"\"}]";
				}finally {
				    if (conn != null) try { conn.close(); } catch (SQLException ignore) {}
				  }		
				
		 		
				return accountsmodel.jsondata="[{\"querystatus\" : \""+accountsmodel.response+"\"}]";
		
			     
			 }
		 	
		 	
		 	
		 	    @RequestMapping(value="/subjectselection", method=RequestMethod.POST)
				public String gotosubjectselection(Model model) {  	    		 
				 	
		 	    	
		 	    	accountsmodel.dbaction("select * from (select concat(NO,' :: ',streanid) AS no ,"
		 	    			+ " concat(class,' ',stream) AS class from studentclass a "
		 	    			+ " cross join studentstreams b "
		 	    			+ " WHERE a.school='"+LoginController.getschoo()+"' AND "
		 	    			+ " b.school='"+LoginController.getschoo()+"' and class!='Transfered' "
		 	    			+ " AND class!='Allumni'  ORDER BY NO,streanid)d"
		 	    			+ " UNION ALL "		 	    			
		 	    			+ " Select 'All' AS no,'All' AS class ",1, 0, 0,0);
			 		   
		 	    	
					List<accountsmodel> newList1 = new ArrayList<accountsmodel>(accountsmodel.datanew);		
					model.addAttribute("classes", newList1); 
					
					
					if(LoginController.getloggedinuseridtype()!=2) {
						return "fragments/exam :: modalerror";        
					}else {
						return "fragments/exam :: subjectselection";        
					}
									
					
		      }
		 	
		 	
		 	    
		 	    @PostMapping(value = "/api/exam/getsubselection")
			 	@ResponseBody
			 	public  String  getsubselection(@RequestBody  JSONObject  search, HttpServletRequest request) {
			 	
		 	    	List<String> nna = new ArrayList<>();
		 	    	 
		 	    	Connection conn=null;
		 	    	try {
		 	    		conn = DbConnector.dataSource.getConnection();
		 	    		
		 	    		ResultSet rt =null;
		 	    		
		 	    		System.out.println(search.get("class")+"   mceee");
		 	    		
		 	    		if(search.get("class").toString().equals("All")) {
		 	    			
		 	    			 rt = conn.prepareStatement("SELECT *  FROM `subjects1`  "
									+ " where school='"+LoginController.getschoo()+"'  "
									+ " group by code  ORDER BY `subid`").executeQuery();
						
		 	    		}else {
		 	    			
		 	    			 rt = conn.prepareStatement("SELECT *  FROM `subjects1`  "
									+ " where school='"+LoginController.getschoo()+"' and "
									+ " class='"+search.get("class").toString().split(" :: ")[0]+"' "
									+ " ORDER BY `subid`").executeQuery();
							
		 	    		}
			 		 	
			 	    	 	
						  while (rt.next()) {
							 
							  if(search.get("class").toString().equals("All")) {
									nna.add("max(IF(code='"+rt.getString("code")+"' and YesNo=1,'YES', 'NO')) as '"+rt.getString("code")+"'");
							  }else {
									nna.add("max(IF(s.sub='"+rt.getInt("subid")+"' and YesNo=1,'YES', 'NO')) as '"+rt.getString("code")+"'");
							  }
							  	
						  }
					  
					  
		 	  
		 	    	}catch (Exception e1) {
					e1.printStackTrace(); accountsmodel.response="an error occured"; return accountsmodel.jsondata="[{\"querystatus\" : \""+accountsmodel.response+"\"}]";
		 	    	}finally {
					    if (conn != null) try { conn.close(); } catch (SQLException ignore) {}
					  }	
		 	    	
		 	    	
		 	    	String query = nna.toString().replace("[", "").replace("]", "").replace(",,", "").trim();
		 	        
		 	    	if(search.get("class").toString().equals("All")) {
		 	    		
		 	    		
		 	    		
		 	    		accountsmodel.dbaction("SELECT r.id,`Adm no`,concat(`first name`,' ',`second name`,' ',surname) AS NAME,"
								+ " "+query+""
								+ " FROM registration r"
								+ " INNER JOIN subjects1 c ON class=`current form` "
								+ " LEFT JOIN subjectselection s ON s.Studentid=r.id  AND c.subid=s.Sub"
								+ " WHERE c.class!='Allumni' AND c.class!='Transfered' "
								+ " AND r.school="+LoginController.getschoo()+" GROUP BY r.id",1, 0, 0,0);
				 		   
	 	    		}else {
	 	    			
	 	    			
		 	    		accountsmodel.dbaction("SELECT r.id,`Adm no`,concat(`first name`,' ',`second name`,' ',surname) AS NAME,"
								+ " "+query+""
								+ " FROM registration r"
								+ " LEFT JOIN subjectselection s ON s.Studentid=r.id"
								+ " WHERE `current form`="+search.get("class").toString().split(" :: ")[0]+" "
								+ " AND stream="+search.get("class").toString().split(" :: ")[1]+" "
								+ " AND r.school="+LoginController.getschoo()+" GROUP BY r.id",1, 0, 0,0);
				 		   
	 	    		}
		 		 	
		 	    	
						
				    
							
						return accountsmodel.jsondata;
				     
				 
			 	}
		 	    
		 	    
		 	    
		 	    
		 	   @PostMapping(value = "/api/exam/getexamstreams")
			 	@ResponseBody
			 	public  String  getexamstreams(@RequestBody  JSONObject  search, HttpServletRequest request) {
		 	   		 		  
			 		accountsmodel.dbaction("SELECT * from (SELECT streanid,s.stream  FROM `allexams` a "
	 	    			 	+ " INNER JOIN registration r on a.class=`current form` "									
	 	    			 	+ " INNER JOIN studentstreams s on s.streanid=r.stream "									
	 	    			 	+ " where r.school='"+LoginController.getschoo()+"' "
	 	    			 	+ " and `exam name`='"+search.get("examname").toString()+"'"
							+ " group by streanid ORDER BY s.`streanid`)t",1, 0, 0,0);
		 		   
		 		   if(accountsmodel.response.equals("an error occured")) {
		 			  return accountsmodel.jsondata="[{\"querystatus\" : \""+accountsmodel.response+"\"}]";
		 		   }
						
		 	    		
		 	    	return accountsmodel.jsondata;
				     
		 	   }
		 	   
		 	   
		 	   
		 	   
		 	   
		 	   
		 	   
		 	  @PostMapping(value = "/api/exam/getallenteredmarksasadminusingstream")
			 	@ResponseBody
			 	public  String  getallenteredmarksasadminusingstream(@RequestBody  JSONObject  search, HttpServletRequest request) {
			 		 	
		 	    	List<String> nna = new ArrayList<>();
		 	    	List<String> nna2 = new ArrayList<>();
		 	    	Connection conn=null;
		 	    	
		 	    	try {
		 	    		
		 	    		
		 	    		conn = DbConnector.dataSource.getConnection();
			 		 	
			 	    	 ResultSet rt = conn.prepareStatement("SELECT *  FROM `subjects1` s "
			 	    			 	+ " INNER JOIN allexams a on s.class=a.Class "									
			 	    			 	+ " where s.school='"+LoginController.getschoo()+"' "
			 	    			 	+ " and `exam name`='"+search.get("examname").toString()+"'"
									+ " ORDER BY `subid`").executeQuery();
			 	    	 
			 	    	       int classs=0;
			 	    	       int examid=0;
								
						  		while (rt.next()) {
						  			
							  		nna.add("Case when s.code='"+rt.getString("code")+"'  "
							  				+ " AND score>0    then Score ELSE '' END AS '"+rt.getString("code")+"'");
							  		
							  		nna2.add("max(`"+rt.getString("code")+"`)  AS '"+rt.getString("code")+"'");
							  		
							  		classs=rt.getInt("class");
							  		
							  		examid=rt.getInt("id");
							  		
				                 }
					  
						  		String query1 = nna2.toString().replace("[", "").replace("]", "").replace(",,", "").trim();
					 	        
						  		String query = nna.toString().replace("[", "").replace("]", "").replace(",,", "").trim();
		 	        
						  		
						  		
						  		if(search.get("stream").equals("All")) {
						  			
						  			accountsmodel.dbaction("SELECT Id,`Adm no`,Name,"+query1+" from (SELECT Id,`Adm no`,Name,"
											+ " "+query+""
											+ " FROM subjects1 s"
											+ " INNER JOIN (SELECT id,`Adm no`,concat(`First name`,' ',`Second name`) "
											+ " Name,`current form`	FROM registration r WHERE `current form`="+classs+")r    "
											+ " ON s.class=r.`current form`"
											+ " INNER JOIN (SELECT studentid,sub,Score	FROM exams e WHERE e.Exam="+examid+")e   "
											+ " ON r.id=e.studentid  and e.sub=subid"
											+ " AND s.school="+LoginController.getschoo()+" and class="+classs+" "
											+ " GROUP BY studentid,subid)j GROUP BY id",1, 0, 0,0);
							 		   
								
						  			
						  		}else {
						  			
						  			accountsmodel.dbaction("SELECT Id,`Adm no`,Name,"+query1+" from (SELECT Id,`Adm no`,Name,"
											+ " "+query+""
											+ " FROM subjects1 s"
											+ " INNER JOIN (SELECT id,`Adm no`,concat(`First name`,' ',`Second name`) "
											+ " Name,`current form`	FROM registration r WHERE `current form`="+classs+""
											+ " and stream="+search.get("stream")+")r    "
											+ " ON s.class=r.`current form`"
											+ " INNER JOIN (SELECT studentid,sub,Score	FROM exams e WHERE e.Exam="+examid+")e   "
											+ " ON r.id=e.studentid  and e.sub=subid"
											+ " AND s.school="+LoginController.getschoo()+" and class="+classs+" "
											+ " GROUP BY studentid,subid)j GROUP BY id",1, 0, 0,0);
							 		   
						  			
						  		}
						  		
						  		
								
			 	    	
			 	    	}catch (Exception e1) {
			 	    		e1.printStackTrace(); accountsmodel.response="an error occured"; 
			 	    		return accountsmodel.jsondata="[{\"querystatus\" : \""+accountsmodel.response+"\"}]";
			 	    	}finally {
							    if (conn != null) try { conn.close(); } catch (SQLException ignore) {}
							  }	
							
		 	    		
		 	    	return accountsmodel.jsondata;
				     
				 
			 	}
		 	   
		 	   
		 	  
		 	  
		 	  
		 	 @PostMapping(value = "/api/exam/getscorepersubject")
			 	@ResponseBody
			 	public  String  getscorepersubject(@RequestBody  JSONObject  search, HttpServletRequest request) {
			 		 	
		 	    		Connection conn=null;
		 	    		
	    		
		 	    	try {
		 	    		
		 	    		conn = DbConnector.dataSource.getConnection();
			
	    		
								int examid=0;
								
								ResultSet rtE = conn.prepareStatement("SELECT a.*,shortname  FROM `allexams` a "
					    			 	+ " INNER JOIN studentclass t on no=a.Class "									
					    			 	+ " where a.school='"+LoginController.getschoo()+"' "
					    			 	+ " and `exam name`='"+search.get("examname").toString()+"'").executeQuery();
					    			while (rtE.next()) {
					    			  		examid=rtE.getInt("id");
					    			}
						
			  			
			  							SQL=  " SELECT LEFT(code,1) AS sub,"
			  								+ " Round((SUM(score)/COUNT(grade)))  avg FROM (  SELECT studentid,"
			  								+ " code,e.sub,s.sub AS Subject,   "
			  								+ " ifnull(ROUND((score/outof)*100),0) score,g.Grade FROM exams e     "
			  								+ " INNER JOIN subjects1 s ON s.subid=e.Sub  "
			  								+ " INNER JOIN registration r ON studentid=r.id    "
			  								+ " INNER JOIN gradingsystem g ON g.class=s.class AND g.Category=s.category  "
			  								+ " AND ifnull(ROUND((score/outof)*100),0)   BETWEEN START AND END AND score>0  "
			  								+ " WHERE  exam="+examid+" AND g.school="+LoginController.getschoo()+"  "
			  								+ " GROUP BY studentid,e.sub)f    GROUP BY sub ";
			  						
			  							accountsmodel.dbaction(SQL,1, 0, 0,0);
			  				 		   
	  					
			 	    	}catch (Exception e1) {
			 	    		e1.printStackTrace(); accountsmodel.response="an error occured"; return accountsmodel.jsondata="[{\"querystatus\" : \""+accountsmodel.response+"\"}]";
			 	    	}finally {
					    if (conn != null) try { conn.close(); } catch (SQLException ignore) {}
			 	    	}	
					
			    		
			    	return accountsmodel.jsondata;
			     
			 
		 	}
					
   		
		 	   
		 	   
		 	   
		 	  
		 	 @PostMapping(value = "/api/exam/getclassmeanandgrade")
			 	@ResponseBody
			 	public  String  getclassmeanandgrade(@RequestBody  JSONObject  search, HttpServletRequest request) {
			 		 	
		 	    		Connection conn=null;
		 	    	
		 	    	try {
		 	    		
		 	    		conn = DbConnector.dataSource.getConnection();
			 	 
		 	    		List<String> nna2 = new ArrayList<>();
		 	    		List<String> nna = new ArrayList<>();
	 	    	
	    		
							classsforreport=0;
							int examid=0;
							classsforreportshortname="";
							String avgandmrksformula="";
							String meangradeformula="";
							
		 	    			
			  	      
			  	      ResultSet rtE = conn.prepareStatement("SELECT a.*,shortname,avgtotmrksformula,"
			  	      				+ " meangradegardingformula  FROM `allexams` a "
				    			 	+ " INNER JOIN studentclass t on no=a.Class "									
				    			 	+ " INNER JOIN analysisformulaandreportformdatesdates l on l.class=no "									
				    			 	+ " where a.school='"+LoginController.getschoo()+"' "
				    			 	+ " and `exam name`='"+search.get("examname").toString()+"'").executeQuery();
			  	      
		    			while (rtE.next()) {
		    				classsforreport=rtE.getInt("class");
					  		classsforreportshortname=rtE.getString("shortname");								  		
					  		examid=rtE.getInt("id");
					  		avgandmrksformula=rtE.getString("avgtotmrksformula");
					  		meangradeformula=rtE.getString("meangradegardingformula");
					  		
				  			
		    			}
			
	    			
	    			avgandmrksformula=avgandmrksformula.replace("classid", ""+classsforreport).
			  				replace("schoolid", ""+LoginController.getschoo()).
			  				replace("examid", ""+examid);
			  		
	    			meangradeformula=meangradeformula.replace("MCEE", avgandmrksformula).replace("classid", ""+classsforreport);
			  		
		
	    			ResultSet rt = conn.prepareStatement("SELECT * FROM grades WHERE "
	 	    	 		+ "school="+LoginController.getschoo()+"").executeQuery();    	 
				  		while (rt.next()) {
					  		nna2.add("SUM(IF(grade="+rt.getString("id")+" ,nuofg, 0 )) AS '"+rt.getString("Grade")+"'");
					  		nna.add("`"+rt.getString("Grade")+"`");
					  		
					  	}
			  		
				  							
				  		accountsmodel.dbaction(meangradeformula,1, 0, 0,0);
				 		   
			  					
		 	    	}catch (Exception e1) {
		 	    		e1.printStackTrace(); accountsmodel.response="an error occured"; return accountsmodel.jsondata="[{\"querystatus\" : \""+accountsmodel.response+"\"}]";
		 	    	}finally {
				    if (conn != null) try { conn.close(); } catch (SQLException ignore) {}
		 	    	}	
				
	    		
	    	return accountsmodel.jsondata;
	     
	 
 	}
	    
		 	   
		 	   
		 	   
		 	   
		 	  
		 	    
		 	    
		 	    @PostMapping(value = "/api/exam/getallenteredmarksasadmin")
			 	@ResponseBody
			 	public  String  getallenteredmarksasadmin(@RequestBody  JSONObject  search, HttpServletRequest request) {
			 		 	
		 	    	List<String> nna = new ArrayList<>();
		 	    	List<String> nna2 = new ArrayList<>();
		 	    	List<String> nna3 = new ArrayList<>();
		 	    	
		 	    	
		 	    	Connection conn=null;
		 	    	
		 	    	try {
		 	    		
		 	    		conn = DbConnector.dataSource.getConnection();
			 		 	
			 	    	 ResultSet rt = conn.prepareStatement("SELECT *  FROM `subjects1` s "
			 	    			 	+ " INNER JOIN allexams a on s.class=a.Class "									
			 	    			 	+ " where s.school='"+LoginController.getschoo()+"' "
			 	    			 	+ " and `exam name`='"+search.get("examname").toString()+"'"
									+ " ORDER BY `subid`").executeQuery();
			 	    	 
			 	    	       int classs=0;
			 	    	       int examid=0;
								
						  		while (rt.next()) {
						  			
							  		nna.add("Case when s.code='"+rt.getString("code")+"'  "
							  				+ " AND score>0    then Score ELSE '' END AS '"+rt.getString("code")+"'");
							  		
							  		nna2.add("max(`"+rt.getString("code")+"`)  AS `"+rt.getString("code")+"`");
							  		
							  		nna3.add("'"+rt.getString("code")+"' , `"+rt.getString("code")+"`");
							  		
							  		classs=rt.getInt("class");
							  		
							  		examid=rt.getInt("id");
							  		
				                 }
					  
						  		
						  		
						  		
						  		String query = nna.toString().replace("[", "").replace("]", "").replace(",,", "").trim();
						  		String query1 = nna2.toString().replace("[", "").replace("]", "").replace(",,", "").trim();
						  		String query2 = nna3.toString().replace("[", "").replace("]", "").replace(",,", "").trim();
					 	        
						  		
						  		
						  		PreparedStatement pstmt= conn.prepareStatement("SELECT "
						  				+ " json_arrayagg(json_object('Id',Id,'Adm no',"
								 		+ " `Adm no`,'Name',Name,"+query2+")) AS obj from ("
								 		+ " Select Id,`Adm no`,Name,"+query1+" from (SELECT Id,`Adm no`,Name,"
										+ " "+query+""
										+ " FROM subjects1 s"
										+ " INNER JOIN (SELECT id,`Adm no`,concat(`First name`,' ',`Second name`) "
										+ " Name,`current form`	FROM registration r WHERE `current form`=?)r    "
										+ " ON s.class=r.`current form`"
										+ " INNER JOIN (SELECT studentid,sub,Score	FROM exams e WHERE e.Exam=?)e   "
										+ " ON r.id=e.studentid  and e.sub=subid"
										+ " AND s.school=? and class=? "
										+ " GROUP BY studentid,subid)j GROUP BY id)f");
							 
					 			pstmt.setInt(1,classs);      
					 			pstmt.setInt(2,examid);      
					 			pstmt.setInt(3,LoginController.getschoo());      
					 			pstmt.setInt(4,classs);      
					 			
								ResultSet rs = pstmt.executeQuery();  
								while (rs.next()) { 
									accountsmodel.jsondata=rs.getString("obj");
								}
						  		 		
								
								
					 	    	}catch (Exception e1) {
					 	    		e1.getMessage();		
					 	    		accountsmodel.response="an error occured";
					 	    		return accountsmodel.jsondata="[{\"querystatus\" : \""+accountsmodel.response+"\"}]";
					 	    		
					 	    	}finally {
							    if (conn != null) try { conn.close(); } catch (SQLException ignore) {}
							    
					 	    	}	
							
		 	    		
		 	    	return accountsmodel.jsondata;
				     
				 
			 	}
		 	    
		 	    
		 	    
		 	    
		 	    
		 	    
		 	    
		 	   @PostMapping(value = "/api/exam/getexammeananddetails")
			 	@ResponseBody
			 	public  String  getexammeananddetails(@RequestBody  JSONObject  search, HttpServletRequest request) {
			 		 	
		 	    	List<String> nna = new ArrayList<>();
		 	    	List<String> nna2 = new ArrayList<>();
		 	    	
		 	    	Connection conn=null;
		 	    	
		 	    	try {
		 	    		
		 	    		conn = DbConnector.dataSource.getConnection();
			 		 	
			 	    	 ResultSet rt = conn.prepareStatement("SELECT *  FROM `subjects1` s "
			 	    			 	+ " INNER JOIN allexams a on s.class=a.Class "									
			 	    			 	+ " where s.school='"+LoginController.getschoo()+"' "
			 	    			 	+ " and `exam name`='"+search.get("examname").toString()+"'"
									+ " ORDER BY `subid`").executeQuery();
			 	    	 
			 	    	       int classs=0;
			 	    	       int examid=0;
								
						  		while (rt.next()) {
						  			
							  		nna.add("Case when s.code='"+rt.getString("code")+"'  "
							  				+ " AND score>0    then Score ELSE '' END AS '"+rt.getString("code")+"'");
							  		
							  		nna2.add("max(`"+rt.getString("code")+"`)  AS '"+rt.getString("code")+"'");
							  		
							  		classs=rt.getInt("class");
							  		
							  		examid=rt.getInt("id");
							  		
				                 }
					  
						  		
						  		String query1 = nna2.toString().replace("[", "").replace("]", "").replace(",,", "").trim();
					 	        
						  		String query = nna.toString().replace("[", "").replace("]", "").replace(",,", "").trim();
		 	        
						  				
								
								accountsmodel.dbaction("SELECT Id,`Adm no`,Name,"+query1+" from (SELECT Id,`Adm no`,Name,"
										+ " "+query+""
										+ " FROM subjects1 s"
										+ " INNER JOIN (SELECT id,`Adm no`,concat(`First name`,' ',`Second name`) "
										+ " Name,`current form`	FROM registration r WHERE `current form`="+classs+")r    "
										+ " ON s.class=r.`current form`"
										+ " INNER JOIN (SELECT studentid,sub,Score	FROM exams e WHERE e.Exam="+examid+")e   "
										+ " ON r.id=e.studentid  and e.sub=subid"
										+ " AND s.school="+LoginController.getschoo()+" and class="+classs+" "
										+ " GROUP BY studentid,subid)j GROUP BY id",1, 0, 0,0);
						 		   
								
			 	    	}catch (Exception e1) {
			 	    		e1.printStackTrace(); 
			 	    		accountsmodel.response="an error occured"; return accountsmodel.jsondata="[{\"querystatus\" : \""+accountsmodel.response+"\"}]";
			 	    	}finally {
							    if (conn != null) try { conn.close(); } catch (SQLException ignore) {}
							  }	
							
		 	    		
		 	    	return accountsmodel.jsondata;
				     
				 
			 	}
		 	    
		 	   
		 	   
		 	   
		 	   
		 	   
		 	   
		 	   
		 	   
		 	   
		 	   
		 	    
		 	    
		 	   int classsforreport=0;
		 	   String  classsforreportshortname="";
		 	   String  ExamName="";
		 	   
		 	   
		 	    @RequestMapping(path = "/api/exam/getexamreports", method = RequestMethod.POST)
			    @ResponseBody
				public ResponseEntity<byte[]> download(@RequestBody  JSONObject  search, HttpServletRequest request) throws IOException {
			    	 	
		 	    	
					List<String> nheadersna = new ArrayList<>();
		 	    	List<String> nna = new ArrayList<>();
		 	    	List<String> nna2 = new ArrayList<>();
		 	    	Connection conn=null;
		 	    	
		 	    	String adm=search.get("report").toString();
		 	    	String uploadDir = "static/"+LoginController.getschoouploadpath()+"/otheruploads";				    
					final String EXTENSION = ".pdf";
			    	File file = new File(uploadDir + File.separator +RequestContextHolder.currentRequestAttributes().getSessionId()+adm+EXTENSION);
			    	
			    	ExamName=search.get("examname").toString();
		 	    	
		 	    	try {
		 	    		
		 	    		conn = DbConnector.dataSource.getConnection();
			 		 	
		 	    		
		 	    		if(search.get("report").equals("rawmarks")) {
		 	    		
			 	    	 ResultSet rt = conn.prepareStatement("SELECT s.*,a.*,shortname  FROM `subjects1` s "
			 	    			 	+ " INNER JOIN allexams a on s.class=a.Class "									
			 	    			 	+ " INNER JOIN studentclass t on no=a.Class "									
			 	    			 	+ " where s.school='"+LoginController.getschoo()+"' "
			 	    			 	+ " and `exam name`='"+search.get("examname").toString()+"'"
									+ " ORDER BY `ORDER`").executeQuery();
			 	    	 
			 	    	classsforreport=0;
			 	    	       int examid=0;
								
						  		while (rt.next()) {
						  			
							  		nna.add("Case when s.code='"+rt.getString("code")+"'  "
							  				+ " AND score>0    then ROUND((Score/outof)*100) ELSE '' END AS '"+rt.getString("code")+"'");
							  		
							  		nna2.add("max(`"+rt.getString("code")+"`)  AS '"+rt.getString("code")+"'");
							  		
							  		classsforreport=rt.getInt("class");
							  		classsforreportshortname=rt.getString("shortname");
							  		
							  		examid=rt.getInt("id");
							  		
				                 }
						  		
					  
						  		String query1 = nna2.toString().replace("[", "").replace("]", "").replace(",,", "").trim();
					 	        
						  		String query = nna.toString().replace("[", "").replace("]", "").replace(",,", "").trim();
						  		
						  		if(search.get("stream").equals("All")) {
							  		
											SQL="SELECT `Adm no`,Name,"+query1+" from (SELECT Id,`Adm no`,Name,"
												+ " "+query+""
														+ " FROM subjects1 s"
														+ " INNER JOIN (SELECT id,`Adm no`,concat(`First name`,' ',`Second name`) "
														+ " Name,`current form`	FROM registration r WHERE `current form`="+classsforreport+")r    "
														+ " ON s.class=r.`current form`"
														+ " INNER JOIN (SELECT studentid,sub,Score,outof	FROM exams e WHERE e.Exam="+examid+")e   "
														+ " ON r.id=e.studentid  and e.sub=subid"
														+ " AND s.school="+LoginController.getschoo()+" and class="+classsforreport+" "
														+ " GROUP BY studentid,subid)j GROUP BY id";
											
											Headermsg = search.get("examname").toString()+ "  -  RAW MARKS";
										    
											
						 	    		}else {
						 	    			
						 	    			SQL="SELECT `Adm no`,Name,"+query1+" from (SELECT Id,`Adm no`,Name,"
													+ " "+query+""
													+ " FROM subjects1 s"
													+ " INNER JOIN (SELECT id,`Adm no`,concat(`First name`,' ',`Second name`) "
													+ " Name,`current form`	FROM registration r WHERE "
													+ " `current form`="+classsforreport+" and stream="+search.get("stream")+")r    "
													+ " ON s.class=r.`current form`"
													+ " INNER JOIN (SELECT studentid,sub,Score,outof	FROM exams e WHERE e.Exam="+examid+")e   "
													+ " ON r.id=e.studentid  and e.sub=subid"
													+ " AND s.school="+LoginController.getschoo()+" and class="+classsforreport+" "
													+ " GROUP BY studentid,subid)j GROUP BY id";
						 	    			
						 	    			Headermsg = search.get("examname").toString()+"   "+
						 	    					classsforreportshortname+""+search.get("streamlong")+ "  -  RAW MARKS";
										    
						 	    			
								  		}
						  		
						  		System.out.println("mcee  "+SQL);
				 	    		
								testReport(dynamicreport_template,search.get("report").toString());
					            
								
		 	    		}
		 	    		
		 	    		
		 	    		else if(search.get("report").equals("meritlist")) {
		 	    						
		 	    			String avgandmrksformula="";
		 	    			
		 	    			
					 	    	 ResultSet rt = conn.prepareStatement("SELECT s.*,a.*,shortname,avgtotmrksformula  FROM `subjects1` s "
					 	    			 	+ " INNER JOIN allexams a on s.class=a.Class "									
					 	    			 	+ " INNER JOIN studentclass t on no=a.Class "									
					 	    			 	+ " INNER JOIN analysisformulaandreportformdatesdates l on l.class=no "									
					 	    			 	+ " where s.school='"+LoginController.getschoo()+"' "
					 	    			 	+ " and `exam name`='"+search.get("examname").toString()+"'"
											+ " ORDER BY `ORDER`").executeQuery();
					 	    	 
					 	    	 
					 	    	  classsforreport=0;
					 	    	       int examid=0;
										
								  		while (rt.next()) {
								  			avgandmrksformula=rt.getString("avgtotmrksformula");
								  			;
								  			
								  			nheadersna.add("`"+rt.getString("code")+"`");
									  		
									  		nna.add("Case when s.code='"+rt.getString("code")+"'  "
									  				+ " AND score>0    then concat(Score,' ',grade) ELSE '' END AS '"+rt.getString("code")+"'");
									  		
									  		nna2.add("max(`"+rt.getString("code")+"`)  AS '"+rt.getString("code")+"'");
									  		
									  		classsforreport=rt.getInt("class");
									  		classsforreportshortname=rt.getString("shortname");
									  		
									  		examid=rt.getInt("id");
									  		
						                 }
								  		
								  		
								  		avgandmrksformula=avgandmrksformula.replace("classid", ""+classsforreport).
								  				replace("schoolid", ""+LoginController.getschoo()).
								  				replace("examid", ""+examid);
								  		  
								  		String query1 = nna2.toString().replace("[", "").replace("]", "").replace(",,", "").trim();
							 	        
								  		String query = nna.toString().replace("[", "").replace("]", "").replace(",,", "").trim();
								  		
								  		String headers = nheadersna.toString().replace("[", "").replace("]", "").replace(",,", "").trim();
								  		
								  		
								  		if(search.get("stream").equals("All")) {
									  		
								  						SQL="SELECT `Adm no`,Name,CLS,"
								  								+ " KCPE,CNT,"+headers+",`TOT MKS`,`MN MKS`,`TOT PTS`,`MN PTS`,'' AS VAP,'-' AS DEV,GRD,"
								  								+ " Case when cp>0 then cp ELSE 'X' END AS CP,"
								  								+ " Case when fp>0 then fp ELSE 'X' END AS FP "
								  								+" from (SELECT `Adm no`,Name,CLS,"
										  						+ " KCPE,CNT,"+query1+",`TOT MKS`,`MN MKS`,`TOT PTS`,`MN PTS`,'' AS VAP,'-' AS DEV,GRD,CP,FP "
																+ " from (SELECT Id,`Adm no`,"
																+ " concat('"+classsforreportshortname+"',' ',Stream) AS ClS,"
																+ " Stream,KCPE,Name,"+query+""
																+ " FROM subjects1 s"
																
																+ " INNER JOIN (SELECT id,s.Stream,`Adm no`,Egrade AS KCPE,"
																+ " concat(`First name`,' ',`Second name`) "
																+ " Name,`current form`	FROM registration r "
																+ " INNER join studentstreams s ON streanid=r.stream "
																+ " WHERE `current form`="+classsforreport+"  "
																+ " AND r.school="+LoginController.getschoo()+" )r    "
																+ " ON s.class=r.`current form`"
																
																
																+ " 	INNER JOIN (  "

																+ " 		SELECT d.*,"
																+ " 		Case when score>0 then g.Grade ELSE '' END AS Grade FROM "
																+ " 		(SELECT studentid,sub,IFNULL(ROUND((score/outof)*100),0) as Score,"
																+ " 		outof	FROM exams e WHERE e.Exam="+examid+")d "
																+ " 		inner JOIN (SELECT * FROM subjects1 WHERE "
																+ " 		class="+classsforreport+" and  school="+LoginController.getschoo()+" )s ON subid=d.sub "
																+ " 		inner JOIN (SELECT * FROM gradingsystem "
																+ " 		WHERE class="+classsforreport+" and  "
																+ " 		school="+LoginController.getschoo()+" )t ON t.Category=s.category "
																+ " 		AND score BETWEEN `START` AND `end` and s.class=t.class "
																+ " 		inner JOIN grades g ON g.id=t.grade "
																+ " 		where   g.school="+LoginController.getschoo()+" ORDER BY studentid,d.sub "
																+ " 		)e    ON r.id=e.studentid  "
																+ " 		and e.sub=subid AND s.school="+LoginController.getschoo()+" "
																+ " 		and class="+classsforreport+"  "
																+ " 		GROUP BY studentid,subid )j"
																
																+ " 		LEFT JOIN ("+avgandmrksformula+")l"
																
																+ "			ON id=l.studentid"
																
																+ "  		GROUP BY j.id ORDER BY CASE  WHEN fp !=0 THEN 1  ELSE 2 END,fp asc,`MN MKS`  DESC,`TOT MKS` DESC)k";
								  						
								  						
													
													Headermsg = search.get("examname").toString()+ "  -  MERIT LIST";
													
													System.out.println(SQL);
												    
													
								 	    		}else {
								 	    			
								 	    			SQL="SELECT `Adm no`,Name,CLS,"
							  								+ " KCPE,CNT,"+headers+",`TOT MKS`,`MN MKS`,`TOT PTS`,`MN PTS`,'' AS VAP,'-' AS DEV,GRD,"
							  								+ " Case when cp>0 then cp ELSE 'X' END AS CP,"
							  								+ " Case when fp>0 then fp ELSE 'X' END AS FP "
							  								+ " from (SELECT `Adm no`,Name,CLS,"
									  						+ " KCPE,CNT,"+query1+",`TOT MKS`,`MN MKS`,`TOT PTS`,`MN PTS`,'' AS VAP,'-' AS DEV,GRD,CP,FP "
															+ " from (SELECT Id,`Adm no`,"
															+ " concat('"+classsforreportshortname+"',' ',Stream) AS ClS,"
															+ " Stream,KCPE,Name,"+query+""
															+ " FROM subjects1 s"
																
															+ " INNER JOIN (SELECT id,s.Stream,"
															+ " `Adm no`,Egrade AS KCPE,concat(`First name`,' ',`Second name`) "
															+ " Name,`current form`	FROM registration r "
															+ " INNER join studentstreams s ON streanid=r.stream "
															+ " WHERE `current form`="+classsforreport+" and "
															+ " r.stream="+search.get("stream")+" AND r.school="+LoginController.getschoo()+")r    "
															+ " ON s.class=r.`current form`"
															
															+ " 	LEFT JOIN (  "

															+ " 		SELECT d.*,"
															+ " 		Case when score>0 then g.Grade ELSE '' END AS Grade FROM "
															+ " 		(SELECT studentid,sub,IFNULL(ROUND((score/outof)*100),0) as Score,"
															+ " 		outof	FROM exams e WHERE e.Exam="+examid+")d "
															+ " 		inner JOIN (SELECT * FROM subjects1 WHERE "
															+ " 		class="+classsforreport+" and  school="+LoginController.getschoo()+")s ON subid=d.sub "
															+ " 		inner JOIN (SELECT * FROM gradingsystem "
															+ " 		WHERE class="+classsforreport+" and  school="+LoginController.getschoo()+")t "
															+ " 		ON t.Category=s.category "
															+ " 		AND score BETWEEN `START` AND `end` and s.class=t.class "
															+ " 		inner JOIN grades g ON g.id=t.grade "
															+ " 		where  g.school="+LoginController.getschoo()+" ORDER BY studentid,d.sub "
															+ " 		)e    ON r.id=e.studentid  "
															+ " 		and e.sub=subid AND s.school="+LoginController.getschoo()+" "
															+ " 		and class="+classsforreport+"  "
															+ " 		GROUP BY studentid,subid )j"
															
															+ " 	LEFT JOIN ("+avgandmrksformula+")l"
															+ "		ON id=l.studentid"
															
															+ "  	GROUP BY j.id ORDER BY CASE  WHEN fp !=0 THEN 1  ELSE 2 END,cp asc,`MN MKS`  DESC)k";
								 	    			
								 	    					Headermsg = search.get("examname").toString()+"   "+
								 	    					classsforreportshortname+""+search.get("streamlong")+ "  -  MERIT LIST";
												    
								 	    					
								 	    					System.out.println(SQL);
															   
											
										  		}
								  		
								  		testReport(dynamicreport_template,search.get("report").toString());
								           
							            
										
				 	    		}
		 	    		
		 	    		
		 	    		
		 	    		
		 	    		else if(search.get("report").equals("bestclass")) {
				 	    		
		 	    			 String avgandmrksformula="";
				 	    	 ResultSet rt = conn.prepareStatement("SELECT s.*,a.*,shortname,avgtotmrksformula,"
				 	    	 			+ " meangradegardingformula "
				 	    	 			+ "  FROM `subjects1` s "
				 	    			 	+ " INNER JOIN allexams a on s.class=a.Class "									
				 	    			 	+ " INNER JOIN studentclass t on no=a.Class "									
				 	    			 	+ " INNER JOIN analysisformulaandreportformdatesdates l on l.class=no "									
				 	    			 	+ " where s.school='"+LoginController.getschoo()+"' "
				 	    			 	+ " and `exam name`='"+search.get("examname").toString()+"'"
										+ " ORDER BY `ORDER`").executeQuery();
				 	    	 
				 	    	 
				 	    	  classsforreport=0;
				 	    	       int examid=0;
									
							  		while (rt.next()) {
							  			
								  		classsforreport=rt.getInt("class");
								  		classsforreportshortname=rt.getString("shortname");								  		
								  		examid=rt.getInt("id");								  		
								  		avgandmrksformula=rt.getString("avgtotmrksformula");
								  		SQL=rt.getString("meangradegardingformula");
								  		
					                 }
							  		
							  		avgandmrksformula=avgandmrksformula.replace("classid", ""+classsforreport).
							  				replace("schoolid", ""+LoginController.getschoo()).replace("examid", ""+examid);
							  						
							  		
							  		SQL=SQL.replace("MCEE", avgandmrksformula).replace("classid", ""+classsforreport);
							  					
												
												Headermsg = search.get("examname").toString()+ "  -  PERFORMANCE PER STREAM";
												
											
									testReport(best_class_report,search.get("report").toString());
											       
						            
									
			 	    		}
			 	    	
		 	    		
		 	    		
		 	    		else if(search.get("report").equals("subjectanalysis")) {
				 	    		
		 	    				classsforreport=0;
		 	    				int examid=0;
		 	    				classsforreportshortname="";
			 	    	      
			 	    	      ResultSet rtE = conn.prepareStatement("SELECT a.*,shortname  FROM `allexams` a "
				 	    			 	+ " INNER JOIN studentclass t on no=a.Class "									
				 	    			 	+ " where a.school='"+LoginController.getschoo()+"' "
				 	    			 	+ " and `exam name`='"+search.get("examname").toString()+"'").executeQuery();
				 	    			while (rtE.next()) {
				 	    				classsforreport=rtE.getInt("class");
								  		classsforreportshortname=rtE.getString("shortname");								  		
								  		examid=rtE.getInt("id");
				 	    			}
		 	    		
		 	    		
				 	    			ResultSet rt = conn.prepareStatement("SELECT * FROM grades WHERE "
					 	    	 		+ "school="+LoginController.getschoo()+"").executeQuery();    	 
								  		while (rt.next()) {
									  		nna.add("`"+rt.getString("Grade")+"`");
									  		nna2.add("SUM(IF(grade="+rt.getString("id")+" ,nuofg, 0 )) AS '"+rt.getString("Grade")+"'");
									  	}
							  		
								  		
							  		String query = nna.toString().replace("[", "").replace("]", "").replace(",,", "").trim();
							  		String query1 = nna2.toString().replace("[", "").replace("]", "").replace(",,", "").trim();
						 	        
							  			
							  						SQL=" SELECT SUBJECT,IFNULL(s.Stream,j.stream) AS STREAM,ENTRY, "
							  								+ " "+query+",d.MEAN,MNPTS,GRADE,'' AS 'PREV MEAN','' AS 'PREV GRADE','' AS 'DEV', "
							  								+ " IFNULL(concat('T.r ',`first name`),'') AS Teacher FROM( "
							  								
							  								+ " SELECT * FROM (select Subject,stream,sub, "
							  								+ " "+query1+" FROM ( "
							  								+ " SELECT stream,sub,grade,COUNT(grade) nuofg,Subject FROM ( "
							  								+ " SELECT studentid,stream,code,e.sub,s.sub AS Subject,  "
							  								+ " ifnull(ROUND((score/outof)*100),0) score,g.Grade FROM exams e  "
							  								+ " INNER JOIN subjects1 s ON s.subid=e.Sub "
							  								+ " INNER JOIN registration r ON studentid=r.id "
							  								+ " INNER JOIN gradingsystem g ON g.class=s.class AND g.Category=s.category "
							  								+ " AND ifnull(ROUND((score/outof)*100),0) BETWEEN START AND END AND score>0 "
							  								+ " WHERE  exam="+examid+" AND "
							  								+ " g.school="+LoginController.getschoo()+"	 GROUP BY studentid,e.sub)f "
							  								+ " GROUP BY stream,sub,grade ORDER BY stream,sub,grade)f "
							  								+ " GROUP BY stream,sub  "
							  								
							  								+ " UNION ALL  "
							  								
							  								+ " select Subject,stream,sub, "
							  								+ " "+query1+" "
							  								+ "  FROM ( "
							  								+ " SELECT stream,sub,grade,COUNT(grade) nuofg,Subject FROM ( "
							  								+ " SELECT studentid,'Whole class' AS stream,code,e.sub,s.sub AS Subject,  "
							  								+ " ifnull(ROUND((score/outof)*100),0) score,g.Grade FROM exams e  "
							  								+ " INNER JOIN subjects1 s ON s.subid=e.Sub "
							  								+ " INNER JOIN registration r ON studentid=r.id "
							  								+ " INNER JOIN gradingsystem g ON g.class=s.class AND g.Category=s.category "
							  								+ " AND ifnull(ROUND((score/outof)*100),0) BETWEEN START AND END AND score>0 "
							  								+ " WHERE  exam="+examid+" AND "
							  								+ " g.school="+LoginController.getschoo()+"	 GROUP BY studentid,e.sub)f "
							  								+ " GROUP BY sub,grade)f "
							  								+ " GROUP BY sub )g "
							  								+ " ORDER BY sub,stream)j "
							  								
							  								+ " INNER JOIN ("
							  								
															+ " 		 SELECT s.*,d.Grade  FROM (  SELECT stream,sub,COUNT(grade) ENTRY, "
															+ " 		 Round((SUM(score)/COUNT(grade)),2) MEAN, Round((SUM(points)/COUNT(grade)),2) MNPTS ,"
							  								+ " 		 Round((SUM(points)/COUNT(grade))) meanforg FROM (SELECT studentid,stream,code,points,"
							  								+ " 		 e.sub,s.sub AS Subject,   ifnull(ROUND((score/outof)*100),0) score,g.Grade FROM exams e "  
							  								+ " 		 INNER JOIN subjects1 s ON s.subid=e.Sub  INNER JOIN registration r ON studentid=r.id  "
							  								+ " 		 INNER JOIN gradingsystem g ON g.class=s.class AND g.Category=s.category  AND ifnull(ROUND((score/outof)*100),0) "
							  								+ " 		 BETWEEN START AND END AND score>0  "
							  								+ " 		 INNER JOIN grades l ON l.id=g.Grade "
							  								+ " 		 WHERE  exam="+examid+" AND g.school="+LoginController.getschoo()+" "
							  								+ " 		 GROUP BY studentid,e.sub)f  GROUP BY stream,sub)s  "
							  								+ " 		 INNER JOIN grades d ON d.points=s.meanforg  GROUP BY stream,sub  "
							  										 
							  								+ " 		 UNION ALL  "
							  										 
							  								+ " 		 SELECT s.*,d.Grade  FROM (  SELECT stream,sub,COUNT(grade) ENTRY,Round((SUM(score)/COUNT(grade)),2)  MEAN,"
							  								+ " 		 Round((SUM(points)/COUNT(grade)),2) MNPTS , Round((SUM(points)/COUNT(grade))) meanforg FROM (  "
							  								+ " 		 SELECT studentid,'Whole class' AS stream,code,points,e.sub,s.sub AS Subject,   "
							  								+ " 		 ifnull(ROUND((score/outof)*100),0) score,g.Grade FROM exams e   INNER JOIN subjects1 s ON s.subid=e.Sub  "
							  								+ " 		 INNER JOIN registration r ON studentid=r.id  INNER JOIN gradingsystem g "
							  								+ " 		 ON g.class=s.class AND g.Category=s.category  "
							  								+ " 		 AND ifnull(ROUND((score/outof)*100),0) BETWEEN START AND END AND score>0  "
							  								+ " 		 INNER JOIN grades l ON l.id=g.Grade "
							  								+ " 		 WHERE  exam="+examid+" AND g.school="+LoginController.getschoo()+"	  "
							  								+ " 		 GROUP BY studentid,e.sub)f  GROUP BY sub ORDER BY stream)s     "
							  								+ " 		 INNER JOIN grades d ON d.points=s.meanforg  GROUP BY sub  ORDER BY sub,stream "
							  								
							  								
							  								+ " )d "
							  								
							  								+ " ON j.stream=d.stream AND j.sub=d.sub "
							  								+ " "
							  								+ " LEFT JOIN studentstreams s ON streanid=j.stream "
							  								+ " LEFT JOIN teaxchersubtought t ON t.stream=streanid "
							  								+ " AND t.sub=j.sub AND t.class="+classsforreport+" "
							  								+ " LEFT JOIN teacherregistration l ON l.id=t.tscnu "
							  								
							  								+ " GROUP BY j.sub,streanid ORDER BY j.sub,CASE  WHEN j.STREAM !='Whole class' THEN 1  ELSE 2 END,mean desc ";
							  						
							  						
												
												Headermsg = "SUBJECT ANALYSIS FOR  "+search.get("examname").toString();
												
												System.out.println(SQL);
											    
												
							 	    		
							  		
							  		testReport(dynamicreport_template,search.get("report").toString());
							           
						            
									
			 	    		}
		 	    		
		 	    		
		 	    		
		 	    		else if(search.get("report").equals("subjectranking")) {
			 	    		
	 	    				classsforreport=0;
	 	    				int examid=0;
	 	    				classsforreportshortname="";
	 	    				String avgandmrksformula="";
	 	    				String SQL2="";
				 	    	 
		 	    	      ResultSet rtE = conn.prepareStatement("SELECT s.*,a.*,shortname,avgtotmrksformula,"
			 	    	 			+ " meangradegardingformula "
			 	    	 			+ "  FROM `subjects1` s "
			 	    			 	+ " INNER JOIN allexams a on s.class=a.Class "									
			 	    			 	+ " INNER JOIN studentclass t on no=a.Class "									
			 	    			 	+ " INNER JOIN analysisformulaandreportformdatesdates l on l.class=no "									
			 	    			 	+ " where s.school='"+LoginController.getschoo()+"' "
			 	    			 	+ " and `exam name`='"+search.get("examname").toString()+"'"
									+ " ORDER BY `ORDER`").executeQuery();
			 	    			while (rtE.next()) {
			 	    				classsforreport=rtE.getInt("class");
							  		classsforreportshortname=rtE.getString("shortname");								  		
							  		examid=rtE.getInt("id");
							  		avgandmrksformula=rtE.getString("avgtotmrksformula");
							  		SQL2=rtE.getString("meangradegardingformula");
							  		
			 	    			}
	 	    		
	 	    		
			 	    			ResultSet rt = conn.prepareStatement("SELECT * FROM grades WHERE "
				 	    	 		+ "school="+LoginController.getschoo()+"").executeQuery();    	 
							  		while (rt.next()) {
								  		nna.add("`"+rt.getString("Grade")+"`");
								  		nna2.add("SUM(IF(grade="+rt.getString("id")+" ,nuofg, 0 )) AS '"+rt.getString("Grade")+"'");
								  	}
						  		
							  		avgandmrksformula=avgandmrksformula.replace("classid", ""+classsforreport).
							  				replace("schoolid", ""+LoginController.getschoo()).replace("examid", ""+examid);
							  		
							  		SQL2=SQL2.replace("MCEE", avgandmrksformula).replace("classid", ""+classsforreport);
							  		
							  		
						  		String query = nna.toString().replace("[", "").replace("]", "").replace(",,", "").trim();
						  		String query1 = nna2.toString().replace("[", "").replace("]", "").replace(",,", "").trim();
					 	        
						  			
						  						SQL="SELECT 0 AS Rank,'Aggregate' AS Subject,ENTRY,"+query+",`MN MKS` AS MEAN,`MN PTS` AS MNPTS,Grade,"
						  								+ " '' AS 'PREV MEAN', '' AS 'PREV GRADE','' AS 'DEV',"
						  								+ " concat(ROUND(((Pass/entry)*100),2),' %') AS Pass,"
						  								+ " concat(ROUND(((Fail/entry)*100),2),' %') AS Fail,'' AS Teacher FROM ( "
						  								+ " SELECT "+query1+","
						  								+ " SUM(pass) AS pass,SUM(fail) AS Fail   "
						  								+ " FROM (SELECT `MN MKS`,grade,`TOT MKS`,"
						  								+ " COUNT(grade) nuofg,if(IFNULL(`MN MKS`,0)>=pass,1,0) AS pass"
						  								+ " ,if(IFNULL(`MN MKS`,0)<pass,1,0) AS Fail FROM ("+avgandmrksformula+")k"
						  								+ " group BY grade,studentid)f)g "
						  								
						  								+ " LEFT JOIN (SELECT stream,`MN MKS`,`MN PTS`,Grade,"
						  								+ " ENTRY FROM ( "+SQL2+" )h )l  ON stream='Aggregate' "
						  								
						  								+ " UNION ALL "
						  								
														+ " SELECT RANK() OVER (ORDER BY MEAN DESC) as RANK,SUBJECT,ENTRY,"+query+",d.MEAN,MNPTS,GRADE,"
						  								+ "  '' AS 'PREV MEAN','' AS 'PREV GRADE','' AS 'DEV',concat(ROUND(((Pass/entry)*100),2),' %') AS Pass,"
						  								+ "  concat(ROUND(((Fail/entry)*100),2),' %') AS Fail,IFNULL(concat('T.r ',`first name`),'') AS Teacher "
						  								+ "  FROM(  SELECT * FROM (select Subject,sub,"+query1+", sum(Pass) AS pass,sum(Fail) AS Fail   "
						  								+ "  FROM (  SELECT sub,grade,COUNT(grade) nuofg,Subject,sum(Pass) AS pass,sum(Fail) AS Fail FROM (  SELECT studentid,"
						  								+ "  if(IFNULL(ROUND((score/outof)*100),0)>=pass,1,0) AS pass,if(IFNULL(ROUND((score/outof)*100),0)<pass,1,0) AS fail, "
						  								+ "  code,e.sub,s.sub AS Subject,   ifnull(ROUND((score/outof)*100),0) score,g.Grade FROM exams e   "
						  								+ "  INNER JOIN subjects1 s ON s.subid=e.Sub  INNER JOIN registration r ON studentid=r.id  "
						  								+ "  INNER JOIN gradingsystem g ON g.class=s.class AND g.Category=s.category  "
						  								+ "  AND ifnull(ROUND((score/outof)*100),0) BETWEEN START AND END AND score>0  WHERE  exam="+examid+" "
						  								+ "  AND  g.school="+LoginController.getschoo()+"	 "
						  								+ "  GROUP BY studentid,e.sub)f  GROUP BY sub,grade)f  "
						  								+ "  GROUP BY sub )g  ORDER BY sub)j  "
						  								 
						  								+ "  INNER JOIN (  "
						  								 
	
															+ " 		 SELECT s.*,d.Grade  FROM (  SELECT stream,sub,COUNT(grade) ENTRY, "
															+ " 		 Round((SUM(score)/COUNT(grade)),2) MEAN, Round((SUM(points)/COUNT(grade)),2) MNPTS ,"
							  								+ " 		 Round((SUM(points)/COUNT(grade))) meanforg FROM (SELECT studentid,stream,code,points,"
							  								+ " 		 e.sub,s.sub AS Subject,   ifnull(ROUND((score/outof)*100),0) score,g.Grade FROM exams e "  
							  								+ " 		 INNER JOIN subjects1 s ON s.subid=e.Sub  INNER JOIN registration r ON studentid=r.id  "
							  								+ " 		 INNER JOIN gradingsystem g ON g.class=s.class AND g.Category=s.category  AND ifnull(ROUND((score/outof)*100),0) "
							  								+ " 		 BETWEEN START AND END AND score>0  "
							  								+ " 		 INNER JOIN grades l ON l.id=g.Grade "
							  								+ " 		 WHERE  exam="+examid+" AND g.school="+LoginController.getschoo()+" "
							  								+ " 		 GROUP BY studentid,e.sub)f  GROUP BY stream,sub)s  "
							  								+ " 		 INNER JOIN grades d ON d.points=s.meanforg  GROUP BY stream,sub  "
							  										 
							  								+ " 		 UNION ALL  "
							  										 
							  								+ " 		 SELECT s.*,d.Grade  FROM (  SELECT stream,sub,COUNT(grade) ENTRY,Round((SUM(score)/COUNT(grade)),2)  MEAN,"
							  								+ " 		 Round((SUM(points)/COUNT(grade)),2) MNPTS , Round((SUM(points)/COUNT(grade))) meanforg FROM (  "
							  								+ " 		 SELECT studentid,'Whole class' AS stream,code,points,e.sub,s.sub AS Subject,   "
							  								+ " 		 ifnull(ROUND((score/outof)*100),0) score,g.Grade FROM exams e   INNER JOIN subjects1 s ON s.subid=e.Sub  "
							  								+ " 		 INNER JOIN registration r ON studentid=r.id  INNER JOIN gradingsystem g "
							  								+ " 		 ON g.class=s.class AND g.Category=s.category  "
							  								+ " 		 AND ifnull(ROUND((score/outof)*100),0) BETWEEN START AND END AND score>0  "
							  								+ " 		 INNER JOIN grades l ON l.id=g.Grade "
							  								+ " 		 WHERE  exam="+examid+" AND g.school="+LoginController.getschoo()+"	  "
							  								+ " 		 GROUP BY studentid,e.sub)f  GROUP BY sub ORDER BY stream)s     "
							  								+ " 		 INNER JOIN grades d ON d.points=s.meanforg  GROUP BY sub  ORDER BY sub,stream "
							  								
							  								
							  								+ " )d "
							  								
						  								+ "  ON j.sub=d.sub   "
						  								+ "  LEFT JOIN teaxchersubtought t ON  t.sub=j.sub "
						  								+ "  AND t.class="+classsforreport+"  "
						  								+ "  LEFT JOIN teacherregistration l ON l.id=t.tscnu  GROUP BY j.sub ORDER BY RANK"
						  								
						  								+ "";
						  						
						  						
											
											Headermsg = "SUBJECT RANKING  "+search.get("examname").toString();
											
											
						  		
						  		testReport(dynamicreport_template,search.get("report").toString());
						           
					            
								
		 	    		}
		 	    		
		 	    		
		 	    		else if(search.get("report").equals("meangradeanalysis")) {
			 	    		
	 	    				classsforreport=0;
	 	    				int examid=0;
	 	    				classsforreportshortname="";
	 	    				String avgandmrksformula="";
	 	    				String meangradeformula="";	 	    				
		 	    			
		 	    	      
		 	    	      ResultSet rtE = conn.prepareStatement("SELECT a.*,shortname,avgtotmrksformula,"
		 	    	      			+ " meangradegardingformula  FROM `allexams` a "
			 	    			 	+ " INNER JOIN studentclass t on no=a.Class "									
			 	    			 	+ " INNER JOIN analysisformulaandreportformdatesdates l on l.class=no "									
			 	    			 	+ " where a.school='"+LoginController.getschoo()+"' "
			 	    			 	+ " and `exam name`='"+search.get("examname").toString()+"'").executeQuery();
			 	    			while (rtE.next()) {
			 	    				classsforreport=rtE.getInt("class");
							  		classsforreportshortname=rtE.getString("shortname");								  		
							  		examid=rtE.getInt("id");
							  		avgandmrksformula=rtE.getString("avgtotmrksformula");
							  		meangradeformula=rtE.getString("meangradegardingformula");
							  		
			 	    			}
	 	    		
			 	    			avgandmrksformula=avgandmrksformula.replace("classid", ""+classsforreport).
						  				replace("schoolid", ""+LoginController.getschoo()).
						  				replace("examid", ""+examid);
						  		
			 	    			meangradeformula=meangradeformula.replace("MCEE", avgandmrksformula).replace("classid", ""+classsforreport);
						  		
	 	    		
			 	    			ResultSet rt = conn.prepareStatement("SELECT * FROM grades WHERE "
				 	    	 		+ "school="+LoginController.getschoo()+"").executeQuery();    	 
							  		while (rt.next()) {
								  		nna2.add("SUM(IF(grade="+rt.getString("id")+" ,nuofg, 0 )) AS '"+rt.getString("Grade")+"'");
								  	}
						  		
							  		
							  		
						  		String query1 = nna2.toString().replace("[", "").replace("]", "").replace(",,", "").trim();					 	        
						  			
						  						SQL="SELECT IFNULL(s.Stream,'Aggregate') AS Stream,ENTRY,g.*,`MN MKS`,`MN PTS` AS MNPTS,Grade,'' AS 'PREV MEAN','' AS 'PREV GRADE','' AS 'DEV' FROM ("
						  								
						  								+ " SELECT g.* FROM (select stream as streams,	 "
						  								
						  								+ "  "+query1+""
						  								
						  								+ "   FROM ("
						  							  
						  								+ "    SELECT k.*,COUNT(grade) nuofg FROM ("+avgandmrksformula+")k "
						  								+ "    group BY 	stream,grade"
						  								  
						  								+ "  UNION ALL"
						  								 
						  								+ " 	 SELECT k.*,COUNT(grade) nuofg FROM "
						  								+ "     ("+avgandmrksformula.replace("d.*,stream", "d.*,'Aggregate' as stream")+")k "
						  								+ " 	group BY grade"
						  									 
						  								+ " 	 )f  GROUP BY streams)g  ORDER BY streams)g"
						  								 
														+ "  LEFT JOIN studentstreams s ON g.streams=streanid "
								  						
						  								+ "  LEFT JOIN ("+meangradeformula+")l"
						  							 
						  								+ " ON l.stream=CASE "
						  								+ " WHEN l.stream = s.stream THEN s.stream "
						  								+ " WHEN l.stream = g.streams THEN g.streams END ";
						  								
											
						  						
											Headermsg = "MEAN GRADE ANALYSIS FOR  "+search.get("examname").toString();
											
											
											
						 	    		
						  		
						  		testReport(dynamicreport_template,search.get("report").toString());
						           
					            
								
		 	    		}
		 	    		
		 	    		JasperExportManager.exportReportToPdfFile(jp,uploadDir + File.separator +RequestContextHolder.
								 currentRequestAttributes().getSessionId()+adm+EXTENSION);
						
		 	    		
		 	    	}catch (Exception e1) {
			 	    		e1.printStackTrace(); 
		 	    	}finally {if (conn != null) 
		 	    		try { conn.close(); } 
		 	    	 	catch (SQLException ignore) {}
					 }	
							
		 	    		
		 	    	
		 	    	
		 	    	HttpHeaders header = new HttpHeaders();
			        header.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename="+RequestContextHolder.
							 currentRequestAttributes().getSessionId()+adm + EXTENSION);
			        header.add("Cache-Control", "no-cache, no-store, must-revalidate");
			        header.add("Pragma", "no-cache");
			        header.add("Expires", "0");

			        byte[] inFileBytes = Files.readAllBytes(Paths.get(file.getAbsolutePath())); 
			        byte[] contents = java.util.Base64.getEncoder().encode(inFileBytes);
			        
			       
			        HttpHeaders headers = new HttpHeaders();
			        headers.setContentType(MediaType.APPLICATION_PDF);
			        // Here you have to set the actual filename of your pdf
			        String filename = RequestContextHolder.
							 currentRequestAttributes().getSessionId()+adm + EXTENSION;
			        headers.setContentDispositionFormData(filename, filename);
			        headers.setCacheControl("must-revalidate, post-check=0, pre-check=0");
			        ResponseEntity<byte[]> response = new ResponseEntity<>(contents, headers, HttpStatus.OK);
			        return response;
			    
				}
		 	    
		 	    
		 	    
		 	    
		 	    
		 	    
		 	    @PostMapping(value = "/api/exam/getperformanceperstream")
			 	@ResponseBody
			 	public  String  getperformanceperstream(@RequestBody  JSONObject  search, HttpServletRequest request) {
			 		 	
		 	    	
		 	    	Connection conn=null;
		 	    	try {
		 	    		
		 	    		conn = DbConnector.dataSource.getConnection();
			 		 	
			 	    	List<String> nna2 = new ArrayList<>();
			 	    	List<String> nna = new ArrayList<>();
			 	    	
		 	    		
 	    				classsforreport=0;
 	    				int examid=0;
 	    				classsforreportshortname="";
 	    				String avgandmrksformula="";
 	    				String meangradeformula="";
 	    				
	 	    			
	 	    	      
	 	    	      ResultSet rtE = conn.prepareStatement("SELECT a.*,shortname,avgtotmrksformula,"
	 	    	      			+ " meangradegardingformula  FROM `allexams` a "
		 	    			 	+ " INNER JOIN studentclass t on no=a.Class "									
		 	    			 	+ " INNER JOIN analysisformulaandreportformdatesdates l on l.class=no "									
		 	    			 	+ " where a.school='"+LoginController.getschoo()+"' "
		 	    			 	+ " and `exam name`='"+search.get("examname").toString()+"'").executeQuery();
		 	    			while (rtE.next()) {
		 	    				classsforreport=rtE.getInt("class");
						  		classsforreportshortname=rtE.getString("shortname");								  		
						  		examid=rtE.getInt("id");
						  		avgandmrksformula=rtE.getString("avgtotmrksformula");
						  		meangradeformula=rtE.getString("meangradegardingformula");
						  		
					  			
		 	    			}
 	    		
		 	    			
		 	    			avgandmrksformula=avgandmrksformula.replace("classid", ""+classsforreport).
					  				replace("schoolid", ""+LoginController.getschoo())
					  				.replace("examid", ""+examid);
					  		
		 	    			meangradeformula=meangradeformula.replace("MCEE", avgandmrksformula).replace("classid", ""+classsforreport);
					  		
 	    		
		 	    			ResultSet rt = conn.prepareStatement("SELECT * FROM grades WHERE "
			 	    	 		+ "school="+LoginController.getschoo()+"").executeQuery();    	 
						  		while (rt.next()) {
							  		nna2.add("SUM(IF(grade="+rt.getString("id")+" ,nuofg, 0 )) AS '"+rt.getString("Grade")+"'");
							  		nna.add("`"+rt.getString("Grade")+"`");
							  		
							  	}
					  		
						  		
						  		
						  	String query = nna.toString().replace("[", "").replace("]", "").replace(",,", "").trim();
						  		
					  		String query1 = nna2.toString().replace("[", "").replace("]", "").replace(",,", "").trim();					 	        
					  			
					  						SQL="SELECT IFNULL(s.Stream,'Tot') AS Stream,"+query+" FROM ("
					  								
					  								+ " SELECT g.* FROM (select stream as streams,	 "
					  								
					  								+ "  "+query1+""
					  								
					  								+ "   FROM ("
					  							  
					  								+ "  SELECT k.*,COUNT(grade) nuofg FROM ("
					  								+ "  SELECT w.*, l.id AS grade,stream  	FROM (SELECT g.*,"
					  								+ "  ROUND(`TOT MKS` /avgandtotalformula) AS `MN MKS` FROM (		"
					  								+ "  SELECT studentid,"
					  								+ "  SUM(ROUND((score/outof)*100)) 'TOT MKS',sum(if(score>0,1,0)) AS CNT	FROM exams e WHERE  exam="+examid+"	"	
					  								+ "  GROUP BY studentid)g		"
					  								+ "  LEFT JOIN analysisformulaandreportformdatesdates a ON a.Class="+classsforreport+")w		"
					  								+ "  LEFT JOIN overallgradingsystem o ON `MN MKS` BETWEEN `Start` AND END AND class="+classsforreport+"	"	
					  								+ "  LEFT JOIN grades l ON l.id=o.Grade		"
					  								+ "  inner JOIN registration r ON r.id=studentid      where   "
					  								+ "   r.school="+LoginController.getschoo()+"  "
					  								+ "  GROUP BY studentid)k group BY 	stream,grade"
					  								  
					  								+ "  UNION ALL"
					  								 
					  								+ " 	 SELECT k.*,COUNT(grade) nuofg FROM ("
					  								+ " 	 SELECT w.*, l.id AS grade,'Aggregate' as stream  	FROM (SELECT g.*,"
					  								+ " 	 ROUND(`TOT MKS` /avgandtotalformula) AS `MN MKS` FROM (		"
					  								+ " 	 SELECT studentid,"
					  								+ " 	 SUM(ROUND((score/outof)*100)) 'TOT MKS',		"
					  								+ " 	 sum(if(score>0,1,0)) AS CNT	FROM exams e WHERE  exam="+examid+"		"
					  								+ " 	 GROUP BY studentid)g		"
					  								+ " 	 LEFT JOIN analysisformulaandreportformdatesdates a ON a.Class="+classsforreport+")w		"
					  								+ " 	 LEFT JOIN overallgradingsystem o ON `MN MKS` BETWEEN `Start` AND END AND class="+classsforreport+"	"	
					  								+ " 	 LEFT JOIN grades l ON l.id=o.Grade		"
					  								+ " 	 inner JOIN registration r ON r.id=studentid      where   "
					  								+ " 	 r.school="+LoginController.getschoo()+"  "
					  								+ " 	 GROUP BY studentid)k group BY grade"
					  									 
					  								+ " 	 )f  GROUP BY streams)g  ORDER BY streams)g"
					  								 
													+ "  LEFT JOIN studentstreams s ON g.streams=streanid "
							  						
					  								+ "  LEFT JOIN ("+meangradeformula+")l"
					  							 
					  								+ " ON l.stream=CASE "
					  								+ " WHEN l.stream = s.stream THEN s.stream "
					  								+ " WHEN l.stream = g.streams THEN g.streams END ";
					  								
					  	
					  						System.out.println(SQL);
											
					  						accountsmodel.dbaction(SQL,1, 0, 0,0);
					  			 		   
					  					
						
		 	    	}catch (Exception e1) {
		 	    		e1.printStackTrace(); accountsmodel.response="an error occured"; return accountsmodel.jsondata="[{\"querystatus\" : \""+accountsmodel.response+"\"}]";
		 	    	}finally {
						    if (conn != null) try { conn.close(); } catch (SQLException ignore) {}
						  }	
							
						return accountsmodel.jsondata;
				     
				 
			 	}
		 	    
		 	    
		 	    
		 	   @SuppressWarnings("unchecked")
		 	   @PostMapping(value = "/api/exam/submitsubjectselection")
			 	@ResponseBody
			 	public  String  postsubjectselection(@RequestBody  JSONObject  search, HttpServletRequest request) {	
			 	  
		 		  Connection conn=null;
		 		   try {
		 			  conn = DbConnector.dataSource.getConnection();
			 		 	
		 			   
		 			  Map <String, Integer> hm = new HashMap<String, Integer>();
			 		   
		 		   
		 			  List<String>   classs=(List<String>) search.get("classs");
		 			  String classs2="";
		 			  String stream="";
		 			  
			 		   for(Object object : classs) {
			 			  LinkedHashMap record =  (LinkedHashMap) object;
			 			  classs2=record.get("class").toString().split(" :: ")[0];
			 			  stream=record.get("class").toString().split(" :: ")[1];
			 		   } 
			 		   
			 		 	 ResultSet rt = conn.prepareStatement("SELECT subid,code  FROM `subjects1`  "
									+ " where school='"+LoginController.getschoo()+"' and "
									+ " class="+classs2+" ").executeQuery();							
							while (rt.next()) {	
								hm.put(rt.getString("code"), rt.getInt("subid"));
							}
			 		   
			 		   
		 			  List<String>   array=(List<String>) search.get("marks");
		 			  
		 			 
			 			
			 			List<String> nna = new ArrayList<>();
		        	 
			 		    for(Object object : array) {
			        	 LinkedHashMap record =  (LinkedHashMap) object;
			        	 
			        	 int val=0;
			        	 int sub=0;
			        	 if(record.get("yesno").toString().equals("NO")) {
			        		  val=0;
			        	 }else {
			        		  val=1;
			        	 }
			        	 
			        	 for (Map.Entry<String, Integer> entry : hm.entrySet()) {
						 		if(record.get("sub").equals(entry.getKey())) {
						 			sub=entry.getValue();	
						 		}
						 }
			        	 
			        	 
			        	 
			        	 nna.add("("+LoginController.getschoo()+","+record.get("studentid")+","+sub+","+val+")");
			        	 
			 		    } 
			 		 
			 		    
			 		   String query=nna.toString().replace("[", "").replace("]", "").trim();
		 			 
		 		   
		 		  
		 		  	conn.prepareStatement(" DELETE w FROM subjectselection w  "
		 		  			+ " INNER JOIN registration r  ON w.studentid=r.id"
		 		  			+ " where r.school="+LoginController.getschoo()+" "
		 		 			+ " and `current form`="+classs2+" and stream="+stream+"; "
		 		 			
		 		 			+ " INSERT IGNORE INTO subjectselection(`school`,`Studentid`,`sub`,`yesno`) "
			 				+ " VALUES "+query+"").execute();; 
		       
			 				accountsmodel.response = "Operation successfull";
			
			 				
			 				
		 		  } catch (Exception e1) {
						e1.printStackTrace(); 
						accountsmodel.response="an error occured"; 
						return accountsmodel.jsondata="[{\"querystatus\" : \""+accountsmodel.response+"\"}]";
				 }finally {
					    if (conn != null) try { conn.close(); } catch (SQLException ignore) {}
					  }		
		 		   
		 		   
				         		
				
			 		
					return accountsmodel.jsondata="[{\"querystatus\" : \""+accountsmodel.response+"\"}]";
			
				     
				 }
		 	   
		 	   
		 	   
		 	   
		 	  @PostMapping(value = "/api/exam/copygrading")
		 	 	@ResponseBody
		 	 	public  String  copygrading(@RequestBody  JSONObject  search, HttpServletRequest request) {		    
		 	 		
		 		 Connection conn=null;
		 		 try {
		 			conn = DbConnector.dataSource.getConnection();
		 		 	
		 			
		 			if(search.get("categorytocopyto").equals("Overall")) {
		 				
		 				conn.prepareStatement("DELETE  FROM  overallgradingsystem "
		 						+ " where  school="+LoginController.getschoo()+"; "
		 								
		 						+ " INSERT IGNORE INTO overallgradingsystem(school,class,START,END,grade) "					 		 			
			 		 			+ " SELECT school,class,START,END,grade FROM gradingsystem  "
			 		 			+ " WHERE category="+search.get("categorytocopyfrom")+" "
			 		 			+ " and school="+LoginController.getschoo()+"").execute();; 
			       
		 				
		 			}
		 			
		 			else if(search.get("categorytocopyto").equals("ALL")) {
		 				
		 				
		 				conn.prepareStatement("DELETE  FROM  gradingsystem "
		 						+ " where category!="+search.get("categorytocopyfrom")+" "
		 						+ " and school="+LoginController.getschoo()+"; "
		 								
		 						+ " INSERT IGNORE INTO gradingsystem(school,class,category,START,END,grade) "					 		 			
			 		 			+ " SELECT g.school,g.class,s.id,START,END,grade FROM gradingsystem  g "
			 		 			+ " CROSS JOIN subjectgroups s "
			 		 			+ " WHERE g.category="+search.get("categorytocopyfrom")+" "
			 		 			+ " and g.school="+LoginController.getschoo()+""
			 		 			+ " and s.school="+LoginController.getschoo()+""
			 		 			+ " ORDER BY s.id,g.id; "
			 		 			
			 		 					+ " DELETE  FROM  overallgradingsystem "
				 						+ " where  school="+LoginController.getschoo()+"; "
		 								
				 						+ " INSERT IGNORE INTO overallgradingsystem(school,class,START,END,grade) "					 		 			
					 		 			+ " SELECT school,class,START,END,grade FROM gradingsystem  "
					 		 			+ " WHERE category="+search.get("categorytocopyfrom")+" "
					 		 			+ " and school="+LoginController.getschoo()+"").execute();; 
			       
		 				
		 			}
		 			
		 			else if(search.get("categorytocopyto").equals("ALLCLASSES")) {
		 				
		 				
		 			
		 				conn.prepareStatement("DELETE  FROM  gradingsystem "
		 						+ " where category!="+search.get("categorytocopyfrom")+" "
		 						+ " and school="+LoginController.getschoo()+"; "
			 		 			
								+ " INSERT IGNORE INTO gradingsystem(school,class,category,START,END,grade) "	
			 		 			+ " SELECT g.school,no,s.id,START,END,grade FROM gradingsystem  g "
			 		 			+ " CROSS JOIN subjectgroups s "
			 		 			+ " CROSS JOIN studentclass c "
			 		 			+ " WHERE g.category="+search.get("categorytocopyfrom")+" "
			 		 			+ " AND c.class!='Allumni' AND c.class!='Transfered' "
			 		 			+ " and g.school="+LoginController.getschoo()+""
			 		 			+ " and s.school="+LoginController.getschoo()+""
			 		 			+ " and c.school="+LoginController.getschoo()+""
			 		 			+ " ORDER BY no,s.id,g.id ;" 
			 		 			
			 		 			
			 		 			+ " DELETE  FROM  overallgradingsystem "
		 						+ " where  school="+LoginController.getschoo()+"; "
 								
		 						+ " INSERT IGNORE INTO overallgradingsystem(school,class,START,END,grade) "					 		 			
			 		 			+ " SELECT g.school,no,START,END,grade FROM gradingsystem  g"
			 		 			+ " CROSS JOIN studentclass c "			 		 			
			 		 			+ " WHERE c.class!='Allumni' AND c.class!='Transfered' and "
			 		 			+ " category="+search.get("categorytocopyfrom")+" "
			 		 			+ " and g.school="+LoginController.getschoo()+""
			 		 			+ " and c.school="+LoginController.getschoo()+"").execute();; 
				 				
		 				
		 			}
		 			
		 			
		 			else{
		 				
		 				conn.prepareStatement("DELETE  FROM  gradingsystem "
		 						+ " where category="+search.get("categorytocopyto")+" "
		 						+ " and school="+LoginController.getschoo()+"; "
			 		 					
		 						+ " INSERT IGNORE INTO gradingsystem(school,class,category,START,END,grade) "					 		 			
			 		 			+ " SELECT school,class,"+search.get("categorytocopyto")+",START,END,grade FROM gradingsystem  "
			 		 			+ " WHERE category="+search.get("categorytocopyfrom")+" "
			 		 			+ " and school="+LoginController.getschoo()+"").execute();; 
			       
		 		
		 				
		 			}
		 			
		 			
		 				accountsmodel.response = "Operation successfull";
					
		 			
		 			} catch (Exception e1) {
						e1.printStackTrace(); 
						accountsmodel.response="an error occured"; return accountsmodel.jsondata="[{\"querystatus\" : \""+accountsmodel.response+"\"}]";
		 			}finally {
					    if (conn != null) try { conn.close(); } catch (SQLException ignore) {}
					  }		
						 	 		
		 			return accountsmodel.jsondata="[{\"querystatus\" : \""+accountsmodel.response+"\"}]";
						 			
		 		 	
		 		}
		 	  
		 	  
		 	  
		 	  
		 	  	@PostMapping(value = "/api/exam/copyremarks")
		 	 	@ResponseBody
		 	 	public  String  copyremarks(@RequestBody  JSONObject  search, HttpServletRequest request) {		    
		 	 		
		 		Connection conn=null;
		 		
		 		 try {
		 			 
		 			conn = DbConnector.dataSource.getConnection();
		 		 	
		 			 
		 			if(search.get("categoryfrom").equals("Teachers") && search.get("categoryto").equals("Teachers")) {
		 				
		 				conn.prepareStatement(" DELETE  FROM  classteacherremarks "
		 						+ " where  school="+LoginController.getschoo()+" and teacher="+search.get("categorytocopyto")+"; "
		 								
		 						+ " INSERT IGNORE INTO classteacherremarks(school,Teacher,Grade,Remark) "					 		 			
			 		 			+ " SELECT school,"+search.get("categorytocopyto")+",Grade,Remark FROM classteacherremarks  "
			 		 			+ " WHERE Teacher="+search.get("categorytocopyfrom")+" "
			 		 			+ " and school="+LoginController.getschoo()+"").execute();; 
			       
		 				
		 			}
		 			
		 			
		 			
		 			
		 			else if(search.get("categoryfrom").equals("category") 
		 					&& search.get("categoryto").equals("category") 
		 					&& !search.get("categorytocopyto").equals("Principal")
		 					&& !search.get("categorytocopyfrom").equals("Principal")
		 					&& !search.get("categorytocopyto").equals("ALL")) {
		 				
		 				conn.prepareStatement("DELETE  FROM  remarks "
		 						+ " where  school="+LoginController.getschoo()+" and Category="+search.get("categorytocopyto")+"; "
		 								
		 						+ " INSERT IGNORE INTO remarks(school,Category,Grade,Remark) "					 		 			
			 		 			+ " SELECT school,"+search.get("categorytocopyto")+",Grade,Remark FROM remarks  "
			 		 			+ " WHERE Category="+search.get("categorytocopyfrom")+" "
			 		 			+ " and school="+LoginController.getschoo()+"").execute();; 
			       
		 				
		 			}
		 			
		 			
		 			
		 			
		 			else if(search.get("categoryfrom").equals("category") 
		 					&& search.get("categorytocopyto").equals("Principal")) {
		 				
		 				conn.prepareStatement("DELETE FROM principalremarks "
		 						+ " where  school="+LoginController.getschoo()+"; "
		 								
		 						+ " INSERT IGNORE INTO principalremarks(school,Grade,Remark) "					 		 			
			 		 			+ " SELECT school,Grade,Remark FROM remarks  "
			 		 			+ " WHERE Category="+search.get("categorytocopyfrom")+" "
			 		 			+ " and school="+LoginController.getschoo()+"").execute();; 
			       
		 				
		 			}
		 			
		 			
		 			
		 			
		 			
		 			else if(search.get("categorytocopyfrom").equals("Principal") && search.get("categoryto").equals("category")) {
		 				
		 				conn.prepareStatement("DELETE  FROM  remarks "
		 						+ " where school="+LoginController.getschoo()+" "
		 						+ " and category="+search.get("categorytocopyto")+"; "
		 								
								+ " INSERT IGNORE INTO remarks(school,category,Grade,Remark) "					 		 			
								+ " SELECT school,"+search.get("categorytocopyto")+",Grade,Remark FROM principalremarks  "
								+ " WHERE  school="+LoginController.getschoo()+"").execute();; 

		 				
		 			}
		 			
		 			
		 			
		 			
		 			
		 			
		 			else if( search.get("categoryfrom").equals("category") && 
		 					!search.get("categorytocopyto").equals("Principal")   && 
		 					!search.get("categorytocopyfrom").equals("Principal") && 
		 					 search.get("categorytocopyto").equals("ALL")) {
		 				
		 				
		 				conn.prepareStatement("DELETE  FROM  remarks "
		 						+ " where category!="+search.get("categorytocopyfrom")+" "
		 						+ " and school="+LoginController.getschoo()+"; "
		 								
		 						+ " INSERT IGNORE INTO remarks(school,Category,Grade,Remark) "					 		 			
			 		 			+ " SELECT g.school,s.id,Grade,Remark FROM remarks  g "
			 		 			+ " CROSS JOIN subjectgroups s "
			 		 			+ " WHERE g.category="+search.get("categorytocopyfrom")+" "
			 		 			+ " and g.school="+LoginController.getschoo()+""
			 		 			+ " and s.school="+LoginController.getschoo()+""
			 		 			+ " ORDER BY s.id,g.id; "
			 		 			
			 		 			
			 		 			
			 		 			+ " DELETE  FROM  principalremarks "
				 				+ " where  school="+LoginController.getschoo()+"; "
		 								
				 				+ " INSERT IGNORE INTO principalremarks(school,Grade,Remark) "					 		 			
					 		 	+ " SELECT school,grade,Remark FROM remarks  "
					 		 	+ " WHERE category="+search.get("categorytocopyfrom")+" "
					 		 	+ " and school="+LoginController.getschoo()+" ;"
					 		 		
					 		 	
					 		 	
								+ " DELETE  FROM  classteacherremarks "
								+ " where  school="+LoginController.getschoo()+"; "
								
								+ " INSERT IGNORE INTO classteacherremarks(school,Teacher,Grade,Remark) "					 		 			
								+ " SELECT r.school,s.id,grade,Remark FROM remarks r "
								+ " CROSS JOIN classteachers s "			 		 			
								+ " WHERE category="+search.get("categorytocopyfrom")+" "
								+ " and r.school="+LoginController.getschoo()+" "
								+ " and s.school="+LoginController.getschoo()+" order by s.teacher;"

					 		 	+ "").execute();; 
			       
		 				
		 			}
		 			
		 			
		 			
		 			else if(search.get("categoryfrom").equals("Principal") && search.get("categorytocopyto").equals("ALL")) {
		 				
		 				
		 				conn.prepareStatement("DELETE  FROM  remarks "
		 						+ " where  school="+LoginController.getschoo()+"; "
		 								
		 						+ " INSERT IGNORE INTO remarks(school,Category,Grade,Remark) "					 		 			
			 		 			+ " SELECT g.school,s.id,Grade,Remark FROM principalremarks  g "
			 		 			+ " CROSS JOIN subjectgroups s  "
			 		 			+ " and g.school="+LoginController.getschoo()+""
			 		 			+ " and s.school="+LoginController.getschoo()+""
			 		 			+ " ORDER BY s.id,g.id; "
			 		 			
								+ " DELETE  FROM  classteacherremarks "
								+ " where school="+LoginController.getschoo()+"; "
								
								+ " INSERT IGNORE INTO classteacherremarks(school,Teacher,Grade,Remark) "					 		 			
								+ " SELECT school,s.id,grade,Remark FROM principalremarks r "
								+ " CROSS JOIN classteachers s "			 		 			
								+ " WHERE  	r.school="+LoginController.getschoo()+" "
								+ " and 	s.school="+LoginController.getschoo()+" ;"

					 		 	+ "").execute();; 
			       
		 				
		 			}
		 			
		 			
		 			
		 			
		 			
		 				accountsmodel.response = "Operation successfull";
					
		 			
		 			} catch (Exception e1) {
						e1.printStackTrace(); 
						accountsmodel.response="an error occured"; return accountsmodel.jsondata="[{\"querystatus\" : \""+accountsmodel.response+"\"}]";
		 			}finally {
					    if (conn != null) try { conn.close(); } catch (SQLException ignore) {}
					  }		
						 	 		
		 			return accountsmodel.jsondata="[{\"querystatus\" : \""+accountsmodel.response+"\"}]";
						 			
		 		 	
		 		}
		 	   
		 	 
		 	 
		 	 
		 	   
		 	   
		 	  @SuppressWarnings("unchecked")
		 	   @PostMapping(value = "/api/exam/processexam")
			 	@ResponseBody
			 	public  String  processexam(@RequestBody  JSONObject  search, HttpServletRequest request) {	
			 	  
		 		   try {
		 			   
		 			   
		 			   
		 		   } catch (Exception e1) {
						e1.printStackTrace(); 
						accountsmodel.response="an error occured"; return accountsmodel.jsondata="[{\"querystatus\" : \""+accountsmodel.response+"\"}]";
		 		   }	
		 		   
			 		
					return accountsmodel.jsondata="[{\"querystatus\" : \""+accountsmodel.response+"\"}]";
			
				     
				 }
		 	   
		 	   
		 	  
		 	  	@PostMapping(value = "/api/exam/refreshexam")
			 	@ResponseBody
			 	public  String  refreshexam(@RequestBody  JSONObject  search, HttpServletRequest request) {	
			 	  
		 	  		Connection conn=null;
			 		
			 		 try {
			 			 
			 			conn = DbConnector.dataSource.getConnection();
			 	
			 			String groups=search.get("exams").toString();
			 			
			 			
					 	List<String> crunchifyListNew=new ArrayList<String>();		
				    	crunchifyListNew = Arrays.asList(groups.replace("[", "").replace("]", "").split("\\s*,\\s*"));
				    	
				    	for(String s: crunchifyListNew){
				    				    			
				    			conn.prepareStatement("INSERT IGNORE INTO `exams` (`studentid`,`exam`,`sub`)  "
				 					+ " SELECT r.id,"+s+","
				 					+ " subid  FROM `subjects1` "
				 					+ " INNER JOIN registration r on `current form`=class "
				 					+ " INNER JOIN allexams a on a.class=r.`current form` "
				 					+ " LEFT JOIN   (SELECT studentid FROM exams WHERE exam="+s+" GROUP BY studentid) "
				 					+ " j ON j.studentid=r.id "
				 					+ " WHERE a.`id`="+s+" and r.school="+LoginController.getschoo()+" "
				 					+ " AND j.studentid IS null ORDER BY `ORDER` ASC").execute(); 
		 			  
				    	}
				    	
				    	
				    	conn.prepareStatement("INSERT IGNORE INTO `subjectselection` (`school`,`studentid`,`sub`,`YesNo`)  "
			 					+ " SELECT r.school,r.id,subid,'1'  FROM `subjects1` "
			 					+ " INNER JOIN registration r on `current form`=class "
			 					+ " WHERE  r.school="+LoginController.getschoo()+" ").execute(); 
	 		
				    	
				    	accountsmodel.response = "Operation successfull";
						
				    	
		 		   } catch (Exception e1) {
						e1.printStackTrace(); 
						accountsmodel.response="an error occured"; 
						return accountsmodel.jsondata="[{\"querystatus\" : \""+accountsmodel.response+"\"}]";
							
		 		   }finally {
					    if (conn != null) try { conn.close(); } catch (SQLException ignore) {}
					  }		
		 		   
			 		
					return accountsmodel.jsondata="[{\"querystatus\" : \""+accountsmodel.response+"\"}]";
			
				     
				 }
		 	  
		 	  	
		 	  
		 	  	
		 	  	
		 	  	@PostMapping(value = "/api/exam/stopsendng")
			 	@ResponseBody
			 	public  String  stopsendng(@RequestBody  JSONObject  search, HttpServletRequest request) {	
			 	
			 		continuesending=search.get("stop").toString()+" :: "+LoginController.getloggedinuserid();
			 		
			 		accountsmodel.response = "Operation canceled";						
	 				
		 	  		return accountsmodel.jsondata="[{\"querystatus\" : \""+accountsmodel.response+"\"}]";			
				    
		 	  	}
		 	 
		 	  	
		 	  	String continuesending="0";
		 	  	
		 	  	
		 	  	
		 	  	
		 	  	@PostMapping(value = "/api/exam/sendexam")
			 	@ResponseBody
			 	public  String  sendexam(@RequestBody  JSONObject  search, HttpServletRequest request) {	
		 	  		
		 	  		
				 
				 	continuesending=search.get("stop").toString()+" :: "+LoginController.getloggedinuserid();
		 	  		
				 	
		 	  		List<String> nheadersna = new ArrayList<>();
		 	    	List<String> nna = new ArrayList<>();
		 	    	List<String> nna2 = new ArrayList<>();
		 	    	String avgandmrksformula="";
		 	    	
 	    			
		 	  		Connection conn=null;
			 		
			 		 try {
			 			conn = DbConnector.dataSource.getConnection();
						 
			 			String username = "";      
						 String smsid = "";      
						 String apiKey = "";      
						 String sms_account = "";      
						 
					 
			 			
			 			 ResultSet rt1 = conn.prepareStatement("select SMSKEY,SMSUSERNAME,SMSID,sms_account from schooldetails where "
					    			+ " id='"+LoginController.getschoo()+"' ").executeQuery();
							while (rt1.next()) {
								username=rt1.getString("SMSUSERNAME");
						 		smsid=rt1.getString("SMSID");
						 		apiKey=rt1.getString("SMSKEY");
						 		sms_account=rt1.getString("sms_account");						}
			 			 
			 			conn = DbConnector.dataSource.getConnection();
			 			String phone="";
			 			if(sms_account.equals("AFRICASTALKING") || sms_account.equals("Africastalking")) { 
			 				phone="+254";
			 			}else {
			 				phone="0";					 			
			 			}
	 	    			
			 	    	 ResultSet rt = conn.prepareStatement("SELECT s.*,a.*,shortname,avgtotmrksformula,"
			 	    	 			+ "  FROM `subjects1` s "
			 	    			 	+ " INNER JOIN allexams a on s.class=a.Class "									
			 	    			 	+ " INNER JOIN studentclass t on no=a.Class "									
			 	    			 	+ " INNER JOIN analysisformulaandreportformdatesdates l on l.class=no "									
				    			 	+ " where s.school='"+LoginController.getschoo()+"' "
			 	    			 	+ " and a.id='"+search.get("id").toString()+"'"
									+ " ORDER BY `ORDER`").executeQuery();
			 	    	 
			 	    	 
    			
    			
			 	    	  classsforreport=0;
			 	    	       int examid=0;
								
						  		while (rt.next()) {
						  			
						  			avgandmrksformula=rt.getString("avgtotmrksformula");
						  			
						  			nheadersna.add("'"+rt.getString("code")+": ',`"+rt.getString("code")+"`,' '");
							  		
							  		nna.add("Case when s.code='"+rt.getString("code")+"'  "
							  				+ " AND score>0    then concat(Score,'',grade) ELSE '' END AS '"+rt.getString("code")+"'");
							  		
							  		nna2.add("max(`"+rt.getString("code")+"`)  AS '"+rt.getString("code")+"'");
							  		
							  		classsforreport=rt.getInt("class");
							  		classsforreportshortname=rt.getString("shortname");
							  		
							  		examid=rt.getInt("id");
							  		
				                 }
						  		
						  		
						  		avgandmrksformula=avgandmrksformula.replace("classid", ""+classsforreport).
						  				replace("schoolid", ""+LoginController.getschoo()).
						  				replace("examid", ""+examid);
						
					  
						  		String query1 = nna2.toString().replace("[", "").replace("]", "").replace(",,", "").trim();
					 	        
						  		String query = nna.toString().replace("[", "").replace("]", "").replace(",,", "").trim();
						  		
						  		String headers = nheadersna.toString().replace("[", "").replace("]", "").replace(",,", "").trim();
						  		
						  			
						  						SQL="SELECT fphone,mphone,gphone,concat(Name,' ',`Adm no`,' ','MG: ',MG,' ',`MN MKS`,' POS: ',"
						  								+ " Case when fp>0 then fp ELSE 'X' END,' ',"
						  								+ " "+headers+") as msg"
						  								
						  								+" from (SELECT `Adm no`,Name,fphone,mphone,gphone,CLS,"
								  						+ " KCPE,CNT,"+query1+",`TOT MKS`,`MN MKS`,'' AS VAP,'-' AS DEV,GRD AS MG,CP,FP "
														+ " from (SELECT Id,`Adm no`,"
														+ " concat('"+classsforreportshortname+"',' ',Stream) AS ClS,"
														+ " Stream,KCPE,Name,fphone,mphone,gphone,"+query+""
														+ " FROM subjects1 s"
														
														+ " INNER JOIN (SELECT id,s.Stream,`Adm no`,Egrade AS KCPE,"
														+ " concat(`First name`,' ',`Second name`)  Name,"
														
														+ " case when CHAR_LENGTH(RIGHT(`Fathers phone`,9))>=9 then "
														+ " concat('"+phone+"',RIGHT(`Fathers phone`,9)) else '' end as 'fphone',"
														+ " case when CHAR_LENGTH(RIGHT(`MotherPhone`,9))>=9   then "
														+ " concat('"+phone+"',RIGHT(MotherPhone,9))       else '' end as 'mPhone',"
														+ " case when CHAR_LENGTH(RIGHT(`Guardianphone`,9))>=9 then "
														+ " concat('"+phone+"',RIGHT(Guardianphone,9))   else '' end as 'gphone',"

														
														+ "`current form`	FROM registration r "
														+ " INNER join studentstreams s ON streanid=r.stream "
														+ " WHERE `current form`="+classsforreport+"  "
														+ " AND r.school="+LoginController.getschoo()+" )r    "
														+ " ON s.class=r.`current form`"
														
														
														+ " 	INNER JOIN (  "
				
														+ " 		SELECT d.*,"
														+ " 		Case when score>0 then g.Grade ELSE '' END AS Grade FROM "
														+ " 		(SELECT studentid,sub,IFNULL(ROUND((score/outof)*100),0) as Score,"
														+ " 		outof	FROM exams e WHERE e.Exam="+examid+")d "
														+ " 		inner JOIN (SELECT * FROM subjects1 WHERE "
														+ " 		class="+classsforreport+" and  school="+LoginController.getschoo()+" )s ON subid=d.sub "
														+ " 		inner JOIN (SELECT * FROM gradingsystem "
														+ " 		WHERE class="+classsforreport+" and  "
														+ " 		school="+LoginController.getschoo()+" )t ON t.Category=s.category "
														+ " 		AND score BETWEEN `START` AND `end` and s.class=t.class "
														+ " 		inner JOIN grades g ON g.id=t.grade "
														+ " 		where   g.school="+LoginController.getschoo()+" ORDER BY studentid,d.sub "
														
														+ " 		)e    ON r.id=e.studentid  "
														+ " 		and e.sub=subid AND s.school="+LoginController.getschoo()+" "
														+ " 		and class="+classsforreport+"  "
														+ " 		GROUP BY studentid,subid )j"
														
														+ " 		LEFT JOIN ("+avgandmrksformula+")l"
														
														+ "			ON id=l.studentid"
														
														+ "  		GROUP BY j.id ORDER BY CASE  WHEN fp !=0 THEN 1  ELSE 2 END,"
														+ "			fp asc,`MN MKS`  DESC,`TOT MKS` DESC"
														+ " )k";
						  						
						  						
						  						List<sendsms>  lists =new ArrayList<sendsms>(); ;
						  						
						  						
						  						ResultSet rt11 = conn.prepareStatement(SQL).executeQuery();
						  						while (rt11.next()) {
						  							
						  							accountsmodel.liststring.clear();
						  							
						  							if(!rt11.getString("fphone").equals("")) {
						  								accountsmodel.liststring.add(rt11.getString("fphone"));
						  							}
						  							if(!rt11.getString("mphone").equals("")) {
						  								accountsmodel.liststring.add(rt11.getString("mphone"));
						  							}
						  							if(!rt11.getString("gphone").equals("")) {
						  								accountsmodel.liststring.add(rt11.getString("gphone"));
						  							}
						  							
						  							
						  							lists.add(new sendsms(accountsmodel.liststring, rt11.getString("msg")));												
						  							
						  							
						  						}
						  						
						  						
						  						
						  						if (sms_account.equals("Mobitech")) {
													for (sendsms element : lists) {
														
														if(continuesending.equals("1 :: "+LoginController.getloggedinuserid())) {
															accountsmodel.response = "Operation canceled";						
															conn.prepareStatement("UPDATE allexams  set `Sms status`='Partially Sent' where id="+examid+"").execute();; 
												 			return accountsmodel.jsondata="[{\"querystatus\" : \""+accountsmodel.response+"\"}]";			
													    }
										 				
														smsController.sendSMSMobitech(accountsmodel.liststring.toString().
																replace("[", "").replace("]", "").replaceAll("\\s+","") , 
																element.sms,0,username,apiKey,smsid);
							  						}
						  						} else {
													for (sendsms element : lists) {
														
														if(continuesending.equals("1 :: "+LoginController.getloggedinuserid())){
															accountsmodel.response = "Operation canceled";						
															conn.prepareStatement("UPDATE allexams  set `Sms status`='Partially Sent' where id="+examid+"").execute();; 
															return accountsmodel.jsondata="[{\"querystatus\" : \""+accountsmodel.response+"\"}]";			
													    }
														
														String[] stringArray = element.phones.toArray(new String[0]);
														smsController.sendSMSAfricastalking(stringArray, element.sms,0,username,apiKey,smsid);
														
							  						}
						  						}
						  						
						  						
						  						
						  			conn.prepareStatement("UPDATE allexams  set `Sms status`='Sent' where id="+examid+"").execute();; 
						 			       	
						  						
			 			
						  			accountsmodel.response = "Operation successfull";
						  			
				    	
			 		   } catch (Exception e1) {
							e1.printStackTrace(); 
							accountsmodel.response="an error occured"; return accountsmodel.jsondata="[{\"querystatus\" : \""+accountsmodel.response+"\"}]";
								
			 		   }finally {
					    if (conn != null) try { conn.close(); } catch (SQLException ignore) {}
					  }		
		 		   
			 		
			 		 
					return accountsmodel.jsondata="[{\"querystatus\" : \""+accountsmodel.response+"\"}]";			
				     
				 }
		 	  	
		 	  	
		 	  	
		 	  	
		 	  	
		 	  	public class sendsms {
		 			
		 			private List<String> phones;
		 			private String sms;
		 			
		 			public sendsms(List<String> name, String sms) {
		 				this.phones = name;
		 				this.sms = sms;		 				
		 			}

		 			
		 			public List<String> getname(List<String> list) {
		 				return list;
		 			}

		 			public String getcompanyname() {
		 				return sms;
		 			}

		 			
		 		}
		 	  	
		 	  	
		 	  	
		 	  	
 
		 	 public DynamicReport buildReport(String reporttemplate) throws Exception {
		 		 
		 	    Style detailStyledontshow = new Style("detail2");
		 	    detailStyledontshow.setFont(new Font(0, null, false));
		 	    detailStyledontshow.setBorder(Border.NO_BORDER());
		 	    detailStyledontshow.setBorderBottom(Border.THIN());
		 	    
		 	    Style detailStyle = new Style("detail");
		 	    detailStyle.setFont(new Font(7, null, false));
		 	    detailStyle.setHorizontalAlign(HorizontalAlign.CENTER);
		 	    detailStyle.setBorder(Border.THIN());
		 	    
		 	    
		 	    Style detailStylebold = new Style("detail");
		 	    detailStylebold.setFont(new Font(7, null, true));
		 	    detailStylebold.setHorizontalAlign(HorizontalAlign.CENTER);
		 	    detailStylebold.setBorder(Border.THIN());
		 	    
		 	    
		 	    Style detailStyle1 = new Style("detail1");
		 	    detailStyle1.setFont(new Font(7, null, true));
		 	    detailStyle1.setHorizontalAlign(HorizontalAlign.LEFT);
		 	    detailStyle1.setBorder(Border.THIN());
		 	    
		 	   
		 	    Style headerStyle = new Style("header");
		 	    headerStyle.setFont(new Font(7, null, true));
		 	    headerStyle.setBorderBottom(Border.THIN());
		 	    headerStyle.setBackgroundColor(RGBColor.gray);
		 	    headerStyle.setTextColor(RGBColor.white);
		 	    headerStyle.setHorizontalAlign(HorizontalAlign.CENTER);
		 	    headerStyle.setVerticalAlign(VerticalAlign.MIDDLE);
		 	    headerStyle.setTransparency(Transparency.OPAQUE);
		 	    
		 	    Style headerStyledontshow = new Style("header2");
		 	    headerStyledontshow.setFont(new Font(0, null, true));
		 	    headerStyledontshow.setBorderBottom(Border.NO_BORDER());
		 	    headerStyledontshow.setHorizontalAlign(HorizontalAlign.CENTER);
		 	    headerStyledontshow.setVerticalAlign(VerticalAlign.MIDDLE);
		 	    headerStyledontshow.setTransparency(Transparency.OPAQUE);
		 	    
		 	    Style titleStyle = new Style("titleStyle");
		 	    titleStyle.setFont(new Font(7, null, true));
		 	    titleStyle.setHorizontalAlign(HorizontalAlign.CENTER);
		 	    
		 	    Style titleStyle1 = new Style("titleStyle1");
		 	    titleStyle1.setFont(new Font(7, null, true));
		 	    titleStyle1.setHorizontalAlign(HorizontalAlign.LEFT);
		 	    
		 	    Style titleStylegg = new Style("titleStylegg");
		 	    titleStylegg.setFont(new Font(7, null, true));
		 	    titleStylegg.setHorizontalAlign(HorizontalAlign.CENTER);
		 	    
		 	    Style subtitlestyle = new Style("titleStyle");
		 	    subtitlestyle.setFont(new Font(7, null, true));
		 	    subtitlestyle.setHorizontalAlign(HorizontalAlign.CENTER);
		 	    
		 	    
		 	    Style subtitleStyle = Style.createBlankStyle("subtitleStyle","subtitleParent");
		 	    subtitleStyle.setFont(Font.GEORGIA_SMALL_BOLD);
		   		
		   		
		   		Style subtitleStyleParent = new Style("subtitleParent");
		   		subtitleStyleParent.setBackgroundColor(RGBColor.CYAN);
		   		subtitleStyleParent.setTransparency(Transparency.OPAQUE);
		 	    
		 	    Style groupVariables = new Style("groupVariables");
		 	    groupVariables.setFont(new Font(7, null, true));
		 	    groupVariables.setTextColor(RGBColor.BLUE);
		 	    groupVariables.setHorizontalAlign(HorizontalAlign.CENTER);
		 	    groupVariables.setVerticalAlign(VerticalAlign.MIDDLE);
		 	    groupVariables.setBorder(Border.PEN_1_POINT());
		 	    
		 	    
		 	    
		 	    DynamicReportBuilder drb = new DynamicReportBuilder();
		 	    
		 	    Integer margin = 3;
		 	    
		 	    drb.setTitleStyle(titleStyle).setTitle(Headermsg).setDetailHeight((new Integer(0)).intValue()).setLeftMargin(margin.intValue())
		 	      .setRightMargin(margin.intValue()).setTopMargin(margin.intValue()).setBottomMargin(margin.intValue());
		 	    drb.setSubtitleStyle(titleStyle).setSubtitle(subttlestyle).setDetailHeight((new Integer(0)).intValue()).setLeftMargin(margin.intValue())
		 	      .setRightMargin(margin.intValue()).setTopMargin(margin.intValue()).setBottomMargin(margin.intValue());
		 	    
		 	    
//		 	   drb.setTitle("November " + getYear() +" sales report")
//		 	   			.setSubtitle("The items in this report correspond "
//		 	   					+"to the main products: DVDs, Books, Foods and Magazines")
//		 	   			.setDetailHeight(15)
//		 	  			.setMargins(30, 20, 30, 15)
//		 	    			.setDefaultStyles(titleStyle, subtitleStyle, headerStyle, null)
//		 	   			.addStyle(subtitleStyleParent);
		 	    
		 	    
		 	    if (Headermsg.contains("RAW MARKS")) {
		 	    	
		 	      AbstractColumn admno = ColumnBuilder.getNew().setColumnProperty("Adm no", String.class.getName())
		 	        .setTitle("Adm no").setWidth(30).setStyle(detailStyle).setHeaderStyle(headerStyle)
		 	        .build();
		 	      drb.addColumn(admno);
		 	      
		 	      AbstractColumn name = ColumnBuilder.getNew().setColumnProperty("Name", String.class.getName())
		 	        .setTitle("Name").setWidth(130).setStyle(detailStyle1).setHeaderStyle(headerStyle)
		 	        .build();
		 	      drb.addColumn(name);
		 	      
		 	      
		 	     AbstractColumn sub = null;
		 	     
		 	     Connection conn=null;
				 
		 	     try {
					 conn = DbConnector.dataSource.getConnection();
					 ResultSet rs = conn.prepareStatement("SELECT `code` FROM subjects1 where class="+classsforreport+" order by `order` ").executeQuery();
			 	      while (rs.next()) {
			 	    	 sub = ColumnBuilder.getNew()
			 	          .setColumnProperty(rs.getString("code"), String.class.getName())
			 	          .setTitle(rs.getString("code")).setStyle(detailStyle)
			 	          .setHeaderStyle(headerStyle).build();
			 	    	sub.setFixedWidth(true);
			 	        drb.addColumn(sub);
			 	      }
			 	      
					 } catch (Exception e1) {
							e1.printStackTrace(); 
					 }finally {
						    if (conn != null) try { conn.close(); } catch (SQLException ignore) {}
				  }	
		 	      
		 	     
		 	      
		 	    } 
		 	    
		 	    else if (Headermsg.contains("MERIT LIST")) {
		 	    	
			 	      AbstractColumn admno = ColumnBuilder.getNew().setColumnProperty("Adm no", String.class.getName())
			 	        .setTitle("Adm no").setWidth(30).setStyle(detailStyle).setHeaderStyle(headerStyle)
			 	        .build();
			 	      drb.addColumn(admno);
			 	      
			 	      AbstractColumn name = ColumnBuilder.getNew().setColumnProperty("Name", String.class.getName())
			 	        .setTitle("Name").setWidth(140).setStyle(detailStyle1).setHeaderStyle(headerStyle)
			 	        .build();
			 	     //name.setFixedWidth(true);
			 	     drb.addColumn(name);
			 	      
			 	     name = ColumnBuilder.getNew().setColumnProperty("CLS", String.class.getName())
					 	        .setTitle("CLS").setWidth(30).setStyle(detailStyle).setHeaderStyle(headerStyle)
					 	        .build();
					 	 drb.addColumn(name);
					 	      
//					 name = ColumnBuilder.getNew().setColumnProperty("KCPE", String.class.getName())
//							 	        .setTitle("KCPE").setWidth(30).setStyle(detailStyle).setHeaderStyle(headerStyle)
//							 	        .build();
//						drb.addColumn(name);
//							 	      
					 name = ColumnBuilder.getNew().setColumnProperty("CNT", String.class.getName())
								.setTitle("CNT").setWidth(30).setStyle(detailStyle).setHeaderStyle(headerStyle) .build();
					 drb.addColumn(name);
			 	      
			 	      
			 	     AbstractColumn sub = null;
			 	     
			 	     Connection conn=null;
					 
			 	     try {
						 conn = DbConnector.dataSource.getConnection();
						 ResultSet rs = conn.prepareStatement("SELECT `code` FROM subjects1 where class="+classsforreport+"  order by `order` ").executeQuery();
				 	      while (rs.next()) {
				 	    	 sub = ColumnBuilder.getNew()
				 	          .setColumnProperty(rs.getString("code"), String.class.getName())
				 	          .setTitle(rs.getString("code")).setWidth(35).setStyle(detailStyle)
				 	          .setHeaderStyle(headerStyle).build();
				 	    	//sub.setFixedWidth(true);
				 	        drb.addColumn(sub);
				 	      }
				 	      
						 } catch (Exception e1) {
								e1.printStackTrace();
								 accountsmodel.jsondata="[{\"querystatus\" : \""+e1.getMessage()+"\"}]";
						 }finally {
							    if (conn != null) try { conn.close(); } catch (SQLException ignore) {}
					  }	
			 	      
			 	     
			 	    name = ColumnBuilder.getNew().setColumnProperty("TOT MKS", Integer.class.getName())
							.setTitle("TOT MKS").setWidth(38).setStyle(detailStyle).setHeaderStyle(headerStyle) .build();
					drb.addColumn(name);
					
					   
					 AbstractColumn MEAN = ColumnBuilder.getNew().setColumnProperty("MN MKS", Double.class.getName())
				 	           .setTitle("MN MKS").setWidth(45).setPattern(".00").setStyle(groupVariables)
				 	           .setHeaderStyle(headerStyle).build();
				 	         drb.addColumn(MEAN);
				 
				 	         
				 name = ColumnBuilder.getNew().setColumnProperty("TOT PTS", Integer.class.getName())
									.setTitle("TOT PTS").setWidth(38).setStyle(detailStyle).setHeaderStyle(headerStyle) .build();
							drb.addColumn(name);
							
				 	         
				  MEAN = ColumnBuilder.getNew().setColumnProperty("MN PTS", Double.class.getName())
						 	           .setTitle("MN PTS").setWidth(45).setPattern(".00").setStyle(groupVariables)
						 	           .setHeaderStyle(headerStyle).build();
						 	         drb.addColumn(MEAN);
						 
					
					name = ColumnBuilder.getNew().setColumnProperty("VAP", String.class.getName())
							.setTitle("VAP").setWidth(35).setStyle(detailStyle).setHeaderStyle(headerStyle) .build();
					drb.addColumn(name);
					
					name = ColumnBuilder.getNew().setColumnProperty("DEV", String.class.getName())
							.setTitle("DEV").setWidth(35).setStyle(detailStyle).setHeaderStyle(headerStyle) .build();
					drb.addColumn(name);
					
					name = ColumnBuilder.getNew().setColumnProperty("GRD", String.class.getName())
							.setTitle("GRADE").setWidth(33).setStyle(detailStyle).setHeaderStyle(headerStyle) .build();
					drb.addColumn(name);
					
					name = ColumnBuilder.getNew().setColumnProperty("CP", String.class.getName())
							.setTitle("CP").setWidth(30).setStyle(detailStyle).setHeaderStyle(headerStyle) .build();
					drb.addColumn(name);
					
					name = ColumnBuilder.getNew().setColumnProperty("FP", String.class.getName())
							.setTitle("FP").setWidth(28).setStyle(detailStyle).setHeaderStyle(headerStyle) .build();
					drb.addColumn(name);
					
			 	      
			 	    } 
		 	    
		 	    
		 	    
		 	    
		 	   else if (Headermsg.contains("SUBJECT ANALYSIS FOR")) {
		 	    	
		 		  AbstractColumn sub = null;
			 	     
		 		 AbstractColumn  subject = ColumnBuilder.getNew().setColumnProperty("SUBJECT", String.class.getName())
		 			        .setTitle("SUBJECT").setWidth(90).setStyle(detailStyle1).setHeaderStyle(headerStyle)
		 			        .build();
		 			      drb.addColumn(subject);
		 			      
		 			      AbstractColumn Stream = ColumnBuilder.getNew().setColumnProperty("STREAM", String.class.getName())
		 			        .setTitle("STREAM").setWidth(45).setStyle(detailStyle).setHeaderStyle(headerStyle)
		 			        .build();
		 			      drb.addColumn(Stream);
		 			      AbstractColumn entry = ColumnBuilder.getNew().setColumnProperty("ENTRY", Integer.class.getName())
		 			        .setTitle("ENTRY").setWidth(28).setStyle(detailStyle).setHeaderStyle(headerStyle)
		 			        .build();
		 			      drb.addColumn(entry);
		 			      
			 	      
			 	      
			 	     
			 	     Connection conn=null;
					 
			 	     try {
			 	    	 
						 conn = DbConnector.dataSource.getConnection();
						 ResultSet rs = conn.prepareStatement("SELECT * FROM grades WHERE "
				 	    	 		+ "school="+LoginController.getschoo()+"").executeQuery();
				 	      while (rs.next()) {
				 	    	 sub = ColumnBuilder.getNew()
				 	          .setColumnProperty(rs.getString("Grade"), String.class.getName())
				 	          .setTitle(rs.getString("Grade")).setWidth(32).setStyle(detailStyle)
				 	          .setHeaderStyle(headerStyle).build();
				 	        drb.addColumn(sub);
				 	      }
				 	      
						 } catch (Exception e1) {
								e1.printStackTrace();
								 accountsmodel.jsondata="[{\"querystatus\" : \""+e1.getMessage()+"\"}]";
						 }finally {
							    if (conn != null) try { conn.close(); } catch (SQLException ignore) {}
						 }	
			 	      
			 	     
			 	    AbstractColumn MEAN = ColumnBuilder.getNew().setColumnProperty("MEAN", Float.class.getName())
			 	           .setTitle("MEAN").setWidth(28).setPattern("#,###.###").setStyle(groupVariables)
			 	           .setHeaderStyle(headerStyle).build();
			 	         drb.addColumn(MEAN);
			 	         
			 	      MEAN = ColumnBuilder.getNew().setColumnProperty("MNPTS", Float.class.getName())
					 	           .setTitle("MNPTS").setWidth(28).setPattern("#,###.###").setStyle(groupVariables)
					 	           .setHeaderStyle(headerStyle).build();
					 	         drb.addColumn(MEAN);
					 	        
			 	         
			 	         AbstractColumn GRD = ColumnBuilder.getNew().setColumnProperty("GRADE", String.class.getName())
			 	           .setTitle("GRADE").setWidth(28).setStyle(detailStyle).setHeaderStyle(headerStyle)
			 	           .build();
			 	         drb.addColumn(GRD);
			 	         AbstractColumn PMEAN = ColumnBuilder.getNew().setColumnProperty("PREV MEAN", Float.class.getName())
			 	           .setTitle("PREV MEAN").setWidth(38).setPattern("#,###.###").setStyle(groupVariables)
			 	           .setHeaderStyle(headerStyle).build();
			 	         drb.addColumn(PMEAN);
			 	         
			 	         sub = ColumnBuilder.getNew().setColumnProperty("PREV GRADE", String.class.getName())
			 	           .setTitle("PREV GRADE").setWidth(35).setStyle(groupVariables).setHeaderStyle(headerStyle)
			 	           .build();
			 	         drb.addColumn(sub);
			 	         
			 	         AbstractColumn DEV = ColumnBuilder.getNew().setColumnProperty("DEV", Float.class.getName()).setTitle("DEV")
			 	           .setWidth(28).setPattern("#,###.###").setStyle(groupVariables).setHeaderStyle(headerStyle)
			 	           .build();
			 	         drb.addColumn(DEV);
			 	         
			 	         
			 	        sub = ColumnBuilder.getNew().setColumnProperty("TEACHER", String.class.getName())
					 	           .setTitle("TEACHER").setWidth(90).setStyle(detailStyle).setHeaderStyle(headerStyle)
					 	           .build();
					 	         drb.addColumn(sub);
					 	        
					
			 	      
			 	        GroupBuilder gb2 = new GroupBuilder();
			 	       DJGroup g2 = gb2.setCriteriaColumn((PropertyColumn)subject)
			 	         .build();
			 	       drb.addGroup(g2);
			 	    } 
		 	       
		 	    
		 	    
		 	    
		 	  else if (Headermsg.contains("MEAN GRADE ANALYSIS FOR")) {
		 	    	
		 		  AbstractColumn sub = null;
			 	     
		 		 AbstractColumn   Stream = ColumnBuilder.getNew().setColumnProperty("STREAM", String.class.getName())
		 			        .setTitle("STREAM").setWidth(45).setStyle(detailStyle).setHeaderStyle(headerStyle)
		 			        .build();
		 			      drb.addColumn(Stream);
		 			      AbstractColumn entry = ColumnBuilder.getNew().setColumnProperty("ENTRY", Integer.class.getName())
		 			        .setTitle("ENTRY").setWidth(28).setStyle(detailStyle).setHeaderStyle(headerStyle)
		 			        .build();
		 			      drb.addColumn(entry);
			 	     
			 	     Connection conn=null;
					 
			 	     try {
			 	    	 
						 conn = DbConnector.dataSource.getConnection();
						 ResultSet rs = conn.prepareStatement("SELECT * FROM grades WHERE "
				 	    	 		+ "school="+LoginController.getschoo()+"").executeQuery();
				 	      while (rs.next()) {
				 	    	 sub = ColumnBuilder.getNew()
				 	          .setColumnProperty(rs.getString("Grade"), String.class.getName())
				 	          .setTitle(rs.getString("Grade")).setWidth(32).setStyle(detailStyle)
				 	          .setHeaderStyle(headerStyle).build();
				 	        drb.addColumn(sub);
				 	      }
				 	      
						 } catch (Exception e1) {
								e1.printStackTrace();
								 accountsmodel.jsondata="[{\"querystatus\" : \""+e1.getMessage()+"\"}]";
						 }finally {
							    if (conn != null) try { conn.close(); } catch (SQLException ignore) {}
						 }	
			 	      
			 	     
			 	    AbstractColumn MEAN = ColumnBuilder.getNew().setColumnProperty("MN MKS", Float.class.getName())
			 	           .setTitle("MN MKS").setWidth(28).setPattern("#,###.###").setStyle(groupVariables)
			 	           .setHeaderStyle(headerStyle).build();
			 	         drb.addColumn(MEAN);
			 	         
			 	         
			 	        MEAN = ColumnBuilder.getNew().setColumnProperty("MNPTS", Float.class.getName())
					 	           .setTitle("MNPTS").setWidth(28).setPattern("#,###.###").setStyle(groupVariables)
					 	           .setHeaderStyle(headerStyle).build();
					 	         drb.addColumn(MEAN);
			 	         
			 	         AbstractColumn GRD = ColumnBuilder.getNew().setColumnProperty("GRADE", String.class.getName())
			 	           .setTitle("GRADE").setWidth(28).setStyle(detailStyle).setHeaderStyle(headerStyle)
			 	           .build();
			 	         drb.addColumn(GRD);
			 	         AbstractColumn PMEAN = ColumnBuilder.getNew().setColumnProperty("PREV MEAN", Float.class.getName())
			 	           .setTitle("PREV MEAN").setWidth(38).setPattern("#,###.###").setStyle(groupVariables)
			 	           .setHeaderStyle(headerStyle).build();
			 	         drb.addColumn(PMEAN);
			 	         
			 	         sub = ColumnBuilder.getNew().setColumnProperty("PREV GRADE", String.class.getName())
			 	           .setTitle("PREV GRADE").setWidth(35).setStyle(groupVariables).setHeaderStyle(headerStyle)
			 	           .build();
			 	         drb.addColumn(sub);
			 	         
			 	         AbstractColumn DEV = ColumnBuilder.getNew().setColumnProperty("DEV", Float.class.getName()).setTitle("DEV")
			 	           .setWidth(28).setPattern("#,###.###").setStyle(groupVariables).setHeaderStyle(headerStyle)
			 	           .build();
			 	         drb.addColumn(DEV);
			 	         
			 	         
					
			 	    } 
		 	    
		 	    
		 	 else if (Headermsg.contains("SUBJECT RANKING")) {
		 	    	
		 		  AbstractColumn sub = null;
			 	     
		 		 AbstractColumn   Stream = ColumnBuilder.getNew().setColumnProperty("RANK", String.class.getName())
		 			        .setTitle("RANK").setWidth(45).setStyle(detailStyle).setHeaderStyle(headerStyle)
		 			        .build();
		 			      drb.addColumn(Stream);
		 			      
		 			      
		 			     Stream = ColumnBuilder.getNew().setColumnProperty("SUBJECT", String.class.getName())
				 			        .setTitle("SUBJECT").setWidth(120).setStyle(detailStyle1).setHeaderStyle(headerStyle)
				 			        .build();
				 			      drb.addColumn(Stream);
				 			      
		 			      
		 			      AbstractColumn entry = ColumnBuilder.getNew().setColumnProperty("ENTRY", Integer.class.getName())
		 			        .setTitle("ENTRY").setWidth(28).setStyle(detailStyle).setHeaderStyle(headerStyle)
		 			        .build();
		 			      drb.addColumn(entry);
			 	     
			 	     Connection conn=null;
					 
			 	     try {
			 	    	 
						 conn = DbConnector.dataSource.getConnection();
						 ResultSet rs = conn.prepareStatement("SELECT * FROM grades WHERE "
				 	    	 		+ "school="+LoginController.getschoo()+"").executeQuery();
				 	      while (rs.next()) {
				 	    	 sub = ColumnBuilder.getNew()
				 	          .setColumnProperty(rs.getString("Grade"), String.class.getName())
				 	          .setTitle(rs.getString("Grade")).setWidth(32).setStyle(detailStyle)
				 	          .setHeaderStyle(headerStyle).build();
				 	        drb.addColumn(sub);
				 	      }
				 	      
						 } catch (Exception e1) {
								e1.printStackTrace();
								 accountsmodel.jsondata="[{\"querystatus\" : \""+e1.getMessage()+"\"}]";
						 }finally {
							    if (conn != null) try { conn.close(); } catch (SQLException ignore) {}
						 }	
			 	      
			 	     
			 	    AbstractColumn MEAN = ColumnBuilder.getNew().setColumnProperty("MEAN", Float.class.getName())
			 	           .setTitle("MEAN").setWidth(32).setPattern("#,###.###").setStyle(groupVariables)
			 	           .setHeaderStyle(headerStyle).build();
			 	         drb.addColumn(MEAN);
			 	         
			 	     
			 	        MEAN = ColumnBuilder.getNew().setColumnProperty("MNPTS", Float.class.getName())
					 	           .setTitle("MNPTS").setWidth(32).setPattern("#,###.###").setStyle(groupVariables)
					 	           .setHeaderStyle(headerStyle).build();
					 	         drb.addColumn(MEAN);
			 	         
			 	         AbstractColumn GRD = ColumnBuilder.getNew().setColumnProperty("GRADE", String.class.getName())
			 	           .setTitle("GRADE").setWidth(28).setStyle(detailStyle).setHeaderStyle(headerStyle)
			 	           .build();
			 	         drb.addColumn(GRD);
			 	         AbstractColumn PMEAN = ColumnBuilder.getNew().setColumnProperty("PREV MEAN", Float.class.getName())
			 	           .setTitle("PREV MEAN").setWidth(38).setPattern("#,###.###").setStyle(groupVariables)
			 	           .setHeaderStyle(headerStyle).build();
			 	         drb.addColumn(PMEAN);
			 	         
			 	         sub = ColumnBuilder.getNew().setColumnProperty("PREV GRADE", String.class.getName())
			 	           .setTitle("PREV GRADE").setWidth(35).setStyle(groupVariables).setHeaderStyle(headerStyle)
			 	           .build();
			 	         drb.addColumn(sub);
			 	         
			 	         AbstractColumn DEV = ColumnBuilder.getNew().setColumnProperty("DEV", Float.class.getName()).setTitle("DEV")
			 	           .setWidth(28).setPattern("#,###.###").setStyle(groupVariables).setHeaderStyle(headerStyle)
			 	           .build();
			 	         drb.addColumn(DEV);
			 	         
			 	        GRD = ColumnBuilder.getNew().setColumnProperty("PASS", String.class.getName())
					 	           .setTitle("PASS").setWidth(50).setStyle(detailStyle).setHeaderStyle(headerStyle)
					 	           .build();
					 	         drb.addColumn(GRD);
					 	         
					 	        GRD = ColumnBuilder.getNew().setColumnProperty("FAIL", String.class.getName())
							 	           .setTitle("FAIL").setWidth(50).setStyle(detailStyle).setHeaderStyle(headerStyle)
							 	           .build();
							 	         drb.addColumn(GRD);
					
							 	        GRD = ColumnBuilder.getNew().setColumnProperty("TEACHER", String.class.getName())
									 	           .setTitle("TEACHER").setWidth(60).setStyle(detailStyle).setHeaderStyle(headerStyle)
									 	           .build();
									 	         drb.addColumn(GRD);
							
			 	    }
		 	    
		 	    
		 	    System.out.println(SQL);
		 	    drb.setQuery(SQL, "sql");
		 	    drb.setUseFullPageWidth(true);
		 	    drb.setPageSizeAndOrientation(new Page(825, 523, false));
		 	    
		 	    drb.setTemplateFile(reporttemplate);

		 	    DynamicReport dr = drb.build();
		 	    return dr;
		 	    
		 	    
		 	  }
		 	  
		 	  String subttlestyle = "";
		 	  
		 	  String Headermsg = "";
		 	  
		 	  String SQL = "";
		 	  
		 	  protected static JasperPrint jp;
		 	  
		 	  protected JasperReport jr;
		 	  
		 	  protected final Map<String, Object> params = new HashMap<>();
		 	  
		 	  protected DynamicReport dr;
		 	  
		 	  public void testReport(String reporttemplate,String report) throws Exception {
		 		 Connection conn=null;
				 try {
					 conn = DbConnector.dataSource.getConnection();
		 		   
					 
				if(report.equals("rawmarks")) {
					
					
					dr = buildReport(reporttemplate);
					params.put("schid", ""+LoginController.getschoo());
					
					jr = DynamicJasperHelper.generateJasperReport(dr, getLayoutManager(), params);
			 	    if (conn != null) {
			 	      jp = JasperFillManager.fillReport(jr, params, conn);
			 	    } else {
			 	      jp = JasperFillManager.fillReport(jr, params);
			 	    } 
			 	    
			 	    jp.setOrientation(OrientationEnum.LANDSCAPE);
			 	    jp.setPageWidth(850);
			 	    jp.setPageHeight(560);
				}
				else if(report.equals("meritlist")) {
					
					dr = buildReport(reporttemplate);
					params.put("schid", ""+LoginController.getschoo());
					
					jr = DynamicJasperHelper.generateJasperReport(dr, getLayoutManager(), params);
			 	    if (conn != null) {
			 	      jp = JasperFillManager.fillReport(jr, params, conn);
			 	    } else {
			 	      jp = JasperFillManager.fillReport(jr, params);
			 	    } 
			 	    
			 	    jp.setOrientation(OrientationEnum.LANDSCAPE);
			 	    jp.setPageWidth(850);
			 	    jp.setPageHeight(560);
				}
				else if(report.equals("subjectanalysis")) {
					params.put("schid", ""+LoginController.getschoo());
					dr = buildReport(reporttemplate);
					
					jr = DynamicJasperHelper.generateJasperReport(dr, getLayoutManager(), params);
			 	    if (conn != null) {
			 	      jp = JasperFillManager.fillReport(jr, params, conn);
			 	    } else {
			 	      jp = JasperFillManager.fillReport(jr, params);
			 	    } 
			 	    
			 	    jp.setOrientation(OrientationEnum.LANDSCAPE);
			 	    jp.setPageWidth(850);
			 	    jp.setPageHeight(560);
				}
				else if(report.equals("bestclass")) {
					
					Map<String, Object> param = new HashMap<>();
					
					
					param.put("ExamName", ExamName);
					param.put("schid", ""+LoginController.getschoo());
					
					final InputStream ip = getClass().getResourceAsStream(reporttemplate);
			        
					JasperDesign jd = JRXmlLoader.load(ip);
					
					JRDesignQuery newQuery = new JRDesignQuery();
					newQuery.setText(SQL);
					jd.setQuery(newQuery);
					JasperReport jr = JasperCompileManager.compileReport(jd);
					
					 jp = JasperFillManager.fillReport(jr, param, conn);
				
				 }
				
				else if(report.equals("meangradeanalysis")) {
					
					params.put("schid", ""+LoginController.getschoo());
					dr = buildReport(reporttemplate);					
					jr = DynamicJasperHelper.generateJasperReport(dr, getLayoutManager(), params);
			 	    if (conn != null) {
			 	      jp = JasperFillManager.fillReport(jr, params, conn);
			 	    } else {
			 	      jp = JasperFillManager.fillReport(jr, params);
			 	    } 			 	    
			 	    jp.setOrientation(OrientationEnum.LANDSCAPE);
			 	    jp.setPageWidth(850);
			 	    jp.setPageHeight(560);
			 	    
			 	    
				}
				
				
				else if(report.equals("subjectranking")) {					
					
					params.put("schid", ""+LoginController.getschoo());
					dr = buildReport(reporttemplate);					
					jr = DynamicJasperHelper.generateJasperReport(dr, getLayoutManager(), params);
			 	    if (conn != null) {
			 	      jp = JasperFillManager.fillReport(jr, params, conn);
			 	    } else {
			 	      jp = JasperFillManager.fillReport(jr, params);
			 	    } 			 	    
			 	    jp.setOrientation(OrientationEnum.LANDSCAPE);
			 	    jp.setPageWidth(850);
			 	    jp.setPageHeight(560);
			 	    
			 	    
				}
				
		 	    
		 	    
		 	    
		 	    
		 	    
				 } catch (Exception e1) {
						 e1.printStackTrace();
						 accountsmodel.jsondata="[{\"querystatus\" : \""+e1.getMessage()+"\"}]";
				 }finally {
					    if (conn != null) try { conn.close(); } catch (SQLException ignore) {}
				 }	
				 
				 
		 	  }
		 	  
		 	  protected LayoutManager getLayoutManager() {
		 	    return (LayoutManager)new ClassicLayoutManager();
		 	  }
		 	  
		 	  
		 	  
		 	  public DynamicReport getDynamicReport() {
		 	    return dr;
		 	  }
		 	  
		 	  public int getYear() {
		 	    return Calendar.getInstance().get(1);
		 	  }
		 	    
	 
}
