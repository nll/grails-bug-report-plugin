package pt.whiteroad.plugins.bugreport

import org.codehaus.groovy.grails.commons.ConfigurationHolder
import grails.util.GrailsUtil

class ConfigUtil {

  static def getConfig() {
    return ConfigurationHolder.config.grails.plugins.bugreport;
    
  }

    // thanks to Quartz plugin
  static def ConfigObject loadReportBugConfigConfig() {
    def config = ConfigurationHolder.config.grails.plugins.bugreport
    GroovyClassLoader classLoader = new GroovyClassLoader(getClass().classLoader)

    config.merge(new ConfigSlurper(GrailsUtil.environment).parse(classLoader.loadClass('DefaultReportBugConfig')))

    try {
      config.merge(new ConfigSlurper(GrailsUtil.environment).parse(classLoader.loadClass('ReportBugConfig')))
    } catch (Exception ignored) {
      // ignore, just use the defaults
    }

    return config.reportBug
  }


}
