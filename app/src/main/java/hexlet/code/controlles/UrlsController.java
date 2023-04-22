package hexlet.code.controlles;

import hexlet.code.model.UrlCheck;
import hexlet.code.model.UrlModel;
import hexlet.code.model.query.QUrlModel;
import io.ebean.PagedList;
import io.javalin.core.validation.JavalinValidation;
import io.javalin.core.validation.ValidationError;
import io.javalin.core.validation.Validator;
import io.javalin.http.Handler;
import io.javalin.http.NotFoundResponse;
import kong.unirest.UnirestException;
import org.apache.commons.validator.routines.UrlValidator;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import kong.unirest.HttpResponse;
import kong.unirest.Unirest;


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
        var correctUrl = getNormalizedUrl(inputContent);
//        URL inputUrl;
//        inputUrl = new URL(inputContent);
//        var port = inputUrl.getPort() == -1 ? "" : ":" + inputUrl.getPort();
//        var correctUrl = inputUrl.getProtocol() + "://" + inputUrl.getHost() + port;
//        UrlValidator urlValidator = new UrlValidator();
//        Validator<String> validator = ctx.formParamAsClass("url", String.class)
//                .check(x -> urlValidator.isValid(x.strip()), "Некорректный URL");
//        Map<String, List<ValidationError<? extends Object>>> errors = JavalinValidation.collectErrors(validator);
        if (correctUrl.isEmpty()) {
//          if (inputContent.isEmpty()) {
            ctx.status(STATUS_INCORRECT_URL);
            ctx.sessionAttribute("flash", "Некорректный URL");
            ctx.sessionAttribute("alert", "alert alert-danger");
            ctx.redirect("/");
            return;
        }
//        URL inputUrl = new URL(inputContent);
//        var port = inputUrl.getPort() == -1 ? "" : ":" + inputUrl.getPort();
//        var correctUrl = inputUrl.getProtocol() + "://" + inputUrl.getHost() + port;
        UrlModel isCorrectUrl = new QUrlModel()
                .url.equalTo(correctUrl)
                .findOne();
        if (isCorrectUrl != null) {
            ctx.status(STATUS_INCORRECT_URL);
            ctx.sessionAttribute("flash", "Страница уже существует");
            ctx.sessionAttribute("alert", "alert alert-info");
            ctx.redirect("/urls");
            return;
        }
        UrlModel urlModel = new UrlModel(correctUrl);
        urlModel.save();
        ctx.sessionAttribute("flash", "Страница успешно добавлена");
        ctx.sessionAttribute("alert", "alert alert-success");
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
        List<UrlCheck> checks = urlModel.getUrlChecks();
        Collections.reverse(checks);
        ctx.attribute("url", urlModel);
        ctx.attribute("checks", checks);
        ctx.render("/urls/showUrl.html");
    };

    public static final Handler CHECK_URL = ctx -> {
        int id = ctx.pathParamAsClass("id", Integer.class).getOrDefault(null);

        UrlModel url = new QUrlModel()
                .id.equalTo(id)
                .findOne();
        if (url == null) {
            throw new NotFoundResponse();
        }
        try {
            HttpResponse<String> response = Unirest
                    .get(url.getUrl())
                    .asString();
            int statusCode = response.getStatus();
            String title = "title";
            String h1 = "h1";
            String description = "description";
            UrlCheck check = new UrlCheck(statusCode, title, h1, description, url);
            check.save();

            ctx.sessionAttribute("flash", "Страница успешно проверена");
            ctx.sessionAttribute("alert", "alert alert-success");
        } catch (UnirestException e) {
            ctx.sessionAttribute("flash", "Не удалось проверить страницу");
            ctx.sessionAttribute("alert", "alert alert-danger");
        }
        ctx.redirect("/urls/" + id);
    };

    public static String getNormalizedUrl(String url) {
        try {
            URL temp = new URL(url);
            String result = String.format("%s://%s", temp.getProtocol(), temp.getHost());
            int port = temp.getPort();
            if (port > 0) {
                result = result + ":" + port;
            }
            return result;
        } catch (MalformedURLException e) {
            return "";
        }
    }
}
