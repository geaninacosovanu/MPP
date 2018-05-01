package network.protobuffProtocol;

import model.User;

public class ProtoUtils {
    public static InscriereProtobufs.InscriereRequest createLoginRequest(User user){
        InscriereProtobufs.User user1=InscriereProtobufs.User.newBuilder().setUsername(user.getId()).setPassword(user.getParola()).build();
        InscriereProtobufs.InscriereRequest request= InscriereProtobufs.InscriereRequest.newBuilder().setType(InscriereProtobufs.InscriereRequest.Type.Login)
                .setUser(user1).build();
        return request;
    }

    public static String getError(InscriereProtobufs.InscriereResponse response) {
        String errorMessage=response.getError();
        return errorMessage;
    }
}
