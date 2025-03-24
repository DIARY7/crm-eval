package site.easy.to.build.crm.service.database;

import jakarta.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DatabaseService {
    
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Transactional
    public void resetDatabase(){
        jdbcTemplate.execute("SET FOREIGN_KEY_CHECKS = 0");

        List<String> tables = List.of(
                "contract_settings",
                "email_template",
                "employee",
                "file",
                "google_drive_file",
                "lead_action",
                "lead_settings",
                "ticket_settings",
                "trigger_lead",
                "trigger_ticket",
                "trigger_contract"
        ); 
        tables.forEach(table->{
            jdbcTemplate.execute("TRUNCATE TABLE "+table);
        });
        jdbcTemplate.execute("SET FOREIGN_KEY_CHECKS = 1");
    }

    public void importerCsv(MultipartFile file) {
        try (Reader reader = new BufferedReader(
                new InputStreamReader(file.getInputStream(), StandardCharsets.UTF_8))) {

            CsvToBean<CustomerCsv> csvToBean = new CsvToBeanBuilder<CustomerCsv>(reader)
                    .withType(CustomerCsv.class)
                    .withIgnoreLeadingWhiteSpace(true)
                    .build();

            List<CustomerCsv> customerCsvList = csvToBean.parse();

            // Convertir chaque ligne CSV en entité Customer et la sauvegarder
            for (CustomerCsv customerCsv : customerCsvList) {
                Customer customer = new Customer();

                // Map les données de customerCsv vers l'entité Customer
                customer.setName(customerCsv.getName());
                customer.setEmail(customerCsv.getEmail());
                customer.setPosition(customerCsv.getPosition());
                customer.setPhone(customerCsv.getPhone());
                customer.setAddress(customerCsv.getAddress());
                customer.setCity(customerCsv.getCity());
                customer.setState(customerCsv.getState());
                customer.setCountry(customerCsv.getCountry());
                customer.setDescription(customerCsv.getDescription());
                customer.setCreatedAt(LocalDateTime.now());

                // Sauvegarder l'entité Customer
                customerRepository.save(customer);
            }

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Erreur lors de l'importation du fichier CSV : " + e.getMessage());
        }
    }
}
