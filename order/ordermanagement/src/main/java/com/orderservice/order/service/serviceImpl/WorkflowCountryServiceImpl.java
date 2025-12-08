package com.orderservice.order.service.serviceImpl;

import com.orderservice.order.entity.CurrentCountryWorkflowMapping;
import com.orderservice.order.exception.ResourceAlreadyExists;
import com.orderservice.order.repository.WorkflowCountryRepository;
import com.orderservice.order.service.WorkflowCountryService;
import org.springframework.stereotype.Service;

import java.util.Optional;
@Service
public class WorkflowCountryServiceImpl implements WorkflowCountryService {

    private final WorkflowCountryRepository workflowCountryRepository;

    public WorkflowCountryServiceImpl(WorkflowCountryRepository workflowCountryRepository) {
        this.workflowCountryRepository = workflowCountryRepository;
    }

    @Override
    public void saveWorkflowPerCountry(String country, String workflow) {
        // Implementation for saving workflow per country
        Optional<CurrentCountryWorkflowMapping> existingWorkflow = workflowCountryRepository.findByCountryCode(country);
        if (existingWorkflow.isPresent()) {
            throw new ResourceAlreadyExists("Workflow for country " + country + " already exists.");
        }
        CurrentCountryWorkflowMapping workflowCountry = new CurrentCountryWorkflowMapping();
        workflowCountry.setCountryCode(country);
        workflowCountry.setWorkflowName(workflow);
        workflowCountryRepository.save(workflowCountry);


    }

    @Override
    public void updateWorkflowForCountry(String country, String workflow) {
        Optional<CurrentCountryWorkflowMapping> existingWorkflow = workflowCountryRepository.findByCountryCode(country);
        if (existingWorkflow.isEmpty()) {
            throw new ResourceAlreadyExists("Workflow for country " + country + " does not exist.");
        }
        CurrentCountryWorkflowMapping workflowCountry = existingWorkflow.get();
        workflowCountry.setWorkflowName(workflow);
        workflowCountryRepository.save(workflowCountry);
    }
}
