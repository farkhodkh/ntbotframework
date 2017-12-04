package ru.haknazarovfarkhod.ntbotframework.dataControlers.repository;

import ru.haknazarovfarkhod.ntbotframework.dataControlers.QuestionAndAnswer;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface QuestionAndAnswerRepo extends CrudRepository<QuestionAndAnswer, Long> {
    QuestionAndAnswer findByNumber(String number);
}
