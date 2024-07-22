package uk.gov.companieshouse.acspprofile.api.mapper.delete;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Supplier;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;
import uk.gov.companieshouse.acspprofile.api.logging.DataMapHolder;
import uk.gov.companieshouse.acspprofile.api.model.mongo.ACSPProfileChild;
import uk.gov.companieshouse.acspprofile.api.model.mongo.ACSPProfileData;
import uk.gov.companieshouse.acspprofile.api.model.mongo.ACSPProfileDeltaTimestamp;
import uk.gov.companieshouse.acspprofile.api.model.mongo.ACSPProfileDocument;

@Component
public class ChildDeleteMapper {

    private final Supplier<Instant> instantSupplier;

    public ChildDeleteMapper(Supplier<Instant> instantSupplier) {
        this.instantSupplier = instantSupplier;
    }

    public <T extends ACSPProfileChild> Optional<ACSPProfileDocument> removeTransaction(String entityId, int index,
                                                                                        ACSPProfileDocument documentCopy, Supplier<List<T>> childListGetter,
                                                                                        Function<List<T>, ACSPProfileData> childListSetter) {

        if (entityId.equals(documentCopy.getEntityId())) {
            return Optional.empty();
        }

        List<T> childList = childListGetter.get();

        if (childList.size() == 1) {
            if (StringUtils.isBlank(documentCopy.getData().getType())) {
                return Optional.empty();
            } else {
                childListSetter.apply(null);
            }
        } else {
            childList.remove(index);
        }

        documentCopy.updated(new ACSPProfileDeltaTimestamp()
                .at(instantSupplier.get())
                .by(DataMapHolder.getRequestId()));
        return Optional.of(documentCopy);
    }
}
