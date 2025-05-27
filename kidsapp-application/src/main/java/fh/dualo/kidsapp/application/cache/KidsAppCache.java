package fh.dualo.kidsapp.application.cache;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Service
public class KidsAppCache extends KidsAppData {

    private Map<String,String> cache;

    private KidsAppDataService dataService;

    private State state;

    @Autowired
    public KidsAppCache(KidsAppDataService dataService){
        this.dataService = dataService;
    }

    @PostConstruct
    public void setup(){
        cache = Collections.synchronizedMap(new HashMap<>());
    }

    public void loadData(String data){
        ObjectMapper mapper = new ObjectMapper();
        mapper.convertValue(data,Map.class);
    }

    public String getOffer(String key) {
        return state.getOffer(key);
    }
}
