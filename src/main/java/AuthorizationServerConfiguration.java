import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.InMemoryTokenStore;

// Creating an Authorization Server
@Configuration
@EnableAuthorizationServer // tell Spring to activate the authorization server
public class AuthorizationServerConfiguration extends AuthorizationServerConfigurerAdapter {
    @Autowired
    @Qualifier("authenticationManagerBean")
    // AuthenticationManager bean that Spring provides automatically
    private AuthenticationManager authenticationManager;

    @Autowired
    private TokenStore tokenStore;

    @Override
    // specify credentials of Authorisation Server
    public void configure (ClientDetailsServiceConfigurer clients) throws Exception {
        // specify that we are going to store the services in memory. In a 'real' application, we would save it in a
        // database, an LDAP server, etc.
        clients.inMemory()
                // 'client' is the user with whom we will identify
                .withClient("client")
                // specify  services configured for the defined user ('client'); only password service here
                .authorizedGrantTypes("password", "authorization_code", "refresh_token", "implicit")
                // specifies roles or groups contained by the service offered. Not use here, let it run for time being
                .authorities("ROLE_CLIENT", "ROLE_TRUSTED_CLIENT", "USER")
                // scope of the service; not used here
                .scopes("read", "write")
                // automatically approve the client's requests
                .autoApprove(true)
                // password of the client
                .secret(passwordEncoder().encode("password"));
                // the password encoder function defined further below is called to specify with what type of encryption
                // the password will be saved. The encode function is annotated with the @Bean because when we supply the
                // password in an HTTP request, Spring will look for a  PasswordEncoder object to check the
                // validity of the delivered password
    }

    @Bean
    public PasswordEncoder passwordEncoder () {
        return new BCryptPasswordEncoder();
    }

    @Override
    // define which authentication controller and store of identifiers should use the end points. Clarify that the end
    // points are the URLs where we will talk to the Authorisation Server to request the code/token
    public void configure (AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
        endpoints
                .authenticationManager (authenticationManager)
                .tokenStore (tokenStore);
    }

    @Bean
    // The TokenStore is where the identifiers that our authentication server is supplying will be stored
    // when the resource server asks for the protected resource (e.g. contacts), it can respond to it
    public TokenStore tokenStore () {
        return new InMemoryTokenStore(); // InMemoryTokenStore stores the identifiers in memory
        // in a real application, we could use a JdbcTokenStore to save them in a database
    }
}
