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
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.RequestContextHolder;

import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.design.JRDesignQuery;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import shule.one.DbConnector;
import shule.one.entity.ERole;
import shule.one.entity.Role;
import shule.one.entity.User;
import shule.one.entity.accountsmodel;
import shule.one.entity.jsontosql;
import shule.one.repository.RoleRepository;
import shule.one.repository.UserRepository;
import shule.one.service.getremoteip;



@Controller
public class StudentsController{	
	

	
	@Autowired
	  UserRepository userRepository;

	@Autowired
	  PasswordEncoder encoder;
	
	@Autowired
	RoleRepository roleRepository;
	
	
	String id="0";
 	

	 @RequestMapping(value="/students", method=RequestMethod.POST)
	 public String gotoregistration(Model model) {  	
		 
   		return "fragments/students :: reglistofstudents";        
	
	}  
	
	
	
	 @RequestMapping(value = "/api/reg/getclasses" , method = RequestMethod.GET)
	 @ResponseBody
	 public String getClasses() {
		 accountsmodel.dbaction("select * from studentclass where class!='Transfered' AND class!='Allumni'"
		 		+ " and  school='"+LoginController.getschoo()+"'", 1, 0, 0,0);
	     return accountsmodel.jsondata;
	 }
	 
	 	 
	 @RequestMapping(value = "/api/reg/getstreams" , method = RequestMethod.GET)
	 @ResponseBody
	 public String getStreams() {
		 accountsmodel.dbaction("select * from studentstreams where  school='"+LoginController.getschoo()+"'", 1, 0, 0,0);
	     return accountsmodel.jsondata;
	 }
	 
	 
	 @RequestMapping(value = "/api/reg/getcounties" , method = RequestMethod.GET)
	 @ResponseBody
	 public String getCounties() {
		 accountsmodel.dbaction("select countycode,countyname FROM sub_county GROUP BY countycode", 1, 0, 0,0);
	     return accountsmodel.jsondata;
	 }
	 
	 
	 @RequestMapping(value = "/api/reg/getsubcounties" , method = RequestMethod.GET)
	 @ResponseBody
	 public String getSubCounties() {
		 accountsmodel.dbaction("select * FROM sub_county GROUP BY subcounty", 1, 0, 0,0);
	     return accountsmodel.jsondata;
	 }
	 
	 @RequestMapping(value = "/api/reg/getclassesreports" , method = RequestMethod.GET)
	 @ResponseBody
	 public String getclassesreports() {
		 
		 accountsmodel.dbaction("select  * FROM (select no, class from studentclass   where class!='Transfered' "
					+ " AND class!='Allumni' and  school='"+LoginController.getschoo()+"' ORDER BY NO)j"
					+ " union all "
					+ " SELECT * FROM (select concat(NO,':a:',streanid) AS no,concat(class,' ',stream) AS class "
					+ " from studentclass  "
					+ " cross join studentstreams where class!='Transfered' AND class!='Allumni' and "
					+ "  studentclass.school='"+LoginController.getschoo()+"' ORDER BY NO,streanid)d "
					+ " union all "
					+ " SELECT 'All','All classes' ", 1, 0, 0,0);
			
	     return accountsmodel.jsondata;
	 }
	
	
	
	 @RequestMapping(value = "/api/reg/gethostels" , method = RequestMethod.GET)
	 @ResponseBody
	 public String gethostels() {
		 accountsmodel.dbaction("select * FROM hostel where  school='"+LoginController.getschoo()+"' ", 1, 0, 0,0);
	     return accountsmodel.jsondata;
	 }
	 
	 
		
	// Student Actions 
		 
	 @RequestMapping(value = "/api/reg/getallstudents" , method = RequestMethod.GET)
	 @ResponseBody
	 public String getallstudents() {
		 
		 accountsmodel.dbaction("select '0' as querystatus,0 as checkstatus,b.* from (SELECT id,`Adm no` as 'Adm',"
	   				+ " concat(`first name`,' ',`SECOND NAME`,' ',surname) AS Name,"
	       			+ " simage as simage,concat(class,' ',studentstreams.stream) AS class,'60' as profile,'Active' as status,"
	       			+ " '' AS action from registration  "
	       			+ " inner join studentclass on  no = `CURRENT FORM` "
	       			+ " inner join studentstreams on  streanid = registration.stream "
	       			+ " where class!='Transfered' AND class!='Allumni' and  "
	       			+ " registration.school='"+LoginController.getschoo()+"' )b "
	       			+ " order by id ", 1, 0, 0,0);
		 
	     return accountsmodel.jsondata;
	 }
	 
	 
	
	 
		@RequestMapping(value = "/api/reg/getstudent", method = RequestMethod.POST)
	 	@ResponseBody
	 	public  String  getstudent(@RequestBody  JSONObject  search, HttpServletRequest request) {		    
	 		
			jsontosql.jsontosqlaction("select * from registration where ", search, 1,"");
		 		
	 		return accountsmodel.jsondata.replace("[", "").replace("]", "");	 			
	 		
		}
		

	 	@SuppressWarnings("unchecked")
		@PostMapping(value = "/api/reg/addstudent")
	 	@ResponseBody
	 	public  String  addstudent(@RequestBody  JSONObject  search, HttpServletRequest request) {		    
	 			
	 		
	 		search.put("school", LoginController.getschoo());
 			
	 		jsontosql.jsontosqlaction("registration", search, 2,"");	 		
	 		
	 		if(accountsmodel.response.equals("an error occured")) {
	 			  return accountsmodel.jsondata="[{\"querystatus\" : \""+accountsmodel.response+"\"}]";
	 		}
 			
 			accountsmodel.dbaction("select '"+accountsmodel.response+"' as querystatus,0 as checkstatus,b.* from ("
 					+ " SELECT id,`Adm no` as 'Adm',concat(`first name`,' ',`SECOND NAME`,' ',surname) AS Name,"
	       			+ " simage as simage,concat(class,' ',studentstreams.stream) AS class,'60' as profile,"
	       			+ " 'Active' as status,'' AS action from registration  "
	       			+ " inner join studentclass on  no = `CURRENT FORM` "
	       			+ " inner join studentstreams on  streanid = registration.stream "
	       			+ " where  registration.school='"+LoginController.getschoo()+"' ORDER BY id DESC LIMIT 1 )b ", 1,0,0,0);
	
 			Connection conn=null;
 			try {
 				conn = DbConnector.dataSource.getConnection();
 				
				conn.prepareStatement("INSERT into logs(school,action,ip,time,user)  "
								+ "  VALUES("+LoginController.getschoo()+","
								+ " 'Added new student "+search.get("first name")+"  "+search.get("second name")+"',"
								+ " '"+getremoteip.getremoteip()+"',NOW(),"
								+ "  "+LoginController.getloggedinuserid()+" )").execute();
				
				
	 			
			} catch (Exception e) {
				e.printStackTrace();
			}finally {
			    if (conn != null) try { conn.close(); } catch (SQLException ignore) {}
			}
			
	 		return accountsmodel.jsondata;

		}
	 
	 	
	 	
	 	
	 	
