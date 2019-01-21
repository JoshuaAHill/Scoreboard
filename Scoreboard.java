import java.io.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
 * This class contains a Scoreboard. The Scoreboard displays ten rows each with a position, name and score. Lower scores are better. 
 The names and scores of the current highscores are loaded in through files of type '.txt'. The scoreboard has the functionality to 
 add a new score and name, and if it make it onto the scoreboard it will be written back to the files. If not it will be discarded.
 * If the files given to the scoreboard don't contain ten lines, then new lines are created and stored back to the files. If one of
 the files is empty then the top score is automatically assigned to 100,000 and the name "none", with each result following having 
 the same name but a score that increments by 1.
 */
public class Scoreboard implements ActionListener {

    //data
    private int length = 11;
    private String[] scores = new String[length];               //A String array to store the scores in read from the file
    private String[] tempscores = new String[length];           //A String array used to store tempoaraly the values of the 'old' scores array whilst the scores array is updated to the 'new' version
    private String[] names = new String[length];                //A String array to store the names in read from the file  -- 11 long becasue needs to hold all 9 + a 10th that will be a user entry
    private String[] tempnames = new String[length];            //A String array used to store tempoaraly the values of the 'old' names array whilst the names array is updated to the 'new' version
    private JFrame frame = new JFrame("Scoreboard");            //A frame in which the scoreboard is held within
    private JPanel panel = new JPanel();                        //A panel on which the content will be displayed
    private GridLayout layout = new GridLayout(13, 3);           //A grid layout that wil structure the content
    private JLabel[] labelArray = new JLabel[20];               //An array of JLabels - 10 for holding the scores in the scoreboard and 10 for their corresponding names
    private JLabel enterNameLabel = new JLabel("Enter your name:");  //A JLabel that has the text "Enter your name:" used to prompt the user
    private JTextField nameInput = new JTextField();            //A JTextField that the user enters their name into
    private int localScore = 0;                                 //A variable used to hold locally the score being added to the scoreboard
    private String localScoresfile;                             //A local variable to hold the name of the scores file being passed into the scoreboard
    private String localNamesfile;                              //A local variable to hold the name of the names file being passed into the scoreboard
    private JLabel positionTitle = new JLabel("Position");      //A JLabel that shows the text 'Position' that is used as a title for the positions column on the scoreboard
    private JLabel titleLabelNames = new JLabel();              //A JLabel that is used as a title for the names column on the scoreboard - depending on the user input
    private JLabel titleLabelScores = new JLabel();             //A JLabel that is used as a title for the scores column on the scoreboard - depending on the user unput
    private JButton closeButton = new JButton("Close");         //A JButton that shows the text 'Close' and is used to prompt the user to close the scoreboard when needed (as an alternative to the red 'X')
    private JTextArea information = new JTextArea();            //A JTextArea that is used to show information to the user
    private JTextArea information2 = new JTextArea();           //A JTextArea that is used to show information to the user
    private int highscorePosition;                              //The position in the Scoreboard the new score has been added to.


    //methods

