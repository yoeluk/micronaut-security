/*
 * Copyright 2017-2020 original authors
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
package io.micronaut.security.token.jwt.generator;

import io.micronaut.context.BeanContext;
import io.micronaut.context.event.ApplicationEventPublisher;
import io.micronaut.core.annotation.NonNull;
import io.micronaut.core.annotation.Nullable;
import io.micronaut.security.authentication.Authentication;
import io.micronaut.security.token.event.RefreshTokenGeneratedEvent;
import io.micronaut.security.token.generator.RefreshTokenGenerator;
import io.micronaut.security.token.generator.TokenGenerator;
import io.micronaut.security.token.jwt.generator.claims.ClaimsGenerator;
import io.micronaut.security.token.jwt.generator.claims.ClaimsGeneratorAdapter;
import io.micronaut.security.token.jwt.generator.claims.OldAccessTokenConfigurationAdapter;
import io.micronaut.security.token.jwt.generator.claims.OldClaimsGeneratorAdapter;
import io.micronaut.security.token.jwt.render.AccessRefreshToken;
import io.micronaut.security.token.jwt.render.OldAccessRefreshTokenAdapter;
import io.micronaut.security.token.jwt.render.OldTokenRenderAdapter;
import io.micronaut.security.token.jwt.render.TokenRenderAdapter;
import io.micronaut.security.token.jwt.render.TokenRenderer;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.Optional;

/**
 * Generates http responses with access and refresh token.
 *
 * @author Sergio del Amo
 * @since 1.0
 * @deprecated Use {@link io.micronaut.security.token.generator.DefaultAccessRefreshTokenGenerator} instead.
 */
@Deprecated
@Singleton
public class DefaultAccessRefreshTokenGenerator implements AccessRefreshTokenGenerator {

    private static final Logger LOG = LoggerFactory.getLogger(AccessRefreshTokenGenerator.class);

    protected final BeanContext beanContext;
    protected final RefreshTokenGenerator refreshTokenGenerator;
    protected final ClaimsGenerator claimsGenerator;
    protected final AccessTokenConfiguration accessTokenConfiguration;
    protected final TokenRenderer tokenRenderer;
    protected final TokenGenerator tokenGenerator;
    protected final ApplicationEventPublisher eventPublisher;

    private final io.micronaut.security.token.generator.AccessRefreshTokenGenerator accessRefreshTokenGenerator;

    /**
     *
     * @param accessTokenConfiguration The access token generator config
     * @param tokenRenderer The token renderer
     * @param tokenGenerator The token generator
     * @param beanContext Bean Context
     * @param refreshTokenGenerator The refresh token generator
     * @param claimsGenerator Claims generator
     * @param eventPublisher The Application event publisher
     * @deprecated Use {@link DefaultAccessRefreshTokenGenerator(AccessRefreshTokenGenerator) instead.
     */
    @Deprecated
    public DefaultAccessRefreshTokenGenerator(AccessTokenConfiguration accessTokenConfiguration,
                                       TokenRenderer tokenRenderer,
                                       TokenGenerator tokenGenerator,
                                       BeanContext beanContext,
                                       @Nullable RefreshTokenGenerator refreshTokenGenerator,
                                       ClaimsGenerator claimsGenerator,
                                       ApplicationEventPublisher eventPublisher) {
        this.accessTokenConfiguration = accessTokenConfiguration;
        this.tokenRenderer = tokenRenderer;
        this.tokenGenerator = tokenGenerator;
        this.beanContext = beanContext;
        this.refreshTokenGenerator = refreshTokenGenerator;
        this.claimsGenerator = claimsGenerator;
        this.eventPublisher = eventPublisher;

        this.accessRefreshTokenGenerator = new io.micronaut.security.token.generator.DefaultAccessRefreshTokenGenerator(new AccessTokenConfigurationAdapter(accessTokenConfiguration),
                new TokenRenderAdapter(tokenRenderer),
                tokenGenerator,
                beanContext,
                refreshTokenGenerator,
                new ClaimsGeneratorAdapter(claimsGenerator),
                eventPublisher);
    }

