package hexlet.code.controlles;

import hexlet.code.model.UrlModel;
import hexlet.code.model.query.QUrlModel;
import io.ebean.PagedList;
import io.javalin.core.validation.JavalinValidation;
import io.javalin.core.validation.ValidationError;
import io.javalin.core.validation.Validator;
import io.javalin.http.Handler;
import io.javalin.http.NotFoundResponse;
import org.apache.commons.validator.routines.UrlValidator;

import java.net.URL;
import java.util.List;
import java.util.Map;

public class UrlsController {
    private static final int ROWS_PER_PAGE = 10;
    private static final int STATUS_INCORRECT_URL = 422;

    public static final Handler LIST_URLS = ctx -> {
        int page = ctx.queryParamAsClass("page", Integer.class).getOrDefault(1);
        int offset = (page - 1) * ROWS_PER_PAGE;
        PagedList<UrlModel> pagedUrl = new QUrlModel()
                .setFirstRow(offset)
                .setMaxRows(ROWS_PER_PAGE)
                .orderBy()
                .id.asc()
                .findPagedList();

        List<UrlModel> urls = pagedUrl.getList();

        ctx.attribute("urls", urls);
        ctx.attribute("page", page);
        ctx.render("/urls/index.html");
    };

    public static final Handler CREATE_URL = ctx -> {

        var inputContent = ctx.formParam("url");
        assert inputContent != null;
        inputContent = inputContent.strip();
        UrlValidator urlValidator = new UrlValidator();
        Validator<String> validator = ctx.formParamAsClass("url", String.class)
                .check(x -> urlValidator.isValid(x.strip()), "Некорректный URL");
        Map<String, List<ValidationError<? extends Object>>> errors = JavalinValidation.collectErrors(validator);
        if (!errors.isEmpty()) {
            ctx.status(STATUS_INCORRECT_URL);
            ctx.sessionAttribute("flash", "Некорректный URL");
            ctx.redirect("/");
            return;
        }
        URL inputUrl = new URL(inputContent);
        var port = inputUrl.getPort() == -1 ? "" : ":" + inputUrl.getPort();
        var correctUrl = inputUrl.getProtocol() + "://" + inputUrl.getHost() + port;
        UrlModel isCorrectUrl = new QUrlModel()
                .url.equalTo(correctUrl)
                .findOne();
        if (isCorrectUrl != null) {
            ctx.status(STATUS_INCORRECT_URL);
            ctx.sessionAttribute("flash", "Страница уже существует");
            ctx.redirect("/urls");
            return;
        }
        UrlModel urlModel = new UrlModel(correctUrl, 1);
        urlModel.save();
        ctx.attribute("flash", "Страница успешно добавлена");
        ctx.attribute("url", urlModel);
        ctx.redirect("/urls");
    };

    public static final Handler SHOW_URL = ctx -> {
        var inputId = ctx.pathParamAsClass("id", Integer.class).getOrDefault(null);
        UrlModel urlModel = new QUrlModel()
                .id.equalTo(inputId)
                .findOne();
        if (urlModel == null) {
            throw new NotFoundResponse();
        }
        ctx.attribute("url", urlModel);
        ctx.render("/urls/showUrl.html");
    };

    public static final Handler CHECKS_URL = ctx -> {

    };
}
