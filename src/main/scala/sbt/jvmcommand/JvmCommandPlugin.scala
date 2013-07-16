package sbt.jvmcommand

import sbt.{Plugin, Command, Keys}
import Keys._
import sbt.complete.DefaultParsers._
import java.util.Date

object JvmCommandPlugin extends Plugin {
  override lazy val settings = Seq(commands += myCommand)

  val SetProperty = "setProperty"
  val GetProperty = "getProperty"
  val GetProperties = "getProperties"
  val Gc = "gc"
  val CurrentTime = "currentTime"
  val GetEnv = "getenv"
  val MemoryStats = "memoryStats"
  val AvailableProcessors = "availableProcessors"

  val parser = (Space ~> SetProperty ~ ((Space ~> NotSpace ) ~ (Space ~> NotSpace))) | 
               (Space ~> GetProperty ~ (Space ~> NotSpace)) |
               (Space ~> GetProperties) |
               (Space ~> GetEnv) |
               (Space ~> Gc) |
               (Space ~> CurrentTime) |
               (Space ~> MemoryStats) |
               (Space ~> AvailableProcessors)

  lazy val myCommand =
    Command("system")( _ => parser) { (st, args) =>
      val runtime = Runtime.getRuntime

      args match {
        case (`SetProperty`,(prop : String, value : String)) => 
          st.log.info( "Setting " + prop + " = " + value )
          System.setProperty(prop, value)
          
        case (`GetProperty`,key : String) => 
          st.log.info( key + " = " + System.getProperty(key))

        case `GetProperties` =>
          st.log.info( System.getProperties.toString )

        case `Gc` =>
          st.log.info("Running garbage collection")
          System.gc()

        case `CurrentTime` =>
          st.log.info(new Date().toString + " (currentTimeMillis = " + System.currentTimeMillis() + ")")

        case `GetEnv` =>
          st.log.info(System.getenv().toString)

        case `MemoryStats` =>
          st.log.info("Memory")
          st.log.info("\ttotal = " + FileSize(runtime.totalMemory()))
          st.log.info("\tfree = " + FileSize(runtime.freeMemory()))
          st.log.info("\tused = " + FileSize(runtime.totalMemory - runtime.freeMemory()))
          st.log.info("\tmax = " + FileSize(runtime.maxMemory()))

        case `AvailableProcessors` =>
          st.log.info("Available processors = " + runtime.availableProcessors())
      }
      
      st
    }


  case class FileSize(bytes : Long) {

    override def toString = {
      val symbol = FileSize.symbols.find { s =>
        bytes < (1L << (s._1 + 10)) &&
        bytes >= (1L << (s._1) )
      }.getOrElse( (60, "EB") )
      val size = bytes.toDouble / ( 1L << symbol._1 )

      "%.2f %s".format(size, symbol._2)
    }
  }

  object FileSize {
    val symbols = Map( 0 -> "B", 10 -> "KB", 20 -> "MB",
                       30 -> "GB", 40 -> "TB", 50 -> "PB" )
  }
}