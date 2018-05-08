package validator;

import model.Proba;
import org.springframework.stereotype.Component;

@Component
public class ProbaValidator implements Validator<Proba> {
    @Override
    public void validate(Proba entity) throws ValidationException {
        String exc="";
        if(entity.getNume()==null || entity.getNume().length()==0)
            exc+="Nume lipsa";
        if(entity.getDistanta()==null || entity.getDistanta()<=0)
            exc+="Distanta incorecta!";
        if(!exc.isEmpty())
            throw new ValidationException(exc);
    }
}
