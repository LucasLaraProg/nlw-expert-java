package com.rocketseat.certification_nlw.modules.questions.controllers;

import com.rocketseat.certification_nlw.modules.questions.dto.AlternativesResultDTO;
import com.rocketseat.certification_nlw.modules.questions.dto.QuestionResultDTO;
import com.rocketseat.certification_nlw.modules.questions.entities.AlternativesEntity;
import com.rocketseat.certification_nlw.modules.questions.entities.QuestionEntity;
import com.rocketseat.certification_nlw.modules.questions.repositories.QuestionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/questions")
public class QuestionController {
    @Autowired
    private QuestionRepository questionRepository;

    @GetMapping("/technology/{technology}")
    public List<QuestionResultDTO> findByTechnology(@PathVariable String technology){
        System.out.println("TECHNOLOGY ==="+ technology);
        var result = this.questionRepository.findByTechnology(technology);
        var toMap = result.stream().map(question -> mapQuestionToDTO(question))
                .collect(Collectors.toList());
        return toMap;

    }
   static QuestionResultDTO mapQuestionToDTO(QuestionEntity question){
      var questionResultDTO= QuestionResultDTO.builder()
               .description(question.getDescription()).build();
      List<AlternativesResultDTO>alternativeResultDTOS=
              question.getAlternatives()
                      .stream().map(alternative -> mapAlternativeResultDTO(alternative))
                      .collect(Collectors.toList());
      questionResultDTO.setAlternatives(alternativeResultDTOS);
      return questionResultDTO;

   }
   static AlternativesResultDTO mapAlternativeResultDTO(AlternativesEntity alternativesResultDTO){
        return AlternativesResultDTO.builder().description(alternativesResultDTO.getDescription()).build();

   }
}
