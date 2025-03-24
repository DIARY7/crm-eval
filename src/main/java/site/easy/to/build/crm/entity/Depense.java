package site.easy.to.build.crm.entity;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

@Entity
@Table(name = "depense")
public class Depense {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false, precision = 18, scale = 3)
    @NotNull(message = "Le montant est obligatoire")
    @DecimalMin(value = "0", message = "Le montant doit être supérieur à zéro")
    @Positive(message = "Le montant doit être positif")
    private double montant;

    @Column(length = 75)
    private String description;

    @Column(name = "date_update")
    private LocalDate dateUpdate;

    @ManyToOne
    @JoinColumn(name = "id_budget",referencedColumnName = "id")
    @JsonIgnore
    Budget budget;


    public Depense() {
    }

    @JsonProperty("budgetId")
    public Integer getBudgetId() {
        return (budget != null) ? budget.getId() : null;
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

    public LocalDate getDateUpdate() {
        return dateUpdate;
    }

    public void setDateUpdate(LocalDate dateUpdate) {
        this.dateUpdate = dateUpdate;
    }
    public Budget getBudget() {
        return budget;
    }

    public void setBudget(Budget budget) {
        this.budget = budget;
    }
}

