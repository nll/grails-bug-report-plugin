package pt.whiteroad.plugins.bugreport

import groovy.xml.StreamingMarkupBuilder
import grails.util.Environment
import org.springframework.web.context.request.RequestContextHolder

class Bug {

  String controller
  String action
  String summary
  String description
  Date timestamp
  BugType type
  Boolean sent = false
  String remoteAddress
  String remoteAgent

  static mapping = {
    summary type:'text'
    description type:'text'
  }

  static constraints = {
    action(nullable:true)
    type(nullable:true)
    summary(blank:false)
    remoteAgent(nullable:true)
    remoteAddress(nullable:true)
  }

  def beforeInsert = {
    try {
      def request = RequestContextHolder.requestAttributes.request
      remoteAddress = request.getRemoteAddr().toString()
      remoteAgent = request.getHeader('User-Agent')
    } catch (Exception e) {
      log.error e.message
    }
  }

  def toUrlEncoded() {
    String data = URLEncoder.encode("controller", "UTF-8") + "=" + URLEncoder.encode("${controller}", "UTF-8");
    data += "&" + URLEncoder.encode("action", "UTF-8") + "=" + URLEncoder.encode("${action}", "UTF-8");
    data += "&" + URLEncoder.encode("summary", "UTF-8") + "=" + URLEncoder.encode("${summary}", "UTF-8");
    data += "&" + URLEncoder.encode("description", "UTF-8") + "=" + URLEncoder.encode("${description}", "UTF-8");
    data += "&" + URLEncoder.encode("timestamp", "UTF-8") + "=" + URLEncoder.encode("${timestamp.getTime()}", "UTF-8");
    return data
  }


  def toAssemblaTicket() {
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
        summary(this.summary)
        description(
                """Host: ${hostname}
                   RemoteAddress: ${this.remoteAddress}
                   RemoteAgent: ${this.remoteAgent}
                   Controller: ${this.controller}
                   Action: ${this.action}
                   Environment: ${Environment.current.name}
                   ---
                   ${this.description}"""
         )
      }
    }
    return doc.toString()
  }


  def sendToAssembla(String url, String username, String password) {
    def content = this.toAssemblaTicket()
    def response = RestUtil.postXML(url, username, password, content)
    if(response.status == 201) {
      this.sent = true
      this.save()
    }
  }

  def sendToPrivateRepository(String url, String username, String password) {
    def content = this.toUrlEncoded()
    def response = RestUtil.postURLENC(url, username, password, content)
    if(response.status == 200) {
      this.sent = true
      this.save()
    }
  }
}
