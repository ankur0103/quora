package com.upgrad.quora.service.entity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.time.ZonedDateTime;


@Entity
@Table(name = "question", schema = "quora")
@NamedQueries({
        @NamedQuery(name = "questionByUuId", query = "select q from QuestionEntity q where q.uuid = :uuid "),
        @NamedQuery(name = "allQuestion", query = "select q from QuestionEntity q"),
        @NamedQuery(name = "questionByQuestionId", query = "select q from QuestionEntity q where q.id = :id ")
})
public class QuestionEntity implements Serializable {

    // CREATE TABLE IF NOT EXISTS QUESTION(id SERIAL,uuid VARCHAR(200) NOT NULL, content VARCHAR(500) NOT NULL, date TIMESTAMP NOT NULL , user_id INTEGER NOT NULL, PRIMARY KEY(id), FOREIGN KEY (user_id) REFERENCES USERS(id) ON DELETE CASCADE);


    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "UUID")
    @NotNull
    @Size(max = 200)
    private String uuid;

    @Column(name = "CONTENT")
    @NotNull
    @Size(max = 500)
    private String content;


    @ManyToOne
    @JoinColumn(name = "USER_ID")
    @NotNull
    private UserEntity user;


    @Column(name = "DATE")
    @NotNull
    private ZonedDateTime date;

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

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
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
}
