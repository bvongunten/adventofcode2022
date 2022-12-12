package ch.nostromo.adventofcode2022.puzzle1202;

import ch.nostromo.adventofcode2022.puzzle0401.Puzzle0401;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;


/**
 * --- Day 12: Hill Climbing Algorithm ---
 * You try contacting the Elves using your handheld device, but the river you're following must be too low to get a decent signal.
 * <p>
 * You ask the device for a heightmap of the surrounding area (your puzzle input). The heightmap shows the local area from above broken into a grid; the elevation of each square of the grid is given by a single lowercase letter, where a is the lowest elevation, b is the next-lowest, and so on up to the highest elevation, z.
 * <p>
 * Also included on the heightmap are marks for your current position (S) and the location that should get the best signal (E). Your current position (S) has elevation a, and the location that should get the best signal (E) has elevation z.
 * <p>
 * You'd like to reach E, but to save energy, you should do it in as few steps as possible. During each step, you can move exactly one square up, down, left, or right. To avoid needing to get out your climbing gear, the elevation of the destination square can be at most one higher than the elevation of your current square; that is, if your current elevation is m, you could step to elevation n, but not to elevation o. (This also means that the elevation of the destination square can be much lower than the elevation of your current square.)
 * <p>
 * For example:
 * <p>
 * Sabqponm
 * abcryxxl
 * accszExk
 * acctuvwj
 * abdefghi
 * Here, you start in the top-left corner; your goal is near the middle. You could start by moving down or right, but eventually you'll need to head toward the e at the bottom. From there, you can spiral around to the goal:
 * <p>
 * v..v<<<<
 * >v.vv<<^
 * .>vv>E^^
 * ..v>>>^^
 * ..>>>>>^
 * In the above diagram, the symbols indicate whether the path exits each square moving up (^), down (v), left (<), or right (>). The location that should get the best signal is still E, and . marks unvisited squares.
 * <p>
 * This path reaches the goal in 31 steps, the fewest possible.
 * <p>
 * What is the fewest steps required to move from your current position to the location that should get the best signal?
 * Your puzzle answer was 352.
 * <p>
 * The first half of this puzzle is complete! It provides one gold star: *
 * <p>
 * --- Part Two ---
 * As you walk up the hill, you suspect that the Elves will want to turn this into a hiking trail. The beginning isn't very scenic, though; perhaps you can find a better starting point.
 * <p>
 * To maximize exercise while hiking, the trail should start as low as possible: elevation a. The goal is still the square marked E. However, the trail should still be direct, taking the fewest steps to reach its goal. So, you'll need to find the shortest path from any square at elevation a to the square marked E.
 * <p>
 * Again consider the example from above:
 * <p>
 * Sabqponm
 * abcryxxl
 * accszExk
 * acctuvwj
 * abdefghi
 * Now, there are six choices for starting position (five marked a, plus the square marked S that counts as being at elevation a). If you start at the bottom-left square, you can reach the goal most quickly:
 * <p>
 * ...v<<<<
 * ...vv<<^
 * ...v>E^^
 * .>v>>>^^
 * >^>>>>>^
 * This path reaches the goal in only 29 steps, the fewest possible.
 * <p>
 * What is the fewest steps required to move starting from any square with elevation a to the location that should get the best signal?
 * <p>
 * Answer:
 */
public class Puzzle1202 {

    public static void main(String... args) throws URISyntaxException, IOException {
        List<String> input = Files.readAllLines(Paths.get(Puzzle0401.class.getClassLoader().getResource("1201/input.txt").toURI()), Charset.defaultCharset());

        int height = input.size();
        int width = input.get(0).length();

        int[][] map = createMap(input, height, width);
        Node[][] nodes = new Node[height][width];

        createNodes(nodes, map);

        List<Node> startNodes = getNodesByNumber(nodes, map, 1);
        Node endNode = getNodesByNumber(nodes, map, 27).get(0);

        int solution = Integer.MAX_VALUE;
        for (Node startNode : startNodes) {
            calculateShortestPathFromSource(startNode);
            solution = Math.min(endNode.distance, solution);
        }

        System.out.println("Solution: " + solution);


    }

    private static List<Node> getNodesByNumber(Node[][] nodes, int[][] map, int number) {

        List<Node> result = new ArrayList<>();
        for (int y = 0; y < map.length; y++) {
            for (int x = 0; x < map[y].length; x++) {
                if (map[y][x] == number) {
                    result.add(nodes[y][x]);
                }
            }
        }
        return result;
    }

