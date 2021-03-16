package ca.mcgill.ecse321.repairshop.config;

import ca.mcgill.ecse321.repairshop.service.utilities.TokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

/**Not sure how it works
 * Code inspired by
 * https://daddyprogrammer.org/post/636/springboot2-springsecurity-authentication-authorization/
 */
@Configuration
public class JwtConfiguration extends WebSecurityConfigurerAdapter {
    @Autowired
    TokenProvider tokenProvider;

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.apply(new JwtConfigurer(tokenProvider));
    }

    @Override
    public void configure(WebSecurity web) {
        web.ignoring().antMatchers("/**");
    }
}