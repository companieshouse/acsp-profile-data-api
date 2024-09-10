package uk.gov.companieshouse.acspprofile.api.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.dao.TypeMismatchDataAccessException;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import uk.gov.companieshouse.acspprofile.api.repository.Repository;

@AutoConfigureMockMvc
@SpringBootTest
class UnsuccessfulControllerIT {

    private static final String GET_PROFILE_URI = "/authorised-corporate-service-providers/{acsp_number}";

    private static final String ACSP_NUMBER = "AP123456";
    private static final String UPDATED_AT = "2024-09-24T12:00:00Z";

    @MockBean
    private Repository repository;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;


    @Test
    void shouldNotGetAcspProfileAndReturn404NotFound() throws Exception {
        // given

        // when
        ResultActions result = mockMvc.perform(get(GET_PROFILE_URI, ACSP_NUMBER)
                .header("ERIC-Identity", "123")
                .header("ERIC-Identity-Type", "key")
                .header("ERIC-Authorised-Key-Privileges", "internal-app")
                .header("X-Request-Id", UPDATED_AT));

        // then
        result.andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    void shouldCatchMongoExceptionAndReturn502BadGateway() throws Exception {
        // given
        when(repository.findById(any())).thenThrow(TypeMismatchDataAccessException.class);

        // when
        ResultActions result = mockMvc.perform(get(GET_PROFILE_URI, ACSP_NUMBER)
                .header("ERIC-Identity", "123")
                .header("ERIC-Identity-Type", "key")
                .header("ERIC-Authorised-Key-Privileges", "internal-app")
                .header("X-Request-Id", UPDATED_AT));

        // then
        result.andExpect(MockMvcResultMatchers.status().isBadGateway());
    }
}
