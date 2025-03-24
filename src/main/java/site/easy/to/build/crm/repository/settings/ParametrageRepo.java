package site.easy.to.build.crm.repository.settings;

import org.springframework.data.jpa.repository.JpaRepository;

import site.easy.to.build.crm.entity.settings.Parametrage;

public interface ParametrageRepo extends JpaRepository<Parametrage,Integer> {
    Parametrage findById(int id);
} 
