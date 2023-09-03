package hexlet.code;

import hexlet.code.controlles.RootController;
import hexlet.code.controlles.UrlsController;
import io.javalin.Javalin;
//import io.javalin.plugin.rendering.template.JavalinThymeleaf;
import io.javalin.rendering.template.JavalinThymeleaf;
import nz.net.ultraq.thymeleaf.layoutdialect.LayoutDialect;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.extras.java8time.dialect.Java8TimeDialect;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;

import static io.javalin.apibuilder.ApiBuilder.path;
import static io.javalin.apibuilder.ApiBuilder.post;
import static io.javalin.apibuilder.ApiBuilder.get;

public class App {

    private static int getPort() {
        String port = System.getenv().getOrDefault("PORT", "5000");
        return Integer.parseInt(port);
    }

    private static String getMode() {
        return System.getenv().getOrDefault("APP_ENV", "development");
    }

    private static boolean isProduction() {
        return getMode().equals("production");
    }

    private static void addRoutes(Javalin app) {
        app.get("/", RootController.INDEX);
        app.routes(() -> {
            path("/urls", () -> {
                get(UrlsController.LIST_URLS);
                post(UrlsController.CREATE_URL);
                path("{id}", () -> {
                    get(UrlsController.SHOW_URL);
                    path("/checks", () -> {
                        post(UrlsController.CHECK_URL);
                    });
                });
            });
        });
    }

    private static TemplateEngine getTemplateEngine() {
        TemplateEngine templateEngine = new TemplateEngine();
        templateEngine.addDialect(new LayoutDialect());
        templateEngine.addDialect(new Java8TimeDialect());
        ClassLoaderTemplateResolver templateResolver = new ClassLoaderTemplateResolver();
        templateResolver.setPrefix("/templates/");
        templateResolver.setCharacterEncoding("UTF-8");
        templateEngine.addTemplateResolver(templateResolver);

        return templateEngine;
    }

    public static Javalin getApp() {
        Javalin app = Javalin.create(config -> {
            if (!isProduction()) {
//                config.enableDevLogging();
                config.plugins.enableDevLogging();
            }
//            config.enableWebjars();
//            JavalinThymeleaf.configure(getTemplateEngine());
            JavalinThymeleaf.init(getTemplateEngine());
        });
        addRoutes(app);
        app.before(ctx -> {
            ctx.attribute("ctx", ctx);
        });
        return app;
    }
    public static void main(String[] args) {
        Javalin app = getApp();
        app.start(getPort());
    }
}
