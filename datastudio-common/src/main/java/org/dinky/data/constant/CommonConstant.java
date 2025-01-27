/*
 *
 *  Licensed to the Apache Software Foundation (ASF) under one or more
 *  contributor license agreements.  See the NOTICE file distributed with
 *  this work for additional information regarding copyright ownership.
 *  The ASF licenses this file to You under the Apache License, Version 2.0
 *  (the "License"); you may not use this file except in compliance with
 *  the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *
 */

package org.dinky.data.constant;

import java.util.Arrays;
import java.util.regex.Pattern;

/**
 * CommonConstant
 *
 * @since 2021/5/28 9:35
 */
public final class CommonConstant {

    /** 实例健康 */
    public static final String HEALTHY = "1";

    public static final String STUDIO_APP_MAIN_CLASS = "org.dinky.app.MainApp";
    public static final String LineSep = System.getProperty("line.separator");

    public static final Pattern GLOBAL_VARIABLE_PATTERN = Pattern.compile("\\$\\{(.+?)}");

    public static final String DEFAULT_EXPRESSION_VARIABLES = String.join(
            ",",
            Arrays.asList(
                    "cn.hutool.core.date.DateUtil",
                    "cn.hutool.core.util.IdUtil",
                    "cn.hutool.core.util.RandomUtil",
                    "cn.hutool.core.util.StrUtil"));
}
