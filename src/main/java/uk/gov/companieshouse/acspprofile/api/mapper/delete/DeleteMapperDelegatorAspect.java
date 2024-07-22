package uk.gov.companieshouse.acspprofile.api.mapper.delete;

import java.util.Optional;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;
import uk.gov.companieshouse.acspprofile.api.ACSPProfileApplication;
import uk.gov.companieshouse.acspprofile.api.exception.BadRequestException;
import uk.gov.companieshouse.acspprofile.api.logging.DataMapHolder;
import uk.gov.companieshouse.acspprofile.api.model.mongo.ACSPProfileDeleteAggregate;
import uk.gov.companieshouse.acspprofile.api.model.mongo.ACSPProfileDocument;
import uk.gov.companieshouse.logging.Logger;
import uk.gov.companieshouse.logging.LoggerFactory;

@Aspect
@Component
@ConditionalOnProperty(prefix = "feature", name = "delete_child_transactions.disabled", havingValue = "true")
public class DeleteMapperDelegatorAspect {

    private static final Logger LOGGER = LoggerFactory.getLogger(ACSPProfileApplication.NAMESPACE);

    @Around("@annotation(DeleteChildTransactions)")
    public Optional<ACSPProfileDocument> deleteChildTransactionsDisabled(JoinPoint joinPoint) {
        LOGGER.debug("Deletion of child transactions disabled", DataMapHolder.getLogMap());
        Object[] args = joinPoint.getArgs();
        String entityId = (String) args[0];
        ACSPProfileDeleteAggregate aggregate = (ACSPProfileDeleteAggregate) args[1];

        if (!entityId.equals(aggregate.getDocument().getEntityId())
                || "RESOLUTIONS".equals(aggregate.getDocument().getData().getType())) {
            LOGGER.error("Cannot delete child while child deletion disabled, _entity_id: [%s]"
                    .formatted(entityId), DataMapHolder.getLogMap());
            throw new BadRequestException("Cannot delete child while child deletion disabled, _entity_id: [%s]"
                    .formatted(entityId));
        } else {
            LOGGER.debug("Matched parent _entity_id: [%s]".formatted(entityId), DataMapHolder.getLogMap());
            return Optional.empty();
        }
    }
}
