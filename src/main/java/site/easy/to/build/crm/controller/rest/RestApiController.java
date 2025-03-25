package site.easy.to.build.crm.controller.rest;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import site.easy.to.build.crm.dto.BudgetDto;
import site.easy.to.build.crm.dto.LeadDto;
import site.easy.to.build.crm.dto.TicketDto;
import site.easy.to.build.crm.entity.Budget;
import site.easy.to.build.crm.entity.Customer;
import site.easy.to.build.crm.entity.Lead;
import site.easy.to.build.crm.entity.Ticket;
import site.easy.to.build.crm.entity.settings.Parametrage;
import site.easy.to.build.crm.repository.BudgetRepo;
import site.easy.to.build.crm.repository.DepenseRepo;
import site.easy.to.build.crm.repository.settings.ParametrageRepo;
import site.easy.to.build.crm.service.budget.BudgetService;
import site.easy.to.build.crm.service.customer.CustomerService;
import site.easy.to.build.crm.service.depense.DepenseService;
import site.easy.to.build.crm.service.lead.LeadService;
import site.easy.to.build.crm.service.ticket.TicketService;

@RestController
@RequestMapping("/rest")
@CrossOrigin(origins = "http://localhost:5244")
public class RestApiController {

    @Autowired
    ParametrageRepo parametrageRepo;

    @Autowired
    BudgetRepo budgetRepo;

    @Autowired
    TicketService ticketService;

    @Autowired
    LeadService leadService;

    @Autowired
    DepenseService depenseService;

    @Autowired
    BudgetService budgetService;

    @Autowired
    CustomerService customerService;

    @GetMapping("/budgets/{idCustomer}")
    public List<Budget> getBudgetCustomer(@PathVariable(name="idCustomer") int idCustomer ){
        return budgetService.findByCustomer(idCustomer);
    }

    @GetMapping("/check_budget")
    public int checkDepassement(@RequestParam(name = "id_customer") int idCustomer , @RequestParam(name = "montant") double montant ) throws Exception{
        Customer customer = customerService.findByCustomerId(idCustomer);
        int response = depenseService.checkDepassementBudgetApi(customer, montant);
        return response;
    }
    
    @GetMapping("/update_taux_alert")
    public ResponseEntity<?> updateTauxAlert(@RequestParam String taux){
        try {
            Parametrage parametrage = parametrageRepo.findById(1);
            NumberFormat format = NumberFormat.getInstance(Locale.FRANCE); // Pour accepter "0,7"
            Number number = format.parse(taux);
            double tauxDouble = number.doubleValue();
            
            parametrage.setTauxAlert(tauxDouble);
            parametrageRepo.save(parametrage);

            
            return ResponseEntity.ok("Taux alert mis à jour avec succès !");
        }catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Une erreur s'est produite lors de la mise à jour.");
        }
        
    }

    @GetMapping("/get_prix_alert")
    public double getTauxAlert(){
        Parametrage parametrage = parametrageRepo.findById(1);
        double taux = parametrage.getTauxAlert();
        return taux;
    }

    @GetMapping("/get_all_ticket")
    public List<TicketDto> getAllTickets(){
        List<Ticket> listeTicket = ticketService.findAll();
        List<TicketDto> liste = new ArrayList<>();
        for (int i = 0; i < listeTicket.size(); i++) {
            liste.add(new TicketDto(listeTicket.get(i)));
        }
        return liste;
    }

    @GetMapping("/get_all_lead")
    public List<LeadDto> getAllLeads(){
        List<Lead> leads = leadService.findAll();
        List<LeadDto> liste = new ArrayList<>();
        for (int i = 0; i < leads.size(); i++) {
            liste.add(new LeadDto(leads.get(i)));
        }
        return liste;
    }

    @GetMapping("/get_all_budget")
    public List<BudgetDto> getAllBudgets(){
        List<Budget> budgets = budgetRepo.findAll();
        List<BudgetDto> liste = new ArrayList<>();
        for (int i = 0; i < budgets.size() ; i++) {
            liste.add(new BudgetDto(budgets.get(i)));
        }
        return liste;
    }

    @GetMapping("/get_ticket_by_priority")
    public List<Map<String,Double>> montantByStatus(){
        return ticketService.getTotalDepenseByPriority();
    }
    
    @GetMapping("/update_depense")
    public ResponseEntity<?> updateMontant(int idDepense , double montant){
        try { 
            depenseService.updateMontant( idDepense , montant);
            return ResponseEntity.ok("Updater");
        
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Une erreur s'est produite lors de la mise à jour.");            
        }
    }

    @GetMapping("/delete_ticket")
    public ResponseEntity<?> deleteTicket(@RequestParam int id_ticket){
        try {
            Ticket ticket = ticketService.findByTicketId(id_ticket);
            ticketService.delete(ticket);
            System.out.println("Ticket supprimer");
            return ResponseEntity.ok("Ticket supprimé");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Une erreur s'est produite lors de la suppression.");
            
        }
    }

    @GetMapping("/delete_lead")
    public ResponseEntity<?> deleteLead(@RequestParam int id_lead){
        try {
            Lead lead = leadService.findByLeadId(id_lead);
            leadService.delete(lead);
            return ResponseEntity.ok("Lead supprimé");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Une erreur s'est produite lors de la suppression.");
        }
    }

}
