package shule.one.filter;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import shule.one.service.JWTService;
import shule.one.service.JWTServiceImpl;


public class JWTAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

	
	private JWTService jwtService;
	
	private AuthenticationManager authenticationManager;
	
	
	public JWTAuthenticationFilter(AuthenticationManager authenticationManager, JWTService jwtService ) {
		
		this.authenticationManager = authenticationManager;
		this.jwtService = jwtService;
		
		setRequiresAuthenticationRequestMatcher(new AntPathRequestMatcher("/api/login", "POST"));
	}

	
	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
			throws AuthenticationException {
		
		String username = obtainUsername(request);
		String password = obtainPassword(request);
		
		if(username != null && password != null) {
			
			logger.info("username received from request parameter (form-data): " + username);
			
		} else {
			//the data comes in a JSON object in the body type RAW

			User usuario = null;
			
			try {
				// I convert the RAW json object to a User object
				usuario = new ObjectMapper().readValue(request.getInputStream(),User.class);
				username = usuario.getUsername();
				password = usuario.getPassword();
				
				logger.info("username received from: " + username);
				
			} catch (JsonParseException e) {
				logger.error(e);
			} catch (JsonMappingException e) {
				logger.error(e);
			} catch (IOException e) {
				logger.error(e);
			}
			
		}
		
		//Spring internal token with logged in user
		UsernamePasswordAuthenticationToken authToken =  new UsernamePasswordAuthenticationToken(username, password);
		
		
		// if the login is successful, it returns the Authentication object with the roles included
		return authenticationManager.authenticate(authToken);
	}

	
	@Override
	protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
			Authentication authResult) throws IOException, ServletException {
		
		String jwtToken = jwtService.createToken(authResult);
		
		//Pass the created JWT to the response, "Authorization" and "Bearer " are required
		response.addHeader("Authorization", JWTServiceImpl.TOKEN_PREFIX + jwtToken);
		
		//apart from the response Header I am going to send a json in the Body Http
		Map<String, Object> body = new HashMap<String, Object>();
		body.put("token", jwtToken);
		body.put("user", (User) authResult.getPrincipal());
		body.put("message", String.format("%s, you have successfully logged in", authResult.getName()));
		
		// I add the map to the response, with ObjectMapper I convert any java object to JSON
		response.getWriter().write(new ObjectMapper().writeValueAsString(body));
		
		response.setStatus(200);	// respuesta OK
		response.setContentType("application/json");
	}

	
	@Override
	protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException failed) throws IOException, ServletException {
		
		Map<String, Object> body = new HashMap<String, Object>();
		body.put("message", "Authentication Error: Incorrect username or password");
		body.put("error", failed.getMessage());
		
		//I add the map to the response, with ObjectMapper I convert any java object to JSON
		response.getWriter().write(new ObjectMapper().writeValueAsString(body));
		response.setStatus(401);	// Unauthorized response
		response.setContentType("application/json");

		
		
	}
	
	
	
	
	

}
