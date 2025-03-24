package site.easy.to.build.crm.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import site.easy.to.build.crm.entity.Depense;

public interface DepenseRepo extends JpaRepository<Depense,Integer>{
    List<Depense> findByBudgetCustomerCustomerId(int customerId);
    Depense findById(int id);
}
