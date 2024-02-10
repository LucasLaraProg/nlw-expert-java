package com.rocketseat.certification_nlw.modules.students.useCases;

import com.rocketseat.certification_nlw.modules.students.dto.VerifyHasCertificationDTO;
import com.rocketseat.certification_nlw.modules.students.repositories.CertificationStudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service//camada de serviço, regras de negocio usecases

public class VerifyIfHasCertificationUseCase {
    @Autowired
    private CertificationStudentRepository certificationStudentRepository;

    public boolean execute(VerifyHasCertificationDTO dto){
      var result=  this.certificationStudentRepository.findByStudentEmailAndTechnology(dto.getEmail(),dto.getTechnology());
        if (!result.isEmpty()){
            return true;
        }
        return false;
    }

}
