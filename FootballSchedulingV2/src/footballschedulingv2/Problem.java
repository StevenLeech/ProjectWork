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
    private double cost;
    private double bestCost;
    private double adjustedCost;
    int currentCycle;
    private Fixture[] boxingDay = new Fixture[46];
    private Fixture[] newYearsDay = new Fixture[46];

    public Problem(Division[] divisions) {
        this.divisions = divisions;
        setFixtures();
        this.cost = getCost();
        this.bestCost = cost;
        this.currentCycle = 0;
    }

    public Problem(Problem p) {
        divisions = new Division[p.divisions.length];
        for (int i = 0; i < p.divisions.length; i++) {
            divisions[i] = new Division(p.divisions[i]);
        }
        cost = p.cost;
        bestCost = p.bestCost;
        adjustedCost = p.adjustedCost;
        currentCycle = p.currentCycle;
        boxingDay = new Fixture[46];
        newYearsDay = new Fixture[46];
        for (int i = 0; i < p.boxingDay.length; i++) {
            boxingDay[i] = new Fixture(p.boxingDay[i]);
        }
        for (int i = 0; i < p.newYearsDay.length; i++) {
            newYearsDay[i] = new Fixture(p.newYearsDay[i]);
        }
    }
    
    public void setFixtures() {
        int fixture = 0;
        for (int i = 0; i < divisions.length; i++) {
            Cycle[] cycles = divisions[i].getCycles();
            Team[] schedule = divisions[i].getSchedule();
            for (int j = 0; j < cycles.length; j++) {
                Cycle cycle = cycles[j];
                ArrayList<Team> teams = cycle.getTeams();
                if (!cycle.isEmpty()) {

                    Team homeTeam = teams.get(0);
                    for (int k = 0; k < teams.size() / 2; k++) {
                        Team awayTeam = schedule[homeTeam.getNextTeam()];
                        boxingDay[fixture] = new Fixture(homeTeam, awayTeam);
                        fixture++;
                        homeTeam = schedule[awayTeam.getNextTeam()];
                    }
                }
            }
        }
        fixture = 0;
        for (int i = 0; i < divisions.length; i++) {
            Cycle[] cycles = divisions[i].getCycles();
            Team[] schedule = divisions[i].getSchedule();
            for (int j = 0; j < cycles.length; j++) {
                Cycle cycle = cycles[j];
                ArrayList<Team> teams = cycle.getTeams();
                if (!cycle.isEmpty()) {
                    Team team = teams.get(0);
                    Team homeTeam = schedule[team.getNextTeam()];
                    for (int k = 0; k < teams.size() / 2; k++) {
                        Team awayTeam = schedule[homeTeam.getNextTeam()];
                        newYearsDay[fixture] = new Fixture(homeTeam, awayTeam);
                        fixture++;
                        homeTeam = schedule[awayTeam.getNextTeam()];
                    }
                }
            }
        }
    }

    public double modify() {
        Random rand = new Random();
        int selectDivision = rand.nextInt(4);
        Division division = divisions[selectDivision];
        Team[] teams = division.getSchedule();
        Cycle[] cycles = division.getCycles();
        int selectTeam = rand.nextInt(teams.length);
        Team team1 = teams[selectTeam];
        int selectMove = rand.nextInt(3);
        int highestCycle = division.getHighestCycle();
        int team1Cycle = team1.getCycle();
        Team team2 = new Team();
        int selectTeam2 = 0;

        if (selectMove == 0) { //move mergeCycle
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
                selectTeam2 = rand.nextInt(otherCycle.getCycleSize());
                team2 = otherCycle.getTeams().get(selectTeam2);
                merge(team1, team2, division);
            }
        }
        if (selectMove == 1) { //split Cycle
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
                split(team1, team2, division);
            }

        }
        if (selectMove == 2) {//rearrange cycle
            Cycle cycle = cycles[team1Cycle];

            boolean feasible = false;
            while (feasible == false) {
                selectTeam2 = rand.nextInt(cycle.getCycleSize());
                team2 = cycle.getTeams().get(selectTeam2);
                feasible = isRearrangeFeasible(team1, team2, division);
            }
            rearrange(team1, team2, division);
        }
        setFixtures();
        getCost();
        return adjustedCost;
    }

    public double getCost() {
        double total = 0.0;
        double penalty = 0.0;
        int boxingDayClashes = 0;
        int boxingDayLondon = 0;
        int boxingDayManchester = 0;
        int bdPremManchester = 0;
        int bdPremLondon = 0;
        int newYearsDayClashes = 0;
        int newYearsDayLondon = 0;
        int newYearsDayManchester = 0;
        int nyPremLondon = 0;
        int nyPremManchester = 0;
        for (int j = 0; j < divisions.length; j++) {
            Division division = divisions[j];
            Team[] schedule = division.getSchedule();
            double[][] distances = division.getDistances();
            for (int i = 0; i < schedule.length; i++) {
                double distance = distances[i][schedule[i].getNextTeam()];
                total += distance;
            }
        }
        ArrayList<Team> boxingDayHomeTeams = new ArrayList<Team>();
        ArrayList<String> bdHomeTeamStrings = new ArrayList<String>();
        ArrayList<Team> newYearsDayHomeTeams = new ArrayList<Team>();
        ArrayList<String> nyHomeTeamStrings = new ArrayList<String>();

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
                    int item = bdHomeTeamStrings.indexOf(pairs.get(j));
                    bdHomeTeamStrings.remove(pairs.get(j));
                    boxingDayHomeTeams.remove(item);
                }
            }
            if (team.getLocation() == 0) {
                boxingDayLondon++;
                if (team.getDivision() == 0) {
                    bdPremLondon++;
                }
            } else if (team.getLocation() == 1) {
                boxingDayManchester++;
                if (team.getDivision() == 0) {
                    bdPremManchester++;
                }
            }
        }
        for (int i = 0; i < newYearsDayHomeTeams.size(); i++) {
            Team team = newYearsDayHomeTeams.get(i);
            ArrayList<String> pairs = team.getPairs();
            for (int j = 0; j < pairs.size(); j++) {
                if (nyHomeTeamStrings.contains(pairs.get(j))) {
                    newYearsDayClashes++;
                    int item = nyHomeTeamStrings.indexOf(pairs.get(j));
                    nyHomeTeamStrings.remove(pairs.get(j));
                    newYearsDayHomeTeams.remove(item);
                }
            }
            if (team.getLocation() == 0) {
                newYearsDayLondon++;
                if (team.getDivision() == 0) {
                    nyPremLondon++;
                }
            } else if (team.getLocation() == 1) {
                newYearsDayManchester++;
                if (team.getDivision() == 0) {
                    nyPremManchester++;
                }
            }
        }

        cost = total;
        adjustedCost = total + penalty;
        return total;
    }

    public void merge(Team team1, Team team2, Division division) {

        Team[] schedule = division.getSchedule();
        Cycle[] cycles = division.getCycles();
        double[][] distances = division.getDistances();
        Team team3 = schedule[(team1.getNextTeam())];
        Team team4 = schedule[(team2.getPrevTeam())];
        int team1Cycle = team1.getCycle();
        int team2Cycle = team2.getCycle();
        int cycle1Length = cycles[team1Cycle].getSize();
        int cycle2Length = cycles[team2Cycle].getSize();
        currentCycle = division.getHighestCycle();
        team1.setNextTeam(team2.getTeamID());
        team2.setPrevTeam(team1.getTeamID());
        team4.setNextTeam(team3.getTeamID());
        team3.setPrevTeam(team4.getTeamID());
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
    }

    public void split(Team team1, Team team2, Division division) {

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
    }

    public void rearrange(Team team1, Team team2, Division division) {
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
        int cycleLength = cycles[team1.getCycle()].getCycleSize();
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
        currentTeam = team1;
        for (int i = 0; i < cycleLength; i++) {
            currentTeam.setCyclePosition(i);
            currentTeam = schedule[currentTeam.getNextTeam()];
        }

    }

    public void reverseCycle(Cycle cycle, Division division) {
        Team[] schedule = division.getSchedule();
        int cycleLength = cycle.getCycleSize();
        Team currentTeam = cycle.getTeams().get(0);
        for (int i = 0; i < cycleLength; i++) {
            int temp = currentTeam.getNextTeam();
            currentTeam.setNextTeam(currentTeam.getPrevTeam());
            currentTeam.setPrevTeam(temp);
            currentTeam = schedule[currentTeam.getPrevTeam()];
        }
    }

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
    }

    public boolean isRearrangeFeasible(Team team1, Team team2, Division division) {
        Boolean feasible = false;
        if (team1.getNextTeam() != team2.getTeamID() && team2.getNextTeam() != team1.getTeamID()
                && team1 != team2) {
            feasible = true;
        }
        return feasible;
    }

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
        return feasible;

    }

    

    public double getMinCost() {
        double total = 0;
        for (int i = 0; i < divisions.length; i++) {
            double[][] distances = divisions[i].getDistances();
            Team[] teams = divisions[i].getSchedule();
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

    public void checkAndDisplay() {
        int clashes = 0;
        System.out.println("");
        System.out.println("Boxing Day");
        System.out.println("Premier League");
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
            System.out.print(boxingDay[i].toString());
            if (boxingDay[i].isPaired() == true) {
                System.out.print("----Clash");
                clashes++;
            }
            System.out.println("");
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
            System.out.print(newYearsDay[i].toString());
            if (newYearsDay[i].isPaired() == true) {
                System.out.print("----Clash");
                clashes++;
            }
            System.out.println("");
        }
        System.out.println("");
        System.out.println("Final Cost: " + getCost());
        System.out.println("Min possible cost: " + getMinCost());
        System.out.println("Clashes: " + clashes);

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
     * @param adjustedCost the adjustedCost to set
     */
    public void setAdjustedCost(double adjustedCost) {
        this.adjustedCost = adjustedCost;
    }
}
