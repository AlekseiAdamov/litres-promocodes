package ru.alekseiadamov.lovikod;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PromoCodeInfoTest {

    @Test
    void getEmailText() {
        final String timeLimit = "август 2022";
        final String link = "https://www.litres.ru/pages/put_money_on_account/?descr=18&code1=BURO15&lfrom=342676429";
        final String description = "Скидка <strong>15%</strong> на всё (3 дня) + <strong>1 книга</strong> из <a href=\"https://www.litres.ru/kollekcii-knig/kniga-na-vybor-i-skidka-15/?lfrom=342676429\" rel=\"nofollow\" target=\"_blank\">подборки</a>";
        final PromoCodeInfo promoCodeInfo = new PromoCodeInfo(timeLimit, link, description);
        final String expected = "<tr><td><a href=\"https://www.litres.ru/pages/put_money_on_account/?descr=18&code1=BURO15&lfrom=342676429\">&#128216; get books</a></td><td>BURO15</td><td>Скидка <strong>15%</strong> на всё (3 дня) + <strong>1 книга</strong> из <a href=\"https://www.litres.ru/kollekcii-knig/kniga-na-vybor-i-skidka-15/?lfrom=342676429\" rel=\"nofollow\" target=\"_blank\">подборки</a></td><td>август 2022</td></tr>";
        assertEquals(expected, promoCodeInfo.getEmailText());
    }
}
