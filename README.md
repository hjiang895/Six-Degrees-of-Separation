# Problem Set 10: Six Degrees of Separation

### Due Thursday, May 2nd @ 11.59pm

---

At long last, you have arrived at the final problem set for CS2! In this problem set, you will be exploring a undirected graph built using data from IMDB, the Internet Movie Database. In this graph, each vertex is an actor, and each edge is movie that unites that actor with another actor. Below is a visualization of a subgraph of this graph:



The graph is implemented as an adjacency list. A `HashMap` object called `people` maps a `String` key (an actor's name) to a value that is an `ArrayList` of `PersonMovie` objects. The `ArrayList` is the adjacency list for that actor. Each `PersonMovie` object in the `ArrayList` stores the name of an actor and a movie that actor was in with the key actor. Thus, for every actor, we know every other actor they starred with and the name of the movie (or movies) that brough them together. This picture might make things a bit clearer. 

Note that the same actor can appear twice in a key's adjacency list if that actor appeared in multiple movies with the key actor. For instance, the adjacency list for Christian Bale would include a `PersonMovie` object for when he starred with Amy Adams in "American Hustle" and a `PersonMovie` object for when he starred with Amy Adams in "The Fighter".

Detailed comments about the implementation can be found in the `SixDegrees.java`.

---

Your tasks, which increase in difficulty from the first to the last, are set out below. I have written the majority of the code for you, and it is well commented. With the exception of task 4, you will mostly be using existing code to answer questions about the graph. 


## Task 1: Max and min degree
When we talk about graphs, we often like to talk about the number of edges coming out of a vertex. This is called the degree of the vertex. Write two methods with the following signatures:

```java
public void maxDegree()
public void minDegree()
```


