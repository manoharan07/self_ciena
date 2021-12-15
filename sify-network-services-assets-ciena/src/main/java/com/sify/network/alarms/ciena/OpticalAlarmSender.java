package com.sify.network.alarms.ciena;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class OpticalAlarmSender {

	/**
	 * @param args
	 */
	OpticalAlarmProducer producer;

	public void sendToQueue(String deviceIP, String strLine) {

	try {
			//producer = new AlertProducer();
			producer = OpticalAlarmProducer.getInstance(deviceIP);
			producer.connect();

			producer.send(strLine);
			//System.out.println("sent input:" + strLine);
		} catch (Exception e) {
			e.printStackTrace();

		} finally {
			//producer.cleanup();
			//producer.close();
			//producer = null;
		}

	}
	
	public void sendToQueue(String strLine) {

	try {
			//producer = new AlertProducer();
			producer = OpticalAlarmProducer.getInstance();
			producer.connect();

			producer.send(strLine);
			//System.out.println("sent input:" + strLine);
		} catch (Exception e) {
			e.printStackTrace();

		} finally {
			//producer.cleanup();
			//producer.close();
			//producer = null;
		}

	}


	public static void main(String[] args) {
		OpticalAlarmSender obj = new OpticalAlarmSender();
		while (true) {
			System.out.print("reading input : ");
			// TODO Auto-generated method strytub
			BufferedReader br = new BufferedReader(new InputStreamReader(
					System.in));
			String strLine = null;
			try {
				strLine = br.readLine();

				if (strLine != null) {
					obj.sendToQueue(strLine);
					System.out.println("sending :" + strLine);
				}
			} catch (Exception e) {
				e.printStackTrace();

			}
		}

		// obj.DoProcess(strLine);

	}

}
