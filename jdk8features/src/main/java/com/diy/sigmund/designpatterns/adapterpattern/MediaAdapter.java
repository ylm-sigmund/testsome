package com.diy.sigmund.designpatterns.adapterpattern;

/**
 * @author ylm-sigmund
 * @since 2020/11/30 16:56
 */
public class MediaAdapter implements MediaPlayer {
    /**
     * 当一个类、属性、方法没有定义访问修饰符，默认为friendly，也叫做包访问修饰符，在同一包下面的任何类都可以访问，包外部(其它包)不能访问。
     *
     * 子类无法访问，protected的子类是可以访问的
     */
    AdvancedMediaPlayer advancedMediaPlayer;

    public MediaAdapter(String audioType) {
        if ("vlc".equalsIgnoreCase(audioType)) {
            advancedMediaPlayer = new VlcPlayer();
        } else if ("mp4".equalsIgnoreCase(audioType)) {
            advancedMediaPlayer = new Mp4Player();
        }
    }

    @Override
    public void play(String audioType, String fileName) {
        if ("vlc".equalsIgnoreCase(audioType)) {
            advancedMediaPlayer.playVlc(fileName);
        } else if ("mp4".equalsIgnoreCase(audioType)) {
            advancedMediaPlayer.playMp4(fileName);
        }
    }
}
