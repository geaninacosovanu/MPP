package model;

import utils.Pair;

import java.io.Serializable;

public class Inscriere implements HasId<Pair<String,Integer>>,Serializable {
    private String idParticipant;
    private Integer idProba;

    public Inscriere(String idParticipant, Integer idProba) {
        this.idParticipant = idParticipant;
        this.idProba = idProba;
    }

    @Override
    public Pair<String, Integer> getId() {
        return new Pair<>(idParticipant,idProba);
    }

    @Override
    public void setId(Pair<String, Integer> stringIntegerPair) {
        this.idParticipant=stringIntegerPair.getFirst();
        this.idProba=stringIntegerPair.getSecond();
    }

    public String getIdParticipant() {
        return idParticipant;
    }

    public void setIdParticipant(String idParticipant) {
        this.idParticipant = idParticipant;
    }

    public Integer getIdProba() {
        return idProba;
    }

    public void setIdProba(Integer idProba) {
        this.idProba = idProba;
    }
}
