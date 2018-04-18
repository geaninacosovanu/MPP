package repository;


import model.Participant;

public interface IParticipantRepository extends IRepository<String, Participant> {
    Participant getParticipant(String nume,Integer varsta);
}
