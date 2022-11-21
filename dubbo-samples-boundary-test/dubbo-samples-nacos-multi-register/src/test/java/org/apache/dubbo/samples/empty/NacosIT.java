/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.dubbo.samples.empty;

import org.apache.dubbo.common.logger.LoggerFactory;
import org.apache.dubbo.config.ApplicationConfig;
import org.apache.dubbo.config.ProtocolConfig;
import org.apache.dubbo.config.ReferenceConfig;
import org.apache.dubbo.config.RegistryConfig;
import org.apache.dubbo.config.ServiceConfig;
import org.apache.dubbo.config.bootstrap.DubboBootstrap;
import org.apache.dubbo.rpc.model.FrameworkModel;
import org.apache.dubbo.samples.api.GreetingsService;
import org.apache.dubbo.samples.provider.GreetingsServiceImpl;

import org.junit.After;
import org.junit.Assert;
import org.junit.Test;

import java.util.HashSet;
import java.util.Set;

public class NacosIT {
    @After
    public void after() {
        FrameworkModel.destroyAll();
    }

    @Test
    public void testDefault() throws InterruptedException {
        LoggerFactory.setLoggerAdapter(FrameworkModel.defaultModel(), "log4j");
        String nacosAddress = System.getProperty("nacos.address", "localhost");
        String nacosPort = System.getProperty("nacos.port", "8848");
        System.setProperty("dubbo.application.metadata.publish.delay", "10");

        ServiceConfig<GreetingsService> serviceConfig1 = new ServiceConfig<>();
        serviceConfig1.setInterface(GreetingsService.class);
        serviceConfig1.setRef(new GreetingsServiceImpl("Server 1"));
        serviceConfig1.setProtocol(new ProtocolConfig("dubbo1", 20881));

        DubboBootstrap.getInstance()
                .application(new ApplicationConfig("provider"))
                .registry(new RegistryConfig("nacos://" + nacosAddress + ":" + nacosPort + "?enable-empty-protection=false&username=nacos&password=nacos"))
                .service(serviceConfig1)
                .start();
        Thread.sleep(5000);

        ReferenceConfig<GreetingsService> referenceConfig = new ReferenceConfig<>();
        referenceConfig.setInterface(GreetingsService.class);
        referenceConfig.setRegistry(new RegistryConfig("nacos://" + nacosAddress + ":" + nacosPort + "?enable-empty-protection=false&username=nacos&password=nacos"));
        referenceConfig.setScope("remote");
        GreetingsService greetingsService = referenceConfig.get();

        Assert.assertEquals("hi, Server 1", greetingsService.sayHi());

        ServiceConfig<GreetingsService> serviceConfig2 = new ServiceConfig<>();
        serviceConfig2.setInterface(GreetingsService.class);
        serviceConfig2.setRef(new GreetingsServiceImpl("Server 2"));
        serviceConfig2.setProtocol(new ProtocolConfig("dubbo2", 20882));
        serviceConfig2.setRegistry(new RegistryConfig("nacos://" + nacosAddress + ":" + nacosPort + "?enable-empty-protection=false&username=nacos&password=nacos"));
        serviceConfig2.export();

        Thread.sleep(5000);

        Set<String> results = new HashSet<>();
        for (int i = 0; i < 100; i++) {
            results.add(greetingsService.sayHi());
        }

        Assert.assertEquals(2, results.size());
        Assert.assertTrue(results.contains("hi, Server 1"));
        Assert.assertTrue(results.contains("hi, Server 2"));

        ServiceConfig<GreetingsService> serviceConfig3 = new ServiceConfig<>();
        serviceConfig3.setInterface(GreetingsService.class);
        serviceConfig3.setRef(new GreetingsServiceImpl("Server 3"));
        serviceConfig3.setProtocol(new ProtocolConfig("dubbo3", 20883));
        serviceConfig3.setRegistry(new RegistryConfig("nacos://" + nacosAddress + ":" + nacosPort + "?enable-empty-protection=false&username=nacos&password=nacos"));
        serviceConfig3.export();
        Thread.sleep(5000);

        results = new HashSet<>();
        for (int i = 0; i < 100; i++) {
            results.add(greetingsService.sayHi());
        }

        Assert.assertEquals(3, results.size());
        Assert.assertTrue(results.contains("hi, Server 1"));
        Assert.assertTrue(results.contains("hi, Server 2"));
        Assert.assertTrue(results.contains("hi, Server 3"));

        serviceConfig2.unexport();

        Thread.sleep(5000);

        results = new HashSet<>();
        for (int i = 0; i < 100; i++) {
            results.add(greetingsService.sayHi());
        }

        Assert.assertEquals(2, results.size());
        Assert.assertTrue(results.contains("hi, Server 1"));
        Assert.assertTrue(results.contains("hi, Server 3"));

        serviceConfig1.unexport();
        Thread.sleep(5000);

        Assert.assertEquals("hi, Server 3", greetingsService.sayHi());

        serviceConfig3.unexport();
        Thread.sleep(5000);

        try {
            greetingsService.sayHi();
            Assert.fail();
        } catch (Exception e) {
            Assert.assertTrue(e.getMessage().contains("No provider available"));
        }
    }
}
