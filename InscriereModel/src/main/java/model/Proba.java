package model;

import java.io.Serializable;

public class Proba implements HasId<Integer>,Serializable{
    private Integer id;
    private String nume;
    private Float distanta;

    public Proba(Integer id, String nume, Float distanta) {
        this.id = id;
        this.nume = nume;
        this.distanta = distanta;
    }

    public String getNume() {
        return nume;
    }

    public void setNume(String nume) {
        this.nume = nume;
    }

    public Float getDistanta() {
        return distanta;
    }

    public void setDistanta(Float distanta) {
        this.distanta = distanta;
    }

    @Override
    public Integer getId() {
        return id;
    }

    @Override
    public void setId(Integer id) {
        this.id=id;
    }

    @Override
    public String toString() {
        return nume + " " + distanta+"\n";
    }
}