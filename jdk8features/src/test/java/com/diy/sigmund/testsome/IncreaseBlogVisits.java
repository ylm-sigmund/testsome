package com.diy.sigmund.testsome;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * https://blog.csdn.net/sinat_42483341/article/details/96134556
 */
public class IncreaseBlogVisits {
    private static final int maxPages = 5; // 填写你的博客查找页数
    /**
     * 此处需要修改自己的博客域名
     */
    private static final String domain = "https://blog.csdn.net/qq_41907418/";
    private static final String homeUrl = domain + "article/list/";
    private static Set<String> urlSet = new HashSet<>();

    private static void getUrls() throws IOException, InterruptedException {
        InputStream is;
        String pageStr;
        StringBuilder curUrl;
        for (int i = 1; i < maxPages; i++) {
            Thread.sleep(500);
            System.out.println("正在查找第 " + i + " 页中的博客地址");
            curUrl = new StringBuilder(homeUrl);
            curUrl.append(i);
            System.out.println(curUrl);
            is = doGet(curUrl.toString());
            pageStr = inputStreamToString(is, "UTF-8");// 一整页的html源码

            List<String> list = getMatherSubstrs(pageStr, "(?<=href=\")" + domain + "article/details/[0-9]{8,9}(?=\")");
            urlSet.addAll(list);
            System.out.println("加入 " + list.size() + " 个url");
        }
    }

    public static void main(String[] urlstr) throws IOException, InterruptedException {
        // ----------------------------------------------遍历每一页 获取文章链接----------------------------------------------
        getUrls();

        // ---------------------------------------------------打印每个链接---------------------------------------------------
        System.out.println("打印每个链接");
        for (String s : urlSet) {
            System.out.println(s);
        }
        System.out.println("打印每个链接完毕");

        // ---------------------------------------------------多线程访问每个链接---------------------------------------------------
        ExecutorService executor = Executors.newCachedThreadPool();
        int threadCount = 5; // 并发线程数量
        for (int i = 0; i < threadCount; i++) {
            executor.execute(new MyThread(urlSet));
        }
        executor.shutdown();
    }

    private static InputStream doGet(String urlstr) throws IOException {
        URL url = new URL(urlstr);
        HttpURLConnection conn = (HttpURLConnection)url.openConnection();
        conn.setRequestProperty("User-Agent",
            "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/56.0.2924.87 Safari/537.36");
        InputStream inputStream = conn.getInputStream();
        return inputStream;
    }

    private static String inputStreamToString(InputStream is, String charset) throws IOException {
        byte[] bytes = new byte[1024];
        int byteLength = 0;
        StringBuilder sb = new StringBuilder();
        while ((byteLength = is.read(bytes)) != -1) {
            sb.append(new String(bytes, 0, byteLength, charset));
        }
        return sb.toString();
    }

    // 正则匹配
    private static List<String> getMatherSubstrs(String str, String regex) {
        List<String> list = new ArrayList<>();
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(str);
        while (m.find()) {
            list.add(m.group());
        }
        return list;
    }
}

class MyThread implements Runnable {
    private List<String> urlList;

    MyThread(Set<String> urls) {
        List<String> list = new ArrayList<>(urls);
        Collections.shuffle(list);
        this.urlList = list;
    }

    @Override
    public void run() {
        int i = 0;
        for (String s : urlList) {
            try {
                doGet(s);
                System.out
                    .println(Thread.currentThread().getName() + "成功访问第" + (++i) + "个链接,共" + urlList.size() + "个:" + s);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private static InputStream doGet(String urlstr) throws IOException {
        URL url = new URL(urlstr);
        HttpURLConnection conn = (HttpURLConnection)url.openConnection();
        conn.setRequestProperty("User-Agent",
            "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/56.0.2924.87 Safari/537.36");
        InputStream inputStream = conn.getInputStream();
        return inputStream;
    }
}
