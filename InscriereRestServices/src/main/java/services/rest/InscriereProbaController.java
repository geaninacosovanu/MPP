package services.rest;

import model.Proba;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import repository.IProbaRepository;
import repository.RepositoryException;
import validator.ValidationException;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/inscriere/probe")
public class InscriereProbaController {
    @Autowired
    private IProbaRepository repository;

    @RequestMapping(method = RequestMethod.GET)
    public List<Proba> getAll(){
        List<Proba> all =new ArrayList<>();
        repository.findAll().forEach(p->all.add(p));
        return all;
    }
    @RequestMapping(value="/{id}",method = RequestMethod.GET)
    public ResponseEntity<?> getById(@PathVariable String id){
        Proba p =repository.findOne(Integer.parseInt(id));
        if(p==null)
            return new ResponseEntity<String>("Proba not found",HttpStatus.NOT_FOUND);
        else
            return new ResponseEntity<Proba>(p,HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<?> create(@RequestBody Proba proba){
        try {
            repository.save(proba);
            return new ResponseEntity<Proba>(proba,HttpStatus.OK);
        }
        catch (ValidationException e){
            return new ResponseEntity<String>(e.getMessage(),HttpStatus.BAD_REQUEST);
        }
    }

    @RequestMapping(method = RequestMethod.PUT)
    public ResponseEntity<?> update(@RequestBody Proba proba){
        try {
            repository.update(proba.getId(),proba);
            return new ResponseEntity<Proba>(proba,HttpStatus.OK);
        }
        catch (ValidationException e){
            return new ResponseEntity<String>(e.getMessage(),HttpStatus.BAD_REQUEST);
        }
        catch (RepositoryException e){
            return new ResponseEntity<String>(e.getMessage(),HttpStatus.BAD_REQUEST);
        }
    }

    @RequestMapping(value="/{id}",method = RequestMethod.DELETE)
    ResponseEntity<?> delete(@PathVariable Integer id){
        try {
            repository.delete(id);
            return new ResponseEntity<Proba>(HttpStatus.OK);
        }catch (RepositoryException ex){
            return new ResponseEntity<String>(ex.getMessage(),HttpStatus.BAD_REQUEST);
        }
    }
}
