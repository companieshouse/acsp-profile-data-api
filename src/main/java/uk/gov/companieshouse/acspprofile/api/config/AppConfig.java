package uk.gov.companieshouse.acspprofile.api.config;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.function.Supplier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import uk.gov.companieshouse.api.InternalApiClient;
import uk.gov.companieshouse.api.http.ApiKeyHttpClient;
import uk.gov.companieshouse.acspprofile.api.mapper.upsert.ChildListMapper;
import uk.gov.companieshouse.acspprofile.api.mapper.upsert.ChildMapper;
import uk.gov.companieshouse.acspprofile.api.model.mongo.ACSPProfileAnnotation;
import uk.gov.companieshouse.acspprofile.api.model.mongo.ACSPProfileAssociatedFiling;
import uk.gov.companieshouse.acspprofile.api.model.mongo.ACSPProfileResolution;
import uk.gov.companieshouse.acspprofile.api.serdes.EmptyFieldDeserializer;

@Configuration
public class AppConfig {

    @Bean
    public Supplier<Instant> instantSupplier() {
        return Instant::now;
    }

    @Bean
    public Supplier<InternalApiClient> internalApiClientSupplier(
            @Value("${api.api-key}") String apiKey,
            @Value("${api.api-url}") String apiUrl) {
        return () -> {
            InternalApiClient internalApiClient = new InternalApiClient(new ApiKeyHttpClient(
                    apiKey));
            internalApiClient.setBasePath(apiUrl);
            return internalApiClient;
        };
    }

    @Bean
    @Primary
    public ObjectMapper objectMapper() {
        return new ObjectMapper()
                .registerModule(new JavaTimeModule())
                .setDateFormat(new SimpleDateFormat("yyyy-MM-dd"))
                .registerModule(new SimpleModule().addDeserializer(String.class,
                        new EmptyFieldDeserializer()))
                .setSerializationInclusion(JsonInclude.Include.NON_NULL)
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
                .configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
    }

    @Bean
    public ChildListMapper<ACSPProfileAnnotation> annotationChildListMapper(
            ChildMapper<ACSPProfileAnnotation> childMapper) {
        return new ChildListMapper<>(childMapper);
    }

    @Bean
    public ChildListMapper<ACSPProfileAssociatedFiling> associatedFilingChildListMapper(
            ChildMapper<ACSPProfileAssociatedFiling> childMapper) {
        return new ChildListMapper<>(childMapper);
    }

    @Bean
    public ChildListMapper<ACSPProfileResolution> resolutionChildListMapper(
            ChildMapper<ACSPProfileResolution> childMapper) {
        return new ChildListMapper<>(childMapper);
    }
}
