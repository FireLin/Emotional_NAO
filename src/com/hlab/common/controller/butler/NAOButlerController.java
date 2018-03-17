package com.hlab.common.controller.butler;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;

import javax.imageio.ImageIO;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;

import com.aldebaran.qi.Application;
import com.aldebaran.qi.CallError;
//import com.aldebaran.qi.helper.proxies.ALAudioPlayer;
import com.aldebaran.qi.helper.proxies.ALLeds;
import com.aldebaran.qi.helper.proxies.ALMotion;
import com.aldebaran.qi.helper.proxies.ALRobotPosture;
import com.aldebaran.qi.helper.proxies.ALTextToSpeech;
import com.aldebaran.qi.helper.proxies.ALVideoDevice;

import com.hlab.app.model.ConfigFile;
import com.hlab.common.controller.AbstractButlerController;

/**
 * NAOButlerController extends an abstract buttler, this class is specifically for the 
 * NAO robot
 * 
 * @author Hesham Omran
 *
 */
public class NAOButlerController extends AbstractButlerController {
	private Application application;
	private ALVideoDevice video;
	private ALTextToSpeech tts;
	private ALMotion motion;
	private ALLeds leds;
	private ALRobotPosture posture;
	private boolean isSittingDown;
	//private ALAudioPlayer player;
	//private int FileId;

	/**
	 * Constructor
	 * @throws Exception
	 */
	public NAOButlerController() throws Exception{
		super();
		String robotUrl = ConfigFile.getInstance().getRobotURL();
        // Create a new application
		String[] args = {""};
    	application = new Application(args, robotUrl);
        // Start your application
        application.start();
	    video = new ALVideoDevice(application.session());
	    tts = new ALTextToSpeech(application.session());
	    tts.setLanguage("English");
	    motion = new ALMotion(application.session());
	    posture = new ALRobotPosture(application.session());
	    //player = new ALAudioPlayer(application.session());
	    //FileId = player.loadFile(properties.getButlerWakeUpTone());
	    leds = new ALLeds(application.session());
	    standUpPosture();
	}
	
	/**
	 * Overrides the abstract getImageSeenByButler, which convert the NAO images from
	 * the robot to an image File stored in data folder
	 */
	@SuppressWarnings("finally")
	@Override
	public File getImageSeenByButler(){
		File x = null;
		try{
			String subscribe = video.subscribeCamera("Nao Image", 0, 2, 11, 10);
	    	Object img = video.getImageRemote(subscribe);
	    	video.releaseImage(subscribe);
	    	video.unsubscribe(subscribe); // don't forget to unsubscribe
	    	@SuppressWarnings("unchecked")
	    	ArrayList<Object> imageContainer = (ArrayList<Object>)img;
	    	ByteBuffer buffer = (ByteBuffer)imageContainer.get(6); 
	    	byte[] imageRawData = buffer.array(); // your image is contained here
	    	System.out.println(buffer.array().length);
	    	int imageWidth = (int)imageContainer.get(0);
	    	int imageHeight = (int)imageContainer.get(1);
	    	x = getImage(imageHeight, imageWidth,imageRawData);
		}catch(Exception e){
			System.out.print(e.toString());
		} finally{
			return x;
		}
	}
	
