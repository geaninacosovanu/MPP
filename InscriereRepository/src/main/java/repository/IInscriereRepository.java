package repository;

import model.Inscriere;
import model.Participant;
import model.Proba;
import utils.Pair;

public interface IInscriereRepository extends IRepository<Pair<String,Integer>, Inscriere> {
    Integer getNrParticipantiProba(Integer idProba);
    Iterable<Participant> getParticipantiPtProba(Integer idProba);
    Iterable<Proba> getProbePtParticipant(String idParticipant);
}
