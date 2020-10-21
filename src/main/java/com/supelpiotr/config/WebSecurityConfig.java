package com.supelpiotr.config;

import com.supelpiotr.utils.controller.BaseController;
import com.supelpiotr.user.controller.SessionController;
import com.supelpiotr.user.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Configuration
@AllArgsConstructor
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    private final UserService userService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final CustomLoginHandler customLoginHandler;
    private final CustomLogoutHandler customLogoutHandler;

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http.authorizeRequests()
                .antMatchers("/api/session").permitAll()
                .antMatchers("/api/users").hasRole("ADMIN")
                .antMatchers("/api/register").permitAll()
                .antMatchers("/api/user/**").permitAll()
                .antMatchers(HttpMethod.GET).permitAll()
                .antMatchers("/api/create/subaccount").hasRole("BASIC");

        http.formLogin();

        http.logout()
                .logoutUrl("/api/session/logout")
                .addLogoutHandler(customLogoutHandler)
                .logoutSuccessHandler(customLogoutHandler);

        http.csrf()
                .ignoringAntMatchers("/api/session/**");

        http.addFilterAt(customAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);

        http.csrf().disable();
    }

    private CustomAuthenticationFilter customAuthenticationFilter() throws Exception {
        CustomAuthenticationFilter filter = new CustomAuthenticationFilter();
        filter.setAuthenticationSuccessHandler(customLoginHandler);
        filter.setAuthenticationFailureHandler(customLoginHandler);
        filter.setAuthenticationManager(authenticationManager());
        filter.setFilterProcessesUrl("/api/session/login");
        return filter;
    }

    private static void responseText(HttpServletResponse response, String content) throws IOException {
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        byte[] bytes = content.getBytes(StandardCharsets.UTF_8);
        response.setContentLength(bytes.length);
        response.getOutputStream().write(bytes);
        response.flushBuffer();
    }

    @Component
    public static class CustomLoginHandler extends BaseController implements AuthenticationSuccessHandler, AuthenticationFailureHandler {
        @Override
        public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
            logger.info("User login successfully, name={}", authentication.getName());
            responseText(response, objectResult(SessionController.getJSON(authentication)));
        }

        @Override
        public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException {
            responseText(response, errorMessage(exception.getMessage()));
        }
    }

    @Component
    public static class CustomLogoutHandler extends BaseController implements LogoutHandler, LogoutSuccessHandler {

        @Override
        public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
            logger.info("User logout successfully, name={}", authentication.getName());
        }

        @Override
        public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
            responseText(response, objectResult(SessionController.getJSON(null)));
        }
    }

}
