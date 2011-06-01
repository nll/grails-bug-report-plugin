import pt.whiteroad.plugins.bugreport.ConfigUtil
import org.codehaus.groovy.grails.commons.ConfigurationHolder
import grails.util.GrailsUtil
import pt.whiteroad.plugins.bugreport.BugReportUtil

class BugReportGrailsPlugin {
  // the plugin version
  def version = "0.1"
  // the version or versions of Grails the plugin is designed for
  def grailsVersion = "1.3.5 > *"
  // the other plugins this plugin depends on
  def dependsOn = [quartz:"0.4.2 > *"]
  // resources that are excluded from plugin packaging
  def pluginExcludes = [
          "grails-app/views/error.gsp"
  ]

  // TODO Fill in these fields
  def author = "Nuno Luís, Bruno Félix"
  def authorEmail = "nuno.lopes.luis@gmail.com, felix19350@gmail.com"
  def title = "Bug reporting plugin"
  def description = '''\\
The grails bug report plugin enables the user to report problems and the automatic report of grails exceptions.
This reports are saved and can be sent to a remote server or assembla ticket system.
'''

  // URL to the plugin's documentation
  def documentation = "http://github.com/nll/grails-bug-report-plugin"

  def doWithWebDescriptor = { xml ->
    // TODO Implement additions to web.xml (optional), this event occurs before
  }

  def doWithSpring = {
    // TODO Implement runtime spring config (optional)
  }

  def doWithDynamicMethods = { ctx ->
    // TODO Implement registering dynamic methods to classes (optional)
  }

  def doWithApplicationContext = { applicationContext ->
    // TODO Implement post initialization spring config (optional)

    def config = ConfigUtil.getConfig()

    if(!config.enable) {
      config.enable = true
    }

    if(!config.reporter) {
      config.reporter = BugReportUtil.logReport()
    }

    if(!config.reportInterval) {
      config.reportInterval = 600000
    }

    println config.reportInterval

    UpdateBugInformationJob.schedule(config.reportInterval)
  }

  /*
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
  */

  def onChange = { event ->
    // TODO Implement code that is executed when any artefact that this plugin is
    // watching is modified and reloaded. The event contains: event.source,
    // event.application, event.manager, event.ctx, and event.plugin.
  }

  def onConfigChange = { event ->
    // TODO Implement code that is executed when the project configuration changes.
    // The event is the same as for 'onChange'.
  }

}
