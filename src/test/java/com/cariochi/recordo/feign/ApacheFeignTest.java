package com.cariochi.recordo.feign;

import com.cariochi.recordo.annotation.EnableHttpMocks;
import com.cariochi.recordo.RecordoTestsApplication;
import feign.Client;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@SpringBootTest(
        classes = {RecordoTestsApplication.class, ApacheFeignTest.Config.class},
        properties = "feign.httpclient.enabled=true"
)
class ApacheFeignTest extends FeignTest {

    @Autowired
    @EnableHttpMocks
    private CloseableHttpClient httpClient;

    @Configuration
    @ConditionalOnProperty("feign.httpclient.enabled")
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
