package elec;

import javafx.animation.AnimationTimer;
import javafx.event.ActionEvent;
import javafx.scene.Scene;
import javafx.scene.chart.AreaChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.Image;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * Controls every aspect of the UI, the timers, the prices, and all the buttons
 *
 * @author Kyle Petkovic
 * @version 1.0.0
 */

public class Controller{
    
    //Shows how much energy the player made
    public Label energyMeter;

    //Progress bars
    public ProgressBar zeroOneProg;
    public ProgressBar oneOneProg;
    public ProgressBar twoOneProg;
    public ProgressBar threeOneProg;
    public ProgressBar zeroFourProg;
    public ProgressBar oneFourProg;
    public ProgressBar twoFourProg;
    public ProgressBar threeFourProg;

    //Buttons to buy
    public Button zeroOneBut;
    public Button oneOneBut;
    public Button twoOneBut;
    public Button threeOneBut;
    public Button zeroFourBut;
    public Button oneFourBut;
    public Button twoFourBut;
    public Button threeFourBut;

    //Labels for the progress bars
    public Label zeroOneLab;
    public Label oneOneLab;
    public Label twoOneLab;
    public Label threeOneLab;
    public Label zeroFourLab;
    public Label oneFourLab;
    public Label twoFourLab;
    public Label threeFourLab;

    //Buttons to upgrade
    public Button zeroOneUpg;
    public Button oneOneUpg;
    public Button twoOneUpg;
    public Button threeOneUpg;
    public Button zeroFourUpg;
    public Button oneFourUpg;
    public Button twoFourUpg;
    public Button threeFourUpg;

    //Assistant Buttons
    public Button zeroOneAs;
    public Button oneOneAs;
    public Button twoOneAs;
    public Button threeOneAs;
    public Button zeroFourAs;
    public Button oneFourAs;
    public Button twoFourAs;
    public Button threeFourAs;

    //Booleans to check if assistant was bought
    private boolean zeroOneAsBought = false;
    private boolean oneOneAsBought;
    private boolean twoOneAsBought;
    private boolean threeOneAsBought;
    private boolean zeroFourAsBought;
    private boolean oneFourAsBought;
    private boolean twoFourAsBought;
    private boolean threeFourAsBought;

    //Label to show price
    public Label upgradeClickPrice;

    //Progress bar progress vars
    private double zeroOneProgNum;

    //Upgrade click button
    public Button upgradeClick;

    //Button for help
    public Button helpBtn;

    //Electricity production chart
    public AreaChart elecProd;

    //Clear data in graph
    public Button clearGraphData;

    //Label to show mAH / sec
    public Label mAHPerSec;

    //Keeps track of amount of energy created
    private long mAH = 0;

    //Keeps track of how much to add per click
    private long clickValue = 1;

    // Passive and click objects
    private IncreaseClick inClick = new IncreaseClick((long) 100);


    /**
     * Listener that sets params for the graph, and handles the energy addition
     *
     */
    public void energyClick(ActionEvent action) {
        mAH+= clickValue;
        energyMeter.setText(mAH + " mAH");

        //Remove categories
        elecProd.setCreateSymbols(false);

        elecProd.setHorizontalGridLinesVisible(false);
        elecProd.setVerticalGridLinesVisible(false);



        //Start updating of chart
        elecChartTim.start();
    }


    //Data for the chart
    XYChart.Series elecData = new XYChart.Series();


    /**
     * Timer for electricity chart, adds data and computes mAH per second
     */
    private AnimationTimer elecChartTim = new AnimationTimer() {
        //Data for chart


        boolean firstRun = true;

        int frame = 1;
        long time = -1;

        long average = 0;
        int arrIndex = 0;

        long mAHBefore = mAH;
        long mAHGraph = 0;


        @Override
        public void handle(long now) {




            if(frame % 60 == 0){

                if(mAH != mAHBefore){
                    mAHGraph += mAH - mAHBefore;
                }

                mAHBefore = mAH;

                time++;
                arrIndex++;

                if(mAHGraph < 0)
                    mAHGraph = 0;

                elecData.getData().add(new XYChart.Data(time + "", mAHGraph));



                mAHPerSec.setText(mAHGraph + " mAH/s");

                mAHGraph = 0;

                frame = 1;
            }


            if(firstRun) {
                elecData.setName("mAH");

                elecProd.getData().add(elecData);
                firstRun = false;
            }

            frame++;
        }
    };

