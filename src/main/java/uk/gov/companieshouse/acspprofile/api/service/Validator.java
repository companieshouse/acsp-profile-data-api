package uk.gov.companieshouse.acspprofile.api.service;

public interface Validator<T> {

    boolean isValid(T request);
}
