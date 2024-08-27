package uk.gov.companieshouse.acspprofile.api.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.shaded.org.apache.commons.io.IOUtils;
import uk.gov.companieshouse.acspprofile.api.model.AcspProfileDocument;

@Testcontainers
@SpringBootTest
class AcspRepositoryIT {

    @Container
    private static final MongoDBContainer mongoDBContainer = new MongoDBContainer("mongo:5.0.12");
    private static final String ACSP_PROFILE_COLLECTION = "acsp_profile";
    private static final String ACSP_NUMBER = "12345678";

    @Autowired
    private Repository repository;
    @Autowired
    private MongoTemplate mongoTemplate;
    @Autowired
    private ObjectMapper objectMapper;

    @DynamicPropertySource
    static void setProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.data.mongodb.uri", mongoDBContainer::getReplicaSetUrl);
    }

    @BeforeEach
    void setUp() {
        mongoTemplate.dropCollection(ACSP_PROFILE_COLLECTION);
        mongoTemplate.createCollection(ACSP_PROFILE_COLLECTION);
    }

    @ParameterizedTest
    @CsvSource({
            "independent-legal-professionals-acsp-document",
            "sole-trader-acsp-document",
    })
    void shouldFindAcspDocumentById(String filename) throws IOException {
        // given
        String documentJson = IOUtils.resourceToString("/mongo/%s.json".formatted(filename), StandardCharsets.UTF_8)
                .replaceAll("<acsp_number>", ACSP_NUMBER);
        AcspProfileDocument expected = objectMapper.readValue(documentJson, AcspProfileDocument.class);
        mongoTemplate.insert(expected);

        // when
        Optional<AcspProfileDocument> actual = repository.findAscp(ACSP_NUMBER);

        // then
        assertTrue(actual.isPresent());
        assertEquals(expected, actual.get());
    }
}
