package by.unit.bsu.scsm;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.ldap.authentication.ad.ActiveDirectoryLdapAuthenticationProvider;

import org.springframework.core.env.Environment;
import org.springframework.beans.factory.annotation.Autowired;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    
    @Autowired
    protected Environment env;
    
    @Override
    protected void configure(AuthenticationManagerBuilder authManagerBuilder) throws Exception 
    {
        //в конфиге контроллеры должны быть разделены точкой с запятой (;)
        String[] domains = env.getProperty("ad.domain").split(";");
        String[] urls = env.getProperty("ad.url").split(";");
        for(int i=0; i<domains.length; ++i)
        {
            ActiveDirectoryLdapAuthenticationProvider adProvider = new ActiveDirectoryLdapAuthenticationProvider(domains[i], urls[i]);
            //adProvider.setConvertSubErrorCodesToExceptions(true); //для проверки ошибок
            //adProvider.setUseAuthenticationRequestCredentials(true); //для проверки ошибок
            authManagerBuilder.authenticationProvider(adProvider);
        }
    }    
  
    @Override //basic auth
    protected void configure(HttpSecurity http) throws Exception {
        http
            .csrf().disable()
            .httpBasic()
                .realmName("bsu")
                .and()
            .authorizeRequests()
                //.antMatchers("/logouted").permitAll()
                //.antMatchers("/accessdenied").permitAll()
                .antMatchers("/admin/**").hasAuthority("sce") //доступ только группе SCE
                .anyRequest().authenticated()
                .and()
            .logout()
                .logoutSuccessUrl("/logouted")
                .and()
            .exceptionHandling()
                .accessDeniedPage("/accessdenied");
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web
          .ignoring()
            .antMatchers("/favicon.ico")
            .antMatchers("/css/**", "/img/**", "/js/**", "/images/**", "/fonts/**");
            //.antMatchers("/resources/static/**");
    }    
}
