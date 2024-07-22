package uk.gov.companieshouse.acspprofile.api.service;

import java.util.List;
import java.util.Optional;
import uk.gov.companieshouse.acspprofile.api.model.mongo.ACSPProfileDeleteAggregate;
import uk.gov.companieshouse.acspprofile.api.model.mongo.ACSPProfileDocument;
import uk.gov.companieshouse.acspprofile.api.model.mongo.ACSPProfileListAggregate;

public interface Service {

    Optional<ACSPProfileListAggregate> findCompanyFilingHistoryList(String companyNumber, int startIndex,
                                                                    int itemsPerPage, List<String> categories);

    Optional<ACSPProfileDocument> findExistingFilingHistory(final String transactionId, final String companyNumber);

    Optional<ACSPProfileDeleteAggregate> findFilingHistoryByEntityId(String entityId);

    void insertFilingHistory(final ACSPProfileDocument documentToSave);

    void updateFilingHistory(final ACSPProfileDocument documentToSave, ACSPProfileDocument originalDocumentCopy);

    void deleteExistingFilingHistory(ACSPProfileDocument existingDocument);
}
