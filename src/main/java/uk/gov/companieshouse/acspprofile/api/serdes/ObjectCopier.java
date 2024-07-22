package uk.gov.companieshouse.acspprofile.api.serdes;

public interface ObjectCopier<T> {

    T deepCopy(T originalObject);
}
