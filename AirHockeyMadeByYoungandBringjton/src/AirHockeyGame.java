import java.awt.Color;

import javax.swing.JOptionPane;

import framework.Game;

// Brighton Ng + Youngwoo Nam - ICS3U Culminating
// main game class, all the actual game logic lives here
public class AirHockeyGame extends Game {

    // rink dimensions - we figured these out by trial and error until it looked right
    private static final int WINDOW_WIDTH = 800;
    private static final int WINDOW_HEIGHT = 600;
    private static final int RINK_X = 50;
    private static final int RINK_Y = 80;
    private static final int RINK_WIDTH = 700;
    private static final int RINK_HEIGHT = 440;
    private static final int GOAL_HEIGHT = 150;

    private Rink rink;
    private Paddle playerPaddle;
    private Paddle opponentPaddle;
    private Puck puck;

    // default names in case the player skips the input
    private String player1Name = "Player 1";
    private String player2Name = "Player 2";

    // pre:  the game window has been created
    // post: window is sized, objects are created and added, game is ready to play
    public void setup() {
        setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
        setTitle("Air Hockey");
        setBackground(new Color(20, 30, 48));
        setDelay(16);

        // show the controls before anything starts
        JOptionPane.showMessageDialog(this,
                "Player 1 (Blue): W / A / S / D\nPlayer 2 (Red): Arrow Keys\n\nScore by hitting the puck into the other side's goal.",
                "How to Play", JOptionPane.INFORMATION_MESSAGE);

        player1Name = promptForName("Enter Player 1's name:", "Player 1");
        player2Name = promptForName("Enter Player 2's name:", "Player 2");

        // puck is added first so it ends up on top visually,
        // paddles are next, rink goes last so it's behind everything
        puck = new Puck(
                RINK_X + RINK_WIDTH / 2,
                RINK_Y + RINK_HEIGHT / 2);

        playerPaddle = new Paddle(
                RINK_X + 80,
                RINK_Y + RINK_HEIGHT / 2,
                new Color(54, 124, 230));

        opponentPaddle = new Paddle(
                RINK_X + RINK_WIDTH - 80,
                RINK_Y + RINK_HEIGHT / 2,
                new Color(220, 70, 60));

        rink = new Rink(WINDOW_WIDTH, WINDOW_HEIGHT,
                RINK_X, RINK_Y, RINK_WIDTH, RINK_HEIGHT, GOAL_HEIGHT,
                player1Name, player2Name);

        add(puck);
        add(playerPaddle);
        add(opponentPaddle);
        add(rink);
    }

    // pre:  setup() has been called and all objects exist
    // post: puck moves, input is read, collisions and goals are checked for this frame
    public void act() {
        handleInput();
        puck.update();
        handleGoals();
        handleWallCollisions();
        handlePaddleCollisions();
    }

    // pre:  message and defaultName are not null
    // post: returns whatever the player typed, or defaultName if they left it blank
    private String promptForName(String message, String defaultName) {
        String name = JOptionPane.showInputDialog(this,
                message, "Player Names", JOptionPane.PLAIN_MESSAGE);

        if (name == null || name.equals("")) {
            return defaultName;
        }

        return name;
    }

    // pre:  playerPaddle and opponentPaddle exist
    // post: both paddles move based on which keys are currently held
    private void handleInput() {
        playerPaddle.move(
                WKeyPressed(), SKeyPressed(), AKeyPressed(), DKeyPressed(),
                RINK_X, RINK_X + RINK_WIDTH / 2,
                RINK_Y, RINK_Y + RINK_HEIGHT);

        opponentPaddle.move(
                UpKeyPressed(), DownKeyPressed(), LeftKeyPressed(), RightKeyPressed(),
                RINK_X + RINK_WIDTH / 2, RINK_X + RINK_WIDTH,
                RINK_Y, RINK_Y + RINK_HEIGHT);
    }

    // pre:  puck exists somewhere on the screen
    // post: if the puck hit a wall, its speed is flipped and it is pushed back inside
    //       left and right walls only apply outside the goal opening
    private void handleWallCollisions() {
        int goalTop      = RINK_Y + (RINK_HEIGHT - GOAL_HEIGHT) / 2;
        int goalBottom   = goalTop + GOAL_HEIGHT;
        int puckDiameter = puck.getRadius() * 2;

        boolean inGoalOpening = false;
        if (puck.getCenterY() >= goalTop && puck.getCenterY() <= goalBottom) {
            inGoalOpening = true;
        }

        // top wall
        if (puck.getY() <= RINK_Y) {
            puck.setY(RINK_Y);
            puck.bounceVertical();
        }

        // bottom wall
        if (puck.getY() + puckDiameter >= RINK_Y + RINK_HEIGHT) {
            puck.setY(RINK_Y + RINK_HEIGHT - puckDiameter);
            puck.bounceVertical();
        }

        // left and right walls - skip if the puck is lined up with the goal
        if (puck.getX() <= RINK_X && inGoalOpening == false) {
            puck.setX(RINK_X);
            puck.bounceHorizontal();
        }

        if (puck.getX() + puckDiameter >= RINK_X + RINK_WIDTH && inGoalOpening == false) {
            puck.setX(RINK_X + RINK_WIDTH - puckDiameter);
            puck.bounceHorizontal();
        }
    }

    // pre:  puck exists
    // post: if the puck crossed a goal line while lined up with the opening,
    //       it gets reset to center and sent toward the player who was scored on
    // TODO: add score tracking here for the final version
    private void handleGoals() {
        int goalTop      = RINK_Y + (RINK_HEIGHT - GOAL_HEIGHT) / 2;
        int goalBottom   = goalTop + GOAL_HEIGHT;
        int puckDiameter = puck.getRadius() * 2;

        boolean inGoalOpening = false;
        if (puck.getCenterY() >= goalTop && puck.getCenterY() <= goalBottom) {
            inGoalOpening = true;
        }

        if (inGoalOpening == false) {
            return;
        }

        // puck crossed the left goal line
        if (puck.getX() + puckDiameter < RINK_X) {
            puck.reset(RINK_X + RINK_WIDTH / 2, RINK_Y + RINK_HEIGHT / 2, -1);
        }

        // puck crossed the right goal line
        if (puck.getX() > RINK_X + RINK_WIDTH) {
            puck.reset(RINK_X + RINK_WIDTH / 2, RINK_Y + RINK_HEIGHT / 2, 1);
        }
    }

    // pre:  puck, playerPaddle, and opponentPaddle all exist
    // post: if the puck is touching a paddle it bounces off
    //       else if used so only one paddle is handled per frame
    private void handlePaddleCollisions() {
        if (puck.collides(playerPaddle)) {
            puck.hitByPaddle(playerPaddle);
        } else if (puck.collides(opponentPaddle)) {
            puck.hitByPaddle(opponentPaddle);
        }
    }
}
