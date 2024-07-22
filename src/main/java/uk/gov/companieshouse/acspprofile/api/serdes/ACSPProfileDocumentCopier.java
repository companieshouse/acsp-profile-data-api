package uk.gov.companieshouse.acspprofile.api.serdes;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;
import uk.gov.companieshouse.acspprofile.api.ACSPProfileApplication;
import uk.gov.companieshouse.acspprofile.api.exception.InternalServerErrorException;
import uk.gov.companieshouse.acspprofile.api.logging.DataMapHolder;
import uk.gov.companieshouse.acspprofile.api.model.mongo.ACSPProfileDocument;
import uk.gov.companieshouse.logging.Logger;
import uk.gov.companieshouse.logging.LoggerFactory;

@Component
public class ACSPProfileDocumentCopier implements ObjectCopier<ACSPProfileDocument> {

    private static final Logger LOGGER = LoggerFactory.getLogger(ACSPProfileApplication.NAMESPACE);

    private final ObjectMapper objectMapper;

    public ACSPProfileDocumentCopier(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public ACSPProfileDocument deepCopy(ACSPProfileDocument originalDocument) {
        try {
            return objectMapper.readValue(
                    objectMapper.writeValueAsString(originalDocument), ACSPProfileDocument.class);
        } catch (JsonProcessingException ex) {
            LOGGER.error("Failed to serialise/deserialise Filing History document", ex, DataMapHolder.getLogMap());
            throw new InternalServerErrorException("Failed to serialise/deserialise Filing History document");
        }
    }
}
