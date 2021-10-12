/*
 * Copyright 2017-2020 original authors
 *
 *  Licensed under the Apache License, Version 2.0 \(the "License"\);
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *  https://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *
 */
package io.micronaut.security.token.paseto.config;

import io.micronaut.context.annotation.ConfigurationProperties;
import io.micronaut.context.annotation.Requires;
import io.micronaut.core.util.StringUtils;
import io.micronaut.security.token.config.TokenConfigurationProperties;

/**
 * {@link PasetoConfiguration} implementation.
 *
 * @author Utsav Varia
 * @since 3.0
 */
@Requires(property = TokenConfigurationProperties.PREFIX + ".enabled", notEquals = StringUtils.FALSE)
@ConfigurationProperties(PasetoConfigurationProperties.PREFIX)
public class PasetoConfigurationProperties implements PasetoConfiguration {

    public static final String PREFIX = TokenConfigurationProperties.PREFIX  + ".paseto";
    /**
     * The default enable value.
     */
    public static final boolean DEFAULT_ENABLED = true;

    private boolean enabled = DEFAULT_ENABLED;

    /**
     *
     * @return a boolean flag indicating whether PASETO beans should be enabled or not
     */
    @Override
    public boolean isEnabled() {
        return enabled;
    }

    /**
     * Sets whether Paseto security is enabled. Default value ({@value #DEFAULT_ENABLED}).
     * @param enabled True if it is
     */
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }
}
