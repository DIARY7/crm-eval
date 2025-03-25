package site.easy.to.build.crm.entity.settings;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "parametrage")
public class Parametrage {

    @Id
    private Integer id;

    private double tauxAlert;

    public Parametrage() {
    }

    public double getTauxAlert() {
        return tauxAlert;
    }

    public void setTauxAlert(double taux_alert) {
        this.tauxAlert = taux_alert;
    }
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

}