	 	@PostMapping(value = "/api/reg/updatestudent")
	 	@ResponseBody
	 	public  String  updatestudent(@RequestBody  JSONObject  search, HttpServletRequest request) {		    
	 			
	 		
	 		jsontosql.jsontosqlaction("registration", search, 3,"");
	 		if(accountsmodel.response.equals("an error occured")) {
	 			  return accountsmodel.jsondata="[{\"querystatus\" : \""+accountsmodel.response+"\"}]";
	 		}
	 			
 			accountsmodel.dbaction("select '"+accountsmodel.response+"' as querystatus,0 as checkstatus,b.* from "
 					+ " (SELECT id,`Adm no` as 'Adm',concat(`first name`,' ',`SECOND NAME`,' ',surname) AS Name,"
	       			+ " simage as simage,concat(class,' ',studentstreams.stream) AS class,'60' as profile,"
	       			+ " 'Active' as status,'' AS action  from registration  "
	       			+ " inner join studentclass on  no = `CURRENT FORM` "
	       			+ " inner join studentstreams on  streanid = registration.stream where"
	       			+ " registration.school='"+LoginController.getschoo()+"' )b  "
	       			+ " where id='"+search.get("id").toString()+"' ", 1, 0, 0,0);
	 
 			Connection conn=null;
 			try {
 				conn = DbConnector.dataSource.getConnection();
 				
 				conn.prepareStatement("INSERT into logs(school,action,ip,time,user)  "
								+ "  VALUES("+LoginController.getschoo()+","
								+ " 'updated student information for  "+search.get("first name")+"  "+search.get("second name")+"',"
								+ " '"+getremoteip.getremoteip()+"',NOW(),"
								+ "  "+LoginController.getloggedinuserid()+" )").execute();
				
				
 			} catch (Exception e) {
				e.printStackTrace();
			}finally {
			    if (conn != null) try { conn.close(); } catch (SQLException ignore) {}
			} 
			
 			return accountsmodel.jsondata;	 			
 		 	
		}
	 	
	 	
	 	@PostMapping(value = "/api/reg/deletestudent")
	 	@ResponseBody
	 	public  String  deletestudent(@RequestBody  JSONObject  search, HttpServletRequest request) {		    
	 			
	 		
	 		jsontosql.jsontosqlaction("delete from registration where ", search, 4,"");
	 		if(accountsmodel.response.equals("an error occured")) {
	 			  return accountsmodel.jsondata="[{\"querystatus\" : \""+accountsmodel.response+"\"}]";
	 		}
	 		
	 		
	 		Connection conn=null;
			try {
				conn = DbConnector.dataSource.getConnection();
				
				conn.prepareStatement("INSERT into logs(school,action,ip,time,user)  "
								+ "  VALUES("+LoginController.getschoo()+","
								+ " 'deleted student  id "+search.get("id")+"',"
								+ " '"+getremoteip.getremoteip()+"',NOW(),"
								+ "  "+LoginController.getloggedinuserid()+" )").execute();
				
				
				
			} catch (Exception e) {
				e.printStackTrace();
			}finally {
			    if (conn != null) try { conn.close(); } catch (SQLException ignore) {}
			}
	 		
 			return accountsmodel.jsondata="[{\"querystatus\" : \""+accountsmodel.response+"\"}]";
 			 			
	 		
		}
	 	
	 	
	 	
	 	
	 	
	 	 @RequestMapping(value="/teachers", method=RequestMethod.POST)
		 public String loadlistofteachers(Model model) {   
			
	   	return "fragments/students :: reglistofstudents";	        
		
		}  
	 	
	 	
	 	
	 	
	 	 @RequestMapping(value = "/api/reg/classesandstreams" , method = RequestMethod.GET)
		 @ResponseBody
		 public String getclassesandstreams() {
			 
	 		 accountsmodel.dbaction("select concat(NO,'::',streanid) AS no,concat(class,' ',stream) AS class "
			 			+ " from studentclass  cross "
						+ " join studentstreams where class!='Transfered' AND class!='Allumni' and "
						+ " studentclass.school='"+LoginController.getschoo()+"' ORDER BY NO,streanid ", 1, 0, 0,0);
		 
			 return accountsmodel.jsondata;
		 }
	 
	 	
	 	
	 	
	 	
	 	
	 	@RequestMapping(value = "/api/reg/getallteachers" , method = RequestMethod.GET)
		 @ResponseBody
		 public String getallteachers() {
	 		
	 		accountsmodel.dbaction("select '0' as querystatus,0 as checkstatus,b.* from (SELECT id,Trnu, "
	   				+ "	 concat(`first name`,' ',`SECOND NAME`,' ',surname) AS Name,gender, "
	   				+ "	`PHONE NU`, simage,'50' as profile, status, '' AS action from teacherregistration   "
	   				+ "	 where status='Active' and  school='"+LoginController.getschoo()+"')b  "
	   				+ "	 order by id ", 1, 0, 0,0);
	 		 		
		     return accountsmodel.jsondata;
		 }
	    
	 	
	 	
	 	
	 	@SuppressWarnings("unchecked")
		@RequestMapping(value = "/api/reg/getteacher", method = RequestMethod.POST)
	 	@ResponseBody
	 	public  String  getteacher(@RequestBody  JSONObject  search, HttpServletRequest request) {		    
	 			
	 		search.put("t.id", search.get("id"));
	 		search.remove("id");
	 		search.put("t.school", LoginController.getschoo());
	 		
	 		id=search.get("t.id").toString();
	 		 
	 		jsontosql.jsontosqlaction("select t.*,ifnull(concat(class,'::',stream),'') as classteacherfor from "
 					+ " teacherregistration t  left join classteachers c on t.id=c.teacher where",search, 1,""); 
 			
	 		return accountsmodel.jsondata.replace("[", "").replace("]", "");	 			
	 		
		}
		

	 	
	 	
	 	@SuppressWarnings("unchecked")
		@PostMapping(value = "/api/reg/addteacher")
	 	@ResponseBody
	 	public  String  addteacher(@RequestBody  JSONObject  search, HttpServletRequest request) {		    
	 		
	 		
	 		search.put("school", LoginController.getschoo());
 			
	 		jsontosql.jsontosqlaction("teacherregistration", search, 2,"");
	 		if(accountsmodel.response.equals("an error occured")) {
	 			  return accountsmodel.jsondata="[{\"querystatus\" : \""+accountsmodel.response+"\"}]";
	 		}
	 		
 			
 			if(accountsmodel.response.equals("Operation successfull")) {
 				
 				User user = new User(LoginController.getschoo(),LoginController.getlastteacherid(),
 						search.get("email").toString(),search.get("email").toString(),encoder.encode("1234@#"));
 			    Set<Role> roles = new HashSet<>();
 			   Role userRole = roleRepository.findByName(ERole.ROLE_USER)
 			          .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
 			      roles.add(userRole);
 			 
 			    user.setRoles(roles);
 			    userRepository.save(user);
 			    
 			}
 			
 			
 			accountsmodel.dbaction("select '"+accountsmodel.response+"' as querystatus,0 as checkstatus,b.* from (SELECT id,Trnu, "
	   				+ "				       concat(`first name`,' ',`SECOND NAME`,' ',surname) AS Name,gender, "
	   				+ "	       			 `PHONE NU`, simage,'50' as profile, status, "
	   				+ "	       			 '' AS action from teacherregistration   "
	   				+ "	       			 where status='Active' and  school='"+LoginController.getschoo()+"')b  "
	   				+ "	       			 order by id DESC LIMIT 1 ", 1, 0, 0,0);
	 
	
 			Connection conn=null;
 			try {
 				conn = DbConnector.dataSource.getConnection();
 				
				conn.prepareStatement("INSERT into logs(school,action,ip,time,user)  "
								+ "  VALUES("+LoginController.getschoo()+","
								+ " 'Added new teacher "+search.get("first name")+"  "+search.get("second name")+"',"
								+ " '"+getremoteip.getremoteip()+"',NOW(),"
								+ "  "+LoginController.getloggedinuserid()+" )").execute();
			
				
 			} catch (Exception e) {
				e.printStackTrace();
			}finally {
			    if (conn != null) try { conn.close(); } catch (SQLException ignore) {}
			} 
       
 			
	 		return accountsmodel.jsondata;

		}
	 	
	 	
	 	
	 	
	 
