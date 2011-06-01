import pt.whiteroad.plugins.bugreport.ConfigUtil
import pt.whiteroad.plugins.bugreport.Bug

class UpdateBugInformationJob {
  static triggers = {
    //simple name: 'updateBugInformationJob', startDelay: 10000 , repeatInterval: 10000  //Runs every 10 minutes
  }

  def bugService

  def synchronized execute() {

    log.debug "Start sending bug information"
	

    def config = ConfigUtil.getConfig()
	
	if(config.enable) {
	    List<Bug> bugs = Bug.findAllBySent(false, [sort:"id", order:"asc"])
	
	    if(config.reporter) {
          config.reporter.properties.putAt("log",this.log)
          bugs.each { Bug bug ->
            try {
              if(config.reporter.call(bug, this.log)) {
                bug.sent = true
                bug.save()
              } else {
                log.debug "Bug not sent"
              }
            } catch(Exception e) {
              log.error "Bug not sent cause by ${e.message}"
            }
          }
        }
	}
	

    log.debug "Stop sending bug information"
  }

}


