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

    public Long getId() {
        return id;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public String getTitle() {
        return title;
    }

    public String getH1() {
        return h1;
    }

    public String getDescription() {
        return description;
    }

    public UrlModel getUrl() {
        return url;
    }
}
