package za.co.example.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.threetenbp.ThreeTenModule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.threeten.bp.LocalDate;

@Configuration
public class JacksonConfig {

    @Bean
    public ObjectMapper objectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();

        objectMapper.registerModule(new ThreeTenModule());

        objectMapper.registerModule(new JavaTimeModule());

        SimpleModule customModule = new SimpleModule();
        customModule.addSerializer(LocalDate.class, new LocalDateSerializer());
        customModule.addDeserializer(LocalDate.class, new LocalDateDeserializer());

        objectMapper.registerModule(customModule);

        return objectMapper;
    }
}