    /**
     * Constructor - reads files and opens an instance of the scoreboard.
     *
     * @param scoresfile - a String title for the file name in which the Scoreboard scores are stored.
     * @param namesfile  - a String title for the file name in which the Scoreboard names are stored.
     * @param title      - the name the created scoreboard will use in its column headings - (title) names and (title) scores.
     */
    public Scoreboard(String scoresfile, String namesfile, String title) {
        localScoresfile = scoresfile;                                                       //set the paramaters to local variables
        localNamesfile = namesfile;

        File scoreboardscores = new File(scoresfile);                                       //create two new file variables, one for the scores and one for the names to use when reading the scores and names into the scoreboard
        File scoreboardnames = new File(namesfile);

        frame.setContentPane(panel);                                                        //configure the frame and panel 
        frame.setSize(430, 530);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        panel.setLayout(layout);

        panel.add(positionTitle);                                                           //add the column titles to the panel layout
        titleLabelNames.setText(title + " names");
        panel.add(titleLabelNames);
        titleLabelScores.setText(title + " scores");
        panel.add(titleLabelScores);

        try {                                                                               //reads lines from the files and sets them into an array. If there is no value in the file, the array values are set to score: previous + 1 (can always be beaten) and name: none - so that if the files dont have enough lines of data in, the scoreboard will still work. These values are written back to the files, so that they have the right amount of data in them for next time.
            BufferedReader nameReader = new BufferedReader(new FileReader(namesfile));
            BufferedReader scoreReader = new BufferedReader(new FileReader(scoresfile));
            for (int i = 0; i < 10; i++) {
                names[i] = nameReader.readLine();
                scores[i] = scoreReader.readLine();
                if (names[i] == null || scores[i] == null) {
                    if (i == 0) {                                                             //Special case - if either of the files is empty then a default value is created - the top score is set to 100,000 with name 'none' and each lower positioned score is incremented by one with the same name.
                        scores[0] = "100000";
                        names[0] = "none";
                    } else {                                                                   //fill the scores and names file with values if the files dont provide them.
                        scores[i] = String.valueOf(Integer.valueOf(scores[i - 1]) + 1);
                        names[i] = "none";
                    }
                }
            }

            for (int i = 0; i < 10; i++) {                                                  //add labels to the scoreboard with the text just entered into the names and scores array that was taken from the input files
                JLabel positionLabel = new JLabel((i + 1) + ") ");
                panel.add(positionLabel);
                JLabel name = new JLabel(names[i]);
                JLabel score = new JLabel(scores[i]);
                panel.add(name);
                labelArray[i] = name;
                panel.add(score);
                labelArray[10 + i] = score;
            }

            panel.add(enterNameLabel);                                                      //add the 'enter name', input box and close button to the layout.
            panel.add(nameInput);
            panel.add(closeButton);

            closeButton.setEnabled(false);                                                  //configure the information text areas.
            information.setLineWrap(true);
            information.setWrapStyleWord(true);
            information.setEditable(false);
            information2.setLineWrap(true);
            information2.setWrapStyleWord(true);
            information2.setEditable(false);

            panel.add(information);                                                         //add the information text areas to the layout.
            panel.add(information2);

        } catch (IOException e) {
            System.out.println("error loading scoreboard");
        }

        frame.setResizable(false);                                                          //make the frame non-resisable and visable (by default created invisible).
        frame.setVisible(true);
    }

    /**
     * addScore method, stores information given to the scoreboard to static local variables to be used in the 'action performed' method later.
     *
     * @param moves      - the number of moves the player took to complete the puzzle
     * @param scoresfile - a String title for the file name in which the Scoreboard scores are stored.
     * @param namesfile  - a String title for the file name in which the Scoreboard names are stored.
     */
    public void addScore(int score, String scoresfile, String namesfile) {                  //adds the actionlistener to the input text field, this emans that you cannot add a score untill the addScore method has been called.
        nameInput.addActionListener(this);
        information.setText("Well done! - Completed.");                                     //set the information boxes to inform the user 
        information2.setText("Enter your name and press enter to submit");
        localScore = score;                                                                 //set the passed parameters to local variables to be used later
        localScoresfile = scoresfile;
        localNamesfile = namesfile;
    }

