package network.dto;

import java.io.Serializable;

public class ParticipantDTO implements Serializable {
    private String nume;
    private Integer varsta;

    public ParticipantDTO(String nume, Integer varsta) {
        this.nume = nume;
        this.varsta = varsta;
    }

    public String getNume() {
        return nume;
    }

    public void setNume(String nume) {
        this.nume = nume;
    }

    public Integer getVarsta() {
        return varsta;
    }

    public void setVarsta(Integer varsta) {
        this.varsta = varsta;
    }
}
