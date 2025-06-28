package main.processminig.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import main.processminig.domain.Variant;
import main.processminig.service.ProcessMiningService;
import main.processminig.service.LogProcessorService;
import java.io.IOException;
import java.text.ParseException;
import java.util.List;

@RestController
@RequestMapping("/process")
public class ProcessController {

    private final ProcessMiningService processMiningService;
    private final LogProcessorService logService;

    public ProcessController(ProcessMiningService processMiningService,
                           LogProcessorService logService) {
        this.processMiningService = processMiningService;
        this.logService = logService;
    }
    
    @PostMapping
    public void processData(@RequestBody String processLog) throws ParseException, IOException {
        logService.readLogs(processLog, true, true);
    }

    @PostMapping("/retrieveProcessData")
    public String retrieveProcessData(@RequestParam(value = "selectedVariants", required = false) String selectedVariantsStr, 
                                    @RequestBody(required = false) String requestBody) {
        Gson gson = new Gson();
        List<Variant> selectedVariantList = null;
        
        try {
            if (selectedVariantsStr != null && !selectedVariantsStr.isEmpty()) {
                selectedVariantList = gson.fromJson(selectedVariantsStr, new TypeToken<List<Variant>>(){}.getType());
            } else if (requestBody != null && !requestBody.isEmpty()) {
                selectedVariantList = gson.fromJson(requestBody, new TypeToken<List<Variant>>(){}.getType());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        return processMiningService.retrieveProcessData(selectedVariantList).toString();
    }
}
