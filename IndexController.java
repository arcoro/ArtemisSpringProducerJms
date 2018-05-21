package com.spring.jms.controller;

import com.spring.jms.datos.Operaciones;
import com.spring.jms.servicios.ServicioOperaciones;
import org.springframework.context.ApplicationContext;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.web.context.annotation.SessionScope;
import org.springframework.web.jsf.FacesContextUtils;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.jms.ObjectMessage;
import java.io.Serializable;

@ManagedBean(name = "operacionAction")
@SessionScoped
public class IndexController implements Serializable{
    private Operaciones operaciones;
    private JmsTemplate jmsTemplate;

    public Operaciones getOperaciones() {
        return operaciones;
    }

    public void setOperaciones(Operaciones operaciones) {
        this.operaciones = operaciones;
    }

    @PostConstruct
    public void init(){
        ApplicationContext context = FacesContextUtils.getWebApplicationContext(FacesContext.getCurrentInstance());
        jmsTemplate = context.getBean(JmsTemplate.class);
        operaciones = new Operaciones();
    }

    public String send(){
        jmsTemplate.send(
                (session)->{
                    ObjectMessage jmsMessage = session.createObjectMessage(operaciones);
                    System.out.println(">>> Enviando"+operaciones);
                    return jmsMessage;
                }
        );
        return "index";
    }
}
