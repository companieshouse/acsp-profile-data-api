package uk.gov.companieshouse.acspprofile.api.service;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import java.time.Instant;
import java.util.Optional;
import java.util.function.Supplier;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.function.Executable;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import uk.gov.companieshouse.api.filinghistory.InternalData;
import uk.gov.companieshouse.api.filinghistory.InternalData.TransactionKindEnum;
import uk.gov.companieshouse.api.filinghistory.InternalFilingHistoryApi;
import uk.gov.companieshouse.acspprofile.api.exception.BadRequestException;
import uk.gov.companieshouse.acspprofile.api.exception.ConflictException;
import uk.gov.companieshouse.acspprofile.api.exception.ServiceUnavailableException;
import uk.gov.companieshouse.acspprofile.api.mapper.upsert.AbstractTransactionMapperFactory;
import uk.gov.companieshouse.acspprofile.api.mapper.upsert.AnnotationTransactionMapper;
import uk.gov.companieshouse.acspprofile.api.mapper.upsert.TopLevelTransactionMapper;
import uk.gov.companieshouse.acspprofile.api.model.mongo.ACSPProfileDocument;
import uk.gov.companieshouse.acspprofile.api.serdes.ACSPProfileDocumentCopier;

@ExtendWith(MockitoExtension.class)
class ACSPProfileUpsertProcessorTest {

    private static final String TRANSACTION_ID = "transactionId";
    private static final String COMPANY_NUMBER = "12345678";
    private static final Instant INSTANT = Instant.now();

    @InjectMocks
    private ACSPProfileUpsertProcessor filingHistoryProcessor;

    @Mock
    private ACSPProfileService ACSPProfileService;
    @Mock
    private AbstractTransactionMapperFactory mapperFactory;
    @Mock
    private ValidatorFactory validatorFactory;
    @Mock
    private TopLevelTransactionMapper topLevelMapper;
    @Mock
    private AnnotationTransactionMapper annotationTransactionMapper;
    @Mock
    private Validator<InternalFilingHistoryApi> topLevelPutRequestValidator;
    @Mock
    private ACSPProfileDocumentCopier ACSPProfileDocumentCopier;
    @Mock
    private Supplier<Instant> instantSupplier;
    @Mock
    private InternalFilingHistoryApi request;
    @Mock
    private InternalData internalData;
    @Mock
    private ACSPProfileDocument documentToUpsert;

    @Mock
    private ACSPProfileDocument existingDocument;
    @Mock
    private ACSPProfileDocument existingDocumentCopy;

    @Test
    void shouldSuccessfullyCallSaveWhenInsert() {
        // given
        when(validatorFactory.getPutRequestValidator(any())).thenReturn(topLevelPutRequestValidator);
        when(instantSupplier.get()).thenReturn(INSTANT);
        when(topLevelPutRequestValidator.isValid(any())).thenReturn(true);
        when(request.getInternalData()).thenReturn(internalData);
        when(internalData.getTransactionKind()).thenReturn(TransactionKindEnum.TOP_LEVEL);
        when(mapperFactory.getTransactionMapper(any())).thenReturn(topLevelMapper);
        when(ACSPProfileService.findExistingFilingHistory(any(), any())).thenReturn(Optional.empty());
        when(topLevelMapper.mapNewFilingHistory(anyString(), any(), any())).thenReturn(documentToUpsert);

        // when
        filingHistoryProcessor.processFilingHistory(TRANSACTION_ID, COMPANY_NUMBER, request);

        // then
        verify(mapperFactory).getTransactionMapper(TransactionKindEnum.TOP_LEVEL);
        verify(ACSPProfileService).findExistingFilingHistory(TRANSACTION_ID, COMPANY_NUMBER);
        verifyNoInteractions(ACSPProfileDocumentCopier);
        verify(instantSupplier).get();
        verify(topLevelMapper).mapNewFilingHistory(TRANSACTION_ID, request, INSTANT);
        verifyNoMoreInteractions(topLevelMapper);
        verify(ACSPProfileService).insertFilingHistory(documentToUpsert);
    }

