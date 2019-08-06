import deployssh.DeploySSH._
import fr.janalyse.ssh.SSH

name := "raspi-akkatyped-blink"

maintainer := "frederic@auberson.net"

version := "1.0"

scalaVersion := "2.12.7"

lazy val akkaVersion = "2.6.0-M5"

libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-actor-typed" % akkaVersion,
  "com.typesafe.akka" %% "akka-testkit" % akkaVersion,
  "com.pi4j" % "pi4j-core" % "1.2",
  "com.pi4j" % "pi4j-native" % "1.2",
  "junit" % "junit" % "4.12") 
  
lazy val riot = project.in(file(".")).enablePlugins(JavaAppPackaging, DeploySSH).settings(
 version := "1.1",
 deployConfigs ++= Seq(
  ServerConfig("raspi", "169.254.18.72", Some("pi"), Some("raspberry"))
 ),
    deploySshExecBefore ++= Seq(
        (ssh: SSH) => {
           ssh.execute(s"screen -XS raspi-akkatyped-blink quit")
        }
    ),
 deployArtifacts ++= Seq(
  ArtifactSSH((stage in Universal).value, s"/tmp/${name.value}_${version.value}") 
 ),
    deploySshExecAfter ++= Seq(
        (ssh: SSH) => {
            val log = sLog.value
            ssh.execute(s"sudo rm -rf /usr/share/${name.value}_${version.value}")
            ssh.execute(s"sudo mv /tmp/${name.value}_${version.value} /usr/share")
            ssh.execute(s"chmod +x /usr/share/${name.value}_${version.value}/bin/${name.value}")
            ssh.execute(s"screen -dmS ${name.value} /usr/share/${name.value}_${version.value}/bin/${name.value}")
            log.info(s"Process started in screen. Reattach to screen with 'screen -r ${name.value}', then leave with 'ctrl-A d'.")
        }
    ),
   deploySshServersNames ++= Seq("raspi")
)
