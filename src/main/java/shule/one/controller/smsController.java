package shule.one.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.servlet.http.HttpServletRequest;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.json.simple.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.africastalking.AfricasTalking;
import com.africastalking.ApplicationService;
import com.africastalking.SmsService;
import com.africastalking.application.ApplicationResponse;
import com.africastalking.sms.Recipient;

import shule.one.DbConnector;
import shule.one.entity.accountsmodel;
import shule.one.entity.jsontosql;


@Controller
public class smsController {

	 
	 	@RequestMapping(value="/newmessage", method=RequestMethod.POST)
		 public String gotowelfare(Model model) { 
	 		
	 		 	if(LoginController.getloggedinuseridtype()!=2) {
					return "fragments/exam :: modalerror";        
				}else {
					
					return "fragments/sms :: nemsgfrag";				       
				}
	 		
	    	
	 	}
	 	
	 	
	 	
	 	
		 @RequestMapping(value = "/api/sms/loadlisttosendsms" , method = RequestMethod.GET)
		 @ResponseBody
		 public String loadleaveoutsdata() {
			 
				accountsmodel.dbaction(" SELECT * FROM (SELECT @rownum := @rownum + 1 as Sn,'0' as querystatus,`Group` FROM ("
		 				+ " SELECT class as 'Group' FROM  studentclass  "
		 				+ " WHERE class!='Allumni' AND class!='Transfered' "
		 				+ " and school='"+LoginController.getschoo()+"' GROUP BY class "
		 				+ " UNION ALL  "
		 				
		 				+ " SELECT concat(s.class,'   ',d.stream) FROM registration r "
		 				+ " INNER JOIN studentclass s on r.`current form`=s.`no` "
		 				+ " INNER JOIN studentstreams d on d.streanid=r.STREAM "
		 				+ " WHERE r.school="+LoginController.getschoo()+" GROUP BY no,streanid "
		 				
		 				+ " UNION ALL  "
		 				+ " SELECT 'Teachers' "
		 				+ " UNION ALL  "
		 				+ " SELECT 'Support staff' "
		 				+ " UNION ALL  "
		 				+ " SELECT 'Board members' )l"
		 				+ " cross join (select @rownum := 0) r)g",1, 0, 0,0);
		
			 
		     return accountsmodel.jsondata;
		 }
	 	
		 
		 
		 @RequestMapping(value = "/api/sms/getaccoutbal" , method = RequestMethod.GET)
		 @ResponseBody
		 public String welcome() throws IOException {	
			 
			 String username = "";   
			 String apiKey = "";      
			 String sms_account = "";      
			
			 Connection conn=null;
		 		
	 		 try {
	 			 
	 			conn = DbConnector.dataSource.getConnection();
	 		
			 ResultSet rt1 = conn.prepareStatement("select SMSKEY,SMSUSERNAME,SMSID,sms_account from schooldetails where "
		    			+ " id='"+LoginController.getschoo()+"' ").executeQuery();
				while (rt1.next()) {
				
				 	username=rt1.getString("SMSUSERNAME");
			 		apiKey=rt1.getString("SMSKEY");
			 		sms_account=rt1.getString("sms_account");

				}
				
	 		} catch (Exception e1) {
				e1.printStackTrace();
				 accountsmodel.response =e1.getMessage();
					
 		   }finally {
		    if (conn != null) try { conn.close(); } catch (SQLException ignore) {}
		  }		
		   
			
				
			 
			 if (sms_account.equals("Mobitech")) {

					BufferedReader br = checkBalanceMobitech(apiKey,username);
					String bal="";
					for (String line = br.readLine(); line != null; line = br.readLine()) {
						bal=line.replace("{\"balance\":", "").replace("}", "");
					}

					return "[{\"smsaccbal\" : \" KES "+
				 			new DecimalFormat("#,###.##").format(Double.parseDouble(bal))+"\"}]";	 				 			
					
				} 
			 
			 else{
					
					return "[{\"smsaccbal\" : \" KES "+
				 			new DecimalFormat("#,###.##").format(Double.parseDouble(
				 					checkBalanceAfricasTalking(apiKey,username).replace("KES ", "")))+"\"}]";	 				 			
					
				}
			 
 			 }
	 	
	 	
	 
