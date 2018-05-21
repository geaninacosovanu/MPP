import network.utils.AbstractServer;
import network.utils.InscriereConcurrentServer;
import network.utils.ServerException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import server.InscriereService;
import services.IInscriereService;

import java.io.IOException;
import java.util.Properties;

public class StartServer {
    public static void main(String[] args) {
        Properties serverProps = new Properties();
        try {
            serverProps.load(StartServer.class.getResourceAsStream("/server.properties"));
        } catch (IOException e) {
            System.err.println("Eroare la incarcarea fisierului de proprietati");
            return;
        }
        ApplicationContext context = new ClassPathXmlApplicationContext("configurare.xml");
        IInscriereService service = context.getBean(InscriereService.class);
        int serverPort = 5000;
        try {
            serverPort = Integer.parseInt(serverProps.getProperty("server.port"));
        }catch(NumberFormatException e){
            System.err.println("Port gresit "+e.getMessage());

        }
        System.out.println("Starting server on port: "+serverPort);
        AbstractServer server = new InscriereConcurrentServer(serverPort,service);
        try{
            server.start();
        }catch (ServerException e){
            System.err.println("Eroare pornire server" + e.getMessage());
        }
    }
}