    private static void createNodes(Node[][] nodes, int[][] map) {
        for (int y = 0; y < map.length; y++) {
            for (int x = 0; x < map[y].length; x++) {
                nodes[y][x] = new Node();
            }
        }

        // Link nodes
        for (int y = 0; y < map.length; y++) {
            for (int x = 0; x < map[y].length; x++) {

                int currentChar = map[y][x];

                if (y > 0 && map[y - 1][x] - currentChar <= 1) {
                    nodes[y][x].addDestination(nodes[y - 1][x], 1);
                }
                if (y < map.length - 1 && map[y + 1][x] - currentChar <= 1) {
                    nodes[y][x].addDestination(nodes[y + 1][x], 1);
                }
                if (x > 0 && map[y][x - 1] - currentChar <= 1) {
                    nodes[y][x].addDestination(nodes[y][x - 1], 1);
                }
                if (x < map[y].length - 1 && map[y][x + 1] - currentChar <= 1) {
                    nodes[y][x].addDestination(nodes[y][x + 1], 1);
                }
            }
        }

    }

    private static int[][] createMap(List<String> lines, int height, int width) {
        int[][] map = new int[height][width];

        for (int i = 0; i < lines.size(); i++) {
            String line = lines.get(i);
            for (int n = 0; n < line.length(); n++) {
                char character = line.charAt(n);
                if (character == 'S') {
                    map[i][n] = 0;
                } else if (character == 'E') {
                    map[i][n] = 27;
                } else {
                    int code = (int) character - 96;
                    map[i][n] = code;
                }
            }
        }

        return map;
    }


    // Dijkstra from Baeldung
    public static class Node {

        private List<Node> shortestPath = new LinkedList<>();
        private Integer distance = Integer.MAX_VALUE;
        Map<Node, Integer> adjacentNodes = new HashMap<>();

        public void addDestination(Node destination, int distance) {
            adjacentNodes.put(destination, distance);
        }

        public Integer getDistance() {
            return distance;
        }

        public void setDistance(Integer distance) {
            this.distance = distance;
        }

        public Map<Node, Integer> getAdjacentNodes() {
            return adjacentNodes;
        }

        public List<Node> getShortestPath() {
            return shortestPath;
        }

        public void setShortestPath(List<Node> shortestPath) {
            this.shortestPath = shortestPath;
        }
    }

    public static void calculateShortestPathFromSource(Node source) {
        source.setDistance(0);

        Set<Node> settledNodes = new HashSet<>();
        Set<Node> unsettledNodes = new HashSet<>();

        unsettledNodes.add(source);

        while (unsettledNodes.size() != 0) {
            Node currentNode = getLowestDistanceNode(unsettledNodes);
            unsettledNodes.remove(currentNode);
            for (Map.Entry<Node, Integer> adjacencyPair :
                    currentNode.getAdjacentNodes().entrySet()) {
                Node adjacentNode = adjacencyPair.getKey();
                Integer edgeWeight = adjacencyPair.getValue();
                if (!settledNodes.contains(adjacentNode)) {
                    CalculateMinimumDistance(adjacentNode, edgeWeight, currentNode);
                    unsettledNodes.add(adjacentNode);
                }
            }
            settledNodes.add(currentNode);
        }
    }

    private static Node getLowestDistanceNode(Set<Node> unsettledNodes) {
        Node lowestDistanceNode = null;
        int lowestDistance = Integer.MAX_VALUE;
        for (Node node : unsettledNodes) {
            int nodeDistance = node.getDistance();
            if (nodeDistance < lowestDistance) {
                lowestDistance = nodeDistance;
                lowestDistanceNode = node;
            }
        }
        return lowestDistanceNode;
    }

    private static void CalculateMinimumDistance(Node evaluationNode, Integer edgeWeigh, Node sourceNode) {
        Integer sourceDistance = sourceNode.getDistance();
        if (sourceDistance + edgeWeigh < evaluationNode.getDistance()) {
            evaluationNode.setDistance(sourceDistance + edgeWeigh);
            LinkedList<Node> shortestPath = new LinkedList<>(sourceNode.getShortestPath());
            shortestPath.add(sourceNode);
            evaluationNode.setShortestPath(shortestPath);
        }
    }

}
