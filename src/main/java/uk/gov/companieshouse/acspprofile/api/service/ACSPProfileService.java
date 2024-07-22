package uk.gov.companieshouse.acspprofile.api.service;

import static uk.gov.companieshouse.acspprofile.api.ACSPProfileApplication.NAMESPACE;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import uk.gov.companieshouse.api.model.ApiResponse;
import uk.gov.companieshouse.acspprofile.api.client.ResourceChangedApiClient;
import uk.gov.companieshouse.acspprofile.api.exception.ServiceUnavailableException;
import uk.gov.companieshouse.acspprofile.api.logging.DataMapHolder;
import uk.gov.companieshouse.acspprofile.api.model.ResourceChangedRequest;
import uk.gov.companieshouse.acspprofile.api.model.mongo.ACSPProfileDeleteAggregate;
import uk.gov.companieshouse.acspprofile.api.model.mongo.ACSPProfileDocument;
import uk.gov.companieshouse.acspprofile.api.model.mongo.ACSPProfileListAggregate;
import uk.gov.companieshouse.acspprofile.api.repository.Repository;
import uk.gov.companieshouse.logging.Logger;
import uk.gov.companieshouse.logging.LoggerFactory;

@Component
public class ACSPProfileService implements Service {

    private static final Logger LOGGER = LoggerFactory.getLogger(NAMESPACE);
    private final ResourceChangedApiClient apiClient;
    private final Repository repository;

    public ACSPProfileService(ResourceChangedApiClient apiClient, Repository repository) {
        this.apiClient = apiClient;
        this.repository = repository;
    }


    @Override
    public Optional<ACSPProfileListAggregate> findCompanyFilingHistoryList(String companyNumber,
                                                                           int startIndex,
                                                                           int itemsPerPage, List<String> categories) {
        List<String> categoryList = categories == null ? new ArrayList<>() : new ArrayList<>(categories);
        if (categoryList.contains("confirmation-statement")) {
            categoryList.add("annual-return");
        }
        if (categoryList.contains("incorporation")) {
            categoryList.addAll(
                    List.of("change-of-constitution", "change-of-name", "court-order",
                            "gazette", "reregistration", "resolution", "restoration"));
        }

        return Optional.of(repository.findCompanyFilingHistory(companyNumber, startIndex, itemsPerPage, categoryList))
                .filter(listAggregate -> listAggregate.getTotalCount() > 0);
    }

    @Override
    public Optional<ACSPProfileDocument> findExistingFilingHistory(final String transactionId,
                                                                   final String companyNumber) {
        return repository.findByIdAndCompanyNumber(transactionId, companyNumber);
    }

    @Override
    public Optional<ACSPProfileDeleteAggregate> findFilingHistoryByEntityId(String entityId) {
        return repository.findByEntityId(entityId);
    }

    @Override
    public void insertFilingHistory(final ACSPProfileDocument documentToSave) {
        handleTransaction(documentToSave, null);
    }

    @Override
    public void updateFilingHistory(ACSPProfileDocument documentToSave, ACSPProfileDocument originalDocumentCopy) {
        handleTransaction(documentToSave, originalDocumentCopy);
    }

    @Transactional
    @Override
    public void deleteExistingFilingHistory(ACSPProfileDocument existingDocument) {
        repository.deleteById(existingDocument.getTransactionId());
        ApiResponse<Void> response = apiClient.callResourceChanged(new ResourceChangedRequest(existingDocument, true));
        if (!HttpStatus.valueOf(response.getStatusCode()).is2xxSuccessful()) {
            throwServiceUnavailable();
        }
    }

    private void handleTransaction(ACSPProfileDocument documentToSave, ACSPProfileDocument originalDocumentCopy) {
        repository.save(documentToSave);
        ApiResponse<Void> result = apiClient.callResourceChanged(new ResourceChangedRequest(documentToSave, false));
        if (!HttpStatus.valueOf(result.getStatusCode()).is2xxSuccessful()) {
            if (originalDocumentCopy == null) {
                repository.deleteById(documentToSave.getTransactionId());
                LOGGER.info("Deleting previously inserted document", DataMapHolder.getLogMap());
            } else {
                repository.save(originalDocumentCopy);
                LOGGER.info("Reverting previously updated document", DataMapHolder.getLogMap());
            }
            throwServiceUnavailable();
        }
    }

    private void throwServiceUnavailable() {
        LOGGER.error("Resource changed endpoint unavailable", DataMapHolder.getLogMap());
        throw new ServiceUnavailableException("Resource changed endpoint unavailable");
    }
}
