package com.upgrad.quora.api.controller;

import com.upgrad.quora.api.model.*;
import com.upgrad.quora.service.business.AnswerService;
import com.upgrad.quora.service.business.QuestionService;
import com.upgrad.quora.service.business.UserDetailsService;
import com.upgrad.quora.service.entity.AnswerEntity;
import com.upgrad.quora.service.entity.UserAuthTokenEntity;
import com.upgrad.quora.service.exception.AuthorizationFailedException;
import com.upgrad.quora.service.exception.InvalidQuestionException;
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
public class AnswerController {

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private AnswerService answerService;

    @Autowired
    private QuestionService questionService;

    @RequestMapping(method = RequestMethod.POST, path = "/question/{questionId}/answer/create", produces = MediaType.APPLICATION_JSON_UTF8_VALUE, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<AnswerResponse> createAnswer (@RequestBody final AnswerRequest answerRequest,
                                                        @RequestHeader("authorization") final String accessToken,
                                                        @PathVariable("questionId") final String questionId) throws AuthorizationFailedException, InvalidQuestionException {

        UserAuthTokenEntity userAuthTokenEntity = userDetailsService.getUserDetailsByAccessToken(accessToken);

        AnswerEntity answerEntity = new AnswerEntity();
        answerEntity.setAns(answerRequest.getAnswer());
        answerEntity.setUuid(userAuthTokenEntity.getUser().getUuid());
        answerEntity.setUser(userAuthTokenEntity.getUser());
        final ZonedDateTime now = ZonedDateTime.now();
        answerEntity.setDate(now);
        answerEntity.setQuestion(questionService.getQuestionByQuestionId(questionId));

        AnswerEntity createdAnswerEntity = answerService.createAnswer(answerEntity, accessToken);

        AnswerResponse answerResponse = new AnswerResponse();
        answerResponse.setId(createdAnswerEntity.getUuid());
        answerResponse.setStatus("ANSWER CREATED");

        return new ResponseEntity<AnswerResponse>(answerResponse, HttpStatus.CREATED);


    }

    @RequestMapping(method = RequestMethod.PUT, path = "/answer/edit/{answerId}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<AnswerEditResponse> editAnswer (@RequestBody final AnswerEditRequest answerEditRequest,
                                                                     @RequestHeader("authorization") final String accessToken,
                                                                     @PathVariable("answerId") final String answerId) throws AuthorizationFailedException, InvalidQuestionException {

        AnswerEntity answerEntity = answerService.getAnswerByAnswerId(answerId);
        answerEntity.setAns(answerEditRequest.getContent());
        AnswerEntity editedAnswerEntity = answerService.editAnswer(answerEntity, accessToken);

        AnswerEditResponse answerEditResponse = new AnswerEditResponse();
        answerEditResponse.setId(editedAnswerEntity.getUuid());
        answerEditResponse.setStatus("ANSWER EDITED");

        return new ResponseEntity<>(answerEditResponse, HttpStatus.OK);
    }


    @RequestMapping(method = RequestMethod.DELETE, path = "/answer/delete/{answerId}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<AnswerDeleteResponse> deleteAnswer (@RequestHeader("authorization") final String accessToken,
                                                            @PathVariable("answerId") final String answerId) throws AuthorizationFailedException, InvalidQuestionException {

        AnswerEntity answerEntity = answerService.getAnswerByAnswerId(answerId);
        AnswerEntity deletedAnswerEntity = answerService.deleteAnswer(answerEntity, accessToken);

        AnswerDeleteResponse answerDeleteResponse = new AnswerDeleteResponse();
        answerDeleteResponse.setId(deletedAnswerEntity.getUuid());
        answerDeleteResponse.setStatus("ANSWER DELETED");

        return new ResponseEntity<>(answerDeleteResponse, HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.GET, path = "/answer/all/{questionId}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<List<AnswerDetailsResponse>> getAllAnswersByQuestionId (@RequestHeader("authorization") final String accessToken, @PathVariable("questionId") final String questionId) throws AuthorizationFailedException, InvalidQuestionException {

        List<AnswerEntity> answerEntityList = answerService.getAllAnswerByQuestionId(questionId, accessToken);

        List<AnswerDetailsResponse> answerDetailsResponseArrayList = new ArrayList<>();

        Iterator iterator = answerEntityList.iterator();
        while(iterator.hasNext()) {
            AnswerEntity answerEntity = (AnswerEntity) iterator.next();
            AnswerDetailsResponse answerDetailsResponse = new AnswerDetailsResponse();
            answerDetailsResponse.setAnswerContent(answerEntity.getAns());
            answerDetailsResponse.setQuestionContent(answerEntity.getQuestion().getContent());
            answerDetailsResponse.setId(answerEntity.getUuid());
        }

        return new ResponseEntity<List<AnswerDetailsResponse>>(answerDetailsResponseArrayList, HttpStatus.OK);

    }

}