	 	@PostMapping(value = "/api/reg/updateteacher")
	 	@ResponseBody
	 	public  String  updateteacher(@RequestBody  JSONObject  search, HttpServletRequest request) {		    
	 		
	 		
	 		jsontosql.jsontosqlaction("teacherregistration", search, 3,""); 			
	 		
	 		if(accountsmodel.response.equals("an error occured")) {
	 			  return accountsmodel.jsondata="[{\"querystatus\" : \""+accountsmodel.response+"\"}]";
	 		}
	 		
	 		
	 		accountsmodel.dbaction(" SELECT '"+accountsmodel.response+"' as querystatus,0 as checkstatus,"
 					+ " b.* from (SELECT id,Trnu, concat(`first name`,' ',`SECOND NAME`,' ',surname) AS Name,gender, "
	   				+ "`PHONE NU`, simage,'50' as profile, status,'' AS action from teacherregistration   "
	   				+ "	where `id` ='"+search.get("id").toString()+"' and  school='"+LoginController.getschoo()+"')b "
	   				+ " order by id DESC LIMIT 1 ", 1, 0, 0,0);
	 		
	 		
	 		Connection conn=null;
	 		
			try {
				
				conn = DbConnector.dataSource.getConnection();
				
				
	 				conn.prepareStatement("INSERT into logs(school,action,ip,time,user)  "
								+ "  VALUES("+LoginController.getschoo()+","
								+ " 'Updated teacher info for  "+search.get("first name")+"  "+search.get("second name")+"',"
								+ " '"+getremoteip.getremoteip()+"',NOW(),"
								+ "  "+LoginController.getloggedinuserid()+" )").execute();
	 				
	 		} catch (Exception e) {
				e.printStackTrace();
			}finally {
			    if (conn != null) try { conn.close(); } catch (SQLException ignore) {}
			}
       
	 		
	 		return accountsmodel.jsondata;	 			
		 	
		}
	 	
	 	
	 	
	 	
	 	@PostMapping(value = "/api/reg/deleteteacher")
	 	@ResponseBody
	 	public  String  deleteteacher(@RequestBody  JSONObject  search, HttpServletRequest request) {		    
	 		
	 		Connection conn=null;
			try {
				conn = DbConnector.dataSource.getConnection();
				
				
	 			
	 		 
	 			conn.prepareStatement("delete from users where trnu='"+search.get("id")+"'").execute();
	 		
	 			jsontosql.jsontosqlaction("delete from teacherregistration where ", search, 4,"");

		 		if(accountsmodel.response.equals("an error occured")) {
		 			  return accountsmodel.jsondata="[{\"querystatus\" : \""+accountsmodel.response+"\"}]";
		 		}
		 		
		 		
	 			
	 			
	 			conn.prepareStatement("INSERT into logs(school,action,ip,time,user)  "
								+ "  VALUES("+LoginController.getschoo()+","
								+ " 'Deleted teacher number   "+search.get("id")+"',"
								+ " '"+getremoteip.getremoteip()+"',NOW(),"
								+ "  "+LoginController.getloggedinuserid()+" )").execute();
	 			
	 			
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
			    if (conn != null) try { conn.close(); } catch (SQLException ignore) {}
			}
	 		
 			
 			return accountsmodel.jsondata="[{\"querystatus\" : \""+accountsmodel.response+"\"}]";
 			 			
	 		
		}
	 	
	 	
	 	 @RequestMapping(value = "/api/reg/getsubtaught" , method = RequestMethod.GET)
		 @ResponseBody
		 public String getsubtaught() {
	 			
	 		accountsmodel.dbaction("select '0' as querystatus,case when sub2 IS NOT NULL then TRUE ELSE FALSE END AS checkstatus,"
	 				+ "  				id,class,sub from (SELECT f.*,k.sub AS sub2 FROM ("
	 				+ " 				SELECT concat(NO,':a:',streanid,':b:',subid) AS id,"
	 				+ "	 				concat(s.class,' ',stream) AS class,code AS sub "
	 				+ "	 				 FROM studentclass s "
	 				+ "	 				 CROSS JOIN studentstreams d "
	 				
	 				+ " 				INNER JOIN subjects1 v ON no=v.class"
	 				
	 				+ "	 				 WHERE (s.class!='Transfered' AND s.class!='Allumni') "
	 				+ " 				 and  "
	 				+ "					 s.school='"+LoginController.getschoo()+"'  and "
	 			    + "  				 d.school='"+LoginController.getschoo()+"' and "
	 				+ " 				 v.school='"+LoginController.getschoo()+"'   "
	 			    + " 				 GROUP BY NO,streanid,sub ORDER BY NO,streanid)f"
	 				+ "	 				 LEFT JOIN (SELECT tscnu,concat(class,':a:',stream,':b:',sub) AS sub FROM "
	 				+ "                  teaxchersubtought WHERE tscnu='"+id+"' and  school='"+LoginController.getschoo()+"')k"
	 				+ "	 				 ON id collate utf8mb4_unicode_ci=k.sub )l "
	 				+ "                  ORDER BY checkstatus desc",1,0,0,0); 
	 		
	 		
	 			id="";
	 		
	 		return accountsmodel.jsondata;
		 }
			 	
	 	
	 	
		 
		 
		 
		 
		 	
		 
		 	
	@GetMapping("/staff")
	public String gotoregistrationlistofstaff(Model model) {	 
    	
		      return "fragments/students :: reglistofstudents";
		
	    }	
	
	
	
	
	 @RequestMapping(value = "/api/reg/getallstaff" , method = RequestMethod.GET)
	 @ResponseBody
	 public String getallstaff() {
		 
		 accountsmodel.dbaction("select '0' as querystatus,0 as checkstatus,b.* from (SELECT id,`Idnu`, "
	   				+ "	concat(`first name`,' ',`SECOND NAME`,' ',surname) AS Name , `Phone nu`, simage,"
	   				+ " '75' as profile, status, '' AS action  from subregistration where status='Active'"
	   				+ " and  school='"+LoginController.getschoo()+"' )b   order by id ", 1, 0, 0,0);			
			
	     return accountsmodel.jsondata;
	 }
	
	
	@RequestMapping(value = "/api/reg/getstaff", method = RequestMethod.POST)
 	@ResponseBody
 	public  String  getstaff(@RequestBody  JSONObject  search, HttpServletRequest request) {		    
 		
		id=search.get("id").toString();
 		
		jsontosql.jsontosqlaction("select * from subregistration where ", search, 1,"");
			 
			
 		return accountsmodel.jsondata.replace("[", "").replace("]", "");	 			
 		
	}
	

 	@SuppressWarnings("unchecked")
	@PostMapping(value = "/api/reg/addstaff")
 	@ResponseBody
 	public  String  addstaff(@RequestBody  JSONObject  search, HttpServletRequest request) {		    
 			
 		
 			search.put("school", LoginController.getschoo());			
 		
 			jsontosql.jsontosqlaction("subregistration", search, 2,"");

	 		if(accountsmodel.response.equals("an error occured")) {
	 			  return accountsmodel.jsondata="[{\"querystatus\" : \""+accountsmodel.response+"\"}]";
	 		}
	 		
	 		
			accountsmodel.dbaction("select '"+accountsmodel.response+"' as querystatus,0 as checkstatus,b.* from "
					+ " (SELECT id,`Idnu`, concat(`first name`,' ',`SECOND NAME`,' ',surname) AS Name, "
	   				+ "	`PHONE NU`, simage,'75' as profile, status, '' AS action from subregistration "
	   				+ "  where  school='"+LoginController.getschoo()+"'  ORDER BY id DESC LIMIT 1 )b  ", 1,0,0,0);
 
			Connection conn=null;
			try {
				conn = DbConnector.dataSource.getConnection();
				
				
	 			
				conn.prepareStatement("INSERT into logs(school,action,ip,time,user)  "
								+ "  VALUES("+LoginController.getschoo()+","
								+ " 'Added new student "+search.get("first name")+"  "+search.get("second name")+"',"
								+ " '"+getremoteip.getremoteip()+"',NOW(),"
								+ "  "+LoginController.getloggedinuserid()+" )").execute();
				
				
				
			} catch (Exception e) {
				e.printStackTrace();
			}finally {
			    if (conn != null) try { conn.close(); } catch (SQLException ignore) {}
			} 
       
			
 		return accountsmodel.jsondata;

	}
 
