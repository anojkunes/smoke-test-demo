package com.ak.project.smoketest

import com.ak.project.BaseTest
import com.ak.project.ServiceException
import com.ak.project.config.TestLogging
import com.ak.project.user.model.UserRequest

@TestLogging
class UserTest extends BaseTest {

    static final String USER_EMAIL = "${UUID.randomUUID()}@transformingsystems.com"

    def "Create a User"() {
        given: "setup access token"
        def userRequest= new UserRequest("TS Tester", USER_EMAIL, "Male", "Active")

        when: "create a user with authorization headers given"
        def createdUser = userServiceClient.createUser(userRequest, ACCESS_TOKEN);

        then: "check created user details"
        createdUser != null
        createdUser.data.name == userRequest.name
        createdUser.data.email == userRequest.email

        when: "get user by userId"
        def getUser = userServiceClient.getUser(createdUser.data.id)

        then: "check created user exists"
        getUser.data.id == createdUser.data.id
        getUser.data.email == createdUser.data.email
    }

    def "User already been created"() {
        given: "setup access token"
        def userRequest= new UserRequest("TS Tester", USER_EMAIL, "Male", "Active")

        when: "create a user with authorization headers given"
        def createdUser = userServiceClient.createUser(userRequest, ACCESS_TOKEN);

        then: "check created user details"
        createdUser != null
        createdUser.data.name == userRequest.name
        createdUser.data.email == userRequest.email

        when: "get user by userId"
        def getUser = userServiceClient.getUser(createdUser.data.id)

        then: "check created user exists"
        getUser.data.id == createdUser.data.id
        getUser.data.email == createdUser.data.email

        when: "create the same user again with authorization headers given"
        userServiceClient.createUser(userRequest, ACCESS_TOKEN);

        then:
        def ex = thrown(ServiceException)
        ex.errorCode == 422
    }
}