    /**
     * Resets the data in the graph when the clear graph data button is clicked
     *
     */
    public void clearGraphDataClick (ActionEvent actionEvent){
        elecData.getData().clear();
    }



    private double zeroOneDelay = 0.001;


    /**
     * Timer for the relevant set of upgrade, buy, and assistant button
     * Keeps track of the upgrade, sets the progressbar progress, and
     * subtracts the relevant amount of mAH from the users reserves.
     */
    private  AnimationTimer zeroOneTim = new AnimationTimer(){

        double zeroOneProgDelay;
        double zeroOneProgNum = 0;
        boolean zeroOneSubtractMAH = true;

        public void handle(long now){

            if(zeroOneSubtractMAH && !zeroOneAsBought){


                updateMAH(-5);


                zeroOneSubtractMAH = false;
            }

            if(zeroOneProgDelay % 5 == 0)
                zeroOneProgNum += zeroOneDelay;

            zeroOneProgDelay++;

            if(zeroOneProgNum >= 1.000)
                zeroOneProg.setProgress(1);
            else
                zeroOneProg.setProgress(zeroOneProgNum);

            if(zeroOneProg.getProgress() >= 1.0) {

                //Here so that the progress bar doesn't flash
                if(zeroOneProgNum >= 1.000 && zeroOneAsBought)
                    zeroOneProg.setProgress(1);
                else
                    zeroOneProg.setProgress(0);

                //Done otherwise the mAH count will flicker
                if(zeroOneAsBought)
                    updateMAH(10);
                else
                    updateMAH(15);

                zeroOneProgDelay = 0;
                zeroOneProgNum = 0;
                zeroOneSubtractMAH = true;

                if(!zeroOneAsBought)
                    zeroOneTim.stop();
            }
        }

    };

   private double oneOneDelay = 0.001;

    /**
     * Timer for the relevant set of upgrade, buy, and assistant button
     * Keeps track of the upgrade, sets the progressbar progress, and
     * subtracts the relevant amount of mAH from the users reserves.
     */
    private  AnimationTimer oneOneTim = new AnimationTimer(){

        double oneOneProgDelay;
        double oneOneProgNum = 0;
        boolean oneOneSubtractMAH = true;

        public void handle(long now){

            if(oneOneSubtractMAH && !oneOneAsBought){


                updateMAH(-10000);


                oneOneSubtractMAH = false;
            }

            if(oneOneProgDelay % 5 == 0)
                oneOneProgNum += oneOneDelay;

            oneOneProgDelay++;

            if(oneOneProgNum >= 1.000)
                oneOneProg.setProgress(1);
            else
                oneOneProg.setProgress(oneOneProgNum);

            if(oneOneProg.getProgress() >= 1.0) {

                //Here so that the progress bar doesn't flash
                if(oneOneProgNum >= 1.000 && oneOneAsBought)
                    oneOneProg.setProgress(1);
                else
                    oneOneProg.setProgress(0);

                //Done otherwise the mAH count will flicker
                if(oneOneAsBought)
                    updateMAH(5000);
                else
                    updateMAH(15000);

                oneOneProgDelay = 0;
                oneOneProgNum = 0;
                oneOneSubtractMAH = true;

                if(!oneOneAsBought)
                    oneOneTim.stop();
            }
        }

    };

    private double twoOneDelay = 0.001;

