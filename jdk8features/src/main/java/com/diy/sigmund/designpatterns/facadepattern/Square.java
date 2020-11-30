package com.diy.sigmund.designpatterns.facadepattern;

import org.springframework.stereotype.Service;

/**
 * 正方形
 * 
 * @author ylm-sigmund
 * @since 2020/11/30 16:08
 */
@Service
public class Square implements Shape {
    @Override
    public void draw() {
        System.out.println("Square::draw");
    }
}
