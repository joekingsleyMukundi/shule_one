package shule.one;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.mchange.v2.c3p0.ComboPooledDataSource;

import shule.one.entity.accountsmodel;

@SpringBootApplication
public class Main  {
	
	public static void main(String[] args) {
		SpringApplication.run(Main.class, args);
		DbConnector.dbconnect();
 		
	}

}
