package uk.gov.companieshouse.acspprofile.api.mapper.get;

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
import uk.gov.companieshouse.api.acspprofile.AcspFullProfile;
import uk.gov.companieshouse.api.acspprofile.AcspProfile;
import uk.gov.companieshouse.api.acspprofile.Address;
import uk.gov.companieshouse.api.acspprofile.AmlDetailsItem;
import uk.gov.companieshouse.api.acspprofile.BusinessSector;
import uk.gov.companieshouse.api.acspprofile.Links;
import uk.gov.companieshouse.api.acspprofile.SoleTraderDetails;
import uk.gov.companieshouse.api.acspprofile.Status;
import uk.gov.companieshouse.api.acspprofile.Type;

@ExtendWith(MockitoExtension.class)
class AcspGetMapperTest {

    private static final String ACSP_NUMBER = "AP123456";
    private static final String SELF_LINK = "/authorised-corporate-service-provider/%s".formatted(ACSP_NUMBER);
    private static final String NAME = "acsp name";
    private static final LocalDate NOTIFIED_FROM = LocalDate.of(2024, 8, 23);
    private static final LocalDate DEAUTHORISED_FROM = LocalDate.of(2024, 8, 25);
    private static final LocalDate DATE_OF_BIRTH = LocalDate.of(2000, 8, 27);
    private static final String KIND = "authorised-corporate-service-provider-info";
    private static final String FULL_PROFILE_KIND = "authorised-corporate-service-provider-full-profile-info";
    private static final String STATUS = "active";
    private static final String TYPE = "corporate-body";
    private static final String ETAG = "etag";
    private static final String EMAIL = "email";
    private static final String BUSINESS_SECTOR = "financial-institutions";

    @InjectMocks
    private AcspGetMapper mapper;
    @Mock
    private AddressMapper addressMapper;
    @Mock
    private SoleTraderDetailsMapper soleTraderDetailsMapper;
    @Mock
    private AmlDetailsMapper amlDetailsMapper;

    @Mock
    private AcspAddress acspAddress;
    @Mock
    private Address expectedAddress;
    @Mock
    private AcspSoleTraderDetails acspSoleTraderDetails;
    @Mock
    private SoleTraderDetails expectedSoleTraderDetails;
    @Mock
    private AcspAmlDetails acspAmlDetails;
    @Mock
    private AmlDetailsItem expectedAmlDetailsItem;

    @Test
    void shouldMapDocumentToAcspProfileResponse() {
        // given
        AcspData data = new AcspData()
                .acspNumber(ACSP_NUMBER)
                .name(NAME)
                .status(STATUS)
                .type(TYPE)
                .links(new AcspLinks()
                        .self(SELF_LINK));

        AcspProfileDocument document = new AcspProfileDocument()
                .data(data);

        AcspProfile expected = new AcspProfile()
                .number(ACSP_NUMBER)
                .name(NAME)
                .status(Status.ACTIVE)
                .type(Type.CORPORATE_BODY)
                .kind(KIND)
                .links(new Links()
                        .self(SELF_LINK));

        // when
        AcspProfile actual = mapper.mapProfile(document);

        // then
        assertEquals(expected, actual);
    }

