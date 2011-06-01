package pt.whiteroad.plugins.bugreport

import groovyx.net.http.RESTClient
import org.apache.commons.codec.binary.Base64
import static groovyx.net.http.ContentType.XML
import static groovyx.net.http.ContentType.URLENC

class RestUtil {

  def static postXML(String url, String username, String password, String content) {
    def client = new RESTClient(url)
    byte[] authBytes = "${username}:${password}".getBytes('iso-8859-1')
    def authHashBytes = (new Base64(0, null)).encode(authBytes)
    def authHash = new String(authHashBytes, 'iso-8859-1')
    def response =
    client.post(
            contentType: XML,
            headers:[
                    Authorization:"Basic $authHash",
                    accept: XML
                    ],
            body: content)
    return response
  }

    def static postURLENC(String url, String username, String password, String content) {
    def client = new RESTClient(url)
    byte[] authBytes = "${username}:${password}".getBytes('iso-8859-1')
    def authHashBytes = (new Base64()).encode(authBytes)
    def authHash = new String(authHashBytes, 'iso-8859-1')
    def response =
    client.post(
            contentType: URLENC,
            headers:[
                    Authorization:"Basic $authHash"
                    ],
            body: content)
    return response
  }


}
