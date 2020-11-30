package com.diy.sigmund.designpatterns.facadepattern;

/**
 * @author ylm-sigmund
 * @since 2020/11/30 16:14
 */
public class FacadePatternTest {

    /**
     * 不使用@service注解完成
     * 
     * @param args
     *            args
     */
    public static void main(String[] args) {
        final ShapeMaker shapeMaker = new ShapeMaker();
        shapeMaker.drawCircle();
        shapeMaker.drawRectangle();
        shapeMaker.drawSquare();
    }
}
