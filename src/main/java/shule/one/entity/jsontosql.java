package shule.one.entity;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import org.json.simple.JSONObject;
import org.springframework.context.annotation.Configuration;

import shule.one.DbConnector;
import shule.one.controller.LoginController;

@Configuration
public class jsontosql {	

	

	
	public static String jsontosqlaction(String table,JSONObject object, int type,String extratext) {
		
		Connection conn=null;
		try {
			conn = DbConnector.dataSource.getConnection();
			
		String classtrfor="";
		List<String> crunchifyListNew=new ArrayList<String>();	
		
		
		if (type == 1) {
		
			
			 			ArrayList<String> paramList = new ArrayList<String>();
			
			 			for(Iterator<?> iterator = object.keySet().iterator(); iterator.hasNext();) {					
							String key = (String) iterator.next();				    
						    
							if (!key.isEmpty() && !key.equals("action")) {
								table += " AND "+key+"=?";
				 			    paramList.add(object.get(key).toString().replace("'", ""));
				 			}
						    			    
						}
			 			
			 			
			 			if(!extratext.isEmpty()) {
							 table +=" "+extratext;
			 			}
			 			
			 			
//			 			if(limit!=0) {
//						 table += " limit "+limit;
//			 			}
			 			
			 			
			 			table=table.replace("where  AND", "WHERE ").replace("where AND", "WHERE ");			 			
			 			
			 			PreparedStatement ps = conn.prepareStatement(table);
			 		    int index = 1;
			 		    for (Object param : paramList) {
			 		    	ps.setObject(index, param);
			 		        index++;
			 		    }
			 		    
			 		    accountsmodel.dbactionprepared(ps,1, 0, 0,1);	
			 		   
			 		    
				
			
		}
		
		
		
		
		else if (type == 2) {
			
				List<String> cols = new ArrayList<String>();
				List<String> valuesquestionmrks = new ArrayList<String>();
				List<String> values = new ArrayList<String>();
				
				cols.add("Insert into "+table+" (");

				
				for(Iterator<?> iterator = object.keySet().iterator(); iterator.hasNext();) {					
					String key = (String) iterator.next();				    
				    
				    if(key.equals("classteacherfor")) {
				    	
				    	if(!object.get("classteacherfor").equals(""))  {				    		
				    		classtrfor=object.get(key).toString().replace("'", "");							
				    	}
				    	
				    }else if(key.equals("substought")) {			        						    
				    	crunchifyListNew = Arrays.asList(object.get("substought").toString().replace("[", "").replace("]", "").split("\\s*,\\s*"));
				    
				    }else {
				    	
				    	if (!key.equals("id") && !key.equals("action") && !key.equals("substought") && !key.equals("classteacherfor")){
				    		cols.add("`"+key+"`");				    	
				    		valuesquestionmrks.add("?");
						    values.add(object.get(key).toString().replace("'", ""));
				    	}
				    	
				    }
				    			    
				}
				
				
				
				cols.add(")");
				
				
				String query=cols.toString().replace("(, ", "(").replace(", )]", ")").replace("[", "")+"  VALUES( "+
						valuesquestionmrks.toString().replace("(, ", "(").replace("[", "").replace("]", ")").replace("?, ))", "? )");
				
				
				mapParams(conn.prepareStatement(query),values);
				if(accountsmodel.response.equals("an error occured")) {
					  return accountsmodel.jsondata="[{\"querystatus\" : \""+accountsmodel.response+"\"}]";				 		
		 		}
				
				if(!classtrfor.equals("")) {
					
					if(classtrfor.contains("::")) {
						accountsmodel.dbactiondirectaction("INSERT INTO classteachers(school,Teacher,class,stream) "
								+ " values ((select "+LoginController.getschoo()+",max(id) from teacherregistration),"
								+ " '"+classtrfor.split("::")[0]+"',"
								+ " '"+classtrfor.split("::")[1]+"')",2,0,0,0);	
								
					}else {
						accountsmodel.dbactiondirectaction("INSERT INTO classteachers(school,Teacher,class,stream) "
								+ " values ((select "+LoginController.getschoo()+",max(id) "
								+ " from teacherregistration),'"+classtrfor.split("::")[0]+"',"
								+ " '"+classtrfor+"')",2,0,0,0);	
							
					}
					
					
					
					
					
				}
				
				
				if(crunchifyListNew.toString().contains(":a:")) {								
					for(String s: crunchifyListNew){						
						accountsmodel.dbactiondirectaction("INSERT INTO teaxchersubtought(school,tscnu,class,stream,sub) "
								+ " values ((select "+LoginController.getschoo()+",max(id) from "
								+ " teacherregistration),'"+s.split(":a:")[0]+"',"
								+ " '"+(s.split(":a:")[1]).split(":b:")[0]+"','"+s.split(":b:")[1]+"')",2,0,0,0);						
					}
				
				}
				
				
				
				
			}
			
			
			
	//  for update
			
		else if (type == 3) {
				
				
				List<String> cols = new ArrayList<String>();
				List<String> values = new ArrayList<String>();
				cols.add("UPDATE "+table+" SET ");				
				
				List<String> colsupdate = new ArrayList<String>();
				
				
				for(Iterator<?> iterator = object.keySet().iterator(); iterator.hasNext();) {
				    String key = (String) iterator.next();
				    
				    colsupdate.add(key);
				    
				        if(key.equals("classteacherfor")) {				        	
				        	
				        	if(!object.get("classteacherfor").equals(""))  {				    		
					    		classtrfor=object.get(key).toString().replace("'", "");							
					    	}
				        	
							if(object.get("classteacherfor").equals("")) {								
								accountsmodel.dbactiondirectaction("delete from  classteachers  where teacher='"+object.get("id")+"' ",2,0,0,0);								
							}else {								
								
								accountsmodel.dbactiondirectaction("delete from  classteachers  where teacher='"+object.get("id")+"' ",2,0,0,0);								
								
								if(classtrfor.contains("::")) {
									
									classtrfor=object.get("classteacherfor").toString().replace("'", "");								
									accountsmodel.dbactiondirectaction("insert into  classteachers(school,teacher,class,stream)"
										+ " values("+LoginController.getschoo()+",'"+object.get("id")+"',"
										+ " '"+classtrfor.split("::")[0]+"',"
										+ " '"+classtrfor.split("::")[1]+"' )",2,0,0,0);
								
								}else {
									
									classtrfor=object.get("classteacherfor").toString().replace("'", "");								
									accountsmodel.dbactiondirectaction("insert into  classteachers(school,teacher,class,stream)"
										+ " values("+LoginController.getschoo()+",'"+object.get("id")+"',"
										+ " '"+classtrfor.split("::")[0]+"',"
										+ " '"+classtrfor+"' )",2,0,0,0);
									
								}
									
									
									
							}							
							
				        }
				        
				        else if(key.equals("substought")) {				        	
					        
							crunchifyListNew = Arrays.asList(object.get("substought").toString().replace("[", "").replace("]", "").split("\\s*,\\s*"));								
							
							
							if(crunchifyListNew.toString().contains(":a:")) {														
								
								accountsmodel.dbactiondirectaction("delete from  teaxchersubtought  where tscnu='"+object.get("id")+"' ",2,0,0,0);														
								
								for(String s: crunchifyListNew){
										
									accountsmodel.dbactiondirectaction("INSERT INTO teaxchersubtought(school,tscnu,class,stream,sub) "
											+ " values ("+LoginController.getschoo()+",'"+object.get("id")+"','"+s.split(":a:")[0]+"',"
											+ " '"+(s.split(":a:")[1]).split(":b:")[0]+"','"+s.split("b:")[1]+"')",2,0,0,0);													
								}
								
								
								
								accountsmodel.dbactiondirectaction("update users set trnu='"+object.get("id")+"'"
										+ " where username='"+object.get("email")+"' ",2,0,0,0);						
					
								
								
							}else {									
								accountsmodel.dbactiondirectaction("delete from  teaxchersubtought  where tscnu='"+object.get("id")+"' ",2,0,0,0);
							}
											        
				        
				        }else { 
				        		
				        	if(!key.equals("id") && !key.equals("action") && !key.equals("substought") && !key.equals("classteacherfor")) {
				        		cols.add("`"+key+"`=?");
							    values.add(object.get(key).toString().replace("'", ""));
				        	}
				        	
				        }
				    
				}
				
				String idcol=colsupdate.get(0);
				String query="";
				if(object.containsKey("subid")) {
					query=cols.toString().replace("SET ,", "SET").replace("]", "").replace("[", "")+"  "
							+ " where `"+idcol+"`= "+object.get(idcol).toString().replace("'", "");	
				}else {
					query=cols.toString().replace("SET ,", "SET").replace("]", "").replace("[", "")+"  "
							+ " where `id`= "+object.get("id").toString().replace("'", "");
				}
						
				mapParams(conn.prepareStatement(query),values);
				if(accountsmodel.response.equals("an error occured")) {
					  return accountsmodel.jsondata="[{\"querystatus\" : \""+accountsmodel.response+"\"}]";				 		
		 		}
				
			}
		
		
		else if (type == 4) {


			 			ArrayList<String> paramList = new ArrayList<String>();
			
			 			for(Iterator<?> iterator = object.keySet().iterator(); iterator.hasNext();) {					
							String key = (String) iterator.next();				    
						    
							if (!key.isEmpty() && !key.equals("action")) {
								table += " AND "+key+"=?";
				 			    paramList.add(object.get(key).toString().replace("'", ""));
				 			}
						    			    
						}			 			
			 			
			 			table=table.replace("where  AND", "WHERE ");
			 			
			 		   mapParams(conn.prepareStatement(table),paramList);
				 		  if(accountsmodel.response.equals("an error occured")) {
							  return accountsmodel.jsondata="[{\"querystatus\" : \""+accountsmodel.response+"\"}]";				 		
				 		  }
			 		   
			
		}

		  
		else if (type == 5) {

 			ArrayList<String> paramList = new ArrayList<String>();

 			for(Iterator<?> iterator = object.keySet().iterator(); iterator.hasNext();) {					
				String key = (String) iterator.next();				    
			    
				if (!key.isEmpty() && !key.equals("action")) {
					table += " AND "+key+"=?";
	 			    paramList.add(object.get(key).toString().replace("'", ""));
	 			}
			    			    
			}	
			
			mapParams(conn.prepareStatement(table),paramList);
			if(accountsmodel.response.equals("an error occured")) {
				  return accountsmodel.jsondata="[{\"querystatus\" : \""+accountsmodel.response+"\"}]";				 		
	 		}
 		   

		}
		
		else if (type == 6) {
			accountsmodel.dbactiondirectaction(table,2,0,0,0);						
		}
		
			accountsmodel.response = "Operation successfull";			


		} catch (Exception e) {
			e.printStackTrace();
			accountsmodel.response="an error occured";
	    		
		}finally {
		    if (conn != null) try { conn.close(); } catch (SQLException ignore) {}
		}
		
		return accountsmodel.response;
    	
	}

	
	
	
	
	
	
	
	List<String> cols = new ArrayList<String>();
	
