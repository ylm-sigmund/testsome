package com.diy.sigmund.chainofresponsibility;

import java.util.concurrent.LinkedBlockingQueue;

/**
 * @author ylm-sigmund
 * @since 2020/9/27 21:55
 */
public class PrintProcessor extends Thread implements IRequestProcessor {
    LinkedBlockingQueue<Request> requests = new LinkedBlockingQueue<>();

    private IRequestProcessor nextProcessor;
    private volatile boolean finish = false;

    public PrintProcessor() {

    }

    public PrintProcessor(IRequestProcessor nextProcessor) {
        this.nextProcessor = nextProcessor;
    }

    public void shutdown() {
        this.finish = true;
    }

    @Override
    public void process(Request request) {
        requests.add(request);// 生产者
    }

    @Override
    public void run() {
        while (!finish) {
            try {
                // 阻塞式获取数据
                final Request request = requests.take();
                // 真正的处理逻辑
                System.out.println("PrintProcessor" + request);
                // 交给下一个责任链去做
                nextProcessor.process(request);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
