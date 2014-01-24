package com.xmog.reporter;

import static org.slf4j.LoggerFactory.getLogger;

import org.slf4j.Logger;

import com.google.inject.Singleton;
import com.xmog.stack.reporter.AbstractErrorReporter;

/**
 * @author Transmogrify LLC.
 */
@Singleton
public class ErrorReporter extends AbstractErrorReporter {
  private Logger logger = getLogger(getClass());

  @Override
  public void sendErrorReport(Throwable throwable) {
    logger.debug("TODO: send an error report (maybe an email?) for this exception", throwable);
  }
}