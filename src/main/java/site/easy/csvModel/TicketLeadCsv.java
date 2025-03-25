package site.easy.csvModel;

import com.opencsv.bean.CsvBindByName;

public class TicketLeadCsv {

    @CsvBindByName(column = "customer_email")
    private String email;

    @CsvBindByName(column = "subject_or_name")
    private String subjectOrName;

    @CsvBindByName(column = "type")
    private String type;

    @CsvBindByName(column = "status")
    private String status;

    @CsvBindByName(column = "expense")
    private Double expense;

    public String getEmail() {
        return email;
    }

    public String getSubjectOrName() {
        return subjectOrName;
    }

    public String getType() {
        return type;
    }

    public String getStatus() {
        return status;
    }

    public Double getExpense() {
        return expense;
    }
}
