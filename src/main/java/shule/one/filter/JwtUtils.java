package shule.one.filter;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

//
//import java.util.Date;
//
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.security.core.Authentication;
//import org.springframework.stereotype.Component;
//
//import io.jsonwebtoken.*;
//import shule.one.service.UserDetailsImpl;
//
//@Component
//public class JwtUtils {
//  private static final Logger logger = LoggerFactory.getLogger(JwtUtils.class);
//
//  @Value("${bezkoder.app.jwtSecret}")
//  private String jwtSecret;
//
//  @Value("${bezkoder.app.jwtExpirationMs}")
//  private int jwtExpirationMs;
//
//  public String generateJwtToken(Authentication authentication) {
//
//    UserDetailsImpl userPrincipal = (UserDetailsImpl) authentication.getPrincipal();
//
//    return Jwts.builder()
//        .setSubject((userPrincipal.getUsername()))
//        .setIssuedAt(new Date())
//        .setExpiration(new Date((new Date()).getTime() + jwtExpirationMs))
//        .signWith(SignatureAlgorithm.HS512, jwtSecret)
//        .compact();
//  }
//
//  public String getUserNameFromJwtToken(String token) {
//    return Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token).getBody().getSubject();
//  }
//
//  public boolean validateJwtToken(String authToken) {
//    try {
//      Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(authToken);
//      return true;
//    } catch (SignatureException e) {
//      logger.error("Invalid JWT signature: {}", e.getMessage());
//    } catch (MalformedJwtException e) {
//      logger.error("Invalid JWT token: {}", e.getMessage());
//    } catch (ExpiredJwtException e) {
//      logger.error("JWT token is expired: {}", e.getMessage());
//    } catch (UnsupportedJwtException e) {
//      logger.error("JWT token is unsupported: {}", e.getMessage());
//    } catch (IllegalArgumentException e) {
//      logger.error("JWT claims string is empty: {}", e.getMessage());
//    }
//
//    return false;
//  }
//}
@Component
public class JwtUtils{
	  private String SECRET_KEY = "secret";

	    public String extractUsername(String token) {
	        return extractClaim(token, Claims::getSubject);
	    }

	    public Date extractExpiration(String token) {
	        return extractClaim(token, Claims::getExpiration);
	    }

	    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
	        final Claims claims = extractAllClaims(token);
	        return claimsResolver.apply(claims);
	    }
	    private Claims extractAllClaims(String token) {
	        return Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token).getBody();
	    }

	    private Boolean isTokenExpired(String token) {
	        return extractExpiration(token).before(new Date());
	    }

//	    public String generateToken(Authentication authentication) {
//	        Map<String, Object> claims = new HashMap<>();
//	        return createToken(claims, authentication.getName());
//	    }
	    public String generateToken(Authentication authentication) {
			Map<String, Object> claims = new HashMap<>();
	        return createToken(claims, authentication.getName());
		}

	    private String createToken(Map<String, Object> claims, String subject) {

	        return Jwts.builder().setClaims(claims).setSubject(subject).setIssuedAt(new Date(System.currentTimeMillis()))
	                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 10))
	                .signWith(SignatureAlgorithm.HS256, SECRET_KEY).compact();
	    }

	    public Boolean validateToken(String token, UserDetails userDetails) {
	        final String username = extractUsername(token);
	        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
	    }

		
}

