/*
 * Copyright 2021 EPAM Systems, Inc
 *
 * See the NOTICE file distributed with this work for additional information
 * regarding copyright ownership. Licensed under the Apache License,
 * Version 2.0 (the "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package com.epam.deltix.util.lang;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Serves as directive for Deltix Dependency Analyzer that annotated class has dependency on classes listed by values parameter.
 *
 * Sometimes third-party library may use yet another third-party via reflection. This directive allows to reflect such hidden dependencies.
 *
 * For example:
 * <pre>
 * &#64;DependsOnClass (org.apache.xbean.spring.context.ClassPathXmlApplicationContext.class)
 * </pre>
 *
 * @see Depends
 */
@Documented
@Retention (RetentionPolicy.CLASS)
@Target (ElementType.TYPE)
public @interface DependsOnClass {
    Class<?> [] value();
}