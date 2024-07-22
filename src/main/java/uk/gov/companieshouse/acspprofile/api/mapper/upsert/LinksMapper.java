package uk.gov.companieshouse.acspprofile.api.mapper.upsert;

import java.util.Optional;
import org.springframework.stereotype.Component;
import uk.gov.companieshouse.api.filinghistory.Links;
import uk.gov.companieshouse.acspprofile.api.model.mongo.ACSPProfileLinks;

@Component
public class LinksMapper {

    ACSPProfileLinks map(final Links requestLinks) {
        return Optional.ofNullable(requestLinks)
                .map(links -> new ACSPProfileLinks()
                        .self(links.getSelf()))
                .orElse(null);
    }
}
