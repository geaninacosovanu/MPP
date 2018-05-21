import network.utils.AbstractServer;
import network.utils.InscriereAMSConcurrentServer;
import network.utils.ServerException;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class StartAMSServer {
    public static void main(String[] args) {
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("configurare.xml");
        AbstractServer server=context.getBean("chatTCPServer", InscriereAMSConcurrentServer.class);
        try {
            server.start();
        } catch (ServerException e) {
            System.err.println("Error starting the server" + e.getMessage());
        }
    }
}
