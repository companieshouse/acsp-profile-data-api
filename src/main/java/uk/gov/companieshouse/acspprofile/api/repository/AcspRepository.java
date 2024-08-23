package uk.gov.companieshouse.acspprofile.api.repository;

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
    public AcspProfileDocument findAscp(String acspNumber) {
        return mongoTemplate.findById(acspNumber, AcspProfileDocument.class);
    }
}
