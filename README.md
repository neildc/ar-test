# Submission

This was mainly done in Scala while poking around in Clojure as I went along.

I tried to limit the amount of Scala specific features to what I thought would allow ChatGPT to easily translate into Clojure.

The main limitation of this was avoiding using a priority queue to have a more efficient shortestPath function given I wasn't confident that the Scala implementation would translate well given it uses features unique to Scala (implicit instances for defining how items in the PQ are prioritised). 

It is also a mutable datastructure in Scala, my gut feeling was that sticking to pure functions would allow the LLM translation to be less troublesome, especially when I'm quite unfamiliar with the semantics of how mutation works in Clojure and if there was an equivalent datastructure available.

Overall it seems to have translated decently, even the tests were ported and they seem to be passing. The test suite is definitely quite lacking though.

If this was actually needed for production it probably would be more sensible to write it closer to how the algorithm is more commonly documented with mutations and more imperative control flow, given there's less chance of getting the algorithm wrong and it'll probably have documented optimisations/variations that other people have thought about already (if that is needed).

