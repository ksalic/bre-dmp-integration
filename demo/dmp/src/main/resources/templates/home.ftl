<!DOCTYPE html>
<html lang="en">
  <head>
    <meta charset="UTF-8">
    <title>DMP</title>
  </head>

  <style>
    table, th, td {
      border: 1px solid black;
      border-collapse: collapse;
      padding: 10px;
    }
  </style>

  <body>
    <h1>DMP platform FOO!</h1>

    <p>the pixel:</p>

    <p>
      <img src="http://localhost:8080/site/integration/dmp/sync/foo?q_sid=ca_cookie&amp;q_uid=UID&amp;rd=http%3A%2F%2Flocalhost%3A9090%2Fdmp%2Ffoo%2Fpixel.png%3Fuuid%3DUID%26ca_cookie%3D${id}" width="1" height="1"/>
    </p>
    <p>registering... a DMP user id with a BRE id... (client side)</p>
    <h2>Users registered: (refresh if required)</h2>


  <#if users??>
    <table>
      <tr>
        <th>BRE User ID</th>
        <th>DMP User ID</th>
        <th>Interest In</th>
        <th>Change</th>
      </tr>
        <#list users as user>
          <tr>
            <td><a href="http://localhost:8080/site/integration/dmp/${user.breId}">${user.breId}</a></td>
            <td>${user.dmpId}</td>
            <td>
            ${user.interest}
            </td>
            <td>
                <#if user.interest=="apples">
                  <a href="/dmp/${user.breId}/change/oranges">change interest "oranges"</a>
                <#else>
                  <a href="/dmp/${user.breId}/change/apples">change interest to "apples"</a>
                </#if>
            </td>
          </tr>
        </#list>
    </table>
  </#if>

  </body>
</html>