package am.bdgexame.bancapplication.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .cors()
                .and()
                .csrf()
                .disable()
                .authorizeRequests()
                .antMatchers("/api/user/register").permitAll()
                .antMatchers("/api/user/signIn").permitAll()
                .antMatchers("/").permitAll()
                .antMatchers(HttpMethod.POST, "/api/account/**").permitAll()
                .antMatchers(HttpMethod.PUT, "/api/user/**").permitAll();
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/v2/api-docs",
                "/configuration/ui",
                "/swagger-resources/**",
                "/configuration/security",
                "/swagger-ui.html",
                "/webjars/**");
    }
//    https://stackoverflow.com/questions/37671125/how-to-configure-spring-security-to-allow-swagger-url-to-be-accessed-without-aut
}
