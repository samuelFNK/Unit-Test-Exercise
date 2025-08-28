package com.demo.exerciseunittest.controllers;

import java.util.Arrays;
import java.util.List;
import com.demo.exerciseunittest.entities.Tool;
import com.demo.exerciseunittest.services.ToolService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.http.MediaType;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class ToolControllerTest {

    @Mock
    private ToolService toolService;

    @InjectMocks
    private ToolController toolController;  //tells controller to use my toolService Mock

    private MockMvc mockMvc; //to test http endpoints
    private ObjectMapper objectMapper; //to test json objects

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(toolController).build();
        objectMapper = new ObjectMapper();
    }


    @Test
    void findAll_ShouldReturnAllTools() throws Exception {
        // Arrange
        List<Tool> tools = Arrays.asList(
                new Tool("Hammer", "A heavy tool"),
                new Tool("Screwdriver", "A precision tool")
        );
        when(toolService.getAllTools()).thenReturn(tools);

        // Act & Assert
        mockMvc.perform(get("/api/v1/tools/all"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].name").value("Hammer"))
                .andExpect(jsonPath("$[1].name").value("Screwdriver"));

        verify(toolService).getAllTools();
    }



}
