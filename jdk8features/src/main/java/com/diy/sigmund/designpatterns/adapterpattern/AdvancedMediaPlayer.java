package com.diy.sigmund.designpatterns.adapterpattern;

/**
 * 高级的媒体播放器
 * 
 * @author ylm-sigmund
 * @since 2020/11/30 16:54
 */
public interface AdvancedMediaPlayer {
    void playVlc(String fileName);

    void playMp4(String fileName);
}
