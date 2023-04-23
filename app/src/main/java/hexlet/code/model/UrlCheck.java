package hexlet.code.model;

import io.ebean.Model;
import io.ebean.annotation.WhenCreated;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import java.time.Instant;

@Entity
public class UrlCheck extends Model {
    @Id
    @GeneratedValue
    private Long id;
    private int statusCode;
    @WhenCreated
    private Instant createdAt;
    private String title;
    private String h1;
    @Lob
    private String description;
    @ManyToOne
    private UrlModel url;

    public UrlCheck(int inputStatusCode, String inputTitle, String inputH1, String inputDescription,
                    UrlModel inputUrl) {
        this.statusCode = inputStatusCode;
        this.title = inputTitle;
        this.h1 = inputH1;
        this.description = inputDescription;
        this.url = inputUrl;
    }

    public final Long getId() {
        return id;
    }

    public final int getStatusCode() {
        return statusCode;
    }

    public final Instant getCreatedAt() {
        return createdAt;
    }

    public final String getTitle() {
        return title;
    }

    public final String getH1() {
        return h1;
    }

    public final String getDescription() {
        return description;
    }

    public final UrlModel getUrl() {
        return url;
    }
}
