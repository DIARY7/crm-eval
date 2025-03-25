package site.easy.to.build.crm.dto;

import site.easy.to.build.crm.entity.Depense;
import site.easy.to.build.crm.entity.Lead;

public class LeadDto {

    public int leadId;
    public String name;
    public String status;
    public String phone;
    public Depense depense;

    public LeadDto(Lead lead){
        this.leadId = lead.getLeadId();
        this.name = lead.getName();
        this.status = lead.getStatus();
        this.phone = lead.getPhone();
        this.depense = lead.getDepense();
    }
}
