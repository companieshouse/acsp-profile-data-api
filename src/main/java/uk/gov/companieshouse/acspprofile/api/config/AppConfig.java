package uk.gov.companieshouse.acspprofile.api.config;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.text.SimpleDateFormat;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import tools.jackson.databind.DeserializationFeature;
import tools.jackson.databind.SerializationFeature;
import tools.jackson.databind.json.JsonMapper;
import tools.jackson.databind.module.SimpleModule;
import uk.gov.companieshouse.acspprofile.api.serdes.EmptyFieldDeserializer;

@Configuration
public class AppConfig {

    @Bean
    public JsonMapper jacksonJsonMapper() {
        return JsonMapper.builder()
                .addModule(new SimpleModule().addDeserializer(String.class,
                        new EmptyFieldDeserializer()))
                .defaultDateFormat(new SimpleDateFormat("yyyy-MM-dd"))
                .changeDefaultPropertyInclusion(incl ->
                        incl.withValueInclusion(JsonInclude.Include.NON_NULL))
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
                .configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false)
                .build();
    }
}
