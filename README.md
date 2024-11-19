# BGvACC Web application

## Authentication

In order to use the authentication for APIs, locate Tomcat directory and find `conf/context.xml` file.

In the `<Context>` tag place the following lines:

```
<Environment name="config/vatsim/client-secret" value="" type="java.lang.String"/>
<Environment name="config/vateud/api-key" value="" type="java.lang.String"/>

<Environment name="config/db/host" value="" type="java.lang.String"/>
<Environment name="config/db/port" value="" type="java.lang.String"/>
<Environment name="config/db/schema" value="" type="java.lang.String"/>
<Environment name="config/db/username" value="" type="java.lang.String"/>
<Environment name="config/db/password" value="" type="java.lang.String"/>

<Environment name="config/discord/client-id" value="" type="java.lang.String"/>
<Environment name="config/discord/bot/token" value="" type="java.lang.String"/>

<Environment name="config/discord/controllers-online/announcements-channel-id" value="" type="java.lang.String"/>
<Environment name="config/discord/controllers-online/webhook-id" value="" type="java.lang.String"/>

<Environment name="config/discord/events/announcements-channel-id" value="" type="java.lang.String"/>
<Environment name="config/discord/events/webhook-id" value="" type="java.lang.String"/>

<Environment name="config/mail/host" value="" type="java.lang.String"/>
<Environment name="config/mail/port" value="" type="java.lang.String"/>
<Environment name="config/mail/protocol" value="" type="java.lang.String"/>
<Environment name="config/mail/username" value="" type="java.lang.String"/>
<Environment name="config/mail/password" value="" type="java.lang.String"/>
```