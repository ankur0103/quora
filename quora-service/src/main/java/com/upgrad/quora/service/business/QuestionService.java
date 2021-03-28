package com.upgrad.quora.service.business;

import com.upgrad.quora.service.dao.QuestionDao;
import com.upgrad.quora.service.entity.QuestionEntity;
import com.upgrad.quora.service.entity.UserAuthTokenEntity;
import com.upgrad.quora.service.exception.AuthorizationFailedException;
import com.upgrad.quora.service.exception.InvalidQuestionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class QuestionService {

    @Autowired
    private QuestionDao questionDao;

    @Autowired
    private UserDetailsService userDetailsService;

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


    public List<QuestionEntity> getAllQuestionsByUuid(final String uuid) {
        return questionDao.getQuestionByUuid(uuid);

    }

}
