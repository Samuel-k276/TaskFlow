package taskapp.security;

import io.github.cdimascio.dotenv.Dotenv;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.nio.charset.StandardCharsets;

@Service
public class JwtService {
   // Carrega diretamente do arquivo .env usando a biblioteca dotenv-java
   private final static String SECRET_KEY = Dotenv.configure().load().get("JWT_SECRET");
   private final static long EXPIRATION_TIME = Long.parseLong(Dotenv.configure().load().get("JWT_EXPIRATION"));

   public String extractUsername(String token) {
      return extractClaim(token, Claims::getSubject);
   }

   public String generateToken(UserDetails userDetails) {
      return generateToken(new HashMap<>(), userDetails);
   }

   public boolean isTokenValid(String token, UserDetails userDetails) {
      final String username = extractUsername(token);
      return (username.equals(userDetails.getUsername())) && !isTokenExpired(token);
   }

   private String generateToken(Map<String, Object> extraClaims, UserDetails userDetails) {
      return Jwts.builder()
         .setClaims(extraClaims)
         .setSubject(userDetails.getUsername())
         .setIssuedAt(new Date(System.currentTimeMillis()))
         .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
         .signWith(getSigningKey(), SignatureAlgorithm.HS256)
         .compact();
   }

   private boolean isTokenExpired(String token) {
      return extractExpiration(token).before(new Date());
   }

   private Date extractExpiration(String token) {
      return extractClaim(token, Claims::getExpiration);
   }

   private <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
      final Claims claims = extractAllClaims(token);
      return claimsResolver.apply(claims);
   }

   private Claims extractAllClaims(String token) {
      return Jwts.parserBuilder()
         .setSigningKey(getSigningKey())
         .build()
         .parseClaimsJws(token)
         .getBody();
   }

   private Key getSigningKey() {
      // Usar a string diretamente como bytes em vez de tentar decodificar de Base64
      return Keys.hmacShaKeyFor(SECRET_KEY.getBytes(StandardCharsets.UTF_8));
   }
}