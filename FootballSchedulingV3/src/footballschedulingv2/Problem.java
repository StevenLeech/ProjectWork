/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package footballschedulingv2;

import java.util.ArrayList;
import java.util.Random;

public class Problem {

    private Division[] divisions;
    private Division backup;
    private double cost;
    private double bestCost;
    private double adjustedCost;
    int currentCycle;
    private Fixture[] boxingDay = new Fixture[46];
    private Fixture[] newYearsDay = new Fixture[46];
    int boxingDayClashes = 0;
    int boxingDayLondon = 0;
    int boxingDayManchester = 0;
    int bdPremLondon = 0;
    int newYearsDayClashes = 0;
    int newYearsDayLondon = 0;
    int newYearsDayManchester = 0;
    int nyPremLondon = 0;
    ArrayList<String> bdHomeTeamStrings;
    ArrayList<String> nyHomeTeamStrings;
    
    /**
     * Initial constructor
     */
    public Problem(Division[] divisions) {
        this.divisions = divisions;
        setFixtures();
        calculateCost();
        this.bestCost = adjustedCost;
        this.currentCycle = 0;
    }
    
    /**
     * copy constructor
     */
    public Problem(Problem p) {
        divisions = new Division[p.divisions.length];
        for (int i = 0; i < p.divisions.length; i++) {
            divisions[i] = new Division(p.getDivisions()[i]);
        }
        cost = p.cost;
        bestCost = p.bestCost;
        adjustedCost = p.adjustedCost;
        currentCycle = p.currentCycle;
        setFixtures();
        boxingDayClashes = p.boxingDayClashes;
        boxingDayLondon = p.boxingDayLondon;
        boxingDayManchester = p.boxingDayManchester;
        bdPremLondon = p.bdPremLondon;
        newYearsDayClashes = p.newYearsDayClashes;
        newYearsDayLondon = p.newYearsDayLondon;
        newYearsDayManchester = p.newYearsDayManchester;
        nyPremLondon = p.nyPremLondon;
        bdHomeTeamStrings = new ArrayList<String>();
        nyHomeTeamStrings = new ArrayList<String>();
        for(int i=0;i<p.bdHomeTeamStrings.size();i++){
            bdHomeTeamStrings.add(p.bdHomeTeamStrings.get(i));
            nyHomeTeamStrings.add(p.nyHomeTeamStrings.get(i));
        }
        
    }
    
    /**
     * constructor for full backup
     */
    public Problem (Division division, Problem p){
        backup = new Division(division);
        int id = backup.getId();
        divisions = new Division[p.divisions.length];
        for(int i=0;i<4;i++){
            Division div = p.divisions[i];
            if(i == id){
                divisions[i] = backup;
            }
            else{
                divisions[i]=div;
            }
        }
        cost = p.cost;
        bestCost = p.bestCost;
        adjustedCost = p.adjustedCost;
        currentCycle = p.currentCycle;
        setFixtures();
        boxingDayClashes = p.boxingDayClashes;
        boxingDayLondon = p.boxingDayLondon;
        boxingDayManchester = p.boxingDayManchester;
        bdPremLondon = p.bdPremLondon;
        newYearsDayClashes = p.newYearsDayClashes;
        newYearsDayLondon = p.newYearsDayLondon;
        newYearsDayManchester = p.newYearsDayManchester;
        nyPremLondon = p.nyPremLondon;
        bdHomeTeamStrings = new ArrayList<String>();
        nyHomeTeamStrings = new ArrayList<String>();
        for(int i=0;i<p.bdHomeTeamStrings.size();i++){
            bdHomeTeamStrings.add(p.bdHomeTeamStrings.get(i));
            nyHomeTeamStrings.add(p.nyHomeTeamStrings.get(i));
        }
    }
    
