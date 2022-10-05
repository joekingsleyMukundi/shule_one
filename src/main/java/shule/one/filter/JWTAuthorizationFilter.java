package shule.one.filter;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import shule.one.service.JWTService;
import shule.one.service.JWTServiceImpl;
import shule.one.service.JpaUserDetailsService;


public class JWTAuthorizationFilter extends BasicAuthenticationFilter {

	JWTService jwtService;
	
	@Autowired
	private JwtUtils jwtUtils;
	
	@Autowired
	private JpaUserDetailsService userDetailsService;
	
	public JWTAuthorizationFilter(AuthenticationManager authenticationManager, JWTService jwtService, JwtUtils jwtUtils, JpaUserDetailsService userDetailsService ) {
		super(authenticationManager);
		this.jwtService = jwtService;
		this.jwtUtils=jwtUtils;
		this.userDetailsService= userDetailsService;

	}

	
	
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
			throws IOException, ServletException {

		String headerJwt = request.getHeader("Authorization");
		String username = null;
		String jwt = null;
		if (headerJwt != null && headerJwt.startsWith("Bearer ")) {
          jwt = headerJwt.substring(7);
          System.out.println(jwt);
          username = jwtUtils.extractUsername(jwt);
          System.out.println(username);
      }
		
		UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = null;
      if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {

    	  UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);
    	  
          if (jwtUtils.validateToken(jwt, userDetails)) {

              usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
            		  userDetails, null, userDetails.getAuthorities());
              usernamePasswordAuthenticationToken
                      .setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
              SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
             
          }
          
          
      }
      
      chain.doFilter(request, response);
     
	}
}




//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
//import org.springframework.stereotype.Component;
//import org.springframework.web.filter.OncePerRequestFilter;
//
//import javax.servlet.FilterChain;
//import javax.servlet.ServletException;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import java.io.IOException;
//
//@Component
//public class JWTAuthorizationFilter extends OncePerRequestFilter {
//
//    @Autowired
//    private MyUserDetailsService userDetailsService;
//
//    @Autowired
//    private JwtUtils jwtUtil;
//
//    @Override
//    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
//            throws ServletException, IOException {
//
//        final String authorizationHeader = request.getHeader("Authorization");
//
//        String username = null;
//        String jwt = null;
//
//        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
//            jwt = authorizationHeader.substring(7);
//            username = jwtUtil.extractUsername(jwt);
//        }
//
//
//        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
//
//            UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);
//
//            if (jwtUtil.validateToken(jwt, userDetails)) {
//
//                UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
//                        userDetails, null, userDetails.getAuthorities());
//                usernamePasswordAuthenticationToken
//                        .setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
//                SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
//            }
//        }
//        chain.doFilter(request, response);
//    }
//
