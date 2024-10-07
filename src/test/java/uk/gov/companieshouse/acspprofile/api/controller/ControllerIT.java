package uk.gov.companieshouse.acspprofile.api.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;
import static org.springframework.http.HttpHeaders.LOCATION;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.shaded.org.apache.commons.io.IOUtils;
import uk.gov.companieshouse.acspprofile.api.mapper.InstantSupplier;
import uk.gov.companieshouse.acspprofile.api.model.AcspProfileDocument;
import uk.gov.companieshouse.api.acspprofile.AcspFullProfile;
import uk.gov.companieshouse.api.acspprofile.AcspProfile;
import uk.gov.companieshouse.api.acspprofile.InternalAcspApi;

@Testcontainers
@AutoConfigureMockMvc
@SpringBootTest
class ControllerIT {

    @Container
    private static final MongoDBContainer mongoDBContainer = new MongoDBContainer("mongo:5.0.12");
    private static final String ACSP_PROFILE_COLLECTION = "acsp_profile";
    private static final String GET_PROFILE_URI = "/authorised-corporate-service-providers/{acsp_number}";
    private static final String GET_FULL_PROFILE_URI = "/authorised-corporate-service-providers/{acsp_number}/full-profile";
    private static final String PUT_ACSP_URI = "/authorised-corporate-service-providers/{acsp_number}/internal";
    private static final String ACSP_NUMBER = "AP123456";
    private static final String CREATED_AT = "2024-08-23T00:00:00Z";
    private static final String UPDATED_AT = "2024-09-24T12:00:00Z";
    private static final String CONTEXT_ID = "context_id";
    private static final String UPDATED_CONTEXT_ID = "updated_context_id";
    private static final String SELF_LINK = "/authorised-corporate-service-providers/%s".formatted(ACSP_NUMBER);
    private static final String DELTA_AT = "20241003085145522153";
    private static final String UPDATED_DELTA_AT = "20241003093217479012";

    @Autowired
    private MongoTemplate mongoTemplate;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private InstantSupplier instantSupplier;

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

        AcspProfile expected = getExpectedProfileByType(acspType);

        // when
        ResultActions result = mockMvc.perform(get(GET_PROFILE_URI, ACSP_NUMBER)
                .header("ERIC-Identity", "123")
                .header("ERIC-Identity-Type", "key")
                .header("ERIC-Authorised-Key-Privileges", "internal-app")
                .header("X-Request-Id", CONTEXT_ID));

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
    void shouldReturn200WhenGetFullProfile(String acspType) throws Exception {
        // given
        insertDocumentByType(acspType);

        AcspFullProfile expected = getExpectedFullProfileByType(acspType);
        // when
        ResultActions result = mockMvc.perform(get(GET_FULL_PROFILE_URI, ACSP_NUMBER)
                .header("ERIC-Identity", "123")
                .header("ERIC-Identity-Type", "key")
                .header("ERIC-Authorised-Key-Privileges", "sensitive-data")
                .header("X-Request-Id", CONTEXT_ID));

        // then
        result.andExpect(MockMvcResultMatchers.status().isOk());

        String actualJson = result.andReturn().getResponse().getContentAsString();
        AcspFullProfile actual = objectMapper.readValue(actualJson, AcspFullProfile.class);

        assertEquals(expected, actual);
    }

    @ParameterizedTest
    @CsvSource({
            "limited-company",
            "sole-trader",
    })
    void shouldReturn200WhenInsertAcspInternal(String acspType) throws Exception {
        // given
        InternalAcspApi request = getInternalAcspApi(acspType, DELTA_AT, CONTEXT_ID);

        AcspProfileDocument expected = getExpectedAcspDocument(acspType, CREATED_AT, CONTEXT_ID, DELTA_AT);

        when(instantSupplier.get()).thenReturn(Instant.parse(CREATED_AT));

        // when
        ResultActions result = mockMvc.perform(put(PUT_ACSP_URI, ACSP_NUMBER)
                .header("ERIC-Identity", "123")
                .header("ERIC-Identity-Type", "key")
                .header("ERIC-Authorised-Key-Privileges", "internal-app")
                .header("X-Request-Id", CONTEXT_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)));

        // then
        result.andExpect(MockMvcResultMatchers.status().isOk());
        result.andExpect(MockMvcResultMatchers.header().string(LOCATION, SELF_LINK));

