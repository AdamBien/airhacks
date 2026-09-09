<%@ include file="ann_header.html" %>
<font size="+2"><bold>Transactions</bold></font><br>
<br>The developer does not need to do extra work to insure transaction semantics.
Transaction semantics are taken care of by the J2EE environment.
Transaction semantics are specified in the deployment descriptor.<br>

<br>

    <table cols=1 border=0 cellpadding=0 cellspacing=0>
      <tr>
        <td bgcolor="#000000">
          <center><b><font color="#FFFFFF" size="+2">
              <bold>EJB Deployment Descriptor</font></b>
          </center>
        </td>
      </tr>
      <tr>
        <td>
&lt;container-transaction>
<br>&nbsp; &lt;method>
<br>&nbsp;&nbsp;&nbsp;&nbsp; &lt;ejb-name><b>TheOrder</b>&lt;/ejb-name>
<br>&nbsp;&nbsp;&nbsp;&nbsp; &lt;method-intf><b>Remote</b>&lt;/method-intf>
<br>&nbsp;&nbsp;&nbsp;&nbsp; &lt;method-name><b>getDetails</b>&lt;/method-name>
<br>&nbsp;&nbsp;&nbsp;&nbsp; &lt;method-params />
<br>&nbsp;&nbsp; &lt;/method>
<br>&nbsp;&nbsp; &lt;trans-attribute><b><font color="#FF0000">Required</font></b>&lt;/trans-attribute>
<br>&lt;/container-transaction>
 <br>
</body>
</html>