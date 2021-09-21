public class Team {


    private Player team[]; //team will be an array of 7 players
    private String teamName;
    private double teamOverall;
    private int teamRawScore;

    public Team() {

        this.team = new Player[7];
        this.teamName = "Nameless Team";
        this.teamOverall = 0;
        this.teamRawScore = 0;

    }
    void setTeamName(String name){
        this.teamName = name;
    }

    public double calcOverall(){
        teamOverall = 0; //resets the current overall
        for(int i = 0; i < 7; i++){
            teamOverall = teamOverall + team[i].calcOverall();
        }
        teamOverall = teamOverall / 7; //will get the average overall
        teamOverall = Math.round(teamOverall * 100.0) / 100.0; // rounds the team overall to two decimal places

        return teamOverall;
    }

    public int calcRawScore(){
        teamRawScore = 0; //resets the current raw score
        for(int i = 0; i < 7; i++){
            teamRawScore = teamRawScore + team[i].calcRawScore();
        }
        return teamRawScore;
    }

    public void AddPlayer(Player newPlayer, int teamNum){
        team[teamNum] = newPlayer;
    }

    public String getTeamName(){
        return teamName;
    }

    public Player[] getTeamPlayers(){
        return this.team;
    }

}
