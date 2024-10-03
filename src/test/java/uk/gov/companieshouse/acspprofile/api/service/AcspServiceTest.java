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
import uk.gov.companieshouse.acspprofile.api.mapper.AcspResponseMapper;
import uk.gov.companieshouse.acspprofile.api.model.AcspProfileDocument;
import uk.gov.companieshouse.acspprofile.api.repository.AcspRepository;
import uk.gov.companieshouse.api.acspprofile.AcspFullProfile;
import uk.gov.companieshouse.api.acspprofile.AcspProfile;

@ExtendWith(MockitoExtension.class)
class AcspServiceTest {

    private static final String ACSP_NUMBER = "AP123456";

    @InjectMocks
    private AcspService service;
    @Mock
    private AcspRepository repository;
    @Mock
    private AcspResponseMapper responseMapper;

    @Mock
    private AcspProfileDocument document;
    @Mock
    private AcspProfile expectedProfile;
    @Mock
    private AcspFullProfile expectedFullProfile;

    @Test
    void shouldGetProfile() {
        // given
        when(repository.findAcsp(any())).thenReturn(Optional.of(document));
        when(responseMapper.mapProfile(any())).thenReturn(expectedProfile);

        // when
        Object actual = service.getProfile(ACSP_NUMBER);

        // then
        assertEquals(expectedProfile, actual);
        verify(repository).findAcsp(ACSP_NUMBER);
        verify(responseMapper).mapProfile(document);
    }

    @Test
    void shouldThrowNotFoundExceptionWhenGetProfileEmptyOptional() {
        // given
        when(repository.findAcsp(any())).thenReturn(Optional.empty());

        // when
        Executable actual = () -> service.getProfile(ACSP_NUMBER);

        // then
        Exception exception = assertThrows(NotFoundException.class, actual);
        assertEquals("ACSP document not found", exception.getMessage());
        verify(repository).findAcsp(ACSP_NUMBER);
        verifyNoMoreInteractions(responseMapper);
    }

    @Test
    void shouldGetFullProfile() {
        // given
        when(repository.findAcsp(any())).thenReturn(Optional.of(document));
        when(responseMapper.mapFullProfile(any())).thenReturn(expectedFullProfile);

        // when
        Object actual = service.getFullProfile(ACSP_NUMBER);

        // then
        assertEquals(expectedFullProfile, actual);
        verify(repository).findAcsp(ACSP_NUMBER);
        verify(responseMapper).mapFullProfile(document);
    }

    @Test
    void shouldThrowNotFoundExceptionWhenGetFullProfileEmptyOptional() {
        // given
        when(repository.findAcsp(any())).thenReturn(Optional.empty());

        // when
        Executable actual = () -> service.getFullProfile(ACSP_NUMBER);

        // then
        Exception exception = assertThrows(NotFoundException.class, actual);
        assertEquals("ACSP document not found", exception.getMessage());
        verify(repository).findAcsp(ACSP_NUMBER);
        verifyNoMoreInteractions(responseMapper);
    }
}