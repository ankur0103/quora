package com.upgrad.quora.service.business;

import com.upgrad.quora.service.dao.UserDao;
import com.upgrad.quora.service.entity.UserAuthTokenEntity;
import com.upgrad.quora.service.entity.UserEntity;
import com.upgrad.quora.service.exception.AuthorizationFailedException;
import com.upgrad.quora.service.exception.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserDetailsService {

    @Autowired
    private UserDao userDao;


    @Transactional
    public UserEntity getUserDetails(final String userId, final String accessToken) throws AuthorizationFailedException, UserNotFoundException {



        UserEntity userEntity = userDao.getUserByUuid(userId);
        if (userEntity == null) {
            throw new UserNotFoundException("USR-001", "User with entered uuid does not exist");
        }

        return userEntity;
    }


    public UserAuthTokenEntity getUserDetailsByAccessToken(final String accessToken) {


        return userDao.getUserAuthToken(accessToken);

    UserAuthTokenEntity userAuthTokenEntity = userDao.getUserAuthToken(accessToken);

        if (userAuthTokenEntity == null) {
                throw new AuthorizationFailedException("ATHR-001", "User has not signed in");
                }

final ZonedDateTime tokenExpireTime = userAuthTokenEntity.getExpiresAt();
final ZonedDateTime now = ZonedDateTime.now();

        if (userAuthTokenEntity.getLogoutAt() != null || tokenExpireTime.compareTo(now) < 0) {

        throw new AuthorizationFailedException("ATHR-002", "User is signed out.Sign in first to get user details");
        }UserAuthTokenEntity userAuthTokenEntity = userDao.getUserAuthToken(accessToken);

        if (userAuthTokenEntity == null) {
        throw new AuthorizationFailedException("ATHR-001", "User has not signed in");
        }

final ZonedDateTime tokenExpireTime = userAuthTokenEntity.getExpiresAt();
final ZonedDateTime now = ZonedDateTime.now();

        if (userAuthTokenEntity.getLogoutAt() != null || tokenExpireTime.compareTo(now) < 0) {

        throw new AuthorizationFailedException("ATHR-002", "User is signed out.Sign in first to get user details");
        }