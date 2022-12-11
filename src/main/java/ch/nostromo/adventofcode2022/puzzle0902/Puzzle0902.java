package ch.nostromo.adventofcode2022.puzzle0902;

import ch.nostromo.adventofcode2022.puzzle0401.Puzzle0401;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;


/**
 * --- Day 9: Rope Bridge ---
 * This rope bridge creaks as you walk along it. You aren't sure how old it is, or whether it can even support your weight.
 * <p>
 * It seems to support the Elves just fine, though. The bridge spans a gorge which was carved out by the massive river far below you.
 * <p>
 * You step carefully; as you do, the ropes stretch and twist. You decide to distract yourself by modeling rope physics; maybe you can even figure out where not to step.
 * <p>
 * Consider a rope with a knot at each end; these knots mark the head and the tail of the rope. If the head moves far enough away from the tail, the tail is pulled toward the head.
 * <p>
 * Due to nebulous reasoning involving Planck lengths, you should be able to model the positions of the knots on a two-dimensional grid. Then, by following a hypothetical series of motions (your puzzle input) for the head, you can determine how the tail will move.
 * <p>
 * Due to the aforementioned Planck lengths, the rope must be quite short; in fact, the head (H) and tail (T) must always be touching (diagonally adjacent and even overlapping both count as touching):
 * <p>
 * ....
 * .TH.
 * ....
 * <p>
 * ....
 * .H..
 * ..T.
 * ....
 * <p>
 * ...
 * .H. (H covers T)
 * ...
 * If the head is ever two steps directly up, down, left, or right from the tail, the tail must also move one step in that direction so it remains close enough:
 * <p>
 * .....    .....    .....
 * .TH.. -> .T.H. -> ..TH.
 * .....    .....    .....
 * <p>
 * ...    ...    ...
 * .T.    .T.    ...
 * .H. -> ... -> .T.
 * ...    .H.    .H.
 * ...    ...    ...
 * Otherwise, if the head and tail aren't touching and aren't in the same row or column, the tail always moves one step diagonally to keep up:
 * <p>
 * .....    .....    .....
 * .....    ..H..    ..H..
 * ..H.. -> ..... -> ..T..
 * .T...    .T...    .....
 * .....    .....    .....
 * <p>
 * .....    .....    .....
 * .....    .....    .....
 * ..H.. -> ...H. -> ..TH.
 * .T...    .T...    .....
 * .....    .....    .....
 * You just need to work out where the tail goes as the head follows a series of motions. Assume the head and the tail both start at the same position, overlapping.
 * <p>
 * For example:
 * <p>
 * R 4
 * U 4
 * L 3
 * D 1
 * R 4
 * D 1
 * L 5
 * R 2
 * This series of motions moves the head right four steps, then up four steps, then left three steps, then down one step, and so on. After each step, you'll need to update the position of the tail if the step means the head is no longer adjacent to the tail. Visually, these motions occur as follows (s marks the starting position as a reference point):
 * <p>
 * == Initial State ==
 * <p>
 * ......
 * ......
 * ......
 * ......
 * H.....  (H covers T, s)
 * <p>
 * == R 4 ==
 * <p>
 * ......
 * ......
 * ......
 * ......
 * TH....  (T covers s)
 * <p>
 * ......
 * ......
 * ......
 * ......
 * sTH...
 * <p>
 * ......
 * ......
 * ......
 * ......
 * s.TH..
 * <p>
 * ......
 * ......
 * ......
 * ......
 * s..TH.
 * <p>
 * == U 4 ==
 * <p>
 * ......
 * ......
 * ......
 * ....H.
 * s..T..
 * <p>
 * ......
 * ......
 * ....H.
 * ....T.
 * s.....
 * <p>
 * ......
 * ....H.
 * ....T.
 * ......
 * s.....
 * <p>
 * ....H.
 * ....T.
 * ......
 * ......
 * s.....
 * <p>
 * == L 3 ==
 * <p>
 * ...H..
 * ....T.
 * ......
 * ......
 * s.....
 * <p>
 * ..HT..
 * ......
 * ......
 * ......
 * s.....
 * <p>
 * .HT...
 * ......
 * ......
 * ......
 * s.....
 * <p>
 * == D 1 ==
 * <p>
 * ..T...
 * .H....
 * ......
 * ......
 * s.....
 * <p>
 * == R 4 ==
 * <p>
 * ..T...
 * ..H...
 * ......
 * ......
 * s.....
 * <p>
 * ..T...
 * ...H..
 * ......
 * ......
 * s.....
 * <p>
 * ......
 * ...TH.
 * ......
 * ......
 * s.....
 * <p>
 * ......
 * ....TH
 * ......
 * ......
 * s.....
 * <p>
 * == D 1 ==
 * <p>
 * ......
 * ....T.
 * .....H
 * ......
 * s.....
 * <p>
 * == L 5 ==
 * <p>
 * ......
 * ....T.
 * ....H.
 * ......
 * s.....
 * <p>
 * ......
 * ....T.
 * ...H..
 * ......
 * s.....
 * <p>
 * ......
 * ......
 * ..HT..
 * ......
 * s.....
 * <p>
 * ......
 * ......
 * .HT...
 * ......
 * s.....
 * <p>
 * ......
 * ......
 * HT....
 * ......
 * s.....
 * <p>
 * == R 2 ==
 * <p>
 * ......
 * ......
 * .H....  (H covers T)
 * ......
 * s.....
 * <p>
 * ......
 * ......
 * .TH...
 * ......
 * s.....
 * After simulating the rope, you can count up all of the positions the tail visited at least once. In this diagram, s again marks the starting position (which the tail also visited) and # marks other positions the tail visited:
 * <p>
 * ..##..
 * ...##.
 * .####.
 * ....#.
 * s###..
 * So, there are 13 positions the tail visited at least once.
 * <p>
 * Simulate your complete hypothetical series of motions. How many positions does the tail of the rope visit at least once?
 * <p>
 * Your puzzle answer was 6256.
 * <p>
 * The first half of this puzzle is complete! It provides one gold star: *
 * <p>
 * --- Part Two ---
 * A rope snaps! Suddenly, the river is getting a lot closer than you remember. The bridge is still there, but some of the ropes that broke are now whipping toward you as you fall through the air!
 * <p>
 * The ropes are moving too quickly to grab; you only have a few seconds to choose how to arch your body to avoid being hit. Fortunately, your simulation can be extended to support longer ropes.
 * <p>
 * Rather than two knots, you now must simulate a rope consisting of ten knots. One knot is still the head of the rope and moves according to the series of motions. Each knot further down the rope follows the knot in front of it using the same rules as before.
 * <p>
 * Using the same series of motions as the above example, but with the knots marked H, 1, 2, ..., 9, the motions now occur as follows:
 * <p>
 * == Initial State ==
 * <p>
 * ......
 * ......
 * ......
 * ......
 * H.....  (H covers 1, 2, 3, 4, 5, 6, 7, 8, 9, s)
 * <p>
 * == R 4 ==
 * <p>
 * ......
 * ......
 * ......
 * ......
 * 1H....  (1 covers 2, 3, 4, 5, 6, 7, 8, 9, s)
 * <p>
 * ......
 * ......
 * ......
 * ......
 * 21H...  (2 covers 3, 4, 5, 6, 7, 8, 9, s)
 * <p>
 * ......
 * ......
 * ......
 * ......
 * 321H..  (3 covers 4, 5, 6, 7, 8, 9, s)
 * <p>
 * ......
 * ......
 * ......
 * ......
 * 4321H.  (4 covers 5, 6, 7, 8, 9, s)
 * <p>
 * == U 4 ==
 * <p>
 * ......
 * ......
 * ......
 * ....H.
 * 4321..  (4 covers 5, 6, 7, 8, 9, s)
 * <p>
 * ......
 * ......
 * ....H.
 * .4321.
 * 5.....  (5 covers 6, 7, 8, 9, s)
 * <p>
 * ......
 * ....H.
 * ....1.
 * .432..
 * 5.....  (5 covers 6, 7, 8, 9, s)
 * <p>
 * ....H.
 * ....1.
 * ..432.
 * .5....
 * 6.....  (6 covers 7, 8, 9, s)
 * <p>
 * == L 3 ==
 * <p>
 * ...H..
 * ....1.
 * ..432.
 * .5....
 * 6.....  (6 covers 7, 8, 9, s)
 * <p>
 * ..H1..
 * ...2..
 * ..43..
 * .5....
 * 6.....  (6 covers 7, 8, 9, s)
 * <p>
 * .H1...
 * ...2..
 * ..43..
 * .5....
 * 6.....  (6 covers 7, 8, 9, s)
 * <p>
 * == D 1 ==
 * <p>
 * ..1...
 * .H.2..
 * ..43..
 * .5....
 * 6.....  (6 covers 7, 8, 9, s)
 * <p>
 * == R 4 ==
 * <p>
 * ..1...
 * ..H2..
 * ..43..
 * .5....
 * 6.....  (6 covers 7, 8, 9, s)
 * <p>
 * ..1...
 * ...H..  (H covers 2)
 * ..43..
 * .5....
 * 6.....  (6 covers 7, 8, 9, s)
 * <p>
 * ......
 * ...1H.  (1 covers 2)
 * ..43..
 * .5....
 * 6.....  (6 covers 7, 8, 9, s)
 * <p>
 * ......
 * ...21H
 * ..43..
 * .5....
 * 6.....  (6 covers 7, 8, 9, s)
 * <p>
 * == D 1 ==
 * <p>
 * ......
 * ...21.
 * ..43.H
 * .5....
 * 6.....  (6 covers 7, 8, 9, s)
 * <p>
 * == L 5 ==
 * <p>
 * ......
 * ...21.
 * ..43H.
 * .5....
 * 6.....  (6 covers 7, 8, 9, s)
 * <p>
 * ......
 * ...21.
 * ..4H..  (H covers 3)
 * .5....
 * 6.....  (6 covers 7, 8, 9, s)
 * <p>
 * ......
 * ...2..
 * ..H1..  (H covers 4; 1 covers 3)
 * .5....
 * 6.....  (6 covers 7, 8, 9, s)
 * <p>
 * ......
 * ...2..
 * .H13..  (1 covers 4)
 * .5....
 * 6.....  (6 covers 7, 8, 9, s)
 * <p>
 * ......
 * ......
 * H123..  (2 covers 4)
 * .5....
 * 6.....  (6 covers 7, 8, 9, s)
 * <p>
 * == R 2 ==
 * <p>
 * ......
 * ......
 * .H23..  (H covers 1; 2 covers 4)
 * .5....
 * 6.....  (6 covers 7, 8, 9, s)
 * <p>
 * ......
 * ......
 * .1H3..  (H covers 2, 4)
 * .5....
 * 6.....  (6 covers 7, 8, 9, s)
 * Now, you need to keep track of the positions the new tail, 9, visits. In this example, the tail never moves, and so it only visits 1 position. However, be careful: more types of motion are possible than before, so you might want to visually compare your simulated rope to the one above.
 * <p>
 * Here's a larger example:
 * <p>
 * R 5
 * U 8
 * L 8
 * D 3
 * R 17
 * D 10
 * L 25
 * U 20
 * These motions occur as follows (individual steps are not shown):
 * <p>
 * == Initial State ==
 * <p>
 * ..........................
 * ..........................
 * ..........................
 * ..........................
 * ..........................
 * ..........................
 * ..........................
 * ..........................
 * ..........................
 * ..........................
 * ..........................
 * ..........................
 * ..........................
 * ..........................
 * ..........................
 * ...........H..............  (H covers 1, 2, 3, 4, 5, 6, 7, 8, 9, s)
 * ..........................
 * ..........................
 * ..........................
 * ..........................
 * ..........................
 * <p>
 * == R 5 ==
 * <p>
 * ..........................
 * ..........................
 * ..........................
 * ..........................
 * ..........................
 * ..........................
 * ..........................
 * ..........................
 * ..........................
 * ..........................
 * ..........................
 * ..........................
 * ..........................
 * ..........................
 * ..........................
 * ...........54321H.........  (5 covers 6, 7, 8, 9, s)
 * ..........................
 * ..........................
 * ..........................
 * ..........................
 * ..........................
 * <p>
 * == U 8 ==
 * <p>
 * ..........................
 * ..........................
 * ..........................
 * ..........................
 * ..........................
 * ..........................
 * ..........................
 * ................H.........
 * ................1.........
 * ................2.........
 * ................3.........
 * ...............54.........
 * ..............6...........
 * .............7............
 * ............8.............
 * ...........9..............  (9 covers s)
 * ..........................
 * ..........................
 * ..........................
 * ..........................
 * ..........................
 * <p>
 * == L 8 ==
 * <p>
 * ..........................
 * ..........................
 * ..........................
 * ..........................
 * ..........................
 * ..........................
 * ..........................
 * ........H1234.............
 * ............5.............
 * ............6.............
 * ............7.............
 * ............8.............
 * ............9.............
 * ..........................
 * ..........................
 * ...........s..............
 * ..........................
 * ..........................
 * ..........................
 * ..........................
 * ..........................
 * <p>
 * == D 3 ==
 * <p>
 * ..........................
 * ..........................
 * ..........................
 * ..........................
 * ..........................
 * ..........................
 * ..........................
 * ..........................
 * .........2345.............
 * ........1...6.............
 * ........H...7.............
 * ............8.............
 * ............9.............
 * ..........................
 * ..........................
 * ...........s..............
 * ..........................
 * ..........................
 * ..........................
 * ..........................
 * ..........................
 * <p>
 * == R 17 ==
 * <p>
 * ..........................
 * ..........................
 * ..........................
 * ..........................
 * ..........................
 * ..........................
 * ..........................
 * ..........................
 * ..........................
 * ..........................
 * ................987654321H
 * ..........................
 * ..........................
 * ..........................
 * ..........................
 * ...........s..............
 * ..........................
 * ..........................
 * ..........................
 * ..........................
 * ..........................
 * <p>
 * == D 10 ==
 * <p>
 * ..........................
 * ..........................
 * ..........................
 * ..........................
 * ..........................
 * ..........................
 * ..........................
 * ..........................
 * ..........................
 * ..........................
 * ..........................
 * ..........................
 * ..........................
 * ..........................
 * ..........................
 * ...........s.........98765
 * .........................4
 * .........................3
 * .........................2
 * .........................1
 * .........................H
 * <p>
 * == L 25 ==
 * <p>
 * ..........................
 * ..........................
 * ..........................
 * ..........................
 * ..........................
 * ..........................
 * ..........................
 * ..........................
 * ..........................
 * ..........................
 * ..........................
 * ..........................
 * ..........................
 * ..........................
 * ..........................
 * ...........s..............
 * ..........................
 * ..........................
 * ..........................
 * ..........................
 * H123456789................
 * <p>
 * == U 20 ==
 * <p>
 * H.........................
 * 1.........................
 * 2.........................
 * 3.........................
 * 4.........................
 * 5.........................
 * 6.........................
 * 7.........................
 * 8.........................
 * 9.........................
 * ..........................
 * ..........................
 * ..........................
 * ..........................
 * ..........................
 * ...........s..............
 * ..........................
 * ..........................
 * ..........................
 * ..........................
 * ..........................
 * <p>
 * Now, the tail (9) visits 36 positions (including s) at least once:
 * <p>
 * ..........................
 * ..........................
 * ..........................
 * ..........................
 * ..........................
 * ..........................
 * ..........................
 * ..........................
 * ..........................
 * #.........................
 * #.............###.........
 * #............#...#........
 * .#..........#.....#.......
 * ..#..........#.....#......
 * ...#........#.......#.....
 * ....#......s.........#....
 * .....#..............#.....
 * ......#............#......
 * .......#..........#.......
 * ........#........#........
 * .........########.........
 * Simulate your complete series of motions on a larger rope with ten knots. How many positions does the tail of the rope visit at least once?
 */
