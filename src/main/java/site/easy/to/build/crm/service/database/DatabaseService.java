package site.easy.to.build.crm.service.database;

import jakarta.transaction.Transactional;
import site.easy.csvModel.BudgetCsv;
import site.easy.csvModel.CustomerCsv;
import site.easy.csvModel.TicketLeadCsv;
import site.easy.exception.MyCsvException;
import site.easy.to.build.crm.entity.Budget;
import site.easy.to.build.crm.entity.Customer;
import site.easy.to.build.crm.entity.CustomerLoginInfo;
import site.easy.to.build.crm.entity.Depense;
import site.easy.to.build.crm.entity.Lead;
import site.easy.to.build.crm.entity.Ticket;
import site.easy.to.build.crm.entity.User;
import site.easy.to.build.crm.repository.CustomerRepository;
import site.easy.to.build.crm.service.budget.BudgetService;
import site.easy.to.build.crm.service.customer.CustomerLoginInfoService;
import site.easy.to.build.crm.service.lead.LeadService;
import site.easy.to.build.crm.service.ticket.TicketService;
import site.easy.to.build.crm.service.user.UserService;
import site.easy.to.build.crm.util.AuthenticationUtils;
import site.easy.to.build.crm.util.EmailTokenUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import com.opencsv.exceptions.CsvException;

import java.io.BufferedReader;

import java.io.InputStreamReader;
import java.io.Reader;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

@Service
public class DatabaseService {
    
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    CustomerRepository customerRepository;

    @Autowired
    AuthenticationUtils authenticationUtils;

    @Autowired
    CustomerLoginInfoService customerLoginInfoService;
    
    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    UserService userService;

    @Autowired
    LeadService leadService;

    @Autowired
    TicketService ticketService;

    @Autowired
    BudgetService budgetService;

