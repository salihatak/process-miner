package com.asa.processminer.controller;

import com.asa.processminer.dto.PMActivityDTO;
import com.asa.processminer.service.PMActivityService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {PMActivityController.class, PMActivityService.class})
@WebMvcTest
class PMActivityControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    private PMActivityService activityService;

    @WithMockUser("admin")
    @Test
    public void findAllNodes() throws Exception{
        List<PMActivityDTO> pmActivities = new ArrayList<>();
        PMActivityDTO pmActivityDTO = new PMActivityDTO();
        pmActivityDTO.setActivityId("1");
        pmActivityDTO.setActivityName("1");
        pmActivities.add(pmActivityDTO);

        when(activityService.findAll()).thenReturn(pmActivities);

        mockMvc.perform(get("/activity/findAll").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").exists())
                .andDo(print())
                .andReturn();

    }
}