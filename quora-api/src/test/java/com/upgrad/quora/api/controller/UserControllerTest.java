package com.upgrad.quora.api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.upgrad.quora.api.QuoraApiApplication;
import com.upgrad.quora.api.model.SignupUserRequest;
import org.junit.Before;
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
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment =
        SpringBootTest.WebEnvironment.MOCK,
        classes = QuoraApiApplication.class)
@AutoConfigureMockMvc
public class UserControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    WebApplicationContext webApplicationContext;


    @Before
    public void setUp() {
        mvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    //This test case passes when you signup with a username that already exists in the database.
    @Test
    public void signupWithRepeatedUserName() throws Exception {

        SignupUserRequest s = new SignupUserRequest();
        s.setUserName("database_username");
        s.setAboutMe("database_username");
        s.setContactNumber("database_username");
        s.setFirstName("database_username");
        s.setLastName("database_username");
        s.setDob("database_username");
        s.setCountry("database_username");
        s.setEmailAddress("database_username");
        s.setPassword("database_username");

        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
        String requestJson=ow.writeValueAsString(s );
        mvc.perform(MockMvcRequestBuilders.post("/user/signup").content(requestJson).contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isConflict())
                .andExpect(MockMvcResultMatchers.jsonPath("code").value("SGR-001"));
    }

    //This test case passes when you signup with an email that already exists in the database.
    @Test
    public void signupWithRepeatedEmail() throws Exception {

        SignupUserRequest s = new SignupUserRequest();
        s.setUserName("different user");
        s.setAboutMe("database_username");
        s.setContactNumber("database_username");
        s.setFirstName("different user");
        s.setLastName("database_username");
        s.setDob("database_username");
        s.setCountry("database_username");
        s.setEmailAddress("database_email");
        s.setPassword("database_username");

        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
        String requestJson=ow.writeValueAsString(s );
        mvc.perform(MockMvcRequestBuilders.post("/user/signup").content(requestJson).contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isConflict())
                .andExpect(MockMvcResultMatchers.jsonPath("code").value("SGR-002"));
    }


    //This test case passes when you try to signout but the JWT token entered does not exist in the database.
    @Test
    public void signoutWithNonExistingAccessToken() throws Exception {
        mvc.perform(MockMvcRequestBuilders.post("/user/signout").header("authorization", "non_existing_access_token"))
                .andExpect(status().isUnauthorized())
                .andExpect(MockMvcResultMatchers.jsonPath("code").value("SGR-001"));
    }
}