package hexlet.code.controlles;

import io.javalin.http.Handler;

public class RootController {
    public static Handler welcome = ctx -> {
        ctx.render("/layouts/application.html");
    };
}
