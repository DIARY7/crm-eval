package site.easy.csvModel;

import com.opencsv.bean.CsvBindByName;

public class BudgetCsv {
    
    @CsvBindByName(column = "customer_email")
    private String email;
    
    @CsvBindByName(column = "Budget")
    private double budget;

    // Getters et setters
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public double getBudget() {
        return budget;
    }

    public void setBudget(double budget) {
        this.budget = budget;
    }
}
