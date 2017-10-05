package com.spring.app.controller.web;

import org.springframework.social.connect.ConnectionFactoryLocator;
import org.springframework.social.connect.ConnectionRepository;
import org.springframework.social.connect.web.ConnectController;
import org.springframework.stereotype.Controller;

@Controller
public class CustomConnectController extends ConnectController {

    public CustomConnectController(ConnectionFactoryLocator connectionFactoryLocator,
            ConnectionRepository connectionRepository) {
        super(connectionFactoryLocator, connectionRepository);
    }
    
    @Override
    protected String connectView(String providerId) {
        return providerId + "/connectHandle";
    }

    @Override
    protected String connectedView(String providerId) {
        return providerId + "/connectFinish";
    }
}