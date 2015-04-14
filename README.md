# Build/run
To run, clone this repo, cd into it, and run this at the command line:

```sh
$ ./gradlew run
```

Or, if you're on Windows, use `gradlew.bat`, instead of `gradlew`.

To build, run `./gradlew build`. This will compile all source code, run the tests,
and put the compiled `.class` files under `build/classes`.

To run the program independently of Gradle, you can build a jar that includes
all dependencies by running:

```sh
$ gradlew fatjar
```

This will make the jar compiled into `build/libs` also contain all dependencies,
so it can be run standalone with:

```sh
$ java -cp ./build/libs/scraper.jar Main
```

## Eclipse
To set up an Eclipse project that includes all dependencies, you can run:

```sh
$ ./gradlew eclipse
```

Then in Eclipse, you can import this directory as an Eclipse project.
---------------------------------------------------------------------------------
Doing everything above will create a text file (data.txt) with output in it.
Then, setup the database sructure  by importing education.sql (from canvas) file from canvas.t
Run dbwriter.php [writes everything to the db] : localhost/view.php  
Then, run view.php.
