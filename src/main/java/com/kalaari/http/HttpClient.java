package com.kalaari.http;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.retry.RetryCallback;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

import com.kalaari.exception.DownstreamServiceException;
import com.kalaari.exception.KalaariErrorCode;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class HttpClient {

    @Autowired
    private RestTemplate restTemplate;

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    private HttpEntity<?> setCommonHeaders(HttpEntity<?> requestEntity) {
        HttpHeaders headers = new HttpHeaders();

        if (requestEntity != null && requestEntity.getHeaders() != null)
            headers.putAll(requestEntity.getHeaders());

        if (requestEntity != null && requestEntity.hasBody())
            return new HttpEntity<>(requestEntity.getBody(), headers);

        return new HttpEntity<>(headers);
    }

    public <T> T send(String uriString, HttpMethod httpMethod, HttpEntity<?> requestEntity, Class<T> tClass,
            RetryTemplate retryTemplate) throws DownstreamServiceException {
        return send(uriString, httpMethod, requestEntity, tClass, false, retryTemplate);
    }

    public <T> ResponseEntity<T> sendInternal(String uriString, HttpMethod httpMethod, HttpEntity<?> requestEntity,
            Class<T> tClass, Boolean debugLogsEnabled, RetryTemplate retryTemplate) throws DownstreamServiceException {
        try {
            final HttpEntity<?> updatedRequestEntity = setCommonHeaders(requestEntity);
            if (debugLogsEnabled) {
                log.info(httpMethod.name() + " for : " + uriString);
                log.info("REQUEST: {}", updatedRequestEntity);
            }
            ResponseEntity<T> response;
            if (null != retryTemplate) {
                response = retryTemplate.execute((RetryCallback<ResponseEntity<T>, Exception>) arg0 -> restTemplate
                        .exchange(uriString, httpMethod, updatedRequestEntity, tClass));
            } else {
                response = restTemplate.exchange(uriString, httpMethod, updatedRequestEntity, tClass);
            }
            if (debugLogsEnabled) {
                log.info("RESPONSE: {}", response.toString());
            } else {
                log.trace("RESPONSE: {}", response.toString());
            }
            return response;

        } catch (HttpClientErrorException e) {
            log.error("url = {}, requestEntity = {}, alertStatus: {}, message: {} body: {}", uriString,
                    requestEntity == null ? null : requestEntity.getBody(), e.getStatusCode(), e.getMessage(),
                    e.getResponseBodyAsString());
            if (e.getStatusCode().equals(HttpStatus.NOT_FOUND)) {
                throw new DownstreamServiceException(KalaariErrorCode.NOT_FOUND, e.getResponseBodyAsString(),
                        e.getStatusCode(), e);
            } else {
                throw new DownstreamServiceException(KalaariErrorCode.UNKNOWN, e.getResponseBodyAsString(),
                        e.getStatusCode(), e);
            }
        } catch (HttpServerErrorException e) {
            log.error("url = {}, requestEntity = {}, alertStatus: {}, message: {} body: {}", uriString,
                    requestEntity == null ? null : requestEntity.getBody(), e.getStatusCode(), e.getMessage(),
                    e.getResponseBodyAsString());
            throw new DownstreamServiceException(KalaariErrorCode.INTERNAL_SERVER_ERROR, e.getResponseBodyAsString(),
                    e.getStatusCode(), e);
        } catch (Exception e) {
            log.error("url = {}, requestEntity = {}, e = {}", uriString,
                    requestEntity == null ? null : requestEntity.getBody(), e);
            throw new DownstreamServiceException(KalaariErrorCode.UNKNOWN, e.getMessage(), e);
        }
    }

    public <T> ResponseEntity<T> sendInternal(String uriString, HttpMethod httpMethod, HttpEntity<?> requestEntity,
            Class<T> tClass, ParameterizedTypeReference<T> tRef, Boolean debugLogsEnabled, RetryTemplate retryTemplate)
            throws DownstreamServiceException {
        try {
            final HttpEntity<?> updatedRequestEntity = setCommonHeaders(requestEntity);
            if (debugLogsEnabled) {
                log.info(httpMethod.name() + " for : " + uriString);
                log.info("REQUEST: {}", updatedRequestEntity);
            }
            ResponseEntity<T> response;
            if (null != retryTemplate) {
                response = retryTemplate.execute((RetryCallback<ResponseEntity<T>, Exception>) arg0 -> restTemplate
                        .exchange(uriString, httpMethod, updatedRequestEntity, tClass));
            } else {
                if (tClass != null) {
                    response = restTemplate.exchange(uriString, httpMethod, updatedRequestEntity, tClass);
                } else {
                    response = restTemplate.exchange(uriString, httpMethod, updatedRequestEntity, tRef);
                }

            }
            if (debugLogsEnabled) {
                log.info("RESPONSE: {}", response.toString());
            } else {
                log.trace("RESPONSE: {}", response.toString());
            }
            return response;

        } catch (HttpClientErrorException e) {
            log.error("url = {}, requestEntity = {}, alertStatus: {}, message: {} body: {}", uriString,
                    requestEntity == null ? null : requestEntity.getBody(), e.getStatusCode(), e.getMessage(),
                    e.getResponseBodyAsString());
            if (e.getStatusCode().equals(HttpStatus.NOT_FOUND)) {
                throw new DownstreamServiceException(KalaariErrorCode.NOT_FOUND, e.getResponseBodyAsString(),
                        e.getStatusCode(), e);
            } else {
                throw new DownstreamServiceException(KalaariErrorCode.UNKNOWN, e.getResponseBodyAsString(),
                        e.getStatusCode(), e);
            }
        } catch (HttpServerErrorException e) {
            log.error("url = {}, requestEntity = {}, alertStatus: {}, message: {} body: {}", uriString,
                    requestEntity == null ? null : requestEntity.getBody(), e.getStatusCode(), e.getMessage(),
                    e.getResponseBodyAsString());
            throw new DownstreamServiceException(KalaariErrorCode.INTERNAL_SERVER_ERROR, e.getResponseBodyAsString(),
                    e.getStatusCode(), e);
        } catch (Exception e) {
            log.error("url = {}, requestEntity = {}, e = {}", uriString,
                    requestEntity == null ? null : requestEntity.getBody(), e);
            throw new DownstreamServiceException(KalaariErrorCode.UNKNOWN, e.getMessage(), e);
        }
    }

    public <T> T send(String uriString, HttpMethod httpMethod, HttpEntity<?> requestEntity, Class<T> tClass,
            Boolean debugLogsEnabled, RetryTemplate retryTemplate) throws DownstreamServiceException {
        return sendInternal(uriString, httpMethod, requestEntity, tClass, null, debugLogsEnabled, retryTemplate)
                .getBody();
    }

    public <T> T send(String uriString, HttpMethod httpMethod, HttpEntity<?> requestEntity, Class<T> tClass,
            ParameterizedTypeReference<T> tRef, Boolean debugLogsEnabled, RetryTemplate retryTemplate)
            throws DownstreamServiceException {
        return sendInternal(uriString, httpMethod, requestEntity, tClass, tRef, debugLogsEnabled, retryTemplate)
                .getBody();
    }

}
