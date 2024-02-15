package com.rocketseat.certification_nlw.modules.certifications.controllers;

import com.rocketseat.certification_nlw.modules.certifications.useCases.Top10RakingUseCase;
import com.rocketseat.certification_nlw.modules.students.entities.CertificationStudentEntity;
import com.rocketseat.certification_nlw.modules.students.entities.StudentEntity;
import com.rocketseat.certification_nlw.modules.students.repositories.CertificationStudentRepository;
import com.rocketseat.certification_nlw.modules.students.repositories.StudentReposirtory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/ranking")
public class RankingController {
    @Autowired
    private Top10RakingUseCase top10RakingUseCase;
    @RequestMapping("/top10")
    public List<CertificationStudentEntity> top10(){
        return this.top10RakingUseCase.execute();

    }
}
