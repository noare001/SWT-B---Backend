package fh.dualo.kidsapp.application.cache;

import com.fasterxml.jackson.databind.JsonNode;

import java.util.Map;

public abstract class KidsAppData {

    public abstract Map<String, JsonNode> getOffer();
}
