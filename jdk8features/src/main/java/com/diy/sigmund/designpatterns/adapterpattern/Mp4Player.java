package com.diy.sigmund.designpatterns.adapterpattern;

/**
 * @author ylm-sigmund
 * @since 2020/11/30 16:56
 */
public class Mp4Player implements AdvancedMediaPlayer {
    @Override
    public void playVlc(String fileName) {
        // 什么也不做
    }

    @Override
    public void playMp4(String fileName) {
        System.out.println("Playing mp4 file. Name: " + fileName);
    }
}
