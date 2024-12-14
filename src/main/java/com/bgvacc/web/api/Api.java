package com.bgvacc.web.api;

import com.bgvacc.web.enums.Methods;
import com.bgvacc.web.utils.LogRequest;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionType;
import com.google.gson.Gson;
import jakarta.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

/**
 *
 * @author Atanas Yordanov Arshinkov
 * @since 1.0.0
 */
public abstract class Api {

  private final Logger log = LoggerFactory.getLogger(getClass());

  @Autowired
  private RestTemplate restTemplate;

  @Autowired
  private ObjectMapper objectMapper;

  /**
   * Sets and returns the headers, required for the API call
   *
   * @return the populated headers
   */
  protected HttpHeaders getHeaders() {
    HttpHeaders headers = new HttpHeaders();
    headers.set("Accept", "application/json");
    headers.set("Content-Type", "application/json");
    headers.set("User-Agent", "Application");

    return headers;
  }

  /**
   * Executes a request to an API.
   *
   * @param <T> the expected response type class
   * @param <E> the request body type
   * @param method the HTTP method to be performed
   * @param url the relative URL to be hit
   * @param requestBody the request body
   * @param responseType the response class type
   * @return
   */
  protected <T, E> T doRequest(Methods method, String url, E requestBody, Class<T> responseType) {
    return doRequest(method, url, requestBody, responseType, null, null, null);
  }

  /**
   * Executes a request to an API.
   *
   * @param <T> the expected response type class
   * @param <E> the request body type
   * @param method the HTTP method to be performed
   * @param url the relative URL to be hit
   * @param requestBody the request body
   * @param responseType the response class type
   * @param httpRequest the HTTP request
   * @return
   */
  protected <T, E> T doRequest(Methods method, String url, E requestBody, Class<T> responseType, HttpServletRequest httpRequest) {
    return doRequest(method, url, requestBody, responseType, httpRequest, null, null);
  }

  /**
   * Executes a request to an API.
   *
   * @param <T> the expected response type class
   * @param <E> the request body type
   * @param method the HTTP method to be performed
   * @param url the relative URL to be hit
   * @param requestBody the request body
   * @param responseType the response class type
   * @param httpRequest the HTTP request
   * @param authHeaderKey
   * @param authHeaderValue
   * @return
   */
  protected <T, E> T doRequest(Methods method, String url, E requestBody, Class<T> responseType, HttpServletRequest httpRequest, String authHeaderKey, String authHeaderValue) {

    HttpHeaders headers = getHeaders();
    HttpEntity entity;

    if (isAuthPairNotNull(authHeaderKey, authHeaderValue)) {
      headers.set(authHeaderKey, authHeaderValue);
    }

//    if (isAuthorizationRequired && httpRequest != null) {
//      headers.set("Authorization", getAuthorizationToken(httpRequest));
//    }
    if (requestBody == null) {
      entity = new HttpEntity(headers);
    } else {
      entity = new HttpEntity(requestBody, headers);
    }

    LogRequest logRequest = new LogRequest(method.getMethod(), url);
    log.debug(logRequest.toString());

    try {

      ResponseEntity<T> response = restTemplate.exchange(url, method.getHttpMethod(), entity, responseType);

      return response.getBody();
    } catch (HttpClientErrorException e) {
      String responseBody = e.getResponseBodyAsString();
      Gson g = new Gson();
      log.error("Error", e);
//      throw new FCException();
//      throw g.fromJson(responseBody, FieldValidationException.class);
    }

    return null;
  }

  /**
   * Executes a request to an API and returns a list.
   *
   * @param <T> the expected response type class
   * @param <E> the request body type
   * @param method the HTTP method to be performed
   * @param url the relative URL to be hit
   * @param requestBody the request body
   * @param responseType the response class type
   * @return
   */
  protected <T, E> List<T> doRequestList(Methods method, String url, E requestBody, Class<T> responseType) {
    return doRequestList(method, url, requestBody, responseType, null, false);
  }

  /**
   * Executes a request to an API and returns a list.
   *
   * @param <T> the expected response type class
   * @param <E> the request body type
   * @param method the HTTP method to be performed
   * @param url the relative URL to be hit
   * @param requestBody the request body
   * @param responseType the response class type
   * @param httpRequest the HTTP request added; false - if not
   * @return
   */
  protected <T, E> List<T> doRequestList(Methods method, String url, E requestBody, Class<T> responseType, HttpServletRequest httpRequest) {
    return doRequestList(method, url, requestBody, responseType, httpRequest, false);
  }

  /**
   * Executes a request to an API and returns a list.
   *
   * @param <T> the expected response type class
   * @param <E> the request body type
   * @param method the HTTP method to be performed
   * @param url the relative URL to be hit
   * @param requestBody the request body
   * @param responseType the response class type
   * @param httpRequest the HTTP request
   * @param isAuthorizationRequired true - if the authorization header should be
   * added; false - if not
   * @return
   */
  protected <T, E> List<T> doRequestList(Methods method, String url, E requestBody, Class<T> responseType, HttpServletRequest httpRequest, boolean isAuthorizationRequired) {

    HttpHeaders headers = getHeaders();
    HttpEntity entity;

//    if (isAuthorizationRequired && httpRequest != null) {
//      headers.set("Authorization", getAuthorizationToken(httpRequest));
//    }
    if (requestBody == null) {
      entity = new HttpEntity(headers);
    } else {
      entity = new HttpEntity(requestBody, headers);
    }

    LogRequest logRequest = new LogRequest(method.getMethod(), url);
    log.debug(logRequest.toString());

    try {

      ResponseEntity<String> response;
      CollectionType collectionType;

      if (method == Methods.POST) {
        response = restTemplate.postForEntity(url, entity, String.class);
        collectionType = objectMapper.getTypeFactory().constructCollectionType(List.class, responseType);
      } else {
        response = restTemplate.exchange(url, method.getHttpMethod(), entity, String.class);
        collectionType = objectMapper.getTypeFactory().constructCollectionType(List.class, responseType);
      }

      return readValue(response, collectionType);

    } catch (HttpClientErrorException e) {
      String responseBody = e.getResponseBodyAsString();
      Gson g = new Gson();
//      throw new FCException();
//      throw g.fromJson(responseBody, FieldValidationException.class);
    }

    return null;
  }

  private <T> T readValue(ResponseEntity<String> response, JavaType javaType) {

    T result = null;
    if (response.getStatusCode() == HttpStatus.OK || response.getStatusCode() == HttpStatus.CREATED) {
      try {
        result = objectMapper.readValue(response.getBody(), javaType);
      } catch (IOException e) {
        log.info(e.getMessage());
      }
    } else {
      log.info("No data found {}", response.getStatusCode());
    }
    return result;
  }

  private boolean isAuthPairNotNull(String key, String value) {
    return !((key == null || key.trim().isEmpty()) || (value == null || value.trim().isEmpty()));
  }
}