    @Test
    void shouldMapDocumentToAcspFullProfileResponse() {
        // given
        AcspData data = new AcspData()
                .etag(ETAG)
                .acspNumber(ACSP_NUMBER)
                .name(NAME)
                .status(STATUS)
                .type(TYPE)
                .notifiedFrom(Instant.from(NOTIFIED_FROM.atStartOfDay(UTC)))
                .deauthorisedFrom(Instant.from(DEAUTHORISED_FROM.atStartOfDay(UTC)))
                .businessSector(BUSINESS_SECTOR)
                .registeredOfficeAddress(acspAddress)
                .serviceAddress(acspAddress)
                .soleTraderDetails(acspSoleTraderDetails)
                .amlDetails(List.of(acspAmlDetails))
                .links(new AcspLinks()
                        .self(SELF_LINK));

        AcspProfileDocument document = new AcspProfileDocument()
                .data(data)
                .sensitiveData(new AcspSensitiveData()
                        .email(EMAIL)
                        .dateOfBirth(Instant.from(DATE_OF_BIRTH.atStartOfDay(UTC))));

        AcspFullProfile expected = new AcspFullProfile()
                .etag(ETAG)
                .number(ACSP_NUMBER)
                .name(NAME)
                .status(Status.ACTIVE)
                .type(Type.CORPORATE_BODY)
                .kind(FULL_PROFILE_KIND)
                .businessSector(BusinessSector.FINANCIAL_INSTITUTIONS)
                .notifiedFrom(NOTIFIED_FROM)
                .deauthorisedFrom(DEAUTHORISED_FROM)
                .registeredOfficeAddress(expectedAddress)
                .serviceAddress(expectedAddress)
                .soleTraderDetails(expectedSoleTraderDetails)
                .amlDetails(List.of(expectedAmlDetailsItem))
                .email(EMAIL)
                .dateOfBirth(DATE_OF_BIRTH)
                .links(new Links()
                        .self(SELF_LINK));

        when(addressMapper.mapAcspAddress(any())).thenReturn(expectedAddress);
        when(soleTraderDetailsMapper.mapAcspSoleTraderDetails(any())).thenReturn(expectedSoleTraderDetails);
        when(amlDetailsMapper.mapAmlDetails(any())).thenReturn(List.of(expectedAmlDetailsItem));

        // when
        AcspFullProfile actual = mapper.mapFullProfile(document);

        // then
        assertEquals(expected, actual);
        verify(addressMapper, times(2)).mapAcspAddress(acspAddress);
        verify(soleTraderDetailsMapper).mapAcspSoleTraderDetails(acspSoleTraderDetails);
        verify(amlDetailsMapper).mapAmlDetails(List.of(acspAmlDetails));
    }

    @Test
    void shouldMapDocumentWithOnlyRequiredFieldsToAcspFullProfileResponse() {
        // given
        AcspData data = new AcspData()
                .etag(ETAG)
                .acspNumber(ACSP_NUMBER)
                .name(NAME)
                .status(STATUS)
                .type(TYPE)
                .notifiedFrom(Instant.from(NOTIFIED_FROM.atStartOfDay(UTC)))
                .registeredOfficeAddress(acspAddress)
                .links(new AcspLinks()
                        .self(SELF_LINK));

        AcspProfileDocument document = new AcspProfileDocument()
                .data(data)
                .sensitiveData(new AcspSensitiveData()
                        .email(EMAIL));

        AcspFullProfile expected = new AcspFullProfile()
                .etag(ETAG)
                .number(ACSP_NUMBER)
                .name(NAME)
                .status(Status.ACTIVE)
                .type(Type.CORPORATE_BODY)
                .kind(FULL_PROFILE_KIND)
                .notifiedFrom(NOTIFIED_FROM)
                .registeredOfficeAddress(expectedAddress)
                .email(EMAIL)
                .links(new Links()
                        .self(SELF_LINK));

        when(addressMapper.mapAcspAddress(any())).thenReturn(expectedAddress, (Address) null);
        when(soleTraderDetailsMapper.mapAcspSoleTraderDetails(any())).thenReturn(null);
        when(amlDetailsMapper.mapAmlDetails(any())).thenReturn(null);

        // when
        AcspFullProfile actual = mapper.mapFullProfile(document);

        // then
        assertEquals(expected, actual);
        verify(addressMapper).mapAcspAddress(acspAddress);
        verify(addressMapper).mapAcspAddress(null);
        verify(soleTraderDetailsMapper).mapAcspSoleTraderDetails(null);
        verify(amlDetailsMapper).mapAmlDetails(null);
    }
}