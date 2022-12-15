package com.vgorash.main.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception{
        http
                .csrf().disable().authorizeRequests()
                .antMatchers("/developer").hasAuthority("ROLE_DEVELOPER")
                .antMatchers("/123").authenticated()
                .anyRequest().permitAll()
                .and().formLogin().loginPage("/auth").permitAll()
                .loginProcessingUrl("/auth").defaultSuccessUrl("/")
                .and().logout().permitAll().invalidateHttpSession(true);
    }
}
