package com.upgrad.quora.service.business;

import com.upgrad.quora.service.dao.QuestionDao;
import com.upgrad.quora.service.dao.UserDao;
import com.upgrad.quora.service.entity.QuestionEntity;
import com.upgrad.quora.service.entity.UserAuthTokenEntity;
import com.upgrad.quora.service.entity.UserEntity;
import com.upgrad.quora.service.exception.AuthorizationFailedException;
import com.upgrad.quora.service.exception.InvalidQuestionException;
import com.upgrad.quora.service.exception.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class QuestionService {

    @Autowired
    private QuestionDao questionDao;

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private UserDao userDao;

    public QuestionEntity createQuestion(QuestionEntity questionEntity, final String accessToken) throws  AuthorizationFailedException {
        return questionDao.createQuestion(questionEntity);

    }

    public List<QuestionEntity> getAllQuestion() {
        return questionDao.getAllQuestion();

    }

    public QuestionEntity editQuestion(final String questionId, final String accessToken) throws AuthorizationFailedException, InvalidQuestionException{

        UserAuthTokenEntity userAuthTokenEntity = userDetailsService.getUserDetailsByAccessToken(accessToken);
        QuestionEntity questionEntity = questionDao.getQuestionByQuestionId(questionId);

        if(questionEntity == null) {
            throw new InvalidQuestionException("QUES=001","Entered question uuid does not exist");
        }

        if(!(userAuthTokenEntity.getUser().getUuid()).equals(questionEntity.getUuid())) {
            throw new AuthorizationFailedException("ATHR-003","Only the question owner can edit the question");
        }

        return questionDao.editQuestion(questionEntity);

    }

    public QuestionEntity deleteQuestion(final String questionId, final String accessToken) throws AuthorizationFailedException, InvalidQuestionException{

        UserAuthTokenEntity userAuthTokenEntity = userDetailsService.getUserDetailsByAccessToken(accessToken);
        QuestionEntity questionEntity = questionDao.getQuestionByQuestionId(questionId);

        if(questionEntity == null) {
            throw new InvalidQuestionException("QUES=001","Entered question uuid does not exist");
        }

        if ((userAuthTokenEntity.getUser().getRole()).equals("nonadmin") && !(userAuthTokenEntity.getUser().getUuid()).equals(questionEntity.getUuid())) {
            throw new AuthorizationFailedException("ATHR-003","Only the question owner or admin can delete the question'");
        }

        return questionDao.deleteQuestion(questionEntity);

    }


    public List<QuestionEntity> getAllQuestionsByUuid(final String uuid, final String accessToken) throws AuthorizationFailedException, UserNotFoundException {
        userDetailsService.getUserDetailsByAccessToken(accessToken);
        UserEntity userEntity = userDao.getUserByUuid(uuid);
        if (userEntity == null) {
            throw new UserNotFoundException("USR-001", "User with entered uuid whose question details are to be seen does not exist");
        }
        return questionDao.getQuestionByUuid(uuid);

    }

    public QuestionEntity getQuestionByQuestionId(final String questionId) throws InvalidQuestionException{

        QuestionEntity questionEntity = questionDao.getQuestionByQuestionId(questionId);
        if(questionEntity == null) {
            throw new InvalidQuestionException("QUES-001", "The question entered is invalid");
        }
        return questionEntity;

    }

}