    /**
     *
     * @param accessTokenConfiguration The access token generator config
     * @param tokenRenderer The token renderer
     * @param tokenGenerator The token generator
     * @param beanContext Bean Context
     * @param refreshTokenGenerator The refresh token generator
     * @param claimsGenerator Claims generator
     * @param eventPublisher The Application event publisher
     * @param accessRefreshTokenGenerator Access Refresh Token Generator
     */
    @Inject
    public DefaultAccessRefreshTokenGenerator(io.micronaut.security.token.generator.AccessTokenConfiguration accessTokenConfiguration,
                                              io.micronaut.security.token.render.TokenRenderer tokenRenderer,
                                              TokenGenerator tokenGenerator,
                                              BeanContext beanContext,
                                              @Nullable RefreshTokenGenerator refreshTokenGenerator,
                                              io.micronaut.security.token.claims.ClaimsGenerator claimsGenerator,
                                              ApplicationEventPublisher eventPublisher,
                                              io.micronaut.security.token.generator.AccessRefreshTokenGenerator accessRefreshTokenGenerator) {
        this.accessTokenConfiguration = new OldAccessTokenConfigurationAdapter(accessTokenConfiguration);
        this.tokenRenderer = new OldTokenRenderAdapter(tokenRenderer);
        this.tokenGenerator = tokenGenerator;
        this.beanContext = beanContext;
        this.refreshTokenGenerator = refreshTokenGenerator;
        this.claimsGenerator = new OldClaimsGeneratorAdapter(claimsGenerator);
        this.eventPublisher = eventPublisher;
        this.accessRefreshTokenGenerator = accessRefreshTokenGenerator;
    }

    /**
     * Generate an {@link AccessRefreshToken} response for the given
     * user details.
     *
     * @param authentication Authenticated user's representation.
     * @return The http response
     */
    @NonNull
    @Override
    public Optional<AccessRefreshToken> generate(@NonNull Authentication authentication) {
        return accessRefreshTokenGenerator.generate(authentication).map(OldAccessRefreshTokenAdapter::new);
    }

    /**
     * Generates a refresh token and emits a {@link RefreshTokenGeneratedEvent}.
     * @param authentication Authenticated user's representation.
     * @return {@literal Optional#empty()} if refresh token was not generated or the refresh token wrapped in an Optional.
     */
    @NonNull
    public Optional<String> generateRefreshToken(@NonNull Authentication authentication) {
        return accessRefreshTokenGenerator.generateRefreshToken(authentication);
    }

    /**
     * Generate an {@link AccessRefreshToken} response for the given
     * refresh token and claims.
     *
     * @param refreshToken The refresh token
     * @param oldClaims The claims to generate the access token
     * @return The http response
     */
    @NonNull
    public Optional<AccessRefreshToken> generate(@Nullable String refreshToken, @NonNull Map<String, ?> oldClaims) {
        return accessRefreshTokenGenerator.generate(refreshToken, oldClaims).map(OldAccessRefreshTokenAdapter::new);
    }

    /**
     * Generate a new access refresh token.
     *
     * @param refreshToken The refresh token
     * @param authentication The user details to create a new access token
     * @return The optional access refresh token
     */
    @NonNull
    @Override
    public Optional<AccessRefreshToken> generate(@Nullable String refreshToken, @NonNull Authentication authentication) {
        return accessRefreshTokenGenerator.generate(refreshToken, authentication).map(OldAccessRefreshTokenAdapter::new);
    }

    /**
     *
     * @param authentication User details for which the access token is being generated
     * @return expiration of the new access token
     */
    @NonNull
    public Integer accessTokenExpiration(@NonNull Authentication authentication) {
        return accessTokenConfiguration.getExpiration();
    }

    /**
     *
     * @param oldClaims The old claims used to build the new token
     * @return expiration of the new access token
     */
    @NonNull
    public Integer accessTokenExpiration(@NonNull Map<String, ?> oldClaims) {
        return accessTokenConfiguration.getExpiration();
    }
}
