package uk.gov.companieshouse.acspprofile.api.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.bson.Document;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.shaded.org.apache.commons.io.IOUtils;
import uk.gov.companieshouse.acspprofile.api.model.mongo.ACSPProfileAnnotation;
import uk.gov.companieshouse.acspprofile.api.model.mongo.ACSPProfileData;
import uk.gov.companieshouse.acspprofile.api.model.mongo.ACSPProfileDeleteAggregate;
import uk.gov.companieshouse.acspprofile.api.model.mongo.ACSPProfileDeltaTimestamp;
import uk.gov.companieshouse.acspprofile.api.model.mongo.ACSPProfileDescriptionValues;
import uk.gov.companieshouse.acspprofile.api.model.mongo.ACSPProfileDocument;
import uk.gov.companieshouse.acspprofile.api.model.mongo.ACSPProfileLinks;
import uk.gov.companieshouse.acspprofile.api.model.mongo.ACSPProfileListAggregate;
import uk.gov.companieshouse.acspprofile.api.model.mongo.ACSPProfileOriginalValues;

@Testcontainers
@SpringBootTest
class RepositoryIT {

    private static final String ACSP_PROFILE_COLLECTION = "company_filing_history";
    private static final String TRANSACTION_ID = "transactionId";
    private static final String TRANSACTION_ID_TWO = "transactionIdTwo";
    private static final String ENTITY_ID = "1234567890";
    private static final String COMPANY_NUMBER = "12345678";
    private static final String TOP_LEVEL_ENTITY_ID = "1234567890";
    private static final String CHILD_ENTITY_ID = "2234567890";
    private static final String EXISTING_DELTA_AT = "20140815230459600643";
    private static final String EXISTING_DELTA_AT_TWO = "20140816230459600643";
    private static final String UPDATED_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS).toString();
    private static final int START_INDEX = 0;
    private static final int DEFAULT_ITEMS_PER_PAGE = 25;
    private static final String OFFICERS_CATEGORY = "officers";
    private static final int TOTAL_RESULTS_NUMBER = 55;

    @Container
    private static final MongoDBContainer mongoDBContainer = new MongoDBContainer("mongo:5.0.12");

    @Autowired
    private MongoTemplate mongoTemplate;
    @Autowired
    private Repository repository;

    @DynamicPropertySource
    static void setProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.data.mongodb.uri", mongoDBContainer::getReplicaSetUrl);
    }

    @BeforeEach
    void setUp() {
        mongoTemplate.dropCollection(ACSP_PROFILE_COLLECTION);
        mongoTemplate.createCollection(ACSP_PROFILE_COLLECTION);
    }

    @Test
    void testMappingsFromMongoToDocument() throws IOException {
        // given
        final String jsonToInsert = IOUtils.resourceToString("/mongo_docs/acsp-profile-document.json",
                        StandardCharsets.UTF_8)
                .replaceAll("<id>", TRANSACTION_ID)
                .replaceAll("<entity_id>", ENTITY_ID)
                .replaceAll("<company_number>", COMPANY_NUMBER)
                .replaceAll("<category>", OFFICERS_CATEGORY);
        mongoTemplate.insert(Document.parse(jsonToInsert), ACSP_PROFILE_COLLECTION);

        final ACSPProfileDocument expectedDocument = getFilingHistoryDocument(TRANSACTION_ID);

        // when
        final Optional<ACSPProfileDocument> actualDocument = repository.findByIdAndCompanyNumber(TRANSACTION_ID,
                COMPANY_NUMBER);

        // then
        assertTrue(actualDocument.isPresent());
        assertEquals(expectedDocument, actualDocument.get());
    }

    @Test
    void testAggregationQueryToFindTwoDocuments() throws IOException {
        // given
        final String jsonToInsert = IOUtils.resourceToString("/mongo_docs/acsp-profile-document.json",
                        StandardCharsets.UTF_8)
                .replaceAll("<id>", TRANSACTION_ID)
                .replaceAll("<entity_id>", ENTITY_ID)
                .replaceAll("<company_number>", COMPANY_NUMBER)
                .replaceAll("<category>", OFFICERS_CATEGORY);
        final String jsonToInsertTwo = IOUtils.resourceToString("/mongo_docs/acsp-profile-document.json",
                        StandardCharsets.UTF_8)
                .replaceAll("<id>", TRANSACTION_ID_TWO)
                .replaceAll("<entity_id>", ENTITY_ID)
                .replaceAll("<company_number>", COMPANY_NUMBER)
                .replaceAll("<category>", OFFICERS_CATEGORY);
        mongoTemplate.insert(Document.parse(jsonToInsert), ACSP_PROFILE_COLLECTION);
        mongoTemplate.insert(Document.parse(jsonToInsertTwo), ACSP_PROFILE_COLLECTION);

        final ACSPProfileListAggregate expected = getFilingHistoryListAggregate();

        // when
        final ACSPProfileListAggregate actual = repository.findCompanyFilingHistory(COMPANY_NUMBER,
                START_INDEX, DEFAULT_ITEMS_PER_PAGE, List.of());

        // then
        assertEquals(expected, actual);
    }

    @Test
    void testAggregationQueryToFindOneDocumentWhenCategoryFilter() throws IOException {
        // given
        final String jsonToInsert = IOUtils.resourceToString("/mongo_docs/acsp-profile-document.json",
                        StandardCharsets.UTF_8)
                .replaceAll("<id>", TRANSACTION_ID)
                .replaceAll("<entity_id>", ENTITY_ID)
                .replaceAll("<company_number>", COMPANY_NUMBER)
                .replaceAll("<category>", OFFICERS_CATEGORY);
        final String jsonToInsertTwo = IOUtils.resourceToString("/mongo_docs/acsp-profile-document.json",
                        StandardCharsets.UTF_8)
                .replaceAll("<id>", TRANSACTION_ID_TWO)
                .replaceAll("<entity_id>", ENTITY_ID)
                .replaceAll("<company_number>", COMPANY_NUMBER)
                .replaceAll("<category>", "incorporation");
        mongoTemplate.insert(Document.parse(jsonToInsert), ACSP_PROFILE_COLLECTION);
        mongoTemplate.insert(Document.parse(jsonToInsertTwo), ACSP_PROFILE_COLLECTION);

        final ACSPProfileListAggregate expected = getFilingHistoryListAggregateOneDocument();
        expected.getDocumentList().getFirst().getData().category("incorporation");

        // when
        final ACSPProfileListAggregate actual = repository.findCompanyFilingHistory(COMPANY_NUMBER,
                START_INDEX, DEFAULT_ITEMS_PER_PAGE, List.of("incorporation"));

        // then
        assertEquals(expected, actual);
    }

    @Test
    void testAggregationQueryToFindDocumentsWithLargeStartIndex() {
        for (int i = 0; i < TOTAL_RESULTS_NUMBER; i++) {
            ACSPProfileDocument ACSPProfileDocument = new ACSPProfileDocument();
            ACSPProfileDocument.transactionId(TRANSACTION_ID + i);
            ACSPProfileDocument.companyNumber(COMPANY_NUMBER);
            mongoTemplate.insert(ACSPProfileDocument);
        }

        // when
        final ACSPProfileListAggregate actual = repository.findCompanyFilingHistory(COMPANY_NUMBER,
                20, DEFAULT_ITEMS_PER_PAGE, List.of());

        // then
        assertEquals(TOTAL_RESULTS_NUMBER, actual.getTotalCount());
        assertEquals(TRANSACTION_ID + 20, actual.getDocumentList().getFirst().getTransactionId());
        assertEquals(DEFAULT_ITEMS_PER_PAGE, actual.getDocumentList().size());
    }

    @Test
    void testAggregationQueryToFindDocumentsWithStartIndexHigherThanItemsPerPage() {
        for (int i = 0; i < TOTAL_RESULTS_NUMBER; i++) {
            ACSPProfileDocument ACSPProfileDocument = new ACSPProfileDocument();
            ACSPProfileDocument.transactionId(TRANSACTION_ID + i);
            ACSPProfileDocument.companyNumber(COMPANY_NUMBER);
            mongoTemplate.insert(ACSPProfileDocument);
        }

        // when
        final ACSPProfileListAggregate actual = repository.findCompanyFilingHistory(COMPANY_NUMBER,
                60, DEFAULT_ITEMS_PER_PAGE, List.of());

        // then
        assertEquals(TOTAL_RESULTS_NUMBER, actual.getTotalCount());
        assertTrue(actual.getDocumentList().isEmpty());
    }

    @Test
    void testAggregateQueryWhenNoDocumentsInDatabase() {
        // given

        // when
        final ACSPProfileListAggregate actual = repository.findCompanyFilingHistory(COMPANY_NUMBER,
                START_INDEX, DEFAULT_ITEMS_PER_PAGE, List.of());

        // then
        assertEquals(0, actual.getTotalCount());
        assertTrue(actual.getDocumentList().isEmpty());
    }

    @Test
    void testAggregationQueryToFindDocumentsWithSortingOnDate() {
        for (int i = 0; i < TOTAL_RESULTS_NUMBER; i++) {
            ACSPProfileDocument ACSPProfileDocument = new ACSPProfileDocument();
            ACSPProfileDocument.transactionId(TRANSACTION_ID + i);
            ACSPProfileDocument.companyNumber(COMPANY_NUMBER);
            ACSPProfileDocument.data(new ACSPProfileData().date(Instant.now()));
            mongoTemplate.insert(ACSPProfileDocument);
        }

        // when
        final ACSPProfileListAggregate actual = repository.findCompanyFilingHistory(COMPANY_NUMBER,
                START_INDEX, DEFAULT_ITEMS_PER_PAGE, List.of());

        // then
        assertEquals(TOTAL_RESULTS_NUMBER, actual.getTotalCount());
        assertEquals("transactionId54", actual.getDocumentList().getFirst().getTransactionId());
    }

    @Test
    void testAggregationQueryToFindDocumentsWithSortingAndPaginationOnVeryLargeDataSet() {
        List<ACSPProfileDocument> documentList = new ArrayList<>();
        for (int i = 0; i < 500_000; i++) {
            ACSPProfileDocument ACSPProfileDocument = new ACSPProfileDocument();
            ACSPProfileDocument.transactionId(TRANSACTION_ID + i);
            ACSPProfileDocument.companyNumber(COMPANY_NUMBER);
            ACSPProfileDocument.data(new ACSPProfileData().date(Instant.now().plusMillis(i)));
            documentList.add(ACSPProfileDocument);
        }
        mongoTemplate.insert(documentList, ACSP_PROFILE_COLLECTION);

        // when
        final ACSPProfileListAggregate actual = repository.findCompanyFilingHistory(COMPANY_NUMBER,
                499_974, DEFAULT_ITEMS_PER_PAGE, List.of());

        // then
        assertEquals(500_000, actual.getTotalCount());
        assertEquals("transactionId25", actual.getDocumentList().getFirst().getTransactionId());
    }

    @Test
    void testInvalidCompanyNumber() throws IOException {
        // given
        final String jsonToInsert = IOUtils.resourceToString("/mongo_docs/acsp-profile-document.json",
                        StandardCharsets.UTF_8)
                .replaceAll("<id>", TRANSACTION_ID)
                .replaceAll("<company_number>", COMPANY_NUMBER)
                .replaceAll("<category>", OFFICERS_CATEGORY);
        mongoTemplate.insert(Document.parse(jsonToInsert), ACSP_PROFILE_COLLECTION);

        // when
        final Optional<ACSPProfileDocument> actualDocument = repository.findByIdAndCompanyNumber(TRANSACTION_ID,
                "87654321");

        // then
        assertTrue(actualDocument.isEmpty());
    }

    @Test
    void shouldSuccessfullyInsertDocumentById() {
        // given
        final ACSPProfileDocument document = getFilingHistoryDocument(TRANSACTION_ID);

        // when
        repository.save(document);

        // then
        ACSPProfileDocument actual = mongoTemplate.findById(TRANSACTION_ID, ACSPProfileDocument.class);
        assertEquals(document, actual);
    }

    @Test
    void shouldSuccessfullyDeleteDocumentById() throws IOException {
        // given
        final String jsonToInsert = IOUtils.resourceToString("/mongo_docs/acsp-profile-document.json",
                        StandardCharsets.UTF_8)
                .replaceAll("<id>", TRANSACTION_ID)
                .replaceAll("<company_number>", COMPANY_NUMBER)
                .replaceAll("<category>", OFFICERS_CATEGORY);
        mongoTemplate.insert(Document.parse(jsonToInsert), ACSP_PROFILE_COLLECTION);

        // when
        repository.deleteById(TRANSACTION_ID);

        // then
        ACSPProfileDocument actual = mongoTemplate.findById(TRANSACTION_ID, ACSPProfileDocument.class);
        assertNull(actual);
    }

    @Test
    void testDeleteAggregationQueryToCorrectlyMatchOnTopLevelEntityIdAndReturnAggregation() throws IOException {
        // given
        final String jsonToInsert = IOUtils.resourceToString("/mongo_docs/acsp-profile-document.json",
                        StandardCharsets.UTF_8)
                .replaceAll("<id>", TRANSACTION_ID)
                .replaceAll("<entity_id>", TOP_LEVEL_ENTITY_ID)
                .replaceAll("<company_number>", COMPANY_NUMBER)
                .replaceAll("<category>", OFFICERS_CATEGORY);
        mongoTemplate.insert(Document.parse(jsonToInsert), ACSP_PROFILE_COLLECTION);

        // when
        final Optional<ACSPProfileDeleteAggregate> actual = repository.findByEntityId(TOP_LEVEL_ENTITY_ID);

        // then
        assertTrue(actual.isPresent());
        assertEquals(-1, actual.get().getAnnotationIndex());
        assertEquals(-1, actual.get().getResolutionIndex());
        assertEquals(-1, actual.get().getAssociatedFilingIndex());
        assertNotNull(actual.get().getDocument());
    }

    @Test
    void testDeleteAggregationQueryToCorrectlyMatchOnChildAnnotationEntityIdAndReturnAggregation() throws IOException {
        // given
        final String jsonToInsert = IOUtils.resourceToString("/mongo_docs/acsp-profile-document.json",
                        StandardCharsets.UTF_8)
                .replaceAll("<id>", TRANSACTION_ID)
                .replaceAll("<company_number>", COMPANY_NUMBER)
                .replaceAll("<category>", OFFICERS_CATEGORY);
        mongoTemplate.insert(Document.parse(jsonToInsert), ACSP_PROFILE_COLLECTION);

        // when
        final Optional<ACSPProfileDeleteAggregate> actual = repository.findByEntityId(CHILD_ENTITY_ID);

        // then
        assertTrue(actual.isPresent());
        assertEquals(0, actual.get().getAnnotationIndex());
        assertEquals(-1, actual.get().getResolutionIndex());
        assertEquals(-1, actual.get().getAssociatedFilingIndex());
        assertNotNull(actual.get().getDocument());
    }

    @Test
    void testDeleteAggregationQueryToCorrectlyMatchOnChildWithMultipleChildrenAndReturnAggregation()
            throws IOException {
        // given
        String existingDocumentJson = IOUtils.resourceToString(
                "/mongo_docs/resolutions/existing_resolution_doc_with_two_resolutions.json", StandardCharsets.UTF_8);
        existingDocumentJson = existingDocumentJson
                .replaceAll("<transaction_id>", TRANSACTION_ID)
                .replaceAll("<company_number>", COMPANY_NUMBER)
                .replaceAll("<first_resolution_entity_id>", "3333333333")
                .replaceAll("<first_resolution_delta_at>", EXISTING_DELTA_AT)
                .replaceAll("<second_resolution_entity_id>", CHILD_ENTITY_ID)
                .replaceAll("<second_resolution_delta_at>", EXISTING_DELTA_AT_TWO)
                .replaceAll("<resolution_date>", UPDATED_AT)
                .replaceAll("<barcode>", "AOPYXMJN")
                .replaceAll("<updated_at>", UPDATED_AT)
                .replaceAll("<created_at>", UPDATED_AT);
        mongoTemplate.insert(Document.parse(existingDocumentJson), ACSP_PROFILE_COLLECTION);

        // when
        final Optional<ACSPProfileDeleteAggregate> actual = repository.findByEntityId(CHILD_ENTITY_ID);

        // then
        assertTrue(actual.isPresent());
        assertEquals(-1, actual.get().getAnnotationIndex());
        assertEquals(1, actual.get().getResolutionIndex());
        assertEquals(-1, actual.get().getAssociatedFilingIndex());
        assertNotNull(actual.get().getDocument());
    }

    @Test
    void shouldReturnDeleteAggregationWhenAssociatedFilingIdMatched()
            throws IOException {
        // given
        String existingDocumentJson = IOUtils.resourceToString(
                "/mongo_docs/resolutions/expected_certnm_doc_with_nm01_and_res15.json", StandardCharsets.UTF_8);
        existingDocumentJson = existingDocumentJson
                .replaceAll("<parent_entity_id>", ENTITY_ID)
                .replaceAll("<res_entity_id>", "3333333333")
                .replaceAll("<af_entity_id>", CHILD_ENTITY_ID)
                .replaceAll("<updated_at>", UPDATED_AT)
                .replaceAll("<created_at>", UPDATED_AT);
        mongoTemplate.insert(Document.parse(existingDocumentJson), ACSP_PROFILE_COLLECTION);

        // when
        final Optional<ACSPProfileDeleteAggregate> actual = repository.findByEntityId(CHILD_ENTITY_ID);

        // then
        assertTrue(actual.isPresent());
        assertEquals(-1, actual.get().getAnnotationIndex());
        assertEquals(-1, actual.get().getResolutionIndex());
        assertEquals(0, actual.get().getAssociatedFilingIndex());
        assertNotNull(actual.get().getDocument());
    }


    @Test
    void testDeleteAggregationWithNoDocumentsInDatabase() {
        // given

        // when
        final Optional<ACSPProfileDeleteAggregate> actual = repository.findByEntityId(TOP_LEVEL_ENTITY_ID);

        // then
        assertTrue(actual.isEmpty());
    }

    private static ACSPProfileListAggregate getFilingHistoryListAggregateOneDocument() {
        return new ACSPProfileListAggregate()
                .totalCount(1)
                .documentList(
                        List.of(
                                getFilingHistoryDocument(TRANSACTION_ID_TWO)));
    }

    private static ACSPProfileListAggregate getFilingHistoryListAggregate() {
        return new ACSPProfileListAggregate()
                .totalCount(2)
                .documentList(
                        List.of(
                                getFilingHistoryDocument(TRANSACTION_ID),
                                getFilingHistoryDocument(TRANSACTION_ID_TWO)));
    }

    private static ACSPProfileDocument getFilingHistoryDocument(final String transactionId) {
        return new ACSPProfileDocument()
                .transactionId(transactionId)
                .companyNumber(COMPANY_NUMBER)
                .data(new ACSPProfileData()
                        .actionDate(Instant.parse("2014-08-29T00:00:00.000Z"))
                        .category(OFFICERS_CATEGORY)
                        .type("TM01")
                        .description("termination-director-company-with-name-termination-date")
                        .subcategory("termination")
                        .date(Instant.parse("2014-09-15T23:21:18.000Z"))
                        .descriptionValues(new ACSPProfileDescriptionValues()
                                .terminationDate(Instant.parse("2014-08-29T00:00:00.000Z"))
                                .officerName("John Tester"))
                        .annotations(List.of(new ACSPProfileAnnotation()
                                .annotation("Clarification This document was second filed with the CH04 registered on 26/11/2011")
                                .category("annotation")
                                .date(Instant.parse("2011-11-26T11:27:55.000Z"))
                                .description("annotation")
                                .descriptionValues(new ACSPProfileDescriptionValues()
                                        .description("Clarification This document was second filed with the CH04 registered on 26/11/2011"))
                                .type("ANNOTATION")
                                .entityId("2234567890")
                                .deltaAt("20140815230459600643")))
                        .links(new ACSPProfileLinks()
                                .documentMetadata("/document/C1_z-KlM567zSgwJz8uN-UZ3_xnGfCljj3k7L69LxwA")
                                .self("/company/%s/acsp-profile/%s".formatted(COMPANY_NUMBER, transactionId)))
                        .pages(1))
                .barcode("X4BI89B6")
                .deltaAt("20140815230459600643")
                .entityId("1234567890")
                .updated(new ACSPProfileDeltaTimestamp()
                        .at(Instant.parse("2014-09-17T18:52:08.001Z"))
                        .by("5419d856b6a59f32b7684d0e"))
                .created(new ACSPProfileDeltaTimestamp()
                        .at(Instant.parse("2014-09-14T18:52:08.001Z"))
                        .by("5419d856b6a59f32b7684dE4"))
                .originalValues(new ACSPProfileOriginalValues()
                        .officerName("John Tester")
                        .resignationDate("29/08/2014"))
                .originalDescription("Appointment Terminated, Director john tester")
                .documentId("000X4BI89B65846");
    }
}
