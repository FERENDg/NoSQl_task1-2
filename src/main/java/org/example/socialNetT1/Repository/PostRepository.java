package org.example.socialNetT1.Repository;

import org.example.socialNetT1.Entity.Post;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface PostRepository extends MongoRepository<Post, String> {
    List<Post> findAllByOrderByCreatedAtDesc();
    List<Post> findByAuthorId(String authorId);
}

