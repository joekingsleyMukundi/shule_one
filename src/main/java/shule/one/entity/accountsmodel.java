package shule.one.entity;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.springframework.context.annotation.Configuration;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import shule.one.DbConnector;

@Configuration
public class accountsmodel {

	
	static int countBills = 0;
	static int allBills=0;

	public static List<String> collumns = new ArrayList<>();
    public static List<accountsmodel> itemsnew =new ArrayList<accountsmodel>();
    public static List<accountsmodel> datanew =new ArrayList<accountsmodel>();
    public static List<String> json = new ArrayList<>();
    public static String jsondata="";

    public static String response = "";
    public static List<String> liststring = new ArrayList<>();
  
	
	public static String serverip = "";
	public static String passtodecrypt = "";
	public static String url = "";
	public static String pass = "";
	public static String user = "";

	
    
    
	public static String dbaction(String query, int type, int allowduplicates, int clearexistingdata,int colcase) {
				
		Connection conn=null;
		
		
		try {
			
			conn = DbConnector.dataSource.getConnection();
			
		countBills = 0;

		
			response = "";

			if (type == 1) {
			
				if (clearexistingdata == 0) {
					
					itemsnew.clear();
					datanew.clear();
					json.clear();
					jsondata="";
					
				}
				
				collumns.clear();
				allBills = 0;
				liststring.clear();
				
				
				ResultSet rs = conn.prepareStatement(query).executeQuery();

				ResultSetMetaData rsmd = rs.getMetaData();
				for (int i = 1; i <= rsmd.getColumnCount(); i++) {
					collumns.add(rsmd.getColumnName(i));	
					}
								
				
				// Now Populate Data
				
				if (!rs.isBeforeFirst() ) {  
					
					json.add("{");					
					
// convert cols to lower case  i.e if theres no results========================
					if(colcase==1) {
						for (int i = 1; i <= rsmd.getColumnCount(); i++) {										
							json.add("\""+rsmd.getColumnName(i).toLowerCase()+"\" : \"\"");
						}	
					}else {
						for (int i = 1; i <= rsmd.getColumnCount(); i++) {										
							json.add("\""+rsmd.getColumnName(i)+"\" : \"\"");
						}	
					
					}

					json.add("}");
					
				}else { 
				

// convert cols to lower case  ========================
					
					if(colcase==1) {
						
						while (rs.next()) {

							accountsmodel row = new accountsmodel(collumns);
							
							json.add("{");					
							
							for (int i = 1; i <= rsmd.getColumnCount(); i++) {
								
								if(rs.getString(rsmd.getColumnName(i)) ==null) {
									
									if(rs.getString(rsmd.getColumnName(i))!=null) {
										json.add("\""+rsmd.getColumnName(i).toLowerCase()+"\" : \""+rs.getString(rsmd.getColumnName(i)).replace("'", "")+"\"");
									}	
								}else {
									if(rs.getString(rsmd.getColumnName(i))!=null) {								
										
										if(rsmd.getColumnName(i).equals("Sn")) {
											json.add("\""+rsmd.getColumnName(i).toLowerCase()+"\" : \""+String.valueOf(rs.getInt(rsmd.getColumnName(i))).replace("'", "").replace("\\", "/")+"\"");
										}else {
											json.add("\""+rsmd.getColumnName(i).toLowerCase()+"\" : \""+rs.getString(rsmd.getColumnName(i)).replace("'", "").replace("\\", "/")+"\"");
										}
									}
									
								}

								row.setallowduplicates(allowduplicates);
								row.setadmno(rs.getString(rsmd.getColumnName(1)));
								row.setChecked(false);
								row.setActive(rsmd.getColumnName(i), rs.getString(rsmd.getColumnName(i)));
								liststring.add(rs.getString(rsmd.getColumnName(i)));
							}

							json.add("}");
							
							
							allBills++;
							itemsnew.add(row);

							datanew = FXCollections.observableArrayList();
						}	
						
						
  						
						
					}

//  no convertin cols to lower case  ========================
					
					else {
						
						
						
						while (rs.next()) {

							accountsmodel row = new accountsmodel(collumns);
							
							json.add("{");					
							
							for (int i = 1; i <= rsmd.getColumnCount(); i++) {
								
								if(rs.getString(rsmd.getColumnName(i)) ==null) {
									
									if(rs.getString(rsmd.getColumnName(i))!=null) {
										json.add("\""+rsmd.getColumnName(i)+"\" : \""+rs.getString(rsmd.getColumnName(i)).replace("'", "")+"\"");
									}	
								}else {
									if(rs.getString(rsmd.getColumnName(i))!=null) {								
										
										if(rsmd.getColumnName(i).equals("Sn")) {
											json.add("\""+rsmd.getColumnName(i)+"\" : \""+String.valueOf(rs.getInt(rsmd.getColumnName(i))).replace("'", "").replace("\\", "/")+"\"");
										}else {
											json.add("\""+rsmd.getColumnName(i)+"\" : \""+rs.getString(rsmd.getColumnName(i)).replace("'", "").replace("\\", "/")+"\"");
										}
									}
									
								}

								row.setallowduplicates(allowduplicates);
								row.setadmno(rs.getString(rsmd.getColumnName(1)));
								row.setChecked(false);
								row.setActive(rsmd.getColumnName(i), rs.getString(rsmd.getColumnName(i)));
								liststring.add(rs.getString(rsmd.getColumnName(i)));
								
							}

							json.add("}");
							
							
							allBills++;
							itemsnew.add(row);

							datanew = FXCollections.observableArrayList();
						}
						
						
						
					}
				
			}
				
				
				jsondata=json.toString().replace(", }", "}").replace("{,", "{");
				
				datanew.addAll(itemsnew);


			}

			
		

		} catch (Exception e) {
			e.printStackTrace();
			return accountsmodel.response="an error occured";
		}finally {
		    if (conn != null) try { conn.close(); } catch (SQLException ignore) {}
		}
		
		
		return jsondata;

	}

	
	