    /**
     * method to get set of fixtures from cycles
     */
    public void setFixtures() {
        int fixture = 0;
        for (int i = 0; i < getDivisions().length; i++) {
            Cycle[] cycles = getDivisions()[i].getCycles();
            Team[] schedule = getDivisions()[i].getSchedule();
            double[][] distances = getDivisions()[i].getDistances();
            for (int j = 0; j < cycles.length; j++) {
                Cycle cycle = cycles[j];
                ArrayList<Team> teams = cycle.getTeams();
                if (!cycle.isEmpty()) {

                    Team homeTeam = teams.get(0);
                    for (int k = 0; k < teams.size() / 2; k++) {
                        Team awayTeam = schedule[homeTeam.getNextTeam()];
                        boxingDay[fixture] = new Fixture(homeTeam, awayTeam);
                        double distance = distances[homeTeam.getTeamID()][awayTeam.getTeamID()];
                        boxingDay[fixture].setDistance(distance);
                        fixture++;
                        homeTeam = schedule[awayTeam.getNextTeam()];
                    }
                }
            }
        }
        fixture = 0;
        for (int i = 0; i < getDivisions().length; i++) {
            Cycle[] cycles = getDivisions()[i].getCycles();
            Team[] schedule = getDivisions()[i].getSchedule();
            double[][] distances = getDivisions()[i].getDistances();
            for (int j = 0; j < cycles.length; j++) {
                Cycle cycle = cycles[j];
                ArrayList<Team> teams = cycle.getTeams();
                if (!cycle.isEmpty()) {
                    Team team = teams.get(0);
                    Team homeTeam = schedule[team.getNextTeam()];
                    for (int k = 0; k < teams.size() / 2; k++) {
                        Team awayTeam = schedule[homeTeam.getNextTeam()];
                        newYearsDay[fixture] = new Fixture(homeTeam, awayTeam);
                        double distance = distances[homeTeam.getTeamID()][awayTeam.getTeamID()];
                        newYearsDay[fixture].setDistance(distance);
                        fixture++;
                        homeTeam = schedule[awayTeam.getNextTeam()];
                    }
                }
            }
        }
    }
    
    /**
     * method to call transformations
     */
    public double modify(Division division) {
        Random rand = new Random();
        Team[] teams = division.getSchedule();
        Cycle[] cycles = division.getCycles();
        int selectTeam = rand.nextInt(teams.length);
        Team team1 = teams[selectTeam];
        int selectMove = rand.nextInt(5);
        int highestCycle = division.getHighestCycle();
        int team1Cycle = team1.getCycle();
        Team team2 = new Team();
        int selectTeam2 = 0;
        double costChange = 0;
        if (selectMove == 0) { //move mergeCycle
            //System.out.println("Merge");
            if (highestCycle == 0) {
            } else {
                ArrayList<Cycle> otherCycles = new ArrayList<Cycle>();
                for (int i = 0; i <= highestCycle; i++) {
                    if (i == team1Cycle) {
                    } else {
                        otherCycles.add(cycles[i]);
                    }
                }
                int selectCycle = rand.nextInt(otherCycles.size());
                Cycle otherCycle = otherCycles.get(selectCycle);
                int i = 0;
                boolean feasible = false;
                boolean possible = true;
                while (feasible == false) {
                    i++;
                    selectTeam2 = rand.nextInt(otherCycle.getCycleSize());
                    team2 = otherCycle.getTeams().get(selectTeam2);
                    feasible = isMergeFeasible(team1, team2);
                    if (i == 10) {
                        feasible = true;
                        possible = false;
                    }
                }
                if (possible == true) {
                   costChange = merge(team1, team2, division);
                }
            }
        }
        if (selectMove == 1) { //split Cycle
            //System.out.println("Split");
            Cycle cycle = cycles[team1Cycle];
            boolean feasible = false;
            boolean possible = true;
            int i = 0;
            while (feasible == false) {
                i++;
                selectTeam2 = rand.nextInt(cycle.getCycleSize());
                team2 = cycle.getTeams().get(selectTeam2);
                feasible = isSplitFeasible(team1, team2, division);
                if (i == 10) {
                    feasible = true;
                    possible = false;
                }
            }
            if (possible == true) {
                costChange = split(team1, team2, division);
            }

        }
        if (selectMove == 2) {//rearrange cycle
            //System.out.println("Rearrange");
            Cycle cycle = cycles[team1Cycle];
            int i=0;
            boolean feasible = false;
            boolean possible = true;
            while (feasible == false) {
                i++;
                selectTeam2 = rand.nextInt(cycle.getCycleSize());
                team2 = cycle.getTeams().get(selectTeam2);
                feasible = isRearrangeFeasible(team1, team2, division);
                 if (i == 10) {
                    feasible = true;
                    possible = false;
                }
            }
            if (possible == true) {
                
                costChange = rearrange(team1, team2, division);
            }
        }
        if (selectMove == 3) {
            //System.out.println("Reverse");
            Cycle cycle = cycles[team1Cycle];
            costChange = reverseCycle(cycle, division);
        }
        if (selectMove == 4) {
            //System.out.println("Adjust");
            Cycle cycle = cycles[team1Cycle];
            adjustDates(cycle, division);
        }
        setFixtures();
        //getCost();
        adjustedCost = calculateAdjustedCost(costChange);
        return adjustedCost;
    }
    
