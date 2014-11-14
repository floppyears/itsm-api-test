import static groovy.json.JsonOutput.*
@Grab(group='org.codehaus.groovy.modules.http-builder', module='http-builder', version='0.7' )
import groovyx.net.http.*

import java.text.MessageFormat

import static groovyx.net.http.ContentType.*
import static groovyx.net.http.Method.*


def config = getConfig()
def pathMappings = [
    "accounts": [
        "get":              "/accounts"
    ],
    "tickets": [
        "get":              "/tickets/{0}",
        "getContacts":      "/tickets/{0}/contacts",
        "getFeeds":         "/tickets/{0}/feed"
    ],
    "projects": [
        "get":              "/projects/{0}",
    ]

]


def apiPath = MessageFormat.format(getPath(pathMappings, "tickets", "get"), "157021")
apiCall(config.itsm.url, apiPath, config)

apiPath = MessageFormat.format(getPath(pathMappings, "tickets", "getContacts"), "157021")
apiCall(config.itsm.url, apiPath, config)

apiPath = MessageFormat.format(getPath(pathMappings, "tickets", "getFeeds"), "157021")
apiCall(config.itsm.url, apiPath, config)

apiPath = MessageFormat.format(getPath(pathMappings, "accounts", "get"))
apiCall(config.itsm.url, apiPath, config)

/**
 * Performs api calls to ITSM tool. Only supports GET requests
 *
 * @param url
 * @param apiPath
 * @param config
 */
private void apiCall(url, apiPath, config) {
    if (debug(config)) {
        println "url: ${url}"
        println "apiPath: ${apiPath}"
        println "token: ${config.itsm.authToken}"
    }

    def http = new HTTPBuilder(url + apiPath)
    def response = http.request(GET, JSON) { req ->
        headers.'Authorization' = "Bearer ${config.itsm.authToken}"

        response.success = { resp, json ->
            println prettyPrint(toJson(json))

            if (debug(config)) {
                println resp
                println resp.statusLine
                println json
                println "-" * 50
            }
        }
    }
}

/**
 * Reads config file and parses it
 *
 * @return
 */
private ConfigObject getConfig() {
    new ConfigSlurper().parse(new File("../itsm-config.groovy").toURL())
}

/**
 * Returns the path to use for the api operation
 *
 * @param pathMappings
 * @param section
 * @param action
 * @return
 */
String getPath(pathMappings, section, action) {
    pathMappings."${section}"."${action}"
}

/**
 * Whether or not debug mode is enabled.
 *
 * @param config
 * @return
 */
def debug(config) {
    config.debug
}