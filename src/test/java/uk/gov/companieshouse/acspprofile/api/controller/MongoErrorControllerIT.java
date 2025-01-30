package uk.gov.companieshouse.acspprofile.api.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static uk.gov.companieshouse.acspprofile.api.auth.AuthConstants.API_KEY_IDENTITY_TYPE;
import static uk.gov.companieshouse.acspprofile.api.auth.AuthConstants.ERIC_AUTHORISED_KEY_PRIVILEGES_HEADER;
import static uk.gov.companieshouse.acspprofile.api.auth.AuthConstants.ERIC_IDENTITY;
import static uk.gov.companieshouse.acspprofile.api.auth.AuthConstants.ERIC_IDENTITY_TYPE;
import static uk.gov.companieshouse.acspprofile.api.auth.AuthConstants.INTERNAL_APP_PRIVILEGE;
import static uk.gov.companieshouse.acspprofile.api.auth.AuthConstants.SENSITIVE_DATA_PRIVILEGE;

import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.TypeMismatchDataAccessException;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import uk.gov.companieshouse.acspprofile.api.model.AcspProfileDocument;
import uk.gov.companieshouse.acspprofile.api.repository.AcspMongoRepository;
import uk.gov.companieshouse.api.acspprofile.InternalAcspApi;

class MongoErrorControllerIT extends AbstractControllerIT {

    @MockitoBean
    private AcspMongoRepository repository;
    @Autowired
    private MockMvc mockMvc;

    @Test
    void shouldReturn502WhenGetProfileAndMongoDBError() throws Exception {
        when(repository.findById(any())).thenThrow(TypeMismatchDataAccessException.class);

        mockMvc.perform(get(GET_PROFILE_URI, ACSP_NUMBER)
                        .header(ERIC_IDENTITY, ERIC_IDENTITY_VALUE)
                        .header(ERIC_IDENTITY_TYPE, API_KEY_IDENTITY_TYPE)
                        .header(ERIC_AUTHORISED_KEY_PRIVILEGES_HEADER, INTERNAL_APP_PRIVILEGE)
                        .header(REQUEST_ID_HEADER, CONTEXT_ID))
                .andExpect(status().isBadGateway());
    }

    @Test
    void shouldReturn502WhenGetFullProfileAndMongoDBError() throws Exception {
        when(repository.findById(any())).thenThrow(TypeMismatchDataAccessException.class);

        mockMvc.perform(get(GET_FULL_PROFILE_URI, ACSP_NUMBER)
                        .header(ERIC_IDENTITY, ERIC_IDENTITY_VALUE)
                        .header(ERIC_IDENTITY_TYPE, API_KEY_IDENTITY_TYPE)
                        .header(ERIC_AUTHORISED_KEY_PRIVILEGES_HEADER, SENSITIVE_DATA_PRIVILEGE)
                        .header(REQUEST_ID_HEADER, CONTEXT_ID))
                .andExpect(status().isBadGateway());
    }

    @Test
    void shouldReturn502WhenWhenPutAcspInternalAndMongoDBErrorDuringFind() throws Exception {
        InternalAcspApi request = getInternalAcspApi("limited-company", DELTA_AT, CONTEXT_ID);

        when(repository.findById(any())).thenThrow(TypeMismatchDataAccessException.class);

        mockMvc.perform(put(PUT_ACSP_URI, ACSP_NUMBER)
                        .header(ERIC_IDENTITY, ERIC_IDENTITY_VALUE)
                        .header(ERIC_IDENTITY_TYPE, API_KEY_IDENTITY_TYPE)
                        .header(ERIC_AUTHORISED_KEY_PRIVILEGES_HEADER, INTERNAL_APP_PRIVILEGE)
                        .header(REQUEST_ID_HEADER, CONTEXT_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadGateway());
    }

    @Test
    void shouldReturn502WhenWhenPutAcspInternalAndMongoDBErrorDuringInsert() throws Exception {
        InternalAcspApi request = getInternalAcspApi("limited-company", DELTA_AT, CONTEXT_ID);

        when(repository.findById(any())).thenReturn(Optional.empty());
        when(repository.insert(any(AcspProfileDocument.class))).thenThrow(TypeMismatchDataAccessException.class);

        mockMvc.perform(put(PUT_ACSP_URI, ACSP_NUMBER)
                        .header(ERIC_IDENTITY, ERIC_IDENTITY_VALUE)
                        .header(ERIC_IDENTITY_TYPE, API_KEY_IDENTITY_TYPE)
                        .header(ERIC_AUTHORISED_KEY_PRIVILEGES_HEADER, INTERNAL_APP_PRIVILEGE)
                        .header(REQUEST_ID_HEADER, CONTEXT_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadGateway());
    }
}
