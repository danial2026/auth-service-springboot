package com.springboot.server.authenticationservice.config;


import com.springboot.server.authenticationservice.entity.RoleEntity;
import com.springboot.server.authenticationservice.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.BeanIds;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.http.HttpServletResponse;

@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityCredentialsConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private JwtConfig jwtConfig;

    @Autowired
    private JwtTokenProvider tokenProvider;

    @Autowired
    private UserRepository userRepository;

    @Value("${security.service.username}")
    private String serviceUsername;

    @Value("${security.service.password}")
    private String servicePassword;

    private static final String baseUrl = "/api/v1";

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .cors()
                .and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .exceptionHandling().authenticationEntryPoint((req, rsp, e) -> rsp.sendError(HttpServletResponse.SC_UNAUTHORIZED))
                .and()
                .addFilterBefore(new JwtTokenAuthenticationFilter(serviceUsername, jwtConfig, tokenProvider, userRepository), UsernamePasswordAuthenticationFilter.class)
                .authorizeRequests()
                .antMatchers(HttpMethod.GET, baseUrl + "/users/up").permitAll()
                .antMatchers(HttpMethod.POST, baseUrl + "/users/signin").permitAll()
                .antMatchers(HttpMethod.POST, baseUrl + "/users/signin/refresh-token").permitAll()
                .antMatchers(HttpMethod.PUT, baseUrl + "/users/signin/forgot-password").anonymous()
                .antMatchers(HttpMethod.PUT, baseUrl + "/users/signin/reset-password").anonymous()
                .antMatchers(HttpMethod.POST, baseUrl + "/users/signup").anonymous()
                .antMatchers(HttpMethod.POST, baseUrl + "/users/verify").anonymous()
                .antMatchers(HttpMethod.POST, baseUrl + "/users/resend-verification-code").anonymous()
                .anyRequest().authenticated();
    }

    // Spring has UserDetailsService interface, which can be overriden to provide our implementation for fetching user from database (or any other source).
    // The UserDetailsService object is used by the auth manager to load the user from database.
    // In addition, we need to define the password encoder also. So, auth manager can compare and verify passwords.
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        // Configure in-memory authentication provider for service accounts for
        // service inter-communication
        auth.inMemoryAuthentication()
                .withUser(serviceUsername)
                .password(passwordEncoder().encode(servicePassword))
                .roles(RoleEntity.SERVICE.getName());

        // Configure DB authentication provider for user accounts
        auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean(BeanIds.AUTHENTICATION_MANAGER)
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

}
