package assignement1;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.BooleanControl;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;

import javax.sound.sampled.SourceDataLine;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;


public class Audio extends JFrame implements ActionListener{

   	  private static final long serialVersionUID = 1L;
	  AudioFormat audioFormat;
	  AudioInputStream audioInputStream;
	  SourceDataLine sourceDataLine;
	  boolean isPaused = false;
	  boolean repeat = false;
	  boolean isPlaying = false;
	  boolean isMuted = false;
	  boolean stopped = false;
	  JPanel buttonHolder = new JPanel();
	  JFileChooser chooser = new JFileChooser();
	  
	  final JButton errorBtn = new JButton("Ok");
	  JFrame a ;
	  
	  final JButton repeatBtn = new JButton("Repeat");
	  final JButton pauseBtn = new JButton("Pause");
	  final JButton stopBtn = new JButton("Stop");
	  final JButton playBtn = new JButton("Play");
	  final JButton muteBtn = new JButton("Mute");
	  final JButton unMuteBtn = new JButton("UnMute");
	  //final JTextField textField = new JTextField("E:\\old\\1-welcome.wav");
	  private Clip clip;
	  long stopTime =0;
	 
	  public  Audio(){
		  buttonHolder.setLayout(new GridLayout(2,2));
		  stopBtn.setEnabled(false);
		  pauseBtn.setEnabled(false);
		  repeatBtn.setEnabled(false);
		  playBtn.setEnabled(true);
		  muteBtn.setEnabled(false);
		  unMuteBtn.setEnabled(false);
		  playBtn.addActionListener(this);
		  stopBtn.addActionListener(this);
		  repeatBtn.addActionListener(this);
		  pauseBtn.addActionListener(this);
		  muteBtn.addActionListener(this);
		  unMuteBtn.addActionListener(this);
		  buttonHolder.add(muteBtn);
		  buttonHolder.add(playBtn);
		  buttonHolder.add(repeatBtn);
		  buttonHolder.add(pauseBtn);
		  buttonHolder.add(stopBtn);
		  buttonHolder.add(unMuteBtn);
		  
		  chooser.setCurrentDirectory(new java.io.File("."));
		  chooser.setDialogTitle("Browse the folder to process");
		  getContentPane().add(buttonHolder,"South");
		  getContentPane().add(chooser,"North");
		  setTitle("MP3_Player");
		  setDefaultCloseOperation(EXIT_ON_CLOSE);
		  setSize(500,365);
		  setLocation(500,200);
		  setResizable(false);
		  setVisible(true);
		  
	  }
	  public static void main  (String[]args){
		  new Audio();
	  }
	
	  private void playAudio() {
		
	    try{
	    		isPlaying = true;
	    		File soundFile =chooser.getSelectedFile();
	    		audioInputStream = AudioSystem.getAudioInputStream(soundFile);
	    		audioFormat = audioInputStream.getFormat();
	    		clip = AudioSystem.getClip();
	      	    clip.open(audioInputStream);
	      	    if(!stopped){
	      		    clip.setMicrosecondPosition(stopTime);
	      		}else{
	      			stopped = false;
	      			clip.setMicrosecondPosition(0);
	      		}
	      	
	      	
	        new PlayThread().start();
	      	
	    	}catch (Exception e) {
	    		isPlaying = false;
	    		setBtn();
	    		this.setVisible(false);
	    		a = new JFrame("Input Error");
	    		JLabel s = new JLabel();
	    		s.setText("Please Insert .Wav File only");
	    		a.getContentPane().add(s,"Center");
	    		a.setTitle("Input Error");
	    		
	  		    errorBtn.addActionListener(this);
	  		    errorBtn.setEnabled(true);
	  		    a.add(errorBtn,"South");
	  		    
	  		    a.setSize(200,100);
	  		    a.setLocation(700,300);
	  		    a.setResizable(false);
	  		    a.setVisible(true); 
	  		    a.setDefaultCloseOperation(EXIT_ON_CLOSE);
	  		    
	    	    //e.printStackTrace();
	    	    //System.out.println("exit");
	      	    //System.exit(0);
	    	}
		
	  }
	  public void setBtn(){
		  playBtn.setEnabled(true);
		  pauseBtn.setEnabled(false);
		  muteBtn.setEnabled(false);
		  unMuteBtn.setEnabled(false);
		  stopBtn.setEnabled(false);
		  repeatBtn.setEnabled(false);
	  }

