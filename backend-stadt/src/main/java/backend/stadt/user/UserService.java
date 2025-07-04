package backend.stadt.user;

import backend.stadt.helperClasses.AppUserDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Map;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public UserService(){

    }

    public AppUserDTO login(String username, String password) {
        AppUser user =  userRepository.getAppUserByNameAndPassword(username, sha256Hash(password));
        return new AppUserDTO(user);
    }

    public AppUserDTO register(String username, String password, String email) {
        if (userRepository.existsByName(username)) {
            throw new IllegalArgumentException("Benutzername bereits vergeben");
        }

        // Passwort hashen
        String hashedPassword = sha256Hash(password);

        // Neues Benutzerobjekt erstellen
        AppUser user = new AppUser();
        user.setName(username);
        user.setPassword(hashedPassword);
        user.setEmail(email);
        user.setRole(Role.USER);
        userRepository.save(user);

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