    /**
     * method to return cost of problem
     */
    public double returnCost() {
        return this.cost;
    }
    
    /**
     * method to calculate the current cost
     */
    public double calculateCost() {
        double total = 0.0;
        for (int j = 0; j < getDivisions().length; j++) {
            Division division = getDivisions()[j];
            Team[] schedule = division.getSchedule();
            double[][] distances = division.getDistances();
            for (int i = 0; i < schedule.length; i++) {
                double distance = distances[i][schedule[i].getNextTeam()];
                total += distance;
            }
        }
        cost = total;
        double penalty = getPenalty();
        adjustedCost = total + penalty;
        return adjustedCost;
    }
    
    /**
     * method to update clashes
     */
    public void getClashes(){
        boxingDayClashes = 0;
        boxingDayLondon = 0;
        boxingDayManchester = 0;
        bdPremLondon = 0;
        newYearsDayClashes = 0;
        newYearsDayLondon = 0;
        newYearsDayManchester = 0;
        nyPremLondon = 0;
        ArrayList<Team> boxingDayHomeTeams = new ArrayList<Team>();
        bdHomeTeamStrings = new ArrayList<String>();
        ArrayList<Team> newYearsDayHomeTeams = new ArrayList<Team>();
        nyHomeTeamStrings = new ArrayList<String>();

        for (int i = 0; i < boxingDay.length; i++) {
            Team team = boxingDay[i].getHomeTeam();
            bdHomeTeamStrings.add(team.getTeamName());
            boxingDayHomeTeams.add(team);
        }
        for (int i = 0; i < newYearsDay.length; i++) {
            Team team = newYearsDay[i].getHomeTeam();
            nyHomeTeamStrings.add(team.getTeamName());
            newYearsDayHomeTeams.add(team);

        }
        for (int i = 0; i < boxingDayHomeTeams.size(); i++) {
            Team team = boxingDayHomeTeams.get(i);
            ArrayList<String> pairs = team.getPairs();
            for (int j = 0; j < pairs.size(); j++) {
                if (bdHomeTeamStrings.contains(pairs.get(j))) {
                    boxingDayClashes++;
                }
            }
            if (team.getLocation() == 0) {
                boxingDayLondon++;
                if (team.getDivision() == 0) {
                    bdPremLondon++;
                }
            } else if (team.getLocation() == 1) {
                boxingDayManchester++;
            }
        }
        for (int i = 0; i < newYearsDayHomeTeams.size(); i++) {
            Team team = newYearsDayHomeTeams.get(i);
            ArrayList<String> pairs = team.getPairs();
            for (int j = 0; j < pairs.size(); j++) {
                if (nyHomeTeamStrings.contains(pairs.get(j))) {
                    newYearsDayClashes++;
                }
            }
            if (team.getLocation() == 0) {
                newYearsDayLondon++;
                if (team.getDivision() == 0) {
                    nyPremLondon++;
                }
            } else if (team.getLocation() == 1) {
                newYearsDayManchester++;
            }
        }
        
    }
    
    /**
     * method to calculate the penalty
     */
    public double getPenalty(){
        double penalty = 0.0;
        getClashes();
        double averageCost = 20;
        if (boxingDayClashes > 10) {
            penalty += averageCost * (boxingDayClashes - 10);
        }
        if (newYearsDayClashes > 10) {
            penalty += averageCost * (newYearsDayClashes - 10);
        }
        if (boxingDayLondon > 6) {
            penalty += averageCost * (boxingDayLondon - 6);
        }
        if (newYearsDayLondon > 6) {
            penalty += averageCost * (newYearsDayLondon - 6);
        }
        if (boxingDayManchester > 4) {
            penalty += averageCost * (boxingDayManchester - 4);
        }
        if (newYearsDayManchester > 4) {
            penalty += averageCost * (newYearsDayManchester - 4);
        }
        if (bdPremLondon > 3) {
            penalty += averageCost * (bdPremLondon - 3);
        }
        if (nyPremLondon > 3) {
            penalty += averageCost * (nyPremLondon - 3);
        }
        return penalty;
    }
    
