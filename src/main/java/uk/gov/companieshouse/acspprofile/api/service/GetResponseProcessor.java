package uk.gov.companieshouse.acspprofile.api.service;

import uk.gov.companieshouse.api.filinghistory.ExternalData;
import uk.gov.companieshouse.api.filinghistory.FilingHistoryList;
import uk.gov.companieshouse.acspprofile.api.model.ACSPProfileListRequestParams;

public interface GetResponseProcessor {

    ExternalData processGetSingleFilingHistory(final String companyNumber, final String transactionId);

    FilingHistoryList processGetCompanyFilingHistoryList(final ACSPProfileListRequestParams requestParams);
}
