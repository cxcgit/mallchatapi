package com.caixc.mallchat.common.designPatterns.decorativeMode;

/**
 * @author caixc
 * @version 1.0.0
 * @ClassName ComponentDecorator.java
 * @Description 构件装饰类，充当抽象装饰类角色
 * @createTime 2024年01月17日 16:34:00
 */
public abstract class ComponentDecorator implements Component{
    private final Component component;

    public ComponentDecorator(Component component) {
        this.component = component;
    }

    /**
     * 仅调用抽象构建类的display方法
     */
    @Override
    public void display() {
        component.display();
    }
}