	/**
	 * This extends the abstract say method, using the NAO library for Text to Speech
	 */
	@Override
	public void say(String speechtext) {
		try {
			tts.async().say(speechtext);
		} catch (CallError | InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * this extends the abstract animate method, using nao library to animate the robot using 
	 * predefined commands
	 */
	@Override
	public void animate(Object[] args) {
		try{
			if(args != null){
				if(args.length > 0){
					if(args[0].toString().equals("LEFT")){
						pointToLeft();
					}
					else if(args[0].toString().equals("RIGHT")){
						pointToRight();
					}
					else if(args[0].toString().equals("STAND")){
						standUpPosture();
					}
					else if(args[0].toString().equals("SIT")){
						sitPosture();
					}
				}
			}
		}catch(Exception exp){
			System.out.print(exp.toString());
		}
	}	
	
	/**
	 * This extends the wakeup NAO call, when NAO is being called
	 */
	@Override
	public void wakeUp() {
		playSound(ConfigFile.getInstance().getButlerWakeUpTone());
//		try {
//			leds.async().fadeRGB("FaceLeds", "yellow", 1f);
//			//player.play(FileId);
//		} catch (CallError | InterruptedException e) {
//			e.printStackTrace();
//		}
	}
	
	
	public boolean getIsButlerSittingDown(){
		return this.isSittingDown;
	}
	
	/**
	 * This method retrieves image memory from the NAO and encode it to a Gray scale image
	 * that is stored in the data folder
	 * @param Height int
	 * @param Width int 
	 * @param rawData byte[]
	 * @return
	 * @throws IOException
	 */
	private File getImage(int Height, int Width, byte[] rawData) throws IOException{
		 int[] intArray = new int[Height * Width];
        for (int i = 0; i < Height * Width; i++) {
            intArray[i] =
                    ((rawData[(i * 3)] & 0xFF) << 16) | // red
                    ((rawData[i * 3 + 1] & 0xFF) << 8) | // green
                    ((rawData[i * 3 + 2] & 0xFF)); // blue
        }
        BufferedImage image = new BufferedImage(Width, Height, 
       		 BufferedImage.TYPE_BYTE_GRAY);
        image.getRaster().setPixels(0, 0, Width, Height, intArray);
        File outputfile = new File(this.visionfolderpath+"image.jpg");
        ImageIO.write(image, "jpg", outputfile);
        return outputfile;
	}
	
	/**
	 * This method manpulate the NAO movement to point it's hand and head to the left
	 * @throws CallError
	 * @throws InterruptedException
	 */
	private void pointToLeft() throws CallError, InterruptedException{
		ArrayList<String> pNames = 
				new ArrayList<String>(Arrays.asList( new String[]{"HeadYaw","LShoulderRoll"}));
		
		ArrayList<Object> anglelists = new ArrayList<Object>();
		ArrayList<Float> Headyawangles = 
				new ArrayList<Float>(Arrays.asList(new Float[]{0.972f,0.0f}));
		ArrayList<Float> SholderRollangles = 
				new ArrayList<Float>(Arrays.asList(new Float[]{1.322f,0.195f}));
		anglelists.add(Headyawangles);
		anglelists.add(SholderRollangles);
		
		ArrayList<Object> timelists = new ArrayList<Object>();
		ArrayList<Float> Headyawtimes = 
				new ArrayList<Float>(Arrays.asList(new Float[]{1.0f,3.0f}));
		ArrayList<Float> SholderRolltimes = 
				new ArrayList<Float>(Arrays.asList(new Float[]{1.0f,3.0f}));
		timelists.add(Headyawtimes);
		timelists.add(SholderRolltimes);
		
        motion.angleInterpolation(pNames, anglelists, timelists, true);
	}
	
	/**
	 * This method manpulate the NAO movement to point it's hand and head to the right
	 * @throws CallError
	 * @throws InterruptedException
	 */
	private void pointToRight() throws CallError, InterruptedException{
		
		ArrayList<String> pNames = 
				new ArrayList<String>(Arrays.asList( new String[]{"HeadYaw","RShoulderRoll"}));
		
		ArrayList<Object> anglelists = new ArrayList<Object>();
		ArrayList<Float> Headyawangles = 
				new ArrayList<Float>(Arrays.asList(new Float[]{-0.933f,0.0f}));
		ArrayList<Float> SholderRollangles = 
				new ArrayList<Float>(Arrays.asList(new Float[]{-0.933f,-0.185f}));
		anglelists.add(Headyawangles);
		anglelists.add(SholderRollangles);
		
		ArrayList<Object> timelists = new ArrayList<Object>();
		ArrayList<Float> Headyawtimes = 
				new ArrayList<Float>(Arrays.asList(new Float[]{1.0f,3.0f}));
		ArrayList<Float> SholderRolltimes = 
				new ArrayList<Float>(Arrays.asList(new Float[]{1.0f,3.0f}));
		timelists.add(Headyawtimes);
		timelists.add(SholderRolltimes);
		
        motion.angleInterpolation(pNames, anglelists, timelists, true);
	}
	
	/**
	* This method manpulate the NAO to stand up
	*/
	private void standUpPosture(){
		try {
			posture.goToPosture("Stand", 0.5f);
			this.isSittingDown = false;
		} catch (CallError | InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	/**
	* This method manpulate the NAO to sit down
	*/
	private void sitPosture(){
		try {
			posture.goToPosture("Sit", 0.5f);
			this.isSittingDown = true;
		} catch (CallError | InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * This method extends abstract sleepBack to push NAO to sleep again, and not to react until
	 * woken up once more 
	 */
	@Override
	public void sleepBack() {
		//playSound(properties.getButlerWakeUpTone());
//		try {
//			leds.async().fadeRGB("FaceLeds", "white", 1f);
//		} catch (CallError | InterruptedException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
	}
	
	/**
	 * The voice played when NAO is woken up
	 * @param filename
	 */
	private void playSound(String filename){
		String strFilename = filename;
		int BUFFER_SIZE = 128000;
		AudioInputStream audioStream = null;
		SourceDataLine sourceLine = null;
		AudioFormat audioFormat = null;
		File soundFile = null;
        try {
        	soundFile = new File(strFilename);
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }

        try {
        	audioStream = AudioSystem.getAudioInputStream(soundFile);
        } catch (Exception e){
            e.printStackTrace();
            System.exit(1);
        }

        audioFormat = audioStream.getFormat();

        DataLine.Info info = new DataLine.Info(SourceDataLine.class, audioFormat);
        try {
            sourceLine = (SourceDataLine) AudioSystem.getLine(info);
            sourceLine.open(audioFormat);
        } catch (LineUnavailableException e) {
            e.printStackTrace();
            System.exit(1);
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }

        sourceLine.start();

        int nBytesRead = 0;
        byte[] abData = new byte[BUFFER_SIZE];
        while (nBytesRead != -1) {
            try {
                nBytesRead = audioStream.read(abData, 0, abData.length);
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (nBytesRead >= 0) {
                @SuppressWarnings("unused")
                int nBytesWritten = sourceLine.write(abData, 0, nBytesRead);
            }
        }

        sourceLine.drain();
        sourceLine.close();
    }
	
	
}