 	@PostMapping(value = "/api/reg/updatestaff")
 	@ResponseBody
 	public  String  updatestaff(@RequestBody  JSONObject  search, HttpServletRequest request) {		    
 		
 		
 			jsontosql.jsontosqlaction("subregistration", search,3,"");

	 		if(accountsmodel.response.equals("an error occured")) {
	 			  return accountsmodel.jsondata="[{\"querystatus\" : \""+accountsmodel.response+"\"}]";
	 		}
	 		
	 		
			accountsmodel.dbaction("select '"+accountsmodel.response+"' as querystatus,0 as checkstatus,b.* from (SELECT id,`Idnu`, "
	   				+ "	concat(`first name`,' ',`SECOND NAME`,' ',surname) AS Name, "
	   				+ "	`PHONE NU`, simage,'75' as profile, status, "
	   				+ "	 '' AS action from subregistration   where status='Active' and  school='"+LoginController.getschoo()+"'  "
	   				+ "  and `id` ='"+id+"' )b   ", 1, 0, 0,0);
 		
			Connection conn=null;
			try {
				conn = DbConnector.dataSource.getConnection();
				
				
	 			
				conn.prepareStatement("INSERT into logs(school,action,ip,time,user)  "
								+ "  VALUES("+LoginController.getschoo()+","
								+ " 'Update information for staff member "+search.get("first name")+"  "+search.get("second name")+"',"
								+ " '"+getremoteip.getremoteip()+"',NOW(),"
								+ "  "+LoginController.getloggedinuserid()+" )").execute();
				
				
			} catch (Exception e) {
				e.printStackTrace();
			}finally {
			    if (conn != null) try { conn.close(); } catch (SQLException ignore) {}
			} 
 		
			return accountsmodel.jsondata;	 			
		 	
	}
 	
 	@PostMapping(value = "/api/reg/deletestaff")
 	@ResponseBody
 	public  String  deletestaff(@RequestBody  JSONObject  search, HttpServletRequest request) {		    
 			
 		
 		jsontosql.jsontosqlaction("delete from subregistration where ", search, 4,"");

 		if(accountsmodel.response.equals("an error occured")) {
 			  return accountsmodel.jsondata="[{\"querystatus\" : \""+accountsmodel.response+"\"}]";
 		}
 		
 		
 		Connection conn=null;
		try {
			conn = DbConnector.dataSource.getConnection();
			
			
 			
			conn.prepareStatement("INSERT into logs(school,action,ip,time,user)  "
							+ "  VALUES("+LoginController.getschoo()+","
							+ " 'Deleted staff "+search.get("id")+"',"
							+ " '"+getremoteip.getremoteip()+"',NOW(),"
							+ "  "+LoginController.getloggedinuserid()+" )").execute();
			
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
		    if (conn != null) try { conn.close(); } catch (SQLException ignore) {}
		} 
 		
			return accountsmodel.jsondata="[{\"querystatus\" : \""+accountsmodel.response+"\"}]";
			 			
 		
	}
	
	
	
	
	
	 
	 
	 
	 @GetMapping("/board")
		public String gotoregistrationlistofboard(Model model) {	 
	    	
			return "fragments/students :: reglistofstudents";
			
	 }
	 
	 
	 
	 

	 @RequestMapping(value = "/api/reg/getallboard" , method = RequestMethod.GET)
	 @ResponseBody
	 public String getallboard() {
		 
		 accountsmodel.dbaction("select '0' as querystatus,0 as checkstatus,b.* from (SELECT id,`Idnu`, "
	   				+ "		concat(`first name`,' ',`SECOND NAME`,' ',surname) AS Name,gender, "
	   				+ "	   `PHONE`, simage, status,  '' AS action from board   "
	   				+ "	   where status='Active' and  school='"+LoginController.getschoo()+"' )b  order by id ", 1, 0, 0,0);
			
		
	     return accountsmodel.jsondata;
	 }
	 
	 
	 
	 
	 	@RequestMapping(value = "/api/reg/getboard", method = RequestMethod.POST)
	 	@ResponseBody
	 	public  String  getofboard(@RequestBody  JSONObject  search, HttpServletRequest request) {		    
	 			
				id=search.get("id").toString();
	 		
				jsontosql.jsontosqlaction("select * from board where ", search, 1,"");
				 
				
	 		return accountsmodel.jsondata.replace("[", "").replace("]", "");	 			
	 		
		}
	 
	
	    @SuppressWarnings("unchecked")
		@PostMapping(value = "/api/reg/addboard")
	 	@ResponseBody
	 	public  String  addboard(@RequestBody  JSONObject  search, HttpServletRequest request) {		    
	 		
	    	
	    		search.put("school", LoginController.getschoo());

		 		if(accountsmodel.response.equals("an error occured")) {
		 			  return accountsmodel.jsondata="[{\"querystatus\" : \""+accountsmodel.response+"\"}]";
		 		}
		 		
		 		
	    	
	 			jsontosql.jsontosqlaction("board", search, 2,"");
				
	 			accountsmodel.dbaction("select '"+accountsmodel.response+"' as querystatus,0 as checkstatus,b.* from ("
	 					+ "  SELECT id,`Idnu`,  concat(`first name`,' ',`SECOND NAME`,' ',surname) AS Name,gender, "
	 	   				+ "	 `PHONE`, simage, status, '' AS action from board  where  school='"+LoginController.getschoo()+"'"
	 	   				+ "   ORDER BY id DESC LIMIT 1 )b  ", 1,0,0,0);
		 
	 			Connection conn=null;
				try {
					conn = DbConnector.dataSource.getConnection();
					
					
		 			
	 				conn.prepareStatement("INSERT into logs(school,action,ip,time,user)  "
	 								+ "  VALUES("+LoginController.getschoo()+","
	 								+ " 'Added board member "+search.get("first name")+"  "+search.get("second name")+"',"
	 								+ " '"+getremoteip.getremoteip()+"',NOW(),"
	 								+ "  "+LoginController.getloggedinuserid()+" )").execute();
	 				
	 				
	 			} catch (Exception e) {
	 				e.printStackTrace();
	 			}finally {
	 			    if (conn != null) try { conn.close(); } catch (SQLException ignore) {}
	 			} 
	 			
	 		return accountsmodel.jsondata;

		}
	 
	 	@PostMapping(value = "/api/reg/updateboard")
	 	@ResponseBody
	 	public  String  updateboard(@RequestBody  JSONObject  search, HttpServletRequest request) {		    
	 			
	 		
	 			jsontosql.jsontosqlaction("board", search,3,"");

		 		if(accountsmodel.response.equals("an error occured")) {
		 			  return accountsmodel.jsondata="[{\"querystatus\" : \""+accountsmodel.response+"\"}]";
		 		}
		 		
		 		
	 			accountsmodel.dbaction("select '"+accountsmodel.response+"' as querystatus,0 as checkstatus,b.* from ("
	 					+ " SELECT id,`Idnu`,  concat(`first name`,' ',`SECOND NAME`,' ',surname) AS Name,gender, "
	 	   				+ "	`PHONE`, simage, status,  '' AS action from board  where status='Active' and  "
	 	   				+ " school='"+LoginController.getschoo()+"'  and `id` ='"+id+"' )b   ", 1, 0, 0,0);
		 	
	 			Connection conn=null;
				try {
					conn = DbConnector.dataSource.getConnection();
					
					
		 			
	 				conn.prepareStatement("INSERT into logs(school,action,ip,time,user)  "
	 								+ "  VALUES("+LoginController.getschoo()+","
	 								+ " 'Added board member "+search.get("first name")+"  "+search.get("second name")+"',"
	 								+ " '"+getremoteip.getremoteip()+"',NOW(),"
	 								+ "  "+LoginController.getloggedinuserid()+" )").execute();
	 				
	 				
	 			} catch (Exception e) {
	 				e.printStackTrace();
	 			}finally {
	 			    if (conn != null) try { conn.close(); } catch (SQLException ignore) {}
	 			} 
	 		
	 		return accountsmodel.jsondata;	 			
	 		
		}
	 	
