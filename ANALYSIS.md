# Analysis

## 1. DSU

### Steps

1. Create `parents` and `size` arrays.
2. Initially every vertex is its own parent.
3. For every operation, find the roots of two vertices.
4. If the roots are different, join the smaller set to the larger set.
5. Decrease the number of components.
6. Store the current number of components and the size of the largest component.

### Invariant

Every vertex belongs to exactly one set. The root of every set represents the whole component.

### Correctness

The `find` method returns the root of a component and uses path compression.

The `union` method connects only different components. Union by size keeps the trees smaller.

After every successful union, the number of components decreases by one.

### Boundary cases

- Repeated union of already connected vertices.
- One vertex.
- No operations.
- Large number of operations.

### Complexity

`find` and `union` work in almost constant amortized time.

Time complexity:

```text
O(m * α(n))
```

Memory complexity:

```text
O(n)
```

---

# 2. Kruskal

### Steps

1. Copy all edges and save their original IDs.
2. Sort edges by weight.
3. Create a DSU structure.
4. Check edges from the smallest weight to the largest.
5. Add an edge if its vertices belong to different components.
6. Add its weight to the total cost.
7. Save its original edge ID.

### Invariant

The selected edges never create a cycle.

### Correctness

Kruskal always selects the currently cheapest edge that connects two different components.

Because an edge is only added when its vertices have different DSU roots, cycles cannot be created.

The result is a minimum spanning forest.

### Boundary cases

- Disconnected graph.
- Isolated vertices.
- Negative edge weights.
- Parallel edges.
- Loops.
- Empty edge list.

### Complexity

Sorting requires:

```text
O(m log m)
```

DSU operations are almost constant.

Total time complexity:

```text
O(m log m)
```

Memory complexity:

```text
O(n + m)
```

---

# 3. Prim

### Steps

1. Create an adjacency list.
2. Create a priority queue ordered by edge weight.
3. Start from an unvisited vertex.
4. Add its edges to the priority queue.
5. Select the smallest edge leading to an unvisited vertex.
6. Add the edge to the forest.
7. Repeat until the component is finished.
8. Restart Prim from another unvisited vertex.

### Invariant

Every selected edge connects the current visited part of a component with a new vertex.

### Correctness

The algorithm always chooses the cheapest available edge that expands the current tree.

Visited vertices are not added again, so a cycle is not created.

When a component finishes, the algorithm starts again from another vertex. Because of this, disconnected graphs and isolated vertices are also handled.

### Boundary cases

- Disconnected graph.
- Isolated vertices.
- Negative weights.
- Parallel edges.
- Empty edge list.

### Complexity

Using a priority queue:

```text
O(m log m)
```

Memory complexity:

```text
O(n + m)
```

---

# 4. Strongly Connected Components

Kosaraju's algorithm is used.

### Steps

1. Build the original graph.
2. Build the transpose graph.
3. Run the first DFS and store vertices by finish time.
4. Process vertices in reverse finish order.
5. Run DFS on the transpose graph.
6. Give all vertices reached by one DFS the same component number.
7. Check original edges and build the condensation graph.
8. Ignore duplicate edges between the same components.

### Invariant

During the second pass, every DFS visits exactly one strongly connected component.

### Correctness

The first pass produces the vertices in finish-time order.

After reversing this order and running DFS on the transpose graph, vertices belonging to the same strongly connected component are visited together.

Edges connecting different components are added to the condensation graph.

### Boundary cases

- Graph with no edges.
- One large strongly connected component.
- Every vertex in its own component.
- Loops.
- Parallel edges.
- Isolated vertices.

### Complexity

Every vertex and edge is processed a constant number of times.

Time complexity:

```text
O(n + m)
```

Memory complexity:

```text
O(n + m)
```

---

# 5. Topological Sort

Three-state DFS is used.

The states are:

```text
0 - not visited
1 - currently being processed
2 - finished
```

### Steps

1. Build an adjacency list.
2. Start DFS from every unvisited vertex.
3. Mark a vertex as state `1` when DFS enters it.
4. If an edge leads to another state `1` vertex, a cycle exists.
5. Mark a vertex as state `2` when it is finished.
6. Add finished vertices to the order.
7. Reverse the finish order.

### Invariant

A vertex with state `1` belongs to the current DFS path.

### Correctness

If DFS finds an edge from a state `1` vertex to another state `1` vertex, there is a directed cycle.

If there is no cycle, reversing DFS finish order gives a valid topological order.

