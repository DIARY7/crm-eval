package site.easy.to.build.crm.service.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import site.easy.to.build.crm.service.budget.BudgetService;
import site.easy.to.build.crm.service.lead.LeadService;
import site.easy.to.build.crm.service.ticket.TicketService;

@Service
public class RestApiService {
    
    @Autowired
    LeadService leadService;
    @Autowired
    BudgetService budgetService;
    @Autowired
    TicketService ticketService;

    // public TotalThree getTotalThree(){
    //      leadService.findAll();
    // }
}
