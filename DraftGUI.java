import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

public class DraftGUI implements DraftUserInterface{

    private Player [] playersAvailable;

    private JFrame mainFrame;
    private JPanel mainPanel;

    private JFrame frame1;
    private JPanel panel1;

    private JList playerDisplayList;
    private JScrollPane playerScroll;

    private JLabel playerImageLabel;

    private JLabel TeamOneImageLabel;
    private JLabel TeamTwoImageLabel;

    private JLabel playerName;
    private JLabel playerPosition;
    private JLabel overallLabel;
    private JLabel rawLabel;
    private JLabel passRating;
    private JLabel speedRating;
    private JLabel blockRating;
    private JLabel agilityRating;
    private JLabel catchRating;
    private JLabel runPowRating;
    private JLabel carryRating;
    private JLabel tackleRating;
    private JLabel coverageRating;
    private JLabel dmovesRating;

    private JLabel passGraph;
    private JLabel speedGraph;
    private JLabel blockGraph;
    private JLabel agilityGraph;
    private JLabel catchGraph;
    private JLabel runPowGraph;
    private JLabel carryGraph;
    private JLabel tackleGraph;
    private JLabel coverageGraph;
    private JLabel dmovesGraph;

    private JList teamOneList;
    private JScrollPane teamOneScroll;

    private JList teamTwoList;
    private JScrollPane teamTwoScroll;

    private JLabel spaceLabel; // a JLabel that will hold a consistent 20 space width for the box displaying the overall ratings

    //private member variables to hold the team for each user
    private Team teamOnePlayers;
    private Team teamTwoPlayers;

    private Boolean areYouSureDecision; // variable to hold the result of a decision that a user may have

    LinkedList<Player> playerList; //linked list to hold in all of the players from the text file

    //JFileChooser playerImageChooser;


    //default constructor
    public DraftGUI(){}



    public void LoadPlayers(){
        playerList = new LinkedList(); // initializes the playerList as a new LinkedList


        String[] currentPlayer = new String[13]; //array to hold the members of the current player loaded in from the text file
        String playerInfo;

        try {
            Scanner fileReader = new Scanner(new File("PlayersData.txt"));
            while(fileReader.hasNextLine()){ //while there is a next line to read from

                playerInfo = fileReader.nextLine();
                currentPlayer = playerInfo.split(" ");
                Player newPlayer = new Player(currentPlayer[0],currentPlayer[1],currentPlayer[2],Integer.parseInt(currentPlayer[3]),Integer.parseInt(currentPlayer[4]),Integer.parseInt(currentPlayer[5]),Integer.parseInt(currentPlayer[6]),Integer.parseInt(currentPlayer[7]),Integer.parseInt(currentPlayer[8]),Integer.parseInt(currentPlayer[9]),Integer.parseInt(currentPlayer[10]),Integer.parseInt(currentPlayer[11]),Integer.parseInt(currentPlayer[12]));
                playerList.add(newPlayer);

            }

            fileReader.close();


            playersAvailable = new Player[playerList.size()];

            for(int i = 0; i < playerList.size();i++){
                playersAvailable[i] = playerList.get(i);
            }



        }
        catch (Exception e){
            System.out.println("File could not be found!");
        }
    }

    // main menu for the menu screen that loads up pre-draft
    public void mainMenu(){

        mainFrame = new JFrame("NFL Street Draft Simulator Menu");
        mainFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        mainFrame.setBackground(Color.black);
        mainPanel = new JPanel();
        mainFrame.add(mainPanel);


        JLabel mainLogo = new JLabel();

        mainPanel.add(mainLogo);

        // trying to use the image created for the menu but if not it will throw an exception and use text instead
        try{
            BufferedImage mainImage = ImageIO.read(new File("NFL Street draft simulator logo.png"));
            //All Rights Reserved to the NFL and EA Sports for the NFL Street Logo itself


            mainLogo.setIcon(new ImageIcon(mainImage.getScaledInstance(600,300,0)));
        }
        catch(IOException exception){
            mainLogo.setIcon(null);
            mainLogo.setText("NFL Street Draft Simulator by Joshua Knudsen");
        }

        Box controlBox = Box.createHorizontalBox(); // the control box will hold all the options available in the menu


        // button for starting a new draft
        JButton startDraftButton = new JButton("Start New Draft");

        startDraftButton.addActionListener(e ->{

            //since they are starting a new draft two new teams get dynamically allocated
            teamOnePlayers = new Team();
            teamTwoPlayers = new Team();

            RegisterTeamNames(); // from this function it will begin the draft afterwards

            mainFrame.setState(JFrame.ICONIFIED);


        });

        controlBox.add(startDraftButton); // will add the start draft button to the control box

        //button for seeing the results of the most recent draft
        JButton draftResultsButton = new JButton("View Results from the Previous Draft");

        draftResultsButton.addActionListener(e ->{
            DisplayDraftResults();
            //mainFrame.setState(JFrame.ICONIFIED);
        });

        controlBox.add(draftResultsButton);


        //button for viewing/editing the player roster
        JButton viewRosterButton = new JButton("View/Edit Player Roster");

        viewRosterButton.addActionListener(e ->{
            ViewRoster();
        });

        controlBox.add(viewRosterButton);



        mainPanel.add(controlBox); // will add the controlbox to the panel

        // setting the size of the main frame and making it visible to the user
        mainFrame.setSize(750,400);
        mainFrame.setVisible(true);
        mainFrame.setLocationRelativeTo(null);


    }

