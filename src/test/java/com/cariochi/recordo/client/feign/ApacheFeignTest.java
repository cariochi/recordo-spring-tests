package com.cariochi.recordo.client.feign;

import com.cariochi.recordo.RecordoTestsApplication;
import com.cariochi.recordo.annotation.EnableRecordo;
import com.cariochi.recordo.annotation.Resources;
import com.cariochi.recordo.client.AbstractTest;
import feign.Client;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@SpringBootTest(
        classes = {RecordoTestsApplication.class, ApacheFeignTest.Config.class},
        properties = "feign.httpclient.enabled=true"
)
@Resources("/client/feign-apache")
class ApacheFeignTest extends AbstractTest {

    @Autowired
    @EnableRecordo
    private HttpClient httpClient;

    @Configuration
    @ConditionalOnProperty("feign.httpclient.enabled")
    @EnableFeignClients
    public static class Config {

        @Bean
        public CloseableHttpClient httpClient() {
            return HttpClients.createDefault();
        }

        @Bean
        public Client feignClient() {
            return new feign.httpclient.ApacheHttpClient(httpClient());
        }
    }
}