    /**
     * method to calculate cost + penalty
     */
    public double calculateAdjustedCost(double costChange){
        cost = cost + costChange;
        adjustedCost = cost + getPenalty();
        return adjustedCost;
    }
    
    /**
     * method to merge two cycles
     */
    public double merge(Team team1, Team team2, Division division) {
        Team[] schedule = division.getSchedule();
        Cycle[] cycles = division.getCycles();
        double[][] distances = division.getDistances();
        Team team3 = schedule[(team1.getNextTeam())];
        Team team4 = schedule[(team2.getPrevTeam())];
        double oldCost =0;
        double newCost =0;
        int team1Cycle = team1.getCycle();
        int team2Cycle = team2.getCycle();
        int cycle1Length = cycles[team1Cycle].getSize();
        int cycle2Length = cycles[team2Cycle].getSize();
        currentCycle = division.getHighestCycle();   
        oldCost = distances[team1.getTeamID()][team3.getTeamID()] + distances[team4.getTeamID()][team2.getTeamID()];
        team1.setNextTeam(team2.getTeamID());
        team2.setPrevTeam(team1.getTeamID());
        team4.setNextTeam(team3.getTeamID());
        team3.setPrevTeam(team4.getTeamID());
        newCost = distances[team1.getTeamID()][team2.getTeamID()] + distances[team4.getTeamID()][team3.getTeamID()];
        double deltaCost = newCost - oldCost;
        int newCycle;
        int oldCycle;
        if (team1Cycle > team2Cycle) {
            newCycle = team2Cycle;
            oldCycle = team1Cycle;
        } else {
            newCycle = team1Cycle;
            oldCycle = team2Cycle;
        }
        Team currentTeam = team1;
        int newCycleLength = cycle1Length + cycle2Length;
        cycles[newCycle].clearList();
        for (int i = 0; i < newCycleLength; i++) {
            currentTeam.setCycle(newCycle);
            currentTeam.setCyclePosition(i);
            cycles[newCycle].addTeam(currentTeam);
            currentTeam = schedule[currentTeam.getNextTeam()];

        }
        cycles[oldCycle].clearList();

        if (cycles[currentCycle].isEmpty()) {
            currentCycle--;
            division.setHighestCycle(currentCycle);
        } else {
            cycles[oldCycle] = cycles[currentCycle];
            cycles[currentCycle] = new Cycle(currentCycle);
            Cycle c1 = cycles[oldCycle];
            for (int i = 0; i < c1.getCycleSize(); i++) {
                c1.getTeams().get(i).setCycle(oldCycle);
            }
            currentCycle--;
            division.setHighestCycle(currentCycle);
        }
        return deltaCost;
    }
    
    /**
     * method split a cycle
     */
    public double split(Team team1, Team team2, Division division) {
        Team[] schedule = division.getSchedule();
        Cycle[] cycles = division.getCycles();
        double[][] distances = division.getDistances();
        Team team3 = schedule[(team1.getNextTeam())];
        Team team4 = schedule[(team2.getPrevTeam())];
        int oldCycle = team1.getCycle();
        currentCycle = division.getHighestCycle();
        currentCycle++;
        team1.setNextTeam(team2.getTeamID());
        team2.setPrevTeam(team1.getTeamID());
        team3.setPrevTeam(team4.getTeamID());
        team4.setNextTeam(team3.getTeamID());
        double oldCost = distances[team1.getTeamID()][team3.getTeamID()]
                + distances[team4.getTeamID()][team2.getTeamID()];
        double newCost = distances[team1.getTeamID()][team2.getTeamID()]
                + distances[team4.getTeamID()][team3.getTeamID()];
        double deltaCost = newCost - oldCost;
        Team currentTeam = team1;
        int position = 0;
        cycles[oldCycle].clearList();
        boolean finished = false;
        while (finished == false) {
            currentTeam.setCyclePosition(position);
            cycles[oldCycle].addTeam(currentTeam);
            position++;
            currentTeam = schedule[currentTeam.getNextTeam()];
            if (currentTeam == team1) {
                finished = true;
            }
        }
        position = 0;
        currentTeam = team3;
        finished = false;
        while (finished == false) {
            currentTeam.setCycle(currentCycle);
            currentTeam.setCyclePosition(position);
            position++;
            cycles[oldCycle].removeTeam(currentTeam);
            cycles[currentCycle].addTeam(currentTeam);
            currentTeam = schedule[currentTeam.getNextTeam()];
            if (currentTeam == team3) {
                finished = true;
            }
        }
        division.setHighestCycle(currentCycle);
        return deltaCost;
    }
    
