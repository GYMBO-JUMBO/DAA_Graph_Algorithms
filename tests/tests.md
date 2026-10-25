# Tests

## DSU

Input:
```json
{"task":"dsu","n":5,"operations":[[0,1],[1,2],[3,4],[2,4]]}
```

Expected:
```text
states:
[4,2]
[3,3]
[2,3]
[1,5]
```

Actual output:
```text
states:
[4,2]
[3,3]
[2,3]
[1,5]
```

Result:

```text
Passed
```

---

## Kruskal

Input:
```json
{"task":"kruskal","n":4,"edges":[[0,1,1],[1,2,2],[2,3,3],[0,3,10],[0,2,5]]}
```

Expected:
```json
{
  "cost": 6,
  "edge_ids": [0,1,2],
  "components": 1
}
```

Actual output:
```json
{
  "cost": 6,
  "edge_ids": [0,1,2],
  "components": 1
}
```

Result:

```text
Passed
```

---

## Prim

Input:
```json
{"task":"prim","n":5,"edges":[[0,1,2],[1,2,3],[0,2,5],[3,4,1]]}
```

Expected:
```text
cost = 6
edge_ids = [0,1,3]
components = 2
```

Actual output:
```text
cost = 6
edge_ids = [0,1,3]
components = 2
```

Result:

```text
Passed
```

---

## SCC

Input:
```json
{"task":"scc","n":5,"edges":[[0,1],[1,2],[2,0],[2,3],[3,4],[4,3]]}
```

Expected:
```text
Two strongly connected components:
[0,1,2]
[3,4]

One condensation edge between the two components.
```

Actual output:
```text
component = [0,0,0,1,1]
condensation = [[0,1]]
```

Result:

```text
Passed
```

---

## Topological Sort

Input:
```json
{"task":"topo","n":5,"edges":[[0,1],[0,2],[1,3],[2,3],[3,4]]}
```

Expected:
```text
acyclic = true
A valid order, for example:
[0,2,1,3,4]
```

Actual output:
```text
acyclic = true
order = [0,2,1,3,4]
```

Result:

```text
Passed
```

---

## Topological Sort With Cycle

Input:
```json
{"task":"topo","n":3,"edges":[[0,1],[1,2],[2,0]]}
```

Expected:
```text
acyclic = false
order = []
```

Actual output:
```text
acyclic = false
order = []
```

Result:

```text
Passed
```

---

## DAG Shortest Path

Input:
```json
{"task":"dag","n":5,"source":0,"edges":[[0,1,2],[0,2,4],[1,2,1],[1,3,7],[2,3,3]]}
```

Expected:
```text
acyclic = true
distance = [0,2,3,6,null]
parent_edge = [-1,0,2,4,-1]
```

Actual output:
```text
acyclic = true
distance = [0,2,3,6,null]
parent_edge = [-1,0,2,4,-1]
```

Result:

```text
Passed
```

---

## DAG With Cycle

Input:
```json
{"task":"dag","n":4,"source":0,"edges":[[0,1,1],[1,2,1],[2,1,1],[2,3,1]]}
```

Expected:
```text
acyclic = false
distance = []
parent_edge = []
```

Actual output:
```text
acyclic = false
distance = []
parent_edge = []
```

Result:

```text
Passed
```

---

## Boundary Test

Input:
```json
{"task":"kruskal","n":3,"edges":[]}
```

Expected:
```text
cost = 0
edge_ids = []
components = 3
```

Actual output:
```text
cost = 0
edge_ids = []
components = 3
```

Result:

```text
Passed
```

---

# Commands

Setup:

```bash
python tools/run.py --setup
```

Run public checks:

```bash
python tools/run.py --check
```