	public void actionPerformed(ActionEvent e) {
		if(e.getActionCommand().equals("Play")){
			if(isPaused){
				isPaused = false;
			//	System.out.println(22);
				stopBtn.setEnabled(true);
				pauseBtn.setEnabled(true);
        		playBtn.setEnabled(false);
        		if(!repeat){
        		//	System.out.println("here");
        			repeatBtn.setEnabled(true);	
        			playAudio();
        		}else{
        			clip.loop(Clip.LOOP_CONTINUOUSLY);
        		}

        		muteBtn.setEnabled(true);
			}
			else{
				isPaused=false;
				stopBtn.setEnabled(true);
				pauseBtn.setEnabled(true);
        		playBtn.setEnabled(false);
        		repeatBtn.setEnabled(true);
        		muteBtn.setEnabled(true);
        	//	System.out.println("play");
        		playAudio();
			}
		}
		else if(e.getActionCommand().equals("Stop")){
			stopped = true;
			clip.flush();
			clip.close();
			repeat = false;
			stopBtn.setEnabled(false);
			pauseBtn.setEnabled(false);
        	playBtn.setEnabled(true);
        	repeatBtn.setEnabled(false);
        	muteBtn.setEnabled(false);
        	unMuteBtn.setEnabled(false);
        	isPaused = false;
			
		}else if(e.getActionCommand().equals("Pause")){
			isPaused = true;
			pauseBtn.setEnabled(false);
			playBtn.setEnabled(true);
			stopBtn.setEnabled(false);
			if(!repeat){
				repeatBtn.setEnabled(true);
			}
			muteBtn.setEnabled(false);
			stopTime = (clip.getMicrosecondPosition());
			clip.stop();
			
		}else if(e.getActionCommand().equals("Repeat")){
			
			repeatBtn.setEnabled(false);
			repeat = true;
			//new RepeatThread().start();
			clip.loop(Clip.LOOP_CONTINUOUSLY);
	
		}
		else if(e.getActionCommand().equals("Mute")){
			muteBtn.setEnabled(false);
			FloatControl gainControl = (FloatControl) clip
			        .getControl(FloatControl.Type.MASTER_GAIN);
			    double gain = .5D; // number between 0 and 1 (loudest)
			    float dB = (float) (Math.log(gain) / Math.log(10.0) * 20.0);
			    gainControl.setValue(dB);

			    BooleanControl muteControl = (BooleanControl) clip
			        .getControl(BooleanControl.Type.MUTE);
			    muteControl.setValue(true);
			    unMuteBtn.setEnabled(true);
		}
		else if (e.getActionCommand().equals("UnMute")){
			muteBtn.setEnabled(true);
			FloatControl gainControl = (FloatControl) clip
			        .getControl(FloatControl.Type.MASTER_GAIN);
			    double gain = .5D; // number between 0 and 1 (loudest)
			    float dB = (float) (Math.log(gain) / Math.log(10.0) * 20.0);
			    gainControl.setValue(dB);

			    BooleanControl muteControl = (BooleanControl) clip
			        .getControl(BooleanControl.Type.MUTE);
			    muteControl.setValue(false);
			    unMuteBtn.setEnabled(false);
		}else if(e.getActionCommand().equals("Ok")){
			//System.exit(0);
			//System.out.println("asd");
			a.setVisible(false);
			new Audio();
		}
	}
	class PlayThread extends Thread	
	{
		public void run(){
			
			try{
				
				
	      		clip.start(); 
	      		long dur = clip.getMicrosecondLength();
	      		long mili = dur/1000;
	      		mili++;
	      		try {
	      			Thread.sleep(mili);
	      		} catch (InterruptedException ex) {
					ex.printStackTrace();
				}
	      		if(!isPaused)
	      		{
	      			if(!repeat){
	      			clip.drain();
	      			clip.close();
	      			muteBtn.setEnabled(false);
				    stopBtn.setEnabled(false);
				    pauseBtn.setEnabled(false);
				    playBtn.setEnabled(true);
				    repeatBtn.setEnabled(false);
				    isPlaying = false;
	      			}else{
	      				
	      			}
	      		}
	      	//	System.out.println(clip.getMicrosecondLength());
			}catch (Exception e) {
		    	e.printStackTrace();
		    	System.out.println("exit");
		      	System.exit(0);
		    	}
			
		}

	}
	

	

}
