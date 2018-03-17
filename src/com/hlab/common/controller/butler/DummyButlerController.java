package com.hlab.common.controller.butler;

import java.io.File;
import java.io.IOException;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;
import javax.imageio.ImageIO;

import com.github.sarxos.webcam.Webcam;

import com.hlab.app.model.ConfigFile;
import com.hlab.common.controller.AbstractButlerController;

/**
 * DummyButlerController extends an abstract buttler, this class is made for creating a light
 * weight butler robot for testing
 * 
 * @author Hesham Omran
 *
 */
public class DummyButlerController extends AbstractButlerController {
	
	Webcam webcam;
	public DummyButlerController(){
		super();
		webcam = Webcam.getDefault();
		webcam.open();
	}
	
	@Override
	public File getImageSeenByButler(){
		File img = new File(this.visionfolderpath+"hello-world.png");
		try {
			ImageIO.write(webcam.getImage(), "PNG",img );
		} catch (IOException e) {
			e.printStackTrace();
		}
		return img;
	}
	
	@Override
	public void say(String speechtext) {
		Process p = null;
		try {
			p = new ProcessBuilder("say", speechtext).start();
			p.waitFor();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
	}

	@Override
	public void animate(Object[] args) {
		System.out.println(args[0].toString());
	}
	
	@Override
	public void wakeUp() {
		playSound(ConfigFile.getInstance().getButlerWakeUpTone());
	}
	
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

	@Override
	public void sleepBack() {
		//playSound(properties.getButlerWakeUpTone());
	}
}
