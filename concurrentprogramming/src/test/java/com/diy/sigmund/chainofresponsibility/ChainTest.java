package com.diy.sigmund.chainofresponsibility;

import org.junit.Before;
import org.junit.Test;

/**
 * @author ylm-sigmund
 * @since 2020/9/27 21:58
 */
public class ChainTest {
    static IRequestProcessor requestProcessor;

    @Before
    public void setUp() throws Exception {
        final SaveProcessor saveProcessor = new SaveProcessor();
        saveProcessor.start();
        final PrintProcessor printProcessor = new PrintProcessor(saveProcessor);
        printProcessor.start();
        requestProcessor = new PrevProcessor(printProcessor);
        ((PrevProcessor)requestProcessor).start();
    }

    @Test
    public void test1() throws Exception {
        final Request request = new Request();
        request.setName("ylm-sigmund");
        requestProcessor.process(request);
    }
}
