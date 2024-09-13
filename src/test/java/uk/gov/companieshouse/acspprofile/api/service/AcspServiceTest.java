package uk.gov.companieshouse.acspprofile.api.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
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
import uk.gov.companieshouse.acspprofile.api.repository.AcspRepository;
import uk.gov.companieshouse.api.acspprofile.AcspFullProfile;
import uk.gov.companieshouse.api.acspprofile.AcspProfile;

@ExtendWith(MockitoExtension.class)
class AcspServiceTest {

    private static final String ACSP_NUMBER = "AP123456";

    @InjectMocks
    private AcspService getProcessor;
    @Mock
    private AcspRepository service;
    @Mock
    private AcspGetMapper mapper;

    @Mock
    private AcspProfileDocument document;
    @Mock
    private AcspProfile expectedProfile;
    @Mock
    private AcspFullProfile expectedFullProfile;

    @Test
    void shouldGetProfile() {
        // given
        when(service.findAcsp(any())).thenReturn(Optional.of(document));
        when(mapper.mapProfile(any())).thenReturn(expectedProfile);

        // when
        Object actual = getProcessor.getProfile(ACSP_NUMBER);

        // then
        assertEquals(expectedProfile, actual);
        verify(service).findAcsp(ACSP_NUMBER);
        verify(mapper).mapProfile(document);
    }

    @Test
    void shouldThrowNotFoundExceptionWhenGetProfileEmptyOptional() {
        // given
        when(service.findAcsp(any())).thenReturn(Optional.empty());

        // when
        Executable actual = () -> getProcessor.getProfile(ACSP_NUMBER);

        // then
        Exception exception = assertThrows(NotFoundException.class, actual);
        assertEquals("ACSP document not found", exception.getMessage());
        verify(service).findAcsp(ACSP_NUMBER);
        verifyNoMoreInteractions(mapper);
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
        assertEquals("ACSP document not found", exception.getMessage());
        verify(service).findAcsp(ACSP_NUMBER);
        verifyNoMoreInteractions(mapper);
    }
}