package uk.gov.companieshouse.acspprofile.api.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.function.Executable;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import uk.gov.companieshouse.acspprofile.api.exception.NotFoundException;
import uk.gov.companieshouse.acspprofile.api.mapper.get.AcspGetMapper;
import uk.gov.companieshouse.acspprofile.api.model.AcspProfileDocument;
import uk.gov.companieshouse.api.acspprofile.AcspFullProfile;

@ExtendWith(MockitoExtension.class)
class AcspGetProcessorTest {

    private static final String ACSP_NUMBER = "AP123456";

    @InjectMocks
    private AcspGetProcessor getProcessor;
    @Mock
    private AcspService service;
    @Mock
    private AcspGetMapper mapper;

    @Mock
    private AcspProfileDocument document;
    @Mock
    private AcspFullProfile expectedFullProfile;

    @Test
    void shouldNotGetProfileWhenNotImplemented() {
        // given

        // when
        Object actual = getProcessor.getProfile(ACSP_NUMBER);

        // then
        assertNull(actual);
        verifyNoInteractions(service);
        verifyNoInteractions(mapper);
    }

    @Test
    void shouldGetFullProfile() {
        // given
        when(service.findAcsp(any())).thenReturn(Optional.of(document));
        when(mapper.mapFullProfile(any())).thenReturn(expectedFullProfile);

        // when
        Object actual = getProcessor.getFullProfile(ACSP_NUMBER);

        // then
        assertEquals(expectedFullProfile, actual);
        verify(service).findAcsp(ACSP_NUMBER);
        verify(mapper).mapFullProfile(document);
    }

    @Test
    void shouldThrowNotFoundExceptionWhenGetFullProfileEmptyOptional() {
        // given
        when(service.findAcsp(any())).thenReturn(Optional.empty());

        // when
        Executable actual = () -> getProcessor.getFullProfile(ACSP_NUMBER);

        // then
        Exception exception = assertThrows(NotFoundException.class, actual);
        assertEquals("ACSP profile not found", exception.getMessage());
        verify(service).findAcsp(ACSP_NUMBER);
        verifyNoMoreInteractions(mapper);
    }
}