package com.upgrad.quora.service.business;

import com.upgrad.quora.service.dao.UserDao;
import com.upgrad.quora.service.entity.UserAuthTokenEntity;
import com.upgrad.quora.service.exception.SignOutRestrictedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;

@Service
public class SignoutSerivice {

    @Autowired
    private UserDao userDao;

    public UserAuthTokenEntity signout(final String accessToken) throws SignOutRestrictedException {

        UserAuthTokenEntity userAuthTokenEntity = userDao.getUserAuthToken(accessToken);

        if(userAuthTokenEntity == null) {
            throw new SignOutRestrictedException("SGR-001","User is not Signed in");
        }

        final ZonedDateTime tokenExpireTime = userAuthTokenEntity.getExpiresAt();
        final ZonedDateTime now = ZonedDateTime.now();

        if(tokenExpireTime.compareTo(now) > 0) {
            userAuthTokenEntity.setLogoutAt(now);
            userDao.updateUserAuthToken(userAuthTokenEntity);

        }

        return userAuthTokenEntity;


    }

}
