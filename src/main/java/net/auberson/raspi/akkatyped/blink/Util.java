package net.auberson.raspi.akkatyped.blink;

import com.pi4j.platform.PlatformManager;
import com.pi4j.system.NetworkInfo;
import com.pi4j.system.SystemInfo;

/**
 * A collection of utility methods that do not add to the example. They have
 * been moved here to reduce clutter in the example code.
 */
final class Util {

	/**
	 * Block on forever
	 */
	static final void waitForever(Class<? extends Object> monitor) {
		try {
			synchronized (monitor) {
				while (true) {
					monitor.wait();
				}
			}
		} catch (InterruptedException e) {
			// This is acceptable behaviour: Resume execution by notifying the monitor.
		}
	}

	/**
	 * Dump miscellaneous information to StdOut
	 */
	static final void dumpInfo() {
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
