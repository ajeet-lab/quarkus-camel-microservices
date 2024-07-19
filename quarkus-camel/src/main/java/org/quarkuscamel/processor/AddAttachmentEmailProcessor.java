package org.quarkuscamel.processor;


import java.io.File;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.attachment.AttachmentMessage;

import jakarta.activation.DataHandler;
import jakarta.activation.FileDataSource;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Named;

@ApplicationScoped
@Named("AddAttachmentEmailProcessor")
public class AddAttachmentEmailProcessor implements Processor{

    @Override
    public void process(Exchange exchange) throws Exception {
        String fullPath=exchange.getIn().getHeader("CamelFilePath", String.class);
        String fileName=exchange.getIn().getHeader("CamelFileName", String.class);
        AttachmentMessage attMsg = exchange.getIn(AttachmentMessage.class);
        attMsg.addAttachment(fileName, new DataHandler(new FileDataSource(new File(fullPath))));
    }
    
}
