package uk.gov.companieshouse.acspprofile.api.mapper;

import static java.time.ZoneOffset.UTC;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.Instant;
import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import uk.gov.companieshouse.acspprofile.api.model.AcspAddress;
import uk.gov.companieshouse.acspprofile.api.model.AcspAmlDetails;
import uk.gov.companieshouse.acspprofile.api.model.AcspData;
import uk.gov.companieshouse.acspprofile.api.model.AcspLinks;
import uk.gov.companieshouse.acspprofile.api.model.AcspProfileDocument;
import uk.gov.companieshouse.acspprofile.api.model.AcspSensitiveData;
import uk.gov.companieshouse.acspprofile.api.model.AcspSoleTraderDetails;
import uk.gov.companieshouse.acspprofile.api.model.DeltaTimeStamp;
import uk.gov.companieshouse.api.acspprofile.AcspFullProfile;
import uk.gov.companieshouse.api.acspprofile.Address;
import uk.gov.companieshouse.api.acspprofile.AmlDetailsItem;
import uk.gov.companieshouse.api.acspprofile.BusinessSector;
import uk.gov.companieshouse.api.acspprofile.InternalAcspApi;
import uk.gov.companieshouse.api.acspprofile.InternalData;
import uk.gov.companieshouse.api.acspprofile.Links;
import uk.gov.companieshouse.api.acspprofile.SoleTraderDetails;
import uk.gov.companieshouse.api.acspprofile.Status;
import uk.gov.companieshouse.api.acspprofile.Type;

@ExtendWith(MockitoExtension.class)
class AcspRequestMapperTest {

    private static final String ACSP_NUMBER = "AP123456";
    private static final String SELF_LINK = "/authorised-corporate-service-providers/%s".formatted(ACSP_NUMBER);
    private static final String NAME = "acsp name";
    private static final LocalDate NOTIFIED_FROM = LocalDate.of(2024, 8, 23);
    private static final LocalDate DEAUTHORISED_FROM = LocalDate.of(2024, 8, 25);
    private static final LocalDate DATE_OF_BIRTH = LocalDate.of(2000, 8, 27);
    private static final String STATUS = "active";
    private static final String TYPE = "corporate-body";
    private static final String ETAG = "etag";
    private static final String EMAIL = "email";
    private static final String BUSINESS_SECTOR = "financial-institutions";
    private static final String DELTA_AT = "20241003085145522153";
    private static final Instant CREATED_AT = Instant.parse("2024-08-23T00:00:00Z");
    private static final String CREATED_BY = "context_id";
    private static final String DELTA_TYPE = "acsp_delta";
    private static final DeltaTimeStamp CREATED = new DeltaTimeStamp()
            .at(CREATED_AT)
            .by(CREATED_BY)
            .type(DELTA_TYPE);
    private static final Instant UPDATED_AT = Instant.parse("2024-09-24T12:00:00Z");
    private static final String UPDATED_BY = "updated_context_id";

    @InjectMocks
    private AcspRequestMapper requestMapper;
    @Mock
    private AddressMapper addressMapper;
    @Mock
    private SoleTraderDetailsMapper soleTraderDetailsMapper;
    @Mock
    private AmlDetailsMapper amlDetailsMapper;
    @Mock
    private InstantSupplier instantSupplier;

    @Mock
    private AcspAddress expectedAddress;
    @Mock
    private Address address;
    @Mock
    private AcspSoleTraderDetails expectedSoleTraderDetails;
    @Mock
    private SoleTraderDetails soleTraderDetails;
    @Mock
    private AcspAmlDetails expectedAmlDetails;
    @Mock
    private AmlDetailsItem amlDetailsItem;

    @Test
    void shouldMapInternalAcspRequestToNewDocument() {
        // given
        AcspFullProfile acspFullProfile = getAcspFullProfileWithRequiredFields()
                .businessSector(BusinessSector.FINANCIAL_INSTITUTIONS)
                .deauthorisedFrom(DEAUTHORISED_FROM)
                .serviceAddress(address)
                .soleTraderDetails(soleTraderDetails)
                .amlDetails(List.of(amlDetailsItem))
                .dateOfBirth(DATE_OF_BIRTH);

        InternalAcspApi internalAcspRequest = getInternalAcspRequest(acspFullProfile, CREATED_BY);

        AcspData data = getExpectedDataWithRequiredFields()
                .deauthorisedFrom(Instant.from(DEAUTHORISED_FROM.atStartOfDay(UTC)))
                .businessSector(BUSINESS_SECTOR)
                .serviceAddress(expectedAddress)
                .soleTraderDetails(expectedSoleTraderDetails)
                .amlDetails(List.of(expectedAmlDetails));
        AcspSensitiveData sensitiveData = new AcspSensitiveData()
                .email(EMAIL)
                .dateOfBirth(Instant.from(DATE_OF_BIRTH.atStartOfDay(UTC)));
        AcspProfileDocument expected = getExpectedDocument(data, sensitiveData, CREATED);

        when(addressMapper.mapAddressRequest(any())).thenReturn(expectedAddress);
        when(soleTraderDetailsMapper.mapSoleTraderDetailsRequest(any())).thenReturn(expectedSoleTraderDetails);
        when(amlDetailsMapper.mapAmlDetailsRequest(any())).thenReturn(List.of(expectedAmlDetails));
        when(instantSupplier.get()).thenReturn(CREATED_AT);

        // when
        AcspProfileDocument actual = requestMapper.mapNewAcsp(internalAcspRequest);

        // then
        assertEquals(expected, actual);
        verify(addressMapper, times(2)).mapAddressRequest(address);
        verify(soleTraderDetailsMapper).mapSoleTraderDetailsRequest(soleTraderDetails);
        verify(amlDetailsMapper).mapAmlDetailsRequest(List.of(amlDetailsItem));
    }

