package Actors;

import akka.actor.AbstractActor;
import akka.actor.Actor;
import akka.actor.ActorRef;
import akka.actor.Props;
import org.springframework.web.socket.WebSocketSession;

public class ChatClientActor extends AbstractActor {
    private final WebSocketSession session;
    private final ActorRef roomManager;
    private ActorRef room;

    public ChatClientActor(WebSocketSession session,ActorRef roomManager){
        this.session = session;
        this.roomManager = roomManager;
    }

    public static Props props(WebSocketSession session, ActorRef roomMangerActor){
        return Props.create(ChatClientActor.class,() -> new ChatClientActor(session,roomMangerActor));
    }

    @Override
    public void preStart() throws Exception {
        String roomName = session.getUri().getQuery().split("=")[1];
        roomManager.tell(new RoomManagerActor.JoinRoom(roomName,getSelf()),getSelf());
    }

    @Override
    public Receive createReceive() {
        return null;
    }
}
