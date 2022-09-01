package shule.one.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import shule.one.entity.accountsmodel;


@Controller
public class biometricsController {
	 
	 	@RequestMapping(value="/biomain", method=RequestMethod.POST)
		 public String biomain(Model model) { 
	 		
	 		return "fragments/biometrics :: biometricsdashboardmain";				      
	 	}
	 	
	 	
	 	@RequestMapping(value="/biostudents", method=RequestMethod.POST)
		 public String biostudents(Model model) {
	 		
	 		return "fragments/biometrics :: biometricslistofstudents";				      
	 	}
	 	
	 	@RequestMapping(value = "/api/bio/loadbiostudents" , method = RequestMethod.GET)
		 @ResponseBody
		 public String listofgrades() {
	 		 
	 		accountsmodel.dbaction("SELECT * from (SELECT Simage,`Adm no` AS Regno,"
	 				+ " CONCAT(`first name`,' ',`second name`,' ',surname) AS NAME, "
	 				+ " CONCAT(c.class,' ',m.stream) AS Class, "
	 				+ " ifnull(case when s.`status`=0 then DATE_FORMAT(TIME(DATE), '%r') END,'') AS tin, "
	 				+ " ifnull(case when s.`status`=1 then DATE_FORMAT(TIME(DATE), '%r') END,'') AS tout, "
	 				+ " l.reason  FROM studentsloginlogout s "
	 				+ " INNER JOIN registration r ON s.studentid=r.id  "
	 				+ " INNER JOIN studentclass c ON NO=`current form`  "
	 				+ " INNER JOIN studentstreams m ON streanid=r.stream "
	 				+ " INNER JOIN leaveout_reason l ON l.id=s.reason_id "
	 				+ " WHERE DATE(DATE)=date(NOW()) AND r.school="+LoginController.getschoo()+")d ",1, 0, 0,0);
		
		     return accountsmodel.jsondata;
		 }
	 	
}
