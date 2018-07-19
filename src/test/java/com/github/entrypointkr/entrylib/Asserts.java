package com.github.entrypointkr.entrylib;

import org.junit.Assert;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

/**
 * Created by JunHyeong Lim on 2018-05-27
 */
public class Asserts {
    public static void await(CountDownLatch latch) throws InterruptedException {
        if (!latch.await(3, TimeUnit.SECONDS)) {
            Assert.fail();
        }
    }

    public static <T> Answer<T> createVoidAnswer(Consumer<InvocationOnMock> consumer) {
        return invocation -> {
            consumer.accept(invocation);
            return null;
        };
    }
}
