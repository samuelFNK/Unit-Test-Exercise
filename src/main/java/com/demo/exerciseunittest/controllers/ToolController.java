package com.demo.exerciseunittest.controllers;
import com.demo.exerciseunittest.entities.Tool;
import com.demo.exerciseunittest.services.ToolService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/v1/tools")
public class ToolController {

    // Autowire by constructor
    private final ToolService toolService;

    public ToolController(ToolService toolService){
        this.toolService = toolService;
    }

    // Use ResponseEntity and create endpoints for
    // Get all tools
    @GetMapping("/all")
    public ResponseEntity<Iterable<Tool>> getAllTools() {
        Iterable<Tool> tools = toolService.getAllTools();
        return ResponseEntity.ok(tools);
    }

    // Get one specific tool (use @PathVariable)
    @GetMapping("/{id}")
    public ResponseEntity<Tool> getToolById(@PathVariable long id) {
        Tool tool = toolService.getToolById(id);
        return  ResponseEntity.ok(tool);
    }

    // Add a tool
    @PostMapping("/add")
    public ResponseEntity<Tool> addTool(@RequestBody Tool tool){
        Tool addedTool = toolService.addTool(tool);
        return ResponseEntity.ok(addedTool);
    }

    // Update a tool
    @PutMapping("/update/{id}")
    public ResponseEntity<Tool> updateToolById(@PathVariable long id, @RequestBody Tool tool){
        Tool updatedTool = toolService.updateTool(id, tool);
        return ResponseEntity.ok(updatedTool);
    }

    // Delete a tool (use @RequestParam here)
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Tool> deleteToolById(@RequestParam long id){
        toolService.deleteToolById(id);
        return ResponseEntity.ok().build();
    }

}
