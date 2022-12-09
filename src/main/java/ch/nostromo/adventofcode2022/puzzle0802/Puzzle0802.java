package ch.nostromo.adventofcode2022.puzzle0802;

import ch.nostromo.adventofcode2022.puzzle0401.Puzzle0401;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;


/**
 * --- Day 8: Treetop Tree House ---
 * The expedition comes across a peculiar patch of tall trees all planted carefully in a grid. The Elves explain that a previous expedition planted these trees as a reforestation effort. Now, they're curious if this would be a good location for a tree house.
 * <p>
 * First, determine whether there is enough tree cover here to keep a tree house hidden. To do this, you need to count the number of trees that are visible from outside the grid when looking directly along a row or column.
 * <p>
 * The Elves have already launched a quadcopter to generate a map with the height of each tree (your puzzle input). For example:
 * <p>
 * 30373
 * 25512
 * 65332
 * 33549
 * 35390
 * Each tree is represented as a single digit whose value is its height, where 0 is the shortest and 9 is the tallest.
 * <p>
 * A tree is visible if all of the other trees between it and an edge of the grid are shorter than it. Only consider trees in the same row or column; that is, only look up, down, left, or right from any given tree.
 * <p>
 * All of the trees around the edge of the grid are visible - since they are already on the edge, there are no trees to block the view. In this example, that only leaves the interior nine trees to consider:
 * <p>
 * The top-left 5 is visible from the left and top. (It isn't visible from the right or bottom since other trees of height 5 are in the way.)
 * The top-middle 5 is visible from the top and right.
 * The top-right 1 is not visible from any direction; for it to be visible, there would need to only be trees of height 0 between it and an edge.
 * The left-middle 5 is visible, but only from the right.
 * The center 3 is not visible from any direction; for it to be visible, there would need to be only trees of at most height 2 between it and an edge.
 * The right-middle 3 is visible from the right.
 * In the bottom row, the middle 5 is visible, but the 3 and 4 are not.
 * With 16 trees visible on the edge and another 5 visible in the interior, a total of 21 trees are visible in this arrangement.
 * <p>
 * Consider your map; how many trees are visible from outside the grid?
 * <p>
 * Your puzzle answer was 1681.
 * <p>
 * The first half of this puzzle is complete! It provides one gold star: *
 * <p>
 * --- Part Two ---
 * Content with the amount of tree cover available, the Elves just need to know the best spot to build their tree house: they would like to be able to see a lot of trees.
 * <p>
 * To measure the viewing distance from a given tree, look up, down, left, and right from that tree; stop if you reach an edge or at the first tree that is the same height or taller than the tree under consideration. (If a tree is right on the edge, at least one of its viewing distances will be zero.)
 * <p>
 * The Elves don't care about distant trees taller than those found by the rules above; the proposed tree house has large eaves to keep it dry, so they wouldn't be able to see higher than the tree house anyway.
 * <p>
 * In the example above, consider the middle 5 in the second row:
 * <p>
 * 30373
 * 25512
 * 65332
 * 33549
 * 35390
 * Looking up, its view is not blocked; it can see 1 tree (of height 3).
 * Looking left, its view is blocked immediately; it can see only 1 tree (of height 5, right next to it).
 * Looking right, its view is not blocked; it can see 2 trees.
 * Looking down, its view is blocked eventually; it can see 2 trees (one of height 3, then the tree of height 5 that blocks its view).
 * A tree's scenic score is found by multiplying together its viewing distance in each of the four directions. For this tree, this is 4 (found by multiplying 1 * 1 * 2 * 2).
 * <p>
 * However, you can do even better: consider the tree of height 5 in the middle of the fourth row:
 * <p>
 * 30373
 * 25512
 * 65332
 * 33549
 * 35390
 * Looking up, its view is blocked at 2 trees (by another tree with a height of 5).
 * Looking left, its view is not blocked; it can see 2 trees.
 * Looking down, its view is also not blocked; it can see 1 tree.
 * Looking right, its view is blocked at 2 trees (by a massive tree of height 9).
 * This tree's scenic score is 8 (2 * 2 * 1 * 2); this is the ideal spot for the tree house.
 * <p>
 * Consider each tree on your map. What is the highest scenic score possible for any tree?
 */
public class Puzzle0802 {

    public static void main(String... args) throws URISyntaxException, IOException {
        List<String> input = Files.readAllLines(Paths.get(Puzzle0401.class.getClassLoader().getResource("0801/input.txt").toURI()), Charset.defaultCharset());

        int height = input.size();
        int width = input.get(0).length();

        int[][] forrest = createForrest(input, height, width);
        int solution = countVisibleTrees(forrest, height, width);

        System.out.println("Solution: " + solution);


    }

    private static int countVisibleTrees(int[][] forrest, int height, int width) {
        int result = 0;

        for (int heightIdx = 1; heightIdx < height - 1; heightIdx++) {
            for (int widthIdx = 1; widthIdx < width - 1; widthIdx++) {


                int northDistance = 1;
                for (int i = heightIdx - 1; i >0 ; i--) {
                    if (forrest[i][widthIdx] >= forrest[heightIdx][widthIdx]) {
                        break;
                    }
                    northDistance++;

                }

                int southDitstance = 1;
                for (int i = heightIdx + 1; i < height -1; i++) {
                    if (forrest[i][widthIdx] >= forrest[heightIdx][widthIdx]) {
                        break;
                    }
                    southDitstance++;
                }

                int westDistance = 1;
                for (int i = widthIdx - 1; i > 0; i--) {
                    if (forrest[heightIdx][i] >= forrest[heightIdx][widthIdx]) {
                        break;
                    }
                    westDistance++;

                }

                int eastDistance = 1;
                for (int i = widthIdx + 1; i < width -1; i++) {
                    if (forrest[heightIdx][i] >= forrest[heightIdx][widthIdx]) {
                        break;
                    }

                    eastDistance++;
                }

                int scenicScore  = northDistance * southDitstance * eastDistance * westDistance;

                if (scenicScore > result) {
                    result = scenicScore;

            }
        }
    }


        return result;

}


    private static int[][] createForrest(List<String> lines, int height, int width) {
        int[][] forrest = new int[height][width];

        for (int i = 0; i < lines.size(); i++) {
            String line = lines.get(i);
            for (int n = 0; n < line.length(); n++) {
                forrest[i][n] = Integer.valueOf(line.substring(n, n + 1));
            }
        }

        return forrest;
    }


}
