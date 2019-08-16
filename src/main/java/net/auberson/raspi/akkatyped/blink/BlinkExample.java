package net.auberson.raspi.akkatyped.blink;

import java.time.Duration;

import akka.actor.ActorSystem;
import akka.actor.Cancellable;
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
		RaspberryPi raspi = RaspberryPi.create(system);

		// Set-up a timer: Send a GPIOState.TOGGLE object every 500 millis
		Source<GPIOState, Cancellable> timer = Source.tick(Duration.ZERO, Duration.ofMillis(500), GPIOState.TOGGLE);
		
		// Define the streams. Only one here: On each timer tick, toggle the LED: 
		timer.runWith(raspi.getGPIOSink(PIN, false), materializer);

		System.out.println("Blinking led on Pin " + PIN);
		Util.waitForever(BlinkExample.class);
	}

}
