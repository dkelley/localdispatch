package com.xmog;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.xmog.stack.Environment;
import com.xmog.stack.StackConfiguration;
import com.xmog.stack.web.server.ServerConfiguration;

/**
 * @author Transmogrify LLC.
 */
@Singleton
public class Configuration extends StackConfiguration {
  private String exampleProperty;

  @Inject
  public Configuration(Environment environment, ServerConfiguration serverConfiguration) {
    super(environment, serverConfiguration);
  }

  @Override
  protected void initialize() {
    super.initialize();
    exampleProperty = getValueForProperty("example.property");
  }

  public String getExampleProperty() {
    return exampleProperty;
  }
}
