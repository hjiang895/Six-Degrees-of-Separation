import java.io.*;
import java.util.*;

public class SixDegrees {

  // ------------------------------
  // INSTANCE VARIABLE: people HashMap
  // ------------------------------
  // This HashMap stores the graph itself.
  // Each key is a String corresponding to an actor's name.
  // Each value is an ArrayList of PersonMovie objects.
  // This ArrayList is how we store the adjacency list for the current key.
  // Each PersonMovie object contains the name of an actor and a
  // movie that actor was in with the actor in the key.
  HashMap<String, ArrayList<PersonMovie>> people;


  // ------------------------------
  // INNER CLASS FOR ADJACENCY LIST
  // ------------------------------
  // The PersonMovie class stores an actor
  // and one movie that actor was in.
  public class PersonMovie {
    String person;
    String movie;

    public PersonMovie(String p, String m) {
      this.person = p;
      this.movie = m;
    }
  }

  // ------------------------------
  // CONSTRUCTOR
  // ------------------------------
  // Constructor - initializes the HashMap for storing the graph.
  public SixDegrees() {
    this.people = new HashMap<String, ArrayList<PersonMovie>>();
  }

  // ------------------------------
  // METHOD FOR READING IN IMDB
  // ------------------------------
  // This method reads in the IMDB file and creates the graph.
  // The IMDB file has the 999 most popular movies from 2006-2016.
  public void populateGraph(String s) throws IOException {

    // read in the file line by line
    BufferedReader br = new BufferedReader(new FileReader(s));
    String line;
    while ((line = br.readLine()) != null) {

      // split the line on tab
      String[] parts = line.split("\\t+");

      // the first element is the movie
      String movie = parts[0];

      // the second element is a comma-sepaated list of actors
      String[] actors = parts[1].split(",");

      // add an edge between each actor and every other actor
      for (int i = 0; i < actors.length-1; i++) {
        for (int j=i+1; j < actors.length; j++) {
          addEdge(actors[i], actors[j], movie);
        }
      }
    }
  }


  // -------------------------------------
  // METHODS FOR CREATING NODES AND EDGES
  // -------------------------------------
  // This method just creates a "node", i.e., an entry in the
  // HashMap with an actor as the key and an empty adjacency list.
  public void createNode(String a) {
    ArrayList<PersonMovie> ll = new ArrayList<PersonMovie>();
    people.put(a, ll);
  }

  // This method adds an edge to actor a's list to actor b in the selected movie.
  // And it adds an edge to actor b's list to actor a in the selected movie.
  public void addEdge(String a, String b, String movie) {

    // if neither actor is in the graph yet, create a vertex for them
    if (!people.containsKey(a)) {
      createNode(a);
    }
    if (!people.containsKey(b)) {
      createNode(b);
    }

    // get the current adjacency list for actor a, and add a new
    // PersonMovie object to it for person b and the selected movie
    ArrayList<PersonMovie> ll = people.get(a);
    PersonMovie pm = new PersonMovie(b, movie);
    ll.add(pm);
    people.put(a, ll);

    // get the current adjacency list for actor b, and add a new
    // PersonMovie object to it for person a and the selected movie
    ArrayList<PersonMovie> ll2 = people.get(b);
    pm = new PersonMovie(a, movie);
    ll2.add(pm);
    people.put(b, ll2);

  }


  // -------------------------------------
  // RANDOM WALK METHOD
  // -------------------------------------
  // This method performs a random walk of length=steps,
  // starting at actor a, and returns the final actor.
  public ArrayList<String> randomWalk(String a, int steps) {
    ArrayList<String> visitedActors = new ArrayList<String>();
    // while the number of steps is greater than 0
    while (steps > 0) {
      steps--;

      // Get a random person from the adjacency list.
      ArrayList<PersonMovie> al = people.get(a);
      int rando = (int)(Math.random() * al.size());
      PersonMovie pm = al.get(rando);

      // Print out the relationship.
      visitedActors.add(pm.person);

      // Move on to that person, and keep walking.
      a = pm.person;
    }
    return visitedActors;
  }

  // --------------------------------
  //     YOUR METHODS GO HERE!!!
  // --------------------------------

  // TASK 1: print out the actor(s) with the maximum degree
  // signature: public void maxDegree()
  // Instructions are in the problem set.

  public void maxDegree(){
    // Initializes maxKey with null and max with an impossible value
    String maxKey = null;
    int max = -1;
    for(String actor : people.keySet()){
      // On first iteration, sets maxKey/max to values from first actor
      if (max == -1) {
        max = people.get(actor).size();
        maxKey = actor;
      }
      // After first iteration, changes maxKey/max only if degree exceeds current min
      else if(people.get(actor).size() > max){
        max = people.get(actor).size();
        maxKey = actor;
      }
    }
    System.out.println(maxKey);
    System.out.println(max);
  }

