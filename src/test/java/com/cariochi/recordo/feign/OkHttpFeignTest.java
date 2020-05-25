package com.cariochi.recordo.feign;

import com.cariochi.recordo.AbstractTest;
import com.cariochi.recordo.RecordoTestsApplication;
import com.cariochi.recordo.annotation.EnableHttpMocks;
import feign.Client;
import lombok.extern.slf4j.Slf4j;
import okhttp3.OkHttpClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@SpringBootTest(
        classes = {RecordoTestsApplication.class, OkHttpFeignTest.Config.class},
        properties = "feign.okhttp.enabled=true"
)
class OkHttpFeignTest extends AbstractTest {

    @Autowired
    @EnableHttpMocks
    private OkHttpClient client;

    @Configuration
    @ConditionalOnProperty("feign.okhttp.enabled")
    @EnableFeignClients
    public static class Config {

        @Bean
        public OkHttpClient client() {
            return new OkHttpClient();
        }

        @Bean
        public Client feignClient() {
            return new feign.okhttp.OkHttpClient(client());
        }
    }

}
