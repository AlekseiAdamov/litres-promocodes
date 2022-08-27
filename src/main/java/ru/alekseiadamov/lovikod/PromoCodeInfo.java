package ru.alekseiadamov.lovikod;

import java.util.Objects;

public class PromoCodeInfo {
    private final String timeLimit;
    private final String link;
    private final String description;

    public PromoCodeInfo(String timeLimit, String link, String description) {
        this.timeLimit = sanitize(timeLimit);
        this.link = sanitize(Objects.requireNonNull(link));
        this.description = sanitize(description);
    }

    private String sanitize(String value) {
        return value.replace("\"", "\\\"");
    }

    public String getTimeLimit() {
        return this.timeLimit;
    }

    public String getLink() {
        return this.link;
    }

    public String getDescription() {
        return this.description;
    }

    public String getEmailText() {
        final String promoCode = this.link
                .replaceAll("^.+code1=", "")
                .replaceAll("&.+$", "");
        return String.format("<tr><td><a href=\"%s\">&#128216; get books</a></td><td>%s</td><td>%s</td><td>%s</td></tr>",
                this.link, promoCode, this.description, this.timeLimit);
    }
}
