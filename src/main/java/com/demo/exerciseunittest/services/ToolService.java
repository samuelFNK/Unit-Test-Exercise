package com.demo.exerciseunittest.services;


import com.demo.exerciseunittest.entities.Tool;

public interface ToolService {

    Tool getToolById(Long id);

    Tool addTool(Tool tool);

    void deleteToolById(Long id);

    Tool updateTool(Long id, Tool tool);

    Iterable<Tool> getAllTools();

    long countTools();

    void deleteAllTools();
}
