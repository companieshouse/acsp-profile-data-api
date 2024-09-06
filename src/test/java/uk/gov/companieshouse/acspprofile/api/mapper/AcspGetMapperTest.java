package uk.gov.companieshouse.acspprofile.api.mapper;

import static java.time.ZoneOffset.UTC;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.Instant;
import java.time.LocalDate;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.function.Executable;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import uk.gov.companieshouse.acspprofile.api.mapper.get.AcspGetMapper;
import uk.gov.companieshouse.acspprofile.api.model.AcspAddress;
import uk.gov.companieshouse.acspprofile.api.model.AcspData;
import uk.gov.companieshouse.acspprofile.api.model.AcspLinks;
import uk.gov.companieshouse.acspprofile.api.model.AcspProfileDocument;
import uk.gov.companieshouse.acspprofile.api.model.AcspSensitiveData;
import uk.gov.companieshouse.acspprofile.api.model.DeltaTimeStamp;
import uk.gov.companieshouse.acspprofile.api.model.enums.AcspCountry;
import uk.gov.companieshouse.acspprofile.api.model.enums.AcspStatus;
import uk.gov.companieshouse.acspprofile.api.model.enums.AcspType;
import uk.gov.companieshouse.api.acspprofile.AcspProfile;
import uk.gov.companieshouse.api.acspprofile.Links;

@ExtendWith(MockitoExtension.class)
class AcspGetMapperTest {

    private static final String ACSP_NUMBER = "AP123456";
    private static final String SELF_LINK = "/authorised-corporate-service-provider/%s".formatted(ACSP_NUMBER);
    private static final String NAME = "acsp name";
    private static final String UPDATED_AT = "2024-08-25T12:30:00Z";
    private static final String CREATED_AT = "2024-08-23T00:00:00Z";
    private static final LocalDate NOTIFIED_FROM = LocalDate.of(2024, 8, 23);
    private static final LocalDate DEAUTHORISED_FROM = LocalDate.of(2024, 8, 25);
    private static final String KIND = "authorised-corporate-service-provider-info";
    private static final String FULL_PROFILE_KIND = "authorised-corporate-service-provider-full-profile-info";
    private static final String CARE_OF = "Jane Smith";
    private static final String ADDRESS_LINE_1 = "456 Another Street";
    private static final String ADDRESS_LINE_2 = "Floor 2";
    private static final String LOCALITY = "Manchester";
    private static final String PO_BOX = "PO Box 123";
    private static final String POSTAL_CODE = "M1 2AB";
    private static final String PREMISES = "Another Building";
    private static final String REGION = "Greater Manchester";

    @InjectMocks
    private AcspGetMapper mapper;

    @Test
    void shouldMapDocumentToAcspProfileResponse() {
        // given
        AcspData data = new AcspData()
                .acspNumber(ACSP_NUMBER)
                .name(NAME)
                .status(AcspStatus.ACTIVE)
                .type(AcspType.CORPORATE_BODY)
                .notifiedFrom(Instant.from(NOTIFIED_FROM.atStartOfDay(UTC)))
                .deauthorisedFrom(Instant.from(DEAUTHORISED_FROM.atStartOfDay(UTC)))
                .registeredOfficeAddress(new AcspAddress()
                        .careOf(CARE_OF)
                        .addressLine1(ADDRESS_LINE_1)
                        .addressLine2(ADDRESS_LINE_2)
                        .country(AcspCountry.UNITED_KINGDOM)
                        .locality(LOCALITY)
                        .poBox(PO_BOX)
                        .postalCode(POSTAL_CODE)
                        .premises(PREMISES)
                        .region(REGION))
                .links(new AcspLinks()
                        .self(SELF_LINK));

        AcspProfileDocument document = new AcspProfileDocument()
                .id(ACSP_NUMBER)
                .data(data)
                .sensitiveData(new AcspSensitiveData())
                .created(new DeltaTimeStamp()
                        .at(Instant.parse(CREATED_AT)))
                .updated(new DeltaTimeStamp()
                        .at(Instant.parse(UPDATED_AT)))
                .deltaAt("delta_at");

        AcspProfile expected = new AcspProfile()
                .number(ACSP_NUMBER)
                .name(NAME)
                .status(AcspProfile.StatusEnum.ACTIVE)
                .type(AcspProfile.TypeEnum.CORPORATE_BODY)
                .kind(KIND)
                .links(new Links()
                        .self(SELF_LINK));

        // when
        AcspProfile actual = mapper.mapProfile(document);

        // then
        assertEquals(expected, actual);
    }

    @Test
    void shouldNotMapDocumentToAcspFullProfileResponseWhenNotImplemented() {
        // given

        // when
        Executable actual = () -> mapper.mapFullProfile(new AcspProfileDocument());

        // then
        assertThrows(UnsupportedOperationException.class, actual);
    }
}