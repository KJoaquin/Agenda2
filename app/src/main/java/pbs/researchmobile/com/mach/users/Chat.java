package pbs.researchmobile.com.mach.users;

/**
 * Created by delaroy on 4/13/17.
 */
public class Chat {
    public String sender;
    public String urlFoto;
    public String message;
    public long timestamp;
    public String receiver;
    public String senderUid;
    public String receiverUid;
    public String type_mensaje;

    public Chat(){

    }

    public Chat(String sender, String receiver, String senderUid, String receiverUid, String message, long timestamp,String type_mensaje){
        this.sender = sender;
        this.message = message;
        this.receiver = receiver;
        this.senderUid = senderUid;
        this.timestamp = timestamp;
        this.receiverUid = receiverUid;
        this.type_mensaje = type_mensaje;

    }

    public Chat(String sender, String receiver, String senderUid, String receiverUid, String message,String urlFoto, long timestamp,String type_mensaje){
        this.sender = sender;
        this.message = message;
        this.urlFoto = urlFoto;
        this.receiver = receiver;
        this.senderUid = senderUid;
        this.timestamp = timestamp;
        this.receiverUid = receiverUid;
        this.type_mensaje = type_mensaje;

    }
}
