package uk.gov.companieshouse.acspprofile.api.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Component;
import uk.gov.companieshouse.acspprofile.api.model.AcspProfileDocument;

@Component
public interface AcspMongoRepository extends MongoRepository<AcspProfileDocument, String> {

}
