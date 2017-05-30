package ro.develbox.model;

import java.io.Serializable;
import javax.persistence.*;

/**
 * The persistent class for the checkpoint_game_definition database table.
 * 
 */
@Entity
@Table(name = "checkpoint_game_definition")
@NamedQuery(name = "CheckpointGameDefinition.findAll", query = "SELECT c FROM CheckpointGameDefinition c")
public class CheckpointGameDefinition implements Serializable {
    private static final long serialVersionUID = 1L;

    private float latitude;

    private float longitude;

    private int pointIdx;

    // bi-directional many-to-one association to GameBaseDefinition
    @ManyToOne
    @JoinColumn(name = "map")
    private GameBaseDefinition gameBaseDefinition;

    public CheckpointGameDefinition() {
    }

    public float getLatitude() {
        return this.latitude;
    }

    public void setLatitude(float latitude) {
        this.latitude = latitude;
    }

    public float getLongitude() {
        return this.longitude;
    }

    public void setLongitude(float longitude) {
        this.longitude = longitude;
    }

    public int getPointIdx() {
        return this.pointIdx;
    }

    public void setPointIdx(int pointIdx) {
        this.pointIdx = pointIdx;
    }

    public GameBaseDefinition getGameBaseDefinition() {
        return this.gameBaseDefinition;
    }

    public void setGameBaseDefinition(GameBaseDefinition gameBaseDefinition) {
        this.gameBaseDefinition = gameBaseDefinition;
    }

}