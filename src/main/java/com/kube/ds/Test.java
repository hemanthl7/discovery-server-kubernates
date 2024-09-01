package com.kube.ds;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Scheduler;
import reactor.core.scheduler.Schedulers;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

public class Test {

    public static void main(String[] args) {

       Flux.merge(hi(), hello())
               .subscribeOn(Schedulers.boundedElastic())
               .subscribe(System.out::println);
       while(true){

       }
    }

    public static Flux<Integer> hi() {
        return Flux.defer(()->Flux.just(1))
                .delayElements(Duration.ofSeconds(1));
    }

    public static Flux<Integer> hello() {
        return Flux.defer(()->Flux.just(2));
    }
}
