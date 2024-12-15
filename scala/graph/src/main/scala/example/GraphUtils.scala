package example

import scala.collection.immutable.{Queue, SortedMap}

object GraphUtils {

  case class Edge(node: Int, weight: Int)

  type Graph = SortedMap[Int, List[Edge]]

  // Write an algorithm to randomly generate a simple directed graph
  // Such that
  // Input:
  //     N - size of generated graph
  //     S - sparseness (number of directed edges actually; from N-1 (inclusive) to N(N-1) (inclusive))
  // Output:
  //     simple connected graph G(n,s) with N vertices and S edges
  def makeGraph(
      N: Int,
      S: Int
  ): Graph = {
    makeGraphWithSeed(scala.util.Random.nextInt(), N, S)
  }

  // Makes it easier to test having the seed being a parameter
  def makeGraphWithSeed(
      seed: Int,
      N: Int,
      S: Int
  ): Graph = {

    val MAX_WEIGHT = 10

    if (S < N - 1 || S > N * (N - 1)) {
      throw new IllegalArgumentException(
        s"""S must be between N-1 and N(N-1) inclusive
            |N: $N
            |S: $S
            |N-1: ${N - 1}
            |N(N-1): ${N * (N - 1)}
            |""".stripMargin
      )
    }

    val randomGen = new scala.util.Random(seed)

    def randomEdgeFromGraphSoFar(g: Map[Int, List[Edge]]) = {
      g.keys.toSeq(randomGen.nextInt(g.size))
    }

    val initialConnectedGraph =
      Range
        .inclusive(1, N - 1)
        .reverse
        .foldLeft((SortedMap(N -> List()): Graph))((acc, node) => {
          acc + (node -> List(
            Edge(randomEdgeFromGraphSoFar(acc), randomGen.nextInt(MAX_WEIGHT))
          ))
        })

    // Add random edges to the graph until the number of edges is equal to S
    // ensure there aren't duplicate edges
    Range
      .inclusive(numEdges(initialConnectedGraph) + 1, S)
      .foldLeft(initialConnectedGraph)((acc, _) => {

        val randomNode = randomEdgeFromGraphSoFar(acc)
        var randomEdge = randomEdgeFromGraphSoFar(acc)

        while (acc(randomNode).exists(edge => edge.node == randomEdge)) {
          randomEdge = randomEdgeFromGraphSoFar(acc)
        }

        val updatedEdges =
          acc(randomNode) :+ Edge(randomEdge, randomGen.nextInt(MAX_WEIGHT))

        acc + (randomNode -> updatedEdges)
      })

  }

  def numEdges(graph: Graph): Int = {
    graph.values.foldLeft(0)((acc, edges) => acc + edges.size)
  }

  // dijsktra's algorithm
  def shortestPath(
      graph: Graph,
      start: Int,
      end: Int
  ): List[Int] = {
    val distances = graph.keys.foldLeft(Map[Int, Int]())((acc, node) => {
      if (node == start) {
        acc + (node -> 0)
      } else {
        acc + (node -> Int.MaxValue)
      }
    })

    var prev =
      graph.keys.foldLeft(Map[Int, Option[Int]]())((acc, node) => {
        acc + (node -> None)
      })

    val queue = Queue().enqueue(start)

    return shortestPathRecur(graph, prev, queue, distances, start, end)
  }

  private def shortestPathRecur(
      graph: Graph,
      prev: Map[Int, Option[Int]],
      queue: Queue[Int],
      distances: Map[Int, Int],
      start: Int,
      end: Int
  ): List[Int] = {
    if (queue.isEmpty) {
      List()
    } else {
      val (currentNode, newQueue) = queue.dequeue

      if (currentNode == end) {
        return getPath(prev, start, end)
      } else {
        val currentDistance = distances(currentNode)

        case class Accum(
            distances: Map[Int, Int],
            queue: Queue[Int],
            prev: Map[Int, Option[Int]]
        )

        val updated =
          graph(currentNode)
            .sortBy(edge => distances(edge.node))
            .foldLeft(Accum(distances, newQueue, prev))((acc, edge) => {

              val newDistance = currentDistance + edge.weight

              if (newDistance < distances(edge.node)) {
                Accum(
                  acc.distances + (edge.node -> newDistance),
                  acc.queue.enqueue(edge.node),
                  acc.prev + (edge.node -> Some(currentNode))
                )
              } else {
                acc
              }
            })

        shortestPathRecur(graph, updated.prev, updated.queue, updated.distances, start, end)
      }
    }
  }

  private def getPath(
      prev: Map[Int, Option[Int]],
      start: Int,
      end: Int
  ): List[Int] = {
    if (start == end) {
      List(start)
    } else {
      getPath(prev, start, prev(end).get) :+ end
    }
  }

  def eccentricity(graph: Graph, node: Int): Int =
    graph.keys
      .filter(n => n != node)
      .foldLeft(0)((acc, otherNode) => {
        val path = shortestPath(graph, node, otherNode)

        val sum = path
          .sliding(2)
          .map(pair =>
            graph(pair(0))
              .find(edge => edge.node == pair(1))
              .get
              .weight
          )
          .sum

        // NOTE: assuming that a single edge is considered a path
        // if we only wanted to consider paths with at least 3 nodes (A -> B -> C)
        // if (path.size < 3) { acc } else { Math.max(acc, sum) }
        Math.max(acc, sum)
      })

  def radius(graph: Graph): Int =
    graph.keys.foldLeft(Int.MaxValue)((acc, node) => {
      val e = eccentricity(graph, node)

      // We are dealing with directed graphs so we can nodes that don't have a path to all other nodes
      if (e == 0) {
        acc
      } else {
        Math.min(acc, e)
      }
    })

  def diameter(graph: Graph): Int =
    graph.keys.foldLeft(0)((acc, node) => {
      Math.max(acc, eccentricity(graph, node))
    })

}
