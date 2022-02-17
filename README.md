# Tinkoff-invest-Java-SDK
 <b>Java SDK for Tinkoff-invest API(gRPC)</b> <br/>
  (в разработке)<br/>

Пример.<br>
<pre>TinkoffSDK sdk = new TinkoffSDK(token);
GetAccountsResponse accounts = sdk.getServices().getAccounts().GetAccounts();
System.out.printf("Список счетов \n%s", accounts.toString());</pre>
 
Особенности.<br/>

Сделан контроль лимита запросов(x-ratelimit-remaining).<br/>
При достижении лимита выдерживается пауза до сброса лимита, которая расчитывается как: "время последнего запроса" (поле date из Header) + x-ratelimit-reset пауза расчитывается относительно времени на хосте, <b>рекомендуется синхронизировать время. </b><br/>
Для выключения контроля лимита запросов: <br/>
<pre>TinkoffSDK sdk = new TinkoffSDK(token).setControlLimit(false);</pre>


