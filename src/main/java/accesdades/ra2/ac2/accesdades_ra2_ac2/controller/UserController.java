package accesdades.ra2.ac2.accesdades_ra2_ac2.controller;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
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
import org.springframework.web.multipart.MultipartFile;

import accesdades.ra2.ac2.accesdades_ra2_ac2.model.User;
import accesdades.ra2.ac2.accesdades_ra2_ac2.service.UserService;


@RestController
@RequestMapping("/api")
public class UserController {

    @Autowired
    UserService userService;


    // Endpoint per crear un usuari (POST /api/users)
    @PostMapping("/users")
    public ResponseEntity<String> createUser(@RequestBody User usuari) {
    User usuarioGuardado = userService.addUser(usuari);
    return ResponseEntity.ok(usuarioGuardado.getName() + " creat correctament");
    }

    // Endpoint per obtenir tots els usuaris (GET /api/users)
    @GetMapping("/users")
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = userService.findAll();
        if (users == null || users.isEmpty()) {
           return ResponseEntity.ok(null);
        }
        return ResponseEntity.ok(users);
    }

    // Endpoint per obtenir un usuari per id (GET /api/users/{user_id})
    @GetMapping("/users/{user_id}")
    public ResponseEntity<?> getUserById(@PathVariable Long user_id) {
        User user = userService.findById(user_id);
        if (user != null) {
            return ResponseEntity.ok(user);
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuari no trobat");
    }

    // Endpoint per actualitzacio completa (PUT /api/users/{user_id})
    @PutMapping("/users/{user_id}")
    public ResponseEntity<String> updateUser(@PathVariable Long user_id, @RequestBody User user) {
        String result = userService.update(user_id, user);
        return ResponseEntity.ok(result);
    }

    // Endpoint per actualitzar nomes el nom (PATCH /api/users/{user_id}/name)
    @PatchMapping("/users/{user_id}/name")
    public ResponseEntity<String> updateUserName(@PathVariable Long user_id, @RequestParam String name) {
        String result = userService.updateName(user_id, name);
        return ResponseEntity.ok(result);
    }

    // Endpoint per eliminar un usuari (DELETE /api/users/{user_id})
    @DeleteMapping("/users/{user_id}")
    public ResponseEntity<String> deleteUser(@PathVariable Long user_id) {
        String result = userService.delete(user_id);
        return ResponseEntity.ok(result);
    }

    // Endpoint per añadir una imatge a un usuari per ID (POST /api/users/{user_id}/image)
    @PostMapping("/users/{user_id}/image")
    public ResponseEntity<?> addImage(@PathVariable Long user_id, @RequestParam MultipartFile imageFile) {
        return userService.uploadImage(user_id, imageFile);
    }

    // Endpoint per crear 10 usuaris d'un fitxer .csv (POST /api/users/upload-csv)
    @PostMapping("/users/upload-csv")
    public ResponseEntity<String> insertStudent(@RequestParam MultipartFile csvFile) {
        return userService.insertStudentByCsv(csvFile);
    }
    
    @PostMapping("/users/upload-json")
    public ResponseEntity<String> postMethodName(@RequestParam MultipartFile jsonFile) {
        
        return userService.carregaMassiva();
    }
    
    
}
