package uk.gov.companieshouse.acspprofile.api.auth;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static uk.gov.companieshouse.acspprofile.api.auth.AuthConstants.ERIC_AUTHORISED_KEY_PRIVILEGES_HEADER;
import static uk.gov.companieshouse.acspprofile.api.auth.AuthConstants.ERIC_IDENTITY;
import static uk.gov.companieshouse.acspprofile.api.auth.AuthConstants.ERIC_IDENTITY_TYPE;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
class AuthIT {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void shouldReturn200FromGetHealthEndpoint() throws Exception {
        this.mockMvc.perform(get("/healthcheck"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string("{\"status\":\"UP\"}"));
    }

    @Test
    void shouldReturn401FromEndpointWithoutAuth() throws Exception {
        this.mockMvc.perform(get("/test"))
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }

    @Test
    void shouldPassAuthAndReturn404WithSensitiveDataPrivileges() throws Exception {
        this.mockMvc.perform(get("/test/full-profile")
                        .header(ERIC_IDENTITY, "123")
                        .header(ERIC_IDENTITY_TYPE, "key")
                        .header(ERIC_AUTHORISED_KEY_PRIVILEGES_HEADER, "sensitive-data"))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldFailAuthAndReturn403WithoutSensitiveDataPrivileges() throws Exception {
        this.mockMvc.perform(get("/test/full-profile")
                        .header(ERIC_IDENTITY, "123")
                        .header(ERIC_IDENTITY_TYPE, "key")
                        .header(ERIC_AUTHORISED_KEY_PRIVILEGES_HEADER, "internal-app"))
                .andDo(print())
                .andExpect(status().isForbidden());
    }
}