    /**
     * Timer for the relevant set of upgrade, buy, and assistant button
     * Keeps track of the upgrade, sets the progressbar progress, and
     * subtracts the relevant amount of mAH from the users reserves.
     */
    private  AnimationTimer twoOneTim = new AnimationTimer(){

        double twoOneProgDelay;
        double twoOneProgNum = 0;
        boolean twoOneSubtractMAH = true;

        public void handle(long now){

            if(twoOneSubtractMAH && !twoOneAsBought){


                updateMAH(-650000);


                twoOneSubtractMAH = false;
            }

            if(twoOneProgDelay % 5 == 0)
                twoOneProgNum += twoOneDelay;

            twoOneProgDelay++;

            if(twoOneProgNum >= 1.000)
                twoOneProg.setProgress(1);
            else
                twoOneProg.setProgress(twoOneProgNum);

            if(twoOneProg.getProgress() >= 1.0) {

                //Here so that the progress bar doesn't flash
                if(twoOneProgNum >= 1.000 && twoOneAsBought)
                    twoOneProg.setProgress(1);
                else
                    twoOneProg.setProgress(0);

                //Done otherwise the mAH count will flicker
                if(twoOneAsBought)
                    updateMAH(850000);
                else
                    updateMAH(1500000);

                twoOneProgDelay = 0;
                twoOneProgNum = 0;
                twoOneSubtractMAH = true;

                if(!twoOneAsBought)
                    twoOneTim.stop();
            }
        }

    };

    private double threeOneDelay = 0.001;

    /**
     * Timer for the relevant set of upgrade, buy, and assistant button
     * Keeps track of the upgrade, sets the progressbar progress, and
     * subtracts the relevant amount of mAH from the users reserves.
     */
    private  AnimationTimer threeOneTim = new AnimationTimer(){

        double threeOneProgDelay;
        double threeOneProgNum = 0;
        boolean threeOneSubtractMAH = true;

        public void handle(long now){

            if(threeOneSubtractMAH && !threeOneAsBought){


                updateMAH(-100000000);


                threeOneSubtractMAH = false;
            }

            if(threeOneProgDelay % 5 == 0)
                threeOneProgNum += threeOneDelay;

            threeOneProgDelay++;

            if(threeOneProgNum >= 1.000)
                threeOneProg.setProgress(1);
            else
                threeOneProg.setProgress(threeOneProgNum);

            if(threeOneProg.getProgress() >= 1.0) {

                //Here so that the progress bar doesn't flash
                if(threeOneProgNum >= 1.000 && threeOneAsBought)
                    threeOneProg.setProgress(1);
                else
                    threeOneProg.setProgress(0);

                //Done otherwise the mAH count will flicker
                if(threeOneAsBought)
                    updateMAH(150000000);
                else
                    updateMAH(250000000);

                threeOneProgDelay = 0;
                threeOneProgNum = 0;
                threeOneSubtractMAH = true;

                if(!threeOneAsBought)
                    threeOneTim.stop();
            }
        }

    };

    private double zeroFourDelay = 0.001;

    /**
     * Timer for the relevant set of upgrade, buy, and assistant button
     * Keeps track of the upgrade, sets the progressbar progress, and
     * subtracts the relevant amount of mAH from the users reserves.
     */
    private  AnimationTimer zeroFourTim = new AnimationTimer(){

        double zeroFourProgDelay;
        double zeroFourProgNum = 0;
        boolean zeroFourSubtractMAH = true;

        public void handle(long now){

            if(zeroFourSubtractMAH && !zeroFourAsBought){


                updateMAH(-70);


                zeroFourSubtractMAH = false;
            }

            if(zeroFourProgDelay % 5 == 0)
                zeroFourProgNum += zeroFourDelay;

            zeroFourProgDelay++;

            if(zeroFourProgNum >= 1.000)
                zeroFourProg.setProgress(1);
            else
                zeroFourProg.setProgress(zeroFourProgNum);

            if(zeroFourProg.getProgress() >= 1.0) {

                //Here so that the progress bar doesn't flash
                if(zeroFourProgNum >= 1.000 && zeroFourAsBought)
                    zeroFourProg.setProgress(1);
                else
                    zeroFourProg.setProgress(0);

                //Done otherwise the mAH count will flicker
                if(zeroFourAsBought)
                    updateMAH(80);
                else
                    updateMAH(150);

                zeroFourProgDelay = 0;
                zeroFourProgNum = 0;
                zeroFourSubtractMAH = true;

                if(!zeroFourAsBought)
                    zeroFourTim.stop();
            }
        }

    };
    
