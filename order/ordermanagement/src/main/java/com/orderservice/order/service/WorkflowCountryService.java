package com.orderservice.order.service;

import com.orderservice.order.entity.CurrentCountryWorkflowMapping;
import com.orderservice.order.exception.ResourceAlreadyExists;
import org.springframework.stereotype.Service;

import java.util.Optional;

public interface WorkflowCountryService {

    public void saveWorkflowPerCountry(String country, String workflow);

    void updateWorkflowForCountry(String country, String workflow);
}
