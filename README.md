# Tinkoff-invest-Java-SDK
 <b>Java SDK for Tinkoff-invest API(gRPC)</b> <br/>
  (в разработке)<br/>

За основу в SDK взяты <a href="https://github.com/Tinkoff/investAPI/tree/main/src/docs/contracts">proto-файлы из официального API Tinkoff</a>.<br/>

Пример.<br>
<pre>TinkoffSDK sdk = new TinkoffSDK(token);
GetAccountsResponse accounts = sdk.getServices().getAccounts().GetAccounts();
System.out.printf("Список счетов \n%s", accounts.toString());</pre>
 
Особенности.<br/>

Сделан контроль лимита запросов(x-ratelimit-remaining).<br/>
При достижении лимита выдерживается пауза до сброса лимита, которая расчитывается как: "время последнего запроса" (поле date из Header) + x-ratelimit-reset пауза расчитывается относительно времени на хосте, <b>рекомендуется синхронизировать время. </b><br/>
Для выключения контроля лимита запросов: <br/>
<pre>TinkoffSDK sdk = new TinkoffSDK(token).setControlLimit(false);</pre>

Сервисы.<br/>

Список доступных(реализованных) сервисов см. interface IAllServices <br/>
Доступ к сервисам организован через 2 метода: <br/>
Реализованы методы описанные официальной документации с минимальными изменениями в параметрах методов (<i>например вместо Quotation передаем BigDecimal и т.п.</i>)
<pre>new TinkoffSDK(token).getServices()</pre>
В основе базовые классы сервисов с дополнительным(модифицированным) функционалом. Например: <ul><li>Получение инструмента по Тикеру(без указания class_code)</li> <li>Получения истории свечей за произвольный период, если интервал выходит за <a href="https://tinkoff.github.io/investAPI/load_history/">ограничения</a>, то будет выполнен в несколько запросов)</li><li>и др.</li></ul>
<pre>new TinkoffSDK(token).getServicesWithChild()</pre>

