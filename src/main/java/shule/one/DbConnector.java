package shule.one;

import java.beans.PropertyVetoException;
import java.sql.Connection;
import java.sql.SQLException;

import com.mchange.v2.c3p0.ComboPooledDataSource;



public class DbConnector {
	
	public static ComboPooledDataSource dataSource = new ComboPooledDataSource();
	
	public static ComboPooledDataSource dbconnect(){
		
		try {
			
			dataSource.setDriverClass("com.mysql.jdbc.Driver");
			dataSource.setJdbcUrl("jdbc:mysql://127.0.0.1:3306/sec?allowMultiQueries=true&useUnicode=true&characterEncoding=UTF-8&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC");
			dataSource.setUser("root");
			dataSource.setPassword("");
			
		} catch (PropertyVetoException e) {
			e.printStackTrace();
		}
		
		return dataSource;
	}
	
	

}
