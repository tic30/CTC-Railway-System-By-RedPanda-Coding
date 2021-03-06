/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.redpanda.traincontrolsystem.trackmodelstandalone;

import org.redpanda.traincontrolsystem.trainmodel.Train;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.concurrent.ThreadLocalRandom;
import javax.swing.JLabel;
import javax.swing.DefaultListModel;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import java.awt.*;
import javax.swing.*;

public class TrackModelUI extends JPanel{
    
        private static TrackModelUI instance; // single instance of class
	private JFrame theWindow; // main window
        private JComboBox switchList, crossingList;
        int numSwitches=0, numCrossings=0;
        String[] switches=new String[1];
        String[] crossings=new String[1];
        
        private DefaultListModel<String> model; //warning info
	private JList<String> failureList;
        private DefaultListModel<String> model2; //warning info
	private JList<String> tracklist;
        private JScrollPane failurePane;
        private JScrollPane trackPane;
        private JPanel failurePanel= new JPanel();
        private JPanel failureButtonPanel=new JPanel();
        private JTextField addFailure=new JTextField("Add Failure");
        private JTextField removeFailure=new JTextField("Remove Failure (using #)");
        private int numFailures=0;
      
        private JTextField inputTemp, inputFile, inputMaxPass;
        
        private int temp, maxPass;
        private JLabel tempLabel, railLabel; 
        private JLabel passengerLabel=new JLabel("Number Passengers on Train: ");
        private JLabel passLimLabel = new JLabel("Max Number of Passengers per Car: ");
        
        private JPanel topHalf=new JPanel();
        private JPanel infoPanel1=new JPanel();
        private JPanel infoPanel2=new JPanel();
        private JPanel inputPanel=new JPanel();
        private JPanel trackPanel=new JPanel();
        
        private TrackModel trackModel;
        
    
    public TrackModelUI() {
        initializeUI();
    }
    
    public void createModel(){
        trackModel = new TrackModel();
        System.out.println("track instance" + trackModel);
    }
    
    public void initializeUI() {
                
                //WINDOW
                theWindow = new JFrame("Track Model");
                theWindow.getContentPane().setLayout(new GridLayout(3,1)); //2 rows, 1 col
                
                //STATIONS, CROSSINGS
                infoPanel1.setLayout(new GridLayout(1,2)); //switches 
                switchList = new JComboBox(switches);
                switchList.setSelectedIndex(0);
                crossingList = new JComboBox(crossings);
                crossingList.setSelectedIndex(0);
                infoPanel1.add(switchList); //crossings
                infoPanel1.add(crossingList);
                
                //INPUTS - FILE, TEMP, MAX PASS
                inputPanel.setLayout(new GridLayout(1,2));
                inputTemp=new JTextField("Modify Temperature",3);
                TempInputActionListener tempInputListener= new TempInputActionListener();
                inputTemp.addActionListener(tempInputListener);
                inputMaxPass=new JTextField("Modify Passenger Limit",4);
                PassLimActionListener passLimListener=new PassLimActionListener();
                inputMaxPass.addActionListener(passLimListener);
                inputPanel.add(inputTemp);
                inputPanel.add(inputMaxPass);
                
                //TEMP, MAX PASS, CURR PASS
                infoPanel2.setLayout(new GridLayout(1,4));
                temp=ThreadLocalRandom.current().nextInt(20, 85 + 1);
                tempLabel=new JLabel("Temperature: " + temp); //add info
                if (temp<=35){
                    railLabel=new JLabel("Rails Heated");}
                else{
                    railLabel=new JLabel("Rails not Heated");}
                infoPanel2.add(tempLabel);
                infoPanel2.add(railLabel);
                passengerLabel.setText("Number Passengers on Train: "+ThreadLocalRandom.current().nextInt(20, 85 + 1));
                infoPanel2.add(passengerLabel);
                infoPanel2.add(passLimLabel);
                topHalf.setLayout(new GridLayout(3,1));
                topHalf.add(infoPanel1);
                topHalf.add(inputPanel);
                topHalf.add(infoPanel2);
               
                //FAILURES
                failurePanel.setLayout(new GridLayout(2,1));
                model = new DefaultListModel<String>();
                failureList = new JList<String>(model);
                failurePane = new JScrollPane(failureList);
                model.addElement("Track Failures: " );
                failurePanel.add(failurePane);
                failureButtonPanel.setLayout(new GridLayout(1,2));
                AddFailureActionListener addActionListener=new AddFailureActionListener();
                addFailure.addActionListener(addActionListener);
                RemoveFailureActionListener removeActionListener= new RemoveFailureActionListener();
                removeFailure.addActionListener(removeActionListener);  
                failureButtonPanel.add(addFailure);
                failureButtonPanel.add(removeFailure);
                failurePanel.add(failurePane);
                failurePanel.add(failureButtonPanel);
                
                //TRACK PANEL
                model2 = new DefaultListModel<String>();
                tracklist = new JList<String>(model2);
                trackPane = new JScrollPane(tracklist);
                model2.addElement("Track Pieces" );
                trackPanel.add(trackPane);
                
                theWindow.getContentPane().add(topHalf);
                theWindow.getContentPane().add(failurePanel);
                theWindow.getContentPane().add(trackPanel);
                
                theWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		theWindow.setSize(1000,1000);
		theWindow.setVisible(true);
	}
    
