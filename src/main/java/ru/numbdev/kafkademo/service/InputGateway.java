package ru.numbdev.kafkademo.service;


import org.springframework.integration.annotation.Gateway;
import org.springframework.integration.annotation.MessagingGateway;
import org.springframework.messaging.handler.annotation.Header;
import ru.numbdev.kafkademo.model.TestMessage;

@MessagingGateway
public interface InputGateway {
    @Gateway(requestChannel = "testOutput")
    void process(
            @Header("type") String type,
            TestMessage message
    );
}
