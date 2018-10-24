import javax.swing.*;
import java.awt.*;
import java.util.ResourceBundle;

public class GreedyBall extends JFrame {
    private Font font = new Font("黑体", Font.PLAIN, 23);
    public static final ResourceBundle def = ResourceBundle.getBundle("content");

    // 获取电脑屏幕的宽度和高度
    private int pw = Toolkit.getDefaultToolkit().getScreenSize().width;
    private int ph = Toolkit.getDefaultToolkit().getScreenSize().height;

    // 主窗体大小
    private final int width = 1000;
    private final int height = 740;

    private int level = 1;

    private JRadioButtonMenuItem[] levelItems = new JRadioButtonMenuItem[3];
    private String[] levelName = {
            def.getString("newbie"),
            def.getString("midbie"),
            def.getString("highbie")};

    private BallJPanel panel;

    private GreedyBall() {
        setTitle(def.getString("title"));
        setBounds((pw - width) / 2, (ph - height) / 2, width, height);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setResizable(false);
        initBar();

        panel = new BallJPanel(level);
        panel.startGame();
        this.add(panel);

        setVisible(true);
    }


    // 开始一个新游戏
    private void newGame() {
        this.remove(panel);
        panel = new BallJPanel(level);
        panel.startGame();
        this.add(panel);
        this.setVisible(true);
    }

    // 初始化菜单栏
    private void initBar() {
        // 创建游戏菜单并设置字体
        JMenu gameMenu = new JMenu(def.getString("game"));
        gameMenu.setFont(font);

        // 创建菜单选项new并添加响应动作
        JMenuItem newItem = new JMenuItem(def.getString("new"));
        newItem.setFont(font);
        gameMenu.add(newItem);
        newItem.addActionListener(
                e -> newGame()
        );

        gameMenu.addSeparator();
        for (int i = 0; i < levelItems.length; i++) {
            levelItems[i] = new JRadioButtonMenuItem(levelName[i]);
            levelItems[i].setFont(font);
            gameMenu.add(levelItems[i]);
        }

        // 初始默认难度为初级
        levelItems[0].setSelected(true);

        levelItems[0].addActionListener(
                e -> {
                    // 设置为初级难度
                    levelItems[0].setSelected(true);
                    levelItems[1].setSelected(false);
                    levelItems[2].setSelected(false);
                    level = 1;
                    newGame();
                }
        );

        levelItems[1].addActionListener(
                e -> {
                    // 设置为中级难度
                    levelItems[1].setSelected(true);
                    levelItems[0].setSelected(false);
                    levelItems[2].setSelected(false);
                    level = 2;
                    newGame();
                }
        );

        levelItems[2].addActionListener(
                e -> {
                    // 设置为高级难度
                    levelItems[2].setSelected(true);
                    levelItems[0].setSelected(false);
                    levelItems[1].setSelected(false);
                    level = 3;
                    newGame();
                }
        );

        gameMenu.addSeparator();

        // 创建菜单选项about并添加响应动作
        JMenuItem aboutItem = new JMenuItem(def.getString("about"));
        aboutItem.setFont(font);
        gameMenu.add(aboutItem);
        aboutItem.addActionListener(
                e -> JOptionPane.showMessageDialog(this,
                        "<html><h1>" + def.getString("title") + "</h1><h2>"
                                + def.getString("version") + "：V 1.0</h2></html>",
                        def.getString("about"), JOptionPane.PLAIN_MESSAGE)
        );

        // 创建菜单选项exit并添加响应动作
        JMenuItem exitItem = new JMenuItem(def.getString("exit"));
        exitItem.setFont(font);
        gameMenu.add(exitItem);
        exitItem.addActionListener(
                e -> System.exit(0)
        );

        // 创建工具栏
        JMenuBar bar = new JMenuBar();
        bar.add(gameMenu);
        this.setJMenuBar(bar);
    }

    public static void main(String[] args) {
        new GreedyBall();
    }
}
