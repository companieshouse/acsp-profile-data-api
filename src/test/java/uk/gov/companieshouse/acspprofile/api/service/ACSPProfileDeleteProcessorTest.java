package uk.gov.companieshouse.acspprofile.api.service;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.function.Executable;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import uk.gov.companieshouse.acspprofile.api.exception.NotFoundException;
import uk.gov.companieshouse.acspprofile.api.mapper.delete.DeleteMapperDelegator;
import uk.gov.companieshouse.acspprofile.api.model.mongo.ACSPProfileDeleteAggregate;
import uk.gov.companieshouse.acspprofile.api.model.mongo.ACSPProfileDocument;

@ExtendWith(MockitoExtension.class)
class ACSPProfileDeleteProcessorTest {


    private static final String ENTITY_ID = "transactionId";

    @InjectMocks
    private ACSPProfileDeleteProcessor ACSPProfileDeleteProcessor;
    @Mock
    private ACSPProfileService ACSPProfileService;
    @Mock
    private DeleteMapperDelegator deleteMapperDelegator;

    @Mock
    private ACSPProfileDeleteAggregate deleteAggregate;
    @Mock
    private ACSPProfileDocument existingDocument;
    @Mock
    private ACSPProfileDocument updatedDocument;

    @Test
    void shouldCallDeleteWhenParentDocumentRemoved() {
        // given
        when(ACSPProfileService.findFilingHistoryByEntityId(any())).thenReturn(Optional.of(deleteAggregate));
        when(deleteMapperDelegator.delegateDelete(any(), any())).thenReturn(Optional.empty());
        when(deleteAggregate.getDocument()).thenReturn(existingDocument);

        // when
        ACSPProfileDeleteProcessor.processFilingHistoryDelete(ENTITY_ID);

        // then
        verify(ACSPProfileService).findFilingHistoryByEntityId(ENTITY_ID);
        verify(deleteMapperDelegator).delegateDelete(ENTITY_ID, deleteAggregate);
        verify(ACSPProfileService).deleteExistingFilingHistory(existingDocument);
    }

    @Test
    void shouldCallUpdateWhenResolutionRemovedFromComposite() {
        // given
        when(ACSPProfileService.findFilingHistoryByEntityId(any())).thenReturn(Optional.of(deleteAggregate));
        when(deleteMapperDelegator.delegateDelete(any(), any())).thenReturn(Optional.of(updatedDocument));
        when(deleteAggregate.getDocument()).thenReturn(existingDocument);

        // when
        ACSPProfileDeleteProcessor.processFilingHistoryDelete(ENTITY_ID);

        // then
        verify(ACSPProfileService).findFilingHistoryByEntityId(ENTITY_ID);
        verify(deleteMapperDelegator).delegateDelete(ENTITY_ID, deleteAggregate);
        verify(ACSPProfileService).updateFilingHistory(updatedDocument, existingDocument);
    }

    @Test
    void shouldThrowNotFoundExceptionWhenCannotFindDocumentInDB() {
        // given
        when(ACSPProfileService.findFilingHistoryByEntityId(any())).thenReturn(Optional.empty());

        // when
        Executable executable = () -> ACSPProfileDeleteProcessor.processFilingHistoryDelete(ENTITY_ID);

        // then
        assertThrows(NotFoundException.class, executable);
        verify(ACSPProfileService).findFilingHistoryByEntityId(ENTITY_ID);
        verifyNoInteractions(deleteMapperDelegator);
    }
}
