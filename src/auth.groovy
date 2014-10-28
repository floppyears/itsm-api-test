@Grab(group='org.codehaus.groovy.modules.http-builder', module='http-builder', version='0.7' )
import groovyx.net.http.*
import static groovyx.net.http.ContentType.*
import static groovyx.net.http.Method.*


def config = getConfig()
println "url: ${config.itsm.url}"
def http = new HTTPBuilder(config.itsm.url)
http.get( path: '/api' ) { resp, reader ->
    println "response status: ${resp.statusLine}"
    println "Headers"
    resp.headers.each { h ->
        println " ${h.name} : ${h.value}"
    }
    println "Response data:"
    System.out << reader
}



private ConfigObject getConfig() {
    new ConfigSlurper().parse(new File("../itsm-config.groovy").toURL())
}