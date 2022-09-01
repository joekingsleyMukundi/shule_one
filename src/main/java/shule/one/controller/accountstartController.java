package shule.one.controller;



import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.json.simple.JSONObject;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import shule.one.entity.accountsmodel;
import shule.one.entity.jsontosql;



@RestController
public class accountstartController {
	
	
	// Fee structure
	
	@RequestMapping(value = "/postclasstogetfeestructure")
	 @ResponseBody
	 public String getclassfeestructure(@RequestBody  JSONObject  search, HttpServletRequest request) {
		
		 String classs	=search.get("classs").toString();
		 String term	=search.get("term").toString();
		 String year	=search.get("year").toString();
		 
		 String action=search.get("action")+"";
		 		 
		 if(action.equals("4")) {
			 
			 
			 JSONObject item = new JSONObject();
			 item.put("particulars",search.get("id").toString());
			 item.put("class", classs);
			 item.put("sem", term);
			 item.put("year", year);
			 			 
			 jsontosql.jsontosqlaction("Delete from `feestructure` where", item, 4,"");
			  
	 			return accountsmodel.jsondata="[{\"querystatus\" : \""+accountsmodel.response+"\"}]";	 				 			

		 }else {
		 
			 jsontosql.jsontosqlaction("select '0' as querystatus,0 as checkstatus,chartofaccounts.id,Account,Amount,'' as Action from feestructure "
						+ " inner join schoolsemesters on schoolsemesters.id=feestructure.sem "
						+ " inner join studentclass       on studentclass.no=feestructure.class "
						+ " inner join chartofaccounts       on chartofaccounts.id=feestructure.particulars"
						+ " where feestructure.year='"+year+"' and studentclass.no='"+classs+"'"
						+ " and schoolsemesters.id='"+term+"'  ", null,1,"");
		 
		 	return accountsmodel.jsondata;

		 }
		 
	 		
	 }
		
	
	
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/postaddinvoiceitem")
	 @ResponseBody
	 public String addinvoiceitem(@RequestBody  JSONObject  search, HttpServletRequest request) {	
		 
		String classs	=search.get("classs").toString();
		String term	    =search.get("term").toString();
		String year	    =search.get("year").toString();
		  
		 String account	=search.get("account").toString();
		 String amount	=search.get("amount").toString();
		 
		 JSONObject item = new JSONObject();
		 item.put("particulars",account);
		 item.put("class", classs);
		 item.put("sem", term);
		 item.put("year", year);
		 item.put("amount", amount);
		 
		 jsontosql.jsontosqlaction("feestructure", item, 2,"");
		 
		 
		 jsontosql.jsontosqlaction("select '0' as querystatus,0 as checkstatus,chartofaccounts.id,Account,Amount,'' as Action from feestructure "
					+ " inner join schoolsemesters on schoolsemesters.id=feestructure.sem "
					+ " inner join studentclass       on studentclass.no=feestructure.class "
					+ " inner join chartofaccounts       on chartofaccounts.id=feestructure.particulars"
					+ " where feestructure.year='"+year+"' and studentclass.no='"+classs+"'"
					+ " and schoolsemesters.id='"+term+"'  ", null,1,"");
	
		 return accountsmodel.jsondata;

	 }
	
	
	
	
	
	
	
	@RequestMapping(value = "/postgetstudentstoinvoice")
	 @ResponseBody
	 public String getstudentstoinvoice(@RequestBody  JSONObject  search, HttpServletRequest request) {
		
		 String classs	=search.get("classs").toString();
		 
		 jsontosql.jsontosqlaction("select * from (select '0' as querystatus,1 as checkstatus,studentid,`adm no` as Adm,"
		 		    + " concat(`first name`,' ',`second name`,' ',surname) as Name "
					+ " from registration  where `current form`='"+classs+"' )j", null,1,"");
		 
		 	return accountsmodel.jsondata;
	 		
	 }
	
	
	
