package com.codeprep;

import java.net.http.HttpClient;
import java.time.Duration;

public class HttpClientExample {

    private final HttpClient httpClient = HttpClient.newBuilder()
            .connectTimeout(Duration.ofSeconds(2))
            .build();

    static void main() {

    }
}
