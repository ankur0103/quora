package com.upgrad.quora.api.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.upgrad.quora.api.QuoraApiApplication;
import com.upgrad.quora.api.model.QuestionEditRequest;
import com.upgrad.quora.api.model.QuestionRequest;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment =
        SpringBootTest.WebEnvironment.MOCK,
        classes = QuoraApiApplication.class)
@AutoConfigureMockMvc

public class QuestionControllerTest {

    @Autowired
    private MockMvc mvc;



    //This test case passes when you try to create the question but the JWT token entered does not exist in the database.
    @Test
    public void createQuestionWithNonExistingAccessToken() throws Exception {
        QuestionRequest r = new QuestionRequest();
        r.setContent("test question");


        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
        String requestJson=ow.writeValueAsString(r );

        mvc.perform(MockMvcRequestBuilders.post("/question/create").content(requestJson).contentType(MediaType.APPLICATION_JSON_UTF8_VALUE).header("authorization", "non_existing_access_token"))
                .andExpect(status().isForbidden())
                .andExpect(MockMvcResultMatchers.jsonPath("code").value("ATHR-001"));
    }

    //This test case passes when you try to create the question but the user corresponding to the JWT token entered is signed out of the application.
    @Test
    public void createQuestionWithSignedOutUser() throws Exception {

        QuestionRequest r = new QuestionRequest();
        r.setContent("test question");


        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
        String requestJson=ow.writeValueAsString(r );

        mvc.perform(MockMvcRequestBuilders.post("/question/create").content(requestJson).contentType(MediaType.APPLICATION_JSON_UTF8_VALUE).header("authorization", "database_accesstoken3"))
                .andExpect(status().isForbidden())
                .andExpect(MockMvcResultMatchers.jsonPath("code").value("ATHR-002"));
    }