	 	@PostMapping(value = "/api/reg/deleteboard")
	 	@ResponseBody
	 	public  String  deleteboard(@RequestBody  JSONObject  search, HttpServletRequest request) {		    
	 			
	 		
	 		jsontosql.jsontosqlaction("delete from board where ", search, 4,"");

	 		if(accountsmodel.response.equals("an error occured")) {
	 			  return accountsmodel.jsondata="[{\"querystatus\" : \""+accountsmodel.response+"\"}]";
	 		}
	 		
	 		
	 		Connection conn=null;
			try {
				conn = DbConnector.dataSource.getConnection();
				
				
	 			
		 			conn.prepareStatement("INSERT into logs(school,action,ip,time,user)  "
	 								+ "  VALUES("+LoginController.getschoo()+","
	 								+ " 'Deleted board member id  "+search.get("id")+"',"
	 								+ " '"+getremoteip.getremoteip()+"',NOW(),"
	 								+ "  "+LoginController.getloggedinuserid()+" )").execute();
		 			
	 				
	 			} catch (Exception e) {
	 				e.printStackTrace();
	 			}finally {
	 			    if (conn != null) try { conn.close(); } catch (SQLException ignore) {}
	 			} 
	 		
				return accountsmodel.jsondata="[{\"querystatus\" : \""+accountsmodel.response+"\"}]";
				 			
	 		
		}
	 
	 
	 	
	 	
	 	
	 	
	 	
	 	
	 	
	 	
		 // REPORTS ====================================================================	
		 	
		 
	 	
	 	 @RequestMapping(value = "/api/reg/getreports/classlists", method = RequestMethod.POST)
		 @ResponseBody
		 public String getclasslists(@RequestBody  JSONObject  search, HttpServletRequest request) {
		
	 		 
	 		 
				 String value=search.get("value").toString();
				 
				 
					 List<String> nna = new ArrayList<String>();
					 accountsmodel.dbaction("select code from subjects1 WHERE "
					 		+ "  school='"+LoginController.getschoo()+"'  "
					 		+ "  and class='"+value+"'", 1, 0, 0,0);
					 	
					 accountsmodel.datanew.forEach((tab) -> {
						 nna.add("'' as '"+  tab.activeProperty(accountsmodel.collumns.get(0)).getValue()+"'");
						});				 
					 		
					 
					 if(value.contains(":a:")) {
						
							 
						 accountsmodel.dbaction("select * FROM ( SELECT @rownum := @rownum + 1 as Sn,`adm no` as Regno,"
					 				+ " concat(`first name`,' ',`SECOND NAME`,' ',surname) AS Name"
					 				+ ", "+nna.toString().replace("[", "").replace("]", "")+" from registration r "
					 				+ " inner join studentstreams s on streanid=r.stream "
					 				+ " cross join (select @rownum := 0) r "
					 				+ " where `current form`="+value.split(":a:")[0]+" and  "
					 				+ " r.school='"+LoginController.getschoo()+"'  and "
					 				+ " streanid="+value.split(":a:")[1]+")f ", 1, 0, 0,0);
						
						
							 
					 }else if(value.equals("All")) {
						
						 
						 accountsmodel.dbaction("select * FROM ( SELECT @rownum := @rownum + 1 as Sn,`adm no` as Regno,"
					 				+ " concat(`first name`,' ',`SECOND NAME`,' ',surname) AS Name,concat(class,' ',s.Stream) as Class"
					 				+ ", "+nna.toString().replace("[", "").replace("]", "")+" from registration r "
					 				+ " inner join studentstreams s on streanid=r.stream "
					 				+ " inner join studentclass c on no=`current form` "
					 				+ " cross join (select @rownum := 0) r "
					 				+ " where  school='"+LoginController.getschoo()+"'  and "
					 				+ " class!='Transfered'  AND class!='Allumni'  order by no,streanid )f ", 1, 0, 0,0);
						
						
							 
					 }else {
						 
						 accountsmodel.dbaction("select * FROM ( SELECT @rownum := @rownum + 1 as Sn,`adm no` as Regno,"
					 				+ " concat(`first name`,' ',`SECOND NAME`,' ',surname) AS Name,s.Stream"
					 				+ ", "+nna.toString().replace("[", "").replace("]", "")+" from registration r "
					 				+ " inner join studentstreams s on streanid=r.stream cross join (select @rownum := 0) r "
					 				+ " where  r.school='"+LoginController.getschoo()+"'  and `current form`="+value+" )f ", 1, 0, 0,0);
				
						 
					 }
					 
			 
					 return accountsmodel.jsondata;
	 	 }
	 	
	 	 
	 	 
	 	 
	 	 
	 	 
	 	 @RequestMapping(value = "/api/reg/getreports/emptyclasslists", method = RequestMethod.POST)
		 @ResponseBody
		 public String getemptyclasslists(@RequestBody  JSONObject  search, HttpServletRequest request) {
			
			 		 
					 String value=search.get("value").toString();					 
				 
					 if(value.contains(":a:")) {						
						 
						 accountsmodel.dbaction("select * FROM ( SELECT @rownum := @rownum + 1 as Sn,`adm no` as Regno,"
					 				+ " concat(`first name`,' ',`SECOND NAME`,' ',surname) AS Name,"
					 				+ " '' as `1 st`,''  as `2 nd`,''  as `3 rd`,''  as `4 th`  from registration r "
					 				+ " inner join studentstreams s on streanid=r.stream "
					 				+ " cross join (select @rownum := 0) r "
					 				+ " where  r.school='"+LoginController.getschoo()+"'  and  `current form`="+value.split(":a:")[0]+""
					 				+ "  and streanid="+value.split(":a:")[1]+")f ", 1, 0, 0,0);
						
						
							 
					 }else if(value.equals("All")) {
							
						 
						 accountsmodel.dbaction("select * FROM ( SELECT @rownum := @rownum + 1 as Sn,`adm no` as Regno,"
					 				+ " concat(`first name`,' ',`SECOND NAME`,' ',surname) AS Name,concat(class,' ',s.Stream) as Class"
					 				+ ", '' as `1 st`,''  as `2 nd`,''  as `3 rd`,''  as `4 th` from registration r "
					 				+ " inner join studentstreams s on streanid=r.stream "
					 				+ " inner join studentclass c on no=`current form` "
					 				+ " cross join (select @rownum := 0) r "
					 				+ " where  r.school='"+LoginController.getschoo()+"'  and class!='Transfered' "
					 				+ " AND class!='Allumni'  order by no,streanid )f ", 1, 0, 0,0);
						
						
							 
					 }else {
						 
						 accountsmodel.dbaction("select * FROM ( SELECT @rownum := @rownum + 1 as Sn,`adm no` as Regno,"
					 				+ " concat(`first name`,' ',`SECOND NAME`,' ',surname) AS Name,s.Stream,"
					 				+ " '' as `1 st`,''  as `2 nd`,''  as `3 rd`,''  as `4 th` from registration r "
					 				+ " inner join studentstreams s on streanid=r.stream cross join (select @rownum := 0) r "
					 				+ " where  r.school='"+LoginController.getschoo()+"'  and `current form`="+value+" )f ", 1, 0, 0,0);
				
						 }
	 						
	  
		 
					 
			 
					 return accountsmodel.jsondata;
	 	 }
	 	
	 	
	 	 
	 	 
	 	 
