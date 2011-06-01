package pt.whiteroad.plugins.bugreport

import groovy.xml.StreamingMarkupBuilder
import grails.util.Environment
//import org.apache.commons.logging.LogFactory

class BugReportUtil {

  //private static def log = LogFactory.getLog(this);      

  private static def bugToAssemblaTicket(Bug bug) {
    def hostname = InetAddress.localHost.canonicalHostName
    def mb = new StreamingMarkupBuilder()
    mb.encoding = "UTF-8"
    //def milestoneId = type && type == BugType.REPORTED ? 347127 : 325319
    def doc = mb.bind {
      ticket {
        //number(type: "integer") {tickerNumber}
        priority(type: "integer", "3")
        status(type: "integer", "0")
        //'milestone-id'(type: "integer", milestoneId)
        //reporter - id(reporterId)
        //created_on(type: "datetime", this.timestamp.toString())
        summary(bug.summary)
        description(
                """Host: ${hostname}
                   RemoteAddress: ${bug.remoteAddress}
                   RemoteAgent: ${bug.remoteAgent}
                   Controller: ${bug.controller}
                   Action: ${bug.action}
                   Environment: ${Environment.current.name}
                   ---
                   ${bug.description}"""
        )
      }
    }
    return doc.toString()
  }


  private static def sendBugToAssembla(Bug bug , String url, String username, String password) {
    def content = bugToAssemblaTicket(bug)
    def response = RestUtil.postXML(url, username, password, content)
    if(response.status == 201) {
      return true
    }
    return false
  }

  public static def assemblaReport(String url, String username, String password) {
    return {Bug bug ->
      sendBugToAssembla(bug, url, username, password)
    }
  }

  public static def logReport() {
    return { Bug bug, log ->
      log.debug "We have a bug"
      return true
    }
  }


}
