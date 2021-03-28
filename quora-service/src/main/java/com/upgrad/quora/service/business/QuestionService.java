package com.upgrad.quora.service.business;

import com.upgrad.quora.service.dao.QuestionDao;
import com.upgrad.quora.service.entity.QuestionEntity;
import com.upgrad.quora.service.entity.UserAuthTokenEntity;
import com.upgrad.quora.service.exception.AuthorizationFailedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;
import java.util.List;

@Service
public class QuestionService {

    @Autowired
    private QuestionDao questionDao;

    public QuestionEntity createQuestion(QuestionEntity questionEntity, final String accessToken) throws  AuthorizationFailedException {




        return questionDao.createQuestion(questionEntity);

    }

    public List<QuestionEntity> getAllQuestionsByUuid(final String uuid) {
        UserAuthTokenEntity userAuthTokenEntity = questionDao.getUserAuthToken(accessToken);

        if (userAuthTokenEntity == null) {
            throw new AuthorizationFailedException("ATHR-001", "User has not signed in");
        }

        final ZonedDateTime tokenExpireTime = userAuthTokenEntity.getExpiresAt();
        final ZonedDateTime now = ZonedDateTime.now();

        if(userAuthTokenEntity.getLogoutAt() != null || tokenExpireTime.compareTo(now) < 0) {

            throw new AuthorizationFailedException("ATHR-002", "User is signed out.Sign in first to get user details");
        }
        return questionDao.getQuestionByUuid(uuid);

    }

}
