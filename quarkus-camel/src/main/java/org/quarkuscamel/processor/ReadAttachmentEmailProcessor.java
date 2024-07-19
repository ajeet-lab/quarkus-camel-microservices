package org.quarkuscamel.processor;

import java.io.FileOutputStream;
import java.util.HashMap;
import java.util.Map;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.attachment.AttachmentMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.activation.DataHandler;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Named;


@ApplicationScoped
@Named("ReadAttachmentEmailProcessor")
public class ReadAttachmentEmailProcessor implements Processor{

    Logger log = LoggerFactory.getLogger(ReadAttachmentEmailProcessor.class);

    @Override
    public void process(Exchange exchange) throws Exception {
        String pathName = exchange.getContext().resolvePropertyPlaceholders("{{readAttachmentDir}}");         
        AttachmentMessage attachmentMessage = exchange.getIn(AttachmentMessage.class);
        Map<String, DataHandler> attachments = attachmentMessage.getAttachments();
        

        if(attachments == null){
            Map<String, Object> errorMessage= new HashMap<>();
            errorMessage.put("isSuccess", false);
            errorMessage.put("error_message", "Attachment is not attached in the mail");
            exchange.getIn().setBody(errorMessage);
        }else{
            if(attachments.size() > 0){
                for(String name:attachments.keySet()){
                    DataHandler dh = attachments.get(name);
                    // get the file name
                    String fileName = dh.getName();
                    // get the content and convert it to byte[]
                    byte[] data = exchange.getContext().getTypeConverter().convertTo(byte[].class, dh.getInputStream());
    
                    FileOutputStream out = new FileOutputStream(pathName+"\\"+fileName);
                    out.write(data);
                    out.flush();
                    out.close();
                }
        }

        
        }
    }
    
}
