package shule.one.controller;


import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.http.HttpServletRequest;

import org.json.simple.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import shule.one.DbConnector;
import shule.one.entity.accountsmodel;
import shule.one.entity.jsontosql;



@Controller
public class FeePaymentController {	
	

	 @RequestMapping(value = "/loadlistofstudents" , method = RequestMethod.GET)
	 @ResponseBody
	 public String loadlistofstudents() {	
		 
		 jsontosql.jsontosqlaction("select concat(studentid,'  ',`adm no`,' ',`first name`,' ',`second name`,' ',`surname`) Name"
					+ " from `registration`  ", null,1,"");
		
	     return accountsmodel.jsondata;
	 }
	
	 int studentid=0;
	 int Year=0;
	 int currentterm=0;
	 int cls=0;
	 String dataidentify="";
	 
	 @RequestMapping(value = "/fetchstudentfeebalance")
	 @ResponseBody
	 public String loaddata(@RequestBody  JSONObject  search, HttpServletRequest request) {
		
		  studentid=Integer.parseInt(search.get("studentid").toString());		 
				
		  jsontosql.jsontosqlaction(" SELECT * from (SELECT studentid,DATE,class,term FROM feereceiptstatement "
		 			+ " inner join registration on studentid=admno  WHERE studentid='"+studentid+"' "
		 			+ " ORDER BY DATE,class,term DESC LIMIT 1)a "
		 			
		 			+ " INNER JOIN "
		 			
		 			+ " (SELECT studentid,`adm no`,concat(`first name`,' ',`second name`,' ',surname) name,"
		 			+ " concat(class,' ',s.stream) AS cls,simage,(SELECT concat('FP',reference+1)  FROM feereference)"
		 			+ " reference FROM `registration` r "
		 			+ " inner join studentclass ON  `current form`=no "
		 			+ " inner join studentstreams s ON  streanid=r.stream "
		 			+ " where `studentid` = '"+studentid+"')b ON a.studentid=b.studentid", null,1,"");
		  		 
		 
		 accountsmodel.datanew.forEach((tab) -> {
			 
			 Year=Integer.parseInt(tab.activeProperty(accountsmodel.collumns.get(1)).getValue());
			 cls=Integer.parseInt(tab.activeProperty(accountsmodel.collumns.get(2)).getValue());
			 currentterm=Integer.parseInt(tab.activeProperty(accountsmodel.collumns.get(3)).getValue());
			 
		 });	
		 
		 
		 
		 jsontosql.jsontosqlaction("select *,"
		 			+ " (select requestdateandtransnu 	from schooldetails) as requestdateandtransnu,"
		 			+ " (select allowbackdating 		from schooldetails) as allowbackdating,"
		 			+ " (select includebalinrct 		from schooldetails) as includebalinrct,"
		 			+ " (select typeofreceipt 			from schooldetails) as typeofreceipt "
		 			
		 			+ " from (SELECT studentid,`adm no`,concat(`first name`,' ',`second name`,' ',surname) name,"
		 			+ " concat(class,' ',s.stream) AS cls,simage,(SELECT concat('FP',reference+1)  FROM feereference)"
		 			+ " reference FROM `registration` r "
		 			+ " inner join studentclass ON  `current form`=no "
		 			+ " inner join studentstreams s ON  streanid=r.stream "
		 			+ " where `studentid` = '"+studentid+"')a "
		 			
		 			+ " INNER JOIN "
		 			
		 			+ " (SELECT "+studentid+" as studentid,admno, "				  
		 			+ " sum( if( ((class<" +cls + ") OR (class=" + cls + "   AND term <" + currentterm + ") OR (class<="+cls+"  "
				  + " and DATE<"+Year+") ) ,(invos-pays), 0 ) ) AS `BF`,   " + 
		            " sum( if( (class=" + cls + " AND term =" + currentterm + ")  and status='Approved' "+
		            " and docnu=concat('"+studentid+"','-','"+currentterm+"','-','"+Year+"') , (invos), 0 ) ) AS `TermFees`,  " + 
		            " ( sum( if( ((class<" + cls + ") OR (class=" + cls + "  AND term <" + currentterm + ")  "
		          + " OR (class<="+cls+"  and DATE<"+Year+") ),(invos-pays), 0 ) )+   " + 
		            " sum( if( (class=" + cls + " AND term =" + currentterm + ")  and status='Approved' "+
		            " and docnu=concat('"+studentid+"','-','"+currentterm+"','-','"+Year+"') , (invos), 0 ) )) AS 'Payable',  " + 
		            " sum( if( (class=" + cls + " AND term =" + currentterm + ")  and status='Approved' "
		          + " and docnu=concat('"+studentid+"','-','"+currentterm+"','-','"+Year+"') , (pays), 0 ) ) AS `PAID`,  " + 
		            " ((sum( if( ((class<" + cls + ") OR (class=" + cls + " AND term <" + currentterm + ")  "
		          + " OR (class<="+cls+"  and DATE<"+Year+") ) ,(invos-pays), 0 ) )+   " + 
		            " sum( if( (class=" + cls + " AND term =" + currentterm + ")  and status='Approved' "+
		            " and docnu=concat('"+studentid+"','-','"+currentterm+"','-','"+Year+"') , (invos), 0 ) ))-  " + 
		            " sum( if( (class=" + cls + " AND term =" + currentterm + ")  and status='Approved' "+
		            " and docnu=concat('"+studentid+"','-','"+currentterm+"','-','"+Year+"') , (pays), 0 ) )) AS BAL  " + 
		            " FROM (SELECT admno,date,status,docnu,class,term,  " + 
		            " sum( if( TYPE='INVOICES'   and status='Approved' ,(amount), 0 ) ) AS invos ,  " + 
		            " sum( if( TYPE='PAYMENTS'   and status='Approved' ,(amount), 0 ) ) AS pays  " + 
		            " FROM (SELECT admno,date,status,CONCAT(admno,'-',term,'-',DATE) docnu,f.class,term,type,  " + 
		            " SUM(amount) as amount FROM feereceiptstatement f  " + 
		            " inner join registration ON studentid=admno   " + 
		            " inner join studentclass s ON `current form`=no WHERE studentid='"+studentid+"' " + 
		            " GROUP BY admno,class,CONCAT(admno,'-',term,'-',DATE)  " + 
		            
		            " UNION ALL " + 
		            
		            " SELECT individual,date,status,docnumber,'' AS class ,'' AS term,'PAYMENTS' AS type,"
		          + " (sum( if( description = 'FEE PAYMENT'  AND STATUS='Approved' ,(amount), 0 ))-" + 
		            " sum( if( type = 'EXPENSE',(amount), 0 ))) as pays FROM accounttransactions   " + 
		            " inner join registration ON `adm no`=individual   " + 
		            " inner join studentclass s ON `current form`=no WHERE studentid='"+studentid+"' " + 
		            " GROUP BY docnumber,reference)s  " + 
		            " GROUP BY admno,docnu)a  " + 
		            " GROUP BY admno)b ON a.studentid=b.studentid ", null,1,"");
		 
		 
	     return accountsmodel.jsondata;
		 
	 }
	 
	 
	 
	 
	 @RequestMapping(value = "/processandloadstudentreceipt")
	 @ResponseBody
	 public String makefeepayment(@RequestBody  JSONObject  search, HttpServletRequest request) {
		 
		 Connection conn=null;
			try {
				conn = DbConnector.dataSource.getConnection();
				 
		 
		 studentid=Integer.parseInt(search.get("studentid").toString());
		 
		 jsontosql.jsontosqlaction(" SELECT * from (SELECT DATE,class,term FROM feereceiptstatement "
		 			+ " inner join registration on studentid=admno  WHERE studentid='"+studentid+"' "
		 			+ " ORDER BY DATE,class,term DESC LIMIT 1)a ", null,1,"");
		 
		 accountsmodel.datanew.forEach((tab) -> {			 
			 Year=Integer.parseInt(tab.activeProperty(accountsmodel.collumns.get(1)).getValue());
			 cls=Integer.parseInt(tab.activeProperty(accountsmodel.collumns.get(2)).getValue());
			 currentterm=Integer.parseInt(tab.activeProperty(accountsmodel.collumns.get(3)).getValue());			 
		 });
		 
		 double balltext=Double.parseDouble(search.get("amount").toString());
		 String date=search.get("date").toString();
		 int paymentmethod=Integer.parseInt(search.get("paymentmethod").toString());
		 String depodate=search.get("depodate").toString();
		 String transnu=search.get("transnu").toString();
		 double totalbal=Double.parseDouble(search.get("totalbal").toString());
		 double excesspayment=0;
	     dataidentify = studentid+ "-"+currentterm+"-"+Year;

			
			if (balltext > 0) {				
				
				 excesspayment=0;
		        	  
				if(totalbal>=0){
		          excesspayment=balltext-totalbal;
				}
		          
		          if(excesspayment > 0) {
		        	  balltext=balltext-excesspayment;
		          }
		          
		            if (balltext > 0) {
		            	
		            	 ResultSet kl = conn.prepareStatement("SELECT transaction,case when transaction=firstrow then (case when ("+balltext+"-totalpaid)>0 then "
		            			+" paid+("+balltext+"-totalpaid) ELSE paid-(totalpaid-"+balltext+") END )  else paid end as paid FROM "
		            			+" (SELECT *,(SELECT SUM(paid) FROM (SELECT ROUND("+balltext+"*((amount/total))) AS paid "
		            			+" FROM (SELECT  *,(SELECT SUM(amount) FROM feereceiptstatement WHERE admno='"+studentid+"' "
		            			+" AND term='"+currentterm+"' AND date='"+Year+"') AS total  "
		            			+" FROM feereceiptstatement WHERE admno='"+studentid+"' AND term='"+currentterm+"'  "
		            			+" AND date='"+Year+"')h)f) AS totalpaid,"
		            			+" (SELECT transaction FROM feereceiptstatement WHERE admno='"+studentid+"' "
		            			+" AND term='"+currentterm+"' AND date='"+Year+"' LIMIT 1) AS firstrow "
		            			+" FROM (SELECT transaction,ROUND("+balltext+"*((amount/total))) AS paid "
		            			+" FROM (SELECT  *,(SELECT SUM(amount) FROM feereceiptstatement WHERE admno='"+studentid+"' "
		            			+" AND term='"+currentterm+"' AND date='"+Year+"' AND amount>0) AS total  "
		            			+" FROM feereceiptstatement WHERE admno='"+studentid+"' AND term='"+currentterm+"' "
		            			+" AND date='"+Year+"' AND amount>0)h)d)s").executeQuery();
				          
		                     
				          while (kl.next()) {
				        	  
				        	  conn.prepareStatement("INSERT INTO `accounttransactions`(`date`,`reference`,`docnumber`,"
										+ "`individual`,`bank`,`account`, `type`, `description`,"
										+ " `amount`, `receivedby`,`Transactionnu`,`depodate`)"
										+ " select '"+date+"',reference,'"+dataidentify+"',"
										+ " '"+studentid+ "','"+paymentmethod+"','"+kl.getString("transaction")+ "',"
										+ " 'INCOME','FEE PAYMENT','"+ kl.getString("paid")+ "',"
										+ " '"+IndexController.user+ "','"+transnu+"','"+depodate+"' from feereference ").execute();								          
				        	  } 
		            	
		            } 
		          
				}
		          
		          if (excesspayment > 0) {
		        	  
		        	  conn.prepareStatement("INSERT INTO accounttransactions(`date`,`reference`,`docnumber`,"
										+ " `individual`,`bank`,`account`, `type`, `description`, `amount`,"
										+ " `receivedby`,`Transactionnu` ,`depodate`)"
										+ " select '"+date+"',reference,'"+dataidentify+"',"
										+ " '"+studentid+"',"
										+ " '"+paymentmethod+"','2',"
										+ " 'INCOME','FEE PAYMENT',"+ " '"+excesspayment+ "','"+IndexController.user+"',"
										+ " '"+transnu+"','"+depodate+"'  from feereference ").execute();
		        } 
			
		


		ResultSet TY = conn.prepareStatement("SELECT `pledge amount`,paid,`pledge amount`-`paid` AS bal ,`adm no` FROM  `pledgelist` p "
						+ " inner join registration r on p.`adm no`=r.`adm no` WHERE `studentid`='"+ studentid+ "' "
						+ " AND `pledge amount`-`paid`>0 ").executeQuery();

		while (TY.next()) {
			
			if (TY.getDouble("bal") > totalbal) {

				conn.prepareStatement("UPDATE `pledgelist` SET `paid`='"
								+ totalbal + "' "
								+ " WHERE `adm no`='"
								+ studentid
								+ "' ").execute();
				totalbal = 0;

			} else {
				
				conn.prepareStatement(
						"UPDATE `pledgelist` SET `paid`='"
								+ TY.getDouble("bal") + "' "
								+ " WHERE `adm no`='"
								+ studentid
								+ "'").execute();
				totalbal = totalbal	- TY.getDouble("bal");

			}

		}

		
		
		
		conn.prepareStatement(" UPDATE `feereference` SET `reference`=reference+1 ;"
		+ " INSERT INTO `logs`(`date`,`user`,`Terminal`,`Action`)"
		+ " VALUES ('"+(java.time.LocalDate.now()+ " "+ java.time.LocalTime.now().getHour()
		+ " :"+ java.time.LocalTime.now().getMinute() + ":" + java.time.LocalTime.now().getSecond()) + "',"
		+ " '" + IndexController.user + "','"+IndexController.hostname + "','Paid fees for  "+studentid+"')").execute();

		 } catch (Exception e) {
				e.printStackTrace();
		 }finally {
			    if (conn != null) try { conn.close(); } catch (SQLException ignore) {}
			}
		 
		return dataidentify;
		
	 }
	 
	 
}
