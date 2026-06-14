package com.expensetracker.controller;

import com.expensetracker.dto.EmiRequest;
import com.expensetracker.dto.EmiResponse;
import com.expensetracker.service.EmiCalculatorService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/tools")
public class FinancialToolsController {

    private final EmiCalculatorService emiCalculatorService;

    public FinancialToolsController(
            EmiCalculatorService emiCalculatorService) {

        this.emiCalculatorService = emiCalculatorService;
    }

    @PostMapping("/emi")
    public EmiResponse calculateEmi(
            @RequestBody EmiRequest request) {

        return emiCalculatorService.calculate(request);
    }
}