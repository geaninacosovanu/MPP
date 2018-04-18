package services.dto;

import model.Proba;

import java.io.Serializable;

public class ProbaDTO implements Serializable {
    private Proba proba;
    private Integer nrParticipanti;

    public ProbaDTO(Proba proba, Integer nrParticipanti) {
        this.proba = proba;
        this.nrParticipanti = nrParticipanti;
    }

    public Proba getProba() {
        return proba;
    }

    public void setProba(Proba proba) {
        this.proba = proba;
    }

    public Integer getNrParticipanti() {
        return nrParticipanti;
    }

    public void setNrParticipanti(Integer nrParticipanti) {
        this.nrParticipanti = nrParticipanti;
    }
    public Integer getIdProba(){
        return proba.getId();
    }
    public String getNumeProba(){
        return proba.getNume();
    }
    public Float getDistantaProba(){
        return proba.getDistanta();
    }
}
