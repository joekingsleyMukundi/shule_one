package shule.one.service;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;
import org.springframework.util.Base64Utils;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import shule.one.filter.SimpleGrantedAuthorityMixing;

@Component
public class JWTServiceImpl implements JWTService {

	public static final String SECRET_KEY = Base64Utils.encodeToString("ALGUNA-CLAAV3-SECRETA-454864564".getBytes());
	
	public static final String TOKEN_PREFIX = "Bearer ";
	
	public static final long TOKEN_EXPIRATION_TIME_MILISEG = 3600000L; // an hour
	
	private final Logger log = LoggerFactory.getLogger(this.getClass());
	

	@Override
	public String createToken(Authentication authentication) throws JsonProcessingException {

		Collection<? extends GrantedAuthority> roles = authentication.getAuthorities();

		// can also be obtained directly with authResult.getName();
		String username = ((User) authentication.getPrincipal()).getUsername();

		Claims claims = Jwts.claims();
		// I convert roles to JSON and put them in claims to include them in the jwtToken
		claims.put("authorities", new ObjectMapper().writeValueAsString(roles));
		
		// I create the token
		String jwtToken = Jwts.builder().setClaims(claims).setSubject(username)
				.signWith(SignatureAlgorithm.HS512, SECRET_KEY.getBytes()).setIssuedAt(new Date())
				.setExpiration(new Date(System.currentTimeMillis() + TOKEN_EXPIRATION_TIME_MILISEG)).compact();
		
		
		System.out.println(jwtToken);

		return jwtToken;
	}

	@Override
	public boolean validarToken(String token) {
		
		
		// validate the token and get the body
		try {
			 
			@SuppressWarnings("unused")
			Claims claims = getClaims(token);
			
			return true;

		} catch (JwtException | IllegalArgumentException e) {
			log.error("Error validating JWT token: ", e);
			return false;
			
		} catch (Exception e) {
			log.error("Failed to validate JWT token Exception: ", e);
			return false;
		}
	}

	
	@Override
	public Claims getClaims(String token) {

		Claims claims = Jwts.parser()
				.setSigningKey(SECRET_KEY.getBytes())
				.parseClaimsJws(limpiarToken(token)) 		// validate the token
				.getBody();
		
		return claims;
	}
	
	@Override
	public String getUsername(String token) {

		return getClaims(token).getSubject();
	}


	@Override
	public Collection<? extends GrantedAuthority> getAuthorities(String token) throws JsonParseException, JsonMappingException, IOException {

		Object roles = getClaims(token).get("authorities");
		
		// I convert the JSON to a list of GrantedAuthority
		Collection<? extends GrantedAuthority> authorities = Arrays.asList(
				new ObjectMapper()
				// I define the mixin class that does the conversion of the object
				.addMixIn(SimpleGrantedAuthority.class, SimpleGrantedAuthorityMixing.class) 
				.readValue(roles.toString().getBytes(),SimpleGrantedAuthority[].class));
		
		return authorities;
	}

	
	@Override
	public String limpiarToken(String token) {

		if (token!=null && token.startsWith(TOKEN_PREFIX)) {
			return  token.replace(TOKEN_PREFIX, "");
		}
		return null;
		
	}

}
