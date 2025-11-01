package ppi.e_commerce.Config;package ppi.e_commerce.Config;



import com.paypal.base.rest.APIContext;import com.paypal.base.rest.APIContext;

import com.paypal.base.rest.PayPalRESTException;import com.paypal.base.rest.PayPalRESTException;

import org.springframework.beans.factory.annotation.Value;import org.springframework.beans.factory.annotation.Value;

import org.springframework.context.annotation.Bean;import org.springframework.context.annotation.Bean;

import org.springframework.context.annotation.Configuration;import org.springframework.context.annotation.Configuration;



import java.util.HashMap;import java.util.HashMap;

import java.util.Map;import java.util.Map;



@Configuration@Configuration

public class PayPalConfig {public class PayPalConfig {



    @Value("${app.paypal.client-id}")    @Value("${app.paypal.client-id}")

    private String clientId;    private String clientId;



    @Value("${app.paypal.client-secret}")    @Value("${app.paypal.client-secret}")

    private String clientSecret;    private String clientSecret;



    @Value("${app.paypal.mode}")    @Value("${app.paypal.mode}")

    private String mode;    private String mode;



    @Bean    @Bean

    public APIContext apiContext() throws PayPalRESTException {    public APIContext apiContext() throws PayPalRESTException {

        Map<String, String> configMap = new HashMap<>();        Map<String, String> configMap = new HashMap<>();

        configMap.put("mode", mode);        configMap.put("mode", mode);



        return new APIContext(clientId, clientSecret, mode);        return new APIContext(clientId, clientSecret, mode);

    }    }

}}
