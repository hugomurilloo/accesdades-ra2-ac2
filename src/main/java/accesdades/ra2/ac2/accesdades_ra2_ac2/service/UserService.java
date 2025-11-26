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

    public static class DataWrapper { public Data data; }
    public static class Data { public int count; public String control; public JsonUser[] users; }
    public static class JsonUser { public String name; public String description; public String email; public String password; }
    
    public ResponseEntity<String> insertUsersFromJson(MultipartFile jsonFile) {
        // Validar que s'ha enviat el fitxer
        if (jsonFile == null || jsonFile.isEmpty()) {
            return ResponseEntity.badRequest().body("El fitxer no pot estar buit");
        }

        try {
            // Mapejar el JSON
            com.fasterxml.jackson.databind.ObjectMapper mapper = new com.fasterxml.jackson.databind.ObjectMapper();
            DataWrapper wrapper = mapper.readValue(jsonFile.getInputStream(), DataWrapper.class);

            // Comprovar estructura
            if (wrapper == null || wrapper.data == null || wrapper.data.users == null) {
                return ResponseEntity.status(org.springframework.http.HttpStatus.BAD_REQUEST)
                        .body("Format JSON incorrecte");
            }

            // Comprovar control == "OK"
            if (!"OK".equals(wrapper.data.control) || wrapper.data.count != wrapper.data.users.length) {
                return ResponseEntity.status(org.springframework.http.HttpStatus.BAD_REQUEST)
                        .body("Control o count incorrecte");
            }

            // Iterar usuaris i guardar-los a la BDD
            int added = 0;
            for (JsonUser uj : wrapper.data.users) {
                User u = new User();
                u.setName(uj.name);
                u.setDescription(uj.description);
                u.setEmail(uj.email);
                u.setPassword(uj.password);
                u.setImagePath(null);
                u.setDataCreated(java.time.LocalDateTime.now());
                u.setDataUpdated(java.time.LocalDateTime.now());

                if (addUser(u) != null) added++;
            }

            // Guardar el fitxer JSON a la carpeta "json_processed"
            Path jsonDir = Paths.get("src/main/resources/public/json_processed");
            if (!Files.exists(jsonDir)) Files.createDirectories(jsonDir);
            Path dest = jsonDir.resolve(jsonFile.getOriginalFilename());
            Files.copy(jsonFile.getInputStream(), dest, StandardCopyOption.REPLACE_EXISTING);

            return ResponseEntity.status(org.springframework.http.HttpStatus.CREATED).body(String.valueOf(added));
        } catch (Exception e) {
            return ResponseEntity.status(org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error " + e.getMessage());
        }
    }
}
