package com.upgrad.quora.service.entity;


import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.ZonedDateTime;

@Entity
@Table(name = "answer", schema = "quora")
@NamedQueries({
        @NamedQuery(name = "answerByAnswerId", query = "select a from AnswerEntity a where a.id = :answerId"),
        @NamedQuery(name = "answerByQuestionId", query = "select a from AnswerEntity a where a.question = :questionId")



})
public class AnswerEntity {

//  CREATE TABLE IF NOT EXISTS ANSWER(id SERIAL,uuid VARCHAR(200) NOT NULL, ans VARCHAR(255) NOT NULL,date TIMESTAMP NOT NULL , user_id INTEGER NOT NULL, question_id INTEGER NOT NULL , PRIMARY KEY(id), FOREIGN KEY (user_id) REFERENCES USERS(id) ON DELETE CASCADE, FOREIGN KEY (question_id) REFERENCES QUESTION(id) ON DELETE CASCADE);

    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "UUID")
    @NotNull
    @Size(max = 200)
    private String uuid;

    @Column(name = "ANS")
    @NotNull
    @Size(max = 255)
    private String ans;


    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "USER_ID")
    @NotNull
    private UserEntity user;


    @Column(name = "DATE")
    @NotNull
    private ZonedDateTime date;


    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "QUESTION_ID")
    @NotNull
    private QuestionEntity question;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getAns() {
        return ans;
    }

    public void setAns(String ans) {
        this.ans = ans;
    }

    public UserEntity getUser() {
        return user;
    }

    public void setUser(UserEntity user) {
        this.user = user;
    }

    public ZonedDateTime getDate() {
        return date;
    }

    public void setDate(ZonedDateTime date) {
        this.date = date;
    }

    public QuestionEntity getQuestion() {
        return question;
    }

    public void setQuestion(QuestionEntity question) {
        this.question = question;
    }
}
