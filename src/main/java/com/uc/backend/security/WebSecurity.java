package com.uc.backend.security;

import com.uc.backend.security.jwt.JwtEntryPoint;
import com.uc.backend.security.jwt.JwtTokenFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurity extends WebSecurityConfigurerAdapter {

    @Autowired
    UserDetailsServiceImpl userDetailsServiceImpl;

    @Autowired
    JwtEntryPoint jwtEntryPoint;

    @Bean
    JwtTokenFilter jwtTokenFilter(){
        return new JwtTokenFilter();
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }


    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Override
    protected AuthenticationManager authenticationManager() throws Exception {
        return super.authenticationManager();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsServiceImpl).passwordEncoder(passwordEncoder());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.cors().and().csrf().disable()
                .authorizeRequests()
                .antMatchers("/oauth/google", "/oauth/facebook", "/oauth/logout").permitAll()
                .antMatchers("/model/service/get-prices").hasAnyRole("ADMIN", "MOD", "TEACH") // Prices info
                .antMatchers("/open", "/open/*", "/open/**").permitAll() // OpenClientController
                .antMatchers("/model/blog", "/model/blog/*", "/model/blog/**").permitAll()
                .antMatchers("/android/**").permitAll()

                .antMatchers("/model/comment-forum", "/model/comment-forum/*", "/model/comment-forum/**").hasAnyRole("CLIENT", "TEACH", "ADMIN")


                .antMatchers("/model/user/me").authenticated() // To get the current User
                .antMatchers("/model/user", "/model/user/*", "/model/user/**").hasAnyRole("ADMIN", "MOD", "TEACH")
                .antMatchers("/model/course", "/model/course/*", "/model/course/**").hasAnyRole("ADMIN", "TEACH")
                .antMatchers("/model/sale", "/model/sale/*", "/model/sale/**").hasAnyRole("ADMIN")
                .antMatchers("/model/service", "/model/service/*", "/model/service/**").hasAnyRole("ADMIN", "MOD", "TEACH")
                .antMatchers("/model/sale-canceled", "/model/sale-canceled/*", "/model/sale-canceled/**").hasAnyRole("ADMIN")
                .antMatchers("/model/service-session", "/model/service-session/*", "/model/service-session/**").hasAnyRole("ADMIN")
                .antMatchers("/model/enrollment", "/model/enrollment/*", "/model/enrollment/**").hasAnyRole("ADMIN")
                .antMatchers("/model/course-suggestion", "/model/course-suggestion/*", "/model/course-suggestion/**").hasAnyRole("ADMIN")
                .antMatchers("/model/comment-forum", "/model/comment-forum/*", "/model/comment-forum/**").hasAnyRole("ADMIN")

                .antMatchers("/s3/comment-forum", "/s3/comment-forum/*", "/s3/comment-forum/**").hasAnyRole("CLIENT", "TEACH", "ADMIN")


                .antMatchers("/s3/course", "/s3/course/*", "/s3/course/**").hasAnyRole("ADMIN", "MOD")
                .antMatchers("/s3/service", "/s3/service/*", "/s3/service/**").hasAnyRole("ADMIN", "MOD", "TEACH")


                .antMatchers("/c", "/c/*","/c/**").hasRole("CLIENT")
                .antMatchers("/t", "/t/*","/t/**").hasAnyRole("TEACH", "ADMIN")
                .antMatchers("/a", "/a/*","/a/**").hasAnyRole( "ADMIN")


                .anyRequest().hasRole("ADMIN")
                .and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        http.addFilterBefore(jwtTokenFilter(), UsernamePasswordAuthenticationFilter.class);

    }
}
