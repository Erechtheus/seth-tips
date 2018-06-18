package de.dfki.nlp.config;

import de.dfki.nlp.domain.rest.ServerRequest;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.amqp.outbound.AmqpOutboundEndpoint;
import org.springframework.integration.amqp.support.DefaultAmqpHeaderMapper;
import org.springframework.integration.annotation.IntegrationComponentScan;
import org.springframework.integration.annotation.MessagingGateway;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.handler.annotation.Header;

@Configuration
@IntegrationComponentScan
public class MessagingConfig {

    private final static String queueName = "input";

    @Bean
    Queue inputQueue() {
        return new Queue(queueName, true);
    }

    @Bean
    @ServiceActivator(inputChannel = "requestChannel")
    public AmqpOutboundEndpoint amqpOutbound(AmqpTemplate amqpTemplate) {
        AmqpOutboundEndpoint outbound = new AmqpOutboundEndpoint(amqpTemplate);
        outbound.setRoutingKey(queueName); // default exchange - route to queue 'queuename'
        outbound.setExpectReply(false);
        outbound.setHeaderMapper(DefaultAmqpHeaderMapper.outboundMapper());
        // handle the priority
        return outbound;
    }

    @Bean
    public MessageChannel amqpOutboundChannel() {
        return new DirectChannel();
    }

    @MessagingGateway(defaultRequestChannel = "requestChannel")
    public interface ProcessingGateway {

        String HEADER_REQUEST_TIME = "requestTime";
        void sendForProcessing(ServerRequest data,
                               @Header(AmqpHeaders.EXPIRATION) String ttlInMs,
                               @Header(HEADER_REQUEST_TIME) long now);
    }

    @Bean
    public Jackson2JsonMessageConverter jackson2Converter() {
        return new Jackson2JsonMessageConverter();
    }


    @Bean
    public MessageChannel seth() {
        return new DirectChannel();
    }

    @Bean
    public MessageChannel mirner() {
        return new DirectChannel();
    }

    @Bean
    public MessageChannel diseases() {
        return new DirectChannel();
    }

    @Bean
    public MessageChannel parsed() {
        return new DirectChannel();
    }


}
