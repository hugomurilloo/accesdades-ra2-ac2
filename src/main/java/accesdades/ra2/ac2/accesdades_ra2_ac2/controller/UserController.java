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
import accesdades.ra2.ac2.accesdades_ra2_ac2.service.UserService;
import accesdades.ra2.ac2.accesdades_ra2_ac2.logging.CustomLogging;

@RestController
@RequestMapping("/api")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private CustomLogging customLogging;

    // GET all users
    @GetMapping("/users")
    public ResponseEntity<List<User>> getUser() {
        customLogging.info("UserController", "getUser", "Accessed GET /api/users");
        List<User> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }

    // GET user by id
    @GetMapping("/users/{user_id}")
    public ResponseEntity<?> getUserById(@PathVariable Long user_id) {
        customLogging.info("UserController", "getUserById", "Accessed GET /api/users/" + user_id);
        User user = userService.getUserById(user_id);
        if (user != null) {
            return ResponseEntity.ok(user);
        }
        customLogging.error("UserController", "getUserById", "User not found: " + user_id);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuari no trobat");
    }

    // POST create user
    @PostMapping("/users")
    public ResponseEntity<String> createUser(@RequestBody User user) {
        customLogging.info("UserController", "createUser", "Accessed POST /api/users - email: " + user.getEmail());
        try {
            String result = userService.createUser(user);
            return ResponseEntity.status(HttpStatus.CREATED).body(result);
        } catch (Exception e) {
            customLogging.error("UserController", "createUser", "Error creating user: " + user.getEmail());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error creando usuario");
        }
    }

    // PUT full update
    @PutMapping("/users/{user_id}")
    public ResponseEntity<String> updateUser(@PathVariable Long user_id, @RequestBody User user) {
        customLogging.info("UserController", "updateUser", "Accessed PUT /api/users/" + user_id);
        try {
            String result = userService.updateUser(user_id, user);
            if (result.contains("correctament")) {
                return ResponseEntity.ok(result);
            }
            customLogging.error("UserController", "updateUser", "User not found for update: " + user_id);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(result);
        } catch (Exception e) {
            customLogging.error("UserController", "updateUser", "Error updating user: " + user_id);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error actualizando usuario");
        }
    }

    // PATCH update name only
    @PatchMapping("/users/{user_id}/name")
    public ResponseEntity<String> updateUserName(@PathVariable Long user_id, @RequestParam String name) {
        customLogging.info("UserController", "updateUserName", "Accessed PATCH /api/users/" + user_id + "/name - newName: " + name);
        try {
            String result = userService.updateUserName(user_id, name);
            if (result.contains("correctament")) {
                return ResponseEntity.ok(result);
            }
            customLogging.error("UserController", "updateUserName", "User not found for update: " + user_id);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(result);
        } catch (Exception e) {
            customLogging.error("UserController", "updateUserName", "Error updating name for user: " + user_id);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error actualizando nombre");
        }
    }

    // DELETE user
    @DeleteMapping("/users/{user_id}")
    public ResponseEntity<String> deleteUser(@PathVariable Long user_id) {
        customLogging.info("UserController", "deleteUser", "Accessed DELETE /api/users/" + user_id);
        try {
            String result = userService.deleteUser(user_id);
            if (result.contains("correctament")) {
                return ResponseEntity.ok(result);
            }
            customLogging.error("UserController", "deleteUser", "User not found for delete: " + user_id);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(result);
        } catch (Exception e) {
            customLogging.error("UserController", "deleteUser", "Error deleting user: " + user_id);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error eliminando usuario");
        }
    }
}
