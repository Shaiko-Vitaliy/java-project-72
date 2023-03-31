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


    public Url(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }
}



//    В нашем приложении мы будем добавлять в базу данных другие сайты,
//        а точнее их адреса. Для этого создайте модель Url со свойствами id, name, createdAt.
//        Свойство id — идентификатор урла, должно генерироваться автоматически.
//        Поле createdAt отражает дату и время добавления урла на сайт и также должно заполняться автоматически