package com.diy.sigmund.chainofresponsibility;

import java.util.concurrent.LinkedBlockingQueue;

/**
 * @author ylm-sigmund
 * @since 2020/9/27 21:42
 */
public class PrevProcessor extends Thread implements IRequestProcessor {
    LinkedBlockingQueue<Request> requests = new LinkedBlockingQueue<>();

    private IRequestProcessor nextProcessor;

    public PrevProcessor() {

    }

    public PrevProcessor(IRequestProcessor nextProcessor) {
        this.nextProcessor = nextProcessor;
    }

    private volatile boolean finish = false;

    /**
     * 对外提供关闭的方法
     */
    public void shutdown() {
        this.finish = true;
    }

    @Override
    public void process(Request request) {
        // TODO 按实际需求做处理
        requests.add(request);// 生产者
    }

    @Override
    public void run() {
        while (!finish) {
            try {
                // 阻塞式获取数据
                final Request request = requests.take();
                // 真正的处理逻辑
                System.out.println("PrevProcessor" + request);
                // 交给下一个责任链去做
                nextProcessor.process(request);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
