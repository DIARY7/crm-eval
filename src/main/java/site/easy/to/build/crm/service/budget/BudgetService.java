package site.easy.to.build.crm.service.budget;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import site.easy.to.build.crm.entity.Budget;
import site.easy.to.build.crm.entity.Customer;
import site.easy.to.build.crm.repository.BudgetRepo;
import site.easy.to.build.crm.repository.CustomerRepository;

@Service
public class BudgetService {
    @Autowired
    BudgetRepo budgetRepo;

    @Autowired
    CustomerRepository customerRepo;

    public Budget createBudget(double montant , String desciption , int id_customer) throws Exception {
        Customer customer = customerRepo.findById(id_customer).orElseThrow(()-> new Exception("Custommer introuvable"));
        Budget budget = new Budget();
        budget.setMontant(montant);
        budget.setDescription(desciption);
        budget.setCustomer(customer);
        budget = budgetRepo.save(budget);
        return budget;
    }

    public Budget createBudget(Budget budget) throws Exception {
        budget = budgetRepo.save(budget);
        return budget;
    }

    public List<Budget> findAllBudget(){
        return budgetRepo.findAll();
    }
    public Budget findById(int id_budget) throws Exception {
        return budgetRepo.findById(id_budget).orElseThrow(()-> new Exception("Budget introuvable"));
    }

    public List<Budget> findByCustomer(int id_customer){
        return budgetRepo.findByCustomer_CustomerId(id_customer);
    }
}
