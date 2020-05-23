package com.cariochi.recordo;

import feign.RequestInterceptor;
import feign.RequestTemplate;

public class GitHubInterceptor implements RequestInterceptor {

    public static final String KEY = "4e5986483be7bb55cd7d00d24fb45bd7e4ac3054";

    @Override
    public void apply(RequestTemplate requestTemplate) {
        requestTemplate
                .header("Authorization", "token " + KEY);
    }
}
