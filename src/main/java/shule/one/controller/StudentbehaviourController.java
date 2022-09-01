package shule.one.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.json.simple.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import shule.one.entity.accountsmodel;
import shule.one.entity.jsontosql;



@Controller
public class StudentbehaviourController {

	@GetMapping("/leaveouts")
	public String gotoregistrationleaveouts(Model model) {	 
	   
    	jsontosql.jsontosqlaction("SELECT  * FROM (select no, class from studentclass   where class!='Transfered' "
				+ " AND class!='Allumni' ORDER BY NO)j"
				+ " union all "
				+ " SELECT * FROM (select CONCAT(NO,':a:',streanid) AS no,CONCAT(class,' ',stream) AS class "
				+ " from studentclass  "
				+ " cross join studentstreams where class!='Transfered' AND class!='Allumni' ORDER BY NO,"
				+ " streanid)d "
				+ " union all "
				+ " SELECT 'All','All classes'", null, 1 ,"");
		List<accountsmodel> newListofclasses = new ArrayList<accountsmodel>(accountsmodel.datanew);		
		model.addAttribute("classesreports", newListofclasses);
		
		jsontosql.jsontosqlaction("SELECT * FROM hostel", null, 1 ,"");
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
		
    	
		jsontosql.jsontosqlaction("select * from studentclass where class!='Transfered' AND class!='Allumni'", null, 1 ,"");
		List<accountsmodel> newList = new ArrayList<accountsmodel>(accountsmodel.datanew);		
		model.addAttribute("classes", newList);      	
		
		jsontosql.jsontosqlaction(" SELECT '0' as querystatus,0 as checkstatus,b.* from (SELECT id,`admno`, \n"
				+ "	   	CONCAT(`first name`,' ',`SECOND NAME`,' ',surname) AS Name,time_out, \n"
				+ "	   	`time_back`, issued_by, status,issued_at,  '' AS action from leaveout\n"
				+ "		INNER JOIN registration ON `adm no`=admno )b  order by id ", null, 1 ,"");
   		
			return "register/leaveouts";
		
	    }
    
 

 @RequestMapping(value = "/loadregistrationleaveouts" , method = RequestMethod.GET)
 @ResponseBody
 public String loadleaveoutsdata() {
	 
	      
     return accountsmodel.jsondata;
 }	 

 
 
    @PostMapping(value = "/postleaveoutsdataforreg")
 	@ResponseBody
 	public  String  registerupdateanddeleteleaveouts(@RequestBody  JSONObject  search, HttpServletRequest request) {		    
 		
 		String action=search.get("action").toString();
 		
 		return accountsmodel.jsondata;
 		
 		}
	 	

}
