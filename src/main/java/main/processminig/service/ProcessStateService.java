package main.processminig.service;

import org.springframework.stereotype.Service;
import com.google.gson.JsonArray;
import main.processminig.domain.Variant;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class ProcessStateService {
    private final Map<String, Object> stateStore = new ConcurrentHashMap<>();
    
    public void setAttribute(String key, Object value) {
        stateStore.put(key, value);
    }
    
    public Object getAttribute(String key) {
        return stateStore.get(key);
    }
    
    @SuppressWarnings("unchecked")
    public Map<String, Variant> getVariants() {
        return (Map<String, Variant>) stateStore.get(ProcessMiningService.VARIANTS);
    }
    
    public JsonArray getCurrentActivities() {
        return (JsonArray) stateStore.get(ProcessMiningService.CURRENT_ACTIVITIES);
    }
    
    public JsonArray getCurrentEdges() {
        return (JsonArray) stateStore.get(ProcessMiningService.CURRENT_EDGES);
    }
    
    public void clear() {
        stateStore.clear();
    }
} 