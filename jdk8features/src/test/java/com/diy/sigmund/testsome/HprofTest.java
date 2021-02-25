package com.diy.sigmund.testsome;

import com.diy.sigmund.entity.User;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author ylm-sigmund
 * @since 2020/10/13 22:25
 */
public class HprofTest {

    /**
     * C:\Users\ylm>jps
     * 9696
     * 10004 RemoteMavenServer
     * 13832 Jps
     * 21144 HprofTest
     * 1260 Launcher
     * <p>
     * F:\project\knight\github\testsome>jmap -dump:file=test.hprof,format=b 21144
     * Dumping heap to F:\project\knight\github\testsome\test.hprof ...
     * Heap dump file created
     *
     * VM options:-Xmx10m -XX:+HeapDumpOnOutOfMemoryError
     *
     * @param args args
     * @throws InterruptedException InterruptedException
     */
    public static void main(String[] args) throws InterruptedException {
//        List<Integer> ids = new ArrayList<>();
//        for (int i = 0; i < 1230000; i++) {
//            ids.add(123456789);
//        }

        List<User> users =new ArrayList<>();
        for (int i = 0; i < 12340000; i++) {
            final User user = new User();
            user.setId(i);
            user.setName(i+"");
            user.setDate(new Date());
            users.add(user);
        }
        TimeUnit.MINUTES.sleep(10);
    }
}
