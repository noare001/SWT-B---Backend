package backend.stadt.repositorys;

import backend.stadt.helperClasses.AppUserDTO;
import backend.stadt.user.AppUser;
import backend.stadt.user.Role;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private UserRepository userRepo;

    public UserService(UserRepository userRepo){
        this.userRepo=userRepo;
    }
    public List<AppUser> findAll() {
        return userRepo.findAll();
    }

    public Optional<AppUser> findById(Integer id) {
        return userRepo.findById(id);
    }

    public void save(AppUser user) {
        userRepo.save(user);
    }

    public void deleteById(Integer id) {
        userRepo.deleteById(id);
    }
    public AppUserDTO login(String username, String password) {
        AppUser user = userRepo.getAppUserByNameAndPassword(username, sha256Hash(password)).orElse(null);
        if(user==null) {
            return null;
        }
        return new AppUserDTO(user);
    }

    public AppUserDTO register(String username, String password, String email) {
        if (userRepo.existsByName(username)) {
            return null;
        }

        // Passwort hashen
        String hashedPassword = sha256Hash(password);

        // Neues Benutzerobjekt erstellen
        AppUser user = new AppUser();
        user.setName(username);
        user.setPassword(hashedPassword);
        user.setEmail(email);
        user.setRole(Role.USER);
        userRepo.save(user);

        return new AppUserDTO(user);
    }

    public static String sha256Hash(String input) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] encodedHash = digest.digest(input.getBytes(StandardCharsets.UTF_8));

            // Byte-Array in Hex-String umwandeln
            StringBuilder hexString = new StringBuilder(2 * encodedHash.length);
            for (byte b : encodedHash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }

            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("SHA-256 Algorithmus nicht verfügbar", e);

        }
    }
}