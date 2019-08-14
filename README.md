# raspi-akkastreams-blink
The simplest possible "blink" example on a Raspberry Pi using Akka Stream and SBT deployment

To deploy, execute `sbt deploy` in the project's root directory.

Open issues:
- Requires the packages 'oracle-java8-jdk', 'wiringpi' and 'screen' to be installed on your Pi.
- There's currently no way to auto-detect the Raspberry PI's IP address. For now, it must be manually edited in build.sbt in case your device cannot be found under the name 'raspberrypi.local'.
- When the Raspberry Pi reboots, the application isn't restarted.