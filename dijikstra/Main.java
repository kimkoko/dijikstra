import java.util.*;
import java.io.*;

public class Main{
    static final int INF = 987654321;
    static final int MAX_N = 15;
    static int N;
    static int[][] Graph = new int[MAX_N][MAX_N];
    static int[] Dist = new int[MAX_N];
    static int[] Prev = new int[MAX_N];
    static char[] Rep = new char[MAX_N];

    static void dijkstra(int src) {
        PriorityQueue<int[]> pq = new PriorityQueue<>((a,b)->a[0]-b[0]);
        boolean[] visited = new boolean[MAX_N];
        for (int i = 0; i < N; i++) {
            Prev[i] = - 1;
            Dist[i] = INF;
        }
        Dist[src] = 0;
        pq.add(new int[] {0,src});
        while (!pq.isEmpty()) {
            int[] curr = pq.poll();
            int u = curr[1];
            if (visited[u]) continue;
            visited[u] = true;

            for (int v = 0; v < N; v++) {
                if (Dist[v] > Dist[u] + Graph[u][v]) {
                    Prev[v] = u;
                    Dist[v] = Dist[u] + Graph[u][v];
                    pq.add(new int[] {Dist[v], v});
                }
            }
        }
    }

    static void dijkstra2(int src) {
        PriorityQueue<int[]> pq = new PriorityQueue<>((a,b)->a[0]-b[0]);
        boolean[] visited = new boolean[MAX_N];
        int[] order = new int[N];
        for (int i = 0; i < N; i++) {
            Prev[i] = - 1;
            Dist[i] = INF;
        }
        Dist[src] = 0;
        pq.add(new int[] {0,src});
        while (!pq.isEmpty()) {
            int[] curr = pq.poll();
            int u = curr[1];
            if (visited[u]) continue;
            visited[u] = true;
            for (int v = 0; v < N; v++) {
                if (Dist[v] > Dist[u] + Graph[u][v]) {
                    Prev[v] = u;
                    Dist[v] = Dist[u] + Graph[u][v];
                    pq.add(new int[] {Dist[v], v});
                }
            }
            if (src == u) continue;
            else {
                System.out.print("Found "+Rep[u] +": Path: ");
                int k = u;
                int id = 0;
                while (k != -1){
                    order[id] = k;
                    k = Prev[k];
                    id++;
                }
                for (int x = 0; x < id; x++) {
                    int a = order[id-x-1];
                    System.out.print(Rep[a]);
                    if ((x+1) != id) System.out.print(">");
                }
                System.out.print(" Cost:" +Dist[u]);
                System.out.print("  [press any key to continue]");
                try {
                    System.in.read();
                } 
                catch (Exception e) {}
            }
                      
        }
        System.out.println("\nThe summary table");
        comp_all(src);
    }

    static void comp_all(int src) {
        System.out.println("Source " + Rep[src] + ":");
        int[] order = new int[N];
        
        for (int i = 0; i < N; i++) {
            int j = i;
            if (src == i) continue;
            else {
                System.out.print(Rep[i]+": Path: ");
                int id = 0;
                while(j != -1) {
                    order[id] = j;
                    j = Prev[j];
                    id++;
                }
                for (int x = 0; x < id; x++) {
                    int a = order[id-x-1];
                    System.out.print(Rep[a]);
                    if ((x+1) != id) System.out.print(">");
                }
                System.out.print(" Cost: " + Dist[i]);
                System.out.print("\n");
            }
        }
    }

