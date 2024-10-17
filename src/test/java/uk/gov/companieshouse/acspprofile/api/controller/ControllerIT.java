package uk.gov.companieshouse.acspprofile.api.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;
import static org.springframework.http.HttpHeaders.LOCATION;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static uk.gov.companieshouse.acspprofile.api.auth.AuthConstants.API_KEY_IDENTITY_TYPE;
import static uk.gov.companieshouse.acspprofile.api.auth.AuthConstants.ERIC_AUTHORISED_KEY_PRIVILEGES_HEADER;
import static uk.gov.companieshouse.acspprofile.api.auth.AuthConstants.ERIC_IDENTITY;
import static uk.gov.companieshouse.acspprofile.api.auth.AuthConstants.ERIC_IDENTITY_TYPE;
import static uk.gov.companieshouse.acspprofile.api.auth.AuthConstants.INTERNAL_APP_PRIVILEGE;
import static uk.gov.companieshouse.acspprofile.api.auth.AuthConstants.SENSITIVE_DATA_PRIVILEGE;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
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
class ControllerIT extends AbstractControllerIT {

    private static final String ACSP_PROFILE_COLLECTION = "acsp_profile";
    private static final String UPDATED_CONTEXT_ID = "updated_context_id";
    private static final String CREATED_AT = "2024-08-23T00:00:00Z";
    private static final String UPDATED_AT = "2024-09-24T12:00:00Z";
    private static final String UPDATED_DELTA_AT = "20241003093217479012";
    private static final String STALE_DELTA_AT = "20230222125145522153";
    private static final String SELF_LINK = "/authorised-corporate-service-providers/%s".formatted(ACSP_NUMBER);

    @Container
    private static final MongoDBContainer mongoDBContainer = new MongoDBContainer("mongo:5.0.12");

