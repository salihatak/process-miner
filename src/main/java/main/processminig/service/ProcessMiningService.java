package main.processminig.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ArrayList;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.Gson;

import main.processminig.domain.Variant;
import org.springframework.stereotype.Service;

@Service
public class ProcessMiningService {
	
	private final ProcessStateService stateService;
	private final Gson gson = new Gson();
	
	public ProcessMiningService(ProcessStateService stateService) {
		this.stateService = stateService;
	}
	
	public static String FOOTPRINTMAP = "FOOTPRINTMAP";
	public static String CASES = "CASES";
	public static String LINES = "LINES";
	public static String PROJECT = "PROJECT";
	public static String VARIANTS = "VARIANTS";
	public static String MOSTCOMMON_VARIANT = "MOSTCOMMON_VARIANT";
	public static String ACTIVITIES = "ACTIVITIES";
	public static String CURRENT_ACTIVITIES = "CURRENT_ACTIVITIES";
	public static String CURRENT_EDGES = "CURRENT_EDGES";
	public static String PREVLIST = "PREVLIST";
	public static String NEXTLIST = "NEXTLIST";
	
	public void createFootPrints(Map<String, Variant> selectedVariants, 
								  Map<String, Map<String, Map<String, Integer>>> footprintMap, 
								  List<String> activitiesList) {
		Map<String, String> uniqueActivities = new HashMap<>();
		Map<String, Variant> variants = stateService.getVariants();
		
		// Find footprint
		for(String key : variants.keySet()) {
			if(selectedVariants.get(key) != null) {
				Variant variant = variants.get(key);
				List<String> activitiesOfVariant = variant.getActivities();
				for(int i = 0; i < activitiesOfVariant.size(); i++) {
					String activity = activitiesOfVariant.get(i);
					uniqueActivities.put(activity, "-");
					
					// Put prev footprint of current
					Map<String, Map<String, Integer>> footprint = footprintMap.computeIfAbsent(activity, 
						k -> new HashMap<>());
					
					Map<String, Integer> fpMap = footprint.computeIfAbsent(PREVLIST, 
						k -> new HashMap<>());
					
					if(i > 0) {
						String prev = activitiesOfVariant.get(i - 1);
						fpMap.merge(prev, variant.getCount(), Integer::sum);
						
						// Put current next footprint of prev
						footprint = footprintMap.computeIfAbsent(prev, k -> new HashMap<>());
						fpMap = footprint.computeIfAbsent(NEXTLIST, k -> new HashMap<>());
						fpMap.merge(activity, variant.getCount(), Integer::sum);
					}
				}				
			}
		}
		
		activitiesList.add("Start");
		activitiesList.addAll(uniqueActivities.keySet());
		activitiesList.add("End");
		
		// Create and store JSON arrays
		JsonArray activityJsonArray = new JsonArray();
		JsonArray footPrintJsonArray = new JsonArray();

		for(String activity : activitiesList) {
			JsonObject dataJson = new JsonObject();
			JsonObject idJson = new JsonObject();
			idJson.addProperty("id", activity);
			dataJson.add("data", idJson);
			activityJsonArray.add(dataJson);
		}

		// Generate edges between activities based on footprintMap
		for (String fromActivity : footprintMap.keySet()) {
			Map<String, Map<String, Integer>> toMap = footprintMap.get(fromActivity);
			if (toMap.containsKey(NEXTLIST)) {
				Map<String, Integer> nextMap = toMap.get(NEXTLIST);
				for (Map.Entry<String, Integer> entry : nextMap.entrySet()) {
					String toActivity = entry.getKey();
					int weight = entry.getValue();
					// Skip self-loops and invalid edges
					if (fromActivity.equals(toActivity) || fromActivity.equals("End") || toActivity.equals("Start")) {
						continue;
					}
					JsonObject edgeJson = new JsonObject();
					JsonObject dataJson = new JsonObject();
					dataJson.addProperty("id", fromActivity + "_" + toActivity);
					dataJson.addProperty("source", fromActivity);
					dataJson.addProperty("target", toActivity);
					dataJson.addProperty("weight", weight);
					edgeJson.add("data", dataJson);
					footPrintJsonArray.add(edgeJson);
				}
			}
		}

		stateService.setAttribute(CURRENT_ACTIVITIES, activityJsonArray);
		stateService.setAttribute(CURRENT_EDGES, footPrintJsonArray);
	}
	
	public JsonObject retrieveProcessData(List<Variant> selectedVariantList) {
		Map<String, Variant> variants = stateService.getVariants();
		Variant mostCommonVariant = (Variant) stateService.getAttribute(MOSTCOMMON_VARIANT);
		Map<String, Map<String, Map<String, Integer>>> footprintMap = new HashMap<>();
		List<String> activitiesList = new ArrayList<>();
		
		// Determine selected variants
		Map<String, Variant> selectedVariants = new HashMap<>();
		if (selectedVariantList == null || selectedVariantList.isEmpty()) {
			selectedVariants.put(mostCommonVariant.getVariantId(), mostCommonVariant);
		} else {
			for (Variant variant : selectedVariantList) {
				selectedVariants.put(variant.getVariantId(), variant);
			}
		}
		
		createFootPrints(selectedVariants, footprintMap, activitiesList);
		
		// Build response
		JsonObject response = new JsonObject();
		JsonArray variantsJsonArray = new JsonArray();
		JsonArray selectedVariantsJsonArray = new JsonArray();
		int totalCaseCount = 0;
		
		// Process variants
		for (Variant variant : variants.values()) {
			variantsJsonArray.add(gson.toJsonTree(variant));
			totalCaseCount += variant.getCount();
		}
		
		// Process selected variants
		for (String key : selectedVariants.keySet()) {
			selectedVariantsJsonArray.add(gson.toJsonTree(variants.get(key)));
		}
		
		response.add("nodes", stateService.getCurrentActivities());
		response.add("edges", stateService.getCurrentEdges());
		response.add("variants", variantsJsonArray);
		response.add("selectedVariants", selectedVariantsJsonArray);
		response.addProperty("totalCaseCount", totalCaseCount);
		
		return response;
	}
}
