package com.peironcely.springmodifybodyinfilter;

import com.peironcely.springmodifybodyinfilter.filter.CsrfNonceFilter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class SpringModifyBodyInFilterApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringModifyBodyInFilterApplication.class, args);
    }

    @Bean
    public FilterRegistrationBean myFilterRegistration() {
        FilterRegistrationBean registration = new FilterRegistrationBean();
        registration.addUrlPatterns("/*");
        registration.setFilter(new CsrfNonceFilter());
        return registration;
    }

}
