package hexlet.code;

import hexlet.code.model.UrlCheck;
import hexlet.code.model.UrlModel;
import hexlet.code.model.query.QUrlCheck;
import hexlet.code.model.query.QUrlModel;
import io.ebean.DB;
import io.ebean.Database;
import io.javalin.Javalin;
import kong.unirest.HttpResponse;
import kong.unirest.Unirest;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class TestApp {
    private static Javalin app;
    private static String baseUrl;
    private static Database database;
    private static final int CODE_200 = 200;
    private static final int CODE_302 = 302;


    @BeforeAll
    public static void beforeAll() {
        app = App.getApp();
        app.start(0);
        int port = app.port();
        baseUrl = "http://localhost:" + port;
        database = DB.getDefault();
    }
    @AfterAll
    public static void afterAll() {
        app.stop();
    }

    @BeforeEach
    final void beforeEach() {
        database.script().run("/truncate.sql");
        database.script().run("/seed-test-db.sql");
    }

    @Nested
    class RootTest {

        @Test
        void testIndex() {
            HttpResponse<String> response = Unirest.get(baseUrl).asString();
            String body = response.getBody();
            assertThat(response.getStatus()).isEqualTo(CODE_200);
            assertThat(body).contains("Анализатор страниц");
        }
    }

    @Nested
    class UrlTest {

        @Test
        void testUrlList() {
            HttpResponse<String> response = Unirest.get(baseUrl + "/urls").asString();
            String body = response.getBody();
            assertThat(response.getStatus()).isEqualTo(CODE_200);
            assertThat(body).contains("https://habr.com");
        }

        @Test
        void testCreateUrl() {
            String url = "https://www.example.com";
            HttpResponse<String> responsePost = Unirest
                    .post(baseUrl + "/urls")
                    .field("url", url)
                    .asString();

            assertThat(responsePost.getStatus()).isEqualTo(CODE_302);
            assertThat(responsePost.getHeaders().getFirst("Location")).isEqualTo("/urls");

            HttpResponse<String> response = Unirest
                    .get(baseUrl + "/urls")
                    .asString();
            String body = response.getBody();

            assertThat(response.getStatus()).isEqualTo(CODE_200);
            assertThat(body).contains(url);
            assertThat(body).contains("Страница успешно добавлена");

            UrlModel actualUrl = new QUrlModel()
                    .url.equalTo(url)
                    .findOne();

            assertThat(actualUrl).isNotNull();
            assertThat(actualUrl.getUrl()).isEqualTo(url);
        }

        @Test
        void testIncorrectUrl() {
            String url = "test";
            HttpResponse<String> responsePost = Unirest
                    .post(baseUrl + "/urls")
                    .field("url", url)
                    .asString();

            assertThat(responsePost.getHeaders().getFirst("Location")).isEqualTo("/");

            HttpResponse<String> response = Unirest
                    .get(baseUrl + "/")
                    .asString();
            String body = response.getBody();

            assertThat(body).contains("Некорректный URL");
        }

        @Test
        void addExistingUrl() {
            HttpResponse<String> responsePost1 = Unirest
                    .post(baseUrl + "/urls")
                    .field("url", "https://ru.hexlet.io")
                    .asString();

            assertThat(responsePost1.getHeaders().getFirst("Location")).isEqualTo("/urls");

            HttpResponse<String> response = Unirest
                    .get(baseUrl + "/urls")
                    .asString();
            String body = response.getBody();

            assertThat(body).contains("https://ru.hexlet.io");
            assertThat(body).contains("Страница уже существует");
        }

        @Test
        void showUrl() {
            HttpResponse<String> response = Unirest
                    .get(baseUrl + "/urls/" + 1)
                    .asString();
            String body = response.getBody();

            assertThat(response.getStatus()).isEqualTo(CODE_200);
            assertThat(body).contains("https://habr.com");
        }

        @Test
        void testCheck() throws IOException {
            var path = "src/test/resources/fixtures/index.html";
            var mockServer = new MockWebServer();
            var content = Files.readString(Paths.get(path));
            var mockedResponse = new MockResponse().setBody(content);
            mockServer.enqueue(mockedResponse);
            mockServer.start();
            String sampleUrl = mockServer.url("/").toString();
            HttpResponse<String> response = Unirest
                    .post(baseUrl + "/urls/")
                    .field("url", sampleUrl)
                    .asString();

            UrlModel url = new QUrlModel()
                    .url.equalTo(sampleUrl.substring(0, sampleUrl.length() - 1))
                    .findOne();
            assertThat(url).isNotNull();

            HttpResponse<String> response1 = Unirest
                    .post(baseUrl + "/urls/" + url.getId() + "/checks")
                    .asString();

            assertThat(response1).isNotNull();

            HttpResponse<String> response2 = Unirest
                    .get(baseUrl + "/urls/" + url.getId())
                    .asString();

            assertThat(response2).isNotNull();
            assertThat(response2.getStatus()).isEqualTo(CODE_200);

            UrlCheck check = new QUrlCheck()
                    .findList().get(0);

            assertThat(check).isNotNull();
            assertThat(check.getUrl().getId()).isEqualTo(url.getId());
            System.out.println(response2.getBody());
            assertThat(response2.getBody()).contains("correct title");
            assertThat(response2.getBody()).contains("correct description");
            assertThat(response2.getBody()).contains("correct header");
            mockServer.shutdown();
        }
    }
}
