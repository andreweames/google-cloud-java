/*
 * Copyright 2015 Google Inc. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.gcloud.bigquery;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import com.google.common.collect.ImmutableList;

import org.junit.Test;

import java.util.List;

public class QueryResponseTest {

  private static final String ETAG = "etag";
  private static final Field FIELD_SCHEMA1 =
      Field.builder("StringField", Field.Type.string())
          .mode(Field.Mode.NULLABLE)
          .description("FieldDescription1")
          .build();
  private static final Schema SCHEMA = Schema.of(FIELD_SCHEMA1);
  private static final JobId JOB_ID = JobId.of("project", "job");
  private static final Long TOTAL_ROWS = 42L;
  private static final QueryResult.QueryResultsPageFetcher FETCHER =
      new QueryResult.QueryResultsPageFetcher() {
        @Override
        public QueryResult nextPage() {
          return null;
        }
      };
  private static final Long TOTAL_BYTES_PROCESSED = 4200L;
  private static final Boolean JOB_COMPLETE = true;
  private static final List<BigQueryError> ERRORS = ImmutableList.of(
      new BigQueryError("reason1", "location1", "message1", "debugInfo1"),
      new BigQueryError("reason2", "location2", "message2", "debugInfo2")
  );
  private static final Boolean CACHE_HIT = false;
  private static final QueryResult QUERY_RESULT = QueryResult.builder()
      .schema(SCHEMA)
      .totalRows(TOTAL_ROWS)
      .totalBytesProcessed(TOTAL_BYTES_PROCESSED)
      .cursor("cursor")
      .pageFetcher(FETCHER)
      .results(ImmutableList.<List<FieldValue>>of())
      .cacheHit(CACHE_HIT)
      .build();
  private static final QueryResponse QUERY_RESPONSE = QueryResponse.builder()
      .etag(ETAG)
      .jobId(JOB_ID)
      .jobComplete(JOB_COMPLETE)
      .executionErrors(ERRORS)
      .result(QUERY_RESULT)
      .build();

  @Test
  public void testBuilder() {
    assertEquals(ETAG, QUERY_RESPONSE.etag());
    assertEquals(QUERY_RESULT, QUERY_RESPONSE.result());
    assertEquals(JOB_ID, QUERY_RESPONSE.jobId());
    assertEquals(JOB_COMPLETE, QUERY_RESPONSE.jobComplete());
    assertEquals(ERRORS, QUERY_RESPONSE.executionErrors());
    assertTrue(QUERY_RESPONSE.hasErrors());
  }

  @Test
  public void testBuilderIncomplete() {
    QueryResponse queryResponse = QueryResponse.builder().jobComplete(false).build();
    assertNull(queryResponse.etag());
    assertNull(queryResponse.result());
    assertNull(queryResponse.jobId());
    assertFalse(queryResponse.jobComplete());
    assertEquals(ImmutableList.<BigQueryError>of(), queryResponse.executionErrors());
    assertFalse(queryResponse.hasErrors());
  }

  @Test
  public void testEquals() {
    compareQueryResponse(QUERY_RESPONSE, QUERY_RESPONSE);
  }

  private void compareQueryResponse(QueryResponse expected, QueryResponse value) {
    assertEquals(expected, value);
    assertEquals(expected.etag(), value.etag());
    assertEquals(expected.result(), value.result());
    assertEquals(expected.jobId(), value.jobId());
    assertEquals(expected.jobComplete(), value.jobComplete());
    assertEquals(expected.executionErrors(), value.executionErrors());
    assertEquals(expected.hasErrors(), value.hasErrors());
  }
}