    @Autowired
    private MongoTemplate mongoTemplate;

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
                .header(ERIC_IDENTITY, ERIC_IDENTITY_VALUE)
                .header(ERIC_IDENTITY_TYPE, API_KEY_IDENTITY_TYPE)
                .header(ERIC_AUTHORISED_KEY_PRIVILEGES_HEADER, INTERNAL_APP_PRIVILEGE)
                .header(REQUEST_ID_HEADER, CONTEXT_ID));

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
                .header(ERIC_IDENTITY, ERIC_IDENTITY_VALUE)
                .header(ERIC_IDENTITY_TYPE, API_KEY_IDENTITY_TYPE)
                .header(ERIC_AUTHORISED_KEY_PRIVILEGES_HEADER, SENSITIVE_DATA_PRIVILEGE)
                .header(REQUEST_ID_HEADER, CONTEXT_ID));

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

        AcspProfileDocument expected = getExpectedAcspDocument(acspType, CREATED_AT, CONTEXT_ID, DELTA_AT, 0L);

        when(instantSupplier.get()).thenReturn(Instant.parse(CREATED_AT));

        // when
        ResultActions result = mockMvc.perform(put(PUT_ACSP_URI, ACSP_NUMBER)
                .header(ERIC_IDENTITY, ERIC_IDENTITY_VALUE)
                .header(ERIC_IDENTITY_TYPE, API_KEY_IDENTITY_TYPE)
                .header(ERIC_AUTHORISED_KEY_PRIVILEGES_HEADER, INTERNAL_APP_PRIVILEGE)
                .header(REQUEST_ID_HEADER, CONTEXT_ID)
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
                UPDATED_DELTA_AT, 1L);

        when(instantSupplier.get()).thenReturn(Instant.parse(UPDATED_AT));

        // when
        ResultActions result = mockMvc.perform(put(PUT_ACSP_URI, ACSP_NUMBER)
                .header(ERIC_IDENTITY, ERIC_IDENTITY_VALUE)
                .header(ERIC_IDENTITY_TYPE, API_KEY_IDENTITY_TYPE)
                .header(ERIC_AUTHORISED_KEY_PRIVILEGES_HEADER, INTERNAL_APP_PRIVILEGE)
                .header(REQUEST_ID_HEADER, UPDATED_CONTEXT_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)));

        // then
        result.andExpect(MockMvcResultMatchers.status().isOk());
        result.andExpect(MockMvcResultMatchers.header().string(LOCATION, SELF_LINK));

        AcspProfileDocument actual = mongoTemplate.findById(ACSP_NUMBER, AcspProfileDocument.class);
        assertEquals(expected, actual);
    }

    @Test
    void shouldReturn401WhenGetProfileWithoutAuthentication() throws Exception {
        mockMvc.perform(get(GET_PROFILE_URI, ACSP_NUMBER))
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }

    @Test
    void shouldReturn403WhenGetProfileWithWrongAuthorisation() throws Exception {
        mockMvc.perform(get(GET_PROFILE_URI, ACSP_NUMBER)
                        .header(ERIC_IDENTITY, ERIC_IDENTITY_VALUE)
                        .header(ERIC_IDENTITY_TYPE, API_KEY_IDENTITY_TYPE)
                        .header(ERIC_AUTHORISED_KEY_PRIVILEGES_HEADER, SENSITIVE_DATA_PRIVILEGE))
                .andDo(print())
                .andExpect(status().isForbidden());
    }

    @Test
    void shouldReturn404WhenGetProfileAndNoDocumentExists() throws Exception {
        mockMvc.perform(get(GET_PROFILE_URI, ACSP_NUMBER)
                        .header(ERIC_IDENTITY, ERIC_IDENTITY_VALUE)
                        .header(ERIC_IDENTITY_TYPE, API_KEY_IDENTITY_TYPE)
                        .header(ERIC_AUTHORISED_KEY_PRIVILEGES_HEADER, INTERNAL_APP_PRIVILEGE)
                        .header(REQUEST_ID_HEADER, CONTEXT_ID))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldReturn401WhenGetFullProfileWithoutAuthentication() throws Exception {
        mockMvc.perform(get(GET_FULL_PROFILE_URI, ACSP_NUMBER))
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }

    @Test
    void shouldReturn403WhenGetFullProfileWithWrongAuthorisation() throws Exception {
        mockMvc.perform(get(GET_FULL_PROFILE_URI, ACSP_NUMBER)
                        .header(ERIC_IDENTITY, ERIC_IDENTITY_VALUE)
                        .header(ERIC_IDENTITY_TYPE, API_KEY_IDENTITY_TYPE)
                        .header(ERIC_AUTHORISED_KEY_PRIVILEGES_HEADER, INTERNAL_APP_PRIVILEGE))
                .andDo(print())
                .andExpect(status().isForbidden());
    }

    @Test
    void shouldReturn404WhenGetFullProfileAndNoDocumentExists() throws Exception {
        mockMvc.perform(get(GET_FULL_PROFILE_URI, ACSP_NUMBER)
                        .header(ERIC_IDENTITY, ERIC_IDENTITY_VALUE)
                        .header(ERIC_IDENTITY_TYPE, API_KEY_IDENTITY_TYPE)
                        .header(ERIC_AUTHORISED_KEY_PRIVILEGES_HEADER, SENSITIVE_DATA_PRIVILEGE)
                        .header(REQUEST_ID_HEADER, CONTEXT_ID))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldReturn400WhenWhenPutAcspInternalAndMissingRequiredFields() throws Exception {
        InternalAcspApi request = getInternalAcspApi("missing-required-fields", STALE_DELTA_AT, CONTEXT_ID);

        mockMvc.perform(put(PUT_ACSP_URI, ACSP_NUMBER)
                        .header(ERIC_IDENTITY, ERIC_IDENTITY_VALUE)
                        .header(ERIC_IDENTITY_TYPE, API_KEY_IDENTITY_TYPE)
                        .header(ERIC_AUTHORISED_KEY_PRIVILEGES_HEADER, INTERNAL_APP_PRIVILEGE)
                        .header(REQUEST_ID_HEADER, CONTEXT_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldReturn401WhenPutAcspInternalWithoutAuthentication() throws Exception {
        InternalAcspApi request = getInternalAcspApi("limited-company", DELTA_AT, CONTEXT_ID);

        mockMvc.perform(put(PUT_ACSP_URI, ACSP_NUMBER)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }

    @Test
    void shouldReturn403WhenWhenPutAcspInternalWithWrongAuthorisation() throws Exception {
        InternalAcspApi request = getInternalAcspApi("limited-company", DELTA_AT, CONTEXT_ID);

        mockMvc.perform(put(PUT_ACSP_URI, ACSP_NUMBER)
                        .header(ERIC_IDENTITY, ERIC_IDENTITY_VALUE)
                        .header(ERIC_IDENTITY_TYPE, API_KEY_IDENTITY_TYPE)
                        .header(ERIC_AUTHORISED_KEY_PRIVILEGES_HEADER, "invalid auth")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andDo(print())
                .andExpect(status().isForbidden());
    }

    @Test
    void shouldReturn409WhenWhenPutAcspInternalAndStaleDelta() throws Exception {
        insertDocumentByType("limited-company");

        InternalAcspApi request = getInternalAcspApi("limited-company", STALE_DELTA_AT, CONTEXT_ID);

        mockMvc.perform(put(PUT_ACSP_URI, ACSP_NUMBER)
                        .header(ERIC_IDENTITY, ERIC_IDENTITY_VALUE)
                        .header(ERIC_IDENTITY_TYPE, API_KEY_IDENTITY_TYPE)
                        .header(ERIC_AUTHORISED_KEY_PRIVILEGES_HEADER, INTERNAL_APP_PRIVILEGE)
                        .header(REQUEST_ID_HEADER, CONTEXT_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isConflict());
    }

    private void insertDocumentByType(String acspType) throws IOException {
        String documentJson = IOUtils.resourceToString("/mongo/%s-acsp-document.json".formatted(acspType),
                        StandardCharsets.UTF_8)
                .replaceAll("<acsp_number>", ACSP_NUMBER)
                .replaceAll("\"<version>\"", "0")
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

    private AcspProfileDocument getExpectedAcspDocument(String acspType, String updatedAt, String updatedBy,
            String deltaAt, Long version) throws IOException {
        String expectedJson = IOUtils.resourceToString("/mongo/%s-acsp-document.json".formatted(acspType),
                        StandardCharsets.UTF_8)
                .replaceAll("<acsp_number>", ACSP_NUMBER)
                .replaceAll("\"<version>\"", version.toString())
                .replaceAll("<delta_at>", deltaAt)
                .replaceAll("<created_at>", CREATED_AT)
                .replaceAll("<created_by>", CONTEXT_ID)
                .replaceAll("<updated_at>", updatedAt)
                .replaceAll("<updated_by>", updatedBy);
        return objectMapper.readValue(expectedJson, AcspProfileDocument.class);
    }
}
