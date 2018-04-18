package network.dto;

import model.Proba;

import java.io.Serializable;
import java.util.List;

public class InscriereDTO implements Serializable {
    private String nume;
    private Integer varsta;
    private List<Proba> probe;
    private boolean existent;

    public InscriereDTO(String nume, Integer varsta, List<Proba> probe, boolean existent) {
        this.nume = nume;
        this.varsta = varsta;
        this.probe = probe;
        this.existent = existent;
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

    public List<Proba> getProbe() {
        return probe;
    }

    public void setProbe(List<Proba> probe) {
        this.probe = probe;
    }

    public boolean isExistent() {
        return existent;
    }

    public void setExistent(boolean existent) {
        this.existent = existent;
    }
}
