package com.upgrad.quora.service.business;

import com.upgrad.quora.service.dao.QuestionDao;
import com.upgrad.quora.service.entity.QuestionEntity;
import com.upgrad.quora.service.exception.AuthorizationFailedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class QuestionService {

    @Autowired
    private QuestionDao questionDao;

    public QuestionEntity createQuestion(QuestionEntity questionEntity, final String accessToken) throws  AuthorizationFailedException {
        return questionDao.createQuestion(questionEntity);

    }

    public List<QuestionEntity> getAllQuestionsByUuid(final String uuid) {
        return questionDao.getQuestionByUuid(uuid);

    }

}
