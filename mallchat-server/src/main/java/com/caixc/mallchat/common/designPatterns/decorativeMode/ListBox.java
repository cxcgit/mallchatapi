package com.caixc.mallchat.common.designPatterns.decorativeMode;

/**
 * @author caixc
 * @version 1.0.0
 * @ClassName ListBox.java
 * @Description 列表框类，充当具体构件类角色
 * @createTime 2024年01月17日 15:54:00
 */
public class ListBox  implements Component {
    @Override
    public void display() {
        System.out.println("显示列表框！");
    }
}
