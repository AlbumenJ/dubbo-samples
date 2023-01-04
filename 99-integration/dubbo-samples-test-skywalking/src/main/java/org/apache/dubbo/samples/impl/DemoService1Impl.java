/*
 *
 *   Licensed to the Apache Software Foundation (ASF) under one or more
 *   contributor license agreements.  See the NOTICE file distributed with
 *   this work for additional information regarding copyright ownership.
 *   The ASF licenses this file to You under the Apache License, Version 2.0
 *   (the "License"); you may not use this file except in compliance with
 *   the License.  You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 *
 */

package org.apache.dubbo.samples.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.dubbo.samples.api.DemoService1;
import org.apache.dubbo.samples.api.DemoService2;
import org.apache.dubbo.samples.api.Span;
import org.apache.skywalking.apm.toolkit.trace.TraceContext;

public class DemoService1Impl implements DemoService1 {
	private DemoService2 demoServiceLocal;

	private DemoService2 demoServiceRemote;

	public void setDemoServiceLocal(DemoService2 demoServiceLocal) {
		this.demoServiceLocal = demoServiceLocal;
	}

	public void setDemoServiceRemote(DemoService2 demoServiceRemote) {
		this.demoServiceRemote = demoServiceRemote;
	}

	@Override
	public List<Span> request() {
		List<Span> spans = new ArrayList<>();

		Span span = new Span();
		span.setTraceId(TraceContext.traceId());
		span.setSegmentId(TraceContext.segmentId());
		span.setSpanId(TraceContext.spanId());
		spans.add(span);

		System.out.println(TraceContext.getCorrelation("a").orElse(""));
		spans.addAll(demoServiceLocal.request());
		spans.addAll(demoServiceRemote.request());

		spans.addAll(demoServiceLocal.request());
		spans.addAll(demoServiceRemote.request());

		return spans;
	}
}
