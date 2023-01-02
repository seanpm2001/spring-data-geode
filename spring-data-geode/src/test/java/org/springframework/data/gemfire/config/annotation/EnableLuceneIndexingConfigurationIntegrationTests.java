/*
 * Copyright 2018-2023 the original author or authors.
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
package org.springframework.data.gemfire.config.annotation;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Collection;
import java.util.stream.Collectors;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import org.apache.geode.cache.Region;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.DependsOn;
import org.springframework.data.gemfire.search.lucene.ProjectingLuceneOperations;
import org.springframework.data.gemfire.search.lucene.ProjectingLuceneTemplate;
import org.springframework.data.gemfire.test.model.Book;
import org.springframework.data.gemfire.tests.integration.IntegrationTestsSupport;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * Integration Tests for Lucene Indexing Configuration.
 *
 * @author John Blum
 * @see org.junit.Test
 * @see org.apache.geode.cache.Region
 * @see org.springframework.data.gemfire.search.lucene.ProjectingLuceneOperations
 * @see org.springframework.data.gemfire.search.lucene.ProjectingLuceneTemplate
 * @see org.springframework.data.gemfire.tests.integration.IntegrationTestsSupport
 * @see org.springframework.test.context.ContextConfiguration
 * @see org.springframework.test.context.junit4.SpringRunner
 * @since 2.0.0
 */
@RunWith(SpringRunner.class)
@ContextConfiguration
@SuppressWarnings("unused")
public class EnableLuceneIndexingConfigurationIntegrationTests extends IntegrationTestsSupport {

	@Autowired
	private ProjectingLuceneOperations luceneTemplate;

	@Autowired
	@Qualifier("Books")
	private Region<Long, Book> books;

	@Before
	public void setup() {

		long isbn = 1L;

		put(Book.newBook(++isbn, "Lord of the Rings - The Fellowship of the Ring"));
		put(Book.newBook(++isbn, "Star Wars III - Revenge of the Sith"));
		put(Book.newBook(++isbn, "Hitch Hikers Guide to the Galaxy"));
		put(Book.newBook(++isbn, "Star Wars VI - Return of the Jedi"));
		put(Book.newBook(++isbn, "Lord of the Rings - The Two Towers"));
		put(Book.newBook(++isbn, "Star Wars VIII - The Last Jedi"));
		put(Book.newBook(++isbn, "Lord of the Rings - The Return of the King"));
	}

	private void put(Book book) {
		this.books.put(book.getId(), book);
	}

	@Test
	public void searchForAllStarWarsBooksIsSuccessful() {

		Collection<BookTitleView> books =
			this.luceneTemplate.query("title: Star Wars*", "title", BookTitleView.class);

		assertThat(books).isNotNull();
		assertThat(books).hasSize(3);
		assertThat(books.stream().map(BookTitleView::getTitle).collect(Collectors.toList()))
			.containsOnly("Star Wars III - Revenge of the Sith", "Star Wars VI - Return of the Jedi",
				"Star Wars VIII - The Last Jedi");
	}

	@PeerCacheApplication
	@EnableEntityDefinedRegions(basePackageClasses = Book.class)
	@EnableIndexing
	static class TestConfiguration {

		@Bean
		@DependsOn("BookTitleIdx")
		ProjectingLuceneOperations luceneTemplate() {
			return new ProjectingLuceneTemplate("BookTitleIdx", "/Books");
		}
	}

	interface BookTitleView {
		String getTitle();
	}
}
