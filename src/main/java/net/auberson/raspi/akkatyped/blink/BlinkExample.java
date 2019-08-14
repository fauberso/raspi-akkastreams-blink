package net.auberson.raspi.akkatyped.blink;

import java.time.Duration;

import akka.actor.ActorSystem;
import akka.stream.ActorMaterializer;
import akka.stream.Materializer;
import akka.stream.javadsl.Source;

/**
 * Example code using Akka Stream that blinks a LED on a Raspberry Pi (connect
 * the LED to GND/Pin 6 and GPIO07/Pin 7, with a resistor): A timer is started
 * that sends a 'Toggle' message every half second. A Raspberry Pi GPIO pin
 * receives this message, and toggles the GPIO.
 */
public class BlinkExample {
	private static final int PIN = 7;

	public static void main(String[] args) throws InterruptedException {
		Util.dumpInfo();
		System.out.println();

		ActorSystem system = ActorSystem.create("QuickStart");
		Materializer materializer = ActorMaterializer.create(system);

		Source.tick(Duration.ZERO, Duration.ofMillis(500), GPIOState.TOGGLE)
				.runWith(RaspberryPi.getGPIOSink(system, PIN, false), materializer);

		System.out.println("Blinking led on Pin " + PIN);

		Util.waitForever(BlinkExample.class);
	}

}
