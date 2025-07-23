# Campus Paths

This project is a complete campus pathfinding and mapping tool for the University of Washington. It includes both a web application and a set of Java tools for exploring and visualizing the shortest paths between campus buildings.

## Features

- **Interactive Web Map:**
  Find and visualize the shortest walking path between any two buildings on the UW Seattle campus. The map is fully interactive and responsive.

- **Java Backend:**
  A Spark server provides the backend API for pathfinding, using Dijkstra’s algorithm on real campus data.

- **Text-Based Tools:**
  Includes a console-based pathfinder and other utilities for working with campus and Marvel universe graphs.

- **Polynomial Calculator:**
  A GUI calculator for working with rational polynomials.

## Getting Started

### Web Application

1. **Start the backend server:**
   ```sh
   ./gradlew :hw-campuspaths-server:runSpark
   ```
   This will start the Java backend on port 4567.

2. **Start the frontend:**
   ```sh
   cd hw-campuspaths
   npm install
   npm start
   ```
   The React app will open at [http://localhost:3000](http://localhost:3000).

### Java Console Tools

- **Pathfinder (text interface):**
  ```sh
  ./gradlew :hw-pathfinder:runPathfinder
  ```

- **Marvel Paths:**
  ```sh
  ./gradlew :hw-marvel:runMarvel
  ```

- **Polynomial Calculator GUI:**
  ```sh
  ./gradlew :hw-poly:runCalculator
  ```

### Running All Tests

To run all tests for all modules:
```sh
./gradlew build
```

## Project Structure

- `hw-campuspaths/` — React frontend for the campus map
- `hw-campuspaths-server/` — Java backend (Spark server)
- `hw-pathfinder/` — Java pathfinding tools and algorithms
- `hw-marvel/` — Marvel universe graph tools
- `hw-poly/` — Polynomial calculator (GUI and logic)
- `hw-graph/` — Core graph data structures and algorithms

## Notes

- The map uses OpenStreetMap and Leaflet for rendering.
- All code is original and intended for educational use at the University of Washington.

## License

This project is for educational purposes only.

---

If you have any questions or want to contribute, feel free to open an issue or pull request!