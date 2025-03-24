package site.easy.to.build.crm.controller.rest;

import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import site.easy.to.build.crm.entity.Budget;
import site.easy.to.build.crm.entity.Depense;
import site.easy.to.build.crm.service.budget.BudgetService;
import site.easy.to.build.crm.service.depense.DepenseService;

@RestController
@RequestMapping("depense/rest")
public class RestDepenseController {
    @Autowired
    private BudgetService budgetService;
    @Autowired
    private DepenseService depenseService;

    @GetMapping("/check_taux")
    public HashMap<String, Object> checkSeuil(@RequestParam int id_budget, @RequestParam double montantPlus) {
        HashMap<String, Object> data = new HashMap<>();
        try {
            Budget budget = budgetService.findById(id_budget);
            depenseService.checkDepassementSeuilBudget(data, budget, montantPlus);
        } catch (Exception e) {
            // Retourner une réponse JSON d'erreur en cas d'exception
            data.put("error", "Une erreur s'est produite : " + e.getMessage());
            e.printStackTrace();
        }
        return data;
    }

    @GetMapping("/liste-depense")
    public List<Depense> getListeDepense(@RequestParam int id_customer){
        List<Depense> liste= depenseService.findAllDepenseCustomer(id_customer);
        System.out.println(liste.size());
        return liste;
    }

}
