<g:javascript>

function BugReportPlugin() {}

BugReportPlugin.alertMsgBox = function(msg, title) {
    title = (title !== undefined ? title : "${message(code:'default.plugin.bugreport.alert.title')}");
    var alertDiv = jQuery("<div><\/div>");
    alertDiv.html(msg);
    alertDiv.dialog({
        modal: true,
        title: title,
        buttons: {
            Ok: function() {
                jQuery(this).dialog('close');
                jQuery(this).remove();
            }
        }
    });
}


BugReportPlugin.reportErrorDialog = function(url, srcAction, srcController) {
    var dialogDiv = jQuery("<div title='${message(code:'default.plugin.bugreport.dialog.title')}'>\
                <div style='text-align:left;'>\
                  <p class='bold'>${message(code:'default.plugin.bugreport.dialog.summary')}:<\/p><p><input style='width:100%' type='tex' id='bugSummary'\/><\/p>\
                  <p class='bold'>${message(code:'default.plugin.bugreport.dialog.description')}:<\/p><p><textarea style='width:100%' rows='5' id='bugDescription'><\/textarea><\/p>\
                <\/div>\
              <\/div>");

    dialogDiv.dialog({
        autoOpen: true,
        modal: true,
        buttons:{
            "${message(code:'default.plugin.bugreport.dialog.send')}":function() {
                var summary = jQuery("#bugSummary").val();
                var description = jQuery("#bugDescription").val();
                jQuery(this).before("<p align='center'>${message(code:'default.plugin.bugreport.dialog.sending')}<\/p>");
                jQuery(this).dialog( "option", "disabled", true );
                jQuery(this).dialog( "option", "buttons", {} );
                jQuery.ajax({
                    type:"post",
                    url: url,
                    data:{"summary":summary, "description":description, "srcAction":srcAction, "srcController":srcController},
                    error:function(){
                        BugReportPlugin.alertMsgBox("${message(code:'default.plugin.bugreport.dialog.error')}");
                    },
                    success:function(data, textStatus, XMLHttpRequest) {
                        BugReportPlugin.alertMsgBox("${message(code:'default.plugin.bugreport.dialog.success')}");
                    },
                    complete:function(XMLHttpRequest, textStatus) {
                        dialogDiv.dialog('close');
                        dialogDiv.remove();
                    }
                });

            }, "${message(code:'default.plugin.bugreport.dialog.cancel')}": function() {
                dialogDiv.dialog('close');
                dialogDiv.remove();
            }}
    });
}
</g:javascript>