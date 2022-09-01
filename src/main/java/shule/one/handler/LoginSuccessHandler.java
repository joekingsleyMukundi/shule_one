package shule.one.handler;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;

import shule.one.DbConnector;
import shule.one.entity.accountsmodel;

@Component
public class LoginSuccessHandler implements AuthenticationSuccessHandler {
	
	private RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();

	@Override
	public void onAuthenticationSuccess(HttpServletRequest arg0, HttpServletResponse arg1, Authentication authentication)
			throws IOException, ServletException {
			
		Connection conn=null;
			try {
				conn = DbConnector.dataSource.getConnection();
			
			conn.prepareStatement("update users set sessionid='"+RequestContextHolder.currentRequestAttributes().getSessionId()+"'"
					+ " where username='"+authentication.getName()+"'").execute();
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
		    if (conn != null) try { conn.close(); } catch (SQLException ignore) {}
		  }	

		
		redirectStrategy.sendRedirect(arg0, arg1, "/home");

		
	}
 
}
