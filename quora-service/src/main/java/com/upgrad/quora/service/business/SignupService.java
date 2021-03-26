package com.upgrad.quora.service.business;

import com.upgrad.quora.service.dao.UserDao;
import com.upgrad.quora.service.entity.UserEntity;
import com.upgrad.quora.service.exception.SignUpRestrictedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
public class SignupService {

    @Autowired
    private UserDao userDao;

    @Autowired
    private PasswordCryptographyProvider cryptographyProvider;

    @Transactional
    public UserEntity signup(UserEntity userEntity) throws SignUpRestrictedException{


        System.out.println("test kar rha hun"+userEntity.getUserName());
        UserEntity userByUserName = userDao.getUserByUserName(userEntity.getUserName());
if(userByUserName!=null){

    System.out.println("user mila");
}else{
    System.out.println("user nahi mila");
}

        if(userByUserName != null) {
            throw new SignUpRestrictedException("SGR-001", "Try any other Username, this Username has already been taken");
        }
        UserEntity userByEmail = userDao.getUserByEmail(userEntity.getEmail());
        if(userByEmail != null) {
            throw new SignUpRestrictedException("SGR-002", "This user has already been registered, try with any other emailId");
        }

        String[] encryptText = cryptographyProvider.encrypt(userEntity.getPassword());
        userEntity.setSalt(encryptText[0]);
        userEntity.setPassword(encryptText[1]);

        return userDao.createUser(userEntity);
    }


}
