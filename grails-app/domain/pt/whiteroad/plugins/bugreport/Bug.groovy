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
}
