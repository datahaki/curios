// code by jph
package ch.alpine.curios.mvn;

import org.apache.maven.plugin.surefire.log.api.ConsoleLogger;

public enum DummyCL implements ConsoleLogger {
  INSTANCE;

  @Override
  public boolean isDebugEnabled() {
    // TODO Auto-generated method stub
    return false;
  }

  @Override
  public void debug(String message) {
    // TODO Auto-generated method stub
  }

  @Override
  public boolean isInfoEnabled() {
    // TODO Auto-generated method stub
    return false;
  }

  @Override
  public void info(String message) {
    // TODO Auto-generated method stub
  }

  @Override
  public boolean isWarnEnabled() {
    // TODO Auto-generated method stub
    return false;
  }

  @Override
  public void warning(String message) {
    // TODO Auto-generated method stub
  }

  @Override
  public boolean isErrorEnabled() {
    // TODO Auto-generated method stub
    return false;
  }

  @Override
  public void error(String message) {
    // TODO Auto-generated method stub
  }

  @Override
  public void error(String message, Throwable t) {
    // TODO Auto-generated method stub
  }

  @Override
  public void error(Throwable t) {
    // TODO Auto-generated method stub
  }
}
