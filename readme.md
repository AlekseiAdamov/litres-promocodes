# Litres promo codes

Приложение для рассылки новых промокодов [Litres](https://www.litres.ru/).

## Принцип работы

Промокоды парсятся со страницы https://lovikod.ru/knigi/promokody-litres.

Ранее высланные промокоды сохраняются в одной директории с приложением в файле `litres.csv`. По ним определяются новые промокоды.

Туда же сохраняется лог `litres.log`.

Новые промокоды высылаются в виде таблицы (пример ниже) на указанный в настройках адрес почты.

В колонке **Promo code** код указывается только для тех ссылок, где требуется внести код вручную (страница пополнения кошелька). Для прямых ссылок код в колонке не указывается.

<table>
	<thead>
		<tr>
			<th>Link</th>
			<th>Promo code</th>
			<th>Description</th>
			<th>Time limit</th>
		</tr>
	</thead>
	<tbody>
		<tr><td><a href=\"https://www.litres.ru/?mnogo268&lfrom=342676429\">&#128216; get books</a></td><td></td><td>Скидка 30% на всё (24 часа) + 100 бонусных рублей</td><td>до 31.08.2022</td></tr>
		<tr><td><a href=\"https://www.litres.ru/?RUSKINO30&lfrom=342676429\">&#128216; get books</a></td><td></td><td>Скидка 30% на лучшие экранизированные романы</td><td>до 30.08.2022</td></tr>
		<tr><td><a href=\"https://www.litres.ru/pages/put_money_on_account/?descr=18&code1=OLYMP20&lfrom=342676429\">&#128216; get books</a></td><td>OLYMP20</td><td>Скидка 20% на всё (3 дня) + 1 книга от Olymp</td><td>до 31.08.2022</td></tr>
		<tr><td><a href=\"https://www.litres.ru/pages/put_money_on_account/?descr=18&code1=SYMBOL&lfrom=342676429\">&#128216; get books</a></td><td>SYMBOL</td><td>Скидка 15% на всё (3 дня) + 1 книга из подборки</td><td>август 2022</td></tr>
	</tbody>
</table>

## Настройки почты для рассылки

Настройки почты нужно сохранить в директории приложения в файле `mail.properties`.

Состав файла должен быть следующий (на примере Gmail):
```properties
# Эти настройки оставить в таком же виде.
mail.smtp.auth=true
mail.smtp.host=smtp.gmail.com
mail.smtp.port=587
mail.smtp.starttls.enable=true
mail.smtp.ssl.trust=smtp.gmail.com
mail.smtp.ssl.protocols=TLSv1.2

# Почтовый адрес, с которого будет выполняться рассылка.
mail.from.address=

# Почтовые адреса, на которые нужно выполнять рассылку, разделённые запятыми.
mail.to.address=

# Имя пользователя (адрес электронной почты).
mail.user=

# Пароль от учётной записи. См. комментарий под блоком кода.
mail.password=

# Тема письма с плейсхолдером даты.
# Без плейсхолдера будет исключение.
mail.message.title=Litres promo codes %1$td.%1$tm.%1$tY
```

Если для учётной записи Gmail включена двухфакторная аутентификация, то требуется сгенерировать пароль приложения по [ссылке](https://accounts.google.com/IssuedAuthSubTokens?hide_authsub=1).

## Структура директории приложения

Структура директории должна быть примерно такой:
```text
./
└┬─ libs/
 ├─ litres-promocodes-1.0-SNAPSHOT.jar
 ├─ litres.csv
 ├─ litres.log
 └─ mail.properties
```
