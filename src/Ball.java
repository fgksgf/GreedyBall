import java.awt.*;

public class Ball {
    // 球外接正方形左上角的坐标
    int x, y;

    // 球的运动速度
    private int xSpeed;
    private int ySpeed;

    // 球的直径
    int d;

    // 球的运动方向
    private int f;

    // 球的颜色
    Color ballColor;

    // 运动方向
    private final int MAX_SPEED = 5;
    private final int LEFT_UP = 2;
    private final int LEFT_DOWN = 1;
    private final int RIGHT_UP = 3;
    private final int RIGHT_DOWN = 0;

    // 构造函数
    Ball(int x, int y, int xSpeed, int ySpeed, int d, int f, Color ballColor) {
        this.x = x;
        this.y = y;
        this.xSpeed = xSpeed;
        this.ySpeed = ySpeed;
        this.d = d;
        this.f = f;
        this.ballColor = ballColor;
    }

    // 在画布中画球
    void paintBall(Graphics g) {
        g.setColor(ballColor);
        g.fillOval(x, y, d, d);
    }

    void paintRect(Graphics g) {
        g.setColor(ballColor);
        g.fillRect(x, y, d, d);
    }

    // 小球移动
    void moveBall() {
        switch (f) {
            case RIGHT_DOWN:
                x += xSpeed;
                y += ySpeed;
                break;
            case LEFT_DOWN:
                x -= xSpeed;
                y += ySpeed;
                break;
            case LEFT_UP:
                x -= xSpeed;
                y -= ySpeed;
                break;
            case RIGHT_UP:
                x += xSpeed;
                y -= ySpeed;
                break;
        }

        // 处理碰撞边界的情况
        if (x <= 0) { // 碰到左边
            xSpeed = (int) (Math.random() * MAX_SPEED + 1);
            ySpeed = (int) (Math.random() * MAX_SPEED + 1);
            if ((int) (Math.random() * 10 + 1) % 2 == 0) {
                f = 0;
            } else {
                f = 3;
            }
        } else if (y <= 0) { // 碰到上边
            xSpeed = (int) (Math.random() * MAX_SPEED + 1);
            ySpeed = (int) (Math.random() * MAX_SPEED + 1);
            if ((int) (Math.random() * 10 + 1) % 2 == 0) {
                f = 1;
            } else {
                f = 0;
            }
        } else if (x + d >= 1000) { // 碰到右边
            xSpeed = (int) (Math.random() * MAX_SPEED + 1);
            ySpeed = (int) (Math.random() * MAX_SPEED + 1);
            if ((int) (Math.random() * 10 + 1) % 2 == 0) {
                f = 1;
            } else {
                f = 2;
            }
        } else if (y + d >= 690) { // 碰到下边
            xSpeed = (int) (Math.random() * MAX_SPEED + 1);
            ySpeed = (int) (Math.random() * MAX_SPEED + 1);
            if ((int) (Math.random() * 10 + 1) % 2 == 0) {
                f = 2;
            } else {
                f = 3;
            }
        }
    }

    // 增加小球的直径
    void addD(int d, boolean punish) {
        if (this.d < 250 && this.d > 25) {
            if (punish) {
                if (this.d > 125) {
                    this.d -= d;
                } else {
                    this.d -= d / 10;
                }
            } else {
                this.d += d / 10;
            }
        }
        changeSpeed(d);
    }

    private void changeSpeed(int x) {
        int d = Math.abs(x);
        if (d % 2 == 0) {
            this.xSpeed += d % 4;
            this.xSpeed += d % 4;
        } else {
            if (this.xSpeed > 1) {
                xSpeed--;
            }
            if (this.ySpeed > 1) {
                ySpeed--;
            }
        }
    }

    private int getD() {
        return d;
    }

    void setBallColor(Color ballColor) {
        this.ballColor = ballColor;
    }

    // 判断小球是否吃掉到其他小球
    boolean eatBall(Ball ball) {
        boolean ret = false;
        if ((d > ball.getD()) && (Math.abs(d / 2 - ball.d / 2) > calculateDistance(ball))) {
            ret = true;
        }
        return ret;
    }

    // 计算两球心之间的圆心距离
    private double calculateDistance(Ball ball) {
        int x1 = this.x + d / 2;
        int y1 = this.y + d / 2;
        int x2 = ball.x + ball.d / 2;
        int y2 = ball.y + ball.d / 2;
        return Math.sqrt((x1 - x2) * (x1 - x2) + (y1 - y2) * (y1 - y2));
    }

    // 判断小球是否发生碰撞
    boolean collide(Ball ball) {
        boolean ret = false;
        if (d / 2 + ball.d / 2 >= calculateDistance(ball)) {
            ret = true;
        }
        return ret;
    }
}
