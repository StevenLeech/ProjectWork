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
import java.util.Scanner;

public class FootballSchedulingV2 {
    private static Division[] divisions = new Division[4];
    
    /**
     * get data from text files
     */
    public static void getData(int season) throws FileNotFoundException, IOException {
        String teamFile = "";
        String distanceFile = "";
        if(season ==1){
            teamFile = "teamClashes.txt";
            distanceFile = "Distances.txt";
        }
        if(season ==2){
            teamFile = "0506teams.txt";
            distanceFile = "0506distances.txt";
        }
        FileReader fr = new FileReader(teamFile);
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
           // System.out.println(teamName);
        }
        String a = br.readLine();
        //System.out.println(a);
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
           // System.out.println(teamName);
        }
        a = br.readLine();
        //System.out.println(a);
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
            //System.out.println(teamName);
        }
        a = br.readLine();
        //System.out.println(a);
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
            //System.out.println(teamName);
        }
        fr = new FileReader(distanceFile);
        br = new BufferedReader(fr);

        for (int i = 0; i < 20; i++) {
            for (int j = 0; j < 20; j++) {
                double distance = Double.parseDouble(br.readLine());
                //if(j>=i){
                //    premDistances[i][j] = distance;
                    premDistances[j][i] = distance;
                //}
            }
        }
        a=br.readLine();
        for (int i = 0; i < 24; i++) {
            for (int j = 0; j < 24; j++) {
                double distance = Double.parseDouble(br.readLine());
                //if(j>=i){
               // champDistances[i][j] = distance;
                champDistances[j][i] = distance;
               // }
            }
        }
        a = br.readLine();
        for (int i = 0; i < 24; i++) {
            for (int j = 0; j < 24; j++) {
                double distance = Double.parseDouble(br.readLine());
                //if(j>=i){
                //lg1Distances[i][j] = distance;
                lg1Distances[j][i] = distance;
               // }
            }
        }
        a = br.readLine();
        for (int i = 0; i < 24; i++) {
            for (int j = 0; j < 24; j++) {
                double distance = Double.parseDouble(br.readLine());
               // if(j>=i){
               // lg2Distances[i][j] = distance;
                lg2Distances[j][i] = distance;
               // }
            }
        }
        Division prem = new Division(0,"Premier League", premTeams, premCycles, premDistances);
        Division champ = new Division(1,"Championship", champTeams, champCycles, champDistances);
        Division league1 = new Division(2,"League One", lg1Teams, lg1Cycles, lg1Distances);
        Division league2 = new Division(3,"League Two", lg2Teams, lg2Cycles, lg2Distances);

        divisions[0] = prem;
        divisions[1] = champ;
        divisions[2] = league1;
        divisions[3] = league2;
        /**
         * @param args the command line arguments
         */
    }
    
    /**
     * checks if string contains an integer
     */
    public static boolean isInteger( String input ) {
    try {
        Integer.parseInt( input );
        return true;
    }
    catch( Exception e ) {
        return false;
    }
}
    
    /**
     * checks if a string contains a double
     */
    public static boolean isDouble( String input ) {
    try {
        Double.parseDouble( input );
        return true;
    }
    catch( Exception e ) {
        return false;
    }
}
    
    /**
     * main method.
     * creates objects and starts program.
     */
    public static void main(String[] args) throws IOException {
        double total =0;
        double best= 5000000;
        int runs = 1;
        double temp = 300;
        int iterations = 30000;
        int season = 1;
        double runtime = 10.0;
        Scanner scan = new Scanner(System.in);
        System.out.println("Select a Season");
        System.out.println("1. 04/05    2. 05/06     (Enter 1 or 2)");
        String seas = scan.nextLine();
        if(seas.equalsIgnoreCase("1") ||seas.equalsIgnoreCase("2")){
            season = Integer.parseInt(seas);
        }
        String seasonStr = "";
        if(season == 1){
            seasonStr = "04/05";
        }
        if(season == 2){
            seasonStr = "05/06";
        }
        System.out.println("Enter the stating temperature (default is 300)");
        String tempS = scan.nextLine();
        if(isDouble(tempS)){
            temp = Double.parseDouble(tempS);
        }
        runtime += (((temp - 300)/100)*0.8);
        System.out.println("Enter the number of Iterations per step (default is 30000)");
        String iter = scan.nextLine();
        if(isInteger(iter)){
            iterations = Integer.parseInt(iter);
        }
        runtime = runtime * (iterations/3000);
        int runMinutes = (int) runtime/60;
        System.out.println("Estimated run-time per run is "+ runMinutes +" Minutes, "+ runtime%60 +" Seconds.");
        System.out.println("Enter the number of runs (default is 1)");
        String runStr = scan.nextLine();
        if(isInteger(runStr)){
            runs = Integer.parseInt(runStr);
        }
        System.out.println("Season: "+ seasonStr+ ", Temperature: "+ temp + ", Iterations: "+ iterations + ", Runs: "+ runs);
        getData(season);
        Problem bestp = new Problem(divisions);
        System.out.println("-------------------------------------");
        for(int i=0; i<runs;i++){
            getData(season);
            Problem problem = new Problem(divisions);
            RandGen rgen = new RandGen(500.0);
            rgen.setSeed(500.0);
            SA mysa = new SA(problem, rgen, temp, 200, iterations, 0.95, 0.01, 1e-6, 1e10, -50);
            double cost = mysa.simulation();
            total+= cost;
            if(cost<best){
                best = cost;
                bestp = mysa.getProblem();
            
            }
        }
        bestp.checkAndDisplay();
        System.out.println("");
        System.out.println("Average cost = "+ total/runs);
        System.out.println("Best Cost = "+ best);
    }
}
