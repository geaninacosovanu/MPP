package server;

import model.Inscriere;
import model.Participant;
import model.Proba;
import model.User;
import repository.*;
import services.IInscriereObserver;
import services.IInscriereService;
import services.InscriereServiceException;
import services.dto.ParticipantProbeDTO;
import services.dto.ProbaDTO;
import validator.ValidationException;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class InscriereService implements IInscriereService {
    private IParticipantRepository participantRepository;
    private IProbaRepository probaRepository;
    private IInscriereRepository inscriereRepository;
    private IUserRepository userRepository;
    private Map<String, IInscriereObserver> loggedClients;

    private final int nrThreaduri = 10;

    public InscriereService(IParticipantRepository participantRepository, IProbaRepository probaRepository, IInscriereRepository inscriereRepository, IUserRepository userRepository) {
        this.participantRepository = participantRepository;
        this.probaRepository = probaRepository;
        this.inscriereRepository = inscriereRepository;
        this.userRepository = userRepository;
        loggedClients = new ConcurrentHashMap<>();
    }

    private void notifyInscriereAdded() {
        ExecutorService executor = Executors.newFixedThreadPool(nrThreaduri);
        for(IInscriereObserver obs:loggedClients.values()){
            executor.execute(() -> {
            System.out.println("Notifying client inscriere added...");
                try {
                    obs.inscriereAdded();
                } catch (InscriereServiceException e) {
                    System.out.println(e.getMessage());
                }
            });

        }
        executor.shutdown();
    }

    @Override
    public synchronized void logout(User user, IInscriereObserver client)  {
        loggedClients.remove(user.getId());
    }

    @Override
    public synchronized boolean login(String username, String parola,IInscriereObserver client) {
        loggedClients.put(username, client);
        return userRepository.exists(new User(username, parola));
    }
    @Override
    public synchronized List<ProbaDTO> getAllProba() {
        List<ProbaDTO> all = new LinkedList<>();
        probaRepository.findAll().forEach(e -> all.add(new ProbaDTO(e, inscriereRepository.getNrParticipantiProba(e.getId()))));
        return all;
    }


    @Override
    public synchronized List<ParticipantProbeDTO> getParticipanti(Integer idProba) {
        List<ParticipantProbeDTO> all = new LinkedList<>();
        List<Proba> probe;
        for (Participant e : inscriereRepository.getParticipantiPtProba(idProba)) {
            probe = new ArrayList<>();
            for (Proba p : inscriereRepository.getProbePtParticipant(e.getId()))
                probe.add(p);
            all.add(new ParticipantProbeDTO(e, probe));
        }
        return all;
    }

    @Override
    public synchronized void saveInscriere(String nume, Integer varsta, List<Proba> probe,boolean existent) throws ValidationException, InscriereServiceException {
        Participant p = null;
        if (existent == false && getParticipant(nume, varsta) == null) {
            Integer id;
            Random rand = new Random();
            do {
                id = rand.nextInt(200) + 1;
            } while (participantRepository.findOne(id.toString()) != null);
            p = new Participant(id.toString(), nume, varsta);
            participantRepository.save(p);

        } else if (existent == true && getParticipant(nume, varsta) != null)
            p = getParticipant(nume, varsta);
        else if (existent == true && getParticipant(nume, varsta) == null)
            throw new RepositoryException("Participantul nu exista!");


        for (Proba pr : probe) {
            try {
                inscriereRepository.save(new Inscriere(p.getId(), pr.getId()));

            } catch (ValidationException e) {
                //ShowMessage.showMessage(Alert.AlertType.ERROR, "Eroare", e.getMessage());
            } finally {
                notifyInscriereAdded();
            }
        }

    }




    public synchronized Participant getParticipant(String nume, Integer varsta) {
        return participantRepository.getParticipant(nume, varsta);
    }
    /*private IParticipantRepository participantRepository;
    private IProbaRepository probaRepository;
    private IInscriereRepository inscriereRepository;
    private IUserRepository userRepository;
    //private List<Observer<Inscriere>> inscriereObservers = new ArrayList<>();

    public server.InscriereService(IParticipantRepository participantRepository, IProbaRepository probaRepository, IInscriereRepository inscriereRepository, IUserRepository userRepository) {
        this.participantRepository = participantRepository;
        this.probaRepository = probaRepository;
        this.inscriereRepository = inscriereRepository;
        this.userRepository = userRepository;
    }


    public boolean login(String username, String parola) {
        User user = new User(username, parola);
        return userRepository.exists(user);
    }

    public List<Proba> getAllProba() {
        List<Proba> all = new LinkedList<>();
        probaRepository.findAll().forEach(e -> all.add(e));
        return all;
    }

    public List<Participant> getAllParticipant() {
        List<Participant> all = new LinkedList<>();
        participantRepository.findAll().forEach(e -> all.add(e));
        return all;
    }

    public Integer getNrParticipantiProba(Integer idproba) {
        return inscriereRepository.getNrParticipantiProba(idproba);
    }

    public List<Proba> getProbeParticipant(String idParticipant) {
        List<Proba> all = new LinkedList<>();
        inscriereRepository.getProbePtParticipant(idParticipant).forEach(e -> all.add(e));
        return all;
    }

    public List<Participant> getParticipanti(Integer idProba) {
        List<Participant> all = new LinkedList<>();
        inscriereRepository.getParticipantiPtProba(idProba).forEach(e -> all.add(e));
        return all;
    }

    public void saveInscriere(String nume, Integer varsta, List<Proba> probe, boolean existent) throws ValidationException {
        Participant p = null;
        if (existent == false && getParticipant(nume, varsta) == null) {
            Integer id;
            Random rand = new Random();
            do {
                id = rand.nextInt(200) + 1;
            } while (participantRepository.findOne(id.toString()) != null);
            p = new Participant(id.toString(), nume, varsta);
            participantRepository.save(p);

        } else if (existent == true && getParticipant(nume, varsta) != null)
            p = getParticipant(nume, varsta);
        else if (existent == true && getParticipant(nume, varsta) == null)
            throw new RepositoryException("Participantul nu exista!");


        for (Proba pr : probe) {
            try {
                inscriereRepository.save(new Inscriere(p.getId(), pr.getId()));

            } catch (ValidationException e) {
                //ShowMessage.showMessage(Alert.AlertType.ERROR, "Eroare", e.getMessage());
            } finally {
                //notifyObservers();
            }
        }


    }
    public Participant getParticipant(String nume, Integer varsta) {
        return participantRepository.getParticipant(nume, varsta);
    }*/
}