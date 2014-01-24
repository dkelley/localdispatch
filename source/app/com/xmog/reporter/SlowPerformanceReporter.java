package com.xmog.reporter;

import static java.lang.String.format;
import static org.slf4j.LoggerFactory.getLogger;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;

import com.google.inject.Singleton;
import com.xmog.stack.db.QueryLogRecord;
import com.xmog.stack.reporter.AbstractSlowPerformanceReporter;

/**
 * @author Transmogrify LLC.
 */
@Singleton
public class SlowPerformanceReporter extends AbstractSlowPerformanceReporter {
  private Logger logger = getLogger(getClass());

  @Override
  public void sendSlowQueryReport(QueryLogRecord queryLogRecord) {
    logger.debug(format("TODO: send a report (maybe an email?) for this slow query: %s", queryLogRecord));
  }

  @Override
  public void sendSlowHttpRequestReport(HttpServletRequest httpRequest, long httpRequestDurationInMilliseconds) {
    logger
      .debug(format("TODO: send a report (maybe an email?) for this slow request: %s", httpRequest.getRequestURI()));
  }

  @Override
  public boolean shouldSendSlowQueryReport(QueryLogRecord queryLogRecord) {
    // TODO: you can override this if you want to control when slow query reports should be sent
    return super.shouldSendSlowQueryReport(queryLogRecord);
  }

  @Override
  public boolean shouldSendSlowHttpRequestReport(HttpServletRequest httpRequest, long httpRequestDurationInMilliseconds) {
    // TODO: you can override this if you want to control when slow HTTP request reports should be sent
    return super.shouldSendSlowHttpRequestReport(httpRequest, httpRequestDurationInMilliseconds);
  }
}