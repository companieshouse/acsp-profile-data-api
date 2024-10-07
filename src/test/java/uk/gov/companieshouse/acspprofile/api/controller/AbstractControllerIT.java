package uk.gov.companieshouse.acspprofile.api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.shaded.org.apache.commons.io.IOUtils;
import uk.gov.companieshouse.api.acspprofile.InternalAcspApi;

@AutoConfigureMockMvc
@SpringBootTest
abstract class AbstractControllerIT {

    static final String GET_PROFILE_URI = "/authorised-corporate-service-providers/{acsp_number}";
    static final String GET_FULL_PROFILE_URI = "/authorised-corporate-service-providers/{acsp_number}/full-profile";
    static final String PUT_ACSP_URI = "/authorised-corporate-service-providers/{acsp_number}/internal";
    static final String ACSP_NUMBER = "AP123456";
    static final String CONTEXT_ID = "context_id";
    static final String DELTA_AT = "20241003085145522153";
    static final String REQUEST_ID_HEADER = "X-Request-Id";
    static final String ERIC_IDENTITY_VALUE = "123";

    @Autowired
    MockMvc mockMvc;
    @Autowired
    ObjectMapper objectMapper;

    InternalAcspApi getInternalAcspApi(String acspType, String deltaAt, String updatedBy) throws IOException {
        String expectedJson = IOUtils.resourceToString("/requests/%s-acsp-internal-request.json".formatted(acspType),
                        StandardCharsets.UTF_8)
                .replaceAll("<acsp_number>", ACSP_NUMBER)
                .replaceAll("<delta_at>", deltaAt)
                .replaceAll("<updated_by>", updatedBy);
        return objectMapper.readValue(expectedJson, InternalAcspApi.class);
    }
}
