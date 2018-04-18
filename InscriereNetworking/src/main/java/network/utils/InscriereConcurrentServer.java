package network.utils;

import network.protocol.InscriereClientWorker;
import services.IInscriereService;

import java.net.Socket;

public class InscriereConcurrentServer extends AbstractConcurrentServer{
    private IInscriereService service;

    public InscriereConcurrentServer(int port, IInscriereService service) {
        super(port);
        this.service = service;
        System.out.println("Concurrent Server RPC");
    }

    @Override
    protected Thread createWorker(Socket client) {
        InscriereClientWorker worker=new InscriereClientWorker(service, client);
        Thread tw=new Thread(worker);
        return tw;
    }
}
