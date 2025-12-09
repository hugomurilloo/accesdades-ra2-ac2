package accesdades.ra2.ac2.accesdades_ra2_ac2.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import accesdades.ra2.ac2.accesdades_ra2_ac2.model.User;
import accesdades.ra2.ac2.accesdades_ra2_ac2.repository.UserRepository;
import accesdades.ra2.ac2.accesdades_ra2_ac2.logging.CustomLogging;

@Service
public class UserService {
    @Autowired
    UserRepository userRepository;

    @Autowired
    CustomLogging customLogging;

    public List<User> getAllUsers() {
        customLogging.info("UserService", "getAllUsers", "Retrieve all users");
        return userRepository.findAll();
    }

    public User getUserById(Long id) {
        customLogging.info("UserService", "getUserById", "Search user: " + id);
        return userRepository.findById(id);
    }

    public String createUser(@RequestBody User user) {
        customLogging.info("UserService", "createUser", "Create user: " + user.getEmail());
        return userRepository.create(user);
    }

    public String updateUser(Long id, User user) {
        customLogging.info("UserService", "updateUser", "Update user: " + id);
        return userRepository.update(id, user);
    }

    public String updateUserName(Long id, String name) {
        customLogging.info("UserService", "updateUserName", "Update name for user: " + id);
        return userRepository.updateName(id, name);
    }

    public String deleteUser(Long id) {
        customLogging.info("UserService", "deleteUser", "Delete user: " + id);
        return userRepository.delete(id);
    }
}
