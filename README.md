Blackjack Game Application
This project is a Blackjack Game Application implemented in Java. The application simulates a card game of Blackjack, where players compete against a dealer. The game is designed to be modular and extendable, allowing easy addition of features and enhancements.

Features
- Core Gameplay: Supports basic Blackjack rules.
- Playeres vs Dealer: Play against the dealer with realistic card mechanics.

Game Rules
- Each player is dealt two cards to start.
- Players can choose to hit (draw a card) or stand (keep their current hand).
- The dealer must draw until reaching a minimum score (e.g., 17).
- The goal is to get as close to 21 as possible without exceeding it.

Tech Stack
- Language: Java (version 17 or higher)
- Build Tool: Maven
- Testing: JUnit for unit and integration tests
- Documentation: Swagger (OpenAPI)
    - http://localhost:8080/swagger-ui.html


Getting Started
  - Prerequisites
    Ensure you have the following installed:
    - Java Development Kit (JDK) (version 17 or higher)
    - Maven

  - Setup Instructions
    Clone the repository:
    - git clone https://github.com/paumaneja/5.1-Spring_Framework_Advanced.Webflux_-Blackjack-.git
    - cd 5.1-Spring_Framework_Advanced.Webflux_-Blackjack-

  - Build the project:
    - mvn clean install

  - Run the application:
    - mvn exec:java -Dexec.mainClass="cat.itacademy.s05.t01.n01.S05T01N01Application"

  - Run the tests:
    - mvn test

Project Structure
  - src/main/java: Contains the main application code.
  - src/main/resources: Configuration files and assets.
  - src/test/java: Unit and integration tests.

Future Enhancements
  - Implement advanced Blackjack rules (e.g., splitting, doubling down).
  - Add graphical user interface (GUI) or web-based client.
  - Enhance AI for dealer strategies.

Contributing
  Contributions are welcome! Follow these steps:

  - Fork the repository.
    Create a feature branch: git checkout -b feature-name.
  - Commit your changes: git commit -m 'Add feature description'.
    Push to the branch: git push origin feature-name.
  - Submit a pull request.