    // this function executes if the startDraftButton is clicked in the main menu
    public void processCommands() {

        LoadPlayers(); // loads the players from the text file

        frame1 = new JFrame("NFL Street Draft Simulator");
        //frame1.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE); //exits the program if closed
        frame1.setBackground(Color.black);
        panel1 = new JPanel();

        frame1.add(panel1);


        panel1.setLayout(new BorderLayout());

        Box headerBox = Box.createVerticalBox();

        Box teamOneBox = Box.createVerticalBox();


        teamOneList = new JList(); // creates a new JList for the first team drafting
        teamOneList.addListSelectionListener(e ->{
            if(teamOnePlayers.getTeamPlayers()[teamOneList.getSelectedIndex()] != null){ //if the current selected index of the teamOneList is not null

                try{
                    teamTwoList.clearSelection(); // will attempt to clear the selection of the other list if there is a selection made
                }
                catch(Exception error){

                }
                //playerDisplayList.setSelectedIndex(0); //will clear the selection from the player display list
                formatPlayerDisplay(teamOneList.getSelectedIndex(),teamOnePlayers.getTeamPlayers()); //updates the shown player as the current selected player from the list
                //teamOneList.clearSelection(); // clears the selection of the team one list

            }
        });

        JLabel teamOneNameLabel = new JLabel(teamOnePlayers.getTeamName() + "'s Team");
        JLabel teamTwoNameLabel = new JLabel(teamTwoPlayers.getTeamName() + "'s Team");

        TeamOneImageLabel = new JLabel();
        TeamTwoImageLabel = new JLabel();




        teamOneList.setListData(teamOnePlayers.getTeamPlayers());
        teamOneScroll = new JScrollPane(teamOneList);
        teamOneList.setVisibleRowCount(7);

        // loading teamOne's image
        try{
            BufferedImage TeamOneImage = ImageIO.read(new File("TEAMONE.png"));


            TeamOneImageLabel.setIcon(new ImageIcon(TeamOneImage.getScaledInstance(100,75,0)));
            TeamOneImageLabel.setText(null);



        }
        catch (IOException error){

            TeamOneImageLabel.setIcon(null);
            TeamOneImageLabel.setText("Team One");
        }

        teamOneBox.add(TeamOneImageLabel); // adds team one's image to the label
        teamOneBox.add(teamOneNameLabel); // adds team one's name label
        TeamOneImageLabel.setAlignmentX(teamOneBox.CENTER_ALIGNMENT); // will align the team one picture with the center of
                                                                      // team one's vertical box

        teamOneNameLabel.setFont(new Font("Script",Font.BOLD,20));
        teamOneNameLabel.setAlignmentX((teamOneBox.CENTER_ALIGNMENT));


        teamOneScroll.setPreferredSize(new Dimension(250,130));
        teamOneScroll.setMaximumSize(new Dimension(500,130)); //sets the Maximum size of the player box
        teamOneBox.add(teamOneScroll);


        panel1.add(teamOneBox,BorderLayout.WEST);

        Box RosterBox = Box.createVerticalBox();

        Box buttonBox = Box.createHorizontalBox();

        JLabel sortInfo = new JLabel("Sort By:  "); //labeling what you can do with the following buttons
        //Buttons to sort the players
        JButton sortByPositionButton = new JButton("Position");

        sortByPositionButton.addActionListener(e ->{
            playersAvailable = sortPlayersByPosition(playersAvailable);

            playerDisplayList.setListData(playersAvailable);


        });


        JButton sortByOverallButton = new JButton("Overall");

        sortByOverallButton.addActionListener(eTwo ->{
            playersAvailable = sortPlayersByOverall(playersAvailable);
            playerDisplayList.setListData(playersAvailable);
        });

        JButton sortByRawButton = new JButton("RAW Score");

        sortByRawButton.addActionListener(eThree ->{
            playersAvailable = sortPlayersByRawScore(playersAvailable);
            playerDisplayList.setListData(playersAvailable);
        });



        buttonBox.add(sortInfo);
        buttonBox.add(sortByPositionButton);
        buttonBox.add(sortByOverallButton);
        buttonBox.add(sortByRawButton);


        buttonBox.setAlignmentX(Box.LEFT_ALIGNMENT);
        RosterBox.add(buttonBox);



        playerDisplayList = new JList();

        playerDisplayList.setListData(playersAvailable);

        playerScroll = new JScrollPane(playerDisplayList);

        playerDisplayList.setVisibleRowCount(12);

        //the following are atomic in order to function with the actionlisteners and button
        AtomicInteger round = new AtomicInteger(); // variable to hold which round it is
        AtomicBoolean isTeamOnesTurn = new AtomicBoolean(true); // boolean to determine whether or not it is team one's turn


        JLabel roundLabel = new JLabel("Round #" + (round.get() + 1));
        roundLabel.setFont(new Font("Script",Font.BOLD,50));





        JLabel whosTurnLabel = new JLabel( "Team One is on the clock!");
        whosTurnLabel.setFont(new Font("Script",Font.BOLD,25));



        headerBox.add(roundLabel);
        headerBox.add(whosTurnLabel);

        panel1.add(headerBox,BorderLayout.NORTH);



        playerScroll.setMaximumSize(new Dimension(800,500)); //sets the Maximum size of the player box

        RosterBox.add(playerScroll);



        panel1.add(RosterBox,BorderLayout.CENTER);

        JButton draftButton = new JButton("Draft Selected Player");




        draftButton.addActionListener(e -> {
            try{
                //if its team one's turn
                if(isTeamOnesTurn.get()) {
                    teamOnePlayers.AddPlayer(playersAvailable[playerDisplayList.getSelectedIndex()], round.get());
                    //System.out.println(playersAvailable[playerDisplayList.getSelectedIndex()]);

                    isTeamOnesTurn.set(false);
                    whosTurnLabel.setText("Team Two is on the Clock!");
                    teamOneList.setListData(teamOnePlayers.getTeamPlayers()); //updates teamOne's list

                }
                else{
                    teamTwoPlayers.AddPlayer(playersAvailable[playerDisplayList.getSelectedIndex()], round.get()); //will add the selected player and the round they were selected to the team
                    isTeamOnesTurn.set(true);
                    round.getAndIncrement(); //will increment the round since player two has gone
                    roundLabel.setText("Round #" + (round.get() + 1));
                    whosTurnLabel.setText("Team One is on the Clock!");
                    teamTwoList.setListData(teamTwoPlayers.getTeamPlayers()); // updates teamTwo's list
                }
                if(round.get() == 7){
                    //if the round is 7 then end the draft
                    //System.out.println("Draft Over");

                    EndDraft();

                    JFrame draftOverFrame = new JFrame("Draft Over!");
                    JPanel draftOverPanel = new JPanel();

                    draftOverFrame.add(draftOverPanel);

                    Box layoutBox = Box.createVerticalBox();

                    draftOverPanel.add(layoutBox);

                    JLabel draftOverLabel = new JLabel("You have completed this draft!");
                    draftOverLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
                    JLabel rowTwoLabel = new JLabel("To see the results then click on the 'View Results from the Previous Draft' button in the main menu!");
                    rowTwoLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

                    layoutBox.add(draftOverLabel);
                    layoutBox.add(rowTwoLabel);

                    JButton acceptButton = new JButton("Accept");
                    acceptButton.setAlignmentX(Component.CENTER_ALIGNMENT);

                    acceptButton.addActionListener(eOne ->{
                        draftOverFrame.dispatchEvent(new WindowEvent(draftOverFrame,WindowEvent.WINDOW_CLOSING));
                    });

                    layoutBox.add(acceptButton);

                    draftOverFrame.pack();
                    draftOverFrame.setLocationRelativeTo(null);
                    draftOverFrame.setVisible(true);

                }
                /*
                //for every player from the one that got selected to the last player, every player will be shifted upward
                for(int i = playerDisplayList.getSelectedIndex(); i < playersAvailable.length; i++){
                    if(playersAvailable[i] == null){ //if the current player is null then the previous player will be set to null (this will occur in a case where a player other than the first is shifted to the bottom)
                        playersAvailable[i-1] = null;
                        break;
                    }
                    if(i == (playersAvailable.length - 1)){ //last player will be set to null to signify the player that will
                        playersAvailable[i] = null;
                    }
                    playersAvailable[i] = playersAvailable[i+1]; //will shift every player up a space

                }

                 */
                playerList.remove(playersAvailable[playerDisplayList.getSelectedIndex()]); //will remove the selected player from the playerlist
                playersAvailable = new Player[playerList.size()]; //allocates a new array of the new size after player got drafted
                for(int i = 0; i < playerList.size();i++){ //populates the array with the contents of the playerlist
                    playersAvailable[i] = playerList.get(i);
                }

                playerDisplayList.setListData(playersAvailable); //updates the list data with the new array
                playerDisplayList.setSelectedIndex(0); // sets the selected index as the top element of the list
                formatPlayerDisplay(playerDisplayList.getSelectedIndex(),playersAvailable); // updates the display to show the current selected player
                //playerDisplayList.clearSelection(); //will clear the selection

                //will update the UI of all 3 containers

                playerDisplayList.updateUI();
                teamOneList.updateUI();
                teamTwoList.updateUI();


            }
            catch(Exception error){

                // the following code was added to the catch due to an array index out of bounds exception that would pop up due to the list action listeners for team one and team two's lists

                if(round.get() == 7){
                    //if the round is 7 then end the draft
                    //System.out.println("Draft Over");

                    EndDraft();

                    JFrame draftOverFrame = new JFrame("Draft Over!");
                    JPanel draftOverPanel = new JPanel();

                    draftOverFrame.add(draftOverPanel);

                    Box layoutBox = Box.createVerticalBox();

                    draftOverPanel.add(layoutBox);

                    JLabel draftOverLabel = new JLabel("You have completed this draft!");
                    draftOverLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
                    JLabel rowTwoLabel = new JLabel("To see the results then click on the 'View Results from the Previous Draft' button in the main menu!");
                    rowTwoLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

                    layoutBox.add(draftOverLabel);
                    layoutBox.add(rowTwoLabel);

                    JButton acceptButton = new JButton("Accept");
                    acceptButton.setAlignmentX(Component.CENTER_ALIGNMENT);

                    acceptButton.addActionListener(eOne ->{
                        draftOverFrame.dispatchEvent(new WindowEvent(draftOverFrame,WindowEvent.WINDOW_CLOSING));
                    });

                    layoutBox.add(acceptButton);

                    draftOverFrame.pack();
                    draftOverFrame.setLocationRelativeTo(null);
                    draftOverFrame.setVisible(true);

                }

                playerList.remove(playersAvailable[playerDisplayList.getSelectedIndex()]); //will remove the selected player from the playerlist
                playersAvailable = new Player[playerList.size()]; //allocates a new array of the new size after player got drafted
                for(int i = 0; i < playerList.size();i++){ //populates the array with the contents of the playerlist
                    playersAvailable[i] = playerList.get(i);
                }
                playerDisplayList.setListData(playersAvailable); //updates the list data with the new array
                playerDisplayList.setSelectedIndex(0); // sets the selected index as the top element of the list
                formatPlayerDisplay(playerDisplayList.getSelectedIndex(),playersAvailable); // updates the display to show the current selected player
                //playerDisplayList.clearSelection(); //will clear the selection

                //will update the UI of all 3 containers

                playerDisplayList.updateUI();
                teamOneList.updateUI();
                teamTwoList.updateUI();

            }

        });

        RosterBox.add(draftButton);



        //RosterBox.setSize(300,350);

        playerDisplayList.addListSelectionListener(e -> {
            if(!e.getValueIsAdjusting()){
                try{
                    formatPlayerDisplay(playerDisplayList.getSelectedIndex(), playersAvailable);
                }
                catch(Exception exception){
                    playerDisplayList.setSelectedIndex(0);
                    formatPlayerDisplay(0, playersAvailable);
                }
            }
        });




        Box playerBox = getPlayerBox(RosterBox);

        panel1.add(playerBox,BorderLayout.CENTER);


        Box teamTwoBox = Box.createVerticalBox();

        teamTwoList = new JList();
        teamTwoList.addListSelectionListener(e ->{
            if(teamTwoPlayers.getTeamPlayers()[teamTwoList.getSelectedIndex()] != null){ //if the current selected index of the teamTwoList is not null

                //teamOneList.setSelectedIndex(-1);
                //playerDisplayList.setSelectedIndex(-1); //sets selected index as -1
                try{
                    teamOneList.clearSelection(); // tries to clear the selection for team one
                }
                catch (Exception error){

                }
                formatPlayerDisplay(teamTwoList.getSelectedIndex(),teamTwoPlayers.getTeamPlayers());
                //teamTwoList.clearSelection();


            }
        });

        teamTwoList.setListData(teamTwoPlayers.getTeamPlayers());
        teamTwoScroll = new JScrollPane(teamTwoList);
        teamTwoList.setVisibleRowCount(7);

        // loading teamTwo's image
        try{
            BufferedImage TeamTwoImage = ImageIO.read(new File("TEAMTWO.png"));


            TeamTwoImageLabel.setIcon(new ImageIcon(TeamTwoImage.getScaledInstance(100,75,0)));
            TeamTwoImageLabel.setText(null);



        }
        catch (IOException error){

            TeamTwoImageLabel.setIcon(null);
            TeamTwoImageLabel.setText("Team Two");
        }



        teamTwoBox.add(TeamTwoImageLabel); // adds team two's image to the label


        teamTwoBox.add(teamTwoNameLabel); // adds team two's name label
        TeamTwoImageLabel.setAlignmentX(teamTwoBox.CENTER_ALIGNMENT); // will align the team one picture with the center of
                                                                    // team two's vertical box



        teamTwoNameLabel.setFont(new Font("Script",Font.BOLD,20));
        teamTwoNameLabel.setAlignmentX((teamTwoBox.CENTER_ALIGNMENT));


        teamTwoScroll.setPreferredSize(new Dimension(250,130));
        teamTwoScroll.setMaximumSize(new Dimension(500,130)); //sets the Maximum size of the player box
        teamTwoBox.add(teamTwoScroll);

        panel1.add(teamTwoBox,BorderLayout.EAST);

        //teamTwoBox.add(Box.createRigidArea(new Dimension(200,100)));



        frame1.setExtendedState(JFrame.MAXIMIZED_BOTH); // sets the JFrame to full screen
        frame1.setLocationRelativeTo(null);
        frame1.setVisible(true);


    }

    // function takes in a roster box in order to have a spot in the frame to show the player
    private Box getPlayerBox(Box rb){

        Box aboutPlayer = Box.createVerticalBox();


        playerImageLabel = new JLabel();



        playerName = new JLabel();
        playerPosition = new JLabel();

        overallLabel = new JLabel();
        rawLabel = new JLabel();

        passRating = new JLabel();
        speedRating = new JLabel();
        blockRating = new JLabel();
        agilityRating = new JLabel();
        catchRating = new JLabel();
        runPowRating = new JLabel();
        carryRating = new JLabel();
        tackleRating = new JLabel();
        coverageRating = new JLabel();
        dmovesRating = new JLabel();

        passGraph = new JLabel();
        speedGraph = new JLabel();
        blockGraph = new JLabel();
        agilityGraph = new JLabel();
        catchGraph = new JLabel();
        runPowGraph = new JLabel();
        carryGraph = new JLabel();
        tackleGraph = new JLabel();
        coverageGraph = new JLabel();
        dmovesGraph = new JLabel();


        Box statBox = Box.createVerticalBox();

        aboutPlayer.add(playerImageLabel);
        aboutPlayer.add(playerName);
        aboutPlayer.add(playerPosition);
        aboutPlayer.add(overallLabel);
        aboutPlayer.add(rawLabel);

        statBox.add(passRating);
        statBox.add(passGraph);

        statBox.add(speedRating);
        statBox.add(speedGraph);

        statBox.add(blockRating);
        statBox.add(blockGraph);

        statBox.add(agilityRating);
        statBox.add(agilityGraph);

        statBox.add(catchRating);
        statBox.add(catchGraph);

        statBox.add(runPowRating);
        statBox.add(runPowGraph);

        statBox.add(carryRating);
        statBox.add(carryGraph);


        statBox.add(tackleRating);
        statBox.add(tackleGraph);

        statBox.add(coverageRating);
        statBox.add(coverageGraph);

        statBox.add(dmovesRating);
        statBox.add(dmovesGraph);

        spaceLabel = new JLabel();
        statBox.add(spaceLabel);

        statBox.setOpaque(true);
        statBox.setBackground(Color.DARK_GRAY);
        //statBox.setSize(100,300);

        Box playerBox = Box.createHorizontalBox();
        playerBox.add(rb);
        playerBox.add(aboutPlayer);
        playerBox.add(statBox);

        return playerBox;
    }

    private void RegisterTeamNames() {
        JFrame teamNameInfoFrame = new JFrame("Team Registration");
        JPanel registrationPanel = new JPanel();
        teamNameInfoFrame.add(registrationPanel);
        Box registrationBox = Box.createVerticalBox();

        JLabel instructionsLabel = new JLabel("Please enter in the names of both users who will be drafting!");
        JLabel instructionsLabelNote = new JLabel("*Note that Team One will have the first pick!*");
        JLabel teamOneLabel = new JLabel("Team One User's First Name:");
        JTextField teamOneTextField = new JTextField("");
        JLabel teamTwoLabel = new JLabel("Team Two User's First Name:");
        JTextField teamTwoTextField = new JTextField("");

        //adds all of the Swing fragments to the vertical box
        registrationBox.add(instructionsLabel);
        registrationBox.add(instructionsLabelNote);
        registrationBox.add(teamOneLabel);
        registrationBox.add(teamOneTextField);
        registrationBox.add(teamTwoLabel);
        registrationBox.add(teamTwoTextField);


        JButton submitButton = new JButton("Submit");
        submitButton.addActionListener(e -> {

            if(teamOneTextField.getText().equals("")){
                teamOneTextField.setText("Player 1");
            }

            if(teamTwoTextField.getText().equals("")){
                teamTwoTextField.setText("Player 2");
            }


            // gets each team's respective name
            teamOnePlayers.setTeamName(teamOneTextField.getText());
            teamTwoPlayers.setTeamName(teamTwoTextField.getText());



            processCommands(); //begins the draft
            teamNameInfoFrame.dispatchEvent(new WindowEvent(mainFrame,WindowEvent.WINDOW_CLOSING)); //closes the registration window
        });

        registrationBox.add(submitButton);
        registrationPanel.add(registrationBox);



        teamNameInfoFrame.setSize(400,200);
        teamNameInfoFrame.setLocationRelativeTo(null);
        teamNameInfoFrame.setVisible(true);


    }

