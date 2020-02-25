package com.asa.processminer.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    protected void configure(final AuthenticationManagerBuilder auth) throws Exception {
        auth.inMemoryAuthentication()
                .withUser("user1").password(passwordEncoder().encode("a")).roles("USER")
                .and()
                .withUser("user2").password(passwordEncoder().encode("a")).roles("USER")
                .and()
                .withUser("admin").password(passwordEncoder().encode("a")).roles("ADMIN");
    }

//    protected void configure(HttpSecurity http) throws Exception {
//
//        http
//                .csrf().disable()
//                .authorizeRequests()
//                .antMatchers(HttpMethod.POST,"/project").permitAll()
//                .anyRequest().authenticated();
                //HTTP Basic authentication
//                .httpBasic()
//                .and()
//                .authorizeRequests()
//                .antMatchers(HttpMethod.GET, "/books/**").hasRole("USER")
//                .antMatchers(HttpMethod.POST, "/books").hasRole("ADMIN")
//                .antMatchers(HttpMethod.PUT, "/books/**").hasRole("ADMIN")
//                .antMatchers(HttpMethod.PATCH, "/books/**").hasRole("ADMIN")
//                .antMatchers(HttpMethod.DELETE, "/books/**").hasRole("ADMIN")
//                .and()
//                .csrf().disable()
//                .formLogin().disable();
//    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
