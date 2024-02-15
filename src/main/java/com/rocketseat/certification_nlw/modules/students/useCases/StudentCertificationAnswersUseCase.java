package com.rocketseat.certification_nlw.modules.students.useCases;
import com.rocketseat.certification_nlw.modules.questions.entities.QuestionEntity;
import com.rocketseat.certification_nlw.modules.questions.repositories.QuestionRepository;
import com.rocketseat.certification_nlw.modules.students.dto.StudentCertificationAnswerDTO;
import com.rocketseat.certification_nlw.modules.students.dto.VerifyHasCertificationDTO;
import com.rocketseat.certification_nlw.modules.students.entities.AnswersCertificationsEntity;
import com.rocketseat.certification_nlw.modules.students.entities.CertificationStudentEntity;
import com.rocketseat.certification_nlw.modules.students.entities.StudentEntity;
import com.rocketseat.certification_nlw.modules.students.repositories.CertificationStudentRepository;
import com.rocketseat.certification_nlw.modules.students.repositories.StudentReposirtory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class StudentCertificationAnswersUseCase {
    @Autowired
    private StudentReposirtory studentReposirtory;
    @Autowired
    private QuestionRepository questionRepository;
    @Autowired
    private CertificationStudentRepository certificationStudentRepository;
    @Autowired
    private VerifyIfHasCertificationUseCase verifyIfHasCertificationUseCase;

    public CertificationStudentEntity execute(StudentCertificationAnswerDTO dto) throws Exception {
       var hasCertification= this.verifyIfHasCertificationUseCase.execute(new VerifyHasCertificationDTO(dto.getEmail(), dto.getTechnology()));
       if (hasCertification){
           throw new Exception("Você já possui certificação");
       }



        //buscar as alternativas das perguntas
            // -Correta ou incorreta
       List<QuestionEntity> questionsEntity= questionRepository.findByTechnology(dto.getTechnology());
       List<AnswersCertificationsEntity> answersCertifications= new ArrayList<>();
       AtomicInteger correctAnswersCount= new AtomicInteger(0);
        //percorrer  todas infos e ver se está correta
        //outra maneira de fazer, como foreach-> for (QuestionEntity questionsEntity : dto.getQuestionsAnswers()) {}

        dto.getQuestionsAnswers().stream().forEach(questionAnswer -> {
                   var question= questionsEntity.stream()
                           .filter(q -> q.getId().equals(questionAnswer.getQuestionID()))
                           .findFirst().get();

                  var findCorrectAlternative = question.getAlternatives().stream()
                          .filter(alternative -> alternative.isCorrect()).findFirst().get();

                  if(findCorrectAlternative.getId().equals(questionAnswer.getAlternativeID())){
                      questionAnswer.setCorrect(true);
                      correctAnswersCount.incrementAndGet();
                  }
                  else {
                      questionAnswer.setCorrect(false);
                  }
            var answersCertificationsEntity =AnswersCertificationsEntity.builder()
                    .answerID(questionAnswer.getAlternativeID())
                    .questionID(questionAnswer.getQuestionID())
                    .isCorrect(questionAnswer.isCorrect()).build();
            answersCertifications.add(answersCertificationsEntity);
                });
        //Verificar se estudante existe pelo email
        var student= studentReposirtory.findByEmail(dto.getEmail());
        UUID studentID;
        if (student.isEmpty()){
            var studentCreated=StudentEntity.builder().email(dto.getEmail()).build();
            studentCreated=studentReposirtory.save(studentCreated);
            studentID=studentCreated.getId();

        }else {
            studentID = student.get().getId();
        }
        CertificationStudentEntity certificationStudentEntity =
                CertificationStudentEntity.builder()
                        .technology(dto.getTechnology())
                        .studentID(studentID)
                        .grade(correctAnswersCount.get())
                        .build();
        var certificationStudentCreated =certificationStudentRepository.save(certificationStudentEntity);
        answersCertifications.stream().forEach(answerCertification ->{
        answerCertification.setCertificationID(certificationStudentEntity.getId());
        answerCertification.setCertificationStudentEntity(certificationStudentEntity);

        });
        certificationStudentEntity.setAnswersCertificationsEntities(answersCertifications);
        certificationStudentRepository.save(certificationStudentEntity);
        return  certificationStudentCreated;

        //Salvar as informações da certificação

    }
}
