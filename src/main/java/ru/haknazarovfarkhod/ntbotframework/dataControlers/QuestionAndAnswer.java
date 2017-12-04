package ru.haknazarovfarkhod.ntbotframework.dataControlers;
import javax.persistence.*;

@Entity
@Table(name="QUESTIONSANDANSWERS")
public class QuestionAndAnswer {
    @Id
    @Column(name="ID")
    @GeneratedValue(strategy = GenerationType.AUTO)
    Long id;
    @Column(name="QUESTION")
    String question;
    @Column(name="ANSWER")
    String answer;

    public void setId(Long id) {
        this.id = id;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public Long getId() {
        return id;
    }

    public String getQuestion() {
        return question;
    }

    public String getAnswer() {
        return answer;
    }

}
