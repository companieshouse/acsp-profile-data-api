package uk.gov.companieshouse.acspprofile.api.mapper.upsert;

import uk.gov.companieshouse.api.filinghistory.InternalFilingHistoryApi;
import uk.gov.companieshouse.acspprofile.api.model.mongo.ACSPProfileChild;

public interface ChildMapper<T extends ACSPProfileChild> {

    T mapChild(InternalFilingHistoryApi request, T child);

    T newInstance();

    default T mapChild(InternalFilingHistoryApi request) {
        return mapChild(request, newInstance());
    }
}
