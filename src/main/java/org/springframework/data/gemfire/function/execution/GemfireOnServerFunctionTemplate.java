/*
 * Copyright 2020 the original author or authors.
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
package org.springframework.data.gemfire.function.execution;

import static org.springframework.data.gemfire.util.RuntimeExceptionFactory.newIllegalStateException;

import java.util.Optional;

import org.apache.geode.cache.RegionService;
import org.apache.geode.cache.client.ClientCache;
import org.apache.geode.cache.client.Pool;
import org.apache.geode.cache.execute.Execution;
import org.apache.geode.cache.execute.Function;

import org.springframework.data.gemfire.GemfireUtils;
import org.springframework.data.gemfire.client.PoolResolver;
import org.springframework.data.gemfire.client.support.PoolManagerPoolResolver;
import org.springframework.data.gemfire.util.CacheUtils;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

/**
 * Creates an {@literal OnServer} {@link Function} {@link Execution} initialized with
 * either a {@link RegionService cache} or a {@link Pool}.
 *
 * @author David Turanski
 * @author John Blum
 * @see org.apache.geode.cache.RegionService
 * @see org.apache.geode.cache.client.ClientCache
 * @see org.apache.geode.cache.client.Pool
 * @see org.apache.geode.cache.execute.Execution
 * @see org.apache.geode.cache.execute.Function
 * @see org.springframework.data.gemfire.function.execution.AbstractFunctionTemplate
 */
@SuppressWarnings("unused")
public class GemfireOnServerFunctionTemplate extends AbstractFunctionTemplate {

	protected static final PoolResolver DEFAULT_POOL_RESOLVER = new PoolManagerPoolResolver();

	private Pool pool;

	private PoolResolver poolResolver = DEFAULT_POOL_RESOLVER;

	private final RegionService cache;

	private String poolName;

	public GemfireOnServerFunctionTemplate(RegionService cache) {

		Assert.notNull(cache, "RegionService must not be null");

		this.cache = cache;
	}

	public GemfireOnServerFunctionTemplate(Pool pool) {
		this.cache = resolveClientCache();
		this.pool = pool;
	}

	public GemfireOnServerFunctionTemplate(String poolName) {
		this.cache = resolveClientCache();
		this.poolName = poolName;
	}

	public void setPool(Pool pool) {
		this.pool = pool;
	}

	public void setPoolName(String poolName) {
		this.poolName = poolName;
	}

	public void setPoolResolver(PoolResolver poolResolver) {
		this.poolResolver = poolResolver;
	}

	protected PoolResolver getPoolResolver() {

		PoolResolver poolResolver = this.poolResolver;

		return poolResolver != null ? poolResolver : DEFAULT_POOL_RESOLVER;
	}

	@Override
	protected AbstractFunctionExecution getFunctionExecution() {

		Object gemfireObject = resolveRequiredGemFireObject();

		return gemfireObject instanceof Pool
			? new PoolServerFunctionExecution((Pool) gemfireObject)
			: new ServerFunctionExecution((RegionService) gemfireObject);
	}

	private Object resolveRequiredGemFireObject() {
		return Optional.<Object>ofNullable(resolvePool()).orElseGet(this::resolveClientCache);
	}

	protected ClientCache resolveClientCache() {

		return Optional.ofNullable(CacheUtils.getClientCache())
			.orElseThrow(() -> newIllegalStateException("No ClientCache instance is present"));
	}

	protected Pool resolveDefaultPool() {

		return Optional.ofNullable(getPoolResolver().resolve(GemfireUtils.DEFAULT_POOL_NAME))
			.orElseThrow(() -> newIllegalStateException("No Pool was configured"));
	}

	protected Pool resolveNamedPool() {

		if (StringUtils.hasText(this.poolName)) {
			this.pool = Optional.ofNullable(getPoolResolver().resolve(this.poolName))
				.orElseThrow(() -> newIllegalStateException("No Pool with name [%s] exists",
					this.poolName));
		}

		return this.pool;
	}

	protected Pool resolvePool() {

		this.pool = Optional.ofNullable(this.pool)
			.orElseGet(this::resolveNamedPool);

		return this.pool;
	}
}
