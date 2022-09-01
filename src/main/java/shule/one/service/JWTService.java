package shule.one.service;

import java.io.IOException;
import java.util.Collection;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;

import io.jsonwebtoken.Claims;

public interface JWTService {
	
	public String createToken (Authentication authentication) throws JsonProcessingException;
	public boolean validarToken (String token);
	public String getUsername (String token);
	public Claims getClaims (String token);
	public Collection<? extends GrantedAuthority> getAuthorities (String token) throws JsonParseException, JsonMappingException, IOException;
	public String limpiarToken(String token);

}
