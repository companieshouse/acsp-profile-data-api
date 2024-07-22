package uk.gov.companieshouse.acspprofile.api.mapper.upsert;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.function.Executable;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import uk.gov.companieshouse.api.filinghistory.InternalData;
import uk.gov.companieshouse.api.filinghistory.InternalFilingHistoryApi;
import uk.gov.companieshouse.acspprofile.api.exception.ConflictException;
import uk.gov.companieshouse.acspprofile.api.model.mongo.ACSPProfileAnnotation;
import uk.gov.companieshouse.acspprofile.api.model.mongo.ACSPProfileData;

@ExtendWith(MockitoExtension.class)
class ChildListMapperTest {

    private static final String ENTITY_ID = "entity ID";
    private static final String EXISTING_DELTA_AT = "20240315120619000000";
    private static final String NEW_DELTA_AT = "20240422120619000000";
    private static final String STALE_DELTA_AT = "20230315120619000000";

    @InjectMocks
    private ChildListMapper<ACSPProfileAnnotation> childListMapper;
    @Mock
    private ChildMapper<ACSPProfileAnnotation> childMapper;
    @Mock
    private ACSPProfileAnnotation existingAnnotation;
    @Mock
    private ACSPProfileAnnotation updatedAnnotation;

    @Test
    void shouldUpdateExistingChildOnExistingListWhenSameEntityId() {
        // given
        ACSPProfileData existingData = new ACSPProfileData()
                .annotations(new ArrayList<>(List.of(existingAnnotation)));

        InternalFilingHistoryApi request = new InternalFilingHistoryApi()
                .internalData(new InternalData()
                        .entityId(ENTITY_ID)
                        .deltaAt(NEW_DELTA_AT));

        when(existingAnnotation.getEntityId()).thenReturn(ENTITY_ID);
        when(existingAnnotation.getDeltaAt()).thenReturn(EXISTING_DELTA_AT);

        // when
        childListMapper.mapChildList(request, existingData.getAnnotations(), existingData::annotations);

        // then
        verify(childMapper).mapChild(request, existingAnnotation);
        verify(existingAnnotation).getEntityId();
    }

    @Test
    void shouldThrowConflictExceptionWhenStaleDeltaAt() {
        // given
        ACSPProfileData existingData = new ACSPProfileData()
                .annotations(new ArrayList<>(List.of(existingAnnotation)));

        InternalFilingHistoryApi request = new InternalFilingHistoryApi()
                .internalData(new InternalData()
                        .entityId(ENTITY_ID)
                        .deltaAt(STALE_DELTA_AT));

        when(existingAnnotation.getEntityId()).thenReturn(ENTITY_ID);
        when(existingAnnotation.getDeltaAt()).thenReturn(EXISTING_DELTA_AT);

        // when
        Executable actual = () -> childListMapper.mapChildList(request, existingData.getAnnotations(),
                existingData::annotations);

        // then
        assertThrows(ConflictException.class, actual);
        verify(existingAnnotation).getEntityId();
        verifyNoInteractions(childMapper);
    }

    @Test
    void shouldAddNewChildToExistingListWhenDifferentEntityId() {
        // given
        ACSPProfileData actual = new ACSPProfileData()
                .annotations(new ArrayList<>(List.of(existingAnnotation)));

        InternalFilingHistoryApi request = new InternalFilingHistoryApi()
                .internalData(new InternalData()
                        .entityId("different entity ID")
                        .deltaAt(NEW_DELTA_AT));

        ACSPProfileData expected = new ACSPProfileData()
                .annotations(new ArrayList<>(List.of(existingAnnotation, updatedAnnotation)));

        when(existingAnnotation.getEntityId()).thenReturn(ENTITY_ID);
        when(childMapper.mapChild(any())).thenReturn(updatedAnnotation);

        // when
        childListMapper.mapChildList(request, actual.getAnnotations(), actual::annotations);

        // then
        assertEquals(expected, actual);
        verify(childMapper).mapChild(request);
        verify(existingAnnotation, times(2)).getEntityId();
        verifyNoMoreInteractions(existingAnnotation);
    }

    @Test
    void shouldAddNewChildToExistingListWhenExistingChildMissingEntityId() {
        // given
        ACSPProfileData existingData = new ACSPProfileData()
                .annotations(new ArrayList<>(List.of(existingAnnotation)));

        InternalFilingHistoryApi request = new InternalFilingHistoryApi()
                .internalData(new InternalData()
                        .entityId("different entity ID")
                        .deltaAt(NEW_DELTA_AT));

        // when
        childListMapper.mapChildList(request, existingData.getAnnotations(), existingData::annotations);

        // then
        verify(childMapper).mapChild(request);
        verify(existingAnnotation, times(2)).getEntityId();
        verifyNoMoreInteractions(existingAnnotation);
    }

    @Test
    void shouldAddNewChildToNewListWhenNoChildExists() {
        // given
        ACSPProfileData actual = new ACSPProfileData();

        InternalFilingHistoryApi request = new InternalFilingHistoryApi()
                .internalData(new InternalData()
                        .entityId("different entity ID")
                        .deltaAt(NEW_DELTA_AT));

        ACSPProfileData expected = new ACSPProfileData()
                .annotations(List.of(updatedAnnotation));

        when(childMapper.mapChild(any())).thenReturn(updatedAnnotation);

        // when
        childListMapper.mapChildList(request, actual.getAnnotations(), actual::annotations);

        // then
        assertEquals(expected, actual);
        verify(childMapper).mapChild(request);
        verifyNoInteractions(existingAnnotation);
    }
}