import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
public final class Algorithms {
	static Map<String, Object> dsu(Map<String, Object> input) {
		int n = Input.vertexCount(input);
		int[][] operations = Input.operations(input);
		int[] parents = new int[n];
		int[] size = new int[n];
		for(int i = 0; i < n; i++){
			parents[i] = i;
			size[i] = 1;
		}
		int components = n;
		int largest = 1;
		List<int[]> states = new ArrayList<>();
		for(int i = 0; i < operations.length; i++) {
			int u = operations[i][0];
			int v = operations[i][1];
			boolean united = union(parents, size, u, v);
			if(united) {
				components--;
				int root = find(parents, u);
				largest = Math.max(largest, size[root]);
			}
			states.add(new int[]{components, largest});
		}
		Map<String, Object> result = new HashMap<>();
		result.put("states", states);
		return result;
	}
	static int find(int[] parents, int x){
		if(parents[x] == x){
			return x;
		}
		parents[x] = find(parents, parents[x]);
		return parents[x];
	}
	static boolean union(int[] parents, int[] size, int a, int b){
		int coreA = find(parents, a);
		int coreB = find(parents, b);
		if(coreA == coreB){
			return false;
		}
		int sizeA = size[coreA];
		int sizeB = size[coreB];
		if(sizeA >= sizeB){
			parents[coreB] = coreA;
			size[coreA] = size[coreA] + size[coreB];
		}
		else{
			parents[coreA] = coreB;
			size[coreB] = size[coreB] + size[coreA];
		}
		return true;
	}
	static Map<String, Object> kruskal(Map<String, Object> input) {
		int n = Input.vertexCount(input);
		int[][] edges = Input.edges(input);
		int[][] sortedEdges = new int[edges.length][4];
		for(int i = 0; i < edges.length; i++){
			sortedEdges[i][0] = edges[i][0];
			sortedEdges[i][1] = edges[i][1];
			sortedEdges[i][2] = edges[i][2];
			sortedEdges[i][3] = i;
		}
		Arrays.sort(sortedEdges, Comparator.comparingInt(edge -> edge[2]));
		int[] parents = new int[n];
		int[] size = new int[n];
		List<Integer> edgeIds = new ArrayList<>();
		int components = n;
		long cost = 0;
		for(int i = 0; i < n; i++){
			parents[i] = i;
			size[i] = 1;
		}
		for(int i = 0; i < sortedEdges.length; i++) {
			int u = sortedEdges[i][0];
			int v = sortedEdges[i][1];
			int rootU = find(parents, u);
			int rootV = find(parents, v);
			if(rootU == rootV){
				continue;
			}
			else{
				union(parents, size, rootU, rootV);
				--components;
				cost += sortedEdges[i][2];
				edgeIds.add(sortedEdges[i][3]);
			}
		}
		Map<String, Object> result = new HashMap<>();
		result.put("cost", cost);
		result.put("edge_ids", edgeIds);
		result.put("components", components);
		return result;
	}
	static Map<String, Object> prim(Map<String, Object> input) {
		int n = Input.vertexCount(input);
		int[][] edges = Input.edges(input);
		List<int[]>[] graph = new ArrayList[n];
		for(int i = 0; i < n; i++){
			graph[i] = new ArrayList<>();
		}
		for(int i = 0; i < edges.length; i++){
			int u = edges[i][0];
			int v = edges[i][1];
			int weight = edges[i][2];

			graph[u].add(new int[]{v, weight, i});
			graph[v].add(new int[]{u, weight, i});
		}
		boolean[] visited = new boolean[n];
		PriorityQueue<int[]> queue = new PriorityQueue<>(Comparator.comparingInt(edge -> edge[1]));
		List<Integer> edgeIds = new ArrayList<>();
		long cost = 0;
		int components = 0;
		for(int start = 0; start < n; start++){
			if(visited[start]){
				continue;
			}
			components++;
			visited[start] = true;
			for(int[] edge : graph[start]){
				queue.add(edge);
			}
			while(!queue.isEmpty()){
				int[] edge = queue.poll();
				int v = edge[0];
				int weight = edge[1];
				int id = edge[2];
				if(visited[v]){
					continue;
				}
				visited[v] = true;
				cost += weight;
				edgeIds.add(id);
				for(int[] next : graph[v]){
					if(!visited[next[0]]){
						queue.add(next);
					}
				}
			}
		}
		Map<String, Object> result = new HashMap<>();
		result.put("cost", cost);
		result.put("edge_ids", edgeIds);
		result.put("components", components);
		return result;
	}
	static Map<String, Object> scc(Map<String, Object> input) {
		int n = Input.vertexCount(input);
		int[][] edges = Input.edges(input);
		List<Integer>[] graph = new ArrayList[n];
		List<Integer>[] reversed = new ArrayList[n];
		for(int i = 0; i < n; i++){
			graph[i] = new ArrayList<>();
			reversed[i] = new ArrayList<>();
		}
		for(int[] edge : edges){
			int u = edge[0];
			int v = edge[1];
			graph[u].add(v);
			reversed[v].add(u);
		}
		boolean[] visited = new boolean[n];
		List<Integer> order = new ArrayList<>();
		int[] stack = new int[n];
		int[] position = new int[n];
		for(int start = 0; start < n; start++){
			if(visited[start]){
				continue;
			}
			int top = 0;
			stack[top] = start;
			position[top] = 0;
			visited[start] = true;
			while(top >= 0){
				int current = stack[top];
				if(position[top] < graph[current].size()){
					int next = graph[current].get(position[top]);
					position[top]++;
					if(!visited[next]){
						top++;
						stack[top] = next;
						position[top] = 0;
						visited[next] = true;
					}
				}
				else{
					order.add(current);
					top--;
				}
			}
		}
		int[] component = new int[n];
		Arrays.fill(component, -1);
		int componentNumber = 0;
		for(int i = order.size() - 1; i >= 0; i--){
			int start = order.get(i);
			if(component[start] != -1){
				continue;
			}
			int top = 0;
			stack[top] = start;
			component[start] = componentNumber;
			while(top >= 0){
				int current = stack[top];
				top--;
				for(int next : reversed[current]){
					if(component[next] == -1){
						component[next] = componentNumber;
						top++;
						stack[top] = next;
					}
				}
			}
			componentNumber++;
		}
		List<int[]> condensation = new ArrayList<>();
		HashSet<Long> used = new HashSet<>();
		for(int[] edge : edges){
			int a = component[edge[0]];
			int b = component[edge[1]];
			if(a != b){
				long pair = ((long)a << 32) | (b & 0xffffffffL);
				if(!used.contains(pair)){
					used.add(pair);
					condensation.add(new int[]{a, b});
				}
			}
		}
		Map<String, Object> result = new HashMap<>();
		result.put("component", component);
		result.put("condensation", condensation);
		return result;
	}
	static Map<String, Object> topo(Map<String, Object> input) {
		int n = Input.vertexCount(input);
		int[][] edges = Input.edges(input);
		List<Integer>[] graph = new ArrayList[n];
		for(int i = 0; i < n; i++){
			graph[i] = new ArrayList<>();
		}
		for(int[] edge : edges){
			graph[edge[0]].add(edge[1]);
		}
		int[] state = new int[n];
		List<Integer> order = new ArrayList<>();
		int[] stack = new int[n];
		int[] position = new int[n];
		boolean acyclic = true;
		for(int start = 0; start < n && acyclic; start++){
			if(state[start] != 0){
				continue;
			}
			int top = 0;
			stack[top] = start;
			position[top] = 0;
			state[start] = 1;
			while(top >= 0 && acyclic){
				int current = stack[top];
				if(position[top] < graph[current].size()){
					int next = graph[current].get(position[top]);
					position[top]++;
					if(state[next] == 0){
						state[next] = 1;

						top++;
						stack[top] = next;
						position[top] = 0;
					}
					else if(state[next] == 1){
						acyclic = false;
					}
				}
				else{
					state[current] = 2;
					order.add(current);
					top--;
				}
			}
		}
		Map<String, Object> result = new HashMap<>();
		if(!acyclic){
			result.put("acyclic", false);
			result.put("order", new ArrayList<Integer>());
			return result;
		}
		List<Integer> finalOrder = new ArrayList<>();
		for(int i = order.size() - 1; i >= 0; i--){
			finalOrder.add(order.get(i));
		}

		result.put("acyclic", true);
		result.put("order", finalOrder);
		return result;
	}
	static Map<String, Object> dag(Map<String, Object> input) {
		int n = Input.vertexCount(input);
		int source = Input.source(input);
		int[][] edges = Input.edges(input);
		Map<String, Object> topoResult = topo(input);
		boolean acyclic = (boolean)topoResult.get("acyclic");
		Map<String, Object> result = new HashMap<>();
		if(!acyclic){
			result.put("acyclic", false);
			result.put("distance", new Long[0]);
			result.put("parent_edge", new int[0]);
			return result;
		}
		List<Integer> order = (List<Integer>)topoResult.get("order");
		List<int[]>[] graph = new ArrayList[n];
		for(int i = 0; i < n; i++){
			graph[i] = new ArrayList<>();
		}
		for(int i = 0; i < edges.length; i++){
			int u = edges[i][0];
			int v = edges[i][1];
			int weight = edges[i][2];
			graph[u].add(new int[]{v, weight, i});
		}
		Long[] distance = new Long[n];
		int[] parentEdge = new int[n];
		Arrays.fill(parentEdge, -1);
		distance[source] = 0L;
		for(int u : order){
			if(distance[u] == null){
				continue;
			}
			for(int[] edge : graph[u]){
				int v = edge[0];
				int weight = edge[1];
				int id = edge[2];
				long newDistance = distance[u] + weight;
				if(distance[v] == null || newDistance < distance[v]){
					distance[v] = newDistance;
					parentEdge[v] = id;
				}
			}
		}
		result.put("acyclic", true);
		result.put("distance", distance);
		result.put("parent_edge", parentEdge);
		return result;
	}
	static List<Integer> reconstructRoute(
		int source,
		int target,
		int[] parentEdge,
		int[][] edges
	){
		List<Integer> route = new ArrayList<>();
		if(source == target){
			route.add(source);
			return route;
		}
		if(target < 0 || target >= parentEdge.length){
			return route;
		}
		if(parentEdge[target] == -1){
			return route;
		}
		int current = target;
		while(current != source){
			route.add(current);
			int edgeId = parentEdge[current];
			if(edgeId < 0 || edgeId >= edges.length){
				return new ArrayList<>();
			}
			current = edges[edgeId][0];
		}
		route.add(source);
		for(int i = 0; i < route.size() / 2; i++){
			int j = route.size() - 1 - i;
			int temp = route.get(i);
			route.set(i, route.get(j));
			route.set(j, temp);
		}
		return route;
	}
}