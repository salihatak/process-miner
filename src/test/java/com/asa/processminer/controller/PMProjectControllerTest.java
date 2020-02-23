package com.asa.processminer.controller;

import com.asa.processminer.config.WebSecurityConfig;
import com.asa.processminer.config.WebSecurityConfigTest;
import com.asa.processminer.model.PMProject;
import com.asa.processminer.dto.PMProjectDTO;
import com.asa.processminer.service.PMProjectService;
import com.fasterxml.jackson.databind.ObjectMapper;


import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.Matchers.is;


@ContextConfiguration(classes = {PMProjectController.class, PMProjectService.class})
@WebMvcTest
@WithMockUser(username = "admin", roles = "ADMIN")
@Import(WebSecurityConfigTest.class)
public class PMProjectControllerTest {

    /*
    * .andExpect(jsonPath("$.data.roles.length()").value(correctRoles.size()));
    * .andExpect((jsonPath("$.data.roles", Matchers.hasSize(size))));
    * .andExpect((jsonPath("$.data.roles", Matchers.containsInAnyOrder("role1", "role2", "role3"))));
    * .andExpect(jsonPath("$.fieldErrors[*].path", containsInAnyOrder("title", "description")))
    * .andExpect(jsonPath("$[0].id", is(1)))
    * .andExpect(jsonPath("$[0].description", is("Lorem ipsum")))
    * */

    @Autowired
    MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private PMProjectService projectService;

    @Test
    public void findAllPMProjects() throws Exception{
        List<PMProject> pmProjects = new ArrayList<>();
        PMProject pmProject = PMProject.builder()
                .projectId(1L)
                .projectName("p1")
                .description("test").build();

        when(projectService.findAll()).thenReturn(pmProjects);

        mockMvc.perform(get("/project").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andReturn();

    }

    @Test
    public void savePMProject() throws Exception{
        PMProjectDTO pmProjectDTO = new PMProjectDTO("p1","test");

        PMProject pmProject = PMProject.builder()
                                        .projectId(1L)
                                        .projectName("p1")
                                        .description("test").build();

//        doReturn(pmProject).when(projectService).createProject(pmProjectDTO);
        when(projectService.createProject(pmProjectDTO))
                .thenReturn(pmProject);

        mockMvc.perform(post("/project")
                    .content(objectMapper.writeValueAsString(pmProjectDTO))
                    .characterEncoding("utf-8")
                    .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.projectId").exists())
                .andDo(print())
                .andReturn();
    }

    @Test
    public void updatePMProject() throws Exception{
        PMProjectDTO pmProjectDTO = new PMProjectDTO("p1","test1");

        PMProject pmProject = PMProject.builder()
                                        .projectId(1L)
                                        .projectName("p1")
                                        .description("test1").build();

//        doReturn(pmProject).when(projectService).createProject(pmProjectDTO);
        when(projectService.updateProject(1L, pmProjectDTO))
                .thenReturn(pmProject);

        mockMvc.perform(put("/project/" + 1L)
                    .content(objectMapper.writeValueAsString(pmProjectDTO))
                    .characterEncoding("utf-8")
                    .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.projectId").exists())
                .andExpect(jsonPath("$.description", is("test1")))
                .andDo(print())
                .andReturn();
    }

}
