package net.auberson.raspi.akkatyped.blink;

import java.util.HashMap;
import java.util.Map;

import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.GpioPinDigitalOutput;
import com.pi4j.io.gpio.Pin;
import com.pi4j.io.gpio.PinState;
import com.pi4j.io.gpio.RaspiPin;

import akka.actor.typed.Behavior;
import akka.actor.typed.javadsl.Behaviors;

public class RaspberryPi {
	private static final GpioController gpio = GpioFactory.getInstance();
	private static final Map<Pin, GpioPinDigitalOutput> gpioMap = new HashMap<Pin, GpioPinDigitalOutput>();

	public static final Behavior<GPIOState> getGPIO(Pin pin) {
		return Behaviors.receive((ctx, msg) -> {
			GpioPinDigitalOutput output = gpioMap.get(pin);
			if (output == null) {
				output = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_01, pin.getName(),
						msg.equals(GPIOState.ON) ? PinState.HIGH : PinState.LOW);
				gpioMap.put(pin, output);
			} else {
				switch (msg) {
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
			}
			return Behaviors.same();
		});
	}
}
