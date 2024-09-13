package uk.gov.companieshouse.acspprofile.api.service;

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
import uk.gov.companieshouse.acspprofile.api.repository.AcspMongoRepository;
import uk.gov.companieshouse.acspprofile.api.repository.AcspRepository;

@ExtendWith(MockitoExtension.class)
class AcspRepositoryTest {

    private static final String ACSP_NUMBER = "AP123456";

    @InjectMocks
    private AcspRepository service;
    @Mock
    private AcspMongoRepository repository;

    @Mock
    private AcspProfileDocument expected;

    @Test
    void shouldFindAcsp() {
        // given
        when(repository.findById(any())).thenReturn(Optional.of(expected));

        // when
        Optional<AcspProfileDocument> actual = service.findAcsp(ACSP_NUMBER);

        // then
        assertTrue(actual.isPresent());
        assertEquals(expected, actual.get());
        verify(repository).findById(ACSP_NUMBER);
    }

    @Test
    void shouldThrowBadGatewayWhenTransientDataAccessExceptionThrown() {
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
    void shouldThrowBadGatewayWhenNonTransientDataAccessExceptionThrown() {
        // given
        when(repository.findById(any())).thenThrow(NonTransientDataAccessResourceException.class);

        // when
        Executable actual = () -> service.findAcsp(ACSP_NUMBER);

        // then
        BadGatewayException exception = assertThrows(BadGatewayException.class, actual);
        assertEquals("MongoDB error during find", exception.getMessage());
        verify(repository).findById(ACSP_NUMBER);
    }
}