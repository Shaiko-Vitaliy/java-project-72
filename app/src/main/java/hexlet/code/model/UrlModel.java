package hexlet.code.model;

import io.ebean.Model;
import io.ebean.annotation.NotNull;
import io.ebean.annotation.WhenCreated;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Entity
@Table(name = "urls")
public class UrlModel extends Model {

    @Id
    private Long id;
    @NotNull
    @Column(name = "name")
    private final String url;
    @WhenCreated
    private Instant createdAt;
    @OneToMany(mappedBy = "url")
    private List<UrlCheck> urlChecks;

    public UrlModel(String inputUrl) {
        this.url = inputUrl;
    }

    public final Long getId() {
        return id;
    }

    public final String getUrl() {
        return url;
    }

    public final Instant getCreatedAt() {
        return createdAt;
    }

    public final List<UrlCheck> getUrlChecks() {
        return this.urlChecks;
    }

    public final List<UrlCheck> getReverseUrlChecks() {
        var result = this.urlChecks;
        Collections.reverse(result);
        return result;
    }

    public final UrlCheck getLastUrlChecks() {
        List<UrlCheck> tempList = new ArrayList<>(urlChecks);
        Collections.reverse(tempList);
        return tempList.get(0);
    }
}
