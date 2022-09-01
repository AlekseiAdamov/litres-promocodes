package ru.alekseiadamov.lovikod.message;

import org.junit.jupiter.api.Test;
import ru.alekseiadamov.lovikod.PromoCodeInfo;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class MessageTextTest {

    @Test
    void create() {
        final String expected = "<!DOCTYPE html>\n" +
                "<html lang=\"ru\">\n" +
                "<head>\n" +
                "  <meta charset=\"UTF-8\">\n" +
                "  <title>New Litres promo codes</title>\n" +
                "  <style>\n" +
                "    table, th, td {\n" +
                "      border: 1px solid grey;\n" +
                "      border-collapse: collapse;\n" +
                "    }\n" +
                "    a {\n" +
                "      color: red;\n" +
                "      text-decoration: none;\n" +
                "    }\n" +
                "  </style>\n" +
                "</head>\n" +
                "<body>\n" +
                "  <table>\n" +
                "    <thead>\n" +
                "      <tr>\n" +
                "        <th>Link</th>\n" +
                "        <th>Promo code</th>\n" +
                "        <th>Description</th>\n" +
                "        <th>Time limit</th>\n" +
                "      </tr>\n" +
                "    </thead>\n" +
                "    <tbody>\n" +
                "<tr><td><a href=\"https://www.litres.ru/?mnogo268&lfrom=342676429\">&#128216; get books</a></td><td></td><td>Скидка 30% на всё (24 часа) + 100 бонусных рублей</td><td>до 31.08.2022</td></tr>\n" +
                "<tr><td><a href=\"https://www.litres.ru/?RUSKINO30&lfrom=342676429\">&#128216; get books</a></td><td></td><td>Скидка 30% на лучшие экранизированные романы</td><td>до 30.08.2022</td></tr>\n" +
                "<tr><td><a href=\"https://www.litres.ru/pages/put_money_on_account/?descr=18&code1=OLYMP20&lfrom=342676429\">&#128216; get books</a></td><td>OLYMP20</td><td>Скидка 20% на всё (3 дня) + 1 книга от Olymp</td><td>до 31.08.2022</td></tr>\n" +
                "<tr><td><a href=\"https://www.litres.ru/pages/put_money_on_account/?descr=18&code1=SYMBOL&lfrom=342676429\">&#128216; get books</a></td><td>SYMBOL</td><td>Скидка 15% на всё (3 дня) + 1 книга из подборки</td><td>август 2022</td></tr>\n" +
                "\n" +
                "    </tbody>\n" +
                "  </table>\n" +
                "</body>\n" +
                "</html>";
        final List<PromoCodeInfo> promoCodes = new ArrayList<>();
        promoCodes.add(new PromoCodeInfo(
                "до 31.08.2022",
                "https://www.litres.ru/?mnogo268&lfrom=342676429",
                "Скидка 30% на всё (24 часа) + 100 бонусных рублей"
        ));
        promoCodes.add(new PromoCodeInfo(
                "до 30.08.2022",
                "https://www.litres.ru/?RUSKINO30&lfrom=342676429",
                "Скидка 30% на лучшие экранизированные романы"
        ));
        promoCodes.add(new PromoCodeInfo(
                "до 31.08.2022",
                "https://www.litres.ru/pages/put_money_on_account/?descr=18&code1=OLYMP20&lfrom=342676429",
                "Скидка 20% на всё (3 дня) + 1 книга от Olymp"
        ));
        promoCodes.add(new PromoCodeInfo(
                "август 2022",
                "https://www.litres.ru/pages/put_money_on_account/?descr=18&code1=SYMBOL&lfrom=342676429",
                "Скидка 15% на всё (3 дня) + 1 книга из подборки"
        ));
        assertEquals(expected, MessageText.create(promoCodes));
    }
}
