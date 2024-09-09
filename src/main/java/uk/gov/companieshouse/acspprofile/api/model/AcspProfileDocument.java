package uk.gov.companieshouse.acspprofile.api.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Objects;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Document(collection = "acsp_profile")
public class AcspProfileDocument {

    @Id
    @JsonProperty("_id")
    private String id;
    private AcspData data;
    @Field("sensitive_data")
    @JsonProperty("sensitive_data")
    private AcspSensitiveData sensitiveData;
    private DeltaTimeStamp created;
    private DeltaTimeStamp updated;
    @Field("delta_at")
    @JsonProperty("delta_at")
    private String deltaAt;

    public String getId() {
        return id;
    }

    public AcspProfileDocument id(String id) {
        this.id = id;
        return this;
    }

    public AcspData getData() {
        return data;
    }

    public AcspProfileDocument data(AcspData data) {
        this.data = data;
        return this;
    }

    public AcspSensitiveData getSensitiveData() {
        return sensitiveData;
    }

    public AcspProfileDocument sensitiveData(AcspSensitiveData sensitiveData) {
        this.sensitiveData = sensitiveData;
        return this;
    }

    public DeltaTimeStamp getCreated() {
        return created;
    }

    public AcspProfileDocument created(DeltaTimeStamp created) {
        this.created = created;
        return this;
    }

    public DeltaTimeStamp getUpdated() {
        return updated;
    }

    public AcspProfileDocument updated(DeltaTimeStamp updated) {
        this.updated = updated;
        return this;
    }

    public String getDeltaAt() {
        return deltaAt;
    }

    public AcspProfileDocument deltaAt(String deltaAt) {
        this.deltaAt = deltaAt;
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
        AcspProfileDocument document = (AcspProfileDocument) o;
        return Objects.equals(id, document.id) && Objects.equals(data, document.data)
                && Objects.equals(sensitiveData, document.sensitiveData) && Objects.equals(created,
                document.created) && Objects.equals(updated, document.updated) && Objects.equals(
                deltaAt, document.deltaAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, data, sensitiveData, created, updated, deltaAt);
    }

    @Override
    public String toString() {
        return "AcspProfileDocument{" +
                "id='" + id + '\'' +
                ", data=" + data +
                ", sensitiveData=" + sensitiveData +
                ", created=" + created +
                ", updated=" + updated +
                ", deltaAt=" + deltaAt +
                '}';
    }
}
