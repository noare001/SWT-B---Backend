package backend.stadt.repositorys;

import backend.stadt.modells.Provider;
import backend.stadt.user.AppUser;
import backend.stadt.user.Role;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ProviderService {

    private final ProviderRepository providerRepo;
    private final UserRepository userRepo;

    public ProviderService(ProviderRepository providerRepo, UserRepository userRepo) {
        this.providerRepo = providerRepo;
        this.userRepo = userRepo;
    }

    public List<Provider> findAll() {
        return providerRepo.findAll();
    }

    public Provider findById(int id) {
        return providerRepo.findById(id);
    }

    public void save(Provider provider) {
        providerRepo.save(provider);
    }

    @Transactional
    public void deleteProviderAndDetachUsers(int providerId) {
        Provider provider = providerRepo.findById(providerId);
        List<AppUser> users = userRepo.findAllByProviderId(providerId);

        for (AppUser user : users) {
            user.setProvider(null);
            user.setRole(Role.USER);
        }

        userRepo.saveAll(users);
        providerRepo.delete(provider);
    }
}
