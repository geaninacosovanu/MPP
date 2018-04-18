package services.dto;

import model.Participant;
import model.Proba;

import java.io.Serializable;
import java.util.List;

public class ParticipantProbeDTO implements Serializable {
    private Participant participant;
    private List<Proba> probe;

    public ParticipantProbeDTO(Participant participant, List<Proba> probe) {
        this.participant = participant;
        this.probe = probe;
    }

    public List<Proba> getProbe() {
        return probe;
    }

    public void setProbe(List<Proba> probe) {
        this.probe = probe;
    }
    public String getParticipantId(){
        return participant.getId();
    }
    public String getParticipantNume(){
        return participant.getNume();
    }

    public Integer getParticipantVarsta() {
        return participant.getVarsta();
    }
}
