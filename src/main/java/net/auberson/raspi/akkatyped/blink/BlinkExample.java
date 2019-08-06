package net.auberson.raspi.akkatyped.blink;

import java.time.Duration;

import com.pi4j.io.gpio.Pin;
import com.pi4j.io.gpio.RaspiPin;

import akka.actor.typed.ActorSystem;
import akka.actor.typed.Behavior;
import akka.actor.typed.javadsl.Behaviors;

/**
 * Example code using Akka-Typed that blinks a LED on a Raspberry Pi (conect the
 * LED to GND/Pin 6 and GPIO07/Pin 7, with a resistor): A timer is started that
 * sends a 'Toggle' message every half second. A Raspberry Pi GPIO pin receives
 * this message, and toggles the GPIO.
 */
public class BlinkExample {

	public static void main(String[] args) throws InterruptedException {
		Util.dumpInfo();
		System.out.println();

		Pin pin = RaspiPin.GPIO_07;

		Behavior<GPIOState> timedTrigger = Behaviors.withTimers(timers -> {
			timers.startTimerAtFixedRate("LedTimer", GPIOState.TOGGLE, Duration.ofMillis(500));
			return RaspberryPi.getGPIO(pin);
		});

		System.out.println("Blinking led on " + pin.getName());

		ActorSystem.create(timedTrigger, "ActorSystem");

		Util.waitForever(BlinkExample.class);
	}

}
