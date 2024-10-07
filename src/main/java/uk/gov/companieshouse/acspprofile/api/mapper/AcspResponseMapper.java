package uk.gov.companieshouse.acspprofile.api.mapper;

import static uk.gov.companieshouse.acspprofile.api.mapper.DateUtils.instantToLocalDate;

import org.springframework.stereotype.Component;
import uk.gov.companieshouse.acspprofile.api.model.AcspData;
import uk.gov.companieshouse.acspprofile.api.model.AcspProfileDocument;
import uk.gov.companieshouse.acspprofile.api.model.AcspSensitiveData;
import uk.gov.companieshouse.api.acspprofile.AcspFullProfile;
import uk.gov.companieshouse.api.acspprofile.AcspProfile;
import uk.gov.companieshouse.api.acspprofile.BusinessSector;
import uk.gov.companieshouse.api.acspprofile.Links;
import uk.gov.companieshouse.api.acspprofile.Status;
import uk.gov.companieshouse.api.acspprofile.Type;

@Component
public class AcspResponseMapper implements ResponseMapper {

    private static final String PROFILE_KIND = "authorised-corporate-service-provider-info";
    private static final String FULL_PROFILE_KIND = "authorised-corporate-service-provider-full-profile-info";

    private final AddressMapper addressMapper;
    private final SoleTraderDetailsMapper soleTraderDetailsMapper;
    private final AmlDetailsMapper amlDetailsMapper;

    public AcspResponseMapper(AddressMapper addressMapper, SoleTraderDetailsMapper soleTraderDetailsMapper,
            AmlDetailsMapper amlDetailsMapper) {
        this.addressMapper = addressMapper;
        this.soleTraderDetailsMapper = soleTraderDetailsMapper;
        this.amlDetailsMapper = amlDetailsMapper;
    }

    @Override
    public AcspProfile mapProfile(AcspProfileDocument document) {
        AcspData data = document.getData();
        return new AcspProfile()
                .number(data.getAcspNumber())
                .name(data.getName())
                .type(Type.fromValue(data.getType()))
                .kind(PROFILE_KIND)
                .status(Status.fromValue(data.getStatus()))
                .links(new Links()
                        .self(data.getLinks().getSelf()));
    }

    @Override
    public AcspFullProfile mapFullProfile(AcspProfileDocument document) {
        AcspData data = document.getData();
        AcspSensitiveData sensitiveData = document.getSensitiveData();
        return new AcspFullProfile()
                .etag(data.getEtag())
                .number(data.getAcspNumber())
                .name(data.getName())
                .type(Type.fromValue(data.getType()))
                .notifiedFrom(instantToLocalDate(data.getNotifiedFrom()))
                .deauthorisedFrom(instantToLocalDate(data.getDeauthorisedFrom()))
                .businessSector(data.getBusinessSector() != null ?
                        BusinessSector.fromValue(data.getBusinessSector()) : null)
                .kind(FULL_PROFILE_KIND)
                .status(Status.fromValue(data.getStatus()))
                .registeredOfficeAddress(addressMapper.mapAddressResponse(data.getRegisteredOfficeAddress()))
                .serviceAddress(addressMapper.mapAddressResponse(data.getServiceAddress()))
                .soleTraderDetails(soleTraderDetailsMapper.mapSoleTraderDetailsResponse(data.getSoleTraderDetails()))
                .amlDetails(amlDetailsMapper.mapAmlDetailsResponse(data.getAmlDetails()))
                .email(sensitiveData.getEmail())
                .dateOfBirth(instantToLocalDate(sensitiveData.getDateOfBirth()))
                .links(new Links()
                        .self(data.getLinks().getSelf()));
    }
}
