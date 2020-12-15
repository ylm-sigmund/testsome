package com.diy.sigmund.testsome;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.junit.Test;

/**
 * 【强制】线程池不允许使用 Executors 去创建，而是通过 ThreadPoolExecutor 的方式， 这样的处理方式让写的同学更加明确线程池的运行规则，规避资源耗尽的风险。
 * 
 * 说明：Executors 返回的线程池对象的弊端如下： 1） FixedThreadPool 和 SingleThreadPool： 允许的请求队列长度为 Integer.MAX_VALUE，可能会堆积大量的请求，从而导致 OOM。
 * 创建一个单线程的线程池.这个线程池只有一个线程在工程,也就是相当于单线程串行执行所有任务.如果这个唯一的线程因为异常结束,那么会有一个新的线程来替代它.此线程池保证所有任务的执行顺序都会按照提交的顺序执行.
 *
 * 2） CachedThreadPool： 允许的创建线程数量为 Integer.MAX_VALUE，可能会创建大量的线程，从而导致 OOM。
 * 
 * @author ylm-sigmund
 * @since 2020/10/10 21:13
 */
public class CreateThreadTest {

    @Test
    public void createThreadTest() {
        final ExecutorService executorService = Executors.newSingleThreadExecutor();
    }
}
