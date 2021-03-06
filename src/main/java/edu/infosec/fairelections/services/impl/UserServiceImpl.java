package edu.infosec.fairelections.services.impl;

import edu.infosec.fairelections.model.api.UserFactory;
import edu.infosec.fairelections.model.entities.impl.User;
import edu.infosec.fairelections.model.entities.forms.UserCreateForm;
import edu.infosec.fairelections.repository.UserRepository;
import edu.infosec.fairelections.services.api.UserService;
import edu.infosec.fairelections.utils.api.EncryptionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final EncryptionService encryptionService;
    private final UserFactory userFactory;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, EncryptionService encryptionService, UserFactory userFactory) {
        this.userRepository = userRepository;
        this.encryptionService = encryptionService;
        this.userFactory = userFactory;
    }

    @Override
    public Optional<User> getUserById(Long id) {
        return Optional.ofNullable(userRepository.findOne(id));
    }

    @Override
    public Optional<User> getUserByUsername(String username) {
        return userRepository.findOneByUsername(username);
    }

    @Override
    public Collection<User> getAllUsers() {
        return userRepository.findAll(new Sort("username"));
    }

    @Override
    public User create(UserCreateForm form) {
        User user = userFactory.createUser(form.getUsername(),
                encryptionService.getEncoder().encode(form.getPassword()), form.getUserRole());
        return userRepository.save(user);
    }
}
