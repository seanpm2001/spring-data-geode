/*
 * Copyright 2017-2023 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.springframework.data.gemfire.transaction.config;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.context.annotation.Import;
import org.springframework.data.gemfire.transaction.event.TransactionApplicationEvent;

/**
 * The {@link EnableGemfireCacheTransactions} annotation enables Pivotal GemFire or Apache Geode Cache Transactions
 * in Spring's Transaction Management infrastructure.
 *
 * @author John Blum
 * @see java.lang.annotation.Documented
 * @see java.lang.annotation.Inherited
 * @see java.lang.annotation.Retention
 * @see java.lang.annotation.Target
 * @see org.springframework.context.annotation.Import
 * @see <a href="https://docs.spring.io/spring-framework/docs/current/reference/html/data-access.html#transaction">Spring Transaction Management</a>
 * @see <a href="https://docs.spring.io/spring-data-gemfire/docs/current/reference/html/#apis:transaction-management">Spring Data GemFire Transaction Management</a>
 * @see <a href="https://geode.apache.org/docs/guide/113/developing/transactions/chapter_overview.html">Geode Transactions</a>
 * @since 2.0.0
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
@Import(GemfireCacheTransactionsConfiguration.class)
@SuppressWarnings("unused")
public @interface EnableGemfireCacheTransactions {

	/**
	 * Configures whether {@link TransactionApplicationEvent} objects are automatically fired by the framework.
	 *
	 * @return a boolean value indicating whether transactional events are automatically fired by the framework
	 * without the need to manually publish transaction events.  Defaults to {@literal false}.
	 */
	boolean enableAutoTransactionEventPublishing() default false;

}
