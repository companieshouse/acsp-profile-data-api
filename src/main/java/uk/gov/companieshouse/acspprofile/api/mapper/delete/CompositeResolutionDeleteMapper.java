package uk.gov.companieshouse.acspprofile.api.mapper.delete;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;
import org.springframework.stereotype.Component;
import uk.gov.companieshouse.acspprofile.api.logging.DataMapHolder;
import uk.gov.companieshouse.acspprofile.api.model.mongo.ACSPProfileDeltaTimestamp;
import uk.gov.companieshouse.acspprofile.api.model.mongo.ACSPProfileDocument;
import uk.gov.companieshouse.acspprofile.api.model.mongo.ACSPProfileResolution;

@Component
public class CompositeResolutionDeleteMapper {

    private final Supplier<Instant> instantSupplier;

    public CompositeResolutionDeleteMapper(Supplier<Instant> instantSupplier) {
        this.instantSupplier = instantSupplier;
    }

    public Optional<ACSPProfileDocument> removeTransaction(int index, ACSPProfileDocument documentCopy) {
        List<ACSPProfileResolution> resolutions = documentCopy.getData().getResolutions();

        if (resolutions.size() == 1) {
            return Optional.empty();
        } else {
            resolutions.remove(index);
            documentCopy.updated(new ACSPProfileDeltaTimestamp()
                    .at(instantSupplier.get())
                    .by(DataMapHolder.getRequestId()));
            return Optional.of(documentCopy);
        }
    }
}
