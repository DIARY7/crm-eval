package site.easy.to.build.crm.dto;

import java.time.LocalDate;

import site.easy.to.build.crm.entity.Budget;

public class BudgetDto {
    public Integer id;
    public double montant;
    public Integer customerId;
    public String customerName;
    public LocalDate dateCreation;

    public BudgetDto(Budget budget) {
        this.id = budget.getId();
        this.montant = budget.getMontant();
        this.customerId = budget.getCustomerId();
        this.dateCreation = budget.getDateCreation();
        this.customerName = budget.getCustomerName();
    }
}
