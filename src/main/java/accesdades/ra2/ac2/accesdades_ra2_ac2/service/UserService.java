package accesdades.ra2.ac2.accesdades_ra2_ac2.service;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import accesdades.ra2.ac2.accesdades_ra2_ac2.model.User;
import accesdades.ra2.ac2.accesdades_ra2_ac2.repository.UserRepository;

@Service
public class UserService {
    @Autowired
    UserRepository userRepository;
    public List<User> getUser(){
        return userRepository.findAll();
    }
    public User addUser(User user){
        LocalDateTime now = LocalDateTime.now();
        user.setDataCreated(now);
        user.setDataUpdated(now);
        return userRepository.save(user);
    }

    public User findById(Long id) {
        return userRepository.findById(id);
    }

    public List<User> findAll(){
        return userRepository.findAll();
    }
    public String update(Long id, User user){
        return userRepository.update(id, user);
    }
    public String updateName(Long id, String name){
            return userRepository.updateName(id, name);
    }
    public String delete(Long id){
        return userRepository.delete(id);
    }
    public ResponseEntity<?> uploadImage(Long userId, MultipartFile imageFile) {
        try {
            if (imageFile.isEmpty()) {
                return ResponseEntity.badRequest().body("La imatge no pot estar buida.");
            }
            
            User user = userRepository.findById(userId);
            if (user == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No existeix l'usuari");
            }
            
            // Crear carpeta
            Path imgDir = Paths.get("src/main/resources/public/images");
            if (!Files.exists(imgDir)) {
                Files.createDirectories(imgDir);
            }

            // Guardar imagen
            String originalFilename = imageFile.getOriginalFilename();
            String imgName = "usuari_" + userId + "_" + originalFilename;
            Path destFile = imgDir.resolve(imgName);
            Files.copy(imageFile.getInputStream(), destFile, StandardCopyOption.REPLACE_EXISTING);

            // Guardar path en la BDD
            String imgPath = "/images/" + imgName;
            userRepository.updateImagePath(userId, imgPath);
            
            return ResponseEntity.status(HttpStatus.CREATED).body("S'ha pujat " + imgPath);
            
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error en pujar la imatge: " + e.getMessage());
        }
    }

    public ResponseEntity<String> insertStudentByCsv(MultipartFile csvFile) {
        if (csvFile.isEmpty()) {
            return ResponseEntity.badRequest().body("El fitxer no pot estar buit.");
        }

        int nRegistre = 0;
        try (BufferedReader br = new BufferedReader(new InputStreamReader(csvFile.getInputStream()))) {
            String linia = br.readLine();
            int nLinia = 0;
            
            while (linia != null) {
                if (nLinia > 0) {  // Saltar capçalera
                    String[] camp = linia.split(",");
                    
                    if (camp.length >= 4) {
                        User user = new User();
                        user.setName(camp[0].trim());
                        user.setDescription(camp[1].trim());
                        user.setEmail(camp[2].trim());
                        user.setPassword(camp[3].trim());
                        
                        // Guardar l'usuari
                        User usuarioGuardado = addUser(user);
                        if (usuarioGuardado != null) {
                            nRegistre++;
                        }
                    }
                }
                
                linia = br.readLine();
                nLinia++;
            }
            
            // Crear carpeta si no existeix
            Path csvDir = Paths.get("src/main/resources/public/csv_processed");
            if (!Files.exists(csvDir)) {
                Files.createDirectories(csvDir);
            }
            
            // Guardar el fitxer
            String filename = csvFile.getOriginalFilename();
            Path destFile = csvDir.resolve(filename);
            Files.copy(csvFile.getInputStream(), destFile, StandardCopyOption.REPLACE_EXISTING);
            
            if (nRegistre == 0) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("No s'ha pogut importar cap usuari.");
            }
            
            return ResponseEntity.status(HttpStatus.CREATED).body("S'han importat " + nRegistre + " usuaris correctament.");
            
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error en insertar CSV: " + e.getMessage());
        }
    }
}