  // TASK 1: print out the actor(s) with the minimum degree
  // signature: public void minDegree()
  // Instructions are in the problem set.
  public void minDegree(){
    // Initializes minKey with null and min with an impossible value
    String minKey = null;
    int min = -1;
    for(String actor : people.keySet()){
      // On first iteration, sets minKey/min to values from first actor
      if (min == -1) {
        min = people.get(actor).size();
        minKey = actor;
      }
      // After first iteration, changes minKey/min only if degree is below the current min
      else if(people.get(actor).size() <= min){
        min = people.get(actor).size();
        minKey = actor;
      }
    }
    System.out.println(minKey);
    System.out.println(min);
  }

  // TASK 2: print out the 5 most popular actors based on number of visits in a random walk
  // signature: public void mostPopular()
  // Instructions are in the problem set.
  // Note: this code will pick a random actor from the people HashMap and save it to key.
  //       ArrayList<String> allActors = new ArrayList<String>(people.keySet());
  //       String key = allActors.get(new Random().nextInt(allActors.size()));
  public void mostPopular(){
    HashMap<String, Integer> actorCount = new HashMap<String, Integer>();

    // Adds every actor to a new HashMap with an initial number of visits as 0
    for(String k: people.keySet()){
      actorCount.put(k, 0);
    }
    // Runs 10000 random walks and adds 1 to every reached actor's value in acterCount
    for(int count = 1; count <= 10000; count++){
      ArrayList<String> allActors = new ArrayList<String>(people.keySet());
      String key = allActors.get(new Random().nextInt(allActors.size()));
      ArrayList<String> reachedActor = randomWalk(key, 100);

      for (String reached : reachedActor) {
        actorCount.replace(reached, actorCount.get(reached) + 1);
      }
    }

    // Finds the five most reached actors
    for (int removed = 0; removed < 5; removed++) {
      int max = 0;
      String maxKey = "";
      // Iterates through all remaining actors to find the most reached remaining actor
      for (String actor : actorCount.keySet()) {
        if (actorCount.get(actor) > max) {
          maxKey = actor;
          max = actorCount.get(actor);
        }
      }
      // Prints the name of the actor and removes them from the HashMap
      System.out.println(maxKey);
      actorCount.remove(maxKey);
    }
  }

  // TASK 3: Find the shortest path between two actors using breadth-first search.
  // You need to print out the full path from actor a to actor b.
  // You also need to keep track of the length of the path.
  // The algorithm is set out for you below.
  public void findShortestPath(String a, String b) {
    HashMap<String, PersonMovie> camefrom = new HashMap<String, PersonMovie>();
    LinkedList<String> queue = new LinkedList<String>();

    queue.add(a);

    while (queue.size() != 0) {
      // poll() off the actor at the front of the queue.
      String current = queue.poll();
      // Get the adjacency list for that actor.
      ArrayList<PersonMovie> al = people.get(current);
      // For each PersonMovie in the adjacency list...
      for (PersonMovie pm : al) {
        // If the actor in the PersonMovie is the actor, b, you are looking for.
        if (pm.person.equals(b)) {
          String person = current;
          ArrayList<String> path = new ArrayList<String>();
          // Add the last and second-to-last value to the path, then
          // iterate through and add the rest.
          path.add(0, b);
          path.add(0, person);
          int length = 2;
          while (!person.equals(a)) {
            person = camefrom.get(person).person;
            path.add(0, person);
            length++;
          }
          // Print out the path to b as well as its length.
          System.out.println("Length of path " + length);
          for (String s : path) {
            System.out.println(s);
          }
          return;
        }
        else {
          // If that actor has not already been visited and is not in the queue...
          if (!queue.contains(pm.person) && !camefrom.containsKey(pm.person)) {
            // Add that actor to the queue.
            queue.add(pm.person);
            // And add that actor to camefrom with the current actor
            // and the movie that they were in together as the value.
            camefrom.put(pm.person, new PersonMovie(current, pm.movie));
          }
        }
      }
    }
    // If you end up with an empty queue and no match, there was no path.
    return;
  }



  // --------------------------------
  // MAIN METHOD
  // --------------------------------
  public static void main(String[] args) throws IOException {
    SixDegrees sd = new SixDegrees();
    sd.populateGraph(args[0]);
    sd.randomWalk("Kevin Bacon", 5);

    // -------------------------------------------
    // UNCOMMENT THESE TO TEST YOUR IMPLEMENTATION
    // -------------------------------------------
    System.out.println("Max degree: ");
    sd.maxDegree();
    System.out.println();

    System.out.println("Min degree: ");
    sd.minDegree();
    System.out.println();

    System.out.println("Most popular: ");
    sd.mostPopular();
    System.out.println();

    System.out.println("Shortest path: ");
    sd.findShortestPath("Pablo Schreiber", "Sarah Clarke");
    System.out.println();

    // ---------------------------
    // CODE FOR TASK 4 GOES HERE!
    // ---------------------------
    System.out.println("Longest shortest path: ");
    sd.findShortestPath("Dong-seok Ma", "Kim Cattrall");
    System.out.println();
  }
}
