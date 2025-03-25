package site.easy.to.build.crm.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import site.easy.to.build.crm.entity.Customer;
import site.easy.to.build.crm.entity.Ticket;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public interface TicketRepository extends JpaRepository<Ticket, Integer> {
    public Ticket findByTicketId(int ticketId);

    public List<Ticket> findByManagerId(int id);

    public List<Ticket> findByEmployeeId(int id);

    List<Ticket> findByCustomerCustomerId(Integer customerId);

    List<Ticket> findByManagerIdOrderByCreatedAtDesc(int managerId, Pageable pageable);

    List<Ticket> findByEmployeeIdOrderByCreatedAtDesc(int managerId, Pageable pageable);

    List<Ticket> findByCustomerCustomerIdOrderByCreatedAtDesc(int customerId, Pageable pageable);

    long countByEmployeeId(int employeeId);

    long countByManagerId(int managerId);

    long countByCustomerCustomerId(int customerId);

    void deleteAllByCustomer(Customer customer);

    @Query(value = "SELECT p.priority AS priority, COALESCE(SUM(d.montant), 0) AS totalMontant " +
               "FROM ( " +
               "    SELECT 'low' AS priority " +
               "    UNION ALL " +
               "    SELECT 'medium' " +
               "    UNION ALL " +
               "    SELECT 'high' " +
               "    UNION ALL " +
               "    SELECT 'closed' " +
               "    UNION ALL " +
               "    SELECT 'urgent' " +
               "    UNION ALL " +
               "    SELECT 'critical' " +
               ") p " +
               "LEFT JOIN trigger_ticket t ON p.priority = t.priority " +
               "LEFT JOIN depense d ON t.id_depense = d.id " +
               "GROUP BY p.priority", nativeQuery = true)
List<Map<String, Double>> getTotalDepenseByPriority();

}
