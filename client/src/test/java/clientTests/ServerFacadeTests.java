package clientTests;

import model.ListResponse;
import org.junit.jupiter.api.*;
import server.Server;
import ui.ServerFacade;

import java.net.ProtocolException;


public class ServerFacadeTests {

    private static Server server;
    static ServerFacade facade;

    @BeforeAll
    public static void init() throws Exception {
        server = new Server();
        var port = server.run(0);
        System.out.println("Started test HTTP server on " + port);
        facade = new ServerFacade(port);
    }

    @BeforeEach
    void before() {
        server.clear();
    }

    @AfterAll
    static void stopServer() {
        server.stop();
    }


    @Test
    void register() throws Exception {
        var authData = facade.register("player1", "password", "p1@email.com");
        Assertions.assertTrue(authData.getAuthToken().length() > 10);
    }
    @Test
    void badRegister() {
        Assertions.assertThrows(Throwable.class, () -> facade.register(null, "password", "p1@email.com"), "Doesn't throw error");
    }
    @Test
    void login() throws Exception {
        facade.register("player1", "password", "p1@email.com");
        var authData = facade.login("player1", "password");
        Assertions.assertTrue(authData.getAuthToken().length() > 10);
    }
    @Test
    void badLogin() {
        Assertions.assertThrows(Throwable.class, () -> facade.login(null, "password"), "Doesn't throw error");
    }
    @Test
    void logout() throws Exception {
        facade.register("player1", "password", "p1@email.com");
        Assertions.assertDoesNotThrow(() -> facade.logout(), "Throws an error");

    }
    @Test
    void badLogout() {
        Assertions.assertThrows(Throwable.class, () -> facade.logout(), "Doesn't throw error");
    }
    @Test
    void createGame() throws Exception {
        facade.register("player1", "password", "p1@email.com");
        int response = Integer.parseInt(facade.createGame("testGame"));
        Assertions.assertTrue(response > 0, "Bad gameID" + response);
    }
    @Test
    void badCreateGame() {
        Assertions.assertThrows(Throwable.class, () -> facade.createGame("testGame"), "Doesn't throw error");
    }
    @Test
    void listGames() throws Exception {
        facade.register("player1", "password", "p1@email.com");
        facade.createGame("testGame1");
        facade.createGame("testGame2");
        facade.createGame("testGame3");
        ListResponse response = facade.listGames();
        Assertions.assertEquals(3, response.games.length, "List lengths did not match");
    }
    @Test
    void badlistGames() {
        Assertions.assertThrows(Throwable.class, () -> facade.listGames(), "Doesn't throw error");
    }

    @Test
    void joinGame() throws Exception {
        facade.register("player1", "password", "p1@email.com");
        facade.createGame("testGame1");
        facade.createGame("testGame2");
        facade.createGame("testGame3");
        facade.listGames();
        Assertions.assertDoesNotThrow(() -> facade.joinGame("WHITE", 2));
    }
    @Test
    void badJoinGame() {
        Assertions.assertThrows(Throwable.class, () -> facade.joinGame("WHITE", 2), "Doesn't throw error");
    }

    @Test
    void observeGame() throws Exception {
        facade.register("player1", "password", "p1@email.com");
        facade.createGame("testGame1");
        facade.createGame("testGame2");
        facade.createGame("testGame3");
        facade.listGames();
        Assertions.assertDoesNotThrow(() -> facade.observeGame( 2));
    }
    @Test
    void badObserveGame() {
        Assertions.assertThrows(Throwable.class, () -> facade.observeGame( 2), "Doesn't throw error");
    }

}
