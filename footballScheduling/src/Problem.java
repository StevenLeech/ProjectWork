
import java.util.ArrayList;
import java.util.Random;

public class Problem {

    private Division[] divisions;
    private double cost;
    private double bestCost;
    int currentCycle;
    private Fixture[] boxingDay = new Fixture[46];
    private Fixture[] newYearsDay = new Fixture[46];

    public Problem(Division[] divisions) {
        this.divisions = divisions;
        this.cost = getCost();
        this.bestCost = cost;
        this.currentCycle = 0;
    }

    public Problem(Problem p) {
        this.copy(p);
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

    public void move(SA sa) {
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
                merge(team1, team2, division, sa);
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
                split(team1, team2, division, sa);
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
            rearrange(team1, team2, division, sa);
        }
        setFixtures();
        cost = getCost();
        if (cost < bestCost) {
            bestCost = cost;
        }
    }

    public double getCost() {
        double total = 0.0;
        for (int j = 0; j < divisions.length; j++) {
            Division division = divisions[j];
            Team[] schedule = division.getSchedule();
            double[][] distances = division.getDistances();
            for (int i = 0; i < schedule.length; i++) {
                double distance = distances[i][schedule[i].getNextTeam()];
                total += distance;
            }
        }
        return total;

    }

    public void merge(Team team1, Team team2, Division division, SA sa) {

        Team[] schedule = division.getSchedule();
        Cycle[] cycles = division.getCycles();
        double[][] distances = division.getDistances();
        Team team3 = schedule[(team1.getNextTeam())];
        Team team4 = schedule[(team2.getPrevTeam())];
        int team1Cycle = team1.getCycle();
        int team2Cycle = team2.getCycle();
        int cycle1Length = cycles[team1Cycle].getSize();
        int cycle2Length = cycles[team2Cycle].getSize();
        double oldCost = distances[team1.getTeamID()][team3.getTeamID()]
                + distances[team2.getTeamID()][team4.getTeamID()];
        double newCost = distances[team1.getTeamID()][team2.getTeamID()]
                + distances[team4.getTeamID()][team3.getTeamID()];
        double deltaCost = newCost - oldCost;
        boolean moveAccepted = sa.metropolis(deltaCost);
        currentCycle = division.getHighestCycle();
        if (moveAccepted == true) {
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
    }

    public void split(Team team1, Team team2, Division division, SA sa) {

        Team[] schedule = division.getSchedule();
        Cycle[] cycles = division.getCycles();
        double[][] distances = division.getDistances();
        Team team3 = schedule[(team1.getNextTeam())];
        Team team4 = schedule[(team2.getPrevTeam())];
        int oldCycle = team1.getCycle();
        currentCycle = division.getHighestCycle();
        double oldCost = distances[team1.getTeamID()][team3.getTeamID()]
                + distances[team4.getTeamID()][team2.getTeamID()];
        double newCost = distances[team1.getTeamID()][team2.getTeamID()]
                + distances[team4.getTeamID()][team3.getTeamID()];
        double deltaCost = newCost - oldCost;
        boolean moveAccepted = sa.metropolis(deltaCost);
        if (moveAccepted == true) {
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
    }

    public void rearrange(Team team1, Team team2, Division division, SA sa) {
        Team[] schedule = division.getSchedule();
        Cycle[] cycles = division.getCycles();
        double[][] distances = division.getDistances();

        Team team3 = schedule[team1.getNextTeam()];
        Team team4 = schedule[team2.getNextTeam()];
        int team1Position = team1.getCyclePosition();
        int team2Position = team2.getCyclePosition();
        int team3Position = team3.getCyclePosition();
        int team4Position = team4.getCyclePosition();
        double oldCost = distances[team1.getTeamID()][team3.getTeamID()]
                + distances[team2.getTeamID()][team4.getTeamID()];
        double newCost = distances[team1.getTeamID()][team2.getTeamID()]
                + distances[team3.getTeamID()][team4.getTeamID()];
        double deltaCost = newCost - oldCost;
        boolean moveAccepted = sa.metropolis(deltaCost);
        if (moveAccepted == true) {
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
    }
    public void reverseCycle(Cycle cycle,Division division){
        Team[] schedule = division.getSchedule();
        int cycleLength = cycle.getCycleSize();
        Team currentTeam = cycle.getTeams().get(0);
        for(int i=0;i<cycleLength;i++){
            int temp = currentTeam.getNextTeam();
            currentTeam.setNextTeam(currentTeam.getPrevTeam());
            currentTeam.setPrevTeam(temp);
            currentTeam = schedule[currentTeam.getPrevTeam()];
        }
    }
    public void adjustDates(Cycle cycle, Division division){
        Team[] schedule = division.getSchedule();
        int cycleLength = cycle.getCycleSize();
        ArrayList<Team> teams = cycle.getTeams();
        ArrayList<Team> adjustedCycle = new ArrayList<Team>();
        Team team;
        team = teams.get(cycleLength-1);
        team.setCyclePosition(0);
        adjustedCycle.add(team);
        for(int i=0;i<cycleLength-1;i++){
            team = teams.get(i);
            team.setCyclePosition(i+1);
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

    public void copy(Problem p) {

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
                        }else if (distance > smallest) {
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
            System.out.println(boxingDay[i].toString());
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
            System.out.println(newYearsDay[i].toString());
        }
        System.out.println("");
        System.out.println("Final Cost: " + getCost());
        System.out.println("Min possible cost: " + getMinCost());

    }
}
