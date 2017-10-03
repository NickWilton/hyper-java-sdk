package com.guidedsoftware.hyper;

/*
  Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
  the License. You may obtain a copy of the License at

  http://www.apache.org/licenses/LICENSE-2.0

  Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
  an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
  specific language governing permissions and limitations under the License.

  Copyright 2016 the original author or authors.
 */

import uk.co.lucasweb.aws.v4.signer.HttpRequest;

/**
 * @author Richard Lucas
 */
class CanonicalRequest {

	private final HttpRequest httpRequest;
	private final CanonicalHeaders headers;
	private final String contentSha256;

	CanonicalRequest(HttpRequest httpRequest, CanonicalHeaders headers, String contentSha256) {
		this.httpRequest = httpRequest;
		this.headers = headers;
		this.contentSha256 = contentSha256;
	}

	String get() {

		String path = httpRequest.getPath();
		if (path.startsWith("/")) {
			path = path.substring(1);
		}

		return httpRequest.getMethod() +
				"\n" + path +
				"\n" + httpRequest.getQuery() +
				"\n" + headers.get() +
				"\n" + headers.getNames() +
				"\n" + contentSha256;
	}

	CanonicalHeaders getHeaders() {
		return headers;
	}

	@Override
	public String toString() {
		return get();
	}
}