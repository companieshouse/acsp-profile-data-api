package uk.gov.companieshouse.acspprofile.api.mapper.get;

import java.util.Optional;
import org.springframework.stereotype.Component;
import uk.gov.companieshouse.acspprofile.api.model.AcspAddress;
import uk.gov.companieshouse.api.acspprofile.Address;
import uk.gov.companieshouse.api.acspprofile.Country;

@Component
public class AddressMapper {

    public Address mapAcspAddress(AcspAddress acspAddress) {
        return Optional.ofNullable(acspAddress)
                .map(address -> new Address()
                        .addressLine1(address.getAddressLine1())
                        .addressLine2(address.getAddressLine2())
                        .careOf(address.getCareOf())
                        .country(address.getCountry() != null ? Country.fromValue(address.getCountry()) : null)
                        .poBox(address.getPoBox())
                        .locality(address.getLocality())
                        .postalCode(address.getPostalCode())
                        .premises(address.getPremises())
                        .region(address.getRegion()))
                .orElse(null);
    }
}
