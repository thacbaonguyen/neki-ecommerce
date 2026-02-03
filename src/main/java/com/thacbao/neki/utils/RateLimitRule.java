package com.thacbao.neki.utils;

import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.BucketConfiguration;
import lombok.Getter;
import lombok.Setter;

import java.time.Duration;
@Getter
@Setter
public class RateLimitRule {

    int capacity;
    Duration duration;
    String prefix;

    public RateLimitRule(int capacity, Duration duration, String prefix) {
        this.capacity = capacity;
        this.duration = duration;
        this.prefix = prefix;
    }

    public BucketConfiguration bucketConfig() {
        return BucketConfiguration.builder()
                .addLimit(Bandwidth.simple(capacity, duration))
                .build();
    }
}
