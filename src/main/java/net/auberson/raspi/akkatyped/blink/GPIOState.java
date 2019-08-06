package net.auberson.raspi.akkatyped.blink;

/**
 * Models the state of a GPIO pin: ON (high), OFF (low), or TOGGLE (will become
 * ON if it was OFF, and vice-versa).
 */
public enum GPIOState {
	ON, OFF, TOGGLE
}
