package hexlet.code.controlles;

import io.javalin.http.Handler;

public final class RootController {
    public static final Handler INDEX = ctx -> {
        ctx.render("index.html");
    };
}
