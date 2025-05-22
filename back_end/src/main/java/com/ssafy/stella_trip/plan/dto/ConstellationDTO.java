package com.ssafy.stella_trip.plan.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
public class ConstellationDTO {
    private List<Node> nodes = new ArrayList<>();
    private List<Edge> edges = new ArrayList<>();

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Node {
        private String name;
        private int x;
        private int y;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Edge {
        private int from;
        private int to;
    }

    public ConstellationDTO addNode(String name, int x, int y) {
        nodes.add(new Node(name, x, y));
        return this;
    }

    public ConstellationDTO addEdge(int from, int to) {
        if(from < 0 || from >= this.nodes.size() || to < 0 || to >= this.nodes.size() || from == to) {
            return this;
        }
        this.edges.add(new Edge(from, to));
        return this;
    }
}
