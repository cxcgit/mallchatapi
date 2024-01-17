package com.caixc.mallchat.common.designPatterns.decorativeMode;

/**
 * @author caixc
 * @version 1.0.0
 * @ClassName ScrollBarDecorator.java
 * @Description 滚动条装饰类，充当具体装饰类角色
 * @createTime 2024年01月17日 16:35:00
 */
public class ScrollBarDecorator extends ComponentDecorator{
    public ScrollBarDecorator(Component component) {
        super(component);
    }

    @Override
    public void display() {
        super.display();
        this.setScrollBar();
    }

    /**
     * 装饰方法
     */
    public void setScrollBar(){
        System.out.println("为构件添加滚动条！");
    }
}
