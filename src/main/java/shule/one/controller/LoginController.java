package shule.one.controller;

import java.security.Principal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.africastalking.AfricasTalking;

import shule.one.DbConnector;
import shule.one.entity.accountsmodel;
import shule.one.entity.jsontosql;
import shule.one.service.getremoteip;

@Controller
public class LoginController {
	

	@GetMapping("/")
	public String login(@RequestParam(value = "error", required = false) String error,
			@RequestParam(value = "logout", required = false) String logout,
			Model model, RedirectAttributes flash, 
			Principal principal) {	// this object is created when you have already logged in
		
		
		if(principal != null) {
			flash.addFlashAttribute("info","You have previously logged in");
			return "redirect:"; 
		}
		
		if(error != null) {
			model.addAttribute("error", "Incorrect username or password");
		}
		
		if(logout != null) {
			model.addAttribute("success", "You have successfully logged out");
		}

		
		return "login";
	}
	
	
	@RequestMapping(value="/logout", method=RequestMethod.GET)  
    public String logoutPage(HttpServletRequest request, HttpServletResponse response) {  
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();  
        if (auth != null){      
           new SecurityContextLogoutHandler().logout(request, response, auth);  
        }  
         
        return "redirect:/";  
     }  

	
	
	
	  @RequestMapping(value = "home", method = RequestMethod.GET)
	  public String index(Model model) {
		  		  
				 
		  accountsmodel.dbaction("SELECT *  from (SELECT s.id,schoolname,SUBSTRING_INDEX(schoolname, ' ', 2) as "
		  			+ " shortname,regname,logo,s.address,city,"
		  			+ " case when surname='' or surname is null then `second name` when `second name`=''  or `second name` is null "
		  			+ " then `first name` when `first name`=''   or `first name` is NULL then u.email ELSE '' end as  username,"
		  			+ " phone,ifnull(image,simage) as image FROM users u "
			 		+ " inner join schooldetails s on u.school=s.id "
			 		+ " LEFT join teacherregistration t on t.id=u.trnu "
			 		+ " where sessionid='"+RequestContextHolder.currentRequestAttributes().getSessionId()+"')d  ",1, 0, 0,0);
		  
			List<accountsmodel> newList = new ArrayList<accountsmodel>(accountsmodel.datanew);	
			model.addAttribute("schoolname", newList.get(0).activeProperty("schoolname").getValue()); 		
			model.addAttribute("shortname", newList.get(0).activeProperty("shortname").getValue()); 		
			model.addAttribute("motto", 	 newList.get(0).activeProperty("regname").getValue()); 		
			model.addAttribute("pobox", 	 newList.get(0).activeProperty("address").getValue()); 		
			model.addAttribute("city",  	 newList.get(0).activeProperty("city").getValue()); 		
			model.addAttribute("phone", 	 newList.get(0).activeProperty("phone").getValue()); 		
			model.addAttribute("logo", 	 	 newList.get(0).activeProperty("logo").getValue()); 
			model.addAttribute("email", 	 	 newList.get(0).activeProperty("username").getValue()); 
			model.addAttribute("image", 	 	 newList.get(0).activeProperty("image").getValue()); 
			
			
			
			Connection conn=null;
 			try {
 				conn = DbConnector.dataSource.getConnection();
 				
				
					conn.prepareStatement("INSERT into logs(school,action,ip,time,user)  "
								+ "  VALUES("+LoginController.getschoo()+",'Logged in',"
								+ " '"+getremoteip.getremoteip()+"',NOW(),"
								+ "  "+getloggedinuserid()+" )").execute();
				
					
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
			    if (conn != null) try { conn.close(); } catch (SQLException ignore) {}
			}
       
			
    	return "index";
    	
    	
    }
	  
	  
	  
	  
	  
	  
	  	 @ResponseBody
		 public static Integer getschoo() {
	  		 
	  		 int school=0;
	  		
	  		Connection conn=null;
 			try {
 				conn = DbConnector.dataSource.getConnection();
 				 
	  			PreparedStatement pstmt = conn.prepareStatement("SELECT s.id FROM users u "
				 		+ " inner join schooldetails s on u.school=s.id "
				 		);			 
	 			//System.out.println(pstmt);
	  			//pstmt.setString(1,RequestContextHolder.currentRequestAttributes().getSessionId());      
	 			//System.out.println(RequestContextHolder.currentRequestAttributes().getSessionId());
	 			ResultSet rs = pstmt.executeQuery(); 
	 			System.out.println(rs);
	 			if(rs.next()) {
	 				school=rs.getInt("id");
					System.out.println("shcool is"+school);
	 			}
//				while (rs.next()) { 
//					
//				}
				
				System.out.println("shcool is"+school);
	  		} catch (Exception e) {
				e.printStackTrace();
			}finally {
			    if (conn != null) try { conn.close(); } catch (SQLException ignore) {}
			}
			 
	  		 return school;
		 }
	  	 
	  	 
	  	 
