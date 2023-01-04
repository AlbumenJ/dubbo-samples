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

public class Provider2 {
    public static void main(String[] args) {
        ApplicationConfig applicationConfig = new ApplicationConfig("provider2");

        RegistryConfig registryConfig = new RegistryConfig(
                "nacos://" + System.getProperty("nacos.address", "127.0.0.1") + ":8848?username=nacos&password=nacos");

        ProtocolConfig protocolConfig = new ProtocolConfig("dubbo", 20882);

        ServiceConfig<DemoService2> serviceConfig2 = new ServiceConfig<>();
        serviceConfig2.setInterface(DemoService2.class);
        serviceConfig2.setRef(new DemoService2Impl());

        DubboBootstrap.getInstance()
                .application(applicationConfig)
                .registry(registryConfig)
                .protocol(protocolConfig)
                .service(serviceConfig2)
                .start()
                .await();
    }
}
