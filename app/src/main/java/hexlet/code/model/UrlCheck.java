package hexlet.code.model;

import io.ebean.Model;
import io.ebean.annotation.WhenCreated;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.time.Instant;

@Entity
@Table(name = "url_checks")
public class UrlCheck extends Model {
    @Id
    private Long id;
    @Column(name = "status_code")
    private int statusCode;
    @WhenCreated
    private Instant createdAt;
    private String title;
    private String h1;
    @Column(columnDefinition = "TEXT")
    private String description;
    @ManyToOne
    @JoinColumn(name = "url_id", referencedColumnName = "id")
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
