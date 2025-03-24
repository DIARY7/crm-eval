package site.easy.to.build.crm.service.depense;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import site.easy.to.build.crm.entity.Budget;
import site.easy.to.build.crm.entity.Depense;
import site.easy.to.build.crm.repository.BudgetRepo;
import site.easy.to.build.crm.repository.DepenseRepo;
import site.easy.to.build.crm.repository.settings.ParametrageRepo;

@Service
public class DepenseService {

    @Autowired
    DepenseRepo depenseRepo;
    @Autowired
    ParametrageRepo parametrageRepo;
    @Autowired
    BudgetRepo budgetRepo;

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
        return depenseRepo.findByBudgetCustomerCustomerId(id_customer);
    }
    public Depense findById(int idDepense){
        return depenseRepo.findById(idDepense);
    }

    public int checkDepassementBudget(Model model , Budget budget,double montantPlus) {
        List<Depense> depenses = budget.getDepenses();
        double sommeDepense = 0;
        
        for (int i = 0; i < depenses.size(); i++) {
            sommeDepense+=depenses.get(i).getMontant();
        }
        if ((sommeDepense + montantPlus) > budget.getMontant() ) {
            double difference = sommeDepense - budget.getMontant();
            model.addAttribute("alert", 1);
            model.addAttribute("difference", difference);
            return 1; /* Nihotra */
        }
        
        return 0;
    }
    public int checkDepassementBudgetApi(Budget budget,double montantPlus) {
        List<Depense> depenses = budget.getDepenses();
        double sommeDepense = 0;
        
        for (int i = 0; i < depenses.size(); i++) {
            sommeDepense+=depenses.get(i).getMontant();
        }
        if ((sommeDepense + montantPlus) > budget.getMontant() ) {
            double difference = sommeDepense - budget.getMontant();
            return 1; /* Nihotra */
        }
        
        return 0;
    }
    
    public void checkDepassementSeuilBudget( HashMap<String,Object> data ,Budget budget,double montantPlus){
        List<Depense> depenses = budget.getDepenses();
        double sommeDepense = 0;
        double taux = parametrageRepo.findById(1).getTauxAlert();
        for (int i = 0; i < depenses.size(); i++) {
            sommeDepense+=depenses.get(i).getMontant();
        }

        double seuil = budget.getMontant()*taux;
        /* Raha ohatra ka somme */
        //double seuil = budget.getCustomer().getTotalBudget()*taux;
        
        if ((sommeDepense + montantPlus) > seuil ) {
            data.put("alert", 2);
            data.put("totalDepense", sommeDepense);
            data.put("Budget",budget.getMontant());
        }

    }

    public void updateMontant(int id_depense,double montant){
        Depense depense = depenseRepo.findById(id_depense);
        depense.setMontant(montant);
        depense.setDateUpdate(LocalDate.now());
        depenseRepo.save(depense);
    };

}
