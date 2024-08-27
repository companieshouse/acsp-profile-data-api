package uk.gov.companieshouse.acspprofile.api.repository;

import java.util.Optional;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Component;
import uk.gov.companieshouse.acspprofile.api.model.AcspProfileDocument;

@Component
public class AcspRepository implements Repository {

    private final MongoTemplate mongoTemplate;

    public AcspRepository(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public Optional<AcspProfileDocument> findAscp(String acspNumber) {
        return Optional.ofNullable(mongoTemplate.findById(acspNumber, AcspProfileDocument.class));
    }
}
