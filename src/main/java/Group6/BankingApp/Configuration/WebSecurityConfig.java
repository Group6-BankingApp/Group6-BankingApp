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
                .requestMatchers("/accounts").authenticated()
                .requestMatchers("/accounts/savings/{id}").authenticated()
                .requestMatchers("/accounts/customer").authenticated()
                .requestMatchers("/accounts/customerIban").authenticated()
                .requestMatchers("/accounts/customer/{id}").authenticated()
                .requestMatchers("/accounts/{iban}").authenticated()
                .requestMatchers("/accounts/{iban}/delete").authenticated()
                .requestMatchers("/accounts/update").authenticated()
                .requestMatchers("/accounts/{iban}/debitcard").authenticated()
                .requestMatchers("/accounts/{iban}/debitcard/activate").authenticated()
                .requestMatchers("/accounts/{iban}/deactivateDebitCard/{cardNumber}").authenticated()
                .requestMatchers("/users").permitAll()
                .requestMatchers("/users/login").permitAll()
                .requestMatchers("/users/{id}").authenticated()
                .requestMatchers("/users/withAccount").authenticated()
                .requestMatchers("/users/withoutAccount").authenticated()
                .requestMatchers("/transactions").authenticated()
                .requestMatchers("/transactions/deposit").permitAll()
                .requestMatchers("/transactions/withdraw").permitAll()
                .requestMatchers("/transactions/{id}").authenticated()
                .requestMatchers("/transactions/customer/{iban}").authenticated()
                .requestMatchers("/transactions/customer/{iban}/filter").authenticated()
                .requestMatchers("/debitcards").authenticated()
                .requestMatchers("/debitcards/cardInsert").permitAll()
                .requestMatchers("/debitcards/{iban}").authenticated();

        httpSecurity.addFilterBefore(jwtTokenFilter, UsernamePasswordAuthenticationFilter.class);

        return httpSecurity.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList("https://group6-bankingapp.github.io/Group6-BankingApp-FrontEnd/"));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE"));
        configuration.setAllowedHeaders(Arrays.asList("Content-Type", "Authorization"));

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
