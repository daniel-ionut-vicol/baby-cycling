package ro.develbox.model;

import java.io.Serializable;
import javax.persistence.*;
import java.util.List;

/**
 * The persistent class for the groups database table.
 * 
 */
@Entity
@Table(name = "groups")
@NamedQuery(name = "Group.findAll", query = "SELECT g FROM Group g")
public class Group implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    private String name;

    // bi-directional many-to-many association to GameBaseDefinition
    @ManyToMany(mappedBy = "groups")
    private List<GameBaseDefinition> gameBaseDefinitions;

    // bi-directional many-to-many association to User
    @ManyToMany(mappedBy = "groups")
    private List<User> users;

    public Group() {
    }

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<GameBaseDefinition> getGameBaseDefinitions() {
        return this.gameBaseDefinitions;
    }

    public void setGameBaseDefinitions(List<GameBaseDefinition> gameBaseDefinitions) {
        this.gameBaseDefinitions = gameBaseDefinitions;
    }

    public List<User> getUsers() {
        return this.users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }

}