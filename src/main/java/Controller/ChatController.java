package Controller;

import Actors.RoomManagerActor;
import akka.actor.ActorRef;
import akka.pattern.Patterns;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Duration;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/rooms")
public class ChatController {

    @Autowired
    private ActorRef roomManagerActor;

    @GetMapping("/create")
    public CompletableFuture<ResponseEntity<Object>> createRoom(HttpServletResponse response) {
        return Patterns.ask(roomManagerActor, new RoomManagerActor.Create(), Duration.ofSeconds(3))
                .thenApply(result -> {
                    RoomManagerActor.RoomCreated msg = (RoomManagerActor.RoomCreated) result;
                    response.setHeader("Location", "/chat/" + msg.getRoomName());
                    return ResponseEntity.status(HttpStatus.FOUND).build();
                }).toCompletableFuture();
    }
}
