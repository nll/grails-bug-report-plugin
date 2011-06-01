package pt.whiteroad.plugins.bugreport

class BugController {

   /**
   * Adds a new bug and sends it to the central database somewhere (preferably assembla)
   * */
  def reportBug = {
    log.debug("Adding bug: " + params)

    try {
      def bug = new Bug(controller: params.srcController, action: params.srcAction, summary: params.summary, description: params.description, timestamp: new Date(), type: BugType.REPORTED)
      if (!bug.save()) {
        bug.errors.each {log.error it}
        throw new RuntimeException()
      }
      render(status: 200, text: "ok")
    } catch (Exception e) {
      e.printStackTrace()
      render(status: 503, text: "Erro a adicionar o bug")
    }
  }
}
