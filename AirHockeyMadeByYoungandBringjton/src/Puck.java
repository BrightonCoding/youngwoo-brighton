import java.awt.Color;
import java.awt.Graphics;

import framework.GameObject;

// the puck - moves around and bounces off walls and paddles
public class Puck extends GameObject {

    private static final int RADIUS = 14;
    private static final int DIAMETER = RADIUS * 2;
    private static final int SPEED = 9;

    int xSpeed = SPEED;
    int ySpeed = 3;

    // pre:  centerX and centerY are valid positions inside the rink
    // post: puck is created and placed so its center is at (centerX, centerY)
    public Puck(int centerX, int centerY) {
        setSize(DIAMETER, DIAMETER);
        // the framework positions things by top-left corner, so we shift by radius
        setX(centerX - RADIUS);
        setY(centerY - RADIUS);
    }

    // pre:  puck exists on screen
    // post: returns the x coordinate of the center of the puck
    public int getCenterX() {
        return getX() + RADIUS;
    }

    // pre:  puck exists on screen
    // post: returns the y coordinate of the center of the puck
    public int getCenterY() {
        return getY() + RADIUS;
    }

    // post: returns the radius of the puck in pixels
    public int getRadius() {
        return RADIUS;
    }

    // pre:  puck is overlapping the given paddle
    // post: xSpeed is flipped, puck is pushed outside the paddle so it doesn't get stuck
    public void hitByPaddle(Paddle paddle) {
        xSpeed = -xSpeed;

        if (xSpeed > 0) {
            setX(paddle.getX() + paddle.getWidth() + 1);
        } else {
            setX(paddle.getX() - DIAMETER - 1);
        }

        repaint();
    }

    // post: xSpeed is flipped (puck reverses left/right direction)
    public void bounceHorizontal() {
        xSpeed = -xSpeed;
    }

    // post: ySpeed is flipped (puck reverses up/down direction)
    public void bounceVertical() {
        ySpeed = -ySpeed;
    }

    // post: puck's position is updated by adding xSpeed and ySpeed
    public void update() {
        setX(getX() + xSpeed);
        setY(getY() + ySpeed);
        repaint();
    }

    // pre:  direction is either 1 or -1
    // post: puck is moved to (centerX, centerY) and speed is reset toward the given direction
    public void reset(int centerX, int centerY, int direction) {
        setX(centerX - RADIUS);
        setY(centerY - RADIUS);
        xSpeed = SPEED * direction;
        ySpeed = 3;
        repaint();
    }

    // post: nothing - movement is handled in AirHockeyGame, not here
    public void act() {
    }

    // pre:  g is a valid Graphics object
    // post: puck is drawn as a white circle with a dark grey fill inside
    public void paint(Graphics g) {
        g.setColor(Color.WHITE);
        g.fillOval(0, 0, DIAMETER, DIAMETER);

        g.setColor(Color.DARK_GRAY);
        g.fillOval(3, 3, DIAMETER - 6, DIAMETER - 6);
    }
}
