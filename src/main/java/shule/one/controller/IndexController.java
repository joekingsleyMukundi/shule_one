package shule.one.controller;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.RequestContextHolder;

import com.africastalking.AfricasTalking;

import shule.one.entity.accountsmodel;
import shule.one.entity.jsontosql;
import shule.one.repository.RoleRepository;
import shule.one.repository.UserRepository;





@Controller
public class IndexController implements ErrorController{
	
	
	public static final String PATH = "/error";

    @RequestMapping(value = PATH)
    public String error() {
        return "redirect:/";
    }

    @Override
    public String getErrorPath() {
        return "redirect:/";  
    }
	
	
	
	public static String[] suffixes =
			  //    0     1     2     3     4     5     6     7     8     9
			     { "th", "st", "nd", "rd", "th", "th", "th", "th", "th", "th",
			  //    10    11    12    13    14    15    16    17    18    19
			       "th", "th", "th", "th", "th", "th", "th", "th", "th", "th",
			  //    20    21    22    23    24    25    26    27    28    29
			       "th", "st", "nd", "rd", "th", "th", "th", "th", "th", "th",
			  //    30    31
			       "th", "st" };

	static Calendar cal = Calendar.getInstance();
	static int day=Integer.parseInt(new SimpleDateFormat("dd").format(cal.getTime()));
	 