	 	 @RequestMapping(value = "/api/reg/getreports/detailedlist", method = RequestMethod.POST)
		 @ResponseBody
		 public String getdetailedlist(@RequestBody  JSONObject  search, HttpServletRequest request) {
		
 	
	 		 
			 String value=search.get("value").toString();
			 
			 
					 if(value.contains(":a:")) {
						
						 
						 accountsmodel.dbaction("select * FROM ( SELECT @rownum := @rownum + 1 as Sn,`adm no` as Regno,"
					 				+ " concat(`first name`,' ',`SECOND NAME`,' ',surname) AS Name,"
					 				+ " r.`DATE OF JOINING` AS Doa,r.`DATE OF BIRTH` AS Dob,r.`BIRTH CERT NU` AS 'B.cert',"
					 				+ " r.`LEAVING CERT NU` AS 'L.cert', "
					 				+ " IFNULL(`Fathers phone`,motherphone) AS Phone  from registration r "
					 				+ " inner join studentstreams s on streanid=r.stream "
					 				+ " cross join (select @rownum := 0) r "
					 				+ " where  r.school='"+LoginController.getschoo()+"'  and "
					 				+ " `current form`="+value.split(":a:")[0]+" and streanid="+value.split(":a:")[1]+")f ", 1, 0, 0,0);
						
						
							 
					 }else if(value.equals("All")) {
							
						 
						 accountsmodel.dbaction("select * FROM ( SELECT @rownum := @rownum + 1 as Sn,`adm no` as Regno,"
					 				+ " concat(`first name`,' ',`SECOND NAME`,' ',surname) AS Name,concat(class,' ',s.Stream) as Class"
					 				+ " r.`DATE OF JOINING` AS Doa,r.`DATE OF BIRTH` AS Dob,r.`BIRTH CERT NU` AS 'B.cert',"
					 				+ " r.`LEAVING CERT NU` AS 'L.cert', "
					 				+ " IFNULL(`Fathers phone`,motherphone) AS Phone  from registration r "
					 				+ " inner join studentstreams s on streanid=r.stream "
					 				+ " inner join studentclass c on no=`current form` "
					 				+ " cross join (select @rownum := 0) r "
					 				+ " where  r.school='"+LoginController.getschoo()+"' "
					 				+ " and class!='Transfered'  AND class!='Allumni'  order by no,streanid )f ", 1, 0, 0,0);
						
						
							 
					 }else {
						 
						 
						 accountsmodel.dbaction("select * FROM ( SELECT @rownum := @rownum + 1 as Sn,`adm no` as Regno,"
					 				+ " concat(`first name`,' ',`SECOND NAME`,' ',surname) AS Name,s.Stream,"
					 				+ " r.`DATE OF JOINING` AS Doa,r.`DATE OF BIRTH` AS Dob,r.`BIRTH CERT NU` AS 'B.cert',"
					 				+ " r.`LEAVING CERT NU` AS 'L.cert',"
					 				+ " IFNULL(`Fathers phone`,motherphone) AS Phone from registration r "
					 				+ " inner join studentstreams s on streanid=r.stream cross join (select @rownum := 0) r "
					 				+ " where  r.school='"+LoginController.getschoo()+"'  and `current form`="+value+" )f ", 1, 0, 0,0);
				
						 }
	 						
	  
		 
	 						
	  
		 
					 
			 
					 return accountsmodel.jsondata;
	 	 }
	 	 
	 	 
	 	
	    
	 	 @RequestMapping(value = "/api/reg/getreports/transferedstudents", method = RequestMethod.POST)
		 @ResponseBody
		 public String gettransferedstudents(@RequestBody  JSONObject  search, HttpServletRequest request) {
		 
	 		 
	 		Connection conn=null;
			try {
				conn = DbConnector.dataSource.getConnection();
				
				
	 			
					 String daterange=search.get("daterange").toString();			 
					
					 accountsmodel.jsondata="";
					 
					 PreparedStatement pstmt= conn.prepareStatement(" SELECT json_arrayagg(json_object('Sn',Sn,'Regno',"
						 		+ " Regno,'Name',Name,'Class',Class,'Joining date',`Joining date`,'Date left',`Date left`,"
						 		+ " 'Reason for transfer',`Reason for transfer`,'Approved by',`Approved by`)) AS obj FROM "
						 		+ " ( SELECT @rownum := @rownum + 1 as Sn,r.`adm no` as Regno, "
						 		+ " concat(`first name`,' ',`SECOND NAME`,' ',surname) AS Name,concat(class,' ',s.Stream) as Class,"
						 		+ " r.`DATE OF JOINING` AS 'Joining date',r.`date of leaving` AS 'DATE LEFT',"
						 		+ " ReasonForTransfer AS 'Reason for transfer', ApprovedBy AS 'Approved by'  from registration r   "
						 		+ " inner join studentstreams s on streanid=r.stream "
				 				+ " INNER JOIN transffered t ON t.studentid=r.id "
				 				+ " inner join studentclass c on no=`current form` "
				 				+ " cross join (select @rownum := 0) r "
				 			    + " where  r.school='"+LoginController.getschoo()+"'  and `date of leaving` between ? and ?  "
				 			    + " and c.class='Transfered'  )f ");
					 
			 			pstmt.setString(1,daterange.split(" - ")[0]);      
			 			pstmt.setString(2,daterange.split(" - ")[1]);
			 			
						ResultSet rs = pstmt.executeQuery();  
						while (rs.next()) { 
							accountsmodel.jsondata=rs.getString("obj");
						}
						
						
						if (accountsmodel.jsondata==null) {  
							accountsmodel.response = "No records found";
							return accountsmodel.jsondata="[{\"querystatus\" : \""+accountsmodel.response+"\"}]";
						}
						
			 
	 		} catch (Exception e) {
				e.printStackTrace();
				accountsmodel.response = ""+e.getMessage();
			}finally {
			    if (conn != null) try { conn.close(); } catch (SQLException ignore) {}
			}
				
						
			 
					 return accountsmodel.jsondata;
	 	 }
	
		
	 	 
	 	 @RequestMapping(value = "/api/reg/getreports/listofteachers", method = RequestMethod.POST)
		 @ResponseBody
		 public String getlistofteachers(@RequestBody  JSONObject  search, HttpServletRequest request) {
		 
	 		accountsmodel.dbaction(" SELECT * FROM ( SELECT @rownum := @rownum + 1 as Sn, Trnu, concat(`first name`,' ',`SECOND NAME`,' ',surname) AS NAME,\n"
				 		+ " Gender,r.`DATE OF JOINING` AS 'Joining date',Service from teacherregistration  r \n"
				 		+ " cross join (select @rownum := 0) r  WHERE  r.school='"+LoginController.getschoo()+"' and "
				 		+ "  STATUS='Active')f  ", 1, 0, 0,0);								
			 
					 return accountsmodel.jsondata;
	 	 }
	 	 
	 	 
	 	 @RequestMapping(value = "/api/reg/getreports/listofstaff", method = RequestMethod.POST)
		 @ResponseBody
		 public String getlistofstaff(@RequestBody  JSONObject  search, HttpServletRequest request) {
		 
	 		accountsmodel.dbaction("select * FROM ( SELECT @rownum := @rownum + 1 as Sn, Idnu, "
					 + " concat(`first name`,' ',`SECOND NAME`,' ',surname) AS NAME, "
					 + " Gender,r.`DATE OF JOINING` AS 'Joining date',Position from subregistration   r"
					 + " cross join (select @rownum := 0) r  WHERE  school='"+LoginController.getschoo()+"' "
					 + "  and STATUS='Active')f", 1, 0, 0,0);
			 
					 return accountsmodel.jsondata;
	 	 }
	 	 
	 	 
	 	 
	 	 @RequestMapping(value = "/api/reg/getreports/allumni", method = RequestMethod.POST)
		 @ResponseBody
		 public String getallumni(@RequestBody  JSONObject  search, HttpServletRequest request) {
		 	 			 
				 String daterange=search.get("year").toString();
				 
				 Connection conn=null;
					try {
						conn = DbConnector.dataSource.getConnection();
					
						 accountsmodel.jsondata="";
						 
						 PreparedStatement pstmt= conn.prepareStatement(" SELECT json_arrayagg(json_object('Sn',Sn,'Regno',Regno,"
						 			+ "  'Name',NAME,'Leaving date',`Leaving date`)) as obj FROM ( "
						 			+ " SELECT @rownum := @rownum + 1 as Sn,"
						 			+ " r.`adm no` as Regno,"
							 		+ " concat(`first name`,' ',`SECOND NAME`,' ',surname) AS NAME,"
							 		+ " r.`date of leaving` AS 'Leaving date' from registration r  "
							 		+ " inner join studentstreams s on streanid=r.stream "
							 		+ " inner join studentclass c on no=`current form` "
							 		+ " cross join (select @rownum := 0) r  "
							 		+ " where  r.school='"+LoginController.getschoo()+"'  and YEAR(r.`date of leaving`)=? "
							 		+ " and c.class='Allumni' )f   ");
						 
				 			pstmt.setString(1,daterange.split("-")[0]);      
				 			
				 			ResultSet rs = pstmt.executeQuery();  
							while (rs.next()) { 
								accountsmodel.jsondata=rs.getString("obj");
							}
				 
							if (accountsmodel.jsondata==null) {  
								accountsmodel.response = "No records found";
								return accountsmodel.jsondata="[{\"querystatus\" : \""+accountsmodel.response+"\"}]";
							}
							
							
							
							
				 	} catch (Exception e) {
						e.printStackTrace();
						accountsmodel.response = ""+e.getMessage();
					}finally {
					    if (conn != null) try { conn.close(); } catch (SQLException ignore) {}
					}

			 				
			 
					 return accountsmodel.jsondata;
	 	 }
	 	 
	 	 
	 	 
