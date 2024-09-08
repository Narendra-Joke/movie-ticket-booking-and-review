package com.root.service;

import com.root.domain.User;
import com.root.repository.UserRepository;
import com.root.resource.UserResource;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Slf4j
public class UserService {
    @Autowired
    private UserRepository userRepository;

    public UserResource addUser(UserResource userResource) {
        if (userRepository.existsByMobile(userResource.getMobile())) {
            return userResource;
        }

        User user = User.toEntity(userResource);

        user = userRepository.save(user);

        log.info("Added New User" + user.toString());

        return User.toResource(user);

    }

    public UserResource getUser(Long id) {

        Optional<User> userEntity = userRepository.findById(id);

        if (userEntity.isEmpty()) {
            log.error("User not found for id: " + id);
            throw new EntityNotFoundException("User Not Found with ID: " + id);
        }

        return User.toResource(userEntity.get());
    }
}
