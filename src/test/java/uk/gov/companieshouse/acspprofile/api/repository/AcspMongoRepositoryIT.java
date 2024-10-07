package uk.gov.companieshouse.acspprofile.api.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
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
import uk.gov.companieshouse.acspprofile.api.exception.BadGatewayException;
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
        AcspProfileDocument expected = getAcspProfileDocument(filename, UPDATED_AT);
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
        AcspProfileDocument document = getAcspProfileDocument(filename, CREATED_AT);

        // when
        repository.insertAcsp(document);

        // then
        AcspProfileDocument actual = mongoTemplate.findById(ACSP_NUMBER, AcspProfileDocument.class);
        assertNotNull(actual);
        assertEquals(CREATED_AT, actual.getUpdated().getAt().toString());
    }

    @Test
    void shouldThrowBadGatewayAndNotInsertWhenDocumentAlreadyInserted() {
        // given
        mongoTemplate.insert(new AcspProfileDocument()
                .id(ACSP_NUMBER));

        // when
        Executable actual = () -> repository.insertAcsp(new AcspProfileDocument()
                .id(ACSP_NUMBER));

        // then
        assertThrows(BadGatewayException.class, actual);
    }

    @ParameterizedTest
    @CsvSource({
            "limited-company-acsp-document",
            "sole-trader-acsp-document",
    })
    void shouldUpdateAcspDocument(String filename) throws IOException {
        // given
        AcspProfileDocument document = getAcspProfileDocument(filename, CREATED_AT);
        mongoTemplate.insert(document);
        document.getUpdated().at(Instant.parse(UPDATED_AT));

        // when
        repository.updateAcsp(document);

        // then
        AcspProfileDocument actual = mongoTemplate.findById(ACSP_NUMBER, AcspProfileDocument.class);
        assertNotNull(actual);
        assertEquals(1, actual.getVersion());
        assertEquals(UPDATED_AT, actual.getUpdated().getAt().toString());
    }

    @Test
    void shouldThrowBadGatewayAndNotUpdateWhenNotLatestVersion() {
        // given
        mongoTemplate.insert(new AcspProfileDocument()
                .id(ACSP_NUMBER));
        mongoTemplate.save(new AcspProfileDocument()
                .id(ACSP_NUMBER)
                .version(0L));

        // when
        Executable actual = () -> repository.updateAcsp(new AcspProfileDocument()
                .id(ACSP_NUMBER)
                .version(0L));

        // then
        assertThrows(BadGatewayException.class, actual);
    }

    private AcspProfileDocument getAcspProfileDocument(String filename, String updatedAt) throws IOException {
        String documentJson = IOUtils.resourceToString("/mongo/%s.json".formatted(filename), StandardCharsets.UTF_8)
                .replaceAll("<acsp_number>", ACSP_NUMBER)
                .replaceAll("\"<version>\"", "0")
                .replaceAll("<created_at>", CREATED_AT)
                .replaceAll("<updated_at>", updatedAt);
        return objectMapper.readValue(documentJson, AcspProfileDocument.class);
    }
}