    // this function will display the results of the previous draft
    public void DisplayDraftResults(){

        LoadResults(); //calls the load results function that will set the teams

        AtomicBoolean isTeamOneShown = new AtomicBoolean(true); // an atomic boolean to determine which list is being shown

        JFrame displayResultsFrame = new JFrame("Draft Results");

        JPanel displayResultsPanel = new JPanel();

        displayResultsFrame.add(displayResultsPanel);


        JList<Player> playerJList = new JList<>(teamOnePlayers.getTeamPlayers());



        Box rosterBox = Box.createVerticalBox(); // vertical box to hold all of the roster info
        JLabel instructionsLabel = new JLabel("Select The Team You would like to View");
        instructionsLabel.setFont(new Font("Script", Font.BOLD,22));
        instructionsLabel.setAlignmentX(Component.CENTER_ALIGNMENT);


        //label to display which team is currently displayed
        JLabel currentTeamLabel = new JLabel("  " + teamOnePlayers.getTeamName() + "'s Team  ");
        currentTeamLabel.setFont(new Font("Script",Font.BOLD,17));
        currentTeamLabel.setForeground(Color.white);
        currentTeamLabel.setOpaque(true);
        currentTeamLabel.setBackground(Color.darkGray);
        currentTeamLabel.setAlignmentX(Component.CENTER_ALIGNMENT);


        try {


            // label to display the overall and raw score of the current team
            JLabel currentTeamOverallLabel = new JLabel("Team Overall: " + teamOnePlayers.calcOverall() + " | RAW Score: " + teamOnePlayers.calcRawScore());
            currentTeamOverallLabel.setFont(new Font("Script", Font.BOLD, 17));
            currentTeamOverallLabel.setForeground(Color.white);
            currentTeamOverallLabel.setOpaque(true);
            currentTeamOverallLabel.setBackground(Color.darkGray);
            currentTeamOverallLabel.setAlignmentX(Component.CENTER_ALIGNMENT);


            rosterBox.add(instructionsLabel);

            Box buttonBox = Box.createHorizontalBox(); // horizontal box to hold the buttons for the roster

            JButton teamOneButton = new JButton("Team One");

            teamOneButton.addActionListener(e -> {

                playerJList.setListData(teamOnePlayers.getTeamPlayers());
                isTeamOneShown.set(true);
                currentTeamLabel.setText("  " + teamOnePlayers.getTeamName() + "'s Team  ");
                currentTeamOverallLabel.setText("Team Overall: " + teamOnePlayers.calcOverall() + " | RAW Score: " + teamOnePlayers.calcRawScore());

            });

            JButton teamTwoButton = new JButton("Team Two");
            teamTwoButton.addActionListener(e -> {
                playerJList.setListData(teamTwoPlayers.getTeamPlayers());
                isTeamOneShown.set(false);
                currentTeamLabel.setText("  " + teamTwoPlayers.getTeamName() + "'s Team  ");
                currentTeamOverallLabel.setText("Team Overall: " + teamTwoPlayers.calcOverall() + " | RAW Score: " + teamTwoPlayers.calcRawScore());

            });


            //list selection listener for showing the stats of the current player
            playerJList.addListSelectionListener(e -> {
                if (!e.getValueIsAdjusting()) {
                    try {
                        if (isTeamOneShown.get()) { //if team one is being shown then it will show a player from team one at the selected index
                            formatPlayerDisplay(playerJList.getSelectedIndex(), teamOnePlayers.getTeamPlayers());
                        } else { // otherwise it will show the player from team two at the given index
                            formatPlayerDisplay(playerJList.getSelectedIndex(), teamTwoPlayers.getTeamPlayers());
                        }

                    } catch (Exception exception) {
                        playerJList.setSelectedIndex(0);
                        if (isTeamOneShown.get()) {
                            formatPlayerDisplay(0, teamOnePlayers.getTeamPlayers());
                        } else {
                            formatPlayerDisplay(0, teamTwoPlayers.getTeamPlayers());
                        }

                    }
                }
            });

            // adds the team buttons to the roster box
            buttonBox.add(teamOneButton);
            buttonBox.add(teamTwoButton);

            rosterBox.add(buttonBox);

            rosterBox.add(currentTeamLabel);
            rosterBox.add(currentTeamOverallLabel);

            rosterBox.add(playerJList); //adds the list to the roster box

            Box playerBox = getPlayerBox(rosterBox); //gets the player box to pack into the panel for displaying the current player

            //adds the roster box and then the player box for the specific player
            displayResultsPanel.add(rosterBox);
            displayResultsPanel.add(playerBox);


            displayResultsFrame.setExtendedState(JFrame.MAXIMIZED_BOTH);
            displayResultsFrame.setLocationRelativeTo(null);
            displayResultsFrame.setVisible(true);

        }
        catch (Exception err){
            // this try catch block is here to prevent the program from crashing in case the user attempts to load draft results when there is no draftresults.txt or when there is no data/unreadable data in the file
        }

    }

    // loads and displays the results of the most recent draft from the Draft Results text file
    private void LoadResults(){
        //dynamically allocates new teams since they will be loaded in from the DraftResults.txt file
        teamOnePlayers = new Team();
        teamTwoPlayers = new Team();

        String[] currentPlayer = new String[13]; // holds each member of the player info
        String playerInfo;


        try{
            Scanner fileReader = new Scanner(new File("DraftResults.txt"));


            teamOnePlayers.setTeamName(fileReader.nextLine()); //sets the team name of team one as the first line

            // will load in each player to the respective spot on team One
            for(int i = 0; i < 7; i++){
                playerInfo = fileReader.nextLine();
                currentPlayer = playerInfo.split(" ");
                Player newPlayer = new Player(currentPlayer[0],currentPlayer[1],currentPlayer[2],Integer.parseInt(currentPlayer[3]),Integer.parseInt(currentPlayer[4]),Integer.parseInt(currentPlayer[5]),Integer.parseInt(currentPlayer[6]),Integer.parseInt(currentPlayer[7]),Integer.parseInt(currentPlayer[8]),Integer.parseInt(currentPlayer[9]),Integer.parseInt(currentPlayer[10]),Integer.parseInt(currentPlayer[11]),Integer.parseInt(currentPlayer[12]));
                teamOnePlayers.getTeamPlayers()[i] = newPlayer;
            }

            teamTwoPlayers.setTeamName(fileReader.nextLine()); //sets the team name of team two

            for(int i = 0; i < 7; i++){
                playerInfo = fileReader.nextLine();
                currentPlayer = playerInfo.split(" ");
                Player newPlayer = new Player(currentPlayer[0],currentPlayer[1],currentPlayer[2],Integer.parseInt(currentPlayer[3]),Integer.parseInt(currentPlayer[4]),Integer.parseInt(currentPlayer[5]),Integer.parseInt(currentPlayer[6]),Integer.parseInt(currentPlayer[7]),Integer.parseInt(currentPlayer[8]),Integer.parseInt(currentPlayer[9]),Integer.parseInt(currentPlayer[10]),Integer.parseInt(currentPlayer[11]),Integer.parseInt(currentPlayer[12]));
                teamTwoPlayers.getTeamPlayers()[i] = newPlayer;
            }


            fileReader.close();


        }
        catch(Exception error){

            JFrame errorFrame = new JFrame("Error Frame");
            JPanel errorPanel = new JPanel();

            errorFrame.add(errorPanel);

            Box layoutBox = Box.createVerticalBox();
            JLabel errorLabel = new JLabel("There was an error loading the Draft Results");
            errorLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            JButton acceptButton = new JButton("Accept");
            acceptButton.setAlignmentX(Component.CENTER_ALIGNMENT);

            acceptButton.addActionListener(e ->{
                errorFrame.dispatchEvent(new WindowEvent(errorFrame,WindowEvent.WINDOW_CLOSING)); // closes the errorFrame
                mainFrame.setState(JFrame.NORMAL); //opens the menu back up
            });

            layoutBox.add(errorLabel);
            layoutBox.add(acceptButton);

            errorPanel.add(layoutBox);


            errorFrame.pack();
            errorFrame.setLocationRelativeTo(null);
            errorFrame.setVisible(true);

        }


    }



