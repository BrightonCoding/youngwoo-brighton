import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;

import framework.GameObject;

public class Powerup extends GameObject {

    public static final int RADIUS        = 18;
    public static final int FIELD_LIVE_MS = 5000;
    public static final int EFFECT_MS     = 5000;
    public static final int RESPAWN_MS    = 6000;

    public static final int TYPE_SIZE  = 1;
    public static final int TYPE_SPEED = 2;
    public static final int TYPE_SLOW  = 3;

    // index directly by type (1–3), slot 0 is just a placeholder
    private static final Color[] FILL_COLORS = {
        null,
        new Color(255, 200,   0),   // SIZE  — gold
        new Color(  0, 200, 220),   // SPEED — cyan
        new Color(255, 140,   0),   // SLOW  — orange
    };
    private static final Color[] GLOW_COLORS = {
        null,
        new Color(255, 220,  50, 120),
        new Color(  0, 220, 255, 120),
        new Color(255, 160,   0, 120),
    };
    private static final String[] LABELS = {
        null, "1.5x", ">>", "<<"
    };

    private int  radius;
    private int  ownerPlayer;
    private int  type;
    private long spawnTime;

    /**
     * Creates a powerup token centered at (cx, cy).
     * Pre:  cx and cy are valid coordinates inside the rink; owner is 1 or 2;
     *       powerupType is one of TYPE_SIZE, TYPE_SPEED, or TYPE_SLOW;
     *       spawnMillis is the current time; scale is positive.
     * Post: A new active, uncollected powerup is sized and positioned so its
     *       center is at (cx, cy), ready to be added to the game.
     */
    public Powerup(int cx, int cy, int owner, int powerupType, long spawnMillis, double scale) {
        ownerPlayer = owner;
        type        = powerupType;
        spawnTime   = spawnMillis;
        radius      = Math.max(6, (int) Math.round(RADIUS * scale));
        positionAt(cx, cy);
    }

    /**
     * Sets the component's size and position so the powerup is centered at (cx, cy).
     * Pre:  radius is already set; cx and cy are valid rink coordinates.
     * Post: The component bounds are updated with 4px glow padding on each side.
     */
    private void positionAt(int cx, int cy) {
        int pad = 4; // glow ring bleeds past the circle edge, so we need a little breathing room
        setSize((radius + pad) * 2, (radius + pad) * 2);
        setX(cx - radius - pad);
        setY(cy - radius - pad);
    }

    /**
     * Returns the player who benefits from collecting this powerup.
     * Pre:  The powerup exists.
     * Post: Returns 1 for player 1 (left side) or 2 for player 2 (right side).
     */
    public int getOwnerPlayer() { return ownerPlayer; }

    /**
     * Returns the effect type of this powerup.
     * Pre:  The powerup exists.
     * Post: Returns one of TYPE_SIZE, TYPE_SPEED, or TYPE_SLOW.
     */
    public int getType() { return type; }

    /**
     * Checks whether this powerup has been on the field too long and should disappear.
     * Pre:  nowMillis is the current system time in milliseconds.
     * Post: Returns true if the powerup has been alive for at least FIELD_LIVE_MS milliseconds.
     */
    public boolean isExpired(long nowMillis) {
        return nowMillis - spawnTime >= FIELD_LIVE_MS;
    }
    public void act() {}

    /**
     * Draws the powerup icon in the component's local coordinate space.
     * Pre:  g is a valid Graphics context; type is a valid TYPE_* constant.
     * Post: A glow ring, filled circle, white border, and centered label are drawn
     *       using colors and text looked up from the type arrays.
     */
    public void paint(Graphics g) {
        // component center accounts for the 4px glow padding
        int cx = radius + 4;
        int cy = radius + 4;
        int r  = radius;

        Color  fill  = FILL_COLORS[type];
        Color  glow  = GLOW_COLORS[type];
        String label = LABELS[type];

        // soft glow ring behind the circle
        g.setColor(glow);
        g.fillOval(cx - r - 4, cy - r - 4, (r + 4) * 2, (r + 4) * 2);

        g.setColor(fill);
        g.fillOval(cx - r, cy - r, r * 2, r * 2);

        g.setColor(Color.WHITE);
        g.drawOval(cx - r, cy - r, r * 2, r * 2);

        // center the label both horizontally and vertically
        g.setColor(Color.BLACK);
        g.setFont(new Font("SansSerif", Font.BOLD, 8));
        FontMetrics fm = g.getFontMetrics();
        g.drawString(label, cx - fm.stringWidth(label) / 2, cy + fm.getAscent() / 2 - 2);
    }
}