	public static String dbactionprepared(PreparedStatement query, int type, int allowduplicates, int clearexistingdata,int colcase) {
		
		Connection conn=null;
		
		
		try {
			
			conn = DbConnector.dataSource.getConnection();
			
		countBills = 0;

		
			response = "";

			if (type == 1) {
			
				if (clearexistingdata == 0) {
					
					itemsnew.clear();
					datanew.clear();
					json.clear();
					jsondata="";
					
				}
				
				collumns.clear();
				allBills = 0;
				liststring.clear();
				
				
				ResultSet rs = query.executeQuery();

				ResultSetMetaData rsmd = rs.getMetaData();
				for (int i = 1; i <= rsmd.getColumnCount(); i++) {
					collumns.add(rsmd.getColumnName(i));	
					}
								
				
				// Now Populate Data
				
				if (!rs.isBeforeFirst() ) {  
					
					json.add("{");					
					
// convert cols to lower case  i.e if theres no results========================
					if(colcase==1) {
						for (int i = 1; i <= rsmd.getColumnCount(); i++) {										
							json.add("\""+rsmd.getColumnName(i).toLowerCase()+"\" : \"\"");
						}	
					}else {
						for (int i = 1; i <= rsmd.getColumnCount(); i++) {										
							json.add("\""+rsmd.getColumnName(i)+"\" : \"\"");
						}	
					
					}

					json.add("}");
					
				}else { 
				

// convert cols to lower case  ========================
					
					if(colcase==1) {
						
						while (rs.next()) {

							accountsmodel row = new accountsmodel(collumns);
							
							json.add("{");					
							
							for (int i = 1; i <= rsmd.getColumnCount(); i++) {
								
								if(rs.getString(rsmd.getColumnName(i)) ==null) {
									
									if(rs.getString(rsmd.getColumnName(i))!=null) {
										json.add("\""+rsmd.getColumnName(i).toLowerCase()+"\" : \""+rs.getString(rsmd.getColumnName(i)).replace("'", "")+"\"");
									}	
								}else {
									if(rs.getString(rsmd.getColumnName(i))!=null) {								
										
										if(rsmd.getColumnName(i).equals("Sn")) {
											json.add("\""+rsmd.getColumnName(i).toLowerCase()+"\" : \""+String.valueOf(rs.getInt(rsmd.getColumnName(i))).replace("'", "").replace("\\", "/")+"\"");
										}else {
											json.add("\""+rsmd.getColumnName(i).toLowerCase()+"\" : \""+rs.getString(rsmd.getColumnName(i)).replace("'", "").replace("\\", "/")+"\"");
										}
									}
									
								}

								row.setallowduplicates(allowduplicates);
								row.setadmno(rs.getString(rsmd.getColumnName(1)));
								row.setChecked(false);
								row.setActive(rsmd.getColumnName(i), rs.getString(rsmd.getColumnName(i)));
								liststring.add(rs.getString(rsmd.getColumnName(i)));
							}

							json.add("}");
							
							
							allBills++;
							itemsnew.add(row);

							datanew = FXCollections.observableArrayList();
						}	
						
						
  						
						
					}

//  no convertin cols to lower case  ========================
					
					else {
						
						
						
						while (rs.next()) {

							accountsmodel row = new accountsmodel(collumns);
							
							json.add("{");					
							
							for (int i = 1; i <= rsmd.getColumnCount(); i++) {
								
								if(rs.getString(rsmd.getColumnName(i)) ==null) {
									
									if(rs.getString(rsmd.getColumnName(i))!=null) {
										json.add("\""+rsmd.getColumnName(i)+"\" : \""+rs.getString(rsmd.getColumnName(i)).replace("'", "")+"\"");
									}	
								}else {
									if(rs.getString(rsmd.getColumnName(i))!=null) {								
										
										if(rsmd.getColumnName(i).equals("Sn")) {
											json.add("\""+rsmd.getColumnName(i)+"\" : \""+String.valueOf(rs.getInt(rsmd.getColumnName(i))).replace("'", "").replace("\\", "/")+"\"");
										}else {
											json.add("\""+rsmd.getColumnName(i)+"\" : \""+rs.getString(rsmd.getColumnName(i)).replace("'", "").replace("\\", "/")+"\"");
										}
									}
									
								}

								row.setallowduplicates(allowduplicates);
								row.setadmno(rs.getString(rsmd.getColumnName(1)));
								row.setChecked(false);
								row.setActive(rsmd.getColumnName(i), rs.getString(rsmd.getColumnName(i)));
								liststring.add(rs.getString(rsmd.getColumnName(i)));
								
							}

							json.add("}");
							
							
							allBills++;
							itemsnew.add(row);

							datanew = FXCollections.observableArrayList();
						}
						
						
						
					}
				
			}
				
				
				jsondata=json.toString().replace(", }", "}").replace("{,", "{");
				
				datanew.addAll(itemsnew);


			}

			
		

		} catch (Exception e) {
			e.printStackTrace();
			return accountsmodel.response="an error occured";
		}finally {
		    if (conn != null) try { conn.close(); } catch (SQLException ignore) {}
		}
		
		
		return jsondata;

	}
	
	
	
