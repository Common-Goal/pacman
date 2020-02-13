import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.LinkedList;

public class FallingBall extends JFrame {
    private static Thread animate;
    private static LinkedList<Point> carrierInfo;
    private static int firstX, firstY;
    private final static int carrierWidth = 80;
    private final static int carrierRange = 60;
    private final static int carrierHeight = 10;
    private static int stepCount = 0;
    private static int width;
    private static int height;
    private static Ball ball;
    private static final int radius = 15;
    private static boolean gameOver = false;

    private FallingBall() {
        setTitle("FALLING BALL");
        GamePanel game = new GamePanel();
        setContentPane(game);
        setSize(new Dimension(400, 500));
        setResizable(false);
        setVisible(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        requestFocusInWindow();

        carrierInfo = new LinkedList<>();
        animate = new Thread(() -> {
            while (!gameOver) {
                try {
                    Thread.sleep(50);
                    game.repaint();
                } catch (InterruptedException e) {
                    return;
                }

            }
        });
        animate.setDaemon(true);
    }

    private static void updateCarrier() {
        stepCount++;
        if (carrierInfo.isEmpty()) {  // when the game starts
            carrierInfo.add(new Point(firstX, firstY));
            ball = new Ball(firstX + (carrierWidth / 2) + radius, firstY - (2 * radius) - 2 * carrierRange);
        } else {
            if (stepCount == carrierRange) {
                stepCount = 0;    // reset the counter
                addCarrier();
            }
            for (Point i : carrierInfo)
                i.y -= 1;

        }
        if (carrierInfo.getFirst().y < -carrierHeight)
            carrierInfo.removeFirst();

    }

    private static class Ball {
        int centerX, centerY;


        Ball(int x, int y) {
            centerX = x;
            centerY = y;

        }


    }

    private static void moveLeft() {
        int i = ball.centerX;
        if ((i + radius) - 1 >= 2 * radius)
            ball.centerX -= 1;
    }

    private static void moveRight() {
        int i = ball.centerX;
        if ((i + radius) + 1 <= width)
            ball.centerX += 1;
    }

    private static void updateBall() {
        boolean movingUp = false;
        for (Point i : carrierInfo) {
            if (new Rectangle(i.x, i.y, carrierWidth, carrierHeight).intersects(new
                    Rectangle(ball.centerX - radius, ball.centerY - radius, 2 * radius, 2 * radius))) {
                movingUp = true;
                break;
            }
        }
        if (movingUp)
            ball.centerY -= 1;
        else
            ball.centerY += 2;

        if (ball.centerY - radius < 0 || ball.centerY > height + 2 * radius)
            gameOver = true;
    }

    private static void addCarrier() {
        double prob = Math.random();
        Point last = carrierInfo.getLast();
        int x;
        if (prob < 0.5) {
            if (prob < 0.2) {
                x = last.x - 2 * carrierWidth;
                if (x < carrierWidth / 2)
                    x = width / 2 - (int) (Math.random() * 2 * carrierWidth);
                carrierInfo.addLast(new Point(x, last.y + carrierRange));
            } else {
                x = last.x - carrierWidth;
                if (x < carrierWidth / 2)
                    x = width / 2 - (int) (Math.random() * carrierWidth);
                carrierInfo.addLast(new Point(x, last.y + carrierRange));
            }

        } else {
            if (prob < 0.7) {
                x = last.x + carrierWidth;
                if (x > width - carrierWidth / 2)
                    x = width / 2 + (int) (Math.random() * carrierWidth);
                carrierInfo.addLast(new Point(x, last.y + carrierRange));
            } else {
                x = last.x + 2 * carrierWidth;
                if (x > carrierWidth / 2)
                    x = width / 2 + (int) (Math.random() * 2 * carrierWidth);
                carrierInfo.addLast(new Point(x, last.y + carrierRange));
            }
        }
    }

    private static class GamePanel extends JPanel implements KeyListener, FocusListener {
        private GamePanel() {
            setBackground(Color.LIGHT_GRAY);
            addKeyListener(this);
            requestFocusInWindow();
            addFocusListener(this);
            addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    requestFocus();
                }
            });

        }

        public void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON);

            width = getWidth();
            height = getHeight();

            firstX = ((width - carrierWidth) / 2);
            firstY = (7 * height / 8);
            updateCarrier();
            g2.setColor(Color.GRAY);
            for (Point j : carrierInfo) {
                g2.fill3DRect(j.x, j.y, carrierWidth, carrierHeight, true);
            }
            updateBall();
            GradientPaint paint = new GradientPaint(ball.centerX - radius, ball.centerY - radius,
                    Color.RED, ball.centerX, ball.centerY, Color.BLUE, true);
            g2.setPaint(paint);
            g2.fillOval(ball.centerX - radius, ball.centerY - radius, 2 * radius, 2 * radius);


        }

        @Override
        public void keyTyped(KeyEvent keyEvent) {
        }

        @Override
        public void keyPressed(KeyEvent keyEvent) {
            int key = keyEvent.getKeyCode();
            if (key == KeyEvent.VK_LEFT) {
                moveLeft();
            } else if (key == KeyEvent.VK_RIGHT)
                moveRight();

        }

        @Override
        public void keyReleased(KeyEvent keyEvent) {
        }

        @Override
        public void focusGained(FocusEvent focusEvent) {

        }

        @Override
        public void focusLost(FocusEvent focusEvent) {

        }
    }

    public static void main(String[] args) {
        new FallingBall();
        animate.start();

    }
}
