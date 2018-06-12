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
package example.app.geode.function.impl;

import org.springframework.data.gemfire.function.annotation.GemfireFunction;

/**
 * {@link GemfireFunction} implementing an {@literal echo}.
 *
 * @author John Blum
 * @see org.springframework.data.gemfire.function.annotation.GemfireFunction
 * @since 1.0.0
 */
@SuppressWarnings("unused")
public class EchoFunction {

	@GemfireFunction(id = "echo", hasResult = true)
	public String echo(String value) {
		return value;
	}
}