    @Test
    void shouldMapInternalAcspRequestToNewDocumentWithOnlyRequiredFields() {
        // given
        AcspFullProfile acspFullProfile = getAcspFullProfileWithRequiredFields();
        InternalAcspApi internalAcspRequest = getInternalAcspRequest(acspFullProfile, CREATED_BY);

        AcspData data = getExpectedDataWithRequiredFields();
        AcspSensitiveData sensitiveData = new AcspSensitiveData().email(EMAIL);
        AcspProfileDocument expected = getExpectedDocument(data, sensitiveData, CREATED);

        when(addressMapper.mapAddressRequest(any())).thenReturn(expectedAddress, (AcspAddress) null);
        when(soleTraderDetailsMapper.mapSoleTraderDetailsRequest(any())).thenReturn(null);
        when(amlDetailsMapper.mapAmlDetailsRequest(any())).thenReturn(null);
        when(instantSupplier.get()).thenReturn(CREATED_AT);

        // when
        AcspProfileDocument actual = requestMapper.mapNewAcsp(internalAcspRequest);

        // then
        assertEquals(expected, actual);
        verify(addressMapper).mapAddressRequest(address);
        verify(addressMapper).mapAddressRequest(null);
        verify(soleTraderDetailsMapper).mapSoleTraderDetailsRequest(null);
        verify(amlDetailsMapper).mapAmlDetailsRequest(null);
    }

    @Test
    void shouldMapInternalAcspRequestToNewDocumentWithExistingCreatedTimestamp() {
        // given
        AcspFullProfile acspFullProfile = getAcspFullProfileWithRequiredFields();
        InternalAcspApi internalAcspRequest = getInternalAcspRequest(acspFullProfile, UPDATED_BY);

        AcspProfileDocument existingDocument = new AcspProfileDocument()
                .created(CREATED)
                .version(0L);

        AcspData data = getExpectedDataWithRequiredFields();
        AcspSensitiveData sensitiveData = new AcspSensitiveData().email(EMAIL);
        DeltaTimeStamp updated = new DeltaTimeStamp()
                .at(UPDATED_AT)
                .by(UPDATED_BY)
                .type(DELTA_TYPE);
        AcspProfileDocument expected = getExpectedDocument(data, sensitiveData, updated)
                .version(0L);

        when(addressMapper.mapAddressRequest(any())).thenReturn(expectedAddress, (AcspAddress) null);
        when(soleTraderDetailsMapper.mapSoleTraderDetailsRequest(any())).thenReturn(null);
        when(amlDetailsMapper.mapAmlDetailsRequest(any())).thenReturn(null);
        when(instantSupplier.get()).thenReturn(UPDATED_AT);

        // when
        AcspProfileDocument actual = requestMapper.mapExistingAcsp(internalAcspRequest, existingDocument);

        // then
        assertEquals(expected, actual);
        verify(addressMapper).mapAddressRequest(address);
        verify(addressMapper).mapAddressRequest(null);
        verify(soleTraderDetailsMapper).mapSoleTraderDetailsRequest(null);
        verify(amlDetailsMapper).mapAmlDetailsRequest(null);
    }

    private InternalAcspApi getInternalAcspRequest(AcspFullProfile acspFullProfile, String updatedBy) {
        return new InternalAcspApi()
                .acspFullProfile(acspFullProfile)
                .internalData(new InternalData()
                        .deltaAt(DELTA_AT)
                        .updatedBy(updatedBy)
                        .updatedType(DELTA_TYPE));
    }

    private AcspFullProfile getAcspFullProfileWithRequiredFields() {
        return new AcspFullProfile()
                .etag(ETAG)
                .number(ACSP_NUMBER)
                .name(NAME)
                .status(Status.ACTIVE)
                .type(Type.CORPORATE_BODY)
                .notifiedFrom(NOTIFIED_FROM)
                .registeredOfficeAddress(address)
                .email(EMAIL)
                .links(new Links()
                        .self(SELF_LINK));
    }

    private AcspProfileDocument getExpectedDocument(AcspData data, AcspSensitiveData sensitiveData,
            DeltaTimeStamp updatedAt) {
        return new AcspProfileDocument()
                .id(ACSP_NUMBER)
                .data(data)
                .sensitiveData(sensitiveData)
                .deltaAt(DELTA_AT)
                .created(CREATED)
                .updated(updatedAt);
    }

    private AcspData getExpectedDataWithRequiredFields() {
        return new AcspData()
                .etag(ETAG)
                .acspNumber(ACSP_NUMBER)
                .name(NAME)
                .status(STATUS)
                .type(TYPE)
                .notifiedFrom(Instant.from(NOTIFIED_FROM.atStartOfDay(UTC)))
                .registeredOfficeAddress(expectedAddress)
                .links(new AcspLinks()
                        .self(SELF_LINK));
    }
}