    /**
     * method to rearrange a cycle
     */
    public double rearrange(Team team1, Team team2, Division division) {
        Team[] schedule = division.getSchedule();
        Cycle[] cycles = division.getCycles();
        double[][] distances = division.getDistances();
        Team team3 = schedule[team1.getNextTeam()];
        Team team4 = schedule[team2.getNextTeam()];
        int team1Position = team1.getCyclePosition();
        int team2Position = team2.getCyclePosition();
        int team3Position = team3.getCyclePosition();
        int team4Position = team4.getCyclePosition();
        int temp;
        double oldCost =0;
        double newCost =0;
        Cycle cycle = cycles[team1.getCycle()];
        int cycleLength = cycle.getCycleSize();
        for(int i=0;i<cycleLength;i++){
            Team currentTeam = cycle.getTeams().get(i);
            oldCost += distances[currentTeam.getTeamID()][currentTeam.getNextTeam()];
        }
        team1.setNextTeam(team2.getTeamID());
        team4.setPrevTeam(team3.getTeamID());
        team2.setNextTeam(team1.getTeamID());
        team3.setPrevTeam(team4.getTeamID());
        Team currentTeam = team3;
        if (team2Position > team3Position) {
            for (int i = team3Position; i <= team2Position; i++) {
                temp = currentTeam.getNextTeam();
                currentTeam.setNextTeam(currentTeam.getPrevTeam());
                currentTeam.setPrevTeam(temp);
                currentTeam = schedule[currentTeam.getPrevTeam()];
            }

        } else if (team3Position > team2Position) {
            currentTeam = team3;
            for (int i = team3Position; i <= cycleLength + team2Position; i++) {
                temp = currentTeam.getNextTeam();
                currentTeam.setNextTeam(currentTeam.getPrevTeam());
                currentTeam.setPrevTeam(temp);
                currentTeam = schedule[currentTeam.getPrevTeam()];
            }

        }
        for(int i=0;i<cycleLength;i++){
            currentTeam = cycle.getTeams().get(i);
            newCost += distances[currentTeam.getTeamID()][currentTeam.getNextTeam()];
        }
        double deltaCost = newCost - oldCost;
        currentTeam = team1;
        for (int i = 0; i < cycleLength; i++) {
            currentTeam.setCyclePosition(i);
            currentTeam = schedule[currentTeam.getNextTeam()];
        }
        return deltaCost;
    }
    
    /**
     * method to reverse direction of a cycle
     */
    public double reverseCycle(Cycle cycle, Division division) {
        Team[] schedule = division.getSchedule();
        int cycleLength = cycle.getCycleSize();
        double[][] distances = division.getDistances();
        double oldCost =0;
        double newCost =0;
        //Team currentTeam = cycle.getTeams().get(0);
        for (int i = 0; i < cycleLength; i++) {
            Team currentTeam = cycle.getTeams().get(i);
            int temp = currentTeam.getNextTeam();
            oldCost += distances[currentTeam.getTeamID()][temp];
            newCost += distances[temp][currentTeam.getTeamID()];
            currentTeam.setNextTeam(currentTeam.getPrevTeam());
            currentTeam.setPrevTeam(temp);

        }
        double deltaCost = newCost - oldCost;
        Team currentTeam = cycle.getTeams().get(0);
        cycle.clearList();
        for (int i = 0; i < cycleLength; i++) {
            currentTeam.setCyclePosition(i);
            cycle.addTeam(currentTeam);
            currentTeam = schedule[currentTeam.getNextTeam()];
        }
        return deltaCost;
    }
    
