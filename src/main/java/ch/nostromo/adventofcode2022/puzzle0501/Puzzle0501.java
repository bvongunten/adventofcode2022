package ch.nostromo.adventofcode2022.puzzle0501;

import ch.nostromo.adventofcode2022.puzzle0401.Puzzle0401;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.temporal.ValueRange;
import java.util.List;
import java.util.Stack;


/**
 * --- Day 5: Supply Stacks ---
 * The expedition can depart as soon as the final supplies have been unloaded from the ships. Supplies are stored in stacks of marked crates, but because the needed supplies are buried under many other crates, the crates need to be rearranged.
 * <p>
 * The ship has a giant cargo crane capable of moving crates between stacks. To ensure none of the crates get crushed or fall over, the crane operator will rearrange them in a series of carefully-planned steps. After the crates are rearranged, the desired crates will be at the top of each stack.
 * <p>
 * The Elves don't want to interrupt the crane operator during this delicate procedure, but they forgot to ask her which crate will end up where, and they want to be ready to unload them as soon as possible so they can embark.
 * <p>
 * They do, however, have a drawing of the starting stacks of crates and the rearrangement procedure (your puzzle input). For example:
 * <p>
 * [D]
 * [N] [C]
 * [Z] [M] [P]
 * 1   2   3
 * <p>
 * move 1 from 2 to 1
 * move 3 from 1 to 3
 * move 2 from 2 to 1
 * move 1 from 1 to 2
 * In this example, there are three stacks of crates. Stack 1 contains two crates: crate Z is on the bottom, and crate N is on top. Stack 2 contains three crates; from bottom to top, they are crates M, C, and D. Finally, stack 3 contains a single crate, P.
 * <p>
 * Then, the rearrangement procedure is given. In each step of the procedure, a quantity of crates is moved from one stack to a different stack. In the first step of the above rearrangement procedure, one crate is moved from stack 2 to stack 1, resulting in this configuration:
 * <p>
 * [D]
 * [N] [C]
 * [Z] [M] [P]
 * 1   2   3
 * In the second step, three crates are moved from stack 1 to stack 3. Crates are moved one at a time, so the first crate to be moved (D) ends up below the second and third crates:
 * <p>
 * [Z]
 * [N]
 * [C] [D]
 * [M] [P]
 * 1   2   3
 * Then, both crates are moved from stack 2 to stack 1. Again, because crates are moved one at a time, crate C ends up below crate M:
 * <p>
 * [Z]
 * [N]
 * [M]     [D]
 * [C]     [P]
 * 1   2   3
 * Finally, one crate is moved from stack 1 to stack 2:
 * <p>
 * [Z]
 * [N]
 * [D]
 * [C] [M] [P]
 * 1   2   3
 * The Elves just need to know which crate will end up on top of each stack; in this example, the top crates are C in stack 1, M in stack 2, and Z in stack 3, so you should combine these together and give the Elves the message CMZ.
 * <p>
 * After the rearrangement procedure completes, what crate ends up on top of each stack?
 */
public class Puzzle0501 {

    public static void main(String... args) throws URISyntaxException, IOException {
        List<String> input = Files.readAllLines(Paths.get(Puzzle0401.class.getClassLoader().getResource("0501/input.txt").toURI()), Charset.defaultCharset());

        int height = 8;
        int width = 9;

        Stack<String>[] stacks = readStacks(input, height, width);

        for (int i = height + 2; i < input.size(); i++) {
            computeOrder(stacks, input.get(i));
        }

        String solution = "";
        for (Stack<String> stack : stacks) {
            solution += stack.peek();
        }

        System.out.println("Solution: " + solution);

    }

    private static void computeOrder(Stack<String>[] stacks, String order) {
        int count = Integer.valueOf(order.substring(5, order.indexOf("from") -1));
        int from = Integer.valueOf(order.substring(order.indexOf("from") +5, order.indexOf("to") -1)) -1;
        int to = Integer.valueOf(order.substring(order.indexOf("to") +3)) -1;

        for (int i = 0; i < count; i++) {
            stacks[to].push(stacks[from].pop());
        }
    }

    private static Stack<String>[] readStacks(List<String> lines, int height, int width) {
        Stack<String>[] result = new Stack[width];

        for (int i = 0; i < width; i++) {
            Stack<String> col = new Stack<>();
            for (int x = height - 1; x >= 0; x--) {
                String crate = readCharAtPos(lines.get(x), i * 4 + 1);
                if (!crate.isBlank()) {
                    col.push(crate);
                }
            }
            result[i] = col;
        }

        return result;
    }

    private static String readCharAtPos(String line, int pos) {
        try {
            return line.substring(pos, pos + 1);
        } catch (StringIndexOutOfBoundsException ignore) {
            return "";
        }
    }

}
