jarsurgeon
==========

jarsurgeon makes it easy to instrument and rebuild JAR files without source
code.  It uses the [Krakatau](https://github.com/Storyyeller/Krakatau)
assembler and disassembler to convert binary .class files to and from ".j"
bytecode files.  The bytecode files can be modified in any standard text
editor, adding debug prints or modifying the program's behavior.

Unlike most Java decompiler output, the human-readable bytecode files used by
Krakatau/jarsurgeon can usually be converted back into fully functional
Java binaries, even for complex applications.  Displaying intermediate
values, function arguments, and other internal data can significantly speed
up the analysis of undocumented code.

## Prerequisites

* Ruby 1.9.3 or above.  Instructions for upgrading ruby on old Ubuntu
systems can be found [here](http://brightbox.com/docs/ruby/ubuntu/).
* The [rubyzip](https://rubygems.org/gems/rubyzip) gem

## Sample usage

First, clone the necessary source repos, and set up some environment
variables to tell jarsurgeon where to find them:

    git clone git://github.com/Storyyeller/Krakatau
    export KRAKATAU_HOME=`pwd`/Krakatau
    git clone git://github.com/cernekee/jarsurgeon
    export JARSURGEON_INST=`pwd`/jarsurgeon/Inst.java

Now look at the bundled test program.  It prints "hello world" and exits:

    $ cd jarsurgeon
    $ java -jar examples/app.jar
    hello world

We can unpack the JAR and add a debug print:

    $ ./jarsurgeon.rb --git examples/app.jar
      COPY      META-INF/MANIFEST.MF
      DISASM    com/example/Test.class
      COPY      inst/Inst.java
      GIT       app
    $ cd app
    $ git am -3 ../examples/num-args.patch 
    Applying: main: Add 'number of args' instrumentation
    $ make
      ASM    obj/com/example/Test.class
      JAR    new.jar

(We used --git to initialize app/ as its own git repository, making it easy
to track and revert changes to the bytecode.)

Executing the new JAR, we see the result:

    $ java -jar new.jar
    ;Number of args passed to main: 0
    hello world

The supplied Inst.java contains a few example methods that have proven
useful in the past, but adding more instrumentation is as simple as editing
the source file and running "make".  To see the type signatures for calling
each Inst.java function, use:

    javap -public -s obj/inst/Inst

jarsurgeon.rb can be copied to any directory in your PATH, if desired.

## Licensing

jarsurgeon is distributed under the MIT License.
