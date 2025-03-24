package site.easy.to.build.crm.controller;

import java.time.LocalDate;
import java.util.ArrayList;
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

import site.easy.to.build.crm.entity.Budget;
import site.easy.to.build.crm.entity.Customer;
import site.easy.to.build.crm.entity.Ticket;
import site.easy.to.build.crm.entity.User;
import site.easy.to.build.crm.service.budget.BudgetService;
import site.easy.to.build.crm.service.customer.CustomerService;
import site.easy.to.build.crm.service.user.UserService;
import site.easy.to.build.crm.util.AuthenticationUtils;
import site.easy.to.build.crm.util.AuthorizationUtil;

@Controller
@RequestMapping("/budget")
public class BudgetController {

    @Autowired
    private AuthenticationUtils authenticationUtils;

    @Autowired
    private UserService userService;

    @Autowired
    private BudgetService budgetService;

    @Autowired
    private CustomerService customerService;

    @GetMapping("/create")
    public String toFormBudget(Model model, Authentication authentication){
        int userId = authenticationUtils.getLoggedInUserId(authentication);
        User user = userService.findById(userId);
        if(user.isInactiveUser()) {
            return "error/account-inactive";
        }
        this.populateModelAttributes(model, authentication);
        model.addAttribute("budget", new Budget());
        return "budget/create-budget";
    }

    @GetMapping("/all-budgets")
    public String showAllBudgets(Model model) {
        List<Budget> tickets = budgetService.findAllBudget();
        model.addAttribute("budgets",tickets);
        return "budget/my-budgets";
    }

    @PostMapping("/create-budget")
    public String createBudget(Model model,Authentication authentication,
     @ModelAttribute("budget") @Validated Budget budget,BindingResult bindingResult , @RequestParam("customer_id") int customerId ){
        int userId = authenticationUtils.getLoggedInUserId(authentication);
        User manager = userService.findById(userId);
        if(manager.isInactiveUser()) {
            return "error/account-inactive";
        }

        if(bindingResult.hasErrors()) {
            this.populateModelAttributes(model, authentication);
            return "budget/create-budget";
        }

        Customer customer = customerService.findByCustomerId(customerId);
        budget.setCustomer(customer);
        budget.setDateCreation(LocalDate.now());
        
        try {
            budgetService.createBudget(budget);
        } catch (Exception e) {
            // TODO: handle exception
            throw new RuntimeException(e);
        }

        return "redirect:/budget/all-budgets" ;
    }

    private void populateModelAttributes(Model model,Authentication authentication){
        model.addAttribute("customers", customerService.findAll());
    }
}