	@RequestMapping(value = "/poststudentstoinvoice")
	 @ResponseBody
	 public String studentstoinvoice(@RequestBody  JSONObject  search, HttpServletRequest request) {
		
		 String studnts	=search.get("studentstoinvoice").toString();		 
		 String classs	=search.get("classs").toString();
		 String term	=search.get("term").toString();
		 String year	=search.get("year").toString();
			  
				
		 List<String> crunchifyListNew=new ArrayList<String>();		
		 	crunchifyListNew = Arrays.asList(studnts.replace("[", "").replace("]", "").split("\\s*,\\s*"));	   		    	
 	    		
		 	
		 	 JSONObject item = new JSONObject();
			 item.put("date",year);
			 item.put("class", classs);
			 item.put("term", term);
			 			 
			 jsontosql.jsontosqlaction("DELETE  FROM feereceiptstatement where ", item, 4,"");
		 	
	    	   
		    		   for(String s: crunchifyListNew){ 
		    			   
		    			   
		    			   JSONObject item1 = new JSONObject();
		    				 item1.put("date",year);
		    				 item1.put("class", classs);
		    				 item1.put("term", term);
		    					  			 
		    				 jsontosql.jsontosqlaction("INSERT INTO feeReceiptStatement(`date`,`transactiondate`, `admno`,`term`,`class`,"
										+ " `transaction`,`paidmentrecno`,`amount`) "
										+ " SELECT '"+year+"',((DATE_FORMAT(NOW(),'%Y-%m-%d'))),'"+s+"',"+term+","+classs+",particulars, "
										+ "	concat('INV# ','"+s+"'),amount from feestructure   "
										+ " where sem=? AND class=? and  year=? ", item1, 5,"");
		    			 	
		    			    		  	   
				    	}
	    	
		 
	 			return accountsmodel.jsondata="[{\"querystatus\" : \""+accountsmodel.response+"\"}]";	 				 			
	 		
	 }
	
	
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/postcopyvoteheads")
	 @ResponseBody
	 public String copyvoteheads(@RequestBody  JSONObject  search, HttpServletRequest request) {
		
		 String classfrom	=search.get("classfrom").toString();
		 String classto		=search.get("classto").toString();
		 String term	=search.get("term").toString();
		 String year	=search.get("year").toString();
			  
			  
	    	  
		 JSONObject item = new JSONObject();
		 item.put("year",year);
		 item.put("class", classfrom);
		 item.put("sem", term);
		 
		 jsontosql.jsontosqlaction("INSERT INTO `feestructure`(`particulars`,`class`,`sem`,`year`,`amount`) "
					+ " select `particulars`,'"+classto+"',"+term+","+year+","
					+ " `amount` from feestructure  where year=? and class=?  and sem=? ", item, 5,"");

		 
	 	 return accountsmodel.jsondata="[{\"querystatus\" : \""+accountsmodel.response+"\"}]";	 				 			
	 		
	 }
	
	
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/postvotestoreplicate")
	 @ResponseBody
	 public String replicatevoteheads(@RequestBody  JSONObject  search, HttpServletRequest request) {
		
		 int classs			=Integer.parseInt(search.get("classs").toString());
		 int currentterm    =Integer.parseInt(search.get("term").toString());
		 int Year			=Integer.parseInt(search.get("year").toString());
		 int votecombo		=Integer.parseInt(search.get("votecombo").toString());
		 int replicateamt	=Integer.parseInt(search.get("replicateamt").toString());
				  
			
		 int previousterm = currentterm-1;
		 int previousyear = Year;


			
			if (currentterm == 1) {
				previousyear = (Year - 1);
				previousterm = 3;
			}
			
	    	
			 JSONObject item = new JSONObject();
			 item.put("class",classs);
			 item.put("term", previousterm);
			 item.put("TRANSACTION", votecombo);
			 item.put("DATE", previousyear);
			 
			 jsontosql.jsontosqlaction("INSERT INTO feeReceiptStatement(`date`,`transactiondate`, `admno`,`term`,`class`,"
						+ " `transaction`,`paidmentrecno`,`amount`,`type`,`Status`)  SELECT " + Year
						+ " ,`transactiondate`,`admno`,"+currentterm+","+classs+",`transaction`,`paidmentrecno`,"
						+ " (`amount`+"+replicateamt+"),`type`,`Status` from `feeReceiptStatement` "
						+ " WHERE class=? AND term=? AND TRANSACTION=?"
						+ " AND DATE=? AND amount>0  ", item, 5,"");

		
		 
	 			return accountsmodel.jsondata="[{\"querystatus\" : \""+accountsmodel.response+"\"}]";	 				 			
	 		
	 }
		
}
