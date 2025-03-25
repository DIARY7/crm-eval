package site.easy.to.build.crm.controller;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import site.easy.to.build.crm.entity.Budget;
import site.easy.to.build.crm.entity.Depense;
import site.easy.to.build.crm.entity.User;
import site.easy.to.build.crm.service.budget.BudgetService;
import site.easy.to.build.crm.service.depense.DepenseService;
import site.easy.to.build.crm.service.user.UserService;
import site.easy.to.build.crm.util.AuthenticationUtils;

@Controller
@RequestMapping("/depense")
public class DepenseController {

    @Autowired
    private AuthenticationUtils authenticationUtils;
    @Autowired
    private UserService userService;
    @Autowired
    private BudgetService budgetService;
    @Autowired
    private DepenseService depenseService;

    @GetMapping("/create")
    public String toFormDepense(Model model,Authentication authentication){
        int userId = authenticationUtils.getLoggedInUserId(authentication);
        User user = userService.findById(userId);
        if(user.isInactiveUser()) {
            return "error/account-inactive";
        }
        model.addAttribute("budgets", budgetService.findAllBudget());
        model.addAttribute("depense", new Depense());
        return "depense/create-depense";
    }

    @GetMapping("/all-depenses")
    public String showAllDepense(Model model){
        List<Depense> depenses = depenseService.findAllDepense();
        model.addAttribute("depenses", depenses);
        return "depense/my-depenses";
    }

    // @PostMapping("/create-depense")
    // public String toFormDepense(Model model,Authentication authentication,
    //  @ModelAttribute("depense") @Validated Depense depense,BindingResult bindingResult , @RequestParam("id_budget") int idBudget  
    // ){
    //     int userId = authenticationUtils.getLoggedInUserId(authentication);
    //     User user = userService.findById(userId);
    //     if(user.isInactiveUser()) {
    //         return "error/account-inactive";
    //     }

    //     if(bindingResult.hasErrors()) {
    //         model.addAttribute("budgets", budgetService.findAllBudget());
    //         return "depense/create-depense";
    //     }
        
    //     try {
    //         Budget budget = budgetService.findById(idBudget);
    //         int value = depenseService.checkDepassementBudget(model, budget, depense.getMontant());
    //         depense.setBudget(budget);
    //         depense.setDateUpdate(LocalDate.now());
    //         if (value==1) { /* Nihotra */
    //             model.addAttribute("budgets", budgetService.findAllBudget());
    //             return "depense/create-depense";
    //         }
    //         if (value==0) { /* Tsy nihotra mintsy */   
    //             depenseService.saveDepense(depense);
    //         }
    //     } catch (Exception e) {
    //         // TODO: handle exception
    //         throw new RuntimeException(e);
    //     }
    //     return "redirect:/depense/all-depenses";
    // }

    @PostMapping("/confirm-depense")
    public String confirmDepense(Model model,@ModelAttribute("depense") Depense depense , @RequestParam("id_budget") int idBudget ){
        try {
            Budget budget = budgetService.findById(idBudget);
            depense.setBudget(budget);
            depense.setDateUpdate(LocalDate.now());
            depenseService.saveDepense(depense);
            
        } catch (Exception e) {
            // TODO: handle exception
            new RuntimeException(e);
        }
        return "redirect:/depense/all-depenses";
    }


}
