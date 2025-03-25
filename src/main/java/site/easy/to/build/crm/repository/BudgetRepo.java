package site.easy.to.build.crm.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import site.easy.to.build.crm.entity.Budget;


public interface BudgetRepo extends JpaRepository<Budget,Integer> {
    public List<Budget> findByCustomer_CustomerId(int customer_id);    
}
