package com.rocketseat.certification_nlw.modules.students.controllers;

import com.rocketseat.certification_nlw.modules.students.dto.StudentCertificationAnswerDTO;
import com.rocketseat.certification_nlw.modules.students.dto.VerifyHasCertificationDTO;
import com.rocketseat.certification_nlw.modules.students.entities.CertificationStudentEntity;
import com.rocketseat.certification_nlw.modules.students.useCases.StudentCertificationAnswersUseCase;
import com.rocketseat.certification_nlw.modules.students.useCases.VerifyIfHasCertificationUseCase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/students")
public class StudentController {
    //Preciso usar o meu USECASE
    @Autowired
    private VerifyIfHasCertificationUseCase verifyIfHasCertificationUseCase;
    @Autowired
    private StudentCertificationAnswersUseCase studentCertificationAnswersUseCase;
   @PostMapping("/verifyIfHasCertification")
    public String verifyIfHasCertification(@RequestBody VerifyHasCertificationDTO verifyHasCertificationDTO){
       //email
       //technology
       var result= this.verifyIfHasCertificationUseCase.execute(verifyHasCertificationDTO);
       if (result){
           return "Usuario ja fez a prova";
       }
       return "Usuario pode fazer a prova";

    }
    @PostMapping("/certification/answer")
    public ResponseEntity<Object> certificationAnswer(
    @RequestBody StudentCertificationAnswerDTO studentCertificationAnswerDTO) {
        {
            try {
               var result = studentCertificationAnswersUseCase.execute(studentCertificationAnswerDTO);
                return ResponseEntity.ok().body(result);
            } catch (Exception e) {
                return ResponseEntity.badRequest().body(e.getMessage());
            }
            }

        }
}
