jvm-command
===========

The plugin has not been released yet. It is simple enough to copy `JvmCommandPlugin.scala` directly to your '~/.sbt/plugins/' folder.
See [SBT Global Plugins](http://www.scala-sbt.org/release/docs/Getting-Started/Using-Plugins#global-plugins).


Command is `system`, then use tab to list the available sub-commands.


Ex:

Setting a property

     > system setProperty logback.debug true
     [info] Setting logback.debug = true


Memory stats

     > system memoryStats
     [info] Memory
     [info]   total = 275.63 MB
     [info]   free = 134.19 MB
     [info]   used = 141.50 MB
     [info]   max = 1.33 GB


Published under the [Apache License 2.0](http://www.apache.org/licenses/LICENSE-2.0.txt)
