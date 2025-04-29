package backend.stadt;

import org.aspectj.weaver.ast.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TestService {

    @Autowired
    private TestRepository testRepository;

    public Iterable<TestEntity> findAll() {
        return testRepository.findAll();
    }

    public TestEntity findById(Long id) {
        return testRepository.findById(id).orElse(null);
    }
    
    public void save(TestEntity testEntity){
        testRepository.save(testEntity);
    }
}
