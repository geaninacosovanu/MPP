package network.utils;

import network.protocol.ams.InscriereClientAMSWorker;
import services.IInscriereServiceAMS;

import java.net.Socket;

public class InscriereAMSConcurrentServer extends AbstractConcurrentServer {
    private IInscriereServiceAMS service;
    public InscriereAMSConcurrentServer(int port, IInscriereServiceAMS service) {
        super(port);
        this.service = service;
        System.out.println("Inscriere - InscriereAMSConcurrentServer port "+port);
    }

    @Override
    protected Thread createWorker(Socket client) {
        InscriereClientAMSWorker worker=new InscriereClientAMSWorker(service, client);
        Thread tw=new Thread(worker);
        return tw;
    }
}
