package com.prog4.employee_db.service;

import com.prog4.employee_db.controller.model.ModelEmployee;
import com.prog4.employee_db.repository.RepositoryImplementation;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class MapDTOService {
    private final RepositoryImplementation repositoryImplementation;

    public ModelEmployee getByEndToEndId(Long id){
        return repositoryImplementation.getEmployeeDtoByEndToEndId(id);
    }
}
