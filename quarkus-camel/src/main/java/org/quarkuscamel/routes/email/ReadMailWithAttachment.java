package org.quarkuscamel.routes.email;

import org.apache.camel.builder.RouteBuilder;

public class ReadMailWithAttachment extends RouteBuilder {

    @Override
    public void configure() throws Exception {
        from("{{readMailUri}}")
        .process("ReadAttachmentEmailProcessor")
        .log("${body}")
        .to("log:body");
    }
    
}
