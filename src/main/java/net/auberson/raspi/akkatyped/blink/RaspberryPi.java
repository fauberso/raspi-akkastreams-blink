package net.auberson.raspi.akkatyped.blink;

import akka.actor.typed.Behavior;
import akka.actor.typed.javadsl.Behaviors;

public class RaspberryPi {

	public static final Behavior<GPIOState> GPIO_LED = Behaviors.receive((ctx, msg) -> {
		System.out.println("GPIO State is now " + msg.toString() + "!");
		return Behaviors.same();
	});

}
