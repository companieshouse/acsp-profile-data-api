package uk.gov.companieshouse.acspprofile.api.model;

import uk.gov.companieshouse.acspprofile.api.model.mongo.ACSPProfileDocument;

public record ResourceChangedRequest(ACSPProfileDocument ACSPProfileDocument, boolean isDelete) {

}
