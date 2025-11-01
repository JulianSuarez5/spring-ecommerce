package ppi.e_commerce.Config;package ppi.e_commerce.Config;



import org.springframework.boot.context.properties.ConfigurationProperties;import org.springframework.boot.context.properties.ConfigurationProperties;

import org.springframework.context.annotation.Configuration;import org.springframework.context.annotation.Configuration;



@Configuration@Configuration

@ConfigurationProperties(prefix = "app")@ConfigurationProperties(prefix = "app")

public class AppProperties {public class AppProperties {

        

    private Jwt jwt = new Jwt();    private Jwt jwt = new Jwt();

    private Paypal paypal = new Paypal();    private Paypal paypal = new Paypal();

    private Seed seed = new Seed();    private Seed seed = new Seed();

    private Upload upload = new Upload();    private Upload upload = new Upload();

    private Admin admin = new Admin();    private Admin admin = new Admin();

        

    public static class Jwt {    // Getters y Setters

        private String secret;    public static class Jwt {

        private long expiration;        private String secret;

                private long expiration;

        public String getSecret() { return secret; }        

        public void setSecret(String secret) { this.secret = secret; }        public String getSecret() { return secret; }

        public long getExpiration() { return expiration; }        public void setSecret(String secret) { this.secret = secret; }

        public void setExpiration(long expiration) { this.expiration = expiration; }        public long getExpiration() { return expiration; }

    }        public void setExpiration(long expiration) { this.expiration = expiration; }

        }

    public static class Paypal {    

        private String clientId;    public static class Paypal {

        private String clientSecret;        private String clientId;

        private String mode;        private String clientSecret;

        private String baseUrl;        private String mode;

        private String businessEmail;        private String baseUrl;

                private String businessEmail;

        public String getClientId() { return clientId; }        

        public void setClientId(String clientId) { this.clientId = clientId; }        // Getters y Setters

        public String getClientSecret() { return clientSecret; }        public String getClientId() { return clientId; }

        public void setClientSecret(String clientSecret) { this.clientSecret = clientSecret; }        public void setClientId(String clientId) { this.clientId = clientId; }

        public String getMode() { return mode; }        public String getClientSecret() { return clientSecret; }

        public void setMode(String mode) { this.mode = mode; }        public void setClientSecret(String clientSecret) { this.clientSecret = clientSecret; }

        public String getBaseUrl() { return baseUrl; }        public String getMode() { return mode; }

        public void setBaseUrl(String baseUrl) { this.baseUrl = baseUrl; }        public void setMode(String mode) { this.mode = mode; }

        public String getBusinessEmail() { return businessEmail; }        public String getBaseUrl() { return baseUrl; }

        public void setBusinessEmail(String businessEmail) { this.businessEmail = businessEmail; }        public void setBaseUrl(String baseUrl) { this.baseUrl = baseUrl; }

    }        public String getBusinessEmail() { return businessEmail; }

            public void setBusinessEmail(String businessEmail) { this.businessEmail = businessEmail; }

    public static class Seed {    }

        private boolean createSampleData;    

        private boolean createDefaultAdmin;    public static class Seed {

                private boolean createSampleData;

        public boolean isCreateSampleData() { return createSampleData; }        private boolean createDefaultAdmin;

        public void setCreateSampleData(boolean createSampleData) { this.createSampleData = createSampleData; }        

        public boolean isCreateDefaultAdmin() { return createDefaultAdmin; }        // Getters y Setters

        public void setCreateDefaultAdmin(boolean createDefaultAdmin) { this.createDefaultAdmin = createDefaultAdmin; }        public boolean isCreateSampleData() { return createSampleData; }

    }        public void setCreateSampleData(boolean createSampleData) { this.createSampleData = createSampleData; }

            public boolean isCreateDefaultAdmin() { return createDefaultAdmin; }

    public static class Upload {        public void setCreateDefaultAdmin(boolean createDefaultAdmin) { this.createDefaultAdmin = createDefaultAdmin; }

        private String dir;    }

            

        public String getDir() { return dir; }    public static class Upload {

        public void setDir(String dir) { this.dir = dir; }        private String dir;

    }        

            public String getDir() { return dir; }

    public static class Admin {        public void setDir(String dir) { this.dir = dir; }

        private Registration registration = new Registration();    }

            

        public Registration getRegistration() { return registration; }    public static class Admin {

        public void setRegistration(Registration registration) { this.registration = registration; }        private Registration registration = new Registration();

                

        public static class Registration {        public Registration getRegistration() { return registration; }

            private String token;        public void setRegistration(Registration registration) { this.registration = registration; }

                    

            public String getToken() { return token; }        public static class Registration {

            public void setToken(String token) { this.token = token; }            private String token;

        }            

    }            public String getToken() { return token; }

                public void setToken(String token) { this.token = token; }

    public Jwt getJwt() { return jwt; }        }

    public void setJwt(Jwt jwt) { this.jwt = jwt; }    }

    public Paypal getPaypal() { return paypal; }    

    public void setPaypal(Paypal paypal) { this.paypal = paypal; }    // Getters y Setters de la clase principal

    public Seed getSeed() { return seed; }    public Jwt getJwt() { return jwt; }

    public void setSeed(Seed seed) { this.seed = seed; }    public void setJwt(Jwt jwt) { this.jwt = jwt; }

    public Upload getUpload() { return upload; }    public Paypal getPaypal() { return paypal; }

    public void setUpload(Upload upload) { this.upload = upload; }    public void setPaypal(Paypal paypal) { this.paypal = paypal; }

    public Admin getAdmin() { return admin; }    public Seed getSeed() { return seed; }

    public void setAdmin(Admin admin) { this.admin = admin; }    public void setSeed(Seed seed) { this.seed = seed; }

}    public Upload getUpload() { return upload; }
    public void setUpload(Upload upload) { this.upload = upload; }
    public Admin getAdmin() { return admin; }
    public void setAdmin(Admin admin) { this.admin = admin; }
}