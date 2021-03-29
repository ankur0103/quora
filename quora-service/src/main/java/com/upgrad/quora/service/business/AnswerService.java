package com.upgrad.quora.service.business;


import com.upgrad.quora.service.dao.AnswerDao;
import com.upgrad.quora.service.dao.QuestionDao;
import com.upgrad.quora.service.entity.AnswerEntity;
import com.upgrad.quora.service.entity.QuestionEntity;
import com.upgrad.quora.service.entity.UserAuthTokenEntity;
import com.upgrad.quora.service.exception.AuthorizationFailedException;
import com.upgrad.quora.service.exception.InvalidQuestionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class AnswerService {

    @Autowired
    private AnswerDao answerDao;

    @Autowired
    private QuestionDao questionDao;



    @Autowired
    private UserDetailsService userDetailsService;

    @Transactional(propagation = Propagation.REQUIRED)
    public AnswerEntity createAnswer(AnswerEntity answerEntity, final String accessToken) throws AuthorizationFailedException {
        return answerDao.createAnswer(answerEntity);

    }

    @Transactional(propagation = Propagation.REQUIRED)
    public AnswerEntity editAnswer(AnswerEntity answerEntity, final String accessToken) throws AuthorizationFailedException {
        UserAuthTokenEntity userAuthTokenEntity = userDetailsService.getUserDetailsByAccessToken(accessToken);
        if (!(answerEntity.getUuid()).equals(userAuthTokenEntity.getUser().getUuid())) {
            throw new AuthorizationFailedException("ATHR-003", "Only the answer owner can edit the answer");
        }
        return answerDao.createAnswer(answerEntity);

    }

    @Transactional(propagation = Propagation.REQUIRED)
    public AnswerEntity deleteAnswer(AnswerEntity answerEntity, final String accessToken) throws AuthorizationFailedException {
        UserAuthTokenEntity userAuthTokenEntity = userDetailsService.getUserDetailsByAccessToken(accessToken);
        if ((answerEntity.getUser().getRole()).equals("nonadmin") && !(answerEntity.getUuid()).equals(userAuthTokenEntity.getUser().getUuid())) {
            throw new AuthorizationFailedException("ATHR-003", "Only the answer owner or admin can delete the answer");
        }
        return answerDao.deleteAnswer(answerEntity);

    }

    @Transactional(propagation = Propagation.REQUIRED)
    public List<AnswerEntity> getAllAnswerByQuestionId(final String questionId, final String accessToken) throws AuthorizationFailedException, InvalidQuestionException {
        userDetailsService.getUserDetailsByAccessToken(accessToken);
        QuestionEntity questionEntity = questionDao.getQuestionByQuestionId(questionId);
        if (questionEntity == null) {
            throw new InvalidQuestionException("QUES-001", "The question with entered uuid whose details are to be seen does not exist");
        }
        return answerDao.getAllAnswersByQuestionId(questionId);

    }


    @Transactional(propagation = Propagation.REQUIRED)
    public AnswerEntity getAnswerByAnswerId(final String answerId) throws InvalidQuestionException {

        AnswerEntity answerEntity = answerDao.getAnswerByAnswerId(answerId);
        if(answerEntity == null) {
            throw new InvalidQuestionException("ANS-001", "Entered answer uuid does not exist");
        }
        return answerEntity;

    }
}