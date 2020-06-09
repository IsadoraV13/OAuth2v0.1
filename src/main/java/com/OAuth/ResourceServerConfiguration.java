package com.OAuth;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

// Creating a Resource Server
@EnableResourceServer // tell Spring to activate the resource server
@RestController
public class ResourceServerConfiguration extends ResourceServerConfigurerAdapter {

    @RequestMapping("/public")
    public String publish () {
        return "Public Page";
    }

    @RequestMapping ("/private")
    public String priVate () {
        return "Private Page";
    }

    @RequestMapping ("/admin")
    public String admin () {
        return "Administrator Page";
    }

    // configure security of resource server (and authorisation server at the same time, give in same program)
    @Override
    public void configure (HttpSecurity http) throws Exception {
        http
                .authorizeRequests().antMatchers ("/oauth/token", "/oauth/authorise**", "/public"). permitAll ();
        // .anyRequest().authenticated(); // commented otherwise all resources would only be accessible if user has been validated
        http.requestMatchers().antMatchers ("/private") // Deny access to "/ private"
                .and().authorizeRequests()
                .antMatchers("/private").access ("hasRole ('USER')")
                .and().requestMatchers().antMatchers("/admin") // Deny access to "/ admin"
                .and().authorizeRequests()
                .antMatchers ("/admin").access ("hasRole ('ADMIN')");
    }

}