    static void file_process(){
        BufferedReader reader;
        int from = 0;
        int node_no = 0;
        for (int i = 0; i < MAX_N; i++){
            for (int j = 0; j < MAX_N; ++j){
                if (i == j) Graph[i][j] = 0;
                else Graph[i][j] = INF;
            }
        }
        try {
            reader = new BufferedReader(new FileReader("routes.lsa"));
            String line = reader.readLine();
            while (line != null) {
                String[] proc = line.split(" "); 
                Rep[from] = line.charAt(0);
                for (int i = 1; i < proc.length; i++) {
                    int to = proc[i].charAt(0) - 'A';
                    int cost = proc[i].charAt(2) - '0';
                    Graph[from][to] = Graph[to][from] = cost;
                }
                from++;
                line = reader.readLine();
                node_no++;                               
            }
            N = node_no;
            reader.close();           
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    static void add_node(){
        Scanner sc = new Scanner(System.in);
        System.out.println("Enter the new node to add");
        String new_node = sc.nextLine();
        try {
            BufferedWriter out = new BufferedWriter(new FileWriter("routes.lsa", true));
            out.write("\n");
            out.write(new_node);
            out.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    static void remove_node(){
        Scanner sc = new Scanner(System.in);
        System.out.println("Enter the node to be removed");
        String rem_node = sc.next();
        String overwrite_file = "";

        try {
            BufferedReader reader = new BufferedReader(new FileReader("routes.lsa"));
            String line = reader.readLine();
            while (line != null) {
                if (line.charAt(0) == rem_node.charAt(0)) {
                    line = reader.readLine();
                    continue;
                } else if (line.contains(rem_node)) {
                    int rem_ID = line.indexOf(rem_node);
                    String toBeRemoved = line.substring(rem_ID - 1, rem_ID + 3);
                    overwrite_file = overwrite_file + line.replace(toBeRemoved, "");
                } else {
                    overwrite_file = overwrite_file + line;
                }

                line = reader.readLine();
                if (line != null) overwrite_file = overwrite_file + "\n";
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            BufferedWriter overwrite = new BufferedWriter(new FileWriter("routes.lsa", false));
            overwrite.write(overwrite_file);
            overwrite.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    static void link_broken(){
        Scanner sc = new Scanner(System.in);
        System.out.println("Enter the link to be broken");
        String brokenLink = sc.next();
        char first = brokenLink.charAt(0);
        char second = brokenLink.charAt(2);
        String overwrite_file = "";

        try {
            BufferedReader reader = new BufferedReader(new FileReader("routes.lsa"));
            String line = reader.readLine();
            while (line != null) {
                if (line.charAt(0) == first) {
                    int linkID = line.indexOf(String.valueOf(second));
                    String toBeRemoved = line.substring(linkID - 1, linkID + 3);
                    overwrite_file = overwrite_file + line.replace(toBeRemoved, "");
                } else if (line.charAt(0) == second) {
                    int linkID = line.indexOf(String.valueOf(first));
                    String toBeRemoved = line.substring(linkID - 1, linkID + 3);
                    overwrite_file = overwrite_file + line.replace(toBeRemoved, "");
                } else {
                    overwrite_file += line;
                }
                line = reader.readLine();
                if (line != null) overwrite_file = overwrite_file + "\n";
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        try {
            BufferedWriter overwrite = new BufferedWriter(new FileWriter("routes.lsa", false));
            overwrite.write(overwrite_file);
            overwrite.close();

        } catch (IOException e) {
            e.printStackTrace();
        }                
                    
    }

    public static void main(String[] args){       
        Scanner sc = new Scanner(System.in);
        System.out.println("This is the program for LSR computation. Enter y to start and n to quit.");
        String s = sc.next();
        while (s.equals("y")) {
            System.out.println("Press any key to load the file routes.lsa");
            try {
                System.in.read();
                file_process();
            } 
            catch (Exception e) {}
            System.out.println("Enter the mode you want:    [CA for Compute all | SS for Single Step | TU for Topology Update]");
            s = sc.next();
            if (s.equals("TU")) {
                System.out.println("Enter the option you want:  [Add for Addition of a new node | Remove for Removal of node | Link for Link break]");
                String op = sc.next();
                if (op.equals("Add")){
                    add_node();
                } else if (op.equals("Remove")){
                    remove_node();
                } else if (op.equals("Link")){
                    link_broken();
                }

            }else {
                System.out.println("Enter the source node: ");
                char c = sc.next().charAt(0);
                int src = c - '0' -17; 
            
                if (s.equals("CA")) {
                    dijkstra(src);
                    comp_all(src);
                } else if (s.equals("SS")) dijkstra2(src);
            }

            System.out.println("Do you want to continue:? Enter y for yes and n for no.");
            s = sc.next();
        }
    }
}

