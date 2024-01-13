import java.util.*;

public class App {
    public static void main(String[] args) {
        DirectedGraph graph = new DirectedGraph();
        graph.addEdge(1, 3);
        graph.addEdge(1, 4);
        graph.addEdge(2, 1);
        graph.addEdge(2, 3);
        graph.addEdge(3, 4);
        graph.addEdge(4, 1);
        graph.addEdge(4, 2);

        System.out.println("BFS Traversal:");
        bfsTraversal(graph, 1);

        System.out.println("\nDFS Traversal:");
        dfsTraversal(graph, 1);

        System.out.println("\nCycle Detection:");
        boolean hasCycle = hasCycle(graph);
        System.out.println("Graph has a cycle: " + hasCycle);

        System.out.println("All Cycles in the Graph:");
        printAllCycles(graph);

        System.out.println("Bipartiteness Check:");
        boolean isBipartite = isBipartite(graph, 1);
        System.out.println("Graph is bipartite: " + isBipartite);
    }

    private static void bfsTraversal(DirectedGraph graph, int start) {
        Queue<Integer> queue = new LinkedList<>();
        Set<Integer> visited = new HashSet<>();

        queue.add(start);
        visited.add(start);

        while (!queue.isEmpty()) {
            int current = queue.poll();
            System.out.print(current + " ");

            List<Integer> neighbors = graph.getAdjacencyList().getOrDefault(current, Collections.emptyList());
            for (int neighbor : neighbors) {
                if (!visited.contains(neighbor)) {
                    queue.add(neighbor);
                    visited.add(neighbor);
                }
            }
        }
    }

    private static void dfsTraversal(DirectedGraph graph, int start) {
        Set<Integer> visited = new HashSet<>();
        dfsHelper(graph, start, visited);
    }

    private static void dfsHelper(DirectedGraph graph, int current, Set<Integer> visited) {
        if (visited.contains(current)) {
            return;
        }

        System.out.print(current + " ");
        visited.add(current);

        List<Integer> neighbors = graph.getAdjacencyList().getOrDefault(current, Collections.emptyList());
        for (int neighbor : neighbors) {
            dfsHelper(graph, neighbor, visited);
        }
    }

    private static boolean hasCycle(DirectedGraph graph) {
        Set<Integer> visited = new HashSet<>();
        Set<Integer> recStack = new HashSet<>();

        for (int node : graph.getAdjacencyList().keySet()) {
            if (!visited.contains(node)) {
                if (hasCycleHelper(graph, node, visited, recStack)) {
                    return true;
                }
            }
        }
        return false;
    }

    private static boolean hasCycleHelper(DirectedGraph graph, int current, Set<Integer> visited, Set<Integer> recStack) {
        visited.add(current);
        recStack.add(current);

        List<Integer> neighbors = graph.getAdjacencyList().getOrDefault(current, Collections.emptyList());
        for (int neighbor : neighbors) {
            if (recStack.contains(neighbor)) {
                return true; // Cycle detected
            }
            if (!visited.contains(neighbor)) {
                if (hasCycleHelper(graph, neighbor, visited, recStack)) {
                    return true;
                }
            }
        }

        recStack.remove(current); // Backtrack
        return false;
    }

    private static void printAllCycles(DirectedGraph graph) {
        Set<Integer> visited = new HashSet<>();
        Set<Integer> recStack = new HashSet<>();

        for (int node : graph.getAdjacencyList().keySet()) {
            if (!visited.contains(node)) {
                dfsPrintCycles(graph, node, visited, recStack, new ArrayList<>());
            }
        }
    }

    private static void dfsPrintCycles(DirectedGraph graph, int current, Set<Integer> visited, Set<Integer> recStack, List<Integer> currentPath) {
        visited.add(current);
        recStack.add(current);
        currentPath.add(current);

        List<Integer> neighbors = graph.getAdjacencyList().getOrDefault(current, Collections.emptyList());
        for (int neighbor : neighbors) {
            if (recStack.contains(neighbor)) {
                // Cycle detected
                printCycle(neighbor, recStack, currentPath);
            }
            if (!visited.contains(neighbor)) {
                dfsPrintCycles(graph, neighbor, visited, recStack, currentPath);
            }
        }

        recStack.remove(current); // Backtrack
        currentPath.remove(currentPath.size() - 1);
    }

    private static void printCycle(int start, Set<Integer> recStack, List<Integer> currentPath) {
        List<Integer> cycle = new ArrayList<>(currentPath);
        int startIndex = cycle.indexOf(start);
        int endIndex = cycle.size();
        cycle = cycle.subList(startIndex, endIndex);

        System.out.println("Cycle: " + cycle);
    }

    private static boolean isBipartite(DirectedGraph graph, int start) {
        Map<Integer, Integer> colorMap = new HashMap<>();
        Queue<Integer> queue = new LinkedList<>();

        queue.add(start);
        colorMap.put(start, 0);

        while (!queue.isEmpty()) {
            int current = queue.poll();
            int currentColor = colorMap.get(current);

            List<Integer> neighbors = graph.getAdjacencyList().getOrDefault(current, Collections.emptyList());
            for (int neighbor : neighbors) {
                if (!colorMap.containsKey(neighbor)) {
                    int neighborColor = 1 - currentColor; // Alternate colors
                    colorMap.put(neighbor, neighborColor);
                    queue.add(neighbor);
                } else if (colorMap.get(neighbor) == currentColor) {
                    return false; // Not bipartite
                }
            }
        }

        return true; // Bipartite
    }
}

class DirectedGraph {
    private Map<Integer, List<Integer>> adjacencyList;

    public DirectedGraph() {
        this.adjacencyList = new HashMap<>();
    }

    public void addEdge(int from, int to) {
        adjacencyList.computeIfAbsent(from, k -> new ArrayList<>()).add(to);
    }

    public Map<Integer, List<Integer>> getAdjacencyList() {
        return adjacencyList;
    }
}
/*
 * An undirected graph is a tree if and only if it is connected and has no cycles. To determine whether an undirected graph is a tree, you can use depth-first search (DFS) or breadth-first search (BFS) to traverse the graph and check for the presence of cycles and connectivity.

Here are the steps to determine if an undirected graph is a tree:

1. **Check for Connectivity:**
   - Run DFS or BFS from any vertex in the graph.
   - If all vertices are visited during the traversal, the graph is connected. If not, the graph is not a tree.

2. **Check for Cycles:**
   - During the traversal, if you encounter a visited vertex that is not the parent of the current vertex, then there is a cycle in the graph.
   - If a cycle is found at any point, the graph is not a tree.

3. **Final Check:**
   - After the traversal, if the graph is connected and has no cycles, then it is a tree.

The running time for determining whether an undirected graph is a tree using DFS or BFS is O(V + E), where V is the number of vertices and E is the number of edges in the graph. Both DFS and BFS visit each vertex and edge once.

If the algorithm detects a cycle during the traversal, it can terminate early, making the running time even faster in practice.

In summary, the algorithm checks for connectivity and the absence of cycles in the graph, making it a tree if these conditions are met. The running time is linear with respect to the number of vertices and edges in the graph.
 */