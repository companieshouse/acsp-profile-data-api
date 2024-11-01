package uk.gov.companieshouse.acspprofile.api.mapper;

import java.util.Optional;
import org.springframework.stereotype.Component;
import uk.gov.companieshouse.acspprofile.api.model.AcspAddress;
import uk.gov.companieshouse.api.acspprofile.Address;

@Component
public class AddressMapper {

    public Address mapAddressResponse(AcspAddress acspAddress) {
        return Optional.ofNullable(acspAddress)
                .map(address -> new Address()
                        .addressLine1(address.getAddressLine1())
                        .addressLine2(address.getAddressLine2())
                        .careOf(address.getCareOf())
                        .country(address.getCountry())
                        .poBox(address.getPoBox())
                        .locality(address.getLocality())
                        .postalCode(address.getPostalCode())
                        .premises(address.getPremises())
                        .region(address.getRegion()))
                .orElse(null);
    }

    public AcspAddress mapAddressRequest(Address addressRequest) {
        return Optional.ofNullable(addressRequest)
                .map(address -> new AcspAddress()
                        .addressLine1(address.getAddressLine1())
                        .addressLine2(address.getAddressLine2())
                        .careOf(address.getCareOf())
                        .country(address.getCountry())
                        .poBox(address.getPoBox())
                        .locality(address.getLocality())
                        .postalCode(address.getPostalCode())
                        .premises(address.getPremises())
                        .region(address.getRegion()))
                .orElse(null);
    }
}
