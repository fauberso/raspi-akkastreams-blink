package net.auberson.raspi.akkatyped.blink;

import java.time.Duration;

import com.pi4j.io.gpio.Pin;
import com.pi4j.io.gpio.RaspiPin;

import akka.actor.typed.ActorSystem;
import akka.actor.typed.Behavior;
import akka.actor.typed.javadsl.Behaviors;

public class BlinkExample {

	public static void main(String[] args) throws InterruptedException {
		Pin pin = RaspiPin.GPIO_26;

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

}
