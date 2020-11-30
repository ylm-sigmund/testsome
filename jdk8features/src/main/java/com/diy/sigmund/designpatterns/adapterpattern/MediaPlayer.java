package com.diy.sigmund.designpatterns.adapterpattern;

/**
 * 媒体播放器
 * 
 * @author ylm-sigmund
 * @since 2020/11/30 16:53
 */
public interface MediaPlayer {
    /**
     * 播放
     * 
     * @param audioType
     *            audioType
     * @param fileName
     *            fileName
     */
    void play(String audioType, String fileName);
}
