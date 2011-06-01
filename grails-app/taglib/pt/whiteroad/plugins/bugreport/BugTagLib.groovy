package pt.whiteroad.plugins.bugreport


class BugTagLib {

  def bugService

  static namespace = 'wrs'

  def reportBugError = { attrs, body ->
    bugService.reportRequestBug(attrs.request, attrs.exception)
  }

  def reportBug= { attrs, body ->
    bugService.reportBug(attrs.summary, attrs.action, attrs.controller, attrs.description)
  }

  def reportJavascript = { attrs, body ->
    out << render(template:"/bug/script", plugin:"bug-report")    
  }

  def reportButton = { attrs, body ->
    def action = attrs.action ?: "N/A"
    def controller = attrs.controller ?: "N/A"
    def id = attrs.id ?: "reportButton"
    def insertScript = booleanAttrValue(attrs, 'insertScript', false)

    def style = attrs.style ? attrs.style : ""

    if(insertScript) {
      out << render(template:"/bug/script", plugin:"bug-report")
    }

    out << "<div id='${id}' style='${style}' ><p><img style='vertical-align:middle; margin-right: 6px;' src='${g.resource(dir:'images', file:'bug-buddy.png', plugin:'bug-report')}' width='20' height='20'/>${g.message(code:'default.plugin.bugreport.button.title')}</p></div>"
    out << "<script type='text/javascript'>"
    out << "jQuery(function(){jQuery('#${id}').button().click("
    out << "function(){ reportErrorDialog('${g.createLink(controller:'bug', action:'reportBug')}', '${action}', '${controller}'); }"
    out << ");});"
    out << "</script>"
  }

  private boolean booleanAttrValue(Map attrs,
                                   String attrName,
                                   boolean defaultValue = true) {
    def val = defaultValue
    if (attrs.containsKey(attrName)) {
      val = Boolean.valueOf(attrs.remove(attrName))
    }
    val
  }

}
