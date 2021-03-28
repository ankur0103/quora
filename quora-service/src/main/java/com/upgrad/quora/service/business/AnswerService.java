package com.upgrad.quora.service.business;


import com.upgrad.quora.service.dao.AnswerDao;
import com.upgrad.quora.service.entity.AnswerEntity;
import com.upgrad.quora.service.exception.AuthorizationFailedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AnswerService {

    @Autowired
    private AnswerDao answerDao;

    public AnswerEntity createAnswer(AnswerEntity answerEntity, final String accessToken) throws AuthorizationFailedException {
        return answerDao.createAnswer(answerEntity);

    }

}