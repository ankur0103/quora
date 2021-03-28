package com.upgrad.quora.api.controller;


import com.upgrad.quora.api.model.*;
import com.upgrad.quora.service.business.QuestionService;
import com.upgrad.quora.service.business.UserDetailsService;
import com.upgrad.quora.service.entity.QuestionEntity;
import com.upgrad.quora.service.entity.UserAuthTokenEntity;
import com.upgrad.quora.service.exception.AuthorizationFailedException;
import com.upgrad.quora.service.exception.InvalidQuestionException;
import com.upgrad.quora.service.exception.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@RestController
@RequestMapping("/")
public class QuestionController {

    @Autowired
    private QuestionService questionService;

    @Autowired
    private UserDetailsService userDetailsService;

    @RequestMapping(method = RequestMethod.POST, path = "/question/create", produces = MediaType.APPLICATION_JSON_UTF8_VALUE, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<QuestionResponse> createQuestion (@RequestBody final QuestionRequest questionRequest,
                                                            @RequestHeader("authorization") final String accessToken) throws AuthorizationFailedException  {

        UserAuthTokenEntity userAuthTokenEntity = userDetailsService.getUserDetailsByAccessToken(accessToken);

        QuestionEntity questionEntity = new QuestionEntity();
        questionEntity.setUuid(userAuthTokenEntity.getUser().getUuid());
        questionEntity.setContent(questionRequest.getContent());
        final ZonedDateTime now = ZonedDateTime.now();
        questionEntity.setDate(now);
        questionEntity.setUser(userAuthTokenEntity.getUser());

        QuestionEntity questionEntity1 = questionService.createQuestion(questionEntity,accessToken);

        QuestionResponse questionResponse = new QuestionResponse();
        questionResponse.setId(questionEntity1.getUuid());
        questionResponse.setStatus("QUESTION CREATED");

        return new ResponseEntity<QuestionResponse>(questionResponse, HttpStatus.CREATED);


    }

    @RequestMapping(method = RequestMethod.GET, path = "/question/all", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<List<QuestionDetailsResponse>> getAllQuestions (@RequestHeader("authorization") final String accessToken) throws AuthorizationFailedException {

        UserAuthTokenEntity userAuthTokenEntity = userDetailsService.getUserDetailsByAccessToken(accessToken);
        List<QuestionEntity> questionEntityList = questionService.getAllQuestion();

        List<QuestionDetailsResponse> questionDetailsResponseList = new ArrayList<>();

        Iterator iterator = questionEntityList.iterator();
        while(iterator.hasNext()) {
            QuestionEntity questionEntity = (QuestionEntity) iterator.next();
            QuestionDetailsResponse questionDetailsResponse = new QuestionDetailsResponse();
            questionDetailsResponse.setContent(questionEntity.getContent());
            questionDetailsResponse.setId(questionEntity.getUuid());
            questionDetailsResponseList.add(questionDetailsResponse);
        }

        return new ResponseEntity<List<QuestionDetailsResponse>>(questionDetailsResponseList, HttpStatus.OK);

    }


    @RequestMapping(method = RequestMethod.GET, path = "question/all/{userId}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<List<QuestionDetailsResponse>> getAllQuestionsByUser (@PathVariable("userId") final String userId,
                                                                                @RequestHeader("authorization") final String accessToken) throws AuthorizationFailedException, UserNotFoundException {
        List<QuestionEntity> questionEntityList = questionService.getAllQuestionsByUuid(userId, accessToken);

        List<QuestionDetailsResponse> questionDetailsResponseList = new ArrayList<>();

        Iterator iterator = questionEntityList.iterator();
        while(iterator.hasNext()) {
            QuestionEntity questionEntity = (QuestionEntity) iterator.next();
            QuestionDetailsResponse questionDetailsResponse = new QuestionDetailsResponse();
            questionDetailsResponse.setContent(questionEntity.getContent());
            questionDetailsResponse.setId(questionEntity.getUuid());
            questionDetailsResponseList.add(questionDetailsResponse);
        }

        return new ResponseEntity<List<QuestionDetailsResponse>>(questionDetailsResponseList, HttpStatus.OK);

    }


    @RequestMapping(method = RequestMethod.PUT, path = "/question/edit/{questionId}", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<QuestionEditResponse> editQuestion (@RequestBody final QuestionEditRequest questionEditRequest,
                                                              @RequestHeader("authorization") final String accessToken,
                                                              @PathVariable("questionId") final String questionId) throws AuthorizationFailedException, InvalidQuestionException {
        QuestionEntity questionEntity = questionService.editQuestion(questionId, accessToken);

        QuestionEditResponse questionEditResponse = new QuestionEditResponse();
        questionEditResponse.setId(questionEntity.getUuid());
        questionEditResponse.setStatus("QUESTION EDITED");

        return new ResponseEntity<QuestionEditResponse>(questionEditResponse, HttpStatus.OK);

    }

}
