package org.apache.dubbo.samples.test;

import java.util.List;

import org.apache.dubbo.config.ApplicationConfig;
import org.apache.dubbo.config.ReferenceConfig;
import org.apache.dubbo.config.RegistryConfig;
import org.apache.dubbo.config.bootstrap.DubboBootstrap;
import org.apache.dubbo.samples.api.DemoService1;
import org.apache.dubbo.samples.api.Span;
import org.apache.skywalking.apm.toolkit.trace.Trace;
import org.apache.skywalking.apm.toolkit.trace.TraceContext;
import org.junit.Assert;
import org.junit.Test;

public class SkywalkingIT {
	private DemoService1 demoService1;

	public SkywalkingIT() {
		ApplicationConfig applicationConfig = new ApplicationConfig("provider1");

		RegistryConfig registryConfig = new RegistryConfig(
				"nacos://" + System.getProperty("nacos.address", "127.0.0.1") + ":8848?username=nacos&password=nacos");

		ReferenceConfig<DemoService1> referenceConfig = new ReferenceConfig<>();
		referenceConfig.setInterface(DemoService1.class);

		DubboBootstrap.getInstance()
				.application(applicationConfig)
				.registry(registryConfig)
				.reference(referenceConfig)
				.start();

		demoService1 = referenceConfig.get();
	}

	@Test
	public void query() {
		List<Span> spans = request();
		Assert.assertEquals(6, spans.size());
		String traceId = spans.get(0).getTraceId();
		Assert.assertEquals(traceId, spans.get(1).getTraceId());
		Assert.assertEquals(traceId, spans.get(2).getTraceId());
		Assert.assertEquals(traceId, spans.get(3).getTraceId());
		Assert.assertEquals(traceId, spans.get(4).getTraceId());
		Assert.assertEquals(traceId, spans.get(5).getTraceId());

		System.out.println(spans);
	}

	@Trace
	private List<Span> request() {
		Span span = new Span();
		span.setTraceId(TraceContext.traceId());
		span.setSegmentId(TraceContext.segmentId());
		span.setSpanId(TraceContext.spanId());
		TraceContext.putCorrelation("a", "b");
		List<Span> spans = demoService1.request();
		spans.add(span);
		return spans;
	}
}
