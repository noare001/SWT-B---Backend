package fh.dualo.kidsapp.application.cache;

import com.fasterxml.jackson.databind.JsonNode;
import fh.dualo.kidsapp.application.enums.RegistrationStatus;

import java.util.Map;

public abstract class State {
    public abstract Map<String, JsonNode> getOffer();
    public abstract Map<String, RegistrationStatus> getRegistrations();
}
