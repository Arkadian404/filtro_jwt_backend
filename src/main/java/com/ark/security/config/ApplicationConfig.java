package com.ark.security.config;

import com.ark.security.service.UserService;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.cfg.CoercionAction;
import com.fasterxml.jackson.databind.cfg.CoercionConfig;
import com.fasterxml.jackson.databind.cfg.CoercionInputShape;
import com.fasterxml.jackson.datatype.jsr310.JSR310Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.RequiredArgsConstructor;

import org.hibernate.tool.schema.internal.StandardUserDefinedTypeExporter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;


@Configuration
@EnableWebMvc
@RequiredArgsConstructor
public class ApplicationConfig{


    private final UserService userService;

    @Bean
    public UserDetailsService userDetailsService(){
//        System.out.println("Truy cap userDetailsService trong ApplicationConfig");
        return userService;
    }

    @Bean
    public ObjectMapper objectMapper(){
//        System.out.println("Truy cap object Mapper trong ApplicationConfig");
        ObjectMapper mapper = new ObjectMapper();
        mapper.setAnnotationIntrospector(new CustomJacksonAnnotation());
        mapper.findAndRegisterModules();
        mapper.coercionConfigDefaults()
                .setAcceptBlankAsEmpty(true)
                .setCoercion(CoercionInputShape.EmptyObject, CoercionAction.AsNull)
                .setCoercion(CoercionInputShape.EmptyString, CoercionAction.AsNull);
        mapper.configure(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT, true);
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        mapper.disable(SerializationFeature.WRITE_DATE_TIMESTAMPS_AS_NANOSECONDS);
        return mapper;
    }

    @Bean
    public AuthenticationProvider authenticationProvider(){
//        System.out.println("Truy cap authenticationProvider trong ApplicationConfig");
        DaoAuthenticationProvider daoAuthProvider = new DaoAuthenticationProvider();
        daoAuthProvider.setUserDetailsService(userDetailsService());
        daoAuthProvider.setPasswordEncoder(passwordEncoder());
        return daoAuthProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
//        System.out.println("Truy cap authenticationManager trong ApplicationConfig");
        return config.getAuthenticationManager();
    }

    @Bean
     public PasswordEncoder passwordEncoder(){
//        System.out.println("Truy cap passwordEncoder trong ApplicationConfig");
        return new BCryptPasswordEncoder();
    }


}
