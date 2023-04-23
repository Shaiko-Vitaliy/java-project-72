package hexlet.code.model;

import io.ebean.Model;
import io.ebean.annotation.NotNull;
import io.ebean.annotation.WhenCreated;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Entity
public class UrlModel extends Model {

    @Id
    @GeneratedValue
    private Long id;
    @NotNull
    private final String url;
    @WhenCreated
    private Instant createdAt;
    @OneToMany(cascade = CascadeType.ALL)
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
        return urlChecks;
    }

    public final UrlCheck getLastUrlChecks() {
        List<UrlCheck> tempList = new ArrayList<>(urlChecks);
        Collections.reverse(tempList);
        return tempList.get(0);
    }
}
