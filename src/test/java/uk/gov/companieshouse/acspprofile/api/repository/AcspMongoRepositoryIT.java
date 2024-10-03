package uk.gov.companieshouse.acspprofile.api.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
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
class AcspMongoRepositoryIT {

    @Container
    private static final MongoDBContainer mongoDBContainer = new MongoDBContainer("mongo:5.0.12");
    private static final String ACSP_PROFILE_COLLECTION = "acsp_profile";
    private static final String ACSP_NUMBER = "AP123456";
    private static final String CREATED_AT = "2024-08-23T00:00:00Z";
    private static final String UPDATED_AT = "2024-09-24T12:00:00Z";

    @Autowired
    private AcspRepository repository;
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
            "limited-company-acsp-document",
            "sole-trader-acsp-document",
    })
    void shouldFindAcspDocumentById(String filename) throws IOException {
        // given
        String documentJson = IOUtils.resourceToString("/mongo/%s.json".formatted(filename), StandardCharsets.UTF_8)
                .replaceAll("<acsp_number>", ACSP_NUMBER)
                .replaceAll("<created_at>", CREATED_AT)
                .replaceAll("<updated_at>", UPDATED_AT);
        AcspProfileDocument expected = objectMapper.readValue(documentJson, AcspProfileDocument.class);
        mongoTemplate.insert(expected);

        // when
        Optional<AcspProfileDocument> actual = repository.findAcsp(ACSP_NUMBER);

        // then
        assertTrue(actual.isPresent());
        assertEquals(expected, actual.get());
    }

    @ParameterizedTest
    @CsvSource({
            "limited-company-acsp-document",
            "sole-trader-acsp-document",
    })
    void shouldInsertAcspDocument(String filename) throws IOException {
        // given
        String documentJson = IOUtils.resourceToString("/mongo/%s.json".formatted(filename), StandardCharsets.UTF_8)
                .replaceAll("<acsp_number>", ACSP_NUMBER)
                .replaceAll("<created_at>", CREATED_AT)
                .replaceAll("<updated_at>", CREATED_AT);
        AcspProfileDocument document = objectMapper.readValue(documentJson, AcspProfileDocument.class);

        // when
        repository.insertAcsp(document);

        // then
        AcspProfileDocument actual = mongoTemplate.findById(ACSP_NUMBER, AcspProfileDocument.class);
        assertNotNull(actual);
        assertEquals(CREATED_AT, actual.getUpdated().getAt().toString());
    }

    @ParameterizedTest
    @CsvSource({
            "limited-company-acsp-document",
            "sole-trader-acsp-document",
    })
    void shouldUpdateAcspDocument(String filename) throws IOException {
        // given
        String documentJson = IOUtils.resourceToString("/mongo/%s.json".formatted(filename), StandardCharsets.UTF_8)
                .replaceAll("<acsp_number>", ACSP_NUMBER)
                .replaceAll("<created_at>", CREATED_AT)
                .replaceAll("<updated_at>", CREATED_AT);
        AcspProfileDocument document = objectMapper.readValue(documentJson, AcspProfileDocument.class);
        mongoTemplate.insert(document);
        document.getUpdated().at(Instant.parse(UPDATED_AT));

        // when
        repository.updateAcsp(document);

        // then
        AcspProfileDocument actual = mongoTemplate.findById(ACSP_NUMBER, AcspProfileDocument.class);
        assertNotNull(actual);
        assertEquals(UPDATED_AT, actual.getUpdated().getAt().toString());
    }
}
