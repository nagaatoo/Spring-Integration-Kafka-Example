package ru.numbdev.kafkademo.config;

import org.apache.kafka.clients.producer.ProducerConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.expression.common.LiteralExpression;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;
import org.springframework.integration.kafka.outbound.KafkaProducerMessageHandler;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.kafka.core.*;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.kafka.support.serializer.JsonSerializer;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHandler;


import java.util.HashMap;
import java.util.Map;

@Configuration
@ConditionalOnProperty(name = "app.type", havingValue = "producer", matchIfMissing = true)
public class KafkaProducerConfig {

    @Value("${app.kafka.server}")
    public String server;

    @Value("${app.kafka.topic}")
    public String topic;

    @Bean
    public MessageChannel testOutput() {
        return new DirectChannel();
    }

    @Bean
    public KafkaTemplate<String, String> kafkaTemplate() {
        return new KafkaTemplate<>(producerFactory());
    }

    @Bean
    public MessageHandler handler() {
        KafkaProducerMessageHandler<String, String> handler =
                new KafkaProducerMessageHandler<>(kafkaTemplate());
//        handler.setPartitionIdExpression(new FunctionExpression<>());
        handler.setTopicExpression(new LiteralExpression(topic));
        handler.setMessageKeyExpression(new LiteralExpression("someKey"));
        return handler;
    }

    @Bean
    public ProducerFactory<String, String> producerFactory() {
        Map<String, Object> props = new HashMap<>();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, server);
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        return new DefaultKafkaProducerFactory<>(props);
    }

    @Bean
    public IntegrationFlow kafkaTestFlow() {
        return IntegrationFlows
                .from(testOutput())
                .transform(
                        Message.class,
                        m -> MessageBuilder
                                .fromMessage(m)
                                .setHeader(KafkaHeaders.PARTITION_ID, 3)
                )
                .handle(handler())
                .get();
    }

    public void partitionHandler(Message<?> message) {

//        message.getHeaders().put(KafkaHeaders.PARTITION_ID, type.hashCode()/5);
    }
}
