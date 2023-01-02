/*
 * Copyright 2020-2023 the original author or authors.
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
package example.app.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.gemfire.mapping.annotation.Region;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.util.Assert;

import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * Abstract Data Type (ADT) modeling a user.
 *
 * @author John Blum
 * @see org.springframework.data.gemfire.mapping.annotation.Region
 * @since 2.5.0
 */
@Getter
@ToString
@EqualsAndHashCode
@Setter(AccessLevel.PROTECTED)
@Region("Users")
@NoArgsConstructor
@RequiredArgsConstructor(staticName = "as")
@SuppressWarnings("unused")
public class User {

	@Id
	private Integer id;

	@lombok.NonNull
	private String name;

	public @NonNull User identifiedBy(@Nullable Integer id) {
		setId(id);
		return this;
	}

	public @NonNull User withName(@NonNull String name) {
		Assert.hasText(name, String.format("Name [%s] is required", name));
		setName(name);
		return this;
	}
}
