package stuff.gui;

import java.util.ArrayList;

public class ShortestPath {

    static int n=0;

    int minDistance(double dist[], boolean sptSet[]) {
        double min = Double.MAX_VALUE;
        int min_index = -1;

        for (int v=0; v<n; v++) {
            if (sptSet[v] == false && dist[v] <= min) {
                min = dist[v];
                min_index = v;
            }
        }
        return min_index;
    }

    PathAndDistances[] dijkstra(ArrayList<GraphNode> graphNodes, int src){


        n = graphNodes.size();
        PathAndDistances pad[] = new PathAndDistances[n];
        double dist[] = new double[n];
        boolean sptSet[] = new boolean[n];

        for (int i=0; i<n; i++) {
            dist[i] = Double.MAX_VALUE;
            sptSet[i] = false;
            pad[i] = new PathAndDistances();

        }

        dist[src] = 0;


        for (int count = 0; count<n-1; count++) {
            int u = minDistance(dist, sptSet);
            sptSet[u] = true;

            for (int v=0; v<n; v++) {

                int thereIsAnEdgeFromUToV = -1;
                double distance = Double.MAX_VALUE;
                for (int k=0; k<graphNodes.get(u).neighbours.length; k++) {
                    GraphNode n = graphNodes.get(u).neighbours[k];
                    if (n!= null && n.equals(graphNodes.get(v))) {
                        thereIsAnEdgeFromUToV = k;
                        distance = graphNodes.get(u).getDistance(k);
                    }
                }

                if (!sptSet[v] && thereIsAnEdgeFromUToV>-1 && dist[u] != Double.MAX_VALUE && dist[u] + distance < dist[v]) {
                    dist[v] = dist[u] + distance;
                    //System.out.println("Distance " + v + " updated");
                    pad[v].dist = dist[v];
                    pad[v].node = graphNodes.get(v);
                    pad[v].predecessor=pad[u];
                }
            }

        }

        return pad;
    }

    PathAndDistances[] dijkstra(ArrayList<GraphNode> graphNodes, GraphNode sourceNode) {


        int src = 0; //id of sourceNode
        for (int i=0; i<graphNodes.size(); i++) {
            if (sourceNode.equals(graphNodes.get(i)))
                src = i;
        }
       return dijkstra(graphNodes, src);

    }
}
