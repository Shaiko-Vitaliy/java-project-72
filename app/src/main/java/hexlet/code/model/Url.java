package hexlet.code.model;

import io.ebean.Model;
import io.ebean.annotation.WhenCreated;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.time.Instant;

@Entity
public class Url extends Model {
    @Id
    private int id;
    private String name;
    @WhenCreated
    private Instant createdAt;


    public Url(String inputName) {
        this.name = inputName;
    }

    public final int getId() {
        return id;
    }

    public final String getName() {
        return name;
    }

    public final Instant getCreatedAt() {
        return createdAt;
    }
}
