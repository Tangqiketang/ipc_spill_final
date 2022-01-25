package com.zdhk.ipc.config;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalTimeSerializer;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;import com.zdhk.ipc.data.constant.TimeConst;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

@Configuration
public class JacksonConfig {
    @Bean
    public ObjectMapper objectMapper(){
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        objectMapper.disable(DeserializationFeature.ADJUST_DATES_TO_CONTEXT_TIME_ZONE);
        JavaTimeModule javaTimeModule = new JavaTimeModule();
        javaTimeModule.addSerializer(LocalDateTime.class,new LocalDateTimeSerializer(DateTimeFormatter.ofPattern(TimeConst.DEFAULT_DATE_TIME_FORMAT)));
        javaTimeModule.addSerializer(LocalDate.class,new LocalDateSerializer(DateTimeFormatter.ofPattern(TimeConst.DEFAULT_DATE_FORMAT)));
        javaTimeModule.addSerializer(LocalTime.class,new LocalTimeSerializer(DateTimeFormatter.ofPattern(TimeConst.DEFAULT_TIME_FORMAT)));
        javaTimeModule.addDeserializer(LocalDateTime.class,new LocalDateTimeDeserializer(DateTimeFormatter.ofPattern(TimeConst.DEFAULT_DATE_TIME_FORMAT)));
        javaTimeModule.addDeserializer(LocalDate.class,new LocalDateDeserializer(DateTimeFormatter.ofPattern(TimeConst.DEFAULT_DATE_FORMAT)));
        javaTimeModule.addDeserializer(LocalTime.class,new LocalTimeDeserializer(DateTimeFormatter.ofPattern(TimeConst.DEFAULT_TIME_FORMAT)));
        objectMapper.registerModule(javaTimeModule).registerModule(new ParameterNamesModule());
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES,false);
        return objectMapper;
    }
}
