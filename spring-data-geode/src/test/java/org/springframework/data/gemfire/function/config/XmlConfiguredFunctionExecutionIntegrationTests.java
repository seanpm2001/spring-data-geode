/*
 * Copyright 2002-2023 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 * https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 */
package org.springframework.data.gemfire.function.config;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;
import org.junit.runner.RunWith;

import org.apache.geode.cache.Region;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.data.gemfire.TestUtils;
import org.springframework.data.gemfire.function.config.two.TestOnRegionFunction;
import org.springframework.data.gemfire.function.execution.GemfireOnRegionFunctionTemplate;
import org.springframework.data.gemfire.function.execution.OnRegionFunctionProxyFactoryBean;
import org.springframework.data.gemfire.tests.integration.IntegrationTestsSupport;
import org.springframework.data.gemfire.tests.unit.annotation.GemFireUnitTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author David Turanski
 * @author John Blum
 */
@RunWith(SpringRunner.class)
@GemFireUnitTest
@SuppressWarnings("unused")
public class XmlConfiguredFunctionExecutionIntegrationTests extends IntegrationTestsSupport {

	@Autowired
	private ApplicationContext applicationContext;

	@Test
	public void testProxyFactoryBeanCreated() throws Exception {

		OnRegionFunctionProxyFactoryBean factoryBean =
			applicationContext.getBean("&testFunction", OnRegionFunctionProxyFactoryBean.class);

		Class<?> serviceInterface = TestUtils.readField("functionExecutionInterface", factoryBean);

		assertThat(TestOnRegionFunction.class).isEqualTo(serviceInterface);

		Region<?, ?> regionOne = applicationContext.getBean("r1", Region.class);

		GemfireOnRegionFunctionTemplate template = TestUtils.readField("gemfireFunctionOperations", factoryBean);

		assertThat(TestUtils.<Object>readField("region", template)).isSameAs(regionOne);
	}
}
