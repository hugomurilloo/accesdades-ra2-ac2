package accesdades.ra2.ac2.accesdades_ra2_ac2.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import accesdades.ra2.ac2.accesdades_ra2_ac2.model.User;
import accesdades.ra2.ac2.accesdades_ra2_ac2.repository.UserRepository;

@Service
public class UserService {
    @Autowired
    UserRepository userRepository;
    public List<User> getCustomer(){
        return userService.getUser();
    }

    public String createUser(@RequestBody User user) {
        return userRepository.create(user);
    }
}