	 	private String checkBalanceAfricasTalking(String apiKey,String username) {
	 		ApplicationResponse balance = null ;
	 		try {
	 			
	 			ApplicationService balanceQuery = AfricasTalking.getService(AfricasTalking.SERVICE_APPLICATION);
				 balance = balanceQuery.fetchApplicationData();

			} catch (Exception e) {
				e.printStackTrace();
			}
	 		
			return balance.userData.balance.toString();
		}
	 	
	 	
	 	
	 	private BufferedReader checkBalanceMobitech(String apiKey,String username) {
			BufferedReader buf = null;
			try {
				JSONObject json = new JSONObject();
				json.put("api_key", apiKey);
				json.put("username", username);

				CloseableHttpClient httpClient = HttpClientBuilder.create().build();

				try {
					HttpPost request = new HttpPost("http://bulksms.mobitechtechnologies.com/api/account_balance");
					StringEntity params = new StringEntity(json.toString());
					request.addHeader("content-type", "application/json");
					request.setEntity(params);

					HttpResponse response = httpClient.execute(request);

					InputStream ips = response.getEntity().getContent();
					buf = new BufferedReader(new InputStreamReader(ips, "UTF-8"));
					// handle response here...
				} catch (Exception ex) {
					ex.printStackTrace();
				} finally {
					//httpClient.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			return buf;
		}
	 	
		 
		 @RequestMapping(value = "/api/sms/postsmslisttogetphones")
		 @ResponseBody
		 public String download(@RequestBody  JSONObject  search, HttpServletRequest request) {
			
			  String sms_account = "";      
			
			 Connection conn=null;
	 		 try {
	 			conn = DbConnector.dataSource.getConnection();
			 ResultSet rt1 = conn.prepareStatement("select SMSKEY,SMSUSERNAME,SMSID,sms_account from schooldetails where "
		    			+ " id='"+LoginController.getschoo()+"' ").executeQuery();
				while (rt1.next()) {
				 	sms_account=rt1.getString("sms_account");
				}
				
	 		} catch (Exception e1) {
				e1.printStackTrace();
				 accountsmodel.response =e1.getMessage();
					
 		   }finally {
		    if (conn != null) try { conn.close(); } catch (SQLException ignore) {}
		  }		
			 
			 String groups=search.get("groups").toString();
			 String bothorsingleparents=search.get("bothorsingleparents").toString();
			 recipients="";
			 
			 
			 String numstoPPEND="";
			 if(sms_account.equals("AFRICASTALKING") || sms_account.equals("Africastalking")) { 
				 numstoPPEND="+254";				  
			 }else {
				 numstoPPEND="0";				  
			 }
			 
			 
			 List<String> sql=new ArrayList<String>();		
		    	
			 	List<String> crunchifyListNew=new ArrayList<String>();		
		    	crunchifyListNew = Arrays.asList(groups.replace("[", "").replace("]", "").split("\\s*,\\s*"));
		    	
		    	for(String s: crunchifyListNew){
		    		
		    		recipients=crunchifyListNew.toString();
		    		
		    		if(s.equals("Teachers")) {
		    			sql.add("  UNION ALL SELECT RIGHT(`phone nu`,9) as phone FROM teacherregistration WHERE STATUS='Active' "
		    					+ " and school='"+LoginController.getschoo()+"' AND CHAR_LENGTH(RIGHT(`phone nu`,9))>=9 ");
		    		}
		    		else if(s.equals("Support staff")) {
		    			sql.add("  UNION ALL  SELECT RIGHT(`phone nu`,9)  as phone  FROM subregistration WHERE STATUS='Active' "
		    					+ " and school='"+LoginController.getschoo()+"' AND CHAR_LENGTH(RIGHT(`phone nu`,9))>=9");
		    		}
		    		else if(s.equals("Board members")) {
		    			sql.add("  UNION ALL  SELECT RIGHT(`phone`,9)  as phone  FROM board WHERE STATUS='Active' "
		    					+ " and school='"+LoginController.getschoo()+"' AND CHAR_LENGTH(RIGHT(`phone`,9))>=9 ");
		    		}
		    		
		    		else if(s.contains("   ")) {
		    			
		    			if(bothorsingleparents.equals("One parent")) {
		    				
			    			sql.add("  UNION ALL   SELECT RIGHT(REPLACE(`Fathers phone`, ' ', ''),9)  as phone  FROM registration r "
			    					+ " inner join studentclass on NO=`current form` "
			    					+ "	inner join studentstreams s on streanid=r.stream "
							 		+ " WHERE  class='"+s.split("   ")[0]+"' and s.stream='"+s.split("   ")[1]+"' "
							 		+ " and r.school='"+LoginController.getschoo()+"' AND CHAR_LENGTH(RIGHT(REPLACE(`Fathers phone`, ' ', ''),9))>=9 ");	
			    			
		    			}else if(bothorsingleparents.equals("Both parents")) {
		    				
			    			sql.add("  UNION ALL  SELECT RIGHT(REPLACE(`Fathers phone`, ' ', ''),9)  as phone  FROM registration r"
			    					+ " inner join studentclass on NO=`current form` "
			    					+ "	inner join studentstreams s on streanid=r.stream "
							 		+ " WHERE  class='"+s.split("   ")[0]+"' and s.stream='"+s.split("   ")[1]+"' "
							 		+ " and r.school='"+LoginController.getschoo()+"' AND CHAR_LENGTH(RIGHT(REPLACE(`Fathers phone`, ' ', ''),9))>=9 "
						    		
			    					+ " UNION all "
			    					
			    					+ " SELECT RIGHT(REPLACE(`MotherPhone`, ' ', ''),9) FROM registration r"
			    					+ " inner join studentclass on NO=`current form` "
			    					+ "	inner join studentstreams s on streanid=r.stream "
							 		+ " WHERE  class='"+s.split("   ")[0]+"' and s.stream='"+s.split("   ")[1]+"'  "
							 		+ " and r.school='"+LoginController.getschoo()+"' AND CHAR_LENGTH(RIGHT(REPLACE(`MotherPhone`, ' ', ''),9))>=9 ");
			    			
			    			
		    			}else {
		    				
			    			sql.add("  UNION ALL  SELECT RIGHT(REPLACE(`Fathers phone`, ' ', ''),9)  as phone  FROM registration r"
			    					+ " inner join studentclass on NO=`current form` "
			    					+ "	inner join studentstreams s on streanid=r.stream "
							 		+ " WHERE  class='"+s.split("   ")[0]+"' and s.stream='"+s.split("   ")[1]+"' "
							 		+ " and r.school='"+LoginController.getschoo()+"' AND CHAR_LENGTH(RIGHT(REPLACE(`Fathers phone`, ' ', ''),9))>=9 "
			    					
			    					+ " UNION all "
			    					
			    					+ " SELECT RIGHT(REPLACE(`MotherPhone`, ' ', ''),9) FROM registration r"
			    					+ " inner join studentclass on NO=`current form` "
			    					+ "	inner join studentstreams s on streanid=r.stream "
							 		+ " WHERE  class='"+s.split("   ")[0]+"' and s.stream='"+s.split("   ")[1]+"' "
							 		+ " and r.school='"+LoginController.getschoo()+"' AND CHAR_LENGTH(RIGHT(REPLACE(`MotherPhone`, ' ', ''),9))>=9 "
			    					
			    					+ " UNION all "
			    					
			    					+ " SELECT RIGHT(REPLACE(`Guardianphone`, ' ', ''),9) FROM registration r"
			    					+ " inner join studentclass on NO=`current form` "
			    					+ "	inner join studentstreams s on streanid=r.stream "
							 		+ " WHERE  class='"+s.split("   ")[0]+"' and s.stream='"+s.split("   ")[1]+"' "
							 		+ " and r.school='"+LoginController.getschoo()+"' "
							 		+ " AND CHAR_LENGTH(RIGHT(REPLACE(`Guardianphone`, ' ', ''),9))>=9");
		    			}
		    			
		    		}
		    		
		    		
		    		
		    		
		    		else {
		    			
		    			if(bothorsingleparents.equals("One parent")) {
			    			
		    				sql.add("  UNION ALL   SELECT RIGHT(REPLACE(`Fathers phone`, ' ', ''),9)  as phone FROM registration "
			    					+ " inner join studentclass on NO=`current form` "
			    					+ " WHERE  class='"+s+"'  and registration.school='"+LoginController.getschoo()+"' "
			    					+ " AND CHAR_LENGTH(RIGHT(REPLACE(`Fathers phone`, ' ', ''),9))>=9 ");	
			    			
		    			}else if(bothorsingleparents.equals("Both parents")) {
		    				
			    			sql.add("  UNION ALL  SELECT RIGHT(REPLACE(`Fathers phone`, ' ', ''),9)  as phone FROM registration "
			    					+ " inner join studentclass on NO=`current form` "
			    					+ " WHERE  registration.school='"+LoginController.getschoo()+"' and class='"+s+"' "
			    					+ " AND CHAR_LENGTH(RIGHT(REPLACE(`Fathers phone`, ' ', ''),9))>=9 "
			    					
			    					+ " UNION all "
			    					
			    					+ " SELECT RIGHT(REPLACE(`MotherPhone`, ' ', ''),9) FROM registration "
			    					+ " inner join studentclass on NO=`current form` "
			    					+ " WHERE  class='"+s+"' and registration.school='"+LoginController.getschoo()+"' "
			    					+ " AND CHAR_LENGTH(RIGHT(REPLACE(`MotherPhone`, ' ', ''),9))>=9 ");
			    			
		    			}else {
		    				
			    			sql.add("  UNION ALL  SELECT RIGHT(REPLACE(`Fathers phone`, ' ', ''),9)  as phone FROM registration "
			    					+ " inner join studentclass on NO=`current form` "
			    					+ " WHERE  registration.school='"+LoginController.getschoo()+"' and class='"+s+"' "
			    					+ " AND CHAR_LENGTH(RIGHT(REPLACE(`Fathers phone`, ' ', ''),9))>=9 "
			    					
			    					+ " UNION all "
			    					
			    					+ " SELECT RIGHT(REPLACE(`MotherPhone`, ' ', ''),9) FROM registration "
			    					+ " inner join studentclass on NO=`current form` "
			    					+ " WHERE  registration.school='"+LoginController.getschoo()+"' and class='"+s+"' "
			    					+ " AND CHAR_LENGTH(RIGHT(REPLACE(`MotherPhone`, ' ', ''),9))>=9 "
			    					
			    					+ " UNION all "
			    					
			    					+ " SELECT RIGHT(REPLACE(`Guardianphone`, ' ', ''),9) FROM registration "
			    					+ " inner join studentclass on NO=`current form` "
			    					+ " WHERE  registration.school='"+LoginController.getschoo()+"' and "
			    					+ " class='"+s+"' AND CHAR_LENGTH(RIGHT(REPLACE(`Guardianphone`, ' ', ''),9))>=9");
		    			}
		    			
		    		}
		    		
		    	}
		    	
		    	
		    	String query="SELECT DISTINCT(concat('"+numstoPPEND+"',phone)) AS phone "
		    			+ " FROM ( "+sql.toString().replace("[  UNION ALL  ", "").replace(",  UNION ALL  ", "  UNION ALL  ").
		    			replace("]", "").replace(",   UNION ALL", " UNION ALL").replace("[  UNION ALL ", "")+")j";
		    	
    			
		    	accountsmodel.dbaction(query,1, 0, 0,0);
	    		
		    	
		    	
	    		if(accountsmodel.liststring != null && accountsmodel.liststring.isEmpty()) {
			 		return accountsmodel.liststring.toString().replace("[", "").replace("]", "");	
	    		}else {
			 		return accountsmodel.liststring.toString().replace("[", "").replace("]", "");
	    		}
	    		
	    		
	    		
		 }
		 
	 	
		 static int alredysent=0;		 
		 static int Totalrecipients=0;
		 static int failed=0;
		 static String recipients="";

		 
		 
		 
		 
		 
		 
		 
		 
		 
		 
		 
		 @SuppressWarnings("unchecked")
		public static void sendSMSMobitech(String phone, String message,int saveornot,String username,String apiKey,String smsid) {
				try {
					
					JSONObject json = new JSONObject();
					json.put("api_key", apiKey);
					json.put("username", username);
					
					if (smsid.isEmpty()) {
						json.put("sender_id", "22136");
					} else {
						json.put("sender_id", smsid);
					}
					
					json.put("message", message);
					json.put("phone", phone);
					
					System.out.println(phone);
					 

					CloseableHttpClient httpClient = HttpClientBuilder.create().build();

					try {
						
						HttpPost request = new HttpPost("http://bulksms.mobitechtechnologies.com/api/sendsms");
						StringEntity params = new StringEntity(json.toString());
						request.addHeader("content-type", "application/json");
						request.setEntity(params);

						HttpResponse response = httpClient.execute(request);
						System.out.println(response);
						  
						InputStream ips = response.getEntity().getContent();
						BufferedReader buf = new BufferedReader(new InputStreamReader(ips, "UTF-8"));

						alredysent=phone.split(",").length;
						Totalrecipients=phone.split(",").length;
						alredysent=phone.split(",").length;
						
						if(recipients.isEmpty()) {
							 recipients="Selected Students"; 
						 }
						
						
						 JSONObject item = new JSONObject();
						 item.put("school",LoginController.getschoo());
						 item.put("Message",message);
						 item.put("Recipients", recipients);
						 item.put("Date_sent", IndexController.stringdate);
						 item.put("phones", phone);
						 item.put("sent", Totalrecipients);
						 item.put("Delivered", alredysent);
						 item.put("Failed", "0");
						 
						 if(saveornot==1) {
							 jsontosql.jsontosqlaction("smshistory", item, 2,"");						 	 
						 }
						 
						 
						accountsmodel.response = "Messages sent successfully";
						
						buf.close();

					} catch (Exception ex) {
						accountsmodel.response  = "Messages couldnt be sent \n" + ex.getLocalizedMessage();
						ex.printStackTrace();
					} 
					
				} catch (Exception e) {
					accountsmodel.response = "Messages couldnt be sent \n" + e.getLocalizedMessage();
					e.printStackTrace();
				}
			}
		 
		 
		 
		 
		 
		 
			@SuppressWarnings("unchecked")
			public static List<Recipient> sendSMSAfricastalking(String[] phone, String message,int saveornot,
					String username,String apiKey,String smsid) {
				List<Recipient> response = null;
				try {
					
					alredysent=0;
					
					SmsService sms = AfricasTalking.getService(AfricasTalking.SERVICE_SMS);

					if (smsid.isEmpty()) {
						response = sms.send(message, phone, true);

					} else {
						
						response = sms.send(message, smsid, phone, true);
						System.out.println("none noned   "+response);

					}
					
					
					
					Totalrecipients=phone.length;
					 
					 for (Recipient recipient : response) {				 
						 	
						 	if(recipient.status.equals("Success")) {
								alredysent++;
							}else {
								failed++;						  
							}
					 }
					 
					 if(recipients.isEmpty()) {
						 recipients="Selected Students"; 
					 }
					 
					 JSONObject item = new JSONObject();
					 item.put("school",LoginController.getschoo());
					 item.put("Message",message);
					 item.put("Recipients", recipients);
					 item.put("Date_sent", IndexController.stringdate);
					 item.put("phones", accountsmodel.liststring.toString().replace("[", "").replace("]", ""));
					 item.put("sent", Totalrecipients);
					 item.put("Delivered", alredysent);
					 item.put("Failed", failed);
					 
					 if(saveornot==1) {
						 jsontosql.jsontosqlaction("smshistory", item, 2,"");						 	 
					 }
					 
					accountsmodel.response = "Messages sent successfully";

				} catch (Exception e) {
					accountsmodel.response = "Messages couldnt be sent \n" + e.getLocalizedMessage();

					e.printStackTrace();
				}
				return response;

			}
			
			
		 
		 
		 
		 @SuppressWarnings("unchecked")
		@RequestMapping(value = "/api/sms/sendthattext")
		 @ResponseBody
		 public String sendthattext(@RequestBody  JSONObject  search, HttpServletRequest request) {
			 
			 
			 	if(LoginController.getloggedinuseridtype()!=2) {
					return "fragments/exam :: modalerror";        
				}else {
					
					
					 String username = "";      
					 String smsid = "";      
					 String apiKey = "";      
					 String sms_account = "";      
					 
					 Connection conn=null;
			 		 try {
			 			conn = DbConnector.dataSource.getConnection();
					 ResultSet rt1 = conn.prepareStatement("select SMSKEY,SMSUSERNAME,SMSID,sms_account from schooldetails where "
				    			+ " id='"+LoginController.getschoo()+"' ").executeQuery();
						while (rt1.next()) {
							username=rt1.getString("SMSUSERNAME");
					 		smsid=rt1.getString("SMSID");
					 		apiKey=rt1.getString("SMSKEY");
					 		sms_account=rt1.getString("sms_account");						}
			 		} catch (Exception e1) {
						e1.printStackTrace();
						 accountsmodel.response =e1.getMessage();
		 		   }finally {
				    if (conn != null) try { conn.close(); } catch (SQLException ignore) {}
				  }		
				 
			 		 
			 		 
					 try {
						 
						  Totalrecipients=0;
						  failed=0;
						  alredysent=0;
						 
						  String textmsg=search.get("sms").toString();
						  
						  String[] stringArray = accountsmodel.liststring.toArray(new String[0]);
		 
						  
						  
						  if (sms_account.equals("Mobitech")) {
								sendSMSMobitech(accountsmodel.liststring.toString().replace("[", "").replace("]", "").
										replaceAll("\\s+","") , textmsg,0,username,apiKey,smsid);
						  } else {
								sendSMSAfricastalking(stringArray, textmsg,1,username,apiKey,smsid);
						  }
						 
			 		
						  recipients="";
					 
						 accountsmodel.liststring.clear();
						 
						 
						 } catch (Exception e) {
							 e.printStackTrace();
							 accountsmodel.response=""+e; 
						
						 }
					 
					 return accountsmodel.jsondata="[{\"querystatus\" : \""+accountsmodel.response+"\",\"alredysent\" : \""+alredysent+"\"}]";	 				 			
		 
				} 
		 }
		 
		 
		 
		 
		 @RequestMapping(value = "/api/sms/getsearchstudents")
		 @ResponseBody
		 public String getsearchstudents() {
			
			 
			 String sms_account = "";      
				
			 Connection conn=null;
	 		 try {
	 			conn = DbConnector.dataSource.getConnection();
			 ResultSet rt1 = conn.prepareStatement("select SMSKEY,SMSUSERNAME,SMSID,sms_account from schooldetails where "
		    			+ " id='"+LoginController.getschoo()+"' ").executeQuery();
				while (rt1.next()) {
				 	sms_account=rt1.getString("sms_account");
				}
				
	 		} catch (Exception e1) {
				e1.printStackTrace();
				 accountsmodel.response =e1.getMessage();
					
 		   }finally {
		    if (conn != null) try { conn.close(); } catch (SQLException ignore) {}
		  }		
			 
			if(sms_account.equals("AFRICASTALKING") || sms_account.equals("Africastalking")) { 
				
				accountsmodel.dbaction("SELECT '0' as querystatus,b.* from (SELECT id,`Adm no` as 'Adm',"
					 	+ " concat(`first name`,' ',`SECOND NAME`,' ',surname) AS Name,"
		       			+ " concat(class,' ',studentstreams.stream) AS class,"
		       			
		       			+ " case when CHAR_LENGTH(RIGHT(REPLACE(`Fathers phone`, ' ', ''),9))>=9 then "
		       			+ " concat('+254',RIGHT(REPLACE(`Fathers phone`, ' ', ''),9)) else '' end as 'Fathers phone',"
					 	+ " case when CHAR_LENGTH(RIGHT(REPLACE(`MotherPhone`, ' ', ''),9))>=9   then "
					 	+ " concat('+254',RIGHT(MotherPhone,9))       else '' end as 'Mother Phone',"
					 	+ " case when CHAR_LENGTH(RIGHT(REPLACE(`Guardianphone`, ' ', ''),9))>=9 then "
					 	+ " concat('+254',RIGHT(Guardianphone,9))   else '' end as 'Guardian phone',"
					 	
					 	+ " '' as action from registration  "
					 	+ " inner join studentclass on  no = `CURRENT FORM` "
		       			+ " inner join studentstreams on  streanid = registration.stream "
		       			+ " where registration.school='"+LoginController.getschoo()+"' and class!='Transfered' AND class!='Allumni')b "
		       			+ " order by id ",1, 0, 0,0);
			 
			}else {
				
				accountsmodel.dbaction("SELECT '0' as querystatus,b.* from (SELECT id,`Adm no` as 'Adm',"
					 	+ " concat(`first name`,' ',`SECOND NAME`,' ',surname) AS Name,"
		       			+ " concat(class,' ',studentstreams.stream) AS class,"
		       			+ " case when CHAR_LENGTH(RIGHT(REPLACE(`Fathers phone`, ' ', ''),9))>=9 then "
		       			+ " concat('0',RIGHT(REPLACE(`Fathers phone`, ' ', ''),9)) else '' end as 'Fathers phone',"
					 	+ " case when CHAR_LENGTH(RIGHT(REPLACE(`MotherPhone`, ' ', ''),9))>=9   then "
					 	+ " concat('0',RIGHT(MotherPhone,9))       else '' end as 'Mother Phone',"
					 	+ " case when CHAR_LENGTH(RIGHT(REPLACE(`Guardianphone`, ' ', ''),9))>=9 then "
					 	+ " concat('0',RIGHT(Guardianphone,9))   else '' end as 'Guardian phone','' as action from registration  "
					 	+ " inner join studentclass on  no = `CURRENT FORM` "
		       			+ " inner join studentstreams on  streanid = registration.stream "
		       			+ " where registration.school='"+LoginController.getschoo()+"' and class!='Transfered' AND class!='Allumni')b "
		       			+ " order by id ",1, 0, 0,0);
			
			}
			
			
			
		     return accountsmodel.jsondata;
		     
		 } 
		
	   		
		 @RequestMapping(value = "/api/sms/receivenumbersfromsearchstudent")
		 @ResponseBody
		 public String receivenumbersfromsearchstudent(@RequestBody  JSONObject  search, HttpServletRequest request) {
		 
			  	String numarray=search.get("numarray").toString();

			  	accountsmodel.liststring.clear();
			   	
			  	accountsmodel.liststring = Stream.of(numarray.replace("[", "").replace("]", "").split(","))
		                .map(String::trim)
		                .collect(Collectors.toList());
			  	
			  	
	 			return "";	 				 			
		 } 

}
