package site.easy.csvModel;
import com.opencsv.bean.CsvBindByName;

public class CustomerCsv {
    
    @CsvBindByName(column = "name") // La colonne "name" du CSV sera mappée à la propriété "name"
    private String name;

    @CsvBindByName(column = "email") // La colonne "email" sera mappée à "email"
    private String email;

    @CsvBindByName(column = "position") // La colonne "position" sera mappée à "position"
    private String position;

    @CsvBindByName(column = "phone") // La colonne "phone" sera mappée à "phone"
    private String phone;

    @CsvBindByName(column = "address") // La colonne "address" sera mappée à "address"
    private String address;

    @CsvBindByName(column = "city") // La colonne "city" sera mappée à "city"
    private String city;

    @CsvBindByName(column = "state") // La colonne "state" sera mappée à "state"
    private String state;

    @CsvBindByName(column = "country") // La colonne "country" sera mappée à "country"
    private String country;

    @CsvBindByName(column = "description") // La colonne "description" sera mappée à "description"
    private String description;

    // Getters et setters pour chaque propriété
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPosition() { return position; }
    public void setPosition(String position) { this.position = position; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public String getCity() { return city; }
    public void setCity(String city) { this.city = city; }

    public String getState() { return state; }
    public void setState(String state) { this.state = state; }

    public String getCountry() { return country; }
    public void setCountry(String country) { this.country = country; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
}

