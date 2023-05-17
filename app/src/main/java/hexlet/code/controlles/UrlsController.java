package hexlet.code.controlles;

import hexlet.code.model.UrlCheck;
import hexlet.code.model.UrlModel;
import hexlet.code.model.query.QUrlModel;
import io.ebean.PagedList;
import io.javalin.http.Handler;
import io.javalin.http.NotFoundResponse;
import kong.unirest.UnirestException;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.stream.IntStream;

import kong.unirest.HttpResponse;
import kong.unirest.Unirest;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;


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
        int currentPage = pagedUrl.getTotalPageCount();
        List<Integer> pages = IntStream
                .range(1, currentPage + 1)
                .boxed()
                .toList();
        ctx.attribute("urls", urls);
        ctx.attribute("page", page);
        ctx.attribute("pages", pages);
        ctx.attribute("currentPage", currentPage);
        ctx.render("/urls/index.html");
    };

    public static final Handler CREATE_URL = ctx -> {
        var inputContent = ctx.formParam("url");
        assert inputContent != null;
        inputContent = inputContent.strip();
        var validateUrl = getNormalizedUrl(inputContent);
        if (validateUrl.isEmpty()) {
            ctx.status(STATUS_INCORRECT_URL);
            ctx.sessionAttribute("flash", "Некорректный URL");
            ctx.sessionAttribute("alert", "alert alert-danger");
            ctx.redirect("/");
            return;
        }
        UrlModel verificationOfExistence = new QUrlModel()
                .url.equalTo(validateUrl)
                .findOne();
        if (verificationOfExistence != null) {
            ctx.status(STATUS_INCORRECT_URL);
            ctx.sessionAttribute("flash", "Страница уже существует");
            ctx.sessionAttribute("alert", "alert alert-info");
            ctx.redirect("/urls");
            return;
        }
        UrlModel urlModel = new UrlModel(validateUrl);
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
        ctx.attribute("url", urlModel);
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
            Document body = Jsoup.parse(response.getBody());
            String title = body.title();
            String h1 = body.selectFirst("h1") != null ? body.selectFirst("h1").text() : "";
            String description = body.selectFirst("meta[name=description]") != null
                    ? body.selectFirst("meta[name=description]").attr("content") : "";
            int statusCode = response.getStatus();
            UrlCheck check = new UrlCheck(statusCode, title, h1, description, url);
            check.save();
            ctx.sessionAttribute("flash", "Страница успешно проверена");
            ctx.sessionAttribute("alert", "alert alert-success");
        } catch (UnirestException e) {
            ctx.sessionAttribute("flash", "Некорректный адрес");
            ctx.sessionAttribute("alert", "alert alert-danger");
        }  catch (RuntimeException e) {
            ctx.sessionAttribute("flash", "Произошла ошибка, повторите ещё раз или измените запрос");
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