    //This function will append a player to the playersData.txt file and close the previous frame passed in
    public void AddNewPlayer(JFrame frameToClose){
        try{
            FileWriter fileWriter = new FileWriter("PlayersData.txt",true); // will append to this file

            JFrame newPlayerFrame = new JFrame("Add New Player");
            JPanel newPlayerPanel = new JPanel();


            newPlayerFrame.add(newPlayerPanel);

            Box FieldsBox = Box.createVerticalBox();


            JLabel instructionsLabel = new JLabel("Please enter in all of the new player's information and stats");
            instructionsLabel.setFont(new Font("Script",Font.BOLD,15));
            instructionsLabel.setAlignmentX(JLabel.CENTER);
            FieldsBox.add(instructionsLabel);



            // first name
            JLabel firstNameLabel = new JLabel("First Name:");
            firstNameLabel.setAlignmentX(JLabel.LEFT);
            JTextField firstNameTextField = new JTextField("");
            FieldsBox.add(firstNameLabel);
            FieldsBox.add(firstNameTextField);

            //last name
            JLabel lastNameLabel = new JLabel("Last Name:");
            lastNameLabel.setAlignmentX(JLabel.LEFT);
            JTextField lastNameTextField = new JTextField("");
            FieldsBox.add(lastNameLabel);
            FieldsBox.add(lastNameTextField);

            // position
            JLabel positionOptionLabel = new JLabel("Position:");
            positionOptionLabel.setAlignmentX(JLabel.LEFT);
            String[] positionOptions = {"QB","RB","WR","OL","DL","LB","DB"};
            JComboBox<String> positionComboBox = new JComboBox<>(positionOptions);
            FieldsBox.add(positionOptionLabel);
            FieldsBox.add(positionComboBox);


            Integer[] statsArray = new Integer[21];

            //will populate the stats array with numbers for the possible rating in each position
            for(int i = 0; i < 21; i++){
                statsArray[i] = i;
            }

            //passing
            JLabel passingLabel = new JLabel("Passing:");
            passingLabel.setAlignmentX(JLabel.LEFT);
            JComboBox<Integer> passingComboBox = new JComboBox<Integer>(statsArray);
            FieldsBox.add(passingLabel);
            FieldsBox.add(passingComboBox);

            //speed
            JLabel speedLabel = new JLabel("Speed:");
            speedLabel.setAlignmentX(JLabel.LEFT);
            JComboBox<Integer> speedComboBox = new JComboBox<Integer>(statsArray);
            FieldsBox.add(speedLabel);
            FieldsBox.add(speedComboBox);


            //blocking
            JLabel blockingLabel = new JLabel("Blocking:");
            blockingLabel.setAlignmentX(JLabel.LEFT);
            JComboBox<Integer> blockingComboBox = new JComboBox<Integer>(statsArray);
            FieldsBox.add(blockingLabel);
            FieldsBox.add(blockingComboBox);

            //agility
            JLabel agilityLabel = new JLabel("Agility:");
            agilityLabel.setAlignmentX(JLabel.LEFT);
            JComboBox<Integer> agilityComboBox = new JComboBox<Integer>(statsArray);
            FieldsBox.add(agilityLabel);
            FieldsBox.add(agilityComboBox);

            //catching
            JLabel catchingLabel = new JLabel("Catching:");
            catchingLabel.setAlignmentX(JLabel.LEFT);
            JComboBox<Integer> catchingComboBox = new JComboBox<Integer>(statsArray);
            FieldsBox.add(catchingLabel);
            FieldsBox.add(catchingComboBox);


            //run power
            JLabel runPowerLabel = new JLabel("Run Power:");
            runPowerLabel.setAlignmentX(JLabel.LEFT);
            JComboBox<Integer> runPowerComboBox = new JComboBox<Integer>(statsArray);
            FieldsBox.add(runPowerLabel);
            FieldsBox.add(runPowerComboBox);


            //carrying
            JLabel carryingLabel = new JLabel("Carrying:");
            carryingLabel.setAlignmentX(JLabel.LEFT);
            JComboBox<Integer> carryingComboBox = new JComboBox<Integer>(statsArray);
            FieldsBox.add(carryingLabel);
            FieldsBox.add(carryingComboBox);


            //tackling
            JLabel tacklingLabel = new JLabel("Tackling:");
            tacklingLabel.setAlignmentX(JLabel.LEFT);
            JComboBox<Integer> tacklingComboBox = new JComboBox<Integer>(statsArray);
            FieldsBox.add(tacklingLabel);
            FieldsBox.add(tacklingComboBox);

            //coverage
            JLabel coverageLabel = new JLabel("Coverage:");
            coverageLabel.setAlignmentX(JLabel.LEFT);
            JComboBox<Integer> coverageComboBox = new JComboBox<Integer>(statsArray);
            FieldsBox.add(coverageLabel);
            FieldsBox.add(coverageComboBox);

            //d-moves
            JLabel dMovesLabel = new JLabel("D-Moves:");
            dMovesLabel.setAlignmentX(JLabel.LEFT);
            JComboBox<Integer> dMovesComboBox = new JComboBox<Integer>(statsArray);
            FieldsBox.add(dMovesLabel);
            FieldsBox.add(dMovesComboBox);

            JButton submitButton = new JButton("Submit");
            submitButton.setAlignmentX(JButton.CENTER);
            submitButton.addActionListener(e ->{
                if(firstNameTextField.getText().equals("")){
                    JFrame firstNameErrorFrame = new JFrame("Please Enter a First Name");
                    JPanel firstNameErrorPanel = new JPanel();

                    firstNameErrorFrame.add(firstNameErrorPanel);
                    JLabel firstNameErrorLabel = new JLabel("It appears that you forgot to enter a First Name. Please go back and enter the Player's first name");
                    firstNameErrorLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
                    JButton acceptFirstNameButton = new JButton("Accept");
                    acceptFirstNameButton.setAlignmentX(Component.CENTER_ALIGNMENT);
                    acceptFirstNameButton.addActionListener(eTwo ->{
                        firstNameErrorFrame.dispatchEvent(new WindowEvent(firstNameErrorFrame,WindowEvent.WINDOW_CLOSING));
                    });

                    firstNameErrorPanel.add(firstNameErrorLabel);
                    firstNameErrorPanel.add(acceptFirstNameButton);

                    //firstNameErrorFrame.setSize(600,100);
                    firstNameErrorFrame.pack();
                    firstNameErrorFrame.setLocationRelativeTo(null);
                    firstNameErrorFrame.setVisible(true);


                }
                if(lastNameTextField.getText().equals("")){
                    JFrame lastNameErrorFrame = new JFrame("Please Enter a Last Name");
                    JPanel lastNameErrorPanel = new JPanel();

                    lastNameErrorFrame.add(lastNameErrorPanel);
                    JLabel lastNameErrorLabel = new JLabel("It appears that you forgot to enter a Last Name. Please go back and enter the Player's last name");
                    lastNameErrorLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
                    JButton acceptLastNameButton = new JButton("Accept");
                    acceptLastNameButton.setAlignmentX(Component.CENTER_ALIGNMENT);
                    acceptLastNameButton.addActionListener(eTwo ->{
                        lastNameErrorFrame.dispatchEvent(new WindowEvent(lastNameErrorFrame,WindowEvent.WINDOW_CLOSING));
                    });

                    lastNameErrorPanel.add(lastNameErrorLabel);
                    lastNameErrorPanel.add(acceptLastNameButton);

                    //lastNameErrorFrame.setSize(600,100);
                    lastNameErrorFrame.pack();
                    lastNameErrorFrame.setLocationRelativeTo(null);
                    lastNameErrorFrame.setVisible(true);


                }
                else{
                    String playerToAdd = firstNameTextField.getText() + " " + lastNameTextField.getText() + " "
                            + positionComboBox.getSelectedItem() + " " + passingComboBox.getSelectedItem() + " "
                            + speedComboBox.getSelectedItem() + " " + blockingComboBox.getSelectedItem() + " "
                            + agilityComboBox.getSelectedItem() + " " + catchingComboBox.getSelectedItem() + " "
                            + runPowerComboBox.getSelectedItem() + " " + carryingComboBox.getSelectedItem() + " "
                            + tacklingComboBox.getSelectedItem() + " " + coverageComboBox.getSelectedItem() + " "
                            + dMovesComboBox.getSelectedItem() + "\n";
                    try {
                        fileWriter.write(playerToAdd);
                        fileWriter.close();

                        ViewRoster();
                        frameToClose.dispatchEvent(new WindowEvent(frameToClose,WindowEvent.WINDOW_CLOSING));

                        JFrame successFrame = new JFrame("Player Successfully Added!");
                        JPanel successPanel = new JPanel();

                        successFrame.add(successPanel);

                        Box layoutBox = Box.createVerticalBox();

                        successPanel.add(layoutBox);

                        JLabel successLabel = new JLabel(firstNameTextField.getText() + " " + lastNameTextField.getText() + " was successfully added to the Player Data!");
                        successLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
                        layoutBox.add(successLabel);
                        JButton successButton = new JButton("Accept");
                        successButton.setAlignmentX(Component.CENTER_ALIGNMENT);
                        successButton.addActionListener(eFour ->{
                            successFrame.dispatchEvent(new WindowEvent(successFrame,WindowEvent.WINDOW_CLOSING));
                        });
                        layoutBox.add(successButton);

                        newPlayerFrame.dispatchEvent(new WindowEvent(newPlayerFrame,WindowEvent.WINDOW_CLOSING));
                        mainFrame.setState(JFrame.NORMAL);


                        //successFrame.setSize(400,100);
                        successFrame.pack();
                        successFrame.setLocationRelativeTo(null);
                        successFrame.setVisible(true);


                    }
                    catch (IOException exception) {
                        System.out.println(playerToAdd);
                        JFrame errorFrame = new JFrame("Error");
                        JPanel errorPanel = new JPanel();
                        JLabel errorLabel = new JLabel("Unable to add the player!");
                        errorLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
                        JButton acceptButton = new JButton("Return to Menu");
                        acceptButton.setAlignmentX(Component.CENTER_ALIGNMENT);

                        acceptButton.addActionListener(eThree ->{
                            errorFrame.dispatchEvent(new WindowEvent(errorFrame,WindowEvent.WINDOW_CLOSING));
                            mainFrame.setState(JFrame.NORMAL);
                        });

                        errorPanel.add(errorLabel);
                        errorPanel.add(acceptButton);

                        errorFrame.add(errorPanel);

                        //errorFrame.setSize(400,100);
                        errorFrame.pack();
                        errorFrame.setVisible(true);
                        errorFrame.setLocationRelativeTo(null);
                    }



                }



            });

            FieldsBox.add(submitButton);


            newPlayerPanel.add(FieldsBox);


            newPlayerFrame.setExtendedState(JFrame.MAXIMIZED_BOTH);
            newPlayerFrame.setLocationRelativeTo(null);
            newPlayerFrame.setVisible(true);


        }
        catch (IOException exception) {
            JFrame errorFrame = new JFrame("Error");
            JPanel errorPanel = new JPanel();

            Box layoutBox = Box.createVerticalBox();

            errorPanel.add(layoutBox);
            JLabel errorLabel = new JLabel("Unable to access the Player Data!");
            errorLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            JButton acceptButton = new JButton("Return to Menu");
            acceptButton.setAlignmentX(Component.CENTER_ALIGNMENT);

            acceptButton.addActionListener(e ->{
                errorFrame.dispatchEvent(new WindowEvent(errorFrame,WindowEvent.WINDOW_CLOSING));
                mainFrame.setState(JFrame.NORMAL);
            });

            layoutBox.add(errorLabel);
            layoutBox.add(acceptButton);

            errorFrame.add(errorPanel);

            //errorFrame.setSize(400,100);
            errorFrame.pack();
            errorFrame.setVisible(true);
            errorFrame.setLocationRelativeTo(null);

        }

    }

