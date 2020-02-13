import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Iterator;
import java.util.LinkedList;

public class SnakeGUI extends JPanel implements KeyListener {
    private final static int width = 420; // width of the panel
    private final static int height = 470; // height of the panel
    Timer gameTime;

    static Snake mySnake;
    static food snakeFood;
    static boolean isPlaying;
    static int gameLevel;
    static String message;
    static LinkedList<Location> dat;
    static boolean gameStart = false;
    private JMenuBar myBar;
    private static int levelTime;
    private static int levelScore;
    private static LinkedList<Location> obstacles;
    private JButton pauseResume;

    JLabel level;   // displays the current game level
    JLabel score;   // displays the players score


    // playing mode


    // possible directions of the snake
    private final static int UP = 1;
    private final static int DOWN = -1;
    private final static int LEFT = -2;
    private final static int RIGHT = 2;

    private static int currentDirection = DOWN;  // current snake direction

    private static class food {
        int foodY;
        int foodX;
        boolean eaten;

        food(Location loc) {
            foodX = loc.getCol();
            foodY = loc.getRow();
        }
    }


    private SnakeGUI() {
        setLayout(null);

        setBackground(Color.GREEN);
        addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent evt) {
                requestFocusInWindow();

            }
        });
        mySnake = new Snake(20, 20);    // create a new snake at row and col 20
        dat = mySnake.getSizeData();
        message = "CLICK ON THE SCREEN TO START GAME";

        // define the timer
        levelTime = 200;
        gameTime = new Timer(levelTime, (e) -> {
            playGame();
            repaint();
        });
        addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                if (gameStart) {
                    isPlaying = true;
                    gameLevel = 0;
                }
                gameTime.start();
                message = null;
                // if (gameLevel)

            }

            public void focusLost(FocusEvent e) {
                gameTime.stop();
                message = "CLICK ON THE SCREEN TO CONTINUE GAME";
                repaint();
            }
        });
        snakeFood = new food(snakeFoodLoc());
        addKeyListener(this);

        // define and place component
        level = new JLabel("  LEVEL : ", JLabel.CENTER);
        level.setForeground(Color.BLUE);

        level.setOpaque(true);

        score = new JLabel("   SCORE : ", JLabel.CENTER);
        score.setForeground(Color.RED);
        score.setOpaque(true);

        pauseResume = new JButton("Pause");
        pauseResume.addActionListener((e) -> {

            if (isPlaying) {
                pauseResume.setActionCommand("Resume");
                gameTime.stop();

            } else if (gameStart) {
                gameTime.start();
                pauseResume.setActionCommand("Pause");
            }
        });


        myBar = new JMenuBar();

        myBar.add(level);
        myBar.add(new JToolBar.Separator());
        myBar.add(score);
        myBar.add(new JToolBar.Separator());
        myBar.add(pauseResume);


    }

    private void playGame() {
        int totalScore = (gameLevel * 400) + levelScore;
        try {
            // if (selfPlayMode.isSelected())
            //autoDirect();
            keepMoving();
            setObstacles();
            if (snakeFood.eaten) {
                snakeFood = new food(snakeFoodLoc());
            }
            if (levelScore == 400) {
                gameLevel += 1;
                levelScore = 0;
                levelTime -= 50;
                mySnake = new Snake(20, 20);
                dat = mySnake.getSizeData();
            }

            level.setText("     LEVEL   : " + (gameLevel + 1));
            score.setText("     SCORE   : " + totalScore);


        } catch (GameOverException g) {
            gameTime.stop();
            gameStart = false;
            isPlaying = false;
            message = "Game Over!!!! Your Score is : " + totalScore + "  Press \'Enter\' to play again";
        }
    }

    public int getLevelTime() {
        return levelTime;
    }

    // returns the JMenu
    private JMenuBar getJMenuBar() {
        return myBar;
    }


    // defining the keyListener interface
    public void keyPressed(KeyEvent evt) {
        int key = evt.getKeyCode();


        if (key == KeyEvent.VK_DOWN || key == KeyEvent.VK_D)
            directionUpdate(DOWN);
        else if (key == KeyEvent.VK_UP || key == KeyEvent.VK_U)
            directionUpdate(UP);
        else if (key == KeyEvent.VK_RIGHT || key == KeyEvent.VK_R)
            directionUpdate(RIGHT);
        else if (key == KeyEvent.VK_LEFT || key == KeyEvent.VK_L)
            directionUpdate(LEFT);


        if (key == KeyEvent.VK_ENTER && !isPlaying) {
            mySnake = new Snake(20, 20);
            dat = mySnake.getSizeData();
            snakeFood = new food(snakeFoodLoc());
            isPlaying = true;
            message = null;
            gameStart = true;
            gameLevel = 0;
            levelScore = 0;


            gameTime.start();
            repaint();
        }


    }

    public void keyTyped(KeyEvent evt) {

    }

    public void keyReleased(KeyEvent evt) {

    }

    private void setObstacles() {
        LinkedList<Location> temp = new LinkedList<Location>();
        int level = gameLevel % 10;
        switch (level) {
            case 0:
                obstacles = null;
                break;
            case 1:
                for (int x = 10; x <= 30; x++) {
                    temp.add(new Location(x, 10));
                    temp.add(new Location(x, 30));
                }
                obstacles = temp;
                break;
            case 2:
                for (int x = 10; x <= 30; x++) {
                    temp.add(new Location(x, 10));
                    temp.add(new Location(x, 30));
                }
                for (int y = 15; y <= 25; y++) {
                    temp.add(new Location(15, y));
                    temp.add(new Location(25, y));
                }
                obstacles = temp;
                break;
            case 3:
                for (int x = 0; x < 40; x++) {
                    temp.add(new Location(x, 0));
                    temp.add(new Location(x, 39));
                    temp.add(new Location(0, x));
                    temp.add(new Location(39, x));
                }
                obstacles = temp;
                break;
            case 4:
                for (int x = 0; x < 40; x++) {
                    temp.add(new Location(x, 0));
                    temp.add(new Location(x, 39));
                    temp.add(new Location(0, x));
                    temp.add(new Location(39, x));
                }
                for (int x = 10; x <= 30; x++) {
                    temp.add(new Location(x, 10));
                    temp.add(new Location(x, 30));
                }
                obstacles = temp;
                break;
            case 5:
                for (int y = 5; y <= 35; y++) {
                    temp.add(new Location(5, y));
                    temp.add(new Location(34, y));
                }
                for (int x = 0; x <= 5; x++)
                    temp.add(new Location(x, 5));
                for (int x = 35; x < 40; x++)
                    temp.add(new Location(x, 35));
                obstacles = temp;
                break;
            case 6:
                for (int x = 10; x <= 30; x++) {
                    temp.add(new Location(x, 10));
                    temp.add(new Location(x, 30));
                }
                for (int x = 0; x <= 5; x++)
                    temp.add(new Location(x, 5));
                for (int x = 35; x < 40; x++)
                    temp.add(new Location(x, 35));
                obstacles = temp;
                break;
            case 7:
                for (int x = 10; x <= 30; x++) {
                    temp.add(new Location(x, 5));
                    temp.add(new Location(x, 35));
                }
                for (int x = 0; x <= 5; x++) {
                    temp.add(new Location(5, x));
                    temp.add(new Location(35, x));
                }
                for (int x = 35; x < 40; x++) {
                    temp.add(new Location(5, x));
                    temp.add(new Location(35, x));
                }
                obstacles = temp;
                break;
            case 8:
                for (int x = 0; x < 40; x++) {
                    temp.add(new Location(x, 0));
                    temp.add(new Location(x, 39));
                    temp.add(new Location(0, x));
                    temp.add(new Location(39, x));
                }
                for (int y = 5; y <= 35; y++) {
                    temp.add(new Location(5, y));
                    temp.add(new Location(34, y));
                }
                for (int x = 0; x <= 5; x++)
                    temp.add(new Location(5, x));
                for (int x = 35; x < 40; x++)
                    temp.add(new Location(35, x));
                obstacles = temp;
                break;
            case 9:
                for (int x = 10; x <= 30; x++) {
                    temp.add(new Location(x, 10));
                    temp.add(new Location(x, 30));
                }
                for (int x = 13; x <= 30; x++)
                    temp.add(new Location(10, x));
                for (int x = 10; x <= 27; x++)
                    temp.add(new Location(30, x));

                for (int x = 0; x < 40; x++) {
                    temp.add(new Location(x, 0));
                    temp.add(new Location(x, 39));
                    temp.add(new Location(0, x));
                    temp.add(new Location(39, x));
                }
                for (int x = 10; x <= 30; x++) {
                    temp.add(new Location(x, 5));
                    temp.add(new Location(x, 35));
                }
                for (int x = 0; x <= 5; x++) {
                    temp.add(new Location(5, x));
                    temp.add(new Location(35, x));
                }
                for (int x = 35; x < 40; x++) {
                    temp.add(new Location(5, x));
                    temp.add(new Location(35, x));
                }
                obstacles = temp;
                break;
            default:
                obstacles = null;
                break;


        }

    }

    public static void main(String[] args) {
        JFrame window = new JFrame("SNAKE GAME");
        SnakeGUI content = new SnakeGUI();
        window.setJMenuBar(content.getJMenuBar());
        window.setContentPane(content);
        window.setLocation(50, 50);
        window.setSize(new Dimension(width, height));
        window.setVisible(true);
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setResizable(false);
        window.setFocusable(true);

    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setStroke(new BasicStroke(2));


        if (message != null) {
            g.setColor(Color.RED);
            g.drawString(message, 40, 100);
        }

        g.drawRect(0, 0, 400, 400);

        if (!snakeFood.eaten) {
            g.setColor(Color.YELLOW);
            g.fillOval(snakeFood.foodX * 10, snakeFood.foodY * 10, 10, 10);

        }
        // Paints the snake
        if (isPlaying || !gameStart)
            g.setColor(Color.ORANGE);
        paint(g, mySnake.getSizeData());

        // paints the obstacle

        if (obstacles != null) {
            g.setColor(Color.GRAY);
            paint(g, obstacles);
        }

    }

    private void paint(Graphics g, LinkedList<Location> location) {
        for (Location loc : location) {
            g.fill3DRect(loc.getCol() * 10, loc.getRow() * 10, 10, 10, true);
        }
    }

    private boolean directionUpdate(int direction) {
        int newDirection = direction;
        currentDirection = (currentDirection == direction || (currentDirection - direction == 0) || (currentDirection + direction == 0) || direction - currentDirection == 0) ? currentDirection : direction;
        return direction != currentDirection;

    }

    private void keepMoving() throws GameOverException {
        Location newLoc = dat.getFirst();
        if (currentDirection == DOWN) {
            int r = newLoc.getRow() + 1;
            int c = newLoc.getCol();
            r = r == 40 ? 0 : r;
            if (isCrossing() || hitsObstacle())
                throw new GameOverException(r, c);
            dat.addFirst(new Location(r, c));
            dat.removeLast();


        } else if (currentDirection == UP) {

            int r = newLoc.getRow() - 1;
            int c = newLoc.getCol();
            r = r < 0 ? 39 : r;
            if (isCrossing() || hitsObstacle())
                throw new GameOverException(r, c);
            dat.addFirst(new Location(r, c));
            dat.removeLast();


        } else if (currentDirection == RIGHT) {

            int r = newLoc.getRow();
            int c = newLoc.getCol() + 1;
            c = c == 40 ? 0 : c;
            if (isCrossing() || hitsObstacle())
                throw new GameOverException(r, c);
            dat.addFirst(new Location(r, c));
            dat.removeLast();


        } else if (currentDirection == LEFT) {

            int r = newLoc.getRow();
            int c = newLoc.getCol() - 1;
            c = c < 0 ? 39 : c;
            if (isCrossing() || hitsObstacle())
                throw new GameOverException(r, c);
            dat.addFirst(new Location(r, c));
            dat.removeLast();


        }
        if (dat.getFirst().getCol() == snakeFood.foodX && dat.getFirst().getRow() == snakeFood.foodY) {
            snakeFood.eaten = true;
            levelScore += 50;
            mySnake.addSize();
        }
    }

    public class GameOverException extends Exception {
        int r;
        int c;
        int snakeSize;


        GameOverException(int r, int c) {
            this.r = r;
            this.c = c;
            snakeSize = mySnake.getSize();
        }

        public String toString() {
            return "Game Over";
        }

        public String getMessage() {
            return toString();
        }
    }

    private static boolean isCrossing() {
        Iterator<Location> itr = dat.iterator();
        Location head = itr.next();
        while (itr.hasNext()) {
            if (head.equals(itr.next()))
                return true;

        }
        return false;
    }

    private static boolean hitsObstacle() {
        if (obstacles == null)
            return false;
        Iterator<Location> itr = obstacles.iterator();
        Location head = dat.getFirst(); // location of the snake head
        while (itr.hasNext()) {
            if (head.equals(itr.next()))
                return true;
        }
        return false;
    }

    private Location snakeFoodLoc() {
        Location newLoc;
        Iterator<Location> itr;
        Iterator<Location> itr2;
        first:
        while (true) {
            newLoc = new Location((int) (Math.random() * 40), (int) (Math.random() * 40));
            itr = dat.iterator();
            while (itr.hasNext()) {
                if (newLoc.equals(itr.next()))
                    continue first;


            }
            if (obstacles != null) {
                itr2 = obstacles.iterator();
                while (itr2.hasNext()) {
                    if (newLoc.equals(itr2.next()))
                        continue first;
                }
            }
            return newLoc;
        }
    }
}