    private double oneFourDelay = 0.001;

    /**
     * Timer for the relevant set of upgrade, buy, and assistant button
     * Keeps track of the upgrade, sets the progressbar progress, and
     * subtracts the relevant amount of mAH from the users reserves.
     */
    private  AnimationTimer oneFourTim = new AnimationTimer(){

        double oneFourProgDelay;
        double oneFourProgNum = 0;
        boolean oneFourSubtractMAH = true;

        public void handle(long now){

            if(oneFourSubtractMAH && !oneFourAsBought){


                updateMAH(-150000);


                oneFourSubtractMAH = false;
            }

            if(oneFourProgDelay % 5 == 0)
                oneFourProgNum += oneFourDelay;

            oneFourProgDelay++;

            if(oneFourProgNum >= 1.000)
                oneFourProg.setProgress(1);
            else
                oneFourProg.setProgress(oneFourProgNum);

            if(oneFourProg.getProgress() >= 1.0) {

                //Here so that the progress bar doesn't flash
                if(oneFourProgNum >= 1.000 && oneFourAsBought)
                    oneFourProg.setProgress(1);
                else
                    oneFourProg.setProgress(0);

                //Done otherwise the mAH count will flicker
                if(oneFourAsBought)
                    updateMAH(127000);
                else
                    updateMAH(270000);

                oneFourProgDelay = 0;
                oneFourProgNum = 0;
                oneFourSubtractMAH = true;

                if(!oneFourAsBought)
                    oneFourTim.stop();
            }
        }

    };

    private double twoFourDelay = 0.001;

    /**
     * Timer for the relevant set of upgrade, buy, and assistant button
     * Keeps track of the upgrade, sets the progressbar progress, and
     * subtracts the relevant amount of mAH from the users reserves.
     */
    private  AnimationTimer twoFourTim = new AnimationTimer(){

        double twoFourProgDelay;
        double twoFourProgNum = 0;
        boolean twoFourSubtractMAH = true;

        public void handle(long now){

            if(twoFourSubtractMAH && !twoFourAsBought){


                updateMAH(-1000000);


                twoFourSubtractMAH = false;
            }

            if(twoFourProgDelay % 5 == 0)
                twoFourProgNum += twoFourDelay;

            twoFourProgDelay++;

            if(twoFourProgNum >= 1.000)
                twoFourProg.setProgress(1);
            else
                twoFourProg.setProgress(twoFourProgNum);

            if(twoFourProg.getProgress() >= 1.0) {

                //Here so that the progress bar doesn't flash
                if(twoFourProgNum >= 1.000 && twoFourAsBought)
                    twoFourProg.setProgress(1);
                else
                    twoFourProg.setProgress(0);

                //Done otherwise the mAH count will flicker
                if(twoFourAsBought)
                    updateMAH(5000000);
                else
                    updateMAH(6000000);

                twoFourProgDelay = 0;
                twoFourProgNum = 0;
                twoFourSubtractMAH = true;

                if(!twoFourAsBought)
                    twoFourTim.stop();
            }
        }

    };
    
    private double threeFourDelay = 0.001;

