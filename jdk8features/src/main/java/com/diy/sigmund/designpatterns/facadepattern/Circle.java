package com.diy.sigmund.designpatterns.facadepattern;

import org.springframework.stereotype.Service;

/**
 * 圆形
 * 
 * @author ylm-sigmund
 * @since 2020/11/30 16:05
 */
@Service
public class Circle implements Shape {

    @Override
    public void draw() {
        System.out.println("Circle::draw");
    }
}