    // this function will prompt the list of players and the user may select which player they would like to remove/edit
    public void ViewRoster(){
        LoadPlayers(); // loads the players from the Player Data

        JFrame removePlayerFrame = new JFrame("View/Edit Roster");
        JPanel removePlayerPanel = new JPanel();

        removePlayerFrame.add(removePlayerPanel);

        JLabel welcomeLabel = new JLabel("Welcome to the Roster!");
        welcomeLabel.setFont(new Font("Script",Font.BOLD,25));


        Box RosterBox = Box.createVerticalBox(); // box to put the roster and buttons in

        RosterBox.add(welcomeLabel);
        welcomeLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel instructionsLabel = new JLabel("You may edit, remove and add new players!");
        instructionsLabel.setFont(new Font("Script",Font.BOLD,15));

        Box sortButtonsBox = Box.createHorizontalBox(); // horizontal box to hold the buttons for ways to sort the players

        // label for the sorting instructions
        JLabel sortInstructions = new JLabel("Sort By: ");
        sortInstructions.setAlignmentX(Component.LEFT_ALIGNMENT);
        sortButtonsBox.add(sortInstructions);

        // button to rearrange the player list by position
        JButton positionSortButton = new JButton("Position");
        positionSortButton.addActionListener(e ->{
            playersAvailable = sortPlayersByPosition(playersAvailable); // will sort the playersAvailable by position
            try{ //will try and rewrite the player data with the sorted list of players
                FileWriter fw = new FileWriter(new File("PlayersData.txt"));
                for(int i = 0; i < playersAvailable.length; i++){
                    fw.write(playersAvailable[i].StringDataStore()); // writes the current player to the player data
                }
                playerDisplayList.setListData(playersAvailable);
                playerDisplayList.updateUI();

                fw.close();
            }
            catch(IOException err){

                JFrame errorFrame = new JFrame("Error!");
                JPanel errorPanel = new JPanel();

                errorFrame.add(errorPanel);

                Box errorLayoutBox = Box.createVerticalBox();

                JLabel errorLabel = new JLabel("There was an error resorting the players to the Player Data!");
                errorLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
                JButton acceptButton = new JButton("Accept");
                acceptButton.setAlignmentX(Component.CENTER_ALIGNMENT);
                acceptButton.addActionListener(eOne ->{
                    errorFrame.dispatchEvent(new WindowEvent(errorFrame,WindowEvent.WINDOW_CLOSING)); // closes the error frame
                });

                errorLayoutBox.add(errorLabel);
                errorLayoutBox.add(acceptButton);

                errorPanel.add(errorLayoutBox);


                errorFrame.pack();
                errorFrame.setLocationRelativeTo(null);
                errorFrame.setVisible(true);

            }

        });
        positionSortButton.setAlignmentX(Component.LEFT_ALIGNMENT);
        sortButtonsBox.add(positionSortButton);

        // button to rearrange the player list by overall
        JButton overallSortButton = new JButton("Overall");
        overallSortButton.addActionListener(e ->{
            playersAvailable = sortPlayersByOverall(playersAvailable); // will sort the playersAvailable by overall
            try{ //will try and rewrite the player data with the sorted list of players
                FileWriter fw = new FileWriter(new File("PlayersData.txt"));
                for(int i = 0; i < playersAvailable.length; i++){
                    fw.write(playersAvailable[i].StringDataStore()); // writes the current player to the player data
                }
                playerDisplayList.setListData(playersAvailable);
                playerDisplayList.updateUI();

                fw.close();
            }
            catch(IOException err){

                JFrame errorFrame = new JFrame("Error!");
                JPanel errorPanel = new JPanel();

                errorFrame.add(errorPanel);

                Box errorLayoutBox = Box.createVerticalBox();

                JLabel errorLabel = new JLabel("There was an error resorting the players to the Player Data!");
                errorLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
                JButton acceptButton = new JButton("Accept");
                acceptButton.setAlignmentX(Component.CENTER_ALIGNMENT);
                acceptButton.addActionListener(eOne ->{
                    errorFrame.dispatchEvent(new WindowEvent(errorFrame,WindowEvent.WINDOW_CLOSING)); // closes the error frame
                });

                errorLayoutBox.add(errorLabel);
                errorLayoutBox.add(acceptButton);

                errorPanel.add(errorLayoutBox);


                errorFrame.pack();
                errorFrame.setLocationRelativeTo(null);
                errorFrame.setVisible(true);

            }
        });
        overallSortButton.setAlignmentX(Component.LEFT_ALIGNMENT);
        sortButtonsBox.add(overallSortButton);

        // button to rearrange the player list by RAW Score
        JButton rawSortButton = new JButton("RAW Score");
        rawSortButton.addActionListener(e ->{
            playersAvailable = sortPlayersByRawScore(playersAvailable); // will sort the playersAvailable by RAW Score
            try{ //will try and rewrite the player data with the sorted list of players
                FileWriter fw = new FileWriter(new File("PlayersData.txt"));
                for(int i = 0; i < playersAvailable.length; i++){
                    fw.write(playersAvailable[i].StringDataStore()); // writes the current player to the player data
                }
                playerDisplayList.setListData(playersAvailable);
                playerDisplayList.updateUI();

                fw.close(); // closes the file
            }
            catch(IOException err){

                JFrame errorFrame = new JFrame("Error!");
                JPanel errorPanel = new JPanel();

                errorFrame.add(errorPanel);

                Box errorLayoutBox = Box.createVerticalBox();

                JLabel errorLabel = new JLabel("There was an error resorting the players to the Player Data!");
                errorLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
                JButton acceptButton = new JButton("Accept");
                acceptButton.setAlignmentX(Component.CENTER_ALIGNMENT);
                acceptButton.addActionListener(eOne ->{
                    errorFrame.dispatchEvent(new WindowEvent(errorFrame,WindowEvent.WINDOW_CLOSING)); // closes the error frame
                });

                errorLayoutBox.add(errorLabel);
                errorLayoutBox.add(acceptButton);

                errorPanel.add(errorLayoutBox);


                errorFrame.pack();
                errorFrame.setLocationRelativeTo(null);
                errorFrame.setVisible(true);

            }
        });
        rawSortButton.setAlignmentX(Component.LEFT_ALIGNMENT);
        sortButtonsBox.add(rawSortButton);


        RosterBox.add(instructionsLabel);
        instructionsLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        RosterBox.add(sortButtonsBox);

        playerDisplayList = new JList(playersAvailable);
        playerScroll = new JScrollPane(playerDisplayList);

        playerDisplayList.setVisibleRowCount(23);

        //list selection listener for showing the stats of the current player
        playerDisplayList.addListSelectionListener(e -> {
            if(!e.getValueIsAdjusting()){
                try{
                    teamOneList.clearSelection();
                    teamTwoList.clearSelection();
                }
                catch(Exception clrError){

                }
                try{
                    formatPlayerDisplay(playerDisplayList.getSelectedIndex(), playersAvailable);
                }
                catch(Exception exception){
                    playerDisplayList.setSelectedIndex(0);
                    formatPlayerDisplay(0, playersAvailable);
                }
            }
        });

        RosterBox.add(playerScroll);

        Box buttonBox = Box.createHorizontalBox();

        //buttons to edit selected player and buttons to delete selected player
        JButton editButton = new JButton("Edit Player");

        editButton.addActionListener(e -> {

            if(playerDisplayList.getSelectedIndex() == -1){ // if there is no player selected then a pop up will appear to inform the user that they must select a player
                JFrame unselectedFrame = new JFrame("No Player Selected");
                JPanel unselectedPanel = new JPanel();

                unselectedFrame.add(unselectedPanel);

                Box layoutBox = Box.createVerticalBox();

                JLabel unselectedLabel = new JLabel("There is no player selected. Please select a player and try again!");
                unselectedLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
                JButton acceptButton = new JButton("Accept");
                acceptButton.setAlignmentX(Component.CENTER_ALIGNMENT);
                acceptButton.addActionListener(eOne ->{
                    unselectedFrame.dispatchEvent(new WindowEvent(unselectedFrame,WindowEvent.WINDOW_CLOSING)); // closes the unselected frame
                });

                layoutBox.add(unselectedLabel);
                layoutBox.add(acceptButton);

                unselectedPanel.add(layoutBox);

                unselectedFrame.pack();
                unselectedFrame.setLocationRelativeTo(null);
                unselectedFrame.setVisible(true);

            }
            else{
                EditPlayer(playersAvailable[playerDisplayList.getSelectedIndex()]); // passes in the current selected player

            }

        });


        JButton deleteButton = new JButton("Delete Player");

        deleteButton.addActionListener(e ->{
            //System.out.println(playerDisplayList.getSelectedIndex());

            if(playerDisplayList.getSelectedIndex() == -1){ // if there is no player selected then a pop up will appear to inform the user that they must select a player
                JFrame unselectedFrame = new JFrame("No Player Selected");
                JPanel unselectedPanel = new JPanel();

                unselectedFrame.add(unselectedPanel);

                Box layoutBox = Box.createVerticalBox();

                JLabel unselectedLabel = new JLabel("There is no player selected. Please select a player and try again!");
                unselectedLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
                JButton acceptButton = new JButton("Accept");
                acceptButton.setAlignmentX(Component.CENTER_ALIGNMENT);
                acceptButton.addActionListener(eOne ->{
                    unselectedFrame.dispatchEvent(new WindowEvent(unselectedFrame,WindowEvent.WINDOW_CLOSING)); // closes the unselected frame
                });

                layoutBox.add(unselectedLabel);
                layoutBox.add(acceptButton);

                unselectedPanel.add(layoutBox);

                unselectedFrame.pack();
                unselectedFrame.setLocationRelativeTo(null);
                unselectedFrame.setVisible(true);

            }
            else{
                DeletePlayer(playersAvailable[playerDisplayList.getSelectedIndex()], playerDisplayList.getSelectedIndex(), removePlayerFrame);
            }


        });

        //button for adding a new player to the player data
        JButton registerNewFootballPlayerButton = new JButton("Add New Player");

        registerNewFootballPlayerButton.addActionListener(e ->{
            AddNewPlayer(removePlayerFrame);
        });


        buttonBox.add(registerNewFootballPlayerButton);
        buttonBox.add(editButton);
        buttonBox.add(deleteButton);

        JButton addPictureButton = new JButton("Add Image For Player");

        addPictureButton.addActionListener(e ->{

            if(playerDisplayList.getSelectedIndex() == -1){ // if there is no player selected then a pop up will appear to inform the user that they must select a player
                JFrame unselectedFrame = new JFrame("No Player Selected");
                JPanel unselectedPanel = new JPanel();

                unselectedFrame.add(unselectedPanel);

                Box layoutBox = Box.createVerticalBox();

                JLabel unselectedLabel = new JLabel("There is no player selected. Please select a player and try again!");
                unselectedLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
                JButton acceptButton = new JButton("Accept");
                acceptButton.setAlignmentX(Component.CENTER_ALIGNMENT);
                acceptButton.addActionListener(eOne ->{
                    unselectedFrame.dispatchEvent(new WindowEvent(unselectedFrame,WindowEvent.WINDOW_CLOSING)); // closes the unselected frame
                });

                layoutBox.add(unselectedLabel);
                layoutBox.add(acceptButton);

                unselectedPanel.add(layoutBox);

                unselectedFrame.pack();
                unselectedFrame.setLocationRelativeTo(null);
                unselectedFrame.setVisible(true);

            }
            else{ // this is the case where there is a player who is selected
                JFileChooser fileChooser = new JFileChooser();
                FileNameExtensionFilter extensionFilter = new FileNameExtensionFilter("JPG Files","jpg");
                fileChooser.setFileFilter(extensionFilter);
                fileChooser.setCurrentDirectory(new File(System.getProperty("user.home") + "\\Downloads"));
                int returnVal = fileChooser.showOpenDialog(null);
                if(returnVal == JFileChooser.APPROVE_OPTION) {

                    File currentFile = fileChooser.getSelectedFile();

                    String moveDirectory = System.getProperty("user.dir") + "\\" + playersAvailable[playerDisplayList.getSelectedIndex()].getFirstName().toUpperCase() + playersAvailable[playerDisplayList.getSelectedIndex()].getLastName().toUpperCase() + ".jpg"; // string to hold the directory that the file will get moved to
                    File checkFile = new File(moveDirectory);
                    if(checkFile.exists()){ //if the file already exists in the directory
                        JFrame replaceFrame = new JFrame("File Exists!");
                        JPanel replacePanel = new JPanel();
                        replaceFrame.add(replacePanel);

                        Box layoutBox = Box.createVerticalBox();

                        JLabel replaceLabel = new JLabel("This file already exists! Would you like to replace it?");
                        replaceLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
                        layoutBox.add(replaceLabel);

                        Box optionBox = Box.createHorizontalBox();
                        JButton replaceButton = new JButton("Replace");

                        replaceButton.setAlignmentX(Component.CENTER_ALIGNMENT);

                        replaceButton.addActionListener(eOne ->{
                            checkFile.delete(); // deletes the file that is already there
                            currentFile.renameTo(new File(moveDirectory)); //adds the new file in its place

                            formatPlayerDisplay(playerDisplayList.getSelectedIndex(),playersAvailable);

                            replaceFrame.dispatchEvent(new WindowEvent(replaceFrame,WindowEvent.WINDOW_CLOSING)); // closes the window

                        });

                        JButton cancelButton = new JButton("Cancel");

                        cancelButton.setAlignmentX(Component.CENTER_ALIGNMENT);

                        cancelButton.addActionListener(eTwo ->{
                            replaceFrame.dispatchEvent(new WindowEvent(replaceFrame,WindowEvent.WINDOW_CLOSING)); //closes the window
                        });

                        // adds the buttons to the option box
                        optionBox.add(replaceButton);
                        optionBox.add(cancelButton);

                        layoutBox.add(optionBox);

                        replacePanel.add(layoutBox);

                        replaceFrame.pack();
                        replaceFrame.setLocationRelativeTo(null);
                        replaceFrame.setVisible(true);
                    }
                    else{
                        currentFile.renameTo(new File(moveDirectory)); //adds the new file into the project directory

                        formatPlayerDisplay(playerDisplayList.getSelectedIndex(),playersAvailable); //updates the display
                    }


                }
            }




        });

        //buttonBox.add(addPictureButton);

        RosterBox.add(buttonBox);

        JButton removePictureButton = new JButton("Remove Player Image");
        removePictureButton.addActionListener(e ->{
            if(playerDisplayList.getSelectedIndex() == -1){ // if there is no player selected then a pop up will appear to inform the user that they must select a player
                JFrame unselectedFrame = new JFrame("No Player Selected");
                JPanel unselectedPanel = new JPanel();

                unselectedFrame.add(unselectedPanel);

                Box layoutBox = Box.createVerticalBox();

                JLabel unselectedLabel = new JLabel("There is no player selected. Please select a player and try again!");
                unselectedLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
                JButton acceptButton = new JButton("Accept");
                acceptButton.setAlignmentX(Component.CENTER_ALIGNMENT);
                acceptButton.addActionListener(eOne ->{
                    unselectedFrame.dispatchEvent(new WindowEvent(unselectedFrame,WindowEvent.WINDOW_CLOSING)); // closes the unselected frame
                });

                layoutBox.add(unselectedLabel);
                layoutBox.add(acceptButton);

                unselectedPanel.add(layoutBox);

                unselectedFrame.pack();
                unselectedFrame.setLocationRelativeTo(null);
                unselectedFrame.setVisible(true);

            }
            else{
                // remove the picture
                String picDirectory = System.getProperty("user.dir") + "\\" + playersAvailable[playerDisplayList.getSelectedIndex()].getFirstName().toUpperCase() + playersAvailable[playerDisplayList.getSelectedIndex()].getLastName().toUpperCase() + ".jpg"; // string to hold the directory that the file will get moved to
                File checkFile = new File(picDirectory);

                if(checkFile.exists()){
                    JFrame areYouSureFrame = new JFrame("Remove Image Confirmation");
                    JPanel areYouSurePanel = new JPanel();

                    areYouSureFrame.add(areYouSurePanel);

                    Box areYouSureLayoutBox = Box.createVerticalBox();

                    areYouSurePanel.add(areYouSureLayoutBox);

                    JLabel areYouSureLabel = new JLabel("You are about to delete '" + playersAvailable[playerDisplayList.getSelectedIndex()].getFirstName().toUpperCase() + playersAvailable[playerDisplayList.getSelectedIndex()].getLastName().toUpperCase() + ".jpg', Are you sure you would like to delete this file?");
                    areYouSureLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

                    areYouSureLayoutBox.add(areYouSureLabel); // adds the label to the layout box

                    Box areYouSureButtonBox = Box.createHorizontalBox();

                    //button to confirm the deletion
                    JButton confirmDeleteButton = new JButton("Confirm");
                    confirmDeleteButton.setAlignmentX(Component.CENTER_ALIGNMENT);
                    confirmDeleteButton.addActionListener(eOne ->{
                        checkFile.delete(); // deletes the file
                        formatPlayerDisplay(playerDisplayList.getSelectedIndex(),playersAvailable);
                        areYouSureFrame.dispatchEvent(new WindowEvent(areYouSureFrame,WindowEvent.WINDOW_CLOSING)); // closes the window
                    });
                    areYouSureButtonBox.add(confirmDeleteButton);

                    //button to cancel the deletion
                    JButton cancelDeleteButton = new JButton("Cancel");
                    cancelDeleteButton.setAlignmentX(Component.CENTER_ALIGNMENT);
                    cancelDeleteButton.addActionListener(eTwo ->{
                        areYouSureFrame.dispatchEvent(new WindowEvent(areYouSureFrame,WindowEvent.WINDOW_CLOSING)); //will close the window
                    });
                    areYouSureButtonBox.add(cancelDeleteButton);

                    areYouSureLayoutBox.add(areYouSureButtonBox);


                    areYouSureFrame.pack();
                    areYouSureFrame.setLocationRelativeTo(null);
                    areYouSureFrame.setVisible(true);

                }
                else{
                    JFrame doesNotExistFrame = new JFrame("File does not exist!");
                    JPanel doesNotExistPanel = new JPanel();

                    doesNotExistFrame.add(doesNotExistPanel);

                    Box dneLayoutBox = Box.createVerticalBox();

                    JLabel dneLabel = new JLabel("The Player Image for " + playersAvailable[playerDisplayList.getSelectedIndex()].getFirstName() + " " + playersAvailable[playerDisplayList.getSelectedIndex()].getLastName() + " does not exist!");
                    dneLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

                    JButton dneAcceptButton = new JButton("Accept");
                    dneAcceptButton.setAlignmentX(Component.CENTER_ALIGNMENT);
                    dneAcceptButton.addActionListener(eOne ->{
                        doesNotExistFrame.dispatchEvent(new WindowEvent(doesNotExistFrame,WindowEvent.WINDOW_CLOSING));
                    });

                    dneLayoutBox.add(dneLabel);
                    dneLayoutBox.add(dneAcceptButton);

                    doesNotExistPanel.add(dneLayoutBox);

                    doesNotExistFrame.pack();
                    doesNotExistFrame.setLocationRelativeTo(null);
                    doesNotExistFrame.setVisible(true);
                }

            }
        });

        Box buttonBoxRowTwo = Box.createHorizontalBox();

        buttonBoxRowTwo.add(addPictureButton);
        buttonBoxRowTwo.add(removePictureButton);

        //removePictureButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        RosterBox.add(buttonBoxRowTwo); // adds the second row of the button box


        Box playerBox = getPlayerBox(RosterBox);



        removePlayerPanel.add(playerBox);


        removePlayerFrame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        removePlayerFrame.setLocationRelativeTo(null);
        removePlayerFrame.setVisible(true);


    }