   public static synchronized TrackModelUI getInstance() {
        if(instance==null) {
            instance = new TrackModelUI();}
	return instance;}
   
   public void addcrossingtoUI(int blockNumber){
       if (numSwitches==switches.length){
           String[] temp=new String[2*switches.length];
           for (int i=0;i<switches.length;i++){
               temp[i]=switches[i];}
           switches=new String[temp.length];
           for (int i=0;i<switches.length;i++){
               switches[i]=temp[i];}
       }
       switches[numSwitches]=new String("Switch "+blockNumber);
       switchList.addItem(switches[numSwitches]);
       numSwitches++;
   }
   
   public void addswitchtoUI(int blockNumber){
       if (numCrossings==crossings.length){
           String[] temp=new String[2*crossings.length];
           for (int i=0;i<crossings.length;i++){
               temp[i]=crossings[i];}
           crossings=new String[temp.length];
           for (int i=0;i<crossings.length;i++){
               crossings[i]=temp[i];}
       }
       crossings[numCrossings]=new String("Crossing "+blockNumber);
       crossingList.addItem(crossings[numCrossings]);
       numCrossings++;
   }
   
   public void addTrackPiece(String s)  
   {
        model2.addElement(s);
   }
    
    private class AddFailureActionListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            String newFailure=addFailure.getText();
            if (newFailure.toLowerCase().contains("rail")){
                model.addElement("Warning: Rail Failure at " + "");
            }
            else if(newFailure.toLowerCase().contains("circuit")){
                model.addElement("Warning: Circuit Failure on " + "");
            }
            else if(newFailure.toLowerCase().contains("power")){
                model.addElement("Warning: Power Failure - SYSTEM STOP");}
            else{
                System.out.println("The failure is irrelevant");
            }
            numFailures++;}}
    
     private class RemoveFailureActionListener implements ActionListener {
	public void actionPerformed(ActionEvent e) {
            if (model.getSize() > 0){
                int x = Integer.parseInt(removeFailure.getText());
                model.removeElementAt(x);
                numFailures--;}}}
     
     private class PassLimActionListener implements ActionListener {
	public void actionPerformed(ActionEvent e) {
            maxPass=Integer.parseInt(inputMaxPass.getText());
            passLimLabel.setText("Max Number of Passengers per car: " + maxPass);}}
     
     private class TempInputActionListener implements ActionListener {
	public void actionPerformed(ActionEvent e) {
            temp=Integer.parseInt(inputTemp.getText());
            if (temp<=35){
                    railLabel.setText("Rails Heated");}
            else{
                    railLabel.setText("Rails not Heated");}
            tempLabel.setText(String.valueOf(temp));}}
}
