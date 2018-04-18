package validator;

import model.Participant;

public class ParticipantValidator implements Validator<Participant> {
    @Override
    public void validate(Participant entity) throws ValidationException {
        String exc="";
        if(entity.getId()==null || entity.getNume().length()==0)
            exc+="Id lipsa";
        if(entity.getNume()==null || entity.getNume().length()==0)
            exc+="Nume lipsa";
        if(entity.getVarsta()==null || entity.getVarsta()<=0)
            exc+="Varsta incorecta";
        if(!exc.isEmpty())
            throw new ValidationException(exc);
    }
}