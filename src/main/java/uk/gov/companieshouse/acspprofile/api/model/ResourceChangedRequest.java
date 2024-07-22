package uk.gov.companieshouse.acspprofile.api.model;

import uk.gov.companieshouse.acspprofile.api.model.mongo.FilingHistoryDocument;

public record ResourceChangedRequest(FilingHistoryDocument filingHistoryDocument, boolean isDelete) {

}
