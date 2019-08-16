package net.auberson.raspi.akkatyped.blink;

import java.time.Duration;

import akka.NotUsed;
import akka.actor.ActorSystem;
import akka.actor.Cancellable;
import akka.stream.ActorMaterializer;
import akka.stream.Materializer;
import akka.stream.javadsl.Sink;
import akka.stream.javadsl.Source;

/**
 * Example code using Akka Stream that blinks a LED on a Raspberry Pi (connect
 * the LED to GND/Pin 6 and GPIO07/Pin 7, with a resistor): A timer is started
 * that sends a 'Toggle' message every half second. A Raspberry Pi GPIO pin
 * receives this message, and toggles the GPIO.
 */
public class BlinkExample {

	public static void main(String[] args) throws InterruptedException {
		Util.dumpInfo();
		System.out.println();

		ActorSystem system = ActorSystem.create("QuickStart");
		Materializer materializer = ActorMaterializer.create(system);
		RaspberryPi raspi = RaspberryPi.create(system);

		Sink<GPIOState, NotUsed> gpio7 = raspi.getGPIOSink(7, false);
		Sink<GPIOState, NotUsed> gpio8 = raspi.getGPIOSink(8, false);
		Sink<GPIOState, NotUsed> gpio9 = raspi.getGPIOSink(9, false);

		// Set-up a timer: Send a GPIOState.TOGGLE object every 500 millis
		Source<GPIOState, Cancellable> timerSource = Source.tick(Duration.ZERO, Duration.ofMillis(500),
				GPIOState.TOGGLE);

		// Create an 'Empty' source, which will never issue any message
		Source<GPIOState, NotUsed> emptySource = Source.empty(GPIOState.class);

		// Define the streams: On each timer tick, toggle the LED on GPIO7. Also, use
		// GPIO9 on an empty source, which causes it to be in initialised, but never
		// used (this ensures GPIO9 is low, and not floating. GPIO8 isn't used anywhere,
		// and won't be initialised at all.
		timerSource.runWith(gpio7, materializer);
		emptySource.runWith(gpio9, materializer);

		System.out.println("Blinking led on " + gpio7);
		Util.waitForever(BlinkExample.class);
	}

}
