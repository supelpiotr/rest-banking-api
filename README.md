<div>
<p><strong>Rest banking API</strong></p>
</div>
<div>
<p><strong>To run:</strong></p>
</div>
<div>
<ul>
<li>git clone https://github.com/supelpiotr/rest-banking-api.git</li>
<li>cd rest-banking-api/</li>
<li>mvn spring-boot:run</li>
</ul>
</div>
<div>
<p>&nbsp;</p>
</div>
<div>
<p><strong>Available endpoints:</strong></p>
</div>
<div>
<p>&nbsp;</p>
</div>
<div>
<ul>
<li>POST /api/register</li>
</ul>
</div>
<div>
<p>Accepting JSON:<br />{<br /> "firstName": "xxx",<br /> "lastName": "xxx",<br /> "password": "test123",<br /> "initialPlnBalance": 250,<br /> "pesel": "xxx"<br />}</p>
</div>
<div>
<p>&nbsp;</p>
</div>
<div>
<ul>
<li>POST /api/session/login</li>
</ul>
</div>
<div>
<p>Accepting JSON:<br />{<br /> "password": "test123",<br /> "pesel": "xxx"<br />}</p>
</div>
<div>
<p>&nbsp;</p>
<ul>
<li>POST /api/create/subaccount/{currency}</li>
</ul>
</div>
<p>&nbsp;</p>
<div>
<ul>
<li>POST /api/exchange</li>
</ul>
</div>
<div>
<p>Accepting JSON:<br />{<br /> "initialCurrency": "PLN",<br /> "finalCurrency": "USD",<br /> "requestedValue": "100"<br />}</p>
</div>
<div>
<p>&nbsp;</p>
</div>
<div>
<ul>
<li>GET /api/session/logout</li>
<li>GET /api/session</li>
<li>GET /api/user/details</li>
</ul>
</div>
<div>
<p>&nbsp;</p>
</div>
