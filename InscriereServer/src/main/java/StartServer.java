import jdk.jfr.StackTrace;
import model.Inscriere;
import model.Participant;
import model.Proba;
import model.User;
import network.utils.AbstractServer;
import network.utils.InscriereConcurrentServer;
import network.utils.ServerException;
import org.hibernate.Hibernate;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import repository.*;
import services.IInscriereService;
import util.HibernateUtils;
import utils.Pair;
import validator.*;

import java.io.IOException;
import java.util.Properties;

public class StartServer {

    public static void main(String[] args) {
        try {
            initServer();
        } catch (Exception e) {
            System.out.println(e);
        } finally {
            HibernateUtils.close();
        }


    }

    private static void initServer() {
        Properties serverProps = new Properties();
        try {
            serverProps.load(StartServer.class.getResourceAsStream("/server.properties"));
        } catch (IOException e) {
            System.err.println("Eroare la incarcarea fisierului de proprietati");
            return;
        }
//        String propFile = "InscriereServer/src/main/resources/server.properties";
//        Validator<User> userValidator = new UserValidator();
//        Validator<Proba> probaValidator = new ProbaValidator();
//        Validator<Participant> participantValidator = new ParticipantValidator();
//        Validator<Inscriere> inscriereValidator = new InscriereValidator();
//        IUserRepository userRepo = new UserDBRepository(userValidator,"hibernate.cfg.xml");
//        IProbaRepository probaRepo = new ProbaDBRepository(probaValidator, propFile);
//        IParticipantRepository partRepo = new ParticipantDBRepository(participantValidator, propFile);
//        IInscriereRepository insRepo = new InscriereDBRepository(inscriereValidator, propFile);
//
//        IInscriereService service = new InscriereService(partRepo, probaRepo, insRepo, userRepo);
        ApplicationContext factory = new ClassPathXmlApplicationContext("classpath:configurare.xml"); //obtinerea referintei catre un bean din container
        IInscriereService service= factory.getBean(InscriereService.class);
        int serverPort = 5000;
        try {
            serverPort = Integer.parseInt(serverProps.getProperty("server.port"));
        } catch (NumberFormatException e) {
            System.err.println("Port gresit " + e.getMessage());

        }
        System.out.println("Starting server on port: " + serverPort);
        AbstractServer server = new InscriereConcurrentServer(serverPort, service);
        try {
            server.start();
        } catch (ServerException e) {
            System.err.println("Eroare pornire server" + e.getMessage());
        }
    }
}