For every directed edge `u -> v`, vertex `u` appears before `v`.

### Boundary cases

- Empty graph.
- Isolated vertices.
- One vertex.
- Directed cycle.
- Self-loop.
- Multiple disconnected DAG components.

### Complexity

Time complexity:

```text
O(n + m)
```

Memory complexity:

```text
O(n + m)
```

---

# 6. DAG Shortest Paths

### Steps

1. Check the complete graph for cycles using topological sort.
2. If a cycle exists, return `acyclic = false`.
3. Build a weighted adjacency list.
4. Set the source distance to `0`.
5. Set other distances to `null`.
6. Process vertices in topological order.
7. Relax all outgoing edges of every reachable vertex.
8. Store the original edge ID that produced the shortest distance.

### Invariant

When a reachable vertex is processed in topological order, all possible paths from earlier vertices to it have already been considered.

### Correctness

In a DAG, every edge goes forward in topological order.

Because vertices are processed in this order, the shortest known distance is final when its outgoing edges are relaxed.

Negative edge weights are also supported because there are no cycles.

### Boundary cases

- Unreachable vertices.
- Negative weights.
- Parallel edges.
- Source with no outgoing edges.
- Cycle in a part of the graph unreachable from the source.
- Single vertex graph.

### Complexity

Topological sort:

```text
O(n + m)
```

Relaxation:

```text
O(n + m)
```

Total:

```text
O(n + m)
```

Memory complexity:

```text
O(n + m)
```

---

# Route Reconstruction

The DAG implementation stores the original edge ID used to reach every vertex.

The helper starts at the target vertex and follows `parent_edge` backwards until it reaches the source.

After this, the route is reversed.

### Reachable target

Example:

```text
source = 0
target = 3

parent edges produce:

0 -> 1 -> 2 -> 3
```

Result:

```text
[0, 1, 2, 3]
```

### Source as target

Example:

```text
source = 0
target = 0
```

Result:

```text
[0]
```

### Unreachable target

If:

```text
parent_edge[target] = -1
```

and the target is not the source, the result is an empty route.

```text
[]
```

### Parallel edges

If there are multiple edges between two vertices, `parent_edge` stores the original ID of the edge that was actually used during relaxation.

This makes it possible to reconstruct the selected route correctly.

---

# State Reset

All arrays, lists, queues and graph structures are created inside the algorithm methods.

There is no shared mutable algorithm state between requests.

Because of this, one request cannot affect another request.

---

# Deep Graphs

The assignment can contain graphs with many vertices.

Recursive DFS can cause `StackOverflowError` in Java on a very deep graph.

For SCC and topological sort, explicit arrays are used as DFS stacks instead of Java recursion.

This allows deep chains to be processed without depending on the Java call stack.

---

# Kruskal and Prim Comparison

Both algorithms find a minimum spanning forest, but they use different approaches.

Kruskal sorts all edges first and uses DSU.

Prim grows the forest using a priority queue.

## Benchmark environment

Language:

```text
Java 17
```

Computer:

```text
Apple MacBook Air
```

Timer:

```text
System.nanoTime()
```

Warm-up:

A warm-up run should be performed before recording the final measurements.

Compilation time is not included.

## Test sizes

Three graph sizes are used:

```text
Small
Medium
Large
```

Each algorithm should be executed at least three times for every graph size.

### Small graph

Kruskal:

```text
Run 1: not measured
Run 2: not measured
Run 3: not measured
Median: not measured
```

Prim:

```text
Run 1: not measured
Run 2: not measured
Run 3: not measured
Median: not measured
```

### Medium graph

Kruskal:

```text
Run 1: not measured
Run 2: not measured
Run 3: not measured
Median: not measured
```

Prim:

```text
Run 1: not measured
Run 2: not measured
Run 3: not measured
Median: not measured
```

### Large graph

Kruskal:

```text
Run 1: not measured
Run 2: not measured
Run 3: not measured
Median: not measured
```

Prim:

```text
Run 1: not measured
Run 2: not measured
Run 3: not measured
Median: not measured
```

The benchmark values should be filled after running the algorithms because real execution times depend on the computer and graph data.

---

# AI Assistance

AI tools were used as assistance during the project.

AI was used to:

- review algorithm implementations;
- help find and explain errors;
- suggest possible fixes and implementation approaches;
- help prepare and organize Markdown documentation and test descriptions.

The final implementation was reviewed and adapted for the project before submission.