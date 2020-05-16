package p3.config.security;

import org.slf4j.Logger;

import org.springframework.context.annotation.Configuration;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration

public class MyWebSecurityConfigurerAdapter extends WebSecurityConfigurerAdapter{
	private static final Logger logger = LoggerFactory.getLogger(MyWebSecurityConfigurerAdapter.class);
	
	@Value("${mh.security.authserver.BCryptPasswordEncoder.usedToEncodePassword:true}")
	private boolean useBCryptPasswordEncoder2encodePassword;
	private String encode(String rawPassword) {
		String password = useBCryptPasswordEncoder2encodePassword ? passwordEncoder().encode(rawPassword) : rawPassword;
		logger.info("--mike --> encode({}) with useBCryptPasswordEncoder2encodePassword:{} returning password as {}", rawPassword, useBCryptPasswordEncoder2encodePassword, password);
		return password;
	}
		
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.authorizeRequests()
		// sample MVC urls
		.antMatchers("/", "/home","/public").permitAll()	//  urls allowed to see with or without login(without being authenticated == anonymous)
		.antMatchers("/anonymous").anonymous()				// the difference of this is from above is, if user is logged in, then this page will NOT be accessible. When user is not logged in, this is another way of doing same thing as above, another url allowed to see without login
		.antMatchers("/authenticated", "userSettings/**").authenticated()	
		.antMatchers("/admin", "/h2_console/**").hasAnyRole("ADMIN", "DEVELOPER")
		
		// animal MVC urls
		.antMatchers("/cats").hasRole("catMaster")
		.antMatchers("/dogs").hasRole("dogMaster")
		.antMatchers("/dogs").hasAuthority("haveDogs")
		
		// REST api urls
		.antMatchers("/rest/v1/cats/**").hasAnyRole("catMaster")
		.antMatchers("/rest/v1/dogs/**").hasAuthority("haveDogs")
		
	// rest of urls that are not matched via above antMathers will be caught by below line. Rest of urls require user to be logged in, meaning if authenticated then allow to proceed to that url, otherwise redirect to "/login"
		.anyRequest().authenticated()	//  if not authenticated and trying to access an authenticated url, it will 1st try to go to url, then will be "redirected"(302) to "login" page(with "location" attribute in response pointing to login url), after user logs in, he will go to url 
		.and()
		// below 3 lines is for "Form validation with Spring Security"
		
		.formLogin().loginPage("/login").permitAll()
		.and()
		.logout().permitAll();

		http.exceptionHandling().accessDeniedPage("/403");
		http.csrf().disable();
		http.headers().frameOptions().disable();
	}

	
	

	@Autowired
	public void configureGlobal_(AuthenticationManagerBuilder auth) throws Exception {
		auth.inMemoryAuthentication()
				.withUser("admin").password(encode("admin")).roles("ADMIN")
				.and()
				.withUser("developer").password(encode("developer")).authorities("ROLE_DEVELOPER", "haveDogs", "ROLE_catMaster", "ROLE_dogMaster", "ROLE_ADMIN")
				.and()
				.withUser("catsUser").password(encode("catsUser")).roles("catMaster")
				.and()
				.withUser("dogsUser").password(encode("dogsUser")).authorities("haveDogs", "ROLE_dogMaster");
				
	}
}

