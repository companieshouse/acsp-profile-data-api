package uk.gov.companieshouse.acspprofile.api.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import uk.gov.companieshouse.acspprofile.api.service.AcspGetProcessor;
import uk.gov.companieshouse.api.acspprofile.AcspFullProfile;
import uk.gov.companieshouse.api.acspprofile.AcspProfile;

@ExtendWith(MockitoExtension.class)
class AcspControllerTest {

    private static final String ACSP_NUMBER = "AP123456";

    @InjectMocks
    private AcspController controller;
    @Mock
    private AcspGetProcessor getProcessor;

    @Mock
    private AcspProfile expectedProfile;
    @Mock
    private AcspFullProfile expectedFullProfile;

    @Test
    void shouldGetProfile() {
        // given
        when(getProcessor.getProfile(any())).thenReturn(expectedProfile);

        // when
        ResponseEntity<AcspProfile> actual = controller.getProfile(ACSP_NUMBER);

        // then
        assertEquals(HttpStatus.OK, actual.getStatusCode());
        assertEquals(expectedProfile, actual.getBody());
        verify(getProcessor).getProfile(ACSP_NUMBER);
    }

    @Test
    void shouldGetFullProfile() {
        // given
        when(getProcessor.getFullProfile(any())).thenReturn(expectedFullProfile);

        // when
        ResponseEntity<AcspFullProfile> actual = controller.getFullProfile(ACSP_NUMBER);

        // then
        assertEquals(HttpStatus.OK, actual.getStatusCode());
        assertEquals(expectedFullProfile, actual.getBody());
        verify(getProcessor).getFullProfile(ACSP_NUMBER);
    }
}