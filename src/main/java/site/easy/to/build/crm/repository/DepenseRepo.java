package site.easy.to.build.crm.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import site.easy.to.build.crm.entity.Depense;

public interface DepenseRepo extends JpaRepository<Depense,Integer>{
    List<Depense> findByBudgetCustomerCustomerId(int customerId);
    Depense findById(int id);

    @Query(value = """
        SELECT d.*
        FROM depense d
        LEFT JOIN trigger_ticket t ON d.id = t.id_depense
        LEFT JOIN trigger_lead l ON d.id = l.id_depense
        WHERE t.customer_id = :id_customer OR l.customer_id = :id_customer
    """, nativeQuery = true)
    List<Depense> findAllDepenseCustomer(@Param("id_customer") int idCustomer);
}
