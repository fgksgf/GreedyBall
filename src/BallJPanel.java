import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class BallJPanel extends JPanel implements MouseListener, MouseMotionListener {
    // 玩家控制的小球
    private Ball player;
    // 游戏难度
    private int level;
    // 玩家得分
    private int score1 = 0;
    // AI得分
    private int score2 = 0;

    // 游戏是否结束
    private boolean over = false;
    // 游戏是否开始
    private boolean begin = false;
    // 玩家是否胜利
    private boolean win = false;
    // 补充静止小球次数
    private int addCount = 3;

    // 初始静止小球和动球总数
    private int miniCount = 600;
    private int middleCount = 2;

    // 受到惩罚（扣分并减小直径）的概率为1/PUNISH_PROB
    private final int PUNISH_PROB = 3;
    // 存储所有球对象
    private List<Ball> miniBalls = new ArrayList<>();
    private List<Ball> middleBalls = new ArrayList<>();

    BallJPanel(int level) {
        this.level = level;

        switch (level) {
            case 1:
                middleCount = 2;
                addCount = 1;
                break;

            case 2:
                middleCount = 3;
                addCount = 2;
                break;

            case 3:
                middleCount = 4;
                addCount = 3;
                break;
        }
        // 初始化玩家控制的球
        player = new Ball(0, 0, 1, 1, 50, 0, Color.BLACK);

        // 产生小球
        for (int i = 0; i < miniCount; i++) {
            addMiniBall();
        }

        // 产生动球
        for (int i = 0; i < middleCount; i++) {
            addMiddleBall();
        }

        // 监听鼠标事件
        addMouseListener(this);
        addMouseMotionListener(this);
    }

    public void paint(Graphics g) {
        super.paint(g);
        // 画小球
        for (int i = 0; i < miniBalls.size(); i++) {
            miniBalls.get(i).paintBall(g);
        }

        // 画动球
        for (int i = 0; i < middleBalls.size(); i++) {
            middleBalls.get(i).paintRect(g);
        }

        // 画玩家的球
        if (begin) {
            player.paintBall(g);
        }

        // 显示游戏结束提示
        if (over && !win) {
            g.setColor(Color.RED);
            g.setFont(new Font("黑体", Font.BOLD, 100));
            g.drawString("GAME OVER", 250, 350);
        }

        // 显示游戏胜利提示
        if (win) {
            g.setColor(Color.RED);
            g.setFont(new Font("黑体", Font.BOLD, 70));
            g.drawString("Congratulations !!!", 170, 300);
        }

        // 显示得分
        g.setColor(Color.BLACK);
        g.setFont(new Font("黑体", Font.BOLD, 40));
        g.drawString(GreedyBall.def.getString("you") + ": " + score1, 730, 50);
        g.drawString(GreedyBall.def.getString("com") + ": " + score2, 730, 100);
    }

    // 产生静止小球
    private void addMiniBall() {
        int x = (int) (Math.random() * 900);
        int y = (int) (Math.random() * 650);
        int d = (int) (Math.random() * 40 + 10);
        int f = 0;
        int xSpeed = 0;
        int ySpeed = 0;
        int r = (int) (Math.random() * 256);
        int g = (int) (Math.random() * 256);
        int b = (int) (Math.random() * 256);
        miniBalls.add(new Ball(x, y, xSpeed, ySpeed, d, f, new Color(r, g, b)));
    }

    // 产生动球
    private void addMiddleBall() {
        int x, y;
        // 防止生成的动球与玩家控制的小球在同一位置
        do {
            x = (int) (Math.random() * 900);
        } while (x == player.x);

        do {
            y = (int) (Math.random() * 650);
        } while (y == player.y);

        int d = (int) (Math.random() * 60 + 60);
        int f = (int) (Math.random() * 4);
        int xSpeed = (int) (Math.random() * 5 + level);
        int ySpeed = (int) (Math.random() * 5 + level);
        int r = (int) (Math.random() * 256);
        int g = (int) (Math.random() * 256);
        int b = (int) (Math.random() * 256);
        middleBalls.add(new Ball(x, y, xSpeed, ySpeed, d, f, new Color(r, g, b)));
    }

    private void middleBallAction() {
        for (int i = 0; i < middleBalls.size(); ++i) {
            Ball b1 = middleBalls.get(i);

            // 判断动球是否碰到比自己直径小的玩家球
            if (b1.collide(player) && b1.d >= player.d) {
                over = true;
                begin = false;
                repaint();
                break;
            }

            // 判断玩家球是否吃掉动球
            if (player.eatBall(b1)) {
                // 玩家吃掉动球，加分
                score1 += b1.d / 5;
                // 增加玩家球的直径
                player.addD(b1.d, false);
                // 从列表中删除被吃的球
                middleBalls.remove(i);
                break;
            }

            for (int j = 0; j < miniBalls.size(); j++) {
                Ball b2 = miniBalls.get(j);

                // 判断动球是否吃小球
                if (b1.eatBall(b2)) {
                    boolean punish = (int) (Math.random() * 100) % PUNISH_PROB == 0;
                    b1.addD(b2.d, punish);
                    b1.setBallColor(b2.ballColor);
                    miniBalls.remove(j);
                    if (punish) {
                        score2 -= b2.d / 5;
                    } else {
                        score2 += b2.d / 7;
                    }
                    break;
                }
            }
        }
    }

    private void miniBallAction() {
        for (int i = 0; i < miniBalls.size(); i++) {
            Ball ball = miniBalls.get(i);

            // 玩家球吃小球
            if (player.eatBall(ball)) {
                boolean punish = (int) (Math.random() * 100) % PUNISH_PROB == 0;
                player.addD(ball.d, punish);
                if (punish) {
                    score1 -= ball.d / 6;
                } else {
                    score1 += ball.d / 6;
                }
                miniBalls.remove(i);
                break;
            }
        }

        // 当小球数量不足时，增加一次小球
        if (addCount > 0 && miniBalls.size() < 100) {
            for (int i = 0; i < 300; i++) {
                addMiniBall();
            }
            addCount--;
        }
    }

    void startGame() {
        new Thread(() -> {
            while (!over) {

                // 当所有小球或动球都被吃完时
                if (miniBalls.size() == 0 || middleBalls.size() == 0) {
                    // 如果玩家得分低于电脑得分,则玩家输
                    win = score1 >= score2;
                    begin = false;
                    over = true;
                    repaint();
                } else {
                    // 动球移动
                    for (Ball b : middleBalls) {
                        b.moveBall();
                    }

                    if (begin) {
                        middleBallAction();
                        miniBallAction();
                    }
                }

                // 重绘画面
                repaint();

                // 休眠
                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        switch (e.getButton()) {
            case MouseEvent.BUTTON1:
                if (!begin && !over) {
                    begin = true;
                    // 设置空光标以达到隐藏鼠标的效果
                    URL classUrl = this.getClass().getResource("");
                    Image imageCursor = Toolkit.getDefaultToolkit().getImage(classUrl);
                    setCursor(Toolkit.getDefaultToolkit().createCustomCursor(imageCursor, new Point(0, 0), "cursor"));
                }
                break;
        }
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        // 将鼠标所在处坐标设置为玩家球的坐标
        player.x = e.getX() - player.d / 2;
        player.y = e.getY() - player.d / 2;
    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

    @Override
    public void mouseDragged(MouseEvent e) {

    }
}
