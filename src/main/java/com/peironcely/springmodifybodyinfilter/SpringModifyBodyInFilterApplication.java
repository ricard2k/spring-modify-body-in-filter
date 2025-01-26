package com.peironcely.springmodifybodyinfilter;

import com.peironcely.springmodifybodyinfilter.filter.CspNonceFilter;
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
        //It is important to filter only HTML files. Otherwise, the so much slow filter will be applied to all requests.
        registration.addUrlPatterns("/");
        registration.addUrlPatterns("*.html");
        registration.setFilter(new CspNonceFilter());
        return registration;
    }

}
