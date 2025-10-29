package accesdades.ra2.ac2.accesdades_ra2_ac2.controller;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import accesdades.ra2.ac2.accesdades_ra2_ac2.model.User;
import accesdades.ra2.ac2.accesdades_ra2_ac2.repository.UserRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;



@RestController
@RequestMapping("/api")
public class UserController {

    @Autowired
    UserRepository userRepository;


    @PostMapping("/users")
    public String createUser(@RequestBody User user) {
        return userRepository.create(user);
    }

    @GetMapping("/users")
    public List<User> getUser() {
       return userRepository.findAll();
    }
    
    
}
