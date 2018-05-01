package network.protobuffProtocol;

import model.Inscriere;
import model.Participant;
import model.Proba;
import model.User;
import network.dto.InscriereDTO;
import network.dto.ParticipantDTO;
import services.dto.ParticipantProbeDTO;
import services.dto.ProbaDTO;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class ProtoUtils {
    public static InscriereProtobufs.InscriereRequest createLoginRequest(User user) {
        InscriereProtobufs.User user1 = InscriereProtobufs.User.newBuilder().setUsername(user.getId()).setPassword(user.getParola()).build();
        InscriereProtobufs.InscriereRequest request = InscriereProtobufs.InscriereRequest.newBuilder().setType(InscriereProtobufs.InscriereRequest.Type.Login)
                .setUser(user1).build();
        return request;
    }

    public static String getError(InscriereProtobufs.InscriereResponse response) {
        String errorMessage = response.getError();
        return errorMessage;
    }

    public static InscriereProtobufs.InscriereRequest createGetAllProbaDTORequest() {
        InscriereProtobufs.InscriereRequest request = InscriereProtobufs.InscriereRequest.newBuilder().setType(InscriereProtobufs.InscriereRequest.Type.GetAllProbeDto).build();
        return request;
    }

    public static List<ProbaDTO> getAllProbeDTO(InscriereProtobufs.InscriereResponse response) {
        List<ProbaDTO> all = new LinkedList<>();
        for (int i = 0; i < response.getProbeDtoCount(); i++) {
            InscriereProtobufs.ProbaDTO probaDTO = response.getProbeDto(i);
            all.add(new ProbaDTO(new Proba(probaDTO.getProba().getId(), probaDTO.getProba().getNume(), probaDTO.getProba().getDistanta()), probaDTO.getNrParticipanti()));
        }
        return all;

    }

    public static InscriereProtobufs.InscriereRequest createGetParticipantiRequest(Integer idProba) {
        InscriereProtobufs.InscriereRequest request = InscriereProtobufs.InscriereRequest.newBuilder().setType(InscriereProtobufs.InscriereRequest.Type.GetParticipantiDto).setIdProba(idProba).build();
        return request;

    }

    public static List<ParticipantProbeDTO> getAllParticipantiProbaDTO(InscriereProtobufs.InscriereResponse response) {
        List<ParticipantProbeDTO> all = new LinkedList<>();
        for (int i = 0; i < response.getParticipantProbeDtoCount(); i++) {
            InscriereProtobufs.ParticipantProbeDTO participantProbeDto = response.getParticipantProbeDto(i);
            List<Proba> probe = new ArrayList<>();
            for (int j = 0; j < participantProbeDto.getProbeCount(); j++)
                probe.add(new Proba(participantProbeDto.getProbe(j).getId(), participantProbeDto.getProbe(j).getNume(), participantProbeDto.getProbe(j).getDistanta()));
            all.add(new ParticipantProbeDTO(new Participant(participantProbeDto.getParticipant().getId(), participantProbeDto.getParticipant().getNume(), participantProbeDto.getParticipant().getVarsta()), probe));
        }
        return all;

    }

    public static InscriereProtobufs.InscriereRequest createSaveInscriereRequest(InscriereDTO inscriereDTO) {
        List<InscriereProtobufs.Proba> probe = new ArrayList<>();
        for (Proba p : inscriereDTO.getProbe())
            probe.add(InscriereProtobufs.Proba.newBuilder().setId(p.getId()).setNume(p.getNume()).setDistanta(p.getDistanta()).build());
        InscriereProtobufs.InscriereDTO.Builder builder = InscriereProtobufs.InscriereDTO.newBuilder()
                .setNume(inscriereDTO.getNume())
                .setVarsta(inscriereDTO.getVarsta());
        for (Proba p : inscriereDTO.getProbe()) {
            builder.addProbe( InscriereProtobufs.Proba.newBuilder().setId(p.getId()).setNume(p.getNume()).setDistanta(p.getDistanta()).build());
        }
        InscriereProtobufs.InscriereRequest request = InscriereProtobufs.InscriereRequest.newBuilder().setType(InscriereProtobufs.InscriereRequest.Type.AddInscriere).setInscriere(builder.build()).build();
        return request;

    }


    public static InscriereProtobufs.InscriereRequest createLogoutRequest(User u) {
        InscriereProtobufs.User user = InscriereProtobufs.User.newBuilder().setUsername(u.getId()).setPassword(u.getParola()).build();
        InscriereProtobufs.InscriereRequest request = InscriereProtobufs.InscriereRequest.newBuilder().setType(InscriereProtobufs.InscriereRequest.Type.Logout)
                .setUser(user).build();
        return request;
    }

    public static InscriereProtobufs.InscriereRequest createGetParticipantRequest(ParticipantDTO participantDTO) {
        InscriereProtobufs.Participant p = InscriereProtobufs.Participant.newBuilder().setNume(participantDTO.getNume()).setVarsta(participantDTO.getVarsta()).build();
        InscriereProtobufs.InscriereRequest request = InscriereProtobufs.InscriereRequest.newBuilder().setType(InscriereProtobufs.InscriereRequest.Type.GetParticipant)
                .setParticipant(p).build();
        return request;
    }

    public static Participant getParticipant(InscriereProtobufs.InscriereResponse response) {
        return new Participant(response.getParticipant().getId(), response.getParticipant().getNume(), response.getParticipant().getVarsta());
    }
}