	  	@ResponseBody
		 public static String getschoouploadpath() {
	  		 
	  		String uploadpath="";
	  		
	  		Connection conn=null;
			try {
				conn = DbConnector.dataSource.getConnection();
				 
	  			PreparedStatement pstmt= conn.prepareStatement("SELECT uploadpath FROM users u "
				 		+ " inner join schooldetails s on u.school=s.id "
				 		+ " where sessionid=?  ");			 
	 			
	  			pstmt.setString(1,RequestContextHolder.currentRequestAttributes().getSessionId());      
	 			
	 			ResultSet rs = pstmt.executeQuery();  
				while (rs.next()) { 
					uploadpath=rs.getString("uploadpath");
				}
				
			
	  		} catch (Exception e) {
				e.printStackTrace();
			}finally {
			    if (conn != null) try { conn.close(); } catch (SQLException ignore) {}
			}
			 		
	  		 return uploadpath;
		 }
	  	 
	  	 
	  	@ResponseBody
		 public static Integer getlastteacherid() {
	  		 int id=0;
	  		
	  		Connection conn=null;
 			try {
 				conn = DbConnector.dataSource.getConnection();
 			
	  			PreparedStatement pstmt= conn.prepareStatement(""
	  					+ " SELECT max(t.id) as id FROM users u "
	  					+ " inner join schooldetails s on u.school=s.id "
	  					+ " inner join teacherregistration t on t.school=s.id "
				 		+ " where sessionid=?  ");			 
	 			pstmt.setString(1,RequestContextHolder.currentRequestAttributes().getSessionId());      
	 			
	 			ResultSet rs = pstmt.executeQuery();  
				while (rs.next()) { 
					id=rs.getInt("id");
				}
			
	  		} catch (Exception e) {
				e.printStackTrace();
			}finally {
			    if (conn != null) try { conn.close(); } catch (SQLException ignore) {}
			}
			 		
	  		 return id;
		 }
	  	 
	  	 
	  	 
	  	@ResponseBody
		 public static String getloggedinuserid() {
	  		 String user="";
	  		
	  		 Connection conn=null;
 			try {
 				conn = DbConnector.dataSource.getConnection();
 			
 				PreparedStatement pstmt= conn.prepareStatement("SELECT u.id FROM users u "
				 		+ " where sessionid=?  ");			 
	 			pstmt.setString(1,RequestContextHolder.currentRequestAttributes().getSessionId());      
	 			
	 			ResultSet rs = pstmt.executeQuery();  
				while (rs.next()) { 
					user=rs.getString("id");
				}
			
	  		} catch (Exception e) {
				e.printStackTrace();
				user = ""+e.getMessage();
			}finally {
			    if (conn != null) try { conn.close(); } catch (SQLException ignore) {}
			}
			 		
	  		 return user;
		 }
	  	
	  	
	  	@ResponseBody
		 public static int getloggedinuseridtype() {
	  		 int user=0;
	  		Connection conn=null;
 			try {
 				conn = DbConnector.dataSource.getConnection();
 			
	  			PreparedStatement pstmt= conn.prepareStatement("SELECT role_id FROM user_roles  where user_id=?  ");			 
	 			pstmt.setString(1,getloggedinuserid());      
	 			
	 			ResultSet rs = pstmt.executeQuery();  
				while (rs.next()) { 
					user=rs.getInt("role_id");
				}
			
	  		} catch (Exception e) {
				e.printStackTrace();
			}finally {
			    if (conn != null) try { conn.close(); } catch (SQLException ignore) {}
			}
			 		
	  		 return user;
		 }
	  	 
	  	 
	  	 