    /**
     * Timer for the relevant set of upgrade, buy, and assistant button
     * Keeps track of the upgrade, sets the progressbar progress, and
     * subtracts the relevant amount of mAH from the users reserves.
     */
    private  AnimationTimer threeFourTim = new AnimationTimer(){

        double threeFourProgDelay;
        double threeFourProgNum = 0;
        boolean threeFourSubtractMAH = true;

        public void handle(long now){

            if(threeFourSubtractMAH && !threeFourAsBought){


                updateMAH(-500000000);


                threeFourSubtractMAH = false;
            }

            if(threeFourProgDelay % 5 == 0)
                threeFourProgNum += threeFourDelay;

            threeFourProgDelay++;

            if(threeFourProgNum >= 1.000)
                threeFourProg.setProgress(1);
            else
                threeFourProg.setProgress(threeFourProgNum);

            if(threeFourProg.getProgress() >= 1.0) {

                //Here so that the progress bar doesn't flash
                if(threeFourProgNum >= 1.000 && threeFourAsBought)
                    threeFourProg.setProgress(1);
                else
                    threeFourProg.setProgress(0);

                //Done otherwise the mAH count will flicker
                if(threeFourAsBought)
                    updateMAH(250000000);
                else
                    updateMAH(750000000);

                threeFourProgDelay = 0;
                threeFourProgNum = 0;
                threeFourSubtractMAH = true;

                if(!threeFourAsBought)
                    threeFourTim.stop();
            }
        }

    };


    /**
     * Keeps track of how much the click has been upgraded, updates the label for the price
     * and increments the clickValue for every upgrade
     *
     * @param action In this case, the object that initiated the listener
     */
    public void shopUpgClick(ActionEvent action){
       if(action.getSource().equals(upgradeClick)){
           if(inClick.buyUpg(mAH)){

               if(inClick.getPrice() <= mAH) {
                   clickValue++;

                   updateMAH(inClick.getPrice() * -1);
                   inClick.setPrice(inClick.getPrice() * 3);

                   //Set value of price label
                   upgradeClickPrice.setText(inClick.getPrice() + " mAH");
               }

           }
       }
   }

    /**
     * Listener to show help screen, when the help button is clicked it creates the stage and
     * displays it to the user along with the information on how to play.
     *
     */
    public void showHelpScreen (ActionEvent action){
        final Stage helpScreen = new Stage();

        helpScreen.initModality(Modality.APPLICATION_MODAL);
        
        //Set icon
        helpScreen.getIcons().add( new Image(Main.class.getResourceAsStream( "helpScreenIcon.png" )));
        helpScreen.setTitle(" How to play: ");
        
        VBox dialogVbox = new VBox(20);
        dialogVbox.getChildren().add(new Text(
                                " Everything is bought with energy, which is generated by clicking the energy icon on the left side of the screen. \n" +
                                " The amount of energy generated is displayed at the bottom left-hand corner of the screen. Energy can also be \n" +
                                " generated by buying the items in the center of the screen, when their bar fills up a lot of energy will be generated.\n" +
                                " For the same cost, one can also upgrade the item to lower the time it takes for the bar to fill up. On the\n" +
                                " upper-right side of the screen one can buy an upgrade to their click, which adds one energy every time you click\n" +
                                " and so on. Lastly, underneath that, are the assistants which automatically buy the items corresponding to their \n" +
                                " names." ));

        Scene dialogScene = new Scene(dialogVbox, 610, 115);
        helpScreen.setScene(dialogScene);

        //Doesn't allow the window to be resized
        helpScreen.setResizable(false);

        //Displays it to the user
        helpScreen.show();
    }

    /**
     * Handles the purchase of it's respective item, whether or not the assistant was bought, disables button when the
     * assistant is bought as well. Also handles the upgrading by increasing the amount added every five frames to the progressbar
     *
     * Cost: 5
     * Output: 15
     * Assistant cost: 200
     *
     * @param action In this case, used to check which object initiated the listener.
     */
    public void zeroOneClick (ActionEvent action){
        if(action.getSource().equals(zeroOneBut)){
            if(mAH >= 5) {

                zeroOneTim.start();
            }
        }
        else if(action.getSource().equals(zeroOneUpg)) {

            if (mAH >= 5) {

                if(zeroOneDelay >= 1.0)
                        zeroOneUpg.setDisable(true);
                    else
                        zeroOneDelay += 0.001;



                    updateMAH(-5);
            }
        }
            else if (action.getSource().equals(zeroOneAs)){

                if(mAH >= 200){
                    zeroOneTim.start();
                    zeroOneBut.setDisable(true);
                    zeroOneAs.setDisable(true);
                    zeroOneAsBought = true;
                    updateMAH(-200);
                }

            }

    }

