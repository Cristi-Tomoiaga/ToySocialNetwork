package toysocialnetwork.toysocialnetworkfx.service.graph;

import toysocialnetwork.toysocialnetworkfx.domain.User;

import java.util.*;

/**
 * Models a graph of users and provides a few algorithms on the graph
 */
public class UserGraph {
    private final Map<User, List<User>> adjacencyList;
    private final Map<User, Integer> visitedComponents;
    private final Map<User, Boolean> visited;
    private final Map<User, Integer> distances;
    private int maxPathLength;

    /**
     * Constructor for UserGraph
     *
     * @param users the users to be added in the graph
     */
    public UserGraph(Iterable<User> users) {
        adjacencyList = new HashMap<>();
        visitedComponents = new HashMap<>();
        visited = new HashMap<>();
        distances = new HashMap<>();

        List<User> usersList = new ArrayList<>();
        users.forEach(usersList::add);

        buildAdjacencyList(usersList);
    }

    /**
     * Builds the adjacency list using the users provided
     *
     * @param users the list of users
     */
    private void buildAdjacencyList(List<User> users) {
        users.forEach(user -> adjacencyList.put(user, new ArrayList<>()));

        users.forEach(user -> {
            List<User> userList = adjacencyList.get(user);

            for (Long uid : user.getFriends()) {
                User u = users.stream().filter(x -> x.getId().equals(uid)).toList().get(0);
                userList.add(u);
            }
        });
    }

    /**
     * Depth first search algorithm
     *
     * @param currentUser    the current user visited by dfs
     * @param componentIndex the index of the current component
     */
    private void DFS(User currentUser, int componentIndex) {
        visitedComponents.put(currentUser, componentIndex);

        for (User other : adjacencyList.get(currentUser))
            if (visitedComponents.get(other) == 0)
                DFS(other, componentIndex);
    }

    /**
     * Finds all the connected components
     *
     * @return a list of connected components (graphs)
     */
    public List<UserGraph> getConnectedComponents() {
        Set<User> userSet = adjacencyList.keySet();
        userSet.forEach(user -> visitedComponents.put(user, 0));

        int currentComponentIndex = 0;
        for (User user : userSet)
            if (visitedComponents.get(user) == 0) {
                currentComponentIndex++;
                DFS(user, currentComponentIndex);
            }

        List<UserGraph> components = new ArrayList<>();
        for (int index = 1; index <= currentComponentIndex; index++) {
            int finalIndex = index;

            UserGraph component = new UserGraph(visitedComponents.keySet().
                    stream().filter(x -> visitedComponents.get(x).equals(finalIndex)).toList());
            components.add(component);
        }

        return components;
    }

    /**
     * Generates the longest paths in the graph starting from a given user
     *
     * @param currentUser the current user
     */
    private void generateLongestPaths(User currentUser) {
        visited.put(currentUser, true);

        boolean hasVisitedNeighbors = false;
        for (User other : adjacencyList.get(currentUser))
            if (!visited.get(other)) {
                hasVisitedNeighbors = true;
                distances.put(other, distances.get(currentUser) + 1);

                generateLongestPaths(other);
            }

        if (!hasVisitedNeighbors && distances.get(currentUser) > maxPathLength)
            maxPathLength = distances.get(currentUser);

        visited.put(currentUser, false);
    }

    /**
     * Finds the length of the longest path in the graph
     *
     * @return the length of the path
     */
    public int getMaxPathLength() {
        List<User> users = new ArrayList<>(adjacencyList.keySet());

        maxPathLength = 0;
        for (User u : users) {
            users.forEach(user -> visited.put(user, false));
            users.forEach(user -> distances.put(user, 0));

            generateLongestPaths(u);
        }

        return maxPathLength;
    }

    /**
     * Gets the connected component with the longest maximum path
     *
     * @return the connected component (graph)
     */
    public UserGraph getConnectedComponentMaxPath() {
        List<UserGraph> connectedComponents = getConnectedComponents();

        int maxLength = -1;
        UserGraph maxComponent = null;
        for (UserGraph component : connectedComponents) {
            int length = component.getMaxPathLength();

            if (length > maxLength) {
                maxLength = length;
                maxComponent = component;
            }
        }

        return maxComponent;
    }

    /**
     * Gets all the users stored in the graph
     *
     * @return the users
     */
    public Iterable<User> getUsers() {
        return adjacencyList.keySet();
    }
}
