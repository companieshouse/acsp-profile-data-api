package uk.gov.companieshouse.acspprofile.api.mapper.get;

import static uk.gov.companieshouse.acspprofile.api.mapper.DateUtils.instantToLocalDate;

import org.springframework.stereotype.Component;
import uk.gov.companieshouse.acspprofile.api.model.AcspAddress;
import uk.gov.companieshouse.acspprofile.api.model.AcspData;
import uk.gov.companieshouse.acspprofile.api.model.AcspProfileDocument;
import uk.gov.companieshouse.acspprofile.api.model.AcspSensitiveData;
import uk.gov.companieshouse.api.acspprofile.AcspFullProfile;
import uk.gov.companieshouse.api.acspprofile.AcspFullProfile.StatusEnum;
import uk.gov.companieshouse.api.acspprofile.AcspFullProfile.TypeEnum;
import uk.gov.companieshouse.api.acspprofile.AcspProfile;
import uk.gov.companieshouse.api.acspprofile.Links;
import uk.gov.companieshouse.api.acspprofile.RegisteredOfficeAddress;
import uk.gov.companieshouse.api.acspprofile.RegisteredOfficeAddress.CountryEnum;

@Component
public class AcspGetMapper implements GetMapper {

    private static final String KIND = "authorised-corporate-service-provider-info";

    private final SupervisoryBodiesMapper supervisoryBodiesMapper;

    public AcspGetMapper(SupervisoryBodiesMapper supervisoryBodiesMapper) {
        this.supervisoryBodiesMapper = supervisoryBodiesMapper;
    }

    @Override
    public AcspProfile mapProfile(AcspProfileDocument document) {
        // not implemented
        return null;
    }

    @Override
    public AcspFullProfile mapFullProfile(AcspProfileDocument document) {
        AcspData data = document.getData();
        AcspAddress roa = data.getRegisteredOfficeAddress();
        AcspSensitiveData sensitiveData = document.getSensitiveData(); // TODO
        return new AcspFullProfile()
                .etag("etag")
                .number(data.getAcspNumber())
                .name(data.getName())
                .status(StatusEnum.fromValue(data.getTradingStatus().getValue()))
                .type(TypeEnum.fromValue(data.getType().getValue()))
                .updatedAt(document.getUpdated().getAt().toString())
                .createdAt(document.getCreated().getAt().toString())
                .notifiedFrom(instantToLocalDate(data.getCreatedDate()))
                .deauthorisedFrom(instantToLocalDate(data.getEndDate()))
                .kind(KIND)
                .supervisoryBodies(supervisoryBodiesMapper.map(data))
                .registeredOfficeAddress(new RegisteredOfficeAddress()
                        .careOf(roa.getCareOf())
                        .addressLine1(roa.getAddressLine1())
                        .addressLine2(roa.getAddressLine2())
                        .country(CountryEnum.fromValue(roa.getCountry().getValue()))
                        .locality(roa.getLocality())
                        .poBox(roa.getPoBox())
                        .postalCode(roa.getPostalCode())
                        .premises(roa.getPremises())
                        .region(roa.getRegion()))
                .links(new Links()
                        .self(data.getLinks().getSelf()));
    }
}