    /**
     * actionPerformed Method - is run when an action listener interupt is caused. The method uses the stored 'localScore' variable and compares it the the scoreboard loaded in when constructed, adding in the localScore 'score' to the file 'LocalScoresFile' if it
     * beats (is lower than) one of the current scores. If this is the case all of the scores lower than the new entry will be moved down one place unless they are the bottom in which case the
     * score will not be written back to the file hence being removed. If the score is too high to be on the scoreboard then it will not be added.
     *
     * @param a - the action event caused when an action listener signals an interupt.
     * @return - returns from adding a score if the nameInput field has been left blank.
     **/
    public void actionPerformed(ActionEvent a) {
        if (a.getSource() == nameInput) {

            if (nameInput.getText().length() < 1) {                             //checks if the input field is blank - if so, the function is returned if the nameInput field has been left blank
                information.setText("Please enter a name to add your score");
                information2.setText("");
                return;
            }
            File scoreboardscores = new File(localScoresfile);                   //create two new file variables, one for the scores and one for the names to use when reading the scores and names into the scoreboard
            File scoreboardnames = new File(localNamesfile);
            for (int i = 0; i < 10; i++) {                                       //traverses through the array of scores which is created when initilised. If at anytime the value of localScore is lower than one of these scores, the process off adding a score is started then the loop is broken. If the loop finishes before being broken then the score was too large and hence is not added.

                if (localScore < Integer.valueOf(scores[i])) {                   //checks to see if Local moves is smaller than the current score in position i
                    highscorePosition = i + 1;
                    for (int j = i; j < 10; j++) {                               //if so, move all the scores and names below i from the main arrays to temporary arrays (so they dont get overwritten) at one place down from their position in the main array (to make room for the new entry)
                        tempscores[j + 1] = scores[j];
                        tempnames[j + 1] = names[j];
                    }

                    scores[i] = String.valueOf(localScore);                      // set the localScore score and coresponding name that had been input into the 'input' text field into their respective positions, overwriting the old score and name in the local arrays.
                    names[i] = nameInput.getText();

                    for (int k = i + 1; k < 10 + 1; k++) {                       // re-write the moved scores from the temporary array back to the main score and names array.
                        scores[k] = tempscores[k];
                        names[k] = tempnames[k];
                    }

                    try {                                                        // update the score input file with the new scores
                        FileWriter writer = new FileWriter(localScoresfile);
                        PrintWriter printwriter = new PrintWriter(writer);
                        for (int l = 0; l < 10; l++)
                            printwriter.println(scores[l]);
                        printwriter.close();
                    } catch (IOException e) {
                        System.out.println("error writing values scores to file");
                    }
                    try {                                                          // update the name input file with the new names
                        FileWriter writer = new FileWriter(localNamesfile);
                        PrintWriter printwriter = new PrintWriter(writer);
                        for (int l = 0; l < 10; l++)
                            printwriter.println(names[l]);
                        printwriter.close();
                    } catch (IOException e) {
                        System.out.println("error writing values to names file");
                    }

                    for (i = 0; i < 10; i++) {                                               // print to console the new scoreboard
                        System.out.print(names[i] + " ");
                        System.out.print(scores[i] + "\n");
                    }

                    for (i = 0; i < 10; i++) {                                               //Update the labels on-screen with the new scores and names
                        labelArray[i].setText(names[i]);

                    }
                    int j = 0;
                    for (i = 10; i < 20; i++) {
                        labelArray[i].setText(scores[j]);
                        j++;
                    }
                    information.setText("Well done you achieved a new highscore!");          //update the information boxes to inform the user
                    information2.setText("You achieved position " + highscorePosition + ".");
                    break;                                                                   //stops the loop as soon as a score that is lower than the current 'moves' array score is found - only sorts and inputs once.

                } // end if smaller than
                information.setText("Unlucky you did not achieve a new highscore this time");//update the information boxes to inform the user
                information2.setText("");
            } // end i loop
            enterNameLabel.setText("");                                                      //update the information boxes to inform the user
            information2.setText(information2.getText() + "\n Press Button to close");

            nameInput.setEnabled(false);                                                     //disable the nameINput field, so the user cannot enter their name more than once
            closeButton.addActionListener(this);                                             //add an action listener to the close button and make it visible, so now the user can use the close button to close the scoreboard.
            closeButton.setEnabled(true);
        } // end of if (source == nameInput)

        if (a.getSource() == closeButton) {                                                  //if the close button is pressed, dispose of the scoreboard.
            dispose();
        }


    } // end action performed

    /**
     * dispose method - this method can be used to close the scoreboard instance.
     */
    public void dispose() {                                                                 //can be called by any class to close the scoreboard instance.
        frame.dispose();
    }
}
