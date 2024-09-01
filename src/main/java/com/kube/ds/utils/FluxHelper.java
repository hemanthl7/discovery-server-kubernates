package com.kube.ds.utils;

import org.reactivestreams.Publisher;
import reactor.core.publisher.Flux;
import reactor.core.scheduler.Schedulers;

import java.util.function.Supplier;

public class FluxHelper {

    public static <T> Flux<T> runSeparately(Supplier<? extends Publisher<T>> supplier) {
        return Flux.defer(supplier)
                .subscribeOn(Schedulers.boundedElastic());
    }
}
