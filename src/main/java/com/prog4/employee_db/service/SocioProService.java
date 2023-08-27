package com.prog4.employee_db.service;

import com.prog4.employee_db.entity.SocioPro;
import com.prog4.employee_db.repository.SocioProRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@Service
public class SocioProService {

    private final SocioProRepository repository;

    public List<SocioPro> findAll(){
        return repository.findAll();
    }

    public SocioPro getById(Long id){
        Optional<SocioPro> optionalSocioPro = repository.findById(id);
        SocioPro socioPro = new SocioPro();

        optionalSocioPro.ifPresent(value -> socioPro.setCategories(value.getCategories()));
        socioPro.setId(id);

        return socioPro;
    }
    public SocioPro getByCategory(String category){
        return repository.findSocioProByCategoriesIgnoreCase(category);
    }
    public SocioPro saveSocioPro(SocioPro socioPro){
        repository.save(socioPro);
        return this.getById(socioPro.getId());
    }
}
