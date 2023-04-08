package hexlet.code.model;

import io.ebean.Model;
import io.ebean.annotation.NotNull;
import io.ebean.annotation.WhenCreated;
import io.ebean.annotation.WhenModified;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.time.Instant;
@Entity
public class UrlModel extends Model {

    @Id
    private int id;
    @NotNull
    private final String url;
    //    @WhenCreated
//    private Instant createdAt;
    @WhenCreated
    private Instant createdAt;

    @WhenModified
    private Instant updatedAt;
    @NotNull
    private int responceCode;


    public UrlModel(String inputUrl, int res) {
        this.url = inputUrl;
        this.responceCode = res;
    }

    public final int getId() {
        return id;
    }

    public final String getUrl() {
        return url;
    }

    public final Instant getCreatedAt() {
        return createdAt;
    }

    public final Instant getUpdatedAt() {
        return updatedAt;
    }

    public final int getResponceCode() {
        return responceCode;
    }
}