    //This test case passes when you try to get the detail of all the questions and the JWT token entered exists in the database and the user corresponding to that JWT token is signed in.
    @Test
    public void getAllQuestions() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get("/question/all").header("authorization", "database_accesstoken1"))
                .andExpect(status().isOk());
    }

    //This test case passes when you try to get the detail of all the questions but the JWT token entered does not exist in the database.
    @Test
    public void getAllQuestionsWithNonExistingAccessToken() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get("/question/all").header("authorization", "non_existing_access_token"))
                .andExpect(status().isForbidden())
                .andExpect(MockMvcResultMatchers.jsonPath("code").value("ATHR-001"));
    }

    //This test case passes when you try to get the detail of all the questions and the JWT token entered exists in the database but the user corresponding to that JWT token is signed out.
    @Test
    public void getAllQuestionsWithSignedOutUser() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get("/question/all").header("authorization", "database_accesstoken3"))
                .andExpect(status().isForbidden())
                .andExpect(MockMvcResultMatchers.jsonPath("code").value("ATHR-002"));
    }

    //This test case passes when you try to edit the question but the JWT token entered does not exist in the database.
    @Test
    public void editQuestionWithNonExistingAccessToken() throws Exception {

        QuestionRequest r = new QuestionRequest();
        r.setContent("test question");


        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
        String requestJson=ow.writeValueAsString(r );

        mvc.perform(MockMvcRequestBuilders.put("/question/edit/database_question_uuid").content(requestJson).contentType(MediaType.APPLICATION_JSON_UTF8_VALUE).header("authorization", "non_existing_access_token"))
                .andExpect(status().isForbidden())
                .andExpect(MockMvcResultMatchers.jsonPath("code").value("ATHR-001"));
    }

    //This test case passes when you try to edit the question and the JWT token entered exists in the database but the user corresponding to that JWT token is signed out.
    @Test
    public void editQuestionWithWithSignedOutUser() throws Exception {

        QuestionRequest r = new QuestionRequest();
        r.setContent("test question");


        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
        String requestJson=ow.writeValueAsString(r );
        mvc.perform(MockMvcRequestBuilders.put("/question/edit/database_question_uuid").content(requestJson).contentType(MediaType.APPLICATION_JSON_UTF8_VALUE).header("authorization", "database_accesstoken3"))
                .andExpect(status().isForbidden())
                .andExpect(MockMvcResultMatchers.jsonPath("code").value("ATHR-002"));
    }

    //This test case passes when you try to edit the question and the JWT token entered exists in the database and the user corresponding to that JWT token is signed in but the corresponding user is not the owner of the question.
    @Test
    public void editQuestionWithoutOwnership() throws Exception {

        QuestionRequest r = new QuestionRequest();
        r.setContent("test question");


        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
        String requestJson=ow.writeValueAsString(r );
        mvc.perform(MockMvcRequestBuilders.put("/question/edit/database_question_uuid").content(requestJson).contentType(MediaType.APPLICATION_JSON_UTF8_VALUE).header("authorization", "database_accesstoken"))
                .andExpect(status().isForbidden())
                .andExpect(MockMvcResultMatchers.jsonPath("code").value("ATHR-003"));
    }

    //This test case passes when you try to edit the question which does not exist in the database.
    @Test
    public void editNonExistingQuestion() throws Exception {

        QuestionEditRequest r = new QuestionEditRequest();
        r.setContent("test question");


        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
        String requestJson=ow.writeValueAsString(r );

        mvc.perform(MockMvcRequestBuilders.put("/question/edit/non_exisitng_question_uuid").content(requestJson).contentType(MediaType.APPLICATION_JSON_UTF8_VALUE).header("authorization", "database_accesstoken1"))
                .andExpect(status().isNotFound())
                .andExpect(MockMvcResultMatchers.jsonPath("code").value("QUES-001"));
    }

    //This test case passes when you try to delete the question but the JWT token entered does not exist in the database.
    @Test
    public void deleteQuestionWithNonExistingAccessToken() throws Exception {
        mvc.perform(MockMvcRequestBuilders.delete("/question/delete/database_question_uuid").header("authorization", "non_existing_access_token"))
                .andExpect(status().isForbidden())
                .andExpect(MockMvcResultMatchers.jsonPath("code").value("ATHR-001"));
    }

    //This test case passes when you try to delete the question and the JWT token entered exists in the database but the user corresponding to that JWT token is signed out.
    @Test
    public void deleteQuestionWithSignedOutUser() throws Exception {
        mvc.perform(MockMvcRequestBuilders.delete("/question/delete/database_question_uuid").header("authorization", "database_accesstoken3"))
                .andExpect(status().isForbidden())
                .andExpect(MockMvcResultMatchers.jsonPath("code").value("ATHR-002"));
    }

    //This test case passes when you try to delete the question and the JWT token entered exists in the database and the user corresponding to that JWT token is signed in but the corresponding user is not the owner of the question or he is not the admin.
    @Test
    public void deleteQuestionWithoutOwnership() throws Exception {
        mvc.perform(MockMvcRequestBuilders.delete("/question/delete/database_question_uuid").header("authorization", "database_accesstoken2"))
                .andExpect(status().isForbidden())
                .andExpect(MockMvcResultMatchers.jsonPath("code").value("ATHR-003"));
    }


    //This test case passes when you try to delete the question which does not exist in the database.
    @Test
    public void deleteNoneExistingQuestion() throws Exception {
        mvc.perform(MockMvcRequestBuilders.delete("/question/delete/non_existing_question_uuid").header("authorization", "database_accesstoken1"))
                .andExpect(status().isNotFound())
                .andExpect(MockMvcResultMatchers.jsonPath("code").value("QUES-001"));
    }

    //This test case passes when you try to get all the questions posted by a specific user but the JWT token entered does not exist in the database.
    @Test
    public void getAllQuestionsByUserWithNonExistingAccessToken() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get("/question/all/database_uuid1").header("authorization", "non_existing_access_token"))
                .andExpect(status().isForbidden())
                .andExpect(MockMvcResultMatchers.jsonPath("code").value("ATHR-001"));
    }

    //This test case passes when you try to get all the questions posted by a specific user and the JWT token entered exists in the database but the user corresponding to that JWT token is signed out.
    @Test
    public void getAllQuestionsByUserWithSignedOutUser() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get("/question/all/database_uuid1").header("authorization", "database_accesstoken3"))
                .andExpect(status().isForbidden())
                .andExpect(MockMvcResultMatchers.jsonPath("code").value("ATHR-002"));
    }

    //This test case passes when you try to get all the questions posted by a specific user which does not exist in the database.
    @Test
    public void getAllQuestionsForNonExistingUser() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get("/question/all/non_existing_user_uuid").header("authorization", "database_accesstoken1"))
                .andExpect(status().isNotFound())
                .andExpect(MockMvcResultMatchers.jsonPath("code").value("USR-001"));
    }


}
