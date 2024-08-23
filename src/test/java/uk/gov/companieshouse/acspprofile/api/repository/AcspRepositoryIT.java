package uk.gov.companieshouse.acspprofile.api.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import uk.gov.companieshouse.acspprofile.api.model.AcspProfileDocument;

@Testcontainers
@SpringBootTest
class AcspRepositoryIT {

    private static final String ACSP_NUMBER = "12345678";

    @Container
    private static final MongoDBContainer mongoDBContainer = new MongoDBContainer("mongo:5.0.12");

    @Autowired
    private MongoTemplate mongoTemplate;
    @Autowired
    private Repository repository;

    @DynamicPropertySource
    static void setProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.data.mongodb.uri", mongoDBContainer::getReplicaSetUrl);
    }

    @Test
    void shouldFindAcspDocumentById() {
        // given
        AcspProfileDocument expected = new AcspProfileDocument()
                .id(ACSP_NUMBER);

        mongoTemplate.insert(expected);

        // when
        AcspProfileDocument actual = repository.findAscp(ACSP_NUMBER);

        // then
        assertEquals(expected, actual);
    }
}
