# Graph Test

## Install

### Docker

Enter directly into the `sbt` (Scala's build tool) shell

The `sbt <SUB COMMAND>`s listed below can be interactively in here by removing the `sbt ` prefix.

``` sh
docker run -it --rm -v $PWD:/app -w /app sbtscala/scala-sbt:eclipse-temurin-17.0.4_1.7.1_3.2.0 sbt
```

Otherwise here's a normal shell

``` sh
docker run -it --rm -v $PWD:/app -w /app sbtscala/scala-sbt:eclipse-temurin-17.0.4_1.7.1_3.2.0 /bin/sh
```

### Coursier

MacOS:

```
brew install coursier/formulas/coursier && cs setup
```

Info for other platforms: https://www.scala-lang.org/download/

## Development

### REPL 

Enter the Scala REPL with:

``` sh
sbt console
```

Once the `scala>` prompt is available

```scala
import example.GraphUtils.{makeGraph, shortestPath, radius, diameter, eccentricity}

shortestPath(randomGraph, randomGraph.keys.toSeq.head, randomGraph.keys.toSeq.last)

eccentricity(randomGraph, randomGraph.keys.toSeq.head)

radius(randomGraph)

diameter(randomGraph)
```

### Running

Alternatively one can run the CLI entrypoint via

``` sh
sbt 'run -N 5 -S 10'
```


### Testing

``` sh
sbt test
```


### Building the code into a jar

``` sh
sbt assembly
```

Which should build a jar to `./target/scala-2.13/graph.jar`

Which can then be launched with:

``` sh
java -jar ./target/scala-2.13/graph.jar -N 5 -S 5
```