    // this function will edit the stats/information of the given player
    private void EditPlayer(Player editPlayer){
        JFrame editPlayerFrame = new JFrame("Edit Player");
        JPanel editPlayerPanel = new JPanel();

        editPlayerFrame.add(editPlayerPanel); // adds the panel to the frame


        Box FieldsBox = Box.createVerticalBox(); // box to hold all of the fields for the player that needs to be updated


        JLabel instructionsLabel = new JLabel("Edit the Stats of " + editPlayer.getFirstName() + " " + editPlayer.getLastName());
        instructionsLabel.setFont(new Font("Script",Font.BOLD,15));
        instructionsLabel.setAlignmentX(JLabel.CENTER);
        FieldsBox.add(instructionsLabel);

        // first name
        JLabel firstNameLabel = new JLabel("First Name:");
        firstNameLabel.setAlignmentX(JLabel.LEFT);
        JTextField firstNameTextField = new JTextField(editPlayer.getFirstName());
        FieldsBox.add(firstNameLabel);
        FieldsBox.add(firstNameTextField);


        // last name
        JLabel lastNameLabel = new JLabel("Last Name:");
        lastNameLabel.setAlignmentX(JLabel.LEFT);
        JTextField lastNameTextField = new JTextField(editPlayer.getLastName());
        FieldsBox.add(lastNameLabel);
        FieldsBox.add(lastNameTextField);



        // position
        JLabel positionOptionLabel = new JLabel("Position:");
        positionOptionLabel.setAlignmentX(JLabel.LEFT);
        String[] positionOptions = {"QB","RB","WR","OL","DL","LB","DB"};
        JComboBox<String> positionComboBox = new JComboBox<>(positionOptions);
        int positionIndex = 0; //sets the index to 0 by default. This is meant to get the index corresponding to the position that the player plays

        // switch statement to get the proper position index
        switch (editPlayer.getPosition()){
            case "QB":
                positionIndex = 0;
                break;
            case "RB":
                positionIndex = 1;
                break;
            case "WR":
                positionIndex = 2;
                break;
            case "OL":
                positionIndex = 3;
                break;
            case "DL":
                positionIndex = 4;
                break;
            case "LB":
                positionIndex = 5;
                break;
            case "DB":
                positionIndex = 6;
                break;
        }
        positionComboBox.setSelectedIndex(positionIndex);
        FieldsBox.add(positionOptionLabel);
        FieldsBox.add(positionComboBox);


        Integer[] statsArray = new Integer[21];

        //will populate the stats array with numbers for the possible rating in each position
        for(int i = 0; i < 21; i++){
            statsArray[i] = i;
        }

        //passing
        JLabel passingLabel = new JLabel("Passing:");
        passingLabel.setAlignmentX(JLabel.LEFT);
        JComboBox<Integer> passingComboBox = new JComboBox<Integer>(statsArray);
        passingComboBox.setSelectedIndex(editPlayer.getPassing());
        FieldsBox.add(passingLabel);
        FieldsBox.add(passingComboBox);

        //speed
        JLabel speedLabel = new JLabel("Speed:");
        speedLabel.setAlignmentX(JLabel.LEFT);
        JComboBox<Integer> speedComboBox = new JComboBox<Integer>(statsArray);
        speedComboBox.setSelectedIndex(editPlayer.getSpeed());
        FieldsBox.add(speedLabel);
        FieldsBox.add(speedComboBox);


        //blocking
        JLabel blockingLabel = new JLabel("Blocking:");
        blockingLabel.setAlignmentX(JLabel.LEFT);
        JComboBox<Integer> blockingComboBox = new JComboBox<Integer>(statsArray);
        blockingComboBox.setSelectedIndex(editPlayer.getBlocking());
        FieldsBox.add(blockingLabel);
        FieldsBox.add(blockingComboBox);

        //agility
        JLabel agilityLabel = new JLabel("Agility:");
        agilityLabel.setAlignmentX(JLabel.LEFT);
        JComboBox<Integer> agilityComboBox = new JComboBox<Integer>(statsArray);
        agilityComboBox.setSelectedIndex(editPlayer.getAgility());
        FieldsBox.add(agilityLabel);
        FieldsBox.add(agilityComboBox);

        //catching
        JLabel catchingLabel = new JLabel("Catching:");
        catchingLabel.setAlignmentX(JLabel.LEFT);
        JComboBox<Integer> catchingComboBox = new JComboBox<Integer>(statsArray);
        catchingComboBox.setSelectedIndex(editPlayer.getCatching());
        FieldsBox.add(catchingLabel);
        FieldsBox.add(catchingComboBox);


        //run power
        JLabel runPowerLabel = new JLabel("Run Power:");
        runPowerLabel.setAlignmentX(JLabel.LEFT);
        JComboBox<Integer> runPowerComboBox = new JComboBox<Integer>(statsArray);
        runPowerComboBox.setSelectedIndex(editPlayer.getRunPower());
        FieldsBox.add(runPowerLabel);
        FieldsBox.add(runPowerComboBox);


        //carrying
        JLabel carryingLabel = new JLabel("Carrying:");
        carryingLabel.setAlignmentX(JLabel.LEFT);
        JComboBox<Integer> carryingComboBox = new JComboBox<Integer>(statsArray);
        carryingComboBox.setSelectedIndex(editPlayer.getCarrying());
        FieldsBox.add(carryingLabel);
        FieldsBox.add(carryingComboBox);


        //tackling
        JLabel tacklingLabel = new JLabel("Tackling:");
        tacklingLabel.setAlignmentX(JLabel.LEFT);
        JComboBox<Integer> tacklingComboBox = new JComboBox<Integer>(statsArray);
        tacklingComboBox.setSelectedIndex(editPlayer.getTackling());
        FieldsBox.add(tacklingLabel);
        FieldsBox.add(tacklingComboBox);

        //coverage
        JLabel coverageLabel = new JLabel("Coverage:");
        coverageLabel.setAlignmentX(JLabel.LEFT);
        JComboBox<Integer> coverageComboBox = new JComboBox<Integer>(statsArray);
        coverageComboBox.setSelectedIndex(editPlayer.getCoverage());
        FieldsBox.add(coverageLabel);
        FieldsBox.add(coverageComboBox);

        //d-moves
        JLabel dMovesLabel = new JLabel("D-Moves:");
        dMovesLabel.setAlignmentX(JLabel.LEFT);
        JComboBox<Integer> dMovesComboBox = new JComboBox<Integer>(statsArray);
        dMovesComboBox.setSelectedIndex(editPlayer.getDMoves());
        FieldsBox.add(dMovesLabel);
        FieldsBox.add(dMovesComboBox);

        JButton submitButton = new JButton("Submit");
        submitButton.setAlignmentX(JButton.CENTER);
        submitButton.addActionListener(e ->{
            if(firstNameTextField.getText().equals("")){
                JFrame firstNameErrorFrame = new JFrame("Please Enter a First Name");
                JPanel firstNameErrorPanel = new JPanel();

                firstNameErrorFrame.add(firstNameErrorPanel);
                JLabel firstNameErrorLabel = new JLabel("It appears that you forgot to enter a First Name. Please go back and enter the Player's first name");
                JButton acceptFirstNameButton = new JButton("Accept");
                acceptFirstNameButton.addActionListener(eTwo ->{
                    firstNameErrorFrame.dispatchEvent(new WindowEvent(firstNameErrorFrame,WindowEvent.WINDOW_CLOSING));
                });

                firstNameErrorPanel.add(firstNameErrorLabel);
                firstNameErrorPanel.add(acceptFirstNameButton);

                //firstNameErrorFrame.setSize(600,100);
                firstNameErrorFrame.pack();
                firstNameErrorFrame.setLocationRelativeTo(null);
                firstNameErrorFrame.setVisible(true);


            }
            if(lastNameTextField.getText().equals("")){
                JFrame lastNameErrorFrame = new JFrame("Please Enter a Last Name");
                JPanel lastNameErrorPanel = new JPanel();

                lastNameErrorFrame.add(lastNameErrorPanel);
                JLabel lastNameErrorLabel = new JLabel("It appears that you forgot to enter a Last Name. Please go back and enter the Player's last name");
                JButton acceptLastNameButton = new JButton("Accept");
                acceptLastNameButton.addActionListener(eTwo ->{
                    lastNameErrorFrame.dispatchEvent(new WindowEvent(lastNameErrorFrame,WindowEvent.WINDOW_CLOSING));
                });

                lastNameErrorPanel.add(lastNameErrorLabel);
                lastNameErrorPanel.add(acceptLastNameButton);

                //lastNameErrorFrame.setSize(600,100);
                lastNameErrorFrame.pack();
                lastNameErrorFrame.setLocationRelativeTo(null);
                lastNameErrorFrame.setVisible(true);


            }
            else{
                try{
                    FileWriter fileWriter = new FileWriter(new File("PlayersData.txt"));

                    editPlayer.setFirstName(firstNameTextField.getText());
                    editPlayer.setLastName(lastNameTextField.getText());
                    editPlayer.setPosition(positionComboBox.getSelectedItem().toString());
                    editPlayer.setPassing(passingComboBox.getSelectedIndex());
                    editPlayer.setSpeed(speedComboBox.getSelectedIndex());
                    editPlayer.setBlocking((blockingComboBox.getSelectedIndex()));
                    editPlayer.setAgility(agilityComboBox.getSelectedIndex());
                    editPlayer.setCatching(catchingComboBox.getSelectedIndex());
                    editPlayer.setRunPower(runPowerComboBox.getSelectedIndex());
                    editPlayer.setCarrying(carryingComboBox.getSelectedIndex());
                    editPlayer.setTackling(tacklingComboBox.getSelectedIndex());
                    editPlayer.setCoverage(coverageComboBox.getSelectedIndex());
                    editPlayer.setDMoves(dMovesComboBox.getSelectedIndex());



                    for(int i = 0; i < playerList.size(); i++){ //for every player that exists, they will be written and saved to the text file
                        fileWriter.write(playerList.get(i).StringDataStore());
                    }

                    fileWriter.close();

                    //frameToClose.dispatchEvent(new WindowEvent(frameToClose, WindowEvent.WINDOW_CLOSING));
                    //ViewRoster();

                    formatPlayerDisplay(playerDisplayList.getSelectedIndex(),playersAvailable); // gets called to update the display of the updated player


                    JFrame successFrame = new JFrame("Player Successfully Updated!");
                    JPanel successPanel = new JPanel();

                    successFrame.add(successPanel);

                    Box layoutBox = Box.createVerticalBox();

                    successPanel.add(layoutBox);

                    JLabel successLabel = new JLabel(editPlayer.getFirstName() + " " + editPlayer.getLastName() + " was successfully updated!");
                    successLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
                    layoutBox.add(successLabel);
                    JButton successButton = new JButton("Accept");
                    successButton.setAlignmentX(Component.CENTER_ALIGNMENT);
                    successButton.addActionListener(eFour ->{
                        successFrame.dispatchEvent(new WindowEvent(successFrame,WindowEvent.WINDOW_CLOSING));
                    });
                    layoutBox.add(successButton);

                    editPlayerFrame.dispatchEvent(new WindowEvent(editPlayerFrame,WindowEvent.WINDOW_CLOSING));
                    //mainFrame.setState(JFrame.NORMAL); // reopens the main menu


                    //successFrame.setSize(400,100);
                    successFrame.pack();
                    successFrame.setLocationRelativeTo(null);
                    successFrame.setVisible(true);




                }
                catch(IOException ioerror){

                    JFrame errorFrame = new JFrame("Error");
                    JPanel errorPanel = new JPanel();
                    JLabel errorLabel = new JLabel("Unable to edit the player!");
                    JButton acceptButton = new JButton("Return to Menu");

                    acceptButton.addActionListener(eThree ->{
                        errorFrame.dispatchEvent(new WindowEvent(errorFrame,WindowEvent.WINDOW_CLOSING));
                        mainFrame.setState(JFrame.NORMAL);
                    });

                    Box errorBox = Box.createVerticalBox();

                    errorBox.add(errorLabel);
                    errorBox.add(acceptButton);

                    errorPanel.add(errorBox);

                    errorFrame.add(errorPanel);

                    //errorFrame.setSize(400,100);
                    errorFrame.pack();
                    errorFrame.setVisible(true);
                    errorFrame.setLocationRelativeTo(null);

                }
            }






        });

        FieldsBox.add(submitButton);


        editPlayerPanel.add(FieldsBox);



        editPlayerFrame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        editPlayerFrame.setLocationRelativeTo(null);
        editPlayerFrame.setVisible(true);

    }

