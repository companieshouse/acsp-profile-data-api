package uk.gov.companieshouse.acspprofile.api.model;

import java.time.Instant;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Document
public class AcspProfileDocument {

    @Id
    private String id;
    private AcspData data;
    private DeltaTimeStamp created;
    private DeltaTimeStamp updated;
    @Field("delta_at")
    private Instant deltaAt;

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

    public Instant getDeltaAt() {
        return deltaAt;
    }

    public AcspProfileDocument deltaAt(Instant deltaAt) {
        this.deltaAt = deltaAt;
        return this;
    }
}
