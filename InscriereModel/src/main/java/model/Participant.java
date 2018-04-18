package model;

import java.io.Serializable;

public class Participant implements HasId<String>,Serializable {
    private String id;
    private String nume;
    private Integer varsta;

    public Participant(String id, String nume, Integer varsta) {
        this.id = id;
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

    @Override
    public String getId() {
        return id;
    }

    @Override
    public void setId(String s) {
        this.id=s;
    }
}
