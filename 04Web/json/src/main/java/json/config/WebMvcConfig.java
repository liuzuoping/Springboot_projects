package json.config;
import com.fasterxml.jackson.databind.ObjectMapper;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.GsonHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;

import java.text.SimpleDateFormat;

@Configuration
public class WebMvcConfig {
//    @Bean
//    MappingJackson2HttpMessageConverter mappingJackson2HttpMessageConverter(){
//        MappingJackson2HttpMessageConverter converter=new MappingJackson2HttpMessageConverter();
//        ObjectMapper om=new ObjectMapper();
//        om.setDateFormat(new SimpleDateFormat("yyyy/MM/dd"));
//        converter.setObjectMapper(om);
//        return converter;
//    }
//    @Bean
//    ObjectMapper objectMapper(){
//        ObjectMapper om=new ObjectMapper();
//        om.setDateFormat(new SimpleDateFormat("yyyy/MM/dd"));
//        return om;
//    }


//    @Bean
//    GsonHttpMessageConverter gsonHttpMessageConverter(){
//        GsonHttpMessageConverter converter=new GsonHttpMessageConverter();
//        converter.setGson(new GsonBuilder().setDateFormat("yyyy/MM/dd").create());
//        return converter;
//    }
    @Bean
    Gson gson(){
        return new GsonBuilder().setDateFormat("yyyy/MM/dd").create();
    }


}