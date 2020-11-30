package com.diy.sigmund.designpatterns.decoratorpattern;

/**
 * 创建实现了 Shape 接口的抽象装饰类。
 * 
 * @author ylm-sigmund
 * @since 2020/11/30 16:40
 */
public abstract class ShapeDecorator implements Shape {
    protected Shape shapeDecorator;

    public ShapeDecorator(Shape shapeDecorator) {
        this.shapeDecorator = shapeDecorator;
    }

    @Override
    public void draw() {
        shapeDecorator.draw();
    }
}
