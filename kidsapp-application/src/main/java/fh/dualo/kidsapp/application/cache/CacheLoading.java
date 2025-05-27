package fh.dualo.kidsapp.application.cache;

public class CacheLoading extends State {
    private KidsAppDataService dataService;

    public CacheLoading(KidsAppDataService dataService) {
        this.dataService = dataService;
    }

    @Override
    public String getOffer(String key) {
        // Implementierung kommt hier hin
        return null;
    }
}
