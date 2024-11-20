
package Game0_6;


import javax.swing.*;

public class GameStarter {
    public static void startGame() {
        int result = JOptionPane.showConfirmDialog(null, "是否开始游戏？", "确认", JOptionPane.YES_NO_OPTION);
        if (result == JOptionPane.YES_OPTION) {
            GameFrame gameFrame = new GameFrame();
            gameFrame.setVisible(true);
        } else {
            // 用户选择不开始游戏，重新显示登录窗口
            new LoginFrame();
        }
    }
}
