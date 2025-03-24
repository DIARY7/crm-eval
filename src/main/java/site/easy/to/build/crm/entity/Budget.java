package site.easy.to.build.crm.entity;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;

@Entity
@Table(name = "budget")
public class Budget {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, precision = 18, scale = 3)
    @NotNull(message = "Le montant est obligatoire")
    @DecimalMin(value = "0", message = "Le montant doit être supérieur à zéro")
    @Positive(message = "Le montant doit être positif")
    private double montant;

    @Column(length = 75)
    private String description;

    @ManyToOne
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;

    @Transient
    private Integer customerId;

    @Transient
    private String customerName;

    @OneToMany(mappedBy = "budget", cascade = CascadeType.ALL)
    List<Depense> depenses; 
    

    LocalDate dateCreation;

    public Budget() {
    }

    public Integer getCustomerId() {
        if (this.customer != null) {
            return this.customer.getCustomerId();
        }
        return null;
    }

    public String getCustomerName() {
        if (this.customer != null) {
            return this.customer.getName();
        }
        return null;
    }



    // Getters et Setters
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public double getMontant() {
        return montant;
    }

    public void setMontant(double montant) {
        this.montant = montant;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
        if (customer != null) {
            this.customerId = customer.getCustomerId(); // Met à jour customerId si le client est défini
            this.customerName = customer.getName();
        }
    }

    public void setDateCreation(LocalDate dateCreation) {
        this.dateCreation = dateCreation;
    }

    public LocalDate getDateCreation() {
        return dateCreation;
    }
    public List<Depense> getDepenses() {
        return depenses;
    }

    public void setDepenses(List<Depense> depenses) {
        this.depenses = depenses;
    }
}

