package uk.gov.companieshouse.acspprofile.api.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.mongodb.core.MongoTemplate;
import uk.gov.companieshouse.acspprofile.api.model.AcspProfileDocument;

@ExtendWith(MockitoExtension.class)
class AcspRepositoryTest {

    private static final String ACSP_NUMBER = "AP123456";

    @InjectMocks
    private AcspRepository repository;
    @Mock
    private MongoTemplate mongoTemplate;

    @Mock
    private AcspProfileDocument expected;

    @Test
    void shouldFindAcspDocument() {
        // given
        when(mongoTemplate.findById(any(), any())).thenReturn(expected);

        // when
        Optional<AcspProfileDocument> actual = repository.findAscp(ACSP_NUMBER);

        // then
        assertTrue(actual.isPresent());
        assertEquals(expected, actual.get());
    }
}