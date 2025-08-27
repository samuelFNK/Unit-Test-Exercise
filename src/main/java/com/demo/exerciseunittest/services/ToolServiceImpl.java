package com.demo.exerciseunittest.services;

import com.demo.exerciseunittest.entities.Tool;
import com.demo.exerciseunittest.repositories.ToolRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class ToolServiceImpl implements ToolService {

    private final ToolRepository toolRepository;

    @Autowired
    public ToolServiceImpl(ToolRepository toolRepository) {
        this.toolRepository = toolRepository;
    }

    @Override
    public Tool getToolById(Long id) {
        return toolRepository.findById(id).orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND, "Tool not found"));
    }

    @Override
    public Tool addTool(Tool tool) {
        validateNameContent(tool);
        if(toolRepository.findAll().stream().anyMatch(t -> t.getName().toLowerCase().startsWith(tool.getName().toLowerCase()))) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Tool name already exists");
        }
        return toolRepository.save(tool);
    }

    @Override
    public void deleteToolById(Long id) {
        if(!toolRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Tool not found");
        }
        toolRepository.deleteById(id);
    }

    @Override
    public Tool updateTool(Long id, Tool tool) {
        validateNameContent(tool);
        Tool existingTool = getToolById(id);
        existingTool.setName(tool.getName());
        existingTool.setDescription(tool.getDescription());
        return toolRepository.save(existingTool);

    }

    private static void validateNameContent(Tool tool) {
        if(tool.getName() == null || tool.getName().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Tool name cannot be null or blank");
        }
    }

    @Override
    public Iterable<Tool> getAllTools() {
        return toolRepository.findAll();
    }

    @Override
    public long countTools() {
        return toolRepository.count();
    }



    @Override
    public void deleteAllTools() {
        if(countTools() == 0) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No tools to delete");
        }
        toolRepository.deleteAll();
    }
}
