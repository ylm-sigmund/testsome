package com.diy.sigmund.designpatterns.decoratorpattern;

/**
 * @author ylm-sigmund
 * @since 2020/11/30 16:46
 */
public class DecoratorPatternTest {

    public static void main(String[] args) {
        Shape circle = new Circle();
        ShapeDecorator redCircle = new RedShapeDecorator(new Circle());
        ShapeDecorator redRectangle = new RedShapeDecorator(new Rectangle());
        // Shape redCircle = new RedShapeDecorator(new Circle());
        // Shape redRectangle = new RedShapeDecorator(new Rectangle());
        System.out.println("Circle with normal border");
        circle.draw();

        System.out.println("\nCircle of red border");
        redCircle.draw();

        System.out.println("\nRectangle of red border");
        redRectangle.draw();
    }
}
