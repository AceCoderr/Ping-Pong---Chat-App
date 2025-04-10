package Actors;

import akka.actor.*;

import akka.japi.pf.ReceiveBuilder;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class RoomManagerActor extends AbstractActor {

    public static class Create{}
    public static class JoinRoom{
        public final String roomName;
        public final ActorRef user;
        public JoinRoom(String roomName, ActorRef user)
        {
            this.roomName = roomName;
            this.user = user;
        }
    }
    public static class RoomCreated
    {
        private final String roomName;
        public RoomCreated(String roomName){
            this.roomName = roomName;
        }
        public String getRoomName() {
            return roomName;
        }
    }

    private final ConcurrentHashMap<String,ActorRef> rooms = new ConcurrentHashMap<>();

    public static Props props(){
        return Props.create(RoomManagerActor.class);
    }


    @Override
    public Receive createReceive() {
        return ReceiveBuilder.create()
                .match(Create.class,msg -> {
                    String name = UUID.randomUUID().toString();
                    ActorRef roomRef = getContext().actorOf(RoomActor.props(name), "room-" + name);
                    getContext().watch(roomRef);
                    rooms.put(name, roomRef);
                    getSender().tell(new RoomCreated(name), getSelf());
                })
                .match(JoinRoom.class,msg->{
                    if (!rooms.containsKey(msg.roomName)){
                        msg.user.tell(new Status.Failure(new Exception("room does not exists")),getSelf());
                    }
                    else {
                        rooms.get(msg.roomName).forward(new RoomActor.Join(msg.user),getContext());
                    }
                })
                .build();
    }
}
