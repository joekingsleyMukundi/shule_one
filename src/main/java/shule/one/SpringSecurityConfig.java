package shule.one;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import shule.one.filter.JWTAuthenticationFilter;
import shule.one.filter.JWTAuthorizationFilter;
import shule.one.handler.LoginSuccessHandler;
import shule.one.service.JWTService;
import shule.one.service.JpaUserDetailsService;


@EnableGlobalMethodSecurity(securedEnabled = true)
@Configuration
public class SpringSecurityConfig extends WebSecurityConfigurerAdapter  {
	
	@Bean
	public BCryptPasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	
	
	@Autowired
	JWTService jwtService;
	
	@Autowired
	JpaUserDetailsService jpaUserDetailsService;
	
	@Autowired
	LoginSuccessHandler successHandler;
	
	
	
	@Override
	@Bean
	public AuthenticationManager authenticationManagerBean() throws Exception {
	    return super.authenticationManagerBean();
	}
	
	
	
	@Autowired
	public void configurerGlobal(AuthenticationManagerBuilder builder) throws Exception {
		
		// utilize BCrypt
		PasswordEncoder encoder = passwordEncoder();
		// to make it work with the database
		builder.userDetailsService(jpaUserDetailsService).passwordEncoder(encoder);
		
	}


	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		
		http.logout(logout -> logout.deleteCookies("JSESSIONID"))
			.sessionManagement(session -> session.invalidSessionUrl("/")).authorizeRequests()
			.antMatchers("/css/**", "/js/**", "/images/**","/img/**", "/", "//", ";").permitAll()
			.antMatchers("/api/auth/**").permitAll()
			.antMatchers( "./assets/img/favicon.ico").permitAll()
			.antMatchers("/resources/**").permitAll()
		    .and().formLogin().successHandler(successHandler).loginPage("/").and().logout().permitAll()
			.and().addFilter(new JWTAuthenticationFilter(authenticationManager(), jwtService)) //here I add the filter to handle the login through the body of the POST method
			.addFilter(new JWTAuthorizationFilter(authenticationManager(),  jwtService)) //I add the filter that checks if the JWT beare comes in the Authorization header
			.csrf().disable() 
			.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);  //I delete the session
		
				
	}
                
	
}