    // this function will delete prompt the user as to whether or not they would like to delete the player and if yes the function will remove a player from the Player Data
    private void DeletePlayer(Player delPlayer, int positionInPlayerData, JFrame frameToClose){

        LoadPlayers(); // will reload the players to delete the correct player

        JFrame areYouSureFrame = new JFrame("Please Confirm");
        JPanel areYouSurePanel = new JPanel();

        Box promptBox = Box.createVerticalBox();

        areYouSureFrame.add(areYouSurePanel);
        areYouSurePanel.add(promptBox);

        JLabel areYouSureLabel = new JLabel("You have selected to delete " + delPlayer.getFirstName() + " " + delPlayer.getLastName() + " from the Player Data.");
        areYouSureLabel.setAlignmentX(Component.CENTER_ALIGNMENT);


        promptBox.add(areYouSureLabel);



        Box buttonBox = Box.createHorizontalBox();

        JButton confirmButton = new JButton("Confirm");
        confirmButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        confirmButton.addActionListener(e ->{

            try{
                FileWriter resultsFileWriter = new FileWriter("PlayersData.txt"); // opening up the filewriter for the playersdata

                //stores the name of the player who is going to be removed from the player list
                String playerToRemoveName = playerList.get(positionInPlayerData).getFirstName() + " " + playerList.get(positionInPlayerData).getLastName();

                playerList.remove(positionInPlayerData); // will remove the player in the specific position in the player data

                for(int i = 0; i < playerList.size(); i++){
                    resultsFileWriter.write(playerList.get(i).StringDataStore());
                }

                resultsFileWriter.close();

                ViewRoster();

                frameToClose.dispatchEvent(new WindowEvent(frameToClose,WindowEvent.WINDOW_CLOSING));

                JFrame notificationFrame = new JFrame("Player Deleted");
                JPanel notificationPanel = new JPanel();

                notificationFrame.add(notificationPanel);

                Box componentBox = Box.createVerticalBox();

                JLabel notificationLabel = new JLabel(playerToRemoveName + " has successfully been removed from the Player Data!");
                notificationLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

                JButton acceptButton = new JButton("Accept");
                acceptButton.setAlignmentX(Component.CENTER_ALIGNMENT);

                acceptButton.addActionListener(eTwo ->{
                    notificationFrame.dispatchEvent(new WindowEvent(notificationFrame,WindowEvent.WINDOW_CLOSING));
                });

                componentBox.add(notificationLabel);
                componentBox.add(acceptButton);

                notificationPanel.add(componentBox);

                //notificationFrame.setSize(500,100);
                notificationFrame.pack();
                notificationFrame.setLocationRelativeTo(null);
                notificationFrame.setVisible(true);





            }
            catch(IOException error){



            }


            areYouSureFrame.dispatchEvent(new WindowEvent(areYouSureFrame,WindowEvent.WINDOW_CLOSING)); // closes the frame

        });

        JButton cancelButton = new JButton("Cancel");
        cancelButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        cancelButton.addActionListener(e ->{

            areYouSureFrame.dispatchEvent(new WindowEvent(areYouSureFrame,WindowEvent.WINDOW_CLOSING)); // closes the frame
        });


        buttonBox.add(confirmButton);
        buttonBox.add(cancelButton);



        promptBox.add(buttonBox);


        //areYouSureFrame.setSize(500,200);
        areYouSureFrame.pack();
        areYouSureFrame.setLocationRelativeTo(null);
        areYouSureFrame.setVisible(true);




    }

    // this function will take in an array of players and sort them by position and then by overall
    private Player[] sortPlayersByPosition(Player [] playerArray){

        LinkedList<Player> qbList = new LinkedList<>();
        LinkedList<Player> rbList = new LinkedList<>();
        LinkedList<Player> wrList = new LinkedList<>();
        LinkedList<Player> olList = new LinkedList<>();
        LinkedList<Player> dlList = new LinkedList<>();
        LinkedList<Player> lbList = new LinkedList<>();
        LinkedList<Player> dbList = new LinkedList<>();

        for(int i = 0; i < playerArray.length; i++){
            if(playerArray[i] == null){ //if a player at a particular index has been selected then it will go to the next index
                break;
            }

            switch (playerArray[i].getPosition()){
                case "QB":
                    qbList.add(playerArray[i]);
                    break;
                case "RB":
                    rbList.add(playerArray[i]);
                    break;
                case "WR":
                    wrList.add(playerArray[i]);
                    break;
                case "OL":
                    olList.add(playerArray[i]);
                    break;
                case "DL":
                    dlList.add(playerArray[i]);
                    break;
                case "LB":
                    lbList.add(playerArray[i]);
                    break;
                case "DB":
                    dbList.add(playerArray[i]);
                    break;
            }

        }

        // will sort each position group by overall
        qbList.sort(new Comparator<Player>() {
            @Override
            public int compare(Player o1, Player o2) {
                return o2.calcOverall() - o1.calcOverall();
            }
        });
        rbList.sort(new Comparator<Player>() {
            @Override
            public int compare(Player o1, Player o2) {
                return o2.calcOverall() - o1.calcOverall();
            }
        });
        wrList.sort(new Comparator<Player>() {
            @Override
            public int compare(Player o1, Player o2) {
                return o2.calcOverall() - o1.calcOverall();
            }
        });
        olList.sort(new Comparator<Player>() {
            @Override
            public int compare(Player o1, Player o2) {
                return o2.calcOverall() - o1.calcOverall();
            }
        });
        dlList.sort(new Comparator<Player>() {
            @Override
            public int compare(Player o1, Player o2) {
                return o2.calcOverall() - o1.calcOverall();
            }
        });
        lbList.sort(new Comparator<Player>() {
            @Override
            public int compare(Player o1, Player o2) {
                return o2.calcOverall() - o1.calcOverall();
            }
        });
        dbList.sort(new Comparator<Player>() {
            @Override
            public int compare(Player o1, Player o2) {
                return o2.calcOverall() - o1.calcOverall();
            }
        });

        int newArraySize = qbList.size() + rbList.size() + wrList.size() + olList.size() + dlList.size() + lbList.size() + dbList.size();

        Player[] returnArray = new Player[newArraySize]; //creates a new array for the array passed in

        playerList = new LinkedList<Player>(); // resets the linked list of players

        int returnArrayIndex = 0; //keeps track of the index in the return array

        // populating the new array with the players from each position group
        for(int i = 0; i < qbList.size();i++){
            playerList.add(qbList.get(i));
            returnArray[returnArrayIndex] = qbList.get(i);
            returnArrayIndex++; // increments the position in the
        }
        for(int i = 0; i < rbList.size();i++){
            playerList.add(rbList.get(i));
            returnArray[returnArrayIndex] = rbList.get(i);
            returnArrayIndex++;
        }
        for(int i = 0; i < wrList.size();i++){
            playerList.add(wrList.get(i));
            returnArray[returnArrayIndex] = wrList.get(i);
            returnArrayIndex++;
        }
        for(int i = 0; i < olList.size();i++){
            playerList.add(olList.get(i));
            returnArray[returnArrayIndex] = olList.get(i);
            returnArrayIndex++;
        }
        for(int i = 0; i < dlList.size();i++){
            playerList.add(dlList.get(i));
            returnArray[returnArrayIndex] = dlList.get(i);
            returnArrayIndex++;
        }
        for(int i = 0; i < lbList.size();i++){
            playerList.add(lbList.get(i));
            returnArray[returnArrayIndex] = lbList.get(i);
            returnArrayIndex++;
        }
        for(int i = 0; i < dbList.size();i++){
            playerList.add(dbList.get(i));
            returnArray[returnArrayIndex] = dbList.get(i);
            returnArrayIndex++;
        }


        return returnArray; // returns the array of sorted players
    }

