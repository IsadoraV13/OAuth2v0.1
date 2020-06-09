package com.OAuth;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

@SpringBootApplication
@EnableWebSecurity // tell Spring to activate web security (although redundant here)
public class WebSecurityConfiguration extends WebSecurityConfigurerAdapter {

    public static void main (String [] args) {
        SpringApplication.run(WebSecurityConfiguration.class, args);
    }
    @Bean
    @Override
    // return the class responsible for managing authentications
    public AuthenticationManager authenticationManagerBean () throws Exception {
        return super.authenticationManagerBean();
    }

    @Bean
    @Override
    // define users that will have access to our website
    public UserDetailsService userDetailsService () {
        UserDetails user = User.builder().username("user").password(pwdEncoder().encode("secret")).
                roles("USER").build();
        UserDetails userAdmin = User.builder().username("admin").password(pwdEncoder().encode("secret")).
                roles("ADMIN").build();
        return new InMemoryUserDetailsManager(user,userAdmin);
    }

    @Bean
    public PasswordEncoder pwdEncoder () {
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure (HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .authorizeRequests ()
                .antMatchers( "/", "/index", "/webpublic").permitAll()
                .antMatchers( "/webprivate").authenticated()
                .antMatchers( "/webadmin").hasRole("ADMIN").and()
                .formLogin()
                .loginPage( "/login")
                .permitAll()
                .and()
                .logout() // get method for I desabilitado CSRF
                .permitAll();
    }
}



