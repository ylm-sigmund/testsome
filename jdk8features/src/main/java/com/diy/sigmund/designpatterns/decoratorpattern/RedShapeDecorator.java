package com.diy.sigmund.designpatterns.decoratorpattern;

/**
 * 创建扩展了 ShapeDecorator 类的实体装饰类。
 * 
 * @author ylm-sigmund
 * @since 2020/11/30 16:43
 */
public class RedShapeDecorator extends ShapeDecorator {
    public RedShapeDecorator(Shape shapeDecorator) {
        super(shapeDecorator);
    }

    @Override
    public void draw() {
        super.draw();
        setRedBorder(shapeDecorator);
    }

    /**
     * 给与边框颜色
     * 
     * @param shapeDecorator
     *            shapeDecorator
     */
    private void setRedBorder(Shape shapeDecorator) {
        System.out.println("Border Color: Red");
    }
}
