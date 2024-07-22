package uk.gov.companieshouse.acspprofile.api.mapper.delete;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import uk.gov.companieshouse.acspprofile.api.model.mongo.ACSPProfileData;
import uk.gov.companieshouse.acspprofile.api.model.mongo.ACSPProfileDeleteAggregate;
import uk.gov.companieshouse.acspprofile.api.model.mongo.ACSPProfileDocument;
import uk.gov.companieshouse.acspprofile.api.model.mongo.ACSPProfileResolution;

@SpringBootTest
class DeleteMapperDelegatorAspectFeatureEnabledIT {

    private static final String ENTITY_ID = "entity ID";
    private static final String COMPOSITE_RES_TYPE = "RESOLUTIONS";

    @Autowired
    private DeleteMapperDelegator deleteMapperDelegator;
    @MockBean
    private CompositeResolutionDeleteMapper compositeResolutionDeleteMapper;

    @Test
    void shouldDeleteChildTransactionsWhenFeatureEnabled() {
        ACSPProfileDeleteAggregate aggregate = new ACSPProfileDeleteAggregate()
                .resolutionIndex(1)
                .document(new ACSPProfileDocument()
                        .entityId(ENTITY_ID)
                        .data(new ACSPProfileData()
                                .type(COMPOSITE_RES_TYPE)
                                .resolutions(List.of(
                                        new ACSPProfileResolution()
                                                .entityId("first ID"),
                                        new ACSPProfileResolution()
                                                .entityId(ENTITY_ID)))));

        when(compositeResolutionDeleteMapper.removeTransaction(anyInt(), any())).thenReturn(
                Optional.of(new ACSPProfileDocument()));

        // when
        Optional<ACSPProfileDocument> actual = deleteMapperDelegator.delegateDelete(ENTITY_ID, aggregate);

        // then
        assertTrue(actual.isPresent());
        verify(compositeResolutionDeleteMapper).removeTransaction(1, aggregate.getDocument());
    }
}
