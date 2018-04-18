package network.protocol;

import model.Inscriere;
import model.User;
import network.dto.InscriereDTO;
import network.dto.ParticipantDTO;
import services.IInscriereObserver;
import services.IInscriereService;
import services.InscriereServiceException;
import validator.ValidationException;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class InscriereClientWorker implements Runnable, IInscriereObserver {
    private IInscriereService service;
    private Socket connection;

    private ObjectInputStream input;
    private ObjectOutputStream output;
    private volatile boolean connected;


    private static Response okResponse = new Response.Builder().type(ResponseType.OK).build();

    public InscriereClientWorker(IInscriereService service, Socket connection) {
        this.service = service;
        this.connection = connection;
        try {
            output = new ObjectOutputStream(connection.getOutputStream());
            output.flush();
            input = new ObjectInputStream(connection.getInputStream());
            connected = true;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        while (connected) {
            try {
                Object request = input.readObject();
                Response response = handleRequest((Request) request);
                if (response != null) {
                    sendResponse(response);
                }
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        try {
            input.close();
            output.close();
            connection.close();
        } catch (IOException e) {
            System.out.println("Error " + e);
        }
    }

    private void sendResponse(Response response) throws IOException {
        System.out.println("sending response " + response);
        output.writeObject(response);
        output.flush();
    }

    private Response handleRequest(Request request) {
        Response response = null;
        if (request.getType() == RequestType.LOGIN) {
            System.out.println("Login request..." + request.getType());
            User user = (User) request.getData();
            boolean exists;
            try {
                exists = service.login(user.getId(), user.getParola(),this);
                return new Response.Builder().type(ResponseType.OK).data(exists).build();
            } catch (InscriereServiceException e) {
                connected = false;
                return new Response.Builder().type(ResponseType.ERROR).data(e.getMessage()).build();

            }
        }
        if (request.getType()==RequestType.LOGOUT){
            System.out.println("Logout request");
            connected=false;
            return okResponse;

        }
        if (request.getType() == RequestType.GET_ALLPROBEDTO) {
            System.out.println("GetAllProbe Request...");
            try {
                return new Response.Builder().type(ResponseType.GET_ALLPROBEDTO).data(service.getAllProba()).build();
            } catch (InscriereServiceException e) {
                return new Response.Builder().type(ResponseType.ERROR).data(e.getMessage()).build();
            }
        }
        if (request.getType() == RequestType.GET_PARTICIPANTIDTO) {
            System.out.println("GetAllParticipantiDTO Request...");
            try {
                return new Response.Builder().type(ResponseType.GET_PARTICIPANTIDTO).data(service.getParticipanti((Integer) request.getData())).build();
            } catch (InscriereServiceException e) {
                return new Response.Builder().type(ResponseType.ERROR).data(e.getMessage()).build();
            }
        }
        if (request.getType() == RequestType.GET_PARTICIPANT) {
            System.out.println("GetAllParticipant Request...");
            try {
                return new Response.Builder().type(ResponseType.GET_PARTICIPANT).data(service.getParticipant(((ParticipantDTO) request.getData()).getNume(), ((ParticipantDTO) request.getData()).getVarsta())).build();
            } catch (InscriereServiceException e) {
                return new Response.Builder().type(ResponseType.ERROR).data(e.getMessage()).build();
            }
        }
        if (request.getType() == RequestType.ADD_INSCRIERE) {
            System.out.println("AddInscriere Request...");
            try {
                InscriereDTO dto = (InscriereDTO) request.getData();
                service.saveInscriere(dto.getNume(), dto.getVarsta(), dto.getProbe(), dto.isExistent());
                return okResponse;
            } catch (InscriereServiceException e) {
                return new Response.Builder().type(ResponseType.ERROR).data(e.getMessage()).build();
            } catch (ValidationException e) {
                return new Response.Builder().type(ResponseType.ERROR).data(e.getMessage()).build();

            }
        }
            return response;
        }

        @Override
        public void inscriereAdded () throws InscriereServiceException {
            Response resp = new Response.Builder().type(ResponseType.NEW_INSCRIERE).build();
            System.out.println("Inscriere adaugata...");
            try {
                sendResponse(resp);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
