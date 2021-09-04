package com.saratovsecurity.psbprofiler.configuration;

import com.saratovsecurity.psbprofiler.security.CustomAuthenticationEntryPoint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private UserDetailsService userService;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationEntryPoint authenticationEntryPoint() {
        return new CustomAuthenticationEntryPoint();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                .antMatchers("/", "/login/**", "/registration", "/activation/**","/answerPoll", "/pollCreate", "/categoryDelete/**", "/createInterview", "/answerInterview","/uploadFile/**","/categoryCreate/**").permitAll()
                .antMatchers("/profile","/profile/*","/poll","/poll/*","/interview/**").permitAll()//.hasAnyAuthority("USER","ADMIN","INTERVIEWER")
                .antMatchers("/getAllUsers","/getAllCategories","/getMyInterviews/**","/getMyInterviewsUser/**","/getMyPolls/**","/getMyPollsUser/**","/getMyInterviews/**").permitAll()
                .anyRequest().authenticated()
                .and().formLogin().disable();
        http
                .csrf().disable()
                .cors();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userService).passwordEncoder(passwordEncoder());
    }
}
