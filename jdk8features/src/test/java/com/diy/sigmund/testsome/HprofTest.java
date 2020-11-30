package com.diy.sigmund.testsome;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author ylm-sigmund
 * @since 2020/10/13 22:25
 */
public class HprofTest {

    /**
     * C:\Users\ylm>jps 9696 10004 RemoteMavenServer 13832 Jps 21144 HprofTest 1260 Launcher
     *
     * F:\project\knight\github\testsome>jmap -dump:file=test.hprof,format=b 21144 Dumping heap to
     * F:\project\knight\github\testsome\test.hprof ... Heap dump file created
     *
     * @param args
     *            args
     * @throws InterruptedException
     *             InterruptedException
     */
    public static void main(String[] args) throws InterruptedException {
        List<Integer> ids = new ArrayList<>();

        TimeUnit.MINUTES.sleep(10);
    }
}