    private static final List<String> VALID_TYPES = List.of("lead", "ticket");
    private static final List<String> VALID_STATUSES_LEADS = List.of("meeting-to-schedule", "assign-to-sales", "archived","success");
    private static final List<String> VALID_STATUSES_TICKETS = List.of(
        "open", 
        "assigned", 
        "on-hold", 
        "in-progress", 
        "resolved", 
        "closed", 
        "reopened", 
        "pending-customer-response", 
        "escalated", 
        "archived"
    );
    private static final List<String> TICKET_PRIORITIES = List.of(
        "low", 
        "medium", 
        "high", 
        "closed", 
        "urgent", 
        "critical"
    );

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
                "depense",
                "budget",
                "customer_login_info",
                "customer",
                "trigger_ticket",
                "trigger_contract"
        ); 
        tables.forEach(table->{
            jdbcTemplate.execute("TRUNCATE TABLE "+table);
        });
        jdbcTemplate.execute("SET FOREIGN_KEY_CHECKS = 1");
    }

    
    @Transactional
    public void importCSV(MultipartFile customerFile, MultipartFile leadTicketFile,MultipartFile budgetFile , Authentication authentication ) throws Exception ,MyCsvException {
        List<TicketLeadCsv> ticketLeads = readLeadsTickets(leadTicketFile);
        List<BudgetCsv> budgets = readBudgets(budgetFile);
        List<CustomerCsv> customers = readCustomers(customerFile);

        validateData(customers, ticketLeads , budgets );
        
        int userId = authenticationUtils.getLoggedInUserId(authentication);
        User user = userService.findById(userId);

        // Insérer les clients
        for (CustomerCsv dto : customers) {
            Customer customer = new Customer();
            customer.setEmail(dto.getEmail());
            customer.setName(dto.getName());
            customer.setUser(user);
            CustomerLoginInfo customerLoginInfo = completeCustomer(customer);
            customerLoginInfoService.save(customerLoginInfo);        
        }

        // Insérer les leads/tickets

        for (TicketLeadCsv dto : ticketLeads) {
            Customer customer = customerRepository.findByEmail(dto.getEmail());
            if (customer == null) {
                throw new RuntimeException("Customer email not found: " + dto.getEmail());
            }
            Depense depense = new Depense();
            depense.setMontant(dto.getExpense());
            depense.setDateUpdate(LocalDate.now());

            if (dto.getType().compareToIgnoreCase("ticket")==0) {
                Ticket ticket = new Ticket();
                ticket.setCustomer(customer);
                ticket.setStatus(dto.getStatus());
                ticket.setDepense(depense);
                ticket.setCreatedAt(LocalDateTime.now());
                ticket.setSubject(dto.getSubjectOrName());
                Random random = new Random();
                String randomPriority = TICKET_PRIORITIES.get(random.nextInt(TICKET_PRIORITIES.size()));
                ticket.setPriority(randomPriority);
                ticket.setEmployee(user);
                ticket.setManager(user);
                ticketService.save(ticket);  
            }
            else if (dto.getType().compareToIgnoreCase("lead")==0) {
                Lead lead = new Lead();
                lead.setCustomer(customer);
                lead.setStatus(dto.getStatus());
                lead.setDepense(depense);
                lead.setCreatedAt(LocalDateTime.now());
                lead.setName(dto.getSubjectOrName());
                lead.setEmployee(user);
                lead.setManager(user);
                leadService.save(lead);
            }
            
        }

        for (BudgetCsv budgetCsv : budgets ) {
            Customer customer = customerRepository.findByEmail(budgetCsv.getEmail());
            if (customer == null) {
                throw new RuntimeException("Customer email not found: " + budgetCsv.getEmail());
            }

            Budget budget = new Budget();
            budget.setCustomer(customer);
            budget.setMontant(budgetCsv.getBudget());
            budget.setDateCreation(LocalDate.now());
            
            budgetService.createBudget(budget);
            
        }

    }

    private List<CustomerCsv> readCustomers(MultipartFile file) throws MyCsvException,Exception {
        List<CustomerCsv> records = new ArrayList<>();
        String fileName = file.getOriginalFilename();
    
        try (Reader reader = new BufferedReader(new InputStreamReader(file.getInputStream()))) {
            CsvToBean<CustomerCsv> csvToBean = new CsvToBeanBuilder<CustomerCsv>(reader)
                    .withType(CustomerCsv.class)
                    .withIgnoreLeadingWhiteSpace(true)
                    .withSeparator(';')
                    .withThrowExceptions(false) // Désactive les exceptions automatiques
                    .build();
    
            int lineNumber = 1; // Compteur de ligne
            for (CustomerCsv ticket : csvToBean) {
                try {
                    records.add(ticket);
                    lineNumber++;
                } catch (Exception e) {
                    throw new MyCsvException(lineNumber, fileName, e.getMessage() );
                    //throw new Exception("Erreur à la ligne " + lineNumber + " dans le fichier " + fileName + ": " + e.getMessage(), e);
                }
            }
    
            // Vérifier les erreurs capturées par OpenCSV
            for (CsvException exception : csvToBean.getCapturedExceptions()) {
                System.err.println("Erreur à la ligne " + exception.getLineNumber() + " dans le fichier " + fileName + ": " + exception.getMessage());
                throw new MyCsvException((int) exception.getLineNumber(), fileName, exception.getMessage());
            }
    
        } 
        catch(MyCsvException ex){
            throw ex;
        }
        catch (Exception e) {
            throw new MyCsvException(0, fileName, e.getMessage() );
            //throw new Exception("Erreur lors de la lecture du fichier " + fileName + ": " + e.getMessage(), e);
        }
        
        return records;
    }

    
    public List<TicketLeadCsv> readLeadsTickets(MultipartFile file) throws Exception ,MyCsvException{
        List<TicketLeadCsv> records = new ArrayList<>();
        String fileName = file.getOriginalFilename();
    
        try (Reader reader = new BufferedReader(new InputStreamReader(file.getInputStream()))) {
            CsvToBean<TicketLeadCsv> csvToBean = new CsvToBeanBuilder<TicketLeadCsv>(reader)
                    .withType(TicketLeadCsv.class)
                    .withIgnoreLeadingWhiteSpace(true)
                    .withSeparator(';')
                    .withThrowExceptions(false) // Désactive les exceptions automatiques
                    .build();
    
            int lineNumber = 1; // Compteur de ligne
            for (TicketLeadCsv ticket : csvToBean) {
                try {
                    records.add(ticket);
                    lineNumber++;
                } catch (Exception e) {
                    throw new MyCsvException(lineNumber, fileName, e.getMessage() );
                    
                    //throw new Exception("Erreur à la ligne " + lineNumber + " dans le fichier " + fileName + ": " + e.getMessage(), e);
                }
            }
    
            // Vérifier les erreurs capturées par OpenCSV
            for (CsvException exception : csvToBean.getCapturedExceptions()) {
                System.err.println("Erreur à la ligne " + exception.getLineNumber() + " dans le fichier " + fileName + ": " + exception.getMessage());
                throw new MyCsvException((int) exception.getLineNumber(), fileName, exception.getMessage());
            }
    
        } 
        catch(MyCsvException ex){
            throw ex;
        }
        
        catch (Exception e) {
            throw new MyCsvException(0, fileName, e.getMessage() );
            //throw new Exception("Erreur lors de la lecture du fichier " + fileName + ": " + e.getMessage(), e);
        }
        
        return records;
    }

    private List<BudgetCsv> readBudgets(MultipartFile file) throws Exception {
        List<BudgetCsv> records = new ArrayList<>();
        String fileName = file.getOriginalFilename();

        try (Reader reader = new InputStreamReader(file.getInputStream())) {
            
            CsvToBean<BudgetCsv> csvToBean = new CsvToBeanBuilder<BudgetCsv>(reader)
                    .withType(BudgetCsv.class)
                    .withIgnoreLeadingWhiteSpace(true)
                    .withIgnoreEmptyLine(true) // Ignore les lignes vides
                    .withSeparator(';') // Spécifie le bon séparateur
                    .build();
            int lineNumber = 1; // Compteur de ligne
            for (BudgetCsv ticket : csvToBean) {
                try {
                    records.add(ticket);
                    lineNumber++;
                } catch (Exception e) {
                    throw new MyCsvException(lineNumber, fileName, e.getMessage() );
                    //throw new Exception("Erreur à la ligne " + lineNumber + " dans le fichier " + fileName + ": " + , e);
                }
            }
    
            // Vérifier les erreurs capturées par OpenCSV
            for (CsvException exception : csvToBean.getCapturedExceptions()) {
                System.err.println("Erreur à la ligne " + exception.getLineNumber() + " dans le fichier " + fileName + ": " + exception.getMessage());
                throw new MyCsvException((int) exception.getLineNumber(), fileName, exception.getMessage());
            }
    
        } 
        catch(MyCsvException ex){
            throw ex;
        }
        catch (Exception e) {
            throw new MyCsvException(0, fileName, e.getMessage() );
            //throw new Exception("Erreur lors de la lecture du fichier " + fileName + ": " + e.getMessage(), e);
        }
        
        return records;
    }


    private void validateData(List<CustomerCsv> customers, List<TicketLeadCsv> ticketLeads, List<BudgetCsv> budgets) throws MyCsvException {
        int nbLigne = 1;
        for (TicketLeadCsv dto : ticketLeads) {
            if (!VALID_TYPES.contains(dto.getType())) {
                
                throw new MyCsvException(nbLigne, TicketLeadCsv.class.getSimpleName() , "Type invalide : " + dto.getType());
            }
            if (!VALID_STATUSES_LEADS.contains(dto.getStatus()) && dto.getType().compareToIgnoreCase("lead")==0 ) {
                throw new MyCsvException(nbLigne, TicketLeadCsv.class.getSimpleName() , "Status invalid: " + dto.getStatus());
            }
            if (!VALID_STATUSES_TICKETS.contains(dto.getStatus()) && dto.getType().compareToIgnoreCase("ticket")==0 ) {
                throw new MyCsvException(nbLigne, TicketLeadCsv.class.getSimpleName() , "Status invalid: " + dto.getStatus());
            }
            if (dto.getExpense() < 0) {
                throw new MyCsvException(nbLigne, TicketLeadCsv.class.getSimpleName() , "Expense cannot be negative: " + dto.getExpense());
            }
            nbLigne++;
        }

        nbLigne = 1;

        for (BudgetCsv budgetCsv : budgets ) {
            if (budgetCsv.getBudget() < 0) {
                throw new MyCsvException(nbLigne, BudgetCsv.class.getSimpleName() , "Le budget ne doit pas être negatif");
            }
            nbLigne++;
        }
    }

    private CustomerLoginInfo completeCustomer(Customer customer){

        customer.setCountry("Madagascar");
        customer.setCreatedAt(LocalDateTime.now());
        

        CustomerLoginInfo customerLoginInfo = new CustomerLoginInfo();
        String token = EmailTokenUtils.generateToken();
        customerLoginInfo.setToken(token);
        String hashPassword = passwordEncoder.encode("123");
        customerLoginInfo.setPassword(hashPassword);
        customerLoginInfo.setPasswordSet(true);

        customer.setCustomerLoginInfo(customerLoginInfo);
        customerLoginInfo.setCustomer(customer);

        return customerLoginInfo;
        
    } 

    
}
