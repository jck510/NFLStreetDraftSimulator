import java.text.DecimalFormat;

public class Player {


    //player info
    private String firstName;
    private String lastName;
    private String position;
    private double overall;
    private int rawScore;

    //Stats
    private int passing;
    private int speed;
    private int blocking;
    private int agility;
    private int catching;
    private int runPower;
    private int carrying;
    private int tackling;
    private int coverage;
    private int dMoves;



    //default constructor
    public Player() {
        this.firstName = "Empty Name";
        this.lastName = "Empty Last Name";
        this.position = "Blank Position";
        this.passing = 0;
        this.speed = 0;
        this.blocking = 0;
        this.agility = 0;
        this.catching = 0;
        this.runPower = 0;
        this.carrying = 0;
        this.tackling = 0;
        this.coverage = 0;
        this.dMoves = 0;

        this.overall = 0;
        this.rawScore = 0;

    }





    public Player(String first, String last, String pos, int pass, int spe, int block, int agi, int cat, int pow, int car, int tack, int cov, int dmov) {
        this.firstName = first;
        this.lastName = last;
        this.position = pos;
        this.passing = pass;
        this.speed = spe;
        this.blocking = block;
        this.agility = agi;
        this.catching = cat;
        this.runPower = pow;
        this.carrying = car;
        this.tackling = tack;
        this.coverage = cov;
        this.dMoves = dmov;

        overall = 0;
        rawScore = 0;

    }

    public int calcOverall() {


        if(position.equals("QB")){
        //overall = ((.875 * (passing * 5)) + (.12 * (speed * 5)) + (.0015 * (agility * 5)) + (.0015 * (runPower * 5)) + (.002 * (carrying * 5)));
        //overall = ((.865 * (passing * 5)) + (.095 * (speed * 5)) + (.035 * (agility * 5)) + (.0025 * (runPower * 5)) + (.0025 * (carrying * 5)));
        overall = ((.625 * (passing * 5)) + (.25 * (speed * 5)) + (.075 * (agility * 5)) + (.005 * (runPower * 5)) + (.045 * (carrying * 5)));

        overall = overall + 10; // sets a curve for the quarterbacks at 10 points

        }
        else if(position.equals("RB")){
        //overall = ((.3 * (speed * 5)) + (.15 * (agility * 5)) + (.20 * (runPower * 5)) + (.25 * (carrying * 5)) + (.1 * (catching * 5)));
        //overall = ((.70 * (speed * 5)) + (.10 * (agility * 5)) + (.16 * (runPower * 5)) + (.03 * (carrying * 5)) + (.01 * (catching * 5)));
        //overall = ((.20 * (speed * 5)) + (.25 * (agility * 5)) + (.30 * (runPower * 5)) + (.20 * (carrying * 5)) + (.05 * (catching * 5)));
        overall = ((.20 * (speed * 5)) + (.25 * (agility * 5)) + (.30 * (runPower * 5)) + (.20 * (carrying * 5)) + (.05 * (catching * 5)));

        overall = overall + 10; //sets a curve for the running backs at 10 points

        }
        else if(position.equals("WR")){
        overall = ((.35 * (catching * 5)) + (.175 * (speed * 5)) + (.30 * (agility * 5)) + (.05 * (runPower * 5)) + (.05 * (carrying * 5)) + (.075 * (blocking * 5)));

        overall = overall + 10; // sets a curve for the wide receivers at 10 points
        }
        else if(position.equals("OL")){
        overall = ((.825 * (blocking * 5)) + (.10 * (dMoves * 5)) + (.05 * (tackling * 5)) + (.025 * (speed * 5)));

        overall = overall + 7; // sets curve for the offensive linemen at 7 points

        }
        else if(position.equals("DL")){
        overall = ((.15 * (tackling * 5)) + (.15 * (speed * 5)) + (.65 * (dMoves * 5)) + (.05 * (blocking * 5)));

        overall = overall + 7; // sets the curve for the offensive linemen at 7 points


        }
        else if(position.equals("LB")){
        overall = ((.095 * (speed * 5)) + (.05 * (agility * 5)) + (.66 * (tackling * 5)) + (.10 * (coverage * 5)) + (.095 * (dMoves * 5)));

        overall = overall + 10; //sets the curve for the linebackers at 10 points
        }
        else if(position.equals("DB")){
        overall = ((.25 * (speed * 5)) + (.30 * (agility * 5)) + (.085 * (catching * 5)) + (.15 * (tackling * 5)) + (.20 * (coverage * 5)) + (.015 * (dMoves * 5)));
        overall = overall + 10; //sets the curve for the defensive backs at 10 points
        }

        int roundedOverall = (int) Math.round(overall);

        return roundedOverall;
    }

    // returns the sum of all the skill points of the player
    public int calcRawScore(){
        rawScore = (passing + speed + blocking + agility + catching + runPower + carrying + tackling + coverage + dMoves);
        return rawScore;
    }


    //mutators

    public void setFirstName(String name){
        this.firstName = name;
    }

    public void setLastName(String name){
        this.lastName = name;
    }

    public void setPosition(String pos){
        this.position = pos;
    }

    public void setPassing(int pass){
        this.passing = pass;
    }
    public void setSpeed(int spe){
        this.speed = spe;
    }
    public void setBlocking(int block){
        this.blocking = block;
    }
    public void setAgility(int agi){
        this.agility = agi;
    }
    public void setCatching(int cat){
        this.catching = cat;
    }
    public void setRunPower(int pow){
        this.runPower = pow;
    }
    public void setCarrying(int car){
        this.carrying = car;
    }
    public void setTackling(int tack){
        this.tackling = tack;
    }
    public void setCoverage(int cov){
        this.coverage = cov;
    }
    public void setDMoves(int dmov){
        this.dMoves = dmov;
    }

    //accessors

    public String getFirstName(){
        return firstName;
    }

    public String getLastName(){
        return lastName;
    }

    public String getPosition(){
        return position;
    }

    public int getPassing(){
        return passing;
    }
    public int getSpeed(){
        return speed;
    }
    public int getBlocking(){
        return blocking;
    }
    public int getAgility(){
        return agility;
    }
    public int getCatching(){
        return catching;
    }
    public int getRunPower(){
        return runPower;
    }
    public int getCarrying(){
        return carrying;
    }
    public int getTackling(){
        return tackling;
    }
    public int getCoverage(){
        return coverage;
    }
    public int getDMoves(){
        return dMoves;
    }

    public String toString(){

        String playerString;
        playerString = this.getPosition() + " |OVR: " + this.calcOverall() + "|RAW: " + this.calcRawScore() +  "| - " + this.getFirstName() + " " + this.getLastName();

        return playerString;
    }

    // method for starting a player into data
    public String StringDataStore(){
        String playerString = "";
        playerString = this.getFirstName() + " " + this.getLastName() + " " + this.getPosition() + " " + this.getPassing() + " " + this.getSpeed()
                + " " + this.getBlocking() + " " + this.getAgility() + " " + this.getCatching() + " " + this.getRunPower() + " " + this.getCarrying()
                + " " + this.getTackling() + " " + this.getCoverage() + " " + this.getDMoves() + "\n";

        return playerString;
    }


}
