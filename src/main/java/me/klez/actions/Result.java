/*
 * Project intellij-jasper-report-support
 *
 * Copyright 2023-2023 Alessandro 'kLeZ' Accardo
 * Previous copyright (c) 2017-2023 Chathura Buddhika
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 *
 */

package me.klez.actions;

import java.util.Objects;
import java.util.StringJoiner;

public final class Result {
	private final ResultType type;
	private final String source;
	private final String destination;

	private final String message;
	private final Exception exception;

	public Result(ResultType type, String source, String destination, String message, Exception exception) {
		this.type = Objects.requireNonNull(type);
		this.source = Objects.requireNonNull(source);
		this.destination = Objects.requireNonNull(destination);
		this.message = message;
		this.exception = exception;
	}

	public static ResultBuilder builder() {
		return new ResultBuilder();
	}

	public ResultType getType() {
		return type;
	}

	public String getSource() {
		return source;
	}

	public String getDestination() {
		return destination;
	}

	public String getMessage() {
		return message;
	}

	public Exception getException() {
		return exception;
	}

	@Override
	public String toString() {
		return new StringJoiner(", ").add("type=" + type)
		                             .add("source='" + source + "'")
		                             .add("destination='" + destination + "'")
		                             .add("message='" + message + "'")
		                             .add("exception=" + exception)
		                             .toString();
	}

	public static final class ResultBuilder {
		private ResultType type;
		private String source;
		private String destination;
		private String message;
		private Exception exception;

		private ResultBuilder() {}

		public ResultBuilder type(ResultType type) {
			this.type = type;
			return this;
		}

		public ResultBuilder source(String source) {
			this.source = source;
			return this;
		}

		public ResultBuilder destination(String destination) {
			this.destination = destination;
			return this;
		}

		public ResultBuilder message(String message) {
			this.message = message;
			return this;
		}

		public ResultBuilder exception(Exception exception) {
			this.exception = exception;
			return this;
		}

		public String getSource() {
			return source;
		}

		public String getDestination() {
			return destination;
		}

		public Result build() {
			return new Result(type, source, destination, message, exception);
		}
	}
}
