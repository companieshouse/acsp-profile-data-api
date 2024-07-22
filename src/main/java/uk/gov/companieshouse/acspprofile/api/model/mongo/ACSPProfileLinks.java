package uk.gov.companieshouse.acspprofile.api.model.mongo;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Objects;
import org.springframework.data.mongodb.core.mapping.Field;

public class ACSPProfileLinks {

    private String self;
    @Field("document_metadata")
    @JsonProperty("document_metadata")
    private String documentMetadata;

    public String getSelf() {
        return self;
    }

    public ACSPProfileLinks self(String self) {
        this.self = self;
        return this;
    }

    public String getDocumentMetadata() {
        return documentMetadata;
    }

    public ACSPProfileLinks documentMetadata(String documentMetadata) {
        this.documentMetadata = documentMetadata;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ACSPProfileLinks that = (ACSPProfileLinks) o;
        return Objects.equals(self, that.self) && Objects.equals(documentMetadata, that.documentMetadata);
    }

    @Override
    public int hashCode() {
        return Objects.hash(self, documentMetadata);
    }

    @Override
    public String toString() {
        return "FilingHistoryLinks{" +
                "self='" + self + '\'' +
                ", documentMetadata='" + documentMetadata + '\'' +
                '}';
    }
}
