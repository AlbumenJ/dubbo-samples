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
package org.apache.dubbo.samples;

import org.apache.dubbo.config.ApplicationConfig;
import org.apache.dubbo.config.ProtocolConfig;
import org.apache.dubbo.config.ReferenceConfig;
import org.apache.dubbo.config.RegistryConfig;
import org.apache.dubbo.config.ServiceConfig;
import org.apache.dubbo.config.bootstrap.DubboBootstrap;
import org.apache.dubbo.samples.api.DemoService1;
import org.apache.dubbo.samples.api.DemoService2;
import org.apache.dubbo.samples.impl.DemoService1Impl;
import org.apache.dubbo.samples.impl.DemoService2Impl;

public class Provider1 {
    public static void main(String[] args) {
        ApplicationConfig applicationConfig = new ApplicationConfig("provider1");

        RegistryConfig registryConfig = new RegistryConfig(
                "nacos://" + System.getProperty("nacos.address", "127.0.0.1") + ":8848?username=nacos&password=nacos");

        ProtocolConfig protocolConfig = new ProtocolConfig("dubbo", 20881);

        ServiceConfig<DemoService1> serviceConfig1 = new ServiceConfig<>();
        serviceConfig1.setInterface(DemoService1.class);
        DemoService1Impl demoService1 = new DemoService1Impl();
        serviceConfig1.setRef(demoService1);

        ServiceConfig<DemoService2> serviceConfig2 = new ServiceConfig<>();
        serviceConfig2.setInterface(DemoService2.class);
        serviceConfig2.setRef(new DemoService2Impl());
        serviceConfig2.setRegister(false);

        ReferenceConfig<DemoService2> referenceConfigLocal = new ReferenceConfig<>();
        referenceConfigLocal.setInterface(DemoService2.class);
        referenceConfigLocal.setScope("local");

        ReferenceConfig<DemoService2> referenceConfigRemote = new ReferenceConfig<>();
        referenceConfigRemote.setInterface(DemoService2.class);
        referenceConfigRemote.setScope("remote");

        DubboBootstrap dubboBootstrap = DubboBootstrap.getInstance();

        dubboBootstrap
                .application(applicationConfig)
                .registry(registryConfig)
                .protocol(protocolConfig)
                .service(serviceConfig1)
                .service(serviceConfig2)
                .reference(referenceConfigLocal)
                .reference(referenceConfigRemote)
                .start();

        demoService1.setDemoServiceLocal(referenceConfigLocal.get());
        demoService1.setDemoServiceRemote(referenceConfigRemote.get());

        dubboBootstrap.await();
    }
}
