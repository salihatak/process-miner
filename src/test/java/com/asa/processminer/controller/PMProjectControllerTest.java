package com.asa.processminer.controller;

import com.asa.processminer.model.PMProject;
import com.asa.processminer.service.PMProjectService;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@ExtendWith(SpringExtension.class)
@WebMvcTest
public class PMProjectControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    private PMProjectService projectService;

    @WithMockUser("admin")
    @Test
    public void findAll() throws Exception{
        List<PMProject> pmProjects = new ArrayList<PMProject>();
        PMProject pmProject = new PMProject();
        pmProject.setProjectId(1);
        pmProject.setProjectName("p1");
        pmProjects.add(pmProject);

        when(projectService.findAll()).thenReturn(pmProjects);

        mockMvc.perform(get("/project/findAll").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).andReturn();

    }

}