    /**
     * Handles the purchase of it's respective item, whether or not the assistant was bought, disables button when the
     * assistant is bought as well. Also handles the upgrading by increasing the amount added every five frames to the progressbar
     *
     * Cost: 10000
     * Output: 15000
     * Assistant cost: 25000
     *
     * @param action In this case, used to check which object initiated the listener.
     */
    public void oneOneClick (ActionEvent action){
        if(action.getSource().equals(oneOneBut)){
            if(mAH >= 10000) {

                oneOneTim.start();
            }
        }
        else if(action.getSource().equals(oneOneUpg)) {

            if (mAH >= 10000) {

                if(oneOneDelay >= 1.0)
                    oneOneUpg.setDisable(true);
                else
                    oneOneDelay += 0.001;



                updateMAH(-10000);
            }
        }
        else if (action.getSource().equals(oneOneAs)){

            if(mAH >= 25000){
                oneOneTim.start();
                oneOneBut.setDisable(true);
                oneOneAs.setDisable(true);
                oneOneAsBought = true;
                updateMAH(-25000);
            }

        }

    }

    /**
     * Handles the purchase of it's respective item, whether or not the assistant was bought, disables button when the
     * assistant is bought as well. Also handles the upgrading by increasing the amount added every five frames to the progressbar
     *
     * Cost: 650000
     * Output: 1500000
     * Assistant cost: 1200000
     *
     * @param action In this case, used to check which object initiated the listener.
     */
    public void twoOneClick (ActionEvent action){
        if(action.getSource().equals(twoOneBut)){
            if(mAH >= 650000) {

                twoOneTim.start();
            }
        }
        else if(action.getSource().equals(twoOneUpg)) {

            if (mAH >= 650000) {

                if(twoOneDelay >= 1.0)
                    twoOneUpg.setDisable(true);
                else
                    twoOneDelay += 0.001;



                updateMAH(-650000);
            }
        }
        else if (action.getSource().equals(twoOneAs)){

            if(mAH >= 1200000){
                twoOneTim.start();
                twoOneBut.setDisable(true);
                twoOneAs.setDisable(true);
                twoOneAsBought = true;
                updateMAH(-1200000);
            }

        }

    }

    /**
     * Handles the purchase of it's respective item, whether or not the assistant was bought, disables button when the
     * assistant is bought as well. Also handles the upgrading by increasing the amount added every five frames to the progressbar
     *
     * Cost: 100000000
     * Output: 250000000
     * Assistant cost: 1500000000
     *
     * @param action In this case, used to check which object initiated the listener.
     */
    public void threeOneClick (ActionEvent action){
        if(action.getSource().equals(threeOneBut)){
            if(mAH >=  100000000) {

                threeOneTim.start();
            }
        }
        else if(action.getSource().equals(threeOneUpg)) {

            if (mAH >= 100000000) {

                if(threeOneDelay >= 1.0)
                    threeOneUpg.setDisable(true);
                else
                    threeOneDelay += 0.001;



                updateMAH(-100000000);
            }
        }
        else if (action.getSource().equals(threeOneAs)){

            if(mAH >= 150000000){
                threeOneTim.start();
                threeOneBut.setDisable(true);
                threeOneAs.setDisable(true);
                threeOneAsBought = true;
                updateMAH(-150000000);
            }

        }

    }

    /**
     * Handles the purchase of it's respective item, whether or not the assistant was bought, disables button when the
     * assistant is bought as well. Also handles the upgrading by increasing the amount added every five frames to the progressbar
     *
     * Cost: 70
     * Output: 150
     * Assistant cost: 500
     *
     * @param action In this case, used to check which object initiated the listener.
     */
    public void zeroFourClick (ActionEvent action){
        if(action.getSource().equals(zeroFourBut)){
            if(mAH >= 70) {

                zeroFourTim.start();
            }
        }
        else if(action.getSource().equals(zeroFourUpg)) {

            if (mAH >= 70) {

                if(zeroFourDelay >= 1.0)
                    zeroFourUpg.setDisable(true);
                else
                    zeroFourDelay += 0.001;



                updateMAH(-70);
            }
        }
        else if (action.getSource().equals(zeroFourAs)){

            if(mAH >= 500){
                zeroFourTim.start();
                zeroFourBut.setDisable(true);
                zeroFourAs.setDisable(true);
                zeroFourAsBought = true;
                updateMAH(-500);
            }

        }

    }

