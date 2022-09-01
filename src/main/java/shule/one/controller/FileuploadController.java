package shule.one.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.multipart.MultipartFile;

import shule.one.entity.accountsmodel;
import shule.one.entity.jsontosql;

@Controller
public class FileuploadController {
	
	
	  @PostMapping("/upload") 
	  @ResponseBody
	  public String handleFileUpload( @RequestParam("pictureFile") MultipartFile file,@RequestParam("morepath") String morepath ) {

		jsontosql.jsontosqlaction("SELECT *  from (SELECT s.id,schoolname,regname,uploadpath,"
				+ " logo,address,city,phone FROM users u "
		 		+ " inner join schooldetails s on u.school=s.id "
		 		+ " where sessionid='"+RequestContextHolder.currentRequestAttributes().getSessionId()+"')d  ", null, 1 ,"");
		
		List<accountsmodel> newList = new ArrayList<accountsmodel>(accountsmodel.datanew);	
	    
		String fileName = file.getOriginalFilename();
	    
	    String uploadDir = "static/"+newList.get(0).activeProperty("uploadpath").getValue()+"/"+morepath;
	    
        
	    try {
	    	
	    	FileUploadUtil.saveFile(uploadDir, fileName, file);
	    	
	    } catch (Exception e) {
	    	e.printStackTrace();
	    	return "{\"querystatus\" : \""+e+"\"}";
	    } 
	   
	    return "{\"querystatus\" : \"Image uploaded successfully\",\"path\" : \""+uploadDir+"/"+fileName+"\"}";
	  }


	}
