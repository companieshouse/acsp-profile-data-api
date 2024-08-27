package uk.gov.companieshouse.acspprofile.api.service;

public interface GetProcessor {

    Object getProfile(String acspNumber);
    Object getFullProfile(String acspNumber);
}
