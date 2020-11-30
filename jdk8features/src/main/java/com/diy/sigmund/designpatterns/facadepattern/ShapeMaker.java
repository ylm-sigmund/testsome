package com.diy.sigmund.designpatterns.facadepattern;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

/**
 * @author ylm-sigmund
 * @since 2020/11/30 16:10
 */
@Service
public class ShapeMaker {
    @Resource(name = "circle")
    private Shape circle;
    @Resource(name = "rectangle")
    private Shape rectangle;
    @Resource(name = "square")
    private Shape square;

    public ShapeMaker() {
        this.circle = new Circle();
        this.rectangle = new Rectangle();
        this.square = new Square();
    }

    public void drawCircle() {
        circle.draw();
    }

    public void drawRectangle() {
        rectangle.draw();
    }

    public void drawSquare() {
        square.draw();
    }
}
