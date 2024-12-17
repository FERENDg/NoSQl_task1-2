package org.example.socialNetT1.Repository;

import org.example.socialNetT1.Entity.User;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserRepository extends MongoRepository<User, String> {
    User findByEmail(String email);

    User getUserById(String id);
}

