package com.orderservice.order.controller;

import com.orderservice.order.service.WorkflowCountryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/orders")
public class WorkflowController {

    @Autowired
    private WorkflowCountryService workflowCountryService;

    // --------------------------- SAVE WORKFLOW ---------------------------

    @PostMapping("/map/workflow")
    public ResponseEntity<String> saveWorkflowPerCountry(
            @RequestParam("workflow") String workflow,
            @RequestParam("country") String country
    ) {
        workflowCountryService.saveWorkflowPerCountry(country, workflow);
        return ResponseEntity.ok("Workflow saved successfully");
    }

    @PutMapping("/update/workflow")
    public ResponseEntity<String> updateWorkflowPerCountry(
            @RequestParam("workflow") String workflow,
            @RequestParam("country") String country
    ) {
        workflowCountryService.updateWorkflowForCountry(country, workflow);
        return ResponseEntity.ok("Workflow updated successfully");
    }
}