	public static void dbactiondirectaction(String query, int type, int allowduplicates, int clearexistingdata,int colcase) {
		
		Connection conn=null;
		
		
		try {
			
			conn = DbConnector.dataSource.getConnection();
			
			response = "";

			conn.prepareStatement("start transaction").execute();
			conn.prepareStatement(query).execute();
			conn.prepareStatement("COMMIT").execute();
			
			response = "Operation successfull";			

		} catch (Exception e) {
			e.printStackTrace();
			response = ""+e.getMessage();
		}finally {
		    if (conn != null) try { conn.close(); } catch (SQLException ignore) {}
		}
		
		
	}
	
	
	
	
	
	

	private  String admno =new String();
	private BooleanProperty Checked =new SimpleBooleanProperty(false);
	private  int allowduplicates ;
	private  Map<String, StringProperty> activeByGroup = new HashMap<>();;

	
	public accountsmodel(List<String> companyGroups) {
		for (String group : companyGroups) {
			activeByGroup.put(group, new SimpleStringProperty());
		}
	}
	
	public StringProperty activeProperty(String group) {

		return activeByGroup.get(group);
	}

	
	public String isActive(String group) {
		return activeProperty(group).get();
	}

	
	public void setActive(String group, String active) {
		if (group.equals("Amount") || group.equals("Total( Local Currency )") || group.equals("Alloc to Women")
				|| group.equals("SCC Contribution") || group.equals("Alloc to Men") || group.equals("Total")
				|| group.equals("balance") || group.equals("Money out") || group.equals("Money in")) {

			if (active.equals("0")) {
				activeProperty(group).set("");

			} else {
				activeProperty(group).set(new DecimalFormat("#,###.##").format(Double.parseDouble(active)));

			}

		} else {
			activeProperty(group).set(active);

		}
	}
	
	

	public  String admnoProperty() {
		return this.admno;
	}

	public  String getadmno() {
		return admno;
	}
	public interface UrlService {
        String getApplicationUrl();
    }

	public final void setadmno(String  admno) {
		this.admno=admno;
	}

	public final int allowduplicatesProperty() {
		return this.allowduplicates;
	}

	public final int getallowduplicates() {
		return allowduplicates;
	}

	public final void setallowduplicates(final int admno) {
		this.allowduplicates=admno;
	}

	public boolean getChecked() {
		return Checked.get();
	}

	public void setChecked(boolean Checked) {
		this.Checked.set(Checked);
	}

	public BooleanProperty CheckedProperty() {
		return Checked;
	}

	public void setSelected(boolean checked) {
		this.Checked.set(checked);
	}

	@Override
	public int hashCode() {
		return Objects.hash(getadmno());
	}


	
}