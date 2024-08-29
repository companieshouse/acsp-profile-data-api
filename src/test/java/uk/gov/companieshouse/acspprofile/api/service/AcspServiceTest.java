package uk.gov.companieshouse.acspprofile.api.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import uk.gov.companieshouse.acspprofile.api.model.AcspProfileDocument;
import uk.gov.companieshouse.acspprofile.api.repository.AcspRepository;

@ExtendWith(MockitoExtension.class)
class AcspServiceTest {

    private static final String ACSP_NUMBER = "AP123456";

    @InjectMocks
    private AcspService service;
    @Mock
    private AcspRepository repository;

    @Mock
    private AcspProfileDocument expected;

    @Test
    void shouldFindAcsp() {
        // given
        when(repository.findAscp(any())).thenReturn(Optional.of(expected));

        // when
        Optional<AcspProfileDocument> actual = service.findAcsp(ACSP_NUMBER);

        // then
        assertTrue(actual.isPresent());
        assertEquals(expected, actual.get());
        verify(repository).findAscp(ACSP_NUMBER);
    }
}