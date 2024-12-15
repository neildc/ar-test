package example

import GraphUtils.{makeGraph, radius, diameter, shortestPath, eccentricity}

object Main {
  def main(args: Array[String]): Unit = {
    args match {
      case Array("-N", n, "-S", s) => {
        val N = n.toInt
        val S = s.toInt

        val graph = makeGraph(N, S)
        pprint.pprintln(graph)

        val rad = radius(graph)
        val dia = diameter(graph)
        println(s"Radius: $rad")
        println(s"Diameter: $dia")

        val randomStart =
          graph.keys.toSeq(scala.util.Random.nextInt(graph.size))
        val randomEnd = graph.keys.toSeq(scala.util.Random.nextInt(graph.size))

        println(s"Finding shortest path from $randomStart to $randomEnd")

        val path = shortestPath(graph, randomStart, randomEnd)

        println(s"Path: $path")

        val randomNode = graph.keys.toSeq(scala.util.Random.nextInt(graph.size))

        println(
          s"Eccentricity of node $randomNode: ${eccentricity(graph, randomNode)}"
        )

      }
      case _ =>
        println(
          s"""
           |Usage:
           |graph -N <N> -S <S>
           |
           |    N - size of generated graph
           |    S - sparseness (number of directed edges actually; from N-1 (inclusive) to N(N-1) (inclusive))
           |
           |Example:
           |graph -N 4 -S 4
           """.stripMargin
        )
    }

  }

}
