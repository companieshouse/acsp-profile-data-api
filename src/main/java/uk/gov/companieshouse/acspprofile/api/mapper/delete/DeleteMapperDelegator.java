package uk.gov.companieshouse.acspprofile.api.mapper.delete;

import java.util.Optional;
import org.springframework.stereotype.Component;
import uk.gov.companieshouse.acspprofile.api.ACSPProfileApplication;
import uk.gov.companieshouse.acspprofile.api.exception.BadRequestException;
import uk.gov.companieshouse.acspprofile.api.logging.DataMapHolder;
import uk.gov.companieshouse.acspprofile.api.model.mongo.ACSPProfileData;
import uk.gov.companieshouse.acspprofile.api.model.mongo.ACSPProfileDeleteAggregate;
import uk.gov.companieshouse.acspprofile.api.model.mongo.ACSPProfileDocument;
import uk.gov.companieshouse.acspprofile.api.serdes.ACSPProfileDocumentCopier;
import uk.gov.companieshouse.logging.Logger;
import uk.gov.companieshouse.logging.LoggerFactory;

@Component
public class DeleteMapperDelegator {

    private static final Logger LOGGER = LoggerFactory.getLogger(ACSPProfileApplication.NAMESPACE);
    private static final String COMPOSITE_RES_TYPE = "RESOLUTIONS";
    private final ACSPProfileDocumentCopier documentCopier;
    private final CompositeResolutionDeleteMapper compositeResolutionDeleteMapper;
    private final ChildDeleteMapper childDeleteMapper;

    public DeleteMapperDelegator(ACSPProfileDocumentCopier documentCopier,
                                 CompositeResolutionDeleteMapper compositeResolutionDeleteMapper, ChildDeleteMapper childDeleteMapper) {
        this.documentCopier = documentCopier;
        this.compositeResolutionDeleteMapper = compositeResolutionDeleteMapper;
        this.childDeleteMapper = childDeleteMapper;
    }

    @DeleteChildTransactions
    public Optional<ACSPProfileDocument> delegateDelete(String entityId, ACSPProfileDeleteAggregate aggregate) {
        ACSPProfileDocument document = documentCopier.deepCopy(aggregate.getDocument());
        ACSPProfileData data = document.getData();

        final int resIndex = aggregate.getResolutionIndex();
        if (resIndex >= 0) {
            if (COMPOSITE_RES_TYPE.equals(data.getType())) {
                LOGGER.debug("Matched composite resolution _entity_id: [%s]".formatted(entityId),
                        DataMapHolder.getLogMap());
                return compositeResolutionDeleteMapper.removeTransaction(resIndex, document);
            } else {
                LOGGER.debug("Matched resolution _entity_id: [%s]".formatted(entityId),
                        DataMapHolder.getLogMap());
                return childDeleteMapper.removeTransaction(entityId, resIndex, document, data::getResolutions,
                        data::resolutions);
            }
        }

        final int annotationIndex = aggregate.getAnnotationIndex();
        if (annotationIndex >= 0) {
            LOGGER.debug("Matched annotation _entity_id: [%s]".formatted(entityId),
                    DataMapHolder.getLogMap());
            return childDeleteMapper.removeTransaction(entityId, annotationIndex, document, data::getAnnotations,
                    data::annotations);
        }

        final int associatedFilingIndex = aggregate.getAssociatedFilingIndex();
        if (associatedFilingIndex >= 0) {
            LOGGER.debug("Matched associated filing _entity_id: [%s]".formatted(entityId),
                    DataMapHolder.getLogMap());
            return childDeleteMapper.removeTransaction(entityId, associatedFilingIndex, document,
                    data::getAssociatedFilings,
                    data::associatedFilings);
        }

        if (entityId.equals(document.getEntityId())) {
            LOGGER.debug("Matched top level _entity_id: [%s]".formatted(entityId), DataMapHolder.getLogMap());
            return Optional.empty();
        } else {
            LOGGER.debug("No match for _entity_id: [%s]".formatted(entityId), DataMapHolder.getLogMap());
            throw new BadRequestException("No match for _entity_id: [%s]".formatted(entityId));
        }
    }
}
