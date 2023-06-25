package Group6.BankingApp.Configuration;

import Group6.BankingApp.filter.JwtTokenFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.util.Arrays;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class WebSecurityConfig {

    JwtTokenFilter jwtTokenFilter;

    public WebSecurityConfig(JwtTokenFilter jwtTokenFilter) {
        this.jwtTokenFilter = jwtTokenFilter;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.cors().and().csrf().disable();
        httpSecurity.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        httpSecurity.authorizeHttpRequests()
                .requestMatchers("/accounts").permitAll()
                .requestMatchers("/accounts/savings/{id}").permitAll()
                .requestMatchers("/accounts/{id}").permitAll()
                .requestMatchers("/accounts/customer").permitAll()
                .requestMatchers("/accounts/customer/{id}").permitAll()
                .requestMatchers("/accounts/{iban}").permitAll()
                .requestMatchers("/accounts/{iban}/pin").permitAll()
                .requestMatchers("/accounts/{iban}/debitcard").permitAll()
                .requestMatchers("/accounts/{iban}/debitcard/activate").permitAll()
                .requestMatchers("/accounts/{iban}/deactivateDebitCard/{cardNumber}").permitAll()
                .requestMatchers("/users/{id}").permitAll()
                .requestMatchers("/users").permitAll()
                .requestMatchers("/users/login").permitAll()
                .requestMatchers("/users/withAccount").authenticated()
                .requestMatchers("/users/withoutAccount").authenticated()
                .requestMatchers("/transactions").permitAll()
                .requestMatchers("/transactions/{id}").permitAll()
                .requestMatchers("/transactions/customer/{iban}").permitAll()
                .requestMatchers("/transactions/customer/{iban}/filter").permitAll()
                .requestMatchers("/transactions/deposit").permitAll()
                .requestMatchers("/transactions/withdraw").permitAll()
                .requestMatchers("/debitcards").permitAll()
                .requestMatchers("/debitcards/cardInsert").permitAll();

        httpSecurity.addFilterBefore(jwtTokenFilter, UsernamePasswordAuthenticationFilter.class);

        return httpSecurity.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList("http://localhost:5173"));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE"));
        configuration.setAllowedHeaders(Arrays.asList("Content-Type", "Authorization"));

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