	 	 @RequestMapping(value = "/api/reg/getreports/hostel", method = RequestMethod.POST)
		 @ResponseBody
		 public String getstudentsperhostel(@RequestBody  JSONObject  search, HttpServletRequest request) {
		  
			 String hostel=search.get("hostel").toString();	
			
			 	
			 Connection conn=null;
				try {
					conn = DbConnector.dataSource.getConnection();
					
					
		 				
				 
				 accountsmodel.jsondata="";
				 
				 PreparedStatement pstmt= conn.prepareStatement("SELECT json_arrayagg(json_object('Sn',Sn,"
				 			+ " 'Regno',Regno,'Name',Name,'Class',Class,'Bednu',Bednu)) obj FROM "
				 			+ " ( SELECT @rownum := @rownum + 1 as Sn,`adm no` as Regno, "
					 		+ "	concat(`first name`,' ',`SECOND NAME`,' ',surname) AS Name,"
					 		+ " concat(class,' ',s.Stream) as Class "
					 		+ "	,r.Bednu from registration r  "
					 		+ "	inner join studentstreams s on streanid=r.stream "
					 		+ "	inner join studentclass c on no=`current form` "
					 		+ "	cross join (select @rownum := 0) r "
					 		+ "	where class!='Transfered'  AND class!='Allumni' AND "
					 		+ " r.school='"+LoginController.getschoo()+"'  and hostel=? order BY `adm no` )f   ");
				 
		 			pstmt.setString(1,hostel);      
		 			
		 			ResultSet rs = pstmt.executeQuery();  
					while (rs.next()) { 
						accountsmodel.jsondata=rs.getString("obj");
					}
		 
					if (accountsmodel.jsondata==null) {  
						accountsmodel.response = "No records found";
						return accountsmodel.jsondata="[{\"querystatus\" : \""+accountsmodel.response+"\"}]";
					}
					
					
					
		 	} catch (Exception e) {
				e.printStackTrace();
				accountsmodel.response = ""+e.getMessage();
			}finally {
			    if (conn != null) try { conn.close(); } catch (SQLException ignore) {}
			}
			 
			 
					 return accountsmodel.jsondata;
	 	 }
	 	 
	 	 
	 	 
	 	 
	 	 
	 	 @RequestMapping(value = "/api/reg/getreports/parentlist", method = RequestMethod.POST)
		 @ResponseBody
		 public String getparentlist(@RequestBody  JSONObject  search, HttpServletRequest request) {
		   
			 String value=search.get("value").toString();
			 accountsmodel.jsondata="";
			 
			 Connection conn=null;
				try {
					conn = DbConnector.dataSource.getConnection();
					
					
		 				
					
			 
			 if(value.contains(":a:")) {
					 
					 
					 PreparedStatement pstmt= conn.prepareStatement("SELECT json_arrayagg(json_object('Sn',Sn,"
					 			+ " 'Regno',Regno,'Parent Name',`Parent Name`,'Student Name',`Student Name`,"
					 			+ " 'Class',Class,'phone',phone)) obj  FROM ( SELECT @rownum := @rownum + 1 as Sn,"
					 			+ " `adm no` as Regno, IFNULL(`fathers name`,r.mothername) AS 'Parent Name', "
						 		+ "	concat(`first name`,' ',`SECOND NAME`,' ',surname) AS 'Student Name',"
						 		+ " concat(class,' ',s.Stream) as Class,"
						 		+ " IFNULL(`fathers phone`,IFNULL(r.motherphone,guardianphone)) AS phone from registration r "
						 		+ "	 inner join studentstreams s on streanid=r.stream "
						 		+ "	 inner join studentclass c on no=`current form` "
						 		+ "	 cross join (select @rownum := 0) j "
						 		+ "  where  r.school='"+LoginController.getschoo()+"'  and `current form`=? and  streanid=?)f ");
					 
			 			pstmt.setString(1,value.split(":a:")[0]);      
			 			pstmt.setString(2,value.split(":a:")[1]);      
			 			
			 			ResultSet rs = pstmt.executeQuery();  
						while (rs.next()) { 
							accountsmodel.jsondata=rs.getString("obj");
						}
			 
						if (accountsmodel.jsondata==null) {  
							accountsmodel.response = "No records found";
							return accountsmodel.jsondata="[{\"querystatus\" : \""+accountsmodel.response+"\"}]";
						}
						
					 
			 }else if(value.equals("All")) {
					
				 
				 accountsmodel.dbaction("select json_arrayagg(json_object('Sn',Sn,"
				 			+ " 'Regno',Regno,'Parent Name',`Parent Name`,'Student Name',`Student Name`,"
				 			+ " 'Class',Class,'phone',phone)) obj FROM ( SELECT @rownum := @rownum + 1 as Sn,`adm no` as Regno,"
					 		+ " IFNULL(`fathers name`,r.mothername) AS 'Parent NAME', "
					 		+ "	concat(`first name`,' ',`SECOND NAME`,' ',surname) AS 'Student NAME',"
					 		+ " concat(class,' ',s.Stream) as Class "
					 		+ "	,IFNULL(`fathers phone`,IFNULL(r.motherphone,guardianphone)) AS phone from registration r "
					 		+ "	 inner join studentstreams s on streanid=r.stream "
					 		+ "	 inner join studentclass c on no=`current form` "
					 		+ "	 cross join (select @rownum := 0) r "
							+ "  where r.school='"+LoginController.getschoo()+"'  and class!='Transfered' "
							+ "  AND class!='Allumni'  order by no,streanid )f ", 1, 0, 0,0);
				
				
					 
			 }else {
				 
				 PreparedStatement pstmt= conn.prepareStatement("SELECT json_arrayagg(json_object('Sn',Sn,"
				 			+ " 'Regno',Regno,'Parent Name',`Parent Name`,'Student Name',`Student Name`,"
				 			+ " 'Class',Class,'phone',phone)) obj FROM ( SELECT @rownum := @rownum + 1 as Sn,`adm no` as Regno,"
					 		+ " IFNULL(`fathers name`,r.mothername) AS 'Parent NAME', "
					 		+ "	concat(`first name`,' ',`SECOND NAME`,' ',surname) AS 'Student NAME',"
					 		+ " concat(class,' ',s.Stream) as Class "
					 		+ "	,IFNULL(`fathers phone`,IFNULL(r.motherphone,guardianphone)) AS phone from registration r "
					 		+ "	 inner join studentstreams s on streanid=r.stream "
					 		+ "	 inner join studentclass c on no=`current form` "
					 		+ "	 cross join (select @rownum := 0) r "
							+ " where  r.school='"+LoginController.getschoo()+"'  and `current form`=? )f ");
				 
		 			pstmt.setString(1,value);      
		 			
		 			ResultSet rs = pstmt.executeQuery();  
					while (rs.next()) { 
						accountsmodel.jsondata=rs.getString("obj");
					}
		 
					if (accountsmodel.jsondata==null) {  
						accountsmodel.response = "No records found";
						return accountsmodel.jsondata="[{\"querystatus\" : \""+accountsmodel.response+"\"}]";
					}
				 
				 
				 }
	 				
			 
			 		
				
			 	} catch (Exception e) {
					e.printStackTrace();
					accountsmodel.response = ""+e.getMessage();
				}finally {
				    if (conn != null) try { conn.close(); } catch (SQLException ignore) {}
				}
			 
					
			 return accountsmodel.jsondata;
			 
	 	 }
	 	 
	 	 
	 	 
	 	 
	 	 
