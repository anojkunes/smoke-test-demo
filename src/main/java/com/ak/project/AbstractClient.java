package com.ak.project;

import com.ak.project.common.ErrorResponse;
import com.ak.project.common.WrappedPageResponse;
import com.ak.project.common.WrappedResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpRequest.BodyPublishers;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.util.List;
import java.util.stream.Collectors;

import static java.lang.String.format;

@Slf4j
@Getter
public abstract class AbstractClient {

    protected static final List<Integer> SUCCESS_STATUS_CODES = List.of(200, 201, 202, 204);
    protected static final List<Integer> AUTH_ERROR_STATUS_CODES = List.of(401, 403);
    protected static final HttpClient CLIENT = HttpClient.newBuilder().build();

    private static final String[] HEADERS = new String[]{
            "Content-Type",
            "application/json"
    };

    protected final ObjectMapper mapper;
    protected final String url;
    private final String[] headers;

    protected AbstractClient(String url) {
        this(url, HEADERS);
    }

    protected AbstractClient(String url, String[] headers) {
        this.url = url;
        this.headers = headers;
        this.mapper = new ObjectMapper();
        this.mapper.registerModule(new JavaTimeModule());
        this.mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        this.mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
    }

    protected void assertResult(HttpResponse<?> response) throws JsonProcessingException {
        log.info("({} {}) [{}] - Response: {}", response.request().method().toUpperCase(), response.uri(), response.statusCode(), response.body().toString());
        handleFailureErrors(response);
        handleSuccessErrors(response);
    }

