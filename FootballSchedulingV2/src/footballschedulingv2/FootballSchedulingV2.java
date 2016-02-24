package footballschedulingv2;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import java.io.IOException;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.util.ArrayList;

public class FootballSchedulingV2 {

    private static Division[] divisions = new Division[4];

    public static void getData() throws FileNotFoundException, IOException {
        FileReader fr = new FileReader("teamClashes.txt");
        BufferedReader br = new BufferedReader(fr);
        Cycle[] premCycles = new Cycle[6];
        Cycle[] champCycles = new Cycle[6];
        Cycle[] lg1Cycles = new Cycle[6];
        Cycle[] lg2Cycles = new Cycle[6];

        for (int i = 0; i < 6; i++) {
            Cycle cycle1 = new Cycle(i);
            Cycle cycle2 = new Cycle(i);
            Cycle cycle3 = new Cycle(i);
            Cycle cycle4 = new Cycle(i);
            premCycles[i] = cycle1;
            champCycles[i] = cycle2;
            lg1Cycles[i] = cycle3;
            lg2Cycles[i] = cycle4;
        }
        Team[] premTeams = new Team[20];
        Team[] champTeams = new Team[24];
        Team[] lg1Teams = new Team[24];
        Team[] lg2Teams = new Team[24];
        double[][] premDistances = new double[20][20];
        double[][] champDistances = new double[24][24];
        double[][] lg1Distances = new double[24][24];
        double[][] lg2Distances = new double[24][24];

        for (int i = 0; i < 20; i++) {
            String teamString = br.readLine();
            String[] teamStrings = teamString.split(":");
            String teamName = teamStrings[0];
            String teamPairs = teamStrings[1];
            int location = Integer.parseInt(teamStrings[2]);
            ArrayList<String> pairs = new ArrayList<String>();
            String[] paired = teamPairs.split(",");
            for(int j =0;j<paired.length;j++){
                pairs.add(paired[j]);
            }
            int next = i + 1;
            int prev = i - 1;
            if (i == 0) {
                prev = 19;
            }
            if (i == 19) {
                next = 0;
            }
            Team team = new Team(teamName, i, next, prev, 0, i,pairs,location,0);
            premTeams[i] = team;
            premCycles[0].addTeam(team);
            System.out.println(teamName);
        }
        String a = br.readLine();
        System.out.println(a);
        for (int i = 0; i < 24; i++) {String teamString = br.readLine();
            String[] teamStrings = teamString.split(":");
            String teamName = teamStrings[0];
            String teamPairs = teamStrings[1];
            int location = Integer.parseInt(teamStrings[2]);
            ArrayList<String> pairs = new ArrayList<String>();
            String[] paired = teamPairs.split(",");
            for(int j =0;j<paired.length;j++){
                pairs.add(paired[j]);
            }
            int next = i + 1;
            int prev = i - 1;
            if (i == 0) {
                prev = 23;
            }
            if (i == 23) {
                next = 0;
            }
            Team team = new Team(teamName, i, next, prev, 0, i,pairs,location,1);
            champTeams[i] = team;
            champCycles[0].addTeam(team);
            System.out.println(teamName);
        }
        a = br.readLine();
        System.out.println(a);
        for (int i = 0; i < 24; i++) {
            String teamString = br.readLine();
            String[] teamStrings = teamString.split(":");
            String teamName = teamStrings[0];
            String teamPairs = teamStrings[1];
            int location = Integer.parseInt(teamStrings[2]);
            ArrayList<String> pairs = new ArrayList<String>();
            String[] paired = teamPairs.split(",");
            for(int j =0;j<paired.length;j++){
                pairs.add(paired[j]);
            }
            int next = i + 1;
            int prev = i - 1;
            if (i == 0) {
                prev = 23;
            }
            if (i == 23) {
                next = 0;
            }
            Team team = new Team(teamName, i, next, prev, 0, i,pairs,location,2);
            lg1Teams[i] = team;
            lg1Cycles[0].addTeam(team);
            System.out.println(teamName);
        }
        a = br.readLine();
        System.out.println(a);
        for (int i = 0; i < 24; i++) {
            String teamString = br.readLine();
            String[] teamStrings = teamString.split(":");
            String teamName = teamStrings[0];
            String teamPairs = teamStrings[1];
            int location = Integer.parseInt(teamStrings[2]);
            ArrayList<String> pairs = new ArrayList<String>();
            String[] paired = teamPairs.split(",");
            for(int j =0;j<paired.length;j++){
                pairs.add(paired[j]);
            }
            int next = i + 1;
            int prev = i - 1;
            if (i == 0) {
                prev = 23;
            }
            if (i == 23) {
                next = 0;
            }
            Team team = new Team(teamName, i, next, prev, 0, i,pairs,location,3);
            lg2Teams[i] = team;
            lg2Cycles[0].addTeam(team);
            System.out.println(teamName);
        }
        fr = new FileReader("Distances.txt");
        br = new BufferedReader(fr);

        for (int i = 0; i < 20; i++) {
            for (int j = 0; j < 20; j++) {
                premDistances[i][j] = Double.parseDouble(br.readLine());
            }
        }
        a = br.readLine();
        for (int i = 0; i < 24; i++) {
            for (int j = 0; j < 24; j++) {
                champDistances[i][j] = Double.parseDouble(br.readLine());
            }
        }
        a = br.readLine();
        for (int i = 0; i < 24; i++) {
            for (int j = 0; j < 24; j++) {
                lg1Distances[i][j] = Double.parseDouble(br.readLine());
            }
        }
        a = br.readLine();
        for (int i = 0; i < 24; i++) {
            for (int j = 0; j < 24; j++) {
                lg2Distances[i][j] = Double.parseDouble(br.readLine());
            }
        }
        Division prem = new Division("Premier League", premTeams, premCycles, premDistances);
        Division champ = new Division("Championship", champTeams, champCycles, champDistances);
        Division league1 = new Division("League One", lg1Teams, lg1Cycles, lg1Distances);
        Division league2 = new Division("League Two", lg2Teams, lg2Cycles, lg2Distances);

        divisions[0] = prem;
        divisions[1] = champ;
        divisions[2] = league1;
        divisions[3] = league2;
        /**
         * @param args the command line arguments
         */
    }

    public static void main(String[] args) throws IOException {
        getData();
        Problem problem = new Problem(divisions);
        RandGen rgen = new RandGen(500.0);
        rgen.setSeed(500.0);
        SA mysa = new SA(problem, rgen, 75, 50, 2000, 0.9, 0.01, 1e-6, 1e10, -50);
        mysa.simulation();
        
    }
}