	public static String mapParams(PreparedStatement ps, List cols){
		
		int i = 1;
	    
Connection conn=null;
		
		
		try {
			
			conn = DbConnector.dataSource.getConnection();
			 
							conn.prepareStatement("start transaction").execute();
			
			    for (Object arg : cols) {         
			         	if (arg instanceof Date) {
			         		ps.setTimestamp(i++, new Timestamp(((Date) arg).getTime()));
					    } else if (arg instanceof Integer) {
					        ps.setInt(i++, (Integer) arg);
					    } else if (arg instanceof Long) {
					        ps.setLong(i++, (Long) arg);
					    } else if (arg instanceof Double) {
					        ps.setDouble(i++, (Double) arg);
					    } else if (arg instanceof Float) {
					        ps.setFloat(i++, (Float) arg);
					    } else {
					        ps.setString(i++, (String) arg);
					    }
			   }	    
	    
			    
			    
			    System.out.println(ps);
			    
			   
			    			ps.execute();
	    
			    			
			    			
			    			conn.prepareStatement("COMMIT").execute();
			    			
			    			accountsmodel.response = "Operation successfull";
			    			


			} catch (Exception e) {
				e.printStackTrace();
				accountsmodel.response = "an error occured";
			}
		
			
		

		return accountsmodel.response;
	  }
	
	
	
	
	
	
	
	
	
	
	
	
}