    protected <T, R> T makePostRequest(String endpointUrl, R requestObj, String[] authHeader, TypeReference<T> responseClass,  Object... params) {
        try {
            URI uri = new URI(format(url + endpointUrl, params));
            String json = mapper.writeValueAsString(requestObj);
            log.info("headers: {}, uri = {} and requestBody: {}", headers, uri, json);

            var requestBuilder = HttpRequest.newBuilder()
                    .uri(uri)
                    .headers(headers)
                    .POST(BodyPublishers.ofString(json));

            if (authHeader != null && authHeader.length == 2) {
                requestBuilder.header(authHeader[0], authHeader[1]);
            }

            var request = requestBuilder.build();

            var response = CLIENT.send(request, BodyHandlers.ofString());
            assertResult(response);

            return mapper.readValue(response.body(), responseClass);

        } catch (URISyntaxException | IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    protected <T, R> T makePostRequest(String endpointUrl, String[] authHeader, TypeReference<T> responseClass,  Object... params) {
        try {
            URI uri = new URI(format(url + endpointUrl, params));
            log.info("headers: {} and uri = {}", headers, uri);

            var requestBuilder = HttpRequest.newBuilder()
                    .uri(uri)
                    .headers(headers)
                    .POST(BodyPublishers.noBody());

            if (authHeader != null && authHeader.length == 2) {
                requestBuilder.header(authHeader[0], authHeader[1]);
            }

            var request = requestBuilder.build();

            var response = CLIENT.send(request, BodyHandlers.ofString());
            assertResult(response);

            return mapper.readValue(response.body(), responseClass);

        } catch (URISyntaxException | IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    protected <T> T makeGetRequest(String endpointUrl, String[] authHeader, TypeReference<T> responseClass, Object... params) {
        try {
            var requestBuilder = HttpRequest.newBuilder()
                    .uri(new URI(format(url + endpointUrl, params)))
                    .headers(headers)
                    .GET();

            if (authHeader != null && authHeader.length == 2) {
                requestBuilder.header(authHeader[0], authHeader[1]);
            }

            var request = requestBuilder.build();

            var response = CLIENT.send(request, BodyHandlers.ofString());
            assertResult(response);
            return mapper.readValue(response.body(), responseClass);
            
        } catch (URISyntaxException | IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    protected <R> void makePutRequest(String endpointUrl, R requestObj, Object... params) {
        try {
            String requestBody = toJson(requestObj);
            var request = HttpRequest.newBuilder()
                    .uri(new URI(format(url + endpointUrl, params)))
                    .headers(headers)
                    .PUT(BodyPublishers.ofString(requestBody))
                    .build();

            var response = CLIENT.send(request, BodyHandlers.ofString());
            assertResult(response);

        } catch (URISyntaxException | IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
    
    protected <R> void makePutRequest(String endpointUrl, String[] authHeader, Object... params) {
        try {
            var requestBuilder = HttpRequest.newBuilder()
                    .uri(new URI(format(url + endpointUrl, params)))
                    .headers(headers)
                    .PUT(BodyPublishers.noBody());

            if (authHeader != null && authHeader.length == 2) {
                requestBuilder.header(authHeader[0], authHeader[1]);
            }
            
            var request = requestBuilder.build();
            
            var response = CLIENT.send(request, BodyHandlers.ofString());
            assertResult(response);

        } catch (URISyntaxException | IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    protected <T, R> T makePutRequest(String endpointUrl, R requestObj, TypeReference<T> responseClass, Object... params) {
        try {
            String requestBody = toJson(requestObj);
            var request = HttpRequest.newBuilder()
                    .uri(new URI(format(url + endpointUrl, params)))
                    .headers(headers)
                    .PUT(BodyPublishers.ofString(requestBody))
                    .build();
            var response = CLIENT.send(request, BodyHandlers.ofString());
            assertResult(response);
            return mapper.readValue(response.body(), responseClass);

        } catch (URISyntaxException | IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
    
    protected void makeDeleteRequest(String endpointUrl, String[] authHeader, Object... params) {
        try {
            var requestBuilder = HttpRequest.newBuilder()
                    .uri(new URI(format(url + endpointUrl, params)))
                    .headers(headers)
                    .DELETE();

            if (authHeader != null && authHeader.length == 2) {
                requestBuilder.header(authHeader[0], authHeader[1]);
            }

            var request = requestBuilder.build();

            var response = CLIENT.send(request, BodyHandlers.ofString());
            assertResult(response);
        } catch (URISyntaxException | IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    private String toJson(Object requestObj) throws JsonProcessingException {
        if (requestObj instanceof String) {
            return (String) requestObj;
        }
        return mapper.writeValueAsString(requestObj);
    }

    private void handleSuccessErrors(HttpResponse<?> response) throws JsonProcessingException {
        var object = mapper.readValue(response.body().toString(), JsonNode.class);

        if (AUTH_ERROR_STATUS_CODES.contains(object.get("code").asInt())) {
            assert false : object.get("code").asInt() + " " + response.toString();
        }

        if (object.get("code").asInt() == 422) {
            var errors = mapper.readValue(response.body().toString(), new TypeReference<WrappedPageResponse<ErrorResponse>>() {});
            var message = errors.getData().stream().map(x -> x.getField() != null ? x.getField() + ": " + x.getMessage() : x.getMessage()).collect(Collectors.joining(","));
            throw new ServiceException(object.get("code").asInt(), message, response.statusCode());
        } else if (object.get("code").asInt() == 404 || object.get("code").asInt() == 405 || object.get("code").asInt() == 415
                || object.get("code").asInt() == 429 || object.get("code").asInt() == 401) {
            var errors = mapper.readValue(response.body().toString(), new TypeReference<WrappedResponse<ErrorResponse>>() {});
            throw new ServiceException(object.get("code").asInt(), errors.getData().getMessage(), response.statusCode());
        }
    }

    private void handleFailureErrors(HttpResponse<?> response) throws JsonProcessingException {
        if (!SUCCESS_STATUS_CODES.contains(response.statusCode())) {

            if (AUTH_ERROR_STATUS_CODES.contains(response.statusCode())) {
                assert false : response.statusCode() + " " + response.toString();
            }

            WrappedResponse<ErrorResponse> error = mapper.readValue(response.body().toString(), new TypeReference<WrappedResponse<ErrorResponse>>() {});

            log.info("ServiceException " + error);
            throw new ServiceException(error.getCode(), error.getData().getMessage(), response.statusCode());
        }
    }
}
