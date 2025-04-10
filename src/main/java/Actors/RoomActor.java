package Actors;

import akka.actor.AbstractActor;
import akka.actor.Actor;
import akka.actor.ActorRef;
import akka.actor.Props;

import java.util.HashSet;
import java.util.Set;

public class RoomActor extends AbstractActor {

    private final String roomName;
    private final Set<ActorRef> users = new HashSet<>();

    public RoomActor(String roomName){
        this.roomName = roomName;
    }

    public static class Join{
        public final ActorRef user;
        public Join(ActorRef user)
        {
            this.user = user;
        }
    }

    @Override
    public Receive createReceive() {
        return null;
    }

    public static Props props(String name){
        return Props.create(RoomActor.class);
    }
}
