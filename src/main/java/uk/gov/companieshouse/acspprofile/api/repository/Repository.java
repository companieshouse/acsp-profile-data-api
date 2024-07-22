package uk.gov.companieshouse.acspprofile.api.repository;

import static org.springframework.data.mongodb.core.aggregation.Aggregation.addFields;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.count;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.facet;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.match;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.newAggregation;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.project;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.sort;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.unwind;
import static org.springframework.data.mongodb.core.aggregation.ArrayOperators.IndexOfArray.arrayOf;
import static org.springframework.data.mongodb.core.aggregation.ConditionalOperators.ifNull;
import static uk.gov.companieshouse.acspprofile.api.ACSPProfileApplication.NAMESPACE;

import java.util.List;
import java.util.Optional;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;
import uk.gov.companieshouse.acspprofile.api.exception.ServiceUnavailableException;
import uk.gov.companieshouse.acspprofile.api.logging.DataMapHolder;
import uk.gov.companieshouse.acspprofile.api.model.mongo.ACSPProfileDeleteAggregate;
import uk.gov.companieshouse.acspprofile.api.model.mongo.ACSPProfileDocument;
import uk.gov.companieshouse.acspprofile.api.model.mongo.ACSPProfileListAggregate;
import uk.gov.companieshouse.logging.Logger;
import uk.gov.companieshouse.logging.LoggerFactory;

@Component
public class Repository {

    private static final Logger LOGGER = LoggerFactory.getLogger(NAMESPACE);

    private final MongoTemplate mongoTemplate;

    public Repository(final MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    public ACSPProfileListAggregate findCompanyFilingHistory(String companyNumber,
                                                             int startIndex, int itemsPerPage, List<String> categories) {
        try {
            Criteria criteria = Criteria.where("company_number").is(companyNumber);
            if (!categories.isEmpty()) {
                criteria.and("data.category").in(categories);
            }

            Aggregation aggregation = newAggregation(
                    match(criteria),
                    facet(
                            count().as("count")).as("total_count")
                            .and(match(new Criteria()), sort(Direction.DESC, "data.date")).as("document_list"),
                    unwind("$total_count", true),
                    project()
                            .and(ifNull("$total_count.count").then(0)).as("total_count")
                            .andExpression("$document_list").slice(itemsPerPage, startIndex).as("document_list"));

            return mongoTemplate.aggregate(aggregation, ACSPProfileDocument.class, ACSPProfileListAggregate.class)
                    .getUniqueMappedResult();
        } catch (DataAccessException ex) {
            LOGGER.error("MongoDB unavailable when finding filing history list: %s".formatted(
                    ex.getMessage()), DataMapHolder.getLogMap());
            throw new ServiceUnavailableException("MongoDB unavailable when finding filing history list");
        }
    }

    public Optional<ACSPProfileDocument> findByIdAndCompanyNumber(final String id, final String companyNumber) {
        try {
            Criteria criteria = Criteria.where("_id").is(id)
                    .and("company_number").is(companyNumber);

            Query query = new Query(criteria);

            return Optional.ofNullable(mongoTemplate.findOne(query, ACSPProfileDocument.class));
        } catch (DataAccessException ex) {
            LOGGER.error("MongoDB unavailable when finding the document: %s".formatted(ex.getMessage()),
                    DataMapHolder.getLogMap());
            throw new ServiceUnavailableException("MongoDB unavailable when finding document");
        }
    }

    public Optional<ACSPProfileDeleteAggregate> findByEntityId(final String entityId) {
        try {
            Aggregation aggregation = newAggregation(
                    match(new Criteria()
                            .orOperator(
                                    Criteria.where("_entity_id").is(entityId),
                                    Criteria.where("data.resolutions._entity_id").is(entityId),
                                    Criteria.where("data.annotations._entity_id").is(entityId),
                                    Criteria.where("data.associated_filings._entity_id").is(entityId))),
                    addFields().build()
                            .addField("resolutionIndex",
                                    ifNull(arrayOf("$data.resolutions._entity_id")
                                            .indexOf(entityId))
                                            .then(-1))
                            .addField("annotationIndex",
                                    ifNull(arrayOf("$data.annotations._entity_id")
                                            .indexOf(entityId))
                                            .then(-1))
                            .addField("associatedFilingIndex",
                                    ifNull(arrayOf("$data.associated_filings._entity_id")
                                            .indexOf(entityId))
                                            .then(-1)),
                    project()
                            .andExclude("_id")
                            .andExpression("$resolutionIndex").as("resolution_index")
                            .andExpression("$annotationIndex").as("annotation_index")
                            .andExpression("$associatedFilingIndex").as("associated_filing_index")
                            .andExpression("$$ROOT").as("document"));

            return Optional.ofNullable(
                    mongoTemplate.aggregate(aggregation, ACSPProfileDocument.class,
                            ACSPProfileDeleteAggregate.class).getUniqueMappedResult());
        } catch (DataAccessException ex) {
            LOGGER.error("MongoDB unavailable when trying to retrieve filing history delete document: %s".formatted(
                    ex.getMessage()), DataMapHolder.getLogMap());
            throw new ServiceUnavailableException(
                    "MongoDB unavailable when trying to retrieve filing history delete document");
        }
    }

    public void save(final ACSPProfileDocument document) {
        try {
            mongoTemplate.save(document);
        } catch (DataAccessException ex) {
            LOGGER.error("MongoDB unavailable when saving the document: %s".formatted(ex.getMessage()),
                    DataMapHolder.getLogMap());
            throw new ServiceUnavailableException("MongoDB unavailable when saving document");
        }
    }

    public void deleteById(final String id) {
        try {
            mongoTemplate.remove(Query.query(Criteria.where("_id").is(id)), ACSPProfileDocument.class);
        } catch (DataAccessException ex) {
            LOGGER.error("MongoDB unavailable when deleting document: %s".formatted(ex.getMessage()),
                    DataMapHolder.getLogMap());
            throw new ServiceUnavailableException("MongoDB unavailable when deleting document");
        }
    }
}
