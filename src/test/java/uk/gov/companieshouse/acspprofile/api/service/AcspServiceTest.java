package uk.gov.companieshouse.acspprofile.api.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
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
import uk.gov.companieshouse.acspprofile.api.exception.ConflictException;
import uk.gov.companieshouse.acspprofile.api.exception.NotFoundException;
import uk.gov.companieshouse.acspprofile.api.mapper.AcspRequestMapper;
import uk.gov.companieshouse.acspprofile.api.mapper.AcspResponseMapper;
import uk.gov.companieshouse.acspprofile.api.model.AcspProfileDocument;
import uk.gov.companieshouse.acspprofile.api.repository.AcspRepository;
import uk.gov.companieshouse.api.acspprofile.AcspFullProfile;
import uk.gov.companieshouse.api.acspprofile.AcspProfile;
import uk.gov.companieshouse.api.acspprofile.InternalAcspApi;
import uk.gov.companieshouse.api.acspprofile.InternalData;

@ExtendWith(MockitoExtension.class)
class AcspServiceTest {

    private static final String ACSP_NUMBER = "AP123456";
    private static final String STALE_DELTA_AT = "20241003085145522153";
    private static final String DELTA_AT = "20241003093217479012";
    private static final String STALE_DELTA_MESSAGE = "Stale delta received, request delta_at: [%s], existing delta_at: [%s]"
            .formatted(STALE_DELTA_AT, DELTA_AT);

    @InjectMocks
    private AcspService service;
    @Mock
    private AcspRepository repository;
    @Mock
    private AcspResponseMapper responseMapper;
    @Mock
    private AcspRequestMapper requestMapper;

    @Mock
    private AcspProfileDocument document;
    @Mock
    private AcspProfile expectedProfile;
    @Mock
    private AcspFullProfile expectedFullProfile;
    @Mock
    private InternalAcspApi internalAcspApi;
    @Mock
    private InternalData internalData;

    @Test
    void shouldGetProfile() {
        // given
        when(repository.findAcsp(any())).thenReturn(Optional.of(document));
        when(responseMapper.mapProfile(any())).thenReturn(expectedProfile);

        // when
        AcspProfile actual = service.getProfile(ACSP_NUMBER);

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
        AcspFullProfile actual = service.getFullProfile(ACSP_NUMBER);

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

    @Test
    void shouldInsertAcspWhenDocumentDoesNotExist() {
        // given
        when(repository.findAcsp(any())).thenReturn(Optional.empty());
        when(requestMapper.mapNewAcsp(any())).thenReturn(document);

        // when
        service.upsertAcsp(ACSP_NUMBER, internalAcspApi);

        // then
        verify(repository).findAcsp(ACSP_NUMBER);
        verify(requestMapper).mapNewAcsp(internalAcspApi);
        verify(repository).insertAcsp(document);
    }

    @Test
    void shouldUpdateAcspWhenMoreRecentDeltaAt() {
        // given
        when(repository.findAcsp(any())).thenReturn(Optional.of(document));
        when(document.getDeltaAt()).thenReturn(STALE_DELTA_AT);
        when(internalAcspApi.getInternalData()).thenReturn(internalData);
        when(internalData.getDeltaAt()).thenReturn(DELTA_AT);
        when(requestMapper.mapExistingAcsp(any(), any())).thenReturn(document);

        // when
        service.upsertAcsp(ACSP_NUMBER, internalAcspApi);

        // then
        verify(repository).findAcsp(ACSP_NUMBER);
        verify(requestMapper).mapExistingAcsp(internalAcspApi, document);
        verify(repository).updateAcsp(document);
    }

    @Test
    void shouldUpdateAcspWhenSameDeltaAt() {
        // given
        when(repository.findAcsp(any())).thenReturn(Optional.of(document));
        when(document.getDeltaAt()).thenReturn(DELTA_AT);
        when(internalAcspApi.getInternalData()).thenReturn(internalData);
        when(internalData.getDeltaAt()).thenReturn(DELTA_AT);
        when(requestMapper.mapExistingAcsp(any(), any())).thenReturn(document);

        // when
        service.upsertAcsp(ACSP_NUMBER, internalAcspApi);

        // then
        verify(repository).findAcsp(ACSP_NUMBER);
        verify(requestMapper).mapExistingAcsp(internalAcspApi, document);
        verify(repository).updateAcsp(document);
    }

    @Test
    void shouldNotUpdateAcspAndThrowConflictExceptionWhenStaleDeltaAt() {
        // given
        when(repository.findAcsp(any())).thenReturn(Optional.of(document));
        when(document.getDeltaAt()).thenReturn(DELTA_AT);
        when(internalAcspApi.getInternalData()).thenReturn(internalData);
        when(internalData.getDeltaAt()).thenReturn(STALE_DELTA_AT);

        // when
        Executable executable = () -> service.upsertAcsp(ACSP_NUMBER, internalAcspApi);

        // then
        ConflictException exception = assertThrows(ConflictException.class, executable);
        assertEquals(STALE_DELTA_MESSAGE, exception.getMessage());
        verify(repository).findAcsp(ACSP_NUMBER);
        verifyNoInteractions(requestMapper);
        verifyNoMoreInteractions(repository);
    }
}