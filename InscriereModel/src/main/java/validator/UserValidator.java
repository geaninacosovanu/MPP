package validator;

import model.User;

public class UserValidator implements Validator<User> {
    @Override
    public void validate(User entity) throws ValidationException {
        String exc="";
        if(entity.getId()==null || entity.getId().length()==0)
            exc+="Id lipsa";

        if(entity.getParola()==null || entity.getParola().length()==0)
            exc+="Parola lipsa";
        if(!exc.isEmpty())
            throw new ValidationException(exc);
    }
}
