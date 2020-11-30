package com.diy.sigmund.designpatterns.adapterpattern;

/**
 * @author ylm-sigmund
 * @since 2020/11/30 16:55
 */
public class VlcPlayer implements AdvancedMediaPlayer {
    @Override
    public void playVlc(String fileName) {
        System.out.println("Playing vlc file. Name: " + fileName);
    }

    @Override
    public void playMp4(String fileName) {
        // 什么也不做
    }
}