	public static String stringdate =cal.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.SHORT, Locale.US)+" "+
			 day+ IndexController.suffixes[day]+" "+
			 new SimpleDateFormat("MMM").format(cal.getTime())+" "+
			 new SimpleDateFormat("YYYY").format(cal.getTime())+" "+
			 new SimpleDateFormat("hh:mm aa").format(cal.getTime());

	public static String stringdatenormal =new SimpleDateFormat("YYYY").format(cal.getTime())+"-"+
	new SimpleDateFormat("MMM").format(cal.getTime())+"-"+cal.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.SHORT, Locale.US);

	@Autowired
	RoleRepository roleRepository;
	
	@Autowired
	  UserRepository userRepository;
	
	@Autowired
	  PasswordEncoder encoder;
	

	
	
	public static String user="";
	static String hostname="";
																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																										
											
				 @GetMapping(value = "/registration")
				  public String gotoregistration(Model model) {
					
//					 Connection conn=null;
//			 			try {
//			 				conn = DbConnector.dataSource.getConnection();
//			 					
//							
//							ResultSet rt = conn.prepareStatement("SELECT email,password,id  FROM `teacherregistration`  "
//										+ " where school='4' and "
//										+ " status='Active' ").executeQuery();
//									while (rt.next()) {	
//									  User user = new User(4,rt.getInt("id"),
//											   rt.getString("email"),rt.getString("email").toString(),encoder.encode(rt.getString("password")));
//						 			    Set<Role> roles = new HashSet<>();
//						 			   Role userRole = roleRepository.findByName(ERole.ROLE_ADMIN)
//						 			          .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
//						 			      roles.add(userRole);
//						 			 
//						 			    user.setRoles(roles);
//						 			    userRepository.save(user);
//									}
//		 			    
//			 			} catch (Exception e) {
//							e.printStackTrace();
//						}finally {
//						    if (conn != null) try { conn.close(); } catch (SQLException ignore) {}
//						}
					 
					 
				
					accountsmodel.dbaction(" SELECT 1  id ,(SELECT COUNT(id)  FROM registration "
							+ " inner join studentclass ON `current form`=no WHERE  "
							+ " class!='Transfered' AND class!='Allumni' "
							+ " and registration.school='"+LoginController.getschoo()+"' )  AS noofstudent,"
							+ " (SELECT COUNT(id)  FROM teacherregistration  where status='Active' "
							+ " and  school='"+LoginController.getschoo()+"')  AS noofteachers,"
							+ " (SELECT COUNT(id)  FROM subregistration  where status='Active'"
							+ "  and school='"+LoginController.getschoo()+"')  AS noofstaff,"
							+ " (SELECT COUNT(id)  FROM board  where status='Active' and"
							+ " school='"+LoginController.getschoo()+"' )  AS noofboard ",1, 0, 0,0);
					
					List<accountsmodel> newList = new ArrayList<accountsmodel>(accountsmodel.datanew);	
					model.addAttribute("noofstudent", new DecimalFormat("#,###").format(Double.parseDouble(newList.get(0).activeProperty("noofstudent").getValue()))); 		
					model.addAttribute("noofteachers", new DecimalFormat("#,###").format(Double.parseDouble(newList.get(0).activeProperty("noofteachers").getValue()))); 		
					model.addAttribute("noofstaff", new DecimalFormat("#,###").format(Double.parseDouble(newList.get(0).activeProperty("noofstaff").getValue()))); 		
					model.addAttribute("noofboard", new DecimalFormat("#,###").format(Double.parseDouble(newList.get(0).activeProperty("noofboard").getValue()))); 		
					
					
					
					accountsmodel.dbaction(" SELECT *  from (SELECT s.id,schoolname,SUBSTRING_INDEX(schoolname, ' ', 2) as "
				  			+ " shortname,regname,logo,s.address,city,phone,"
				  			+ " case when surname='' or surname is null then `second name` when `second name`=''  "
				  			+ " or `second name` is null then `first name` when `first name`=''   or `first name` "
				  			+ " is NULL then u.email ELSE '' end as  username,ifnull(image,simage) as image FROM users u "
					 		+ " inner join schooldetails s on u.school=s.id "
					 		+ " LEFT join teacherregistration t on t.id=u.trnu "
					 		+ " where sessionid='"+RequestContextHolder.currentRequestAttributes().getSessionId()+"')d  ",1, 0, 0,0);
				  
					List<accountsmodel> newListdf = new ArrayList<accountsmodel>(accountsmodel.datanew);	
					model.addAttribute("email", 	 	 newListdf.get(0).activeProperty("username").getValue()); 
					model.addAttribute("image", 	 	 newListdf.get(0).activeProperty("image").getValue()); 

					
					
					
					accountsmodel.dbaction("select class,"
							+ " sum( if( gender = 'Male',(ABS(Total)), 0 ) ) as male,"
							+ " sum( if( gender = 'Female',(ABS(Total)), 0 ) ) as female,"
							+ " (sum( if( gender = 'Male',(ABS(Total)), 0 ) ) +"
							+ " sum( if( gender = 'Female',(ABS(Total)), 0 ) )) total"
							+ " FROM (SELECT class,gender,COUNT(`adm no`) AS total FROM registration"
							+ " right join studentclass on no=`current form` WHERE (`class`!='Allumni' and  `class`!='Transfered' )  "
							+ " and registration.school='"+LoginController.getschoo()+"' GROUP BY `current form`,gender)h GROUP BY class"
							
							+ "  UNION ALL "

							+ " SELECT 'All' Class," + " sum( if( gender = 'Male',(ABS(Total)), 0 ) ) as Male,"
							+ " sum( if( gender = 'Female',(ABS(Total)), 0 ) ) as Female,"
							+ " (sum( if( gender = 'Male',(ABS(Total)), 0 ) ) +"
							+ " sum( if( gender = 'Female',(ABS(Total)), 0 ) )) Total"
							+ " FROM (SELECT class,gender,COUNT(`adm no`) AS total FROM registration "
							+ " right join studentclass on no=`current form` WHERE (`class`!='Allumni' and  `class`!='Transfered' ) "
							+ " and registration.school='"+LoginController.getschoo()+"' GROUP BY `current form`,gender)h",1, 0, 0,0);
					
					List<accountsmodel> newListofstudents = new ArrayList<accountsmodel>(accountsmodel.datanew);	
					List<String> cols = new ArrayList<String>(accountsmodel.collumns);	
					model.addAttribute("collumns", cols); 		
					model.addAttribute("listofstudentsperclass", newListofstudents); 
					
					
					
					accountsmodel.dbaction("SELECT  * FROM (select no, class from studentclass   where class!='Transfered' "
							+ " AND class!='Allumni' and school='"+LoginController.getschoo()+"' ORDER BY NO)j"
							+ " union all "
							+ " SELECT * FROM (select concat(NO,':a:',streanid) AS no,concat(class,' ',stream) AS class "
							+ " from studentclass  "
							+ " cross join studentstreams where class!='Transfered' AND class!='Allumni' "
							+ " and studentclass.school='"+LoginController.getschoo()+"' "
							+ " and studentstreams.school='"+LoginController.getschoo()+"' ORDER BY NO,streanid)d "
							+ " union all "
							+ " SELECT 'All','All classes' ",1, 0, 0,0);
					
					List<accountsmodel> newListofclasses = new ArrayList<accountsmodel>(accountsmodel.datanew);		
					model.addAttribute("classesreports", newListofclasses);
					
					accountsmodel.dbaction("SELECT * FROM hostel where school='"+LoginController.getschoo()+"'",1, 0, 0,0);
					List<accountsmodel> newList1111 = new ArrayList<accountsmodel>(accountsmodel.datanew);		
					model.addAttribute("hostelreport", newList1111);  
					
					accountsmodel.collumns.clear();
					accountsmodel.datanew.clear();
					accountsmodel.collumns.add("religion");				
					
					
					accountsmodel row = new accountsmodel(accountsmodel.collumns);
					row.setallowduplicates(0);
					row.setadmno("1");
					row.setChecked(false);
					row.setActive("religion", "Christian");
					accountsmodel.datanew.add(row);

					row = new accountsmodel(accountsmodel.collumns);
					row.setallowduplicates(0);
					row.setadmno("2");
					row.setChecked(false);
					row.setActive("religion", "Muslim");
					accountsmodel.datanew.add(row);

					row = new accountsmodel(accountsmodel.collumns);
					row.setallowduplicates(0);
					row.setadmno("3");
					row.setChecked(false);
					row.setActive("religion", "Hindu");
					accountsmodel.datanew.add(row);

					row = new accountsmodel(accountsmodel.collumns);
					row.setallowduplicates(0);
					row.setadmno("4");
					row.setChecked(false);
					row.setActive("religion", "Other");
					accountsmodel.datanew.add(row);

					List<accountsmodel> newListreligions = new ArrayList<accountsmodel>(accountsmodel.datanew);
					model.addAttribute("religions", newListreligions);  
					
					
					accountsmodel.dbaction("select * from studentclass where class!='Transfered' AND class!='Allumni'"
							+ " and school='"+LoginController.getschoo()+"'",1, 0, 0,0);
					List<accountsmodel> newListm = new ArrayList<accountsmodel>(accountsmodel.datanew);		
					model.addAttribute("classes", newListm);  
					
					accountsmodel.dbaction("select concat(NO,'::',streanid) AS no,concat(class,' ',stream) AS class "
			 				+ " from studentclass  cross join studentstreams where class!='Transfered' AND class!='Allumni' and  "
							+ " studentclass.school='"+LoginController.getschoo()+"' ORDER BY NO,streanid ",1, 0, 0,0);
			 		
			 		List<accountsmodel> newListclassteacherfor = new ArrayList<accountsmodel>(accountsmodel.datanew);		
					model.addAttribute("classteacherfor", newListclassteacherfor);
					
					
					accountsmodel.dbaction("select * from studentstreams where   school='"+LoginController.getschoo()+"'",1, 0, 0,0);
			 		List<accountsmodel> newList1 = new ArrayList<accountsmodel>(accountsmodel.datanew);		
					model.addAttribute("streams", newList1);      	
					
					accountsmodel.dbaction("SELECT countycode,countyname FROM sub_county GROUP BY countycode",1, 0, 0,0);
			 		List<accountsmodel> newList11 = new ArrayList<accountsmodel>(accountsmodel.datanew);		
					model.addAttribute("counties", newList11);      	
					
					accountsmodel.dbaction("SELECT * FROM sub_county GROUP BY subcounty",1, 0, 0,0);
			 		List<accountsmodel> newList111 = new ArrayList<accountsmodel>(accountsmodel.datanew);		
					model.addAttribute("subcounties", newList111);  
					
					
			        	
			    	return "register/dashboard";
				
				}  				
				
				
				
				 
				 @RequestMapping(value="/registrationmain", method=RequestMethod.POST)
				 public String gotoregistrationmain(Model model) {
					
					 accountsmodel.dbaction(" SELECT *  from (SELECT s.id,schoolname,SUBSTRING_INDEX(schoolname, ' ', 2) as "
					  			+ " shortname,regname,logo,s.address,city,phone,"
					  			+ " case when surname='' or surname is null then `second name` when `second name`=''  "
					  			+ " or `second name` is null then `first name` when `first name`=''   or `first name` is "
					  			+ " NULL then u.email ELSE '' end as  username,ifnull(image,simage) as image FROM users u "
						 		+ " inner join schooldetails s on u.school=s.id "
						 		+ " LEFT join teacherregistration t on t.id=u.trnu "
						 		+ " where sessionid='"+RequestContextHolder.currentRequestAttributes().getSessionId()+"')d  ",1, 0, 0,0);
					  
						List<accountsmodel> newListdf = new ArrayList<accountsmodel>(accountsmodel.datanew);	
						model.addAttribute("email", 	 	 newListdf.get(0).activeProperty("username").getValue()); 
						model.addAttribute("image", 	 	 newListdf.get(0).activeProperty("image").getValue()); 

					accountsmodel.dbaction(" SELECT 1  id ,(SELECT COUNT(id)  FROM registration "
							+ " inner join studentclass ON `current form`=no WHERE  "
							+ " class!='Transfered' AND class!='Allumni' "
							+ " and registration.school='"+LoginController.getschoo()+"' )  AS noofstudent,"
							+ " (SELECT COUNT(id)  FROM teacherregistration  where status='Active' "
							+ " and  school='"+LoginController.getschoo()+"')  AS noofteachers,"
							+ " (SELECT COUNT(id)  FROM subregistration  where status='Active'"
							+ "  and school='"+LoginController.getschoo()+"')  AS noofstaff,"
							+ " (SELECT COUNT(id)  FROM board  where status='Active' and"
							+ " school='"+LoginController.getschoo()+"' )  AS noofboard ",1, 0, 0,0);
					
					List<accountsmodel> newList = new ArrayList<accountsmodel>(accountsmodel.datanew);	
					model.addAttribute("noofstudent", new DecimalFormat("#,###").format(Double.parseDouble(newList.get(0).activeProperty("noofstudent").getValue()))); 		
					model.addAttribute("noofteachers", new DecimalFormat("#,###").format(Double.parseDouble(newList.get(0).activeProperty("noofteachers").getValue()))); 		
					model.addAttribute("noofstaff", new DecimalFormat("#,###").format(Double.parseDouble(newList.get(0).activeProperty("noofstaff").getValue()))); 		
					model.addAttribute("noofboard", new DecimalFormat("#,###").format(Double.parseDouble(newList.get(0).activeProperty("noofboard").getValue()))); 		
					
					
					accountsmodel.dbaction("select class,"
							+ " sum( if( gender = 'Male',(ABS(Total)), 0 ) ) as male,"
							+ " sum( if( gender = 'Female',(ABS(Total)), 0 ) ) as female,"
							+ " (sum( if( gender = 'Male',(ABS(Total)), 0 ) ) +"
							+ " sum( if( gender = 'Female',(ABS(Total)), 0 ) )) total"
							+ " FROM (SELECT class,gender,COUNT(`adm no`) AS total FROM registration"
							+ " right join studentclass on no=`current form` WHERE (`class`!='Allumni' and  `class`!='Transfered' )  "
							+ " and registration.school='"+LoginController.getschoo()+"' GROUP BY `current form`,gender)h GROUP BY class"
							
							+ "  UNION ALL "

							+ " SELECT 'All' Class," + " sum( if( gender = 'Male',(ABS(Total)), 0 ) ) as Male,"
							+ " sum( if( gender = 'Female',(ABS(Total)), 0 ) ) as Female,"
							+ " (sum( if( gender = 'Male',(ABS(Total)), 0 ) ) +"
							+ " sum( if( gender = 'Female',(ABS(Total)), 0 ) )) Total"
							+ " FROM (SELECT class,gender,COUNT(`adm no`) AS total FROM registration "
							+ " right join studentclass on no=`current form` WHERE (`class`!='Allumni' and  `class`!='Transfered' ) "
							+ " and registration.school='"+LoginController.getschoo()+"' GROUP BY `current form`,gender)h",1, 0, 0,0);
					
					List<accountsmodel> newListofstudents = new ArrayList<accountsmodel>(accountsmodel.datanew);	
					List<String> cols = new ArrayList<String>(accountsmodel.collumns);	
					model.addAttribute("collumns", cols); 		
					model.addAttribute("listofstudentsperclass", newListofstudents); 
					
					
					
					accountsmodel.dbaction("SELECT  * FROM (select no, class from studentclass   where class!='Transfered' "
							+ " AND class!='Allumni' and school='"+LoginController.getschoo()+"' ORDER BY NO)j"
							+ " union all "
							+ " SELECT * FROM (select concat(NO,':a:',streanid) AS no,concat(class,' ',stream) AS class "
							+ " from studentclass  "
							+ " cross join studentstreams where class!='Transfered' AND class!='Allumni' "
							+ " and studentclass.school='"+LoginController.getschoo()+"' "
							+ " and studentstreams.school='"+LoginController.getschoo()+"' ORDER BY NO,streanid)d "
							+ " union all "
							+ " SELECT 'All','All classes' ",1, 0, 0,0);
					
					List<accountsmodel> newListofclasses = new ArrayList<accountsmodel>(accountsmodel.datanew);		
					model.addAttribute("classesreports", newListofclasses);
					
					accountsmodel.dbaction("SELECT * FROM hostel where school='"+LoginController.getschoo()+"'",1, 0, 0,0);
					List<accountsmodel> newList1111 = new ArrayList<accountsmodel>(accountsmodel.datanew);		
					model.addAttribute("hostelreport", newList1111);  
					
					accountsmodel.collumns.clear();
					accountsmodel.datanew.clear();
					accountsmodel.collumns.add("religion");				
					
					
					accountsmodel row = new accountsmodel(accountsmodel.collumns);
					row.setallowduplicates(0);
					row.setadmno("1");
					row.setChecked(false);
					row.setActive("religion", "Christian");
					accountsmodel.datanew.add(row);

					row = new accountsmodel(accountsmodel.collumns);
					row.setallowduplicates(0);
					row.setadmno("2");
					row.setChecked(false);
					row.setActive("religion", "Muslim");
					accountsmodel.datanew.add(row);

					row = new accountsmodel(accountsmodel.collumns);
					row.setallowduplicates(0);
					row.setadmno("3");
					row.setChecked(false);
					row.setActive("religion", "Hindu");
					accountsmodel.datanew.add(row);

					row = new accountsmodel(accountsmodel.collumns);
					row.setallowduplicates(0);
					row.setadmno("4");
					row.setChecked(false);
					row.setActive("religion", "Other");
					accountsmodel.datanew.add(row);

					List<accountsmodel> newListreligions = new ArrayList<accountsmodel>(accountsmodel.datanew);
					model.addAttribute("religions", newListreligions);  
					
			        	
					return "fragments/students :: regdashboardmain";        
					
				}  			
				
				
				
				
				 @RequestMapping(value = "/getnuofstudents" , method = RequestMethod.GET)
				 @ResponseBody
				 public String nuofstudents() {	
					
					accountsmodel.dbaction(" SELECT (SELECT COUNT(id)  FROM registration "
							+ " inner join studentclass ON `current form`=no WHERE  "
							+ " registration.class!='Transfered' AND class!='Allumni' and "
							+ " school='"+LoginController.getschoo()+"')  AS noofstudent,"
							+ " (SELECT COUNT(id)  FROM teacherregistration  where status='Active' "
							+ " and school='"+LoginController.getschoo()+"')  AS noofteachers,"
							+ " (SELECT COUNT(id)  FROM subregistration  where status='Active' "
							+ " and school='"+LoginController.getschoo()+"')  AS noofstaff,"
							+ " (SELECT COUNT(id)  FROM board  where status='Active' and "
							+ " school='"+LoginController.getschoo()+"')  AS noofboard ",1, 0, 0,0);
					
				     return accountsmodel.jsondata;
				 }
				
				
				
				@RequestMapping(value = "/getstudentsperclass" , method = RequestMethod.GET)
				 @ResponseBody
				 public String studentsperclass() {	
					
					accountsmodel.dbaction("select class,"
							+ " sum( if( gender = 'Male',(ABS(Total)), 0 ) ) as male,"
							+ " sum( if( gender = 'Female',(ABS(Total)), 0 ) ) as female,"
							+ " (sum( if( gender = 'Male',(ABS(Total)), 0 ) ) +"
							+ " sum( if( gender = 'Female',(ABS(Total)), 0 ) )) total"
							+ " FROM (SELECT class,gender,COUNT(`adm no`) AS total FROM registration"
							+ " right join studentclass on no=`current form` WHERE (`class`!='Allumni' and  `class`!='Transfered' )  "
							+ " and registration.school='"+LoginController.getschoo()+"' GROUP BY `current form`,gender)h "
							+ "  GROUP BY class" + " UNION ALL "

							+ " SELECT 'All' Class," + " sum( if( gender = 'Male',(ABS(Total)), 0 ) ) as Male,"
							+ " sum( if( gender = 'Female',(ABS(Total)), 0 ) ) as Female,"
							+ " (sum( if( gender = 'Male',(ABS(Total)), 0 ) ) +"
							+ " sum( if( gender = 'Female',(ABS(Total)), 0 ) )) Total"
							+ " FROM (SELECT class,gender,COUNT(`adm no`) AS total FROM registration "
							+ " right join studentclass on no=`current form` WHERE (`class`!='Allumni' and  `class`!='Transfered' ) "
							+ " and registration.school='"+LoginController.getschoo()+"' GROUP BY `current form`,gender)h",1, 0, 0,0);
				
				     return accountsmodel.jsondata;
				 }
				 
			 
						
				@RequestMapping(value = "/getlistofreports" , method = RequestMethod.GET)
				 @ResponseBody
				 public String listofreports() {	
					
					accountsmodel.dbaction("SELECT  * FROM (select no, class from studentclass   where class!='Transfered' "
							+ " AND class!='Allumni' and school='"+LoginController.getschoo()+"' ORDER BY NO)j"
							+ " union all "
							+ " SELECT * FROM (select concat(NO,':a:',streanid) AS no,concat(class,' ',stream) AS class "
							+ " from studentclass  "
							+ " cross join studentstreams where class!='Transfered' AND class!='Allumni' "
							+ " and studentclass.school='"+LoginController.getschoo()+"' "
							+ " and studentstreams.school='"+LoginController.getschoo()+"' ORDER BY NO,streanid)d "
							+ " union all "
							+ " SELECT 'All','All classes' ",1, 0, 0,0);
				
				     return accountsmodel.jsondata;
				 }
			
			
		    @GetMapping("/finance")
		    public String gotoaccounts(Model model) { 
		    	
				accountsmodel.dbaction("SELECT * FROM hostel school='"+LoginController.getschoo()+"'",1, 0, 0,0);
				List<accountsmodel> newList = new ArrayList<accountsmodel>(accountsmodel.datanew);		
				model.addAttribute("classes", newList);      	
				
				accountsmodel.dbaction(" SELECT *  from (SELECT s.id,schoolname,SUBSTRING_INDEX(schoolname, ' ', 2) as "
			  			+ " shortname,regname,logo,s.address,city,phone,"
			  			+ " case when surname='' or surname is null then `second name` when `second name`=''  "
			  			+ " or `second name` is null then `first name` when `first name`=''   or `first name` "
			  			+ " is NULL then u.email ELSE '' end as  username,ifnull(image,simage) as image FROM users u "
				 		+ " inner join schooldetails s on u.school=s.id "
				 		+ " LEFT join teacherregistration t on t.id=u.trnu "
				 		+ " where sessionid='"+RequestContextHolder.currentRequestAttributes().getSessionId()+"')d  ",1, 0, 0,0);
			  
				List<accountsmodel> newListdf = new ArrayList<accountsmodel>(accountsmodel.datanew);	
				model.addAttribute("email", 	 	 newListdf.get(0).activeProperty("username").getValue()); 
				model.addAttribute("image", 	 	 newListdf.get(0).activeProperty("image").getValue()); 

				
				accountsmodel.dbaction("select * from (select chartofaccounts.id,Account FROM chartofaccounts  "
						 + " Left JOIN accounttypes ON accounttypes.`id`=chartofaccounts.type "
				         + " where accounttypes.`group`!='Bank' and accounttypes.`group`!='Cash' and account!='ARREARS' "
				         + " AND ACCOUNT!='OVERPAYMENT' and chartofaccounts.school='"+LoginController.getschoo()+"')n   ",1, 0, 0,0);
				List<accountsmodel> newList1 = new ArrayList<accountsmodel>(accountsmodel.datanew);		
				model.addAttribute("voteheads", newList1); 	
				
				
				accountsmodel.dbaction("select * from schoolsemesters where school='"+LoginController.getschoo()+"'",1, 0, 0,0);
				List<accountsmodel> newList11 = new ArrayList<accountsmodel>(accountsmodel.datanew);		
				model.addAttribute("terms", newList11);   				
								
				accountsmodel.dbaction("select * from (select chartofaccounts.id,Account  "
				         + " FROM chartofaccounts  Left JOIN accounttypes ON accounttypes.`id`=chartofaccounts.type "
				         + " where accounttypes.`group`='Bank' or accounttypes.`group`='Cash' "
				         + " and chartofaccounts.school='"+LoginController.getschoo()+"' )n ",1, 0, 0,0);
					List<accountsmodel> newList111 = new ArrayList<accountsmodel>(accountsmodel.datanew);		
				model.addAttribute("bankaccounts", newList111);  				
								
				
				
		    	return "accounts/dashboard";
		    	
		        }     
		    
		    
		    
		    
		    
		    
		    
		    @GetMapping("/exam")
		    public String gotoexam(Model model) { 
		    	
		    	accountsmodel.dbaction("SELECT 1  id ,"
		    			
						+ " (SELECT COUNT(id) AS aboveaverage "
						+ " from  allexams WHERE  school="+LoginController.getschoo()+") totalexams,"
		    			
		    			+ " (SELECT COUNT(id) AS aboveaverage "
		    			+ " from  allexams WHERE performance>=51 AND school="+LoginController.getschoo()+") aboveaverage,"
		    			
		    			+ " (SELECT COUNT(id) AS aboveaverage "
		    			+ "	from  allexams WHERE performance<=50 AND school="+LoginController.getschoo()+" )  AS belowaverage,"
						
						+ " (SELECT COUNT(id) AS aboveaverage "
						+ "	from  allexams WHERE status='Open' AND school="+LoginController.getschoo()+" )  AS activeexams",1, 0, 0,0);
		    
		    	List<accountsmodel> newList = new ArrayList<accountsmodel>(accountsmodel.datanew);	
				model.addAttribute("totalexams", new DecimalFormat("#,###").format(Double.parseDouble(newList.get(0).activeProperty("totalexams").getValue()))); 		
				model.addAttribute("aboveaverage", new DecimalFormat("#,###").format(Double.parseDouble(newList.get(0).activeProperty("aboveaverage").getValue()))); 		
				model.addAttribute("belowaverage", new DecimalFormat("#,###").format(Double.parseDouble(newList.get(0).activeProperty("belowaverage").getValue()))); 		
				model.addAttribute("activeexams", new DecimalFormat("#,###").format(Double.parseDouble(newList.get(0).activeProperty("activeexams").getValue()))); 		
				
				
				accountsmodel.dbaction(" SELECT *  from (SELECT s.id,schoolname,SUBSTRING_INDEX(schoolname, ' ', 2) as "
			  			+ " shortname,regname,logo,s.address,city,phone,"
			  			+ " case when surname='' or surname is null then `second name` when `second name`=''  "
			  			+ " or `second name` is null then `first name` when `first name`=''   or `first name` is NULL"
			  			+ " then u.email ELSE '' end as  username,ifnull(image,simage) as image FROM users u "
				 		+ " inner join schooldetails s on u.school=s.id "
				 		+ " LEFT join teacherregistration t on t.id=u.trnu "
				 		+ " where sessionid='"+RequestContextHolder.currentRequestAttributes().getSessionId()+"')d  ",1, 0, 0,0);
			  
				List<accountsmodel> newListdf = new ArrayList<accountsmodel>(accountsmodel.datanew);	
				model.addAttribute("email", 	 	 newListdf.get(0).activeProperty("username").getValue()); 
				model.addAttribute("image", 	 	 newListdf.get(0).activeProperty("image").getValue()); 

				accountsmodel.dbaction("select class,"
						+ " sum( if( gender = 'Male',(ABS(Total)), 0 ) ) as male,"
						+ " sum( if( gender = 'Female',(ABS(Total)), 0 ) ) as female,"
						+ " (sum( if( gender = 'Male',(ABS(Total)), 0 ) ) +"
						+ " sum( if( gender = 'Female',(ABS(Total)), 0 ) )) total"
						+ " FROM (SELECT class,gender,COUNT(`adm no`) AS total FROM registration"
						+ " right join studentclass on no=`current form` WHERE (`class`!='Allumni' and  `class`!='Transfered' )  "
						+ " and registration.school='"+LoginController.getschoo()+"' GROUP BY `current form`,gender)h GROUP BY class"
						
						+ "  UNION ALL "

						+ " SELECT 'All' Class," + " sum( if( gender = 'Male',(ABS(Total)), 0 ) ) as Male,"
						+ " sum( if( gender = 'Female',(ABS(Total)), 0 ) ) as Female,"
						+ " (sum( if( gender = 'Male',(ABS(Total)), 0 ) ) +"
						+ " sum( if( gender = 'Female',(ABS(Total)), 0 ) )) Total"
						+ " FROM (SELECT class,gender,COUNT(`adm no`) AS total FROM registration "
						+ " right join studentclass on no=`current form` WHERE (`class`!='Allumni' and  `class`!='Transfered' ) "
						+ " and registration.school='"+LoginController.getschoo()+"' GROUP BY `current form`,gender)h",1, 0, 0,0);
				
				List<accountsmodel> newListofstudents = new ArrayList<accountsmodel>(accountsmodel.datanew);	
				List<String> cols = new ArrayList<String>(accountsmodel.collumns);	
				model.addAttribute("collumns", cols); 		
				model.addAttribute("listofstudentsperclass", newListofstudents); 
					
				accountsmodel.dbaction("SELECT * FROM examcategories where school='"+LoginController.getschoo()+"'",1, 0, 0,0);
				List<accountsmodel> newListds = new ArrayList<accountsmodel>(accountsmodel.datanew);		
				model.addAttribute("examcategory", newListds);      	
				
				accountsmodel.dbaction("select * from studentclass where school='"+LoginController.getschoo()+"' "
						+ "and class!='Transfered' AND class!='Allumni'",1, 0, 0,0);
				List<accountsmodel> newList1bv = new ArrayList<accountsmodel>(accountsmodel.datanew);		
				model.addAttribute("classes", newList1bv);      	
				
				accountsmodel.dbaction("SELECT * FROM schoolsemesters where school='"+LoginController.getschoo()+"'",1, 0, 0,0);
				List<accountsmodel> newList11rt = new ArrayList<accountsmodel>(accountsmodel.datanew);		
				model.addAttribute("term", newList11rt);      	
				
			
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
				
				 accountsmodel.dbaction(" SELECT '0' as querystatus,0 as checkstatus,a.id,`Subject group`,"
			 				+ " '' Action FROM subjectgroups a where school='"+LoginController.getschoo()+"' ",1, 0, 0,0);
				List<accountsmodel> newList1111newListofclassesreport = new ArrayList<accountsmodel>(accountsmodel.datanew);		
				model.addAttribute("subjectgroups", newList1111newListofclassesreport); 	
				
				accountsmodel.dbaction("SELECT id,`Subject group` FROM subjectgroups "
		 				+ " where school='"+LoginController.getschoo()+"'  "
		 				+ " UNION ALL "
		 				+ " SELECT 'Overall','Overall' "
		 				+ " UNION ALL "
		 				+ " SELECT 'ALL','Copy to all categories for this class only' "		 				
		 				+ " UNION ALL "
		 				+ " SELECT 'ALLCLASSES','Copy to all categories for all classes' "		 				
		 				+ "",1, 0, 0,0);
		 		
				List<accountsmodel> rty = new ArrayList<accountsmodel>(accountsmodel.datanew);		
				model.addAttribute("subjectgroupswithall", rty); 	
			
				
				accountsmodel.dbaction("SELECT id,`Subject group` FROM subjectgroups "
		 				+ " where school='"+LoginController.getschoo()+"'  "
		 				+ " UNION ALL "
		 				+ " SELECT 'Principal','Principal/Director/Rector' "
		 				+ " UNION ALL "
		 				+ " SELECT 'ALL','Copy to all other categories & class teachers' "		 				
		 				+ "",1, 0, 0,0);
		 		
				List<accountsmodel> nm = new ArrayList<accountsmodel>(accountsmodel.datanew);		
				model.addAttribute("subjectgroupswithallremarks", nm); 	
			
				
				accountsmodel.dbaction("SELECT * FROM grades where school='"+LoginController.getschoo()+"'",1, 0, 0,0);
				List<accountsmodel> grades = new ArrayList<accountsmodel>(accountsmodel.datanew);		
				model.addAttribute("grades", grades);  
				
				
				accountsmodel.dbaction("SELECT id,concat(`First name`,' ',`Second name`,' ',surname) teacher "
						+ "	FROM teacherregistration where status='Active' and school='"+LoginController.getschoo()+"'",1, 0, 0,0);
				List<accountsmodel> teacher = new ArrayList<accountsmodel>(accountsmodel.datanew);		
				model.addAttribute("teacher", teacher);  
				
				
				accountsmodel.dbaction(" SELECT a.id,concat(`First name`,' ',`Second name`,' ',surname,'   -    ',"
						+ " c.class,' ',' ',d.stream) teacher  FROM teacherregistration a"
						+ " INNER JOIN classteachers b ON a.id=b.Teacher"
						+ " INNER JOIN studentclass c ON b.Class=c.`no`"
						+ " INNER JOIN studentstreams d ON b.Stream=d.streanid "
						+ " where a.school='"+LoginController.getschoo()+"' ",1, 0, 0,0);
				
				List<accountsmodel> classteacher = new ArrayList<accountsmodel>(accountsmodel.datanew);		
				model.addAttribute("classteacher", classteacher);  
			
				
				accountsmodel.dbaction(" select * from studentclass where  school='"+LoginController.getschoo()+"' "
						+ " and status='Active' ",1, 0, 0,0);
				List<accountsmodel> classteacher1 = new ArrayList<accountsmodel>(accountsmodel.datanew);		
				model.addAttribute("activecls", classteacher1);  
				
					
		    	return "exam/dashboard";
		    	
		        }    
		    
		    
		     @RequestMapping(value="/exammain", method=RequestMethod.POST)
			 public String gotoexammain(Model model) {
			
		    	 accountsmodel.dbaction("SELECT 1  id ,"
			    			
						+ " (SELECT COUNT(id) AS aboveaverage "
						+ " from  allexams WHERE  school="+LoginController.getschoo()+") totalexams,"
		    			
		    			+ " (SELECT COUNT(id) AS aboveaverage "
		    			+ " from  allexams WHERE performance>=51 AND school="+LoginController.getschoo()+") aboveaverage,"
		    			
		    			+ " (SELECT COUNT(id) AS aboveaverage "
		    			+ "	from  allexams WHERE performance<=50 AND school="+LoginController.getschoo()+" )  AS belowaverage,"
						
						+ " (SELECT COUNT(id) AS aboveaverage "
						+ "	from  allexams WHERE status='Open' AND school="+LoginController.getschoo()+" )  AS activeexams",1, 0, 0,0);
		    	
		    	List<accountsmodel> newList = new ArrayList<accountsmodel>(accountsmodel.datanew);	
				model.addAttribute("totalexams", new DecimalFormat("#,###").format(Double.parseDouble(newList.get(0).activeProperty("totalexams").getValue()))); 		
				model.addAttribute("aboveaverage", new DecimalFormat("#,###").format(Double.parseDouble(newList.get(0).activeProperty("aboveaverage").getValue()))); 		
				model.addAttribute("belowaverage", new DecimalFormat("#,###").format(Double.parseDouble(newList.get(0).activeProperty("belowaverage").getValue()))); 		
				model.addAttribute("activeexams", new DecimalFormat("#,###").format(Double.parseDouble(newList.get(0).activeProperty("activeexams").getValue()))); 		
				
				
				accountsmodel.dbaction(" SELECT *  from (SELECT s.id,schoolname,SUBSTRING_INDEX(schoolname, ' ', 2) as "
			  			+ " shortname,regname,logo,s.address,city,phone,"
			  			+ " case when surname='' or surname is null then `second name` when `second name`=''  "
			  			+ " or `second name` is null then `first name` when `first name`=''   or `first name` is NULL "
			  			+ " then u.email ELSE '' end as  username,ifnull(image,simage) as image FROM users u "
				 		+ " inner join schooldetails s on u.school=s.id "
				 		+ " LEFT join teacherregistration t on t.id=u.trnu "
				 		+ " where sessionid='"+RequestContextHolder.currentRequestAttributes().getSessionId()+"')d  ",1, 0, 0,0);
			  
				List<accountsmodel> newListdf = new ArrayList<accountsmodel>(accountsmodel.datanew);	
				model.addAttribute("email", 	 	 newListdf.get(0).activeProperty("username").getValue()); 
				model.addAttribute("image", 	 	 newListdf.get(0).activeProperty("image").getValue()); 

				
				accountsmodel.dbaction("select class,"
						+ " sum( if( gender = 'Male',(ABS(Total)), 0 ) ) as male,"
						+ " sum( if( gender = 'Female',(ABS(Total)), 0 ) ) as female,"
						+ " (sum( if( gender = 'Male',(ABS(Total)), 0 ) ) +"
						+ " sum( if( gender = 'Female',(ABS(Total)), 0 ) )) total"
						+ " FROM (SELECT class,gender,COUNT(`adm no`) AS total FROM registration"
						+ " right join studentclass on no=`current form` WHERE (`class`!='Allumni' and  `class`!='Transfered' )  "
						+ " and registration.school='"+LoginController.getschoo()+"' GROUP BY `current form`,gender)h GROUP BY class"
						
						+ "  UNION ALL "

						+ " SELECT 'All' Class," + " sum( if( gender = 'Male',(ABS(Total)), 0 ) ) as Male,"
						+ " sum( if( gender = 'Female',(ABS(Total)), 0 ) ) as Female,"
						+ " (sum( if( gender = 'Male',(ABS(Total)), 0 ) ) +"
						+ " sum( if( gender = 'Female',(ABS(Total)), 0 ) )) Total"
						+ " FROM (SELECT class,gender,COUNT(`adm no`) AS total FROM registration "
						+ " right join studentclass on no=`current form` WHERE (`class`!='Allumni' and  `class`!='Transfered' ) "
						+ " and registration.school='"+LoginController.getschoo()+"' GROUP BY `current form`,gender)h",1, 0, 0,0);
				
				List<accountsmodel> newListofstudents = new ArrayList<accountsmodel>(accountsmodel.datanew);	
				List<String> cols = new ArrayList<String>(accountsmodel.collumns);	
				model.addAttribute("collumns", cols); 		
				model.addAttribute("listofstudentsperclass", newListofstudents);
					
				if(LoginController.getloggedinuseridtype()!=2) {
					return "fragments/exam :: modalerror";        
				}else {
					return "fragments/exam :: examdashboardmain";        
				}
			
				
			}  	
		    
		     
		     
		     
		   
			    @GetMapping("/timetable")
			    public String gototimetable(Model model) {   
			    	accountsmodel.dbaction(" SELECT *  from (SELECT s.id,schoolname,SUBSTRING_INDEX(schoolname, ' ', 2) as "
				  			+ " shortname,regname,logo,s.address,city,phone,"
				  			+ " case when surname='' or surname is null then `second name` when `second name`=''  "
				  			+ " or `second name` is null then `first name` when `first name`=''   or `first name` is "
				  			+ " NULL then u.email ELSE '' end as  username,ifnull(image,simage) as image FROM users u "
					 		+ " inner join schooldetails s on u.school=s.id "
					 		+ " LEFT join teacherregistration t on t.id=u.trnu "
					 		+ " where sessionid='"+RequestContextHolder.currentRequestAttributes().getSessionId()+"')d  ",1, 0, 0,0);
				  
					List<accountsmodel> newListdf = new ArrayList<accountsmodel>(accountsmodel.datanew);	
					model.addAttribute("email", 	 	 newListdf.get(0).activeProperty("username").getValue()); 
					model.addAttribute("image", 	 	 newListdf.get(0).activeProperty("image").getValue()); 

					
			    	return "timetable/startpage";
		         }
		    
			    
			    
			    
			    
			    
			    
		    @GetMapping("/sms")
		    public String gotosms(Model model) {  
		    	accountsmodel.dbaction("SELECT `Date_sent`,Recipients,Message,Sent,Delivered,Failed FROM smshistory"
		    			+ " where school='"+LoginController.getschoo()+"'",1, 0, 0,0);
				List<accountsmodel> newListofstudents = new ArrayList<accountsmodel>(accountsmodel.datanew);	
				List<String> cols = new ArrayList<String>(accountsmodel.collumns);	
				model.addAttribute("collumns", cols); 		
				model.addAttribute("smssenthistory", newListofstudents); 
				
				
				accountsmodel.dbaction(" SELECT *  from (SELECT s.id,schoolname,SMSUSERNAME,"
						+ "SMSKEY,SUBSTRING_INDEX(schoolname, ' ', 2) as "
			  			+ " shortname,regname,logo,s.address,city,phone,"
			  			+ " case when surname='' or surname is null then `second name` when `second name`=''  "
			  			+ " or `second name` is null then `first name` when `first name`=''   or `first name` is NULL "
			  			+ " then u.email ELSE '' end as  username,ifnull(image,simage) as image FROM users u "
				 		+ " inner join schooldetails s on u.school=s.id "
				 		+ " LEFT join teacherregistration t on t.id=u.trnu "
				 		+ " where sessionid='"+RequestContextHolder.currentRequestAttributes().getSessionId()+"')d  ",1, 0, 0,0);
			  
				List<accountsmodel> newListdf = new ArrayList<accountsmodel>(accountsmodel.datanew);	
				model.addAttribute("email", 	 	 newListdf.get(0).activeProperty("username").getValue()); 
				model.addAttribute("image", 	 	 newListdf.get(0).activeProperty("image").getValue()); 
				

				AfricasTalking.initialize(newListdf.get(0).activeProperty("SMSUSERNAME").getValue(), 
						newListdf.get(0).activeProperty("SMSKEY").getValue());
				
		    	
		    	return "sms/dashboard";
		        
		    }
		    
		    
		    
		    
		    
		    @RequestMapping(value="/smsmain", method=RequestMethod.POST)
			 public String gotosmsmain(Model model) {
			
		    	accountsmodel.dbaction("SELECT `Date_sent`,Recipients,Message,Sent,Delivered,Failed FROM smshistory"
		    			+ " where school='"+LoginController.getschoo()+"'",1, 0, 0,0);
				List<accountsmodel> newListofstudents = new ArrayList<accountsmodel>(accountsmodel.datanew);	
				List<String> cols = new ArrayList<String>(accountsmodel.collumns);	
				model.addAttribute("collumns", cols); 		
				model.addAttribute("smssenthistory", newListofstudents); 
				
				accountsmodel.dbaction(" SELECT *  from (SELECT s.id,schoolname,SUBSTRING_INDEX(schoolname, ' ', 2) as "
			  			+ " shortname,regname,logo,s.address,city,phone,"
			  			+ " case when surname='' or surname is null then `second name` when "
			  			+ " `second name`=''  or `second name` is null then `first name` when `first name`=''   "
			  			+ " or `first name` is NULL then u.email ELSE '' end as  username,ifnull(image,simage) as image FROM users u "
				 		+ " inner join schooldetails s on u.school=s.id "
				 		+ " LEFT join teacherregistration t on t.id=u.trnu "
				 		+ " where sessionid='"+RequestContextHolder.currentRequestAttributes().getSessionId()+"')d  ",1, 0, 0,0);
			  
				List<accountsmodel> newListdf = new ArrayList<accountsmodel>(accountsmodel.datanew);	
				model.addAttribute("email", 	 	 newListdf.get(0).activeProperty("username").getValue()); 
				model.addAttribute("image", 	 	 newListdf.get(0).activeProperty("image").getValue()); 

				
		    	  	
				return "fragments/sms :: smsdashboardmain";        
				
			}  			
			
			
		    
		    
		    
		    
		    
		    
		    
		    
		    
		    
		    @GetMapping("/biometrics")
		    public String gotobiometrics(Model model) {  
		    	
		    	accountsmodel.dbaction(" SELECT ifnull(stin,0) stin,ifnull(ttin,0) ttin,ifnull(subin,0) subin "
		    			+ " from (SELECT id FROM schooldetails  WHERE id="+LoginController.getschoo()+")j "
		    			
		    			+ " LEFT JOIN( "
		    			+ " SELECT school,ifnull(sum(case when s.`status`=0 then 1 END),0) AS stin "
		    			+ " FROM studentsloginlogout s  "
		    			+ " INNER JOIN registration r ON s.studentid=r.id  "
		    			+ " WHERE DATE(DATE)=date(NOW()) AND r.school="+LoginController.getschoo()+"  "
		    			+ " )k "
		    			+ " ON k.school=id "
		    			
		    			+ " LEFT JOIN (SELECT school, "
		    			+ " ifnull(sum(case when s.`status`=0  then 1 END),0) AS ttin "
		    			+ " FROM teachersloginlogout s  "
		    			+ " LEFT JOIN teacherregistration r ON s.id=r.id  "
		    			+ " WHERE DATE(DATE)=date(NOW()) AND r.school="+LoginController.getschoo()+"  "
		    			+ " )l "
		    			+ " ON l.school=id "
		    			
		    			+ " LEFT JOIN (SELECT school, "
		    			+ " ifnull(sum(case when s.`status`=0  then 1 END),0) AS subin "
		    			+ " FROM subloginlogout s  "
		    			+ " LEFT JOIN subregistration d ON d.id=s.id  "
		    			+ " WHERE DATE(DATE)=date(NOW()) AND d.school="+LoginController.getschoo()+"  "
		    			+ " )m "
		    			+ " ON m.school=id ",1, 0, 0,0);
		    	
				List<accountsmodel> newList = new ArrayList<accountsmodel>(accountsmodel.datanew);	
				model.addAttribute("stin", new DecimalFormat("#,###").format(Double.parseDouble(newList.get(0).activeProperty("stin").getValue()))); 		
				model.addAttribute("ttin", new DecimalFormat("#,###").format(Double.parseDouble(newList.get(0).activeProperty("ttin").getValue()))); 		
				model.addAttribute("subin", new DecimalFormat("#,###").format(Double.parseDouble(newList.get(0).activeProperty("subin").getValue()))); 		
					
				accountsmodel.dbaction("SELECT * from (SELECT Class,"
						+ " ifnull(sum(case when s.`status`=0 then 1 END),0) AS tin,"
						+ " ifnull(sum(case when s.`status`=1 then 1 END),0) AS tout  FROM studentsloginlogout s"
						+ " INNER JOIN registration r ON s.studentid=r.id "
						+ " INNER JOIN studentclass c ON NO=`current form` "
						+ " INNER JOIN leaveout_reason l ON l.id=s.reason_id"
						+ " WHERE DATE(DATE)=date(NOW()) AND r.school="+LoginController.getschoo()+""
						+ " GROUP BY NO)f ",1, 0, 0,0);
			  
				List<accountsmodel> newListdf = new ArrayList<accountsmodel>(accountsmodel.datanew);	
				List<String> cols = new ArrayList<String>(accountsmodel.collumns);	
				model.addAttribute("collumns", cols); 		
				model.addAttribute("listofstudentsperclass", newListdf); 
				
	
		    	return "biometrics/dashboard";
		        
		    }
		    
		    
		    
		     @RequestMapping(value="/biometricsmain", method=RequestMethod.POST)
			 public String biometricsmain(Model model) {
			
		    	accountsmodel.dbaction(" SELECT ifnull(stin,0) stin,ifnull(ttin,0) ttin,ifnull(subin,0) subin "
		    			+ " from (SELECT id FROM schooldetails  WHERE id="+LoginController.getschoo()+")j "
		    			
		    			+ " LEFT JOIN( "
		    			+ " SELECT school,ifnull(sum(case when s.`status`=0 then 1 END),0) AS stin "
		    			+ " FROM studentsloginlogout s  "
		    			+ " INNER JOIN registration r ON s.studentid=r.id  "
		    			+ " WHERE DATE(DATE)=date(NOW()) AND r.school="+LoginController.getschoo()+"  "
		    			+ " )k "
		    			+ " ON k.school=id "
		    			
		    			+ " LEFT JOIN (SELECT school, "
		    			+ " ifnull(sum(case when s.`status`=0  then 1 END),0) AS ttin "
		    			+ " FROM teachersloginlogout s  "
		    			+ " LEFT JOIN teacherregistration r ON s.id=r.id  "
		    			+ " WHERE DATE(DATE)=date(NOW()) AND r.school="+LoginController.getschoo()+"  "
		    			+ " )l "
		    			+ " ON l.school=id "
		    			
		    			+ " LEFT JOIN (SELECT school, "
		    			+ " ifnull(sum(case when s.`status`=0  then 1 END),0) AS subin "
		    			+ " FROM subloginlogout s  "
		    			+ " LEFT JOIN subregistration d ON d.id=s.id  "
		    			+ " WHERE DATE(DATE)=date(NOW()) AND d.school="+LoginController.getschoo()+"  "
		    			+ " )m "
		    			+ " ON m.school=id ",1, 0, 0,0);
		    	
				List<accountsmodel> newList = new ArrayList<accountsmodel>(accountsmodel.datanew);	
				model.addAttribute("stin", new DecimalFormat("#,###").format(Double.parseDouble(newList.get(0).activeProperty("stin").getValue()))); 		
				model.addAttribute("ttin", new DecimalFormat("#,###").format(Double.parseDouble(newList.get(0).activeProperty("ttin").getValue()))); 		
				model.addAttribute("subin", new DecimalFormat("#,###").format(Double.parseDouble(newList.get(0).activeProperty("subin").getValue()))); 		
					
				accountsmodel.dbaction("SELECT * from (SELECT Class,"
						+ " ifnull(sum(case when s.`status`=0 then 1 END),0) AS tin,"
						+ " ifnull(sum(case when s.`status`=1 then 1 END),0) AS tout  FROM studentsloginlogout s"
						+ " INNER JOIN registration r ON s.studentid=r.id "
						+ " INNER JOIN studentclass c ON NO=`current form` "
						+ " INNER JOIN leaveout_reason l ON l.id=s.reason_id"
						+ " WHERE DATE(DATE)=date(NOW()) AND r.school="+LoginController.getschoo()+""
						+ " GROUP BY NO)f ",1, 0, 0,0);
			  
				List<accountsmodel> newListdf = new ArrayList<accountsmodel>(accountsmodel.datanew);	
				List<String> cols = new ArrayList<String>(accountsmodel.collumns);	
				model.addAttribute("collumns", cols); 		
				model.addAttribute("listofstudentsperclass", newListdf); 
				
		    	  	
				return "fragments/biometrics :: biometricsdashboardmain";        
				
			}  		
		    
		    
		    
		    
		    
		    
		    
		    
		    
		    @GetMapping("/hr")
		    public String gotohr(Model model) {    	
		    	return "payroll/startpage";
		         }
		    
		    
		    @GetMapping("/store")
		    public String gotostore(Model model) {    	
		    	return "inventory/startpage";
		         }
		    
		    
		    @GetMapping("/library")
		    public String gotolibrary(Model model) {    	
		    	return "library/startpage";
		         }
		    
		    
		    @GetMapping("/welfare")
		    public String gotowelfare(Model model) {    	
		    	return "welfare/startpage";
		        }
		    
		    
		    @GetMapping("/vlab")
		    public String gotovlab(Model model) {    	
		    	return "virtuallabs/startpage";
		        }
	
}
