package org.quarkuscamel.routes.email;

import org.apache.camel.builder.RouteBuilder;

public class SendEmailWithAttachment extends RouteBuilder{

    @Override
    public void configure() throws Exception {
       from("file:work/output")
            .setHeader("To").simple("{{to}}")
            .setHeader("Cc").simple("{{cc}}")
            .setHeader("Subject").simple("{{subject}}")
            .bean("AddAttachmentEmailProcessor")
            .setHeader("Content-Type", constant("text/html"))
            .to("velocity:configfiles/attachment-email.vm")
            .to("{{smtp}}?username={{from}}&password={{password}}")
            .log("Mail has sent with the attachment successfully");
    }
    
}
