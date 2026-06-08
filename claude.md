# Claude Guide

## What This Is

This is an Eclipse-style Java Swing air hockey game. It uses only the JDK standard library plus a bundled educational `framework` package.

## Important Paths

- Main class: `AirHockeyMadeByYoungandBringjton/src/AirHockeyApp.java`
- Main game logic: `AirHockeyMadeByYoungandBringjton/src/AirHockeyGame.java`
- Game objects: `AirHockeyMadeByYoungandBringjton/src/Paddle.java`, `Puck.java`, and `Rink.java`
- Framework code: `AirHockeyMadeByYoungandBringjton/src/framework/Game.java` and `GameObject.java`
- Generated output: `AirHockeyMadeByYoungandBringjton/bin`
- Original project archive: `AirhockeyYoungBrighton.zip`

## How The Code Works

`AirHockeyApp` starts the program. `AirHockeyGame` extends `framework.Game`, configures the window, prompts for player names, creates the rink, paddles, and puck, then handles each frame in `act()`.

`framework.Game` owns the Swing `Timer`, keyboard listeners, object list, and JFrame setup. It calls the game's `act()` method and then calls each added `GameObject`'s `act()` method.

`Puck`, `Paddle`, and `Rink` extend `framework.GameObject`, which is based on `JComponent`. Drawing happens in each object's `paint(Graphics g)` method.

## Common Commands

Compile:

```sh
mkdir -p AirHockeyMadeByYoungandBringjton/bin
javac -d AirHockeyMadeByYoungandBringjton/bin $(find AirHockeyMadeByYoungandBringjton/src -name '*.java')
```

Run:

```sh
java -cp AirHockeyMadeByYoungandBringjton/bin AirHockeyApp
```

## Change Guidance

- Preserve the simple beginner-friendly Java style unless asked to refactor.
- Prefer focused changes in the concrete game classes before changing the framework.
- Keep generated build artifacts out of git.
- There are currently no automated tests. Verify changes by compiling and, when useful, launching the Swing app locally.
- Be careful with drawing order: the current code adds `puck`, then paddles, then `rink`, and the framework uses Swing components with null layout.
- Be careful with player bounds: Player 1 is constrained to the left half of the rink and Player 2 to the right half.
