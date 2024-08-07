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

package org.dinky.configure;

import org.dinky.context.TenantContextHolder;
import org.dinky.interceptor.PostgreSQLPrepareInterceptor;
import org.dinky.interceptor.PostgreSQLQueryInterceptor;
import org.dinky.mybatis.handler.DateMetaObjectHandler;
import org.dinky.mybatis.properties.MybatisPlusFillProperties;

import java.util.Set;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.handler.TenantLineHandler;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.TenantLineInnerInterceptor;
import com.google.common.collect.ImmutableSet;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.LongValue;
import net.sf.jsqlparser.expression.NullValue;

/** mybatisPlus config class */
@Configuration
@MapperScan("org.dinky.mapper")
@EnableConfigurationProperties(MybatisPlusFillProperties.class)
@Slf4j
@RequiredArgsConstructor
public class MybatisPlusConfig {

    private final MybatisPlusFillProperties autoFillProperties;

    private static final Set<String> IGNORE_TABLE_NAMES = ImmutableSet.of(
            "studio_namespace",
            "studio_alert_group",
            "studio_alert_history",
            "studio_alert_instance",
            "studio_catalogue",
            "studio_cluster",
            "studio_cluster_configuration",
            "studio_database",
            "studio_fragment",
            "studio_history",
            "studio_jar",
            "studio_job_history",
            "studio_job_instance",
            "studio_role",
            "studio_savepoints",
            "studio_task",
            "studio_task_statement",
            "studio_git_project",
            "studio_task_version");

    @Bean
    @Profile("pgsql")
    public PostgreSQLQueryInterceptor postgreSQLQueryInterceptor() {
        return new PostgreSQLQueryInterceptor();
    }

    /**
     * Add the plugin to the MyBatis plugin interceptor chain.
     *
     * @return {@linkplain PostgreSQLPrepareInterceptor}
     */
    @Bean
    @Profile("pgsql")
    public PostgreSQLPrepareInterceptor postgreSQLPrepareInterceptor() {
        return new PostgreSQLPrepareInterceptor();
    }

    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor() {
        log.info("mybatis plus interceptor execute");
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
        interceptor.addInnerInterceptor(new TenantLineInnerInterceptor(new TenantLineHandler() {

            @Override
            public Expression getTenantId() {
                Integer tenantId = (Integer) TenantContextHolder.get();
                if (tenantId == null) {
                    return new NullValue();
                }
                return new LongValue(tenantId);
            }

            @Override
            public boolean ignoreTable(String tableName) {
                if (TenantContextHolder.isIgnoreTenant()) {
                    return true;
                }
                return !IGNORE_TABLE_NAMES.contains(tableName);
            }
        }));
        interceptor.addInnerInterceptor(new PaginationInnerInterceptor());
        return interceptor;
    }

    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnProperty(
            prefix = "dinky.mybatis-plus.fill",
            name = "enabled",
            havingValue = "true",
            matchIfMissing = true)
    public MetaObjectHandler metaObjectHandler() {
        return new DateMetaObjectHandler(autoFillProperties);
    }
}
