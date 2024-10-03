package uk.gov.companieshouse.acspprofile.api.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.function.Executable;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.NonTransientDataAccessResourceException;
import org.springframework.dao.TransientDataAccessResourceException;
import uk.gov.companieshouse.acspprofile.api.exception.BadGatewayException;
import uk.gov.companieshouse.acspprofile.api.model.AcspProfileDocument;

@ExtendWith(MockitoExtension.class)
class AcspRepositoryTest {

    private static final String ACSP_NUMBER = "AP123456";

    @InjectMocks
    private AcspRepository service;
    @Mock
    private AcspMongoRepository repository;

    @Mock
    private AcspProfileDocument document;

    @Test
    void shouldFindAcsp() {
        // given
        when(repository.findById(any())).thenReturn(Optional.of(document));

        // when
        Optional<AcspProfileDocument> actual = service.findAcsp(ACSP_NUMBER);

        // then
        assertTrue(actual.isPresent());
        assertEquals(document, actual.get());
        verify(repository).findById(ACSP_NUMBER);
    }

    @Test
    void shouldThrowBadGatewayWhenRecoverableErrorDuringFind() {
        // given
        when(repository.findById(any())).thenThrow(TransientDataAccessResourceException.class);

        // when
        Executable actual = () -> service.findAcsp(ACSP_NUMBER);

        // then
        BadGatewayException exception = assertThrows(BadGatewayException.class, actual);
        assertEquals("Recoverable MongoDB error during find", exception.getMessage());
        verify(repository).findById(ACSP_NUMBER);
    }

    @Test
    void shouldThrowBadGatewayWhenOtherErrorDuringFind() {
        // given
        when(repository.findById(any())).thenThrow(NonTransientDataAccessResourceException.class);

        // when
        Executable actual = () -> service.findAcsp(ACSP_NUMBER);

        // then
        BadGatewayException exception = assertThrows(BadGatewayException.class, actual);
        assertEquals("MongoDB error during find", exception.getMessage());
        verify(repository).findById(ACSP_NUMBER);
    }

    @Test
    void shouldInsertAcsp() {
        // given
        // when
        service.insertAcsp(document);

        // then
        verify(repository).insert(document);
    }

    @Test
    void shouldThrowBadGatewayWhenRecoverableErrorDuringInsert() {
        // given
        when(repository.insert(any(AcspProfileDocument.class))).thenThrow(TransientDataAccessResourceException.class);

        // when
        Executable actual = () -> service.insertAcsp(document);

        // then
        BadGatewayException exception = assertThrows(BadGatewayException.class, actual);
        assertEquals("Recoverable MongoDB error during insert", exception.getMessage());
        verify(repository).insert(document);
    }

    @Test
    void shouldThrowBadGatewayWhenOtherErrorDuringInsert() {
        // given
        when(repository.insert(any(AcspProfileDocument.class)))
                .thenThrow(NonTransientDataAccessResourceException.class);

        // when
        Executable actual = () -> service.insertAcsp(document);

        // then
        BadGatewayException exception = assertThrows(BadGatewayException.class, actual);
        assertEquals("MongoDB error during insert", exception.getMessage());
        verify(repository).insert(document);
    }

    @Test
    void shouldUpdateAcsp() {
        // given
        // when
        service.updateAcsp(document);

        // then
        verify(repository).save(document);
    }

    @Test
    void shouldThrowBadGatewayWhenRecoverableErrorDuringUpdate() {
        // given
        when(repository.save(any(AcspProfileDocument.class))).thenThrow(TransientDataAccessResourceException.class);

        // when
        Executable actual = () -> service.updateAcsp(document);

        // then
        BadGatewayException exception = assertThrows(BadGatewayException.class, actual);
        assertEquals("Recoverable MongoDB error during update", exception.getMessage());
        verify(repository).save(document);
    }

    @Test
    void shouldThrowBadGatewayWhenOtherErrorDuringUpdate() {
        // given
        when(repository.save(any(AcspProfileDocument.class)))
                .thenThrow(NonTransientDataAccessResourceException.class);

        // when
        Executable actual = () -> service.updateAcsp(document);

        // then
        BadGatewayException exception = assertThrows(BadGatewayException.class, actual);
        assertEquals("MongoDB error during update", exception.getMessage());
        verify(repository).save(document);
    }
}