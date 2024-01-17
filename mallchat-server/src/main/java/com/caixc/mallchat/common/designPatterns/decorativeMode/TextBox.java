package com.caixc.mallchat.common.designPatterns.decorativeMode;

/**
 * @author caixc
 * @version 1.0.0
 * @ClassName Decorator.java
 * @Description 文本框类，充当具体构件类角色
 * @createTime 2024年01月17日 15:53:00
 */
public class TextBox implements Component{
    @Override
    public void display() {
        System.out.println("显示窗体！");
    }
}
