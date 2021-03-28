package com.upgrad.quora.service.dao;

import com.upgrad.quora.service.entity.QuestionEntity;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
public class QuestionDao {

    @PersistenceContext
    private EntityManager entityManager;

    public QuestionEntity createQuestion(QuestionEntity questionEntity) {
        entityManager.persist(questionEntity);
        return questionEntity;
    }

    public QuestionEntity deleteQuestion(QuestionEntity questionEntity) {
        entityManager.remove(questionEntity);
        return questionEntity;
    }

    public QuestionEntity editQuestion(QuestionEntity questionEntity) {
        entityManager.merge(questionEntity);
        return questionEntity;
    }

    public List<QuestionEntity> getAllQuestion() {

        try {
            return entityManager.createNamedQuery("allQuestion", QuestionEntity.class).getResultList();
        } catch (NoResultException nre) {
            return null;
        }
    }


    public List<QuestionEntity> getQuestionByUuid(final String uuid) {

        try {
            return entityManager.createNamedQuery("questionByUuid", QuestionEntity.class).setParameter("uuid", uuid).getResultList();
        } catch (NoResultException nre) {
            return null;
        }
    }


    public QuestionEntity getQuestionByQuestionId(final String questionId) {

        try {
            return entityManager.createNamedQuery("questionByQuestionId", QuestionEntity.class).setParameter("id", questionId).getSingleResult();
        } catch (NoResultException nre) {
            return null;
        }
    }
}
