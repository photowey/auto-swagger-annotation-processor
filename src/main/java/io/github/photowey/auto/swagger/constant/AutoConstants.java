/*
 * Copyright © 2024 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.github.photowey.auto.swagger.constant;

/**
 * {@code AutoConstants}
 *
 * @author photowey
 * @date 2024/03/24
 * @since 1.0.0
 */
public interface AutoConstants {

    /**
     * AutoSwagger Annotation Config Key
     * |- io.github.photowey.auto.swagger.annotation.enabled=true
     * |- io.github.photowey.auto.swagger.annotation.enabled=false
     */
    String AUTO_SWAGGER_ANNOTATION_ENABLED_CONFIG_KEY = "io.github.photowey.auto.swagger.annotation.enabled";
    String AUTO_SWAGGER_ANNOTATION_CONFIG_VALUE_ENABLED = "true";
    String AUTO_SWAGGER_ANNOTATION_CONFIG_VALUE_NOT_ENABLED = "false";

    static boolean determineAutoSwaggerAnnotationIsEnabled() {
        String configValue = System.getenv(AUTO_SWAGGER_ANNOTATION_ENABLED_CONFIG_KEY);
        if (determineAutoSwaggerAnnotationConfigValueIsValid(configValue)) {
            return Boolean.parseBoolean(configValue);
        }

        return false;
    }

    static boolean determineAutoSwaggerAnnotationConfigValueIsValid(String configValue) {
        if (null == configValue) {
            return false;
        }

        return AUTO_SWAGGER_ANNOTATION_CONFIG_VALUE_ENABLED.equalsIgnoreCase(configValue)
                || AUTO_SWAGGER_ANNOTATION_CONFIG_VALUE_NOT_ENABLED.equalsIgnoreCase(configValue);
    }
}