public class Puzzle0902 {

    @Data
    @AllArgsConstructor
    private static class Coordinates {
        int x;
        int y;
    }

    public static void main(String... args) throws URISyntaxException, IOException {
        List<String> input = Files.readAllLines(Paths.get(Puzzle0401.class.getClassLoader().getResource("0901/input.txt").toURI()), Charset.defaultCharset());


        Set<Coordinates> visitedCoordinates = new LinkedHashSet<>();

        List<Coordinates> rope = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            rope.add(new Coordinates(0, 0));
        }


        visitedCoordinates.add(new Coordinates(0, 0));


        for (String line : input) {

            String command = line.split(" ")[0];
            int steps = Integer.valueOf(line.split(" ")[1]);

            switch (command) {
                case "U": {
                    move(visitedCoordinates, rope, false, 1, steps);
                    break;
                }
                case "D": {
                    move(visitedCoordinates, rope, false, -1, steps);
                    break;
                }
                case "R": {
                    move(visitedCoordinates, rope, true, 1, steps);
                    break;
                }
                case "L": {
                    move(visitedCoordinates, rope, true, -1, steps);
                    break;
                }

            }

        }

        int solution = visitedCoordinates.size();

        System.out.println("Solution: " + solution);

    }


    private static void move(Set<Coordinates> visitedCoordinates, List<Coordinates> rope, boolean isXAxis, int direction, int steps) {

        for (int i = 0; i < steps; i++) {
            if (isXAxis) {

                rope.get(0).setX(rope.get(0).getX() + direction);

                for (int n = 1; n < rope.size(); n++) {
                    Coordinates previous = rope.get(n - 1);
                    Coordinates current = rope.get(n);

                    if (isDetachted(current, previous)) {
                        // Diagonal?
                        if (current.getY() != previous.getY()) {
                            if (previous.getY() > current.getY()) {
                                current.setY(current.getY() + 1);
                            } else {
                                current.setY(current.getY() - 1);
                            }

                            // Select (needed) direction during diagonal move
                            if (current.getX() < previous.getX()) {
                                current.setX(current.getX() + 1);
                            } else if (current.getX() > previous.getX()) {
                                current.setX(current.getX() - 1);
                            }
                        } else {
                            current.setX(current.getX() + direction);
                        }
                    }
                }

            } else {

                rope.get(0).setY(rope.get(0).getY() + direction);

                for (int n = 1; n < rope.size(); n++) {
                    Coordinates previous = rope.get(n - 1);
                    Coordinates current = rope.get(n);

                    if (isDetachted(current, previous)) {
                        // Diagonal?
                        if (current.getX() != previous.getX()) {
                            if (previous.getX() > current.getX()) {
                                current.setX(current.getX() + 1);
                            } else {
                                current.setX(current.getX() - 1);
                            }

                            // Select (needed) direction during diagonal move
                            if (current.getY() < previous.getY()) {
                                current.setY(current.getY() + 1);
                            } else if (current.getY() > previous.getY()) {
                                current.setY(current.getY() - 1);
                            }
                        } else {
                            current.setY(current.getY() + direction);
                        }
                    }
                }

            }

            visitedCoordinates.add(new Coordinates(rope.get(rope.size() - 1).getX(), rope.get(rope.size() - 1).getY()));

        }

    }

    private static boolean isDetachted(Coordinates coord1, Coordinates coord2) {
        return (Math.abs(coord1.getX() - coord2.getX()) > 1 || Math.abs(coord1.getY() - coord2.getY()) > 1);
    }


}