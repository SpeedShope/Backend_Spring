package com.pi.Centrale_Achat.security;

import com.pi.Centrale_Achat.entities.ERole;
import com.pi.Centrale_Achat.security.jwt.AuthEntryPointJwt;
import com.pi.Centrale_Achat.security.jwt.AuthTokenFilter;
import com.pi.Centrale_Achat.serviceImpl.UserDetailsServiceImpl;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


@Configuration
@EnableGlobalMethodSecurity(
        prePostEnabled = true)
public class WebSecurityConfig { // extends WebSecurityConfigurerAdapter {

    @Autowired
    UserDetailsServiceImpl userDetailsService;

    @Autowired
    private AuthEntryPointJwt unauthorizedHandler;

    @Bean
    public AuthTokenFilter authenticationJwtTokenFilter() {
        return new AuthTokenFilter();
    }


    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();

        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());

        return authProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }


    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.cors().and().csrf().disable()
                .exceptionHandling().authenticationEntryPoint(unauthorizedHandler).and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
                .authorizeRequests().antMatchers("/api/auth/**").permitAll()
                .antMatchers("/api/test/**").hasAnyAuthority(ERole.ROLE_OPERATOR.toString())
                .antMatchers("/api/product/**").hasAnyAuthority(ERole.ROLE_SUPPLIER.toString(),ERole.ROLE_CUSTOMER.toString(),ERole.ROLE_OPERATOR.toString())
                .antMatchers("/api/category/**").hasAnyAuthority(ERole.ROLE_SUPPLIER.toString())
                .antMatchers(" /api/user/**","/images/**","/imageProduct/**","/predict/product").permitAll()
                .antMatchers("/api/admin/**").hasAnyAuthority(ERole.ROLE_ADMIN.toString())
                .antMatchers("/api/contracts/**").permitAll()


                .antMatchers("/api/tender/**").hasAnyAuthority(ERole.ROLE_CUSTOMER.toString(),ERole.ROLE_OPERATOR.toString())
                .antMatchers("/api/OperatorScore/**").permitAll()
                .antMatchers("/api/comment/**").hasAnyAuthority(ERole.ROLE_CUSTOMER.toString())






                .antMatchers("/api/delivery/**").hasAnyAuthority(ERole.ROLE_CUSTOMER.toString(),ERole.ROLE_DELIVERY.toString())



                .anyRequest().authenticated();

        http.headers().frameOptions().sameOrigin();

        http.authenticationProvider(authenticationProvider());

        http.addFilterBefore(authenticationJwtTokenFilter(), UsernamePasswordAuthenticationFilter.class);

        return http.build();

    }

}
