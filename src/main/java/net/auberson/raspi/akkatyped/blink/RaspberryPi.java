package net.auberson.raspi.akkatyped.blink;

import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.GpioPinDigitalOutput;
import com.pi4j.io.gpio.Pin;
import com.pi4j.io.gpio.PinPullResistance;
import com.pi4j.io.gpio.PinState;
import com.pi4j.io.gpio.RaspiPin;

import akka.NotUsed;
import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import akka.stream.javadsl.Sink;
import net.auberson.raspi.akkatyped.blink.RaspberryPi.GPIOOutputActor.Initialize;
import net.auberson.raspi.akkatyped.blink.RaspberryPi.GPIOOutputActor.Shutdown;

/**
 * Reactive implementation of the GPIO outputs on a Raspberry Pi: Akka sinks can
 * be generated for any GPIO pin. They will react to 'GPIOState' messages, and
 * set the GPIO accordingly.
 */
public class RaspberryPi {
	private enum Ack {
		INSTANCE;
	}

	private static final GpioController gpio = GpioFactory.getInstance();

	public static final Sink<GPIOState, NotUsed> getGPIOSink(ActorSystem system, int pin, boolean initialstate) {
		ActorRef actor = system.actorOf(Props.create(GPIOOutputActor.class), "GPIO-" + pin);
		return Sink.actorRefWithAck(actor, new Initialize(RaspiPin.getPinByAddress(pin), initialstate), Ack.INSTANCE,
				new Shutdown(), ex -> new RuntimeException(ex));
	}

	public static class GPIOOutputActor extends AbstractActor {

		final LoggingAdapter log = Logging.getLogger(getContext().getSystem(), this);
		GpioPinDigitalOutput output;

		public static class Initialize {
			final Pin pin;
			final boolean initialstate;

			public Initialize(Pin pin, boolean initialstate) {
				this.pin = pin;
				this.initialstate = initialstate;
			}
		}

		public static class Shutdown {

		}

		@Override
		public Receive createReceive() {
			return receiveBuilder() //
					.match(Initialize.class, this::onInitialize) //
					.match(Shutdown.class, this::onStreamCompleted) //
					.match(GPIOState.class, this::onGPIOState) //
					.build();
		}

		public void onInitialize(Initialize msg) {
			this.output = gpio.provisionDigitalOutputPin(msg.pin, msg.pin.getName(),
					msg.initialstate ? PinState.HIGH : PinState.LOW);
			output.setShutdownOptions(true, PinState.LOW);
			output.setPullResistance(PinPullResistance.OFF);

			sender().tell(Ack.INSTANCE, self());
			log.info(output.getName() + " initialized");
		}

		public void onStreamCompleted(Shutdown msg) {
			// TODO
		}

		public void onGPIOState(GPIOState state) {
			switch (state) {
			case ON:
				output.high();
				break;
			case OFF:
				output.low();
				break;
			case TOGGLE:
				output.toggle();
				break;
			}

			sender().tell(Ack.INSTANCE, self());
			log.info(output.getName() + " is now " + output.getState());
		}

	}

}
