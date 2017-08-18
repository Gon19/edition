package pt.ist.socialsoftware.edition.config;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.social.SocialWebAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.social.connect.web.SignInAdapter;

import pt.ist.socialsoftware.edition.security.LdoDSignInAdapter;
import pt.ist.socialsoftware.edition.utils.Bootstrap;

@PropertySource({ "classpath:application.properties", "classpath:secrete.properties" })
@ComponentScan(basePackages = "pt.ist.socialsoftware.edition")
@SpringBootApplication
@EnableScheduling
@EnableAutoConfiguration(exclude = { SocialWebAutoConfiguration.class, DataSourceAutoConfiguration.class })
public class Application extends SpringBootServletInitializer implements InitializingBean {

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		return application.sources(Application.class);
	}

	public static void main(String[] args) throws Exception {
		SpringApplication.run(Application.class, args);
	}

	@Override
	public void afterPropertiesSet() {
		Bootstrap.initializeSystem();
	}

	@Bean
	public SignInAdapter signInAdapter() {
		return new LdoDSignInAdapter(new HttpSessionRequestCache());
	}

	@Bean(name = "messageSource")
	public ReloadableResourceBundleMessageSource reloadableResourceBundleMessageSource() {
		ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
		messageSource.setBasenames("classpath:messages");
		messageSource.setDefaultEncoding("UTF-8");
		return messageSource;
	}

	// @Bean
	// public ErrorPageFilter errorPageFilter() {
	// return new ErrorPageFilter();
	// }
	//
	// @Bean
	// public FilterRegistrationBean disableSpringBootErrorFilter(ErrorPageFilter
	// filter) {
	// FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean();
	// filterRegistrationBean.setFilter(filter);
	// filterRegistrationBean.setEnabled(false);
	// return filterRegistrationBean;
	// }
}
