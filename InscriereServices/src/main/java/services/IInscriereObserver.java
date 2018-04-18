package services;

import model.Inscriere;

public interface IInscriereObserver {
    void inscriereAdded() throws InscriereServiceException;
}
