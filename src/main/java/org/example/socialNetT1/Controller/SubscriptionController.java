package org.example.socialNetT1.Controller;

import org.example.socialNetT1.Dto.SubscriptionRequest;
import org.example.socialNetT1.Exception.UserNotFoundException;
import org.example.socialNetT1.Node.UserNode;
import org.example.socialNetT1.Repository.Neo4jRepository.Neo4jUserRepository;
import org.example.socialNetT1.Service.GraphService.UserGraphService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/subscriptions")
public class SubscriptionController {
    private final Neo4jUserRepository neo4jUserRepository;
    private final UserGraphService userGraphService;

    @Autowired
    public SubscriptionController(Neo4jUserRepository neo4jUserRepository, UserGraphService userGraphService) {
        this.neo4jUserRepository = neo4jUserRepository;
        this.userGraphService = userGraphService;
    }

    @PostMapping("/subscribe")
    public ResponseEntity<Void> subscribe(@RequestBody SubscriptionRequest request) {
        userGraphService.subscribe(request.getSubscriberId(), request.getSubscribedUserId());
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/unsubscribe")
    public ResponseEntity<Void> unsubscribe(@RequestBody SubscriptionRequest request) {
        userGraphService.unsubscribe(request.getSubscriberId(), request.getSubscribedUserId());
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{subscriberId}")
    public ResponseEntity<List<UserNode>> getSubscriptions(@PathVariable String subscriberId) {
        UserNode subscriber = neo4jUserRepository.findByUserId(subscriberId)
                .orElseThrow(() -> new UserNotFoundException("User not found: " + subscriberId));
        return ResponseEntity.ok(subscriber.getSubscriptions());
    }
}

