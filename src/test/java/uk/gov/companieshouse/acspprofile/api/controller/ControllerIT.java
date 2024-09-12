package uk.gov.companieshouse.acspprofile.api.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.function.Executable;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.shaded.org.apache.commons.io.IOUtils;
import uk.gov.companieshouse.acspprofile.api.model.AcspProfileDocument;
import uk.gov.companieshouse.api.acspprofile.AcspProfile;

@Testcontainers
@AutoConfigureMockMvc
@SpringBootTest
class ControllerIT {

    @Container
    private static final MongoDBContainer mongoDBContainer = new MongoDBContainer("mongo:5.0.12");
    private static final String ACSP_PROFILE_COLLECTION = "acsp_profile";
    private static final String GET_PROFILE_URI = "/authorised-corporate-service-providers/{acsp_number}";
    private static final String GET_FULL_PROFILE_URI = "/authorised-corporate-service-providers/{acsp_number}/full-profile";
    private static final String ACSP_NUMBER = "AP123456";
    private static final String CREATED_AT = "2024-08-23T00:00:00Z";
    private static final String UPDATED_AT = "2024-09-24T12:00:00Z";

    @Autowired
    private MongoTemplate mongoTemplate;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @DynamicPropertySource
    static void setProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.data.mongodb.uri", mongoDBContainer::getReplicaSetUrl);
    }

    @BeforeEach
    void setUp() {
        mongoTemplate.dropCollection(ACSP_PROFILE_COLLECTION);
        mongoTemplate.createCollection(ACSP_PROFILE_COLLECTION);
    }

    @ParameterizedTest
    @CsvSource({
            "limited-company",
            "sole-trader",
    })
    void shouldReturn200WhenGetProfile(String acspType) throws Exception {
        // given
        insertDocumentByType(acspType);

        AcspProfile expected = getExpectedResponseByType(acspType);

        // when
        ResultActions result = mockMvc.perform(get(GET_PROFILE_URI, ACSP_NUMBER)
                .header("ERIC-Identity", "123")
                .header("ERIC-Identity-Type", "key")
                .header("ERIC-Authorised-Key-Privileges", "internal-app")
                .header("X-Request-Id", UPDATED_AT));

        // then
        result.andExpect(MockMvcResultMatchers.status().isOk());

        String actualJson = result.andReturn().getResponse().getContentAsString();
        AcspProfile actual = objectMapper.readValue(actualJson, AcspProfile.class);

        assertEquals(expected, actual);
    }

    @ParameterizedTest
    @CsvSource({
            "limited-company",
            "sole-trader",
    })
    void shouldReturn500WhenGetFullProfileAndNotImplemented(String acspType) throws Exception {
        // given
        insertDocumentByType(acspType);

        // when
        Executable result = () -> mockMvc.perform(get(GET_FULL_PROFILE_URI, ACSP_NUMBER)
                .header("ERIC-Identity", "123")
                .header("ERIC-Identity-Type", "key")
                .header("ERIC-Authorised-Key-Privileges", "sensitive-data")
                .header("X-Request-Id", UPDATED_AT));

        // then
        assertThrows(ServletException.class, result);
    }

    private AcspProfile getExpectedResponseByType(String acspType) throws IOException {
        String expectedJson = IOUtils.resourceToString("/responses/%s-acsp-profile-response.json".formatted(acspType),
                        StandardCharsets.UTF_8)
                .replaceAll("<acsp_number>", ACSP_NUMBER);
        return objectMapper.readValue(expectedJson, AcspProfile.class);
    }

    private void insertDocumentByType(String acspType) throws IOException {
        String documentJson = IOUtils.resourceToString("/mongo/%s-acsp-document.json".formatted(acspType),
                        StandardCharsets.UTF_8)
                .replaceAll("<acsp_number>", ACSP_NUMBER)
                .replaceAll("<created_at>", CREATED_AT)
                .replaceAll("<updated_at>", UPDATED_AT);
        AcspProfileDocument document = objectMapper.readValue(documentJson, AcspProfileDocument.class);
        mongoTemplate.insert(document);
    }
}
