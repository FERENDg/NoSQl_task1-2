package org.example.socialNetT1.Controller;

import org.example.socialNetT1.Dto.SubscriptionRequest;
import org.example.socialNetT1.Entity.User;
import org.example.socialNetT1.Exception.UserNotFoundException;
import org.example.socialNetT1.Node.UserNode;
import org.example.socialNetT1.Repository.Neo4jRepository.Neo4jUserRepository;
import org.example.socialNetT1.Service.UserService;
import org.example.socialNetT1.Service.GraphService.UserGraphService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final Neo4jUserRepository neo4jUserRepository;
    @Autowired
    private UserService userService;

    @Autowired
    private UserGraphService userGraphService;

    public UserController(Neo4jUserRepository neo4jUserRepository) {
        this.neo4jUserRepository = neo4jUserRepository;
    }

    @PostMapping("/register")
    public User registerUser(@RequestBody User user) {
        User newUser = userService.register(user);
        userGraphService.createUserInGraph(newUser);
        return newUser;
    }

    @PutMapping("/{userId}/add-friend/{friendId}")
    public User addFriend(@PathVariable String userId, @PathVariable String friendId) {
        User updatedUser = userService.addFriend(userId, friendId);
        userGraphService.createFriendshipInGraph(userId, friendId);
        return updatedUser;
    }

    @DeleteMapping("/{userId}/friends/{friendId}")
    public ResponseEntity<Object> removeFriend(@PathVariable String userId, @PathVariable String friendId) {
        userService.removeFriend(userId, friendId);
        userGraphService.removeFriendshipInGraph(userId, friendId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<Object> removeUser(@PathVariable String userId) {
        userService.deleteUser(userId);
        userGraphService.deleteUserInGraph(userId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/search")
    public User searchUser(@RequestParam String email) {
        return userService.findByEmail(email);
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
