package Actors;

import akka.actor.AbstractActor;
import akka.actor.Props;

public class RoomActor extends AbstractActor {

    @Override
    public Receive createReceive() {
        return null;
    }

    public static Props props(String name){

        return Props.create(RoomActor.class);
    }
}
