package com.diy.sigmund.designpatterns.facadepattern;

import org.springframework.stereotype.Service;

/**
 * 长方形
 * 
 * @author ylm-sigmund
 * @since 2020/11/30 16:07
 */
@Service
public class Rectangle implements Shape {
    @Override
    public void draw() {
        System.out.println("Rectangle::draw");
    }
}
