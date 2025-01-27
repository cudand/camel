/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.camel.component.aws.secretsmanager.integration;

import org.apache.camel.EndpointInject;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.aws.secretsmanager.SecretsManagerConstants;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.test.junit5.CamelTestSupport;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import software.amazon.awssdk.services.secretsmanager.model.RotateSecretResponse;

@Disabled("This test must be manually started, you need to specify AWS Credentials")
public class SecretsManagerRotateSecretProducerIntegrationTest extends CamelTestSupport {

    @EndpointInject("mock:result")
    private MockEndpoint mock;

    @Test
    public void translateTextTest() throws Exception {

        mock.expectedMessageCount(1);
        Exchange exchange = template.request("direct:rotateSecret", new Processor() {
            @Override
            public void process(Exchange exchange) throws Exception {
                exchange.getIn().setHeader(SecretsManagerConstants.SECRET_ID, "TestSecret6");
            }
        });

        RotateSecretResponse resultRotate = (RotateSecretResponse) exchange.getIn().getBody();
        System.err.println(resultRotate.toString());
    }

    @Override
    protected RouteBuilder createRouteBuilder() throws Exception {
        return new RouteBuilder() {
            @Override
            public void configure() throws Exception {
                from("direct:rotateSecret")
                        .to("aws-secrets-manager://test?accessKey=RAW(xxxx)&secretKey=RAW(yyyy)&region=eu-west-1&operation=rotateSecret")
                        .to("mock:result");
            }
        };
    }
}
