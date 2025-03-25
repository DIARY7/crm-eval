package site.easy.to.build.crm.service.depense;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import site.easy.to.build.crm.entity.Budget;
import site.easy.to.build.crm.entity.Customer;
import site.easy.to.build.crm.entity.Depense;
import site.easy.to.build.crm.entity.Ticket;
import site.easy.to.build.crm.repository.BudgetRepo;
import site.easy.to.build.crm.repository.DepenseRepo;
import site.easy.to.build.crm.repository.settings.ParametrageRepo;
import site.easy.to.build.crm.service.lead.LeadService;
import site.easy.to.build.crm.service.ticket.TicketService;

@Service
public class DepenseService {

    @Autowired
    DepenseRepo depenseRepo;
    @Autowired
    ParametrageRepo parametrageRepo;
    @Autowired
    BudgetRepo budgetRepo;
    @Autowired
    TicketService ticketService;
    @Autowired
    LeadService leadService;

    public Depense createDepense(double montant , String description,LocalDate dateUpdate) throws Exception {
        Depense depense = new Depense();
        depense.setMontant(montant);
        depense.setDescription(description);
        depense.setDateUpdate(dateUpdate);
        depense = depenseRepo.save(depense);
        return depense;
    }

    public List<Depense> findAllDepense(){
        return depenseRepo.findAll();
    }

    public Depense saveDepense(Depense depense){
        return depenseRepo.save(depense);
    }
    public List<Depense>  findAllDepenseCustomer(int id_customer){
        return depenseRepo.findAllDepenseCustomer(id_customer);
    }
    public Depense findById(int idDepense){
        return depenseRepo.findById(idDepense);
    }

    // public int checkDepassementBudget(Model model , Budget budget , double montantPlus) {
    //     List<Depense> depenses = budget.getDepenses();
    //     double sommeDepense = 0;
        
    //     for (int i = 0; i < depenses.size(); i++) {
    //         sommeDepense+=depenses.get(i).getMontant();
    //     }
    //     if ((sommeDepense + montantPlus) > budget.getMontant() ) {
    //         double difference = sommeDepense - budget.getMontant();
    //         model.addAttribute("alert", 1);
    //         model.addAttribute("difference", difference);
    //         return 1; /* Nihotra */
    //     }
        
    //     return 0;
    // }

    public int checkDepassementBudget(Model model , Customer customer,double montantPlus) {
        
        List<Depense>  depenses = this.findAllDepenseCustomer(customer.getCustomerId());
        double sommeDepense = 0;
        
        for (int i = 0; i < depenses.size(); i++) {
            sommeDepense+=depenses.get(i).getMontant();
        }
        
        double totalBudget = customer.getTotalBudget();
        
        if ((sommeDepense + montantPlus) > totalBudget ) {
            double difference = sommeDepense - totalBudget;
            model.addAttribute("alert", 1);
            model.addAttribute("difference", difference);
            return 1; /* Nihotra */
        }
        
        return 0;
    }

    public int checkDepassementBudgetApi(Customer customer ,double montantPlus) {
        List<Depense> depenses = this.findAllDepenseCustomer(customer.getCustomerId());
        double sommeDepense = 0;
        
        for (int i = 0; i < depenses.size(); i++) {
            sommeDepense+=depenses.get(i).getMontant();
        }

        double totalBudget = customer.getTotalBudget();
        
        if ((sommeDepense + montantPlus) > totalBudget ) {
            double difference = sommeDepense - totalBudget;
            return 1; /* Nihotra */
        }
        
        return 0;
    }
    
    public void checkDepassementSeuilBudget( HashMap<String,Object> data , Customer customer,double montantPlus){
        List<Depense> depenses = this.findAllDepenseCustomer(customer.getCustomerId());
        double sommeDepense = 0;
        double taux = parametrageRepo.findById(1).getTauxAlert();
        for (int i = 0; i < depenses.size(); i++) {
            sommeDepense+=depenses.get(i).getMontant();
        }

        //double seuil = budget.getMontant()*taux;
        /* Raha ohatra ka somme */
        double seuil = customer.getTotalBudget()*taux;
        System.out.println("Le total budget du customer "+customer.getTotalBudget());
        System.out.println("Le seuil est "+seuil);
        System.out.println("La somme depense est "+sommeDepense);
        
        if ((sommeDepense + montantPlus) > seuil ) {
            data.put("alert", 2);
            data.put("totalDepense", sommeDepense);
            data.put("totalBudget",customer.getTotalBudget());
        }

    }

    public void updateMontant(int id_depense,double montant){
        Depense depense = depenseRepo.findById(id_depense);
        depense.setMontant(montant);
        depense.setDateUpdate(LocalDate.now());
        depenseRepo.save(depense);
    };

}
