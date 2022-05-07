package ca.uhn.fhir.jpa.starter;

import javax.servlet.ServletException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;

import ca.uhn.fhir.jpa.model.config.PartitionSettings;
import ca.uhn.fhir.rest.server.interceptor.partition.RequestTenantPartitionInterceptor;
import health.medunited.tenant.JWTTenantIdentificationStrategy;

@Import(AppProperties.class)
public class JpaRestfulServer extends BaseJpaRestfulServer {

  @Autowired
  AppProperties appProperties;

  @Autowired
  private PartitionSettings myPartitionSettings;

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
    setTenantIdentificationStrategy(new JWTTenantIdentificationStrategy());

    // Use the tenant ID supplied by the tenant identification strategy
    // to serve as the partitioning ID
    registerInterceptor(new RequestTenantPartitionInterceptor());

  }

}