    private Player[] sortPlayersByOverall(Player[] playerArray){

        playerList = new LinkedList<>(); // allocates a new linked list for the player list

        for(int i = 0; i < playerArray.length;i++){
            if(playerArray[i] == null){ //if a player has been drafted at a particular index it will break the loop
                break;
            }

            playerList.add(playerArray[i]);

        }


        Player[] returnArray = new Player[playerList.size()];

        playerList.sort(new Comparator<Player>() {
            @Override
            public int compare(Player o1, Player o2) {
                return o2.calcOverall() - o1.calcOverall();
            }
        });

        // will populate the returnArray with the sorted player list
        for(int i = 0; i < playerList.size(); i++){
            returnArray[i] = playerList.get(i);
        }


        return returnArray; // returns the array of sorted players
    }

    // this function will take in an array of players and return an array of players sorted by their Raw Score
    private Player[] sortPlayersByRawScore(Player[] playerArray){

        playerList = new LinkedList<>(); //this is used to refactor the size of the return list since the player array may have players that were selected and removed

        for(int i = 0; i < playerArray.length; i++){
            if(playerArray[i] == null){
                break;
            }

            playerList.add(playerArray[i]); //will add the player from the array to the linked list

        }

        // the playerList gets sorted in descending order
        playerList.sort(new Comparator<Player>() {
            @Override
            public int compare(Player o1, Player o2) {
                return o2.calcRawScore() - o1.calcRawScore();
            }
        });

        Player[] returnArray = new Player[playerList.size()]; // initializes a return array of the size of the linked list

        // populates the returnArray with the sorted player Linked List
        for(int i = 0; i < playerList.size(); i++){
            returnArray[i] = playerList.get(i);
        }

        return returnArray; // returns the array

    }



    // This function will store each person's team to a text file in order to be able to view it later and will return to the main menu
    public void EndDraft(){

        // try and catch block for opening and closing the files necessary to save the data of the draft
        try{
            FileWriter resultsFileWriter = new FileWriter("DraftResults.txt");

            resultsFileWriter.write(teamOnePlayers.getTeamName() + "\n");

            //for every player on the team it will write it to the file
            for(int i = 0; i < teamOnePlayers.getTeamPlayers().length;i++){
                resultsFileWriter.write(teamOnePlayers.getTeamPlayers()[i].StringDataStore());

            }

            // doing the same as team one but for team two
            resultsFileWriter.write(teamTwoPlayers.getTeamName() + "\n");

            //for every player on the team it will write it to the file
            for(int i = 0; i < teamTwoPlayers.getTeamPlayers().length;i++){
                resultsFileWriter.write(teamTwoPlayers.getTeamPlayers()[i].StringDataStore());
            }


            resultsFileWriter.close();
        }
        catch(IOException e){
            System.out.println("Couldn't find file");
        }

        frame1.dispatchEvent(new WindowEvent(frame1,WindowEvent.WINDOW_CLOSING));
        mainFrame.setState(JFrame.NORMAL);
        //mainFrame.dispatchEvent(new WindowEvent(mainFrame,Frame.NORMAL));

    }

    private void formatPlayerDisplay(int position, Player [] playerList){
        Player currentPlayer = playerList[position]; //sets the current player selected from the display list to the current index in the playersAvailable array

        try{
            BufferedImage playerImage = ImageIO.read(new File(currentPlayer.getFirstName().toUpperCase() + currentPlayer.getLastName().toUpperCase() + ".jpg"));

            playerImageLabel.setIcon(new ImageIcon(playerImage.getScaledInstance(300,350,0)));
            playerImageLabel.setText(null);

        }
        catch (IOException error){
            //an attempt to still have an image for a player who may not have a file in the directory
            try{
                BufferedImage backUpPlayerImage = ImageIO.read(new File("NOPICTUREPLAYER.jpg"));
                playerImageLabel.setIcon(new ImageIcon(backUpPlayerImage.getScaledInstance(300,350,0)));
                playerImageLabel.setText(null);

            }
            catch(IOException errorTwo){ //will catch any IO errors and just display no image found
                playerImageLabel.setIcon(null);
                playerImageLabel.setText("*No Image Found*");
            }

        }


        playerName.setText(currentPlayer.getFirstName() + " " + currentPlayer.getLastName());
        playerName.setFont(new Font("Script",Font.BOLD,30));
        //FIND A WAY TO CENTER TEXT WITHIN BOX^

        //combined raw label with overall label for better visual
        //rawLabel.setText("RAW Score: " + currentPlayer.calcRawScore());


        playerPosition.setText(currentPlayer.getPosition());
        playerPosition.setFont(new Font("Script",Font.PLAIN,20));
        overallLabel.setText("Overall: " + currentPlayer.calcOverall() + "  RAW Score: " + currentPlayer.calcRawScore());
        overallLabel.setFont(new Font("Script",Font.PLAIN,15));

        passRating.setText("Passing (" + currentPlayer.getPassing() + ")");
        passRating.setFont(new Font("Script",Font.PLAIN,20));
        passRating.setForeground(Color.white);

        passGraph.setText(calcStatGraph(currentPlayer.getPassing()));
        passGraph.setFont(new Font("Script",Font.BOLD,20));
        passGraph.setForeground(getStatColor(currentPlayer.getPassing()));


        speedRating.setText("Speed (" + currentPlayer.getSpeed() + ")");
        speedRating.setFont(new Font("Script", Font.PLAIN,20));
        speedRating.setForeground(Color.white);

        speedGraph.setText(calcStatGraph(currentPlayer.getSpeed()));
        speedGraph.setFont(new Font("Script",Font.BOLD,20));
        speedGraph.setForeground(getStatColor(currentPlayer.getSpeed()));


        blockRating.setText("Blocking (" + currentPlayer.getBlocking() + ")");
        blockRating.setFont(new Font("Script",Font.PLAIN,20));
        blockRating.setForeground(Color.white);

        blockGraph.setText(calcStatGraph(currentPlayer.getBlocking()));
        blockGraph.setFont(new Font("Script",Font.BOLD,20));
        blockGraph.setForeground(getStatColor(currentPlayer.getBlocking()));


        agilityRating.setText("Agility (" + currentPlayer.getAgility() + ")");
        agilityRating.setFont(new Font("Script",Font.PLAIN,20));
        agilityRating.setForeground(Color.white);

        agilityGraph.setText(calcStatGraph(currentPlayer.getAgility()));
        agilityGraph.setFont(new Font("Script",Font.BOLD,20));
        agilityGraph.setForeground(getStatColor(currentPlayer.getAgility()));


        catchRating.setText("Catching (" + currentPlayer.getCatching() + ")");
        catchRating.setFont(new Font("Script",Font.PLAIN,20));
        catchRating.setForeground(Color.white);

        catchGraph.setText(calcStatGraph(currentPlayer.getCatching()));
        catchGraph.setFont(new Font("Script",Font.BOLD,20));
        catchGraph.setForeground(getStatColor(currentPlayer.getCatching()));


        runPowRating.setText("Run Power (" + currentPlayer.getRunPower() + ")");
        runPowRating.setFont(new Font("Script",Font.PLAIN,20));
        runPowRating.setForeground(Color.white);

        runPowGraph.setText(calcStatGraph(currentPlayer.getRunPower()));
        runPowGraph.setFont(new Font("Script",Font.BOLD,20));
        runPowGraph.setForeground(getStatColor(currentPlayer.getRunPower()));


        carryRating.setText("Carrying (" + currentPlayer.getCarrying() + ")");
        carryRating.setFont(new Font("Script",Font.PLAIN,20));
        carryRating.setForeground(Color.white);

        carryGraph.setText(calcStatGraph(currentPlayer.getCarrying()));
        carryGraph.setFont(new Font("Script",Font.BOLD,20));
        carryGraph.setForeground(getStatColor(currentPlayer.getCarrying()));


        tackleRating.setText("Tackling (" + currentPlayer.getTackling() + ")");
        tackleRating.setFont(new Font("Script",Font.PLAIN,20));
        tackleRating.setForeground(Color.white);

        tackleGraph.setText(calcStatGraph(currentPlayer.getTackling()));
        tackleGraph.setFont(new Font("Script",Font.BOLD,20));
        tackleGraph.setForeground(getStatColor(currentPlayer.getTackling()));


        coverageRating.setText("Coverage (" + currentPlayer.getCoverage() + ")");
        coverageRating.setFont(new Font("Script",Font.PLAIN,20));
        coverageRating.setForeground(Color.white);

        coverageGraph.setText(calcStatGraph(currentPlayer.getCoverage()));
        coverageGraph.setFont(new Font("Script",Font.BOLD,20));
        coverageGraph.setForeground(getStatColor(currentPlayer.getCoverage()));


        dmovesRating.setText("D-Moves (" + currentPlayer.getDMoves() + ")");
        dmovesRating.setFont(new Font("Script",Font.PLAIN,20));
        dmovesRating.setForeground(Color.white);

        dmovesGraph.setText(calcStatGraph(currentPlayer.getDMoves()));
        dmovesGraph.setFont(new Font("Script",Font.BOLD,20));
        dmovesGraph.setForeground(getStatColor(currentPlayer.getDMoves()));


        spaceLabel.setText("                                        ");
        spaceLabel.setFont(new Font("Script",Font.BOLD,20));





    }

    // function that takes in the given rating for the current player stat being analyzed for display. This method will graph out how ever many vertical
    // bars belong to the given attribute value
    private String calcStatGraph(int rating){
        String statGraph = "";
        for(int i = 0; i < rating; i++){
            statGraph = statGraph + "| ";
        }

        return statGraph;
    }

    // Function in order to determine which color will get displayed for the current stat
    private Color getStatColor(int rating){
        if(rating == 20){
            return Color.cyan;
        }
        else if(rating >= 14 && rating <= 19){
            return Color.green;
        }
        else if(rating >= 10 && rating <= 13){
            return Color.yellow;
        }
        else if(rating >= 4 && rating <= 9){
            return Color.orange;
        }
        else{
            return Color.red;
        }
    }

}
