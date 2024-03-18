package clientTests;

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
    void login() throws Exception {
        facade.register("player1", "password", "p1@email.com");
        var authData = facade.login("player1", "password");
        Assertions.assertTrue(authData.getAuthToken().length() > 10);
    }
    @Test
    void logout() throws Exception {
        facade.register("player1", "password", "p1@email.com");
        facade.logout();
    }

}
