package Actors;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;

import akka.japi.pf.ReceiveBuilder;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.stereotype.Service;


import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;


@Service
public class RoomManagerActor extends AbstractActor {
    public static class Create{}
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

    private ActorSystem actorSystem = ActorSystem.create("ChatSystem");
    public ActorRef server = actorSystem.actorOf(RoomManagerActor.props(),"chatserver");
    ObjectMapper objectMapper = new ObjectMapper();

    private final ConcurrentHashMap<String,ActorRef> rooms = new ConcurrentHashMap<>();



    @Override
    public Receive createReceive() {
        return ReceiveBuilder()
                .match(Create.class,msg -> {
                    String name = UUID.randomUUID().toString();
                    ActorRef roomRef = getContext().actorOf(RoomActor.props(name), "room-" + name);
                    getContext().watch(roomRef);
                    rooms.put(name, roomRef);
                    getSender().tell(new RoomCreated(name), getSelf());
                }).build();
    }

    public static Props props(){
        return Props.create(RoomManagerActor.class);
    }
}
