package com.caixc.mallchat.common.designPatterns.decorativeMode;

/**
 * @author caixc
 * @version 1.0.0
 * @ClassName BlackBorderDecorator.java
 * @Description 黑色边框装饰类，充当具体装饰类角色
 * @createTime 2024年01月17日 16:36:00
 */
public class BlackBorderDecorator extends ComponentDecorator{
    public BlackBorderDecorator(Component component) {
        super(component);
    }

    @Override
    public void display() {
        super.display();
        this.setBlackBorder();
    }

    /**
     * 装饰方法,设置黑色边框
     */
    public void setBlackBorder(){
        System.out.println("为构件添加黑色边框！");
    }
}
