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
  "junit" % "junit" % "4.12") 
  
  
val zipFileName = packageBin in Universal
  
lazy val riot = project.in(file(".")).enablePlugins(JavaServerAppPackaging, UniversalPlugin, DeploySSH).settings(
 version := "1.1",
 deployConfigs ++= Seq(
  ServerConfig("raspi", "169.254.18.72", Some("pi"), Some("raspberry"))
 ),
    deploySshExecBefore ++= Seq(
        (ssh: SSH) => {
           ssh.execute(s"test -f /var/run/${name.value}.pid && pkill -F /var/run/${name.value}.pid && rm /var/run/${name.value}.pid")
        }
    ),
 deployArtifacts ++= Seq(
  ArtifactSSH((stage in Universal).value, s"/tmp/${name.value}_${version.value}") 
 ),
    deploySshExecAfter ++= Seq(
        (ssh: SSH) => {
            ssh.execute(s"sudo mv /tmp/${name.value}_${version.value} /usr/share")
            ssh.execute(s"chmod +x /usr/share/${name.value}_${version.value}/bin/${name.value}")
            ssh.execute(s"screen -DmS ${name.value} /usr/share/${name.value}_${version.value}/bin/${name.value}")
            ssh.execute(s"echo $$! > /var/run/${name.value}.pid")
        }
    )
)
