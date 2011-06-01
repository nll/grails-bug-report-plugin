package pt.whiteroad.plugins.bugreport

import grails.util.Environment

class BugService {

  static transactional = true


  def reportRequestBug(request, exception = null) {
    def baseUri = request.scheme + "://" + request.serverName + ":" + request.serverPort + request.getContextPath()
    def summary = "Exception - " + Environment.current.name
    def action = request.'javax.servlet.error.request_uri'
    def controller = ""
    def description = ""

    description += "Method: " + request.method  + "\n"
    if(request.parameterMap?.size() > 0) {
      description += "Parameter Map: \n" + request.parameterMap?.collect{ k, v -> "${k} = ${v}"}?.join("\n") ?: ""
    }


    if(exception) {
      controller = exception.className
      description += "Stack Trace: \n"  + exception.stackTraceLines.join("\n")

    } else {
      controller = request.'javax.servlet.error.servlet_name'
      description += request.'javax.servlet.error.message'

    }
    reportBug(summary, action, controller, description)
  }

  def reportBug(summary, action, controller, description){
    try {
      def bug = new Bug(summary: summary, action: action, controller: controller, description: description, timestamp: new Date(), type: BugType.GENERATED)
      if(!bug.save()) {
        log.error "Failed to save bug"
        bug.errors.each { log.error it }
      }
    } catch(Exception e) {
      log.error "Exception saving bug"
      e.printStackTrace()
    }
  }

  def sendBugsToAssembla(bugs, String url, String username, String password) {
    bugs.each { Bug bug ->
      try {
        bug.sendToAssembla(url, username, password)
        log.debug "Sent bug (${bug.id}) to assembla"
      } catch (Exception e) {
        log.error "Fail sending bug (${bug.id}) to assembla"
        e.printStackTrace()
      }
    }
  }

  def sendBugsToPrivateRepository(bugs, String url, String username, String password) {
    bugs.each { Bug bug ->
      try {
        bug.sendToPrivateRepository(url, username, password)
        log.debug "Sent bug (${bug.id}) to private repository"

      } catch (Exception e) {
        log.error "Fail sending bug (${bug.id}) to private repository"
        e.printStackTrace()
      }
    }
  }

}
