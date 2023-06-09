package Group6.BankingApp.util;

import Group6.BankingApp.Models.Role;
import Group6.BankingApp.Services.CustomerDetailsService;
import io.jsonwebtoken.JwtException;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import io.jsonwebtoken.*;
import jakarta.annotation.PostConstruct;

import java.security.PrivateKey;
import java.util.Date;
import java.util.List;

@Component
public class JwtTokenProvider {

    @Value("${application.token.validity}")
    private long validityInMicroseconds;
    private final CustomerDetailsService customerDetailsService;
    private final JwtKeyProvider jwtKeyProvider;

    public JwtTokenProvider(CustomerDetailsService customerDetailsService, JwtKeyProvider jwtKeyProvider) {
        this.customerDetailsService = customerDetailsService;
        this.jwtKeyProvider = jwtKeyProvider;
    }

    public String createToken(String username, List<Role> roles) throws JwtException {
        Claims claims = Jwts.claims().setSubject(username);
        claims.put("auth", roles.stream().map(Role::name).toList());

        Date now = new Date();
        Date expiration = new Date(now.getTime() + validityInMicroseconds);

        return  Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(expiration)
                .signWith(jwtKeyProvider.getPrivateKey())
                .compact();
    }

    public  Authentication getAuthentication(String token) {
        try{
            Jws<Claims> claims = Jwts.parserBuilder()
                    .setSigningKey(jwtKeyProvider.getPrivateKey())
                    .build()
                    .parseClaimsJws(token);
            String username = claims.getBody().getSubject();
            UserDetails userDetails = customerDetailsService.loadUserByUsername(username);
            return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
        }
        catch (JwtException | IllegalArgumentException e){
            throw new JwtException("Expired or invalid JWT token");
        }
    }
}
