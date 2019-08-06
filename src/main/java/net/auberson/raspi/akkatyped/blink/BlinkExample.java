package net.auberson.raspi.akkatyped.blink;

import java.time.Duration;

import com.pi4j.io.gpio.Pin;
import com.pi4j.io.gpio.RaspiPin;
import com.pi4j.platform.PlatformManager;
import com.pi4j.system.NetworkInfo;
import com.pi4j.system.SystemInfo;

import akka.actor.typed.ActorSystem;
import akka.actor.typed.Behavior;
import akka.actor.typed.javadsl.Behaviors;

public class BlinkExample {

	public static void main(String[] args) throws InterruptedException {
		dumpInfo();
		System.out.println();
		
		Pin pin = RaspiPin.GPIO_25;

		Behavior<GPIOState> timedTrigger = Behaviors.withTimers(timers -> {
			timers.startTimerAtFixedRate("LedTimer", GPIOState.TOGGLE, Duration.ofMillis(500));
			return RaspberryPi.getGPIO(pin);
		});

		System.out.println("Blinking led on " + pin.getName());

		ActorSystem system = ActorSystem.create(timedTrigger, "ActorSystem");

		// Block forever
		synchronized (BlinkExample.class) {
			while (true) {
				BlinkExample.class.wait();
			}
		}
	}

	private static final void dumpInfo() {
		try {
			System.out.println("Platform Name     :  " + PlatformManager.getPlatform().getLabel());
			System.out.println("Platform ID       :  " + PlatformManager.getPlatform().getId());
			System.out.println("Serial Number     :  " + SystemInfo.getSerial());
			System.out.println("CPU Revision      :  " + SystemInfo.getCpuRevision());
			System.out.println("CPU Architecture  :  " + SystemInfo.getCpuArchitecture());
			System.out.println("CPU Part          :  " + SystemInfo.getCpuPart());
			System.out.println("CPU Temperature   :  " + SystemInfo.getCpuTemperature());
			System.out.println("CPU Core Voltage  :  " + SystemInfo.getCpuVoltage());
			System.out.println("CPU Model Name    :  " + SystemInfo.getModelName());
			System.out.println("Processor         :  " + SystemInfo.getProcessor());
			System.out.println("Hardware          :  " + SystemInfo.getHardware());
			System.out.println("Hardware Revision :  " + SystemInfo.getRevision());
			System.out.println("Board Type        :  " + SystemInfo.getBoardType().name());
			System.out.println("Total Memory      :  " + SystemInfo.getMemoryTotal());
			System.out.println("Used Memory       :  " + SystemInfo.getMemoryUsed());
			System.out.println("Free Memory       :  " + SystemInfo.getMemoryFree());
			System.out.println("Shared Memory     :  " + SystemInfo.getMemoryShared());
			System.out.println("Memory Buffers    :  " + SystemInfo.getMemoryBuffers());
			System.out.println("Cached Memory     :  " + SystemInfo.getMemoryCached());
			System.out.println("OS Name           :  " + SystemInfo.getOsName());
			System.out.println("OS Version        :  " + SystemInfo.getOsVersion());
			System.out.println("OS Architecture   :  " + SystemInfo.getOsArch());
			System.out.println("OS Firmware Build :  " + SystemInfo.getOsFirmwareBuild());
			System.out.println("OS Firmware Date  :  " + SystemInfo.getOsFirmwareDate());
			System.out.println("Java Vendor       :  " + SystemInfo.getJavaVendor());
			System.out.println("Java Vendor URL   :  " + SystemInfo.getJavaVendorUrl());
			System.out.println("Java Version      :  " + SystemInfo.getJavaVersion());
			System.out.println("Java VM           :  " + SystemInfo.getJavaVirtualMachine());
			System.out.println("Java Runtime      :  " + SystemInfo.getJavaRuntime());

			System.out.println("Hostname          :  " + NetworkInfo.getHostname());
			for (String ipAddress : NetworkInfo.getIPAddresses())
				System.out.println("IP Addresses      :  " + ipAddress);
			for (String fqdn : NetworkInfo.getFQDNs())
				System.out.println("FQDN              :  " + fqdn);
			for (String nameserver : NetworkInfo.getNameservers())
				System.out.println("Nameserver        :  " + nameserver);

			System.out.println("ARM Frequency     :  " + SystemInfo.getClockFrequencyArm());
			System.out.println("CORE Frequency    :  " + SystemInfo.getClockFrequencyCore());
		} catch (Throwable t) {
			System.err.println(t.getMessage());
		}
	}

}
