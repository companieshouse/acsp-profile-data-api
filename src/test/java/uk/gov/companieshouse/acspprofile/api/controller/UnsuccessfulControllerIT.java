package uk.gov.companieshouse.acspprofile.api.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static uk.gov.companieshouse.acspprofile.api.auth.AuthConstants.ERIC_AUTHORISED_KEY_PRIVILEGES_HEADER;
import static uk.gov.companieshouse.acspprofile.api.auth.AuthConstants.ERIC_IDENTITY;
import static uk.gov.companieshouse.acspprofile.api.auth.AuthConstants.ERIC_IDENTITY_TYPE;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.dao.TypeMismatchDataAccessException;
import org.springframework.test.web.servlet.MockMvc;
import uk.gov.companieshouse.acspprofile.api.repository.AcspMongoRepository;

@AutoConfigureMockMvc
@SpringBootTest
class UnsuccessfulControllerIT {

    private static final String GET_PROFILE_URI = "/authorised-corporate-service-providers/{acsp_number}";
    private static final String GET_FULL_PROFILE_URI = "/authorised-corporate-service-providers/{acsp_number}/full-profile";

    private static final String ACSP_NUMBER = "AP123456";
    private static final String UPDATED_AT = "2024-09-24T12:00:00Z";

    @MockBean
    private AcspMongoRepository repository;
    @Autowired
    private MockMvc mockMvc;

    @Test
    void shouldReturn401WhenGetProfileWithoutAuthentication() throws Exception {
        mockMvc.perform(get(GET_PROFILE_URI, ACSP_NUMBER))
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }

    @Test
    void shouldReturn403WhenGetProfileWithWrongAuthorisation() throws Exception {
        mockMvc.perform(get(GET_PROFILE_URI, ACSP_NUMBER)
                        .header(ERIC_IDENTITY, "123")
                        .header(ERIC_IDENTITY_TYPE, "key")
                        .header(ERIC_AUTHORISED_KEY_PRIVILEGES_HEADER, "sensitive-data"))
                .andDo(print())
                .andExpect(status().isForbidden());
    }

    @Test
    void shouldReturn404WhenGetProfileAndNoDocumentExists() throws Exception {
        mockMvc.perform(get(GET_PROFILE_URI, ACSP_NUMBER)
                        .header("ERIC-Identity", "123")
                        .header("ERIC-Identity-Type", "key")
                        .header("ERIC-Authorised-Key-Privileges", "internal-app")
                        .header("X-Request-Id", UPDATED_AT))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldReturn502WhenMongoDBError() throws Exception {
        when(repository.findById(any())).thenThrow(TypeMismatchDataAccessException.class);

        mockMvc.perform(get(GET_PROFILE_URI, ACSP_NUMBER)
                        .header("ERIC-Identity", "123")
                        .header("ERIC-Identity-Type", "key")
                        .header("ERIC-Authorised-Key-Privileges", "internal-app")
                        .header("X-Request-Id", UPDATED_AT))
                .andExpect(status().isBadGateway());
    }

    @Test
    void shouldReturn403WhenGetFullProfileWithWrongAuthorisation() throws Exception {
        mockMvc.perform(get(GET_FULL_PROFILE_URI, ACSP_NUMBER)
                        .header(ERIC_IDENTITY, "123")
                        .header(ERIC_IDENTITY_TYPE, "key")
                        .header(ERIC_AUTHORISED_KEY_PRIVILEGES_HEADER, "internal-app"))
                .andDo(print())
                .andExpect(status().isForbidden());
    }
}
