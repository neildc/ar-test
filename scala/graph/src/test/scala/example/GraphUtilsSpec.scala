package example

import munit.FunSuite
import example.GraphUtils._
import scala.collection.immutable.SortedMap

class GraphTest extends FunSuite {
  test("numEdges of graph") {
    val graph = SortedMap(
      1 -> List(Edge(2, 1)),
      2 -> List(Edge(3, 1), Edge(4, 1)),
      3 -> List(Edge(4, 1)),
      4 -> List()
    )
    val edgeCount = numEdges(graph)
    assertEquals(edgeCount, 4)
  }

  test("makeGraphWithSeed generates a graph with correct number of edges") {
    val N = 5
    val S = 7
    val seed = 42
    val graph = makeGraphWithSeed(seed, N, S)
    pprint.pprintln(graph)
    val edgeCount = numEdges(graph)
    assert(edgeCount == S)
  }

  val simpleGraph = SortedMap(
    1 -> List(Edge(2, 1)),
    2 -> List(Edge(3, 1)),
    3 -> List(Edge(4, 1)),
    4 -> List()
  )

  test("shortestPath finds correct path in a simple graph") {
    val path = shortestPath(simpleGraph, 1, 4)
    assertEquals(path, List(1, 2, 3, 4))
  }

  test("eccentricity calculates maximum distance correctly") {
    val ecc = eccentricity(simpleGraph, 1)
    assertEquals(ecc, 3)
  }

  test("radius calculates minimum eccentricity") {
    val rad = radius(simpleGraph)
    assertEquals(rad, 1)
  }

  test("diameter calculates maximum eccentricity") {
    val dia = diameter(simpleGraph)
    assertEquals(dia, 3)
  }

  val biggerGraph = SortedMap(
    1 -> List(Edge(2, 1), Edge(3, 1)),
    2 -> List(Edge(4, 2)),
    3 -> List(Edge(4, 3)),
    4 -> List(Edge(5, 3)),
    5 -> List(Edge(6, 2), Edge(7, 1)),
    6 -> List(),
    7 -> List()
  )

  test("Shortest path between ") {
    val path = shortestPath(biggerGraph, 1, 6)
    assertEquals(path, List(1, 2, 4, 5, 6))
  }

  test("Eccentricity") {
    val e = eccentricity(biggerGraph, 4)
    assertEquals(e, 5)
  }

  test("Diameter") {
    val dia = diameter(biggerGraph)
    assertEquals(dia, 8)
  }

  test("Radius") {
    val rad = radius(biggerGraph)
    assertEquals(rad, 2)
  }

}
