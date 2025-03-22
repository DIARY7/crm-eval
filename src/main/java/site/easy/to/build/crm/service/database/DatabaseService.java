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
}
