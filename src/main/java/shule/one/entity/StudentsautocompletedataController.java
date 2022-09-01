package shule.one.entity;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class StudentsautocompletedataController {
	
	  
	  @PostMapping("/getstudentautocompletedata") 
	  @ResponseBody
	  public String getstudentautocompletedataf() {
  
		  jsontosql.jsontosqlaction("select concat(`adm no`,' ',`first name`,' ',`second name`,' ',`surname`) as adm from `registration` "
				  + " inner join studentclass on  no = `CURRENT FORM` where class!='Transfered' AND class!='Allumni' ", null, 1 ,"");
		
		  return accountsmodel.jsondata;		
		 	
	  }

	  
	  
	  
	}
