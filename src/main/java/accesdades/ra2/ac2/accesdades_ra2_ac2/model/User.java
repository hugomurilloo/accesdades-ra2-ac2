package accesdades.ra2.ac2.accesdades_ra2_ac2.model;

import java.time.LocalDateTime;

public class User {
    // Identificador
    private Long id;
    // Nom d'usuari
    private String name;
    // Descripcio de l'usuari
    private String description;
    private String email;
    // Contrasenya
    private String password;
    // Últim acces
    private LocalDateTime ultimAcces;
    // Data de creacio
    private LocalDateTime dataCreated;
    // Data d'ultima actualitzacio
    private LocalDateTime dataUpdated;
    //Image Path
    private String imagePath;

    public User() {}

    // Getters i setters

    public String getImagePath(){return imagePath; }
    public void setImagePath(String imagePath){this.imagePath = imagePath; }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    
    public LocalDateTime getUltimAcces() { return ultimAcces; }
    public void setUltimAcces(LocalDateTime ultimAcces) { this.ultimAcces = ultimAcces; }
    
    public LocalDateTime getDataCreated() { return dataCreated; }
    public void setDataCreated(LocalDateTime dataCreated) { this.dataCreated = dataCreated; }
    
    public LocalDateTime getDataUpdated() { return dataUpdated; }
    public void setDataUpdated(LocalDateTime dataUpdated) { this.dataUpdated = dataUpdated; }

    // ToString
    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", imagePath='" + imagePath + '\'' +
                ", description='" + description + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", ultimAcces=" + ultimAcces +
                ", dataCreated=" + dataCreated +
                ", dataUpdated=" + dataUpdated +
                '}';
    }
}