package services;

import model.Participant;
import model.Proba;
import model.User;
import services.dto.ParticipantProbeDTO;
import services.dto.ProbaDTO;
import validator.ValidationException;

import java.util.List;

public interface IInscriereServiceAMS {
    boolean login(String username, String parola) throws InscriereServiceException;
    void logout(User user) throws InscriereServiceException;
    List<ProbaDTO> getAllProba() throws InscriereServiceException;
    List<ParticipantProbeDTO> getParticipanti(Integer idProba) throws InscriereServiceException;
    void saveInscriere(String nume, Integer varsta, List<Proba> probe, boolean existent) throws ValidationException, InscriereServiceException;
    Participant getParticipant(String nume, Integer varsta) throws InscriereServiceException;
}
