package server;

import model.Inscriere;
import model.Participant;
import model.Proba;
import model.User;
import repository.*;
import services.*;
import services.dto.ParticipantProbeDTO;
import services.dto.ProbaDTO;
import validator.ValidationException;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class InscriereServiceAMS implements IInscriereServiceAMS {
    private IParticipantRepository participantRepository;
    private IProbaRepository probaRepository;
    private IInscriereRepository inscriereRepository;
    private IUserRepository userRepository;
    private Map<String, User> loggedClients;
    private IInscriereNotificationService notificationService;

    //private final int nrThreaduri = 10;

    public InscriereServiceAMS(IParticipantRepository participantRepository, IProbaRepository probaRepository, IInscriereRepository inscriereRepository, IUserRepository userRepository,IInscriereNotificationService notificationService) {
        this.participantRepository = participantRepository;
        this.probaRepository = probaRepository;
        this.inscriereRepository = inscriereRepository;
        this.userRepository = userRepository;
        loggedClients = new ConcurrentHashMap<>();
        this.notificationService=notificationService;
    }

//    private void notifyInscriereAdded() {
//        ExecutorService executor = Executors.newFixedThreadPool(nrThreaduri);
//        for(IInscriereObserver obs:loggedClients.values()){
//            executor.execute(() -> {
//                System.out.println("Notifying client inscriere added...");
//                try {
//                    obs.inscriereAdded();
//                } catch (InscriereServiceException e) {
//                    System.out.println(e.getMessage());
//                }
//            });
//
//        }
//        executor.shutdown();
//    }

    @Override
    public synchronized void logout(User user)  {
        loggedClients.remove(user.getId());
    }

    @Override
    public synchronized boolean login(String username, String parola) {
        loggedClients.put(username, userRepository.findOne(username));
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
                notificationService.inscriereAdded();
            }
        }

    }




    public synchronized Participant getParticipant(String nume, Integer varsta) {
        return participantRepository.getParticipant(nume, varsta);
    }
}
