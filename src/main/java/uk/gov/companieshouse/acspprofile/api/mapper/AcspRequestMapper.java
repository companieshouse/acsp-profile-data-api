package uk.gov.companieshouse.acspprofile.api.mapper;

import static uk.gov.companieshouse.acspprofile.api.mapper.DateUtils.localDateToInstant;

import org.springframework.stereotype.Component;
import uk.gov.companieshouse.acspprofile.api.model.AcspData;
import uk.gov.companieshouse.acspprofile.api.model.AcspLinks;
import uk.gov.companieshouse.acspprofile.api.model.AcspProfileDocument;
import uk.gov.companieshouse.acspprofile.api.model.AcspSensitiveData;
import uk.gov.companieshouse.acspprofile.api.model.DeltaTimeStamp;
import uk.gov.companieshouse.api.acspprofile.AcspFullProfile;
import uk.gov.companieshouse.api.acspprofile.InternalAcspApi;
import uk.gov.companieshouse.api.acspprofile.InternalData;

@Component
public class AcspRequestMapper implements RequestMapper {

    private static final String DELTA_TYPE = "acsp_delta";

    private final AddressMapper addressMapper;
    private final SoleTraderDetailsMapper soleTraderDetailsMapper;
    private final AmlDetailsMapper amlDetailsMapper;
    private final InstantSupplier instantSupplier;

    public AcspRequestMapper(AddressMapper addressMapper, SoleTraderDetailsMapper soleTraderDetailsMapper,
            AmlDetailsMapper amlDetailsMapper, InstantSupplier instantSupplier) {
        this.addressMapper = addressMapper;
        this.soleTraderDetailsMapper = soleTraderDetailsMapper;
        this.amlDetailsMapper = amlDetailsMapper;
        this.instantSupplier = instantSupplier;
    }

    @Override
    public AcspProfileDocument mapNewAcsp(InternalAcspApi internalAcspApi) {
        DeltaTimeStamp created = new DeltaTimeStamp()
                .at(instantSupplier.get())
                .by(internalAcspApi.getInternalData().getUpdatedBy())
                .type(DELTA_TYPE);
        return mapAcspRequest(internalAcspApi, created);
    }

    @Override
    public AcspProfileDocument mapExistingAcsp(InternalAcspApi internalAcspApi, AcspProfileDocument existingDocument) {
        return mapAcspRequest(internalAcspApi, existingDocument.getCreated());
    }

    private AcspProfileDocument mapAcspRequest(InternalAcspApi internalAcspApi, DeltaTimeStamp created) {
        AcspFullProfile fullProfile = internalAcspApi.getAcspFullProfile();
        InternalData internalData = internalAcspApi.getInternalData();
        return new AcspProfileDocument()
                .id(fullProfile.getNumber())
                .data(new AcspData()
                        .etag(fullProfile.getEtag())
                        .acspNumber(fullProfile.getNumber())
                        .name(fullProfile.getName())
                        .type(fullProfile.getType().getValue())
                        .notifiedFrom(localDateToInstant(fullProfile.getNotifiedFrom()))
                        .deauthorisedFrom(localDateToInstant(fullProfile.getDeauthorisedFrom()))
                        .businessSector(fullProfile.getBusinessSector() != null ?
                                fullProfile.getBusinessSector().getValue() : null)
                        .status(fullProfile.getStatus().getValue())
                        .registeredOfficeAddress(
                                addressMapper.mapAddressRequest(fullProfile.getRegisteredOfficeAddress()))
                        .serviceAddress(addressMapper.mapAddressRequest(fullProfile.getServiceAddress()))
                        .soleTraderDetails(soleTraderDetailsMapper.mapSoleTraderDetailsRequest(
                                fullProfile.getSoleTraderDetails()))
                        .amlDetails(amlDetailsMapper.mapAmlDetailsRequest(fullProfile.getAmlDetails()))
                        .links(new AcspLinks()
                                .self(fullProfile.getLinks().getSelf())))
                .sensitiveData(new AcspSensitiveData()
                        .email(fullProfile.getEmail())
                        .dateOfBirth(localDateToInstant(fullProfile.getDateOfBirth())))
                .deltaAt(internalData.getDeltaAt())
                .created(created)
                .updated(new DeltaTimeStamp()
                        .at(instantSupplier.get())
                        .by(internalData.getUpdatedBy())
                        .type(DELTA_TYPE));
    }
}