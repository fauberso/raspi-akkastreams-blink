package net.auberson.raspi.akkatyped.blink;

import java.time.Duration;

import akka.actor.typed.ActorSystem;
import akka.actor.typed.Behavior;
import akka.actor.typed.javadsl.Behaviors;

public class BlinkExample {

	public static void main(String[] args) throws InterruptedException {

		Behavior<GPIOState> timedTrigger = Behaviors.withTimers(timers -> {
			timers.startTimerAtFixedRate("LedTimer", GPIOState.TOGGLE, Duration.ofMillis(500));
			return RaspberryPi.GPIO_LED;
		});

		ActorSystem system = ActorSystem.create(timedTrigger, "ActorSystem");

		// Block forever
		synchronized (BlinkExample.class) {
			while (true) {
				BlinkExample.class.wait();
			}
		}
	}

}
