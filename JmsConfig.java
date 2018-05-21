package com.spring.jms.config;

import org.apache.activemq.artemis.api.core.TransportConfiguration;
import org.apache.activemq.artemis.api.jms.ActiveMQJMSClient;
import org.apache.activemq.artemis.api.jms.JMSFactoryType;
import org.apache.activemq.artemis.core.remoting.impl.netty.NettyConnectorFactory;
import org.apache.activemq.artemis.core.remoting.impl.netty.TransportConstants;
import org.apache.activemq.artemis.jms.client.ActiveMQQueue;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.jms.config.DefaultJmsListenerContainerFactory;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.support.destination.BeanFactoryDestinationResolver;

import javax.jms.ConnectionFactory;
import java.util.HashMap;
import java.util.Map;

@Configuration
@ComponentScan(basePackages = "com.spring.jms.servicios")
@EnableJms
public class JmsConfig {

    @Autowired
    private BeanFactory springContextBeanFactory;

    @Bean
    ActiveMQQueue queueOperaciones(){
        return new ActiveMQQueue("DLQ");
    }

    @Bean
    public ConnectionFactory connectionFactory(){
        Map<String, Object> connDetails = new HashMap<>();
        connDetails.put(TransportConstants.HOST_PROP_NAME, "0.0.0.0");
        connDetails.put(TransportConstants.PORT_PROP_NAME, "61616");
        connDetails.put(TransportConstants.PROTOCOLS_PROP_NAME, "tcp");
        TransportConfiguration transportConfiguration = new TransportConfiguration(
                NettyConnectorFactory.class.getName(), connDetails);
        return ActiveMQJMSClient.createConnectionFactoryWithoutHA(JMSFactoryType.CF,transportConfiguration);
    }

    @Bean
    public DefaultJmsListenerContainerFactory jmsListenerContainerFactory(){
        DefaultJmsListenerContainerFactory factory = new DefaultJmsListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory());
        factory.setDestinationResolver(new BeanFactoryDestinationResolver(springContextBeanFactory));
        factory.setConcurrency("3-10");
        return factory;
    }

    @Bean
    JmsTemplate jmsTemplate(){
        JmsTemplate jmsTemplate = new JmsTemplate(connectionFactory());
        jmsTemplate.setDefaultDestination(queueOperaciones());
        return jmsTemplate;
    }

}
