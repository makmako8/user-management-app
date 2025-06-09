package com.example.usermanagementapp.controller.api;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.usermanagementapp.dto.QuizResponseDTO;
import com.example.usermanagementapp.dto.ReviewRequestDTO;

@RestController
@RequestMapping("/api/quiz")
public class ReviewApiController {

    @PostMapping("/review")
    public ResponseEntity<QuizResponseDTO> getReviewQuestion(@RequestBody ReviewRequestDTO request) {

        String type = request.getReviewType();
        Long qid = request.getQuestionId();

        QuizResponseDTO quiz = new QuizResponseDTO();

        if ("same".equals(type)) {
            quiz.setId(qid);
            quiz.setQuestion("Javaでクラスを定義するキーワードはどれか？");
            quiz.setChoices(List.of("define", "struct", "class", "function"));
            quiz.setCorrectIndex(2);

        } else if ("rephrased".equals(type)) {
            quiz.setId(qid);
            quiz.setQuestion("Javaでオブジェクト指向の基本単位を表すのに使うキーワードは？");
            quiz.setChoices(List.of("object", "structure", "class", "package"));
            quiz.setCorrectIndex(2);

        } else if ("applied".equals(type)) {
            quiz.setId(qid);
            quiz.setQuestion("次のうち、正しくクラスを定義しているのはどれか？");
            quiz.setChoices(List.of(
                "public void MyClass() {}",
                "class MyClass {}",
                "define MyClass()",
                "struct MyClass {}"
            ));
            quiz.setCorrectIndex(1);
        } else {
            return ResponseEntity.badRequest().build();
        }

        return ResponseEntity.ok(quiz);
    }
}
