package com.upgrad.quora.service.dao;


import org.springframework.stereotype.Repository;
import com.upgrad.quora.service.entity.AnswerEntity;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
public class AnswerDao {

    @PersistenceContext
    private EntityManager entityManager;

    public AnswerEntity createAnswer(AnswerEntity answerEntity) {
        entityManager.persist(answerEntity);
        return answerEntity;
    }


    public AnswerEntity deleteAnswer(AnswerEntity answerEntity) {
        entityManager.remove(answerEntity);
        return answerEntity;
    }

    public AnswerEntity editAnswer(AnswerEntity answerEntity) {
        entityManager.merge(answerEntity);
        return answerEntity;
    }

    public AnswerEntity getAnswerByAnswerId(final String answerId) {

        try {
            return entityManager.createNamedQuery("answerByAnswerId", AnswerEntity.class).setParameter("answerId", answerId).getSingleResult();
        } catch (NoResultException nre) {
            return null;
        }
    }

    public List<AnswerEntity> getAllAnswersByQuestionId(final String questionId) {

        try {
            return entityManager.createNamedQuery("answerByQuestionId", AnswerEntity.class).setParameter("questionId", questionId).getResultList();
        } catch (NoResultException nre) {
            return null;
        }
    }
}