	 	@RequestMapping(value = "/api/reg/getreports/listperdenomination", method = RequestMethod.POST)
		 @ResponseBody
		 public String getlistperdenomination(@RequestBody  JSONObject  search, HttpServletRequest request) {
		   
					 String value=search.get("value").toString();
					 String group=search.get("group").toString();
					 
					 Connection conn=null;
						try {
							conn = DbConnector.dataSource.getConnection();
							
							
				 				
					 
					 
					 if(value.contains(":a:")) {
						
						 PreparedStatement pstmt= conn.prepareStatement("SELECT json_arrayagg(json_object('Sn',Sn,'Regno',Regno,"
						 			+ " 'Student name',`Student name`,'Class',Class)) as obj"
						 			+ "  FROM ( SELECT @rownum := @rownum + 1 as Sn,`adm no` as Regno,"
							 		+ "	 concat(`first name`,' ',`SECOND NAME`,' ',surname) AS 'Student name',"
							 		+ "  concat(class,' ',s.Stream) as Class "
							 		+ "	 from registration r "
							 		+ "	 inner join studentstreams s on streanid=r.stream "
							 		+ "	 inner join studentclass c on no=`current form` "
							 		+ "	 cross join (select @rownum := 0) r  where  religion='"+group+"'"
							 		+ "  and  r.school='"+LoginController.getschoo()+"'  and `current form`=? and streanid=? )f ");
						 
				 			pstmt.setString(1,value.split(":a:")[0]);      
				 			pstmt.setString(2,value.split(":a:")[1]);      
				 			
				 			ResultSet rs = pstmt.executeQuery();  
							while (rs.next()) { 
								accountsmodel.jsondata=rs.getString("obj");
							}
				 
							if (accountsmodel.jsondata==null) {  
								accountsmodel.response = "No records found";
								return accountsmodel.jsondata="[{\"querystatus\" : \""+accountsmodel.response+"\"}]";
							}
						 
						 
						 
						
							 
					 }else if(value.equals("All")) {
							
						
						 
						 PreparedStatement pstmt= conn.prepareStatement("SELECT json_arrayagg(json_object('Sn',Sn,'Regno',Regno,"
						 			+ " 'Student name',`Student name`,'Class',Class)) as obj "
						 			+ " FROM ( SELECT @rownum := @rownum + 1 as Sn,`adm no` as Regno,"
							 		+ "	concat(`first name`,' ',`SECOND NAME`,' ',surname) AS 'Student NAME',"
							 		+ " concat(class,' ',s.Stream) as Class "
							 		+ "  from registration r "
							 		+ "	 inner join studentstreams s on streanid=r.stream "
							 		+ "	 inner join studentclass c on no=`current form` "
							 		+ "	 cross join (select @rownum := 0) r   where   religion=? "
							 		+ "  and  r.school='"+LoginController.getschoo()+"'  and class!='Transfered'  "
							 		+ "  AND class!='Allumni'  order by no,streanid )f ");
						 
				 			pstmt.setString(1,group);      
				 			
				 			ResultSet rs = pstmt.executeQuery();  
							while (rs.next()) { 
								accountsmodel.jsondata=rs.getString("obj");
							}
				 
							if (accountsmodel.jsondata==null) {  
								accountsmodel.response = "No records found";
								return accountsmodel.jsondata="[{\"querystatus\" : \""+accountsmodel.response+"\"}]";
							}
						 
						 
						
						
							 
					 }else {
						 
						 PreparedStatement pstmt= conn.prepareStatement("SELECT json_arrayagg(json_object('Sn',Sn,'Regno',Regno,"
						 			+ " 'Student name',`Student name`,'Class',Class)) as obj "
						 			+ "  FROM ( SELECT @rownum := @rownum + 1 as Sn,"
						 			+ " `adm no` as Regno,concat(`first name`,' ',`SECOND NAME`,' ',surname) AS 'Student NAME',"
							 		+ "  concat(class,' ',s.Stream) as Class "
							 		+ "  from registration r "
							 		+ "	 inner join studentstreams s on streanid=r.stream "
							 		+ "	 inner join studentclass c on no=`current form` "
							 		+ "	 cross join (select @rownum := 0) r "
									+ "  where  r.school='"+LoginController.getschoo()+"'  and `current form`=? and   religion=? )f ");
						 
				 			pstmt.setString(1,value);      
				 			pstmt.setString(2,group);      
				 			
				 			ResultSet rs = pstmt.executeQuery();  
							while (rs.next()) { 
								accountsmodel.jsondata=rs.getString("obj");
							}
				 
							if (accountsmodel.jsondata==null) {  
								accountsmodel.response = "No records found";
								return accountsmodel.jsondata="[{\"querystatus\" : \""+accountsmodel.response+"\"}]";
							}
						 
				
						 }
	 						
					 	
						
					
					 } catch (Exception e) {
							e.printStackTrace();
							accountsmodel.response = ""+e.getMessage();
					 }finally {
						    if (conn != null) try { conn.close(); } catch (SQLException ignore) {}
						}
			 
					 
				return accountsmodel.jsondata;
					 
	 	 }
	 	 
		
	 	@Value("${invoice.template.path}")
	    private String invoice_template;

	 	
	    @RequestMapping(path = "/api/reg/getreports/leavingcert", method = RequestMethod.POST)
	    @ResponseBody
		public ResponseEntity<byte[]> download(@RequestBody  JSONObject  search, HttpServletRequest request) throws IOException {
	    	
			accountsmodel.dbaction("select *  from (SELECT s.id,schoolname,regname,uploadpath,"
					+ " logo,address,city,phone FROM users u "
			 		+ " inner join schooldetails s on u.school=s.id "
			 		+ " where sessionid='"+RequestContextHolder.currentRequestAttributes().getSessionId()+"')d  ", 1, 0, 0,0);
			
			List<accountsmodel> newList = new ArrayList<accountsmodel>(accountsmodel.datanew);	
		    
		    
		    String uploadDir = "static/"+newList.get(0).activeProperty("uploadpath").getValue()+"/otheruploads";
		    
			final String EXTENSION = ".pdf";

	    	String adm=search.get("admno").toString();					

	    	File file = new File(uploadDir + File.separator +RequestContextHolder.currentRequestAttributes().getSessionId()+adm + EXTENSION);

	    	
			String SQL = "SELECT `adm no`, concat(`first name`,' ',`second name`,' ',surname) AS name,"
					+ " address,city,`DATE OF JOINING`,`date of leaving`,"
					+ " (SELECT class FROM studentclass WHERE NO=`current form` "
					+ " and school='"+LoginController.getschoo()+"') AS currentc, "
					+ " (SELECT class FROM studentclass WHERE NO=`class joined` "
					+ " and school='"+LoginController.getschoo()+"') AS classjoined,"
					+ "`date of birth`,schoolname,schoolcategory  FROM registration r "
					+ " INNER JOIN schooldetails  s ON  s.id=r.school  where  "
					+ " r.school='"+LoginController.getschoo()+"'  and `ADM NO`='" + adm + "'";

			
			Connection conn=null;
			try {
				conn = DbConnector.dataSource.getConnection();
				
				
	 			
			
			final InputStream ip = getClass().getResourceAsStream(invoice_template);
	        
			JasperDesign jd = JRXmlLoader.load(ip);
			Map<String, Object> param = new HashMap<>();
			param.put("schoolname", LoginController.getschooname());
			param.put("class", LoginController.getlastclass());


			JRDesignQuery newQuery = new JRDesignQuery();
			newQuery.setText(SQL);
			jd.setQuery(newQuery);
			JasperReport jr = JasperCompileManager.compileReport(jd);
			JasperPrint jp = JasperFillManager.fillReport(jr, param, conn);
			
			JasperExportManager.exportReportToPdfFile(jp,uploadDir + File.separator +RequestContextHolder.
					 currentRequestAttributes().getSessionId()+adm + EXTENSION);
			
			
			
	    } catch (Exception e) {
			e.printStackTrace();
		}finally {
		    if (conn != null) try { conn.close(); } catch (SQLException ignore) {}
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
	 	
	 	
		
	 
	
}
