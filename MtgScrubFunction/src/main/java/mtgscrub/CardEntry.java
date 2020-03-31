package mtgscrub;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "card_entry")
public class CardEntry {

    @Id
    @Column(name="id")
    private int id;

    @Column(name="name")
    private String name;

    public CardEntry() {
    }

    public CardEntry(int id, String name) {
        this.id = id;
        this.name = name;
    }

    @Override
    public String toString() {
        return "CardEntry{" + "id=" + id + ", name=" + name + '}';
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
