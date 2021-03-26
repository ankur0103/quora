package com.upgrad.quora.service.business;


import com.upgrad.quora.service.dao.UserDao;
import com.upgrad.quora.service.entity.UserAuthTokenEntity;
import com.upgrad.quora.service.entity.UserEntity;
import com.upgrad.quora.service.exception.AuthorizationFailedException;
import com.upgrad.quora.service.exception.UserNotFoundException;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;

@Service
public class AdminService {

    private UserDao userDao;

    public UserEntity deleteUser(final String userId, final String accessToken) throws AuthorizationFailedException, UserNotFoundException {


        UserAuthTokenEntity userAuthTokenEntity = userDao.getUserAuthToken(accessToken);

        if (userAuthTokenEntity == null) {
            throw new AuthorizationFailedException("ATHR-001", "User has not signed in");
        }

        UserEntity userEntity = userDao.getUserByUuid(userAuthTokenEntity.getUser().getUuid());


        final ZonedDateTime tokenExpireTime = userAuthTokenEntity.getExpiresAt();
        final ZonedDateTime now = ZonedDateTime.now();

        if(userAuthTokenEntity.getLogoutAt() != null || tokenExpireTime.compareTo(now) < 0) {

            throw new AuthorizationFailedException("ATHR-002", "User is signed out");
        }

        if(userEntity.getRole().equals("nonadmin")) {
            throw new AuthorizationFailedException("ATHR-003", "Unauthorized Access, Entered user is not an admin");
        }


        UserEntity deletedUserEntity = userDao.getUserByUuid(userId);
        if(deletedUserEntity == null) {
            throw new AuthorizationFailedException("USR-001", "User with entered uuid to be deleted does not exist");

        }

        userDao.deleteUser(deletedUserEntity);

        return deletedUserEntity;

    }

}
