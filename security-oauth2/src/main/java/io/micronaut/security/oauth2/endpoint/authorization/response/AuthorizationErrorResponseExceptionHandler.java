/*
 * Copyright 2017-2022 original authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.micronaut.security.oauth2.endpoint.authorization.response;

import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.MutableHttpResponse;
import io.micronaut.http.server.exceptions.ExceptionHandler;
import io.micronaut.http.server.exceptions.response.ErrorContext;
import io.micronaut.http.server.exceptions.response.ErrorResponseProcessor;

import jakarta.inject.Inject;
import jakarta.inject.Singleton;

/**
 * An exception handler for {@link AuthorizationErrorResponseException}.
 *
 * @author Sergio del Amo
 * @since 1.2.0
 */
@Singleton
public class AuthorizationErrorResponseExceptionHandler implements ExceptionHandler<AuthorizationErrorResponseException, MutableHttpResponse<?>> {

    private final ErrorResponseProcessor<?> responseProcessor;

    /**
     * Default constructor.
     *
     * @deprecated See @{link #AuthorizationErrorResponseExceptionHandler(ErrorResponseProcessor)}.
     */
    @Deprecated
    public AuthorizationErrorResponseExceptionHandler() {
        this(null);
    }

    /**
     * @param responseProcessor Error Response Processor
     */
    @Inject
    public AuthorizationErrorResponseExceptionHandler(ErrorResponseProcessor<?> responseProcessor) {
        this.responseProcessor = responseProcessor;
    }

    @Override
    public MutableHttpResponse<?> handle(HttpRequest request, AuthorizationErrorResponseException exception) {
        MutableHttpResponse<AuthorizationErrorResponse> response = HttpResponse.badRequest(exception.getAuthorizationErrorResponse());
        if (responseProcessor == null) {
            return response;
        } else {
            return responseProcessor.processResponse(ErrorContext
                    .builder(request)
                    .cause(exception)
                    .errorMessage(exception.getMessage())
                    .build(), response);
        }
    }
}