    /**
     * method to adjust team positions in a cycle
     */
    public void adjustDates(Cycle cycle, Division division) {
        Team[] schedule = division.getSchedule();
        int cycleLength = cycle.getCycleSize();
        ArrayList<Team> teams = cycle.getTeams();
        ArrayList<Team> adjustedCycle = new ArrayList<Team>();
        Team team;
        team = teams.get(cycleLength - 1);
        team.setCyclePosition(0);
        adjustedCycle.add(team);
        for (int i = 0; i < cycleLength - 1; i++) {
            team = teams.get(i);
            team.setCyclePosition(i + 1);
            adjustedCycle.add(team);
        }
        cycle.setTeams(adjustedCycle);
        Team currentTeam = cycle.getTeams().get(0);
        for (int i = 0; i < cycleLength; i++) {
            currentTeam.setCyclePosition(i);
            currentTeam = schedule[currentTeam.getNextTeam()];
        }

    }
    
    /**
     * method to check if a rearrange transformation is feasible
     */
    public boolean isRearrangeFeasible(Team team1, Team team2, Division division) {
        Boolean feasible = false;
        Team team3 = division.getSchedule()[team1.getNextTeam()];
        Team team4 = division.getSchedule()[team2.getNextTeam()];
        if (team1.getNextTeam() != team2.getTeamID() && team2.getNextTeam() != team1.getTeamID()
                && team1 != team2) {
            feasible = true;
        }
        Fixture f1 = new Fixture(team1, team2);
        Fixture f2 = new Fixture(team3, team4);
        if (f1.isPaired() || f2.isPaired()) {
            feasible = false;
        }
        return feasible;
    }
    
    /**
     * method to check if splitting a cycle is feasible
     */
    public boolean isSplitFeasible(Team team1, Team team2, Division division) {
        Team[] schedule = division.getSchedule();
        Cycle[] cycles = division.getCycles();
        double[][] distances = division.getDistances();
        Team team3 = schedule[(team1.getNextTeam())];
        Team team4 = schedule[(team2.getPrevTeam())];
        int oldCycle = team1.getCycle();
        int cycle1Length = 0;
        int cycle2Length = 0;
        int team1Position = team1.getCyclePosition();//get teams cycle position
        int team2Position = team2.getCyclePosition();
        int team3Position = team3.getCyclePosition();
        int team4Position = team4.getCyclePosition();
        boolean feasible = false;
        int cycleLength = cycles[team1.getCycle()].getCycleSize();

        if (team1Position > team2Position) {
            cycle1Length = team1Position - team2Position + 1;
            if (team4Position > team3Position) {
                cycle2Length = team4Position - team3Position + 1;
            } else if (team3Position > team4Position) {
                cycle2Length = team4Position - team3Position + 1 + cycleLength;
            }
        } else if (team2Position > team1Position) {
            cycle1Length = team1Position + cycleLength + 1 - team2Position;
            cycle2Length = team4Position - team3Position + 1;
        }
        if (cycle1Length % 2 == 0 && cycle1Length >= 4 && cycle2Length % 2 == 0 && cycle2Length >= 4
                && team1 != team2 && team1 != team4 && team2 != team3 && team3 != team4) {
            feasible = true;//check feasibility
        }
        Fixture f1 = new Fixture(team1, team2);
        Fixture f2 = new Fixture(team3, team4);
        if (f1.isPaired() || f2.isPaired()) {
            feasible = false;
        }
        return feasible;

    }
    
    /**
     * method to check if a merge transformation if feasible
     */
    public boolean isMergeFeasible(Team team1, Team team2) {
        Division d1 = getDivisions()[team1.getDivision()];
        Division d2 = getDivisions()[team2.getDivision()];
        boolean feasible = true;
        Team team3 = d1.getSchedule()[team1.getNextTeam()];
        Team team4 = d2.getSchedule()[(team2.getPrevTeam())];
        Fixture f1 = new Fixture(team1, team2);
        Fixture f2 = new Fixture(team3, team4);

        if (f1.isPaired() || f2.isPaired()) {
            feasible = false;
        }
        return feasible;
    }
    
