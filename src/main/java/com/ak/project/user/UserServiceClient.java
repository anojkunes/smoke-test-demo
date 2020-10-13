package com.ak.project.user;

import com.ak.project.AbstractClient;
import com.ak.project.common.WrappedResponse;
import com.ak.project.user.model.UserRequest;
import com.ak.project.user.model.UserResponse;
import com.fasterxml.jackson.core.type.TypeReference;

import java.util.ArrayList;
import java.util.List;

public class UserServiceClient extends AbstractClient {

    private static final String GET_USER_URL = "/users/%s";
    private static final String DELETE_USER_URL = "/users/%s";
    private static final String CREATE_USER_URL = "/users";

    private List<Long> idsToDelete = new ArrayList<>();

    public UserServiceClient(String url) {
        super(url);
    }

    public WrappedResponse<UserResponse> getUser(long id) {
        WrappedResponse<UserResponse> userResponse = makeGetRequest(GET_USER_URL, new String[] { }, new TypeReference<>() {}, id);
        return userResponse;
    }

    public WrappedResponse<UserResponse> createUser(UserRequest request, String token) {
        WrappedResponse<UserResponse> userResponse = makePostRequest(CREATE_USER_URL, request, new String[] { "Authorization", "Bearer " + token }, new TypeReference<>() {});
        idsToDelete.add(userResponse.getData().getId());
        return userResponse;
    }

    public void deleteUser(long id, String token) {
        makeDeleteRequest(DELETE_USER_URL, new String[] { "Authorization", "Bearer " + token }, id);
    }

    public void delete(String token) {
        idsToDelete.forEach(x -> deleteUser(x, token));
        idsToDelete.clear();
    }
}