    @Test
    void shouldSuccessfullyCallSaveWhenUpdate() {
        // given
        when(instantSupplier.get()).thenReturn(INSTANT);
        when(validatorFactory.getPutRequestValidator(any())).thenReturn(topLevelPutRequestValidator);
        when(topLevelPutRequestValidator.isValid(any())).thenReturn(true);
        when(request.getInternalData()).thenReturn(internalData);
        when(internalData.getTransactionKind()).thenReturn(TransactionKindEnum.TOP_LEVEL);
        when(mapperFactory.getTransactionMapper(any())).thenReturn(topLevelMapper);
        when(ACSPProfileService.findExistingFilingHistory(any(), any())).thenReturn(Optional.of(existingDocument));
        when(ACSPProfileDocumentCopier.deepCopy(any())).thenReturn(existingDocumentCopy);
        when(topLevelMapper.mapExistingFilingHistory(any(), any(ACSPProfileDocument.class),
                any())).thenReturn(documentToUpsert);

        // when
        filingHistoryProcessor.processFilingHistory(TRANSACTION_ID, COMPANY_NUMBER, request);

        // then
        verify(ACSPProfileService).findExistingFilingHistory(TRANSACTION_ID, COMPANY_NUMBER);
        verify(ACSPProfileDocumentCopier).deepCopy(existingDocument);
        verify(instantSupplier).get();
        verify(topLevelMapper).mapExistingFilingHistory(request, existingDocument, INSTANT);
        verifyNoMoreInteractions(topLevelMapper);
        verify(ACSPProfileService).updateFilingHistory(documentToUpsert, existingDocumentCopy);
    }

    @Test
    void shouldSuccessfullyCallSaveWhenUpdateButStaleDeltaAt() {
        // given
        when(instantSupplier.get()).thenReturn(INSTANT);
        when(validatorFactory.getPutRequestValidator(any())).thenReturn(topLevelPutRequestValidator);
        when(topLevelPutRequestValidator.isValid(any())).thenReturn(true);
        when(request.getInternalData()).thenReturn(internalData);
        when(internalData.getTransactionKind()).thenReturn(TransactionKindEnum.TOP_LEVEL);
        when(mapperFactory.getTransactionMapper(any())).thenReturn(topLevelMapper);
        when(ACSPProfileService.findExistingFilingHistory(any(), any())).thenReturn(Optional.of(existingDocument));
        when(topLevelMapper.mapExistingFilingHistory(any(), any(), any())).thenThrow(ConflictException.class);

        // when
        Executable executable = () -> filingHistoryProcessor.processFilingHistory(TRANSACTION_ID, COMPANY_NUMBER, request);

        // then
        assertThrows(ConflictException.class, executable);
        verify(ACSPProfileService).findExistingFilingHistory(TRANSACTION_ID, COMPANY_NUMBER);
        verify(ACSPProfileDocumentCopier).deepCopy(existingDocument);
        verify(instantSupplier).get();
        verify(topLevelMapper).mapExistingFilingHistory(request, existingDocument, INSTANT);
        verifyNoMoreInteractions(topLevelMapper);
        verifyNoMoreInteractions(ACSPProfileService);
    }

    @Test
    void shouldThrowServiceUnavailableWhenFindingDocumentInDB() {
        // given
        when(instantSupplier.get()).thenReturn(INSTANT);
        when(validatorFactory.getPutRequestValidator(any())).thenReturn(topLevelPutRequestValidator);
        when(topLevelPutRequestValidator.isValid(any())).thenReturn(true);
        when(request.getInternalData()).thenReturn(internalData);
        when(internalData.getTransactionKind()).thenReturn(TransactionKindEnum.TOP_LEVEL);
        when(mapperFactory.getTransactionMapper(any())).thenReturn(topLevelMapper);
        when(ACSPProfileService.findExistingFilingHistory(any(), any())).thenThrow(ServiceUnavailableException.class);

        // when
        Executable executable = () -> filingHistoryProcessor.processFilingHistory(TRANSACTION_ID, COMPANY_NUMBER, request);

        // then
        assertThrows(ServiceUnavailableException.class, executable);
        verify(mapperFactory).getTransactionMapper(TransactionKindEnum.TOP_LEVEL);
        verify(ACSPProfileService).findExistingFilingHistory(TRANSACTION_ID, COMPANY_NUMBER);
        verifyNoInteractions(ACSPProfileDocumentCopier);
        verifyNoInteractions(topLevelMapper);
        verifyNoMoreInteractions(ACSPProfileService);
    }

    @Test
    void shouldThrowBadRequestWhenValidatorReturnsFalse() {
        // given
        when(validatorFactory.getPutRequestValidator(any())).thenReturn(topLevelPutRequestValidator);
        when(topLevelPutRequestValidator.isValid(any())).thenReturn(false);
        when(request.getInternalData()).thenReturn(internalData);
        when(internalData.getTransactionKind()).thenReturn(TransactionKindEnum.TOP_LEVEL);

        // when
        Executable executable = () -> filingHistoryProcessor.processFilingHistory(TRANSACTION_ID, COMPANY_NUMBER, request);

        // then
        assertThrows(BadRequestException.class, executable);
        verifyNoInteractions(mapperFactory);
        verifyNoInteractions(ACSPProfileService);
        verifyNoInteractions(ACSPProfileDocumentCopier);
        verifyNoInteractions(topLevelMapper);
        verifyNoInteractions(annotationTransactionMapper);
        verifyNoInteractions(ACSPProfileService);
    }
}