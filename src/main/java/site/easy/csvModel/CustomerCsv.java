package site.easy.csvModel;
import com.opencsv.bean.CsvBindByName;

public class CustomerCsv {
    
    @CsvBindByName(column = "customer_email")
    private String email;

    @CsvBindByName(column = "customer_name")
    private String name;

    public String getEmail() {
        return email;
    }
    
    public String getName() {
        return name;
    }
}