	  	 @ResponseBody
		 public static String getschooname() {
	  		 String school="";
	  		Connection conn=null;
 			try {
 				conn = DbConnector.dataSource.getConnection();
 			
	  			PreparedStatement pstmt= conn.prepareStatement("SELECT s.Schoolname FROM users u "
				 		+ " inner join schooldetails s on u.school=s.id "
				 		+ " where sessionid=?  ");			 
	 			pstmt.setString(1,RequestContextHolder.currentRequestAttributes().getSessionId());      
	 			
	 			ResultSet rs = pstmt.executeQuery();  
				while (rs.next()) { 
					school=rs.getString("Schoolname");
				}
			
	  		} catch (Exception e) {
				e.printStackTrace();
				school = ""+e.getMessage();
			}finally {
			    if (conn != null) try { conn.close(); } catch (SQLException ignore) {}
			}
			 		
	  		 return school;
		 }
	  
	  	
	  
	  	
	  	
	  	 @ResponseBody
		 public static String getlastclass() {
	  		 String school="";
	  		Connection conn=null;
 			try {
 				conn = DbConnector.dataSource.getConnection();
 			
	  			PreparedStatement pstmt= conn.prepareStatement("SELECT s.class FROM users u "
				 		+ " inner join studentclass s on u.school=s.no "
				 		+ " where sessionid=? and class!='Transfered' and class!='Allumni' ");			 
	 			pstmt.setString(1,RequestContextHolder.currentRequestAttributes().getSessionId());      
	 			
	 			ResultSet rs = pstmt.executeQuery();  
				while (rs.next()) { 
					school=rs.getString("class");
				}
			
	  		} catch (Exception e) {
				e.printStackTrace();
				school = ""+e.getMessage();
			}finally {
			    if (conn != null) try { conn.close(); } catch (SQLException ignore) {}
			}
			 		
	  		 return school;
		 }
	  
	  	
	  	
	  	
	  	
	  	 @RequestMapping(value = "/api/log/getschoolinfo" , method = RequestMethod.GET)
		 @ResponseBody
		 public String getschoolinfo() {
	  		 
	  		accountsmodel.dbaction("SELECT *  from (SELECT s.id,schoolname,regname,address,logo,s.email,city,phone FROM users u "
				 		+ " inner join schooldetails s on u.school=s.id "
				 		+ " where sessionid='"+RequestContextHolder.currentRequestAttributes().getSessionId()+"')d  ",1, 0, 0,0);
		
			 return accountsmodel.jsondata;
		 }
	  	 
	  	 
	  	 
	  	 
	  	 
	  	 
	  	 
	  	 @RequestMapping(value = "/api/log/getschoollogo" , method = RequestMethod.GET)
		 @ResponseBody
		 public String getschoollogo() {
	  		 String log="";
	  		Connection conn=null;
 			try {
 				conn = DbConnector.dataSource.getConnection();
 			
	  			PreparedStatement pstmt= conn.prepareStatement("SELECT json_arrayagg(json_object('logo',logo)) as obj FROM users u "
				 		+ " inner join schooldetails s on u.school=s.id "
				 		+ " where sessionid=?  ");
			 
	 			pstmt.setString(1,RequestContextHolder.currentRequestAttributes().getSessionId());      
	 			
	 			ResultSet rs = pstmt.executeQuery();  
				while (rs.next()) { 
					log=rs.getString("obj");
				}
			
	  		} catch (Exception e) {
				e.printStackTrace();
				log = ""+e.getMessage();
			}finally {
			    if (conn != null) try { conn.close(); } catch (SQLException ignore) {}
			}
			 		
	  		 return log;
		 }

	  	 
	  	 
}
