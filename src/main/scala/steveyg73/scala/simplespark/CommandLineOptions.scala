package com.worldremit.bigdata.scala.simplespark

case class CommandLineOptions(master: String = "local[2]",
                              source: String = ".",
                              tables: Seq[String] = Seq(""),
                              target: String = ".",
                              schemaFile: String = "schema.json",
                              appName: String = "Spark Application",
                              kwargs: Map[String, String] = Map("key" -> "value")
                             )

object CommandLineOptionsImplicits {

  implicit class AugmentedCommandLineOptions(cmdLineOpt: CommandLineOptions) {
    def getKeyWordArg(argName: String): String = {
      cmdLineOpt.kwargs.getOrElse(argName, "Dunno")
    }
  }

}