        AcspProfileDocument actual = mongoTemplate.findById(ACSP_NUMBER, AcspProfileDocument.class);
        assertEquals(expected, actual);
    }

    @ParameterizedTest
    @CsvSource({
            "limited-company",
            "sole-trader",
    })
    void shouldReturn200WhenUpdateAcspInternal(String acspType) throws Exception {
        // given
        insertDocumentByType(acspType);

        InternalAcspApi request = getInternalAcspApi(acspType, UPDATED_DELTA_AT, UPDATED_CONTEXT_ID);

        AcspProfileDocument expected = getExpectedAcspDocument(acspType, UPDATED_AT, UPDATED_CONTEXT_ID,
                UPDATED_DELTA_AT);

        when(instantSupplier.get()).thenReturn(Instant.parse(UPDATED_AT));

        // when
        ResultActions result = mockMvc.perform(put(PUT_ACSP_URI, ACSP_NUMBER)
                .header("ERIC-Identity", "123")
                .header("ERIC-Identity-Type", "key")
                .header("ERIC-Authorised-Key-Privileges", "internal-app")
                .header("X-Request-Id", UPDATED_CONTEXT_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)));

        // then
        result.andExpect(MockMvcResultMatchers.status().isOk());
        result.andExpect(MockMvcResultMatchers.header().string(LOCATION, SELF_LINK));

        AcspProfileDocument actual = mongoTemplate.findById(ACSP_NUMBER, AcspProfileDocument.class);
        assertEquals(expected, actual);
    }

    private void insertDocumentByType(String acspType) throws IOException {
        String documentJson = IOUtils.resourceToString("/mongo/%s-acsp-document.json".formatted(acspType),
                        StandardCharsets.UTF_8)
                .replaceAll("<acsp_number>", ACSP_NUMBER)
                .replaceAll("<delta_at>", DELTA_AT)
                .replaceAll("<created_at>", CREATED_AT)
                .replaceAll("<created_by>", CONTEXT_ID)
                .replaceAll("<updated_at>", UPDATED_AT);
        AcspProfileDocument document = objectMapper.readValue(documentJson, AcspProfileDocument.class);
        mongoTemplate.insert(document);
    }

    private AcspProfile getExpectedProfileByType(String acspType) throws IOException {
        String expectedJson = IOUtils.resourceToString("/responses/%s-acsp-profile-response.json".formatted(acspType),
                        StandardCharsets.UTF_8)
                .replaceAll("<acsp_number>", ACSP_NUMBER);
        return objectMapper.readValue(expectedJson, AcspProfile.class);
    }

    private AcspFullProfile getExpectedFullProfileByType(String acspType) throws IOException {
        String expectedJson = IOUtils.resourceToString(
                        "/responses/%s-acsp-full-profile-response.json".formatted(acspType), StandardCharsets.UTF_8)
                .replaceAll("<acsp_number>", ACSP_NUMBER);
        return objectMapper.readValue(expectedJson, AcspFullProfile.class);
    }

    private InternalAcspApi getInternalAcspApi(String acspType, String deltaAt, String updatedBy) throws IOException {
        String expectedJson = IOUtils.resourceToString("/requests/%s-acsp-internal-request.json".formatted(acspType),
                        StandardCharsets.UTF_8)
                .replaceAll("<acsp_number>", ACSP_NUMBER)
                .replaceAll("<delta_at>", deltaAt)
                .replaceAll("<updated_by>", updatedBy);
        return objectMapper.readValue(expectedJson, InternalAcspApi.class);
    }

    private AcspProfileDocument getExpectedAcspDocument(String acspType, String updatedAt, String updatedBy,
            String deltaAt) throws IOException {
        String expectedJson = IOUtils.resourceToString("/mongo/%s-acsp-document.json".formatted(acspType),
                        StandardCharsets.UTF_8)
                .replaceAll("<acsp_number>", ACSP_NUMBER)
                .replaceAll("<delta_at>", deltaAt)
                .replaceAll("<created_at>", CREATED_AT)
                .replaceAll("<created_by>", CONTEXT_ID)
                .replaceAll("<updated_at>", updatedAt)
                .replaceAll("<updated_by>", updatedBy);
        return objectMapper.readValue(expectedJson, AcspProfileDocument.class);
    }
}
