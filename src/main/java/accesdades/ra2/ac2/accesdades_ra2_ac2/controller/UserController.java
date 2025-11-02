package accesdades.ra2.ac2.accesdades_ra2_ac2.controller;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import accesdades.ra2.ac2.accesdades_ra2_ac2.model.User;
import accesdades.ra2.ac2.accesdades_ra2_ac2.repository.UserRepository;



@RestController
@RequestMapping("/api")
public class UserController {

    @Autowired
    UserRepository userRepository;


    // Endpoint per crear un usuari (POST /api/users)
    @PostMapping("/users")
    public String createUser(@RequestBody User user) {
        return userRepository.create(user);
    }

    // Endpoint per obtenir tots els usuaris (GET /api/users)
    @GetMapping("/users")
    public List<User> getUser() {
       return userRepository.findAll();
    }

    // Endpoint per obtenir un usuari per id (GET /api/users/{user_id})
    @GetMapping("/users/{user_id}")
    public ResponseEntity<?> getUserById(@PathVariable Long user_id) {
        User user = userRepository.findById(user_id);
        if (user != null) {
            return ResponseEntity.ok(user);
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuari no trobat");
    }

    // Endpoint per actualitzacio completa (PUT /api/users/{user_id})
    @PutMapping("/users/{user_id}")
    public ResponseEntity<String> updateUser(@PathVariable Long user_id, @RequestBody User user) {
        String result = userRepository.update(user_id, user);
        return ResponseEntity.ok(result);
    }

    // Endpoint per actualitzar nomes el nom (PATCH /api/users/{user_id}/name)
    @PatchMapping("/users/{user_id}/name")
    public ResponseEntity<String> updateUserName(@PathVariable Long user_id, @RequestParam String name) {
        String result = userRepository.updateName(user_id, name);
        return ResponseEntity.ok(result);
    }

    // Endpoint per eliminar un usuari (DELETE /api/users/{user_id})
    @DeleteMapping("/users/{user_id}")
    public ResponseEntity<String> deleteUser(@PathVariable Long user_id) {
        String result = userRepository.delete(user_id);
        return ResponseEntity.ok(result);
    }
    
}
