package validator;

import model.Inscriere;

public class InscriereValidator implements Validator<Inscriere> {
    @Override
    public void validate(Inscriere entity) throws ValidationException {
        String exc="";
        if(entity.getIdParticipant()==null || entity.getIdParticipant().length()==0)
            exc+="Id participant lipsa";
        if(entity.getIdProba()==null || entity.getIdProba()<0)
            exc+="Id proba lipsa";
        if(!exc.isEmpty())
            throw new ValidationException(exc);
    }
}