    /**
     * Handles the purchase of it's respective item, whether or not the assistant was bought, disables button when the
     * assistant is bought as well. Also handles the upgrading by increasing the amount added every five frames to the progressbar
     *
     * Cost: 150000
     * Output: 270000
     * Assistant cost: 260000
     *
     * @param action In this case, used to check which object initiated the listener.
     */
    public void oneFourClick (ActionEvent action){
        if(action.getSource().equals(oneFourBut)){
            if(mAH >= 150000) {

                oneFourTim.start();
            }
        }
        else if(action.getSource().equals(oneFourUpg)) {

            if (mAH >= 150000) {

                if(oneFourDelay >= 1.0)
                    oneFourUpg.setDisable(true);
                else
                    oneFourDelay += 0.001;



                updateMAH(-5);
            }
        }
        else if (action.getSource().equals(oneFourAs)){

            if(mAH >= 260000){
                oneFourTim.start();
                oneFourBut.setDisable(true);
                oneFourAs.setDisable(true);
                oneFourAsBought = true;
                updateMAH(-260000);
            }

        }

    }

    /**
     * Handles the purchase of it's respective item, whether or not the assistant was bought, disables button when the
     * assistant is bought as well. Also handles the upgrading by increasing the amount added every five frames to the progressbar
     *
     * Cost: 1000000
     * Output: 6000000
     * Assistant cost: 2500000
     *
     * @param action In this case, used to check which object initiated the listener.
     */
    public void twoFourClick (ActionEvent action){
        if(action.getSource().equals(twoFourBut)){
            if(mAH >= 1000000) {

                twoFourTim.start();
            }
        }
        else if(action.getSource().equals(twoFourUpg)) {

            if (mAH >= 1000000) {

                if(twoFourDelay >= 1.0)
                    twoFourUpg.setDisable(true);
                else
                    twoFourDelay += 0.001;



                updateMAH(-5);
            }
        }
        else if (action.getSource().equals(twoFourAs)){

            if(mAH >= 2500000){
                twoFourTim.start();
                twoFourBut.setDisable(true);
                twoFourAs.setDisable(true);
                twoFourAsBought = true;
                updateMAH(-2500000);
            }

        }

    }

    /**
     * Handles the purchase of it's respective item, whether or not the assistant was bought, disables button when the
     * assistant is bought as well. Also handles the upgrading by increasing the amount added every five frames to the progressbar
     *
     * Cost: 500000000
     * Output: 750000000
     * Assistant cost: 750000000
     *
     * @param action In this case, used to check which object initiated the listener.
     */
    public void threeFourClick (ActionEvent action){
        if(action.getSource().equals(threeFourBut)){
            if(mAH >= 500000000) {

                threeFourTim.start();
            }
        }
        else if(action.getSource().equals(threeFourUpg)) {

            if (mAH >= 500000000) {

                if(threeFourDelay >= 1.0)
                    threeFourUpg.setDisable(true);
                else
                    threeFourDelay += 0.001;



                updateMAH(-500000000);
            }
        }
        else if (action.getSource().equals(threeFourAs)){

            if(mAH >= 750000000){
                threeFourTim.start();
                threeFourBut.setDisable(true);
                threeFourAs.setDisable(true);
                threeFourAsBought = true;
                updateMAH(-750000000);
            }

        }

    }


    /**
     * Adds the passed value to the current mAH count and updates the display
     *
     * @param val The amount of mAH to add to the count.
     */
    public void updateMAH(long val){
        mAH += val;

        energyMeter.setText(mAH + " mAH");


    }
}
