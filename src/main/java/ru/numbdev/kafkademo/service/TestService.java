package ru.numbdev.kafkademo.service;

import lombok.RequiredArgsConstructor;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Service;
import ru.numbdev.kafkademo.model.TestMessage;

@Service
@RequiredArgsConstructor
public class TestService {

    private final InputGateway inputGateway;

    public void doTest(int id) {
        inputGateway.process("foo-"+id, new TestMessage("message for "+id));
    }

    @ServiceActivator(inputChannel = "testInput")
    public void test(
            @Header("type") String type,
            @Header(KafkaHeaders.RECEIVED_PARTITION_ID) int partition,
            TestMessage testMessage
    ) {
        System.out.println("Partition id: " + partition + " type: " + type + " ===== " + testMessage.msg());
    }

}
