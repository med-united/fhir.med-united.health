package ca.uhn.fhir.jpa.starter;

import java.util.Arrays;

import javax.servlet.ServletException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.web.cors.CorsConfiguration;

import ca.uhn.fhir.jpa.model.config.PartitionSettings;
import ca.uhn.fhir.jpa.starter.medunited.JWTTenantIdentificationStrategy;
import ca.uhn.fhir.rest.server.interceptor.CorsInterceptor;
import ca.uhn.fhir.rest.server.interceptor.partition.RequestTenantPartitionInterceptor;

@Import(AppProperties.class)
public class JpaRestfulServer extends BaseJpaRestfulServer {

  @Autowired
  AppProperties appProperties;

  @Autowired
  private PartitionSettings myPartitionSettings;
  
  @Autowired
  private JWTTenantIdentificationStrategy jWTTenantIdentificationStrategy;

  private static final long serialVersionUID = 1L;

  public JpaRestfulServer() {
    super();
  }

  @Override
  protected void initialize() throws ServletException {
    super.initialize();

    // Add your own customization here

    // Enable partitioning
    myPartitionSettings.setPartitioningEnabled(true);

    // Set the tenant identification strategy
    setTenantIdentificationStrategy(jWTTenantIdentificationStrategy);

    // Use the tenant ID supplied by the tenant identification strategy
    // to serve as the partitioning ID
    registerInterceptor(new RequestTenantPartitionInterceptor());

  }

}
