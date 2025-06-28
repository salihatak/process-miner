package main.processminig.service;

import org.springframework.stereotype.Service;
import main.processminig.domain.Item;
import main.processminig.domain.Variant;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class LogProcessorService {
    
    private final ProcessStateService stateService;
    
    public LogProcessorService(ProcessStateService stateService) {
        this.stateService = stateService;
    }
    
    public void readLogs(String data, boolean withDate, boolean ignoreHeaderLine) throws IOException, ParseException {
        Map<String, List<String>> cases = new HashMap<>();
        Map<String, Variant> variants = new HashMap<>();
        Map<String, Integer> activities = new HashMap<>();
        
        String[] lines = data.split("\n");
        
        if (withDate) {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
            dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
            List<Item> logList = new ArrayList<>();
            
            for (String line : lines) {
                if (!ignoreHeaderLine) {
                    String[] cols = line.split(",");
                    logList.add(new Item(cols[0], cols[1], dateFormat.parse(cols[2])));
                }
                if (ignoreHeaderLine) {
                    ignoreHeaderLine = false;
                }
            }
            
            Collections.sort(logList, (o1, o2) -> o1.getDate().after(o2.getDate()) ? 1 : -1);
            
            processLogList(logList, cases, variants, activities);
        } else {
            processRawLines(lines, ignoreHeaderLine, cases, variants, activities);
        }
        
        Variant mostCommonVariant = findMostCommonVariant(variants);
        
        stateService.setAttribute(ProcessMiningService.LINES, data);
        stateService.setAttribute(ProcessMiningService.CASES, cases);
        stateService.setAttribute(ProcessMiningService.VARIANTS, variants);
        stateService.setAttribute(ProcessMiningService.MOSTCOMMON_VARIANT, mostCommonVariant);
    }
    
    private void processLogList(List<Item> logList, Map<String, List<String>> cases,
                              Map<String, Variant> variants, Map<String, Integer> activities) {
        for (Item item : logList) {
            List<String> list = cases.computeIfAbsent(item.getCaseId(), k -> new ArrayList<>(List.of("Start")));
            list.add(item.getActivity());
            
            activities.merge(item.getActivity(), 1, Integer::sum);
        }
        processVariants(cases, variants);
    }
    
    private void processRawLines(String[] lines, boolean ignoreHeaderLine,
                               Map<String, List<String>> cases, Map<String, Variant> variants,
                               Map<String, Integer> activities) {
        for (String line : lines) {
            if (!ignoreHeaderLine) {
                String[] cols = line.split(";");
                List<String> list = cases.computeIfAbsent(cols[0], k -> new ArrayList<>(List.of("Start")));
                list.add(cols[1]);
                activities.merge(cols[1], 1, Integer::sum);
            }
            if (ignoreHeaderLine) {
                ignoreHeaderLine = false;
            }
        }
        processVariants(cases, variants);
    }
    
    private void processVariants(Map<String, List<String>> cases, Map<String, Variant> variants) {
        for (Map.Entry<String, List<String>> entry : cases.entrySet()) {
            List<String> activities = entry.getValue();
            activities.add("End");
            
            String variantString = String.join(";", activities) + ";";
            
            variants.computeIfAbsent(variantString, k -> {
                Variant v = new Variant();
                v.setVariantId(variantString);
                v.setActivities(activities);
                v.setCount(0);
                return v;
            }).setCount(variants.get(variantString).getCount() + 1);
        }
    }
    
    private Variant findMostCommonVariant(Map<String, Variant> variants) {
        return variants.values().stream()
                .max(Comparator.comparingInt(Variant::getCount))
                .orElse(null);
    }
} 