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
package io.github.photowey.auto.swagger.annotation;

import io.github.photowey.auto.swagger.shared.io.swagger.annotations.Extension;
import io.github.photowey.auto.swagger.shared.io.swagger.annotations.ExtensionProperty;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * {@code AutoApiModelProperty}
 * Adds and manipulates data of a model property.
 * <p>
 * Notes: copy from io.swagger.annotations.ApiModelProperty
 *
 * @author photowey
 * @date 2024/03/24
 * @since 1.0.0
 */
@Documented
@Target(FIELD)
@Retention(RUNTIME)
@AutoSwagger
public @interface AutoApiModelProperty {

    // Notes: copy from io.swagger.annotations.ApiModelProperty

    /**
     * A brief description of this property.
     */
    String value() default "";

    /**
     * Allows overriding the name of the property.
     *
     * @return the overridden property name
     */
    String name() default "";

    /**
     * Limits the acceptable values for this parameter.
     * <p>
     * There are three ways to describe the allowable values:
     * <ol>
     * <li>To set a list of values, provide a comma-separated list.
     * For example: {@code first, second, third}.</li>
     * <li>To set a range of values, start the value with "range", and surrounding by square
     * brackets include the minimum and maximum values, or round brackets for exclusive minimum and maximum values.
     * For example: {@code range[1, 5]}, {@code range(1, 5)}, {@code range[1, 5)}.</li>
     * <li>To set a minimum/maximum value, use the same format for range but use "infinity"
     * or "-infinity" as the second value. For example, {@code range[1, infinity]} means the
     * minimum allowable value of this parameter is 1.</li>
     * </ol>
     */
    String allowableValues() default "";

    /**
     * Allows for filtering a property from the API documentation. See io.swagger.core.filter.SwaggerSpecFilter.
     */
    String access() default "";

    /**
     * Currently not in use.
     */
    String notes() default "";

    /**
     * The data type of the parameter.
     * <p>
     * This can be the class name or a primitive. The value will override the data type as read from the class
     * property.
     */
    String dataType() default "";

    /**
     * Specifies if the parameter is required or not.
     */
    boolean required() default false;

    /**
     * Allows explicitly ordering the property in the model.
     */
    int position() default 0;

    /**
     * Allows a model property to be hidden in the Swagger model definition.
     */
    boolean hidden() default false;

    /**
     * A sample value for the property.
     */
    String example() default "";

    /**
     * Allows a model property to be designated as read only.
     *
     * @deprecated As of {@code swagger-annotations} 1.5.19, replaced by {@link #accessMode()}
     */
    @Deprecated
    boolean readOnly() default false;

    /**
     * Allows to specify the access mode of a model property (AccessMode.READ_ONLY, READ_WRITE)
     */
    AccessMode accessMode() default AccessMode.AUTO;


    /**
     * Specifies a reference to the corresponding type definition, overrides any other metadata specified
     */

    String reference() default "";

    /**
     * Allows passing an empty value
     */
    boolean allowEmptyValue() default false;

    /**
     * @return an optional array of extensions
     */
    Extension[] extensions() default @Extension(properties = @ExtensionProperty(name = "", value = ""));

    enum AccessMode {
        AUTO,
        READ_ONLY,
        READ_WRITE;
    }
}