    /**
     * method to calculate minimum potential cost (lowest cost for each team)
     */
    public double getMinCost() {
        double total = 0;
        for (int i = 0; i < getDivisions().length; i++) {
            double[][] distances = getDivisions()[i].getDistances();
            Team[] teams = getDivisions()[i].getSchedule();
            for (int j = 0; j < distances.length; j++) {
                double[] teamDistances = distances[j];
                double smallest = 1000;
                double nextSmallest = 1000;
                String team = teams[j].getTeamName();
                for (int k = 0; k < teamDistances.length; k++) {
                    double distance = teamDistances[k];
                    if (distance != 0) {
                        if (distance < smallest) {
                            nextSmallest = smallest;
                            smallest = distance;
                        } else if (distance > smallest) {
                            if (distance < nextSmallest) {
                                nextSmallest = distance;
                            }
                        }
                    }
                }
                //System.out.println(team + " 1st :"+smallest+" 2nd :"+ nextSmallest);
                total = total + ((smallest + nextSmallest) / 2);
            }
        }
        return total;
    }
    
    /**
     * method to display final solution and cost
     */
    public void checkAndDisplay() {
        int clashes = 0;
        System.out.println("");
        System.out.println("Boxing Day");
        String output = String.format("%30s%6s%30s%10s%15s%30s", "Home Team"," vs ","Away Team","Distance","Location", "Clash");
        System.out.println(output);
        System.out.println("Premier League");
        double total=0;
        for (int i = 0; i < boxingDay.length; i++) {
            if (i == 10) {
                System.out.println("");
                System.out.println("Championship");
            }
            if (i == 22) {
                System.out.println("");
                System.out.println("League One");
            }
            if (i == 34) {
                System.out.println("");
                System.out.println("League Two");
            }
            Team homeTeam = boxingDay[i].getHomeTeam();
            ArrayList<String> pairs = homeTeam.getPairs();
            if(pairs.size()> 0){
            for(int j=0;j<pairs.size();j++){
                if(bdHomeTeamStrings.contains(pairs.get(j))){
                    boxingDay[i].setClash(pairs.get(j));
                    
                }
            }
            }
            System.out.println(boxingDay[i].toString());
            total+= boxingDay[i].getDistance();

        }
        System.out.println("");
        System.out.println("");
        System.out.println("New Years Day");
        System.out.println("Premier League");
        for (int i = 0; i < newYearsDay.length; i++) {
            if (i == 10) {
                System.out.println("");
                System.out.println("Championship");
            }
            if (i == 22) {
                System.out.println("");
                System.out.println("League One");
            }
            if (i == 34) {
                System.out.println("");
                System.out.println("League Two");
            }
            Team homeTeam = newYearsDay[i].getHomeTeam();
            ArrayList<String> pairs = homeTeam.getPairs();
            if(pairs.size()> 0){
            for(int j=0;j<pairs.size();j++){
                if(nyHomeTeamStrings.contains(pairs.get(j))){
                    newYearsDay[i].setClash(pairs.get(j));
                    
                }
            }
            }
            System.out.println(newYearsDay[i].toString());
            total+= newYearsDay[i].getDistance();
        }
        System.out.println("");
        System.out.println("Final Cost: " + calculateCost());
        System.out.println("Min possible cost: " + getMinCost());
        System.out.println("BD pair clashes: " + boxingDayClashes);
        System.out.println("NY pair clashes: " + newYearsDayClashes);
        System.out.println("BD London Teams: " + boxingDayLondon);
        System.out.println("NY London Teams: " + newYearsDayLondon);
        System.out.println("BD Manchester Teams: " + boxingDayManchester);
        System.out.println("NY Manchester Teams: " + newYearsDayManchester);
        System.out.println("BD London Prem Teams: " + bdPremLondon);
        System.out.println("NY London Prem Teams: " + nyPremLondon);
    }
    
    /**
     * @return the bestCost
     */
    public double getBestCost() {
        return bestCost;
    }
    
    /**
     * @param bestCost the bestCost to set
     */
    public void setBestCost(double bestCost) {
        this.bestCost = bestCost;
    }
    
    /**
     * @return the adjustedCost
     */
    public double getAdjustedCost() {
        return adjustedCost;
    }
    
    /**
     * @return the divisions
     */
    public Division[] getDivisions() {
        return divisions;
    }
    
}
