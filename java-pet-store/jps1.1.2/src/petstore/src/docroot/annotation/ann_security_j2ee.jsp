<%@ include file="ann_header.html" %>
    <table cols=1 border=0 cellpadding=0 cellspacing=0>
      <tr>
        <td>
          <font size="+2">
            <b>
              J2EE Security
            </b>
          </font>
          <ul>
            <li><font size="+1">Simple end to end security model</font></li>
            <li><font size="+1">Form based login scheme for web</font></li>
            <li><font size="+1">Developer maps securtiy principals to roles</font></li>
            <li><font size="+1">Deployer maps securtiy roles to target platform</font></li>
          </ul>
        </td>
      </tr>
      <tr>
        <td bgcolor="#000000">
          <center><b><font color="#FFFFFF" size="+2">
                Web Deployment Descriptor</font></b>
          </center>
        </td>
      </tr>
      <tr>
        <td>
&nbsp;&lt;security-constraint>
<br>&nbsp;&nbsp;&nbsp; &lt;web-resource-collection>
<br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; &lt;web-resource-name>MySecureBit1&lt;/web-resource-name>
<br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; &lt;description>no description&lt;/description>
<br><b>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; &lt;url-pattern><font color="#3366FF">/control/signin</font>&lt;/url-pattern></b>
<br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; &lt;http-method>POST&lt;/http-method>
<br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; &lt;http-method>GET&lt;/http-method>
<br>&nbsp;&nbsp;&nbsp; &lt;/web-resource-collection>
<br>&nbsp;&nbsp;&nbsp; &lt;auth-constraint>
<br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; &lt;description>no description&lt;/description>
<br><b>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; &lt;role-name><font color="#3366FF">gold_customer</font>&lt;/role-name></b>
<br><b>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; &lt;role-name><font color="#3366FF">customer</font>&lt;/role-name></b>
<br>&nbsp;&nbsp;&nbsp; &lt;/auth-constraint>
<br>&nbsp;&nbsp;&nbsp; ...
<br>&nbsp; &lt;/security-constraint>
        </td>
      </tr>
    </table>
  </body>
</html>
