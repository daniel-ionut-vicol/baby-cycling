package ro.develbox.model;

import java.io.Serializable;
import javax.persistence.*;
import java.util.List;

/**
 * The persistent class for the games database table.
 * 
 */
@Entity
@Table(name = "games")
@NamedQuery(name = "Game.findAll", query = "SELECT g FROM Game g")
public class Game implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    private String name;

    // bi-directional many-to-one association to GameBaseDefinition
    @OneToMany(mappedBy = "game")
    private List<GameBaseDefinition> gameBaseDefinitions;

    public Game() {
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

    public GameBaseDefinition addGameBaseDefinition(GameBaseDefinition gameBaseDefinition) {
        getGameBaseDefinitions().add(gameBaseDefinition);
        gameBaseDefinition.setGame(this);

        return gameBaseDefinition;
    }

    public GameBaseDefinition removeGameBaseDefinition(GameBaseDefinition gameBaseDefinition) {
        getGameBaseDefinitions().remove(gameBaseDefinition);
        gameBaseDefinition.setGame(null);

        return gameBaseDefinition;
    }

}